/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.upload;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.NotNullValidation;

class CsvUploadUtil {

	public static String getRequiredProperties(EntityDefinition ed) {
		StringBuilder requiredProperties = new StringBuilder();
		for (PropertyDefinition pd : ed.getPropertyList()) {
			if (ed.getNamePropertyName() != null && Entity.NAME.equals(pd.getName())) {
				// Name属性が設定されている場合は"name"プロパティは必須としない
				continue;
			}
			for (ValidationDefinition vd : pd.getValidations()) {
				if (vd instanceof NotNullValidation) {
					if (requiredProperties.length() > 0) {
						requiredProperties.append(", ");
					}
					requiredProperties.append(pd.getName());
				}
			}
		}
		return requiredProperties.toString();
	}
}
