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

package org.iplass.mtp.impl.async.rdb.workers;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.async.TaskTimeoutException;
import org.iplass.mtp.impl.async.AsyncTaskContextImpl;
import org.iplass.mtp.impl.async.ExceptionHandleable;
import org.iplass.mtp.impl.async.rdb.Queue;
import org.iplass.mtp.impl.async.rdb.QueueConfig;
import org.iplass.mtp.impl.async.rdb.Task;
import org.iplass.mtp.impl.async.rdb.Worker;
import org.iplass.mtp.impl.async.rdb.WorkerConfig;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.core.config.ServerEnv;
import org.iplass.mtp.impl.rdb.connection.ResourceHolder;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LocalWorker implements Worker {
	private static Logger logger = LoggerFactory.getLogger(LocalWorker.class);
	private static Logger mtpLogger = LoggerFactory.getLogger("mtp.async.rdb");
	private static Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal.async.rdb");
	
	//Executerが2本。
	//キューからPull&タイムアウト監視用Executer
	//実タスクを実行するExecuter（もしくは別プロセス）
	
	private static Random rand = new Random(ServerEnv.getInstance().getServerId().hashCode());
	
	protected final QueueConfig queueConfig;
	protected final WorkerConfig workerConfig;
	protected final int workerId;
	protected final Queue queue;
	
	protected volatile WorkerState state;
	private ScheduledExecutorService queuePoller;
	
	protected AtomicInteger counter = new AtomicInteger();
	
	public LocalWorker(Queue queue, int workerId) {
		this.queue = queue;
		this.queueConfig = queue.getConfig();
		this.workerConfig = queue.getConfig().getWorker();
		this.workerId = workerId;
		this.state = WorkerState.STOPPED;
	}
	
	@Override
	public void start() {
		if (state != WorkerState.STOPPED) {
			logger.warn(queueConfig.getName() + "'s worker:" + workerId + " is not stopped. so can not (re)start worker...");
			return;
		}
		
		synchronized (this) {
			if (state == WorkerState.STOPPED) {
				SecurityManager s = System.getSecurityManager();
				final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
				queuePoller = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
			            Thread t = new Thread(group, r,
			            		queueConfig.getName() + "-" + workerId + "-poller-" + counter.incrementAndGet(),
                                0);
						if (t.isDaemon()) {
							t.setDaemon(false);
						}
						if (t.getPriority() != Thread.NORM_PRIORITY) {
							t.setPriority(Thread.NORM_PRIORITY);
						}
						return t;
					}
				});
				
				startImpl();
				
				state = WorkerState.STARTED;
				long delay = (long) (workerConfig.getPollingInterval() * rand.nextDouble());
				queuePoller.schedule(new PollingTask(false), delay, TimeUnit.MILLISECONDS);
				mtpLogger.info("worker(id:" + workerId + ") of queue:" + queue.getName() + " is started.");
			} else {
				logger.debug(queueConfig.getName() + "'s worker:" + workerId + " is already stared. may be another thread call start().");
			}
		}
	}
	
	protected abstract void startImpl();
	
	@Override
	public void stop() {
		if (state != WorkerState.STARTED) {
			logger.warn(queueConfig.getName() + "'s worker:" + workerId + " is not stared.");
			return;
		}
		
		synchronized (this) {
			if (state == WorkerState.STARTED) {
				state = WorkerState.STOPPING;
				queuePoller.shutdown();
				try {
					boolean isOk = queuePoller.awaitTermination((long) (workerConfig.getExecutionTimeout() * 1.3), TimeUnit.MILLISECONDS);
					if (!isOk) {
						logger.error(queueConfig.getName() + "'s worker:" + workerId + " stop process timeout. may be illegal state....");
					}
				} catch (InterruptedException e) {
					logger.error(queueConfig.getName() + "'s worker:" + workerId + " stop process Interrupted. may be illegal state....", e);
				}
				stopImpl();
				state = WorkerState.STOPPED;
				mtpLogger.info("worker(id:" + workerId + ") of queue:" + queue.getName() + " is stopped.");
			} else {
				logger.warn(queueConfig.getName() + "'s worker:" + workerId + " is not stared. may be another thread call stop().");
			}
		}
	}
	
	protected abstract void stopImpl();
	
	protected abstract Future<Void> doTaskAndStatusUpdate(Task task);
	
	@Override
	public void wakeup() {
		if (state == WorkerState.STARTED) {
			queuePoller.schedule(new PollingTask(true), 0, TimeUnit.MILLISECONDS);
		}
	}
	
	private class PollingTask implements Runnable {
		
		private final boolean oneshot;
		
		public PollingTask(boolean oneshot) {
			this.oneshot = oneshot;
		}

		@Override
		public void run() {
			try {
				ResourceHolder.init();
				
				//テナント-1で初期化
				if (!ExecuteContext.isInited()) {
					TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
					TenantContext tContext = tcs.getSharedTenantContext();
					ExecuteContext econtext = new ExecuteContext(tContext);
					ExecuteContext.initContext(econtext);
				}
				
				try {
					for (;;) {
						if (state == WorkerState.STARTED) {
							final Task task = Transaction.required(t -> {
								return queue.poll(workerId, workerConfig.isLocal());
							});
							
							if (task == null) {
								return;
							}
							
							//TaskのContextに切り替え
							TenantContext taskTC = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(task.getTenantId());
							ExecuteContext.executeAs(taskTC, new Executable<Void>() {
								@Override
								public Void execute() {
									if (logger.isDebugEnabled()) {
										logger.debug("polled task:" + task);
									}
										
									Future<Void> ft = doTaskAndStatusUpdate(task);
									try {
										ft.get(workerConfig.getExecutionTimeout(), TimeUnit.MILLISECONDS);
										
									} catch (InterruptedException e) {
										mtpLogger.error("queue:" + queueConfig.getName() + "'s queuePoller is interrupted on task execution wait...", e);
										Thread.currentThread().interrupt();
									} catch (final TimeoutException | ExecutionException | RuntimeException | Error e) {
										//例外処理は、サブクラス側でつぶす想定なので、ここでは実質、TimeoutExceptionが発生する可能性だけが残るはず
										String msg;
										if (e instanceof TimeoutException) {
											msg = "timeout";
										} else {
											msg = "failed";
										}
										
										switch (task.getExceptionHandlingMode()) {
										case RESTART:
											mtpLogger.error("queue:" + queueConfig.getName() + "'s task:" + task.getTaskId() + "(tenantId:" + task.getTenantId() + ") is " + msg + ". re-run after a while.", e);
											if (!ft.cancel(true)) {
												logger.debug("task process cant cancel.may be completed." + task);
											}
											break;
										case ABORT_LOG_FATAL:
										case ABORT:
											if (task.getExceptionHandlingMode() == ExceptionHandlingMode.ABORT_LOG_FATAL
												|| e instanceof Error) {
												fatalLogger.error("queue:" + queueConfig.getName() + "'s task:" + task.getTaskId() + "(tenantId:" + task.getTenantId() + ") " + msg + ", so abort task.", e);
											} else {
												mtpLogger.error("queue:" + queueConfig.getName() + "'s task:" + task.getTaskId() + "(tenantId:" + task.getTenantId() + ") is " + msg + ", so abort task.", e);
											}
											if (ft.cancel(true)) {
												Transaction.required(transaction -> {
													final Throwable t;
													if (e instanceof TimeoutException) {
														t = new TaskTimeoutException(e);
													} else {
														t = e;
													}
													if (queue.taskAbort(task, true, t, false, false)) {
														if (task.getCallable().getActual() instanceof ExceptionHandleable) {
															AsyncTaskContextImpl asyncTaskContext = new AsyncTaskContextImpl(task.getTaskId(), queue.getName());
															try {
																ExecuteContext.getCurrentContext().setAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME, asyncTaskContext, false);

																if (e instanceof TimeoutException) {
																	((ExceptionHandleable) task.getCallable().getActual()).timeouted();
																} else {
																	((ExceptionHandleable) task.getCallable().getActual()).aborted(t);
																}
															} catch (Throwable ee) {
																fatalLogger.error("ExceptionHandleable's aborted()/timeouted() call failed(queue:" + queue.getConfig().getName() + ", task:" + task.getTaskId() + ",tenantId:" + task.getTenantId() + ")", ee);
																//timeoutの場合は、ステータスは更新する
																if (t instanceof TaskTimeoutException && transaction.isRollbackOnly()) {
																	transaction.addTransactionListener(new TransactionListener() {
																		@Override
																		public void afterRollback(Transaction tt) {
																			queue.taskAbort(task, true, t, false, true);
																		}
																	});
																}
															} finally {
																ExecuteContext.getCurrentContext().removeAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME);
															}
														}
													} else {
														mtpLogger.warn("task status cant update to [abort]. may be another process update status." + task);
													}
												});
											} else {
												mtpLogger.warn("task process cant cancel. may be completed." + task);
											}
											break;
										default:
											break;
										}
									}
									return null;
								}
							});
						} else {
							break;
						}
					}
				} catch (RuntimeException e) {
					logger.error("exception while polling...", e);
				} catch (Error e) {
					fatalLogger.error("fatal Error while polling...", e);
				} finally {
					if (state == WorkerState.STARTED && !oneshot) {
						queuePoller.schedule(this, workerConfig.getPollingInterval(), TimeUnit.MILLISECONDS);
					}
				}
				
			} finally {
				ExecuteContext.finContext();
				ResourceHolder.fin();
			}
			
		}
	}
	
}
