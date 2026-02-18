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

package org.iplass.adminconsole.client.tools.data.permissionexplorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.rpc.permissionexplorer.PermissionExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.permissionexplorer.PermissionExplorerServiceFactory;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RoleDS extends AbstractAdminDataSource {

	private static final DataSourceField[] fields;
	static {
		DataSourceField status = new DataSourceField("status", FieldType.TEXT);
		DataSourceField oid = new DataSourceField("oid", FieldType.TEXT);
		DataSourceField code = new DataSourceField("code",FieldType.TEXT);
		DataSourceField name = new DataSourceField("name",FieldType.TEXT);
		DataSourceField priority = new DataSourceField("priority",FieldType.TEXT);

		fields = new DataSourceField[] {status, oid, code, name, priority};
	}

	private PermissionExplorerServiceAsync service;
	private List<Entity> roleList;

	public static RoleDS getInstance() {
		return new RoleDS();
	}

	private RoleDS() {
		super();

		service = PermissionExplorerServiceFactory.get();

		setFields(fields);

		setClientOnly(true);

	}

	public ListGridField[] getListGridField() {
		List<ListGridField> fields = new ArrayList<ListGridField>();

		ListGridField statusField = new ListGridField("status", "*");
		statusField.setWidth(30);
		fields.add(statusField);

		ListGridField oidField = new ListGridField("oid", AdminClientMessageUtil.getString("ui_tools_data_permissionexplorer_RoleDS_oid"));
		oidField.setHidden(true);
		fields.add(oidField);

		ListGridField roleCodeField = new ListGridField("code", AdminClientMessageUtil.getString("ui_tools_data_permissionexplorer_RoleDS_roleCode"));
		fields.add(roleCodeField);

		ListGridField roleNameField = new ListGridField("name", AdminClientMessageUtil.getString("ui_tools_data_permissionexplorer_RoleDS_roleName"));
		fields.add(roleNameField);

		ListGridField priorityField = new ListGridField("priority", AdminClientMessageUtil.getString("ui_tools_data_permissionexplorer_RoleDS_priority"));
		priorityField.setWidth(100);
		fields.add(priorityField);

		return fields.toArray(new ListGridField[]{});
	}

	public List<Entity> getStoredRoleList() {
		if (roleList == null) {
			return Collections.emptyList();
		}
		return roleList;
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		service.getRoleList(TenantInfoHolder.getId(), new AsyncCallback<List<Entity>>() {

			@Override
			public void onSuccess(List<Entity> result) {
				roleList = result;

				List<ListGridRecord> list = createRoleListRecord(result);
				response.setData(list.toArray(new ListGridRecord[list.size()]));
				response.setTotalRows(list.size());
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});
	}

	private List<ListGridRecord> createRoleListRecord(List<Entity> entities) {
		List<ListGridRecord> list = new ArrayList<ListGridRecord>();

		if (entities != null) {
			for (Entity role : entities) {
				list.add(createRoleRecord(role));
			}
		}
		return list;
	}

	public ListGridRecord createRoleRecord(Entity role) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("status", "");
		record.setAttribute("oid", role.getOid());
		record.setAttribute("name", role.getName());
		record.setAttribute("code", role.getValueAs(String.class, "code"));
		record.setAttribute("priority", role.getValueAs(Long.class, "priority"));
		record.setAttributeAsJavaObject("editEntity", null);
		return record;
	}

	public void applyRoleRecord(ListGridRecord record, Entity role) {
		record.setAttribute("oid", role.getOid());
		record.setAttribute("name", role.getName());
		record.setAttribute("code", role.getValueAs(String.class, "code"));
		record.setAttribute("priority", role.getValueAs(Long.class, "priority"));
	}

}
