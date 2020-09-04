/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

public class JxlsReportParamMapGridPane extends VLayout {

	private enum FIELD_NAME {
		KEY,
		MAP_FROM,
		TO_MAP,
		VALUE_OBJECT
	}
	
	private ParamMapGrid grid;
	
	public JxlsReportParamMapGridPane() {
		setMargin(5);
		setAutoHeight();

		HLayout captionComposit = new HLayout(5);
		captionComposit.setHeight(25);

		Label caption = new Label("Context Parameter Mappings:");
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
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_JxlsReportParamMapGridPane_captionHintComment1")
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_JxlsReportParamMapGridPane_captionHintComment2")
				+ AdminClientMessageUtil.getString("ui_metadata_template_report_JxlsReportParamMapGridPane_captionHintComment3"));
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
	
	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		//チェック対象なし
		return true;
	}
	
	public ReportParamMapDefinition[] getParamMap() {
		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return null;
		}
		
		ReportParamMapDefinition[] maps = new ReportParamMapDefinition[records.length];
		int i = 0;
		for (ListGridRecord record : records) {
			ReportParamMapDefinition rpmd = (ReportParamMapDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
			maps[i] = rpmd;
			i++;
		}
		return maps;
	}

	public void setParamMap(ReportParamMapDefinition[] paramMap) {
		if (paramMap != null) {
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			for (ReportParamMapDefinition rpmd : paramMap) {
				records.add(createRecord(rpmd, null));
			}
			grid.setData(records.toArray(new ListGridRecord[]{}));
		}
	}
	
	private ListGridRecord createRecord(ReportParamMapDefinition rpmd, ListGridRecord record) {
		if (record == null) {
			record = new ListGridRecord();
		}
		record.setAttribute(FIELD_NAME.KEY.name(), rpmd.getName());
		record.setAttribute(FIELD_NAME.MAP_FROM.name(), rpmd.getMapFrom());
		record.setAttribute(FIELD_NAME.TO_MAP.name(), rpmd.isConvertEntityToMap());
		record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), rpmd);

		return record;
	}

	public void deleteAll() {
		ListGridRecord[] records = grid.getRecords();
		if (!(records == null || records.length == 0)) {
			for(ListGridRecord record : records){
				grid.removeData(record);
			}
		}
	}
	
	private void addMap() {
		editMap(null);
	}

	private void editMap(final ListGridRecord record) {
		final JxlsReportParamMapEditDialog dialog = new JxlsReportParamMapEditDialog();
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				ReportParamMapDefinition rpmd = event.getValueObject(ReportParamMapDefinition.class);
				ListGridRecord newRecord = createRecord(rpmd, record);
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

			ListGridField keyField = new ListGridField(FIELD_NAME.KEY.name(), "Key");
			keyField.setWidth("30%");
			ListGridField mapFromField = new ListGridField(FIELD_NAME.MAP_FROM.name(), "Value");
			mapFromField.setWidth("50%");
			ListGridField toMapField = new ListGridField(FIELD_NAME.TO_MAP.name(), "To Map");
			toMapField.setWidth("20%");

			setFields(keyField, mapFromField, toMapField);
		}
	}

}
