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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer.role;

import java.util.List;

import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionListMainPane;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionListPane;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.role.RoleListPane.RoleDataChangeHandler;
import org.iplass.mtp.entity.Entity;

public class RoleMainPane extends PermissionListMainPane {

	private static String TITLE = "Role";

	private RoleListPane listPane;

	public RoleMainPane() {
		super();
	}

	@Override
	public String getTabTitle() {
		return TITLE;
	}

	@Override
	public void selectedPane(List<Entity> roleList) {
		//RoleListPaneではRoleは不要
		if (listPane == null) {
			listPane = new RoleListPane(this);

			addMember(listPane);
		}
	}

	@Override
	public PermissionListPane getListPane() {
		return listPane;
	}

	/**
	 * <p>{@link RoleDataChangeHandler} を追加します。</p>
	 *
	 * <p>ロール編集画面で登録済のロールデータを最新化した際に、
	 * 各Handlerに検索したロールデータを送信します。</p>
	 *
	 * @param handler {@link RoleDataChangeHandler}
	 */
	public void addRoleDataChangeHandler(RoleDataChangeHandler handler) {

		if (listPane == null) {
			selectedPane(null);
		}
		listPane.addRoleDataChangeHandler(handler);
	}

}
