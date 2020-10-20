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
 * 数値型プロパティエディタのスーパークラス
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({DecimalPropertyEditor.class, FloatPropertyEditor.class, IntegerPropertyEditor.class})
public abstract class NumberPropertyEditor extends PrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5896523904039286505L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum NumberDisplayType {
		@XmlEnumValue("Text")TEXT,
		@XmlEnumValue("Label")LABEL
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
	protected int maxlength;

	/** 数値のフォーマット */
	@MetaFieldInfo(
			displayName="数値のフォーマット",
			description="表示する際に整形するフォーマットを指定します。",
			displayOrder=120,
			displayNameKey="generic_editor_NumberPropertyEditor_numberFormatDisplaNameKey",
			descriptionKey="generic_editor_NumberPropertyEditor_numberFormatDescriptionKey")
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	protected String numberFormat;

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
			referenceTypes={FieldReferenceType.DETAIL}
	)
	protected boolean showComma;

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
	protected boolean searchInRange;

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
	protected String defaultValue;

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
//		return displayType == NumberDisplayType.HIDDEN;
		return false;
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
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
