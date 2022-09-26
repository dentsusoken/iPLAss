/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class NormalizerListGrid extends ListGrid {

	public NormalizerListGrid() {

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

		ListGridField typeField = new ListGridField(NormalizerListGridRecord.TYPE_NAME, "Type");
		typeField.setWidth(150);
		ListGridField gpField = new ListGridField(NormalizerListGridRecord.GP, " ");
		setFields(typeField, gpField);
	}

}
