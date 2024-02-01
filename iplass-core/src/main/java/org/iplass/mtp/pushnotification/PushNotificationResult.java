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
	
	public PushNotificationResult() {
	}
	
	public PushNotificationResult(boolean success, Map<String, Object> details) {
		this.success = success;
		this.details = details;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	/**
	 * Push通知が成功した（少なくとも、バックエンドサービスに受け付けられたか）か否か。
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 処理結果の詳細。
	 * 利用するPush通知サービス、また処理結果により返却、格納される値は異なる。
	 * 
	 * @return
	 */
	public Map<String, Object> getDetails() {
		return details;
	}
	
	public void setDetail(String key, Object value) {
		if (details == null) {
			details = new HashMap<>();
		}
		details.put(key, value);
	}
	
	public Object getDetail(String key) {
		if (details == null) {
			return null;
		}
		return details.get(key);
	}
}
