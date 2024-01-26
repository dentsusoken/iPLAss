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
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.command.MetaCommand.CommandRuntime;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.TransactionOption;


public class CompositeCommand implements Command {
	
	private Command[] commands;
	
	private Script executeRule;
	private Command[] proxied;
	
	public CompositeCommand() {
	}
	
	public CompositeCommand(CommandRuntime[] crs, Script executeRule) {
		this.executeRule = executeRule;
		
		InterceptorService is = ServiceRegistry.getRegistry().getService(InterceptorService.class);
		commands = new Command[crs.length];
		proxied = new Command[crs.length];
		for (int i = 0; i < proxied.length; i++) {
			commands[i] = crs[i].newCommand();
			proxied[i] = new CommandProxy(crs[i].name(), commands[i], crs[i].getTransactionOption(), is);
		}
	}
	
	public Command[] getCommands() {
		return commands;
	}

	public String execute(RequestContext request) {

		if (executeRule == null) {
			//TODO 最後のCommandの結果しか返していないが、よいか？
			String result = null;
			for (Command c: proxied) {
				result = c.execute(request);
			}
			return result;
		} else {
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			ScriptContext sc = tc.getScriptEngine().newScriptContext();
			sc.setAttribute(MetaCommand.CMD_BINDING_NAME, proxied);
			sc.setAttribute("request", request);
 			return (String) executeRule.eval(sc);
		}
	}
	
	private static class CommandProxy implements Command {
		
		private String cmdName;
		private Command actual;
		private TransactionOption transactionOption;
		private InterceptorService interceptorService;

		private CommandProxy(String cmdName, Command actual, TransactionOption transactionOption, InterceptorService interceptorService) {
			this.cmdName = cmdName;
			this.actual = actual;
			this.transactionOption = transactionOption;
			this.interceptorService = interceptorService;
		}

		@Override
		public String execute(RequestContext request) {
			InvocationImpl invocation = new InvocationImpl(interceptorService.getInterceptors(CommandInvokerImpl.INVOKER_INTERCEPTOR_NAME), actual, request, transactionOption, cmdName);
			return invocation.proceedCommand();
		}
	}

}
