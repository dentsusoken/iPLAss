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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.defrag;


import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.GridActionImgButton;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataViewGridButton;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.entityexplorer.DefragEntityInfoDS;
import org.iplass.mtp.entity.definition.EntityDefinition;

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
 * EQLWorksheetパネル
 */
public class EntityDefragListPane extends VLayout {

	private static final String RESOURCE_PREFIX = "ui_tools_entityexplorer_EntityDefragListPane_";

	private static final String EXECUTE_ICON = "[SKIN]/actions/forward.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";
	private static final String ERROR_ICON = "[SKINIMG]/actions/exclamation.png";

//	private EntityDefragMainPane mainPane;

	private CheckboxItem showCountItem;
	private Label countLabel;
	private ListGrid grid;

	private MessageTabSet messageTabSet;

	/**
	 * コンストラクタ
	 */
	public EntityDefragListPane(EntityDefragMainPane mainPane) {
//		this.mainPane = mainPane;

		//レイアウト設定
		setWidth100();
		setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		final ToolStripButton executeButton = new ToolStripButton();
		executeButton.setIcon(EXECUTE_ICON);
		executeButton.setTitle("Execute");
		executeButton.setTooltip(SmartGWTUtil.getHoverString(
				AdminClientMessageUtil.getString(RESOURCE_PREFIX + "execTooltip")));
		executeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				execute();
			}
		});
		toolStrip.addButton(executeButton);

		toolStrip.addSeparator();

		showCountItem = new CheckboxItem();
		showCountItem.setTitle("Get Data Count");
		showCountItem.setTooltip(SmartGWTUtil.getHoverString(
				AdminClientMessageUtil.getString(RESOURCE_PREFIX + "dataNumOften")));
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
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(
				AdminClientMessageUtil.getString(RESOURCE_PREFIX + "refreshList")));
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
						button.setActionButtonPrompt(SmartGWTUtil.getHoverString(
								AdminClientMessageUtil.getString(RESOURCE_PREFIX + "showMetaDataEditScreen")));
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
				finishExecute();
			}
		});
		grid.setShowResizeBar(true);		//リサイズ可能
		grid.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、下を収縮

		messageTabSet = new MessageTabSet();
		messageTabSet.setHeight(120);

		addMember(toolStrip);
		addMember(grid);
		addMember(messageTabSet);

		refreshGrid();
	}

	public void refresh() {
		refreshGrid();
	}

	private void setRecordCount(long count) {
		countLabel.setContents("Total Count：" + count);
	}

	public void startCallback() {
		startExecute();
	}

	public void finishCallback() {
		finishExecute();
	}

	public void executeStatusCallback(List<String> messages) {
		messageTabSet.addMessage(messages);
	}

	public void executeErrorCallback(List<String> messages) {
		messageTabSet.addErrorMessage(messages);
	}

	private void execute() {
		ListGridRecord[] records = grid.getSelectedRecords();
		if (records == null || records.length == 0) {
			SC.say(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "selectEntityTarget"));
			return;
		}

		final List<String> defNames = new ArrayList<String>();
		for (ListGridRecord record : records) {
			defNames.add(record.getAttributeAsString("name"));
		}

		SC.ask(AdminClientMessageUtil.getString(RESOURCE_PREFIX + "confirmTitle"),
				AdminClientMessageUtil.getString(RESOURCE_PREFIX + "execConfirm"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {

				if (value) {
					EntityDefragProgressDialog dialog = new EntityDefragProgressDialog(EntityDefragListPane.this, defNames);
					dialog.show();
				}
			}
		});

//		EntityConfigDownloadDialog dialog = new EntityConfigDownloadDialog(defNames);
//		dialog.show();

	}

	private void refreshGrid() {
		startExecute();

		boolean isGetDataCount = showCountItem.getValueAsBoolean();
		DefragEntityInfoDS ds = DefragEntityInfoDS.getInstance(isGetDataCount);
		grid.setDataSource(ds);

		//（参考）setFieldsは、setDataSource後に指定しないと効かない

		//ボタンを表示したいためListGridFieldを指定
		ListGridField explorerField = new ListGridField("explorerButton", " ");
		explorerField.setWidth(25);
		ListGridField errorField = new ListGridField("error", " ");
		errorField.setWidth(25);
		ListGridField nameField = new ListGridField("name", "Name");
		ListGridField displayNameField = new ListGridField("displayName", "Display Name");
		ListGridField countField = new ListGridField("count", "Data Count");
		countField.setWidth(70);
		ListGridField updateDateField = new ListGridField("updateDate", "Update Date");
		updateDateField.setWidth(130);
		ListGridField curVersionField = new ListGridField("currentVersion", "Cur Version");
		curVersionField.setWidth(70);

		grid.setFields(explorerField, errorField, nameField, displayNameField, countField, updateDateField, curVersionField);

		grid.fetchData();
	}

	private void startExecute() {
		if (messageTabSet != null) {
			messageTabSet.clearMessage();
			messageTabSet.setTabTitleProgress();
		}
	}

	private void finishExecute() {
		if (messageTabSet != null) {
			messageTabSet.setTabTitleNormal();
		}
	}

}
