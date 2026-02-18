/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.auth.builtin;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class SelectBuiltinUserDS extends BuiltinUserDS {

	public static SelectBuiltinUserDS getInstance() {
		return new SelectBuiltinUserDS();
	}

	public static SelectBuiltinUserDS setDataSource(ListGrid grid) {
		SelectBuiltinUserDS instance = getInstance();
		grid.setDataSource(instance);

		ListGridField oidField = new ListGridField(OID, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_SelectBuiltinUserDS_oid"));
		ListGridField accountIdField = new ListGridField(ACCOUNT_ID, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_SelectBuiltinUserDS_accountId"));
		ListGridField nameField = new ListGridField(NAME, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_SelectBuiltinUserDS_name"));
		ListGridField mailField = new ListGridField(MAIL, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_SelectBuiltinUserDS_mail"));
		ListGridField adminField = new ListGridField(ADMIN, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_SelectBuiltinUserDS_admin"));
		adminField.setAlign(Alignment.CENTER);
		ListGridField startDateField = new ListGridField(START_DATE, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_SelectBuiltinUserDS_startDate"));
		startDateField.setWidth(130);
		ListGridField endDateField = new ListGridField(END_DATE, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_SelectBuiltinUserDS_endDate"));
		endDateField.setWidth(130);

		grid.setFields(oidField, accountIdField, nameField, mailField, adminField, startDateField, endDateField);

		return instance;

	}

	/**
	 * コンストラクタ
	 *
	 */
	protected SelectBuiltinUserDS() {
	}

	@Override
	protected void setDataSourceFields() {
		DataSourceField accountIdField = new DataSourceTextField(ACCOUNT_ID, "Account ID");
		DataSourceField nameField = new DataSourceTextField(NAME, "Name");
		DataSourceField mailField = new DataSourceTextField(MAIL, "Mail");
		DataSourceField adminField = new DataSourceTextField(ADMIN, "Admin");
		DataSourceField loginErrorCntField = new DataSourceTextField(LOGIN_ERROR_COUNT, "Error Count");
		DataSourceField lastPasswordChangeField = new DataSourceTextField(LAST_PASSWORD_CHANGE, "Last Password Change");
		DataSourceField passwordRemainDaysField = new DataSourceTextField(PASSWORD_REMAIN_DAYS, "PW Remain Days");
		DataSourceField lastLoginField = new DataSourceTextField(LAST_LOGIN, "Last Login");
		DataSourceField startDateField = new DataSourceTextField(START_DATE, "Start Date");
		DataSourceField endDateField = new DataSourceTextField(END_DATE, "End Date");

		setFields(accountIdField, nameField, mailField, adminField, loginErrorCntField, lastPasswordChangeField, passwordRemainDaysField, lastLoginField, startDateField, endDateField);
	}

	@Override
	protected void debug(String message) {
		GWT.log("BuiltinUserSelectListDS DEBUG " + message);
	}

}
