/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.webapi;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

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

public class ResultPane extends VLayout {

	private String RESULT = "result";

	private ResultGrid grid;

	public ResultPane() {
		setMargin(5);
		setAutoHeight();

		Label caption = new Label("Results");
		caption.setHeight(21);

		grid = new ResultGrid();
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

		addMember(caption);
		addMember(grid);
		addMember(mapButtonPane);
	}

	public void setResults(String[] results) {
		grid.setData(new ListGridRecord[]{});
		if (results != null) {
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			for (String result : results) {
				records.add(createRecord(result, null));
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
	 * 編集されたWebAPIDefinition情報を返します。
	 *
	 * @return 編集WebAPIDefinition情報
	 */
	public WebApiDefinition getEditDefinition(WebApiDefinition definition) {

		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return definition;
		}

		String[] results = new String[records.length];
		int i = 0;
		for (ListGridRecord record : records) {
			results[i] = record.getAttribute(RESULT);
			i++;
		}
		definition.setResults(results);
		return definition;
	}


	private ListGridRecord createRecord(String result, ListGridRecord record) {
		if (record == null) {
			record = new ListGridRecord();
		}
		record.setAttribute(RESULT, result);
		return record;
	}

	private void addMap() {
		editMap(null);
	}

	private void editMap(final ListGridRecord record) {
		final ResultEditDialog dialog = new ResultEditDialog();
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				String result = event.getValueObject(String.class);
				ListGridRecord newRecord = createRecord(result, record);
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
			dialog.setResult((String)record.getAttributeAsObject(RESULT));
		}
		dialog.show();
	}

	private void deleteMap() {
		grid.removeSelectedData();
	}

	private class ResultGrid extends ListGrid {

		public ResultGrid() {
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

			ListGridField nameField = new ListGridField(RESULT, "Attribute Name");

			setFields(nameField);
		}
	}
}
