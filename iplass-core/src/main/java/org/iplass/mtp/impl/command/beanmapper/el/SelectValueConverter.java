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
package org.iplass.mtp.impl.command.beanmapper.el;

import jakarta.el.ELContext;
import jakarta.el.TypeConverter;

import org.iplass.mtp.entity.SelectValue;

public class SelectValueConverter extends TypeConverter {

	@Override
	public Object convertToType(ELContext context, Object obj, Class<?> targetType) {
		if (SelectValue.class == targetType) {
			if (obj instanceof String) {
				if ("".equals(obj)) {
					return null;
				}
				context.setPropertyResolved(true);
				return new SelectValue(obj.toString());
			}
		}
		return null;
	}

}
