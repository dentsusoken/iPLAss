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
package org.iplass.mtp.pushnotification;

import java.util.HashMap;
import java.util.Map;

/**
 * Push通知の結果を表すクラス。
 *
 * @author K.Higuchi
 *
 */
public class PushNotificationResult {

	private boolean success;
	private Map<String, Object> details;

	/**
	 * デフォルトコンストラクタ
	 */
	public PushNotificationResult() {
	}

	/**
	 * コンストラクタ
	 * @param success Push通知が成功した（少なくとも、バックエンドサービスに受け付けられたか）か否か
	 * @param details 処理結果の詳細
	 */
	public PushNotificationResult(boolean success, Map<String, Object> details) {
		this.success = success;
		this.details = details;
	}

	/**
	 * Push通知が成功したか否かを設定する
	 * @param success Push通知が成功したか否か（成功している場合true）
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 処理結果の詳細を設定する
	 * @param details 処理結果の詳細
	 */
	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	/**
	 * Push通知が成功した（少なくとも、バックエンドサービスに受け付けられたか）か否か。
	 *
	 * @return Push通知が成功したか否か（成功している場合true）
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 処理結果の詳細。
	 * 利用するPush通知サービス、また処理結果により返却、格納される値は異なる。
	 *
	 * @return 処理結果の詳細
	 */
	public Map<String, Object> getDetails() {
		return details;
	}

	/**
	 * 処理結果の詳細を設定する
	 * @param key 詳細キー
	 * @param value 詳細の値
	 */
	public void setDetail(String key, Object value) {
		if (details == null) {
			details = new HashMap<>();
		}
		details.put(key, value);
	}

	/**
	 * 処理結果の詳細のキーが一致する情報を取得する
	 * @param key 詳細キー
	 * @return 処理結果の詳細のキーが一致する情報
	 */
	public Object getDetail(String key) {
		if (details == null) {
			return null;
		}
		return details.get(key);
	}

	/**
	 * 処理結果の詳細のキーが一致する情報を取得する
	 * @param <T> データ型
	 * @param key キー
	 * @return 処理結果の詳細のキーが一致する情報
	 */
	@SuppressWarnings("unchecked")
	public <T> T getDetailValue(String key) {
		if (details == null) {
			return null;
		}
		return (T) details.get(key);
	}
}
