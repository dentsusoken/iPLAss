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

package org.iplass.adminconsole.shared.tools.dto.metaexplorer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.shared.tools.dto.metaexplorer.Message.Level;

public class DeleteResultInfo implements Serializable {

	private static final long serialVersionUID = 8359889126369383724L;

	private boolean isError = false;
	private boolean isWarn = false;
	private int deleteCount = 0;
	private int errorCount = 0;
	private int warnCount = 0;

	private List<Message> messages;

	/**
	 * コンストラクタ
	 */
	public DeleteResultInfo() {
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;

		isError = false;
		isWarn = false;
		if (messages != null) {
			for (Message message : messages) {
				checkStatus(message.getLevel());
			}
		}
	}

	public void addMessage(Message message) {
		if (messages == null) {
			messages = new ArrayList<Message>();
		}
		messages.add(message);

		checkStatus(message.getLevel());
	}

	public void addMessage(Level level, String message) {
		addMessage(new Message(level, message));
	}

	public void addErrorMessage(String message) {
		addMessage(Level.ERROR, message);
	}
	public void addWarnMessage(String message) {
		addMessage(Level.WARN, message);
	}
	public void addInfoMessage(String message) {
		addMessage(Level.INFO, message);
	}

	public void clearMessages() {
		this.messages = null;
		this.deleteCount = 0;
		this.errorCount = 0;
		this.warnCount = 0;
		this.isError = false;
		this.isWarn = false;
	}

	public boolean isError() {
		return isError;
	}
	public boolean isWarn() {
		return isWarn;
	}

	public int getDeleteCount() {
		return deleteCount;
	}
	public void setDeleteCount(int deleteCount) {
		this.deleteCount = deleteCount;
	}
	public void addDeleteCount() {
		deleteCount++;
	}

	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	public void addErrorCount() {
		errorCount++;
	}

	public int getWarnCount() {
		return warnCount;
	}
	public void setWarnCount(int warnCount) {
		this.warnCount = warnCount;
	}
	public void addWarnCount() {
		warnCount++;
	}

	private void checkStatus(Level level) {
		checkError(level);
		checkWarn(level);
	}
	private void checkError(Level level) {
		if (Level.ERROR.equals(level)) {
			isError = true;
		}
	}
	private void checkWarn(Level level) {
		if (Level.WARN.equals(level)) {
			isWarn = true;
		}
	}

}
