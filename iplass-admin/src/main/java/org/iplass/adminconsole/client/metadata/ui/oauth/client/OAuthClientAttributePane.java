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

package org.iplass.adminconsole.client.metadata.ui.oauth.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem.ItemOption;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.oauth.definition.ClientType;
import org.iplass.mtp.auth.oauth.definition.GrantType;
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition;
import org.iplass.mtp.auth.oauth.definition.OAuthClientDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class OAuthClientAttributePane extends VLayout implements EditablePane<OAuthClientDefinition> {

	/** ClientTypeの種類選択用Map */
	private static LinkedHashMap<String, String> clientTypeMap;
	static {
		clientTypeMap = new LinkedHashMap<>();
		clientTypeMap.put("", "");
		for (ClientType type : ClientType.values()) {
			clientTypeMap.put(type.name(), type.name());
		}
	}

	private DynamicForm form;

	private SelectItem selAuthorizationServer;

	private SelectItem selClientType;

	private TextAreaItem txaRedirectUris;

	private TextItem txtSectorIdentifierUri;

	private DynamicForm formGrantType;
	private Map<GrantType, CheckboxItem> mapGrantTypeItems;

	private TextItem txtClientUri;

	private TextItem txtLogoUri;

	private TextAreaItem txaContacts;

	private TextItem txtTosUri;

	private TextItem txtPolicyUri;

	public OAuthClientAttributePane() {

		initialize();
	}

	@Override
	public void setDefinition(OAuthClientDefinition definition) {

		if (definition.getAuthorizationServer() != null) {
			selAuthorizationServer.setValue(definition.getAuthorizationServer());
		} else {
			selAuthorizationServer.setValue("");
		}

		if (definition.getClientType() != null) {
			selClientType.setValue(definition.getClientType().name());
		} else {
			selClientType.setValue("");
		}

		String redirectUriText = SmartGWTUtil.convertListToString(definition.getRedirectUris(), "\n");
		if (redirectUriText != null) {
			txaRedirectUris.setValue(redirectUriText);
		} else {
			txaRedirectUris.clearValue();
		}

		txtSectorIdentifierUri.setValue(definition.getSectorIdentifierUri());

		if (definition.getGrantTypes() != null) {
			for (GrantType type : definition.getGrantTypes()) {
				if (mapGrantTypeItems.get(type) != null) {
					mapGrantTypeItems.get(type).setValue(true);
				}
			}
		}

		txtClientUri.setValue(definition.getClientUri());
		txtLogoUri.setValue(definition.getLogoUri());

		String contactsText = SmartGWTUtil.convertListToString(definition.getContacts(), "\n");
		if (contactsText != null) {
			txaContacts.setValue(contactsText);
		} else {
			txaContacts.clearValue();
		}

		txtTosUri.setValue(definition.getTosUri());
		txtPolicyUri.setValue(definition.getPolicyUri());
	}

	@Override
	public OAuthClientDefinition getEditDefinition(OAuthClientDefinition definition) {

		definition.setAuthorizationServer(SmartGWTUtil.getStringValue(selAuthorizationServer, true));

		String clientType = SmartGWTUtil.getStringValue(selClientType, true);
		if (clientType != null) {
			definition.setClientType(ClientType.valueOf(clientType));
		} else {
			definition.setClientType(null);
		}

		String redirectUriText = SmartGWTUtil.getStringValue(txaRedirectUris, true);
		definition.setRedirectUris(SmartGWTUtil.convertStringToList(redirectUriText));

		definition.setSectorIdentifierUri(SmartGWTUtil.getStringValue(txtSectorIdentifierUri, true));

		List<GrantType> grantTypes = new ArrayList<>();
		for (Entry<GrantType, CheckboxItem> entry : mapGrantTypeItems.entrySet()) {
			if (SmartGWTUtil.getBooleanValue(entry.getValue())) {
				grantTypes.add(entry.getKey());
			}
		}
		if (grantTypes.isEmpty()) {
			definition.setGrantTypes(null);
		} else {
			definition.setGrantTypes(grantTypes);
		}

		definition.setClientUri(SmartGWTUtil.getStringValue(txtClientUri, true));
		definition.setLogoUri(SmartGWTUtil.getStringValue(txtLogoUri, true));

		String contactsText = SmartGWTUtil.getStringValue(txaContacts, true);
		definition.setContacts(SmartGWTUtil.convertStringToList(contactsText));

		definition.setTosUri(SmartGWTUtil.getStringValue(txtTosUri, true));
		definition.setPolicyUri(SmartGWTUtil.getStringValue(txtPolicyUri, true));

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

		selAuthorizationServer = new MetaDataSelectItem(OAuthAuthorizationDefinition.class, new ItemOption(true, false));
		selAuthorizationServer.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_oauthAuthorization"));

		selClientType = new SelectItem();
		selClientType.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_clientType"));
		selClientType.setWidth("100%");
		selClientType.setValueMap(clientTypeMap);

		txaRedirectUris = new TextAreaItem();
		txaRedirectUris.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_redirectUri"));
		txaRedirectUris.setWidth("100%");
		txaRedirectUris.setHeight(75);
		txaRedirectUris.setBrowserSpellCheck(false);
		txaRedirectUris.setColSpan(3);
		txaRedirectUris.setStartRow(true);
		txaRedirectUris.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_redirectUris")));

		txtSectorIdentifierUri = new TextItem();
		txtSectorIdentifierUri.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_sectorIdentifierUri"));
		txtSectorIdentifierUri.setWidth("100%");
		txtSectorIdentifierUri.setBrowserSpellCheck(false);
		txtSectorIdentifierUri.setColSpan(3);
		txtSectorIdentifierUri.setStartRow(true);

		mapGrantTypeItems = new LinkedHashMap<>();
		CanvasItem canvasGrantTypes = new CanvasItem();
		canvasGrantTypes.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_grantType"));
		formGrantType = new DynamicForm();
		formGrantType.setWidth100();
		formGrantType.setAutoHeight();
		formGrantType.setNumCols(GrantType.values().length);
		formGrantType.setCellPadding(0);
		for (GrantType grantType : GrantType.values()) {
			CheckboxItem chkGrantType = new CheckboxItem();
			chkGrantType.setTitle(grantType.name().replaceAll("_", " "));
			chkGrantType.setShowTitle(false);
			mapGrantTypeItems.put(grantType, chkGrantType);
		}
		formGrantType.setItems(mapGrantTypeItems.values().toArray(new CheckboxItem[0]));
		canvasGrantTypes.setCanvas(formGrantType);
		canvasGrantTypes.setColSpan(3);
		canvasGrantTypes.setStartRow(true);

		txtClientUri = new TextItem();
		txtClientUri.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_clientUri"));
		txtClientUri.setWidth("100%");
		txtClientUri.setBrowserSpellCheck(false);
		txtClientUri.setColSpan(3);
		txtClientUri.setStartRow(true);

		txtLogoUri = new TextItem();
		txtLogoUri.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_logoUri"));
		txtLogoUri.setWidth("100%");
		txtLogoUri.setBrowserSpellCheck(false);
		txtLogoUri.setColSpan(3);
		txtLogoUri.setStartRow(true);

		txaContacts = new TextAreaItem();
		txaContacts.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_contacts"));
		txaContacts.setWidth("100%");
		txaContacts.setHeight(75);
		txaContacts.setBrowserSpellCheck(false);
		txaContacts.setColSpan(3);
		txaContacts.setStartRow(true);
		txaContacts.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_contacts")));

		txtTosUri = new TextItem();
		txtTosUri.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_termsOfServiceUri"));
		txtTosUri.setWidth("100%");
		txtTosUri.setBrowserSpellCheck(false);
		txtTosUri.setColSpan(3);
		txtTosUri.setStartRow(true);

		txtPolicyUri = new TextItem();
		txtPolicyUri.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_client_OAuthClientAttributePane_policyUri"));
		txtPolicyUri.setWidth("100%");
		txtPolicyUri.setBrowserSpellCheck(false);
		txtPolicyUri.setColSpan(3);
		txtPolicyUri.setStartRow(true);

		form.setItems(selAuthorizationServer, selClientType, txaRedirectUris, txtSectorIdentifierUri,
				canvasGrantTypes, txtClientUri, txtLogoUri, txaContacts, txtTosUri, txtPolicyUri);

		addMember(form);
	}

	@Override
	public boolean validate() {

		boolean formValidate = form.validate();
		boolean grantTypeValidate = formGrantType.validate();
		return formValidate && grantTypeValidate;
	}

	@Override
	public void clearErrors() {
	}

}
