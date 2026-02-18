/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.webauthn;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.webauthn.definition.AttestationConveyancePreference;
import org.iplass.mtp.auth.webauthn.definition.AuthenticatorAttachment;
import org.iplass.mtp.auth.webauthn.definition.ResidentKeyRequirement;
import org.iplass.mtp.auth.webauthn.definition.UserVerificationRequirement;
import org.iplass.mtp.auth.webauthn.definition.WebAuthnDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class WebAuthnAttributePane extends VLayout {

	private static LinkedHashMap<String, String> attestationConveyancePreference;
	private static LinkedHashMap<String, String> authenticatorAttachment;
	private static LinkedHashMap<String, String> residentKeyRequirement;
	private static LinkedHashMap<String, String> userVerificationRequirement;
	static {
		attestationConveyancePreference = new LinkedHashMap<>();
		for (AttestationConveyancePreference type : AttestationConveyancePreference.values()) {
			attestationConveyancePreference.put(type.name(), type.name());
		}
		authenticatorAttachment = new LinkedHashMap<>();
		authenticatorAttachment.put("", "");
		for (AuthenticatorAttachment type : AuthenticatorAttachment.values()) {
			authenticatorAttachment.put(type.name(), type.name());
		}
		residentKeyRequirement = new LinkedHashMap<>();
		for (ResidentKeyRequirement type : ResidentKeyRequirement.values()) {
			residentKeyRequirement.put(type.name(), type.name());
		}
		userVerificationRequirement = new LinkedHashMap<>();
		for (UserVerificationRequirement type : UserVerificationRequirement.values()) {
			userVerificationRequirement.put(type.name(), type.name());
		}

	}

	private DynamicForm form;

	private TextItem txtRpIdField;
	private TextItem txtOriginField;
	private SelectItem selAttestationConveyancePreferenceField;
	private SelectItem selAuthenticatorAttachmentField;
	private SelectItem selResidentKeyRequirementField;
	private SelectItem selUserVerificationRequirementField;
	private CheckboxItem chkSelfAttestationAllowed;
	private TextAreaItem txaAllowedAaguidListField;

	public WebAuthnAttributePane() {
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


		txtRpIdField = new TextItem();
		txtRpIdField.setTitle(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnAttributePane_rpId"));
		txtRpIdField.setWidth("100%");
		txtRpIdField.setBrowserSpellCheck(false);
		txtRpIdField.setColSpan(3);
		txtRpIdField.setStartRow(true);

		txtOriginField = new TextItem();
		txtOriginField.setTitle(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnAttributePane_origin"));
		txtOriginField.setWidth("100%");
		txtOriginField.setBrowserSpellCheck(false);
		txtOriginField.setColSpan(3);
		txtOriginField.setStartRow(true);

		selAttestationConveyancePreferenceField = new SelectItem();
		selAttestationConveyancePreferenceField.setTitle(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnAttributePane_attestationConveyancePreference"));
		selAttestationConveyancePreferenceField.setWidth("100%");
		selAttestationConveyancePreferenceField.setValueMap(attestationConveyancePreference);
		selAttestationConveyancePreferenceField.setRequired(true);

		selAuthenticatorAttachmentField = new SelectItem();
		selAuthenticatorAttachmentField.setTitle(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnAttributePane_authenticatorAttachment"));
		selAuthenticatorAttachmentField.setWidth("100%");
		selAuthenticatorAttachmentField.setValueMap(authenticatorAttachment);

		selResidentKeyRequirementField = new SelectItem();
		selResidentKeyRequirementField.setTitle(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnAttributePane_residentKeyRequirement"));
		selResidentKeyRequirementField.setWidth("100%");
		selResidentKeyRequirementField.setValueMap(residentKeyRequirement);
		selResidentKeyRequirementField.setRequired(true);

		selUserVerificationRequirementField = new SelectItem();
		selUserVerificationRequirementField.setTitle(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnAttributePane_userVerificationRequirement"));
		selUserVerificationRequirementField.setWidth("100%");
		selUserVerificationRequirementField.setValueMap(userVerificationRequirement);
		selUserVerificationRequirementField.setRequired(true);

		chkSelfAttestationAllowed = new CheckboxItem("selfAttestationAllowed", "Self Attestation Allowed");
		chkSelfAttestationAllowed.setStartRow(true);

		txaAllowedAaguidListField = new TextAreaItem();
		txaAllowedAaguidListField.setTitle(AdminClientMessageUtil.getString("ui_metadata_webauthn_WebAuthnAttributePane_allowedAaguidList"));
		txaAllowedAaguidListField.setWidth("100%");
		txaAllowedAaguidListField.setBrowserSpellCheck(false);
		txaAllowedAaguidListField.setColSpan(3);
		txaAllowedAaguidListField.setStartRow(true);

		form.setItems(
				txtRpIdField,
				txtOriginField,
				selAttestationConveyancePreferenceField,
				selAuthenticatorAttachmentField,
				selResidentKeyRequirementField,
				selUserVerificationRequirementField,
				chkSelfAttestationAllowed,
				txaAllowedAaguidListField);

		addMember(form);
	}

	public void setDefinition(WebAuthnDefinition definition) {
		if (definition.getRpId() != null) {
			txtRpIdField.setValue(definition.getRpId());
		} else {
			txtRpIdField.clearValue();
		}
		if (definition.getOrigin() != null) {
			txtOriginField.setValue(definition.getOrigin());
		} else {
			txtOriginField.clearValue();
		}
		if (definition.getAttestationConveyancePreference() != null) {
			selAttestationConveyancePreferenceField.setValue(definition.getAttestationConveyancePreference().name());
		} else {
			selAttestationConveyancePreferenceField.clearValue();
		}
		if (definition.getAuthenticatorAttachment() != null) {
			selAuthenticatorAttachmentField.setValue(definition.getAuthenticatorAttachment().name());
		} else {
			selAuthenticatorAttachmentField.clearValue();
		}
		if (definition.getResidentKeyRequirement() != null) {
			selResidentKeyRequirementField.setValue(definition.getResidentKeyRequirement().name());
		} else {
			selResidentKeyRequirementField.clearValue();
		}
		if (definition.getUserVerificationRequirement() != null) {
			selUserVerificationRequirementField.setValue(definition.getUserVerificationRequirement().name());
		} else {
			selUserVerificationRequirementField.clearValue();
		}
		chkSelfAttestationAllowed.setValue(definition.isSelfAttestationAllowed());
		if (definition.getAllowedAaguidList() != null) {
			txaAllowedAaguidListField.setValue(SmartGWTUtil.convertListToString(definition.getAllowedAaguidList(), "\n"));
		} else {
			txaAllowedAaguidListField.clearValue();
		}
	}

	public void getEditDefinition(WebAuthnDefinition definition) {
		definition.setRpId(SmartGWTUtil.getStringValue(txtRpIdField, true));
		definition.setOrigin(SmartGWTUtil.getStringValue(txtOriginField, true));
		String attestationConveyancePreference = SmartGWTUtil.getStringValue(selAttestationConveyancePreferenceField, true);
		if (attestationConveyancePreference != null) {
			definition.setAttestationConveyancePreference(AttestationConveyancePreference.valueOf(attestationConveyancePreference));
		} else {
			definition.setAttestationConveyancePreference(null);
		}
		String authenticatorAttachment = SmartGWTUtil.getStringValue(selAuthenticatorAttachmentField, true);
		if (authenticatorAttachment != null) {
			definition.setAuthenticatorAttachment(AuthenticatorAttachment.valueOf(authenticatorAttachment));
		} else {
			definition.setAuthenticatorAttachment(null);
		}
		String residentKeyRequirement = SmartGWTUtil.getStringValue(selResidentKeyRequirementField, true);
		if (residentKeyRequirement != null) {
			definition.setResidentKeyRequirement(ResidentKeyRequirement.valueOf(residentKeyRequirement));
		} else {
			definition.setResidentKeyRequirement(null);
		}
		String userVerificationRequirement = SmartGWTUtil.getStringValue(selUserVerificationRequirementField, true);
		if (userVerificationRequirement != null) {
			definition.setUserVerificationRequirement(UserVerificationRequirement.valueOf(userVerificationRequirement));
		} else {
			definition.setUserVerificationRequirement(null);
		}
		definition.setSelfAttestationAllowed(SmartGWTUtil.getBooleanValue(chkSelfAttestationAllowed));
		String allowedAaguidList = SmartGWTUtil.getStringValue(txaAllowedAaguidListField, true);
		definition.setAllowedAaguidList(SmartGWTUtil.convertStringToList(allowedAaguidList));
	}

	public boolean validate() {
		return form.validate();
	}
	
}
