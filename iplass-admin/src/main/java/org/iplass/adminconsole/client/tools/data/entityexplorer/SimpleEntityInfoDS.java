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
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.SimpleEntityInfo;
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

public class SimpleEntityInfoDS extends AbstractAdminDataSource {

	public enum FIELD_NAME {
		NAME,
		DISPLAY_NAME,

		DATA_COUNT,

		LISTENER_COUNT,
		VERSIONING,

		DETAIL_VIEW_COUNT,
		SEARCH_VIEW_COUNT,
		BULK_VIEW_COUNT,
		VIEW_CONTROL,

		REPOSITORY,

		IS_ERROR,
		ERROR_MESSAGE,
	}

	/**
	 * DSインスタンスを返します。
	 *
	 * @param isGetDataCount データ件数取得有無
	 * @return DSインスタンス
	 */
	public static SimpleEntityInfoDS getInstance(boolean isGetDataCount) {
		return new SimpleEntityInfoDS(isGetDataCount);
	}

	/** 件数取得制御フラグ */
	private boolean isGetDataCount = false;

	private SimpleEntityInfoDS(boolean isGetDataCount) {
		this.isGetDataCount = isGetDataCount;

		DataSourceField name = new DataSourceTextField(FIELD_NAME.NAME.name());
		name.setPrimaryKey(true);
		DataSourceField displayName = new DataSourceTextField(FIELD_NAME.DISPLAY_NAME.name());

		DataSourceField count = new DataSourceTextField(FIELD_NAME.DATA_COUNT.name());

		DataSourceField listenerCount = new DataSourceTextField(FIELD_NAME.LISTENER_COUNT.name());
		DataSourceField versioning = new DataSourceTextField(FIELD_NAME.VERSIONING.name());

		DataSourceField detailViewCount = new DataSourceTextField(FIELD_NAME.DETAIL_VIEW_COUNT.name());
		DataSourceField searchViewCount = new DataSourceTextField(FIELD_NAME.SEARCH_VIEW_COUNT.name());
		DataSourceField bulkViewCount = new DataSourceTextField(FIELD_NAME.BULK_VIEW_COUNT.name());
		DataSourceField viewControl = new DataSourceTextField(FIELD_NAME.VIEW_CONTROL.name());

		DataSourceField repository = new DataSourceTextField(FIELD_NAME.REPOSITORY.name());

		setFields(name, displayName, count, listenerCount, versioning,
				detailViewCount, searchViewCount, bulkViewCount, viewControl,
				repository);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();
		service.getSimpleEntityList(TenantInfoHolder.getId(), isGetDataCount, new AsyncCallback<List<SimpleEntityInfo>>() {

			@Override
			public void onSuccess(List<SimpleEntityInfo> entities) {
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

	private List<ListGridRecord> createRecord(List<SimpleEntityInfo> entities) {

		List<ListGridRecord> list = new ArrayList<>();

		if (entities != null) {
			for (SimpleEntityInfo entity : entities) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(FIELD_NAME.NAME.name(), entity.getName());
				record.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), entity.getDisplayName());
				if (isGetDataCount) {
					record.setAttribute(FIELD_NAME.DATA_COUNT.name(), entity.getCount());
				} else {
					record.setAttribute(FIELD_NAME.DATA_COUNT.name(), "-");
				}
				record.setAttribute(FIELD_NAME.LISTENER_COUNT.name(), entity.getListenerCount());
				record.setAttribute(FIELD_NAME.VERSIONING.name(), entity.getVersionControlType());
				record.setAttribute(FIELD_NAME.DETAIL_VIEW_COUNT.name(), entity.getDetailFormViewCount());
				record.setAttribute(FIELD_NAME.SEARCH_VIEW_COUNT.name(), entity.getSearchFormViewCount());
				record.setAttribute(FIELD_NAME.BULK_VIEW_COUNT.name(), entity.getBulkFormViewCount());
				record.setAttribute(FIELD_NAME.VIEW_CONTROL.name(), entity.getViewControl());
				record.setAttribute(FIELD_NAME.REPOSITORY.name(), entity.getRepository());

				record.setAttribute(FIELD_NAME.IS_ERROR.name(), entity.isError());
				record.setAttribute(FIELD_NAME.ERROR_MESSAGE.name(), entity.getErrorMessage());
				list.add(record);
			}
		}
		return list;
	}

}
