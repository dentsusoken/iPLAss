/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.datalist;

import org.iplass.adminconsole.client.tools.ui.entityexplorer.EntityExplorerMainPane;

import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entity Explorer Mainパネル
 */
public class EntityDataListMainPane extends VLayout {

	private EntityExplorerMainPane owner;

	private EntityListPane entityListPane;
//	private EntityDataListPane dataListPane;
	private EntityDataListPane dataListPane2;

	/**
	 * コンストラクタ
	 */
	public EntityDataListMainPane(EntityExplorerMainPane owner) {
		this.owner = owner;

		//レイアウト設定
		setWidth100();

		//初期表示時にEntity一覧を作成
		entityListPane = new EntityListPane(this);

		addMember(entityListPane);
	}

	/**
	 * Entity一覧画面に戻ります。
	 */
	public void backEntityListPane() {

		entityListPane.show();

		if (dataListPane2 != null) {
			dataListPane2.hide();
			dataListPane2.destroy();
			dataListPane2 = null;
		}

		setWorkspaceTabName(null);
	}

	/**
	 * Entityデータ一覧画面を表示します。
	 *
	 * @param entityName 対象Entity
	 */
	public void showDataListPane(String entityName) {
		if (dataListPane2 == null) {
			dataListPane2 = new EntityDataListPane(this, entityName);
			addMember(dataListPane2);
		}

		dataListPane2.show();

		entityListPane.hide();
	}

	public void setWorkspaceTabName(String entityName) {
		owner.setWorkspaceTabName(entityName);
	}
}
