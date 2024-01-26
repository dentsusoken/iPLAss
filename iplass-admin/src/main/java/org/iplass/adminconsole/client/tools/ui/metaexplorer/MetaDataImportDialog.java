/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.metaexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.RefreshMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.AnimationFullScreenCallback;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.metaexplorer.MetaDataImportTreeDS;
import org.iplass.adminconsole.client.tools.data.metaexplorer.MetaDataImportTreeDS.FIELD_NAME;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataImportTenantPane.TenantSelectActionCallback;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportFileInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportMetaDataStatus;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataImportResultInfo;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceFactory;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.DataArrivedEvent;
import com.smartgwt.client.widgets.tree.events.DataArrivedHandler;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;

/**
 * アップロードされたMetaData XMLファイルをインポートするダイアログ
 */
public class MetaDataImportDialog extends AbstractWindow {

	private static final String RESOURCE_PREFIX = "ui_tools_metaexplorer_MetaDataImportDialog_";
	private static final String EXPAND_ICON = "[SKIN]/actions/sort_descending.png";
	private static final String CONTRACT_ICON = "[SKIN]/actions/sort_ascending.png";

//	private static final int DEFAULT_WIDTH = 1200;
//	private static final int DEFAULT_HEIGHT = 700;
	private static final int MIN_WIDTH = 800;
	private static final int MIN_HEIGHT = 600;

	private MetaDataExplorerServiceAsync service = MetaDataExplorerServiceFactory.get();

	private MetaDataListPane owner;

	private String fileOid;

	private VLayout mainLayout;

	//メタデータ選択用画面
	private MetaDataImportSelectPane metaSelectPane;

	//テナントプロパティ選択画面
	private MetaDataImportTenantPane tanantSelectPane;

	public static void showFullScreen(final String fileOid, final MetaDataListPane owner) {
		SmartGWTUtil.showAnimationFullScreen(new AnimationFullScreenCallback() {

			@Override
			public void execute(boolean earlyFinish) {
				animateOutline.hide();
				final MetaDataImportDialog dialog = new MetaDataImportDialog(fileOid, owner, width, height);
				dialog.show();
			}
		});
	}

	/**
	 * コンストラクタ
	 */
	private MetaDataImportDialog(String fileOid, MetaDataListPane owner, int width, int height) {
		this.fileOid = fileOid;
		this.owner = owner;

		setWidth(width);
		setMinWidth(MIN_WIDTH);
		setHeight(height);
		setMinHeight(MIN_HEIGHT);
		setTitle("Import MetaData");
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		metaSelectPane = new MetaDataImportSelectPane();

		mainLayout = new VLayout();
		mainLayout.addMember(metaSelectPane);

		addItem(mainLayout);
	}

	private class MetaDataImportSelectPane extends VLayout {

		private VLayout gridLayout;
		private MetaDataImportTreeGrid grid;
		private Label nameLabel;
		private Label countLabel;
		private Label noticeLabel;
		private StatusMessageDetailPane messageDetailPane;

