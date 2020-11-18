/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.menu;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * メニューのツリー構造定義
 *
 * {@link MenuItem} を子階層として保持します。
 */
@XmlRootElement
public class MenuTree implements Definition {

	private static final long serialVersionUID = -4537637892170923373L;

	/** 名前 */
	private String name;
	/** 表示名 */
	@MultiLang(itemNameGetter = "getName")
	private String displayName;
	/** 説明 */
	private String description;

	/** 表示順序 */
	private Integer displayOrder;

	/** 定義の表示名を表示かどうか */
	private boolean isShowMenuDisplayName;

	/** 子階層のメニューアイテム */
	private List<MenuItem> menuItems;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedDisplayNameList;

	/**
	 * 名前を返します。
	 * @return 名前
	 */
	public String getName() {
		return name;
	}

	/**
	 * 名前を設定します。
	 *
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 表示名を返します。
	 *
	 * @return 表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 表示名を設定します。
	 *
	 * @param displayName 表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 説明を返します。
	 *
	 * @return 説明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 説明を設定します。
	 *
	 * @param description 説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 表示順序を取得します。
	 * @return 表示順序
	 */
	public Integer getDisplayOrder() {
	    return displayOrder;
	}

	/**
	 * 表示順序を設定します。
	 * @param displayOrder 表示順序
	 */
	public void setDisplayOrder(Integer displayOrder) {
	    this.displayOrder = displayOrder;
	}

	/**
	 * 定義の表示名を表示かどうかを取得します。
	 * @return 定義の表示名を表示かどうか
	 */
	public boolean getIsShowMenuDisplayName() {
	    return isShowMenuDisplayName;
	}

	/**
	 * 定義の表示名を表示かどうかを設定します。
	 * @param showMenuDisplayName 定義の表示名を表示かどうか
	 */
	public void setIsShowMenuDisplayName(boolean isShowMenuDisplayName) {
	    this.isShowMenuDisplayName = isShowMenuDisplayName;
	}

	/**
	 * 子階層のメニューアイテムを返します。
	 *
	 * @return 子階層のメニューアイテム
	 */
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	/**
	 * 子階層のメニューアイテムを設定します。
	 *
	 * @param menuItems 子階層のメニューアイテム
	 */
	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	/**
	 * 子階層のメニューアイテムを追加します。
	 *
	 * @param menuItem 追加するメニューアイテム
	 */
	public void addMenuItem(MenuItem menuItem) {
		if (menuItems == null) {
			menuItems = new ArrayList<MenuItem>();
		}
		menuItems.add(menuItem);
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedDisplayName(LocalizedStringDefinition localizedDisplayName) {
		if (localizedDisplayNameList == null) {
			localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedDisplayNameList.add(localizedDisplayName);
	}

	/**
	 * チェック処理などを実装した {@link MenuItemVisitor} を実行します。
	 *
	 * @param <R> 戻り値
	 * @param visitor 実行処理
	 * @return 実行結果
	 */
	public <R> R accept(MenuItemVisitor<R> visitor) {
		if (menuItems == null || menuItems.size() == 0) {
			return visitor.visitNoMenu(this);
		}
		return visitor.visit(this);
	}

}
