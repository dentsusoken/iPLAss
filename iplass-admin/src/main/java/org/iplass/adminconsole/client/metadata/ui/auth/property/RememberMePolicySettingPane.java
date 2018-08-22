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

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.RememberMePolicyDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;

public class RememberMePolicySettingPane  extends AbstractSettingPane {

	private IntegerItem txtLifetimeMinutesField;
	private CheckboxItem chkAbsoluteLifetimeField;
	private DynamicForm defaultForm;

	public RememberMePolicySettingPane() {

		defaultForm = new DynamicForm();
		defaultForm.setGroupTitle("Remember Me Policy Setting");
		defaultForm.setIsGroup(true);

		defaultForm.setAlign(Alignment.LEFT);
		defaultForm.setNumCols(6);
		defaultForm.setPadding(5);

		txtLifetimeMinutesField = new IntegerItem();
		txtLifetimeMinutesField.setTitle("Lifetime Minutes");
		txtLifetimeMinutesField.setWidth(100);

		chkAbsoluteLifetimeField = new CheckboxItem();
		chkAbsoluteLifetimeField.setTitle("Absolute Lifetime");
		chkAbsoluteLifetimeField.setWidth(100);

		defaultForm.setItems(txtLifetimeMinutesField, chkAbsoluteLifetimeField);

		addMember(defaultForm);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		RememberMePolicyDefinition rememberMePolicyDefinition = definition.getRememberMePolicy();
		if (rememberMePolicyDefinition != null) {
			txtLifetimeMinutesField.setValue(rememberMePolicyDefinition.getLifetimeMinutes());
			chkAbsoluteLifetimeField.setValue(rememberMePolicyDefinition.isAbsoluteLifetime());
		}
	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		RememberMePolicyDefinition rememberMePolicyDefinition = new RememberMePolicyDefinition();

		if (SmartGWTUtil.getIntegerValue(txtLifetimeMinutesField) != null) {
			rememberMePolicyDefinition.setLifetimeMinutes(SmartGWTUtil.getIntegerValue(txtLifetimeMinutesField));
		}

		rememberMePolicyDefinition.setAbsoluteLifetime(SmartGWTUtil.getBooleanValue(chkAbsoluteLifetimeField));

		definition.setRememberMePolicy(rememberMePolicyDefinition);

		return definition;
	}

	@Override
	public boolean validate() {
		return defaultForm.validate();
	}

}
