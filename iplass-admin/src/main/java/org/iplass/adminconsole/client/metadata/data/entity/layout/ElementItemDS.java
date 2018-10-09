/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.mtp.view.generic.element.BlankSpace;
import org.iplass.mtp.view.generic.element.Button;
import org.iplass.mtp.view.generic.element.Link;
import org.iplass.mtp.view.generic.element.ScriptingElement;
import org.iplass.mtp.view.generic.element.TemplateElement;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ElementItemDS extends AbstractAdminDataSource {

	private static HashMap<String, ElementItemDS> dsList = null;

	public static ElementItemDS getInstance(ViewType viewType) {
		if (dsList == null) {
			dsList = new HashMap<String, ElementItemDS>();
		}

		if (viewType == null) throw new IllegalArgumentException("viewType is null.");

		if (dsList.containsKey(viewType.name())) {
			return dsList.get(viewType.name());
		}

		ElementItemDS ds = new ElementItemDS("ElementListDS_" + viewType.name(), viewType);
		dsList.put(viewType.name(), ds);
		return ds;
	}

	private ViewType viewType = null;

	private ElementItemDS(String id, ViewType viewType) {
		setID(id);

		this.viewType = viewType;

		DataSourceField nameField = new DataSourceField("name", FieldType.TEXT, "name");
		DataSourceField displayNameField = new DataSourceField("displayName", FieldType.TEXT, "displayName");
		setFields(nameField, displayNameField);
	}

	@Override
	protected void executeFetch(String requestId, DSRequest request,
			DSResponse response) {

		List<ListGridRecord> records = new ArrayList<ListGridRecord>();
		if (viewType == ViewType.DETAIL) {
			ListGridRecord button = new ListGridRecord();
			button.setAttribute("name", Button.class.getName());
			button.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_entity_layout_ElementItemDS_btn"));
			records.add(button);

			ListGridRecord script = new ListGridRecord();
			script.setAttribute("name", ScriptingElement.class.getName());
			script.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_entity_layout_ElementItemDS_script"));
			records.add(script);

			ListGridRecord template = new ListGridRecord();
			template.setAttribute("name", TemplateElement.class.getName());
			template.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_entity_layout_ElementItemDS_template"));
			records.add(template);

			ListGridRecord link = new ListGridRecord();
			link.setAttribute("name", Link.class.getName());
			link.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_entity_layout_ElementItemDS_link"));
			records.add(link);

			ListGridRecord space = new ListGridRecord();
			space.setAttribute("name", BlankSpace.class.getName());
			space.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_entity_layout_ElementItemDS_space"));
			records.add(space);

			ListGridRecord virtualProperty = new ListGridRecord();
			virtualProperty.setAttribute("name", VirtualPropertyItem.class.getName());
			virtualProperty.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_entity_layout_ElementItemDS_virtualProperty"));
			records.add(virtualProperty);
		} else if (viewType == ViewType.SEARCH) {
			ListGridRecord space = new ListGridRecord();
			space.setAttribute("name", BlankSpace.class.getName());
			space.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_entity_layout_ElementItemDS_space"));
			records.add(space);

			ListGridRecord virtualProperty = new ListGridRecord();
			virtualProperty.setAttribute("name", VirtualPropertyItem.class.getName());
			virtualProperty.setAttribute("displayName", AdminClientMessageUtil.getString("datasource_entity_layout_ElementItemDS_virtualProperty"));
			records.add(virtualProperty);
		}
		ListGridRecord[] list = records.toArray(new ListGridRecord[records.size()]);
		response.setData(list);
		response.setTotalRows(list.length);
		processResponse(requestId, response);
	}

}
