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

package org.iplass.adminconsole.client.tools.data.entityexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.CrawlEntityInfo;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CrawlEntityInfoDS extends AbstractAdminDataSource {

	/**
	 * DSインスタンスを返します。
	 *
	 * @param isGetDataCount データ件数取得有無
	 * @return DSインスタンス
	 */
	public static CrawlEntityInfoDS getInstance(boolean isGetDataCount) {
		return new CrawlEntityInfoDS(isGetDataCount);
	}

	/** 件数取得制御フラグ */
	private boolean isGetDataCount = false;

	private CrawlEntityInfoDS(boolean isGetDataCount) {
		this.isGetDataCount = isGetDataCount;

		DataSourceField nameField = new DataSourceTextField("name", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_CrawlEntityInfoDS_name"));
		DataSourceField displayNameField = new DataSourceTextField("displayName", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_CrawlEntityInfoDS_dispName"));
		DataSourceField countField = new DataSourceTextField("count", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_CrawlEntityInfoDS_number"));
		DataSourceField updateDateField = new DataSourceTextField("updateDate", "Update Date");
		DataSourceField lastCrawlDateField = new DataSourceTextField("lastCrawlDate", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_CrawlEntityInfoDS_lastCrawlDate"));

		setFields(nameField, displayNameField, countField, updateDateField, lastCrawlDateField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();
		service.getCrawlEntityList(TenantInfoHolder.getId(), isGetDataCount, new AsyncCallback<List<CrawlEntityInfo>>() {

			@Override
			public void onSuccess(List<CrawlEntityInfo> entities) {
				List<ListGridRecord> records = createRecord(entities);
				response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				SC.warn(AdminClientMessageUtil.getString("datasource_tools_entityexplorer_CrawlEntityInfoDS_failedToGetEntityList") + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});

	}

	private List<ListGridRecord> createRecord(List<CrawlEntityInfo> entities) {

		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (entities != null) {
			for (CrawlEntityInfo entity : entities) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("name", entity.getName());
				record.setAttribute("displayName", entity.getDisplayName());
				if (isGetDataCount) {
					record.setAttribute("count", entity.getCount());
				} else {
					record.setAttribute("count", "-");
				}
				record.setAttribute("isError", entity.isError());
				record.setAttribute("errorMessage", entity.getErrorMessage());

				if (entity.getUpdateDate() != null) {
					record.setAttribute("updateDate", SmartGWTUtil.formatTimestamp(entity.getUpdateDate()));
				}

				if (entity.getLastCrawlDate() == null) {
					record.setAttribute("lastCrawlDate", "-");
				} else {
					record.setAttribute("lastCrawlDate", SmartGWTUtil.formatTimestamp(entity.getLastCrawlDate()));
				}
				list.add(record);
			}
		}
		return list;
	}

}
