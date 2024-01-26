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

package org.iplass.mtp.impl.async.rdb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.counter.CounterService;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdbQueueService implements Service {
	public static final String DEFAULT_QUEUE_NAME = "default";

	private static Logger logger = LoggerFactory.getLogger(RdbQueueService.class);

	private List<QueueConfig> queue;

	private HashMap<String, Queue> queueMap;
	private HashMap<Integer, Queue> queueIdMap;
	private Queue defaultQueue;

	private CounterService taskIdCounter;
	private CounterService taskIdCounterForGroup;
	private RdbAdapter rdb;

	private boolean useQueue = false;

	private boolean cleanupHistoryOnInit = false;
	private int historyHoldDay = 1;

	public int getHistoryHoldDay() {
		return historyHoldDay;
	}

	public boolean isCleanupHistoryOnInit() {
		return cleanupHistoryOnInit;
	}

	public boolean isUseQueue() {
		return useQueue;
	}

	@Override
	public void init(Config config) {

		if (config.getValue("useQueue") != null) {
			useQueue = Boolean.valueOf(config.getValue("useQueue"));
		}

		rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();
		taskIdCounter = config.getDependentService("TaskIdCounter");
		taskIdCounterForGroup = config.getDependentService("TaskIdCounterGrouping");

		if (config.getValue("cleanupHistoryOnInit") != null) {
			cleanupHistoryOnInit = Boolean.valueOf(config.getValue("cleanupHistoryOnInit"));
		}

		if (config.getValue("historyHoldDay") != null) {
			historyHoldDay = Integer.parseInt(config.getValue("historyHoldDay"));
		}

		queue = config.getValues("queue", QueueConfig.class);

		WorkerFactory workerFactory = config.getValue("workerFactory", WorkerFactory.class, new DefaultWorkerFactory());

		queueMap = new HashMap<>();
		queueIdMap = new HashMap<>();
		if (queue != null) {
			for (QueueConfig qc: queue) {
				Queue q = new Queue(qc, taskIdCounter, taskIdCounterForGroup, rdb, workerFactory);
				queueMap.put(q.getName(), q);
				queueIdMap.put(qc.getId(), q);
				if (q.getName().equalsIgnoreCase(DEFAULT_QUEUE_NAME)) {
					defaultQueue = q;
				}
			}
		}

		if (useQueue) {
			if (logger.isDebugEnabled()) {
				logger.debug("RdbQueueService initialize process started.init queues...");
			}

			if (defaultQueue == null) {
				throw new ServiceConfigrationException("default queue(named 'default') is not specified");
			}

			if (cleanupHistoryOnInit) {
				logger.debug("RdbQueueService.cleanupHistoryOnInit is true so start cleanup process start.");
				Transaction.required(t -> {
					moveNoGetResultTaskToHistory();
					deleteHistoryByDate(null);
				});
			}

			for (Queue q: queueMap.values()) {
				q.startWorker();
			}

		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("RdbQueueService.useQueue flag is false.so not start RdbQueueService.");
			}
		}
	}

	@Override
	public void destroy() {
		if (useQueue && queueMap != null) {
			logger.info("stopping queue worker...");
			for (Map.Entry<String, Queue> e: queueMap.entrySet()) {
				e.getValue().stopWorker();
				logger.debug("stopped worker of queue:" + e.getValue().getName());
			}
			logger.info("stopping queue worker...done.");
//			queueMap = null;
		}
	}

	public Queue getQueue(String queueName) {
		if (useQueue == false) {
			throw new ServiceConfigrationException("RdbQueueService.useQueue=false. set to true if use RdbQueueService.");
		}
		if (queueName == null) {
			return defaultQueue;
		}

		return queueMap.get(queueName);
	}

	public Queue getQueueById(int id) {
		if (useQueue == false) {
			throw new ServiceConfigrationException("RdbQueueService.useQueue=false. set to true if use RdbQueueService.");
		}
		return queueIdMap.get(id);
	}

	public List<String> getQueueNameList() {

		List<String> queueNameList = new ArrayList<String>();

		if (queue != null) {
			for (QueueConfig q : queue) {
				queueNameList.add(q.getName());
			}
		}

		return queueNameList;
	}

	public void deleteHistoryByDate(Timestamp date) {
		deleteHistoryByDate(date, false);
	}

	public void deleteHistoryByDate(Timestamp date, boolean isDirectTenant) {
		if (date == null) {
			date = new Timestamp(System.currentTimeMillis() - historyHoldDay * 24 * 60 * 60 * 1000);
		}

		new TaskDao(rdb).deleteHistoryByDate(date, isDirectTenant);
	}

	public void moveNoGetResultTaskToHistory() {

		if (queueMap != null) {
			for (Queue e: queueMap.values()) {
				e.moveNoGetResultTaskToHistory();
			}
		}
	}


}
