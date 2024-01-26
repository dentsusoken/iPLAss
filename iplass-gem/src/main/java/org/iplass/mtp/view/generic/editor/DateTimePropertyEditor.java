/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;

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
public abstract class DateTimePropertyEditor extends PrimitivePropertyEditor implements LabelablePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4666753985814020563L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum DateTimeDisplayType {
		@XmlEnumValue("DateTime")DATETIME,
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Hidden")HIDDEN
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

	/** 日付/時刻のフォーマット設定 */
	@MetaFieldInfo(
			displayName="日付/時刻のフォーマット設定",
			displayNameKey="generic_editor_DateTimePropertyEditor_dateTimeFormatListDisplaNameKey",
			description="検索結果、詳細画面で表示する日付/時刻のフォーマットを設定する。",
			inputType=InputType.REFERENCE,
			multiple=false,
			referenceClass=DateTimeFormatSetting.class,
			displayOrder=105,
			descriptionKey="generic_editor_DateTimePropertyEditor_dateTimeFormatListDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.SEARCHRESULT}
	)
	private DateTimeFormatSetting datetimeFormat;

	/** 日付/時刻のフォーマットの多言語設定情報 */
	@MetaFieldInfo(
			displayName="日付/時刻のフォーマットの多言語設定",
			displayNameKey="generic_editor_LocalizedDateTimeFormatSetting_localizedDateTimeFormatSettingNameKey",
			description="検索結果、詳細画面で表示する日付/時刻のフォーマットの多言語設定を行う。",
			inputType=InputType.REFERENCE,
			multiple=true,
			referenceClass=LocalizedDateTimeFormatSetting.class,
			displayOrder=110,
			descriptionKey="generic_editor_LocalizedDateTimeFormatSetting_localizedDateTimeFormatSettingDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.SEARCHRESULT}
	)
	private List<LocalizedDateTimeFormatSetting>  localizedDatetimeFormatList;

	/** 検索条件の単一日指定 */
	@MetaFieldInfo(
			displayName="検索条件の単一日指定",
			displayNameKey="generic_editor_DateTimePropertyEditor_singleDayConditionDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=120,
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
			displayOrder=130,
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
			displayOrder=135,
			description="検索条件のToを非表示にするかを設定します。",
			descriptionKey="generic_editor_DateTimePropertyEditor_hideSearchConditionToDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean hideSearchConditionTo;

	/** 検索条件範囲記号非表示設定 */
	@MetaFieldInfo(
			displayName="検索条件範囲記号非表示",
			displayNameKey="generic_editor_DateTimePropertyEditor_hideSearchConditionRangeSymbolDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=140,
			description="検索条件の範囲記号を非表示にするかを設定します。FromまたはToが非表示の場合に有効になります。",
			descriptionKey="generic_editor_DateTimePropertyEditor_hideSearchConditionRangeSymbolDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean hideSearchConditionRangeSymbol;

	/** Label形式の場合の登録制御 */
	@MetaFieldInfo(
			displayName="Label形式の場合に表示値を登録する",
			displayNameKey="generic_editor_LabelablePropertyEditor_insertWithLabelValueDisplaNameKey",
			description="表示タイプがLabel形式の場合に表示値をそのまま登録するかを指定します。",
			inputType=InputType.CHECKBOX,
			displayOrder=2000,
			descriptionKey="generic_editor_LabelablePropertyEditor_insertWithLabelValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	@MetaFieldInfo(
			displayName="Label形式の場合に表示値で更新する",
			displayNameKey="generic_editor_LabelablePropertyEditor_updateWithLabelValueDisplaNameKey",
			description="表示タイプがLabel形式の場合に表示値で更新するかを指定します。",
			inputType=InputType.CHECKBOX,
			displayOrder=2010,
			descriptionKey="generic_editor_LabelablePropertyEditor_updateWithLabelValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean updateWithLabelValue = false;

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
	 * 日付/時刻のフォーマット設定を取得します。
	 * @return datetimeFormat フォーマット設定
	 */
	public DateTimeFormatSetting getDatetimeFormat() {
		return datetimeFormat;
	}

	/**
	 * 日付/時刻のフォーマット設定を取得します。
	 * @return datetimeFormat フォーマット設定
	 */
	public void setDatetimeFormat(DateTimeFormatSetting datetimeFormat) {
		this.datetimeFormat = datetimeFormat;
	}

	/**
	 * 日付/時刻のフォーマットの多言語設定情報を取得します。
	 * @return localizedDatetimeFormatList フォーマットの多言語設定情報
	 */
	public List<LocalizedDateTimeFormatSetting> getLocalizedDatetimeFormatList() {
		return localizedDatetimeFormatList;
	}

	/**
	 * 日付/時刻のフォーマットの多言語設定情報を取得します。
	 * @return localizedDatetimeFormatList フォーマットの多言語設定情報
	 */
	public void setLocalizedDatetimeFormatList(List<LocalizedDateTimeFormatSetting> localizedDatetimeFormatList) {
		this.localizedDatetimeFormatList = localizedDatetimeFormatList;
	}

	@Override
	public boolean isHide() {
		return displayType == DateTimeDisplayType.HIDDEN;
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

	/**
	 * 検索条件範囲記号非表示設定を取得します。
	 * @return 検索条件範囲記号非表示設定
	 */
	public boolean isHideSearchConditionRangeSymbol() {
		return hideSearchConditionRangeSymbol;
	}

	/**
	 * 検索条件範囲記号非表示設定を設定します。
	 * @param hideSearchConditionRangeSymbol 検索条件範囲記号非表示設定
	 */
	public void setHideSearchConditionRangeSymbol(boolean hideSearchConditionRangeSymbol) {
		this.hideSearchConditionRangeSymbol = hideSearchConditionRangeSymbol;
	}

	@Override
	public boolean isLabel() {
		return displayType == DateTimeDisplayType.LABEL;
	}

	@Override
	public boolean isInsertWithLabelValue() {
		return insertWithLabelValue;
	}

	/**
	 * Label形式の場合の登録制御を設定します。
	 *
	 * @param insertWithLabelValue Label形式の場合の登録制御
	 */
	public void setInsertWithLabelValue(boolean insertWithLabelValue) {
		this.insertWithLabelValue = insertWithLabelValue;
	}

	@Override
	public boolean isUpdateWithLabelValue() {
		return updateWithLabelValue;
	}

	/**
	 * Label形式の場合の更新制御を設定します。
	 *
	 * @param updateWithLabelValue Label形式の場合の更新制御
	 */
	public void setUpdateWithLabelValue(boolean updateWithLabelValue) {
		this.updateWithLabelValue = updateWithLabelValue;
	}

}
