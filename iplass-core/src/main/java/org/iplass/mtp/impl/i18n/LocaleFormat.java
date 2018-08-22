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

package org.iplass.mtp.impl.i18n;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocaleFormat implements Serializable, Cloneable {

	private static final long serialVersionUID = -1342193674900269295L;

	private List<String> locale;

	/**
	 * <p>サーバDate型Format</p>
	 *
	 * <p>サーバ上でのDate型形式を指定します。</p>
	 */
	private String serverDateFormat;

	/**
	 * <p>サーバTime型Format</p>
	 *
	 * <p>サーバ上でのTime型形式を指定します。</p>
	 */
	private String serverTimeFormat;

	/**
	 * <p>ブラウザDate型入力用Format</p>
	 *
	 * <p>ブラウザ上でのDate型入力形式を指定します。</p>
	 */
	private String browserInputDateFormat;

	/**
	 * <p>ブラウザTime型入力用Format(Range:SEC)</p>
	 *
	 * <p>ブラウザ上でのTime型入力形式を指定します。</p>
	 */
	private String browserInputTimeSecFormat;

	/**
	 * <p>ブラウザTime型入力用Format(Range:MIN)</p>
	 *
	 * <p>ブラウザ上でのTime型入力形式を指定します。</p>
	 */
	private String browserInputTimeMinFormat;

	/**
	 * <p>ブラウザTime型入力用Format(Range:HOUR)</p>
	 *
	 * <p>ブラウザ上でのTime型入力形式を指定します。</p>
	 */
	private String browserInputTimeHourFormat;

	/**
	 * <p>Date型出力用Format</p>
	 *
	 * <p>Entity検索結果、CSV出力結果などのDate型出力形式を指定します。</p>
	 */
	private String outputDateFormat;

	/**
	 * <p>Date型出力用Format(曜日)</p>
	 *
	 * <p>Entity検索結果、CSV出力結果などのDate型出力形式を指定します。</p>
	 */
	private String outputDateWeekdayFormat;

	/**
	 * <p>Date型Excel日付形式</p>
	 *
	 */
	private String excelDateFormat;
	/**
	 * <p>Time型Excel時間形式</p>
	 *
	 */
	private String excelTimeFormat;

	/**
	 * <p>Time型出力用Format(Range:SEC)</p>
	 *
	 * <p>Entity検索結果、CSV出力結果などのDate型出力形式を指定します。</p>
	 */
	private String outputTimeSecFormat;

	/**
	 * <p>Time型出力用Format(Range:MIN)</p>
	 *
	 * <p>Entity検索結果、CSV出力結果などのDate型出力形式を指定します。</p>
	 */
	private String outputTimeMinFormat;

	/**
	 * <p>Time型出力用Format(Range:HOUR)</p>
	 *
	 * <p>Entity検索結果、CSV出力結果などのDate型出力形式を指定します。</p>
	 */
	private String outputTimeHourFormat;

	private boolean lastNameIsFirst;


	public List<String> getLocale() {
		return locale;
	}

	public void setLocale(List<String> locale) {
		this.locale = locale;
	}

	public String getServerDateFormat() {
		return serverDateFormat;
	}

	public void setServerDateFormat(String serverDateFormat) {
		this.serverDateFormat = serverDateFormat;
	}

	public String getServerTimeFormat() {
		return serverTimeFormat;
	}

	public void setServerTimeFormat(String serverTimeFormat) {
		this.serverTimeFormat = serverTimeFormat;
	}

	public String getServerDateTimeFormat() {
		return getServerDateFormat() + getServerTimeFormat();
	}

	public String getBrowserInputDateFormat() {
		return browserInputDateFormat;
	}

	public void setBrowserInputDateFormat(String browserInputDateFormat) {
		this.browserInputDateFormat = browserInputDateFormat;
	}

	public String getBrowserInputTimeSecFormat() {
		return browserInputTimeSecFormat;
	}

	public void setBrowserInputTimeSecFormat(String browserInputTimeSecFormat) {
		this.browserInputTimeSecFormat = browserInputTimeSecFormat;
	}

	public String getBrowserInputTimeMinFormat() {
		return browserInputTimeMinFormat;
	}

	public void setBrowserInputTimeMinFormat(String browserInputTimeMinFormat) {
		this.browserInputTimeMinFormat = browserInputTimeMinFormat;
	}

	public String getBrowserInputTimeHourFormat() {
		return browserInputTimeHourFormat;
	}

	public void setBrowserInputTimeHourFormat(String browserInputTimeHourFormat) {
		this.browserInputTimeHourFormat = browserInputTimeHourFormat;
	}

	public String getOutputDateFormat() {
		return outputDateFormat;
	}

	public void setOutputDateFormat(String outputDateFormat) {
		this.outputDateFormat = outputDateFormat;
	}

	public String getOutputDateWeekdayFormat() {
		return outputDateWeekdayFormat;
	}

	public void setOutputDateWeekdayFormat(String outputDateWeekdayFormat) {
		this.outputDateWeekdayFormat = outputDateWeekdayFormat;
	}

	public String getExcelDateFormat() {
		return excelDateFormat;
	}

	public void setExcelDateFormat(String excelDateFormat) {
		this.excelDateFormat = excelDateFormat;
	}

	public String getExcelTimeFormat() {
		return excelTimeFormat;
	}

	public void setExcelTimeFormat(String excelTimeFormat) {
		this.excelTimeFormat = excelTimeFormat;
	}

	public String getOutputTimeSecFormat() {
		return outputTimeSecFormat;
	}

	public void setOutputTimeSecFormat(String outputTimeSecFormat) {
		this.outputTimeSecFormat = outputTimeSecFormat;
	}

	public String getOutputTimeMinFormat() {
		return outputTimeMinFormat;
	}

	public void setOutputTimeMinFormat(String outputTimeMinFormat) {
		this.outputTimeMinFormat = outputTimeMinFormat;
	}

	public String getOutputTimeHourFormat() {
		return outputTimeHourFormat;
	}

	public void setOutputTimeHourFormat(String outputTimeHourFormat) {
		this.outputTimeHourFormat = outputTimeHourFormat;
	}

	public String getOutputDatetimeSecFormat() {
		return getOutputDateFormat() + " " + getOutputTimeSecFormat();
	}

	public String getOutputDatetimeMinFormat() {
		return getOutputDateFormat() + " " + getOutputTimeMinFormat();
	}

	public String getOutputDatetimeHourFormat() {
		return getOutputDateFormat() + " " + getOutputTimeHourFormat();
	}

	public boolean isLastNameIsFirst() {
		return lastNameIsFirst;
	}

	public void setLastNameIsFirst(boolean lastNameIsFirst) {
		this.lastNameIsFirst = lastNameIsFirst;
	}

	@Override
	public LocaleFormat clone() {
		try {
			LocaleFormat copy = (LocaleFormat) super.clone();
			copy.locale = new ArrayList<>(copy.locale);
			return copy;
		} catch (CloneNotSupportedException e) {
			//never occur
			throw new RuntimeException(e);
		}
	}


}
