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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem.ItemOption;
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.entityexplorer.EntitySearchResultDS;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataListResultInfo;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * EntityExplorer Entityデータパネル
 */
public class EntityDataListPane extends VLayout {

	private static final String EDIT_ICON = "icon_edit.png";
	private static final String DELETE_ICON = "[SKINIMG]/MultiUploadItem/icon_remove_files.png";
	private static final String PROGRESS_ICON = "[SKINIMG]/shared/progressCursorTracker.gif";
	private static final String EXPORT_ICON = "[SKIN]/actions/download.png";
	private static final String IMPORT_ICON = "[SKIN]/SchemaViewer/operation.png";

	private EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();

	/** target entity */
	private EntityDefinition curDefinition;

	/** owner */
	private EntityDataListMainPane mainPane;

	/** top toolbar */
	private TopToolbar topToolbar;

	/** workspace */
	private WorkspaceTab workspaceTabSet;

	/** message panel */
	private MessageTabSet messageTabSet;

	/**
	 * コンストラクタ
	 */
	public EntityDataListPane(EntityDataListMainPane mainPane, String entityName) {
		this.mainPane = mainPane;

		//レイアウト設定
		setWidth100();

		//top toolbar
		topToolbar = new TopToolbar(entityName);
		topToolbar.setWidth100();

		//workspace
		workspaceTabSet = new WorkspaceTab(entityName);
		workspaceTabSet.setShowResizeBar(true);		//リサイズ可能
		workspaceTabSet.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、下を収縮

		//message panel
		messageTabSet = new MessageTabSet();
		messageTabSet.setHeight(120);

		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		mainLayout.addMember(topToolbar);
		mainLayout.addMember(workspaceTabSet);
		mainLayout.addMember(messageTabSet);

		addMember(mainLayout);

		initialize(entityName);
	}

	private void initialize(String entityName) {
		entityChanged(entityName);
	}


	/**
	 * Entity一覧に戻る処理
	 */
	private void backEntityList() {
		mainPane.backEntityListPane();
	}

	/**
	 * 対象Entity変更処理
	 *
	 * @param entityName 変更Entity名
	 * @param criteria 検索条件
	 */
	private void entityChanged(String entityName) {
		//対象EntityDefinitionをクリア
		curDefinition = null;

		//サーバ接続時初期化処理
		startExecute("init");

		//Workspaceに対象Entity名セット
		workspaceTabSet.setEntityName(entityName);
		workspaceTabSet.clearEntity();

		//TOPのタブ名を変更
		mainPane.setWorkspaceTabName(entityName);

		//Definition定義取得
		getEntityDefinition(entityName);

	}

	private void startExecute(String message) {
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();

		workspaceTabSet.setTabTitleProgress(message);
	}

	private void finishExecute() {
		messageTabSet.setTabTitleNormal();
		workspaceTabSet.setTabTitleNormal();
	}

	private void getEntityDefinition(final String entityName) {

		MetaDataServiceAsync service = MetaDataServiceFactory.get();
		service.getEntityDefinition(TenantInfoHolder.getId(), entityName, new AsyncCallback<EntityDefinition>() {

			@Override
			public void onSuccess(EntityDefinition definition) {
				curDefinition = definition;

				//WorkspaceにDefinitionをセット
				workspaceTabSet.setDefinition(definition);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);
				List<String> messages = new ArrayList<>();
				messages.add(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_failedToGetEntityDef"));
				messages.add(caught.getMessage());

				messageTabSet.setErrorMessage(messages);

				finishExecute();
			}
		});
	}

	/**
	 * TOP Toolbar
	 */
	private class TopToolbar extends HLayout {

		/** 対象Entity名 */
		private SelectItem entityField;

