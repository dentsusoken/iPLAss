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
import org.iplass.mtp.command.CommandInvoker;
import org.iplass.mtp.command.CommandRuntimeException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.async.AsyncRequestContext;
import org.iplass.mtp.command.async.ResultHandler;
import org.iplass.mtp.impl.command.MetaMetaCommand.MetaCommandRuntime;
import org.iplass.mtp.impl.command.async.AsyncCommandService;
import org.iplass.mtp.impl.command.async.MetaAsyncCommand.AsyncCommandRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.TransactionOption;

public class CommandInvokerImpl implements CommandInvoker {

	public static final String COMMAND_META_META_PATH = "/commandClass/";
	public static final String INVOKER_INTERCEPTOR_NAME = "invoker";

	CommandService service = ServiceRegistry.getRegistry().getService(CommandService.class);
	InterceptorService interceptorService = ServiceRegistry.getRegistry().getService(InterceptorService.class);
	AsyncCommandService acs = ServiceRegistry.getRegistry().getService(AsyncCommandService.class);

	@Override
	public String execute(String cmdName, RequestContext request) {
		return execute(cmdName, request, null);
	}

	@Override
	public String execute(String cmdName, RequestContext request, TransactionOption transactionOption) {
		MetaCommandRuntime handler = service.getRuntimeByName(cmdName);
		InvocationImpl invocation = new InvocationImpl(interceptorService.getInterceptors(INVOKER_INTERCEPTOR_NAME), handler, request, transactionOption);
		return invocation.proceedCommand();
	}

	@Override
	public String execute(Command cmd, RequestContext request) {
		return execute(cmd, request, null);
	}

	@Override
	public String execute(Command cmd, RequestContext request, TransactionOption transactionOption) {
		
		CommandClass cc = cmd.getClass().getAnnotation(CommandClass.class);
		String cmdName;
		if (cc != null) {
			cmdName = cc.name();
		} else {
			cmdName = cmd.getClass().getName();
		}

		InvocationImpl invocation = new InvocationImpl(interceptorService.getInterceptors(INVOKER_INTERCEPTOR_NAME), cmd, request, transactionOption, cmdName);
		return invocation.proceedCommand();
	}


	@Override
	public Command getCommandInstance(String cmdName) {
		MetaCommandRuntime handler = service.getRuntimeByName(cmdName);
		Command cmd = handler.newCommand();

		return cmd;
	}

	@Override
	public long executeAsync(String asyncTaskCmdName,
			AsyncRequestContext request) {
		return executeAsync(asyncTaskCmdName, request, null);
	}

	@Override
	public long executeAsync(String asyncTaskCmdName, AsyncRequestContext request, ResultHandler resultHandler) {
		AsyncCommandRuntime runtime = acs.getRuntimeByName(asyncTaskCmdName);
		if (runtime == null) {
			throw new CommandRuntimeException("AsyncCommand:" + asyncTaskCmdName + " not found");
		}
		if (request == null) {
			request = new AsyncRequestContext();
		}
		return runtime.executeAsync(request, resultHandler);
	}

}
