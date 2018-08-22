/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.element.section;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.element.section.SortSetting;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.ConditionSortType;

public class MetaSortSetting implements MetaData {

	private static final long serialVersionUID = 5223224828928956674L;

	/** ソート項目 */
	private String sortKey;

	/** ソート種別 */
	private ConditionSortType sortType;

	/** null項目のソート順 */
	private NullOrderType nullOrderType;

	/**
	 * ソート項目を取得します。
	 * @return ソート項目
	 */
	public String getSortKey() {
		return sortKey;
	}

	/**
	 * ソート項目を設定します。
	 * @param sortKey ソート項目
	 */
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	/**
	 * ソート種別を取得します。
	 * @return ソート種別
	 */
	public ConditionSortType getSortType() {
		return sortType;
	}

	/**
	 * ソート種別を設定します。
	 * @param sortType ソート種別
	 */
	public void setSortType(ConditionSortType sortType) {
		this.sortType = sortType;
	}

	/**
	 * null項目のソート順を取得します。
	 * @return null項目のソート順
	 */
	public NullOrderType getNullOrderType() {
		return nullOrderType;
	}

	/**
	 * null項目のソート順を設定します。
	 * @param nullOrderType null項目のソート順
	 */
	public void setNullOrderType(NullOrderType nullOrderType) {
		this.nullOrderType = nullOrderType;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(SortSetting setting, EntityContext context, EntityHandler entity) {
		sortKey = convertId(setting.getSortKey(), context, entity);
		sortType = setting.getSortType();
		nullOrderType = setting.getNullOrderType();
	}

	private String convertId(String propName, EntityContext context, EntityHandler entity) {
		if (propName == null || propName.isEmpty()) {
			throw new IllegalArgumentException("sortKey is invalid:sortKey is not allow set null or empty.");
		}
		if (propName.contains(".")) {
			int indexOfDot = propName.indexOf('.');
			String objPropName = propName.substring(0, indexOfDot);
			String subPropPath = propName.substring(indexOfDot + 1, propName.length());

			PropertyHandler property = entity.getProperty(objPropName, context);
			if (!(property instanceof ReferencePropertyHandler)) {
				throw new IllegalArgumentException("path is invalid:" + objPropName + " is not ObjectReferenceProperty of " + entity.getMetaData().getName());
			}
			ReferencePropertyHandler refProp = (ReferencePropertyHandler) property;
			EntityHandler refEntity = refProp.getReferenceEntityHandler(context);
			if (refEntity == null) {
				throw new IllegalArgumentException(objPropName + "'s Entity is not defined.");
			}
			String refProperty = convertId(subPropPath, context, refEntity);
			if (refProperty == null) {
				throw new IllegalArgumentException(subPropPath + "'s Property is not defined.");
			}
			return property.getId() + "." + refProperty;

		} else {
			PropertyHandler property = entity.getProperty(propName, context);
			if (property == null) {
				throw new IllegalArgumentException(propName + "'s Property is not defined.");
			}
			return property.getId();
		}
	}

	public SortSetting currentConfig(EntityContext context, EntityHandler entity) {
		SortSetting setting = new SortSetting();
		setting.setSortKey(convertName(sortKey, context, entity));
		setting.setSortType(sortType);
		setting.setNullOrderType(nullOrderType);
		return setting;
	}

	private String convertName(String id, EntityContext context, EntityHandler entity) {
		//idから復元できない場合→プロパティが消された等が考えられるので、
		//Exceptionを投げずにnullで返しておく
		if (id != null && id.contains(".")) {
			int indexOfDot = id.indexOf(".");
			String objPropId = id.substring(0, indexOfDot);
			String subPropPath = id.substring(indexOfDot + 1, id.length());

			PropertyHandler property = entity.getPropertyById(objPropId, context);
			if (!(property instanceof ReferencePropertyHandler)) {
				return null;
			}
			ReferencePropertyHandler refProp = (ReferencePropertyHandler) property;
			EntityHandler refEntity = refProp.getReferenceEntityHandler(context);
			if (refEntity == null) {
				return null;
			}
			String refProperty = convertName(subPropPath, context, refEntity);
			if (refProperty == null) {
				return null;
			}
			return property.getName() + "." + refProperty;
		} else {
			PropertyHandler property = entity.getPropertyById(id, context);
			if (property == null) {
				return null;
			}
			return property.getName();
		}
	}
}
