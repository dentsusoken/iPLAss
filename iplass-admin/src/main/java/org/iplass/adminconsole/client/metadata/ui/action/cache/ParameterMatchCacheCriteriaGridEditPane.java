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

package org.iplass.adminconsole.client.metadata.ui.action.cache;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.ParameterMatchCacheCriteriaDefinition;

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

public class ParameterMatchCacheCriteriaGridEditPane extends CacheCriteriaTypeEditPane {

	private enum FIELD_NAME {
		PARAM_NAME,
		VALUE_OBJECT
	}

	private ParameterMatchGrid grid;

	public ParameterMatchCacheCriteriaGridEditPane() {
		setMargin(5);
		setAutoHeight();

		HLayout captionComposit = new HLayout(5);
		captionComposit.setHeight(25);

		Label caption = new Label("Matching Parameter Names:");
		caption.setWrap(false);
		caption.setHeight(21);
		captionComposit.addMember(caption);

		grid = new ParameterMatchGrid();
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				editResult((ListGridRecord)event.getRecord());
			}
		});

		IButton addResult = new IButton("Add");
		addResult.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addParamName();
			}
		});

		IButton delResult = new IButton("Remove");
		delResult.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteParamName();
			}
		});

		HLayout resultButtonPane = new HLayout(5);
		resultButtonPane.setMargin(5);
		resultButtonPane.addMember(addResult);
		resultButtonPane.addMember(delResult);

		addMember(captionComposit);
		addMember(grid);
		addMember(resultButtonPane);
	}

	@Override
	public void setDefinition(CacheCriteriaDefinition definition) {
		ParameterMatchCacheCriteriaDefinition def = (ParameterMatchCacheCriteriaDefinition)definition;
		if (def != null) {
			setParamNames(def.getMatchingParameterName());
		}
	}

	private void setParamNames(List<String> names) {
		if (names != null) {
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			for (String name : names) {
				records.add(createRecord(name, null));
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
	 * 編集されたCacheCriteriaDefinition情報を返します。
	 *
	 * @return 編集CacheCriteriaDefinition情報
	 */
	public CacheCriteriaDefinition getEditDefinition(CacheCriteriaDefinition definition) {

		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return definition;
		}

		List<String> paramNames = new ArrayList<String>();
		for (ListGridRecord record : records) {
			String name = record.getAttributeAsString(FIELD_NAME.VALUE_OBJECT.name());
			paramNames.add(name);
		}

		ParameterMatchCacheCriteriaDefinition def = (ParameterMatchCacheCriteriaDefinition)definition;
		def.setMatchingParameterName(paramNames);

		return definition;
	}

	private ListGridRecord createRecord(String name, ListGridRecord record) {
		if (record == null) {
			record = new ListGridRecord();
		}
		record.setAttribute(FIELD_NAME.PARAM_NAME.name(), name);
		record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), name);
		return record;
	}

	private void addParamName() {
		editResult(null);
	}

	private void editResult(final ListGridRecord record) {
		final ParameterMatchEditDialog dialog = new ParameterMatchEditDialog();
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				String name = event.getValueObject(String.class);
				ListGridRecord newRecord = createRecord(name, record);
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
			dialog.setParameterName(record.getAttributeAsString(FIELD_NAME.VALUE_OBJECT.name()));
		}
		dialog.show();
	}

	private void deleteParamName() {
		grid.removeSelectedData();
	}

	private class ParameterMatchGrid extends ListGrid {

		public ParameterMatchGrid() {
			setWidth("50%");
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

			ListGridField statusField = new ListGridField(FIELD_NAME.PARAM_NAME.name(), "Parameter Name");

			setFields(statusField);
		}
	}

}
