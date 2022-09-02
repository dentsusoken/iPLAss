/*
 * Copyright 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.adminconsole.client.metadata.ui.oidc;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.oidc.definition.ClientAuthenticationType;
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinition;
import org.iplass.mtp.auth.oidc.definition.ResponseMode;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class OpenIdConnectAttributePane extends VLayout {

	private static LinkedHashMap<String, String> clientAuthenticationTypeMap;
	private static LinkedHashMap<String, String> responseModeMap;
	static {
		clientAuthenticationTypeMap = new LinkedHashMap<>();
		clientAuthenticationTypeMap.put("", "");
		for (ClientAuthenticationType type : ClientAuthenticationType.values()) {
			clientAuthenticationTypeMap.put(type.name(), type.name());
		}

		responseModeMap = new LinkedHashMap<>();
		responseModeMap.put("", "");
		for (ResponseMode mode : ResponseMode.values()) {
			responseModeMap.put(mode.name(), mode.name());
		}
	}

	private DynamicForm form;

	private TextItem txtIssuerField;
	private TextItem txtAuthorizationEndpointField;
	private TextItem txtTokenEndpointField;
	private TextItem txtUserInfoEndpointField;
	private TextItem txtJwksEndpointField;
	private TextAreaItem txaJwksContentsField;
	private TextItem txtClientIdField;
	private TextAreaItem txaScopesField;
	private SelectItem selClientAuthenticationTypeField;
	private CheckboxItem chkUseNonceField;
	private CheckboxItem chkEnablePKCEField;
	private CheckboxItem chkIssParameterSupportedField;
	private CheckboxItem chkValidateSignField;
	private SelectItem selResponseModeField;
	private TextItem txtSubjectNameClaimField;
	private TextItem txtAutoUserProvisioningHandlerField;
	private CheckboxItem chkEnableTransientUserField;

	private TextItem txtBackUrlAfterAuthField;
	private TextItem txtBackUrlAfterConnectField;

	public OpenIdConnectAttributePane() {
		initialize();
	}

	private void initialize() {
		setWidth100();

		form = new DynamicForm();
		form.setAlign(Alignment.LEFT);
		form.setWidth100();
		form.setNumCols(5);
		form.setColWidths(100, 300, 100, 300, "*");
		form.setMargin(5);

		txtIssuerField = new TextItem();
		txtIssuerField.setTitle("Issuer");
		txtIssuerField.setWidth("100%");
		txtIssuerField.setBrowserSpellCheck(false);
		txtIssuerField.setColSpan(3);
		txtIssuerField.setStartRow(true);
		txtIssuerField.setRequired(true);

		txtAuthorizationEndpointField = new TextItem();
		txtAuthorizationEndpointField.setTitle("Authorization Endpoint");
		txtAuthorizationEndpointField.setWidth("100%");
		txtAuthorizationEndpointField.setBrowserSpellCheck(false);
		txtAuthorizationEndpointField.setColSpan(3);
		txtAuthorizationEndpointField.setStartRow(true);
		txtAuthorizationEndpointField.setRequired(true);

		txtTokenEndpointField = new TextItem();
		txtTokenEndpointField.setTitle("Token Endpoint");
		txtTokenEndpointField.setWidth("100%");
		txtTokenEndpointField.setBrowserSpellCheck(false);
		txtTokenEndpointField.setColSpan(3);
		txtTokenEndpointField.setStartRow(true);
		txtTokenEndpointField.setRequired(true);

		txtUserInfoEndpointField = new TextItem();
		txtUserInfoEndpointField.setTitle("UserInfo Endpoint");
		txtUserInfoEndpointField.setWidth("100%");
		txtUserInfoEndpointField.setBrowserSpellCheck(false);
		txtUserInfoEndpointField.setColSpan(3);
		txtUserInfoEndpointField.setStartRow(true);

		txtJwksEndpointField = new TextItem();
		txtJwksEndpointField.setTitle("Jwks Endpoint");
		txtJwksEndpointField.setWidth("100%");
		txtJwksEndpointField.setBrowserSpellCheck(false);
		txtJwksEndpointField.setColSpan(3);
		txtJwksEndpointField.setStartRow(true);

		txaJwksContentsField = new TextAreaItem();
		txaJwksContentsField.setTitle("Jwks Contents");
		txaJwksContentsField.setWidth("100%");
		txaJwksContentsField.setBrowserSpellCheck(false);
		txaJwksContentsField.setColSpan(3);
		txaJwksContentsField.setStartRow(true);

		txtClientIdField = new TextItem();
		txtClientIdField.setTitle("Client Id");
		txtClientIdField.setWidth("100%");
		txtClientIdField.setBrowserSpellCheck(false);
		txtClientIdField.setColSpan(3);
		txtClientIdField.setStartRow(true);
		txtClientIdField.setRequired(true);

		txaScopesField = new TextAreaItem();
		txaScopesField.setTitle("Scopes");
		txaScopesField.setWidth("100%");
		txaScopesField.setHeight(75);
		txaScopesField.setBrowserSpellCheck(false);
		txaScopesField.setColSpan(3);
		txaScopesField.setStartRow(true);
		txaScopesField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_redirectUris")));

		selClientAuthenticationTypeField = new SelectItem();
		selClientAuthenticationTypeField.setTitle("Client Authentication Type");
		selClientAuthenticationTypeField.setWidth("100%");
		selClientAuthenticationTypeField.setValueMap(clientAuthenticationTypeMap);
		selClientAuthenticationTypeField.setRequired(true);

		chkUseNonceField = new CheckboxItem("useNonce", "Use Nonce");
		chkUseNonceField.setStartRow(true);

		chkEnablePKCEField = new CheckboxItem("enablePKCE", "Enable PKCE");

		chkIssParameterSupportedField = new CheckboxItem("issParameterSupported", "Iss Parameter Supported");

		chkValidateSignField = new CheckboxItem("validateSign", "Validate Sign");

		selResponseModeField = new SelectItem();
		selResponseModeField.setTitle("Response Mode");
		selResponseModeField.setWidth("100%");
		selResponseModeField.setValueMap(responseModeMap);

		txtSubjectNameClaimField = new TextItem();
		txtSubjectNameClaimField.setTitle("Subject Name Claim");
		txtSubjectNameClaimField.setWidth("100%");
		txtSubjectNameClaimField.setBrowserSpellCheck(false);
		txtSubjectNameClaimField.setColSpan(3);
		txtSubjectNameClaimField.setStartRow(true);
		txtSubjectNameClaimField.setRequired(true);

		txtAutoUserProvisioningHandlerField = new TextItem();
		txtAutoUserProvisioningHandlerField.setTitle("AutoUser Provisioning Handler");
		txtAutoUserProvisioningHandlerField.setWidth("100%");
		txtAutoUserProvisioningHandlerField.setBrowserSpellCheck(false);
		txtAutoUserProvisioningHandlerField.setColSpan(3);
		txtAutoUserProvisioningHandlerField.setStartRow(true);

		chkEnableTransientUserField = new CheckboxItem("enableTransientUser", "Enable Transient User");

		txtBackUrlAfterAuthField = new TextItem();
		txtBackUrlAfterAuthField.setTitle("Back Url After Auth");
		txtBackUrlAfterAuthField.setWidth("100%");
		txtBackUrlAfterAuthField.setBrowserSpellCheck(false);
		txtBackUrlAfterAuthField.setColSpan(3);
		txtBackUrlAfterAuthField.setStartRow(true);

		txtBackUrlAfterConnectField = new TextItem();
		txtBackUrlAfterConnectField.setTitle("Back Url After Connect");
		txtBackUrlAfterConnectField.setWidth("100%");
		txtBackUrlAfterConnectField.setBrowserSpellCheck(false);
		txtBackUrlAfterConnectField.setColSpan(3);
		txtBackUrlAfterConnectField.setStartRow(true);

		form.setItems(txtIssuerField,
				txtAuthorizationEndpointField,
				txtTokenEndpointField,
				txtUserInfoEndpointField,
				txtJwksEndpointField,
				txaJwksContentsField,
				txtClientIdField,
				txaScopesField,
				selClientAuthenticationTypeField,
				chkUseNonceField,
				chkEnablePKCEField,
				chkIssParameterSupportedField,
				chkValidateSignField,
				selResponseModeField,
				txtSubjectNameClaimField,
				txtAutoUserProvisioningHandlerField,
				chkEnableTransientUserField,
				txtBackUrlAfterAuthField,
				txtBackUrlAfterConnectField);

		addMember(form);
	}

	public void setDefinition(OpenIdConnectDefinition definition) {
		txtIssuerField.setValue(definition.getIssuer());
		txtAuthorizationEndpointField.setValue(definition.getAuthorizationEndpoint());
		txtTokenEndpointField.setValue(definition.getTokenEndpoint());
		txtUserInfoEndpointField.setValue(definition.getUserInfoEndpoint());
		txtJwksEndpointField.setValue(definition.getJwksEndpoint());
		txaJwksContentsField.setValue(definition.getJwksContents());
		txtClientIdField.setValue(definition.getClientId());
		
		String scopeText = SmartGWTUtil.convertListToString(definition.getScopes(), "\n");
		if (scopeText != null) {
			txaScopesField.setValue(scopeText);
		} else {
			txaScopesField.clearValue();
		}
		
		if (definition.getClientAuthenticationType() != null) {
			selClientAuthenticationTypeField.setValue(definition.getClientAuthenticationType().name());
		} else {
			selClientAuthenticationTypeField.setValue("");
		}
		
		chkUseNonceField.setValue(definition.isUseNonce());
		chkEnablePKCEField.setValue(definition.isEnablePKCE());
		chkIssParameterSupportedField.setValue(definition.isIssParameterSupported());
		chkValidateSignField.setValue(definition.isValidateSign());
		
		if (definition.getResponseMode() != null) {
			selResponseModeField.setValue(definition.getResponseMode().name());
		} else {
			selResponseModeField.setValue("");
		}
		
		txtSubjectNameClaimField.setValue(definition.getSubjectNameClaim());
		txtAutoUserProvisioningHandlerField.setValue(definition.getAutoUserProvisioningHandler());
		chkEnableTransientUserField.setValue(definition.isEnableTransientUser());
		txtBackUrlAfterAuthField.setValue(definition.getBackUrlAfterAuth());
		txtBackUrlAfterConnectField.setValue(definition.getBackUrlAfterConnect());
	}

	public void getEditDefinition(OpenIdConnectDefinition definition) {
		definition.setIssuer(SmartGWTUtil.getStringValue(txtIssuerField));
		definition.setAuthorizationEndpoint(SmartGWTUtil.getStringValue(txtAuthorizationEndpointField));
		definition.setTokenEndpoint(SmartGWTUtil.getStringValue(txtTokenEndpointField));
		definition.setUserInfoEndpoint(SmartGWTUtil.getStringValue(txtUserInfoEndpointField));
		definition.setJwksEndpoint(SmartGWTUtil.getStringValue(txtJwksEndpointField));
		definition.setJwksContents(SmartGWTUtil.getStringValue(txaJwksContentsField));
		definition.setClientId(SmartGWTUtil.getStringValue(txtClientIdField));
		
		String scopesText = SmartGWTUtil.getStringValue(txaScopesField, true);
		definition.setScopes(SmartGWTUtil.convertStringToList(scopesText));
		
		String clientAuthenticationType = SmartGWTUtil.getStringValue(selClientAuthenticationTypeField, true);
		if (clientAuthenticationType != null) {
			definition.setClientAuthenticationType(ClientAuthenticationType.valueOf(clientAuthenticationType));
		} else {
			definition.setClientAuthenticationType(null);
		}

		definition.setUseNonce(SmartGWTUtil.getBooleanValue(chkUseNonceField));
		definition.setEnablePKCE(SmartGWTUtil.getBooleanValue(chkEnablePKCEField));
		definition.setIssParameterSupported(SmartGWTUtil.getBooleanValue(chkIssParameterSupportedField));
		definition.setValidateSign(SmartGWTUtil.getBooleanValue(chkValidateSignField));
		
		String responseMode = SmartGWTUtil.getStringValue(selResponseModeField, true);
		if (responseMode != null) {
			definition.setResponseMode(ResponseMode.valueOf(responseMode));
		} else {
			definition.setResponseMode(null);
		}
		
		definition.setSubjectNameClaim(SmartGWTUtil.getStringValue(txtSubjectNameClaimField));
		definition.setEnableTransientUser(SmartGWTUtil.getBooleanValue(chkEnableTransientUserField));

		definition.setBackUrlAfterAuth(SmartGWTUtil.getStringValue(txtBackUrlAfterAuthField));
		definition.setBackUrlAfterConnect(SmartGWTUtil.getStringValue(txtBackUrlAfterConnectField));
	}

	public boolean validate() {
		return form.validate();
	}
}
