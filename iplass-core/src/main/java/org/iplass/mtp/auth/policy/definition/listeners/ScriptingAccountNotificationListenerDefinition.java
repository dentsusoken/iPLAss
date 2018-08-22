/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.auth.policy.definition.listeners;

import java.util.List;

import org.iplass.mtp.auth.policy.AccountNotificationListener;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.NotificationType;

/**
 * <p>
 * GroovyScriptによるAccountNotificationListener定義。
 * </p>
 * <p>
 * Script記述の方式として2パターンある。
 * </p>
 * <ul>
 * <li>Javaクラス指定時と同様に、{@link AccountNotificationListener}をimplementsしたクラスを記述。</li>
 * <li>Script形式で記述。</li>
 * </ul>
 * <p>
 * 	public static final String NOTIFICATION_BINDING_NAME = "notification";

 * Script形式での記述の場合、notificationの変数名で{@link AccountNotification}のインスタンスが
 * あらかじめバインドされている。
 * また、listenNotificationにてあらかじめ指定されている通知のみScriptが呼び出される。
 * </p>
 * <h5>Script形式での記述例：</h5>
 * <code><pre>
 * if (notification.type == NotificationType.CREATED) {
 *     println "create user:" + notification.userOid;
 *     :
 *     :
 * }
 * </pre></code>
 * 
 * @author K.Higuchi
 *
 */
public class ScriptingAccountNotificationListenerDefinition extends AccountNotificationListenerDefinition {
	private static final long serialVersionUID = 1974904229979573512L;
	
	private String script;
	private List<NotificationType> listenNotification;
	
	/**
	 * GroovyScriptのコード。
	 * 
	 * @return
	 */
	public String getScript() {
		return script;
	}
	
	/**
	 * see {@link #getScript()}
	 * @param script
	 */
	public void setScript(String script) {
		this.script = script;
	}
	
	/**
	 * 通知を受け取るNotificationType。
	 * ここで指定された通知のみScriptのロジックが呼び出される。
	 * 
	 * @return
	 */
	public List<NotificationType> getListenNotification() {
		return listenNotification;
	}
	
	/**
	 * see {@link #getListenNotification()}
	 * @param listenNotification
	 */
	public void setListenNotification(List<NotificationType> listenNotification) {
		this.listenNotification = listenNotification;
	}

}
