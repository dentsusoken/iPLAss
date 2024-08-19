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

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * 選択型プロパティエディタ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/SelectPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/SelectPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class SelectPropertyEditor extends PrimitivePropertyEditor implements LabelablePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7350313249348123012L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum SelectDisplayType {
		@XmlEnumValue("Radio")RADIO,
		@XmlEnumValue("Checkbox")CHECKBOX,
		@XmlEnumValue("Select")SELECT,
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Hidden")HIDDEN
	}

	/** 表示タイプ */
	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_SelectPropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=SelectDisplayType.class,
			required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_SelectPropertyEditor_displayTypeDescriptionKey"
	)
	private SelectDisplayType displayType;

	/** セレクトボックスの値 */
	@MetaFieldInfo(
			displayName="選択値",
			displayNameKey="generic_editor_SelectPropertyEditor_valuesDisplaNameKey",
			inputType=InputType.REFERENCE,
			multiple=true,
			displayOrder=110,
			referenceClass=EditorValue.class
	)
	@MultiLang(isMultiLangValue = false)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private List<EditorValue> values;

	/** 初期値 */
	@MetaFieldInfo(
			displayName="初期値",
			displayNameKey="generic_editor_SelectPropertyEditor_defaultValueDisplaNameKey",
			displayOrder=120,
			description="新規作成時の初期値を設定します。",
			descriptionKey="generic_editor_SelectPropertyEditor_defaultValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String defaultValue;

	/** CSV出力時に選択肢順でソート */
	@MetaFieldInfo(
			displayName="CSV出力時に選択肢順でソート",
			displayNameKey="generic_editor_SelectPropertyEditor_sortCsvOutputValueKey",
			inputType=InputType.CHECKBOX,
			displayOrder=130,
			description="CSV出力時に選択肢順でソートするかをしていします。",
			descriptionKey="generic_editor_SelectPropertyEditor_sortCsvOutputValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT}
	)
	private boolean sortCsvOutputValue;

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
	
	/** RADIO、CHECKBOX形式の場合のアイテムを横に並べるような表示するか */
	@MetaFieldInfo(
			displayName="RADIO、CHECKBOX形式の場合にアイテムを横に並べる",
			displayNameKey="generic_editor_SelectPropertyEditor_itemDirectionInlineDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=220,
			description="RADIO、CHECKBOX形式の場合のアイテムを横に並べるような表示するかを設定します。",
			descriptionKey="generic_editor_SelectPropertyEditor_itemDirectionInlineDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private boolean itemDirectionInline;

	/**
	 * デフォルトコンストラクタ
	 */
	public SelectPropertyEditor() {
	}

	@Override
	public SelectDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを取得します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(SelectDisplayType displayType) {
		this.displayType = displayType;
	}

	@Override
	public boolean isHide() {
		return displayType == SelectDisplayType.HIDDEN;
	}

	/**
	 * セレクトボックスの値を取得します。
	 * @return セレクトボックスの値
	 */
	public List<EditorValue> getValues() {
		if (this.values == null) this.values = new ArrayList<>();
		return values;
	}

	/**
	 * セレクトボックスの値を設定します。
	 * @param values セレクトボックスの値
	 */
	public void setValues(List<EditorValue> values) {
		this.values = values;
	}

	/**
	 * セレクトボックスの値を追加します。
	 * @param val セレクトボックスの値
	 */
	public void addValue(EditorValue val) {
		getValues().add(val);
	}

	public EditorValue getValue(String value) {
		if (value != null) {
			for (EditorValue eValue : getValues()) {
				if (eValue.getValue().equals(value)) {
					return eValue;
				}
			}
		}
		return null;
	}

	/**
	 * CSV出力時に選択肢順でソートするかを取得します。
	 *
	 * @return true:CSV出力時に選択肢順でソート
	 */
	public boolean isSortCsvOutputValue() {
		return sortCsvOutputValue;
	}

	/**
	 * CSV出力時に選択肢順でソートするかを設定します。
	 *
	 * @param sortCsvOutputValue true:CSV出力時に選択肢順でソート
	 */
	public void setSortCsvOutputValue(boolean sortCsvOutputValue) {
		this.sortCsvOutputValue = sortCsvOutputValue;
	}

	@Override
	public boolean isLabel() {
		return displayType == SelectDisplayType.LABEL;
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
	
	/**
	 * RADIO、CHECKBOX形式の場合のアイテムを横に並べるような表示するかを取得します。
	 * @return 表示内容をカンマ表示するか
	 */
	public boolean isItemDirectionInline() {
		return itemDirectionInline;
	}

	/**
	 * RADIO、CHECKBOX形式の場合のアイテムを横に並べるような表示するかを設定します。
	 * @param itemDirectionInline RADIO、CHECKBOX形式の場合のアイテムを横に並べるような表示するか
	 */
	public void setItemDirectionInline(boolean itemDirectionInline) {
		this.itemDirectionInline = itemDirectionInline;
	}

}
