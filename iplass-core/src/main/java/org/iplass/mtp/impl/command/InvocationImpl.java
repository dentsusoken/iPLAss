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
import org.iplass.mtp.command.ExceptionAware;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.command.interceptor.CommandInvocation;
import org.iplass.mtp.impl.command.MetaCommand.CommandRuntime;
import org.iplass.mtp.impl.command.MetaMetaCommand.MetaCommandRuntime;
import org.iplass.mtp.transaction.TransactionOption;

public class InvocationImpl implements CommandInvocation {

	//TODO 後から追加したので、形がいびつ、要リファクタリング
	public static CommandInterceptor[] NULL_COMMAND_INTERCEPTOR = new CommandInterceptor[0];

	private CommandInterceptor[] commandInterceptors;
	private Command cmd;
	private RequestContext request;
	private int index;
	private TransactionOption transactionOption;
	private String cmdName;

	public InvocationImpl(CommandInterceptor[] commandInterceptors, CommandRuntime cmdRuntime, RequestContext request) {
		if (commandInterceptors != null) {
			this.commandInterceptors = commandInterceptors;
		} else {
			this.commandInterceptors = NULL_COMMAND_INTERCEPTOR;
		}
		if (cmdRuntime != null) {
			this.cmd = cmdRuntime.getCommand();
			transactionOption = cmdRuntime.getTransactionOption();
			this.cmdName = cmdRuntime.name();
		}
		this.request = request;
		index = -1;
	}
	
	public InvocationImpl(CommandInterceptor[] commandInterceptors, MetaCommandRuntime metaCmd, RequestContext request, TransactionOption transactionOption) {
		if (commandInterceptors != null) {
			this.commandInterceptors = commandInterceptors;
		} else {
			this.commandInterceptors = NULL_COMMAND_INTERCEPTOR;
		}
		if (metaCmd != null) {
			this.cmd = metaCmd.newCommand();
			this.cmdName = metaCmd.getMetaData().getName();
		}
		this.request = request;
		if (transactionOption == null) {
			this.transactionOption = new TransactionOption();
			this.transactionOption.setReadOnly(metaCmd.getMetaData().isReadOnly());
		} else {
			this.transactionOption = transactionOption;
		}
		index = -1;
	}

	public InvocationImpl(CommandInterceptor[] commandInterceptors, Command cmd, RequestContext request, TransactionOption transactionOption, String cmdName) {
		if (commandInterceptors != null) {
			this.commandInterceptors = commandInterceptors;
		} else {
			this.commandInterceptors = NULL_COMMAND_INTERCEPTOR;
		}
		this.cmd = cmd;
		this.cmdName = cmdName;
		this.request = request;
		if (transactionOption == null) {
			this.transactionOption = new TransactionOption();
		} else {
			this.transactionOption = transactionOption;
		}
		index = -1;
	}
	
	public TransactionOption getTransactionOption() {
		return transactionOption;
	}

	public String proceedCommand() {
		try {
			if (cmd != null) {
				index++;
				try {
					if (index == commandInterceptors.length) {
						return cmd.execute(request);
					} else {
						return commandInterceptors[index].intercept(this);
					}
				} finally {
					index--;
				}
			} else {
				return null;
			}
		} catch (RuntimeException e) {
			if (cmd instanceof ExceptionAware && index == -1) {
				return ((ExceptionAware) cmd).handleException(e, request);
			} else {
				throw e;
			}
		}
	}

	public RequestContext getRequest() {
		return request;
	}

	public Command getCommand() {
		if (cmd != null) {
			return cmd;
		} else {
			return null;
		}
	}

	@Override
	public String getCommandName() {
		return cmdName;
	}

}
