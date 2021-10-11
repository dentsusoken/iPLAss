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

/**
* 日付・時間型のフォーマットの多言語設定情報のプロパティのメタデータ
* @author ISID Shojima
*/
public class MetaLocalizedDateTimeFormatSetting implements MetaData {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1430775391649725683L;

	/** 日付/時刻のフォーマットの多言語設定の言語ロケール */
	private String langage;

	/** 日付/時刻のフォーマットの多言語設定のフォーマット */
	private String dateTimeFormat;

	/** 日付/時刻のフォーマットの多言語設定のロケール */
	private String dateTimeFormatLocale;

	/**
	 * 日付/時刻の言語を取得します。
	 * @return locale 言語
	 */
	public String getLangage() {
		return langage;
	}

	/**
	 * 日付/時刻の言語を設定します。
	 * @param locale 言語
	 */
	public void setLangage(String langage) {
		this.langage = langage;
	}

	/**
	 * 日付/時刻のフォーマットを取得します。
	 * @return datetimeFormat フォーマット
	 */
	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	/**
	 * 日付/時刻のフォーマットを設定します。
	 * @param datetimeFormat フォーマット
	 */
	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	/**
	 * 日付/時刻のロケールを取得します。
	 * @return datetimeFormat ロケール
	 */
	public String getDateTimeFormatLocale() {
		return dateTimeFormatLocale;
	}

	/**
	 * 日付/時刻のフォーマットを設定します。
	 * @param datetimeLocale　ロケール
	 */
	public void setDateTimeFormatLocale(String dateTimeFormatLocale) {
		this.dateTimeFormatLocale = dateTimeFormatLocale;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
