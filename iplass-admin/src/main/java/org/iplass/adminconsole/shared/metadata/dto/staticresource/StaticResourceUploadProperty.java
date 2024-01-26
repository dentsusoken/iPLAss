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

package org.iplass.adminconsole.shared.metadata.dto.staticresource;

import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;

public interface StaticResourceUploadProperty extends UploadProperty {

	public static final String ACTION_URL = "service/staticresourceupload";

	/** 定義名 */
	public static final String DEF_NAME = "defName";
	/** 表示名 */
	public static final String DISPLAY_NAME = "displayName";
	/** 説明 */
	public static final String DESCRIPTION = "description";

	/** 多言語表示名用Prefix */
	public static final String DISPLAY_NAME_LOCALE_PREFIX = "displayNameLocale_";

	/** バイナリタイプ */
	public static final String BINARY_TYPE = "binaryType";

	/** コンテンツタイプ */
	public static final String CONTENT_TYPE = "contentType";
	/** コンテンツタイプ(ファイル) */
	public static final String FILE_CONTENT_TYPE = "fileContentType";

	/** MIMETypeMapping */
	public static final String MIME_TYPE_MAPPING_PREFIX = "mimeTypeMapping_";

	/** EntryTextCharset */
	public static final String ENTRY_TEXT_CHARSET = "entryTextCharset";

	/** EntryPath */
	public static final String ENTRY_PATH_TYPE = "entryPathType";
	public static final String ENTRY_PATH_CONTENT = "entryPathContent";

	/** 多言語用変更前Locale名 */
	public static final String LOCALE_BEFORE = "beforeLocale";

	/** 更新対象Definition version */
	public static final String VERSION = "version";

	/** versionチェックをするか */
	public static final String CHECK_VERSION = "checkVersion";

}
