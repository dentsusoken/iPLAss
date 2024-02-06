/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.service.auditlog;

import java.util.List;

import org.iplass.mtp.entity.EntityKey;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.impl.entity.auditlog.LoggerAuditLoggingService;
import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>AdminConsoleの操作ログService</p>
 *
 * <p>Entityデータの操作ログについては、{@link LoggerAuditLoggingService} を利用して、
 * 出力ログのカテゴリ名だけを変更。load時にもログを出力する。</p>
 *
 * <p>ダウンロード操作については独自に処理を行う。</p>
 */
public class AdminAuditLoggingService extends LoggerAuditLoggingService {

	//FIXME EntityExplorerでのImportによるデータ登録処理などは、toolsで実行されているので、通常のAuditLogとして出力される。
	//FIXME カテゴリとして「mtp.audit.admin.entity」とする必要があるか？

//	private static AdminAuditLoggingService instance = null;

	private static final String CATEGORY_DOWNLOAD = "mtp.audit.admin.download";
	private static final String CATEGORY_ENTITY = "mtp.audit.admin.entity";
	private static final String CATEGORY_AUDITLOG = "mtp.audit.admin.auditlog";

	private static final String ACTION_LOAD = "load";

	private static final Logger entityLogger = LoggerFactory.getLogger(CATEGORY_ENTITY);
	private static final Logger downloadLogger = LoggerFactory.getLogger(CATEGORY_DOWNLOAD);
	private static final Logger auditlogLogger = LoggerFactory.getLogger(CATEGORY_AUDITLOG);

//	private AdminAuditLoggingService() {
//	}
//
//	public static AdminAuditLoggingService getInstance() {
//		if (instance == null) {
//			instance = new AdminAuditLoggingService();
//			instance.init(null);
//		}
//		return instance;
//	}

	@Override
	public void init(Config config) {
		super.init(config);

//		AuditLogToolService alts = ServiceRegistry.getRegistry().getService(AuditLogToolService.class);
//
//		NameValue paramLogQuery = new NameValue("logQuery", String.valueOf(alts.isLogQuery()));
//		NameValue paramLogCompact = new NameValue("logCompact", String.valueOf(alts.isLogCompact()));
//		NameValue paramTextMaxLength = new NameValue("textMaxLength", String.valueOf(alts.getTextMaxLength()));
//
//		//FIXME イリーガルな使い方。。
//		Config custom = new ConfigImpl(AdminAuditLoggingService.class.getName(), new NameValue[]{paramLogQuery, paramLogCompact, paramTextMaxLength});
//		super.init(custom);
	}

	/**
	 * <p>Entityデータに対するログ出力。</p>
	 * <p>Loggerの名前を変更するためOverride。</p>
	 *
	 * @param action 操作
	 * @param detail ログ情報
	 */
	@Override
	public void log(String action, Object detail) {
		entityLogger.info(action + "," + sanitize(detail));
	}

	/**
	 * <p>Entityのload処理に対するログ出力</p>
	 * <p>load処理に対して、ログ出力を追加。</p>
	 *
	 * @param oid ID
	 * @param version バージョン
	 * @param definitionName 定義名
	 * @param option オプション
	 */
	public void logLoad(String oid, Long version, String definitionName, LoadOption option) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"definitionName\":\"").append(definitionName).append("\"");
		sb.append(",\"oid\":\"").append(oid).append("\"");
		if (version != null) {
			sb.append(",\"version\":\"").append(version).append("\"");
		}
		if (option != null) {
			sb.append(",\"option\":{").append(option).append("}");
		}
		sb.append("}");

		log(ACTION_LOAD, sb);
	}

	/**
	 * <p>Entityのバッチload処理に対するログ出力</p>
	 * <p>バッチload処理に対して、ログ出力を追加。</p>
	 *
	 * @param keys EntityKey情報
	 * @param definitionName 定義名
	 * @param option オプション
	 */
	public void logBatchLoad(List<EntityKey> keys, String definitionName, LoadOption option) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"definitionName\":\"").append(definitionName).append("\"");
		sb.append(",\"keys\":[");
		keys.forEach(key -> {
			sb.append(key + ",");
		});
		sb.append("]");
		if (option != null) {
			sb.append(",\"option\":{").append(option).append("}");
		}
		sb.append("}");

		log(ACTION_LOAD, sb);
	}

	/**
	 * Downloadに対するログ出力
	 *
	 * @param type ダウンロードの種別
	 * @param fileName ファイル名
	 * @param detail ログ情報
	 */
	public void logDownload(String type, String fileName, Object detail) {
		downloadLogger.info(type + "," + fileName + "," + sanitize(detail));
	}

	/**
	 * AuditLog操作に対するログ出力
	 *
	 * @param type 種別
	 * @param detail ログ情報
	 */
	public void logAuditLog(String type, String detail) {
		auditlogLogger.info(type + ",," + sanitize(detail));
	}

	/**
	 * 制御文字を取り除く
	 *
	 * @param detail ログ
	 * @return 編集後ログ
	 */
	private String sanitize(Object detail) {
		if (detail != null) {
			return detail.toString().replace("\n", "\\n").replace("\r", "\\r");
		}
		return "";
	}

}
