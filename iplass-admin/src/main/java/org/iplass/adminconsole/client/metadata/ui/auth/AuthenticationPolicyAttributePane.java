/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.List;

import org.iplass.adminconsole.client.metadata.ui.auth.property.AbstractSettingPane;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

public class AuthenticationPolicyAttributePane extends VLayout {

	private AuthenticationPolicyAttributeController controller = GWT.create(AuthenticationPolicyAttributeController.class);

	private List<AbstractSettingPane> settingPaneList;

	public AuthenticationPolicyAttributePane() {
		initialize();
	}

	public void setDefinition(AuthenticationPolicyDefinition definition) {

		for (AbstractSettingPane settingPane : settingPaneList) {
			settingPane.clearErrors();
			settingPane.setDefinition(definition);
		}
	}

	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {

		for (AbstractSettingPane settingPane : settingPaneList) {
			definition = settingPane.getEditDefinition(definition);
		}
		return definition;
	}

	private void initialize() {
		setWidth100();

		LayoutSpacer spacer = new LayoutSpacer();
		spacer.setWidth100();
		spacer.setHeight(5);

		addMember(spacer);

		settingPaneList = controller.settingPaneList();

		for (AbstractSettingPane settingPane : settingPaneList) {
			addMember(settingPane);
		}
	}

	public boolean validate() {

		boolean ret = true;
		for (AbstractSettingPane settingPane : settingPaneList) {
			ret = ret & settingPane.validate();
		}
		return ret;
	}

}
