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

package org.iplass.mtp.impl.view.generic.common;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.HasEntityProperty;
import org.iplass.mtp.view.generic.common.AutocompletionProperty;

public class MetaAutocompletionProperty implements MetaData, HasEntityProperty {

	private static final long serialVersionUID = 8665280705434279354L;

	/** プロパティID */
	private String propertyId;

	/** ネストプロパティか */
	private boolean nestProperty;

	/** 参照プロパティのインデックス */
	private Integer referencePropertyIndex;

	/**
	 * @return propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId セットする propertyId
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * @return nestProperty
	 */
	public boolean isNestProperty() {
		return nestProperty;
	}

	/**
	 * @param nestProperty セットする nestProperty
	 */
	public void setNestProperty(boolean nestProperty) {
		this.nestProperty = nestProperty;
	}

	/**
	 * @return referencePropertyIndex
	 */
	public Integer getReferencePropertyIndex() {
		return referencePropertyIndex;
	}

	/**
	 * @param referencePropertyIndex セットする referencePropertyIndex
	 */
	public void setReferencePropertyIndex(Integer referencePropertyIndex) {
		this.referencePropertyIndex = referencePropertyIndex;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(AutocompletionProperty property, EntityHandler refEntity, EntityHandler rootEntity) {
		String _id = null;
		if (rootEntity == null || property.isNestProperty()) {
			_id = convertId(property.getPropertyName(), EntityContext.getCurrentContext(), refEntity);
		} else {
			_id = convertId(property.getPropertyName(), EntityContext.getCurrentContext(), rootEntity);
		}
		if (_id == null) return;

		propertyId = _id;
		nestProperty = property.isNestProperty();
		referencePropertyIndex = property.getReferencePropertyIndex();
	}

	public AutocompletionProperty currentConfig(EntityHandler refEntity, EntityHandler rootEntity) {
		String _name = null;
		if (rootEntity == null || nestProperty) {
			_name = convertName(propertyId, EntityContext.getCurrentContext(), refEntity);
		} else {
			_name = convertName(propertyId, EntityContext.getCurrentContext(), rootEntity);
		}
		if (_name == null) return null;

		AutocompletionProperty property = new AutocompletionProperty();
		property.setPropertyName(_name);
		property.setNestProperty(nestProperty);
		property.setReferencePropertyIndex(referencePropertyIndex);
		return property;
	}

}
