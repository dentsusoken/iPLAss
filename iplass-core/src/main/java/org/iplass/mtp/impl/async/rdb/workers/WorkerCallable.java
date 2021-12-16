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

import java.util.concurrent.Callable;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.impl.async.AsyncTaskContextImpl;
import org.iplass.mtp.impl.async.ExceptionHandleable;
import org.iplass.mtp.impl.async.rdb.Queue;
import org.iplass.mtp.impl.async.rdb.Task;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.rdb.connection.ResourceHolder;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import net.logstash.logback.argument.StructuredArguments;

public class WorkerCallable implements Callable<Void> {
	public static final String MDC_TASK_ID = "taskId";
	
	private static Logger logger = LoggerFactory.getLogger(WorkerCallable.class);
	private static Logger mtpLogger = LoggerFactory.getLogger("mtp.async.rdb");
	private static Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal.async.rdb");
	
	private final Task task;
	private final Queue queue;
	private final boolean trace;
	private final boolean initRH;
	
	public WorkerCallable(Task task, Queue queue, boolean trace, boolean initRH) {
		this.task = task;
		this.queue = queue;
		this.trace = trace;
		this.initRH = initRH;
	}

	@Override
	public Void call() throws Exception {
		try {
			MDC.put(MDC_TASK_ID, String.valueOf(task.getTaskId()));
			if (task.getCallable() != null && task.getCallable().getTraceId() != null) {
				MDC.put(ExecuteContext.MDC_TRACE_ID, task.getCallable().getTraceId());
			}
			if (initRH) {
				ResourceHolder.init();
			}
			
			final AsyncTaskContextImpl asyncTaskContext = new AsyncTaskContextImpl(task.getTaskId(), queue.getName());
			
			//Tenant設定
			TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(task.getTenantId());
			return ExecuteContext.executeAs(tc, new Executable<Void>() {
				@Override
				public Void execute() {
					ExecuteContext ec = ExecuteContext.getCurrentContext();
					try {
						ec.setAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME, asyncTaskContext, false);

						if (task.getCallable().getUserContext() != null) {
							//Auth設定
							AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
							as.doSecuredAction(task.getCallable().getUserContext(), () -> {
								callImpl();
								return null;
							});
						} else {
							callImpl();
						}
					} catch (final RuntimeException | Error e) {
						
						switch (task.getExceptionHandlingMode()) {
						case RESTART:
							if (task.getRetryCount() >= queue.getConfig().getWorker().getMaxRetryCount()) {
								fatalLogger.error("queue:" + queue.getConfig().getName() + "'s task:" + task.getTaskId() + "(tenantId:" + task.getTenantId() + ") is failed. max retry count over.", e);
							} else {
								mtpLogger.warn("queue:" + queue.getConfig().getName() + "'s task:" + task.getTaskId() + "(tenantId:" + task.getTenantId() + ") is failed(cause:" + e + "). re-run after a while.");
							}
							break;
						case ABORT_LOG_FATAL:
						case ABORT:
							Transaction.required(transaction -> {
								final Throwable t;
								if (e instanceof WrapException) {
									t = e.getCause();
								} else {
									t = e;
								}
								if (queue.taskAbort(task, true, t, false, false)) {
									if (task.getCallable().getActual() instanceof ExceptionHandleable) {
										try {
											((ExceptionHandleable) task.getCallable().getActual()).aborted(t);
										} catch (Throwable ee) {
											fatalLogger.error("ExceptionHandleable's aborted() call failed(queue:" + queue.getConfig().getName() + ", task:" + task.getTaskId() + ",tenantId:" + task.getTenantId() + ")", ee);
											transaction.setRollbackOnly();
										}
									}
								} else {
									logger.warn("task status cant update to [abort].may be another process update status." + task);
								}
							});
							break;
						default:
							break;
						}
					} finally {
						ec.removeAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME);
					}
					
					return null;
				}
			});
		} finally {
			if (initRH) {
				ResourceHolder.fin();
			}
			MDC.remove(ExecuteContext.MDC_TRACE_ID);
			MDC.remove(MDC_TASK_ID);
		}
	}
	
	private void callImpl() {
		//トランザクション開始
		Transaction.required(transaction -> {
			//ログ
			long start = -1;
			if (trace) {
				start = System.currentTimeMillis();
			}
			Throwable t = null;
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("execute " + task);
				}
				
				//タスク実行
				Object res = task.getCallable().getActual().call();
				//queueのステータス＆Result更新
				if (task.isReturnResult()) {
					task.setResult(res);
				}
				queue.taskFinish(task);
				return null;
			} catch (RuntimeException | Error e) {
				transaction.setRollbackOnly();
				t = e;
				throw e;
			} catch (Exception e) {
				transaction.setRollbackOnly();
				t = e;
				throw new WrapException(e);
			} finally {
				if (t != null) {
					if (trace) {
						mtpLogger.error("{}(id={}),{},{}ms,{},{}",
								task.getCallable(),
								task.getTaskId(),
								StructuredArguments.value(LOG_ARG_QUEUE_NAME, queue.getName()),
								StructuredArguments.value(LOG_ARG_EXECUTION_TIME, (System.currentTimeMillis() - start)),
								StructuredArguments.value(LOG_ARG_ERROR_TYPE, errorType(t)),
								StructuredArguments.value(LOG_ARG_ERROR_DESCRIPTION, t.getMessage()),
								t);
					} else {
						mtpLogger.error("{}(id={}),{},{},{}",
								task.getCallable(),
								task.getTaskId(),
								StructuredArguments.value(LOG_ARG_QUEUE_NAME, queue.getName()),
								StructuredArguments.value(LOG_ARG_ERROR_TYPE, errorType(t)),
								StructuredArguments.value(LOG_ARG_ERROR_DESCRIPTION, t.getMessage()),
								t);
					}
					if (t instanceof Error
							|| task.getExceptionHandlingMode() == ExceptionHandlingMode.ABORT_LOG_FATAL) {
						fatalLogger.error("queue:" + queue.getConfig().getName() + "'s task:" + task.getTaskId() + "(tenantId:" + task.getTenantId() + ") fatal error.", t);
					}
				} else {
					if (trace) {
						mtpLogger.info("{}(id={}),{},{}ms",
								task.getCallable(),
								task.getTaskId(),
								StructuredArguments.value(LOG_ARG_QUEUE_NAME, queue.getName()),
								StructuredArguments.value(LOG_ARG_EXECUTION_TIME, (System.currentTimeMillis() - start)));
					}
				}
			}
		});
	}

	private String errorType(Throwable t) {
		if (t instanceof ApplicationException) {
			return LOG_TYPE_APP_ERROR;
		} else {
			return LOG_TYPE_ERROR;
		}
	}

	private static final String LOG_TYPE_APP_ERROR = "AppError";
	private static final String LOG_TYPE_ERROR = "Error";
	private static final String LOG_ARG_QUEUE_NAME = "queue_name";
	private static final String LOG_ARG_EXECUTION_TIME = "execution_time";
	private static final String LOG_ARG_ERROR_TYPE = "error_type";
	private static final String LOG_ARG_ERROR_DESCRIPTION = "error_description";

	private static class WrapException extends RuntimeException {
		private static final long serialVersionUID = 1513733395068136003L;

		public WrapException(Throwable cause) {
			super(cause);
		}
	}

}
