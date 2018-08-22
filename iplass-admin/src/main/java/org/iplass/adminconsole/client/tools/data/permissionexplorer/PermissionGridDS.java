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

package org.iplass.adminconsole.client.tools.data.permissionexplorer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.shared.tools.rpc.permissionexplorer.PermissionExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.permissionexplorer.PermissionExplorerServiceFactory;
import org.iplass.mtp.entity.Entity;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.HeaderSpan;

public abstract class PermissionGridDS extends AbstractAdminDataSource {

	/** 編集中のPermissionInfoを設定する際のCriteria KEY */
	public static final String EDIT_DATA_KEY = "editDataKey";

	private List<String> roleCodeList;

	protected PermissionExplorerServiceAsync service;

	protected PermissionGridDS(LinkedHashMap<String, Entity> roleMap) {
		//現状Codeしか利用しないので、コードのみ保持
		this.roleCodeList = new ArrayList<String>(roleMap.keySet());

		service = PermissionExplorerServiceFactory.get();

		setDataSourceField();
	}

	public abstract String getColRoleCode(int col);
	public abstract int getColRoleCodeIndex(int col);

	protected void setDataSourceField() {
		List<DataSourceField> fields = new ArrayList<DataSourceField>();

		DataSourceField displayNameField = new DataSourceTextField("displayName", "Display Name");
		fields.add(displayNameField);
		DataSourceField definitionNameField = new DataSourceTextField("definitionName", "Name");
		definitionNameField.setPrimaryKey(true);
		fields.add(definitionNameField);

		int i = 0;
		List<DataSourceField> summaryFieldList = new ArrayList<DataSourceField>();
		List<DataSourceField> codeFieldList = new ArrayList<DataSourceField>();
		List<DataSourceField> dataFieldList = new ArrayList<DataSourceField>();
		for (String roleCode : getRoleCodeList()) {
			//タイトルはロールコード
			summaryFieldList.add(new DataSourceTextField("roleSummary_" + i, roleCode));
			codeFieldList.add(new DataSourceTextField("roleCode_" + i, roleCode));
			dataFieldList.add(new DataSourceTextField("roleData_" + i, null));
			i++;
		}
		fields.addAll(summaryFieldList);
		fields.addAll(codeFieldList);
		fields.addAll(dataFieldList);

		setFields(fields.toArray(new DataSourceField[]{}));
	}

	protected List<String> getRoleCodeList() {
		return roleCodeList;
	}

	public HeaderSpan getHeaderSpan() {

		String[] spanFields = new String[roleCodeList.size()];
		for (int i = 0; i < roleCodeList.size(); i++) {
			spanFields[i] = "roleSummary_" + i;
		}

		return new HeaderSpan("Role", spanFields);
	}

	public String getPermissionConfiguredSummaryText(Entity permission) {
		return AdminClientMessageUtil.getString("datasource_tools_permissionexplorer_PermissionGridDS_configured");
	}


	public String getPermissionEditingSummaryText(Entity permission) {
		return AdminClientMessageUtil.getString("datasource_tools_permissionexplorer_PermissionGridDS_editing");
	}

	public String getPermissionDeleteSummaryText() {
		return AdminClientMessageUtil.getString("datasource_tools_permissionexplorer_PermissionGridDS_delete");
	}

	protected String getPermissionSearchErrMessage() {
		return AdminClientMessageUtil.getString("datasource_tools_permissionexplorer_PermissionGridDS_searchErr");
	}

}
