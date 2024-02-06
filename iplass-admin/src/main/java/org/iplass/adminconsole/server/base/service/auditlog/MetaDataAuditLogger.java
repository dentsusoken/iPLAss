/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaDataAuditLogger {

	private static final String CATEGORY_METADATA = "mtp.audit.admin.metadata";
	private static final String CATEGORY_TENANT = "mtp.audit.admin.tenant";

	private static final Logger metadataLogger = LoggerFactory.getLogger(CATEGORY_METADATA);
	private static final Logger tenantLogger = LoggerFactory.getLogger(CATEGORY_TENANT);

	private static MetaDataAuditLogger instance = new MetaDataAuditLogger();

	private MetaDataAuditLogger() {}

	public static MetaDataAuditLogger getLogger() {
		return instance;
	}

	/**
	 * メタデータに対するログ出力
	 *
	 * @param action 操作
	 * @param definitionName 定義名
	 * @param detail ログ情報
	 */
	public void logMetadata(MetaDataAction action, String definitionName, String detail) {
		metadataLogger.info(action.getAction() + "," + definitionName + "," + sanitize(detail));
	}

	/**
	 * テナントに対するログ出力
	 *
	 * @param type 種別
	 * @param detail ログ情報
	 */
	public void logTenant(String type, String detail) {
		tenantLogger.info(type + ",," + sanitize(detail));
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
