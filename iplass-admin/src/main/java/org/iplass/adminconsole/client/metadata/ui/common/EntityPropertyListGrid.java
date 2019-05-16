/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.common;

import org.iplass.adminconsole.client.metadata.data.entity.PropertyDS;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class EntityPropertyListGrid extends ListGrid implements EntityPropertyGrid {

	public EntityPropertyListGrid() {

		setShowHeader(false);
		setDragDataAction(DragDataAction.NONE);
		setCanDragRecordsOut(true);
		setEmptyMessage("no proprety");

		ListGridField displayName = new ListGridField("displayName");
		setFields(displayName);
//		setAutoFitData(Autofit.VERTICAL);
	}

	@Override
	public void refresh(String defName) {

		PropertyDS dataSource = PropertyDS.getInstance(defName);
		dataSource.fetchData(null, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				RecordList list = response.getDataAsRecordList();
				if (list != null) {
					setData(list.toArray());
				}
			}
		});
	}

}
