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

package org.iplass.adminconsole.client.metadata.ui.oauth.authorize;

import java.util.Arrays;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class OAuthAuthorizationAttributePane extends VLayout {

	private DynamicForm form;

	private TextAreaItem txaAvailableRoles;

	private ScopeGridPane gridScopes;

	private MetaDataSelectItem selConsentTemplate;

	private TextItem txtIssuerUri;

	public OAuthAuthorizationAttributePane() {
		initialize();
	}

	public void setDefinition(OAuthAuthorizationDefinition definition) {

		if (definition.getAvailableRoles() != null) {
			String roleText = "";
			for (String roleCode : definition.getAvailableRoles()) {
				roleText += roleCode + "\n";
			}
			if (!roleText.isEmpty()) {
				roleText = roleText.substring(0, roleText.length() - 1);
			}
			txaAvailableRoles.setValue(roleText);
		}

		gridScopes.setDefinition(definition);

		if (definition.getConsentTemplateName() != null) {
			selConsentTemplate.setValue(definition.getConsentTemplateName());
		} else {
			selConsentTemplate.setValue("");
		}

		txtIssuerUri.setValue(definition.getIssuerUri());
	}

	public OAuthAuthorizationDefinition getEditDefinition(OAuthAuthorizationDefinition definition) {

		String availableRolesText = SmartGWTUtil.getStringValue(txaAvailableRoles, true);
		if (availableRolesText != null && !availableRolesText.trim().isEmpty()) {
			String[] availableRolesTextArray = availableRolesText.split("\r\n|[\n\r\u2028\u2029\u0085]");
			definition.setAvailableRoles(Arrays.asList(availableRolesTextArray));
		} else {
			definition.setAvailableRoles(null);
		}

		gridScopes.getEditDefinition(definition);

		definition.setConsentTemplateName(SmartGWTUtil.getStringValue(selConsentTemplate, true));

		definition.setIssuerUri(SmartGWTUtil.getStringValue(txtIssuerUri, true));

		return definition;
	}

	private void initialize() {

		setMargin(5);
		setWidth100();
		setAutoHeight();

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(5);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(100, 300, 100, 300, "*");
		form.setMargin(5);

		txaAvailableRoles = new TextAreaItem();
		txaAvailableRoles.setTitle("Available Roles");
		txaAvailableRoles.setWidth("100%");
		txaAvailableRoles.setHeight(75);
		txaAvailableRoles.setBrowserSpellCheck(false);
		txaAvailableRoles.setColSpan(3);
		txaAvailableRoles.setStartRow(true);
		txaAvailableRoles.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_OAuthAuthorizationAttributePane_availableRoles")));

		gridScopes = new ScopeGridPane();
		CanvasItem canvasScopes = new CanvasItem();
		canvasScopes.setTitle("Scopes");
		canvasScopes.setCanvas(gridScopes);
		canvasScopes.setColSpan(3);
		canvasScopes.setStartRow(true);

		selConsentTemplate = new MetaDataSelectItem(TemplateDefinition.class);
		selConsentTemplate.setTitle("Consent Template");
		selConsentTemplate.setWidth("100%");

		txtIssuerUri = new TextItem();
		txtIssuerUri.setTitle("Issuer URI");
		txtIssuerUri.setWidth("100%");
		txtIssuerUri.setBrowserSpellCheck(false);
		txtIssuerUri.setColSpan(3);
		txtIssuerUri.setStartRow(true);

		form.setItems(txaAvailableRoles, canvasScopes, selConsentTemplate, txtIssuerUri);

		addMember(form);
	}

	public boolean validate() {

		boolean formValidate = form.validate();
		boolean gridScopesValidate = gridScopes.validate();
		return formValidate && gridScopesValidate;
	}

}
