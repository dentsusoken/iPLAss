/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;

/**
 * 日付・時間型プロパティエディタのスーパークラス
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({DatePropertyEditor.class, TimePropertyEditor.class, TimestampPropertyEditor.class})
public abstract class DateTimePropertyEditor extends PrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4666753985814020563L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum DateTimeDisplayType {
		@XmlEnumValue("DateTime")DATETIME,
		@XmlEnumValue("Label")LABEL
	}

	/**
	 * 分の表示間隔
	 * <pre>
	 * _1MIN :1分毎
	 * _5MIN :5分毎
	 * _10MIN:10分毎
	 * _15MIN:15分毎
	 * _30MIN:30分毎
	 * </pre>
	 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum MinIntereval {
		@XmlEnumValue("_1min")_1MIN,
		@XmlEnumValue("_5min")_5MIN,
		@XmlEnumValue("_10min")_10MIN,
		@XmlEnumValue("_15min")_15MIN,
		@XmlEnumValue("_30min")_30MIN;

		/**
		 * Enum値を数値に変換します。
		 * @param val Enum値
		 * @return 分の表示間隔
		 */
		public static int toInt(MinIntereval val) {
			if (val == null) return 1;
			switch (val) {
				case _1MIN: return 1;
				case _5MIN: return 5;
				case _10MIN: return 10;
				case _15MIN: return 15;
				case _30MIN: return 30;
			}
			return 1;
		}
	}

	/**
	 * 時間の表示範囲
	 * <pre>
	 * SEC :秒まで表示
	 * MIN :分まで表示
	 * HOUR:時まで表示
	 * NONE:非表示
	 * </pre>
	 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum TimeDispRange {
		SEC,
		MIN,
		HOUR,
		NONE;

		/**
		 * 時を表示するかを判定します。
		 * @param val Enum値
		 * @return 時を表示するか
		 */
		public static boolean isDispHour(TimeDispRange val) {
			if (val == null) return true;
			switch (val) {
				case SEC: return true;
				case MIN: return true;
				case HOUR: return true;
				case NONE: return false;
			}
			return false;
		}

		/**
		 * 分を表示するかを判定します。
		 * @param val Enum値
		 * @return 分を表示するか
		 */
		public static boolean isDispMin(TimeDispRange val) {
			if (val == null) return true;
			switch (val) {
				case SEC: return true;
				case MIN: return true;
				case HOUR: return false;
				case NONE: return false;
			}
			return false;
		}

		/**
		 * 秒を表示するかを判定します。
		 * @param val Enum値
		 * @return 秒を表示するか
		 */
		public static boolean isDispSec(TimeDispRange val) {
			if (val == null) return true;
			switch (val) {
				case SEC: return true;
				case MIN: return false;
				case HOUR: return false;
				case NONE: return false;
			}
			return false;
		}
	}

	/** 表示タイプ */
	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_DateTimePropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=DateTimeDisplayType.class,
			required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_DateTimePropertyEditor_displayTypeDescriptionKey"
	)
	private DateTimeDisplayType displayType;

	/** 検索条件の単一日指定 */
	@MetaFieldInfo(
			displayName="検索条件の単一日指定",
			displayNameKey="generic_editor_DateTimePropertyEditor_singleDayConditionDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=110,
			description="検索条件の指定を単一日(時)にするかを設定します。<br>" +
					"単一日とした場合、検索条件From非表示及び検索条件To非表示は無効になります。",
			descriptionKey="generic_editor_DateTimePropertyEditor_singleDayConditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean singleDayCondition;

	/** 検索条件From非表示設定 */
	@MetaFieldInfo(
			displayName="検索条件From非表示",
			displayNameKey="generic_editor_DateTimePropertyEditor_hideSearchConditionFromDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=120,
			description="検索条件のFromを非表示にするかを設定します。",
			descriptionKey="generic_editor_DateTimePropertyEditor_hideSearchConditionFromDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean hideSearchConditionFrom;

	/** 検索条件To非表示設定 */
	@MetaFieldInfo(
			displayName="検索条件To非表示",
			displayNameKey="generic_editor_DateTimePropertyEditor_hideSearchConditionToDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=130,
			description="検索条件のToを非表示にするかを設定します。",
			descriptionKey="generic_editor_DateTimePropertyEditor_hideSearchConditionToDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean hideSearchConditionTo;

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(DateTimeDisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	@Override
	public DateTimeDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 検索条件の単一日指定を取得します。
	 * @return 検索条件の単一日指定
	 */
	public boolean isSingleDayCondition() {
		return singleDayCondition;
	}

	/**
	 * 検索条件の単一日指定を設定します
	 * @param singleDayCondition 検索条件の単一日指定
	 */
	public void setSingleDayCondition(boolean singleDayCondition) {
		this.singleDayCondition = singleDayCondition;
	}

	/**
	 * 検索条件From非表示設定を取得します。
	 * @return 検索条件From非表示設定
	 */
	public boolean isHideSearchConditionFrom() {
	    return hideSearchConditionFrom;
	}

	/**
	 * 検索条件From非表示設定を設定します。
	 * @param hideSearchConditionFrom 検索条件From非表示設定
	 */
	public void setHideSearchConditionFrom(boolean hideSearchConditionFrom) {
	    this.hideSearchConditionFrom = hideSearchConditionFrom;
	}

	/**
	 * 検索条件To非表示設定を取得します。
	 * @return 検索条件To非表示設定
	 */
	public boolean isHideSearchConditionTo() {
	    return hideSearchConditionTo;
	}

	/**
	 * 検索条件To非表示設定を設定します。
	 * @param hideSearchConditionTo 検索条件To非表示設定
	 */
	public void setHideSearchConditionTo(boolean hideSearchConditionTo) {
	    this.hideSearchConditionTo = hideSearchConditionTo;
	}

}
