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
	/** テナント毎のウォームアップタスク名*/
	private Map<Integer, List<String>> tenantTaskNameListMap;
	/** ウォームアップ状態 */
	private WarmupStatus status = WarmupStatus.NOT_PROCESSING;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		// taskMap key = タスク名、 value = WarmupTask 実装インスタンス
		Map<String, WarmupTask> taskMap = config.getValue("taskMap", Map.class, Collections.emptyMap());
		// tenantTaskMap key = カンマ区切りのテナントID、 value = カンマ区切りのタスク名
		Map<String, String> tenantTaskMap = config.getValue("tenantTaskMap", Map.class, Collections.emptyMap());

		this.enabled = config.getValue("enabled", Boolean.class, Boolean.FALSE);
		this.statusFile = config.getValue("statusFile", null);

		// タスク初期化
		taskMap.values().forEach(WarmupTask::init);

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
		for (var status : WarmupStatus.values()) {
			if (status.isFinalStatus()) {
				deleteFile(getStatusFile(status));
			}
		}

		this.taskMap = taskMap;
		this.tenantTaskNameListMap = tenantTaskNameListMap;
	}

	@Override
	public void destroy() {
		try {
			// タスク破棄
			taskMap.values().forEach(WarmupTask::destroy);

		} finally {
			// ステータスファイルを削除
			deleteFile(getStatusFile());
			for (var status : WarmupStatus.values()) {
				if (status.isFinalStatus()) {
					deleteFile(getStatusFile(status));
				}
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
	 * ウォームアップ状態が COMPLETE, FAILED, DISABLED に変更された場合、ステータスファイルを出力します。
	 * </p>
	 *
	 * @param nextStatus 変更する状態
	 */
	public void changeStatus(WarmupStatus nextStatus) {
		if (this.status.canChange(nextStatus)) {
			logger.debug("Warmup status changed from {} to {}.", this.status, nextStatus);
			this.status = nextStatus;

			if (nextStatus.isFinalStatus()) {
				// 最終ステータスの場合、ファイルを作成
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
	 * ウォームアップ処理を実行する。
	 * <p>
	 * テナント毎に定義されているウォームアップ処理を実行します。
	 * </p>
	 * <p>
	 * enabled が false の場合は、ウォームアップ処理を実行しません。
	 * </p>
	 */
	public void warmup() {
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
			logger.debug("Start the warmup task \"{}\".", taskName);
			var task = taskMap.get(taskName);
			task.warmup();
		}
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

			// /path/to/file.(complete|failed|disabled) を作成
			getStatusFile(status).createNewFile();

		} catch (Exception e) {
			logger.error("Failed to create status file.", e);
		}
	}
}
