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

package org.iplass.adminconsole.client.metadata.data.entity.webapi;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class EntityWebApiDS extends AbstractAdminDataSource {

    private static EntityWebApiDS instance = null;

    public static EntityWebApiDS getInstance() {
        if (instance == null) {
            instance = new EntityWebApiDS();
        }
        return instance;
    }

	private EntityWebApiDS() {
		DataSourceField nameField = new DataSourceTextField("name", AdminClientMessageUtil.getString("datasource_entity_webapi_EntityWebApiDefinitionInfoDS_name"));
		nameField.setPrimaryKey(true);
		DataSourceField displayNameField = new DataSourceTextField("displayName", AdminClientMessageUtil.getString("datasource_entity_webapi_EntityWebApiDefinitionInfoDS_dispName"));
		DataSourceField isInsertField = new DataSourceBooleanField("isInsert", "INSERT");
		DataSourceField isLoadField = new DataSourceBooleanField("isLoad", "SELECT(Load)");
		DataSourceField isQueryField = new DataSourceBooleanField("isQuery", "SELECT(Query)");
		DataSourceField isUpdateField = new DataSourceBooleanField("isUpdate", "UPDATE");
		DataSourceField isDeleteField = new DataSourceBooleanField("isDelete", "DELETE");
		DataSourceField definitionIdField = new DataSourceTextField("definitionId", "DEFINITION ID");
		DataSourceField versionField = new DataSourceIntegerField("version", "VERSION");

		setFields(nameField, displayNameField, isInsertField, isLoadField, isQueryField, isUpdateField, isDeleteField, definitionIdField, versionField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		MetaDataServiceAsync service = MetaDataServiceFactory.get();
		service.getEntityWebApiDefinitionEntryList(TenantInfoHolder.getId(), new AsyncCallback<List<DefinitionEntry>>() {

			@Override
			public void onSuccess(List<DefinitionEntry> entities) {
				List<ListGridRecord> records = createRecord(entities);
				response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				SC.warn(AdminClientMessageUtil.getString("datasource_entity_webapi_EntityWebApiDefinitionInfoDS_failedToGetEntityWebApiDef") + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});

	}

	@Override
	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
		processResponse(requestId, response);
	}

	private List<ListGridRecord> createRecord(List<DefinitionEntry> entities) {

		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (entities != null) {
			for (DefinitionEntry entry : entities) {
				EntityWebApiDefinition entity = (EntityWebApiDefinition) entry.getDefinition();
				final ListGridRecord record = new ListGridRecord();
				record.setAttribute("name", entity.getName());
				record.setAttribute("displayName", entity.getDisplayName());
				record.setAttribute("isInsert", entity.isInsert());
				record.setAttribute("isLoad", entity.isLoad());
				record.setAttribute("isQuery", entity.isQuery());
				record.setAttribute("isUpdate", entity.isUpdate());
				record.setAttribute("isDelete", entity.isDelete());
				record.setAttribute("definitionId", entry.getDefinitionInfo().getObjDefId());
				record.setAttribute("version", entry.getDefinitionInfo().getVersion());

				list.add(record);
			}
		}
		return list;
	}

}
