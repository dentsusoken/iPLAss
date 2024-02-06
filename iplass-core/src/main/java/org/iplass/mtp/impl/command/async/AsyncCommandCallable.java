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

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.iplass.mtp.async.AsyncTaskContext;
import org.iplass.mtp.async.TaskCancelException;
import org.iplass.mtp.async.TaskTimeoutException;
import org.iplass.mtp.command.CommandRuntimeException;
import org.iplass.mtp.command.async.AsyncRequestConstants;
import org.iplass.mtp.command.async.AsyncRequestContext;
import org.iplass.mtp.command.async.ResultHandler;
import org.iplass.mtp.impl.async.ExceptionHandleable;
import org.iplass.mtp.impl.command.async.MetaAsyncCommand.AsyncCommandRuntime;
import org.iplass.mtp.spi.ServiceRegistry;

public class AsyncCommandCallable implements Callable<Void>, ExceptionHandleable, Serializable {
	private static final long serialVersionUID = -6321493231703224578L;

	private final AsyncRequestContext request;
	private final String asyncTaskCmdName;//idではなく、あえてnameで
	private final ResultHandler resultHandler;

	private transient String result;

	public AsyncCommandCallable(String asyncTaskCmdName, AsyncRequestContext request, ResultHandler resultHandler) {
		this.asyncTaskCmdName = asyncTaskCmdName;
		this.request = request;
		this.resultHandler = resultHandler;
	}


	@Override
	public Void call() throws Exception {

		AsyncCommandRuntime runtime = ServiceRegistry.getRegistry().getService(AsyncCommandService.class).getRuntimeByName(asyncTaskCmdName);
		if (runtime == null) {
			throw new CommandRuntimeException("AsyncCommand:" + asyncTaskCmdName + " not found");
		}

		AsyncTaskContext atc = AsyncTaskContext.getCurrentContext();
		if (atc != null) {
			request.setAttribute(AsyncRequestConstants.TASK_ID, atc.getTaskId());
			request.setAttribute(AsyncRequestConstants.QUEUE, atc.getQueueName());
		}

		result = runtime.executeCommand(request, resultHandler);
		return null;
	}


	@Override
	public String toString() {
		return "AsyncCommand [name=" + asyncTaskCmdName
				+ ", result=" + result + "]";
	}


	@Override
	public void aborted(Throwable cause) {
		if (resultHandler != null) {
			resultHandler.handle(cause);
		}
	}


	@Override
	public void timeouted() {
		if (resultHandler != null) {
			resultHandler.handle(new TaskTimeoutException("timeout"));
		}
	}


	@Override
	public void canceled() {
		if (resultHandler != null) {
			resultHandler.handle(new TaskCancelException("cancel"));
		}
	}

}
