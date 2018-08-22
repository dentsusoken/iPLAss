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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.crawl;


import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.GridActionImgButton;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataViewGridButton;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.entityexplorer.CrawlEntityInfoDS;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;

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
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * クロール対象エンティティパネル
 */
public class EntityCrawlListPane extends VLayout {

	private static final String RE_CRAWL_ICON = "arrow_refresh_small.png";
	private static final String EXECUTE_ICON = "[SKIN]/actions/forward.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";
	private static final String ERROR_ICON = "[SKINIMG]/actions/exclamation.png";

	private CheckboxItem showCountItem;

	private Label countLabel;
	private ListGrid grid;

	private EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();

	/**
	 * コンストラクタ
	 */
	public EntityCrawlListPane() {

		//レイアウト設定
		setWidth100();
		setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		final ToolStripButton startCrawlButton = new ToolStripButton();
		startCrawlButton.setIcon(EXECUTE_ICON);
		startCrawlButton.setTitle("Start Crawl");
		startCrawlButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_startCrawling")));
		startCrawlButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				startCrawl();
			}
		});
		toolStrip.addButton(startCrawlButton);

		toolStrip.addSeparator();

		final ToolStripButton deleteButton = new ToolStripButton();
		deleteButton.setIcon(RE_CRAWL_ICON);
		deleteButton.setTitle("Re Crawl All Entity");
		deleteButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_deleteAllIndex"));
		deleteButton.setHoverWrap(false);
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ListGridRecord[] records = grid.getRecords();
				if (records == null || records.length == 0) {
					SC.say("There is no target entity.");
					return;
				}

				SC.ask(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_confirm"),
						AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_startReCreateDeleteAllIndex"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							startReCrawl();
						}
					}
				});
			}

		});
		toolStrip.addButton(deleteButton);
		toolStrip.addSeparator();

		final ToolStripButton refreshIndexChangesButton = new ToolStripButton();
		refreshIndexChangesButton.setIcon(RE_CRAWL_ICON);
		refreshIndexChangesButton.setTitle("Refresh");
		refreshIndexChangesButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_refreshIndexChanges"));
		refreshIndexChangesButton.setHoverWrap(false);
		refreshIndexChangesButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				SC.ask(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_confirm"),
						AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_startRefreshIndexChanges"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							startRefresh();
						}
					}
				});
			}

		});
		toolStrip.addButton(refreshIndexChangesButton);
		toolStrip.addSeparator();

		showCountItem = new CheckboxItem();
		showCountItem.setTitle("Get Data Count");
		showCountItem.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_entityDataNumberLarge")));
		showCountItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addFormItem(showCountItem);

		toolStrip.addFill();

		countLabel = new Label();
		countLabel.setWrap(false);
		countLabel.setAutoWidth();
		setRecordCount(0);
		toolStrip.addMember(countLabel);

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(REFRESH_ICON);
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_refreshList")));
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
				final String fieldName = this.getFieldName(colNum);
				if ("explorerButton".equals(fieldName)) {
					if (!record.getAttributeAsBoolean("isError")){
						MetaDataViewGridButton button = new MetaDataViewGridButton(EntityDefinition.class.getName());
						button.setActionButtonPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_showMetaDataEditScreen")));
						button.setMetaDataShowClickHandler(new MetaDataViewGridButton.MetaDataShowClickHandler() {
							@Override
							public String targetDefinitionName() {
								return record.getAttributeAsString("name");
							}
						});
						return button;
					}
				} else if ("error".equals(fieldName)) {
					if (record.getAttributeAsBoolean("isError")){
						record.setEnabled(false);
						GridActionImgButton recordCanvas = new GridActionImgButton();
						recordCanvas.setActionButtonSrc(ERROR_ICON);
						recordCanvas.setActionButtonPrompt(record.getAttributeAsString("errorMessage"));
						return recordCanvas;
					}
				}
				return null;
			}
		};

		grid.setWidth100();
		grid.setHeight100();
		//grid.setShowAllRecords(true);		//これをtrueにすると件数が多い場合に全て表示されない不具合発生
		grid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される
		grid.setShowRowNumbers(true);		//行番号表示

		grid.setCanFreezeFields(false);
		grid.setShowSelectedStyle(false);
		grid.setCanGroupBy(false);
		grid.setCanPickFields(false);

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
			}
		});

		addMember(toolStrip);
		addMember(grid);

		refreshGrid();
	}

	public void refresh() {
		refreshGrid();
	}

	private void setRecordCount(long count) {
		countLabel.setContents("Total Count：" + count);
	}

	private void startCrawl() {
		SmartGWTUtil.showProgress("crawling entity ....");

		ListGridRecord[] records = grid.getSelectedRecords();
		if (records == null || records.length == 0) {
			SmartGWTUtil.hideProgress();
			SC.say(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_selectEntityTarget"));
			return;
		}

		String[] defNames = new String[records.length];
		int cnt = 0;
		for (ListGridRecord record : records) {
			defNames[cnt] = record.getAttributeAsString("name");
			cnt ++;
		}

		service.execCrawlEntity(TenantInfoHolder.getId(), defNames, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();
				SC.say("failed","Failed to crawling entity." + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(Void result) {
				SC.say(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_completion"),
						AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_crawlingComp"));
				SmartGWTUtil.hideProgress();
				refreshGrid();
			}
		});
	}

	private void startReCrawl() {
		SmartGWTUtil.showProgress("crawling entity ....");

		service.execReCrawlEntity(TenantInfoHolder.getId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();
				SC.say("failed","Failed to crawling entity." + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(Void result) {
				SC.say(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_completion"),
						AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_crawlingComp"));
				SmartGWTUtil.hideProgress();
				refreshGrid();
			}
		});
	}

	private void startRefresh() {
		SmartGWTUtil.showProgress("refreshing ....");

		service.execRefresh(TenantInfoHolder.getId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();
				SC.say("failed", "Failed to refresh." + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(Void result) {
				SC.say(AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_completion"),
						AdminClientMessageUtil.getString("ui_tools_fulltextsearch_EntityListPane_refreshingComp"));
				SmartGWTUtil.hideProgress();
			}
		});
	}


	private void refreshGrid() {
		boolean isGetDataCount = showCountItem.getValueAsBoolean();
		CrawlEntityInfoDS ds = CrawlEntityInfoDS.getInstance(isGetDataCount);
		grid.setDataSource(ds);

		//（参考）setFieldsは、setDataSource後に指定しないと効かない

		//ボタンを表示したいためListGridFieldを指定
		ListGridField explorerField = new ListGridField("explorerButton", " ");
		explorerField.setWidth(25);
		ListGridField errorField = new ListGridField("error", " ");
		errorField.setWidth(25);
		ListGridField nameField = new ListGridField("name", "Name");
		ListGridField displayNameField = new ListGridField("displayName", "DisplayName");
		ListGridField countField = new ListGridField("count", "Data Count");
		countField.setWidth(70);
		ListGridField updateDateField = new ListGridField("updateDate", "Update Date");
		updateDateField.setWidth(130);
		ListGridField lastCrawlDateField = new ListGridField("lastCrawlDate", "Last Crawl Date");
		lastCrawlDateField.setWidth(130);
		grid.setFields(explorerField, errorField, nameField, displayNameField, countField, updateDateField, lastCrawlDateField);

		grid.fetchData();
	}

}
