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

package org.iplass.adminconsole.client.metadata.data.template;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ReportTemplateDS extends AbstractAdminDataSource {
	private static ReportTemplateDS instance = null;

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public static ReportTemplateDS getInstance() {
		if (instance == null) {
			instance = new ReportTemplateDS("MetaReportTemplateListDS");
		}
		return instance;
	}

	public ReportTemplateDS(String id) {
		setID(id);
		DataSourceField nameField = new DataSourceField("name", FieldType.TEXT, "name");
		DataSourceField displayNameField = new DataSourceField("displayName", FieldType.TEXT, "displayName");
		setFields(nameField, displayNameField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		service.getReportTemplateDefinitionNameList(TenantInfoHolder.getId(), new AsyncCallback<List<Name>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(List<Name> result) {
				GWT.log(result.toString());

				// nameでソート
				Collections.sort(result, new Comparator<Name>() {
					@Override
					public int compare(Name o1, Name o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

				ListGridRecord[] list = new ListGridRecord[result.size()];
				int i = 0;
				for (Name name : result) {
					GWT.log("result:" + name.getName());
					ListGridRecord record = new ListGridRecord();
					list[i] = record;
					copyValues(name, record);
					i++;
				}
				response.setData(list);
				response.setTotalRows(result.size());
				processResponse(requestId, response);
			}

		});

	}

	private static void copyValues(Name from, Record to) {
		to.setAttribute("name", from.getName());
		if (from.getDisplayName() != null) {
			to.setAttribute("displayName", from.getDisplayName());
		} else {
			to.setAttribute("displayName", "(" + from.getName() + ")");
		}
	}

}
