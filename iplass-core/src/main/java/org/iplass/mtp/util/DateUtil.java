/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.iplass.mtp.impl.core.ExecuteContext;


/**
 * 日付操作用のユーティリティです。
 */
public class DateUtil {

	/**
	 * 現在日時を返します。
	 * 同一リクエスト処理中は常に同じ値（リクエスト処理中に最初に当該メソッドが呼ばれた日時）を返します。
	 *
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return ExecuteContext.getCurrentContext().getCurrentTimestamp();
	}

	/**
	 * テナントローカルの現在日付を返します。<br>
	 * 同一リクエスト処理中は常に同じ値（リクエスト処理中に最初に当該メソッドが呼ばれた日付）を返します。<br>
	 * 現在日付をjava.sql.Dateで表現するためのものです。
	 * システムのタイムゾーンとテナントローカルタイムゾーンが異なる場合、タイムゾーンのオフセット分シフトされた値となります。
	 *
	 * @return
	 */
	public static Date getCurrentDate() {
		return ExecuteContext.getCurrentContext().getCurrentLocalDate();
	}

	/**
	 * テナントローカルの現在時間を返します。<br>
	 * 同一リクエスト処理中は常に同じ値（リクエスト処理中に最初に当該メソッドが呼ばれた日付）を返します。<br>
	 * 現在時刻をjava.sql.Timeで表現するためのものです。
	 * 1970-01-01 00:00:00～23:59:59の間の値となります。
	 * システムのタイムゾーンとテナントローカルタイムゾーンが異なる場合、テナントローカルのタイムゾーンのオフセット分シフトされた値となります。
	 *
	 * @return
	 */
	public static Time getCurrentTime() {
		return ExecuteContext.getCurrentContext().getCurrentLocalTime();
	}

