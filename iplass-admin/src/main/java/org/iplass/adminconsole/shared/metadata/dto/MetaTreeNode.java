/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.metadata.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ツリー形式のメタデータ情報。
 */
public class MetaTreeNode extends MetaDataInfo {

	private static final long serialVersionUID = 5573251772123576076L;

	private String displayName;
	private String description;

	private String state;
	private int version;
	private String repository;

	private Timestamp createDate;
	private Timestamp updateDate;

	private List<MetaTreeNode> children;
	private List<MetaTreeNode> items;

	private boolean isError = false;
	private String errorMessage;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public List<MetaTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<MetaTreeNode> children) {
		this.children = children;
	}

	public void addChild(MetaTreeNode child) {
		if (children == null) {
			children = new ArrayList<MetaTreeNode>();
		}
		children.add(child);
	}

	public List<MetaTreeNode> getItems() {
		return items;
	}

	public void setItems(List<MetaTreeNode> items) {
		this.items = items;
	}

	public void addItem(MetaTreeNode item) {
		if (items == null) {
			items = new ArrayList<MetaTreeNode>();
		}
		items.add(item);
	}

	public int getAllNodeCount() {
		int count = children != null ? children.size() : 0;
		return count + (items != null ? items.size() : 0);
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

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
