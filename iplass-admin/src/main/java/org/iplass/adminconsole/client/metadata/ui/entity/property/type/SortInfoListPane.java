/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.type.SortInfoEditDialog.SortInfoEditDialogHandler;
import org.iplass.adminconsole.shared.metadata.dto.entity.SortInfo;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SortInfoListPane extends VLayout {

	private SortInfoGrid gridSort;

	private IButton btnAdd;
	private IButton btnDel;

	private boolean readOnly;

	private SortInfoListPaneHandler handler;

	public interface SortInfoListPaneHandler {
		String referenceName();
	}

	public SortInfoListPane(SortInfoListPaneHandler handler) {
		this.handler = handler;

		setWidth100();
		setAutoHeight();

		gridSort = new SortInfoGrid();
		gridSort.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				startSortInfoEdit(false, (SortInfoListGridRecord)event.getRecord());
			}
		});

		btnAdd = new IButton("Add");
		btnAdd.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startSortInfoEdit(true, new SortInfoListGridRecord());
			}
		});
		btnDel = new IButton("Remove");
		btnDel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				gridSort.removeSelectedData();
			}
		});

		HLayout pnlButtons = new HLayout(5);
		pnlButtons.setWidth100();
		pnlButtons.setHeight(30);
		pnlButtons.setAlign(Alignment.LEFT);
		pnlButtons.setMargin(5);
		pnlButtons.addMember(btnAdd);
		pnlButtons.addMember(btnDel);

		addMember(SmartGWTUtil.titleLabel("Order By"));
		addMember(gridSort);
		addMember(pnlButtons);

	}

	public void applyFrom(PropertyListGridRecord record, ReferenceAttribute referenceAttribute) {

		if (record.isInherited()) {
			readOnly = true;
			btnAdd.setDisabled(true);
			btnDel.setDisabled(true);
		}

		if (SmartGWTUtil.isNotEmpty(referenceAttribute.getSortList())) {
			List<SortInfo> sortList = referenceAttribute.getSortList();
			int size = sortList.size();
			SortInfoListGridRecord[] sortRecords = new SortInfoListGridRecord[size];

			for (int i = 0; i < size; i++) {
				SortInfo sort = sortList.get(i);
				SortInfoListGridRecord sortRecord = new SortInfoListGridRecord(sort.getPropertyName(), sort.getSortType());
				sortRecords[i] = sortRecord;
			}

			gridSort.setData(sortRecords);
		}

	}

	public void applyTo(ReferenceAttribute referenceAttribute) {

		List<SortInfo> sortList = new ArrayList<>();
		for (ListGridRecord sortRecord : gridSort.getRecords()) {
			SortInfoListGridRecord sRecord = (SortInfoListGridRecord) sortRecord;
			SortInfo sort = new SortInfo();
			sort.setPropertyName(sRecord.getPropertyName());
			sort.setSortType(sRecord.getSortType());
			sortList.add(sort);
		}
		referenceAttribute.setSortList(sortList);
	}

	public boolean validate() {
		return true;
	}

	private void startSortInfoEdit(final boolean isNewRow, SortInfoListGridRecord target) {
		String referenceName = handler.referenceName();
		if (SmartGWTUtil.isEmpty(referenceName)) {
			return;
		}
		SortInfoEditDialog dialog = new SortInfoEditDialog(target, referenceName, readOnly, new SortInfoEditDialogHandler() {

			@Override
			public void onSaved(SortInfoListGridRecord record) {

				if (isNewRow) {
					gridSort.addData(record);
				}
				gridSort.updateData(record);
				gridSort.refreshFields();
			}

			@Override
			public ListGridRecord[] listGridRecords() {
				return gridSort.getRecords();
			}
		});
		dialog.show();
	}

	private static class SortInfoGrid extends ListGrid {

		public SortInfoGrid() {

			setMargin(5);
			setHeight(1);
			setWidth100();

			setShowAllColumns(true);
			setShowAllRecords(true);
			setCanResizeFields(true);

			setCanGroupBy(false);
			setCanFreezeFields(false);
			setCanPickFields(false);
			setCanSort(false);
			setCanAutoFitFields(false);

			//grid内でのD&Dでの並べ替えを許可
			setCanDragRecordsOut(true);
			setCanAcceptDroppedRecords(true);
			setCanReorderRecords(true);

			setOverflow(Overflow.VISIBLE);
			setBodyOverflow(Overflow.VISIBLE);
			setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される

			ListGridField sortNameField = new ListGridField("propertyName", AdminClientMessageUtil.getString("ui_metadata_ui_entity_property_type_SortInfoListPane_propertyName"));
			ListGridField sortTypeField = new ListGridField("sortType", AdminClientMessageUtil.getString("ui_metadata_ui_entity_property_type_SortInfoListPane_order"));
			setFields(sortNameField, sortTypeField);

		}
	}

}
