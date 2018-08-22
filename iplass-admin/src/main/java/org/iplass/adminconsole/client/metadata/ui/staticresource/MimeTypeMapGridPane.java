/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.staticresource;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceInfo;
import org.iplass.mtp.web.staticresource.definition.MimeTypeMappingDefinition;

import com.smartgwt.client.data.Record;
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

public class MimeTypeMapGridPane extends VLayout {

	private enum FIELD_NAME {
		EXTENSION,
		MIME_TYPE,
		VALUE_OBJECT;
	}

	private MimeTypeMapGrid grid;

	public MimeTypeMapGridPane() {
		setMargin(5);
		setAutoHeight();

		HLayout captionComposit = new HLayout(5);
		captionComposit.setHeight(25);

		Label caption = new Label("MIME Type Mappings:");
		caption.setHeight(21);
		caption.setWrap(false);
		captionComposit.addMember(caption);

		grid = new MimeTypeMapGrid();
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				editMap((ListGridRecord) event.getRecord());
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

	public void setMimeTypeMap(MimeTypeMappingDefinition[] mimeTypeMaps) {
		grid.setData(new ListGridRecord[]{});
		if (mimeTypeMaps != null) {
			List<ListGridRecord> records = new ArrayList<ListGridRecord>();
			for (MimeTypeMappingDefinition param : mimeTypeMaps) {
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
	 * 編集されたStaticResourceInfo情報を返します。
	 *
	 * @return 編集StaticResourceInfo情報
	 */
	public StaticResourceInfo getEditDefinition(StaticResourceInfo definition) {

		ListGridRecord[] records = grid.getRecords();
		if (records == null || records.length == 0) {
			return definition;
		}

		MimeTypeMappingDefinition[] maps = new MimeTypeMappingDefinition[records.length];
		int i = 0;
		for (ListGridRecord record : records) {
			MimeTypeMappingDefinition mimeTypeMap = (MimeTypeMappingDefinition) record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
			maps[i] = mimeTypeMap;
			i++;
		}
		definition.setMimeTypeMapping(java.util.Arrays.asList(maps));
		return definition;
	}

	public void clearRecord() {
		for (Record record : this.grid.getRecords()) {
			this.grid.removeData(record);
		}
	}

	private ListGridRecord createRecord(MimeTypeMappingDefinition param, ListGridRecord record) {
		if (record == null) {
			record = new ListGridRecord();
		}
		record.setAttribute(FIELD_NAME.EXTENSION.name(), param.getExtension());
		record.setAttribute(FIELD_NAME.MIME_TYPE.name(), param.getMimeType());
		record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), param);
		return record;
	}

	private void editMap(final ListGridRecord record) {
		final MimeTypeMapEditDialog dialog = new MimeTypeMapEditDialog();
		dialog.addDataChangeHandler(new DataChangedHandler() {
			@Override
			public void onDataChanged(DataChangedEvent event) {
				MimeTypeMappingDefinition param = event.getValueObject(MimeTypeMappingDefinition.class);
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
			dialog.setMimeTypeMap((MimeTypeMappingDefinition) record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
		}
		dialog.show();
	}

	private void addMap() {
		editMap(null);
	}

	private void deleteMap() {
		grid.removeSelectedData();
	}

	private class MimeTypeMapGrid extends ListGrid {

		public MimeTypeMapGrid() {
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

			ListGridField extensionField = new ListGridField(FIELD_NAME.EXTENSION.name(), "Extension");
			ListGridField mimeTypeField = new ListGridField(FIELD_NAME.MIME_TYPE.name(), "MIME Type");

			setFields(extensionField, mimeTypeField);
		}
	}

}
