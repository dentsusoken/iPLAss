/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntityUpdateAllResultInfo implements Serializable {

	/** シリアルバージョンNo */
	private static final long serialVersionUID = 6123091297872728806L;

	private int updateCount;

	private boolean isError;
	private List<String> logMessages;


	/**
	 * コンストラクタ
	 */
	public EntityUpdateAllResultInfo() {
		isError = false;
		logMessages = new ArrayList<String>();
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	public int getsetUpdateCount() {
		return updateCount;
	}

	public void addLogMessage(String message) {
		logMessages.add(message);
	}

	public void addLogMessage(List<String> messages) {
		logMessages.addAll(messages);
	}

	public List<String> getLogMessages() {
		return logMessages;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public boolean isError() {
		return isError;
	}

}
