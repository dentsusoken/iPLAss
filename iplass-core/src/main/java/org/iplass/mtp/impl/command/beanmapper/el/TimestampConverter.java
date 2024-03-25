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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import jakarta.el.ELContext;
import jakarta.el.TypeConverter;

import org.apache.commons.lang3.time.DateUtils;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.util.DateUtil;

public class TimestampConverter extends TypeConverter {
	
	private SqlDateConverter dateConverter;
	
	public TimestampConverter() {
		super();
		dateConverter = new SqlDateConverter();
	}
	
	@Override
	public Object convertToType(ELContext context, Object obj, Class<?> targetType) {
		if (Timestamp.class == targetType) {
			if (obj instanceof String) {
				context.setPropertyResolved(true);
				Date ts = conv(obj);
				if (ts == null) {
					return null;
				} else {
					return new Timestamp(ts.getTime());
				}
			}
		}
		if (Date.class == targetType) {
			if (obj instanceof String) {
				context.setPropertyResolved(true);
				return conv(obj);
			}
		}
		return null;
	}
	
	private Date toTs(String exp, DateFormat sdf) {
		try {
			sdf.setLenient(false);
			return sdf.parse(exp);
		} catch (ParseException e) {
		}
		return null;
	}

	private Date conv(Object obj) {
		if (obj instanceof String) {
			String exp = ((String) obj).trim();
			if ("".equals(exp)) {
				return null;
			}
			
			//try some pattern
			Date t = null;
			try {
				//ISO-8601 extend
				OffsetDateTime odt = OffsetDateTime.parse(exp);
				t = new Timestamp(odt.toInstant().toEpochMilli());
			} catch (DateTimeParseException e) {
			}
			//ISO-8601 basic
			if (t == null) {
				t = toTs(exp, new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSZ"));
			}
			if (t == null) {
				t = toTs(exp, new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSZ"));
			}
			if (t == null) {
				t = toTs(exp, new SimpleDateFormat("yyyyMMdd'T'HHmmssZ"));
			}
			
			if (t == null) {
				ExecuteContext ec = ExecuteContext.getCurrentContext();
				LocaleFormat lf = ec.getLocaleFormat();
				
				t = toTs(exp, DateUtil.getSimpleDateFormat(lf.getServerDateTimeFormat(), true));
				if (t == null) {
					t = toTs(exp, DateUtil.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, true));
				}
				if (t == null) {
					t = toTs(exp, DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", true));
				}
				if (t == null) {
					t = toTs(exp, DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX", false));
				}
				if (t == null) {
					t = toTs(exp, DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss", true));
				}
				if (t == null) {
					t = toTs(exp, DateUtil.getSimpleDateFormat("yyyyMMddHHmmssSSS", true));
				}
				if (t == null) {
					t = toTs(exp, DateUtil.getSimpleDateFormat("yyyyMMddHHmmss", true));
				}
				if (t == null) {
					t = toTs(exp, DateUtil.getSimpleDateFormat(ExecuteContext.getCurrentContext().getLocaleFormat().getOutputDatetimeSecFormat(), true));
				}
				if (t == null) {
					t = toTs(exp, DateUtil.getSimpleDateFormat(lf.getExcelDateFormat() + " " + lf.getExcelTimeFormat(), true));
				}
				
				//try parse as Date
				if (t == null) {
					Date dt = dateConverter.conv(exp);
					if (dt != null) {
						dt = DateUtils.truncate(dt, Calendar.DAY_OF_MONTH);
						t = new Timestamp(dt.getTime());
					}
				}
			}
			
			if (t == null) {
				throw new IllegalArgumentException("Can't convert to Timestamp:" + obj);
			}
			
			return t;
		}
		
		return null;
	}
}
