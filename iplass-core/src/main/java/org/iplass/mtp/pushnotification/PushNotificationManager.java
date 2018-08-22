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

import java.util.Map;

import org.iplass.mtp.Manager;

/**
 * プッシュ通知を行う為のManager。
 * 
 * @author K.Higuchi
 *
 */
public interface PushNotificationManager extends Manager {
	
	/**
	 * 指定のテンプレートを利用した形でPushNotificationのインスタンスを生成する。
	 * 
	 * @param tmplDefName
	 * @param bindings
	 * @return
	 */
	public PushNotification createNotification(String tmplDefName, Map<String, Object> bindings);
	
	/**
	 * プッシュ通知する。
	 * 
	 * @param notification
	 * @return
	 */
	public PushNotificationResult push(PushNotification notification);
}
