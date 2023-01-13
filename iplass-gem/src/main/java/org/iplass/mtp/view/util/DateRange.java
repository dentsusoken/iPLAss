/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.util.DateUtil;

//FIXME viewパッケージに
/**
 * 日付の範囲
 */
public enum DateRange {
	/** 先月 */
	LAST_MONTH,
	/** 今月 */
	THIS_MONTH,
	/** 翌月 */
	NEXT_MONTH,
	/** 先週 */
	LAST_WEEK,
	/** 今週 */
	THIS_WEEK,
	/** 来週 */
	NEXT_WEEK,
	/** 昨日 */
	YESTERDAY,
	/** 今日 */
	TODAY,
	/** 明日 */
	TOMORROW,
	/** 過去7日間 */
	LAST_7_DAYS,
	/** 過去30日間 */
	LAST_30_DAYS,
	/** 過去90日間 */
	LAST_90_DAYS,
	/** 過去180日間 */
	LAST_180_DAYS,
	/** 翌7日間 */
	NEXT_7_DAYS,
	/** 翌30日間 */
	NEXT_30_DAYS,
	/** 翌90日間 */
	NEXT_90_DAYS,
	/** 翌180日間 */
	NEXT_180_DAYS,
	/** カスタム */
	CUSTOM;

	/**
	 * 日付範囲の開始日時を返します。
	 * @return 日付範囲の開始日時
	 */
	public Timestamp fromTimestamp() {
		Calendar cal = DateUtil.getCalendar(true);
		cal.setTime(DateUtil.getCurrentDate());
		setFromCalendar(cal);

		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * 日付範囲の開始日付を返します。
	 * @return 日付範囲の開始日付
	 */
	public Date fromDate() {
		Calendar cal = DateUtil.getCalendar(false);
		cal.setTime(DateUtil.getCurrentDate());
		setFromCalendar(cal);

		return new Date(cal.getTimeInMillis());
	}

	/**
	 * カレンダーに日付範囲の開始日時を設定します。
	 * @param cal カレンダー
	 */
	private void setFromCalendar(Calendar cal) {
		switch (this) {
			case LAST_MONTH:
				cal.add(Calendar.MONTH, -1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				break;
			case THIS_MONTH:
				cal.set(Calendar.DAY_OF_MONTH, 1);
				break;
			case NEXT_MONTH:
				cal.add(Calendar.MONTH, 1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				break;
			case LAST_WEEK:
				cal.add(Calendar.WEEK_OF_MONTH, -1);
				cal.add(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - cal.get(Calendar.DAY_OF_WEEK));
				break;
			case THIS_WEEK:
				cal.add(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - cal.get(Calendar.DAY_OF_WEEK));
				break;
			case NEXT_WEEK:
				cal.add(Calendar.WEEK_OF_MONTH, 1);
				cal.add(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - cal.get(Calendar.DAY_OF_WEEK));
				break;
			case YESTERDAY:
				cal.add(Calendar.DAY_OF_MONTH, -1);
				break;
			case TODAY:
				break;
			case TOMORROW:
				cal.add(Calendar.DAY_OF_MONTH, 1);
				break;
			case LAST_7_DAYS:
				cal.add(Calendar.DAY_OF_MONTH, -6);
				break;
			case LAST_30_DAYS:
				cal.add(Calendar.DAY_OF_MONTH, -29);
				break;
			case LAST_90_DAYS:
				cal.add(Calendar.DAY_OF_MONTH, -89);
				break;
			case LAST_180_DAYS:
				cal.add(Calendar.DAY_OF_MONTH, -179);
				break;
			case NEXT_7_DAYS:
				break;
			case NEXT_30_DAYS:
				break;
			case NEXT_90_DAYS:
				break;
			case NEXT_180_DAYS:
				break;
			default:
				break;
		}

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * 日付範囲の終了日時を返します。
	 * @return 日付範囲の終了日時
	 */
	public Timestamp toTimestamp() {
		Calendar cal = DateUtil.getCalendar(true);
		cal.setTime(DateUtil.getCurrentDate());
		setToCalendar(cal);

		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * 日付範囲の終了日付を返します。
	 * @return 日付範囲の終了日付
	 */
	public Date toDate() {
		Calendar cal = DateUtil.getCalendar(false);
		cal.setTime(DateUtil.getCurrentDate());
		setToCalendar(cal);

		return new Date(cal.getTimeInMillis());
	}

	/**
	 * カレンダーに日付範囲の終了日時を設定します。
	 * @param cal カレンダー
	 */
	private void setToCalendar(Calendar cal) {
		switch (this) {
			case LAST_MONTH:
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				break;
			case THIS_MONTH:
				cal.add(Calendar.MONTH, 1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				break;
			case NEXT_MONTH:
				cal.add(Calendar.MONTH, 2);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				break;
			case LAST_WEEK:
				cal.add(Calendar.WEEK_OF_MONTH, -1);
				cal.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK));
				break;
			case THIS_WEEK:
				cal.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK));
				break;
			case NEXT_WEEK:
				cal.add(Calendar.WEEK_OF_MONTH, 1);
				cal.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK));
				break;
			case YESTERDAY:
				cal.add(Calendar.DAY_OF_MONTH, -1);
				break;
			case TODAY:
				break;
			case TOMORROW:
				cal.add(Calendar.DAY_OF_MONTH, 1);
				break;
			case LAST_7_DAYS:
				break;
			case LAST_30_DAYS:
				break;
			case LAST_90_DAYS:
				break;
			case LAST_180_DAYS:
				break;
			case NEXT_7_DAYS:
				cal.add(Calendar.DAY_OF_MONTH, 6);
				break;
			case NEXT_30_DAYS:
				cal.add(Calendar.DAY_OF_MONTH, 29);
				break;
			case NEXT_90_DAYS:
				cal.add(Calendar.DAY_OF_MONTH, 89);
				break;
			case NEXT_180_DAYS:
				cal.add(Calendar.DAY_OF_MONTH, 179);
				break;
			default:
				break;
		}

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
	}

