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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Push通知を表すクラス。
 *
 * @author K.Higuchi
 *
 */
public class PushNotification {
	//target
	private String to;
	private List<String> toList;
	//	private List<String> registrationIds;
	//	private String condition;

	//options
	private Map<String, Object> options = new HashMap<>();

	//payload
	private DataPayload data;
	private NotificationPayload notification;

	/** Push通知汎用メッセージ */
	private Map<String, Object> message;

	/**
	 * 送信先をセット/追加する。<br>
	 * FCMの場合、<br>
	 * toには、登録トークン、通知キー、トピック、
	 * もしくは、メッセージのターゲットを決定する条件の論理式（'yourTopic' in topics）で
	 * 指定可能（詳細はFirebaseのドキュメント参照のこと）。
	 *
	 * @param to
	 */
	public void addTo(String to) {
		if (toList == null) {
			if (this.to == null) {
				this.to = to;
			} else {
				toList = new ArrayList<>();
				toList.add(this.to);
				toList.add(to);
				this.to = null;
			}
		} else {
			toList.add(to);
		}
	}
	public void setToList(List<String> toList) {
		this.toList = toList;
	}

	/**
	 * 現在設定されている送信先のリスト（readOnly）を取得する。
	 *
	 * @return
	 */
	public List<String> getToList() {
		if (toList != null) {
			return Collections.unmodifiableList(toList);
		}
		if (to != null) {
			return Collections.singletonList(to);
		}
		return Collections.emptyList();
	}

	/**
	 * PushNotificationの送信オプションをセットする。<br>
	 * FCMの場合、<br>
	 * <code>setOption("priority", "high");</code><br>
	 * <code>setOption("content_available", true);</code><br>
	 * など設定可能（詳細はFirebaseのドキュメント参照のこと）。
	 *
	 * @param key
	 * @param value
	 */
	public void setOption(String key, Object value) {
		if (options == null) {
			options = new HashMap<>();
		}
		options.put(key, value);
	}
	public Map<String, Object> getOptions() {
		return options;
	}
	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public DataPayload getData() {
		if (data == null) {
			data = new DataPayload();
		}
		return data;
	}

	/**
	 * アプリ固有のデータペイロードをセットする。
	 *
	 * @param data
	 */
	public void setData(DataPayload data) {
		this.data = data;
	}

	/**
	 * data（DataPayload）に指定のkeyでvalueをputします。
	 *
	 * @param key
	 * @param value
	 */
	public void addData(String key, Object value) {
		if (data == null) {
			data = new DataPayload();
		}
		data.put(key, value);
	}

	public NotificationPayload getNotification() {
		if (notification == null) {
			notification = new NotificationPayload();
		}
		return notification;
	}
	/**
	 * 通知ペイロードをセットする。
	 *
	 * @param notification
	 */
	public void setNotification(NotificationPayload notification) {
		this.notification = notification;
	}

	/**
	 * Push通知汎用メッセージを取得する
	 * @return Push通知汎用メッセージ
	 */
	public Map<String, Object> getMessage() {
		return message;
	}

	/**
	 * Push通知汎用メッセージを設定する
	 * <p>
	 * Push通知汎用メッセージはPush通知実現サービスによって設定方法が異なる。
	 * </p>
	 * @param message Push通知汎用メッセージ
	 */
	public void setMessage(Map<String, Object> message) {
		this.message = message;
	}
}
