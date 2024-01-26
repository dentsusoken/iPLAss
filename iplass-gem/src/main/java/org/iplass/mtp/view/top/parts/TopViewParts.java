/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.top.parts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * TOP画面パーツ
 * @author lis3wg
 */
@XmlSeeAlso({TopViewContentParts.class, ScriptParts.class, UserMaintenanceParts.class, FulltextSearchViewParts.class,
	CsvDownloadSettingsParts.class, ApplicationMaintenanceParts.class, PreviewDateParts.class})
public abstract class TopViewParts implements Serializable {

	/** SerialVersionUID */
	private static final long serialVersionUID = -8956929971943022861L;

	/** パラメータ */
	protected Map<String, String> params = new HashMap<>();

	/**
	 * パラメータを設定します
	 * @param params パラメータ
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * パラメータを取得します。
	 * @return パラメータ
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * パラメータを設定します。
	 * @param key キー
	 * @param value 値
	 */
	public void setParam(String key, String value) {
		params.put(key, value);
	}

	/**
	 * パラメータを取得します。
	 * @return パラメータ
	 */
	public String getParam(String key) {
		return params.get(key);
	}
}