	/**
	 * 指定のpatternのSimpleDateFormatのインスタンスを取得します。
	 * ロケールは、テナントに設定されるロケール（未設定の場合はシステムデフォルトロケール）が利用されます。
	 *
	 * @param pattern
	 * @param useTenantTimeZone システムのデフォルトタイムゾーンではなく、テナントに設定されるタイムゾーンを利用する場合はtrueを指定します
	 * @return
	 */
	public static SimpleDateFormat getSimpleDateFormat(String pattern, boolean useTenantTimeZone) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, ExecuteContext.getCurrentContext().getLocale());
		if (useTenantTimeZone) {
			simpleDateFormat.setTimeZone(ExecuteContext.getCurrentContext().getTimeZone());
		}
		return simpleDateFormat;
	}

	/**
	 * 指定のpatternのSimpleDateFormatのインスタンスを取得します。
	 *
	 * @param pattern
	 * @param useTenantTimeZone システムのデフォルトタイムゾーンではなく、テナントに設定されるタイムゾーンを利用する場合はtrueを指定します
	 * @param useUserLangLocale テナントに設定されるロケールではなく、当該処理を実行しているユーザーの言語に紐付くロケールを利用する場合はtrueを指定します。
	 * @return
	 */
	public static SimpleDateFormat getSimpleDateFormat(String pattern, boolean useTenantTimeZone, boolean useUserLangLocale) {
		Locale l;
		if (useUserLangLocale) {
			l = ExecuteContext.getCurrentContext().getLangLocale();
		} else {
			l = ExecuteContext.getCurrentContext().getLocale();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, l);
		if (useTenantTimeZone) {
			simpleDateFormat.setTimeZone(ExecuteContext.getCurrentContext().getTimeZone());
		}
		return simpleDateFormat;
	}

	public static Calendar getCalendar(boolean useTenantTimeZone) {
		Calendar calendar = Calendar.getInstance(ExecuteContext.getCurrentContext().getLocale());
		if (useTenantTimeZone) {
			calendar.setTimeZone(ExecuteContext.getCurrentContext().getTimeZone());
		}
		return calendar;
	}

	public static Calendar getCalendar(Locale locale, boolean useTenantTimeZone) {
		Calendar calendar = Calendar.getInstance(locale);
		if (useTenantTimeZone) {
			calendar.setTimeZone(ExecuteContext.getCurrentContext().getTimeZone());
		}
		return calendar;
	}

	public static GregorianCalendar getGregorianCalendar(boolean useTenantTimeZone) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar(ExecuteContext.getCurrentContext().getLocale());
		if (useTenantTimeZone) {
			gregorianCalendar.setTimeZone(ExecuteContext.getCurrentContext().getTimeZone());
		}
		return gregorianCalendar;
	}

	public static DateFormat getDateInstance(int style, boolean useTenantTimeZone) {
		return getDateInstance(style, null, useTenantTimeZone);
	}
	public static DateFormat getDateInstance(int style, Locale locale, boolean useTenantTimeZone) {
		if (locale == null) {
			locale = ExecuteContext.getCurrentContext().getLocale();
		}
		DateFormat dateFormat = DateFormat.getDateInstance(style, locale);
		if (useTenantTimeZone) {
			dateFormat.setTimeZone(ExecuteContext.getCurrentContext().getTimeZone());
		}
		return dateFormat;
	}

	public static DateFormat getDateTimeInstance(int dateStyle, int timeStyle, boolean useTenantTimeZone) {
		return getDateTimeInstance(dateStyle, timeStyle, null, null, useTenantTimeZone);
	}
	public static DateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale, boolean useTenantTimeZone) {
		return getDateTimeInstance(dateStyle, timeStyle, locale, null, useTenantTimeZone);
	}
	public static DateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale, TimeZone timeZone, boolean useTenantTimeZone) {
		if (locale == null) {
			locale = ExecuteContext.getCurrentContext().getLocale();
		}
		if (timeZone == null) {
			timeZone = ExecuteContext.getCurrentContext().getTimeZone();
		}
		DateFormat dateFormat = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
		if (useTenantTimeZone) {
			dateFormat.setTimeZone(timeZone);
		}
		return dateFormat;
	}

	public static DateFormat getTimeInstance(boolean useTenantTimeZone) {
		return getTimeInstance(DateFormat.DEFAULT, null, useTenantTimeZone);
	}
	public static DateFormat getTimeInstance(int style, boolean useTenantTimeZone) {
		return getTimeInstance(style, null, useTenantTimeZone);
	}
	public static DateFormat getTimeInstance(int style, Locale locale, boolean useTenantTimeZone) {
		if (locale == null) {
			locale = ExecuteContext.getCurrentContext().getLocale();
		}
		DateFormat dateFormat = DateFormat.getTimeInstance(style, locale);
		if (useTenantTimeZone) {
			dateFormat.setTimeZone(ExecuteContext.getCurrentContext().getTimeZone());
		}
		return dateFormat;
	}

	/**
	 * 現在日時を取得する
	 * <p>
	 * テナントローカルなタイムゾーンの現在日時を取得します。
	 * </p>
	 * @return 現在日時
	 */
	public static ZonedDateTime getCurrentZonedDateTime() {
		var tenantZoneId = ExecuteContext.getCurrentContext().getTimeZone().toZoneId();
		return getCurrentZonedDateTime(tenantZoneId);
	}

	/**
	 * 指定されたゾーンIDの現在日時を取得する
	 * @param zoneId ゾーンID
	 * @return 現在日時
	 */
	public static ZonedDateTime getCurrentZonedDateTime(ZoneId zoneId) {
		return ZonedDateTime.ofInstant(DateUtil.getCurrentTimestamp().toInstant(), zoneId);
	}

	/**
	 * 現在時刻から指定された時間を減算した日時を取得する
	 * <p>
	 * テナントローカルなタイムゾーンの現在日時から、指定された時間を減算します。
	 * </p>
	 * @param duration 減算時間
	 * @return 減算後の日時
	 */
	public static ZonedDateTime getCurrentZonedDateTimeMinus(Duration duration) {
		var current = getCurrentZonedDateTime();
		return current.minus(duration);
	}

	/**
	 * 現在時刻を指定されたフォーマットで取得する
	 * @param formatter 日付時刻フォーマット
	 * @return フォーマットされた日時文字列
	 */
	public static String getCurrentZonedDateTimeFormat(DateTimeFormatter formatter) {
		return formatter.format(getCurrentZonedDateTime());
	}
}
