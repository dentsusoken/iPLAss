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

package org.iplass.mtp.entity.definition.properties.selectvalue;

import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.entity.SelectValue;

/**
 * SelectValueDefinitionのManager。
 *
 * @author K.Higuchi
 *
 */
public interface SelectValueDefinitionManager extends TypedDefinitionManager<SelectValueDefinition> {
	
	/**
	 * 値からSelectValueを取得します。
	 *
	 * @param entityName エンティティ名
	 * @param definitionName 定義名
	 * @param value 値
	 * @return SelectValue
	 */
	public SelectValue getSelectValue(String entityName, String propertyName, String value);
	
	/**
	 * 値からSelectValueを取得します。
	 *
	 * @param entityName エンティティ名
	 * @param definitionName 定義名
	 * @param values 値
	 * @return SelectValue
	 */
	public SelectValue[] getSelectValues(String entityName, String propertyName, String[] values);
}
