/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.entityview.gem;


import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.entityexplorer.EntityViewInfoDS;

import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
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
 * EQLWorksheetパネル
 */
public class GemEntityViewListPane extends VLayout {

	private static final String EXPORT_ICON = "[SKIN]/actions/download.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";

	private CheckboxItem showCountItem;

	private Label countLabel;
	private ListGrid grid;

	/**
	 * コンストラクタ
	 */
	public GemEntityViewListPane(GemEntityViewMainPane mainPane) {

		//レイアウト設定
		setWidth100();
		setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		final ToolStripButton configExportButton = new ToolStripButton();
		configExportButton.setIcon(EXPORT_ICON);
		configExportButton.setTitle("ConfigExport");
		configExportButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_GemEntityListPane_exportEntityDef")));
		configExportButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				exportConfig();
			}
		});
		toolStrip.addButton(configExportButton);

		toolStrip.addSeparator();

		showCountItem = new CheckboxItem();
		showCountItem.setTitle("Get Data Count");
		showCountItem.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_GemEntityListPane_dataNumOften")));
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
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_entityexplorer_GemEntityListPane_refreshList")));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addButton(refreshButton);

		grid = new ListGrid();

		grid.setWidth100();
		grid.setHeight100();
		//grid.setShowAllRecords(true);		//これをtrueにすると件数が多い場合に全て表示されない不具合発生
		grid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される
		grid.setShowRowNumbers(true);		//行番号表示

		grid.setCanFreezeFields(false);
		grid.setShowSelectedStyle(false);
		grid.setCanGroupBy(false);
		grid.setCanPickFields(false);

		grid.setCanDragSelectText(true);	//セルの値をドラッグで選択可能（コピー用）にする

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

	private void exportConfig() {
		ListGridRecord[] records = grid.getSelectedRecords();
		if (records == null || records.length == 0) {
			SC.say(AdminClientMessageUtil.getString("ui_tools_entityexplorer_GemEntityListPane_selectEntityTarget"));
			return;
		}

		String[] defNames = new String[records.length];
		int i = 0;
		for (ListGridRecord record : records) {
			defNames[i] = record.getAttributeAsString(EntityViewInfoDS.FIELD_NAME.NAME.name());
			i++;
		}
		GemEntityConfigDownloadDialog dialog = new GemEntityConfigDownloadDialog(defNames);
		dialog.show();

	}


	private void refreshGrid() {
		boolean isGetDataCount = showCountItem.getValueAsBoolean();
		EntityViewInfoDS ds = EntityViewInfoDS.getInstance(isGetDataCount);
		grid.setDataSource(ds);

		//（参考）setFieldsは、setDataSource後に指定しないと効かない

		//ボタンを表示したいためListGridFieldを指定
		ListGridField nameField = new ListGridField(EntityViewInfoDS.FIELD_NAME.NAME.name(), "Name");
		ListGridField displayNameField = new ListGridField(EntityViewInfoDS.FIELD_NAME.DISPLAY_NAME.name(), "DisplayName");
		ListGridField countField = new ListGridField(EntityViewInfoDS.FIELD_NAME.DATA_COUNT.name(), "Count");
		countField.setWidth(70);
		ListGridField detailViewCountField = new ListGridField(EntityViewInfoDS.FIELD_NAME.DETAIL_VIEW_COUNT.name(), "DetailViews");
		detailViewCountField.setWidth(90);
		ListGridField searchViewCountField = new ListGridField(EntityViewInfoDS.FIELD_NAME.SEARCH_VIEW_COUNT.name(), "SearchViews");
		searchViewCountField.setWidth(90);
		ListGridField bulkViewCountField = new ListGridField(EntityViewInfoDS.FIELD_NAME.BULK_VIEW_COUNT.name(), "BulkViews");
		bulkViewCountField.setWidth(90);
		ListGridField viewControlField = new ListGridField(EntityViewInfoDS.FIELD_NAME.VIEW_CONTROL.name(), "ViewControls");
		viewControlField.setWidth(100);

		grid.setFields(nameField, displayNameField, countField, detailViewCountField, searchViewCountField, bulkViewCountField, viewControlField);

		grid.fetchData();
	}

}
