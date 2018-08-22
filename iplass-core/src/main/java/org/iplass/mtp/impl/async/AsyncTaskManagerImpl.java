/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.iplass.mtp.async.AsyncTaskFuture;
import org.iplass.mtp.async.AsyncTaskInfo;
import org.iplass.mtp.async.AsyncTaskInfoSearchCondtion;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.async.AsyncTaskOption;
import org.iplass.mtp.impl.async.rdb.Queue;
import org.iplass.mtp.impl.async.rdb.RdbAsyncTaskService;
import org.iplass.mtp.impl.async.rdb.Task;
import org.iplass.mtp.impl.async.rdb.TaskSearchCondition;
import org.iplass.mtp.impl.async.thread.ThreadingAsyncTaskService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;

public class AsyncTaskManagerImpl implements AsyncTaskManager {

	private ThreadingAsyncTaskService localThreadService;
	private AsyncTaskService queueService;

	public AsyncTaskManagerImpl() {
		ServiceRegistry sr = ServiceRegistry.getRegistry();
		localThreadService = (ThreadingAsyncTaskService) sr.getService(AsyncTaskService.class);//use default threading AsyncTaskService
		queueService = sr.getService(AsyncTaskService.DEFAULT_QUEUE_BASE_ASYNC_TASK_SERVICE);
	}

	@Override
	public <V> Future<V> executeOnThread(Callable<V> task) {
		return localThreadService.execute(task);
	}

	@Override
	public <V> AsyncTaskFuture<V> execute(Callable<V> task) {
		return execute(new AsyncTaskOption(), task);
	}

	@Override
	public <V> AsyncTaskFuture<V> execute(AsyncTaskOption option, Callable<V> task) {
		if (option.getQueue() != null && option.getQueue().equals(AsyncTaskOption.LOCAL_THREAD_QUEUE_NAME)) {
			return localThreadService.execute(task, option, true);
		} else {
			return queueService.execute(task, option, true);
		}
	}

	@Override
	public <V> AsyncTaskFuture<V> getResult(long taskId, String queueName) {
		return queueService.getResult(taskId, queueName);
	}

	@Override
	public AsyncTaskInfo loadAsyncTaskInfo(long taskId, String queueName) {
		//TODO jmsの場合も考慮する
		Queue q = getQueue(queueName);
		Task task = q.peek(taskId, true, true);
		if (task == null) {
			return null;
		}
		return toInfo(q.getName(), task);
	}

	private AsyncTaskInfo toInfo(String queueName, Task task) {
		AsyncTaskInfo info = new AsyncTaskInfo();
		info.setQueue(queueName);
		info.setTaskId(task.getTaskId());
		info.setGroupingKey(task.getGroupingKey());
		info.setStatus(task.getStatus());
		info.setRetryCount(task.getRetryCount());
		info.setExceptionHandlingMode(task.getExceptionHandlingMode());
		info.setReturnResult(task.isReturnResult());
		if (task.getCallable() != null) {
			info.setTask(task.getCallable().getActual());
		}
		info.setResult(task.getResult());

		return info;
	}

	private Queue getQueue(String queueName) {
		if (queueName == null) {
			queueName = AsyncTaskOption.DEFAULT_QUEUE_NAME;
		}
		if (queueName.equals(AsyncTaskOption.LOCAL_THREAD_QUEUE_NAME)) {
			return null;
		}
		//TODO jmsの場合も考慮する
		Queue q = ((RdbAsyncTaskService) queueService).getQueueService().getQueue(queueName);
		if (q == null) {
			throw new IllegalArgumentException("queue:" + queueName + " is not defined.");
		}
		return q;
	}

	@Override
	public List<AsyncTaskInfo> searchAsyncTaskInfo(
			AsyncTaskInfoSearchCondtion cond) {
		//TODO jmsの場合も考慮する
		Queue q = getQueue(cond.getQueue());

		TaskSearchCondition tsc = new TaskSearchCondition();
		tsc.setTenantId(ExecuteContext.getCurrentContext().getClientTenantId());
		tsc.setQueueId(q.getConfig().getId());
		tsc.setTaskId(cond.getTaskId());
		tsc.setStatus(cond.getStatus());
		tsc.setWithHistory(cond.isWithHistory());
		tsc.setGroupingKey(cond.getGroupingKey());
		tsc.setRetryCount(cond.getRetryCount());
		tsc.setReturnResult(cond.getReturnResult());
		tsc.setLimit(cond.getLimit());
		tsc.setOffset(cond.getOffset());

		List<Task> taskList = q.search(tsc);
		List<AsyncTaskInfo> result = new ArrayList<>(taskList.size());
		for (Task t: taskList) {
			result.add(toInfo(q.getName(), t));
		}
		return result;
	}

	@Override
	public void forceDelete(long taskId, String queueName) {
		//TODO jmsの場合も考慮する
		Queue q = getQueue(queueName);
		if (q != null) {
			q.forceDelete(taskId);
		}
	}
}
