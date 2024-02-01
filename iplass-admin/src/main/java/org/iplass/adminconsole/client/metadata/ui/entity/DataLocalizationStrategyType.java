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

package org.iplass.adminconsole.client.metadata.ui.entity;

import org.iplass.mtp.entity.definition.DataLocalizationStrategy;
import org.iplass.mtp.entity.definition.l10n.EachInstanceDataLocalizationStrategy;
import org.iplass.mtp.entity.definition.l10n.EachPropertyDataLocalizationStrategy;

/**
 * DataLocalizationStrategyの種類
 */
public enum DataLocalizationStrategyType {

	EACHINSTANCE("Each Instance", EachInstanceDataLocalizationStrategy.class),
	EACHPROPERTY("Each Property", EachPropertyDataLocalizationStrategy.class);

	private String displayName;
	private Class<DataLocalizationStrategy> definitionClass;

	//Classに対してClass<DataLocalizationStrategy>を指定するとコンパイルエラーになるため未指定
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private DataLocalizationStrategyType(String displayName, Class definitionClass) {
		this.displayName = displayName;
		this.definitionClass = definitionClass;
	}

	public String displayName() {
		return displayName;
	}

	public Class<DataLocalizationStrategy> definitionClass() {
		return definitionClass;
	}

	public static DataLocalizationStrategyType valueOf(DataLocalizationStrategy definition) {
		for (DataLocalizationStrategyType type : values()) {
			//if (definition.getClass().isAssignableFrom(type.definitionClass)) {
			if (definition.getClass().getName().equals(type.definitionClass().getName())) {
				return type;
			}
		}
		return null;
	}

//	public static DataLocalizationStrategy typeOfDefinition(DataLocalizationStrategyType type) throws InstantiationException, IllegalAccessException {
//		return type.definitionClass().newInstance();
//	}
	public static DataLocalizationStrategy typeOfDefinition(DataLocalizationStrategyType type) {
		if (type.definitionClass().equals(EachInstanceDataLocalizationStrategy.class)) {
			return new EachInstanceDataLocalizationStrategy();
		} else if (type.definitionClass().equals(EachPropertyDataLocalizationStrategy.class)) {
			return new EachPropertyDataLocalizationStrategy();
		}
		return null;
	}

}
