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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.plugin.ContentClosedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentSelectedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentStateChangeHandler;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.role.RoleMainPane;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.role.RoleListPane.RoleDataChangeEvent;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.role.RoleListPane.RoleDataChangeHandler;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * PermissionExplorerMainパネル
 */
public class PermissionExplorerMainPane extends VLayout implements ContentStateChangeHandler {

	private TabSet tabSet;

	private RoleMainPane rolePane;

	private boolean isSetupMainPane = false;
	private List<PermissionListMainPane> mainPaneList = new ArrayList<PermissionListMainPane>();

	private List<Entity> roleList;

	private PermissionExplorerMainPaneController controller = GWT.create(PermissionExplorerMainPaneController.class);

	/**
	 * コンストラクタ
	 *
	 */
	public PermissionExplorerMainPane() {

		//レイアウト設定
		setWidth100();

		tabSet = new TabSet();
		tabSet.setTabBarPosition(Side.TOP);
		addMember(tabSet);

		//RolePaneのみ生成して、ロールのリストを取得
		setupRolePane();
	}

	@Override
	public void onContentClosed(ContentClosedEvent event) {
		// 未使用
	}

	@Override
	public void onContentSelected(ContentSelectedEvent event) {
		// 未使用
	}

	public boolean isEditing() {
		if (rolePane != null && rolePane.isEditing()) {
			return true;
		}
		for (PermissionListMainPane mainPane : mainPaneList) {
			if (mainPane.isEditing()) {
				return true;
			}
		}
		return false;
	}

	private void setupRolePane() {
		rolePane = new RoleMainPane();
		rolePane.addRoleDataChangeHandler(new RoleDataChangeHandler() {
			@Override
			public void onRoleDataChanged(RoleDataChangeEvent event) {
				boolean isChange = setRoleList(event.getRoleList());
				if (!isSetupMainPane) {
					//まだMainPaneを生成していない場合は生成
					setupMainPanes();
					isSetupMainPane = true;
				} else {
					if (isChange) {
						//ロールに変更がある場合は通知
						fireRoleDataChanged();
					}
				}
			}
		});

		Tab tab = rolePane.getTab();
		tab.setPane(rolePane);
		tab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				rolePane.selectedPane(null);
			}
		});
		tabSet.addTab(tab);
	}

	private void setupMainPanes() {

		for (PermissionListMainPane mainPane : controller.mainPaneList()) {
			addTab(mainPane);
		}
	}

	private void addTab(final PermissionListMainPane mainPane) {
		Tab tab = mainPane.getTab();
		tab.setPane(mainPane);
		tab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				mainPane.selectedPane(roleList);
			}
		});
		tabSet.addTab(tab);
		mainPaneList.add(mainPane);
	}

	/**
	 * ロールリストをセットします。
	 * 現在のリストに対して変更がある場合はtrueを返します。
	 *
	 * @param newRoleList
	 * @return true:変更あり
	 */
	private boolean setRoleList(List<Entity> newRoleList) {
		if (roleList == null && newRoleList == null) {
			return false;
		}
		boolean isChange = false;
		if (roleList == null && newRoleList != null) {
			isChange = true;
		} else if (roleList != null && newRoleList == null) {
			isChange = true;
		} else if (roleList.size() != newRoleList.size()) {
			isChange = true;
		} else {
			for (Entity current : roleList) {
				boolean isMatch = false;
				for (Entity update : newRoleList) {
					if (current.getOid().equals(update.getOid())) {
						isMatch = true;
						if (!current.getName().equals(update.getName())
								|| !current.getValue("code").equals(update.getValue("code"))) {
							isChange = true;
						}
						break;
					}
				}
				if (!isMatch) {
					isChange = true;
				}
				if (isChange) {
					break;
				}
			}
		}

		roleList = newRoleList;

		return isChange;
	}

	private void fireRoleDataChanged() {
		for (PermissionListMainPane mainPane : mainPaneList) {
			mainPane.changeRoleList(roleList);
		}
	}

}