		public MetaDataImportSelectPane() {

			setWidth100();
			setHeight100();
			setMargin(10);

			//------------------------
			//Lable Title
			//------------------------
			Label title = new Label(getResourceString("importMetaDataLocal"));
			title.setHeight(40);
			title.setWidth100();

			//------------------------
			//Grid Pane
			//------------------------
			gridLayout = new VLayout();
			//gridLayout.setWidth100();
			gridLayout.setHeight100();
			gridLayout.setOverflow(Overflow.AUTO);

			//------------------------
			//Grid ToolStrip
			//------------------------
			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setWidth100();
			toolStrip.setMembersMargin(5);
			toolStrip.setAlign(VerticalAlignment.BOTTOM);

			toolStrip.addSpacer(5);

			nameLabel = new Label();
			nameLabel.setWrap(false);
			nameLabel.setAutoWidth();
			toolStrip.addMember(nameLabel);

			toolStrip.addFill();

			countLabel = new Label();
			countLabel.setWrap(false);
			countLabel.setAutoWidth();
			setRecordCount(0);
			toolStrip.addMember(countLabel);

			noticeLabel = new Label();
			noticeLabel.setWrap(false);
			noticeLabel.setAutoWidth();
			setNoticeCount(0, 0, 0);
			toolStrip.addMember(noticeLabel);

			toolStrip.addSpacer(5);

			final ToolStripButton expandAllButton = new ToolStripButton();
			expandAllButton.setIcon(EXPAND_ICON);
			expandAllButton.setTitle("Expand");
			expandAllButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					grid.expandAll();
				}
			});
			toolStrip.addButton(expandAllButton);

			final ToolStripButton contractAllButton = new ToolStripButton();
			contractAllButton.setIcon(CONTRACT_ICON);
			contractAllButton.setTitle("Collapse");
			contractAllButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					grid.expandRoot();
				}
			});
			toolStrip.addButton(contractAllButton);

			//------------------------
			//Grid
			//------------------------
			grid = new MetaDataImportTreeGrid(this);

			gridLayout.addMember(toolStrip);
			gridLayout.addMember(grid);

			//------------------------
			//Footer Layout
			//------------------------
			IButton importButton = new IButton("Import");
			importButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					executeImport();
				}
			});

			IButton cancelButton = new IButton("Cancel");
			cancelButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					MetaDataImportDialog.this.destroy();
				}
			});

			HLayout footer = new HLayout(5);
			footer.setMargin(5);
			footer.setHeight(20);
			footer.setWidth100();
			//footer.setAlign(Alignment.LEFT);
			footer.setAlign(VerticalAlignment.CENTER);
			footer.setMembers(importButton, cancelButton);

			addMember(title);
			addMember(gridLayout);
			addMember(footer);

			initializeData();
		}

		private void initializeData() {
			grid.initializeData();
		}

		private void setTargetName(String name) {
			nameLabel.setContents(name);
		}

		private void setRecordCount(int count) {
			countLabel.setContents("Total Count：" + count);
		}

		private void setNoticeCount(int error, int warn, int info) {
			noticeLabel.setContents("(ERROR：" + error + " , WARN：" + warn + " , INFO：" + info + ")");
		}

		private void executeStatusCheck(final ImportFileInfo result) {
			//画面表示を0.5秒遅らせる
			new Timer() {

				@Override
				public void run() {
					ImportStatusCheckDialog dialog = new ImportStatusCheckDialog();
					dialog.show();
					dialog.executeCheck(result);
				}
			}.schedule(500);
		}

		public void setCheckStatusResult(List<ImportMetaDataStatus> resultStatus) {
			List<ImportMetaDataStatus> errorList = new ArrayList<>();
			List<ImportMetaDataStatus> warnList = new ArrayList<>();
			List<ImportMetaDataStatus> infoList = new ArrayList<>();

			grid.applyStatusResult(resultStatus, errorList, warnList, infoList);

			setNoticeCount(errorList.size(), warnList.size(), infoList.size());

			//エラーの表示
			if (errorList.size() > 0 || warnList.size() > 0 || infoList.size() > 0) {
				messageDetailPane = new StatusMessageDetailPane(errorList, warnList, infoList);
				gridLayout.addMember(messageDetailPane);
			}
		}

		public List<ListGridRecord> getSelectedRecords() {

			final List<ListGridRecord> selectRecords = new ArrayList<>();

			//trueを指定することでPathは全て選択されていないと含まれない
			final ListGridRecord[] records = grid.getSelectedRecords(true);

			for (ListGridRecord record : records) {
				String path = record.getAttribute(FIELD_NAME.PATH.name());
				//Rootは除外
				if (path == null || path.isEmpty()) {
					continue;
				}

				//Treeを展開していないと、エラーも選択できてしまうためチェック(エラーは除外)
				if (record.getAttribute(FIELD_NAME.IS_ERROR.name()) != null) {
					Boolean isError = record.getAttributeAsBoolean(FIELD_NAME.IS_ERROR.name());
					if (isError) {
						continue;
					}
				}

				TreeNode node = (TreeNode)record;
				if (!grid.getTree().isFolder(node)) {
					selectRecords.add(record);
				}
			}

			return selectRecords;
		}

		public void setSelectedPath(String path) {
			if (messageDetailPane != null) {
				messageDetailPane.setSelectedPath(path);
			}
		}

	}

	private class MetaDataImportTreeGrid extends MtpTreeGrid {

		private MetaDataImportTreeDS dataSource;

		public MetaDataImportTreeGrid(final MetaDataImportSelectPane owner) {
			super(true);

//			setLeaveScrollbarGap(false);
			setCanSort(false);
			setCanFreezeFields(false);
			setCanPickFields(false);

			setSelectionAppearance(SelectionAppearance.CHECKBOX);
			setShowSelectedStyle(false);
			setShowPartialSelection(true);
			setCascadeSelection(true);

			setAutoFetchData(false);
			setShowAllRecords(true);

			setWrapCells(true);	//折り返しOK

			//IDなどDragで選択可能にしたいが、Treeの場合ドラッグ時にエラーになる(JavaScriptException)
			//動作上は問題がないので、GWTログのみ出力するように対応して選択可能にした(@see MtpAdmin.java)
			setCanDragSelectText(true);

			// この２つを指定することでcreateRecordComponentが有効
//			setShowRecordComponents(true);
//			setShowRecordComponentsByCell(true);

			setBaseStyle("importMetaGrid");

			addDataArrivedHandler(new DataArrivedHandler() {

				@Override
				public void onDataArrived(DataArrivedEvent event) {

					//名前表示
					owner.setTargetName(dataSource.getResult().getFileName());

					//件数表示
					owner.setRecordCount(dataSource.getResult().getCount());

					//全展開（全展開すると画面描画が遅いのでやめる）
					//grid.getTree().openAll();
					//Root配下のみ展開
					expandRoot();

					//StatusCheck
					owner.executeStatusCheck(dataSource.getResult());
				}
			});

			addNodeClickHandler(new NodeClickHandler() {

				@Override
				public void onNodeClick(NodeClickEvent event) {
					if (getTree().isLeaf(event.getNode())) {
						owner.setSelectedPath(event.getNode().getAttribute(MetaDataImportTreeDS.FIELD_NAME.PATH.name()));
					}
				}
			});

			//ここで指定しておかないとエラー
			setHeaderSpans(
					new HeaderSpan("Import Data", new String[]{
							FIELD_NAME.DISPLAY_NAME.name()
							,FIELD_NAME.ITEM_ID.name()
					}),
					new HeaderSpan("Action", new String[]{
							FIELD_NAME.ACTION_NAME.name()
							,FIELD_NAME.MESSAGE.name()
					})
				);
			setHeaderHeight(44);	//デフォルト × 2
		}

//		@Override
//		protected Canvas createRecordComponent(final ListGridRecord record,
//				Integer colNum) {
//			final String fieldName = this.getFieldName(colNum);
//			if (FIELD_NAME.STORED_ID_STATUS.name().equals(fieldName)) {
//				final ImportMetaDataStatus status = (ImportMetaDataStatus)record
//								.getAttributeAsObject(FIELD_NAME.STORED_STATUS.name());
//				if (status != null && status.isMatchID()) {
//					final Img img = new Img("tick.png", 16, 16);
//					img.setImageWidth(16);
//					img.setImageHeight(16);
//					img.setImageType(ImageStyle.CENTER);
//					img.setCanHover(true);
//					img.addHoverHandler(new HoverHandler() {
//						@Override
//						public void onHover(HoverEvent event) {
//							img.setPrompt(SmartGWTUtil.getHoverString(
//								record.getAttribute(FIELD_NAME.STORED_ID.name())
//							));
//						}
//					});
//					return img;
//				}
//			} else if (FIELD_NAME.STORED_NAME_STATUS.name().equals(fieldName)) {
//				final ImportMetaDataStatus status = (ImportMetaDataStatus)record
//								.getAttributeAsObject(FIELD_NAME.STORED_STATUS.name());
//				if (status != null && status.isMatchPath()) {
//					final Img img = new Img("tick.png", 16, 16);
//					img.setImageWidth(16);
//					img.setImageHeight(16);
//					img.setImageType(ImageStyle.CENTER);
//					img.setCanHover(true);
//					img.addHoverHandler(new HoverHandler() {
//						@Override
//						public void onHover(HoverEvent event) {
//							img.setPrompt(SmartGWTUtil.getHoverString(
//								record.getAttribute(FIELD_NAME.STORED_NAME.name())
//							));
//						}
//					});
//					return img;
//				}
//			}
//			return null;
//		}

		@Override
		protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum)  {

			//Drag選択可にした際に、NullPointerが発生するようになったため対応。
			if (record == null) {
				return super.getBaseStyle(record, rowNum, colNum);
			}

			if (record.getAttribute(FIELD_NAME.IS_ERROR.name()) != null) {
				Boolean isError = record.getAttributeAsBoolean(FIELD_NAME.IS_ERROR.name());
				if (isError) {
					//record.setEnabled(false);
					record.setCanSelect(false);
					return "ERRORGridRow";
				}
			}

			if (record.getAttribute(FIELD_NAME.IS_WARN.name()) != null) {
				Boolean isWarn = record.getAttributeAsBoolean(FIELD_NAME.IS_WARN.name());
				if (isWarn) {
					return "WARNGridRow";
				}
			}

//			if (record.getAttribute(FIELD_NAME.IS_INFO.name()) != null) {
//				Boolean isInfo = record.getAttributeAsBoolean(FIELD_NAME.IS_INFO.name());
//				if (isInfo) {
//					return "INFOGridRow";
//				}
//			}

			return super.getBaseStyle(record, rowNum, colNum);
		}

		public void initializeData() {

			dataSource = MetaDataImportTreeDS.getInstance();
			setDataSource(dataSource);

			setFields(dataSource.getTreeGridField());

			fetchData(new Criteria(MetaDataImportTreeDS.CRITERIA_FILE_OID, fileOid));
		}

		public void applyStatusResult(List<ImportMetaDataStatus> resultStatus,
				List<ImportMetaDataStatus> errorList,
				List<ImportMetaDataStatus> warnList,
				List<ImportMetaDataStatus> infoList) {

			dataSource.applyStatusResult(resultStatus, errorList, warnList, infoList);

			//まだTopしか表示されていないので、再描画の必要なし
			//refreshFields();
		}

		public void expandRoot() {
			getTree().closeAll();
			getTree().openFolders(
					getTree().getChildren(getTree().getRoot()));
		}

		public void expandAll() {
			getTree().openAll();
		}
	}

	private void executeImport() {

		boolean selectTenant = false;

		final List<String> selectPaths = new ArrayList<>();
		for (ListGridRecord record : metaSelectPane.getSelectedRecords()) {
			String path = record.getAttribute(FIELD_NAME.PATH.name());
			selectPaths.add(path);
			GWT.log("[" + path + "] is selected");

			//テナントの選択チェック
			String definitionClass = record.getAttribute(FIELD_NAME.CLASS_NAME.name());
			if (SmartGWTUtil.isNotEmpty(definitionClass) && definitionClass.equals(Tenant.class.getName())) {
				selectTenant = true;
			}
		}

		if (selectPaths.size() == 0) {
			SC.say(getResourceString("selectMetaDataTarget"));
			return;
		}

		//テナントが含まれている場合は、プロパティ選択を行う
		if (selectTenant) {
			SmartGWTUtil.showProgress("Load Import Tenant Info...");
			service.getImportTenant(TenantInfoHolder.getId(), fileOid, new AsyncCallback<Tenant>() {

				@Override
				public void onSuccess(Tenant importTenant) {
					GWT.log(importTenant.toString());

					tanantSelectPane = new MetaDataImportTenantPane(importTenant, new TenantSelectActionCallback() {

						@Override
						public void selected(Tenant importTenant) {
							//テナント選択画面非表示
							hideTanantSelectPane();

							//インポート開始
							doExecuteImport(selectPaths, importTenant);
						}

						@Override
						public void canceled() {
							//テナント選択画面非表示
							hideTanantSelectPane();
						}

						private void hideTanantSelectPane() {
							metaSelectPane.show();

							if (tanantSelectPane != null) {
								tanantSelectPane.hide();
								tanantSelectPane.destroy();
								tanantSelectPane = null;
							}
						}
					});
					mainLayout.addMember(tanantSelectPane);

					//画面をテナント選択画面に切り替え
					tanantSelectPane.show();
					metaSelectPane.hide();
				}

				@Override
				public void onFailure(Throwable caught) {
					SmartGWTUtil.hideProgress();
					GWT.log(caught.toString(), caught);
					SC.warn(getResourceString("failedToImportMetaDataCause") + caught.getMessage());
				}
			});

		} else {
			//インポート開始
			doExecuteImport(selectPaths, null);
		}
	}

	private void doExecuteImport(final List<String> selectPaths, final Tenant importTenant) {

		SC.ask(getResourceString("confirm"), getResourceString("startImportConf"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					SmartGWTUtil.showProgress();

					service.importMetaData(TenantInfoHolder.getId(), fileOid, selectPaths, importTenant, new AsyncCallback<MetaDataImportResultInfo>() {

						@Override
						public void onFailure(Throwable caught) {
							SmartGWTUtil.hideProgress();

							GWT.log(caught.toString(), caught);
							SC.warn(getResourceString("failedToImportMetaDataCause") + caught.getMessage());
						}

						@Override
						public void onSuccess(final MetaDataImportResultInfo result) {
							SmartGWTUtil.hideProgress();

							if (result.isError()) {
								String cause = "";
								if (result.getMessages() != null && result.getMessages().size() > 0) {
									cause = getResourceString("MetaDataImportDialog_cause") + result.getMessages().get(0);
								}
								SC.warn(getResourceString("failedToImportMetaData") + cause, new BooleanCallback() {

									@Override
									public void execute(Boolean value) {
										showResultDialog(result);
									}
								});
							} else {
								showResultDialog(result);
							}
						}
					});
				}
			}
		});
	}

	@Override
	protected boolean onPreDestroy() {
		removeFileEntity();
		return true;
	}

	private void removeFileEntity() {

		service.removeImportFile(TenantInfoHolder.getId(), fileOid, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("delete file entity failed. fileOid=" + fileOid);
			}

			@Override
			public void onSuccess(Void result) {
				GWT.log("delete file entity success. fileOid=" + fileOid);
			}
		});
	}

	private void showResultDialog(MetaDataImportResultInfo result) {
		ImportResultDialog dialog = new ImportResultDialog(result);
		dialog.show();
		owner.refreshGrid();
		RefreshMetaDataEvent.fire(AdminConsoleGlobalEventBus.getEventBus());

		destroy();
	}

	private class ImportStatusCheckDialog extends AbstractWindow {

		public static final int PAGE_SIZE = 10;

		private Label progressLabel;
		private Progressbar progress;

		private String importTagOid;
		private List<String> importPathList;

		private List<ImportMetaDataStatus> resultStatus;

		public ImportStatusCheckDialog() {
			setWidth(400);
			setHeight(120);
			setTitle("Check Status Import Data ...");
			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(true);
			setCanDragReposition(false);
			setShowCloseButton(false);
			centerInPage();

			VLayout header = new VLayout(5);
			header.setMargin(5);
			header.setHeight(20);
			header.setWidth100();
			header.setAlign(VerticalAlignment.CENTER);

			Label title = new Label(getResourceString("checkMetaDataImport"));
			title.setHeight(20);
			title.setWidth100();
			header.addMember(title);

			VLayout horizontalBars = new VLayout(4);
			horizontalBars.setMargin(5);
			horizontalBars.setAutoHeight();
			horizontalBars.setWidth100();
			horizontalBars.setAlign(VerticalAlignment.CENTER);

			progressLabel = new Label("Check Progress");
			progressLabel.setHeight(16);
			horizontalBars.addMember(progressLabel);

			progress = new Progressbar();
			progress.setHeight(24);
			progress.setVertical(false);
			horizontalBars.addMember(progress);

			addItem(header);
			addItem(horizontalBars);

		}

		public void executeCheck(ImportFileInfo importInfo) {

			//Itemの取得
			setImportFileInfo(importInfo);

			//Statusの初期化
			statusRefresh(0);

			//Check開始
			startCheck();
		}

		private void setImportFileInfo(ImportFileInfo importInfo) {
			importTagOid = importInfo.getTagOid();
			importPathList = new ArrayList<>();
			getPathList(importInfo.getRootNode());
		}

		private void getPathList(MetaTreeNode node) {
			if (node.getChildren() != null && node.getChildren().size() > 0) {
				for (MetaTreeNode child : node.getChildren()) {
					getPathList(child);
				}
			}
			if (node.getItems() != null && node.getItems().size() > 0) {
				for (MetaTreeNode item : node.getItems()) {
					importPathList.add(item.getPath());
				}
			}
		}

		private void statusRefresh(final int execCount) {
			int percent = (int)(((double)execCount / (double)importPathList.size()) * 100.0);

			GWT.log(percent + "% check execute." + execCount + "/" + importPathList.size());
			progressLabel.setContents("Check Progress:" + percent + "%");
			progress.setPercentDone(percent);
		}

		private void startCheck() {
			resultStatus = new ArrayList<>();
			checkExecute(0);
		}

		private void checkExecute(final int offset) {

			int endIndex = offset * PAGE_SIZE + PAGE_SIZE;
			if (endIndex > importPathList.size()) {
				endIndex = importPathList.size();
			}
			//subListのままだと、 「com.google.gwt.user.client.rpc.SerializationException: java.util.RandomAccessSubList is not a serializable type」
			List<String> execPathList = new ArrayList<>(importPathList.subList(offset * PAGE_SIZE , endIndex));

			service.checkImportStatus(TenantInfoHolder.getId(), importTagOid, execPathList, new AsyncCallback<List<ImportMetaDataStatus>>() {

				@Override
				public void onSuccess(List<ImportMetaDataStatus> result) {
					if (result != null && !result.isEmpty()) {
						resultStatus.addAll(result);
					}
					int nextOffset = offset + 1;
					if (importPathList.size() > nextOffset * PAGE_SIZE) {
						statusRefresh(nextOffset * PAGE_SIZE);
						checkExecute(nextOffset);
					} else {
						//終了
						statusRefresh(importPathList.size());
						showResult();
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString(), caught);
					SC.say(getResourceString("failed"), getResourceString("failedToCheckStatusMetaData")
							,new BooleanCallback() {

								@Override
								public void execute(Boolean value) {
									destroy();
								}
							});
				}
			});
		}

		private void showResult() {
			progressLabel.setContents("Disp Check Result Status .....");

			//画面表示を0.2秒遅らせる
			new Timer() {
				@Override
				public void run() {
					metaSelectPane.setCheckStatusResult(resultStatus);
					destroy();
				}
			}.schedule(200);
		}

	}


	private class ImportResultDialog extends AbstractWindow {
		private MessageTabSet messageTabSet;

		public ImportResultDialog(MetaDataImportResultInfo result) {
			setWidth(700);
			setMinWidth(500);
			setHeight(500);
			setMinHeight(400);
			setTitle("Import MetaData");
			setCanDragResize(true);
			setShowMinimizeButton(false);
			setShowMaximizeButton(true);
			setIsModal(true);
			setShowModalMask(true);
			centerInPage();

			//------------------------
			//Input Items
			//------------------------
			Label title = new Label(getResourceString("importComp"));
			title.setHeight(20);
			title.setWidth100();

			VLayout inputLayout = new VLayout(5);
			inputLayout.setMargin(5);
			inputLayout.setHeight(30);
			inputLayout.setMembers(title);

			//------------------------
			//MessagePane
			//------------------------
			messageTabSet = new MessageTabSet();
			if (result.isError()) {
				messageTabSet.setErrorMessage(result.getMessages());
			} else {
				messageTabSet.setMessage(result.getMessages());
			}

			//------------------------
			//Buttons
			//------------------------
			IButton okButton = new IButton("OK");
			okButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			HLayout execButtons = new HLayout(5);
			execButtons.setMargin(5);
			execButtons.setHeight(20);
			execButtons.setWidth100();
			execButtons.setAlign(VerticalAlignment.CENTER);
			execButtons.setMembers(okButton);

			//------------------------
			//Main Layout
			//------------------------
			VLayout mainLayout = new VLayout();
			mainLayout.setWidth100();
			mainLayout.setHeight100();
			mainLayout.setMargin(10);

			mainLayout.addMember(inputLayout);
			mainLayout.addMember(messageTabSet);
			mainLayout.addMember(execButtons);

			addItem(mainLayout);
		}
	}

	private class StatusMessageDetailPane extends VLayout {

		private ListGrid messageGrid;
		private Canvas msgContents;

		public StatusMessageDetailPane(List<ImportMetaDataStatus> errorList, List<ImportMetaDataStatus> warnList, List<ImportMetaDataStatus> infoList) {
			setWidth100();
			setHeight(220);

			Label title = new Label(getResourceString("errCheckSett"));
			title.setHeight(20);
			title.setWidth100();

			messageGrid = new ListGrid() {
				//ツリー上のレコードクリック時の選択状態がわかりにくいので、こっちは普通の表示にする
//				@Override
//				protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum)  {
//
//					if (record.getAttribute("status") != null) {
//						if ("ERROR".equals(record.getAttribute("status"))) {
//							return "ERRORGridRow";
//						} else if ("WARN".equals(record.getAttribute("status"))) {
//							return "WARNGridRow";
////						} else if ("INFO".equals(record.getAttribute("status"))) {
////							return "infoGridRow";
//						}
//					}
//
//					return super.getBaseStyle(record, rowNum, colNum);
//				}
			};

			messageGrid.setWidth100();
			messageGrid.setHeight(100);
			messageGrid.setShowAllRecords(true);
			messageGrid.setWrapCells(true);
			messageGrid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される
			messageGrid.setShowRowNumbers(true);		//行番号表示

			messageGrid.setWrapCells(true);				//折り返しOK
			messageGrid.setCanDragSelectText(true);		//Dragでテキスト選択可

			messageGrid.setCanSort(false);
			messageGrid.setCanFreezeFields(false);
			messageGrid.setCanPickFields(false);
			messageGrid.setCanAutoFitFields(false);
			messageGrid.setCanGroupBy(false);

			messageGrid.setAutoFitData(Autofit.HORIZONTAL);
//			messageGrid.setAutoFitMaxHeight(56);

	        ListGridField statusField = new ListGridField("status", "Status");
	        statusField.setWidth(80);

			ListGridField pathField = new ListGridField("path", "Path", 300);
			pathField.setShowHover(true);
			pathField.setHoverCustomizer(new HoverCustomizer() {
				@Override
				public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
					return SmartGWTUtil.getHoverString(record.getAttribute("path"));
				}
			});

	        ListGridField actionField = new ListGridField("action", "Action");
	        actionField.setWidth(150);

	        ListGridField messageField = new ListGridField("message", "Message");
	        messageField.setShowHover(true);
	        messageField.setHoverCustomizer(new HoverCustomizer() {
				@Override
				public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
					return SmartGWTUtil.getHoverString(record.getAttribute("detailmessage"));
				}
			});

	        messageGrid.setFields(statusField, pathField, actionField, messageField);

	        ListGridRecord[] records = new ListGridRecord[errorList.size() + warnList.size() + infoList.size()];
	        int row = 0;
	        for (ImportMetaDataStatus status : errorList) {
	        	records[row] = new ListGridRecord();
	        	records[row].setAttribute("status", "ERROR");
	        	records[row].setAttribute("path", status.getPath());
	        	records[row].setAttribute("action", status.getImportActionName());
	        	records[row].setAttribute("message", getSimpleMessage(status.getMessage()));
	        	records[row].setAttribute("detailmessage", status.getMessageDetail());
	        	row++;
	        }
	        for (ImportMetaDataStatus status : warnList) {
	        	records[row] = new ListGridRecord();
	        	records[row].setAttribute("status", "WARN");
	        	records[row].setAttribute("path", status.getPath());
	        	records[row].setAttribute("action", status.getImportActionName());
	        	records[row].setAttribute("message", getSimpleMessage(status.getMessage()));
	        	records[row].setAttribute("detailmessage", status.getMessageDetail());
	        	row++;
	        }
	        for (ImportMetaDataStatus status : infoList) {
	        	records[row] = new ListGridRecord();
	        	records[row].setAttribute("status", "INFO");
	        	records[row].setAttribute("path", status.getPath());
	        	records[row].setAttribute("action", status.getImportActionName());
	        	records[row].setAttribute("message", getSimpleMessage(status.getMessage()));
	        	records[row].setAttribute("detailmessage", status.getMessageDetail());
	        	row++;
	        }
	        messageGrid.setData(records);

			Label msgTitle = new Label(getResourceString("messageLabel"));
			msgTitle.setHeight(20);
			msgTitle.setWidth100();

			msgContents = new Canvas();
			msgContents.setHeight100();
			msgContents.setWidth100();
			msgContents.setOverflow(Overflow.VISIBLE);
			msgContents.setCanSelectText(true);
			msgContents.setBorder("1px solid silver");
			msgContents.setAlign(Alignment.LEFT);

	        messageGrid.addRecordClickHandler(new RecordClickHandler() {

				@Override
				public void onRecordClick(RecordClickEvent event) {
					Record record = event.getRecord();
					showDetailMessage(record);
				}
			});

			addMember(title);
			addMember(messageGrid);
			addMember(msgTitle);
			addMember(msgContents);
		}

		public void setSelectedPath(String path) {
			for(ListGridRecord record : messageGrid.getRecords()) {
				if (path.equals(record.getAttribute("path"))) {
					messageGrid.scrollToRow(messageGrid.getRowNum(record));
					messageGrid.selectSingleRecord(record);
					showDetailMessage(record);
					break;
				}
			}
		}

		private void showDetailMessage(Record record) {
			if (record == null) {
				return;
			}

			msgContents.setContents(
					record.getAttribute("detailmessage").replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>").replaceAll("\r", "<br/>"));
			msgContents.setStyleName(record.getAttribute("status") + "MessageCanvas");
		}
	}

	private String getSimpleMessage(String message) {
		if (SmartGWTUtil.isEmpty(message)) {
			return "";
		}
		if (message.indexOf("\n") >= 0) {
			message = message.substring(0, message.indexOf("\n"));
		}
		return message;
	}

	private String getResourceString(String key) {
		return AdminClientMessageUtil.getString(RESOURCE_PREFIX + key);
	}

}