		/**
		 * コンストラクタ
		 */
		private TopToolbar(final String entityName) {
			setWidth100();
			setHeight(30);
			setMembersMargin(10);

			//------------------------
			//Entity一覧に戻る
			//------------------------
			IButton showListButton = new IButton("＜ Entity List");
			showListButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_notRefreshAutoReturnList")));
			showListButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					backEntityList();
				}
			});

			//------------------------
			//Entity選択
			//------------------------
			DynamicForm form = new DynamicForm();
			form.setMargin(0);
			form.setWrapItemTitles(false);
			form.setAutoFocus(true);
			form.setColWidths(100, 300, 10);
			form.setAutoWidth();

			entityField = new MetaDataSelectItem(EntityDefinition.class, "Entity", new ItemOption(false, false, true));
			entityField.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					entityChanged(entityField.getValueAsString());
				}
			});
			entityField.setValue(entityName);	//初期選択

			form.setFields(entityField);

			//------------------------
			//Config Export
			//------------------------
			IButton configExportButton = new IButton("Export Config");
			configExportButton.setIcon(EXPORT_ICON);
			configExportButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_exportEntityDef")));
			configExportButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					exportConfig();
				}
			});

			//------------------------
			//CSV  Data Import
			//------------------------
			IButton importCSVButton = new IButton("Import CSV");
			importCSVButton.setIcon(IMPORT_ICON);
			importCSVButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_importCsvFile")));
			importCSVButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					importCSVData();
				}
			});

			addMember(showListButton);
			addMember(form);
			addMember(configExportButton);
			addMember(importCSVButton);
		}

		private void exportConfig() {
			if (curDefinition == null) {
				return;
			}
			EntityConfigDownloadDialog dialog = new EntityConfigDownloadDialog(curDefinition.getName());
			dialog.show();
		}

		private void importCSVData() {
			if (curDefinition == null) {
				return;
			}

			EntityCsvUploadDialog.showFullScreen(curDefinition);
		}

	}

	/**
	 * Worksheet Tab
	 */
	private class WorkspaceTab extends TabSet {

		private String entityName;

		private Tab workspaceTab;
		private WorkspacePane workspacePane;

		public WorkspaceTab(String entityName) {
			this.entityName = entityName;

			setTabBarPosition(Side.TOP);
			setWidth100();
			setHeight100();

			workspaceTab = new Tab();
			workspaceTab.setTitle(entityName);

			workspacePane = new WorkspacePane();

			workspaceTab.setPane(workspacePane);
			addTab(workspaceTab);
		}

		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}

		public void clearEntity() {
			workspacePane.clearEntity();
		}

		public void setDefinition(EntityDefinition definition) {
			workspacePane.setDefinition(definition);
		}

		public void setTabTitleNormal() {
			workspaceTab.setTitle(entityName);
		}

		public void setTabTitleProgress(String message) {
			workspaceTab.setTitle("<span>" + Canvas.imgHTML(PROGRESS_ICON) + "&nbsp;" + message + "...</span>");
		}

	}

	/**
	 * Worksheet Pane
	 */
	private class WorkspacePane extends VLayout {

		private static final int LIMIT = 30;

		private CriteriaSettingPane criteriaPane;

		private ListGrid grid;

		private CheckboxItem showAllPropertyItem;

		private Label pageNumLabel;
		private int pageNum;

		private Criteria curCriteria;

		private EntitySearchResultDS ds;

		public WorkspacePane() {
			setWidth100();
			setHeight100();

			criteriaPane = new CriteriaSettingPane(this);

			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setWidth100();
			toolStrip.setMembersMargin(5);
			toolStrip.setAlign(VerticalAlignment.BOTTOM);

			//------------------------
			//Delete
			//------------------------
			final ToolStripButton deleteButton = new ToolStripButton();
			deleteButton.setIcon(DELETE_ICON);
			deleteButton.setTitle("Delete");
			SmartGWTUtil.addHoverToCanvas(deleteButton, AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_deleteSelectEntity"));
			deleteButton.setHoverWrap(false);
			deleteButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					deleteData();
				}
			});
			toolStrip.addButton(deleteButton);

			toolStrip.addSeparator();

			//------------------------
			//Search Option
			//------------------------
			showAllPropertyItem = new CheckboxItem();
			showAllPropertyItem.setTitle("show inherited properties");
			showAllPropertyItem.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_showAllPropInherit")));
			showAllPropertyItem.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					showAllProperty(showAllPropertyItem.getValueAsBoolean());
				}
			});
			toolStrip.addFormItem(showAllPropertyItem);

			toolStrip.addFill();

			//------------------------
			//Paging
			//------------------------
			final ToolStripButton firstButton = new ToolStripButton();
			firstButton.setIcon("resultset_first.png");
			firstButton.setTitle("First");
			firstButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doFirst();
				}
			});
			toolStrip.addButton(firstButton);

			final ToolStripButton prevButton = new ToolStripButton();
			prevButton.setIcon("resultset_previous.png");
			prevButton.setTitle("Prev");
			prevButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doPrev();
				}
			});
			toolStrip.addButton(prevButton);

			final ToolStripButton nextButton = new ToolStripButton();
			nextButton.setIcon("resultset_next.png");
			nextButton.setTitle("Next");
			nextButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doNext();
				}
			});
			toolStrip.addButton(nextButton);

			toolStrip.addSeparator();

			pageNumLabel = new Label();
			pageNumLabel.setWrap(false);
			pageNumLabel.setAutoWidth();
			toolStrip.addMember(pageNumLabel);

			Label pageLabel = new Label();
			pageLabel.setWrap(false);
			pageLabel.setAutoWidth();
			pageLabel.setContents("Page");
			toolStrip.addMember(pageLabel);

			toolStrip.addSpacer(5);

			//------------------------
			//Grid
			//------------------------
			grid = new ListGrid() {
				@Override
				protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
					final String fieldName = this.getFieldName(colNum);
					if (ds != null && ds.isPopupColumn(fieldName)) {
						IButton button = new IButton();
						button.setHeight(18);
						button.setWidth(80);
						button.setIcon(MtpWidgetConstants.ICON_SEARCH);
						button.setTitle("Show...");
						button.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								Entity entity = (Entity)record.getAttributeAsObject(EntitySearchResultDS.ENTITY_ATTRIBUTE_NAME);
								if (entity == null) {
									SC.warn(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_canNotGetRecordInfo"));
								} else {
									MultiReferencePropertyDialog dialog =
										new MultiReferencePropertyDialog(ds.getDefinition(), entity, fieldName);
									dialog.show();
								}
							}
						});
						return button;
					}
					return null;
				}

			};
			grid.setWidth100();
			grid.setHeight100();
			grid.setShowAllRecords(false);		//データ件数が多い場合を考慮し、false
			grid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される

			grid.setAutoFitFieldWidths(true);							//データにより幅自動調節
