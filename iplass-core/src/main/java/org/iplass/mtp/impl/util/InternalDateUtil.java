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

package org.iplass.mtp.impl.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.lang3.time.DateUtils;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.util.DateUtil;


/**
 *
 * @author 片野　博之
 *
 */
public final class InternalDateUtil {
	// FIXME 日付は外だしすること。国際化の場合時間の概念が問題になるはず
//	private static final Date YUKO_DATE_TO;
//	public static final java.sql.Date YUKO_DATE_TO_FOR_SQLDATE;
	/** 日付変換用 */
//	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//	private static final SimpleDateFormat sdf = DateUtil.getSimpleDateFormat("yyyyMMdd");
//	static {
//		Calendar c = GregorianCalendar.getInstance();
//		Calendar c = DateUtil.getGregorianCalendar();
//		c.set(2099, 11, 31, 0, 0, 0);
//		YUKO_DATE_TO_FOR_SQLDATE = new java.sql.Date(c.getTimeInMillis());
//		YUKO_DATE_TO = YUKO_DATE_TO_FOR_SQLDATE;
//	}
	private InternalDateUtil() {
	}

	public static final Date getYukoDateTo() {
		Calendar c = DateUtil.getGregorianCalendar(false);
		c.set(2099, 11, 31, 0, 0, 0);
		return new java.sql.Date(c.getTimeInMillis());
	}
//	public static final java.sql.Date getYukoDateToForSqlDate() {
//		return YUKO_DATE_TO_FOR_SQLDATE;
//	}

//	public static final Date getYukoDateFrom() {
//		return new Date();
//	}

	public static final Timestamp getNow() {
		return ExecuteContext.getCurrentContext().getCurrentTimestamp();
	}
	public static final Date getNowForSqlDate() {
		//return new Date(ExecuteContext.getCurrentContext().getCurrentTimestamp().getTime());

		//これで返そうとするとテナント検索処理で無限Loopが発生する
		//return ExecuteContext.getCurrentContext().getCurrentLocalDate();

		//時間をクリアして返す。
		return new Date(DateUtils.truncate(ExecuteContext.getCurrentContext().getCurrentTimestamp(), Calendar.DAY_OF_MONTH).getTime());
	}


	// FIXME 本来なら、Localごとに日付を持たないとおかしな状態となる。
	// 2010/01/01 から新規部署に移動している場合、
	// 現地時間と2時間時差がある場合に、
	// 現地時間、2009/12/31 22:00 分から移動されてしまう。（時差が大きい場合にもっと問題になると思う。）
	public static String formatYYYY_MM_DD(java.util.Date date) {
		return DateUtil.getSimpleDateFormat("yyyyMMdd", false).format(date);
	}

//	public static Date toDate(Timestamp ts) {
//		if (ts == null) {
//			return null;
//		}
////		Calendar c = GregorianCalendar.getInstance();
//		Calendar c = DateUtil.getGregorianCalendar(true);
//		c.setTimeInMillis(ts.getTime());
//		c.set(Calendar.HOUR_OF_DAY, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 0);
//		return new Date(c.getTimeInMillis());
//	}

	/**
	 * 時間（時、分、秒）をクリアします。
	 *
	 * @param date 対象日付
	 * @return 計算結果
	 */
	public static Date truncateTime(Date date) {
//		Calendar cal = Calendar.getInstance();
		Calendar cal = DateUtil.getCalendar(false);
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());

	}

	/**
	 * 日数を加算します。
	 *
	 * @param date 対象日付
	 * @param amount 加算日数
	 * @return 計算結果
	 */
	public static Date addDays(Date date, int amount) {
		return new Date(DateUtils.addDays(date, amount).getTime());
	}

	/**
	 * ２つに日付の差（日数）を計算します。
	 *
	 * @param date1 対象日付１
	 * @param date2 対象日付２
	 * @return 計算結果（対象日付１ - 対象日付２）の日数
	 */
	public static int diffDays(Date date1, Date date2) {
		long dateTime1 = date1.getTime();
		long dateTime2 = date2.getTime();

		long oneDateTime = 1000 * 60 * 60 * 24;

		long days = (dateTime1 - dateTime2) / oneDateTime;

		return (int)days;
	}

}
