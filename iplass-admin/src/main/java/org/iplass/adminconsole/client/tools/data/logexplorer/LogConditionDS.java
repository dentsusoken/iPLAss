/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.logexplorer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogConditionInfo;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class LogConditionDS extends AbstractAdminDataSource {

	public enum FIELD_NAME {
		KEY,
		LEVEL,
		EXPIRES_AT,
		CONDITION,
		LOGGER_NAME_PATTERN,
		VALUE_OBJECT
	}

	public static LogConditionDS getInstance() {
		return new LogConditionDS();
	}

	private LogConditionDS() {

		//実際にはUI側でListGridFieldを利用して定義しているため、ダミーで作成
		//(１つは定義していないとエラーになるので作成)
		DataSourceTextField key = new DataSourceTextField(FIELD_NAME.KEY.name(), "KEY");
		key.setPrimaryKey(true);
		key.setHidden(true);
		setFields(key);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		LogExplorerServiceAsync service = LogExplorerServiceFactory.get();
		service.getLogConditions(TenantInfoHolder.getId(), new AsyncCallback<List<LogConditionInfo>>() {

			@Override
			public void onSuccess(List<LogConditionInfo> result) {
				List<ListGridRecord> records = createRecords(result);
				response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				SC.warn(AdminClientMessageUtil.getString("datasource_tools_logexplorer_LogConditionDS_failedToGetLogCondition") + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

		});

	}

	@Override
	protected void executeAdd(String requestId, DSRequest request, DSResponse response) {
		ListGridRecord record = new ListGridRecord(request.getData());
		response.setData(new ListGridRecord[] {record});
		processResponse(requestId, response);
	}

	@Override
	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
		ListGridRecord record = new ListGridRecord(request.getData());
		response.setData(new ListGridRecord[] {record});
		processResponse(requestId, response);
	}

	@Override
	protected void executeRemove(String requestId, DSRequest request, DSResponse response) {
		ListGridRecord record = new ListGridRecord(request.getData());
		response.setData(new ListGridRecord[] {record});
		processResponse(requestId, response);
	}

	public void appyToRecord(ListGridRecord record, LogConditionInfo condition) {
		if (record.getAttribute(FIELD_NAME.KEY.name()) == null) {
			record.setAttribute(FIELD_NAME.KEY.name(), SC.generateID());
		}
		record.setAttribute(FIELD_NAME.LEVEL.name(), condition.getLevel());
		if (condition.getExpiresAt() > 0) {
			Timestamp date = new Timestamp(condition.getExpiresAt());
			record.setAttribute(FIELD_NAME.EXPIRES_AT.name(), SmartGWTUtil.formatDateTimeItem(date));
		} else {
			record.setAttribute(FIELD_NAME.EXPIRES_AT.name(), "");
		}
		record.setAttribute(FIELD_NAME.CONDITION.name(), condition.getCondition() != null ? "Y" : "");
		record.setAttribute(FIELD_NAME.LOGGER_NAME_PATTERN.name(), condition.getLoggerNamePattern());
		record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), condition);
	}

	private List<ListGridRecord> createRecords(List<LogConditionInfo> logConditions) {

		List<ListGridRecord> list = new ArrayList<>();

		if (logConditions != null) {
			for (LogConditionInfo logCondition : logConditions) {
				ListGridRecord record = new ListGridRecord();
				appyToRecord(record, logCondition);
				list.add(record);
			}
		}
		return list;
	}

}
