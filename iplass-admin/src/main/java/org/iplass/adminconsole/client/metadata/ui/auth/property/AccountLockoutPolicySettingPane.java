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

import org.iplass.adminconsole.client.base.ui.widget.form.MtpIntegerItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.policy.definition.AccountLockoutPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;

import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;

public class AccountLockoutPolicySettingPane extends AbstractSettingPane {

	private IntegerItem txtlockoutFailureCount;
	private IntegerItem txtlockoutDuration;
	private IntegerItem txtlockoutFailureExpirationInterval;

	public AccountLockoutPolicySettingPane() {

		form.setGroupTitle("Account Lockout Policy Setting");

		txtlockoutFailureCount = new MtpIntegerItem();
		txtlockoutFailureCount.setTitle("Lockout Failure Count");
		SmartGWTUtil.setRequired(txtlockoutFailureCount);
		IntegerRangeValidator validator = new IntegerRangeValidator();
		validator.setMin(0);
		validator.setMax(99);
		validator.setErrorMessage("Input range is from 0 to 99.");
		txtlockoutFailureCount.setValidators(validator);
		txtlockoutFailureCount.setStartRow(true);

		txtlockoutDuration = new MtpIntegerItem();
		txtlockoutDuration.setTitle("Lockout Duration(min)");
		SmartGWTUtil.setRequired(txtlockoutDuration);

		txtlockoutFailureExpirationInterval = new MtpIntegerItem();
		txtlockoutFailureExpirationInterval.setTitle("Lockout Failure Expiration Interval(min)");
		SmartGWTUtil.setRequired(txtlockoutFailureExpirationInterval);

		form.setItems(
				txtlockoutFailureCount, txtlockoutDuration, txtlockoutFailureExpirationInterval);

		addMember(form);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		AccountLockoutPolicyDefinition accountLockoutPolicyDefinition = definition.getAccountLockoutPolicy();

		txtlockoutFailureCount.setValue(accountLockoutPolicyDefinition.getLockoutFailureCount());
		txtlockoutDuration.setValue(accountLockoutPolicyDefinition.getLockoutDuration());
		txtlockoutFailureExpirationInterval.setValue(accountLockoutPolicyDefinition.getLockoutFailureExpirationInterval());

	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		AccountLockoutPolicyDefinition accountLockoutPolicyDefinition = new AccountLockoutPolicyDefinition();

		accountLockoutPolicyDefinition.setLockoutDuration(SmartGWTUtil.getIntegerValue(txtlockoutDuration));
		accountLockoutPolicyDefinition.setLockoutFailureCount(SmartGWTUtil.getIntegerValue(txtlockoutFailureCount));
		accountLockoutPolicyDefinition.setLockoutFailureExpirationInterval(SmartGWTUtil.getIntegerValue(txtlockoutFailureExpirationInterval));

		definition.setAccountLockoutPolicy(accountLockoutPolicyDefinition);

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
