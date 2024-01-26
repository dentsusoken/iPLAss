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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.permissionexplorer.RoleDS;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionGrid;
import org.iplass.adminconsole.client.tools.ui.permissionexplorer.PermissionListPane;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.UpdateRoleInfo;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class RoleListPane extends PermissionListPane {

	private RoleListGrid grid;
	private RoleDS ds;

	/** ロールデータ検索結果通知ハンドラ */
	private List<RoleDataChangeHandler> handlers = new ArrayList<RoleDataChangeHandler>();

	public RoleListPane(RoleMainPane mainPane) {
		super(mainPane, null);

		grid = new RoleListGrid();

		IButton add = new IButton("Add");
		add.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.editRole(null);
			}
		});
		IButton remove = new IButton("Remove");
		remove.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.removeSelectedRole();
			}
		});

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setAutoHeight();
		footer.addMember(add);
		footer.addMember(remove);

		addMember(grid);
		addMember(footer);

		refreshGrid();
	}

	/**
	 * {@link RoleDataChangeHandler} を追加します。
	 *
	 * @param handler {@link RoleDataChangeHandler}
	 */
	public void addRoleDataChangeHandler(RoleDataChangeHandler handler) {
		handlers.add(handler);
	}

	@Override
	protected void doRefreshGrid() {
		grid.refreshGrid();
	}

	@Override
	protected void update() {
		//更新対象データの取得
		UpdateRoleInfo storeInfo = grid.getStoreInfo();

		if (storeInfo.getStoreEntityList().isEmpty() && storeInfo.getRemoveOidList().isEmpty()) {
			//更新対象データなし
			SC.say(AdminClientMessageUtil.getString("ui_tools_permissionexplorer_PermissionListPane_notExistStoreData"));
			return;
		}

		//更新処理
		SmartGWTUtil.showProgress("Save ....");
		service.updateRoleData(TenantInfoHolder.getId(), storeInfo, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				SmartGWTUtil.hideProgress();

				//一覧をリフレッシュ
				refreshGrid();
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage(), caught);
				SmartGWTUtil.hideProgress();
				SC.warn(AdminClientMessageUtil.getString("ui_tools_permissionexplorer_PermissionListPane_saveErr") + caught.getMessage());
			}
		});
	}

	@Override
	protected void doRefreshRoleList() {
		//RoleListPaneでは不要(呼ばれない)
	}

	private class RoleListGrid extends MtpListGrid implements PermissionGrid {

		public RoleListGrid() {
			setWidth100();
			setHeight(1);

			//これを指定しないと表示が崩れる
			setOverflow(Overflow.VISIBLE);
			setBodyOverflow(Overflow.VISIBLE);

			// レコード編集
			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					ListGridRecord record = event.getRecord();
					editRole(record);
				}
			});

			//手動Fetchにする
			setAutoFetchData(false);

			ds = RoleDS.getInstance();
			//setDataSource(ds);

			setFields(ds.getListGridField());
		}

		/**
		 * <p>ロール一覧を最新化します。</p>
		 *
		 * <p>ロールデータを最新化した結果をHandlerに通知します。</p>
		 */
		public void refreshGrid() {
			setEditState(false);

			Criteria criteria = new Criteria("dummy", System.currentTimeMillis() + "");	//同じ条件だとDSに飛ばないので
			ds.fetchData(criteria, new DSCallback() {
				@Override
				public void execute(DSResponse dsResponse, Object data, DSRequest dsRequest) {
					setData(dsResponse.getData());
					setProgressFinish();

					setRecordCount(getTotalRows());

					fireStoredRoleDataChange();
				}
			});
		}

		/**
		 * <p>選択ロールデータを削除対象にします。</p>
		 *
		 * <p>新規で追加されているロールについてはレコードを消します。</p>
		 */
		public void removeSelectedRole() {
			ListGridRecord[] records = getSelectedRecords();
			if (records == null || records.length == 0) {
				return;
			}

			for (ListGridRecord record: records) {
				String status = record.getAttribute("status");
				if (status != null && status.equals("I")) {
					//追加データの削除は直接削除
					removeData(record);
				} else {
					record.setAttribute("status", "D");
					updateData(record);
				}
			}
			refreshFields();

			setEditState(true);
		}

		/**
		 * <p>対象ルールデータを編集します。</p>
		 *
		 * @param record 対象ロールレコード(新規時はnull)
		 */
		public void editRole(final ListGridRecord record) {

			final RoleEditDialog dialog = new RoleEditDialog();

			//編集ダイアログでの変更結果取得
			dialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					Entity editRole = event.getValueObject(GenericEntity.class);

					ListGridRecord editRecord = null;
					if (record == null) {
						editRecord = ds.createRoleRecord(editRole);
						addData(editRecord);
						editRecord.setAttribute("status", "I");
					} else {
						editRecord = record;
						ds.applyRoleRecord(editRecord, editRole);
						String status = editRecord.getAttribute("status");
						if (SmartGWTUtil.isEmpty(status) || !status.equals("I")) {
							//新規以外の場合はUに変更
							editRecord.setAttribute("status", "U");
						}
					}
					editRecord.setAttribute("editEntity", editRole);

					updateData(editRecord);
					refreshFields();

					setEditState(true);
				}
			});

			if (record != null) {
				//編集モード
				Entity editRole = (Entity)record.getAttributeAsObject("editEntity");
				if (editRole != null) {
					dialog.setEditRoleEntity(editRole);
					dialog.show();
				} else {
					//まだ一度も編集していない場合は、対象ロールデータをロードしてからダイアログを表示
					service.loadRoleData(TenantInfoHolder.getId(), record.getAttribute("oid"), new AsyncCallback<Entity>() {

						@Override
						public void onSuccess(Entity result) {
							record.setAttribute("editEntity", result);
							dialog.setEditRoleEntity(result);
							dialog.show();
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO 自動生成されたメソッド・スタブ
						}
					});
				}
			} else {
				//新規モード
				dialog.show();
			}
		}

		/**
		 * <p>更新対象ロールデータを返します。</p>
		 *
		 * @return 更新対象ロールデータ
		 */
		public UpdateRoleInfo getStoreInfo() {
			List<Entity> storeEntityList = new ArrayList<Entity>();
			List<String> removeOidList = new ArrayList<String>();
			for (ListGridRecord record : getRecords()) {
				String status = record.getAttribute("status");
				if (SmartGWTUtil.isEmpty(status)) {
					//未編集データ
					continue;
				}

				if (status.equals("D")) {
					//削除データは、詳細データを取得していない可能性があるのでOIDのみ
					removeOidList.add(record.getAttribute("oid"));
				} else {
					//更新データ(I or U)
					storeEntityList.add((Entity)record.getAttributeAsObject("editEntity"));
				}
			}
			return new UpdateRoleInfo(storeEntityList, removeOidList);
		}

		@Override
	    protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
			// grid.setBaseStyleだとセルの高さが不安定になる為ここで指定。

			String status = record.getAttribute("status");

			if ("D".equals(status)) {
				return CELL_STYLE_ROLE_DELETING;
			} else if ("U".equals(status)) {
				return CELL_STYLE_ROLE_EDITING;
			} else if ("I".equals(status)) {
				return CELL_STYLE_ROLE_EDITING;
			} else {
				return CELL_STYLE_DEFAULT;
			}
		}

		/**
		 * <p>検索ロールデータの通知処理</p>
		 *
		 * <p>一覧のリフレッシュなどでロールデータを再取得した際に、
		 * Handler(PermissionExplorerMainPane)に検索したロールデータを通知する。</p>
		 */
		private void fireStoredRoleDataChange() {
			RoleDataChangeEvent event = new RoleDataChangeEvent(ds.getStoredRoleList());
			for (RoleDataChangeHandler handler : handlers) {
				handler.onRoleDataChanged(event);
			}
		}
	}

	public interface RoleDataChangeHandler {

		/**
		 * データ変更イベントです。
		 *
		 * @param event {@link RoleDataChangeEvent}
		 */
		void onRoleDataChanged(RoleDataChangeEvent event);
	}

	public class RoleDataChangeEvent {
		private List<Entity> roleList;

		public RoleDataChangeEvent(List<Entity> roleList) {
			this.roleList = roleList;
		}

		public List<Entity> getRoleList() {
			return roleList;
		}
	}
}
