/*
 * Copyright 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.cleaner;

import java.util.List;

import org.iplass.mtp.impl.async.rdb.RdbQueueService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;

/**
 * 非同期機能のメンテナンス用バッチ
 * Status.RETURNED(タスク実行完了し、結果が取得されるのを待っている。)のままのタスクの履歴への移動
 * 古い履歴の削除
 */
public class AsyncTaskCleaner extends MtpCuiBase {

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private static RdbQueueService rdbQueueService = ServiceRegistry.getRegistry().getService(RdbQueueService.class);

	private int tenantId;

	/**
	 * <p>引数について</p>
	 * <ol>
	 * <li>テナントID：対象テナントID（-1の場合、全テナントが対象になります）</li>
	 * </ol>
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		int tenantId = -1;
		if (args != null && args.length > 0 && StringUtil.isNotEmpty(args[0])) {
			tenantId = Integer.parseInt(args[0]);
		}
		if (tenantId >= 0) {
			(new AsyncTaskCleaner(tenantId)).clean();
		} else {
			//全テナントを対象
			List<TenantInfo> tenants = getValidTenantInfoList();
			if (tenants != null) {
				for (TenantInfo t: tenants) {
					(new AsyncTaskCleaner(t.getId())).clean();
				}
			}
		}
	}

	/**
	 * 対象のテナントIDを指定します。
	 *
	 * @param tenantId 対象のテナントID
	 */
	public AsyncTaskCleaner(int tenantId) {
		setTenantId(tenantId);

		LogListner loggingListner = getLoggingLogListner();
		addLogListner(loggingListner);
	}

	/**
	 * タスクを履歴へ移動、古い履歴は削除する。
	 *
	 * @return boolean 成功：true 失敗：false
	 * @throws Exception
	 */
	public boolean clean() throws Exception {

		setSuccess(false);

		clearLog();

		try {
			ExecuteContext.initContext(new ExecuteContext(tenantContextService.getTenantContext(tenantId)));

			logArguments();

			Transaction.required(t -> {

					/*
					 * QueueConfigのresultRemainingTime設定及びStatus.RETURNED(タスク実行完了し、
					 * 結果が取得されるのを待っている。)のままのタスクの履歴への移動
					 */
					rdbQueueService.moveNoGetResultTaskToHistory();

					/*
					 * RdbQueueServiceのhistoryHoldDay設定に従い古い履歴データを削除
					 */
					rdbQueueService.deleteHistoryByDate(null, true);

					return null;
			});

			setSuccess(true);
		} catch (Throwable e) {
			logError("An error has occurred. : " + e.getMessage());
			e.printStackTrace();
		} finally {
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));

			ExecuteContext.initContext(null);
		}
		return isSuccess();

	}

	/**
	 * パラメータ値ログ出力
	 *
	 */
	private void logArguments() {
		logInfo("■Execute Argument");
		logInfo("\ttenant id :" + getTenantId());
		logInfo("");
	}


	/**
	 * tenantIdを取得します。
	 * @return tenantId
	 */
	public int getTenantId() {
	    return tenantId;
	}

	/**
	 * tenantIdを設定します。
	 * @param tenantId tenantId
	 */
	public void setTenantId(int tenantId) {
	    this.tenantId = tenantId;
	}
}
