package org.iplass.adminconsole.client.metadata.ui.template.report;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.template.report.definition.JxlsContextParamMapDefinition;

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
	
	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		//チェック対象なし
		return true;
	}
	
	public JxlsContextParamMapDefinition[] getContextParamMap() {
		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return null;
		}
		
		JxlsContextParamMapDefinition[] maps = new JxlsContextParamMapDefinition[records.length];
		int i = 0;
		for (ListGridRecord record : records) {
			JxlsContextParamMapDefinition jcpmd = (JxlsContextParamMapDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
			maps[i] = jcpmd;
			i++;
		}
		return maps;
	}

	public void setContextParamMap(JxlsContextParamMapDefinition[] contextParamMap) {
		if (contextParamMap != null) {
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			for (JxlsContextParamMapDefinition jcpmd : contextParamMap) {
				records.add(createRecord(jcpmd, null));
			}
			grid.setData(records.toArray(new ListGridRecord[]{}));
		}
	}
	
	private ListGridRecord createRecord(JxlsContextParamMapDefinition jcpmd, ListGridRecord record) {
		if (record == null) {
			record = new ListGridRecord();
		}
		record.setAttribute(FIELD_NAME.KEY.name(), jcpmd.getKey());
		record.setAttribute(FIELD_NAME.MAP_FROM.name(), jcpmd.getMapFrom());
		record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), jcpmd);

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
				JxlsContextParamMapDefinition jcpmd = event.getValueObject(JxlsContextParamMapDefinition.class);
				ListGridRecord newRecord = createRecord(jcpmd, record);
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
			dialog.setContextParamMap((JxlsContextParamMapDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
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
			ListGridField mapFromField = new ListGridField(FIELD_NAME.MAP_FROM.name(), "Value");

			setFields(keyField, mapFromField);
		}
	}

}
