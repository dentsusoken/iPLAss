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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributePane.PropertyCommonAttributePaneHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.type.PropertyTypeAttributeController;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

public class CommonAttributeParts1 extends PropertyCommonAttributeParts {

	private DynamicForm form = new DynamicForm();

	/** 名前 */
	private TextItem txtName;
	/** 表示名称 */
	private TextItem txtDisplayName;
	/** 表示名称(多言語値) */
	public List<LocalizedStringDefinition> localizedDisplayNameList;
	/** 型 */
	private SelectItem selType;

	private PropertyCommonAttributePane owner;
	private boolean readOnly;

	private PropertyTypeAttributeController typeController = GWT.create(PropertyTypeAttributeController.class);

	public CommonAttributeParts1() {

		txtName = new TextItem();
		txtName.setTitle("Name");
		txtName.setWidth(200);

		//名前のPolicyをカスタマイズ
		RegExpValidator nameValidator = new RegExpValidator();
		nameValidator.setExpression(MetaDataConstants.ENTITY_PROPERTY_NAME_REG_EXP_PATH_PERIOD);
		nameValidator.setErrorMessage(rs("ui_metadata_entity_PropertyEditDialog_nameErr"));
		txtName.setValidators(nameValidator);
		SmartGWTUtil.setRequired(txtName);

		txtDisplayName = new TextItem();
		txtDisplayName.setTitle("Display Name");
		txtDisplayName.setWidth(200);

		ButtonItem btnLangDisplayName = new ButtonItem();
		btnLangDisplayName.setTitle("");
		btnLangDisplayName.setShowTitle(false);
		btnLangDisplayName.setIcon("world.png");
		btnLangDisplayName.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
		btnLangDisplayName.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
		btnLangDisplayName.setPrompt(rs("ui_metadata_entity_PropertyListGrid_eachLangDspName"));
		btnLangDisplayName.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if (localizedDisplayNameList == null) {
					localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
				}
				LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedDisplayNameList, readOnly);
				dialog.show();
			}
		});

		selType = new SelectItem();
		selType.setTitle("Type");
		selType.setWidth(200);
		SmartGWTUtil.setRequired(selType);
		selType.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				typeChanged(getType());
			}
		});

		form.setMargin(5);
		form.setNumCols(7);
		form.setWidth100();
		form.setHeight(30);
		form.setItems(txtName, txtDisplayName, btnLangDisplayName, selType);

		addMember(form);

	}

	@Override
	public void initialize(PropertyCommonAttributePane owner, PropertyCommonAttributePaneHandler handler) {
		this.owner = owner;

		LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
		for (PropertyDefinitionType type : PropertyDefinitionType.values()) {
			typeMap.put(type.name(), typeController.getTypeDisplayName(type));
		}
		selType.setValueMap(typeMap);
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		CommonAttribute commonAttribute = (CommonAttribute)typeAttribute;

		if (commonAttribute.isInherited()) {
			readOnly = true;
		}

		txtName.setValue(commonAttribute.getName());
		txtDisplayName.setValue(commonAttribute.getDisplayName());
		localizedDisplayNameList = commonAttribute.getLocalizedDisplayNameList();

		if (commonAttribute.getType() != null) {
			selType.setValue(commonAttribute.getType().name());
		}
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
		commonAttribute.setLocalizedDisplayNameList(localizedDisplayNameList);

		commonAttribute.setType(PropertyDefinitionType.valueOf(SmartGWTUtil.getStringValue(selType)));
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public int panelHeight() {
		return 40;
	}

	private PropertyDefinitionType getType() {
		PropertyDefinitionType type = null;
		if (SmartGWTUtil.isNotEmpty(SmartGWTUtil.getStringValue(selType))) {
			type = PropertyDefinitionType.valueOf(SmartGWTUtil.getStringValue(selType));
		}
		return type;
	}

	private void typeChanged(PropertyDefinitionType type) {
		owner.onTypeChanged(type);
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}

}
