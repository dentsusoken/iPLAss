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

package org.iplass.mtp.impl.query.prepared;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map;

import org.codehaus.groovy.runtime.MethodClosure;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;


/**
 * PreparedQueryでバインド可能な関数を定義
 *
 * @author 藤田 義弘
 *
 */
public class PreparedQueryBinding extends GroovyTemplateBinding {

	//日付計算用。PreparedQueryBindingは単一スレッドからしか使われない想定なので、インスタンスを使い回す。
	private GregorianCalendar forDateCalc;
	private GregorianCalendar forDateCalcLocal;

	/**
	 * コンストラクタ
	 * @param Writer
	 */
	public PreparedQueryBinding(Timestamp date, UserBinding user, Map<String, Object> additionalBinding) {
		//実行時に別途セット
		super(null);

		setVariable("toDateString", new MethodClosure(this, "toDateString"));
		setVariable("toLocalDateString", new MethodClosure(this, "toLocalDateString"));
		setVariable("toDateTimeString", new MethodClosure(this, "toDateTimeString"));
		setVariable("toTimeString", new MethodClosure(this, "toTimeString"));
		setVariable("toLocalTimeString", new MethodClosure(this, "toLocalTimeString"));
		setVariable("addYear", new MethodClosure(this, "addYear"));
		setVariable("addMonth", new MethodClosure(this, "addMonth"));
		setVariable("addWeek", new MethodClosure(this, "addWeek"));
		setVariable("addDay", new MethodClosure(this, "addDay"));
		setVariable("addHour", new MethodClosure(this, "addHour"));
		setVariable("addMinute", new MethodClosure(this, "addMinute"));
		setVariable("addSecond", new MethodClosure(this, "addSecond"));
		setVariable("addMillisecond", new MethodClosure(this, "addMillisecond"));
		setVariable("toIn", new MethodClosure(this, "toIn"));

		setVariable("date", date);
		setVariable("user", user);

		//テナントローカルの現在日付文字列をバインド
		setVariable("sysdate", ConvertUtil.formatDate(date, Literal.DATE_FROMAT, true));
		setVariable("sysdatetime", ConvertUtil.formatDate(date, Literal.DATE_TIME_FROMAT, true));
		setVariable("systime", ConvertUtil.formatDate(date, Literal.TIME_FROMAT, true));

		if (additionalBinding != null) {
			for (Map.Entry<String, Object> e: additionalBinding.entrySet()) {
				setVariable(e.getKey(), e.getValue());
			}
		}
	}

	public String toDateString(java.util.Date date) {
		return ConvertUtil.formatDate(date, Literal.DATE_FROMAT, false);
	}
	public String toLocalDateString(java.util.Date date) {
		return ConvertUtil.formatDate(date, Literal.DATE_FROMAT, true);
	}

	public String toDateTimeString(java.util.Date date) {
		return ConvertUtil.formatDate(date, Literal.DATE_TIME_FROMAT, true);
	}

	public String toTimeString(java.util.Date date) {
		return ConvertUtil.formatDate(date, Literal.TIME_FROMAT, false);
	}
	public String toLocalTimeString(java.util.Date date) {
		return ConvertUtil.formatDate(date, Literal.TIME_FROMAT, true);
	}


	private java.util.Date calcCal(java.util.Date date, int field, int amount) {

		//引数の型と一致したインスタンスを返却
		if (date instanceof Date) {
			if (forDateCalc == null) {
//				forDateCalc = new GregorianCalendar();
				forDateCalc = DateUtil.getGregorianCalendar(false);
			}
			forDateCalc.setTime(date);
			forDateCalc.add(field, amount);
			return new Date(forDateCalc.getTimeInMillis());
		}
		if (date instanceof Timestamp) {
			if (forDateCalcLocal == null) {
//				forDateCalc = new GregorianCalendar();
				forDateCalcLocal = DateUtil.getGregorianCalendar(true);
			}
			forDateCalcLocal.setTime(date);
			forDateCalcLocal.add(field, amount);
			return new Timestamp(forDateCalcLocal.getTimeInMillis());
		}
		if (date instanceof Time) {
			if (forDateCalc == null) {
//				forDateCalc = new GregorianCalendar();
				forDateCalc = DateUtil.getGregorianCalendar(false);
			}
			forDateCalc.setTime(date);
			forDateCalc.add(field, amount);
			return Time.valueOf(new Time(date.getTime()).toString());
		}
		
		//java.util.Date
		if (forDateCalcLocal == null) {
//			forDateCalc = new GregorianCalendar();
			forDateCalcLocal = DateUtil.getGregorianCalendar(true);
		}
		forDateCalcLocal.setTime(date);
		forDateCalcLocal.add(field, amount);
		return forDateCalcLocal.getTime();
	}

	public java.util.Date addYear(java.util.Date date, int year) {
		return calcCal(date, GregorianCalendar.YEAR, year);
	}

	public java.util.Date addMonth(java.util.Date date, int month) {
		return calcCal(date, GregorianCalendar.MONTH, month);
	}

	public java.util.Date addWeek(java.util.Date date, int week) {
		return calcCal(date, GregorianCalendar.WEEK_OF_YEAR, week);
	}

