/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.data.auth;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AuthProviderDS extends AbstractAdminDataSource {

	private static AuthProviderDS instance = null;

	/** プロバイダ名のリスト(ServiceConfigレベルで変わらないので１度取得したらそれを利用) */
	private static List<String> providerNameList;

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public static void setDataSource(ListGrid grid) {
		grid.setDataSource(AuthProviderDS.getInstance());

		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME);
		nameField.setTitle("Provider Name");
		grid.setFields(nameField);
	}


	public static AuthProviderDS getInstance() {
		if (instance == null) {
			instance = new AuthProviderDS();
		}
		return instance;
	}

	private AuthProviderDS() {
		DataSourceField nameField = new DataSourceField(
				DataSourceConstants.FIELD_NAME, FieldType.TEXT);
		nameField.setTitle(DataSourceConstants.FIELD_NAME_TITLE);
		setFields(nameField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
		if (providerNameList != null) {
			setResponseData(requestId, request, response, providerNameList);
		} else {
			service.getSelectableAuthProviderNameList(TenantInfoHolder.getId(), new AsyncCallback<List<String>>() {

				@Override
				public void onSuccess(List<String> result) {
					providerNameList = result;
					setResponseData(requestId, request, response, result);
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("error!!!", caught);

					response.setStatus(RPCResponse.STATUS_FAILURE);
					processResponse(requestId, response);
				}
			});
		}
	}

	private void setResponseData(final String requestId, final DSRequest request, final DSResponse response, List<String> result) {
		List<ListGridRecord> records = new ArrayList<ListGridRecord>();
		for (String name : result) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(DataSourceConstants.FIELD_NAME, name);
			records.add(record);
		}

		response.setData(records.toArray(new ListGridRecord[0]));
		response.setTotalRows(result.size());
		processResponse(requestId, response);
	}

}
