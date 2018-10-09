/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFile;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class LogFileDS extends AbstractAdminDataSource {

	public enum FIELD_NAME {
		PATH,
		LAST_MODIFIED,
		SIZE,
		FULL_PATH,
		VALUE_OBJECT
	}

	public static LogFileDS getInstance() {
		return new LogFileDS();
	}

	private LogFileDS() {

		//実際にはUI側でListGridFieldを利用して定義しているため、ダミーで作成
		//(１つは定義していないとエラーになるので作成)
		DataSourceField pathField = new DataSourceTextField(FIELD_NAME.PATH.name(), "Path");
		setFields(pathField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

//		SortSpecifier sorts[] = request.getSortBy();
//		if (sorts != null) {
//			com.google.gwt.core.shared.GWT.log("sort conditions:" + sorts.length);
//			for (SortSpecifier sort : sorts) {
//				com.google.gwt.core.shared.GWT.log("item:" + sort.getField() + ", condition:" + sort.getSortDirection());
//			}
//		}

		LogExplorerServiceAsync service = LogExplorerServiceFactory.get();
		service.getLogfileNames(TenantInfoHolder.getId(), new AsyncCallback<List<LogFile>>() {

			@Override
			public void onSuccess(List<LogFile> result) {
				List<ListGridRecord> records = createRecord(result);
				response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				SC.warn(AdminClientMessageUtil.getString("datasource_tools_logexplorer_LogFileDS_failedToGetLogFile") + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

		});

	}

	private List<ListGridRecord> createRecord(List<LogFile> logFiles) {

		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (logFiles != null) {
			for (LogFile logFile : logFiles) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(FIELD_NAME.PATH.name(), logFile.getFileName());
				record.setAttribute(FIELD_NAME.LAST_MODIFIED.name(), logFile.getLastModified());
				record.setAttribute(FIELD_NAME.SIZE.name(), logFile.getSize());
				record.setAttribute(FIELD_NAME.FULL_PATH.name(), logFile.getPath());
				record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), logFiles);
				list.add(record);
			}
		}
		return list;
	}

}
