/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.action;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class RestrictionRequestAttributePane extends VLayout {

	private DynamicForm form;

	private TextAreaItem txaAllowRequestContentTypes;
	private IntegerItem txtMaxRequestBodySize;
	private IntegerItem txtMaxFileSize;

	public RestrictionRequestAttributePane() {

		setWidth100();
		setAutoHeight();
		setMargin(5);
		setMembersMargin(10);

		form = new DynamicForm();
		form.setWidth100();
		form.setPadding(10);
		form.setNumCols(5);
		form.setColWidths(100, 300, 100, 300, "*");
		form.setIsGroup(true);
		form.setGroupTitle("Restriction of Request");

		txaAllowRequestContentTypes = new TextAreaItem();
		txaAllowRequestContentTypes.setTitle("Allow Request Content Types");
		txaAllowRequestContentTypes.setWidth("100%");
		txaAllowRequestContentTypes.setHeight(75);
		txaAllowRequestContentTypes.setBrowserSpellCheck(false);
		txaAllowRequestContentTypes.setColSpan(3);
		txaAllowRequestContentTypes.setStartRow(true);
		txaAllowRequestContentTypes.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_RestrictionRequestAttributePane_allowRequestContentTypes")));

		txtMaxRequestBodySize = new IntegerItem();
		txtMaxRequestBodySize.setTitle("Max Request Body Size");
		txtMaxRequestBodySize.setWidth("100%");
		txtMaxRequestBodySize.setBrowserSpellCheck(false);
		txtMaxRequestBodySize.setStartRow(true);
		txtMaxRequestBodySize.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_RestrictionRequestAttributePane_maxRequestBodySize")));

		txtMaxFileSize = new IntegerItem();
		txtMaxFileSize.setTitle("Max File Size");
		txtMaxFileSize.setWidth("100%");
		txtMaxFileSize.setBrowserSpellCheck(false);
		txtMaxFileSize.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_RestrictionRequestAttributePane_maxFileSize")));

		form.setItems(txaAllowRequestContentTypes, txtMaxRequestBodySize, txtMaxFileSize);

		addMember(form);
	}

	public void setDefinition(ActionMappingDefinition definition) {

		String requestContentTypeText = SmartGWTUtil.convertArrayToString(definition.getAllowRequestContentTypes(), "\n");
		if (requestContentTypeText != null) {
			txaAllowRequestContentTypes.setValue(requestContentTypeText);
		} else {
			txaAllowRequestContentTypes.clearValue();
		}

		if (definition.getMaxRequestBodySize() != null) {
			txtMaxRequestBodySize.setValue(definition.getMaxRequestBodySize());
		} else {
			txtMaxRequestBodySize.clearValue();
		}

		if (definition.getMaxFileSize() != null) {
			txtMaxFileSize.setValue(definition.getMaxFileSize());
		} else {
			txtMaxFileSize.clearValue();
		}
	}

	public ActionMappingDefinition getEditDefinition(ActionMappingDefinition definition) {

		String allowRequestContentTypeText = SmartGWTUtil.getStringValue(txaAllowRequestContentTypes, true);
		definition.setAllowRequestContentTypes(SmartGWTUtil.convertStringToArray(allowRequestContentTypeText));

		definition.setMaxRequestBodySize(SmartGWTUtil.getLongValue(txtMaxRequestBodySize));
		definition.setMaxFileSize(SmartGWTUtil.getLongValue(txtMaxFileSize));

		return definition;
	}

	public boolean validate() {
		return form.validate();
	}

}
