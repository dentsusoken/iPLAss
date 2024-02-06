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

package org.iplass.adminconsole.client.tools.data.metaexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceFactory;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class MetaDataTagDS extends AbstractAdminDataSource {

	public enum FIELD_NAME {
		OID,
		NAME,
		DESCRIPTION,
		CREATEDATE,
		CREATEDATE_DISP
	}

	public static MetaDataTagDS getInstance() {
		return new MetaDataTagDS();
	}

	private MetaDataTagDS() {

		DataSourceField nameField = new DataSourceTextField(FIELD_NAME.OID.name(), AdminClientMessageUtil.getString("datasource_tools_metaexplorer_MetaDataTagDS_tagName"));
		DataSourceField createDateField = new DataSourceDateTimeField(FIELD_NAME.CREATEDATE.name(), AdminClientMessageUtil.getString("datasource_tools_metaexplorer_MetaDataTagDS_createDate"));
		DataSourceField createDateDispField = new DataSourceTextField(FIELD_NAME.CREATEDATE_DISP.name(), AdminClientMessageUtil.getString("datasource_tools_metaexplorer_MetaDataTagDS_createDate"));
		DataSourceField descriptionField = new DataSourceTextField(FIELD_NAME.DESCRIPTION.name(), AdminClientMessageUtil.getString("datasource_tools_metaexplorer_MetaDataTagDS_comment"));

		setFields(nameField, createDateField, createDateDispField, descriptionField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		MetaDataExplorerServiceAsync service = MetaDataExplorerServiceFactory.get();
		service.getTagList(TenantInfoHolder.getId(), new AsyncCallback<List<Entity>>() {

			@Override
			public void onSuccess(List<Entity> entities) {
				List<ListGridRecord> records = createRecord(entities);
				response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				SC.warn(AdminClientMessageUtil.getString("datasource_tools_metaexplorer_MetaDataTagDS_failedToGetTag") + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});

	}

	private List<ListGridRecord> createRecord(List<Entity> entities) {

		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (entities != null) {
			for (Entity entity : entities) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(FIELD_NAME.OID.name(), entity.getOid());
				record.setAttribute(FIELD_NAME.NAME.name(), entity.getName());
				record.setAttribute(FIELD_NAME.DESCRIPTION.name(), entity.getDescription());
				record.setAttribute(FIELD_NAME.CREATEDATE.name(), entity.getCreateDate());
				record.setAttribute(FIELD_NAME.CREATEDATE_DISP.name(), SmartGWTUtil.formatTimestamp(entity.getCreateDate()));
				list.add(record);
			}
		}
		return list;
	}

}
