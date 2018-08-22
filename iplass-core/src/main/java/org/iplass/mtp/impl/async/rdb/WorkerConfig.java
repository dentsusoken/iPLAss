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

import java.util.List;
import java.util.concurrent.TimeUnit;


public class WorkerConfig {
	
	private int virtualWorkerSize = 16;
	private int actualWorkerSize = 1;
	private long executionTimeout = TimeUnit.MINUTES.toMillis(3);
	private long pollingInterval = TimeUnit.SECONDS.toMillis(30);
	private long restartDelay = TimeUnit.SECONDS.toMillis(30);
	
	private int maxRetryCount = 100;
	
	private boolean wakeupOnSubmit = true;//現状localWorkerの場合のみ有効
	
	private boolean trace = true;
	private boolean local = true;
	
	private boolean newProcessPerTask = false;
	private String javaCommand;
	private List<String> vmArgs;
	private String redirectFile;

	public int getMaxRetryCount() {
		return maxRetryCount;
	}
	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}
	public String getRedirectFile() {
		return redirectFile;
	}
	public void setRedirectFile(String redirectFile) {
		this.redirectFile = redirectFile;
	}
	public boolean isWakeupOnSubmit() {
		return wakeupOnSubmit;
	}
	public void setWakeupOnSubmit(boolean wakeupOnSubmit) {
		this.wakeupOnSubmit = wakeupOnSubmit;
	}
	public boolean isNewProcessPerTask() {
		return newProcessPerTask;
	}
	public void setNewProcessPerTask(boolean newProcessPerTask) {
		this.newProcessPerTask = newProcessPerTask;
	}
	public String getJavaCommand() {
		return javaCommand;
	}
	public void setJavaCommand(String javaCommand) {
		this.javaCommand = javaCommand;
	}
	public List<String> getVmArgs() {
		return vmArgs;
	}
	public void setVmArgs(List<String> vmArgs) {
		this.vmArgs = vmArgs;
	}
	public long getRestartDelay() {
		return restartDelay;
	}
	public void setRestartDelay(long restartDelay) {
		this.restartDelay = restartDelay;
	}
	public long getExecutionTimeout() {
		return executionTimeout;
	}
	public void setExecutionTimeout(long executionTimeout) {
		this.executionTimeout = executionTimeout;
	}
	public long getPollingInterval() {
		return pollingInterval;
	}
	public void setPollingInterval(long pollingInterval) {
		this.pollingInterval = pollingInterval;
	}
	public int getVirtualWorkerSize() {
		return virtualWorkerSize;
	}
	public void setVirtualWorkerSize(int virtualWorkerSize) {
		this.virtualWorkerSize = virtualWorkerSize;
	}
	public int getActualWorkerSize() {
		return actualWorkerSize;
	}
	public void setActualWorkerSize(int actualWorkerSize) {
		this.actualWorkerSize = actualWorkerSize;
	}
	public boolean isTrace() {
		return trace;
	}
	public void setTrace(boolean trace) {
		this.trace = trace;
	}
	public boolean isLocal() {
		return local;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}

}
