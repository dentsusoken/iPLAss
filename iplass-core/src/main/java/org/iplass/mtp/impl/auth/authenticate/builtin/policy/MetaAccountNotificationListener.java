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

package org.iplass.mtp.impl.auth.authenticate.builtin.policy;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.policy.AccountNotificationListener;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.JavaClassAccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.MailAccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.listeners.ScriptingAccountNotificationListenerDefinition;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

@XmlSeeAlso({MetaJavaClassAccountNotificationListener.class, MetaScriptingAccountNotificationListener.class, MetaMailAccountNotificationListener.class})
public abstract class MetaAccountNotificationListener implements MetaData {
	private static final long serialVersionUID = -2010834276738481945L;

	static MetaAccountNotificationListener newMeta(AccountNotificationListenerDefinition def) {
		if (def instanceof JavaClassAccountNotificationListenerDefinition) {
			return new MetaJavaClassAccountNotificationListener();
		}
		if (def instanceof MailAccountNotificationListenerDefinition) {
			return new MetaMailAccountNotificationListener();
		}
		if (def instanceof ScriptingAccountNotificationListenerDefinition) {
			return new MetaScriptingAccountNotificationListener();
		}
		return null;
	}

	@Override
	public MetaAccountNotificationListener copy() {
		return ObjectUtil.deepCopy(this);
	}

	public abstract AccountNotificationListener createInstance(String policyName, int i);

	public abstract AccountNotificationListenerDefinition currentConfig();
	public abstract void applyConfig(AccountNotificationListenerDefinition def);

}
