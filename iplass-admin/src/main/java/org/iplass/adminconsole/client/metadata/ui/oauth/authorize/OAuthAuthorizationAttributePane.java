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

package org.iplass.adminconsole.client.metadata.ui.oauth.authorize;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem.ItemOption;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class OAuthAuthorizationAttributePane extends VLayout implements EditablePane<OAuthAuthorizationDefinition> {

	private DynamicForm form;

	private TextAreaItem txaAvailableRoles;

	private ScopeGridPane gridScopes;

	private SelectItem selConsentTemplate;

	private ClientPolicyGridPane gridClientPolicies;

	private SubjectIdentifierTypeEditPane pnlSubjectIdentifierType;

	private TextItem txtIssuerUri;

	public OAuthAuthorizationAttributePane() {
		initialize();
	}

	@Override
	public void setDefinition(OAuthAuthorizationDefinition definition) {

		String roleText = SmartGWTUtil.convertListToString(definition.getAvailableRoles(), "\n");
		if (roleText != null) {
			txaAvailableRoles.setValue(roleText);
		} else {
			txaAvailableRoles.clearValue();
		}

		gridScopes.setDefinition(definition);

		if (definition.getConsentTemplateName() != null) {
			selConsentTemplate.setValue(definition.getConsentTemplateName());
		} else {
			selConsentTemplate.setValue("");
		}

		gridClientPolicies.setDefinition(definition);

		pnlSubjectIdentifierType.setDefinition(definition.getSubjectIdentifierType());

		txtIssuerUri.setValue(definition.getIssuerUri());
	}

	@Override
	public OAuthAuthorizationDefinition getEditDefinition(OAuthAuthorizationDefinition definition) {

		String availableRolesText = SmartGWTUtil.getStringValue(txaAvailableRoles, true);
		definition.setAvailableRoles(SmartGWTUtil.convertStringToList(availableRolesText));

		gridScopes.getEditDefinition(definition);

		definition.setConsentTemplateName(SmartGWTUtil.getStringValue(selConsentTemplate, true));

		gridClientPolicies.getEditDefinition(definition);

		definition.setSubjectIdentifierType(pnlSubjectIdentifierType.getEditDefinition(definition.getSubjectIdentifierType()));

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

		selConsentTemplate = new MetaDataSelectItem(TemplateDefinition.class, new ItemOption(true, false));
		selConsentTemplate.setTitle("Consent Template");

		gridClientPolicies = new ClientPolicyGridPane();
		CanvasItem canvasClientPolicies = new CanvasItem();
		canvasClientPolicies.setTitle("Client Policies");
		canvasClientPolicies.setCanvas(gridClientPolicies);
		canvasClientPolicies.setColSpan(3);
		canvasClientPolicies.setStartRow(true);

		pnlSubjectIdentifierType = new SubjectIdentifierTypeEditPane();
		CanvasItem canvasSubjectIdentifierType = new CanvasItem();
		canvasSubjectIdentifierType.setTitle("SubjectIdentifier");
		canvasSubjectIdentifierType.setCanvas(pnlSubjectIdentifierType);
		canvasSubjectIdentifierType.setColSpan(3);
		canvasSubjectIdentifierType.setStartRow(true);

		txtIssuerUri = new TextItem();
		txtIssuerUri.setTitle("Issuer URI");
		txtIssuerUri.setWidth("100%");
		txtIssuerUri.setBrowserSpellCheck(false);
		txtIssuerUri.setColSpan(3);
		txtIssuerUri.setStartRow(true);

		form.setItems(txaAvailableRoles, canvasScopes, selConsentTemplate, canvasClientPolicies, canvasSubjectIdentifierType, txtIssuerUri);

		addMember(form);
	}

	@Override
	public boolean validate() {

		boolean formValidate = form.validate();
		boolean gridScopesValidate = gridScopes.validate();
		boolean gridClientPoliciesValidate = gridClientPolicies.validate();
		boolean pnlSubjectIdentifierTypeValidate = pnlSubjectIdentifierType.validate();

		return formValidate && gridScopesValidate && gridClientPoliciesValidate && pnlSubjectIdentifierTypeValidate;
	}

	@Override
	public void clearErrors() {
	}

}
