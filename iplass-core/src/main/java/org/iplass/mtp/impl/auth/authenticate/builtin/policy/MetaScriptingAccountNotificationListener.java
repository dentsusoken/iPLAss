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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.auth.policy.AccountNotification;
import org.iplass.mtp.auth.policy.AccountNotificationListener;
import org.iplass.mtp.auth.policy.LoginNotification;
import org.iplass.mtp.auth.policy.PasswordNotification;
import org.iplass.mtp.auth.policy.PropertyNotification;
import org.iplass.mtp.auth.policy.definition.AccountNotificationListenerDefinition;
import org.iplass.mtp.auth.policy.definition.NotificationType;
import org.iplass.mtp.auth.policy.definition.listeners.ScriptingAccountNotificationListenerDefinition;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;

public class MetaScriptingAccountNotificationListener extends MetaAccountNotificationListener {
	private static final long serialVersionUID = 6452808987410984246L;

	public static final String NOTIFICATION_BINDING_NAME = "notification";

	private static final String SCRIPT_PREFIX ="MetaScriptingAccountNotificationListener_script";

	private String script;
	private List<NotificationType> listenNotification;

	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public List<NotificationType> getListenNotification() {
		return listenNotification;
	}
	public void setListenNotification(List<NotificationType> listenNotification) {
		this.listenNotification = listenNotification;
	}

	@Override
	public AccountNotificationListener createInstance(String policyName, int i) {
		TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
		ScriptEngine scriptEngine = tc.getScriptEngine();

		if (script != null) {
			String scriptWithImport = "import " + NotificationType.class.getName() + ";\n" + script;
			String scriptName = SCRIPT_PREFIX + "_" + policyName + "_" + i;
			Script compiledScript = scriptEngine.createScript(scriptWithImport, scriptName);
			if (compiledScript.isInstantiateAs(AccountNotificationListener.class)) {
				return new TypedListener(compiledScript);
			} else {
				return new ScriptListener(compiledScript, scriptEngine);
			}
		}
		return null;
	}

	@Override
	public AccountNotificationListenerDefinition currentConfig() {
		ScriptingAccountNotificationListenerDefinition def = new ScriptingAccountNotificationListenerDefinition();
		def.setScript(script);
		if (listenNotification != null) {
			def.setListenNotification(new ArrayList<NotificationType>(listenNotification));
		}
		return def;
	}
	@Override
	public void applyConfig(AccountNotificationListenerDefinition def) {
		ScriptingAccountNotificationListenerDefinition sdef = (ScriptingAccountNotificationListenerDefinition) def;
		script = sdef.getScript();
		if (sdef.getListenNotification() != null) {
			listenNotification = new ArrayList<>(sdef.getListenNotification());
		} else {
			listenNotification = null;
		}
	}


	private class TypedListener implements AccountNotificationListener {
		private boolean created;
		private boolean reset;
		private boolean rockedout;
		private boolean credentialUpdate;
		private boolean propertyUpdate;
		private boolean remove;
		private boolean loginSuccess;
		private boolean loginFailed;
		private Script compiledScript;
		private AccountNotificationListener delegate;

		private TypedListener(Script compiledScript) {
			this.compiledScript = compiledScript;
			this.delegate = compiledScript.createInstanceAs(AccountNotificationListener.class, null);

			if (listenNotification != null) {
				for (NotificationType t: listenNotification) {
					switch (t) {
					case CREATED:
						created = true;
						break;
					case CREDENTIAL_RESET:
						reset = true;
						break;
					case ROCKEDOUT:
						rockedout = true;
						break;
					case CREDENTIAL_UPDATED:
						credentialUpdate = true;
						break;
					case PROPERTY_UPDATED:
						propertyUpdate = true;
						break;
					case REMOVE:
						remove = true;
						break;
					case LOGIN_SUCCESS:
						loginSuccess = true;
						break;
					case LOGIN_FAILED:
						loginFailed = true;
						break;
					default:
						break;
					}
				}
			}
		}

		@Override
		public void created(PasswordNotification notification) {
			if (created) {
				delegate.created(notification);
			}
		}

		@Override
		public void credentialReset(PasswordNotification notification) {
			if (reset) {
				delegate.credentialReset(notification);
			}
		}

		@Override
		public void rockedout(AccountNotification notification) {
			if (rockedout) {
				delegate.rockedout(notification);
			}
		}

		@Override
		public void credentialUpdated(PasswordNotification notification) {
			if (credentialUpdate) {
				delegate.credentialUpdated(notification);
			}
		}

		@Override
		public void propertyUpdated(PropertyNotification notification) {
			if (propertyUpdate) {
				delegate.propertyUpdated(notification);
			}
		}

		@Override
		public void remove(AccountNotification notification) {
			if (remove) {
				delegate.remove(notification);
			}
		}

		@Override
		public void loginSuccess(LoginNotification notification) {
			if (loginSuccess) {
				delegate.loginSuccess(notification);
			}
		}

		@Override
		public void loginFailed(LoginNotification notification) {
			if (loginFailed) {
				delegate.loginFailed(notification);
			}
		}

	}

	private class ScriptListener implements AccountNotificationListener {
		private boolean created;
		private boolean reset;
		private boolean rockedout;
		private boolean credentialUpdate;
		private boolean propertyUpdate;
		private boolean remove;
		private boolean loginSuccess;
		private boolean loginFailed;
		private Script compiledScript;
		private ScriptEngine scriptEngine;

		private ScriptListener(Script compiledScript, ScriptEngine scriptEngine) {
			this.compiledScript = compiledScript;
			this.scriptEngine = scriptEngine;

			if (listenNotification != null) {
				for (NotificationType t: listenNotification) {
					switch (t) {
					case CREATED:
						created = true;
						break;
					case CREDENTIAL_RESET:
						reset = true;
						break;
					case ROCKEDOUT:
						rockedout = true;
						break;
					case CREDENTIAL_UPDATED:
						credentialUpdate = true;
						break;
					case PROPERTY_UPDATED:
						propertyUpdate = true;
						break;
					case REMOVE:
						remove = true;
						break;
					case LOGIN_SUCCESS:
						loginSuccess = true;
						break;
					case LOGIN_FAILED:
						loginFailed = true;
						break;
					default:
						break;
					}
				}
			}
		}

		private Object callScript(AccountNotification notification) {
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute(NOTIFICATION_BINDING_NAME, notification);
			return compiledScript.eval(sc);
		}

		@Override
		public void created(PasswordNotification notification) {
			if (created) {
				callScript(notification);
			}
		}

		@Override
		public void credentialReset(PasswordNotification notification) {
			if (reset) {
				callScript(notification);
			}
		}

		@Override
		public void rockedout(AccountNotification notification) {
			if (rockedout) {
				callScript(notification);
			}
		}

		@Override
		public void credentialUpdated(PasswordNotification notification) {
			if (credentialUpdate) {
				callScript(notification);
			}
		}

		@Override
		public void propertyUpdated(PropertyNotification notification) {
			if (propertyUpdate) {
				callScript(notification);
			}
		}

		@Override
		public void remove(AccountNotification notification) {
			if (remove) {
				callScript(notification);
			}
		}

		@Override
		public void loginSuccess(LoginNotification notification) {
			if (loginSuccess) {
				callScript(notification);
			}
		}

		@Override
		public void loginFailed(LoginNotification notification) {
			if (loginFailed) {
				callScript(notification);
			}
		}
	}

}
