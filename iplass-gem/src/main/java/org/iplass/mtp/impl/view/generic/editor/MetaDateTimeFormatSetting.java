/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.editor;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.DateTimeFormatSetting;

/**
* 日付・時間型のフォーマットのプロパティのメタデータ
* @author ISID Shojima
*/
public class MetaDateTimeFormatSetting implements MetaData {

	/** SerialVersionUID */
	private static final long serialVersionUID = -72521373464536611L;

	/** 日付/時刻フォーマット */
	private String datetimeFormat;

	/** 日付/時刻ロケール */
	private String datetimeLocale;

	/**
	 * 日付/時刻のフォーマット設定を取得します。
	 * @return datetimeFormat フォーマット設定
	 */
	public String getDatetimeFormat() {
		return datetimeFormat;
	}

	/**
	 * 日付/時刻のフォーマット設定を取得します。
	 * @return datetimeFormat フォーマット設定
	 */
	public void setDatetimeFormat(String datetimeFormat) {
		this.datetimeFormat = datetimeFormat;
	}

	/**
	 * 日付/時刻のロケールを取得します。
	 * @return datetimeFormat
	 */
	public String getDatetimeLocale() {
		return datetimeLocale;
	}

	/**
	 * 日付/時刻のフォーマットを設定します。
	 * @param datetimeLocale
	 */
	public void setDatetimeLocale(String datetimeLocale) {
		this.datetimeLocale = datetimeLocale;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(DateTimeFormatSetting property) {
		datetimeFormat = property.getDatetimeFormat();
		datetimeLocale = property.getDatetimeLocale();
	}

	public DateTimeFormatSetting currentConfig() {
		DateTimeFormatSetting property = new DateTimeFormatSetting();
		property.setDatetimeFormat(datetimeFormat);
		property.setDatetimeLocale(datetimeLocale);

		return property;
	}

}
