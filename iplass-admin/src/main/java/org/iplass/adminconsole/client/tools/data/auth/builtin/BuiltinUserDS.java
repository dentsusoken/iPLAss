/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserListResultDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchConditionDto;
import org.iplass.adminconsole.shared.tools.rpc.auth.builtin.BuiltinAuthExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.auth.builtin.BuiltinAuthExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class BuiltinUserDS extends AbstractAdminDataSource {

	public static final String VALUE_OBJECT = "valueObject";

	public static final String OID = "oid";
	public static final String ACCOUNT_ID = "accountId";
	public static final String NAME = "name";
	public static final String MAIL = "mail";
	public static final String POLICY_NAME = "policyName";
	public static final String ADMIN = "admin";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String LOGIN_ERROR_COUNT = "loginErrorCnt";
	public static final String LOGIN_ERROR_DATE = "loginErrorDate";
	public static final String PASSWORD_REMAIN_DAYS = "passwordRemainDays";
	public static final String LAST_PASSWORD_CHANGE = "lastPasswordChange";
	public static final String LAST_LOGIN = "lastLogin";


	protected BuiltinAuthUserSearchConditionDto condition;

	protected BuiltinAuthUserListResultDto result;

	public static BuiltinUserDS getInstance() {
		return new BuiltinUserDS();
	}

	public static BuiltinUserDS setDataSource(ListGrid grid) {
		BuiltinUserDS instance = getInstance();
		grid.setDataSource(instance);

		ListGridField oidField = new ListGridField(OID, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_oid"));
		ListGridField accountIdField = new ListGridField(ACCOUNT_ID, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_accountId"));
		ListGridField nameField = new ListGridField(NAME, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_name"));
		ListGridField mailField = new ListGridField(MAIL, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_mail"));
		ListGridField policyNameField = new ListGridField(POLICY_NAME, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_policyName"));
		ListGridField adminField = new ListGridField(ADMIN, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_admin"));
		adminField.setAlign(Alignment.CENTER);
		ListGridField loginErrorCntField = new ListGridField(LOGIN_ERROR_COUNT, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_errorCount"));
		loginErrorCntField.setAlign(Alignment.RIGHT);
		ListGridField loginErrorDateField = new ListGridField(LOGIN_ERROR_DATE, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_loginErrorDate"));
		loginErrorDateField.setWidth(130);
		ListGridField lastPasswordChangeField = new ListGridField(LAST_PASSWORD_CHANGE, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_lastPwChange"));
		ListGridField passwordRemainDaysField = new ListGridField(PASSWORD_REMAIN_DAYS, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_pwRemainDays"));
		passwordRemainDaysField.setAlign(Alignment.RIGHT);
		ListGridField lastLoginField = new ListGridField(LAST_LOGIN, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_lastLogin"));
		lastLoginField.setWidth(130);
		ListGridField startDateField = new ListGridField(START_DATE, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_startDate"));
		startDateField.setWidth(130);
		ListGridField endDateField = new ListGridField(END_DATE, AdminClientMessageUtil.getString("ui_tools_data_auth_builtin_BuiltinUserDS_endDate"));
		endDateField.setWidth(130);

		grid.setFields(oidField, accountIdField, nameField, mailField, policyNameField, adminField, loginErrorCntField, loginErrorDateField, lastPasswordChangeField, passwordRemainDaysField, lastLoginField, startDateField, endDateField);

		return instance;

	}

	public void setCondition(BuiltinAuthUserSearchConditionDto condition) {
		this.condition = condition;
	}

	public BuiltinAuthUserListResultDto getResult() {
		return result;
	}

	/**
	 * コンストラクタ
	 *
	 */
	protected BuiltinUserDS() {
		setDataSourceFields();
	}

	protected void setDataSourceFields() {

		DataSourceField accountIdField = new DataSourceTextField(ACCOUNT_ID, "Account ID");
		DataSourceField nameField = new DataSourceTextField(NAME, "Name");
		DataSourceField mailField = new DataSourceTextField(MAIL, "Mail");
		DataSourceField policyNameField = new DataSourceTextField(POLICY_NAME, "Policy Name");
		DataSourceField adminField = new DataSourceTextField(ADMIN, "Admin");
		DataSourceField loginErrorCntField = new DataSourceTextField(LOGIN_ERROR_COUNT, "Error Count");
		DataSourceField loginErrorDateField = new DataSourceTextField(LOGIN_ERROR_DATE, "Login Error Date");
		DataSourceField lastPasswordChangeField = new DataSourceTextField(LAST_PASSWORD_CHANGE, "Last Password Change");
		DataSourceField passwordRemainDaysField = new DataSourceTextField(PASSWORD_REMAIN_DAYS, "PW Remain Days");
		DataSourceField lastLoginField = new DataSourceTextField(LAST_LOGIN, "Last Login");
		DataSourceField startDateField = new DataSourceTextField(START_DATE, "Start Date");
		DataSourceField endDateField = new DataSourceTextField(END_DATE, "End Date");

		setFields(accountIdField, nameField, mailField, policyNameField, adminField, loginErrorCntField, loginErrorDateField, lastPasswordChangeField, passwordRemainDaysField, lastLoginField, startDateField, endDateField);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		this.result = null;

		Criteria criteria = request.getCriteria();

		if (criteria == null) {
			//条件未指定は終了
			final long starttime = System.currentTimeMillis();
			processResponse(requestId, response);

			debug("executeFetch criteria未指定のため終了. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");
			return;
		}

		final long starttime = System.currentTimeMillis();

		BuiltinAuthExplorerServiceAsync service = BuiltinAuthExplorerServiceFactory.get();
		service.search(TenantInfoHolder.getId(), condition,
				new AsyncCallback<BuiltinAuthUserListResultDto>() {

					@Override
					public void onSuccess(BuiltinAuthUserListResultDto result) {
						debug("executeFetch search実行完了. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");

						final long starttime2 = System.currentTimeMillis();

						BuiltinUserDS.this.result = result;

						//レコードの作成
						if (result.isError()) {
							response.setData(new ListGridRecord[]{});
							response.setTotalRows(0);
						} else {
							List<ListGridRecord> records = createRecord(result);
							response.setData(records.toArray(new ListGridRecord[]{}));
							response.setTotalRows(records.size());
						}
						processResponse(requestId, response);

						debug("executeFetch search結果からレコード作成. exec time -> " + (System.currentTimeMillis() - starttime2) + "ms");
					}

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("error!!!", caught);

						//エラーの通知
						BuiltinAuthUserListResultDto result = new BuiltinAuthUserListResultDto();
						result.addLogMessage(AdminClientMessageUtil.getString("datasource_tools_auth_builtin_BuiltinUserDS_errUserSearch"));
						result.addLogMessage(caught.getMessage());
						result.setError(true);

						response.setStatus(RPCResponse.STATUS_FAILURE);
						processResponse(requestId, response);

						debug("executeFetch searchでエラーが発生. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");
					}
		});
	}

	/**
	 * レコードを作成します。
	 *
	 * @param request リクエスト情報
	 * @return {@link ListGridRecord} のリスト
	 */
	private List<ListGridRecord> createRecord(BuiltinAuthUserListResultDto result) {
		if (result == null || result.isError() || result.getUsers() == null) {
			return new ArrayList<ListGridRecord>(0);
		}


		List<ListGridRecord> list = new ArrayList<ListGridRecord>(result.getUsers().size());

		for (BuiltinAuthUserDto user : result.getUsers()) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(VALUE_OBJECT, user);
			record.setAttribute(OID, user.getOid());
			record.setAttribute(ACCOUNT_ID, user.getAccountId());
			record.setAttribute(POLICY_NAME, user.getPolicyName());
			record.setAttribute(NAME, user.getName());
			record.setAttribute(MAIL, user.getMail());
//			record.setAttribute(ADMIN, user.isAdmin());
			if (user.isAdmin()) {
				record.setAttribute(ADMIN, "Y");
			}
//			record.setAttribute(START_DATE, user.getStartDate());
//			record.setAttribute(END_DATE, user.getEndDate());
			if (user.getStartDate() != null) {
				record.setAttribute(START_DATE, SmartGWTUtil.formatTimestamp(user.getStartDate()));
			}
			if (user.getEndDate() != null) {
				record.setAttribute(END_DATE, SmartGWTUtil.formatTimestamp(user.getEndDate()));
			}
			record.setAttribute(LOGIN_ERROR_COUNT, user.getLoginErrorCnt());
			if (user.getLoginErrorDate() != null) {
				record.setAttribute(LOGIN_ERROR_DATE, SmartGWTUtil.formatTimestamp(user.getLoginErrorDate()));
			}
//			record.setAttribute(LAST_PASSWORD_CHANGE, user.getLastPasswordChange());
			if (user.getLastPasswordChange() != null) {
				record.setAttribute(LAST_PASSWORD_CHANGE, SmartGWTUtil.formatDate(user.getLastPasswordChange()));
			}
			if (user.getPasswordRemainDays() != null) {
				record.setAttribute(PASSWORD_REMAIN_DAYS, user.getPasswordRemainDays());
			}

//			record.setAttribute(LAST_LOGIN, user.getLastLoginOn());
			if (user.getLastLoginOn() != null) {
				record.setAttribute(LAST_LOGIN, SmartGWTUtil.formatTimestamp(user.getLastLoginOn()));
			}

			list.add(record);
		}
		return list;
	}

	protected void debug(String message) {
		GWT.log("BuiltinUserListDS DEBUG " + message);
	}

}
