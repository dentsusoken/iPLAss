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

package org.iplass.mtp.impl.async.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.iplass.mtp.async.AsyncTaskFuture;
import org.iplass.mtp.async.AsyncTaskOption;
import org.iplass.mtp.async.StartMode;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.impl.async.AsyncTaskContextImpl;
import org.iplass.mtp.impl.async.AsyncTaskRuntimeException;
import org.iplass.mtp.impl.async.AsyncTaskService;
import org.iplass.mtp.impl.async.ExceptionHandleable;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.rdb.connection.ResourceHolder;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public class ThreadingAsyncTaskService extends AsyncTaskService {
	
	private static Logger logger = LoggerFactory.getLogger(ThreadingAsyncTaskService.class);
	private static Logger fatal = LoggerFactory.getLogger("mtp.fatal.async");
	
	public static final String FIXED = "fixed";
	public static final String SINGLE = "single";
	public static final String CACHED = "cached";
	
	private String threadPoolType = CACHED;
	private int corePoolSize = 0;
	private int maximumPoolSize = -1;
	private long keepAliveTime = 60000;//millis
	
	private boolean useResourceHolder = true;
	
	private ExecutorService executor;

	public String getThreadPoolType() {
		return threadPoolType;
	}

	public void setThreadPoolType(String threadPoolType) {
		this.threadPoolType = threadPoolType;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public ThreadingAsyncTaskService() {
	}

	@Override
	public <V> Future<V> execute(final Callable<V> task) {
		return executeImpl(task, true);
	}

	private <V> V callImpl(final Callable<V> task) {
		try {
			return task.call();
		} catch (Exception e) {
			throw new WrapException(e);
		}
	}

	public <V> Future<V> executeImpl(final Callable<V> task, final boolean inheritAuthContext) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		final TenantContext tc = ec.getTenantContext();
		final UserContext uc;
		AuthContextHolder ach = AuthContextHolder.getAuthContext();
		if (ach.isSecuredAction() && inheritAuthContext) {
			uc = ach.getUserContext();
		} else {
			uc = null;
		}
		final String mdcUser = ec.getClientId();
		final String mdcTraceId = MDC.get(ExecuteContext.MDC_TRACE_ID);
		
		Callable<V> wrapper = new Callable<V>() {
			@Override
			public V call() throws Exception {
				try {
					if (useResourceHolder) {
						ResourceHolder.init();
					}
					return ExecuteContext.executeAs(tc, new Executable<V>() {
						@Override
						public V execute() {
							AsyncTaskContextImpl asyncTaskContext = new AsyncTaskContextImpl(-1, null);
							ExecuteContext ec = ExecuteContext.getCurrentContext();
							try {
								ec.setAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME, asyncTaskContext, false);
								if (mdcTraceId != null) {
									ec.mdcPut(ExecuteContext.MDC_TRACE_ID, mdcTraceId);
								}
								ec.mdcPut(AuthService.MDC_USER, mdcUser);
								if (uc != null) {
									//Auth設定
									AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
									return as.doSecuredAction(uc, () -> {
										return callImpl(task);
									});
								} else {
									return callImpl(task);
								}
							} catch (WrapException e) {
								logger.error("exception occured in async task:" + e.getCause().getMessage(), e.getCause());
								if (task instanceof ExceptionHandleable) {
									((ExceptionHandleable) task).aborted(e.getCause());
								}
								throw e;
							} catch (RuntimeException e) {
								logger.error("exception occured in async task:" + e.getMessage(), e);
								if (task instanceof ExceptionHandleable) {
									((ExceptionHandleable) task).aborted(e);
								}
								throw e;
							} catch (Error e) {
								fatal.error("error occured in async task:" + e.getMessage(), e);
								if (task instanceof ExceptionHandleable) {
									((ExceptionHandleable) task).aborted(e);
								}
								throw e;
							} finally {
								ec.mdcPut(AuthService.MDC_USER, null);
								ec.mdcPut(ExecuteContext.MDC_TRACE_ID, null);
								ec.removeAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME);
							}
						}
					});
				} catch(WrapException e) {
					 throw (Exception) e.getCause();
				} finally {
					if (useResourceHolder) {
						ResourceHolder.fin();
					}
				}
			}
		};
		return executor.submit(wrapper);
	}

	public void destroy() {
		if (executor != null) {
			executor.shutdown();
		}
		executor = null;
	}


	public void init(Config config) {
		
		if (config.getValue("threadPoolType") != null) {
			threadPoolType = config.getValue("threadPoolType");
		}
		if (config.getValue("corePoolSize") != null) {
			corePoolSize = Integer.parseInt(config.getValue("corePoolSize"));
		}
		if (config.getValue("maximumPoolSize") != null) {
			maximumPoolSize = Integer.parseInt(config.getValue("maximumPoolSize"));
		}
		if (config.getValue("keepAliveTime") != null) {
			keepAliveTime = Long.parseLong(config.getValue("keepAliveTime"));
		}
		if (config.getValue("useResourceHolder") != null) {
			useResourceHolder = Boolean.valueOf(config.getValue("useResourceHolder"));
		}
		createExecuter();
	}

	private void createExecuter() {
		
		switch (threadPoolType) {
		case FIXED:
			if (corePoolSize < 1) {
				throw new ServiceConfigrationException(FIXED + " type must specify corePoolSize greater than 1");
			}
			executor = Executors.newFixedThreadPool(corePoolSize);
			break;
		case SINGLE:
			executor = Executors.newSingleThreadExecutor();
			break;
		case CACHED:
			if (maximumPoolSize == -1) {
				executor = new ThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE,
                        keepAliveTime, TimeUnit.MILLISECONDS,
                        new SynchronousQueue<Runnable>());
			} else {
				executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                        keepAliveTime, TimeUnit.MILLISECONDS,
                        new SynchronousQueue<Runnable>());
			}
			break;
		default:
			throw new ServiceConfigrationException("threadPoolType:" + threadPoolType + " unkown");
		}
	}

	@Override
	public <V> AsyncTaskFuture<V> execute(Callable<V> task, AsyncTaskOption option, boolean inheritAuthContext) {
		if (option.getStartMode() == StartMode.AFTER_COMMIT) {
			throw new AsyncTaskRuntimeException("ThreadingAsyncTaskService support only AsyncTaskStartMode.IMMEDIATELY");
		}
		return new ThreadingAsyncTaskFuture<>(executeImpl(task, inheritAuthContext), option.isReturnResult());
	}

	@Override
	public <V> AsyncTaskFuture<V> getResult(long taskId, String queueName) {
		throw new UnsupportedOperationException("ThreadingAsyncTaskService not support getResult() methods");
	}
	
	private static class ThreadingAsyncTaskFuture<V> implements AsyncTaskFuture<V> {
		
		private final Future<V> real;
		private final boolean returnResult;
		
		private ThreadingAsyncTaskFuture(Future<V> real, boolean returnResult) {
			this.real = real;
			this.returnResult = returnResult;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return real.cancel(mayInterruptIfRunning);
		}

		@Override
		public boolean isCancelled() {
			return real.isCancelled();
		}

		@Override
		public boolean isDone() {
			return real.isDone();
		}

		@Override
		public V get() throws InterruptedException, ExecutionException {
			return real.get();
		}

		@Override
		public V get(long timeout, TimeUnit unit) throws InterruptedException,
				ExecutionException, TimeoutException {
			return real.get(timeout, unit);
		}

		@Override
		public long getTaskId() {
			return -1;
		}

		@Override
		public TaskStatus getStatus() {
			if (real.isCancelled()) {
				return TaskStatus.ABORTED;
			}
			if (real.isDone()) {
				if (returnResult) {
					return TaskStatus.RETURNED;
				} else {
					return TaskStatus.COMPLETED;
				}
			}
			return TaskStatus.EXECUTING;
		}

		@Override
		public String getQueueName() {
			return AsyncTaskOption.LOCAL_THREAD_QUEUE_NAME;
		}
		
	}
	
	private static class WrapException extends RuntimeException {
		private static final long serialVersionUID = 6163604599717782498L;

		public WrapException(Throwable cause) {
			super(cause);
		}
	}

}
