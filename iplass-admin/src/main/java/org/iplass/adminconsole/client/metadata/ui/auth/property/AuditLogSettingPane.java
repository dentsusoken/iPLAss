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

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

public class AuditLogSettingPane extends AbstractSettingPane {

	private CheckboxItem chkRecordLastLoginDateField;
	private DynamicForm defaultForm;

	public AuditLogSettingPane() {

		defaultForm = new DynamicForm();
		defaultForm.setGroupTitle("Audit Log Setting");
		defaultForm.setIsGroup(true);

		defaultForm.setAlign(Alignment.LEFT);
		defaultForm.setNumCols(4);
		defaultForm.setPadding(5);
		defaultForm.setColWidths(200, 200, "*", "*");

		chkRecordLastLoginDateField = new CheckboxItem();
		chkRecordLastLoginDateField.setShowTitle(false);
		chkRecordLastLoginDateField.setTitle("Record Last LoginDate.");

		defaultForm.setItems(chkRecordLastLoginDateField);

		addMember(defaultForm);
	}

	public void setDefinition(AuthenticationPolicyDefinition definition) {
		chkRecordLastLoginDateField.setValue(definition.isRecordLastLoginDate());
	}

	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {
		definition.setRecordLastLoginDate(SmartGWTUtil.getBooleanValue(chkRecordLastLoginDateField));
		return definition;
	}

	@Override
	public boolean validate() {
		return defaultForm.validate();
	}

}
