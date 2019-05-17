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
import javax.xml.bind.annotation.XmlType;

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
public class ExpressionPropertyEditor extends PrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 539371370234415459L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum ExpressionDisplayType {
		@XmlEnumValue("Label")LABEL
	}

	@Override
	public ExpressionDisplayType getDisplayType() {
		return ExpressionDisplayType.LABEL;
	}

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
			displayOrder=100,
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
			displayOrder=110,
			displayNameKey="generic_editor_ExpressionPropertyEditor_numberFormatDisplaNameKey",
			descriptionKey="generic_editor_ExpressionPropertyEditor_numberFormatDescriptionKey")
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	protected String numberFormat;

	/**
	 * デフォルトコンストラクタ
	 */
	public ExpressionPropertyEditor() {
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
}
