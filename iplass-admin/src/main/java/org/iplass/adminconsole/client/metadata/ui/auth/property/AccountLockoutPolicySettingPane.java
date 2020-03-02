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

import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;

public class AccountLockoutPolicySettingPane extends AbstractSettingPane {

	private CheckboxItem chkUsePolicy;

	private IntegerItem txtlockoutFailureCount;
	private IntegerItem txtlockoutDuration;
	private IntegerItem txtlockoutFailureExpirationInterval;

	public AccountLockoutPolicySettingPane() {

		form.setGroupTitle("Account Lockout Policy Setting");

		chkUsePolicy = new CheckboxItem();
		chkUsePolicy.setTitle("Use Account Lockout Policy");
		chkUsePolicy.setShowTitle(false);
		chkUsePolicy.setColSpan(2);
		chkUsePolicy.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				changeUsePolicy(SmartGWTUtil.getBooleanValue(chkUsePolicy));
			}
		});

		txtlockoutFailureCount = new MtpIntegerItem();
		txtlockoutFailureCount.setTitle("Lockout Failure Count");
		IntegerRangeValidator validator = new IntegerRangeValidator();
		validator.setMin(0);
		validator.setMax(99);
		validator.setErrorMessage("Input range is from 0 to 99.");
		txtlockoutFailureCount.setValidators(validator);
		txtlockoutFailureCount.setStartRow(true);

		txtlockoutDuration = new MtpIntegerItem();
		txtlockoutDuration.setTitle("Lockout Duration(min)");

		txtlockoutFailureExpirationInterval = new MtpIntegerItem();
		txtlockoutFailureExpirationInterval.setTitle("Lockout Failure Expiration Interval(min)");

		form.setItems(chkUsePolicy,
				txtlockoutFailureCount, txtlockoutDuration, txtlockoutFailureExpirationInterval);

		addMember(form);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		AccountLockoutPolicyDefinition accountLockoutPolicyDefinition = definition.getAccountLockoutPolicy();

		boolean usePolicy = accountLockoutPolicyDefinition != null;
		chkUsePolicy.setValue(usePolicy);

		if (!usePolicy) {
			//未設定の場合は、初期値を設定するためnew
			accountLockoutPolicyDefinition = new AccountLockoutPolicyDefinition();
		}

		txtlockoutFailureCount.setValue(accountLockoutPolicyDefinition.getLockoutFailureCount());
		txtlockoutDuration.setValue(accountLockoutPolicyDefinition.getLockoutDuration());
		txtlockoutFailureExpirationInterval.setValue(accountLockoutPolicyDefinition.getLockoutFailureExpirationInterval());

		changeUsePolicy(usePolicy);
	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		boolean usePolicy = SmartGWTUtil.getBooleanValue(chkUsePolicy);

		AccountLockoutPolicyDefinition accountLockoutPolicyDefinition = null;
		if (usePolicy) {
			accountLockoutPolicyDefinition = new AccountLockoutPolicyDefinition();

			if (SmartGWTUtil.getIntegerValue(txtlockoutDuration) != null) {
				accountLockoutPolicyDefinition.setLockoutDuration(SmartGWTUtil.getIntegerValue(txtlockoutDuration));
			}

			if (SmartGWTUtil.getIntegerValue(txtlockoutFailureCount) != null) {
				accountLockoutPolicyDefinition.setLockoutFailureCount(SmartGWTUtil.getIntegerValue(txtlockoutFailureCount));
			}

			if (SmartGWTUtil.getIntegerValue(txtlockoutFailureExpirationInterval) != null) {
				accountLockoutPolicyDefinition.setLockoutFailureExpirationInterval(SmartGWTUtil.getIntegerValue(txtlockoutFailureExpirationInterval));
			}
		}

		definition.setAccountLockoutPolicy(accountLockoutPolicyDefinition);

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
		txtlockoutFailureCount.setDisabled(disabled);
		txtlockoutDuration.setDisabled(disabled);
		txtlockoutFailureExpirationInterval.setDisabled(disabled);
	}

}
