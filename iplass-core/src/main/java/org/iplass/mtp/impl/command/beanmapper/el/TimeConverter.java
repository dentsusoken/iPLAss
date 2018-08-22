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

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;

import javax.el.ELContext;
import javax.el.TypeConverter;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.util.DateUtil;

public class TimeConverter extends TypeConverter {
	
	@Override
	public Object convertToType(ELContext context, Object obj, Class<?> targetType) {
		if (Time.class == targetType) {
			if (obj instanceof String) {
				context.setPropertyResolved(true);
				return conv(obj);
			}
		}
		return null;
	}
	
	private Time toTime(String exp, DateFormat sdf) {
		try {
			sdf.setLenient(false);
			return new Time(sdf.parse(exp).getTime());
		} catch (ParseException e) {
		}
		return null;
	}
	
	private Time conv(Object obj) {
		if (obj instanceof String) {
			String exp = ((String) obj).trim();
			if ("".equals(exp)) {
				return null;
			}
			
			//try some pattern
			Time t = null;
			try {
				t = Time.valueOf(exp);
			} catch (Exception e) {
			}
			
			if (t == null) {
				ExecuteContext ec = ExecuteContext.getCurrentContext();
				LocaleFormat lf = ec.getLocaleFormat();
				
				t = toTime(exp, DateUtil.getSimpleDateFormat(lf.getServerTimeFormat(), false));
				if (t == null) {
					t = toTime(exp, DateUtil.getTimeInstance(DateFormat.MEDIUM, false));
				}
				if (t == null) {
					t = toTime(exp, DateUtil.getSimpleDateFormat("HH:mm:ss.SSS", false));
				}
				if (t == null) {
					t = toTime(exp, DateUtil.getSimpleDateFormat("HHmmssSSS", false));
				}
				if (t == null) {
					t = toTime(exp, DateUtil.getSimpleDateFormat("HHmmss", false));
				}
				if (t == null) {
					t = toTime(exp, DateUtil.getSimpleDateFormat(ExecuteContext.getCurrentContext().getLocaleFormat().getOutputTimeSecFormat(), false));
				}
				if (t == null) {
					t = toTime(exp, DateUtil.getSimpleDateFormat(lf.getExcelTimeFormat(), true));
				}
			}
			
			if (t == null) {
				throw new IllegalArgumentException("Can't convert to Time:" + obj);
			}
			
			return t;
		}
		return null;
	}}
