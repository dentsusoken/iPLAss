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

package org.iplass.adminconsole.client.base.ui.layout;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.plugin.ContentClosedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentSelectedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentStateChangeHandler;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * メイン画面右側の各種詳細情報を表示するタブセット
 */
public class MainWorkspaceTab extends TabSet {

	private static final String TAB_DEF_NAME_KEY = "mtp.admin.client.ui.MainPain.defName";

	private static MainWorkspaceTab instance;

	private Menu tabContextMenu;

	private List<ContentStateChangeHandler> handlers;

	/**
	 * インスタンスを返します。
	 *
	 * @return MainPane
	 */
	public static MainWorkspaceTab getInstance() {
		if (instance == null) {
			instance = new MainWorkspaceTab();
		}
		return instance;
	}

	private MainWorkspaceTab() {
		initialize();
	}

	private void initialize() {
		setTabBarPosition(Side.TOP);
		setWidth100();
		setHeight100();

		// 【右クリックメニュー初期化】
		tabContextMenu = new Menu();
		tabContextMenu.setWidth(110);

		// メニュー項目1(選択タブ削除)
		MenuItem singleDelItem = new MenuItem(AdminClientMessageUtil.getString("ui_MainPane_close"), "");
		singleDelItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				removeTab(getSelectedTab());
			}
		});

		// メニュー項目2(選択タブ以外のタブを削除)
		MenuItem atherDelItem = new MenuItem(AdminClientMessageUtil.getString("ui_MainPane_closeOther"), "");
		atherDelItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				String tabId = getSelectedTab().getID();
				for (Tab tab : getTabs()) {
					if (!tabId.equals(tab.getID())) {
						removeTab(tab);
					}
				}
			}
		});

		// メニュー項目3(全タブ削除)
		MenuItem allDelItem = new MenuItem(AdminClientMessageUtil.getString("ui_MainPane_closeAll"), "");
		allDelItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				for (Tab tab : getTabs()) {
					removeTab(tab);
				}
			}
		});

		tabContextMenu.setItems(singleDelItem, atherDelItem, allDelItem);

		//タブのCloseが押された時にハンドリングするため、明示的に設定
		addCloseClickHandler(new CloseClickHandler() {

			@Override
			public void onCloseClick(TabCloseClickEvent event) {
				event.cancel();
				removeTab(event.getTab());
			}
		});

		addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				fireTabSelected(event.getTab());
			}
		});

		// TODO タブを右クリックした際に上記メニューが表示されるが、
		//      右クリックしたタブが選択状態にならない。
		//      でも、"addTabContextClickHandler"とかが無い。
		//      Tabインスタンスでは"add～Handler"自体が無い。
	}

	/**
	 * <p>最後の位置にタブを追加します。</p>
	 *
	 * @param name       名前
	 * @param img        タブアイコン
	 * @param tabSubName タブサブ名（グループ名）
	 * @param contents   タブ表示コンテンツ
	 */
	public void addTab(String name, String img, String tabSubName, Canvas contents) {
		addTab(name, img, tabSubName, contents, getTabs().length);
	}

	/**
	 * <p>タブを選択します。</p>
	 * @param name
	 * @param tabSubName
	 */
	public void selectTab(String name, String tabSubName) {
		Tab tab = getTab(getTabID(name, tabSubName));
		if (tab != null) {
			selectTab(tab);
		}
	}

	/**
	 * <p>指定の位置にタブを追加します。</p>
	 *
	 * @param name        名前
	 * @param img         タブアイコン
	 * @param tabSubName  タブサブ名（グループ名）
	 * @param contents    タブ表示コンテンツ
	 * @param insertIndex タブ挿入位置
	 */
	private void addTab(String name, String img, String tabSubName, Canvas contents, int insertIndex) {

		// 対象のタブが作成済みの場合は選択
		Tab tab = getTab(getTabID(name, tabSubName));
		if (tab != null) {
			selectTab(tab);
			return;
		}

		if (insertIndex < 0) {
			insertIndex = 0;
		}
		if (insertIndex > getTabs().length) {
			insertIndex = getTabs().length;
		}
		createTab(name, img, tabSubName, contents, insertIndex);
	}

	/**
	 * 指定のTab名のタブを削除する
	 *
	 * @param name        名前
	 * @param tabSubName  タブサブ名（グループ名）
	 */
	public void removeTab(String name, String tabSubName) {
		Tab tab = getTab(getTabID(name, tabSubName));
		if(tab != null) {
			removeTab(tab);
		}
	}

    @Override
	public void removeTab(final Tab tab) {
		if (fireContentsClosed(tab)) {

			SC.ask(AdminClientMessageUtil.getString("ui_MainPane_closeConfirm") , new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if (value) {
						confirmRemoveTab(tab);
					}
				}
			});

		} else {
	    	super.removeTab(tab);
		}
    }

    private void confirmRemoveTab(Tab tab) {
    	super.removeTab(tab);
    }

	/**
	 * タブの生成
	 *
	 * @param name タブ名称
	 * @param img タブに表示するイメージファイル名
	 * @param tabSubName タブ名称(詳細等 or 日本語等)
	 * @param child タブ内に表示されるCanvas
	 * @param insertIndex タブ生成位置
	 */
	private void createTab(String name, String img, String tabSubName, Canvas child, int insertIndex) {
		final String newTabName = getTabName(name, tabSubName);
		final Tab tab = new Tab();
		tab.setContextMenu(tabContextMenu);
		final String imgHTML = Canvas.imgHTML(img);
		tab.setTitle("<span>" + imgHTML + "&nbsp;" + newTabName + "</span>");
		tab.setID(getTabID(name, tabSubName));
		tab.setPane(child);
		tab.setCanClose(true);
		tab.setAttribute(TAB_DEF_NAME_KEY, name);
		addTab(tab, insertIndex);
		selectTab(tab);
	}

	/**
	 * タブ名を設定します。
	 *
	 * @param id タブID
	 * @param img タブアイコン
	 * @param title タブタイトル
	 */
	public void setTabName(String id, String img, String title) {
		Tab tab = getTab(id);
		setTabName(tab, img, title);
	}

	/**
	 * タブ名を設定します。
	 *
	 * @param tabIndex タブIndex
	 * @param img タブアイコン
	 * @param title タブタイトル
	 */
	public void setTabName(int tabIndex, String img, String title) {
		Tab tab = getTab(tabIndex);
		setTabName(tab, img, title);
	}

	private void setTabName(Tab tab, String img, String title) {
		setTabTitle(tab, "<span>" + Canvas.imgHTML(img) + "&nbsp;" + title + "</span>");
	}

	/**
	 * タブ名を返します。
	 *
	 * @param defName 定義名
	 * @param tabSubName タブサブ名
	 * @return タブ名
	 */
	private String getTabName(String defName, String tabSubName) {
//		return (tabSubName != null && tabSubName != "" ? defName + ":" + tabSubName : defName);
		return defName;
	}

	/**
	 * タブIDを返します。
	 *
	 * @param defName 定義名
	 * @param tabSubName タブサブ名
	 * @return タブID
	 */
	public String getTabID(String defName, String tabSubName) {
		return (SmartGWTUtil.isNotEmpty(tabSubName) ? defName + tabSubName : defName).replace(".", "_").replace("/", "_").replace("-", "_");
	}

	/**
	 * タブの存在を確認します。
	 *
	 * @param defName 定義名
	 * @param tabSubName タブサブ名
	 */
	public boolean existTab(String defName, String tabSubName) {
		String tabID = getTabID(defName, tabSubName);
		Tab curTab = getTab(tabID);
		return (curTab != null);
	}

	/**
	 * タブに設定されている定義名を返します。
	 *
	 * @param Tab タブ
	 * @return 定義名
	 */
	public String getTabDefName(Tab tab) {
		return tab.getAttribute(TAB_DEF_NAME_KEY);
	}

	/**
	 * {@link ContentStateChangeHandler} を追加します。
	 *
	 * @param handler {@link ContentStateChangeHandler}
	 */
	public void addWorkspaceContentsStateChangeHandler(ContentStateChangeHandler handler) {
		if (handlers == null) {
			handlers = new ArrayList<>();
		}
		handlers.add(handler);
	}

	/**
	 * {@link ContentStateChangeHandler} を削除します。
	 *
	 * @param handler {@link ContentStateChangeHandler}
	 */
	public void removeWorkspaceContentsStateChangeHandler(ContentStateChangeHandler handler) {
		if (handlers == null) {
			return;
		}
		if (handlers.contains(handler)) {
			handlers.remove(handler);
		}
	}

	/**
	 * ContensのCloseを通知します。
	 *
	 * @param tab Close対象のTab
	 */
	private boolean fireContentsClosed(Tab tab) {
		if (handlers == null || handlers.isEmpty()) {
			return true;
		}

		ContentClosedEvent event = new ContentClosedEvent(
				tab.getPane(), tab.getAttribute(TAB_DEF_NAME_KEY));
		for (int i = handlers.size() - 1; i >= 0; i--) {
			ContentStateChangeHandler handler = handlers.get(i);
			handler.onContentClosed(event);
		}

		return event.isCloseConfirm();
	}

	/**
	 * ContensのSelectを通知します。
	 *
	 * @param tab Select対象のTab
	 */
	private void fireTabSelected(Tab tab) {
		if (handlers == null || handlers.isEmpty()) {
			return;
		}

		ContentSelectedEvent event = new ContentSelectedEvent(
				tab.getPane(), getTabDefName(tab));
		for (int i = handlers.size() - 1; i >= 0; i--) {
			ContentStateChangeHandler handler = handlers.get(i);
			handler.onContentSelected(event);
		}
	}
}
