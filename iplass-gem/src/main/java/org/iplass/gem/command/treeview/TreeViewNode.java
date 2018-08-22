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

/**
 * Commandから参照するためのツリーのデータ構造
 * @author lis3wg
 */
public abstract class TreeViewNode {

	/** ノードのタイプ */
	protected String type;

	/** ノードのパス */
	protected String path;

	/** 表示名 */
	protected String displayName;

	/** アイコンのURL */
	protected String icon;

	/** スタイルシートのクラス */
	protected String cssStyle;

	/**
	 * ノードのタイプを取得します。
	 * @return ノードのタイプ
	 */
	public String getType() {
		return type;
	}

	/**
	 * ノードのタイプを設定します。
	 * @param type ノードのタイプ
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * ノードのパスを取得します。
	 * @return ノードのパス
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 表示名を取得します。
	 * @return 表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 表示名を設定します。
	 * @param displayName 表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * アイコンのURLを取得します。
	 * @return アイコンのURL
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * アイコンのURLを設定します。
	 * @param icon アイコンのURL
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * スタイルシートのクラスを取得します。
	 * @return スタイルシートのクラス
	 */
	public String getCssStyle() {
		return cssStyle;
	}

	/**
	 * スタイルシートのクラスを設定します。
	 * @param cssStyle スタイルシートのクラス
	 */
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

}
