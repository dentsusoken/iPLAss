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
import java.sql.Timestamp;

public class CrawlEntityInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2727101305802031334L;

	private String name;
	private String displayName;
	private int count;
	private Timestamp lastCrawlDate;
	private Timestamp updateDate;

	private boolean isError = false;
	private String errorMessage;

	public CrawlEntityInfo() {
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public Timestamp getLastCrawlDate() {
		return lastCrawlDate;
	}

	public void setLastCrawlDate(Timestamp upDate) {
		this.lastCrawlDate = upDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}
}
