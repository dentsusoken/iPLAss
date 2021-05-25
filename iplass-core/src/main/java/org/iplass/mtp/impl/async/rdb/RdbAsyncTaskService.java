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

package org.iplass.mtp.impl.async.rdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.iplass.mtp.async.AsyncTaskFuture;
import org.iplass.mtp.async.AsyncTaskOption;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.impl.async.AsyncTaskContextImpl;
import org.iplass.mtp.impl.async.AsyncTaskRuntimeException;
import org.iplass.mtp.impl.async.AsyncTaskService;
import org.iplass.mtp.impl.async.ExceptionHandleable;
import org.iplass.mtp.impl.async.rdb.workers.LocalWorker;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionListener;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class RdbAsyncTaskService extends AsyncTaskService {

	private static Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal.async.rdb");

	//QueueテーブルはRDB物理テーブル。
	//ワーカーはすべてのテナントで共通利用する。
	//実行履歴を保存するかは、このレイヤーでなく上位のレイヤー（たとえばワークフロー履歴、バッチ実行履歴）で行う。履歴保存先としてはEntity。

	//taskId,visibletime,status,tenantId,queuename,groupid,groupseq,vnode,updatetime,task,hasResult,taskResult

	//groupid,groupseqでユニークキー制約
	//レコード、消すタイミング、taskResultをgetした時。呼び元のトランザクション内で。
	//キューと、履歴（実行後のレコード）を分けるか？キューにたまりすぎるとパフォーマンス劣化する？実行後、いつまでデータ（taskResult）を保持するか？

	private long defaultGetResultTimeoutMillis = 60000;
	private long initialGetResultIntervalMillis = 100;

	private RdbQueueService queueService;

	public long getDefaultGetResultTimeoutMillis() {
		return defaultGetResultTimeoutMillis;
	}

	@Override
	public void init(Config config) {
		queueService = config.getDependentService(RdbQueueService.class);

		if (config.getValue("defaultGetResultTimeoutMillis") != null) {
			defaultGetResultTimeoutMillis = Long.parseLong(config.getValue("defaultGetResultTimeoutMillis"));
		}
	}

	@Override
	public void destroy() {
	}

	public RdbQueueService getQueueService() {
		return queueService;
	}

	@Override
	public <V> Future<V> execute(Callable<V> task) {
		return execute(task, new AsyncTaskOption(), true);
	}

	@Override
	public <V> AsyncTaskFuture<V> execute(final Callable<V> task, AsyncTaskOption option, boolean inheritAuthContext) {
		if (!(task instanceof Serializable)) {
			throw new AsyncTaskRuntimeException("AsyncTask's callable must implements Serializable:" + task.getClass());
		}

		Queue q = queueService.getQueue(option.getQueue());
		if (q == null) {
			throw new AsyncTaskRuntimeException("queue:" + option.getQueue() + " not defined.");
		}

		Task newTask = null;
		AuthContextHolder ach = AuthContextHolder.getAuthContext();
		if (ach.isSecuredAction() && inheritAuthContext) {
			newTask = new Task(new CallableInput<>(task, ach.getUserContext(), ach.isPrivileged(), MDC.get(ExecuteContext.MDC_TRACE_ID)), option.getGroupingKey(), option.getExceptionHandlingMode(), option.isReturnResult(), option.getExecutionTime());
		} else {
			newTask = new Task(new CallableInput<>(task, null, false, MDC.get(ExecuteContext.MDC_TRACE_ID)), option.getGroupingKey(), option.getExceptionHandlingMode(), option.isReturnResult(), option.getExecutionTime());
		}
		WorkerWakeUpCaller callback = null;
		if (q.getConfig().getWorker().isWakeupOnSubmit()) {
			callback = new WorkerWakeUpCaller();
		}
		q.submit(newTask, option.getStartMode(), callback);
		return new RdbAsyncTaskFuture<>(newTask, q, defaultGetResultTimeoutMillis, initialGetResultIntervalMillis);
	}

	@Override
	public <V> AsyncTaskFuture<V> getResult(long taskId, String queueName) {
		Queue q = queueService.getQueue(queueName);
		if (q == null) {
			throw new AsyncTaskRuntimeException("queue:" + queueName + " not defined.");
		}
		Task task = new Task();
		task.setTenantId(ExecuteContext.getCurrentContext().getClientTenantId());
		task.setTaskId(taskId);
		task.setStatus(TaskStatus.UNKNOWN);
		return new RdbAsyncTaskFuture<>(task, q, defaultGetResultTimeoutMillis, initialGetResultIntervalMillis);
	}

	private static class WorkerWakeUpCaller implements TaskSubmitListener {
		private static final String WID_LIST = "mtp.async.rdb.workerWakeUpCaller";
		private static Random rand = new Random(System.currentTimeMillis());

		private Task task;
		private Queue queue;

		private WorkerWakeUpCaller() {
		}
		@Override
		public void setContext(Task task, Queue queue) {
			this.task = task;
			this.queue = queue;
		}
		@SuppressWarnings("unchecked")
		@Override
		public void afterCommit(Transaction t) {
			//複数回起動しないように
			int workerId = queue.resolveActualWorkerId(task.getVirtualWorkerId());
			List<Integer> widList = (List<Integer>) t.getAttribute(WID_LIST);
			if (widList == null) {
				widList = new ArrayList<>();
				t.setAttribute(WID_LIST, widList);
			}
			if (!widList.contains(workerId)) {
				Worker w = null;
				if (workerId == -1) {
					//ランダムで選択、LocalWorker優先
					widList.add(workerId);

					workerId = rand.nextInt(queue.getConfig().getWorker().getActualWorkerSize());
					for (int i = 0; i < queue.getConfig().getWorker().getActualWorkerSize(); i++) {
						int cwid = (workerId + i) % queue.getConfig().getWorker().getActualWorkerSize();
						Worker cw = queue.getWorker(cwid);
						if (cw instanceof LocalWorker) {
							w = cw;
							workerId = cwid;
							break;
						}
					}
					if (w == null) {
						w = queue.getWorker(workerId);
					}
				} else {
					w = queue.getWorker(workerId);
				}
				w.wakeup();
				widList.add(workerId);
			}
		}
		@Override
		public void afterRollback(Transaction t) {
		}
	}

	private static class RdbAsyncTaskFuture<V> implements AsyncTaskFuture<V> {

		private Task task;
		private Queue q;
		private long defaultGetResultTimeoutMillis;
		private long initialGetResultIntervalMillis;

		private RdbAsyncTaskFuture(Task task, Queue q, long defaultGetResultTimeoutMillis, long initialGetResultIntervalMillis) {
			this.task = task;
			this.q = q;
			this.defaultGetResultTimeoutMillis = defaultGetResultTimeoutMillis;
			this.initialGetResultIntervalMillis = initialGetResultIntervalMillis;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			try {
				Task reload = q.peek(task.getTaskId(), true, true);
				if (q.taskAbort(reload, mayInterruptIfRunning, null, true, true)) {
					if (reload != null && reload.getCallable().getActual() instanceof ExceptionHandleable) {
						AsyncTaskContextImpl asyncTaskContext = new AsyncTaskContextImpl(task.getTaskId(), q.getName());
						String prevTraceId = MDC.get(ExecuteContext.MDC_TRACE_ID);
						ExecuteContext ec = ExecuteContext.getCurrentContext();
						try {
							ec.setAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME, asyncTaskContext, false);
							
							if (reload.getCallable().getTraceId() != null) {
								ec.mdcPut(ExecuteContext.MDC_TRACE_ID, reload.getCallable().getTraceId());
							}
							
							((ExceptionHandleable) reload.getCallable().getActual()).canceled();
						} catch (Throwable e) {
							fatalLogger.warn("ExceptionHandleable's canceled() call failed.(queue:" + q.getConfig().getName() + ", task:" + reload.getTaskId() + ",tenantId:" + reload.getTenantId() + ")", e);
							//cancelの場合は、ステータスは更新する
							Transaction tran = Transaction.getCurrent();
							if (tran != null && tran.getStatus() == TransactionStatus.ACTIVE && tran.isRollbackOnly()) {
								tran.addTransactionListener(new TransactionListener() {
									@Override
									public void afterRollback(Transaction tt) {
										q.taskAbort(reload, mayInterruptIfRunning, null, true, true);
									}
								});
							}
						} finally {
							ec.mdcPut(ExecuteContext.MDC_TRACE_ID, prevTraceId);
							ec.removeAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME);
						}
					}
					return true;
				} else {
					return false;
				}
			} catch (EntityConcurrentUpdateException e) {
				return false;
			}
		}

		private void reload() {
			if (task.getStatus() != TaskStatus.COMPLETED
					&& task.getStatus() != TaskStatus.ABORTED) {
				Task reload = q.peek(task.getTaskId(), false, true);
				if (reload != null) {
					this.task = reload;
				}
			}
		}

		@Override
		public boolean isCancelled() {
			reload();
			return task.getStatus() == TaskStatus.ABORTED;
		}

		@Override
		public boolean isDone() {
			reload();
			return task.getStatus() == TaskStatus.COMPLETED
					|| task.getStatus() == TaskStatus.ABORTED
					|| task.getStatus() == TaskStatus.RETURNED;
		}

		@Override
		public V get() throws InterruptedException, ExecutionException {
			try {
				return get(-1, null);
			} catch (TimeoutException e) {
				throw new ExecutionException(e);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public V get(long timeout, TimeUnit unit) throws InterruptedException,
				ExecutionException, TimeoutException {
			if (task.getStatus() != TaskStatus.COMPLETED
					&& task.getStatus() != TaskStatus.ABORTED) {
				if (timeout < 0) {
					timeout = defaultGetResultTimeoutMillis;
					unit = TimeUnit.MILLISECONDS;
				}

				long remainTime = unit.toMillis(timeout);
				long interval = initialGetResultIntervalMillis;
				Task getTask = null;
				while (remainTime >= 0) {
					try {
						getTask = q.pullResult(task.getTaskId());
					} catch (Exception e) {
						throw new ExecutionException(e);
					}
					if (getTask != null) {
						break;
					} else {
						Thread.sleep(interval);
						remainTime -= interval;
						interval = interval * 2;
					}
				};
				if (getTask == null) {
					throw new TimeoutException("RdbAsyncTask.get operation timeout.queueName:" + q.getName() + ", taskId:" + task.getTaskId());
				}

				task = getTask;
			}

			Object res = task.getResult();
			if (task.getStatus() == TaskStatus.ABORTED
					&& res instanceof Throwable) {
				throw new ExecutionException((Throwable) res);
			}

			return (V) res;
		}

		@Override
		public long getTaskId() {
			return task.getTaskId();
		}

		@Override
		public TaskStatus getStatus() {
			reload();
			return task.getStatus();
		}

		@Override
		public String getQueueName() {
			return q.getName();
		}

	}

}
