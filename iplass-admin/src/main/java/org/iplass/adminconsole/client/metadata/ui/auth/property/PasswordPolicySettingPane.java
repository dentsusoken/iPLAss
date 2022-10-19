/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.auth.property;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpIntegerItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.PasswordPolicyDefinition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;

public class PasswordPolicySettingPane extends AbstractSettingPane {

	private IntegerItem txtMaximumPasswordAge;
	private IntegerItem txtMinimumPasswordAge;
	private TextItem txtPasswordPattern;
	private CheckboxItem chkDenySamePasswordAsAccountId;
	private TextAreaItem txtDenyList;
	private TextItem txtPasswordPatternErrorMessage;
	private List<LocalizedStringDefinition> localizedPasswordPatternErrorMessageList;
	private ButtonItem langBtn;
	private IntegerItem txtPasswordHistoryCount;
	private IntegerItem txtPasswordHistoryPeriod;
	private CheckboxItem chkCreateAccountWithSpecificPassword;
	private CheckboxItem chkResetPasswordWithSpecificPassword;
	private TextItem txtRandomPasswordIncludeSigns;
	private TextItem txtRandomPasswordExcludeChars;
	private IntegerItem txtRandomPasswordLength;
	private IntegerItem txtMaximumRandomPasswordAge;
	private ButtonItem customUserEndDateBtn;
	private TextAreaItem txaCustomUserEndDate;

