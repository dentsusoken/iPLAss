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

package org.iplass.mtp.impl.view.menu;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuTree;

@XmlRootElement
public class MetaTreeMenu extends BaseRootMetaData implements DefinableMetaData<MenuTree> {

	private static final long serialVersionUID = 4592136234533462455L;

	/** 表示順序 */
	private Integer displayOrder;

	/** 定義の表示名を表示かどうか */
	private boolean showMenuDisplayName;

	/** 子階層メニュ */
	private List<MetaMenuItem> childs;

	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new MetaTreeMenuHandler();
	}

	@Override
	public MetaTreeMenu copy() {
		return ObjectUtil.deepCopy(this);
	}

	//Definition → Meta
	public void applyConfig(MenuTree definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		description = definition.getDescription();
		displayOrder = definition.getDisplayOrder();
		showMenuDisplayName = definition.isShowMenuDisplayName();

		if (definition.getMenuItems() != null) {
			childs = new ArrayList<MetaMenuItem>(definition.getMenuItems().size());
			for (MenuItem item : definition.getMenuItems()) {
				MetaMenuItem child = new MetaMenuItem();
				child.applyConfig(item);
				childs.add(child);
			}

		} else {
			childs = null;
		}

		// 言語毎の文字情報設定
		localizedDisplayNameList = I18nUtil.toMeta(definition.getLocalizedDisplayNameList());
	}

	//Meta → Definition
	public MenuTree currentConfig() {
		MenuTree definition = new MenuTree();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		definition.setDisplayOrder(displayOrder);
		definition.setShowMenuDisplayName(showMenuDisplayName);

		if (childs != null) {
			ArrayList<MenuItem> items = new ArrayList<MenuItem>(childs.size());
			for (MetaMenuItem child : childs) {
				MenuItem item = child.currentConfig();
				if (item != null) {
					items.add(item);
				}
			}
			if (items.size() > 0) {
				items.trimToSize();
			}
			definition.setMenuItems(items);
		}
		// 言語毎の文字情報設定
		definition.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));
		return definition;
	}

	public class MetaTreeMenuHandler extends BaseMetaDataRuntime {
		@Override
		public MetaTreeMenu getMetaData() {
			return MetaTreeMenu.this;
		}
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
	public boolean isShowMenuDisplayName() {
	    return showMenuDisplayName;
	}

	/**
	 * 定義の表示名を表示かどうかを設定します。
	 * @param showMenuDisplayName 定義の表示名を表示かどうか
	 */
	public void setShowMenuDisplayName(boolean showMenuDisplayName) {
	    this.showMenuDisplayName = showMenuDisplayName;
	}

	/**
	 * 子階層メニュを返します。
	 *
	 * @return 子階層メニュ
	 */
	public List<MetaMenuItem> getChilds() {
	    return childs;
	}

	/**
	 * 子階層メニュを設定します。
	 *
	 * @param childs 子階層メニュ
	 */
	public void setChilds(List<MetaMenuItem> childs) {
	    this.childs = childs;
	}

	/**
	 * 子階層メニュを追加します。
	 *
	 * @param metaMenuItem 子階層メニュ
	 */
	public void addChild(MetaMenuItem metaMenuItem) {
		if(childs == null) {
			childs = new ArrayList<MetaMenuItem>();
		}
		childs.add(metaMenuItem);
	}

}
