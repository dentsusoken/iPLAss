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

package org.iplass.adminconsole.shared.metadata.dto.template;

import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;

public interface BinaryTemplateUploadProperty extends UploadProperty {

	public static final String ACTION_URL = "service/binarytemplateupload";

	/** 定義名 */
	public static final String DEF_NAME = "defName";
	/** 表示名 */
	public static final String DISPLAY_NAME = "displayName";
	/** 説明 */
	public static final String DESCRIPTION = "description";

	/** ContentType */
	public static final String CONTENT_TYPE = "contentType";

	/** FileContentType */
	public static final String FILE_CONTENT_TYPE = "fileContentType";

	/** 多言語用変更前Locale名 */
	public static final String LOCALE_BEFORE = "beforeLocale";

	/** 更新対象Definition version */
	public static final String VERSION = "version";

	/** versionチェックをするか */
	public static final String CHECK_VERSION = "checkVersion";

}
