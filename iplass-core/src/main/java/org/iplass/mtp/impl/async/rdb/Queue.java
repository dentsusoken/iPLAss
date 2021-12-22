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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.async.StartMode;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.async.TaskTimeoutException;
import org.iplass.mtp.impl.async.AsyncTaskContextImpl;
import org.iplass.mtp.impl.async.AsyncTaskRuntimeException;
import org.iplass.mtp.impl.async.ExceptionHandleable;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.core.config.ServerEnv;
import org.iplass.mtp.impl.counter.CounterService;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionListener;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Queue {
	private static final String KEY_NEVER_START = "mtp.asynctask.neverstartasynctask";

	private static Logger mtpLogger = LoggerFactory.getLogger("mtp.asynctask");
	private static Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal.asynctask");
	private static Logger logger = LoggerFactory.getLogger(Queue.class);

	private final QueueConfig config;
	private final TaskDao dao;
	private final CounterService counter;
	private final CounterService counterForGroup;
	private final int[][] workerIdMap;

	private final Worker[] workers;
	
	public Queue(QueueConfig config, CounterService counter, CounterService counterForGroup, RdbAdapter rdb) {
		this(config, counter, counterForGroup, rdb, null);
	}

	public Queue(QueueConfig config, CounterService counter, CounterService counterForGroup, RdbAdapter rdb, WorkerFactory workerFactory) {
		this.config = config;
		this.counter = counter;
		this.counterForGroup = counterForGroup;
		dao = new TaskDao(rdb);

		workerIdMap = new int[config.getWorker().getActualWorkerSize()][];
		if (config.getWorker().getActualWorkerSize() > 0) {
			int mod = config.getWorker().getVirtualWorkerSize() % config.getWorker().getActualWorkerSize();
			int length = config.getWorker().getVirtualWorkerSize() / config.getWorker().getActualWorkerSize();
			for (int i = 0; i < workerIdMap.length; i++) {
				if (i < mod) {
					workerIdMap[i] = new int[length + 1];
				} else {
					workerIdMap[i] = new int[length];
				}
			}
			int index = 0;
			for (int i = 0; i < workerIdMap[0].length; i++) {
				for (int j = 0; j < config.getWorker().getActualWorkerSize(); j++) {
					if (workerIdMap[j].length > i) {
						workerIdMap[j][i] = index;
					}
					index++;
				}
			}
		}

		workers = new Worker[config.getWorker().getActualWorkerSize()];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = workerFactory.createWorker(this, i);
		}
	}

	public void startWorker() {
		for (Worker w: workers) {
			w.start();
		}
	}

	public void stopWorker() {
		for (Worker w: workers) {
			w.stop();
		}
	}

	public QueueConfig getConfig() {
		return config;
	}

	public Worker getWorker(int workerId) {
		return workers[workerId];
	}

	private void genVirtualWorkerId(Task task) {
		if (task.getGroupingKey() == null) {
			if (config.isSelectWorkerOnSubmit()) {
				//tenantId,taskId
				final int prime = 31;
				int result = 1;
				result = prime * result + task.getTenantId();
				result = prime * result + (int) (task.getTaskId() ^ (task.getTaskId() >>> 32));
				task.setVirtualWorkerId(Math.abs(result) % config.getWorker().getVirtualWorkerSize());
			} else {
				task.setVirtualWorkerId(-1);
			}
		} else {
			//tenantId,groupingKey
			final int prime = 31;
			int result = 1;
			result = prime * result + task.getTenantId();
			result = prime * result
					+ task.getGroupingKey().hashCode();
			task.setVirtualWorkerId(Math.abs(result) % config.getWorker().getVirtualWorkerSize());
		}
	}

	public void submit(final Task task, StartMode mode, final TaskSubmitListener callback) {
		final ExecuteContext exec = ExecuteContext.getCurrentContext();
		task.setTenantId(exec.getClientTenantId());
		task.setQueueId(config.getId());
		long ct = System.currentTimeMillis();
		if (ct > task.getVisibleTime()) {
			task.setVisibleTime(ct);
		}
		task.setStatus(TaskStatus.SUBMITTED);
		task.setVersion(0);
		task.setServerId(ServerEnv.getInstance().getServerId());

		if (task.getGroupingKey() != null && config.isStrictSequence()) {
			//groupingKeyが設定されていて、strictSequence=true場合は、タスクIDはシーケンシャルに（キャッシュ利用しない）。
			task.setTaskId(counterForGroup.increment(exec.getClientTenantId(), config.getName(), 0));
		} else {
			task.setTaskId(counter.increment(exec.getClientTenantId(), config.getName(), 0));
		}

		genVirtualWorkerId(task);

		switch (mode) {
		case IMMEDIATELY:
			//別トランザクションで
			Transaction.requiresNew(t -> {
					dao.insert(task);
					if (callback != null) {
						callback.setContext(task, Queue.this);
						t.addTransactionListener(callback);
					}
			});
			break;
		case AFTER_COMMIT:
			//呼び出し元でトランザクション開始されてる前提
			dao.insert(task);
			if (callback != null) {
				Transaction t = Transaction.getCurrent();
				if (t.getStatus() == TransactionStatus.ACTIVE) {
					callback.setContext(task, Queue.this);
					t.addTransactionListener(callback);
					addNeverStartId(getName(), task.getTaskId(), t);
				} else {
					throw new IllegalStateException("Queue#submit() call must in Transaction");
				}
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void addNeverStartId(String queueName, long taskId, Transaction t) {
		List<Long> l = (List<Long>) t.getAttribute(KEY_NEVER_START + "." + queueName);
		if (l == null) {
			l = new ArrayList<>();
			t.setAttribute(KEY_NEVER_START + "." + queueName, l);
		}
		l.add(taskId);
	}

	@SuppressWarnings("unchecked")
	private boolean isNeverStartId(String queueName, long taskId, Transaction t) {
		List<Long> l = (List<Long>) t.getAttribute(KEY_NEVER_START + "." + queueName);
		if (l != null) {
			if (l.contains(taskId)) {
				return true;
			}
		}
		return false;
	}

	public int resolveActualWorkerId(int virtualWorkerId) {
		if (virtualWorkerId == -1) {
			return -1;
		} else {
			return virtualWorkerId % config.getWorker().getActualWorkerSize();
		}
	}

	private String getServerId(boolean localOnly) {
		if (localOnly) {
			return ServerEnv.getInstance().getServerId();
		} else {
			return null;
		}
	}

	public synchronized Task poll(final int myWorkerId, boolean localOnly) {
		//wait loopは呼び出し側で

		//トランザクションは、REQUIRES
		//myWorkerIdから、先頭のタスク1件取得。
		//検索したTaskが、EXECUTINGだったら、タイムアウトと判断し、再実行フラグがtrueなら再実行、falseならabort
		//ステータスをEXECUTING、visibletimeを現在時間+タイムアウトに更新。そしてLOAD
		//Taskへんきゃく。

		if (myWorkerId >= workerIdMap.length) {
			throw new IllegalArgumentException("requested workerId:" + myWorkerId + " is out of range.ActualWorkerSize is " + config.getWorker().getActualWorkerSize());
		}

		List<Task> tlist = dao.searchForPoll(
				config.getId(),
				workerIdMap[myWorkerId],
				System.currentTimeMillis(),
				getServerId(localOnly),
				config.getWorker().getMaxRetryCount()
				);
		if (tlist.size() == 0) {
			return null;
		}
		HashSet<String> excludeGroupingKeys = new HashSet<>();
		for (Task t: tlist) {
			//GroupingKey specified
			if (t.getGroupingKey() != null) {
				//check previous is executing
				if (excludeGroupingKeys.contains(t.getGroupingKey())) {
					continue;
				} else if (dao.countPreExecuting(
						ExecuteContext.getCurrentContext().getClientTenantId(),
						config.getId(),
						t.getGroupingKey(),
						t.getTaskId(),
						getServerId(localOnly)
						) > 0) {
					excludeGroupingKeys.add(t.getGroupingKey());
					continue;
				}
			}

			//check executionTimeout
			if (t.getStatus() == TaskStatus.EXECUTING) {
				switch (t.getExceptionHandlingMode()) {
				case RESTART:
					mtpLogger.warn("queue:" + getName() + "'s task:" + t.getTaskId() + "(tenantId:" + t.getTenantId() + ") is timeout. so re-run task.");
					t.setRetryCount(t.getRetryCount() + 1);
					break;
				case ABORT:
				case ABORT_LOG_FATAL:
					final Task loaded = dao.load(t.getTenantId(), t.getQueueId(), t.getTaskId(), true, false, true);
					if (taskAbort(loaded, true, new TaskTimeoutException(), false, true)) {
						mtpLogger.error("queue:" + getName() + "'s task:" + t.getTaskId() + "(tenantId:" + t.getTenantId() + ") is timeout.so abort task.");
						if (t.getExceptionHandlingMode() == ExceptionHandlingMode.ABORT_LOG_FATAL) {
							fatalLogger.error("queue:" + getName() + "'s task:" + t.getTaskId() + "(tenantId:" + t.getTenantId() + ") is timeout.so abort task.");
						}
						try {
							if (loaded.getCallable().getActual() instanceof ExceptionHandleable) {
								ExecuteContext.executeAs(ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(t.getTenantId()), new Executable<Void>() {
									@Override
									public Void execute() {
										AsyncTaskContextImpl asyncTaskContext = new AsyncTaskContextImpl(loaded.getTaskId(), getName());
										ExecuteContext ec = ExecuteContext.getCurrentContext();
										ec.setAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME, asyncTaskContext, false);
										try {
											if (loaded.getCallable().getTraceId() != null) {
												ec.mdcPut(ExecuteContext.MDC_TRACE_ID, loaded.getCallable().getTraceId());
											}
											((ExceptionHandleable) loaded.getCallable().getActual()).timeouted();
											
										} finally {
											ec.mdcPut(ExecuteContext.MDC_TRACE_ID, null);
										}
										return null;
									}
								});
							}
						} catch (Throwable ee) {
							fatalLogger.error("ExceptionHandleable's timeouted() call failed(queue:" + getConfig().getName() + ", task:" + loaded.getTaskId() + ",tenantId:" + loaded.getTenantId() + ")", ee);
							Transaction tran = Transaction.getCurrent();
							//timeoutの場合は、ステータスは更新する
							if (tran != null && tran.getStatus() == TransactionStatus.ACTIVE && tran.isRollbackOnly()) {
								tran.addTransactionListener(new TransactionListener() {
									@Override
									public void afterRollback(Transaction tt) {
										taskAbort(loaded, true, new TaskTimeoutException(), false, true);
									}
								});
							}
						}
					}
					continue;//for loop's continue
				default:
					break;
				}
			}

			//update status & visibleTime
			t.setStatus(TaskStatus.EXECUTING);
			t.setVisibleTime(System.currentTimeMillis() + config.getWorker().getExecutionTimeout() + config.getWorker().getRestartDelay());

			dao.update(t);

			return dao.load(t.getTenantId(), t.getQueueId(), t.getTaskId(), true, false, false);
		}

		return null;
	}

	public void taskFinish(Task task) {

		//トランザクションはREQUIRES（タスクの実行と同じトランザクションに）

		//別プロセスがすでに更新（Abort、別ワーカが再実行）してた場合は、setRollbackOnly
		//ステータスをCOMPLETEに

		if (task.isReturnResult()) {
			task.setStatus(TaskStatus.RETURNED);
			dao.update(task);
		} else {
			dao.moveToHistory(task, TaskStatus.COMPLETED);
		}
	}

	public boolean taskAbort(Task task, boolean mayInterruptIfRunning, Throwable cause, boolean callCancel, boolean withTSCheck) {
		//mayInterruptIfRunningは意味をなさない。。

		//トランザクションはREQUIRES（呼び出し元と同じトランザクションに）
		//ステータスをABORTEDに
		Task loaded = dao.load(task.getTenantId(), task.getQueueId(), task.getTaskId(), true, false, true);
		if (loaded != null) {
			if (withTSCheck) {
				if (!loaded.getUpdateTime().equals(task.getUpdateTime())) {
					return false;
				}
			}

			switch (loaded.getStatus()) {
			case SUBMITTED:
			case EXECUTING:
				if (cause != null) {
					//causeをセット
					//causeがnot serializableの場合がある。。。
					try {
						checkSerialize(cause);
					} catch (Exception e) {
						logger.debug("Can't serialize abort cause exception instance:" + cause);
						e.addSuppressed(new NotSerializableException(cause.toString()));
						cause = e;
					}

					loaded.setResult(cause);
					loaded.setStatus(TaskStatus.ABORTED);
					dao.update(loaded);
					loaded = dao.load(task.getTenantId(), task.getQueueId(), task.getTaskId(), true, false, true);
				}
				if (callCancel || !loaded.isReturnResult()) {
					//明示的なcancelコールか、結果取得がない場合は履歴に移動
					//lockしてるので、必ず成功するはず
					dao.moveToHistory(loaded, TaskStatus.ABORTED);
				}
				return true;
			default:
				break;
			}
		}
		return false;
	}

	private void checkSerialize(Object obj) throws NotSerializableException {
		if (obj != null) {
			try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
				out.writeObject(obj);
			} catch (NotSerializableException e) {
				throw e;
			} catch (IOException e) {
				//多分発生し得ない
				throw new RuntimeException(e);
			}
		}
	}


	public Task pullResult(long taskId) {
		//トランザクションはREQUIRES（呼び出し元と同じトランザクションに）
		//ステータスをCOMPLETEに

		Transaction t = Transaction.getCurrent();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
			//同一トランザクションでsubmitした場合は、永遠に開始しないのでチェック
			if (isNeverStartId(getName(), taskId, t)) {
				throw new AsyncTaskRuntimeException("never start async task, because AsyncTaskStartMode is AFTER_COMMIT");
			}
		}


		Task task = dao.load(ExecuteContext.getCurrentContext().getClientTenantId(),
				config.getId(), taskId, false, false, false);
		if (task == null) {
			//すでに完了済み、or 存在しないタスク
			Task unkown = new Task();
			unkown.setQueueId(config.getId());
			unkown.setTaskId(taskId);
			unkown.setStatus(TaskStatus.UNKNOWN);
			return unkown;
		}


		if (task.getStatus() == TaskStatus.RETURNED
				|| task.getStatus() == TaskStatus.ABORTED) {
			//ロック＆ダブルチェック
			task = dao.load(ExecuteContext.getCurrentContext().getClientTenantId(),
					config.getId(), taskId, true, false, true);
			if (task.getStatus() == TaskStatus.RETURNED) {
				dao.moveToHistory(task, TaskStatus.COMPLETED);
				task.setStatus(TaskStatus.COMPLETED);
				return task;
			} else if (task.getStatus() == TaskStatus.ABORTED) {
				dao.moveToHistory(task, TaskStatus.ABORTED);
				task.setStatus(TaskStatus.ABORTED);
				return task;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public Task peek(long taskId, boolean withBinary, boolean withHistory) {
		return dao.load(ExecuteContext.getCurrentContext().getClientTenantId(), config.getId(), taskId, withBinary, withHistory, false);
	}

	public String getName() {
		return config.getName();
	}

	public List<Task> search(TaskSearchCondition cond) {
		cond.setQueueId(config.getId());
		return dao.search(cond);
	}

	public void moveNoGetResultTaskToHistory() {

		TaskSearchCondition cond = new TaskSearchCondition();
		cond.setStatus(TaskStatus.RETURNED);

		Timestamp dateBefore = new Timestamp(System.currentTimeMillis() - config.getResultRemainingTime());
		cond.setUpdateDate(dateBefore);

		List<Task> list = search(cond);
		for (Task t: list) {
			final Task target = t;
			Task pulled = Transaction.requiresNew(tran -> {
					return pullResult(target.getTaskId());
			});

			if (pulled != null && pulled.getStatus() != TaskStatus.UNKNOWN) {
				mtpLogger.warn("async task(" + "queue:" + getName() + ",tenantId:" + pulled.getTenantId() + ",taskId:" + pulled.getTaskId() + ") waiting get result timeout. so move to history");
			}
		}
	}

	public void forceDelete(long taskId) {
		Task t = dao.load(ExecuteContext.getCurrentContext().getClientTenantId(), config.getId(), taskId, false, false, false);
		if (t == null) {
			mtpLogger.warn("probably async task(" + "queue:" + getName() + ",taskId:" + taskId + ") has been moved to the history. so skip force delete");
		} else {
			dao.delete(t);
		}
	}

}
