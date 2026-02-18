/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpIntegerItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.RememberMePolicyDefinition;

import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class RememberMePolicySettingPane  extends AbstractSettingPane {

	private CheckboxItem chkUsePolicy;

	private IntegerItem txtLifetimeMinutes;
	private CheckboxItem chkAbsoluteLifetime;

	public RememberMePolicySettingPane() {

		form.setGroupTitle("Remember Me Policy Setting");

		chkUsePolicy = new CheckboxItem();
		chkUsePolicy.setTitle(AdminClientMessageUtil.getString("ui_metadata_auth_RememberMePolicySettingPane_useRememberMePolicy"));
		chkUsePolicy.setShowTitle(false);
		chkUsePolicy.setColSpan(2);
		chkUsePolicy.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				changeUsePolicy(SmartGWTUtil.getBooleanValue(chkUsePolicy));
			}
		});

		txtLifetimeMinutes = new MtpIntegerItem();
		txtLifetimeMinutes.setTitle(AdminClientMessageUtil.getString("ui_metadata_auth_RememberMePolicySettingPane_lifetimeMinutes"));
		txtLifetimeMinutes.setStartRow(true);

		chkAbsoluteLifetime = new CheckboxItem();
		chkAbsoluteLifetime.setTitle(AdminClientMessageUtil.getString("ui_metadata_auth_RememberMePolicySettingPane_absoluteLifetime"));

		form.setItems(chkUsePolicy,
				txtLifetimeMinutes, chkAbsoluteLifetime);

		addMember(form);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		RememberMePolicyDefinition rememberMePolicyDefinition = definition.getRememberMePolicy();

		boolean usePolicy = rememberMePolicyDefinition != null;
		chkUsePolicy.setValue(usePolicy);

		if (!usePolicy) {
			//未設定の場合は、初期値を設定するためnew
			rememberMePolicyDefinition = new RememberMePolicyDefinition();
		}

		txtLifetimeMinutes.setValue(rememberMePolicyDefinition.getLifetimeMinutes());
		chkAbsoluteLifetime.setValue(rememberMePolicyDefinition.isAbsoluteLifetime());

		changeUsePolicy(usePolicy);
	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		boolean usePolicy = SmartGWTUtil.getBooleanValue(chkUsePolicy);

		RememberMePolicyDefinition rememberMePolicyDefinition = null;
		if (usePolicy) {
			rememberMePolicyDefinition = new RememberMePolicyDefinition();

			if (SmartGWTUtil.getIntegerValue(txtLifetimeMinutes) != null) {
				rememberMePolicyDefinition.setLifetimeMinutes(SmartGWTUtil.getIntegerValue(txtLifetimeMinutes));
			}

			rememberMePolicyDefinition.setAbsoluteLifetime(SmartGWTUtil.getBooleanValue(chkAbsoluteLifetime));
		}

		definition.setRememberMePolicy(rememberMePolicyDefinition);

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
		txtLifetimeMinutes.setDisabled(disabled);
		chkAbsoluteLifetime.setDisabled(disabled);
	}

}