	public java.util.Date addDay(java.util.Date date, int day) {
		return calcCal(date, GregorianCalendar.DAY_OF_MONTH, day);
	}

	public java.util.Date addHour(java.util.Date date, int hour) {
		return calcCal(date, GregorianCalendar.HOUR_OF_DAY, hour);
	}

	public java.util.Date addMinute(java.util.Date date, int minute) {
		return calcCal(date, GregorianCalendar.MINUTE, minute);
	}

	public java.util.Date addSecond(java.util.Date date, int second) {
		return calcCal(date, GregorianCalendar.SECOND, second);
	}

	public java.util.Date addMillisecond(java.util.Date date, int millisecond) {
		return calcCal(date, GregorianCalendar.MILLISECOND, millisecond);
	}

//	/**
//	 * パラメータの日付に対してパラメータ分の年を加算し、その文字列を返します。
//	 * @param Timestamp
//	 * @param yearNum
//	 * @retrun String
//	 */
//	public String addYear(Timestamp time, int yearNum) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTimeInMillis(time.getTime());
//		cal.add(Calendar.YEAR, yearNum);
//		return DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.getTimeInMillis());
//	}
//
//	/**
//	 * パラメータの日付に対してパラメータ分の月を加算し、その文字列を返します。
//	 * @param Timestamp
//	 * @param monthNum
//	 * @retrun String
//	 */
//	public String addMonth(Timestamp time, int monthNum) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTimeInMillis(time.getTime());
//		cal.add(Calendar.MONTH, monthNum);
//		return DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.getTimeInMillis());
//	}
//
//	/**
//	 * パラメータの日付に対してパラメータ分の日を加算し、その文字列を返します。
//	 * @param Timestamp
//	 * @param dateNum
//	 * @retrun String
//	 */
//	public String addDate(Timestamp time, int dateNum) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTimeInMillis(time.getTime());
//		cal.add(Calendar.DATE, dateNum);
//		return DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.getTimeInMillis());
//	}
//
	/**
	 * 引数のオブジェクトをinの引数として利用する表現へ変換。
	 * valuesは、Collection/Object[]のいずれかを渡す。
	 * 例えば、String[]{"a","b","c"}を渡した場合、
	 * "'a','b','c'"となった文字列が返却される。
	 *
	 * @param values
	 * @return
	 */
	public String toIn(Object values) {
		if (values == null) {
			return "null";
		}
		if (values instanceof Collection<?>) {
			Collection<?> l = (Collection<?>) values;
			if (l.size() == 0) {
				return "null";
			}
			StringBuilder sb = new StringBuilder();
			boolean isFirst = true;
			for (Object v: l) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				if (v instanceof Collection<?>) {
					strRowValueListExp((Collection<?>) v, sb);
				} else if (v instanceof Object[]) {
					strRowValueListExp((Object[]) v, sb);
				} else {
					strExp(v, sb);
				}
			}
			return sb.toString();
		} else if (values instanceof Object[]) {
			Object[] l = (Object[]) values;
			if (l.length == 0) {
				return "null";
			}
			StringBuilder sb = new StringBuilder();
			boolean isFirst = true;
			for (Object v: l) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				if (v instanceof Collection<?>) {
					strRowValueListExp((Collection<?>) v, sb);
				} else if (v instanceof Object[]) {
					strRowValueListExp((Object[]) v, sb);
				} else {
					strExp(v, sb);
				}
			}
			return sb.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			strExp(values, sb);
			return sb.toString();
		}
	}

	private void strRowValueListExp(Collection<?> vals, StringBuilder sb) {
		sb.append('(');
		boolean isFirst = true;
		for (Object v: vals) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(',');
			}
			strExp(v, sb);
		}
		sb.append(')');
	}
	
	private void strRowValueListExp(Object[] vals, StringBuilder sb) {
		sb.append('(');
		boolean isFirst = true;
		for (Object v: vals) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(',');
			}
			strExp(v, sb);
		}
		sb.append(')');
	}
	
	private void strExp(Object val, StringBuilder sb) {
		if (val == null) {
			sb.append("null");
		} else {
			if (val instanceof ValueExpression) {
				sb.append(val.toString());
			} else if (val instanceof SelectValue) {
				sb.append('\'');
				sb.append(StringUtil.escapeEql(((SelectValue) val).getValue()));
				sb.append('\'');
			} else if (val instanceof String) {
				sb.append('\'');
				sb.append(StringUtil.escapeEql((String) val));
				sb.append('\'');
			} else if (val instanceof Date) {
				sb.append('\'');
				sb.append(ConvertUtil.formatDate((Date) val, Literal.DATE_FROMAT, false));
				sb.append('\'');
			} else if (val instanceof Timestamp) {
				sb.append('\'');
				sb.append(ConvertUtil.formatDate((Timestamp) val, Literal.DATE_TIME_FROMAT, true));
				sb.append('\'');
			} else if (val instanceof Time) {
				sb.append('\'');
				sb.append(ConvertUtil.formatDate((Time) val, Literal.TIME_FROMAT, false));
				sb.append('\'');
			} else {
				sb.append(StringUtil.escapeEql(val.toString()));
			}
		}
	}

}