	/**
	 * 表示ラベルを取得します。
	 * @return 表示ラベル
	 */
	public String getDisplayLabel() {
		String ret = null;
		switch (this) {
			case LAST_MONTH:
				ret = resourceString("api.util.DateRange.lastMonth");
				break;
			case THIS_MONTH:
				ret = resourceString("api.util.DateRange.thisMonth");
				break;
			case NEXT_MONTH:
				ret = resourceString("api.util.DateRange.nextMonth");
				break;
			case LAST_WEEK:
				ret = resourceString("api.util.DateRange.lastWeek");
				break;
			case THIS_WEEK:
				ret = resourceString("api.util.DateRange.thisWeek");
				break;
			case NEXT_WEEK:
				ret = resourceString("api.util.DateRange.nextWeek");
				break;
			case YESTERDAY:
				ret = resourceString("api.util.DateRange.yesterday");
				break;
			case TODAY:
				ret = resourceString("api.util.DateRange.today");
				break;
			case TOMORROW:
				ret = resourceString("api.util.DateRange.tomorrow");
				break;
			case LAST_7_DAYS:
				ret = resourceString("api.util.DateRange.last7days");
				break;
			case LAST_30_DAYS:
				ret = resourceString("api.util.DateRange.last30days");
				break;
			case LAST_90_DAYS:
				ret = resourceString("api.util.DateRange.last90days");
				break;
			case LAST_180_DAYS:
				ret = resourceString("api.util.DateRange.last180days");
				break;
			case NEXT_7_DAYS:
				ret = resourceString("api.util.DateRange.next7days");
				break;
			case NEXT_30_DAYS:
				ret = resourceString("api.util.DateRange.next30days");
				break;
			case NEXT_90_DAYS:
				ret = resourceString("api.util.DateRange.next90days");
				break;
			case NEXT_180_DAYS:
				ret = resourceString("api.util.DateRange.next180days");
				break;
			default:
				ret = "";
				break;
		}
		return ret;
	}

	/**
	 * 文字列が日付範囲かチェックします。
	 * @param val 文字列
	 * @return 日付範囲か
	 */
	public static boolean check(String val) {
		if (val == null || val.isEmpty()) return false;
		try {
			valueOf(val);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
