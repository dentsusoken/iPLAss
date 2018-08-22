/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.pack;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageEntryStatusInfo;
import org.iplass.adminconsole.shared.tools.rpc.pack.PackageRpcServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.pack.PackageRpcServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PackageDS extends AbstractAdminDataSource {

	public enum FIELD_NAME {
		OID,
		NAME,
		DESCRIPTION,
		TYPE,
		STATUS,
		PROGRESS,
		EXEC_START_DATE,
		EXEC_END_DATE,
		COMPLETED,
		SIZE,
		VALUE_OBJECT
	}

	public static PackageDS getInstance() {
		return new PackageDS();
	}

	private PackageDS() {

		//実際にはUI側でListGridFieldを利用して定義しているため、ダミーで作成
		//(１つは定義していないとエラーになるので作成)
		DataSourceField oidField = new DataSourceTextField(FIELD_NAME.OID.name(), "OID");
		setFields(oidField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		PackageRpcServiceAsync service = PackageRpcServiceFactory.get();
		service.getPackageList(TenantInfoHolder.getId(), new AsyncCallback<List<PackageEntryStatusInfo>>() {

			@Override
			public void onSuccess(List<PackageEntryStatusInfo> result) {
				List<ListGridRecord> records = createRecord(result);
				response.setData(records.toArray(new ListGridRecord[]{}));
				response.setTotalRows(records.size());
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				SC.warn(AdminClientMessageUtil.getString("datasource_tools_pack_PackageListDS_failedToGetPackageList") + caught.getMessage());

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

		});

	}

	private List<ListGridRecord> createRecord(List<PackageEntryStatusInfo> infos) {

		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (infos != null) {
			for (PackageEntryStatusInfo info : infos) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(FIELD_NAME.OID.name(), info.getOid());
				record.setAttribute(FIELD_NAME.NAME.name(), info.getName());
				record.setAttribute(FIELD_NAME.DESCRIPTION.name(), info.getDescription());
				if (info.getType() != null) {
					record.setAttribute(FIELD_NAME.TYPE.name(), info.getType().getDisplayName());
				}
				if (info.getStatus() != null) {
					record.setAttribute(FIELD_NAME.STATUS.name(), info.getStatus().getDisplayName());
				}
				record.setAttribute(FIELD_NAME.PROGRESS.name(), info.getProgress());
				record.setAttribute(FIELD_NAME.EXEC_START_DATE.name(), SmartGWTUtil.formatTimestamp(info.getExecStartDate()));
				record.setAttribute(FIELD_NAME.EXEC_END_DATE.name(), SmartGWTUtil.formatTimestamp(info.getExecEndDate()));
				if (info.getArchive() != null) {
					record.setAttribute(FIELD_NAME.COMPLETED.name(), true);
					record.setAttribute(FIELD_NAME.SIZE.name(), info.getArchive().getSize());
				} else {
					record.setAttribute(FIELD_NAME.COMPLETED.name(), false);
					record.setAttribute(FIELD_NAME.SIZE.name(), 0);
				}
				record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), info);
				list.add(record);
			}
		}
		return list;
	}

}
