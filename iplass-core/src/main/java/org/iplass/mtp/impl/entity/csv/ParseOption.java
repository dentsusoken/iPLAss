/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.csv;

public class ParseOption {

	/**
	 * <p>Date型出力用Format</p>
	 *
	 * <p>未指定の場合、実行時のLocaleFormatを利用します。</p>
	 */
	private String dateFormat;

	/**
	 * <p>DateTime型出力用Format</p>
	 *
	 * <p>未指定の場合、実行時のLocaleFormatを利用します。</p>
	 */
	private String datetimeSecFormat;

	/**
	 * <p>Time型出力用Format</p>
	 *
	 * <p>未指定の場合、実行時のLocaleFormatを利用します。</p>
	 */
	private String timeSecFormat;

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDatetimeSecFormat() {
		return datetimeSecFormat;
	}

	public void setDatetimeSecFormat(String datetimeSecFormat) {
		this.datetimeSecFormat = datetimeSecFormat;
	}

	public String getTimeSecFormat() {
		return timeSecFormat;
	}

	public void setTimeSecFormat(String timeSecFormat) {
		this.timeSecFormat = timeSecFormat;
	}

	public ParseOption dateFormat(String dateFormat) {
		setDateFormat(dateFormat);
		return this;
	}

	public ParseOption datetimeSecFormat(String datetimeSecFormat) {
		setDatetimeSecFormat(datetimeSecFormat);
		return this;
	}

	public ParseOption timeSecFormat(String timeSecFormat) {
		setTimeSecFormat(timeSecFormat);
		return this;
	}
}
