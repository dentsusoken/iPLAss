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

import java.lang.reflect.Array;

import jakarta.el.ELContext;
import jakarta.el.TypeConverter;

public class ArrayTypeConverter extends TypeConverter {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convertToType(ELContext context, Object obj, Class<T> targetType) {
		if (targetType.isArray() && obj instanceof Object[]) {
			if (targetType.isAssignableFrom(obj.getClass())) {
				context.setPropertyResolved(true);
				return (T) obj;
			}

			Object[] src = (Object[]) obj;
			Object converted = Array.newInstance(targetType.getComponentType(), src.length);
			for (int i = 0; i < src.length; i++) {
				Array.set(converted, i, context.convertToType(src[i], targetType.getComponentType()));
			}
			context.setPropertyResolved(true);
			return (T) converted;
		}

		if (targetType.isArray() && !(obj instanceof Object[])) {
			Object converted = Array.newInstance(targetType.getComponentType(), 1);
			if (targetType.getComponentType().isAssignableFrom(obj.getClass())) {
				Array.set(converted, 0, obj);
			} else {
				Array.set(converted, 0, context.convertToType(obj, targetType.getComponentType()));
			}
			context.setPropertyResolved(true);
			return (T) converted;
		}

		if (!targetType.isArray() && obj instanceof Object[]) {
			Object indexZero = ((Object[]) obj)[0];
			if (indexZero == null) {
				context.setPropertyResolved(true);
				return null;
			}
			if (targetType.isAssignableFrom(indexZero.getClass())) {
				context.setPropertyResolved(true);
				return (T) indexZero;
			} else {
				context.setPropertyResolved(true);
				return context.convertToType(indexZero, targetType);
			}
		}

		return null;
	}

}
