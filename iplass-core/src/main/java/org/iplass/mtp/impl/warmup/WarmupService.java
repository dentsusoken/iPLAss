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
package org.iplass.mtp.impl.warmup;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ウォームアップサービス
 * <p>
 * アプリケーション起動時にウォームアップ処理を行うサービスです。
 * 比較的重い初期処理を事前に実行しておくことにより、初回実行時の処理速度の改善を目的とします。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class WarmupService implements Service {
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(WarmupService.class);

	// service-config 設定値
	private Boolean enabled;
	/** ステータスファイルパス */
	private String statusFile;

	/** ウォームアップタスク */
	private Map<String, WarmupTask> taskMap;
	/** アプリケーションのウォームアップタスク名 */
	private List<String> applicationTaskNameList;
	/** テナント毎のウォームアップタスク名*/
	private Map<Integer, List<String>> tenantTaskNameListMap;
	/** ウォームアップ状態 */
	private volatile WarmupStatus status = WarmupStatus.NOT_PROCESSING;

	/** 非同期タスク実行 */
	private InternalSingleThreadAsyncTaskExecutor asyncTaskExecutor;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		this.enabled = config.getValue("enabled", Boolean.class, Boolean.FALSE);

		if (!enabled) {
			logger.debug("warmup is disabled.");
			return;
		}

		this.statusFile = config.getValue("statusFile", null);

		// taskMap key = タスク名、 value = WarmupTask 実装インスタンス
		Map<String, WarmupTask> taskMap = config.getValue("taskMap", Map.class, Collections.emptyMap());
		// applicationTask = カンマ区切りのタスク名
		String applicationTask = config.getValue("applicationTask", String.class, "");
		// tenantTaskMap key = カンマ区切りのテナントID、 value = カンマ区切りのタスク名
		Map<String, String> tenantTaskMap = config.getValue("tenantTaskMap", Map.class, Collections.emptyMap());

		// タスク初期化
		taskMap.values().forEach(WarmupTask::init);

		// アプリケーションのウォームアップタスク名を設定
		// カンマ区切りのタスク名を分割
		var applicationTaskNames = Stream.of(applicationTask.split(","))
				.filter(t -> StringUtil.isNotEmpty(t))
				.map(t -> t.trim())
				.toList();

		applicationTaskNameList = new ArrayList<>();
		for (String taskName : applicationTaskNames) {
			if (!taskMap.containsKey(taskName)) {
				// tenantTaskMap に設定されたタスク名が、taskMap に存在しない場合は警告を出力しスキップ
				logger.warn("The taskMap is set to the non-existent task name \"{}\". Please check applicationTask.", taskName);
				continue;
			}

			// タスクを追加
			applicationTaskNameList.add(taskName);
		}

		// 全テナントID
		var tenantService = ServiceRegistry.getRegistry().getService(TenantService.class);
		var allTenantIdList = tenantService.getAllTenantIdList();

		Map<Integer, List<String>> tenantTaskNameListMap = new HashMap<>();
		for (Map.Entry<String, String> entry : tenantTaskMap.entrySet()) {
			String commaSeparatedTenantId = entry.getKey();
			String commaSeparatedTaskName = entry.getValue();

			// カンマ区切りのテナントIDを分割。テナントIDは * が指定されていたら、全テナントと判断する。空文字は無視する。
			List<Integer> tenantIdList = "*".equals(commaSeparatedTenantId) ? allTenantIdList
					: Stream.of(commaSeparatedTenantId.split(","))
					.filter(i -> StringUtil.isNotEmpty(i))
					.map(i -> Integer.parseInt(i.trim()))
					.toList();

			// カンマ区切りのタスク名を分割
			var taskNames = Stream.of(commaSeparatedTaskName.split(","))
					.filter(t -> StringUtil.isNotEmpty(t))
					.map(t -> t.trim())
					.toList();

			for (String taskName : taskNames) {
				if (!taskMap.containsKey(taskName)) {
					// tenantTaskMap に設定されたタスク名が、taskMap に存在しない場合は警告を出力しスキップ
					logger.warn("The taskMap is set to the non-existent task name \"{}\". Please check tenantTaskMap.", taskName);
					continue;
				}

				// テナント毎にタスクを追加
				addTaskNameEachTenant(tenantTaskNameListMap, taskName, tenantIdList);
			}
		}

		// ステータスファイルの上位ディレクトリを作成
		var statusFile = getStatusFile();
		createParentDirectory(statusFile);

		// ステータスファイルが存在していたら削除
		deleteFile(statusFile);
		Stream.of(WarmupStatus.values()).forEach(s -> deleteFile(getStatusFile(s)));

		this.taskMap = taskMap;
		this.tenantTaskNameListMap = tenantTaskNameListMap;

		// 非同期タスク処理
		asyncTaskExecutor = new InternalSingleThreadAsyncTaskExecutor();
		// インスタンス初期化
		asyncTaskExecutor.init(config);
	}

	@Override
	public void destroy() {
		try {
			if (null != taskMap) {
				// タスク破棄
				taskMap.values().forEach(WarmupTask::destroy);
			}

		} finally {
			// ステータスファイルを削除
			deleteFile(getStatusFile());
			Stream.of(WarmupStatus.values()).forEach(s -> deleteFile(getStatusFile(s)));
			// インスタンス破棄
			if (null != asyncTaskExecutor) {
				asyncTaskExecutor.destroy();
			}
		}
	}

	/**
	 * ウォームアップが有効かを取得する。
	 * @return ウォームアップが有効な場合 true を返却する。
	 */
	public boolean isEnabled() {
		return enabled == Boolean.TRUE;
	}

	/**
	 * ウォームアップ状態を取得する。
	 * @return ウォームアップ状態
	 */
	public WarmupStatus getStatus() {
		return status;
	}

	/**
	 * ウォームアップ状態を変更する。
	 * <p>
	 * ウォームアップ状態が変更可能であれば変更します。
	 * 詳細は {@link org.iplass.mtp.impl.warmup.WarmupStatus} を確認してください。
	 * </p>
	 * <p>
	 * ウォームアップ状態が COMPLETE, FAILED に変更された場合、ステータスファイルを出力します。
	 * </p>
	 *
	 * @param nextStatus 変更する状態
	 */
	public synchronized void changeStatus(WarmupStatus nextStatus) {
		if (this.status.canChange(nextStatus)) {
			logger.debug("Warmup status changed from {} to {}.", this.status, nextStatus);
			this.status = nextStatus;

			if (WarmupStatus.COMPLETE == nextStatus || WarmupStatus.FAILED == nextStatus) {
				// 最終ステータスの場合、ファイルを作成（DISABLED は作らない）
				createStatusFile(nextStatus);
			}
		} else {
			logger.warn("Cannot change warmup status from {} to {}.", this.status, nextStatus);
		}
	}

	/**
	 * 指定されたテナントIDのウォームアップ処理が存在しないことを確認する。
	 * @param tenantId テナントID
	 * @return ウォームアップ処理が存在しない場合 true を返却する。
	 */
	public boolean notExistsTenantWarmup(Integer tenantId) {
		return !tenantTaskNameListMap.containsKey(tenantId);
	}

	/**
	 * アプリケーションのウォームアップ処理を実行する。
	 * <p>
	 * アプリケーションに定義されているウォームアップ処理を実行します。
	 * </p>
	 * <p>
	 * enabled が false の場合は、ウォームアップ処理を実行しません。
	 * </p>
	 * @param context ウォームアップコンテキスト
	 */
	public void warmupApplication(WarmupContext context) {
		if (!isEnabled()) {
			logger.debug("Warmup is disabled.");
			return;
		}

		for (String taskName : applicationTaskNameList) {
			logger.debug("Start the application warmup task \"{}\".", taskName);
			var task = taskMap.get(taskName);
			task.warmup(context);
		}
	}

	/**
	 * テナントのウォームアップ処理を実行する。
	 * <p>
	 * テナント毎に定義されているウォームアップ処理を実行します。
	 * </p>
	 * <p>
	 * enabled が false の場合は、ウォームアップ処理を実行しません。
	 * </p>
	 * @param context ウォームアップコンテキスト
	 */
	public void warmupTenant(WarmupContext context) {
		if (!isEnabled()) {
			logger.debug("Warmup is disabled.");
			return;
		}

		var tenantId = ExecuteContext.getCurrentContext().getTenantContext().getTenantId();
		var taskNameList = tenantTaskNameListMap.get(tenantId);

		if (taskNameList == null) {
			return;
		}

		for (String taskName : taskNameList) {
			logger.debug("Start the tenant warmup task \"{}\".", taskName);
			var task = taskMap.get(taskName);
			task.warmup(context);
		}
	}

	/**
	 * 非同期タスクを実行する
	 * @param <V> 返却データ型
	 * @param task 非同期タスク
	 * @return 非同期タスクの実行結果
	 */
	public <V> Future<V> execute(Callable<V> task) {
		if (!isEnabled()) {
			logger.debug("Warmup is disabled.");
			return null;
		}

		// asyncTaskExecutor メソッドに移譲
		return asyncTaskExecutor.execute(task);
	}

	/**
	 * テナント毎のタスク名リストにタスク名を追加する。
	 * @param tenantTaskNameListMap テナント毎のタスク名リスト
	 * @param taskName 追加対象のタスク名
	 * @param tenantIdList 追加対象のテナントIDリスト
	 */
	private void addTaskNameEachTenant(Map<Integer, List<String>> tenantTaskNameListMap, String taskName, List<Integer> tenantIdList) {
		for (int tenantId : tenantIdList) {
			var taskNameList = tenantTaskNameListMap.get(tenantId);
			if (taskNameList == null) {
				taskNameList = new ArrayList<>();
				tenantTaskNameListMap.put(tenantId, taskNameList);
			}

			if (!taskNameList.contains(taskName)) {
				// taskNameList にタスク名が存在しなければ追加（ * で追加されるタスクと、個別指定タスクの重複を考慮 ）
				taskNameList.add(taskName);
			}
		}
	}

	/**
	 * ファイルの親ディレクトリを作成します
	 * @param file ファイル
	 */
	private void createParentDirectory(File file) {
		if (null != file) {
			var parent = file.getParentFile();
			parent.mkdirs();
		}
	}

	/**
	 * ファイルを削除します
	 * @param file ステータスファイル
	 */
	private void deleteFile(File file) {
		if (null != file && file.exists()) {
			file.delete();
		}
	}

	/**
	 * ステータスファイルを取得します。
	 * <p>
	 * 返却するファイルは service-config で設定したステータスファイルです。
	 * </p>
	 * @return ステータスファイル
	 */
	private File getStatusFile() {
		if (StringUtil.isEmpty(this.statusFile)) {
			return null;
		}
		return new File(this.statusFile);
	}

	/**
	 * 拡張子に状態が付与されたステータスファイルを取得します。
	 * <p>
	 * 返却するファイルは service-config で設定したステータスファイルに状態を付与したファイルです。
	 * 例えば、/path/to/file が設定されていた場合、 /path/to/file.complete や /path/to/file.failed などが返却されます。
	 * </p>
	 *
	 * @param status ウォームアップ状態
	 * @return ステータスファイル
	 */
	private File getStatusFile(WarmupStatus status) {
		if (StringUtil.isEmpty(this.statusFile)) {
			return null;
		}
		return new File(this.statusFile + "." + status.getStatus());
	}

	/**
	 * ステータスファイルを作成します。
	 * @param status ウォームアップ状態
	 */
	private void createStatusFile(WarmupStatus status) {
		var statusFile = getStatusFile();
		if (null == statusFile) {
			// ステータスファイルの設定がない場合は作成しない。
			return;
		}

		try (var file = new FileWriter(statusFile, StandardCharsets.UTF_8)) {
			// /path/to/file に complete|failed を書き込む
			file.write(status.getStatus());
			file.flush();

			// /path/to/file.(complete|failed) を作成
			getStatusFile(status).createNewFile();

		} catch (Exception e) {
			logger.error("Failed to create status file.", e);
		}
	}

	/**
	 * 内部利用 単一スレッド用の非同期タスク実行
	 * <p>
	 * テナントが未確定な状態で利用する非同期処理です。
	 * </p>
	 */
	private static class InternalSingleThreadAsyncTaskExecutor {
		/** ExecutorService */
		private ExecutorService executor;
		/** 実行タスクのFuture */
		private List<Future<?>> futureList = new ArrayList<>();

		/**
		 * 初期化ライフサイクルメソッド
		 * <p>
		 * 本メソッドを実行することで、インスタンスを利用することができます。
		 * </p>
		 *
		 * @param config 設定情報
		 */
		public void init(Config config) {
			executor = Executors.newSingleThreadExecutor();
		}

		/**
		 * 破棄ライフサイクルメソッド
		 * <p>
		 * アプリケーション終了時に、本メソッドを実行しリソースを開放します。
		 * </p>
		 */
		public void destroy() {
			// 未完了でキャンセルされていないタスクをキャンセル
			futureList.stream().filter(f -> !f.isDone() && !f.isCancelled()).forEach(f -> f.cancel(true));

			if (null != executor) {
				executor.close();
			}
		}

		/**
		 * 非同期タスクを実行する
		 * @param <V> 返却データ型
		 * @param task 非同期タスク
		 * @return 非同期タスクの実行結果（Future）
		 */
		public <V> Future<V> execute(Callable<V> task) {
			// 完了したタスクのFutureを削除
			removeDoneFuture();
			// 新規タスクを実行
			var future = executor.submit(task);
			futureList.add(future);
			return future;
		}

		/**
		 * 完了したタスクのFutureを削除する
		 */
		private void removeDoneFuture() {
			List<Future<?>> doneFutureList = new ArrayList<>();
			for (var future : futureList) {
				if (future.isDone()) {
					doneFutureList.add(future);
				}
			}

			if (!doneFutureList.isEmpty()) {
				futureList.removeAll(doneFutureList);
			}
		}
	}
}
