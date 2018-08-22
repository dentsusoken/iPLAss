/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.lang3.time.DateUtils;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;

public class ConvertUtil {

	private static ConvertUtilsBean cub = new ConvertUtilsBean();

	@SuppressWarnings("unchecked")
	public static <T> T convert(Class<T> type, Object from) {
		if (from == null) {
			return null;
		}

		if (type.isInstance(from)) {
			return (T) from;
		}

		if (from instanceof String) {
			return convertFromString(type, (String) from);
		}

		if (from instanceof SelectValue) {
			from = ((SelectValue) from).getValue();
		}
		
		if (from instanceof byte[] && type == String.class) {
			return (T) convertToString((byte[]) from);
		}

		//可能な限り変換する。
		return (T) cub.convert(from, type);
	}
	
	private static String convertToString(byte[] data) {
		StringBuilder str = new StringBuilder();
		int i = 0;
		for (byte b: data) {
			i = b & 0xFF;
			if (i <= 0xF) {
				str.append('0');
			}
			str.append(Integer.toHexString(i));
		}
		return str.toString();
	}

	/**
	 *
	 * @param date
	 * @param pattern
	 * @return
	 * @throws IllegalArgumentException if pattern is invalid
	 */
	public static String formatDate(java.util.Date date, String pattern, boolean useTenantTimeZone) {
		SimpleDateFormat fdf = DateUtil.getSimpleDateFormat(pattern, useTenantTimeZone);
		return fdf.format(date);
	}



	@SuppressWarnings("unchecked")
	public static <T extends java.util.Date> T convertToDate(Class<T> type, Object from, String pattern, boolean useTenantTimeZone) {
		if (from == null) {
			return null;
		}

		if (type == from.getClass()) {
			return (T) from;
		}

		java.util.Date date = null;
		if (from instanceof java.util.Date) {
			date = (java.util.Date) from;
		} else if (from instanceof String) {
			if (StringUtil.isBlank((String) from)) {
				return null;
			}
			try {
				//TODO ThreaLocalにキャッシュするか？
//				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(pattern, useTenantTimeZone);
				sdf.setLenient(false);
				date = sdf.parse((String) from);
			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			throw new IllegalArgumentException("not support type:" + from);
		}

		if (type == java.sql.Date.class) {
			return (T) new java.sql.Date(date.getTime());
		}
		if (type == java.sql.Timestamp.class) {
			return (T) new java.sql.Timestamp(date.getTime());
		}
		if (type == java.sql.Time.class) {
			//時間だけにしたいので、、
			return (T) Time.valueOf(new java.sql.Time(date.getTime()).toString());
		}

		throw new IllegalArgumentException("not support type:" + type);
	}
	
	public static String convertToString(Object value) {
		if (value == null) {
			return null;
		}
		
		if (value instanceof String) {
			return (String) value;
		}
		
		if (value instanceof Double) {
			return BigDecimal.valueOf((Double) value).toPlainString();
		}
		if (value instanceof Float) {
			return BigDecimal.valueOf((Float) value).toPlainString();
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).toPlainString();
		}
		if (value instanceof Number) {
			return value.toString();
		}
		
		if (value instanceof SelectValue) {
			return ((SelectValue) value).getValue();
		}
		if (value instanceof BinaryReference) {
			return String.valueOf(((BinaryReference) value).getLobId());
		}
		
		if (value instanceof Date) {
			//ISO8601
			SimpleDateFormat format = DateUtil.getSimpleDateFormat("yyyy-MM-dd", false);
			return format.format(value);
		}
		if (value instanceof Time) {
			//ISO8601
			return ((Time) value).toString();
		}
		if (value instanceof Timestamp) {
			//ISO8601
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			return format.format(value);
		}
		if (value instanceof java.util.Date) {
			//ISO8601
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			return format.format(value);
		}
		
		if (value instanceof byte[]) {
			return convertToString((byte[]) value);
		}
		
		//可能な限り変換する。
		return (String) cub.convert(value, String.class);
	}


