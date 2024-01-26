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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.menu.MenuItem;

public class MetaMenuItem implements Serializable {

	private static final long serialVersionUID = -3781564129837689678L;

	/** 参照Menu定義ID */
	private String menuRefId;

	/** 子メニュー */
	private List<MetaMenuItem> childs;

	public MetaMenuItem copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * メニュー定義IDを返します。
	 *
	 * @return メニュー定義ID
	 */
	public String getMenuRefId() {
	    return menuRefId;
	}

	/**
	 * メニュー定義IDを設定します。
	 *
	 * @param menuRefId メニュー定義ID
	 */
	public void setMenuRefId(String menuRefId) {
	    this.menuRefId = menuRefId;
	}

	/**
	 * 子メニューを返します。
	 *
	 * @return 子メニュー
	 */
	public List<MetaMenuItem> getChilds() {
	    return childs;
	}

	/**
	 * 子メニューを設定します。
	 * @param childs 子メニュー
	 */
	public void setChilds(List<MetaMenuItem> childs) {
		this.childs = childs;
	}

	/**
	 * 子メニューを追加します。
	 *
	 * @param metaMenuItem 追加するメニュー
	 */
	public void addChild(MetaMenuItem metaMenuItem) {
		if(childs == null) {
			childs = new ArrayList<MetaMenuItem>();
		}
		childs.add(metaMenuItem);
	}

	//Definition → Meta
	public void applyConfig(MenuItem definition) {

		//name -> id 変換
		MenuItemService service = ServiceRegistry.getRegistry().getService(MenuItemService.class);
		MetaMenu.MetaMenuHandler handler = service.getRuntimeByName(definition.getName());
		menuRefId = handler.getMetaData().getId();

		if (definition.getChilds() != null) {
			childs = new ArrayList<MetaMenuItem>(definition.getChilds().size());
			for (MenuItem item : definition.getChilds()) {
				MetaMenuItem child = new MetaMenuItem();
				child.applyConfig(item);
				childs.add(child);
			}
		} else {
			childs = null;
		}
	}

	//Meta → Definition
	public MenuItem currentConfig() {

		MenuItemService service = ServiceRegistry.getRegistry().getService(MenuItemService.class);
		MetaMenu.MetaMenuHandler handler = service.getRuntimeById(menuRefId);

		if(handler == null) {
			return null;
		}
		if(handler instanceof MetaNodeMenu.MetaNodeMenuHandler && (childs == null || childs.size() == 0)) {
			// Nodeで子階層が0の場合はNullを返す。
			return null;
		}

		MenuItem item = handler.getMetaData().currentConfig();
		if(item == null) {
			return  null;
		}
		// 子供がいない場合は終了
		if(childs == null) {
			return item;
		}

		// 子供の階層作成
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>(childs.size());
		for (MetaMenuItem metaMenuItem : childs) {
			MenuItem child = metaMenuItem.currentConfig();
			if(child != null) {
				menuItems.add(child);
			}
		}
		if(menuItems.size() != 0) {
			menuItems.trimToSize();
			item.setChilds(menuItems);
		}
		return item;

	}

}
