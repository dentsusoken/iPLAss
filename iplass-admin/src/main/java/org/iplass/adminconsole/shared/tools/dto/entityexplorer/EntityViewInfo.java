/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

public class EntityViewInfo implements Serializable {

	private static final long serialVersionUID = 9010024169560355175L;

	private String name;
	private String displayName;
	private int count;
	private int detailFormViewCount;
	private int searchFormViewCount;
	private int bulkFormViewCount;
	private String viewControl;

	private boolean isError = false;
	private String errorMessage;

	public EntityViewInfo() {
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

	public int getDetailFormViewCount() {
		return detailFormViewCount;
	}
	public void setDetailFormViewCount(int count) {
		this.detailFormViewCount = count;
	}

	public int getSearchFormViewCount() {
		return searchFormViewCount;
	}
	public void setSearchFormViewCount(int count) {
		this.searchFormViewCount = count;
	}

	public int getBulkFormViewCount() {
		return bulkFormViewCount;
	}

	public void setBulkFormViewCount(int bulkFormViewCount) {
		this.bulkFormViewCount = bulkFormViewCount;
	}

	public String getViewControl() {
		return viewControl;
	}

	public void setViewControl(String viewControl) {
		this.viewControl = viewControl;
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
