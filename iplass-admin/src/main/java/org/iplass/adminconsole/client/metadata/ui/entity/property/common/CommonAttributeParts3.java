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

package org.iplass.adminconsole.client.metadata.ui.entity.property.common;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributePane.PropertyCommonAttributePaneHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributeParts.TypeChangeHandler;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class CommonAttributeParts3 extends PropertyCommonAttributeParts implements TypeChangeHandler {

	private DynamicForm form;

	/** インデックスタイプ */
	private SelectItem selIndexType;

	/** StoreDefinition マッピングカラム名 */
	private TextItem txtStoreColName;

	public CommonAttributeParts3() {

		selIndexType = new MtpSelectItem();
		selIndexType.setTitle("Index Type");

		txtStoreColName = new MtpTextItem();
		txtStoreColName.setTitle("Store Col Name");

		form = new MtpForm2Column();
		form.setItems(selIndexType, txtStoreColName);

		addMember(form);
	}

	@Override
	public void initialize(PropertyCommonAttributePane owner, PropertyCommonAttributePaneHandler handler) {

		LinkedHashMap<String, String> indexTypeMap = new LinkedHashMap<String, String>();
		indexTypeMap.put(IndexType.NON_INDEXED.name(), "None");
		indexTypeMap.put(IndexType.NON_UNIQUE.name(), "Index");
		indexTypeMap.put(IndexType.UNIQUE.name(), "Unique Index");
		indexTypeMap.put(IndexType.UNIQUE_WITHOUT_NULL.name(), "Unique Index(without null)");
		selIndexType.setValueMap(indexTypeMap);
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		CommonAttribute commonAttribute = (CommonAttribute)typeAttribute;

		if(commonAttribute.getIndexType() != null) {
			selIndexType.setValue(commonAttribute.getIndexType().name());
		}
		if (commonAttribute.getStoreColumnMappingName() != null) {
			txtStoreColName.setValue(commonAttribute.getStoreColumnMappingName());
		}

		if (commonAttribute.getType() != null) {
			typeControl(commonAttribute.getType());
		}
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		CommonAttribute commonAttribute = (CommonAttribute)record.getCommonAttribute();

		if (selIndexType.getValue() != null) {
			commonAttribute.setIndexType(IndexType.valueOf(SmartGWTUtil.getStringValue(selIndexType)));
		}
		if (SmartGWTUtil.isEmpty(SmartGWTUtil.getStringValue(txtStoreColName))) {
			commonAttribute.setStoreColumnMappingName(null);
		} else {
			commonAttribute.setStoreColumnMappingName(SmartGWTUtil.getStringValue(txtStoreColName));
		}
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public int panelHeight() {
		return 40;
	}

	@Override
	public void onTypeChanged(PropertyDefinitionType type) {

		typeControl(type);

		switch (type) {
		case EXPRESSION:
		case REFERENCE:
		case BINARY:
		case LONGTEXT:
			selIndexType.setValue(IndexType.NON_INDEXED.name());
			txtStoreColName.clearValue();
			break;
		default:
		}
	}

	private void typeControl(PropertyDefinitionType type) {

		selIndexType.setDisabled(false);
		txtStoreColName.setDisabled(false);

		switch (type) {
		case EXPRESSION:
		case REFERENCE:
		case BINARY:
		case LONGTEXT:
			selIndexType.setDisabled(true);
			txtStoreColName.setDisabled(true);
			break;
		default:
		}
	}

}
