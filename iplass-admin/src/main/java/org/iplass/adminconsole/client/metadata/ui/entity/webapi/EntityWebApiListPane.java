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

package org.iplass.adminconsole.client.metadata.ui.entity.webapi;


import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.data.entity.webapi.EntityWebApiDS;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * EntityWebApiDefinitionパネル
 */
public class EntityWebApiListPane extends VLayout {

	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";

	private Label countLabel;
	private ListGrid grid;
	private List<String> editDefinitionList;

	/**
	 * コンストラクタ
	 */
	public EntityWebApiListPane() {

		//レイアウト設定
		setWidth100();
		setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		toolStrip.addFill();

		countLabel = new Label();
		countLabel.setWrap(false);
		countLabel.setAutoWidth();
		setRecordCount(0);
		toolStrip.addMember(countLabel);

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(REFRESH_ICON);
		refreshButton.setTooltip(AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionListPane_refreshList"));
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
		grid.setShowAllRecords(true);
		grid.setCellHeight(22);
		grid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される
		grid.setShowRowNumbers(true);		//行番号表示

		//CheckBox選択設定
		grid.setSelectionType(SelectionStyle.SINGLE);
		grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		grid.setSelectOnEdit(false);

		grid.addDataArrivedHandler(new DataArrivedHandler() {
			@Override
			public void onDataArrived(DataArrivedEvent event) {
				setRecordCount(grid.getTotalRows());
			}
		});

		grid.setShowRecordComponents(true);
		grid.setShowRecordComponentsByCell(true);

		addMember(toolStrip);
		addMember(grid);

		refreshGrid();
	}

	public void refresh() {
		refreshGrid();
	}

	private void setRecordCount(long count) {
		countLabel.setContents(AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionListPane_number") + count);
	}

	private void refreshGrid() {

		editDefinitionList = new ArrayList<String>();

		EntityWebApiDS ds = EntityWebApiDS.getInstance();
		grid.setDataSource(ds);

	    //（参考）setFieldsは、setDataSource後に指定しないと効かない
		ListGridField nameField = new ListGridField("name", AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionListPane_name"));
		nameField.setCanEdit(false);
		ListGridField displayNameField = new ListGridField("displayName", AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionListPane_dispName"));
		displayNameField.setCanEdit(false);

		ListGridField isInsert = new ListGridField("isInsert", "INSERT");
		isInsert.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				Record record = grid.getRecord(event.getRowNum());
				String defName = record.getAttributeAsString("name");

				if (!editDefinitionList.contains(defName)) {
					editDefinitionList.add(defName);
				}
			}
		});

		ListGridField isLoad = new ListGridField("isLoad", "SELECT(Load)");
		isLoad.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				Record record = grid.getRecord(event.getRowNum());
				String defName = record.getAttributeAsString("name");

				if (!editDefinitionList.contains(defName)) {
					editDefinitionList.add(defName);
				}
			}
		});

		ListGridField isQuery = new ListGridField("isQuery", "SELECT(Query)");
		isQuery.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				Record record = grid.getRecord(event.getRowNum());
				String defName = record.getAttributeAsString("name");

				if (!editDefinitionList.contains(defName)) {
					editDefinitionList.add(defName);
				}
			}
		});

		ListGridField isUpdate = new ListGridField("isUpdate", "UPDATE");
		isUpdate.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				Record record = grid.getRecord(event.getRowNum());
				String defName = record.getAttributeAsString("name");

				if (!editDefinitionList.contains(defName)) {
					editDefinitionList.add(defName);
				}
			}
		});

		ListGridField isDelete = new ListGridField("isDelete", "DELETE");
		isDelete.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				Record record = grid.getRecord(event.getRowNum());
				String defName = record.getAttributeAsString("name");

				if (!editDefinitionList.contains(defName)) {
					editDefinitionList.add(defName);
				}
			}
		});

		ListGridField definitionIdField = new ListGridField("definitionId", "definitionId");
		definitionIdField.setCanEdit(false);

		ListGridField versionField = new ListGridField("version", "version");
		versionField.setCanEdit(false);

		grid.setFields(nameField, displayNameField, isInsert, isLoad, isQuery, isUpdate, isDelete, definitionIdField, versionField);
		grid.showFields(nameField, displayNameField, isInsert, isLoad, isQuery, isUpdate, isDelete);
		grid.hideFields(definitionIdField, versionField);
		grid.fetchData();
		grid.setCanEdit(true);
		grid.setEditEvent(ListGridEditEvent.CLICK);
		grid.setEditByCell(true);
	}

	public List<DefinitionEntry> getEditGridRecord() {

		Record[] records = grid.getRecords();
		List<DefinitionEntry> entryList = new ArrayList<DefinitionEntry>();

		for (Record record : records) {

			String defName = record.getAttributeAsString("name");
			if (editDefinitionList.contains(defName)) {
				DefinitionEntry entry = new DefinitionEntry();
				EntityWebApiDefinition definition = new EntityWebApiDefinition();

				definition.setName(defName);
				definition.setInsert(record.getAttributeAsBoolean("isInsert"));
				definition.setLoad(record.getAttributeAsBoolean("isLoad"));
				definition.setQuery(record.getAttributeAsBoolean("isQuery"));
				definition.setUpdate(record.getAttributeAsBoolean("isUpdate"));
				definition.setDelete(record.getAttributeAsBoolean("isDelete"));

				entry.setDefinition(definition);

				DefinitionInfo info = new DefinitionInfo();
				info.setVersion(record.getAttributeAsInt("version"));

				entry.setDefinitionInfo(info);

				entryList.add(entry);
			}
		}

		return entryList;
	}

	public Record getHistoryTargetRecord() {
		return grid.getSelectedRecord();
	}
}
