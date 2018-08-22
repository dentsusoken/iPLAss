/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield;

import java.io.Serializable;
import java.util.Map;

import org.iplass.adminconsole.view.annotation.Refrectable;

/**
 * イベントパラメータクラス
 * @author lis3wg
 *
 */
public class MetaFieldUpdateEvent {

	/** メタデータの各フィールドの値を保持するマップ */
	private Map<String, Serializable> valueMap;

	/** メタデータ */
	private Refrectable value;

	/**
	 * コンストラクタ
	 */
	public MetaFieldUpdateEvent() {
	}

	/**
	 * コンストラクタ
	 * @param valueMap
	 */
	public MetaFieldUpdateEvent(Map<String, Serializable> valueMap, Refrectable value) {
		this.valueMap = valueMap;
		this.value = value;
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

	/**
	 * メタデータを取得。
	 * @return
	 */
	public Refrectable getValue() {
		return value;
	}

	/**
	 * メタデータを設定。
	 * @param value
	 */
	public void setValue(Refrectable value) {
		this.value = value;
	}


}
