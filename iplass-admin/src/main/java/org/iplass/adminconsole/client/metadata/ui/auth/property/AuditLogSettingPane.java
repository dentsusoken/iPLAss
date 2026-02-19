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
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;

import com.smartgwt.client.widgets.form.fields.CheckboxItem;

public class AuditLogSettingPane extends AbstractSettingPane {

	private CheckboxItem chkRecordLastLoginDate;

	public AuditLogSettingPane() {

		form.setGroupTitle("Audit Log Setting");

		chkRecordLastLoginDate = new CheckboxItem();
		chkRecordLastLoginDate.setShowTitle(false);
		chkRecordLastLoginDate.setTitle(AdminClientMessageUtil.getString("ui_metadata_auth_property_AuditLogSettingPane_recordLastLoginDate"));
		chkRecordLastLoginDate.setColSpan(2);

		form.setItems(chkRecordLastLoginDate);

		addMember(form);
	}

	@Override
	public void setDefinition(AuthenticationPolicyDefinition definition) {
		chkRecordLastLoginDate.setValue(definition.isRecordLastLoginDate());
	}

	@Override
	public AuthenticationPolicyDefinition getEditDefinition(AuthenticationPolicyDefinition definition) {
		definition.setRecordLastLoginDate(SmartGWTUtil.getBooleanValue(chkRecordLastLoginDate));
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
