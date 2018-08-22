/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.command.definition.config.CompositeCommandConfig;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaCompositeCommand extends MetaCommand {

	private static final long serialVersionUID = 2767161125283111456L;

	private static Logger logger = LoggerFactory.getLogger(MetaCompositeCommand.class);
	
	private MetaCommand[] commands;

	private String rule;//Commandの処理フローの記述。GroovyのScript。最後にステータスをreturnするように実装。

	public MetaCommand[] getCommands() {
		return commands;
	}

	public void setCommands(MetaCommand[] commands) {
		this.commands = commands;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Override
	public CompositeCommandRuntime createRuntime() {
		return new CompositeCommandRuntime("CompositeCommand");
	}

	@Override
	public void applyConfig(CommandConfig definition) {
		fillFrom(definition);
		CompositeCommandConfig def = (CompositeCommandConfig) definition;

		if (def.getCommands() != null) {
			commands = new MetaCommand[def.getCommands().length];
			int i = 0;
			for (CommandConfig config : def.getCommands()) {
				commands[i] = MetaCommand.createInstance(config);
				commands[i].applyConfig(config);
				i++;
			}
		}
		rule = def.getExecuteScript();
	}

	@Override
	public CompositeCommandConfig currentConfig() {
		CompositeCommandConfig definition = new CompositeCommandConfig();
		fillTo(definition);

		if (commands != null) {
			CommandConfig[] configs = new CommandConfig[commands.length];
			int i = 0;
			for (MetaCommand command : commands) {
				configs[i] = command.currentConfig();
				i++;
			}
			definition.setCommands(configs);
		}
		definition.setExecuteScript(rule);
		return definition;
	}

	public class CompositeCommandRuntime extends CommandRuntime {

		private static final String SCRIPT_PREFIX = "CompositeCommandRuntime_rule";

		private boolean readOnly;
		private boolean newInstancePerRequest;
		private CommandRuntime[] crs;
		Script executeRule;
		
		public CompositeCommandRuntime(String identifer) {
			super(identifer);
		}

		@Override
		protected void initImpl(String identifer) {
			crs = new CommandRuntime[commands.length];
			readOnly = true;
			newInstancePerRequest = false;
			for (int i = 0; i < commands.length; i++) {
				crs[i] = commands[i].createRuntime();
				readOnly &= crs[i].readOnly();
				newInstancePerRequest |= crs[i].newInstancePerRequest();
			}
			
			if (rule != null) {
				//TODO ScriptServiceは、すべてで共有して問題ないか？cmd、Entityなどある程度の塊で分けたほうがよいか？
				//TODO tenantIDの決定は、このメソッドを呼び出した際のスレッドに紐付いているテナントIDとなる。これでセキュリティ的、動作的に大丈夫か？
				TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
				KeyGenerator keyGen = new KeyGenerator();
				executeRule = tc.getScriptEngine().createScript(rule, SCRIPT_PREFIX + "_" + identifer + "_" + keyGen.generateId());
			}
		}

		@Override
		public MetaCompositeCommand getMetaData() {
			return MetaCompositeCommand.this;
		}

		@Override
		protected Command newCommand() {
			CompositeCommand cmd = new CompositeCommand(crs, executeRule);
			//Commandの初期化処理（Groovyにより設定可能）
			initCommand(cmd);
			return cmd;
		}
		
		private void initCommand(CompositeCommand cmd) {

			logger.debug("init CompositeCommand instance:" + cmd);

			if (configScript != null) {

				//TODO tenantIDの決定は、このメソッドを呼び出した際のスレッドに紐付いているテナントIDとなる。これでセキュリティ的、動作的に大丈夫か？
				TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
				ScriptEngine ss = tc.getScriptEngine();

				ScriptContext sc = ss.newScriptContext();
				sc.setAttribute(CMD_BINDING_NAME, cmd.getCommands());
				configScript.eval(sc);
			}
		}
		

		@Override
		public boolean readOnly() {
			return readOnly;
		}

		@Override
		protected boolean newInstancePerRequest() {
			return newInstancePerRequest;
		}

		@Override
		public String name() {
			//TODO 一旦固定文字列
			return "CompositeCommand";
		}
	}
	

}
