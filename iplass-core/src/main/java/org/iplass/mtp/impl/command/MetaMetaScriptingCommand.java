/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.command;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.command.definition.ScriptingCommandDefinition;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;

public class MetaMetaScriptingCommand extends MetaMetaCommand {
	private static final long serialVersionUID = -9093215091573995083L;

	private static final String REQUEST_BINDING_NAME = "request";

	private String script;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public void applyConfig(CommandDefinition definition) {
		fillFrom(definition);
		ScriptingCommandDefinition def = (ScriptingCommandDefinition) definition;
		script = def.getScript();
	}

	@Override
	public CommandDefinition currentConfig() {
		ScriptingCommandDefinition definition = new ScriptingCommandDefinition();
		fillTo(definition);
		definition.setScript(script);
		return definition;
	}

	public MetaScriptingCommandRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new MetaScriptingCommandRuntime();
	}

	public class MetaScriptingCommandRuntime extends MetaCommandRuntime {

		private static final String SCRIPT_PREFIX = "MetaScriptingCommandRuntime_script";

		private Script compiledScript;
		private ScriptEngine ss;

		public MetaScriptingCommandRuntime() {
			try {
				if (script != null) {
					//TODO tenantIDの決定は、このメソッドを呼び出した際のスレッドに紐付いているテナントIDとなる。これでセキュリティ的、動作的に大丈夫か？
					TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
					ss = tc.getScriptEngine();
					compiledScript = ss.createScript(script, SCRIPT_PREFIX + "_" + getId());
				}
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		@Override
		public MetaMetaScriptingCommand getMetaData() {
			return MetaMetaScriptingCommand.this;
		}

		@Override
		public Command newCommand() {
			checkState();

			if (compiledScript != null) {
				if (compiledScript.isInstantiateAs(Command.class)) {
					return compiledScript.createInstanceAs(Command.class, null);
				} else {
					return new Command() {
						@Override
						public String execute(RequestContext request) {
							ScriptContext sc = ss.newScriptContext();
							sc.setAttribute(REQUEST_BINDING_NAME, request);
							ManagerLocator sl = ManagerLocator.getInstance();
							sc.setAttribute("em", sl.getManager(EntityManager.class));
							sc.setAttribute("edm", sl.getManager(EntityDefinitionManager.class));
							sc.setAttribute("auth", AuthContext.getCurrentContext());
							Object ret = compiledScript.eval(sc);
							if (ret == null) {
								return null;
							} else {
								return ret.toString();
							}
						}
					};
				}
			}
			return null;
		}

	}

}
