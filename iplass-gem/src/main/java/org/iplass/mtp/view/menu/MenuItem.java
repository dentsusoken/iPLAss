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
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * メニューツリーに設定するアイテム定義
 */
@XmlRootElement
@XmlSeeAlso(value = {
		ActionMenuItem.class,
		EntityMenuItem.class,
		NodeMenuItem.class,
		UrlMenuItem.class
})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class MenuItem implements Definition {

	private static final long serialVersionUID = 6208979768417580786L;

	/** 名前 */
	private String name;
	/** 表示名 */
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;
	/** 説明 */
	private String description;

	/** メニューアイコンURL */
	private String imageUrl;

	/** アイコンタグ */
	private String iconTag;

	/** 子階層のメニューアイテム */
	private List<MenuItem> childs;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedDisplayNameList;

	/** イメージカラー */
	private String imageColor;

	/** カスタマイズ用スクリプト */
	private String customizeScript;

	/**
	 * チェック処理などを実装した {@link MenuItemVisitor} を実行します。
	 *
	 * @param <R> 戻り値
	 * @param visitor 実行処理
	 * @return 実行結果
	 */
	public abstract <R> R accept(MenuItemVisitor<R> visitor);

	/**
	 * 子階層のメニューアイテムを返します。
	 *
	 * @return childs 子階層のメニューアイテム
	 */
	public List<MenuItem> getChilds() {
	    return childs;
	}

	/**
	 * 子階層のメニューアイテムを設定します。
	 *
	 * @param childs 子階層のメニューアイテム
	 */
	public void setChilds(List<MenuItem> childs) {
	    this.childs = childs;
	}


	/**
	 * 子階層のメニューアイテムを追加します。
	 *
	 * @param menuItem 子階層のメニューアイテム
	 */
	public void addChild(MenuItem menuItem) {
		if (childs == null) {
			childs = new ArrayList<MenuItem>();
		}
		childs.add(menuItem);
	}

	/**
	 * 子階層メニューが存在するか否かを判定します。
	 *
	 * @return true：存在する/false：存在しない
	 */
	public boolean hasChild() {
		if(childs != null && childs.size() > 0) {
			return true;
		}
		return false;
	}


	/**
	 * 名前を返します。
	 *
	 * @return name 名前
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
	 * メニューアイコンURLを返します。
	 *
	 * @return メニューアイコンURL
	 */
	public String getImageUrl() {
	    return imageUrl;
	}

	/**
	 * メニューアイコンURLを設定します。
	 *
	 * @param imageUrl メニューアイコンURL
	 */
	public void setImageUrl(String imageUrl) {
	    this.imageUrl = imageUrl;
	}

	/**
	 * アイコンタグを取得します。
	 * @return アイコンタグ
	 */
	public String getIconTag() {
	    return iconTag;
	}

	/**
	 * アイコンタグを設定します。
	 * @param iconTag アイコンタグ
	 */
	public void setIconTag(String iconTag) {
	    this.iconTag = iconTag;
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
	 * イメージカラーを取得します。
	 * @return イメージカラー
	 */
	public String getImageColor() {
	    return imageColor;
	}

	/**
	 * イメージカラーを設定します。
	 * @param imageColor イメージカラー
	 */
	public void setImageColor(String imageColor) {
	    this.imageColor = imageColor;
	}

	/**
	 * カスタマイズ用スクリプトを返します。
	 *
	 * @return カスタマイズ用スクリプト
	 */
	public String getCustomizeScript() {
		return customizeScript;
	}

	/**
	 * カスタマイズ用スクリプトを設定します。
	 *
	 * @param customizeScript カスタマイズ用スクリプト
	 */
	public void setCustomizeScript(String customizeScript) {
		this.customizeScript = customizeScript;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + ((childs == null) ? 0 : childs.hashCode());
		result = prime * result + ((imageColor == null) ? 0: imageColor.hashCode());
		result = prime * result + ((customizeScript == null) ? 0 : customizeScript.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuItem other = (MenuItem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (childs == null) {
			if (other.childs != null)
				return false;
		} else if (!childs.equals(other.childs))
			return false;
		if (imageColor == null) {
			if (other.imageColor != null)
				return false;
		} else if (!imageColor.equals(other.imageColor))
			return false;
		if (customizeScript == null) {
			if (other.customizeScript != null) {
				return false;
			}
		} else if (!customizeScript.equals(other.customizeScript)) {
			return false;
		}
		return true;
	}
}
