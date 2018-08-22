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

package org.iplass.adminconsole.client.tools.data.entityexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.DefragEntityInfo;
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

public class DefragEntityInfoDS extends AbstractAdminDataSource {

	private static final EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();

	/**
	 * DSインスタンスを返します。
	 *
	 * @param isGetDataCount データ件数取得有無
	 * @return DSインスタンス
	 */
	public static DefragEntityInfoDS getInstance(boolean isGetDataCount) {
		return new DefragEntityInfoDS(isGetDataCount);
	}

	/** 件数取得制御フラグ */
	private boolean isGetDataCount = false;

	private DefragEntityInfoDS(boolean isGetDataCount) {
		this.isGetDataCount = isGetDataCount;

		DataSourceField nameField = new DataSourceTextField("name", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_SimpleEntityInfoDS_name"));
		nameField.setPrimaryKey(true);
		DataSourceField displayNameField = new DataSourceTextField("displayName", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_SimpleEntityInfoDS_dispName"));
		DataSourceField updateDateField = new DataSourceTextField("updateDate", "Update Date");
		DataSourceField curVersionField = new DataSourceTextField("currentVersion", "Version");
		DataSourceField countField = new DataSourceTextField("count", AdminClientMessageUtil.getString("datasource_tools_entityexplorer_SimpleEntityInfoDS_number"));

		setFields(nameField, displayNameField, updateDateField, curVersionField, countField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		service.getDefragEntityList(TenantInfoHolder.getId(), isGetDataCount, new AsyncCallback<List<DefragEntityInfo>>() {

			@Override
			public void onSuccess(List<DefragEntityInfo> entities) {
				List<ListGridRecord> records = createRecord(entities);
				response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				response.setStartRow(0);
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				SC.warn(AdminClientMessageUtil.getString("datasource_tools_entityexplorer_SimpleEntityInfoDS_failedToGetEntityList") + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});

	}

	private List<ListGridRecord> createRecord(List<DefragEntityInfo> entities) {

		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (entities != null) {
			for (DefragEntityInfo entity : entities) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("name", entity.getName());
				record.setAttribute("displayName", entity.getDisplayName());
				if (isGetDataCount) {
					record.setAttribute("count", entity.getCount());
				} else {
					record.setAttribute("count", "-");
				}
				if (entity.getUpdateDate() != null) {
					record.setAttribute("updateDate", SmartGWTUtil.formatTimestamp(entity.getUpdateDate()));
				}
				if (entity.getCurrentVersion() != null) {
					record.setAttribute("currentVersion", entity.getCurrentVersion());
				} else {
					record.setAttribute("currentVersion", "-");
				}

				record.setAttribute("isError", entity.isError());
				record.setAttribute("errorMessage", entity.getErrorMessage());
				list.add(record);
			}
		}
		return list;
	}

}
