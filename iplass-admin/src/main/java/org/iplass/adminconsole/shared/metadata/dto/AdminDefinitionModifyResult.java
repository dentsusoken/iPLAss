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

package org.iplass.adminconsole.shared.metadata.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link org.iplass.mtp.definition.DefinitionModifyResult} のラッパークラス
 *
 * コンパイルしたもので実行するとうまくSerializableできないため、
 * Admin側ではこのクラスを利用して結果を受け渡す。
 *
 * @author lis70i
 *
 */
public class AdminDefinitionModifyResult implements Serializable {

	private static final long serialVersionUID = 5017185294034459446L;

	/** 結果 */
	private boolean isSuccess;

	/** メッセージ */
	private String message;

	/** 補足情報 */
	private Map<String, String> attributes;

	/**
	 * コンストラクタ
	 */
	public AdminDefinitionModifyResult() {
		this(false, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param isSuccess 結果
	 */
	public AdminDefinitionModifyResult(boolean isSuccess) {
		this(isSuccess, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param isSuccess 結果
	 * @param message メッセージ
	 */
	public AdminDefinitionModifyResult(boolean isSuccess, String message) {
		this.isSuccess = isSuccess;
		this.message = message;
		this.attributes = new HashMap<>();
	}

	/**
	 * 結果を取得します。
	 *
	 * @return 結果
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * メッセージを取得します。
	 *
	 * @return メッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 属性を設定します。
	 *
	 * @param key 属性
	 * @param value 値
	 */
	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	/**
	 * 属性を返します。
	 *
	 * @param key 属性
	 * @return 値
	 */
	public String getAttribute(String key) {
		return attributes.get(key);
	}

}
