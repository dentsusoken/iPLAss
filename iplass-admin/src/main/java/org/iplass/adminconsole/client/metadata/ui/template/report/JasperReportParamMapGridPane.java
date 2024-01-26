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

package org.iplass.adminconsole.client.metadata.ui.template.report;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.template.report.definition.JasperReportType;
import org.iplass.mtp.web.template.report.definition.ReportParamMapDefinition;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class JasperReportParamMapGridPane extends VLayout {

	private enum FIELD_NAME {
		NAME,
		MAP_FROM,
		VALUE_OBJECT,
		PARAM_TYPE,
		PARAM_TYPE_DISPLAY_NAME,
	}

	private ParamMapGrid grid;

	public JasperReportParamMapGridPane() {
		setMargin(5);
		setAutoHeight();

		HLayout captionComposit = new HLayout(5);
		captionComposit.setHeight(25);

		Label caption = new Label("Parameters:");
		caption.setHeight(21);
		caption.setWrap(false);
		captionComposit.addMember(caption);

		Label captionHint = new Label();
		SmartGWTUtil.addHintToLabel(captionHint,
				"<style type=\"text/css\"><!--"
				+ "ul.notes{margin-top:5px;padding-left:15px;list-style-type:disc;}"
				+ "ul.notes li{padding:5px 0px;}"
				+ "ul.notes li span.strong {text-decoration:underline;color:red}"
				+ "ul.subnotes {margin-top:5px;padding-left:10px;list-style-type:circle;}"
				+ "--></style>"
				+ "<h3>Notes</h3>"
				+ "<ul class=\"notes\">"
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_ReportParamMapGridPane_captionHintComment1")
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_ReportParamMapGridPane_captionHintComment2")
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_ReportParamMapGridPane_captionHintComment3"));
		captionComposit.addMember(captionHint);

		grid = new ParamMapGrid();
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				editMap((ListGridRecord)event.getRecord());
			}
		});

		IButton addMap = new IButton("Add");
		addMap.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addMap();
			}
		});

		IButton delMap = new IButton("Remove");
		delMap.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteMap();
			}
		});

		HLayout mapButtonPane = new HLayout(5);
		mapButtonPane.setMargin(5);
		mapButtonPane.addMember(addMap);
		mapButtonPane.addMember(delMap);

		addMember(captionComposit);
		addMember(grid);
		addMember(mapButtonPane);
	}

	public void setParamMap(ReportParamMapDefinition[] paramMap) {
		if (paramMap != null) {
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			for (ReportParamMapDefinition param : paramMap) {
				records.add(createRecord(param, null));
			}
			grid.setData(records.toArray(new ListGridRecord[]{}));
		}
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		//チェック対象なし
		return true;
	}

	/**
	 * 編集されたパラメータマップ情報を返します。
	 *
	 * @return 編集後のパラメータマップ更新ReportTemplateDefinition情報
	 */
	public JasperReportType getEditDefinition(JasperReportType definition) {

		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return definition;
		}

		ReportParamMapDefinition[] maps = new ReportParamMapDefinition[records.length];
		int i = 0;
		for (ListGridRecord record : records) {
			ReportParamMapDefinition paramMap = (ReportParamMapDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
			maps[i] = paramMap;
			i++;
		}
		definition.setParamMap(maps);
		return definition;
	}

	public ReportParamMapDefinition[] getParamMap(){

		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return null;
		}

		ReportParamMapDefinition[] maps = new ReportParamMapDefinition[records.length];
		int i = 0;
		for (ListGridRecord record : records) {
			ReportParamMapDefinition paramMap = (ReportParamMapDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
			maps[i] = paramMap;
			i++;
		}
		return maps;
	}

	private ListGridRecord createRecord(ReportParamMapDefinition param, ListGridRecord record) {
		if (record == null) {
			record = new ListGridRecord();
		}
		record.setAttribute(FIELD_NAME.MAP_FROM.name(), param.getMapFrom());
		record.setAttribute(FIELD_NAME.NAME.name(), param.getName());
		record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), param);
		record.setAttribute(FIELD_NAME.PARAM_TYPE.name(), param.getParamType());
		if ("string".equals(param.getParamType())) {
			record.setAttribute(FIELD_NAME.PARAM_TYPE_DISPLAY_NAME.name(), "Parameter");
		} else if ("report".equals(param.getParamType())) {
			record.setAttribute(FIELD_NAME.PARAM_TYPE_DISPLAY_NAME.name(), "SubReport");
		} else if ("list".equals(param.getParamType())) {
			//TODO 削除予定
			record.setAttribute(FIELD_NAME.PARAM_TYPE_DISPLAY_NAME.name(), "List(Deprecated)");
		}
		return record;
	}

	private void addMap() {
		editMap(null);
	}

	private void editMap(final ListGridRecord record) {
		final JasperReportParamMapEditDialog dialog = new JasperReportParamMapEditDialog();
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				ReportParamMapDefinition param = event.getValueObject(ReportParamMapDefinition.class);
				ListGridRecord newRecord = createRecord(param, record);
				if (record != null) {
					grid.updateData(newRecord);
				} else {
					//追加
					grid.addData(newRecord);
				}
				grid.refreshFields();
			}
		});

		if (record != null) {
			dialog.setParamMap((ReportParamMapDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
		}
		dialog.show();
	}

	private void deleteMap() {
		grid.removeSelectedData();
	}

	public void deleteAll() {
		ListGridRecord[] records = grid.getRecords();
		if (!(records == null || records.length == 0)) {
			for(ListGridRecord record : records){
				grid.removeData(record);
			}
		}
	}

	private class ParamMapGrid extends ListGrid {

		public ParamMapGrid() {
			setWidth100();
			setHeight(1);

			setShowAllColumns(true);							//列を全て表示
			setShowAllRecords(true);							//レコードを全て表示
			setCanResizeFields(true);							//列幅変更可能
			setCanSort(false);									//ソート不可
			setCanPickFields(false);							//表示フィールドの選択不可
			setCanGroupBy(false);								//GroupByの選択不可
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			setLeaveScrollbarGap(false);						//縦スクロールバー自動表示制御
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			ListGridField typeField = new ListGridField(FIELD_NAME.PARAM_TYPE_DISPLAY_NAME.name(), "Type");
			typeField.setWidth(150);
			ListGridField nameField = new ListGridField(FIELD_NAME.NAME.name(), "Name");
			ListGridField mapFromField = new ListGridField(FIELD_NAME.MAP_FROM.name(), "Value");

			setFields(typeField, nameField, mapFromField);
		}
	}

}
