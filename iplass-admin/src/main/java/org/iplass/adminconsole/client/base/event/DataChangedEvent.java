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

package org.iplass.adminconsole.client.base.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>汎用データ変更イベントです。</p>
 *
 * <p>
 * イベント内部にMapを持ちます。
 * 引き渡したい情報を格納してください。
 * </p>
 *
 * <p>
 * 複数のコンポーネントに分割した際に、変更を伝える手段として利用してください。
 * </p>
 */
public class DataChangedEvent {

	/** ValueObjectとして保存する際のKEY */
	public static final String KEY_VALUE_OBJECT = "valueObject";
	/** ValueNameとして保存する際のKEY */
	public static final String KEY_VALUE_NAME = "valueName";

	/** 値を保持するマップ */
	private Map<String, Serializable> valueMap;

	/**
	 * コンストラクタ
	 */
	public DataChangedEvent() {
		this(new HashMap<String, Serializable>());
	}

	/**
	 * コンストラクタ
	 *
	 * @param valueMap 値を保持するマップ
	 */
	public DataChangedEvent(Map<String, Serializable> valueMap) {
		this.valueMap = valueMap;
	}

	/**
	 * 値を保持するマップを返します。
	 *
	 * @return 値を保持するマップ
	 */
	public Map<String, Serializable> getValueMap() {
		return valueMap;
	}

	/**
	 * 値を保持するマップを設定します。
	 *
	 * @param valueMap 値を保持するマップ
	 */
	public void setValueMap(Map<String, Serializable> valueMap) {
		this.valueMap = valueMap;
	}

	/**
	 * 型を指定して値を取得します。（キャスト不要）
	 *
	 * @param <T> 値の型
	 * @param componentClass 値の型
	 * @param key キー
	 * @return 値
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(final Class<T> componentClass, String key) {
		return (T) valueMap.get(key);
	}

	/**
	 * 値を設定します。
	 *
	 * @param key キー
	 * @param 値
	 */
	public void setValue(String key, Serializable value) {
		valueMap.put(key, value);
	}

	/**
	 * <p>型を指定してValueObjectを取得します。（キャスト不要）<p>
	 * <p>
	 * Mapキーは {@link DataChangedEvent#KEY_VALUE_OBJECT} です。
	 * </p>
	 *
	 * @param <T> 値の型
	 * @param componentClass 値の型
	 * @return ValueObject
	 */
	public <T> T getValueObject(final Class<T> componentClass) {
		return (T) getValue(componentClass, KEY_VALUE_OBJECT);
	}

	/**
	 * <p>ValueObjectとして値を設定します。</p>
	 * <p>
	 * Mapキーは {@link DataChangedEvent#KEY_VALUE_OBJECT} です。
	 * </p>
	 *
	 * @param 値
	 */
	public void setValueObject(Serializable value) {
		setValue(KEY_VALUE_OBJECT, value);
	}

	/**
	 * <p>ValueNameを取得します。<p>
	 * <p>
	 * Mapキーは {@link DataChangedEvent#KEY_VALUE_NAME} です。
	 * </p>
	 *
	 * @return ValueName
	 */
	public String getValueName() {
		return (String) getValue(String.class, KEY_VALUE_NAME);
	}

	/**
	 * <p>ValueNameとして値を設定します。</p>
	 * <p>
	 * Mapキーは {@link DataChangedEvent#KEY_VALUE_NAME} です。
	 * </p>
	 *
	 * @param ValueName
	 */
	public void setValueName(String name) {
		setValue(KEY_VALUE_NAME, name);
	}
}
