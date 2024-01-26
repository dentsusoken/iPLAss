/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.view.treeview;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.view.treeview.TreeViewGridColModelMapping;

/**
 * ツリービューグリッドのColModel
 * @author lis3wg
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaTreeViewGridColModelMapping implements Serializable {

	private static final long serialVersionUID = 8154793749139452064L;

	/** 名前 */
	private String name;

	/** マッピング対象の名前 */
	private String mappingName;

	/**
	 * 名前を取得します。
	 * @return 名前
	 */
	public String getName() {
	    return name;
	}

	/**
	 * 名前を設定します。
	 * @param name 名前
	 */
	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * マッピング対象の名前を取得します。
	 * @return マッピング対象の名前
	 */
	public String getMappingName() {
	    return mappingName;
	}

	/**
	 * マッピング対象の名前を設定します。
	 * @param mappingName マッピング対象の名前
	 */
	public void setMappingName(String mappingName) {
	    this.mappingName = mappingName;
	}

	public void applyConfig(TreeViewGridColModelMapping mapping) {
		this.name = mapping.getName();
		this.mappingName = mapping.getMappingName();
	}

	public TreeViewGridColModelMapping currentConfig() {
		TreeViewGridColModelMapping mapping = new TreeViewGridColModelMapping();
		mapping.setName(name);
		mapping.setMappingName(mappingName);
		return mapping;
	}
}
