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
import java.util.List;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.type.PropertyTypeAttributeController;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.VersionControlType;

import com.google.gwt.core.shared.GWT;

public class CommonAttribute implements PropertyAttribute {

	/** 多重度が-1の時の画面表示・画面入力用値 */
	private static final String MULTIPLE_ASTER_STR_VALUE = "*";
	/** 多重度の入力値が*の時の保存用値 */
	private static final int MULTIPLE_ASTER_INT_VALUE = -1;
	/** boolean値がtrueの場合の画面表示値 */
	private static final String BOOLEAN_TRUE_DISP_VALUE = "Y";

	private PropertyDefinition attribute;

	private PropertyDefinitionType type;

	private String multipleStrValue;

	private boolean notNull;

	private String storeColumnMappingName;

	private PropertyTypeAttributeController typeController = GWT.create(PropertyTypeAttributeController.class);

	public CommonAttribute() {
		attribute = new PropertyDefinition() {

			private static final long serialVersionUID = 1L;

			@Override
			public PropertyDefinitionType getType() {
				return null;
			}

			@Override
			public Class<?> getJavaType() {
				return null;
			}
		};

		setMultiple(1);
		setIndexType(IndexType.NON_INDEXED);
		setUpdatable(Boolean.TRUE);
		setInherited(Boolean.FALSE);

		setNotNull(Boolean.FALSE);
	}

	@Override
	public void applyFrom(PropertyDefinition property, EntityDefinition entity) {

		setName(property.getName());
		setDisplayName(property.getDisplayName());
		setLocalizedDisplayNameList(property.getLocalizedDisplayNameList());

		setMultiple(property.getMultiplicity());
		setIndexType(property.getIndexType());
		setUpdatable(property.isUpdatable());
		setInherited(property.isInherited());

//		setType(OldPropertyTypeController.valueOfDefinition(property));
		setType(property.getType());
	}

	@Override
	public void applyTo(PropertyDefinition property, EntityDefinition entity) {

		property.setName(getName());
		property.setDisplayName(getDisplayName());
		List<LocalizedStringDefinition> localizedDisplayNameList = getLocalizedDisplayNameList();
		if (SmartGWTUtil.isNotEmpty(localizedDisplayNameList)) {
			property.setLocalizedDisplayNameList(localizedDisplayNameList);
		}

		property.setMultiplicity(getMultiple());
		property.setIndexType(getIndexType());

		VersionControlType versionType = entity.getVersionControlType();
		//UniqueIndexでかつEntityがバージョンコントロールされている場合は、PropertyのcanEditをfalseに設定
		if ((property.getIndexType() == IndexType.UNIQUE
					|| property.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL)
				&& (versionType != null && VersionControlType.NONE != versionType)) {
			property.setUpdatable(false);
		} else {
			property.setUpdatable(isUpdatable());
		}

	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		record.setRecordName(getName());
		record.setRecordDispName(getDisplayName());
		record.setRecordMultipleDispValue(getMultipleStringValue());

		if (isNotNull()) {
			record.setRecordNotNullDispValue(BOOLEAN_TRUE_DISP_VALUE);
		} else {
			record.setRecordNotNullDispValue("");
		}

		if (isUpdatable()) {
			record.setRecordUpdatableDispValue(BOOLEAN_TRUE_DISP_VALUE);
		} else {
			record.setRecordUpdatableDispValue("");
		}

		if (getIndexType() != null) {
			switch (getIndexType()) {
			case NON_UNIQUE:
				record.setRecordIndexTypeDispValue("I");
				break;
			case UNIQUE:
				record.setRecordIndexTypeDispValue("U");
				break;
			case UNIQUE_WITHOUT_NULL:
				record.setRecordIndexTypeDispValue("UN");
				break;
			default:
				record.setRecordIndexTypeDispValue("");
			}
		} else {
			record.setRecordIndexTypeDispValue("");
		}

		record.setRecordTypeDispValue(typeController.getTypeDisplayName(getType()));
		record.setRecordStoreColumnMappingName(getStoreColumnMappingName());
	}

	public String getName() {
		return attribute.getName();
	}

	public void setName(String name) {
		attribute.setName(name);
	}

	public String getDisplayName() {
		return attribute.getDisplayName();
	}

	public void setDisplayName(String displayName) {
		attribute.setDisplayName(displayName);
	}

	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		List<LocalizedStringDefinition> list = attribute.getLocalizedDisplayNameList();
		if (list == null) {
			list = new ArrayList<LocalizedStringDefinition>();
			attribute.setLocalizedDisplayNameList(list);
		}
		return list;
	}

	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		attribute.setLocalizedDisplayNameList(localizedDisplayNameList);
	}

	public int getMultiple() {
		return attribute.getMultiplicity();
	}

	public void setMultiple(int multiplicity) {
		attribute.setMultiplicity(multiplicity);

		//-1を*に変更
		if (multiplicity == MULTIPLE_ASTER_INT_VALUE) {
			multipleStrValue = MULTIPLE_ASTER_STR_VALUE;
		} else {
			multipleStrValue = multiplicity + "";
		}
	}

	public boolean isUpdatable() {
		return attribute.isUpdatable();
	}

	public void setUpdatable(Boolean updatable) {
		attribute.setUpdatable(updatable);
	}

	public IndexType getIndexType() {
		return attribute.getIndexType();
	}

	public void setIndexType(IndexType indexType) {
		attribute.setIndexType(indexType);
	}

	public boolean isInherited() {
		return attribute.isInherited();
	}

	public void setInherited(Boolean inherited) {
		attribute.setInherited(inherited);
	}

	public PropertyDefinitionType getType() {
		return type;
	}

	public void setType(PropertyDefinitionType type) {
		this.type = type;
	}

	public String getMultipleStringValue() {
		return multipleStrValue;
	}

	public void setMultipleStringValue(String multiplicity) {
		if (multiplicity == null) {
			multiplicity = "1";
		}
		multipleStrValue = multiplicity;

		//*を-1に変更
		if (MULTIPLE_ASTER_STR_VALUE.equals(multiplicity)) {
			attribute.setMultiplicity(MULTIPLE_ASTER_INT_VALUE);
		} else {
			attribute.setMultiplicity(Integer.parseInt(multiplicity));
		}
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public String getStoreColumnMappingName() {
		return storeColumnMappingName;
	}

	public void setStoreColumnMappingName(String storeColumnMappingName) {
		this.storeColumnMappingName = storeColumnMappingName;
	}

}
