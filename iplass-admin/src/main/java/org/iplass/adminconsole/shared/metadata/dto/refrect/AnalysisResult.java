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

package org.iplass.adminconsole.shared.metadata.dto.refrect;

import java.io.Serializable;
import java.util.Map;

/**
 * 解析結果
 * @author lis3wg
 *
 */
public class AnalysisResult implements Serializable {

	private static final long serialVersionUID = 2026104807360555281L;

	/** メタデータのフィールド情報 */
	private FieldInfo[] fields;

	/** メタデータの各フィールドの値を保持するマップ */
	private Map<String, Serializable> valueMap;

	/** コンストラクタ */
	public AnalysisResult() {
	}

	/**
	 * メタデータのフィールド情報を取得。
	 * @return
	 */
	public FieldInfo[] getFields() {
		return fields;
	}

	/**
	 * メタデータのフィールド情報を設定。
	 * @param fields
	 */
	public void setFields(FieldInfo[] fields) {
		this.fields = fields;
	}

	/**
	 * メタデータの各フィールドの値を保持するマップを取得。
	 * @return
	 */
	public Map<String, Serializable> getValueMap() {
		return valueMap;
	}

	/**
	 * メタデータの各フィールドの値を保持するマップを設定。
	 * @param valueMap
	 */
	public void setValueMap(Map<String, Serializable> valueMap) {
		this.valueMap = valueMap;
	}
}
