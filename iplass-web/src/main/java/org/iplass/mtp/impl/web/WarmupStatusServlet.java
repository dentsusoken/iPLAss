/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.iplass.mtp.impl.web;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iplass.mtp.impl.async.AsyncTaskService;
import org.iplass.mtp.impl.async.internal.InternalAsyncTaskServiceConstant;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.warmup.WarmupService;
import org.iplass.mtp.impl.warmup.WarmupStatus;
import org.iplass.mtp.runtime.EntryPoint;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ウォームアップ状態サーブレット
 * <p>
 * ウォームアップ状態を取得するためのサーブレットです。
 * ウォームアップ処理は、本サーブレットの初期化処理で開始されます。
 * ウォームアップ処理・状態は {@link org.iplass.mtp.impl.warmup.WarmupService} によって管理されます。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class WarmupStatusServlet extends HttpServlet {
	/** serialVersionUID */
	private static final long serialVersionUID = -3645969923289588830L;
	/** デフォルト：ウォームアップ処理待ち時間（秒） */
	private static final long DEFAULT_WAIT_ON_WARMUP = 1;

	@Override
	public void init() throws ServletException {
		ServletConfig config = super.getServletConfig();
		String configWaitOnWarmup =  config.getInitParameter("waitOnWarmup");
		long waitOnWarmup = StringUtil.isNotEmpty(configWaitOnWarmup) ? Long.valueOf(configWaitOnWarmup) : DEFAULT_WAIT_ON_WARMUP;

		// 非同期でウォームアップする
		AsyncTaskService asyncTaskService = ServiceRegistry.getRegistry().getService(InternalAsyncTaskServiceConstant.SERVICE_NAME);
		var warmupTask = new WarmupTaskExecutor(waitOnWarmup);
		asyncTaskService.execute(warmupTask);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var warmupService = ServiceRegistry.getRegistry().getService(WarmupService.class);
		var status = warmupService.getStatus();

		int httpStatus = getHttpStatus(status);
		response.getWriter().write(status.getStatus());
		response.setStatus(httpStatus);
	}

	/**
	 * ウォームアップ状態に対応するHTTPステータスを取得します。
	 * @param status ウォームアップ状態
	 * @return HTTPステータス
	 */
	private int getHttpStatus(WarmupStatus status) {
		switch (status) {
		case NOT_PROCESSING:
			return HttpServletResponse.SC_SERVICE_UNAVAILABLE;
		case PROCESSING:
			return HttpServletResponse.SC_SERVICE_UNAVAILABLE;
		case COMPLETE:
			return HttpServletResponse.SC_OK;
		case FAILED:
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		default:
			// null を想定
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	/**
	 * ウォームアップタスク実行
	 * <p>
	 * サーブレットの初期化処理で非同期実行されるウォームアップ処理のエントリポイントです。
	 * </p>
	 */
	private static class WarmupTaskExecutor implements Callable<Void> {
		/** ロガー */
		private Logger logger = LoggerFactory.getLogger(WarmupTaskExecutor.class);
		/** ウォームアップ処理待ち時間（秒） */
		private long waitOnWarmup;

		/**
		 * コンストラクタ
		 * @param waitOnWarmup ウォームアップ処理待ち時間（秒）
		 */
		public WarmupTaskExecutor(long waitOnWarmup) {
			this.waitOnWarmup = waitOnWarmup;
		}

		@Override
		public Void call() throws Exception {
			try {
				// ウォームアップ処理待ち
				sleep(waitOnWarmup);

				logger.info("Start the warmup.");

				// ウォームアップ処理開始
				long elapsedTime = timer(() -> startWarmup());

				logger.info("End the warmup. elapsed time is {} ms.", elapsedTime);

				return null;

			} catch (Exception e) {
				logger.error("Warmup failed.", e);
				throw e;
			}
		}

		/**
		 * ウォームアップ処理を開始する
		 * <p>
		 * 全テナントに対してウォームアップ処理を実行します。
		 * ウォームアップ処理はテナント単位で、順次実行します。
		 * </p>
		 */
		private void startWarmup() {
			var tenantService = ServiceRegistry.getRegistry().getService(TenantService.class);
			var warmupService = ServiceRegistry.getRegistry().getService(WarmupService.class);
			warmupService.changeStatus(WarmupStatus.PROCESSING);

			boolean isError = false;
			var tenantIdList = tenantService.getAllTenantIdList();
			for (int tenantId : tenantIdList) {
				// テナント毎にウォームアップ処理を実行

				if (warmupService.notExistsTenantWarmup(tenantId)) {
					// ウォームアップ処理が存在しなければ、テナントをスキップ
					logger.debug("No warmup task is configured for tenant {}.", tenantId);
					continue;
				}

				try {
					logger.debug("Tenant {} warmup start.", tenantId);

					EntryPoint.getInstance().withTenant(tenantId).run(() -> {
						Transaction.readOnly(t -> {
							warmupService.warmup();
						});
					});

					logger.debug("Tenant {} warmup finish.", tenantId);

				} catch (RuntimeException e) {
					logger.error("Tenant {} warmup failed.", tenantId, e);
					isError = true;
				}
			}

			warmupService.changeStatus(isError ? WarmupStatus.FAILED : WarmupStatus.COMPLETE);
		}

		/**
		 * 指定秒数スリープします。
		 * @param seconds スリープ秒数
		 * @throws InterruptedException
		 */
		private void sleep(long seconds) throws InterruptedException {
			if (seconds > 0) {
				Thread.sleep(Duration.ofSeconds(seconds));
			}
		}

		/**
		 * 指定処理の実行時間を計測します。
		 * @param fn 処理
		 * @return 実行時間（ミリ秒）
		 */
		private long timer(Runnable fn) {
			long startTime = System.currentTimeMillis();

			fn.run();

			long endTime = System.currentTimeMillis();
			return endTime - startTime;
		}
	}
}
