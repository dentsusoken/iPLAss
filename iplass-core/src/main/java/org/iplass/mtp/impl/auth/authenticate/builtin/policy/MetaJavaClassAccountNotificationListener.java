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

package org.iplass.mtp.impl.auth.authenticate.builtin.policy;

import org.iplass.mtp.auth.policy.AccountNotificationListener;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.JavaClassAccountNotificationListenerDefinition;

public class MetaJavaClassAccountNotificationListener extends MetaAccountNotificationListener {
	private static final long serialVersionUID = 8239520834373772775L;
	
	private String className;
//	private String initScript;
	
	@Override
	public AccountNotificationListener createInstance(String policyName, int i) {
		try {
			return (AccountNotificationListener) Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new IllegalStateException(className + " cant instanceate.", e);
		}
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public AccountNotificationListenerDefinition currentConfig() {
		JavaClassAccountNotificationListenerDefinition def = new JavaClassAccountNotificationListenerDefinition();
		def.setClassName(className);
		return def;
	}

	@Override
	public void applyConfig(AccountNotificationListenerDefinition def) {
		className = ((JavaClassAccountNotificationListenerDefinition) def).getClassName();
	}

}
