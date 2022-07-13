/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.tenant;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.screen.ScreenModuleBasedUIFactory;
import org.iplass.adminconsole.client.base.screen.ScreenModuleBasedUIFactoryHolder;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.data.tenant.BaseTenantDS;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.shared.base.dto.tenant.TenantEnv;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceFactory;
import org.iplass.adminconsole.shared.metadata.dto.MetaVersionCheckException;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

/**
 * テナント編集パネル
 *
 * テナント編集の最上位パネルです。
 * {@link org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab}により生成されます。
 *
 */
public class TenantEditPane extends MetaDataMainEditPane {

	private ListGrid grid;

	private BaseTenantDS dataSource;

	private TenantServiceAsync service = TenantServiceFactory.get();

	private TenantPluginController controller = GWT.create(TenantPluginController.class);

	/** 編集対象 */
	private Tenant curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/**
	 * コンストラクタ
	 */
	public TenantEditPane(final TenantMainPane owner, MetaDataItemMenuTreeNode targetNode,
			DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		// レイアウト設定
		setWidth100();

		controller.setControllTarget(owner, this);

		MetaCommonHeaderPane headerPane = controller.createHeaderPane(targetNode);

		headerPane.setSaveClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				save();
			}
		});
		headerPane.setCancelClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancel();
			}
		});

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				DefinitionEntry entry = dataSource.getTenantEntry();
				curDefinition = (Tenant) entry.getDefinition();
				curVersion = entry.getDefinitionInfo().getVersion();
				curDefinitionId = entry.getDefinitionInfo().getObjDefId();

				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(
						curDefinition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		// 配置
		addMember(headerPane);

		// 表示データの取得
		initializeData();

	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {

		// テナント情報一覧の作成
		createTenantGrid();

		// ステータスチェック
		// TODO 各種URLセレクターのエラーは表示されない
		StatusCheckUtil.statuCheck(Tenant.class.getName(), defName, this);
	}

	/**
	 * テナントGridの作成処理
	 */
	private void createTenantGrid() {

		// 既にGridが表示されている場合は削除
		if (grid != null) {
			removeMember(grid);
			grid.destroy();
			grid = null;
		}

		grid = controller.createGrid();

		// 一覧ダブルクリック処理
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				showPropertyEditDialog(record, event.getRecordNum());
			}
		});

		dataSource = ScreenModuleBasedUIFactoryHolder.getFactory().createTenantDataSource();
		grid.setDataSource(dataSource);
		grid.setAutoFetchData(true);

		addMember(grid);
	}

	/**
	 * 編集ダイアログ表示処理
	 *
	 * @param record 編集対象レコード
	 * @param rowNum 行番号
	 */
	private void showPropertyEditDialog(final Record record, final int rowNum) {
		ScreenModuleBasedUIFactory factory = ScreenModuleBasedUIFactoryHolder.getFactory();
		BaseTenantPropertyEditDialog dialog = factory.createTenantPropertyEditDialog(record);
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {

				// 画面の更新(対象行のみ)
				grid.refreshRow(rowNum);

			}
		});
		dialog.show();

	}

	/**
	 * 保存ボタン処理
	 */
	private void save() {

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_tenant_TenantEditPane_saveConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_tenant_TenantEditPane_saveTenantComment"),
				new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							// 更新対象の取得
							Tenant tenant = dataSource.getUpdateData();
							DefinitionEntry entry = dataSource.getTenantEntry();
							curVersion = entry.getDefinitionInfo().getVersion();

							updateTenant(tenant, true);
						}
					}
				});
	}

	/**
	 * キャンセルボタン処理
	 */
	private void cancel() {

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_tenant_TenantEditPane_cancelConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_tenant_TenantEditPane_cancelConfirmComment"),
				new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if (value) {
							initializeData();
						}
					}
				});
	}

	/**
	 * {@link Tenant} の更新処理
	 *
	 * @param tenant 更新対象{@link Tenant}
	 */
	private void updateTenant(final Tenant tenant, boolean checkVersion) {

		service.updateTenant(TenantInfoHolder.getId(), tenant, curVersion, checkVersion, true,
				new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						if (caught instanceof MetaVersionCheckException) {
							SC.ask(AdminClientMessageUtil
									.getString("ui_metadata_tenant_TenantEditPane_overwriteConfirm"),
									AdminClientMessageUtil
											.getString("ui_metadata_tenant_TenantEditPane_overwriteConfirmComment"),
									new BooleanCallback() {

										@Override
										public void execute(Boolean value) {

											if (value) {
												updateTenant(tenant, false);
											}
										}
									});
						} else {
							// 失敗時
							SC.warn(AdminClientMessageUtil.getString(
									"ui_metadata_tenant_TenantEditPane_failedToSaveTenant") + caught.getMessage());
						}
					}

					@Override
					public void onSuccess(Boolean result) {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_tenant_TenantEditPane_completion"),
								AdminClientMessageUtil.getString("ui_metadata_tenant_TenantEditPane_tenantSave"));

						// TenantInfoHolderのリロード
						reloadTenantInfoHolder();
					}

				});
	}

	private void reloadTenantInfoHolder() {
		service.getTenantEnv(TenantInfoHolder.getId(), new AsyncCallback<TenantEnv>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_tenant_TenantEditPane_failedToSaveTenant")
						+ caught.getMessage());
			}

			@Override
			public void onSuccess(TenantEnv result) {
				TenantInfoHolder.reload(result);

				// 再表示
				initializeData();
			}
		});
	}

}
