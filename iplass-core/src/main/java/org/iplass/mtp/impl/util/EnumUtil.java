/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnumUtil {

	/**
	 * 指定のEnum値を大文字のEnum値に変換します。
	 * 大文字のEnum値が存在しない場合はパラメータのEnum値がそのまま返ります。
	 * @param <T> Enum型のクラス
	 * @param enm Enum値
	 * @return 大文字のEnum値
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T toUpperCase(Enum<T> enm) {
		if (enm == null) return null;
		try {
			Method method = enm.getDeclaringClass().getMethod("valueOf", new Class<?>[]{Class.class, String.class});
			Object ret = method.invoke(null, new Object[]{enm.getDeclaringClass(), enm.name().toUpperCase()});
			return (T) ret;
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return (T) enm;
	}

}
