/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.metadata.dto.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.NodeMenuItem;
import org.iplass.mtp.view.menu.UrlMenuItem;


/**
 * メニューアイテム情報を保持するHolderクラスです。
 *
 * {@link MetaDataService#getMenuItemList(int)}の戻り値です。
 */
public class MenuItemHolder implements Serializable {

	/** シリアルバージョンNo */
	private static final long serialVersionUID = -3905432523984195981L;

	/** NodeMenuItemのリスト */
	private List<NodeMenuItem> nodeMenuItemList;

	/** ActionMenuItemのリスト */
	private List<ActionMenuItem> actionMenuItemList;

	/** EntityMenuItemのリスト */
	private List<EntityMenuItem> entityMenuItemList;

	/** UrlMenuItemのリスト */
	private List<UrlMenuItem> urlMenuItemList;

	/**
	 * コンストラクタ
	 */
	public MenuItemHolder() {
		nodeMenuItemList = new ArrayList<NodeMenuItem>();
		actionMenuItemList = new ArrayList<ActionMenuItem>();
		entityMenuItemList = new ArrayList<EntityMenuItem>();
		urlMenuItemList = new ArrayList<UrlMenuItem>();
	}

	/**
	 * NodeMenuItemリストを返します。
	 *
	 * @return nodeMenuItemList
	 */
	public List<NodeMenuItem> getNodeMenuItemList() {
		return nodeMenuItemList;
	}

	/**
	 * NodeMenuItemリストを設定します。
	 *
	 * @param nodeMenuItemList NodeMenuItemリスト
	 */
	public void setNodeMenuItemList(List<NodeMenuItem> nodeMenuItemList) {
		this.nodeMenuItemList = nodeMenuItemList;
	}

	/**
	 * ActionMenuItemリストを返します。
	 *
	 * @return actionMenuItemList
	 */
	public List<ActionMenuItem> getActionMenuItemList() {
		return actionMenuItemList;
	}

	/**
	 * ActionMenuItemリストを設定します。
	 *
	 * @param actionMenuItemList ActionMenuItemリスト
	 */
	public void setActionMenuItemList(List<ActionMenuItem> actionMenuItemList) {
		this.actionMenuItemList = actionMenuItemList;
	}

	/**
	 * EntityMenuItemリストを返します。
	 *
	 * @return entityMenuItemList
	 */
	public List<EntityMenuItem> getEntityMenuItemList() {
		return entityMenuItemList;
	}

	/**
	 * EntityMenuItemリストを設定します。
	 *
	 * @param entityMenuItemList EntityMenuItemリスト
	 */
	public void setEntityMenuItemList(List<EntityMenuItem> entityMenuItemList) {
		this.entityMenuItemList = entityMenuItemList;
	}

	/**
	 * UrlMenuItemリストを返します。
	 *
	 * @return urlMenuItemList
	 */
	public List<UrlMenuItem> getUrlMenuItemList() {
		return urlMenuItemList;
	}

	/**
	 * UrlMenuItemリストを設定します。
	 *
	 * @param urlMenuItemList UrlMenuItemリスト
	 */
	public void setUrlMenuItemList(List<UrlMenuItem> urlMenuItemList) {
		this.urlMenuItemList = urlMenuItemList;
	}

	/**
	 * MenuItemを追加します。
	 *
	 * @param item MenuItem
	 */
	public void addMenuItem(MenuItem item) {
		if (item instanceof NodeMenuItem) {
			addMenuItem((NodeMenuItem)item);
		} else if (item instanceof ActionMenuItem) {
			addMenuItem((ActionMenuItem)item);
		} else if (item instanceof EntityMenuItem) {
			addMenuItem((EntityMenuItem)item);
		} else if (item instanceof UrlMenuItem) {
			addMenuItem((UrlMenuItem)item);
		}
	}

	/**
	 * NodeMenuItemを追加します。
	 *
	 * @param item NodeMenuItem
	 */
	public void addMenuItem(NodeMenuItem item) {
		nodeMenuItemList.add(item);
	}

	/**
	 * ActionMenuItemを追加します。
	 *
	 * @param item ActionMenuItem
	 */
	public void addMenuItem(ActionMenuItem item) {
		actionMenuItemList.add(item);
	}

	/**
	 * EntityMenuItemを追加します。
	 *
	 * @param item EntityMenuItem
	 */
	public void addMenuItem(EntityMenuItem item) {
		entityMenuItemList.add(item);
	}

	/**
	 * UrlMenuItemを追加します。
	 *
	 * @param item UrlMenuItem
	 */
	public void addMenuItem(UrlMenuItem item) {
		urlMenuItemList.add(item);
	}
}
