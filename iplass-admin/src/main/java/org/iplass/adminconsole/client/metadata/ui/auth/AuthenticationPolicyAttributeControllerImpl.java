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

package org.iplass.adminconsole.client.metadata.ui.auth;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.metadata.ui.auth.property.AbstractSettingPane;
import org.iplass.adminconsole.client.metadata.ui.auth.property.AccountLockoutPolicySettingPane;
import org.iplass.adminconsole.client.metadata.ui.auth.property.AccountNotificationListenerSettingPane;
import org.iplass.adminconsole.client.metadata.ui.auth.property.AuditLogSettingPane;
import org.iplass.adminconsole.client.metadata.ui.auth.property.AuthenticationProviderSettingPane;
import org.iplass.adminconsole.client.metadata.ui.auth.property.OpenIdConnectPolicySettingPane;
import org.iplass.adminconsole.client.metadata.ui.auth.property.PasswordPolicySettingPane;
import org.iplass.adminconsole.client.metadata.ui.auth.property.RememberMePolicySettingPane;

public class AuthenticationPolicyAttributeControllerImpl implements AuthenticationPolicyAttributeController {

	@Override
	public List<AbstractSettingPane> settingPaneList() {

		List<AbstractSettingPane> settingPaneList = new ArrayList<>();

		settingPaneList.add(new PasswordPolicySettingPane());
		settingPaneList.add(new AccountLockoutPolicySettingPane());
		settingPaneList.add(new RememberMePolicySettingPane());
		settingPaneList.add(new AuditLogSettingPane());
		settingPaneList.add(new AccountNotificationListenerSettingPane());
		settingPaneList.add(new AuthenticationProviderSettingPane());
		settingPaneList.add(new OpenIdConnectPolicySettingPane());

		return settingPaneList;
	}


}
