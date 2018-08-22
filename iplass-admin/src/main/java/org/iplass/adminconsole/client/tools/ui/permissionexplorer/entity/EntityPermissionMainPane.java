/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer.entity;

import java.util.List;

import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionListMainPane;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionListPane;
import org.iplass.mtp.entity.Entity;

/**
 * EntityPermissionMainパネル
 */
public class EntityPermissionMainPane extends PermissionListMainPane {

	private static String TITLE = "Entity Permission";

	private EntityPermissionListPane listPane;

	/**
	 * コンストラクタ
	 */
	public EntityPermissionMainPane() {
		super();
	}

	@Override
	public String getTabTitle() {
		return TITLE;
	}

	@Override
	public void selectedPane(List<Entity> roleList) {

		if (listPane == null) {
			listPane = new EntityPermissionListPane(this, roleList);

			addMember(listPane);
		}
	}

	@Override
	public PermissionListPane getListPane() {
		return listPane;
	}

}
