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

package org.iplass.adminconsole.client.metadata.ui.entity.property.common;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributePane.PropertyCommonAttributePaneHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.type.PropertyTypeAttributeController;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

public class CommonAttributeParts1 extends PropertyCommonAttributeParts {

	private DynamicForm form;

	/** 名前 */
	private TextItem txtName;

	/** 表示名称 */
	private MetaDataLangTextItem txtDisplayName;

	/** 型 */
	private SelectItem selType;

	/** 多重度 */
	private TextItem txtMultipl;
	private MultipleValidator multipleValidator;
	private MultipleValidator multipleReferenceValidator;

	private PropertyCommonAttributePane owner;

	private PropertyTypeAttributeController typeController = GWT.create(PropertyTypeAttributeController.class);

	public CommonAttributeParts1() {

		txtName = new MtpTextItem();
		txtName.setTitle("Name");

		//名前のPolicyをカスタマイズ
		RegExpValidator nameValidator = new RegExpValidator();
		nameValidator.setExpression(MetaDataConstants.ENTITY_PROPERTY_NAME_REG_EXP_PATH_PERIOD);
		nameValidator.setErrorMessage(rs("ui_metadata_entity_PropertyEditDialog_nameErr"));
		txtName.setValidators(nameValidator);
		SmartGWTUtil.setRequired(txtName);

		txtDisplayName = new MetaDataLangTextItem();
		txtDisplayName.setTitle("Display Name");

		selType = new MtpSelectItem();
		selType.setTitle("Type");
		SmartGWTUtil.setRequired(selType);
		selType.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				typeChanged(getType());
			}
		});

		txtMultipl = new MtpTextItem();
		txtMultipl.setTitle("Multiple");
		txtMultipl.setKeyPressFilter(KEYFILTER_NUM);
		multipleValidator = new MultipleValidator(false);
		multipleReferenceValidator = new MultipleValidator(true);
		txtMultipl.setValidators(multipleValidator);

		form = new MtpForm2Column();
		form.setItems(txtName, txtDisplayName, selType, txtMultipl);

		addMember(form);
	}

	@Override
	public void initialize(PropertyCommonAttributePane owner, PropertyCommonAttributePaneHandler handler) {
		this.owner = owner;

		LinkedHashMap<String, String> typeMap = new LinkedHashMap<>();
		for (PropertyDefinitionType type : PropertyDefinitionType.values()) {
			typeMap.put(type.name(), typeController.getTypeDisplayName(type));
		}
		selType.setValueMap(typeMap);
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		CommonAttribute commonAttribute = (CommonAttribute)typeAttribute;

		txtName.setValue(commonAttribute.getName());
		txtDisplayName.setValue(commonAttribute.getDisplayName());
		txtDisplayName.setLocalizedList(commonAttribute.getLocalizedDisplayNameList());
		if (commonAttribute.getType() != null) {
			selType.setValue(commonAttribute.getType().name());
			typeControl(commonAttribute.getType());
		}
		txtMultipl.setValue(commonAttribute.getMultipleStringValue());
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		CommonAttribute commonAttribute = (CommonAttribute)record.getCommonAttribute();

		if (txtName.getValue() != null) {
			commonAttribute.setName(SmartGWTUtil.getStringValue(txtName));
		}
		if (txtDisplayName.getValue() != null) {
			commonAttribute.setDisplayName(SmartGWTUtil.getStringValue(txtDisplayName));
		}
		commonAttribute.setLocalizedDisplayNameList(txtDisplayName.getLocalizedList());

		commonAttribute.setType(PropertyDefinitionType.valueOf(SmartGWTUtil.getStringValue(selType)));

		commonAttribute.setMultipleStringValue(SmartGWTUtil.getStringValue(txtMultipl));
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public int panelHeight() {
		return 60;
	}

	private PropertyDefinitionType getType() {
		PropertyDefinitionType type = null;
		if (SmartGWTUtil.isNotEmpty(SmartGWTUtil.getStringValue(selType))) {
			type = PropertyDefinitionType.valueOf(SmartGWTUtil.getStringValue(selType));
		}
		return type;
	}

	private void typeChanged(PropertyDefinitionType type) {

		typeControl(type);

		switch (type) {
		case REFERENCE:
			break;
		default:
			txtMultipl.setValue(1);
		}

		owner.onTypeChanged(type);
	}

	private void typeControl(PropertyDefinitionType type) {

		txtMultipl.setDisabled(false);
		txtMultipl.setKeyPressFilter(KEYFILTER_NUM);
		txtMultipl.setValidators(multipleValidator);

		switch (type) {
		case REFERENCE:
			txtMultipl.setKeyPressFilter(KEYFILTER_NUMANDASTER);
			txtMultipl.setValidators(multipleReferenceValidator);
			break;
		case EXPRESSION:
			txtMultipl.setDisabled(true);
			break;
		case AUTONUMBER:
			txtMultipl.setDisabled(true);
			break;
		default:
		}
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}

	private class MultipleValidator extends CustomValidator {

		private boolean canAster = false;

		public MultipleValidator(boolean canAster) {
			this.canAster = canAster;

			if (canAster) {
				setErrorMessage(rs("ui_metadata_entity_PropertyEditDialog_multipleAsterErr"));
			} else {
				setErrorMessage(rs("ui_metadata_entity_PropertyEditDialog_multipleErr"));
			}
		}

		@Override
		protected boolean condition(Object value) {
			if (value == null) {
				return true;
			}
			String strValue = value.toString();
			if (canAster) {
				if (strValue.equals(CommonAttribute.MULTIPLE_ASTER_STR_VALUE)) {
					return true;
				}
				//アスタリスクを含む不正な入力のチェック
				//アスタリスクを含む場合はアスタリスクのみ可
				if (strValue.contains(CommonAttribute.MULTIPLE_ASTER_STR_VALUE)
						&& !strValue.equals(CommonAttribute.MULTIPLE_ASTER_STR_VALUE)) {
					return false;
				}
			}

			try {
				Integer intValue = Integer.parseInt(strValue);
				if (intValue <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}

			return true;
		}

	}
}
