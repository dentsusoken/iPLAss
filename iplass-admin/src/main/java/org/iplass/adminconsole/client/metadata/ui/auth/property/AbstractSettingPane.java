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

import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.VLayout;

public abstract class AbstractSettingPane extends VLayout implements EditablePane<AuthenticationPolicyDefinition> {

	protected enum FIELD_NAME {
		LISTENER_TYPE,
		EVALUATION_TYPE,
		DISP_NAME,
		VALUE_OBJECT
	}

	protected DynamicForm form;

	public AbstractSettingPane() {
		setWidth100();
		setAutoHeight();

		setMargin(10);
		setPadding(5);

		form = new MtpForm2Column();
		form.setIsGroup(true);
		form.setPadding(5);
	}

}
