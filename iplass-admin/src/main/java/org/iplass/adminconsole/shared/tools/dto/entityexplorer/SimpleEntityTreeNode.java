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

package org.iplass.adminconsole.shared.tools.dto.entityexplorer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleEntityTreeNode implements Serializable {

	private static final long serialVersionUID = -9081264568050691891L;

	private String path;

	private String name;
	private String displayName;
	private int count;
	private int listenerCount;
	private int detailFormViewCount;
	private int searchFormViewCount;
	private String repository;

	private List<SimpleEntityTreeNode> children;
	private List<SimpleEntityTreeNode> items;

	private boolean isError = false;
	private String errorMessage;

	/**
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path セットする path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName セットする displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 *            セットする repository
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	/**
	 * @return children
	 */
	public List<SimpleEntityTreeNode> getChildren() {
		return children;
	}

	/**
	 * @param children セットする children
	 */
	public void setChildren(List<SimpleEntityTreeNode> children) {
		this.children = children;
	}

	/**
	 * @param child 追加する child
	 */
	public void addChild(SimpleEntityTreeNode child) {
		if (children == null) {
			children = new ArrayList<SimpleEntityTreeNode>();
		}
		children.add(child);
	}

	/**
	 * @return items
	 */
	public List<SimpleEntityTreeNode> getItems() {
		return items;
	}

	/**
	 * @param items セットする items
	 */
	public void setItems(List<SimpleEntityTreeNode> items) {
		this.items = items;
	}

	/**
	 * @param item 追加する item
	 */
	public void addItem(SimpleEntityTreeNode item) {
		if (items == null) {
			items = new ArrayList<SimpleEntityTreeNode>();
		}
		items.add(item);
	}

	public int getAllNodeCount() {
		int count = children != null ? children.size() : 0;
		return count + (items != null ? items.size() : 0);
	}

	/**
	 * @return isError
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * @param isError セットする isError
	 */
	public void setError(boolean isError) {
		this.isError = isError;
	}

	/**
	 * @return errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage セットする errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count セットする count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return listenerCount
	 */
	public int getListenerCount() {
		return listenerCount;
	}

	/**
	 * @param listenerCount セットする listenerCount
	 */
	public void setListenerCount(int listenerCount) {
		this.listenerCount = listenerCount;
	}

	/**
	 * @return detailFormViewCount
	 */
	public int getDetailFormViewCount() {
		return detailFormViewCount;
	}

	/**
	 * @param detailFormViewCount セットする detailFormViewCount
	 */
	public void setDetailFormViewCount(int detailFormViewCount) {
		this.detailFormViewCount = detailFormViewCount;
	}

	/**
	 * @return searchFormViewCount
	 */
	public int getSearchFormViewCount() {
		return searchFormViewCount;
	}

	/**
	 * @param searchFormViewCount セットする searchFormViewCount
	 */
	public void setSearchFormViewCount(int searchFormViewCount) {
		this.searchFormViewCount = searchFormViewCount;
	}

}
