/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.pack;


import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.GridActionImgButton;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.pack.PackageDS;
import org.iplass.adminconsole.client.tools.data.pack.PackageDS.FIELD_NAME;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageDownloadProperty;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageEntryStatus;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageEntryStatusInfo;
import org.iplass.adminconsole.shared.tools.rpc.pack.PackageRpcServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.pack.PackageRpcServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Packaging List
 */
public class PackageListPane extends VLayout {

	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";

	private static final String DELETE_ICON = "pack/basket_delete.png";
	private static final String CREATE_ICON = "pack/basket_add.png";
	private static final String UPLOAD_ICON = "pack/basket_put.png";
	private static final String IMPORT_ICON = "pack/basket_go.png";
	private static final String EXPORT_ICON = "[SKINIMG]/actions/download.png";

	@SuppressWarnings("unused")
	private PackageMainPane mainPane;

	private Label sizeLabel;
	private Label countLabel;
	private ListGrid grid;

	/**
	 * コンストラクタ
	 */
	public PackageListPane(PackageMainPane mainPane) {
		this.mainPane = mainPane;

		//レイアウト設定
		setWidth100();
		setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		//------------------------
		//Create
		//------------------------

		final ToolStripButton createButton = new ToolStripButton();
		createButton.setIcon(CREATE_ICON);
		createButton.setTitle("Create");
		createButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_createPackageFromTenant")));
		createButton.setHoverWrap(false);
		createButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createPack();
			}
		});
		toolStrip.addButton(createButton);

		toolStrip.addSeparator();

		//------------------------
		//Upload(Import)
		//------------------------

		final ToolStripButton uploadButton = new ToolStripButton();
		uploadButton.setIcon(UPLOAD_ICON);
		uploadButton.setTitle("Upload");
		uploadButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_importExternalPackage")));
		uploadButton.setHoverWrap(false);
		uploadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				uploadPack();
			}
		});
		toolStrip.addButton(uploadButton);

		toolStrip.addSeparator();

