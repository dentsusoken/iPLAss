/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

/**
 * EntityのProperty情報を保持しているElement定義
 */
public interface HasEntityProperty {

	/**
	 * プロパティIDからプロパティ名を取得します。
	 *
	 * @param propId プロパティID
	 * @param context Entityコンテキスト
	 * @param eh 対象のEntity定義
	 * @return プロパティ名
	 */
	default String convertName(String propId, EntityContext context, EntityHandler eh) {

		//idから復元できない場合→プロパティが消された等が考えられるので、
		//Exceptionを投げずにnullで返しておく
		if (propId != null && propId.contains(".")) {
			int indexOfDot = propId.indexOf(".");
			String objPropId = propId.substring(0, indexOfDot);
			String subPropPath = propId.substring(indexOfDot + 1, propId.length());

			PropertyHandler property = eh.getPropertyById(objPropId, context);
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
			PropertyHandler property = eh.getPropertyById(propId, context);
			if (property == null) {
				return null;
			}
			return property.getName();
		}
	}

	default PropertyDefinition getProperty(String propId, EntityContext context, EntityHandler eh) {

		//idから復元できない場合→プロパティが消された等が考えられるので、
		//Exceptionを投げずにnullで返しておく
		if (propId != null && propId.contains(".")) {
			int indexOfDot = propId.indexOf(".");
			String objPropId = propId.substring(0, indexOfDot);
			String subPropPath = propId.substring(indexOfDot + 1, propId.length());

			PropertyHandler property = eh.getPropertyById(objPropId, context);
			if (!(property instanceof ReferencePropertyHandler)) {
				return null;
			}
			ReferencePropertyHandler refProp = (ReferencePropertyHandler) property;
			EntityHandler refEntity = refProp.getReferenceEntityHandler(context);
			if (refEntity == null) {
				return null;
			}
			//最終階層のみ返す
			PropertyDefinition refProperty = getProperty(subPropPath, context, refEntity);
			if (refProperty == null) {
				return null;
			}
			return refProperty;
		} else {
			PropertyHandler property = eh.getPropertyById(propId, context);
			if (property == null) {
				return null;
			}
			return property.getMetaData().currentConfig(context);
		}
	}

	/**
	 * プロパティ名からプロパティIDを取得します。
	 *
	 * @param propName プロパティ名
	 * @param context Entityコンテキスト
	 * @param entity 対象のEntity定義
	 * @return プロパティID
	 */
	default String convertId(String propName, EntityContext context, EntityHandler entity) {

		if (propName == null || propName.isEmpty()) {
			return null;
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
}
