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

public interface ReportTemplateUploadProperty extends UploadProperty {

	public static final String ACTION_URL = "service/reporttemplateupload";

	/** 定義名 */
	public static final String DEF_NAME = "defName";
	/** 表示名 */
	public static final String DISPLAY_NAME = "displayName";
	/** 説明 */
	public static final String DESCRIPTION = "description";

	/** ReportType */
	public static final String REPORT_TYPE = "reportType";

	/** ContentType */
	public static final String CONTENT_TYPE = "contentType";

	/** FileContentType */
	public static final String FILE_CONTENT_TYPE = "fileContentType";

	/** 出力形式 */
	public static final String OUTPUT_FILE_TYPE = "type";

	/** Jasper パラメータマップ名 */
	public static final String JASPER_PARAM_MAP_NAME = "jasperMapName";

	/** Jasper パラメータ名前 */
	public static final String JASPER_PARAM_MAP_FROM = "jasperMapFrom";

	/** Jasper パラメータタイプ */
	public static final String JASPER_PARAM_TYPE = "jasperParamType";

	/** Jasper パラメータマップ数 */
	public static final String JASPER_PARAM_MAP_CNT = "jasperMapCnt";

	/** Jasper DataSourceAttributeName */
	public static final String JASPER_DATASOURCE_ATTRIBUTE_NAME = "jasperDataSourceAttributeName";

	/** Jasper PasswordAttributeName */
	public static final String JASPER_PASSWORD_ATTRIBUTE_NAME = "jasperPasswordAttributeName";
	
	/** Jasper OwnerPasswordAttributeName */
	public static final String JASPER_OWNER_PASSWORD_ATTRIBUTE_NAME = "jasperOwnerPasswordAttributeName";

	/** Poiレポート出力ロジック形式 */
	public static final String POI_LOGIC_NAME = "poiLogicName";

	/** Poiレポート出力ロジック形式(JAVA) */
	public static final String POI_LOGIC_NAME_JAVA = "poiJavaClass";

	/** Poiレポート出力ロジック形式(GROOVY) */
	public static final String POI_LOGIC_NAME_GROOVY = "poiGroovy";

	/** Poiレポート出力ロジック値 */
	public static final String POI_LOGIC_VALUE = "poiLogicValue";

	/** Poi PasswordAttributeName */
	public static final String POI_PASSWORD_ATTRIBUTE_NAME = "poiPasswordAttributeName";
	
	/** Jxlsレポート出力ロジック形式 */
	public static final String JXLS_LOGIC_NAME = "jxlsLogicName";

	/** Jxlsレポート出力ロジック形式(JAVA) */
	public static final String JXLS_LOGIC_NAME_JAVA = "jxlsJavaClass";

	/** Jxlsレポート出力ロジック形式(GROOVY) */
	public static final String JXLS_LOGIC_NAME_GROOVY = "jxlsGroovy";

	/** jxlsレポート出力ロジック値 */
	public static final String JXLS_LOGIC_VALUE = "jxlsLogicValue";

	/** Jxls PasswordAttributeName */
	public static final String JXLS_PASSWORD_ATTRIBUTE_NAME = "jxlsPasswordAttributeName";
	
	/** Jxls ParamMapKey */
	public static final String JXLS_PARAM_MAP_NAME = "jxlsParamMapName";

	/** Jxls ParamMapValue */
	public static final String JXLS_PARAM_MAP_VALUE = "jxlsParamMapValue ";
	
	/** Jxls ParamMapToMap */
	public static final String JXLS_PARAM_MAP_TO_MAP = "jxlsParamMapToMap ";
	
	/** Jxls ParamMapCount */
	public static final String JXLS_PARAM_MAP_CNT = "jxlsParamMapCnt";
	
	/** 多言語用変更前Locale名 */
	public static final String LOCALE_BEFORE = "beforeLocale";

	/** 更新対象Definition version */
	public static final String VERSION = "version";

	/** versionチェックをするか */
	public static final String CHECK_VERSION = "checkVersion";

}
