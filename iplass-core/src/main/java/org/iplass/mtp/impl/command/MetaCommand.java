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

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.definition.config.CommandConfig;
import org.iplass.mtp.command.definition.config.CompositeCommandConfig;
import org.iplass.mtp.command.definition.config.SingleCommandConfig;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.TransactionOption;


@XmlSeeAlso({MetaSingleCommand.class, MetaCompositeCommand.class})
public abstract class MetaCommand implements MetaData {

	public static final String CMD_BINDING_NAME = "cmd";

	private static final long serialVersionUID = 3346194032850331694L;

	//TODO 汎用的なプロパティ定義（Interceptorでつかう？）

	private String commandConfig;//groovyスクリプトによる設定機構

	private Propagation transactionPropagation = Propagation.REQUIRED;
	private boolean rollbackWhenException = true;
	private boolean throwExceptionIfSetRollbackOnly;

	public boolean isRollbackWhenException() {
		return rollbackWhenException;
	}

	public void setRollbackWhenException(boolean rollbackWhenException) {
		this.rollbackWhenException = rollbackWhenException;
	}

	public boolean isThrowExceptionIfSetRollbackOnly() {
		return throwExceptionIfSetRollbackOnly;
	}

	public void setThrowExceptionIfSetRollbackOnly(boolean throwExceptionIfSetRollbackOnly) {
		this.throwExceptionIfSetRollbackOnly = throwExceptionIfSetRollbackOnly;
	}

	public Propagation getTransactionPropagation() {
		return transactionPropagation;
	}

	public void setTransactionPropagation(Propagation transactionPropagation) {
		this.transactionPropagation = transactionPropagation;
	}

	public String getCommandConfig() {
		return commandConfig;
	}

	public void setCommandConfig(String commandConfig) {
		this.commandConfig = commandConfig;
	}

	public static MetaCommand createInstance(CommandConfig definition) {
		if (definition instanceof SingleCommandConfig) {
			return new MetaSingleCommand();
		} else if (definition instanceof CompositeCommandConfig) {
			return new MetaCompositeCommand();
		}
		return null;
	}

	public abstract void applyConfig(CommandConfig definition);

	public abstract CommandConfig currentConfig();

	protected void fillFrom(CommandConfig definition) {
		commandConfig = definition.getInitializeScript();
		transactionPropagation = definition.getTransactionPropagation();
		rollbackWhenException = definition.isRollbackWhenException();
		throwExceptionIfSetRollbackOnly = definition.isThrowExceptionIfSetRollbackOnly();
	}

	protected void fillTo(CommandConfig definition) {
		definition.setInitializeScript(commandConfig);
		definition.setTransactionPropagation(transactionPropagation);
		definition.setRollbackWhenException(rollbackWhenException);
		definition.setThrowExceptionIfSetRollbackOnly(throwExceptionIfSetRollbackOnly);
	}

	public MetaCommand copy() {
		return ObjectUtil.deepCopy(this);
	}

	public abstract CommandRuntime createRuntime();

	public abstract class CommandRuntime /*implements MetaDataRuntime*/ {

		private static final String SCRIPT_PREFIX = "CommandRuntime_commandConfig";

		private Command instance;
		protected Script configScript;

		public CommandRuntime(String identifer) {
			if (getMetaData().getCommandConfig() != null) {
				//TODO tenantIDの決定は、このメソッドを呼び出した際のスレッドに紐付いているテナントIDとなる。これでセキュリティ的、動作的に大丈夫か？
				TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
				ScriptEngine ss = tc.getScriptEngine();

				KeyGenerator keyGen = new KeyGenerator();
				configScript = ss.createScript(getMetaData().getCommandConfig(), SCRIPT_PREFIX + "_" + identifer + "_" + keyGen.generateId());
			}
			initImpl(identifer);
			if (!newInstancePerRequest()) {
				instance = newCommand();
			}
		}

		public TransactionOption getTransactionOption() {
			TransactionOption transactionOption = new TransactionOption(getTransactionPropagation());
			transactionOption.setReadOnly(readOnly());
			transactionOption.setRollbackWhenException(isRollbackWhenException());
			transactionOption.setThrowExceptionIfSetRollbackOnly(isThrowExceptionIfSetRollbackOnly());
			return transactionOption;
		}

		public final Command getCommand() {
			if (instance == null) {
				return newCommand();
			} else {
				return instance;
			}
		}

		public abstract MetaCommand getMetaData();
		protected abstract void initImpl(String identifer);
		protected abstract Command newCommand();
		public abstract boolean readOnly();
		protected abstract boolean newInstancePerRequest();
		public abstract String name();
	}
}
