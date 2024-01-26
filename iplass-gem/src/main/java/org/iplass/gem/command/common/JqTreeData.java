/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.common;

import java.util.List;

/**
 * jqTree用の共通データ。
 * WebAPIからjsonデータを返す際に利用。
 */
public class JqTreeData {

	private String name;

	private String id;

	private List<JqTreeData> children;

	private boolean load_on_demand;

	public JqTreeData() {
	}

	public JqTreeData(String name, String id, List<JqTreeData> children) {
		this.name = name;
		this.id = id;
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<JqTreeData> getChildren() {
		return children;
	}

	public void setChildren(List<JqTreeData> children) {
		this.children = children;
	}

	public boolean isLoad_on_demand() {
		return load_on_demand;
	}

	public void setLoad_on_demand(boolean load_on_demand) {
		this.load_on_demand = load_on_demand;
	}
}