	public PasswordPolicySettingPane() {

		form.setGroupTitle("Password Policy Setting");

		txtMaximumPasswordAge = new MtpIntegerItem();
		txtMaximumPasswordAge.setTitle("Max Password Age(day)");
		SmartGWTUtil.setRequired(txtMaximumPasswordAge);
		txtMaximumPasswordAge.setValidators(new IsIntegerValidator());
		txtMaximumPasswordAge.setStartRow(true);

		txtMinimumPasswordAge = new MtpIntegerItem();
		txtMinimumPasswordAge.setTitle("Min Password Age(day)");
		SmartGWTUtil.setRequired(txtMinimumPasswordAge);

		txtPasswordPattern = new MtpTextItem();
		txtPasswordPattern.setTitle("Password Pattern");
		txtPasswordPattern.setStartRow(true);
		txtPasswordPattern.setColSpan(3);

		chkDenySamePasswordAsAccountId = new CheckboxItem();
		chkDenySamePasswordAsAccountId.setShowTitle(false);
		chkDenySamePasswordAsAccountId.setTitle("Deny Same Password As Account ID.");

		txtDenyList = new MtpTextAreaItem();
		txtDenyList.setTitle("Deny List");
		txtDenyList.setStartRow(true);
		txtDenyList.setColSpan(3);
		txtDenyList.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_auth_AuthenticationPolicyEditPane_denyList")));

		txtPasswordPatternErrorMessage = new MtpTextItem();
		txtPasswordPatternErrorMessage.setTitle("Password Pattern Error Message");
		txtPasswordPatternErrorMessage.setStartRow(true);
		txtPasswordPatternErrorMessage.setColSpan(3);

		SpacerItem space = new SpacerItem();
		space.setStartRow(true);

		langBtn = new ButtonItem("addDisplayName", "Languages");
		langBtn.setShowTitle(false);
		langBtn.setIcon("world.png");
		langBtn.setStartRow(false);
		langBtn.setEndRow(false);
		langBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_common_MetaCommonAttributeSection_eachLangDspName"));
		langBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

				if (localizedPasswordPatternErrorMessageList == null) {
					localizedPasswordPatternErrorMessageList = new ArrayList<>();
				}
				LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedPasswordPatternErrorMessageList);
				dialog.show();
			}
		});

		txtRandomPasswordIncludeSigns = new MtpTextItem();
		txtRandomPasswordIncludeSigns.setTitle("Random Password Include Signs");
		txtRandomPasswordIncludeSigns.setStartRow(true);

		txtRandomPasswordExcludeChars = new MtpTextItem();
		txtRandomPasswordExcludeChars.setTitle("Random Password Exclude Chars");

		txtRandomPasswordLength = new MtpIntegerItem();
		txtRandomPasswordLength.setTitle("Random Password Length");
		SmartGWTUtil.setRequired(txtRandomPasswordLength);
		txtRandomPasswordLength.setStartRow(true);

		txtMaximumRandomPasswordAge = new MtpIntegerItem();
		txtMaximumRandomPasswordAge.setTitle("Max Random Password Age(day)");
		SmartGWTUtil.setRequired(txtMaximumRandomPasswordAge);
		
		customUserEndDateBtn = new ButtonItem();
		customUserEndDateBtn.setTitle("Edit");
		customUserEndDateBtn.setWidth(100);
		customUserEndDateBtn.setStartRow(false);
		customUserEndDateBtn.setColSpan(3);
		customUserEndDateBtn.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_auth_AuthenticationPolicyEditPane_displayEditDialogSource")));
		customUserEndDateBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(txaCustomUserEndDate),
						ScriptEditorDialogCondition.PASSWORD_POLICY_CUSTOM_USER_END_DATE,
						"ui_metadata_auth_AuthenticationPolicyEditPane_customUserEndDateScriptHint",
						null,
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								txaCustomUserEndDate.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});
			}

		});

		txaCustomUserEndDate = new TextAreaItem();
		txaCustomUserEndDate.setTitle("Custom User End Date Script");
		txaCustomUserEndDate.setColSpan(3);
		txaCustomUserEndDate.setWidth("100%");
		txaCustomUserEndDate.setHeight(100);
		SmartGWTUtil.setReadOnlyTextArea(txaCustomUserEndDate);
		SmartGWTUtil.addHoverToFormItem(txaCustomUserEndDate, AdminClientMessageUtil.getString("metadata_auth_AuthenticationPolicyEditPane_customUserEndDateTooltip"));

		txtPasswordHistoryCount = new MtpIntegerItem();
		txtPasswordHistoryCount.setTitle("Password History Count");
		SmartGWTUtil.setRequired(txtPasswordHistoryCount);
		txtPasswordHistoryCount.setStartRow(true);
		
		txtPasswordHistoryPeriod = new MtpIntegerItem();
		txtPasswordHistoryPeriod.setTitle("Password History Period");
		SmartGWTUtil.setRequired(txtPasswordHistoryPeriod);

		chkCreateAccountWithSpecificPassword = new CheckboxItem();
		chkCreateAccountWithSpecificPassword.setShowTitle(false);
		chkCreateAccountWithSpecificPassword.setTitle("Create Account With Specific Password.");

		chkResetPasswordWithSpecificPassword = new CheckboxItem();
		chkResetPasswordWithSpecificPassword.setShowTitle(false);
		chkResetPasswordWithSpecificPassword.setTitle("Reset Password With Specific Password.");

		form.setItems(
				txtMaximumPasswordAge, txtMinimumPasswordAge, txtPasswordPattern, space,
				chkDenySamePasswordAsAccountId,
				txtDenyList,
				txtPasswordPatternErrorMessage, space, langBtn, txtRandomPasswordIncludeSigns,
				txtRandomPasswordExcludeChars, txtRandomPasswordLength, txtMaximumRandomPasswordAge,
				space, customUserEndDateBtn, txaCustomUserEndDate,
				txtPasswordHistoryCount, txtPasswordHistoryPeriod, 
				space, chkCreateAccountWithSpecificPassword, new SpacerItem(), chkResetPasswordWithSpecificPassword);

		addMember(form);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		PasswordPolicyDefinition passwordPolicyDefinition = definition.getPasswordPolicy();

		txtMaximumPasswordAge.setValue(passwordPolicyDefinition.getMaximumPasswordAge());
		txtMinimumPasswordAge.setValue(passwordPolicyDefinition.getMinimumPasswordAge());
		txtPasswordPattern.setValue(passwordPolicyDefinition.getPasswordPattern());
		chkDenySamePasswordAsAccountId.setValue(passwordPolicyDefinition.isDenySamePasswordAsAccountId());
		txtDenyList.setValue(passwordPolicyDefinition.getDenyList());
		txtPasswordPatternErrorMessage.setValue(passwordPolicyDefinition.getPasswordPatternErrorMessage());
		localizedPasswordPatternErrorMessageList = passwordPolicyDefinition.getLocalizedPasswordPatternErrorMessageList();
		txtPasswordHistoryCount.setValue(passwordPolicyDefinition.getPasswordHistoryCount());
		txtPasswordHistoryPeriod.setValue(passwordPolicyDefinition.getPasswordHistoryPeriod());
		chkCreateAccountWithSpecificPassword.setValue(passwordPolicyDefinition.isCreateAccountWithSpecificPassword());
		chkResetPasswordWithSpecificPassword.setValue(passwordPolicyDefinition.isResetPasswordWithSpecificPassword());
		txtRandomPasswordIncludeSigns.setValue(passwordPolicyDefinition.getRandomPasswordIncludeSigns());
		txtRandomPasswordExcludeChars.setValue(passwordPolicyDefinition.getRandomPasswordExcludeChars());
		txtRandomPasswordLength.setValue(passwordPolicyDefinition.getRandomPasswordLength());
		txaCustomUserEndDate.setValue(passwordPolicyDefinition.getCustomUserEndDate());
		txtMaximumRandomPasswordAge.setValue(passwordPolicyDefinition.getMaximumRandomPasswordAge());
	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		PasswordPolicyDefinition passwordPolicyDefinition = new PasswordPolicyDefinition();

		passwordPolicyDefinition.setMaximumPasswordAge(SmartGWTUtil.getIntegerValue(txtMaximumPasswordAge));

		passwordPolicyDefinition.setMinimumPasswordAge(SmartGWTUtil.getIntegerValue(txtMinimumPasswordAge));
		passwordPolicyDefinition.setPasswordPattern(SmartGWTUtil.getStringValue(txtPasswordPattern, true));
		passwordPolicyDefinition.setDenySamePasswordAsAccountId(SmartGWTUtil.getBooleanValue(chkDenySamePasswordAsAccountId));
		passwordPolicyDefinition.setDenyList(SmartGWTUtil.getStringValue(txtDenyList));
		passwordPolicyDefinition.setPasswordPatternErrorMessage(SmartGWTUtil.getStringValue(txtPasswordPatternErrorMessage, true));
		passwordPolicyDefinition.setLocalizedPasswordPatternErrorMessageList(localizedPasswordPatternErrorMessageList);
		passwordPolicyDefinition.setPasswordHistoryCount(SmartGWTUtil.getIntegerValue(txtPasswordHistoryCount));
		passwordPolicyDefinition.setPasswordHistoryPeriod(SmartGWTUtil.getIntegerValue(txtPasswordHistoryPeriod));
		passwordPolicyDefinition.setCreateAccountWithSpecificPassword(SmartGWTUtil.getBooleanValue(chkCreateAccountWithSpecificPassword));
		passwordPolicyDefinition.setResetPasswordWithSpecificPassword(SmartGWTUtil.getBooleanValue(chkResetPasswordWithSpecificPassword));
		passwordPolicyDefinition.setRandomPasswordIncludeSigns(SmartGWTUtil.getStringValue(txtRandomPasswordIncludeSigns, true));
		passwordPolicyDefinition.setRandomPasswordExcludeChars(SmartGWTUtil.getStringValue(txtRandomPasswordExcludeChars, true));
		passwordPolicyDefinition.setRandomPasswordLength(SmartGWTUtil.getIntegerValue(txtRandomPasswordLength));
		passwordPolicyDefinition.setCustomUserEndDate(SmartGWTUtil.getStringValue(txaCustomUserEndDate));
		passwordPolicyDefinition.setMaximumRandomPasswordAge(SmartGWTUtil.getIntegerValue(txtMaximumRandomPasswordAge));

		definition.setPasswordPolicy(passwordPolicyDefinition);

		return definition;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public void clearErrors() {
		form.clearErrors(true);
	}

}
