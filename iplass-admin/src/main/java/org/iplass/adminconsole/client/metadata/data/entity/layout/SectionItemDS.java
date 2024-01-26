/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.data.entity.layout;

import java.util.HashMap;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.SectionController;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SectionItemDS extends AbstractAdminDataSource {

	private static HashMap<String, SectionItemDS> dsList = null;

	public static SectionItemDS getInstance() {
		return getInstance(ViewType.DETAIL);
	}

	public static SectionItemDS getInstance(ViewType viewType) {
		if (dsList == null) {
			dsList = new HashMap<String, SectionItemDS>();
		}

		if (viewType == null) throw new IllegalArgumentException("viewType is null.");

		if (dsList.containsKey(viewType.name())) {
			return dsList.get(viewType.name());
		}

		SectionItemDS ds = new SectionItemDS("SectionListDS_" + viewType.name(), viewType);
		dsList.put(viewType.name(), ds);
		return ds;
	}

	private ViewType viewType = null;
	private SectionController sectionController = GWT.create(SectionController.class);

	private SectionItemDS(String id, ViewType viewType) {
		setID(id);

		this.viewType = viewType;

		DataSourceField nameField = new DataSourceField("name", FieldType.TEXT, "name");
		DataSourceField displayNameField = new DataSourceField("displayName", FieldType.TEXT, "displayName");
		setFields(nameField, displayNameField);
	}

	@Override
	protected void executeFetch(String requestId, DSRequest request, DSResponse response) {

		ListGridRecord[] list = sectionController.sectionItemList(viewType).toArray(new ListGridRecord[0]);
		response.setData(list);
		response.setTotalRows(list.length);
		processResponse(requestId, response);
	}

}
