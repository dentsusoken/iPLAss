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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem.ItemOption;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class ExpressionAttributePane extends VLayout implements PropertyAttributePane {

	private DynamicForm formResultType;

	/** 結果タイプ */
	private SelectItem selResultType;
	
	/** 結果タイプがSelect時のSelectValue */
	private SelectItem selGlobalSelectValue;

	private DynamicForm formExpression;

	/** 式 */
	private TextAreaItem txtExpression;

	private PropertyTypeAttributeController typeController = GWT.create(PropertyTypeAttributeController.class);

	public ExpressionAttributePane() {

		setWidth100();
		setAutoHeight();

		selResultType = new MtpSelectItem();
		selResultType.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_property_type_ExpressionAttributePane_resultType"));
		selResultType.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				changeExpressionResultType();
			}
		});

		selGlobalSelectValue = new MetaDataSelectItem(SelectValueDefinition.class,
				(new ItemOption(true, false)).tooltip(rs("ui_metadata_entity_PropertyListGrid_expressionGlobalSelectValueComment")));
		selGlobalSelectValue.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_property_type_ExpressionAttributePane_globalValue"));
		SmartGWTUtil.addHoverToFormItem(selGlobalSelectValue, rs("ui_metadata_entity_PropertyListGrid_expressionGlobalSelectValueComment"));

		formResultType = new MtpForm2Column();
		formResultType.setItems(selResultType, selGlobalSelectValue);

		txtExpression = new MtpTextAreaItem();
		txtExpression.setTitle(rs("ui_metadata_entity_PropertyListGrid_expression"));
		txtExpression.setHeight(100);
		txtExpression.setColSpan(3);
		SmartGWTUtil.setRequired(txtExpression);
		SmartGWTUtil.addHoverToFormItem(txtExpression, rs("ui_metadata_entity_PropertyListGrid_expressionItem"));

		formExpression = new MtpForm2Column();
		formExpression.setItems(txtExpression);

		addMember(formResultType);
		addMember(formExpression);

		initialize();
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		ExpressionAttribute expressionAttribute = (ExpressionAttribute)typeAttribute;

		txtExpression.setValue(expressionAttribute.getExpression());
		if (expressionAttribute.getResultType() != null) {
			selResultType.setValue(expressionAttribute.getResultType());
		} else {
			selResultType.setValue("");
		}
		if (expressionAttribute.getGlobalSelectName() != null) {
			selGlobalSelectValue.setValue(expressionAttribute.getGlobalSelectName());
		} else {
			selGlobalSelectValue.setValue("");
		}

		if (PropertyDefinitionType.SELECT == expressionAttribute.getResultType()) {
			selGlobalSelectValue.setVisible(true);
		} else {
			selGlobalSelectValue.setVisible(false);
		}

	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		ExpressionAttribute expressionAttribute = (ExpressionAttribute)record.getTypeAttribute();

		if (txtExpression.getValue() != null) {
			expressionAttribute.setExpression(SmartGWTUtil.getStringValue(txtExpression));
		} else {
			expressionAttribute.setExpression(null);
		}
		PropertyDefinitionType expressionResultType = null;
		if (SmartGWTUtil.isNotEmpty(SmartGWTUtil.getStringValue(selResultType))) {
			expressionResultType = PropertyDefinitionType.valueOf(SmartGWTUtil.getStringValue(selResultType));
			expressionAttribute.setResultType(expressionResultType);
		} else {
			expressionAttribute.setResultType(null);
		}
		if (PropertyDefinitionType.SELECT == expressionResultType) {
			expressionAttribute.setGlobalSelectName(SmartGWTUtil.getStringValue(selGlobalSelectValue));
		} else {
			expressionAttribute.setGlobalSelectName(null);
		}
	}

	@Override
	public boolean validate() {

		boolean isValidate = true;
		//共通Formチェック
		if (!formResultType.validate()) {
			isValidate = false;
		}
		if (!formExpression.validate()) {
			isValidate = false;
		}

		return isValidate;
	}

	@Override
	public int panelHeight() {
		return 160;
	}

	private void initialize() {

		LinkedHashMap<String, String> resultTypeMap = new LinkedHashMap<String, String>();
		resultTypeMap.put("", "");
		for (PropertyDefinitionType type : typeController.getExpressionResultTypes()) {
			resultTypeMap.put(type.name(), typeController.getTypeDisplayName(type));
		}
		selResultType.setValueMap(resultTypeMap);
	}

	private void changeExpressionResultType() {

		if (PropertyDefinitionType.SELECT.name().equals(SmartGWTUtil.getStringValue(selResultType))) {
			selGlobalSelectValue.show();
		} else {
			selGlobalSelectValue.hide();
			selGlobalSelectValue.setValue("");
		}
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}
}
