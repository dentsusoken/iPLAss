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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer;

import java.util.List;

import org.iplass.mtp.entity.Entity;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public abstract class PermissionListMainPane extends VLayout {

	private Tab tab;

	public PermissionListMainPane() {
		tab = new Tab();

		setWidth100();

		tab.setTitle(getTabTitle());
	}

	/**
	 * タブのタイトルを返します。
	 *
	 * @return タブタイトル
	 */
	public abstract String getTabTitle();

	/**
	 * <p>タブの選択処理を実装します。</p>
	 *
	 * <p>起動処理の高速化のため、コンストラクタではMainPaneで保持するListPaneを生成せず、
	 * タブが選択された際にListPaneを生成する。
	 * この生成処理をこのメソッド内で実装してください。</p>
	 *
	 * @param roleList ロールEntity
	 */
	public abstract void selectedPane(List<Entity> roleList);

	/**
	 * <p>編集用のListPaneを返します。</p>
	 *
	 * @return 編集用のListPane
	 */
	public abstract PermissionListPane getListPane();

	/**
	 * 表示するタブを返します。
	 *
	 * @return 表示対象タブ
	 */
	final protected Tab getTab() {
		return tab;
	}

	/**
	 * <p>編集中かを返します。</p>
	 *
	 * @return true:編集中
	 */
	final public boolean isEditing() {
		if (getListPane() == null) {
			return false;
		}

		return getListPane().isEditing();
	}

	/**
	 * ロールの変更をListPaneに通知します。
	 *
	 * @param roleList ロールEntity
	 */
	final public void changeRoleList(List<Entity> roleList) {
		if (getListPane() != null) {
			getListPane().changeRoleList(roleList);
		}
	}

}
