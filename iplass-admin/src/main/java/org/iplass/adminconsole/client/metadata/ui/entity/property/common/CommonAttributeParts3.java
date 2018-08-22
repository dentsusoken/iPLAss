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

	private DynamicForm form = new DynamicForm();

	/** インデックスタイプ */
	private SelectItem selIndexType;
	/** StoreDefinition マッピングカラム名 */
	private TextItem txtStoreColName;

	public CommonAttributeParts3() {

		selIndexType = new SelectItem();
		selIndexType.setTitle("Index Type");
		selIndexType.setWidth(200);

		txtStoreColName = new TextItem();
		txtStoreColName.setTitle("Store Col Name");
		txtStoreColName.setWidth(200);

		form.setMargin(5);
		form.setNumCols(7);
		form.setWidth100();
		form.setHeight(30);
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

		selIndexType.setDisabled(false);
		txtStoreColName.setDisabled(false);

		switch (type) {
		case EXPRESSION:
			selIndexType.setDisabled(true);
			selIndexType.setValue(IndexType.NON_INDEXED.name());
			txtStoreColName.setDisabled(true);
			txtStoreColName.clearValue();
			break;
		case REFERENCE:
			selIndexType.setDisabled(true);
			selIndexType.setValue(IndexType.NON_INDEXED.name());
			txtStoreColName.setDisabled(true);
			txtStoreColName.clearValue();
			break;
		case AUTONUMBER:
			break;
		case BINARY:
			selIndexType.setDisabled(true);
			selIndexType.setValue(IndexType.NON_INDEXED.name());
			txtStoreColName.setDisabled(true);
			txtStoreColName.clearValue();
			break;
		case LONGTEXT:
			selIndexType.setDisabled(true);
			selIndexType.setValue(IndexType.NON_INDEXED.name());
			txtStoreColName.setDisabled(true);
			txtStoreColName.clearValue();
			break;
		default:
		}
	}

}
