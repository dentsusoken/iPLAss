/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;

import javax.el.ELContext;
import javax.el.TypeConverter;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.util.DateUtil;

public class SqlDateConverter extends TypeConverter {
	
	@Override
	public Object convertToType(ELContext context, Object obj, Class<?> targetType) {
		if (Date.class == targetType) {
			if (obj instanceof String) {
				context.setPropertyResolved(true);
				return conv(obj);
			}
		}
		return null;
	}
	
	private Date toDate(String exp, DateFormat sdf) {
		try {
			sdf.setLenient(false);
			return new Date(sdf.parse(exp).getTime());
		} catch (ParseException e) {
		}
		return null;
	}
	
	Date conv(Object obj) {
		if (obj instanceof String) {
			String exp = ((String) obj).trim();
			if ("".equals(exp)) {
				return null;
			}
			
			ExecuteContext ec = ExecuteContext.getCurrentContext();
			LocaleFormat lf = ec.getLocaleFormat();
			
			//try some pattern
			Date d = toDate(exp, DateUtil.getSimpleDateFormat(lf.getServerDateFormat(), false));
			if (d == null) {
				d = toDate(exp, DateUtil.getDateInstance(DateFormat.MEDIUM, false));
			}
			if (d == null) {
				d = toDate(exp, DateUtil.getSimpleDateFormat("yyyy-MM-dd", false));
			}
			if (d == null) {
				d = toDate(exp, DateUtil.getSimpleDateFormat("yyyyMMdd", false));
			}
			if (d == null) {
				d = toDate(exp, DateUtil.getSimpleDateFormat(lf.getOutputDateFormat(), false));
			}
			if (d == null) {
				d = toDate(exp, DateUtil.getSimpleDateFormat(lf.getExcelDateFormat(), false));
			}
			
			if (d == null) {
				throw new IllegalArgumentException("Can't convert to Date:" + obj);
			}
			
			return d;
		}
		return null;
	}
}
