/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.webapi;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class OAuthAttributePane extends VLayout {

	private DynamicForm form;

	/** Bearer Tokenによる認証を許可するか */
	private CheckboxItem supportBearerTokenField;

	/** 許可Scope */
	private TextAreaItem oauthScopesField;

	public OAuthAttributePane() {

		setWidth100();
		setAutoHeight();
		setMargin(5);
		setMembersMargin(10);

		form = new DynamicForm();
		form.setWidth100();
		form.setPadding(10);
		form.setNumCols(5);
		form.setColWidths(100, 300, 100, 300, "*");
		form.setIsGroup(true);
		form.setGroupTitle("OAuth");

		supportBearerTokenField = new CheckboxItem("supportBearerTokenField", "Support Bearer Token");
		supportBearerTokenField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_webapi_OAuthAttributePane_supportBearerToken")));

		oauthScopesField = new TextAreaItem();
		oauthScopesField.setTitle("Scopes");
		oauthScopesField.setWidth("100%");
		oauthScopesField.setHeight(75);
		oauthScopesField.setBrowserSpellCheck(false);
		oauthScopesField.setColSpan(3);
		oauthScopesField.setStartRow(true);
		oauthScopesField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_webapi_OAuthAttributePane_oauthScopes")));

		form.setItems(supportBearerTokenField, oauthScopesField);

		addMember(form);
	}

	public void setDefinition(WebApiDefinition definition) {

		supportBearerTokenField.setValue(definition.isSupportBearerToken());

		String scopeText = SmartGWTUtil.convertArrayToString(definition.getOauthScopes(), "\n");
		if (scopeText != null) {
			oauthScopesField.setValue(scopeText);
		} else {
			oauthScopesField.clearValue();
		}
	}

	public WebApiDefinition getEditDefinition(WebApiDefinition definition) {

		definition.setSupportBearerToken(SmartGWTUtil.getBooleanValue(supportBearerTokenField));

		String scopeText = SmartGWTUtil.getStringValue(oauthScopesField, true);
		definition.setOauthScopes(SmartGWTUtil.convertStringToArray(scopeText));

		return definition;
	}

	public boolean validate() {
		return form.validate();
	}

}
