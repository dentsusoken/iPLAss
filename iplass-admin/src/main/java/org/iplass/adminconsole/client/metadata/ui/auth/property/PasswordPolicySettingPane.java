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
import org.iplass.adminconsole.client.base.ui.widget.form.MtpIntegerItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
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
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;

public class PasswordPolicySettingPane extends AbstractSettingPane {

	private CheckboxItem chkUsePolicy;

	private IntegerItem txtMaximumPasswordAge;
	private IntegerItem txtMinimumPasswordAge;
	private TextItem txtPasswordPattern;
	private CheckboxItem chkDenyTheSamePasswordAsAccountId;
	private TextAreaItem txtDenyList;
	private TextItem txtPasswordPatternErrorMessage;
	private List<LocalizedStringDefinition> localizedPasswordPatternErrorMessageList;
	private ButtonItem langBtn;
	private IntegerItem txtPasswordHistoryCount;
	private CheckboxItem chkCreateAccountWithSpecificPassword;
	private CheckboxItem chkResetPasswordWithSpecificPassword;
	private TextItem txtRandomPasswordIncludeSigns;
	private TextItem txtRandomPasswordExcludeChars;
	private IntegerItem txtRandomPasswordLength;

	public PasswordPolicySettingPane() {

		form.setGroupTitle("Password Policy Setting");

		chkUsePolicy = new CheckboxItem();
		chkUsePolicy.setTitle("Use Password Policy");
		chkUsePolicy.setShowTitle(false);
		chkUsePolicy.setColSpan(2);
		chkUsePolicy.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				changeUsePolicy(SmartGWTUtil.getBooleanValue(chkUsePolicy));
			}
		});

		txtMaximumPasswordAge = new MtpIntegerItem();
		txtMaximumPasswordAge.setTitle("Max Password Age(day)");
		txtMaximumPasswordAge.setValidators(new IsIntegerValidator());
		txtMaximumPasswordAge.setStartRow(true);

		txtMinimumPasswordAge = new MtpIntegerItem();
		txtMinimumPasswordAge.setTitle("Min Password Age(day)");

		txtPasswordPattern = new MtpTextItem();
		txtPasswordPattern.setTitle("Password Pattern");
		txtPasswordPattern.setStartRow(true);
		txtPasswordPattern.setColSpan(3);

		chkDenyTheSamePasswordAsAccountId = new CheckboxItem();
		chkDenyTheSamePasswordAsAccountId.setShowTitle(false);
		chkDenyTheSamePasswordAsAccountId.setTitle("Deny Same Password As Account ID.");

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
		txtRandomPasswordLength.setStartRow(true);

		txtPasswordHistoryCount = new MtpIntegerItem();
		txtPasswordHistoryCount.setTitle("Password History Count");

		chkCreateAccountWithSpecificPassword = new CheckboxItem();
		chkCreateAccountWithSpecificPassword.setShowTitle(false);
		chkCreateAccountWithSpecificPassword.setTitle("Create Account With Specific Password.");

		chkResetPasswordWithSpecificPassword = new CheckboxItem();
		chkResetPasswordWithSpecificPassword.setShowTitle(false);
		chkResetPasswordWithSpecificPassword.setTitle("Reset Password With Specific Password.");

		form.setItems(chkUsePolicy,
				txtMaximumPasswordAge, txtMinimumPasswordAge, txtPasswordPattern, space,
				chkDenyTheSamePasswordAsAccountId,
				txtDenyList,
				txtPasswordPatternErrorMessage, space, langBtn, txtRandomPasswordIncludeSigns,
				txtRandomPasswordExcludeChars, txtRandomPasswordLength,
				txtPasswordHistoryCount, space, chkCreateAccountWithSpecificPassword, new SpacerItem(), chkResetPasswordWithSpecificPassword);

		addMember(form);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		PasswordPolicyDefinition passwordPolicyDefinition = definition.getPasswordPolicy();

		boolean usePolicy = passwordPolicyDefinition != null;
		chkUsePolicy.setValue(usePolicy);

		if (!usePolicy) {
			//未設定の場合は、初期値を設定するためnew
			passwordPolicyDefinition = new PasswordPolicyDefinition();
		}

		txtMaximumPasswordAge.setValue(passwordPolicyDefinition.getMaximumPasswordAge());
		txtMinimumPasswordAge.setValue(passwordPolicyDefinition.getMinimumPasswordAge());
		txtPasswordPattern.setValue(passwordPolicyDefinition.getPasswordPattern());
		chkDenyTheSamePasswordAsAccountId.setValue(passwordPolicyDefinition.isDenyTheSamePasswordAsAccountId());
		txtDenyList.setValue(passwordPolicyDefinition.getDenyList());
		txtPasswordPatternErrorMessage.setValue(passwordPolicyDefinition.getPasswordPatternErrorMessage());
		localizedPasswordPatternErrorMessageList = passwordPolicyDefinition.getLocalizedPasswordPatternErrorMessageList();
		txtPasswordHistoryCount.setValue(passwordPolicyDefinition.getPasswordHistoryCount());
		chkCreateAccountWithSpecificPassword.setValue(passwordPolicyDefinition.isCreateAccountWithSpecificPassword());
		chkResetPasswordWithSpecificPassword.setValue(passwordPolicyDefinition.isResetPasswordWithSpecificPassword());
		txtRandomPasswordIncludeSigns.setValue(passwordPolicyDefinition.getRandomPasswordIncludeSigns());
		txtRandomPasswordExcludeChars.setValue(passwordPolicyDefinition.getRandomPasswordExcludeChars());
		txtRandomPasswordLength.setValue(passwordPolicyDefinition.getRandomPasswordLength());

		changeUsePolicy(usePolicy);
	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		boolean usePolicy = SmartGWTUtil.getBooleanValue(chkUsePolicy);

		PasswordPolicyDefinition passwordPolicyDefinition = null;
		if (usePolicy) {
			passwordPolicyDefinition = new PasswordPolicyDefinition();

			if (SmartGWTUtil.getIntegerValue(txtMaximumPasswordAge) != null) {
				passwordPolicyDefinition.setMaximumPasswordAge(SmartGWTUtil.getIntegerValue(txtMaximumPasswordAge));
			}

			if (SmartGWTUtil.getIntegerValue(txtMinimumPasswordAge) != null) {
				passwordPolicyDefinition.setMinimumPasswordAge(SmartGWTUtil.getIntegerValue(txtMinimumPasswordAge));
			}
			passwordPolicyDefinition.setPasswordPattern(SmartGWTUtil.getStringValue(txtPasswordPattern, true));
			passwordPolicyDefinition.setDenyTheSamePasswordAsAccountId(SmartGWTUtil.getBooleanValue(chkDenyTheSamePasswordAsAccountId));
			passwordPolicyDefinition.setDenyList(SmartGWTUtil.getStringValue(txtDenyList));
			passwordPolicyDefinition.setPasswordPatternErrorMessage(SmartGWTUtil.getStringValue(txtPasswordPatternErrorMessage, true));
			passwordPolicyDefinition.setLocalizedPasswordPatternErrorMessageList(localizedPasswordPatternErrorMessageList);

			if (SmartGWTUtil.getIntegerValue(txtPasswordHistoryCount) != null) {
				passwordPolicyDefinition.setPasswordHistoryCount(SmartGWTUtil.getIntegerValue(txtPasswordHistoryCount));
			}
			passwordPolicyDefinition.setCreateAccountWithSpecificPassword(SmartGWTUtil.getBooleanValue(chkCreateAccountWithSpecificPassword));
			passwordPolicyDefinition.setResetPasswordWithSpecificPassword(SmartGWTUtil.getBooleanValue(chkResetPasswordWithSpecificPassword));
			passwordPolicyDefinition.setRandomPasswordIncludeSigns(SmartGWTUtil.getStringValue(txtRandomPasswordIncludeSigns, true));
			passwordPolicyDefinition.setRandomPasswordExcludeChars(SmartGWTUtil.getStringValue(txtRandomPasswordExcludeChars, true));

			if (SmartGWTUtil.getIntegerValue(txtRandomPasswordLength) != null) {
				passwordPolicyDefinition.setRandomPasswordLength(SmartGWTUtil.getIntegerValue(txtRandomPasswordLength));
			}
		}

		definition.setPasswordPolicy(passwordPolicyDefinition);

		return definition;
	}

	@Override
	public boolean validate() {
		boolean usePolicy = SmartGWTUtil.getBooleanValue(chkUsePolicy);
		if (usePolicy) {
			return form.validate();
		} else {
			return true;
		}
	}

	@Override
	public void clearErrors() {
		form.clearErrors(true);
	}

	private void changeUsePolicy(boolean usePolicy) {
		boolean disabled = !usePolicy;
		txtMaximumPasswordAge.setDisabled(disabled);
		txtMinimumPasswordAge.setDisabled(disabled);
		txtPasswordPattern.setDisabled(disabled);
		chkDenyTheSamePasswordAsAccountId.setDisabled(disabled);
		txtDenyList.setDisabled(disabled);
		txtPasswordPatternErrorMessage.setDisabled(disabled);
		langBtn.setDisabled(disabled);
		txtPasswordHistoryCount.setDisabled(disabled);
		chkCreateAccountWithSpecificPassword.setDisabled(disabled);
		chkResetPasswordWithSpecificPassword.setDisabled(disabled);
		txtRandomPasswordIncludeSigns.setDisabled(disabled);
		txtRandomPasswordExcludeChars.setDisabled(disabled);
		txtRandomPasswordLength.setDisabled(disabled);
	}

}
