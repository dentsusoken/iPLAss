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

import java.util.HashMap;

import org.iplass.mtp.impl.async.rdb.workers.ProcessWorker;
import org.iplass.mtp.impl.async.rdb.workers.RemoteWorker;
import org.iplass.mtp.impl.async.rdb.workers.ThreadWorker;
import org.iplass.mtp.impl.core.config.ServerEnv;

public class DefaultWorkerFactory extends WorkerFactory {
	
	private final HashMap<String, int[]> myWorkers;
	private final boolean isWorkerProcess;
	
	public DefaultWorkerFactory() {
		myWorkers = parseServerProperty();
		String flg = System.getProperty(WORKER_PROCESS, "false");//あえて、直接SystemProperty
		isWorkerProcess = Boolean.valueOf(flg);
	}
	
	private HashMap<String, int[]> parseServerProperty() {
		HashMap<String, int[]> map = new HashMap<>();
		
		//def format:
		// [queueName]:[id]:[id],...
		String def = ServerEnv.getInstance().getProperty(WORKER_ID_DEF_NAME);
		if (def != null) {
			String[] qAndIds = def.split(",");
			for (String qi: qAndIds) {
				String[] qiSplit = qi.trim().split(":");
				int[] ids = new int[qiSplit.length - 1];
				for (int i = 0; i < ids.length; i++) {
					ids[i] = Integer.parseInt(qiSplit[i + 1].trim());
				}
				map.put(qiSplit[0], ids);
			}
		}
		return map;
	}

	@Override
	public Worker createWorker(Queue queue, int workerId) {
		if (isWorkerProcess()) {
			//このプロセス自体がProcessWorkerのProcessの場合は、LocalWorkerは起動しない
			return new RemoteWorker();
		}
		
		WorkerConfig wc = queue.getConfig().getWorker();
		if (wc.isLocal()) {
			if (wc.isNewProcessPerTask()) {
				return new ProcessWorker(queue, workerId);
			} else {
				return new ThreadWorker(queue, workerId);
			}
		} else {
			int[] idList = getMyWorkers().get(queue.getName());
			if (idList != null) {
				for (int id: idList) {
					if (id == workerId) {
						if (wc.isNewProcessPerTask()) {
							return new ProcessWorker(queue, workerId);
						} else {
							return new ThreadWorker(queue, workerId);
						}
					}
				}
			}
			return new RemoteWorker();
		}
	}

	public boolean isWorkerProcess() {
		return isWorkerProcess;
	}

	public HashMap<String, int[]> getMyWorkers() {
		return myWorkers;
	}

}
