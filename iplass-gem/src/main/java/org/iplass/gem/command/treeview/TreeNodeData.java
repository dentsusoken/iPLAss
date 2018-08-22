/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.treeview;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.util.DateUtil;

public class TreeNodeData {

	private String id;
	private String type;
	private String name;
	private String defName;
	private String oid;
	private String action;
	private String viewName;
	private int level;
	private String parent;
	private boolean isLeaf;
	private boolean expanded;
	private boolean loaded;

	private Object[] values;

	public TreeNodeData(EntityDefinitionNode node, String parent, int level, String oid, Object[] values) {
		this.type = "D";
		this.name = node.getDisplayName();
		this.level = level;
		this.parent = parent;
		this.isLeaf = false;
		this.expanded = false;
		this.loaded = false;
		this.values = values;
		if (oid == null) {
			this.id = this.type + "/root/" + node.getPath();
		} else {
			this.id = this.type + "/" + oid + "/" + node.getPath();
		}
	}

	public TreeNodeData(IndexNode node, String parent, int level, String oid, int offset, Object[] values) {
		this.type = "I";
		this.name = node.getDisplayName();
		this.level = level;
		this.parent = parent;
		this.isLeaf = false;
		this.expanded = false;
		this.loaded = false;
		this.values = values;
		if (oid == null) {
			this.id = this.type + "/root/" + offset + "/" + node.getPath();
		} else {
			this.id = this.type + "/" + oid + "/" + offset + "/" + node.getPath();
		}
	}

	public TreeNodeData(EntityNode node, String parent, int level, Object[] values) {
		this.type = "E";
		this.name = node.getDisplayName();
		this.defName = node.getDefName();
		this.oid = node.getOid();
		this.action = node.getAction();
		this.viewName = node.getViewName();
		this.level = level;
		this.parent = parent;
		this.isLeaf = !node.isHasReference();
		this.expanded = false;
		this.loaded = false;
		this.values = values;
		this.id = this.type + "/" + node.getOid() + "/" + node.getPath();
	}

	public List<String> getStringValues() {
		List<String> ret = new ArrayList<>();
		for (Object value : values) {
			String _value = convertValue(value);
			ret.add(_value);
		}
		return ret;
	}

	private String convertValue(Object value) {
		if (value == null) return null;
		if (value instanceof BinaryReference) {
			return ((BinaryReference) value).getName();
		} else if (value instanceof Boolean) {
			return value.toString();
		} else if (value instanceof Date) {
			return DateUtil.getDateInstance(DateFormat.MEDIUM, false).format((java.sql.Date) value);
		} else if (value instanceof Timestamp) {
			return DateUtil.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, true).format((Timestamp)value);
		} else if (value instanceof BigDecimal) {
			return value.toString();
		} else if (value instanceof Double) {
			return value.toString();
		} else if (value instanceof Long) {
			return value.toString();
		} else if (value instanceof Entity) {
			Entity entity = (Entity) value;
			return entity.getName();
		} else if (value instanceof SelectValue) {
			SelectValue sv = (SelectValue) value;
			if (sv.getDisplayName() != null) {
				return sv.getDisplayName();
			}
		} else if (value instanceof String) {
			return value.toString();
		} else if (value instanceof Time) {
			return value.toString();
		}
		return null;
	}

	/**
	 * idを取得します。
	 * @return id
	 */
	public String getId() {
	    return id;
	}

	/**
	 * idを設定します。
	 * @param id id
	 */
	public void setId(String id) {
	    this.id = id;
	}

	/**
	 * typeを取得します。
	 * @return type
	 */
	public String getType() {
	    return type;
	}

	/**
	 * typeを設定します。
	 * @param type type
	 */
	public void setType(String type) {
	    this.type = type;
	}

	/**
	 * nameを取得します。
	 * @return name
	 */
	public String getName() {
	    return name;
	}

	/**
	 * nameを設定します。
	 * @param name name
	 */
	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * defNameを取得します。
	 * @return defName
	 */
	public String getDefName() {
	    return defName;
	}

	/**
	 * defNameを設定します。
	 * @param defName defName
	 */
	public void setDefName(String defName) {
	    this.defName = defName;
	}

	/**
	 * oidを取得します。
	 * @return oid
	 */
	public String getOid() {
	    return oid;
	}

	/**
	 * oidを設定します。
	 * @param oid oid
	 */
	public void setOid(String oid) {
	    this.oid = oid;
	}

	/**
	 * actionを取得します。
	 * @return action
	 */
	public String getAction() {
	    return action;
	}

	/**
	 * actionを設定します。
	 * @param action action
	 */
	public void setAction(String action) {
	    this.action = action;
	}

	/**
	 * viewNameを取得します。
	 * @return viewName
	 */
	public String getViewName() {
	    return viewName;
	}

	/**
	 * viewNameを設定します。
	 * @param viewName viewName
	 */
	public void setViewName(String viewName) {
	    this.viewName = viewName;
	}

	/**
	 * levelを取得します。
	 * @return level
	 */
	public int getLevel() {
	    return level;
	}

	/**
	 * levelを設定します。
	 * @param level level
	 */
	public void setLevel(int level) {
	    this.level = level;
	}

	/**
	 * parentを取得します。
	 * @return parent
	 */
	public String getParent() {
	    return parent;
	}

	/**
	 * parentを設定します。
	 * @param parent parent
	 */
	public void setParent(String parent) {
	    this.parent = parent;
	}

	/**
	 * isLeafを取得します。
	 * @return isLeaf
	 */
	public boolean isLeaf() {
	    return isLeaf;
	}

	/**
	 * isLeafを設定します。
	 * @param isLeaf isLeaf
	 */
	public void setLeaf(boolean isLeaf) {
	    this.isLeaf = isLeaf;
	}

	/**
	 * expandedを取得します。
	 * @return expanded
	 */
	public boolean isExpanded() {
	    return expanded;
	}

	/**
	 * expandedを設定します。
	 * @param expanded expanded
	 */
	public void setExpanded(boolean expanded) {
	    this.expanded = expanded;
	}

	/**
	 * loadedを取得します。
	 * @return loaded
	 */
	public boolean isLoaded() {
	    return loaded;
	}

	/**
	 * loadedを設定します。
	 * @param loaded loaded
	 */
	public void setLoaded(boolean loaded) {
	    this.loaded = loaded;
	}

	/**
	 * valuesを取得します。
	 * @return values
	 */
	public Object[] getValues() {
	    return values;
	}

	/**
	 * valuesを設定します。
	 * @param values values
	 */
	public void setValues(Object[] values) {
	    this.values = values;
	}

}
