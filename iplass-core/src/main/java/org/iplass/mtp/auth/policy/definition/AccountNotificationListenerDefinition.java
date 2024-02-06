/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

/**
 * 
 */
package org.iplass.mtp.auth.policy.definition;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.policy.AccountNotificationListener;
import org.iplass.mtp.auth.policy.definition.listeners.JavaClassAccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.MailAccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.ScriptingAccountNotificationListenerDefinition;

/**
 * {@link AccountNotificationListener}の定義。
 * 定義されたAccountNotificationListenerにて、アカウントの状態の変更通知を受け取ることができる。
 * 
 * @see JavaClassAccountNotificationListenerDefinition
 * @see MailAccountNotificationListenerDefinition
 * @see ScriptingAccountNotificationListenerDefinition
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso({
	JavaClassAccountNotificationListenerDefinition.class,
	MailAccountNotificationListenerDefinition.class,
	ScriptingAccountNotificationListenerDefinition.class})
public class AccountNotificationListenerDefinition implements Serializable {
	private static final long serialVersionUID = -5481438995099093572L;

}
