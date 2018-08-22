/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.data.entity;

import java.util.HashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.NotNullValidation;
import org.iplass.mtp.entity.definition.validations.RangeValidation;
import org.iplass.mtp.entity.definition.validations.RegexValidation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PropertyDS extends AbstractAdminDataSource {

	private static HashMap<String, PropertyDS> dsList = null;

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public static PropertyDS create(String defName) {
		//同じEntityのDSを作らない
		if (dsList == null) {
			dsList = new HashMap<String, PropertyDS>();
		}
		if (dsList.containsKey(defName)) {
			return dsList.get(defName);
		}

		PropertyDS ds = new PropertyDS(defName);
		dsList.put(defName, ds);
		return ds;
	}


	private PropertyDS(String defName) {
		setAttribute("defName", defName, false);

		DataSourceField nameField = new DataSourceField("name", FieldType.TEXT, "name");
		DataSourceField displayNameField = new DataSourceField("displayName", FieldType.TEXT, "displayName");
		DataSourceField javaTypeField = new DataSourceField("javaType", FieldType.TEXT, "javaType");
		DataSourceField mustField = new DataSourceField("must", FieldType.BOOLEAN, "must");
		DataSourceField maxField = new DataSourceField("max", FieldType.TEXT, "max");
		DataSourceField minField = new DataSourceField("min", FieldType.TEXT, "min");
		DataSourceField objRefField = new DataSourceField("objRef", FieldType.TEXT, "objRef");
		setFields(nameField, displayNameField, javaTypeField, mustField, maxField, minField, objRefField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		String defName = getAttribute("defName");

		service.getEntityDefinition(TenantInfoHolder.getId(), defName, new AsyncCallback<EntityDefinition>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(EntityDefinition result) {
				int size = result.getPropertyList().size();

				// Create list for return - it is just requested records
				ListGridRecord[] list = new ListGridRecord[size];
				if (size > 0) {
					for (int i = 0; i < size; i++) {
						PropertyDefinition pd = result.getPropertyList().get(i);

						ListGridRecord record = new ListGridRecord();
						copyValues(pd, record);
						list[i] = record;
					}
				}
				response.setData(list);

				response.setTotalRows(size);
				processResponse(requestId, response);
			}

		});

	}

	public String getDefinitionName() {
		return getAttribute("defName");
	}

	private static void copyValues(PropertyDefinition from, Record to) {
		to.setAttribute("name", from.getName());
		to.setAttribute("displayName", from.getDisplayName());
		to.setAttribute("propertyDefinition", from);
		to.setAttribute("javaType", from.getJavaType().getName());
		copyValidationValues(from.getValidations(), to);
		copyLocalizeStingValues(from.getLocalizedDisplayNameList(), to);
	}

	private static void copyValidationValues(List<ValidationDefinition> vdList, Record to) {
		for (ValidationDefinition vd : vdList) {
			if (vd instanceof NotNullValidation) {
				to.setAttribute("must", true);
			} else if (vd instanceof RangeValidation) {
				RangeValidation rVd = (RangeValidation)vd;
				to.setAttribute("max", rVd.getMax());
				to.setAttribute("min", rVd.getMin());
				to.setAttribute("rangeValidation", rVd);
			} else if (vd instanceof RegexValidation) {
				to.setAttribute("regexValidation", (RegexValidation)vd);
			} else {
				continue;
			}
		}
	}

	private static void copyLocalizeStingValues(List<LocalizedStringDefinition> lsdList, Record to) {
		to.setAttribute("localizedString", lsdList);
	}
}
