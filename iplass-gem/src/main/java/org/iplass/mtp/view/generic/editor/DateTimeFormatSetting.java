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

package org.iplass.mtp.view.generic.editor;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

/**
 * 日付・時間型のフォーマットのプロパティ
 * @author ISID Shojima
 */
public class DateTimeFormatSetting implements Refrectable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4963345852735862575L;

	/** 日付/時刻のフォーマット設定 */
	@MetaFieldInfo(
			displayName="日付/時刻のフォーマット設定",
			displayNameKey="generic_editor_DateTimePropertySetting_dateTimeFormatDisplaNameKey",
			description="検索結果、詳細画面で表示する日付/時刻のフォーマットを設定する。",
			required=true,
			inputType=InputType.TEXT,
			displayOrder=100,
			descriptionKey="generic_editor_DateTimePropertySetting_dateTimeFormatDisplaNameKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String datetimeFormat;

	/** 日付/時刻のロケール設定 */
	@MetaFieldInfo(
			displayName="日付/時刻のロケール設定",
			displayNameKey="generic_editor_DateTimePropertySetting_dateTimeLocaleDisplaNameKey",
			description="検索結果、詳細画面で表示する日付/時刻のロケールを設定する。",
			inputType=InputType.TEXT,
			displayOrder=110,
			descriptionKey="generic_editor_DateTimePropertySetting_dateTimeLocaleDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String datetimeLocale;

	/**
	 * 日付/時刻のフォーマットを取得します。
	 * @return datetimeFormat フォーマット
	 */
	public String getDatetimeFormat() {
		return datetimeFormat;
	}

	/**
	 * 日付/時刻のフォーマットを設定します。
	 * @param datetimeFormat フォーマット
	 */
	public void setDatetimeFormat(String datetimeFormat) {
		this.datetimeFormat = datetimeFormat;
	}

	/**
	 * 日付/時刻のロケールを取得します。
	 * @return datetimeFormat ロケール
	 */
	public String getDatetimeLocale() {
		return datetimeLocale;
	}

	/**
	 * 日付/時刻のフォーマットを設定します。
	 * @param datetimeLocale　ロケール
	 */
	public void setDatetimeLocale(String datetimeLocale) {
		this.datetimeLocale = datetimeLocale;
	}


}