	@SuppressWarnings("unchecked")
	public static <T> T convertFromString(Class<T> type, String from) {
		if (StringUtil.isBlank(from)) {
			return null;
		}
		if (type == String.class) {
			return (T) from;
		}
		if (type == Long.class) {
			return (T) Long.valueOf(from);
		}
		if (type == Integer.class) {
			return (T) Integer.valueOf(from);
		}
		if (type == Double.class) {
			return (T) Double.valueOf(from);
		}
		if (type == Boolean.class) {
			return (T) Boolean.valueOf(from);
		}

		if (type == SelectValue.class) {
			return (T) new SelectValue(from);
		}

		if (type == Date.class) {
			//いくつかのパターンでパース試みる
			try {
				return (T) new Date(DateUtil.getDateInstance(DateFormat.MEDIUM, false).parse(from).getTime());
			} catch (ParseException e) {
			}
//			try {
//				return (T) new Date(DateFormat.getDateInstance(DateFormat.MEDIUM).parse(from).getTime());
//			} catch (ParseException e) {
//			}
			try {
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyy-MM-dd", false);
				sdf.setLenient(false);
				return (T) new Date(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyyMMdd", false);
				sdf.setLenient(false);
				return (T) new Date(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				// Locale設定フォーマット
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputDateFormat(), false);
				sdf.setLenient(false);
				return (T) new Date(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				// Excel用フォーマット追加
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyy/M/d", false);
				sdf.setLenient(false);
				return (T) new Date(sdf.parse(from).getTime());
			} catch (ParseException e) {
				throw new IllegalArgumentException("Date Format must Locale specific DateFormat.MEDIUM or yyyy-MM-dd or yyyyMMdd.", e);
			}
		}
		if (type == Timestamp.class) {
			//いくつかのパターンでパース試みる
			try {
				return (T) new Timestamp(DateUtil.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, true).parse(from).getTime());
			} catch (ParseException e) {
			}
			//ISO8601
			try {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", false);
				sdf.setLenient(false);
				return (T) new Timestamp(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			//timezoneを明示的に指定している場合
			try {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX", false);
				sdf.setLenient(false);
				return (T) new Timestamp(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", true);
				sdf.setLenient(false);
				return (T) new Timestamp(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss", true);
				sdf.setLenient(false);
				return (T) new Timestamp(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyyMMddHHmmssSSS", true);
				sdf.setLenient(false);
				return (T) new Timestamp(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyyMMddHHmmss", true);
				sdf.setLenient(false);
				return (T) new Timestamp(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				// Locale設定フォーマット
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputDatetimeSecFormat(), true);
				sdf.setLenient(false);
				return (T) new Timestamp(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				// Excel用フォーマット追加
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d H:mm");
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyy/M/d H:mm", true);
				sdf.setLenient(false);
				return (T) new Timestamp(sdf.parse(from).getTime());
			} catch (ParseException e) {
				//Dateでパース
				try {
					java.util.Date date = convertFromString(Date.class, from);
					date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);	//時間を切り捨て
					Timestamp ts = convertToDate(Timestamp.class, date, null, true);
					return (T) ts;
				} catch (IllegalArgumentException ie) {
					//Dateでも変換できない場合はエラー
					throw new IllegalArgumentException("Timestamp Format must Locale specific DateFormat.MEDIUM or yyyy-MM-dd HH:mm:ss.SSS or yyyy-MM-dd HH:mm:ss or yyyyMMddHHmmssSSS or yyyyMMddHHmmss.", e);
				}
			}
		}
		if (type == Time.class) {
//			return (T) java.sql.Time.valueOf(from);
			try {
				return (T) new Time(DateUtil.getTimeInstance(DateFormat.MEDIUM, false).parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
//				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("HH:mm:ss.SSS", false);
				sdf.setLenient(false);
				return (T) new Time(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
//				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("HH:mm:ss", false);
				sdf.setLenient(false);
				return (T) new Time(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
//				SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("HHmmssSSS", false);
				sdf.setLenient(false);
				return (T) new Time(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
//				SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("HHmmss", false);
				sdf.setLenient(false);
				return (T) new Time(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				// Locale設定フォーマット
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputTimeSecFormat(), false);
				sdf.setLenient(false);
				return (T) new Time(sdf.parse(from).getTime());
			} catch (ParseException e) {
			}
			try {
				// Excel用フォーマット追加
//				SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("H:mm:ss", false);
				sdf.setLenient(false);
				return (T) new Time(sdf.parse(from).getTime());
			} catch (ParseException e) {
				throw new IllegalArgumentException("Time Format must Locale specific DateFormat.MEDIUM or HH:mm:ss.SSS or HH:mm:ss or HHmmssSSS or HHmmss.", e);
			}

		}
		if (type == BigDecimal.class) {
			return (T) new BigDecimal(from);
		}

		throw new IllegalArgumentException("can not convert String to " + type);
	}

	private static LocaleFormat getLocaleFormat() {
		return ExecuteContext.getCurrentContext().getLocaleFormat();
	}

}
