/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.base.dto.io.upload;

public interface UploadProperty {

	/** テナントID */
	public static final String TENANT_ID = "tenantId";

	/** アップロードファイル名 */
	public static final String UPLOAD_FILE_NAME = "uploadFileName";

	/** アップロードファイル */
	public static final String UPLOAD_FILE = "uploadFile";

	/** ステータスコード */
	public static final String STATUS_CODE = "status";

	/** ステータスメッセージ */
	public static final String STATUS_MESSAGE = "statusMessage";

	/** メッセージ */
	public static final String MESSAGE = "messages";

	/** 多言語用Prefix */
	public static final String LOCALE_PREFIX = "locale_";

	/** ステータス */
	public static enum Status {
		INIT,
		SUCCESS,
		WARN,
		ERROR
	}

}
