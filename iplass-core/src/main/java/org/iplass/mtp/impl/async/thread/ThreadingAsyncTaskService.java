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
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.connection.ResourceHolder;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
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
	
	public <V> Future<V> executeImpl(final Callable<V> task, final boolean inheritAuthContext) {
		// Threadを起動する場合には、現在のExecuteContextを引き継ぐ
		//FIXME Servlet関連リソースは引き継がないようにする。（別スレッドによるアクセスは推奨されてない）
		final ExecuteContext context = ExecuteContext.getCurrentContext();
		if (inheritAuthContext) {
			Callable<V> wrapper = new Callable<V>() {
				@Override
				public V call() throws Exception {
					try {
						if (useResourceHolder) {
							ResourceHolder.init();
						}
						try {
							ExecuteContext.setContext(context);
							//TODO logbackでMDCがスレッド間で引き継がれなくなったので、、、でも記述箇所はここではない気もするので、、要検討
							MDC.put("user", context.getClientId());

							try {
								context.setAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME, new AsyncTaskContextImpl(-1, null), false);
								return task.call();
							} finally {
								context.removeAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME);
							}
						} catch (Exception | Error e) {
							logger.error("exception occured in async task:" + e.getMessage(), e);
							if (e instanceof Error) {
								fatal.error("error occured in async task:" + e.getMessage(), e);
							}
							if (task instanceof ExceptionHandleable) {
								AsyncTaskContextImpl asyncTaskContext = new AsyncTaskContextImpl(-1, null);
								try {
									ExecuteContext.getCurrentContext().setAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME, asyncTaskContext, false);
									((ExceptionHandleable) task).aborted(e);
								} finally {
									ExecuteContext.getCurrentContext().removeAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME);
								}
							}
							throw e;
						} finally {
							//TODO logbackでMDCがスレッド間で引き継がれなくなったので、、、でも記述箇所はここではない気もするので、、要検討
							MDC.remove("user");
							ExecuteContext.setContext(null);
						}
					} finally {
						if (useResourceHolder) {
							ResourceHolder.fin();
						}
					}
				}
			};
			return executor.submit(wrapper);
		} else {
			Callable<V> wrapper = new Callable<V>() {
				@Override
				public V call() throws Exception {
					try {
						if (useResourceHolder) {
							ResourceHolder.init();
						}
						return ExecuteContext.executeAs(context.getTenantContext(), new Executable<V>() {

							@Override
							public V execute() {
								try {
									context.setAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME, new AsyncTaskContextImpl(-1, null), false);
									return task.call();
								} catch (Exception e) {
									logger.error("exception occured in async task:" + e.getMessage(), e);
									throw new WrapException(e);
								} catch (Error e) {
									fatal.error("error occured in async task:" + e.getMessage(), e);
									throw e;
								} finally {
									context.removeAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME);
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
