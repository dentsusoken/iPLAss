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

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.definition.config.CommandConfig;
import org.iplass.mtp.command.definition.config.SingleCommandConfig;
import org.iplass.mtp.impl.command.MetaMetaCommand.MetaCommandRuntime;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaSingleCommand extends MetaCommand {

	private static final long serialVersionUID = 4170356067972278213L;
	
	private static Logger logger = LoggerFactory.getLogger(MetaSingleCommand.class);

	private String metaMetaCommandId;

	public String getMetaMetaCommandId() {
		return metaMetaCommandId;
	}

	public void setMetaMetaCommandId(String metaMetaCommandId) {
		this.metaMetaCommandId = metaMetaCommandId;
	}

	public SingleCommandRuntime createRuntime() {
		return new SingleCommandRuntime(metaMetaCommandId);
	}

	@Override
	public void applyConfig(CommandConfig definition) {
		fillFrom(definition);
		SingleCommandConfig def = (SingleCommandConfig) definition;

		//name -> id 変換
		CommandService service = ServiceRegistry.getRegistry().getService(CommandService.class);
		MetaCommandRuntime runtime =service.getRuntimeByName(def.getCommandName());

		metaMetaCommandId = runtime.getMetaData().getId();
	}

	@Override
	public SingleCommandConfig currentConfig() {
		SingleCommandConfig definition = new SingleCommandConfig();
		fillTo(definition);

		//id -> name 変換
		CommandService service = ServiceRegistry.getRegistry().getService(CommandService.class);
		MetaCommandRuntime runtime =service.getRuntimeById(metaMetaCommandId);

		if (runtime != null) {
			definition.setCommandName(runtime.getMetaData().getName());
		}
		return definition;
	}

	public class SingleCommandRuntime extends CommandRuntime {
		private boolean readOnly;
		private String name;

		public SingleCommandRuntime(String identifer) {
			super(identifer);
		}

		@Override
		protected void initImpl(String identifer) {
			MetaCommandRuntime mcr = getMetaCommandRuntime();
			readOnly = mcr.getMetaData().isReadOnly();
			name = mcr.getMetaData().getName();
		}

		private MetaCommandRuntime getMetaCommandRuntime() {
			CommandService service = ServiceRegistry.getRegistry().getService(CommandService.class);
			MetaCommandRuntime runtime = service.getRuntimeById(metaMetaCommandId);
			if (runtime == null) {
				throw new NullPointerException("CommandClassId:" + metaMetaCommandId + " Command class not found");
			}
			return runtime;
		}

		public MetaSingleCommand getMetaData() {
			return MetaSingleCommand.this;
		}

		@Override
		protected Command newCommand() {
			MetaCommandRuntime runtime = getMetaCommandRuntime();
			Command cmd = runtime.newCommand();
			if (cmd != null) {
				//Commandの初期化処理（Groovyにより設定可能）
				initCommand(cmd);
			}
			return cmd;
		}
		
		private void initCommand(Command cmd) {
			logger.debug("init Command instance:" + cmd);

			if (configScript != null) {
				TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
				ScriptEngine ss = tc.getScriptEngine();

				ScriptContext sc = ss.newScriptContext();
				sc.setAttribute(CMD_BINDING_NAME, cmd);
				configScript.eval(sc);
			}
		}

		@Override
		public boolean readOnly() {
			return readOnly;
		}

		@Override
		protected boolean newInstancePerRequest() {
			MetaCommandRuntime runtime = getMetaCommandRuntime();
			return runtime.getMetaData().isNewInstancePerRequest();
		}

		@Override
		public String name() {
			return name;
		}

	}
}
