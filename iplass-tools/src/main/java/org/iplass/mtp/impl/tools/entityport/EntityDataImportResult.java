/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.entityport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * EntityのImport結果
 */
public class EntityDataImportResult implements Serializable {

	private static final long serialVersionUID = 7565116614783118670L;

	private boolean isError = false;
	private List<String> messages;

	private long insertCount = 0;
	private long updateCount = 0;
	private long deleteCount = 0;
	private long errorCount = 0;

	/**
	 * コンストラクタ
	 */
	public EntityDataImportResult() {
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public boolean isError() {
		return isError;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public void addMessages(String message) {
		if (messages == null) {
			messages = new ArrayList<String>();
		}
		messages.add(message);
	}

	public void clearMessages() {
		this.messages = null;
	}

	/**
	 * @return insertCount
	 */
	public long getInsertCount() {
		return insertCount;
	}

	/**
	 * @param insertCount セットする insertCount
	 */
	public void setInsertCount(long insertCount) {
		this.insertCount = insertCount;
	}

	/**
	 * @return updateCount
	 */
	public long getUpdateCount() {
		return updateCount;
	}

	/**
	 * @param updateCount セットする updateCount
	 */
	public void setUpdateCount(long updateCount) {
		this.updateCount = updateCount;
	}

	public long getDeleteCount() {
		return deleteCount;
	}

	public void setDeleteCount(long deleteCount) {
		this.deleteCount = deleteCount;
	}

	/**
	 * @return errorCount
	 */
	public long getErrorCount() {
		return errorCount;
	}

	/**
	 * @param errorCount セットする errorCount
	 */
	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}

	public void inserted() {
		insertCount++;
	}

	public void updated() {
		updateCount++;
	}

	public void deleted() {
		deleteCount++;
	}

	public void errored() {
		errorCount++;
	}

}
