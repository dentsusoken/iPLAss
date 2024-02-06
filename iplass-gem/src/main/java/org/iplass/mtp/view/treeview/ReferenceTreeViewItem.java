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

package org.iplass.mtp.view.treeview;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * ツリービューにEntityの参照を表示するための定義情報
 * @author lis3wg
 */
public class ReferenceTreeViewItem extends EntityTreeViewItem {

	/** SerialVersionUID */
	private static final long serialVersionUID = 7630455710862858598L;

	/** 親アイテム */
	@XmlTransient
	@JsonIgnore
	private TreeViewItem parent;

	/** 親Entityからの参照プロパティ名 */
	private String propertyName;

	/** Entity定義階層の表示名 */
	private String displayName;

	public ReferenceTreeViewItem() {
	}

	/**
	 * 親アイテムを取得する。
	 * @return 親アイテム
	 */
	public TreeViewItem getParent() {
		return parent;
	}

	/**
	 * 親アイテムを設定する。
	 * @param parent 親アイテム
	 */
	public void setParent(TreeViewItem parent) {
		this.parent = parent;
	}

	/**
	 * プロパティ名を取得する。
	 * @return プロパティ名
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * プロパティ名を設定する。
	 * @param propertyName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * 表示名を取得する。
	 * @return 表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 表示名を設定する。
	 * @param displayName 表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
