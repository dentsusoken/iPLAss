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

package org.iplass.adminconsole.client.metadata.ui.auth.listener;

import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.JavaClassAccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.MailAccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.ScriptingAccountNotificationListenerDefinition;

/**
 * AuthenticationListenerの種類
 */
public enum AuthenticationListenerType {

	JAVACLASS("JavaClass", JavaClassAccountNotificationListenerDefinition.class),
	MAIL("Mail", MailAccountNotificationListenerDefinition.class),
	SCRIPTING("Scripting", ScriptingAccountNotificationListenerDefinition.class);

	private String displayName;
	private Class<AccountNotificationListenerDefinition> definitionClass;

	// Classに対してClass<AccountNotificationListenerDefinition>を指定するとコンパイルエラーになるため未指定
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private AuthenticationListenerType(String displayName, Class definitionClass) {
		this.displayName = displayName;
		this.definitionClass = definitionClass;
	}

	public String displayName() {
		return displayName;
	}

	public Class<AccountNotificationListenerDefinition> definitionClass() {
		return definitionClass;
	}

	public static AuthenticationListenerType valueOf(AccountNotificationListenerDefinition definition) {
		for (AuthenticationListenerType type : values()) {
			if (definition.getClass().getName().equals(type.definitionClass().getName())) {
				return type;
			}
		}
		return null;
	}

	public static AccountNotificationListenerDefinition typeOfDefinition(AuthenticationListenerType type) {
		if (type.definitionClass().equals(JavaClassAccountNotificationListenerDefinition.class)) {
			return new JavaClassAccountNotificationListenerDefinition();
		} else if (type.definitionClass().equals(MailAccountNotificationListenerDefinition.class)) {
			return new MailAccountNotificationListenerDefinition();
		} else if (type.definitionClass().equals(ScriptingAccountNotificationListenerDefinition.class)) {
			return new ScriptingAccountNotificationListenerDefinition();
		}
		return null;
	}

	public static AuthenticationListenerTypeEditPane typeOfEditPane(AuthenticationListenerType type) {
		if (type.definitionClass().equals(JavaClassAccountNotificationListenerDefinition.class)) {
			return new JavaClassAccountNotificationListenerEditPane();
		} else if (type.definitionClass().equals(MailAccountNotificationListenerDefinition.class)) {
			return new MailAccountNotificationListenerEditPane();
		} else if (type.definitionClass().equals(ScriptingAccountNotificationListenerDefinition.class)) {
			return new ScriptingAccountNotificationListenerEditPane();
		}
		return null;
	}
}
