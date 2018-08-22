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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer.webapi;


import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.permissionexplorer.PermissionTreeGridDS;
import org.iplass.adminconsole.client.tools.data.permissionexplorer.WebApiPermissionInfoDS;
import org.iplass.adminconsole.client.tools.data.permissionexplorer.PermissionTreeGridDS.PermissionTreeNode;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionEditDialog;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionEditPane;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionListPane;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionTreeGrid;
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
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * WebApiPermissionListパネル
 */
public class WebApiPermissionListPane extends PermissionListPane {

	private PermissionTreeGrid grid;
	private PermissionTreeGridDS ds;
	private Menu contextMenu;

	//一覧リフレッシュ時の展開状態保存用
	private TreeNode[] editingOpenFolders;

	/**
	 * コンストラクタ
	 *
	 * @param mainPane 起動MainPane
	 * @param roleList ロールのList
	 */
	public WebApiPermissionListPane(WebApiPermissionMainPane mainPane, List<Entity> roleList) {
		super(mainPane, roleList);

		//Treeの為TotalCount非表示
		setHiddenCountLabel();

		//Treeの収縮ボタンの表示
		setShowExpandAllButton();
		setShowContractAllButton();

		refreshGrid();
	}

	@Override
	protected void doGridExpandAll() {
		if (grid != null) {
			//対象が多いと時間がかかるのでProgress表示(効かない、先にhideまで処理が進んでしまう)
			//SmartGWTUtil.showProgress();
			grid.getTree().openAll();
			//SmartGWTUtil.hideProgress();
		}
	}


	@Override
	protected void doGridContractAll() {
		if (grid != null) {
			grid.getTree().closeAll();
			grid.getTree().openFolders(
					grid.getTree().getChildren(grid.getTree().getRoot()));
		}
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

		ds = WebApiPermissionInfoDS.getInstance(getRoleMap());
		contextMenu = new Menu();

		grid = new PermissionTreeGrid(ds, contextMenu) {

			@Override
			public void removeAllDefinitionPermission(PermissionTreeNode record, int rowNum, String defName) {

				ds.deleteAllPermission(record);

				setEditState(true);

				refreshRow(rowNum);
			}

			@Override
			public void removeRolePermission(PermissionTreeNode record, int rowNum, int colNum, String defName, String roleCode, int roleIndex) {

				ds.deletePermission(record, roleIndex);

				setEditState(true);

				refreshCell(rowNum, colNum);
			}

			@Override
			public void applyEditRolePermission(PermissionTreeNode record, int rowNum, int colNum, String defName, String roleCode, int roleIndex, DataChangedEvent event) {

				Entity[] permissionArray = event.getValueObject(Entity[].class);

				ds.updatePermission(record, roleIndex, permissionArray);

				setEditState(true);

				refreshCell(rowNum, colNum);
			}

			@Override
			public void showRolePermissionEditDialog(final PermissionTreeNode record, final String defName, final String roleCode, final int roleIndex, final PermissionEditDialog dialog) {

				Entity[] permissionArray = record.getPermission(roleIndex);

				if (permissionArray != null) {
					for (Entity permission : permissionArray) {
						//ここが個別
						PermissionEditPane permissionEditPane = new WebApiPermissionEditPane(defName, getRoleEntity(roleCode), permission);
						dialog.setPermissionEditPane(permissionEditPane);
					}
				} else {
					//ここが個別
					PermissionEditPane permissionEditPane = new WebApiPermissionEditPane(defName, getRoleEntity(roleCode), null);
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
			JSOHelper.setAttribute(criteria.getJsObj(), PermissionTreeGridDS.EDIT_DATA_KEY, getEditingData());
			//criteria.addCriteria("editPermissionInfoList", getEditingList());
		} else {
			//一覧を最新化するのでステータスも初期化
			setEditState(false);
		}

		grid.fetchData(criteria, new DSCallback() {
			@Override
			public void execute(DSResponse dsResponse, Object data, DSRequest dsRequest) {

				//setRecordCount(grid.getTotalRows());

				if (editingOpenFolders != null) {
					//編集中の状態が保持されている場合は、再現
					grid.openFolders(editingOpenFolders);
				} else {
					grid.expandRoot();
				}

				//一覧を再作成したので編集中としてKEEPしたデータをクリア
				setEditingData(null);
				editingOpenFolders = null;

				setProgressFinish();
			}
		});
	}

	@Override
	protected void update() {

		List<PermissionInfo> updateList = ds.getUpdatePermissionInfoList(grid.getData());

		if (updateList.isEmpty()) {
			//更新対象なし
			SC.say(AdminClientMessageUtil.getString("ui_tools_permissionexplorer_PermissionListPane_notExistStoreData"));
			return;
		} else {
			SmartGWTUtil.showProgress("Save ....");

			//編集中のTree展開状態を退避(Grid再表示完了時にクリアされる)
			editingOpenFolders = grid.getOpenFolders();

			//ここが個別
			service.updateWebApiPermissionData(TenantInfoHolder.getId(), updateList, new AsyncCallback<Void>() {

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
		setEditingData(ds.getEditPermissionInfo(grid.getData()));

		//編集中のTree展開状態を退避(Grid再表示完了時にクリアされる)
		editingOpenFolders = grid.getOpenFolders();

		refreshGrid();
	}

}
