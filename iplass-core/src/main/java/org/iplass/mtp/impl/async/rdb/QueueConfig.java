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

import java.util.concurrent.TimeUnit;

public class QueueConfig {
	
	private int id;
	private String name;
	private long resultRemainingTime = TimeUnit.DAYS.toMillis(1);
	private boolean strictSequence = true;
	private boolean selectWorkerOnSubmit = false;
	
	private WorkerConfig worker = new WorkerConfig();
	
	public QueueConfig() {
	}
	
	public QueueConfig(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public WorkerConfig getWorker() {
		return worker;
	}

	public void setWorker(WorkerConfig worker) {
		this.worker = worker;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isSelectWorkerOnSubmit() {
		return selectWorkerOnSubmit;
	}
	public void setSelectWorkerOnSubmit(boolean selectWorkerOnSubmit) {
		this.selectWorkerOnSubmit = selectWorkerOnSubmit;
	}
	public boolean isStrictSequence() {
		return strictSequence;
	}
	public void setStrictSequence(boolean strictSequence) {
		this.strictSequence = strictSequence;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getResultRemainingTime() {
		return resultRemainingTime;
	}
	public void setResultRemainingTime(long resultRemainingTime) {
		this.resultRemainingTime = resultRemainingTime;
	}

}
