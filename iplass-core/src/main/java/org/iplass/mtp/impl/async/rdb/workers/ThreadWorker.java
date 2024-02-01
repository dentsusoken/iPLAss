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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.impl.async.rdb.Queue;
import org.iplass.mtp.impl.async.rdb.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadWorker extends LocalWorker {
	
	private static Logger logger = LoggerFactory.getLogger(ThreadWorker.class);
	
	private ExecutorService executor;
	
	public ThreadWorker(Queue queue, int workerId) {
		super(queue, workerId);
	}

	@Override
	protected void startImpl() {
		SecurityManager s = System.getSecurityManager();
		final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
	            Thread t = new Thread(group, r,
	            		queueConfig.getName() + "-" + workerId + "-worker-" + counter.incrementAndGet(),
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
				logger.error(queueConfig.getName() + "'s worker:" + workerId + " stop process timeout( at ThreadWorker). may be illegal state....");
			}
		} catch (InterruptedException e) {
			logger.error(queueConfig.getName() + "'s worker:" + workerId + " stop process Interrupted( at ThreadWorker). may be illegal state....", e);
		}
	}

	@Override
	protected Future<Void> doTaskAndStatusUpdate(final Task task) {
		return executor.submit(new WorkerCallable(task, queue, workerConfig.isTrace(), true));
	}
	
}
