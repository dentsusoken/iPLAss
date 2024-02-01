/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.command.async;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.async.AsyncTaskOption;
import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.command.async.AsyncRequestContext;
import org.iplass.mtp.command.async.ResultHandler;
import org.iplass.mtp.command.async.definition.AsyncCommandDefinition;
import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.impl.command.InterceptorService;
import org.iplass.mtp.impl.command.InvocationImpl;
import org.iplass.mtp.impl.command.MetaCommand;
import org.iplass.mtp.impl.command.MetaCommand.CommandRuntime;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;

public class MetaAsyncCommand extends BaseRootMetaData implements DefinableMetaData<AsyncCommandDefinition> {
	private static final long serialVersionUID = -7930313189566333668L;
	public static final String ASYNC_INTERCEPTOR_NAME = "async";

	private String queue;
	private String groupingKeyAttributeName;
	private ExceptionHandlingMode exceptionHandlingMode;
	private MetaCommand command;

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getGroupingKeyAttributeName() {
		return groupingKeyAttributeName;
	}

	public void setGroupingKeyAttributeName(String groupingKeyAttributeName) {
		this.groupingKeyAttributeName = groupingKeyAttributeName;
	}

	public ExceptionHandlingMode getExceptionHandlingMode() {
		return exceptionHandlingMode;
	}

	public void setExceptionHandlingMode(ExceptionHandlingMode exceptionHandlingMode) {
		this.exceptionHandlingMode = exceptionHandlingMode;
	}

	public MetaCommand getCommand() {
		return command;
	}

	public void setCommand(MetaCommand command) {
		this.command = command;
	}

	@Override
	public AsyncCommandRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new AsyncCommandRuntime();
	}

	@Override
	public MetaAsyncCommand copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(AsyncCommandDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		description = definition.getDescription();
		exceptionHandlingMode = definition.getExceptionHandlingMode();
		groupingKeyAttributeName = definition.getGroupingKeyAttributeName();
		queue = definition.getQueue();
		if (definition.getCommandConfig() == null) {
			command = null;
		} else {
			command = MetaCommand.createInstance(definition.getCommandConfig());
			command.applyConfig(definition.getCommandConfig());
		}
	}

	public AsyncCommandDefinition currentConfig() {
		AsyncCommandDefinition def = new AsyncCommandDefinition();
		def.setName(name);
		def.setDisplayName(displayName);
		def.setDescription(description);
		def.setExceptionHandlingMode(exceptionHandlingMode);
		def.setGroupingKeyAttributeName(groupingKeyAttributeName);
		def.setQueue(queue);
		if (command != null) {
			def.setCommandConfig(command.currentConfig());
		}
		return def;
	}

	public class AsyncCommandRuntime extends BaseMetaDataRuntime {

		private AsyncTaskManager atm = ManagerLocator.getInstance().getManager(AsyncTaskManager.class);
		private CommandRuntime cmd;
		private CommandInterceptor[] cmdInterceptors;

		public AsyncCommandRuntime() {
			try {
				if (command != null) {
					cmd = command.createRuntime();
				}

				InterceptorService is = ServiceRegistry.getRegistry().getService(InterceptorService.class);
				cmdInterceptors = is.getInterceptors(ASYNC_INTERCEPTOR_NAME);

			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		public CommandRuntime getCommandRuntime() {
			return cmd;
		}

		@Override
		public MetaAsyncCommand getMetaData() {
			return MetaAsyncCommand.this;
		}

		public long executeAsync(AsyncRequestContext request, ResultHandler resultHandler) {

			AsyncCommandCallable task = new AsyncCommandCallable(getName(), request, resultHandler);
			AsyncTaskOption option = new AsyncTaskOption();
			option.setQueue(queue);
			if (exceptionHandlingMode != null) {
				option.setExceptionHandlingMode(exceptionHandlingMode);
			}
			if (groupingKeyAttributeName != null) {
				option.setGroupingKey(request.getAttribute(groupingKeyAttributeName).toString());
			}

			return atm.execute(option, task).getTaskId();
		}

		public String executeCommand(final AsyncRequestContext req, final ResultHandler resultHandler) {
			checkState();

			//command行＆result処理。
			String res = new InvocationImpl(cmdInterceptors, cmd, req).proceedCommand();
			if (resultHandler != null) {
				resultHandler.handle(res);
			}
			return res;
		}

	}

}
