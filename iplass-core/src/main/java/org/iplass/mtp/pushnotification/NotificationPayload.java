/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

/**
 * 通知ペイロードを表すクラス。
 * いくつかの事前定義されたパラメータが存在する。<br>
 * FCMの場合、<br>
 * title、body、icon、soundなどを設定可能（詳細はFirebaseのドキュメント参照のこと）。
 * 
 * 
 * @author K.Higuchi
 *
 */
public class NotificationPayload extends HashMap<String, Object> implements Payload {
	private static final long serialVersionUID = 273504545521975698L;

	/**
	 * 通知のタイトルを設定する際のkey。<br>
	 * iOS（ウォッチ）、Androidで指定可能。<br>
	 * Androidの場合必須。
	 */
	public static final String TITLE = "title";

	/**
	 * 通知の本文テキストを設定する際のkey。 <br>
	 * iOS、Androidで指定可能。<br>
	 */
	public static final String BODY = "body";

	/**
	 * 通知アイコンを設定する際のkey。<br>
	 * Androidで指定可能かつ必須。<br>
	 */
	public static final String ICON = "icon";
	
	
	public NotificationPayload() {
		super();
	}
	
	/**
	 * 指定のtitle,body,iconで通知を構築する。
	 * 
	 * @param title
	 * @param body
	 * @param icon
	 */
	public NotificationPayload(String title, String body, String icon) {
		super();
		if (title != null) {
			put(TITLE, title);
		}
		if (icon != null) {
			put(ICON, icon);
		}
		if (body != null) {
			put(BODY, body);
		}
	}
	
	/**
	 * 指定のbodyで通知を構築する。
	 * 
	 * @param body
	 */
	public NotificationPayload(String body) {
		super();
		if (body != null) {
			put(BODY, body);
		}
	}
	
	@Override
	public Object get(String key) {
		return get((Object) key);
	}

}
