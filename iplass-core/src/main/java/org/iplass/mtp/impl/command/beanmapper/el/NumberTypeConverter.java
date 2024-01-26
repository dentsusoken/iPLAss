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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormatSymbols;

import javax.el.ELContext;
import javax.el.TypeConverter;

import org.iplass.mtp.impl.core.ExecuteContext;

/**
 * カンマが入っていた場合でもパースするNumberTypeConverter。
 * 
 * @author K.Higuchi
 *
 */
public class NumberTypeConverter extends TypeConverter {
	
	@Override
	public Object convertToType(ELContext context, Object obj, Class<?> targetType) {
		if (obj instanceof String && isNumber(targetType)) {
			if ("".equals(obj)) {
				return null;
			}
			context.setPropertyResolved(true);
			try {
				return toNumber((String) obj, targetType);
			} catch (RuntimeException e) {
				DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(ExecuteContext.getCurrentContext().getLocale());
				if (hasCamma((String) obj, dfs)) {
					try {
						return toNumber(trimCamma((String) obj, dfs), targetType);
					} catch(Exception ee) {
						e.addSuppressed(ee);
					}
				}
				
				throw e;
			}
		}
		
		return null;
	}
	
	private boolean isNumber(Class<?> type) {
		return type == Long.TYPE || type == Integer.TYPE || type == Double.TYPE || Number.class.isAssignableFrom(type)
				|| type == Short.TYPE || type == Byte.TYPE || type == Float.TYPE;
	}
	
	private boolean hasCamma(String val, DecimalFormatSymbols dfs) {
		char camma = dfs.getGroupingSeparator();
		return val.indexOf(camma) != -1;
	}
	
	private String trimCamma(String val, DecimalFormatSymbols dfs) {
		char camma = dfs.getGroupingSeparator();
		StringBuilder sb = new StringBuilder(val.length());
		char c;
		for (int i = 0; i < val.length(); i++) {
			c = val.charAt(i);
			if (c != camma) {
				sb.append(c);
			}
		}
		return sb.toString();
	}
    
	private Number toNumber(String val, final Class<?> type) throws IllegalArgumentException {
		
		if (Long.TYPE == type || Long.class.equals(type)) {
			return Long.valueOf(val);
		}
		if (Integer.TYPE == type || Integer.class.equals(type)) {
			return Integer.valueOf(val);
		}
		if (Double.TYPE == type || Double.class.equals(type)) {
			return Double.valueOf(val);
		}
		if (BigDecimal.class.equals(type)) {
			return new BigDecimal(val);
		}
		if (BigInteger.class.equals(type)) {
			return new BigInteger(val);
		}
		if (Byte.TYPE == type || Byte.class.equals(type)) {
			return Byte.valueOf(val);
		}
		if (Short.TYPE == type || Short.class.equals(type)) {
			return Short.valueOf(val);
		}
		if (Float.TYPE == type || Float.class.equals(type)) {
			return Float.valueOf(val);
		}
		
		throw new IllegalArgumentException("Can not parse to " + type.getName() + ":" +  val);
	}
}
