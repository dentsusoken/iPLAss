/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.langexplorer;

import java.util.LinkedHashMap;
import java.util.Map;

import org.iplass.adminconsole.shared.base.dto.i18n.MultiLangFieldInfo;

/**
 * 定義の多言語情報
 */
public class DefinitionLocalizedInfo {

	/** definitionのパス */
	private String path;

	/** 各項目の多言語情報 */
	private Map<String, MultiLangFieldInfo> fieldInfoList;

	/**
	 * definitionのパスを取得します。
	 * @return definitionのパス
	 */
	public String getPath() {
	    return path;
	}

	/**
	 * definitionのパスを設定します。
	 * @param path definitionのパス
	 */
	public void setPath(String path) {
	    this.path = path;
	}

	/**
	 * 各項目の多言語情報を取得します。
	 * @return 各項目の多言語情報
	 */
	public Map<String, MultiLangFieldInfo> getFieldInfoList() {
	    return fieldInfoList;
	}

	/**
	 * 各項目の多言語情報を設定します。
	 * @param fieldInfoList 各項目の多言語情報
	 */
	public void setFieldInfoList(Map<String, MultiLangFieldInfo> fieldInfoList) {
	    this.fieldInfoList = fieldInfoList;
	}

	/**
	 * 多言語情報を追加します。
	 * @param key 項目のkey
	 * @param fieldInfo 多言語情報
	 */
	public void addFieldInfo(String key, MultiLangFieldInfo fieldInfo) {
	    if (this.fieldInfoList == null) this.fieldInfoList = new LinkedHashMap<String, MultiLangFieldInfo>();
	    this.fieldInfoList.put(key, fieldInfo);
	}
}
