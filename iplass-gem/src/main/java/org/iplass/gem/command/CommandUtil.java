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

package org.iplass.gem.command;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;


public class CommandUtil {

	/** 文字列から日付変換を行う際のフォーマット */
	public static final String FORMAT_DATE = "yyyyMMdd";

	/** 文字列から日時変換を行う際のフォーマット */
	public static final String FORMAT_TIMESTAMP = "yyyyMMddHHmmssSSS";
	public static final String FORMAT_TIMESTAMP14 = "yyyyMMddHHmmss";
	public static final String FORMAT_TIMESTAMP8 = "yyyyMMdd";

	/** 文字列から時間変換を行う際のフォーマット */
	public static final String FORMAT_TIME = "HHmmssSSS";
	public static final String FORMAT_TIME6 = "HHmmss";
	public static final String FORMAT_TIME8 = "HH:mm:ss";

	/**
	 * 文字列からInteger型の値を取得します。
	 * @param value 文字列
	 * @return Integer型の値
	 */
	public static Integer getInteger(String value) {
		if (StringUtil.isBlank(value)) return null;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseNumErr") + "[" + value + "]");
		}
	}

	/**
	 * 文字列からLong型の値を取得します。
	 * @param value 文字列
	 * @return Long型の値
	 */
	public static Long getLong(String value) {
		if (StringUtil.isBlank(value)) return null;
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseNumErr") + "[" + value + "]");
		}
	}

	/**
	 * 文字列からDouble型の値を取得します。
	 * @param value 文字列
	 * @return Double型の値
	 */
	public static Double getDouble(String value) {
		if (StringUtil.isBlank(value)) return null;
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseNumErr") + "[" + value + "]");
		}
	}

	/**
	 * 文字列からDecimal型の値を取得します。
	 * @param value 文字列
	 * @return Decimal型の値
	 */
	public static  BigDecimal getDecimal(String value) {
		if (value == null || value.length() == 0) return null;
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseNumErr") + "[" + value + "]");
		}
	}

	/**
	 * 文字列からDate型の値を取得します。
	 * @param value 文字列
	 * @return Date型の値
	 */
	public static Date getDate(String value) {
		Long date = null;
		try {
			date = getDateTime(value, FORMAT_DATE);
		} catch (ParseException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseDateErr") + "[" + value + "]");
		}
		if (date == null) return null;
		return new Date(date);
	}

	/**
	 * 文字列からTime型の値を取得します。
	 * @param value 文字列
	 * @return Time型の値
	 */
	public static Time getTime(String value) {
		Long date = null;
		try {
			date = getDateTime(value, FORMAT_TIME);
		} catch (ParseException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseTimeErr") + "[" + value + "]");
		}
		if (date == null) return null;
		return new Time(date);
	}

	/**
	 * 文字列からTime型の値を取得します。
	 * @param value 文字列(HHmmss形式)
	 * @return Time型の値
	 */
	public static Time getTime6(String value) {
		Long date = null;
		try {
			date = getDateTime(value, FORMAT_TIME6);
		} catch (ParseException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseTimeErr") + "[" + value + "]");
		}
		if (date == null) return null;
		return new Time(date);
	}

	/**
	 * 文字列からTime型の値を取得します。
	 * @param value 文字列(HH:mm:ss形式)
	 * @return Time型の値
	 */
	public static Time getTime8(String value) {
		Long date = null;
		try {
			date = getDateTime(value, FORMAT_TIME8);
		} catch (ParseException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseTimeErr") + "[" + value + "]");
		}
		if (date == null)
			return null;
		return new Time(date);
	}
	/**
	 * 文字列からTimestamp型の値を取得します。
	 * @param value 文字列
	 * @return Timestamp型の値
	 */
	public static Timestamp getTimestamp(String value) {
		Long date = null;
		try {
			date = getDateTime(value, FORMAT_TIMESTAMP);
		} catch (ParseException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseDateTimeErr") + "[" + value + "]");
		}
		if (date == null) return null;
		return new Timestamp(date);
	}

	/**
	 * 文字列からTimestamp型の値を取得します。
	 * @param value 文字列(yyyyMMddHHmmss形式)
	 * @return Timestamp型の値
	 */
	public static Timestamp getTimestamp14(String value) {
		Long date = null;
		try {
			date = getDateTime(value, FORMAT_TIMESTAMP14);
		} catch (ParseException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseDateTimeErr") + "[" + value + "]");
		}
		if (date == null) return null;
		return new Timestamp(date);
	}

	/**
	 * 文字列からTimestamp型の値を取得します。
	 * @param value 文字列(yyyyMMdd形式)
	 * @return Timestamp型の値
	 */
	public static Timestamp getTimestamp8(String value) {
		Long date = null;
		try {
			date = getDateTime(value, FORMAT_TIMESTAMP8);
		} catch (ParseException e) {
			throw new ApplicationException(resourceString("command.CommandUtil.parseDateTimeErr") + "[" + value + "]");
		}
		if (date == null) return null;
		return new Timestamp(date);
	}

	/**
	 * 文字列から日付・時間を取得します。
	 * @param value 文字列
	 * @param format 日付・時間のフォーマット
	 * @return 日付・時間
	 * @throws ParseException
	 */
	private static Long getDateTime(String value, String format) throws ParseException {
		if (StringUtil.isBlank(value)) return null;

//		SimpleDateFormat sd = new SimpleDateFormat(format);
		SimpleDateFormat sd = DateUtil.getSimpleDateFormat(format, true);
		sd.setLenient(false);
		return sd.parse(value).getTime();
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