//		ToolStripMenuButton packButton = new ToolStripMenuButton();
//		packButton.setTitle("Pack");
//		packButton.setTooltip("Packageの作成、外部Packageの取り込みを行います。");
//		packButton.setHoverWrap(false);
//
//		Menu packCreateMenu = new Menu();
//		packCreateMenu.setShowShadow(true);
//		packCreateMenu.setShadowDepth(3);
//
//		final MenuItem createPackButton = new MenuItem("Create", CREATE_ICON);
//		createPackButton.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//
//			@Override
//			public void onClick(MenuItemClickEvent event) {
//				createPack();
//			}
//		});
//
//		final MenuItem importPackButton = new MenuItem("Upload", IMPORT_ICON);
//		importPackButton.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//
//			@Override
//			public void onClick(MenuItemClickEvent event) {
//				importPack();
//			}
//		});
//
//		packCreateMenu.setItems(createPackButton, importPackButton);
//		packButton.setMenu(packCreateMenu);
//		toolStrip.addMenuButton(packButton);
//
//		toolStrip.addSeparator();

		//------------------------
		//Publish
		//------------------------

		final ToolStripButton publishButton = new ToolStripButton();
		publishButton.setIcon(IMPORT_ICON);
		publishButton.setTitle("Import");
		publishButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_reflectTenantPackage")));
		publishButton.setHoverWrap(false);
		publishButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				publishPack();
			}
		});
		toolStrip.addButton(publishButton);

		toolStrip.addSeparator();

		//------------------------
		//Delete
		//------------------------

		final ToolStripButton deleteButton = new ToolStripButton();
		deleteButton.setIcon(DELETE_ICON);
		deleteButton.setTitle("Delete");
		deleteButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_deleteSelectPackage")));
		deleteButton.setHoverWrap(false);
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deletePack();
			}
		});
		toolStrip.addButton(deleteButton);

		toolStrip.addSeparator();

		toolStrip.addFill();

		sizeLabel = new Label();
		sizeLabel.setWrap(false);
		sizeLabel.setAutoWidth();
		setTotalSize(0);
		toolStrip.addMember(sizeLabel);

		toolStrip.addSeparator();

		countLabel = new Label();
		countLabel.setWrap(false);
		countLabel.setAutoWidth();
		setRecordCount(0);
		toolStrip.addMember(countLabel);

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(REFRESH_ICON);
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_refleshList")));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addButton(refreshButton);

		grid = new ListGrid(){
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				String fieldName = this.getFieldName(colNum);
				if (fieldName.equals("download")) {

					if (record.getAttributeAsBoolean(FIELD_NAME.COMPLETED.name())) {
						GridActionImgButton recordCanvas = new GridActionImgButton();
						recordCanvas.setActionButtonSrc(EXPORT_ICON);
						recordCanvas.setActionButtonPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_packageDownload")));
						recordCanvas.addActionClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								String fileOid = record.getAttribute(FIELD_NAME.OID.name());
								downloadPack(fileOid);
							}
						});
						return recordCanvas;
					}

				} else if (fieldName.equals("sizeStr")) {
					if (record.getAttributeAsBoolean(FIELD_NAME.COMPLETED.name())) {
						long size = record.getAttributeAsLong(FIELD_NAME.SIZE.name());
						Label sizeLabel = new Label(getSizeString(size));
						sizeLabel.setHeight100();
						sizeLabel.setWidth100();
						return sizeLabel;
					} else {
						return null;
					}
				}
				return null;
			}
			@Override
            protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {

				//Statusに対するCSSスタイル定義を取得
				PackageEntryStatusInfo info = (PackageEntryStatusInfo)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
				PackageEntryStatus status =  PackageEntryStatus.valueOf(info.getStatus());
				String style = status.getStyleName();
				if (style.isEmpty()) {
					return super.getBaseStyle(record, rowNum, colNum);
				} else {
					return style;
				}
			}
		};

		grid.setWidth100();
		grid.setHeight100();
		grid.setShowAllRecords(true);
		grid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される

		grid.setCanFreezeFields(false);
		grid.setCanPickFields(false);
		grid.setCanCollapseGroup(false);
		grid.setCanGroupBy(false);

		//CheckBox選択設定
		grid.setSelectionType(SelectionStyle.SIMPLE);
		grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

		//この２つを指定することでcreateRecordComponentが有効
		grid.setShowRecordComponents(true);
		grid.setShowRecordComponentsByCell(true);

		grid.addDataArrivedHandler(new DataArrivedHandler() {

			@Override
			public void onDataArrived(DataArrivedEvent event) {
				setRecordCount(grid.getTotalRows());
				calcTotalSize();
			}
		});
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				if (event.getRecord().getAttributeAsBoolean(FIELD_NAME.COMPLETED.name())) {
					String fileOid = event.getRecord().getAttribute(FIELD_NAME.OID.name());
					unPack(fileOid);
				} else {
					SC.warn(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_genePackageNotFinish"));
				}
			}

		});

		addMember(toolStrip);
		addMember(grid);

		refreshGrid();
	}

	public void refresh() {
		refreshGrid();
	}

	private String getSizeString(long size) {
		if (size > (1024 * 1024 * 1024)) {
			return (size / (1024 * 1024 * 1024)) + " GB";
		} else if (size > (1024 * 1024)) {
			return (size / (1024 * 1024)) + " MB";
		} else if (size > 1024) {
			return (size / 1024) + " KB";
		} else {
			return size + " Byte";
		}
	}

	private void calcTotalSize() {
		long total = 0;
		for (ListGridRecord record : grid.getRecords()) {
			if (record.getAttributeAsBoolean(FIELD_NAME.COMPLETED.name())) {
				long size = record.getAttributeAsLong(FIELD_NAME.SIZE.name());
				total += size;
			}
		}
		setTotalSize(total);
	}

	private void setTotalSize(long size) {
		sizeLabel.setContents("Total Archive Size：" + getSizeString(size));
	}

	private void setRecordCount(long count) {
		countLabel.setContents("Total Count：" + count);
	}

	private boolean isSelect() {
		//trueを指定することでPathは全て選択されていないと含まれない
		ListGridRecord[] records = grid.getSelectedRecords(true);
		if (records == null || records.length == 0) {
			SC.say(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_selectPackageTarget"));
			return false;
		}
		return true;
	}

	private List<String> getSelectedOids() {
		ListGridRecord[] records = grid.getSelectedRecords(true);
		List<String> selectOids = new ArrayList<String>();
		for (ListGridRecord record : records) {
			String oid = record.getAttribute(FIELD_NAME.OID.name());
			selectOids.add(oid);
		}

		return selectOids;
	}

	/**
	 * 一覧のRefresh処理
	 */
	private void refreshGrid() {
		PackageDS ds = PackageDS.getInstance();
		grid.setDataSource(ds);

		//（参考）setFieldsは、setDataSource後に指定しないと効かない

		//ボタンを表示したいためListGridFieldを指定
		ListGridField nameField = new ListGridField(FIELD_NAME.NAME.name(), "Name");
		ListGridField typeField = new ListGridField(FIELD_NAME.TYPE.name(), "Type");
		ListGridField statusField = new ListGridField(FIELD_NAME.STATUS.name(), "Status");
		ListGridField progressField = new ListGridField(FIELD_NAME.PROGRESS.name(), "Progress");
		ListGridField execStartDateField = new ListGridField(FIELD_NAME.EXEC_START_DATE.name(), "Start Time");
		execStartDateField.setWidth(130);
		ListGridField execEndDateField = new ListGridField(FIELD_NAME.EXEC_END_DATE.name(), "Finish Time");
		execEndDateField.setWidth(130);
		ListGridField sizeField = new ListGridField("sizeStr", "Size");
		sizeField.setWidth(100);
		ListGridField downloadField = new ListGridField("download", " ");
		downloadField.setWidth(25);

		grid.setFields(nameField, typeField, statusField, progressField, execStartDateField, execEndDateField, sizeField, downloadField);

		grid.fetchData();
	}

	/**
	 * Packageの削除処理
	 */
	private void deletePack() {
		//選択チェック
		if (!isSelect()) {
			return;
		}

		SC.ask(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_confirm"),
				AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_startDeleteConfirm"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					SmartGWTUtil.showProgress();
					PackageRpcServiceAsync service = PackageRpcServiceFactory.get();
					service.deletePackage(TenantInfoHolder.getId(), getSelectedOids(),
							new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void result) {
									SmartGWTUtil.hideProgress();
									SC.say(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_completion"),
											AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_delete"));

									//一覧再作成
									refresh();
								}

								@Override
								public void onFailure(Throwable caught) {
									SmartGWTUtil.hideProgress();
									SC.warn(AdminClientMessageUtil.getString("ui_tools_pack_PackageListPane_failedToDelete") + caught.getMessage());
								}
							});
				}

			}
		});
	}

	/**
	 * Packageの作成処理
	 */
	private void createPack() {
		PackageCreateDialog.showFullScreen(this);
	}

	/**
	 * PackageのUpload処理
	 */
	private void uploadPack() {
		PackageUploadDialog dialog = new PackageUploadDialog(this);
		dialog.show();
	}

	/**
	 * Packageのダウンロード処理
	 */
	private void downloadPack(String fileOid) {
		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + PackageDownloadProperty.ACTION_URL)
			.addParameter(PackageDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
			.addParameter(PackageDownloadProperty.FILE_OID, fileOid)
			.execute();
	}

	private void publishPack() {
		//選択チェック
		if (!isSelect()) {
			return;
		}

		//複数選択されていた場合は先頭
		String fileOid = getSelectedOids().get(0);

		unPack(fileOid);
	}

	private void unPack(String fileOid) {
		PackageImportDialog.showFullScreen(fileOid, this);
	}

}
