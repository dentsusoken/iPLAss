/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.oauth.client;

import org.iplass.mtp.auth.oauth.definition.OAuthClientDefinition;

import com.smartgwt.client.widgets.layout.VLayout;

public class OAuthClientAttributePane extends VLayout {

	public OAuthClientAttributePane() {
		initialize();
	}

	public void setDefinition(OAuthClientDefinition definition) {

	}

	public OAuthClientDefinition getEditDefinition(OAuthClientDefinition definition) {

		return definition;
	}

	private void initialize() {
		setWidth100();

//		LayoutSpacer spacer = new LayoutSpacer();
//		spacer.setWidth100();
//		spacer.setHeight(5);
//
//		addMember(spacer);

	}

	public boolean validate() {

		boolean ret = true;
		return ret;
	}

}
