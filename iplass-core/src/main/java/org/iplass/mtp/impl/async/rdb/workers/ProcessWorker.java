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

package org.iplass.mtp.impl.async.rdb.workers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.impl.async.rdb.Queue;
import org.iplass.mtp.impl.async.rdb.RdbQueueService;
import org.iplass.mtp.impl.async.rdb.Task;
import org.iplass.mtp.impl.async.rdb.WorkerFactory;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.rdb.connection.ResourceHolder;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessWorker extends LocalWorker {
	private static Logger logger = LoggerFactory.getLogger(ProcessWorker.class);
	private static Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal.async.rdb.processworker");

	//別スレッドでプロセスのリターンを待機する。Process#waitForがタイムアウトを設定できないので（java8からできるっぽいけど）
	private ExecutorService executor;
	
	public ProcessWorker(Queue queue, int workerId) {
		super(queue, workerId);
	}
	
	private Process createProcess(Task task) throws IOException {
		ArrayList<String> command = new ArrayList<>();
		command.add(queue.getConfig().getWorker().getJavaCommand());
		if (queue.getConfig().getWorker().getVmArgs() != null) {
			command.addAll(queue.getConfig().getWorker().getVmArgs());
		}
		command.add("-D" + WorkerFactory.WORKER_PROCESS + "=true");
		command.add(Main.class.getName());
		
		
		Redirect redirect = null;
		if (queue.getConfig().getWorker().getRedirectFile() == null) {
			redirect = Redirect.INHERIT;
		} else {
			File f = new File(queue.getConfig().getWorker().getRedirectFile());
			if (!f.exists()) {
				f.createNewFile();
			}
			redirect = Redirect.appendTo(f);
			
		}
		//TODO 環境変数
		return new ProcessBuilder(command).
				redirectError(redirect).
				redirectOutput(redirect).start();
	}

	@Override
	protected void startImpl() {
		SecurityManager s = System.getSecurityManager();
		final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
	            Thread t = new Thread(group, r,
	            		queueConfig.getName() + "-" + workerId + "-processWatcher-" + counter.incrementAndGet(),
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
	}

	@Override
	protected void stopImpl() {
		executor.shutdown();
		try {
			boolean isOk = executor.awaitTermination((long) (queue.getConfig().getWorker().getExecutionTimeout() * 1.3), TimeUnit.MILLISECONDS);
			if (!isOk) {
				logger.error(queueConfig.getName() + "'s processWatcher:" + workerId + " stop process timeout( at ProcessWorker). may be illegal state....");
			}
		} catch (InterruptedException e) {
			logger.error(queueConfig.getName() + "'s processWatcher:" + workerId + " stop process Interrupted( at ProcessWorker). may be illegal state....", e);
		}
	}

	@Override
	protected Future<Void> doTaskAndStatusUpdate(final Task task) {
		return executor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				
				boolean isDone = false;
				Process process = createProcess(task);
				//taskを標準出力で渡す
				try (OutputStream os = process.getOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
					oos.writeObject(task);
					oos.flush();
					
					int exitCode = process.waitFor();
					if (exitCode != 0) {
						logger.warn("process exited un-normal code:" + exitCode + ", re-run or abort after a while.");
					}
					isDone = true;
					
				} finally {
					process.destroy();
					if (!isDone) {
						logger.warn("process exited illegally, maybe cancel called.");
					}
				}
				return null;
			}
		});
	}
	
	
	public static class Main {
		public static void main(String[] args) throws Exception {
			
			boolean isSuccess = false;
			InputStream is = System.in;
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
			Task task = (Task) ois.readObject();
			
			try {
				ResourceHolder.init();

				//テナントに依存しないので-1で初期化
				if (!ExecuteContext.isInited()) {
					TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
					TenantContext tContext = new TenantContext(tcs.getSharedTenantId(), null, null, true);
					ExecuteContext econtext = new ExecuteContext(tContext);
					ExecuteContext.initContext(econtext);
				}
				
				Queue queue = ServiceRegistry.getRegistry().getService(RdbQueueService.class).getQueueById(task.getQueueId());
				if (queue == null) {
					throw new IllegalArgumentException("queueId:" + task.getQueueId() + " not found");
				}
				
				WorkerCallable wc = new WorkerCallable(task, queue, queue.getConfig().getWorker().isTrace(), false);
				wc.call();
				
				isSuccess = true;
			} catch (Throwable t) {
				logger.error("error occured while task processing:" + task, t);
				if (t instanceof Error) {
					fatalLogger.error("fatal error occured while task processing:" + task, t);
				}
			} finally {
				ExecuteContext.finContext();
				ResourceHolder.fin();
				ServiceRegistry.getRegistry().destroyAllService();
				if (isSuccess) {
					System.exit(0);
				} else {
					System.exit(1);
				}
			}
			
		}
	}
	
}
