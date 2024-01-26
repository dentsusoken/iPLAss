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

package org.iplass.adminconsole.shared.tools.dto.entityexplorer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntityDataCountResultInfo implements Serializable {

	private static final long serialVersionUID = -6546458066487702484L;

	private boolean isError = false;

	private List<String> messages;

	private int allCount;
	private int targetCount;

	/**
	 * コンストラクタ
	 */
	public EntityDataCountResultInfo() {
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

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setTargetCount(int targetCount) {
		this.targetCount = targetCount;
	}

	public int getTargetCount() {
		return targetCount;
	}

}
