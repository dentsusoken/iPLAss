/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.rpc.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.util.DateUtil;

/**
 * Eqlの検索結果についてObject型をString型に変換するPredicate
 *
 * Gwtでは、Object型を返すことができないため、またコンパイル時の効率化のために
 * String型に変換して返す。
 *
 */
public class EqlSearchPredicate implements Predicate<Object[]> {

	private static final int MAX_LIMIT = 1000;

	private static final String FAST_DATE_FROMAT = "yyyy-MM-dd";
	private static final String FAST_DATE_TIME_FROMAT = "yyyy-MM-dd HH:mm:ss.SSSZZ";	//DateFormatでは「XXX」だがFastDateFormatでは「ZZ」と指定
	private static final String FAST_TIME_FROMAT = "HH:mm:ss";

	private SimpleDateFormat dateFormatter;
	private SimpleDateFormat dateTimeFormatter;
	private SimpleDateFormat timeFormatter;

	/** 検索結果(strCallbackが指定された場合はnull) */
	private List<String[]> strRecords;

	/** Stringに変換された値を利用するCallback */
	private Predicate<String[]> strCallback;

	public EqlSearchPredicate() {
		strRecords = new ArrayList<String[]>();
	}

	public EqlSearchPredicate(Predicate<String[]> strCallback) {
		this.strCallback = strCallback;
	}

	@Override
	public boolean test(Object[] record) {
		String[] strRecord = new String[record.length];
		for (int i = 0; i < record.length; i++) {
			//Stringに変換
			strRecord[i] = convertValue(record[i]);
		}

		if (strCallback != null) {
			//Callbackが指定されている場合は返す
			return strCallback.test(strRecord);
		} else {
			//Callbackが指定されていない場合はListにためる
			strRecords.add(strRecord);

			//最大件数に到達していたら終了
			if (strRecords.size() == MAX_LIMIT) {
				return false;
			}
			return true;
		}
	}

	public List<String[]> getStrRecordList() {
		return strRecords;
	}

	public boolean isLimitSearchResult() {
		return strRecords.size() == MAX_LIMIT;
	}

	public int getLimitSize() {
		return MAX_LIMIT;
	}

	protected String convertValue(Object value) {

		String retValue = null;
		if (value != null) {
			if (value.getClass().isArray()) {
				//多重度対応
				retValue = convertValue((Object[])value);
			} else if (value instanceof SelectValue) {
				//Select型
				SelectValue tmp = (SelectValue)value;
				retValue = tmp.getDisplayName() + "(" + tmp.getValue() + ")";
			} else if (value instanceof BinaryReference) {
				//Binary型
				BinaryReference tmp = (BinaryReference)value;
				retValue = tmp.getName() + "(" + tmp.getLobId() + ")";
			} else if (value instanceof Timestamp) {
				//Timestamp型
				Timestamp tmp = (Timestamp)value;
				retValue = getDateTimeFormatter().format(tmp);
			} else if (value instanceof Time) {
				//Time型
				Time tmp = (Time)value;
				retValue = getTimeFormatter().format(tmp);
			} else if (value instanceof Date) {
				//Date型
				Date tmp = (Date)value;
				retValue = getDateFormatter().format(tmp);
			} else if (value instanceof BigDecimal) {
				//BigDecimal型
				BigDecimal bd = (BigDecimal)value;
				retValue = bd.toPlainString();
			} else if (value instanceof Double) {
				//Double型
				BigDecimal bd = BigDecimal.valueOf((Double)value);
				retValue = bd.toPlainString();
			} else if (value instanceof Float) {
				//Double型
				BigDecimal bd = BigDecimal.valueOf((Float)value);
				retValue = bd.toPlainString();
			} else {
				retValue =  value.toString();
			}
		} else {
			retValue = "";
		}

		return retValue;
	}

	protected String convertValue(Object[] values) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object value : values) {
			sb.append(convertValue(value));
			sb.append(",");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}

	protected SimpleDateFormat getDateFormatter() {
		if (dateFormatter == null) {
			dateFormatter = DateUtil.getSimpleDateFormat(FAST_DATE_FROMAT, false);
		}
		return dateFormatter;
	}

	protected SimpleDateFormat getDateTimeFormatter() {
		if (dateTimeFormatter == null) {
			dateTimeFormatter = DateUtil.getSimpleDateFormat(FAST_DATE_TIME_FROMAT, true);
		}
		return dateTimeFormatter;
	}

	protected SimpleDateFormat getTimeFormatter() {
		if (timeFormatter == null) {
			timeFormatter = DateUtil.getSimpleDateFormat(FAST_TIME_FROMAT, false);
		}
		return timeFormatter;
	}
}
