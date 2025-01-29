/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.Map;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFile;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFileCondition;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.logexplorer.LogExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class LogFileDS extends AbstractAdminDataSource {

	public enum FIELD_NAME {
		PATH, LAST_MODIFIED, SIZE, FULL_PATH, VALUE_OBJECT
	}

	public static final String LIMIT = "limit";

	public static LogFileDS getInstance() {
		return new LogFileDS();
	}

	private LogFileDS() {

		//実際にはUI側でListGridFieldを利用して定義しているため、ダミーで作成
		//Filterを表示可能にする場合、ここで作成されているDataSourceFieldから入力TextBoxの型を推察される
		//下記２つをFilter対象にするため、TextFieldとして定義
		DataSourceField pathField = new DataSourceTextField(FIELD_NAME.PATH.name(), "Path");
		DataSourceField lastModifiedField = new DataSourceTextField(FIELD_NAME.LAST_MODIFIED.name(), "Last Modified");
		setFields(pathField, lastModifiedField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		// 画面入力条件の取得
		LogFileCondition logFileCondition = new LogFileCondition();
		Criteria criteria = request.getCriteria();
		if (criteria != null) {
			Map<?, ?> criteriaMap = criteria.getValues();
			for (Object key : criteriaMap.keySet()) {
				if (key.equals(FIELD_NAME.PATH.name())) {
					logFileCondition.setFileName((String) criteriaMap.get(key));
				} else if (key.equals(FIELD_NAME.LAST_MODIFIED.name())) {
					logFileCondition.setLastModified((String) criteriaMap.get(key));
				} else if (key.equals(LIMIT)) {
					logFileCondition.setLimit((Integer) criteriaMap.get(key));
				}
			}
		}

		LogExplorerServiceAsync service = LogExplorerServiceFactory.get();
		service.getLogfileNames(TenantInfoHolder.getId(), logFileCondition, new AsyncCallback<List<LogFile>>() {

			@Override
			public void onSuccess(List<LogFile> result) {
				List<ListGridRecord> records = createRecord(result);
				response.setData(records.toArray(new ListGridRecord[] {}));
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
