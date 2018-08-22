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

package org.iplass.adminconsole.client.metadata.data.entity;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.top.parts.FulltextSearchViewParts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CrawlEntityDS extends AbstractAdminDataSource {

	FulltextSearchViewParts parts;
	boolean isInitDrop;

	/**
	 * DSインスタンスを返します。
	 *
	 * @return DSインスタンス
	 */
	public static CrawlEntityDS getInstance(FulltextSearchViewParts parts, boolean isInitDrop) {
		return new CrawlEntityDS(parts, isInitDrop);
	}

	private CrawlEntityDS(FulltextSearchViewParts parts, boolean isInitDrop) {

		this.parts = parts;
		this.isInitDrop = isInitDrop;
		DataSourceField nameField = new DataSourceTextField("name", AdminClientMessageUtil.getString("datasource_tools_fulltextsearch_CrawlEntityInfoDS_name"));
		nameField.setPrimaryKey(true);
		DataSourceField displayNameField = new DataSourceTextField("displayName", AdminClientMessageUtil.getString("datasource_tools_fulltextsearch_CrawlEntityInfoDS_dispName"));
		DataSourceField entityView = new DataSourceTextField("entityView", "entityView");
		DataSourceBooleanField isDispEntity = new DataSourceBooleanField("isDispEntity", "DisplayEntity");

		setFields(nameField, displayNameField, entityView, isDispEntity);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		MetaDataServiceAsync service = MetaDataServiceFactory.get();
		service.getCrawlTargetEntityList(TenantInfoHolder.getId(), new AsyncCallback<List<EntityDefinition>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				SC.warn(AdminClientMessageUtil.getString("datasource_tools_fulltextsearch_CrawlEntityInfoDS_failedToGetEntityList") + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(List<EntityDefinition> result) {
				List<ListGridRecord> records = createRecord(result);
				response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);
			}
		});

	}

	@Override
	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
		processResponse(requestId, response);
	}

	private List<ListGridRecord> createRecord(List<EntityDefinition> entities) {

		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (entities != null) {
			for (EntityDefinition entity : entities) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("name", entity.getName());
				record.setAttribute("displayName", entity.getDisplayName());

				if (parts.getViewNames() != null && parts.getViewNames().get(entity.getName()) != null) {

					String viewName = parts.getViewNames().get(entity.getName());
					if (viewName.equals("")) {
						viewName = "(default)";
					}

					record.setAttribute("entityView", viewName);
				}

				if (parts.getDispEntities() != null && parts.getDispEntities().get(entity.getName()) != null) {
					record.setAttribute("isDispEntity", parts.getDispEntities().get(entity.getName()));
				}

				if (isInitDrop) {
					record.setAttribute("isDispEntity", true);
				}

				list.add(record);
			}
		}
		return list;
	}

}
