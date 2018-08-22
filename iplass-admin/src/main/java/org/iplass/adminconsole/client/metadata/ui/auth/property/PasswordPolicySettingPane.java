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
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.PasswordPolicyDefinition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;

public class PasswordPolicySettingPane extends AbstractSettingPane {

	private IntegerItem txtMaximumPasswordAgeField;
	private IntegerItem txtMinimumPasswordAgeField;
	private TextItem txtPasswordPatternField;
	private TextItem txtPasswordPatternErrorMessageField;
	private List<LocalizedStringDefinition> localizedPasswordPatternErrorMessageList;
	private ButtonItem langBtn;
	private IntegerItem txtPasswordHistoryCountField;
	private CheckboxItem chkCreateAccountWithSpecificPasswordFiled;
	private CheckboxItem chkResetPasswordWithSpecificPasswordFiled;
	private TextItem txtRandomPasswordIncludeSignsField;
	private TextItem txtRandomPasswordExcludeCharsField;
	private IntegerItem txtRandomPasswordLengthField;
	private DynamicForm defaultForm;

	public PasswordPolicySettingPane() {

		defaultForm = new DynamicForm();
		defaultForm.setGroupTitle("Password Policy Setting");
		defaultForm.setIsGroup(true);

		defaultForm.setAlign(Alignment.LEFT);
		defaultForm.setNumCols(5);
		defaultForm.setWidth100();
		defaultForm.setPadding(5);

		txtMaximumPasswordAgeField = new IntegerItem();
		txtMaximumPasswordAgeField.setTitle("Maximum Password Age(day)");
		txtMaximumPasswordAgeField.setWidth(100);
		txtMaximumPasswordAgeField.setValidators(new IsIntegerValidator());

		txtMinimumPasswordAgeField = new IntegerItem();
		txtMinimumPasswordAgeField.setTitle("Minimum Password Age(day)");
		txtMinimumPasswordAgeField.setWidth(100);

		txtPasswordPatternField = new TextItem();
		txtPasswordPatternField.setTitle("Password Pattern");
		txtPasswordPatternField.setStartRow(true);
		txtPasswordPatternField.setColSpan(3);
		txtPasswordPatternField.setWidth("100%");

		txtPasswordPatternErrorMessageField = new TextItem();
		txtPasswordPatternErrorMessageField.setTitle("Password Pattern Error Message");
		txtPasswordPatternErrorMessageField.setWidth("100%");
		txtPasswordPatternErrorMessageField.setStartRow(true);
		txtPasswordPatternErrorMessageField.setColSpan(3);

		SpacerItem space = new SpacerItem();
		space.setStartRow(true);
		space.setWidth(150);

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
					localizedPasswordPatternErrorMessageList = new ArrayList<LocalizedStringDefinition>();
				}
				LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedPasswordPatternErrorMessageList);
				dialog.show();
			}
		});

		txtRandomPasswordIncludeSignsField = new TextItem();
		txtRandomPasswordIncludeSignsField.setTitle("Random Password Include Signs");
		txtRandomPasswordIncludeSignsField.setStartRow(true);

		txtRandomPasswordExcludeCharsField = new TextItem();
		txtRandomPasswordExcludeCharsField.setTitle("Random Password Exclude Chars");

		txtRandomPasswordLengthField = new IntegerItem();
		txtRandomPasswordLengthField.setTitle("Random Password Length");
		txtRandomPasswordLengthField.setWidth(100);
		txtRandomPasswordLengthField.setStartRow(true);

		txtPasswordHistoryCountField = new IntegerItem();
		txtPasswordHistoryCountField.setTitle("Password History Count");
		txtPasswordHistoryCountField.setWidth(100);

		chkCreateAccountWithSpecificPasswordFiled = new CheckboxItem();
		chkCreateAccountWithSpecificPasswordFiled.setShowTitle(false);
		chkCreateAccountWithSpecificPasswordFiled.setTitle("Create Account With Specific Password.");

		chkResetPasswordWithSpecificPasswordFiled = new CheckboxItem();
		chkResetPasswordWithSpecificPasswordFiled.setShowTitle(false);
		chkResetPasswordWithSpecificPasswordFiled.setTitle("Reset Password With Specific Password.");

		defaultForm.setItems(txtMaximumPasswordAgeField, txtMinimumPasswordAgeField, txtPasswordPatternField,
				txtPasswordPatternErrorMessageField, space, langBtn, txtRandomPasswordIncludeSignsField,
				txtRandomPasswordExcludeCharsField, txtRandomPasswordLengthField,
				txtPasswordHistoryCountField, space, chkCreateAccountWithSpecificPasswordFiled, new SpacerItem(), chkResetPasswordWithSpecificPasswordFiled);

		addMember(defaultForm);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		PasswordPolicyDefinition passwordPolicyDefinition = definition.getPasswordPolicy();
		if (passwordPolicyDefinition != null) {
			txtMaximumPasswordAgeField.setValue(passwordPolicyDefinition.getMaximumPasswordAge());
			txtMinimumPasswordAgeField.setValue(passwordPolicyDefinition.getMinimumPasswordAge());
			txtPasswordPatternField.setValue(passwordPolicyDefinition.getPasswordPattern());
			txtPasswordPatternErrorMessageField.setValue(passwordPolicyDefinition.getPasswordPatternErrorMessage());
			localizedPasswordPatternErrorMessageList = passwordPolicyDefinition.getLocalizedPasswordPatternErrorMessageList();
			txtPasswordHistoryCountField.setValue(passwordPolicyDefinition.getPasswordHistoryCount());
			chkCreateAccountWithSpecificPasswordFiled.setValue(passwordPolicyDefinition.isCreateAccountWithSpecificPassword());
			chkResetPasswordWithSpecificPasswordFiled.setValue(passwordPolicyDefinition.isResetPasswordWithSpecificPassword());
			txtRandomPasswordIncludeSignsField.setValue(passwordPolicyDefinition.getRandomPasswordIncludeSigns());
			txtRandomPasswordExcludeCharsField.setValue(passwordPolicyDefinition.getRandomPasswordExcludeChars());
			txtRandomPasswordLengthField.setValue(passwordPolicyDefinition.getRandomPasswordLength());
		}
	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		PasswordPolicyDefinition passwordPolicyDefinition = new PasswordPolicyDefinition();

		if (SmartGWTUtil.getIntegerValue(txtMaximumPasswordAgeField) != null) {
			passwordPolicyDefinition.setMaximumPasswordAge(SmartGWTUtil.getIntegerValue(txtMaximumPasswordAgeField));
		}

		if (SmartGWTUtil.getIntegerValue(txtMinimumPasswordAgeField) != null) {
			passwordPolicyDefinition.setMinimumPasswordAge(SmartGWTUtil.getIntegerValue(txtMinimumPasswordAgeField));
		}
		passwordPolicyDefinition.setPasswordPattern(SmartGWTUtil.getStringValue(txtPasswordPatternField, true));
		passwordPolicyDefinition.setPasswordPatternErrorMessage(SmartGWTUtil.getStringValue(txtPasswordPatternErrorMessageField, true));
		passwordPolicyDefinition.setLocalizedPasswordPatternErrorMessageList(localizedPasswordPatternErrorMessageList);

		if (SmartGWTUtil.getIntegerValue(txtPasswordHistoryCountField) != null) {
			passwordPolicyDefinition.setPasswordHistoryCount(SmartGWTUtil.getIntegerValue(txtPasswordHistoryCountField));
		}
		passwordPolicyDefinition.setCreateAccountWithSpecificPassword(SmartGWTUtil.getBooleanValue(chkCreateAccountWithSpecificPasswordFiled));
		passwordPolicyDefinition.setResetPasswordWithSpecificPassword(SmartGWTUtil.getBooleanValue(chkResetPasswordWithSpecificPasswordFiled));
		passwordPolicyDefinition.setRandomPasswordIncludeSigns(SmartGWTUtil.getStringValue(txtRandomPasswordIncludeSignsField, true));
		passwordPolicyDefinition.setRandomPasswordExcludeChars(SmartGWTUtil.getStringValue(txtRandomPasswordExcludeCharsField, true));

		if (SmartGWTUtil.getIntegerValue(txtRandomPasswordLengthField) != null) {
			passwordPolicyDefinition.setRandomPasswordLength(SmartGWTUtil.getIntegerValue(txtRandomPasswordLengthField));
		}

		definition.setPasswordPolicy(passwordPolicyDefinition);

		return definition;
	}

	@Override
	public boolean validate() {
		return defaultForm.validate();
	}

}