//			grid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//幅の調節をタイトルとデータに設定
			grid.setAutoFitWidthApproach(AutoFitWidthApproach.TITLE);	//幅の調節をタイトルとデータに設定
			grid.setAutoFitFieldsFillViewport(false);					//幅が足りないときに先頭行の自動的に伸ばさない

			grid.setShowRowNumbers(true);		//行番号表示
			grid.setCanDragSelectText(true);	//セルの値をドラッグで選択可能（コピー用）にする

			grid.setCanSort(false);
			grid.setCanGroupBy(false);
			grid.setCanPickFields(false);

			//CheckBox選択設定
			grid.setSelectionType(SelectionStyle.SIMPLE);
			grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

			grid.addDataArrivedHandler(new DataArrivedHandler() {

				@Override
				public void onDataArrived(DataArrivedEvent event) {

					if (!EntitySearchResultDS.isBlankDS(ds)) {
						//メッセージなどの表示
						showResultInfo(ds.getResult());
					}
				}
			});

			//データ編集画面表示
			grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					//TODO 個別編集画面
//					EntityDefinition definition = ds.getDefinition();
//					String oid = event.getRecord().getAttributeAsString(Entity.OID);
//					//DSでtoStringしているので。
//					String version = event.getRecord().getAttributeAsString(Entity.VERSION);
//					mainPane.showDataEditPane(definition, oid, Long.valueOf(version));
				}
			});

			//この２つを指定することでcreateRecordComponentが有効
			grid.setShowRecordComponents(true);
			grid.setShowRecordComponentsByCell(true);

			//明示的にFetchする
			grid.setAutoFetchData(false);

			addMember(criteriaPane);
			addMember(toolStrip);
			addMember(grid);
		}

		public void clearEntity() {
			//条件のクリア
			criteriaPane.clearCondition();

			//Pageの初期化
			setPageNum(1);

			//ダミーDSのセット
			setDataSource(EntitySearchResultDS.getBlankInstance());
		}

		public void setDefinition(EntityDefinition definition) {
			//DSの作成
			EntitySearchResultDS ds = EntitySearchResultDS.getInstance(definition);
			ds.setShowInheritedProperty(showAllPropertyItem.getValueAsBoolean());

			setDataSource(ds);

			//fetch ← 初期表示時、Entity変更時では検索を行わない
			//doFetch(false);

			finishExecute();
		}

		public void doFetch(boolean isCurrent) {
			startExecute("search");

			//条件の作成
			Criteria criteria = null;
			if (isCurrent && curCriteria != null) {
				criteria = curCriteria;
			} else {
				criteria = criteriaPane.createCriteria();
			}

			//limit情報は最新にする
			setCriteriaLimit(criteria);

			//fetch
			grid.fetchData(criteria);

			//条件の保持
			curCriteria = criteria;
		}

		public Criteria getCurrentCriteria() {
			return curCriteria;
		}

		public boolean isSearchError() {
			return (ds.getResult() == null || ds.getResult().isError());
		}

		private void setDataSource(EntitySearchResultDS ds) {
			if (this.ds != null) {
				this.ds.destroy();
				this.ds = null;
			}
			this.ds = ds;
			grid.setDataSource(ds);
			grid.setFields(ds.getListGridFields());
		}

		private void setPageNum(int page) {
			pageNum = page;
			pageNumLabel.setContents(pageNum + "");
		}

		private int getPageNum() {
			return pageNum;
		}

		private void doFirst() {
			int page = getPageNum();
			if (page == 1) {
				return;
			}
			setPageNum(1);

			doFetch(true);
		}

		private void doPrev() {
			int page = getPageNum();
			if (page == 1) {
				return;
			}
			setPageNum(page - 1);

			doFetch(true);
		}

		private void doNext() {
			int page = getPageNum();
			setPageNum(page + 1);

			doFetch(true);
		}

		private void setCriteriaLimit(Criteria criteria) {
			criteria.addCriteria(EntitySearchResultDS.LIMIT_CRITERIA, LIMIT);
			criteria.addCriteria(EntitySearchResultDS.OFFSET_CRITERIA, getOffset());
			criteria.addCriteria("dummy", System.currentTimeMillis() + "");	//同じ条件だとDSに飛ばないので
		}

		private int getOffset() {

			return (getPageNum() - 1) * LIMIT ;
		}

		private void showResultInfo(EntityDataListResultInfo result) {

			if (result.isError()) {
				messageTabSet.setErrorMessage(result.getLogMessages());
			} else {
				messageTabSet.setMessage(result.getLogMessages());
			}

			finishExecute();
		}

		private void showAllProperty(boolean isAllShow) {
			if (isAllShow) {
				ds.setShowInheritedProperty(true);
			} else {
				ds.setShowInheritedProperty(false);
			}
			grid.refreshFields();
		}

		private void deleteData() {
			if (isSearchError()) {
				return;
			}

			//Pagingしているので全件選択チェックボックスが選択できない場合があるので
			//選択データがない場合も表示可能にする
//			//選択チェック
//			if (!isSelected()) {
//				return;
//			}

			//選択OIDの取得
			ListGridRecord[] records = grid.getSelectedRecords();
			List<String> oids = new ArrayList<>(records.length);
			for (ListGridRecord record : records) {
				oids.add(record.getAttribute("oid"));
			}

			//検索時の検索条件を取得
			Criteria gridCriteria = getCurrentCriteria();
			String gridWhere = "";
			if (gridCriteria != null) {
				gridWhere = gridCriteria.getAttribute(EntitySearchResultDS.WHERE_CRITERIA);
			}

			//ダイアログ表示
			EntityDataDeleteDialog dialog = new EntityDataDeleteDialog(ds.getDefinition().getName(), oids, gridWhere);
			dialog.show();
		}
	}

	private class CriteriaSettingPane extends VLayout {

		private WorkspacePane workspace;

		private TextAreaItem whereField;
		private TextAreaItem orderByField;
		private CheckboxItem searchAllVersion;

		public CriteriaSettingPane(WorkspacePane workspace) {
			this.workspace = workspace;

			setWidth100();
			//setHeight100();
			setHeight(200);
			setMembersMargin(5);

			DynamicForm form = new DynamicForm();
			form.setWidth100();
			form.setHeight100();
			form.setNumCols(2);
			form.setColWidths(70, "*");
			form.setAutoFocus(true);

			whereField = new TextAreaItem();
			whereField.setTitle("Where");
			whereField.setWidth("100%");
			whereField.setHeight("100%");
			whereField.setSelectOnFocus(true);

			orderByField = new TextAreaItem();
			orderByField.setTitle("Oder By");
			orderByField.setWidth("100%");
			orderByField.setHeight(50);
			orderByField.setSelectOnFocus(true);

			searchAllVersion = new CheckboxItem();
			searchAllVersion.setTitle("seach all version");
			searchAllVersion.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_allVerSearch")));


			HLayout footer = new HLayout();
			footer.setHeight(30);
			footer.setMembersMargin(10);

			IButton searchButton = new IButton("Search");
			searchButton.setIcon(MtpWidgetConstants.ICON_SEARCH);
			searchButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_correCondEntitySearch")));
			searchButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					searchData();
				}
			});

			IButton countButton = new IButton("Count");
			countButton.setIcon(MtpWidgetConstants.ICON_SEARCH);
			countButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_correCondEntityCount")));
			countButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					countData();
				}
			});

			IButton exportCSVButton = new IButton("Export CSV");
			exportCSVButton.setIcon(EXPORT_ICON);
			exportCSVButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_exportEntityCorreCond")));
			exportCSVButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					exportCsv();
				}
			});

			IButton exportPackButton = new IButton("Export Package");
			exportPackButton.setIcon(EXPORT_ICON);
			exportPackButton.setAutoFit(true);
			exportPackButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_exportEntityPackCond")));
			exportPackButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					exportPack();
				}
			});

			IButton updateAllButton = new IButton("Update ALL");
			updateAllButton.setIcon(EDIT_ICON);
			updateAllButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_updateEntityCorreCond")));
			updateAllButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					updateAll();
				}
			});

			IButton deleteAllButton = new IButton("Delete ALL");
			deleteAllButton.setIcon(DELETE_ICON);
			deleteAllButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_deleteEntityCorreCond")));
			deleteAllButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					deleteAll();
				}
			});

			LayoutSpacer space1 = new LayoutSpacer();
			space1.setWidth(70);

			footer.addMember(space1);
			footer.addMember(searchButton);
			footer.addMember(countButton);
			footer.addMember(exportCSVButton);
			footer.addMember(exportPackButton);

			LayoutSpacer space2 = new LayoutSpacer();
			space2.setWidth(50);

			footer.addMember(space2);
			footer.addMember(updateAllButton);
			footer.addMember(deleteAllButton);

			form.setFields(whereField, orderByField, searchAllVersion);

			addMember(form);
			addMember(footer);
		}

		public void clearCondition() {
//			whereField.clearValue();
//			orderByField.clearValue();
			whereField.setValue("");
			orderByField.setValue("");
		}

		public Criteria createCriteria() {
			Criteria criteria = new Criteria();
			criteria.addCriteria(EntitySearchResultDS.WHERE_CRITERIA, getWhere2());
			criteria.addCriteria(EntitySearchResultDS.ORDERBY_CRITERIA, getOrderBy2());
			criteria.addCriteria(EntitySearchResultDS.VERSIONED_CRITERIA, isSearchAllVersion());

			//TODO 暫定対応
			workspace.ds.setWhereString(getWhere2());
			workspace.ds.setOrderByString(getOrderBy2());

			return criteria;
		}

		public String getWhere2() {
			return SmartGWTUtil.getStringValue(whereField);
		}
		public String getWhere() {
			String where = SmartGWTUtil.getStringValue(whereField);

			return (where != null ? where : "");
		}
		public String getOrderBy2() {
			return SmartGWTUtil.getStringValue(orderByField);
		}
		public String getOrderBy() {
			String orderBy = SmartGWTUtil.getStringValue(orderByField);

			return (orderBy != null ? orderBy : "");
		}
		public boolean isSearchAllVersion() {
			return SmartGWTUtil.getBooleanValue(searchAllVersion);
		}

		//検索ボタン処理
		private void searchData() {
			if (curDefinition == null) {
				return;
			}

			//Pageを先頭に変更
			workspace.setPageNum(1);

			//検索
			workspace.doFetch(false);
		}

		//件数ボタン処理
		private void countData() {
			if (curDefinition == null) {
				return;
			}

			doCount();
		}

		//CSV Exportボタン処理
		private void exportCsv() {
			if (curDefinition == null) {
				return;
			}

			//一覧の検索条件取得
			Criteria gridCriteria = workspace.getCurrentCriteria();

			if (gridCriteria == null) {
				//検索しないでCSV Exportを直接実行

				//現在の検索条件の検証
				validateCriteria(true, true, new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							showCsvDownloadDialog();
						}
					}
				});
			} else {
				//検索時の条件と一致しているかをチェック
				String gridWhere = gridCriteria.getAttribute(EntitySearchResultDS.WHERE_CRITERIA);
				String gridOrderBy = gridCriteria.getAttribute(EntitySearchResultDS.ORDERBY_CRITERIA);
				boolean gridAllVer = gridCriteria.getAttributeAsBoolean(EntitySearchResultDS.VERSIONED_CRITERIA);
				if (!gridWhere.equals(getWhere())
						|| !gridOrderBy.equals(getOrderBy())
						|| gridAllVer != isSearchAllVersion()) {
					SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_confirm"),
							AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_searchCondDiff")
							, new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (!value) {
								return;
							}
							//現在の検索条件の検証
							validateCriteria(true, true, new BooleanCallback() {

								@Override
								public void execute(Boolean value) {
									if (value) {
										showCsvDownloadDialog();
									}
								}
							});
						}
					});
				} else {
					//検索実行時にエラーが発生していないかを確認
					if (workspace.isSearchError()) {
						SC.warn(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_runSearchErr"));
						return;
					}

					//直接CSV出力画面へ
					showCsvDownloadDialog();
				}
			}
		}

		//Package Exportボタン処理
		private void exportPack() {
			if (curDefinition == null) {
				return;
			}

			//一覧の検索条件取得
			Criteria gridCriteria = workspace.getCurrentCriteria();

			if (gridCriteria == null) {
				//検索しないでPackage Exportを直接実行

				//現在の検索条件の検証
				validateCriteria(true, true, new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							executePack();
						}
					}
				});
			} else {
				//検索時の条件と一致しているかをチェック
				String gridWhere = gridCriteria.getAttribute(EntitySearchResultDS.WHERE_CRITERIA);
				String gridOrderBy = gridCriteria.getAttribute(EntitySearchResultDS.ORDERBY_CRITERIA);
				boolean gridAllVer = gridCriteria.getAttributeAsBoolean(EntitySearchResultDS.VERSIONED_CRITERIA);
				if (!gridWhere.equals(getWhere())
						|| !gridOrderBy.equals(getOrderBy())
						|| gridAllVer != isSearchAllVersion()) {
					SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_confirm"),
							AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_searchCondDiff")
							, new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (!value) {
								return;
							}
							//現在の検索条件の検証
							validateCriteria(true, true, new BooleanCallback() {

								@Override
								public void execute(Boolean value) {
									if (value) {
										executePack();
									}
								}
							});
						}
					});
				} else {
					//検索実行時にエラーが発生していないかを確認
					if (workspace.isSearchError()) {
						SC.warn(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_runSearchErr"));
						return;
					}

					executePack();
				}
			}
		}

		private void executePack() {

			SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_confirm"),
					AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_exportEntityPackConfirm")
					, new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (!value) {
						return;
					}

					PostDownloadFrame frame = new PostDownloadFrame();
					frame.setAction(GWT.getModuleBaseURL() + "service/entitypackagedownload")
						.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
						.addParameter("definitionName", curDefinition.getName())
						.addParameter("whereClause", getWhere())
						.addParameter("orderByClause", getOrderBy())
						.addParameter("isSearchAllVersion", isSearchAllVersion() + "")
						.execute();
				}
			});

		}

		//Update Allボタン処理
		private void updateAll() {

			//編集画面表示前にWhere条件の検証を行うようにしていたが、
			//編集画面側で入力できるようにしたため、検証しないように変更。
			//また不正なデータがある場合に一覧表示時(search時)はエラーになるが、
			//強制的にupdateAllしたい場合もあることを考慮

			if (curDefinition == null) {
				return;
			}

			//一覧の検索条件取得
			Criteria gridCriteria = workspace.getCurrentCriteria();

			if (gridCriteria == null) {
				//検索しないで直接実行

//				//現在の検索条件の検証
//				validateCriteria(false, false, new BooleanCallback() {
//
//					@Override
//					public void execute(Boolean value) {
//						if (value) {
//							showUpdateAllDialog();
//						}
//					}
//				});

				showUpdateAllDialog();

			} else {
				//検索時の条件と一致しているかをチェック
				String gridWhere = gridCriteria.getAttribute(EntitySearchResultDS.WHERE_CRITERIA);
				if (!gridWhere.equals(getWhere())) {
					SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_confirm"),
							AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_searchCondDiff")
							, new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (!value) {
								return;
							}
//							//現在の検索条件の検証
//							validateCriteria(false, false, new BooleanCallback() {
//
//								@Override
//								public void execute(Boolean value) {
//									if (value) {
//										showUpdateAllDialog();
//									}
//								}
//							});

							showUpdateAllDialog();
						}
					});
				} else {
//					//検索実行時にエラーが発生していないかを確認
//					if (workspace.isSearchError()) {
//						SC.warn("検索を実行した際にエラーが発生しています。"
//								+ "<br/>エラー内容を確認してください。");
//						return;
//					}

					//直接編集画面へ
					showUpdateAllDialog();
				}
			}
		}

		private void doCount() {
			//サーバ接続時初期化処理
			startExecute("count");

			service.count(TenantInfoHolder.getId(), curDefinition.getName(),
					getWhere(), isSearchAllVersion(), new AsyncCallback<EntityDataListResultInfo>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("error!!!", caught);
					List<String> messages = new ArrayList<>();
					messages.add(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_runEqlErr"));
					messages.add(caught.getMessage());

					messageTabSet.setErrorMessage(messages);

					finishExecute();
				}

				@Override
				public void onSuccess(EntityDataListResultInfo result) {
					if (result.isError()) {
						messageTabSet.setErrorMessage(result.getLogMessages());

						finishExecute();
					} else {
						messageTabSet.setMessage(result.getLogMessages());

						finishExecute();
					}
				}

			});

		}

		private void validateCriteria(boolean checkOrder, boolean checkVersion, final BooleanCallback callback) {
			//サーバ接続時初期化処理
			startExecute("validate");

			service.validateCriteria(TenantInfoHolder.getId(), curDefinition.getName(),
					getWhere(), (checkOrder ? getOrderBy() : null), (checkVersion ? isSearchAllVersion() : false), new AsyncCallback<EntityDataListResultInfo>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("error!!!", caught);
					List<String> messages = new ArrayList<>();
					messages.add(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_runEqlErr"));
					messages.add(caught.getMessage());

					messageTabSet.setErrorMessage(messages);

					finishExecute();

					SC.warn(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_runEqlErrSpe"));

					//処理を戻す
					callback.execute(false);
				}

				@Override
				public void onSuccess(EntityDataListResultInfo result) {
					if (result.isError()) {
						messageTabSet.setErrorMessage(result.getLogMessages());
						SC.warn(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_runEqlErrSpe"));

						finishExecute();

						//処理を戻す
						callback.execute(false);

					} else {
						messageTabSet.setMessage(result.getLogMessages());

						finishExecute();

						//OK処理を戻す
						callback.execute(true);
					}
				}

			});
		}

		private void deleteAll() {

			//編集画面表示前にWhere条件の検証を行うようにしていたが、
			//編集画面側で入力できるようにしたため、検証しないように変更。
			//また不正なデータがある場合に一覧表示時(search時)はエラーになるが、
			//強制的にdeleteAllしたい場合もあることを考慮

			if (curDefinition == null) {
				return;
			}

			//一覧の検索条件取得
			Criteria gridCriteria = workspace.getCurrentCriteria();

			if (gridCriteria == null) {
				//検索しないで直接実行

//				//現在の検索条件の検証
//				validateCriteria(false, false, new BooleanCallback() {
//
//					@Override
//					public void execute(Boolean value) {
//						if (value) {
//							showDeleteAllDialog();
//						}
//					}
//				});

				showDeleteAllDialog();

			} else {
				//検索時の条件と一致しているかをチェック
				String gridWhere = gridCriteria.getAttribute(EntitySearchResultDS.WHERE_CRITERIA);
				if (!gridWhere.equals(getWhere())) {
					SC.ask(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_confirm"),
							AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityDataListPane_searchCondDiff")
							, new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (!value) {
								return;
							}
//							//現在の検索条件の検証
//							validateCriteria(false, false, new BooleanCallback() {
//
//								@Override
//								public void execute(Boolean value) {
//									if (value) {
//										showDeleteAllDialog();
//									}
//								}
//							});

							showDeleteAllDialog();
						}
					});
				} else {
//					//検索実行時にエラーが発生していないかを確認
//					if (workspace.isSearchError()) {
//						SC.warn("検索を実行した際にエラーが発生しています。"
//								+ "<br/>エラー内容を確認してください。");
//						return;
//					}

					//直接編集画面へ
					showDeleteAllDialog();
				}
			}
		}

		private void showCsvDownloadDialog() {
			EntityCsvDownloadDialog dialog = new EntityCsvDownloadDialog(
					curDefinition.getName(), getWhere(), getOrderBy(), isSearchAllVersion());
			dialog.show();
		}

		private void showUpdateAllDialog() {
			EntityUpdateAllDialog.showFullScreen(curDefinition, getWhere());
		}

		private void showDeleteAllDialog() {
			EntityDataDeleteDialog dialog = new EntityDataDeleteDialog(curDefinition.getName(), null, getWhere());
			dialog.show();
		}
	}

}
