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
 * 数値型プロパティエディタのスーパークラス
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({DecimalPropertyEditor.class, FloatPropertyEditor.class, IntegerPropertyEditor.class})
public abstract class NumberPropertyEditor extends PrimitivePropertyEditor implements LabelablePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5896523904039286505L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum NumberDisplayType {
		@XmlEnumValue("Text")TEXT,
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Hidden")HIDDEN
	}

	/** 表示タイプ */
	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_NumberPropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=NumberDisplayType.class,
			required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_NumberPropertyEditor_displayTypeDescriptionKey"
	)
	private NumberDisplayType displayType;

	/** 最大文字数 */
	@MetaFieldInfo(
			displayName="最大文字数",
			displayNameKey="generic_editor_NumberPropertyEditor_maxlengthDisplaNameKey",
			inputType=InputType.NUMBER,
			displayOrder=110,
			rangeCheck=true,
			minRange=0,
			description="テキストフィールドに入力可能な最大文字数を設定します。<br>" +
					"0の場合は適用されず、1以上の値を設定した場合に有効になります。",
			descriptionKey="generic_editor_NumberPropertyEditor_maxlengthDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private int maxlength;

	/** 数値のフォーマット */
	@MetaFieldInfo(
			displayName="数値のフォーマット",
			description="表示する際に整形するフォーマットを指定します。",
			displayOrder=120,
			displayNameKey="generic_editor_NumberPropertyEditor_numberFormatDisplaNameKey",
			descriptionKey="generic_editor_NumberPropertyEditor_numberFormatDescriptionKey")
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private String numberFormat;

	/** 表示内容をカンマ表示するか */
	@MetaFieldInfo(
			displayName="カンマ表示",
			displayNameKey="generic_editor_NumberPropertyEditor_showCommaDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=130,
			description="編集時にフォーカスアウトした際、表示内容をカンマ区切りで表示するかを設定します。",
			descriptionKey="generic_editor_NumberPropertyEditor_showCommaDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private boolean showComma;

	/** 範囲で検索するか */
	@MetaFieldInfo(
			displayName="範囲で検索",
			displayNameKey="generic_editor_NumberPropertyEditor_searchInRangeDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=140,
			description="数値の検索を範囲指定で行うかを設定します。",
			descriptionKey="generic_editor_NumberPropertyEditor_searchInRangeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean searchInRange;

	/** 検索条件From非表示設定 */
	@MetaFieldInfo(
			displayName="検索条件From非表示",
			displayNameKey="generic_editor_NumberPropertyEditor_hideSearchConditionFromDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=143,
			description="検索条件のFromを非表示にするかを設定します。",
			descriptionKey="generic_editor_NumberPropertyEditor_hideSearchConditionFromDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean hideSearchConditionFrom;

	/** 検索条件To非表示設定 */
	@MetaFieldInfo(
			displayName="検索条件To非表示",
			displayNameKey="generic_editor_NumberPropertyEditor_hideSearchConditionToDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=145,
			description="検索条件のToを非表示にするかを設定します。",
			descriptionKey="generic_editor_NumberPropertyEditor_hideSearchConditionToDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean hideSearchConditionTo;

	/** 検索条件範囲記号非表示設定 */
	@MetaFieldInfo(
			displayName="検索条件範囲記号非表示",
			displayNameKey="generic_editor_NumberPropertyEditor_hideSearchConditionRangeSymbolDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=147,
			description="検索条件の範囲記号を非表示にするかを設定します。FromまたはToが非表示の場合に有効になります。",
			descriptionKey="generic_editor_NumberPropertyEditor_hideSearchConditionRangeSymbolDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean hideSearchConditionRangeSymbol;

	/** 初期値 */
	@MetaFieldInfo(
			displayName="初期値",
			displayNameKey="generic_editor_NumberPropertyEditor_defaultValueDisplaNameKey",
			displayOrder=150,
			description="新規作成時の初期値を設定します。",
			descriptionKey="generic_editor_NumberPropertyEditor_defaultValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String defaultValue;

	/** Label形式の場合の登録制御 */
	@MetaFieldInfo(
			displayName="Label形式の場合に表示値を登録する",
			displayNameKey="generic_editor_LabelablePropertyEditor_insertWithLabelValueDisplaNameKey",
			description="表示タイプがLabel形式の場合に表示値をそのまま登録するかを指定します。",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
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
			displayOrder=210,
			descriptionKey="generic_editor_LabelablePropertyEditor_updateWithLabelValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean updateWithLabelValue = false;

	@Override
	public NumberDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(NumberDisplayType displayType) {
		this.displayType = displayType;
	}

	@Override
	public boolean isHide() {
		return displayType == NumberDisplayType.HIDDEN;
	}

	/**
	 * 数値のフォーマットを取得します。
	 * @return 数値のフォーマット
	 */
	public String getNumberFormat() {
		return numberFormat;
	}

	/**
	 * 数値のフォーマットを設定します。
	 * @param numberFormat 数値のフォーマット
	 */
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	/**
	 * 表示内容をカンマ表示するかを取得します。
	 * @return 表示内容をカンマ表示するか
	 */
	public boolean isShowComma() {
		return showComma;
	}

	/**
	 * 表示内容をカンマ表示するかを設定します。
	 * @param showComma 表示内容をカンマ表示するか
	 */
	public void setShowComma(boolean showComma) {
		this.showComma = showComma;
	}

	/**
	 * 範囲で検索するかを取得します。
	 * @return 範囲で検索するか
	 */
	public boolean isSearchInRange() {
	    return searchInRange;
	}

	/**
	 * 範囲で検索するかを設定します。
	 * @param searchInRange 範囲で検索するか
	 */
	public void setSearchInRange(boolean searchInRange) {
	    this.searchInRange = searchInRange;
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

	/**
	 * 最大文字数を取得します。
	 * @return 最大文字数
	 */
	public int getMaxlength() {
		return maxlength;
	}

	/**
	 * 最大文字数を設定します。
	 * @param maxlength 最大文字数
	 */
	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	@Override
	public boolean isLabel() {
		return displayType == NumberDisplayType.LABEL;
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

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
