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
import org.iplass.mtp.auth.policy.definition.AccountLockoutPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;

public class AccountLockoutPolicySettingPane extends AbstractSettingPane {

	private IntegerItem txtlockoutFailureCountField;
	private IntegerItem txtlockoutDurationField;
	private IntegerItem txtlockoutFailureExpirationIntervalField;
	private DynamicForm defaultForm;

	public AccountLockoutPolicySettingPane() {

		defaultForm = new DynamicForm();
		defaultForm.setGroupTitle("Account Lockout Policy Setting");
		defaultForm.setIsGroup(true);

		defaultForm.setAlign(Alignment.LEFT);
		defaultForm.setNumCols(6);
		defaultForm.setPadding(5);

		txtlockoutFailureCountField = new IntegerItem();
		txtlockoutFailureCountField.setTitle("Lockout Failure Count");
		txtlockoutFailureCountField.setWidth(100);
		IntegerRangeValidator validator = new IntegerRangeValidator();
		validator.setMin(0);
		validator.setMax(99);
		validator.setErrorMessage("Input range is from 0 to 99.");
		txtlockoutFailureCountField.setValidators(validator);

		txtlockoutDurationField = new IntegerItem();
		txtlockoutDurationField.setTitle("Lockout Duration(min)");
		txtlockoutDurationField.setWidth(100);

		txtlockoutFailureExpirationIntervalField = new IntegerItem();
		txtlockoutFailureExpirationIntervalField.setTitle("Lockout Failure Expiration Interval(min)");
		txtlockoutFailureExpirationIntervalField.setWidth(100);

		defaultForm.setItems(txtlockoutFailureCountField, txtlockoutDurationField, txtlockoutFailureExpirationIntervalField);

		addMember(defaultForm);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		AccountLockoutPolicyDefinition accountLockoutPolicyDefinition = definition.getAccountLockoutPolicy();
		if (accountLockoutPolicyDefinition != null) {
			txtlockoutFailureCountField.setValue(accountLockoutPolicyDefinition.getLockoutFailureCount());
			txtlockoutDurationField.setValue(accountLockoutPolicyDefinition.getLockoutDuration());
			txtlockoutFailureExpirationIntervalField.setValue(accountLockoutPolicyDefinition.getLockoutFailureExpirationInterval());
		}
	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		AccountLockoutPolicyDefinition accountLockoutPolicyDefinition = new AccountLockoutPolicyDefinition();

		if (SmartGWTUtil.getIntegerValue(txtlockoutDurationField) != null) {
			accountLockoutPolicyDefinition.setLockoutDuration(SmartGWTUtil.getIntegerValue(txtlockoutDurationField));
		}

		if (SmartGWTUtil.getIntegerValue(txtlockoutFailureCountField) != null) {
			accountLockoutPolicyDefinition.setLockoutFailureCount(SmartGWTUtil.getIntegerValue(txtlockoutFailureCountField));
		}

		if (SmartGWTUtil.getIntegerValue(txtlockoutFailureExpirationIntervalField) != null) {
			accountLockoutPolicyDefinition.setLockoutFailureExpirationInterval(SmartGWTUtil.getIntegerValue(txtlockoutFailureExpirationIntervalField));
		}

		definition.setAccountLockoutPolicy(accountLockoutPolicyDefinition);

		return definition;
	}

	@Override
	public boolean validate() {
		return defaultForm.validate();
	}

}
