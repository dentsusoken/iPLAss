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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * 数式型プロパティエディタ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/ExpressionPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/ExpressionPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class ExpressionPropertyEditor extends PrimitivePropertyEditor implements LabelablePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 539371370234415459L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum ExpressionDisplayType {
		@XmlEnumValue("Text")TEXT,
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Hidden")HIDDEN
	}

	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_ExpressionPropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=ExpressionDisplayType.class,
			//required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_ExpressionPropertyEditor_displayTypeDescriptionKey"
	)
	private ExpressionDisplayType displayType;

	/** プロパティエディタ */
	@MetaFieldInfo(
			displayName="プロパティエディタ",
			displayNameKey="generic_editor_ExpressionPropertyEditor_editorDisplaNameKey",
			inputType=InputType.REFERENCE,
			fixedReferenceClass={
				BooleanPropertyEditor.class,
				DatePropertyEditor.class,
				DecimalPropertyEditor.class,
				FloatPropertyEditor.class,
				IntegerPropertyEditor.class,
				SelectPropertyEditor.class,
				StringPropertyEditor.class,
				TimePropertyEditor.class,
				TimestampPropertyEditor.class
			},
			displayOrder=110,
			description="ExpressionプロパティのResultTypeに設定されている内容に合わせて設定してください。<br>" +
					"設定した場合、その型にあわせて画面表示を行います。<br>" +
					"未設定の場合は、値を文字列として表示します。",
					descriptionKey="generic_editor_ExpressionPropertyEditor_editorDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private PropertyEditor editor;

	/** 数値のフォーマット */
	@MetaFieldInfo(
			displayName="数値のフォーマット",
			description="表示する際に整形するフォーマットを指定します。",
			displayOrder=120,
			displayNameKey="generic_editor_ExpressionPropertyEditor_numberFormatDisplaNameKey",
			descriptionKey="generic_editor_ExpressionPropertyEditor_numberFormatDescriptionKey")
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	protected String numberFormat;

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

	/**
	 * デフォルトコンストラクタ
	 */
	public ExpressionPropertyEditor() {
	}

	@Override
	public ExpressionDisplayType getDisplayType() {
		if (displayType == null) {
			displayType = ExpressionDisplayType.TEXT;
		}
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType
	 */
	public void setDisplayType(ExpressionDisplayType displayType) {
		this.displayType = displayType;
	}

	@Override
	public boolean isHide() {
		if (editor != null) {
			return editor.isHide();
		} else {
			return displayType == ExpressionDisplayType.HIDDEN;
		}
	}

	@Override
	public String getDefaultValue() {
		// デフォルト値なし、空実装
		return null;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		// デフォルト値なし、空実装
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
	 * プロパティエディタを取得します。
	 * @return プロパティエディタ
	 */
	public PropertyEditor getEditor() {
		return editor;
	}

	/**
	 * プロパティエディタを設定します。
	 * @param editor プロパティエディタ
	 */
	public void setEditor(PropertyEditor editor) {
		this.editor = editor;
	}

	@Override
	public boolean isLabel() {
		return displayType == ExpressionDisplayType.LABEL;
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
