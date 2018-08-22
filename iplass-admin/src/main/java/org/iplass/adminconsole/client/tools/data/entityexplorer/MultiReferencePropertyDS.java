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

package org.iplass.adminconsole.client.tools.data.entityexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class MultiReferencePropertyDS extends AbstractAdminDataSource {

	private String defName;
	private String oid;
	private Long version;
	private String propertyName;

	public static MultiReferencePropertyDS getInstance(final String defName, final String oid, final Long version, String propertyName) {
		return new MultiReferencePropertyDS(defName, oid, version, propertyName);
	}

	private MultiReferencePropertyDS(final String defName, final String oid, final Long version, String propertyName) {
		this.defName = defName;
		this.oid = oid;
		this.version = version;
		this.propertyName = propertyName;

		DataSourceField oidField = new DataSourceTextField("oid", "OID");
		DataSourceField nameField = new DataSourceTextField("name", "Name");
		DataSourceField versionField = new DataSourceTextField("version", "Version");

		setFields(oidField, nameField, versionField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();
		service.searchReferenceEntity(TenantInfoHolder.getId(), defName, propertyName, oid, version, new AsyncCallback<List<Entity>>() {

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

				SC.warn(AdminClientMessageUtil.getString("datasource_tools_entityexplorer_MultiReferencePropertyDS_failedToGetData", defName, oid) + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});

	}

	private List<ListGridRecord> createRecord(List<Entity> entities) {

		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (entities != null) {
			for (Entity entity : entities) {
				Entity reference = entity.getValueAs(Entity.class, propertyName);
				if (reference != null) {
					//該当がある場合のみ追加
					ListGridRecord record = new ListGridRecord();
					if (reference.getOid() != null) {
						record.setAttribute("oid", SafeHtmlUtils.htmlEscape(reference.getOid()));
					} else {
						record.setAttribute("oid", (String)null);
					}
					if (reference.getName() != null) {
						record.setAttribute("name", SafeHtmlUtils.htmlEscape(reference.getName()));
					} else {
						record.setAttribute("name", (String)null);
					}
					if (reference.getVersion() != null) {
						record.setAttribute("version", SafeHtmlUtils.htmlEscape(reference.getVersion().toString()));
					} else {
						record.setAttribute("version", (String)null);
					}
					list.add(record);
				}
			}
		}
		return list;
	}
}
