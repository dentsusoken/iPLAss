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

package org.iplass.mtp.impl.tools.auth.builtin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * EntityのImport結果
 */
public class BuiltinAuthUserSearchResult implements Serializable {

	private static final long serialVersionUID = 7565116614783118670L;

	private int totalCount;
	private int executeOffset;

	private List<BuiltinAuthUser> users;

	private boolean isError;
	private List<String> messages;

	/**
	 * コンストラクタ
	 */
	public BuiltinAuthUserSearchResult() {
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getExecuteOffset() {
		return executeOffset;
	}

	public void setExecuteOffset(int executeOffset) {
		this.executeOffset = executeOffset;
	}

	public List<BuiltinAuthUser> getUsers() {
		return users;
	}

	public void setUsers(List<BuiltinAuthUser> users) {
		this.users = users;
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

}
