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

package org.iplass.adminconsole.shared.tools.dto.auth.builtin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BuiltinAuthUserListResultDto implements Serializable {

	/** シリアルバージョンNo */
	private static final long serialVersionUID = 6123091297872728806L;

	private List<BuiltinAuthUserDto> users;

	private int totalCount;
	private int executeOffset;

	private boolean isError;
	private List<String> logMessages;


	/**
	 * コンストラクタ
	 */
	public BuiltinAuthUserListResultDto() {
		isError = false;
		logMessages = new ArrayList<String>();
	}


	public void setUsers(List<BuiltinAuthUserDto> users) {
		this.users = users;
	}

	public List<BuiltinAuthUserDto> getUsers() {
		return users;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setExecuteOffset(int executeOffset) {
		this.executeOffset = executeOffset;
	}

	public int getExecuteOffset() {
		return executeOffset;
	}

//	public void setSearchResult(SearchResult<Entity> searchResult) {
//		this.searchResult = searchResult;
//	}
//
//	public SearchResult<Entity> getSearchResult() {
//		return searchResult;
//	}


	public void addLogMessage(String message) {
		if (logMessages == null) {
			logMessages = new ArrayList<String>();
		}
		logMessages.add(message);
	}

	public void setLogMessages(List<String> logMessages) {
		this.logMessages = logMessages;
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
