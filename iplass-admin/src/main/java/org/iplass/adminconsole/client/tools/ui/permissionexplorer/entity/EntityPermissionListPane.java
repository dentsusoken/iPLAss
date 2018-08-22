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

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.permissionexplorer.EntityPermissionInfoDS;
import org.iplass.adminconsole.client.tools.data.permissionexplorer.PermissionListGridDS;
import org.iplass.adminconsole.client.tools.data.permissionexplorer.PermissionListGridDS.PermissionListGridRecord;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionEditDialog;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionEditPane;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionListGrid;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionListPane;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionInfo;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.menu.Menu;

/**
 * EntityPermissionListパネル
 */
public class EntityPermissionListPane extends PermissionListPane {

	private ListGrid grid;
	private EntityPermissionInfoDS ds;
	private Menu contextMenu;

	/**
	 * コンストラクタ
	 *
	 * @param mainPane 起動MainPane
	 * @param roleList ロールのList
	 */
	public EntityPermissionListPane(EntityPermissionMainPane mainPane, List<Entity> roleList) {
		super(mainPane, roleList);

		refreshGrid();
	}

	@Override
	protected void doRefreshGrid() {

		// gridを作成し直さないとエラーが発生する
		if (grid != null) {
			removeMember(grid);
			grid = null;
		}
		if (ds != null) {
			ds.destroy();
			ds = null;
		}
		if (contextMenu != null) {
			contextMenu.destroy();
			contextMenu = null;
		}

		ds = EntityPermissionInfoDS.getInstance(getRoleMap());
		contextMenu = new Menu();

		grid = new PermissionListGrid(ds, contextMenu) {

			@Override
			public void removeAllDefinitionPermission(PermissionListGridRecord record, int rowNum, String defName) {

				ds.deleteAllPermission(record);

				setEditState(true);

				refreshRow(rowNum);
			}

			@Override
			public void removeRolePermission(PermissionListGridRecord record, int rowNum, int colNum, String defName, String roleCode, int roleIndex) {

				ds.deletePermission(record, roleIndex);

				setEditState(true);

				refreshCell(rowNum, colNum);
			}

			@Override
			public void applyEditRolePermission(PermissionListGridRecord record, int rowNum, int colNum, String defName, String roleCode, int roleIndex, DataChangedEvent event) {

				Entity[] permissionArray = event.getValueObject(Entity[].class);

				ds.updatePermission(record, roleIndex, permissionArray);

				setEditState(true);

				refreshCell(rowNum, colNum);
			}

			@Override
			public void showRolePermissionEditDialog(final PermissionListGridRecord record, final String defName, final String roleCode, final int roleIndex, final PermissionEditDialog dialog) {

				Entity[] permissionArray = record.getPermission(roleIndex);

				if (permissionArray != null) {
					for (Entity permission : permissionArray) {
						//ここが個別
						PermissionEditPane permissionEditPane = new EntityPermissionEditPane(defName, getRoleEntity(roleCode), permission);
						dialog.setPermissionEditPane(permissionEditPane);
					}
				} else {
					//ここが個別
					PermissionEditPane permissionEditPane = new EntityPermissionEditPane(defName, getRoleEntity(roleCode), null);
					dialog.setPermissionEditPane(permissionEditPane);
				}

				dialog.show();
			}
		};
		setContextMenu(contextMenu);

		addMember(grid);

		Criteria criteria = new Criteria();
		if (getEditingData() != null) {
			//既に編集データが存在するので、そのデータをもとに一覧を更新

			//Criteriaに直接addでObjectをセットしようとすると無視されるのでJSOHelper経由でセット
			JSOHelper.setAttribute(criteria.getJsObj(), PermissionListGridDS.EDIT_DATA_KEY, getEditingData());
			//criteria.addCriteria("editPermissionInfoList", getEditingList());
		} else {
			//一覧を最新化するのでステータスも初期化
			setEditState(false);
		}

		grid.fetchData(criteria, new DSCallback() {
			@Override
			public void execute(DSResponse dsResponse, Object data, DSRequest dsRequest) {

				setRecordCount(grid.getTotalRows());

				//一覧を再作成したので編集中としてKEEPしたデータをクリア
				setEditingData(null);

				setProgressFinish();
			}
		});

	}

	@Override
	protected void update() {

		List<PermissionInfo> updateList = ds.getUpdatePermissionInfoList(grid.getRecords());

		if (updateList.isEmpty()) {
			//更新対象なし
			SC.say(AdminClientMessageUtil.getString("ui_tools_permissionexplorer_PermissionListPane_notExistStoreData"));
			return;
		} else {
			SmartGWTUtil.showProgress("Save ....");

			//ここが個別
			service.updateEntityPermissionData(TenantInfoHolder.getId(), updateList, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.getMessage(), caught);
					SmartGWTUtil.hideProgress();
					SC.warn(AdminClientMessageUtil.getString("ui_tools_permissionexplorer_PermissionListPane_saveErr") + caught.getMessage());
				}

				@Override
				public void onSuccess(Void result) {
					SmartGWTUtil.hideProgress();

					refreshGrid();
				}
			});
		}
	}

	@Override
	protected void doRefreshRoleList() {

		//編集中データを退避(Grid再表示完了時にクリアされる)
		setEditingData(ds.getEditPermissionData(grid.getRecords()));

		refreshGrid();
	}

}
