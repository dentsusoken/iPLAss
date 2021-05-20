/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.IgnoreField;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/NumericRangePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
@IgnoreField({"customStyle", "inputCustomStyle"})
public class NumericRangePropertyEditor extends CustomPropertyEditor implements RangePropertyEditor{

	private static final long serialVersionUID = 8195155444735209927L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum NumericRangeDisplayType {
		@XmlEnumValue("NumericRange")NUMERICRANGE
	}

	/** オブジェクト名 */
	private String objectName;

	/** プロパティエディタ */
	@MetaFieldInfo(
			displayName="プロパティエディタ",
			displayNameKey="generic_editor_NumericRangePropertyEditor_editorDisplaNameKey",
			required=true,
			inputType=InputType.REFERENCE,
			fixedReferenceClass={
					FloatPropertyEditor.class,
					IntegerPropertyEditor.class,
					DecimalPropertyEditor.class
					},
			displayOrder=100,
			description="プロパティの型にあわせたプロパティエディタを選択してください。",
			descriptionKey="generic_editor_NumericRangePropertyEditor_editorDescriptionKey"
	)
	@EntityViewField()
	private PropertyEditor editor;

	/** Toプロパティ名 */
	@MetaFieldInfo(displayName="Toプロパティ名",
			displayNameKey="generic_editor_NumericRangePropertyEditor_toPropertyNameDisplaNameKey",
			required=true,
			inputType=InputType.PROPERTY,
			displayOrder=110,
			description="このプロパティと組み合わせて表示する他のプロパティを指定します。<br>"
					+ "指定するプロパティの型はこのプロパティに合わせて下さい。",
			descriptionKey="generic_editor_NumericRangePropertyEditor_toPropertyNameDescriptionKey"
	)
	@EntityViewField()
	private String toPropertyName;

	/** Toプロパティエディタ */
	@MetaFieldInfo(
			displayName="Toプロパティエディタ",
			displayNameKey="generic_editor_NumericRangePropertyEditor_toEditorDisplaNameKey",
			inputType=InputType.REFERENCE,
			fixedReferenceClass={
					FloatPropertyEditor.class,
					IntegerPropertyEditor.class,
					DecimalPropertyEditor.class
					},
			displayOrder=120,
			description="プロパティの型にあわせたプロパティエディタを選択してください。<br>"
					+ "未指定の場合、プロパティエディタの設定が有効になります。",
			descriptionKey="generic_editor_NumericRangePropertyEditor_toEditorDescriptionKey"
	)
	@EntityViewField()
	private PropertyEditor toEditor;

	/** エラーメッセージ */
	@MetaFieldInfo(displayName="エラーメッセージ",
			displayNameKey="generic_editor_NumericRangePropertyEditor_errorMessageNameDisplaNameKey",	
			description="FromとToの大小関係が不正な場合のエラーメッセージを設定します。",
			descriptionKey="generic_editor_NumericRangePropertyEditor_errorMessageNameDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedErrorMessageList",
			displayOrder=130
	)
	@EntityViewField()
	@MultiLang()
	private String errorMessage;

	/** エラーメッセージ多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定情報",
			displayNameKey="generic_editor_NumericRangePropertyEditor_localizedErrorMessageListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=140
	)
	@EntityViewField()
	private List<LocalizedStringDefinition> localizedErrorMessageList;

	@Override
	public NumericRangeDisplayType getDisplayType() {
		return NumericRangeDisplayType.NUMERICRANGE;
	}
	
	
	/**
	 * オブジェクト名を取得します。
	 * @return オブジェクト名
	 */
	public String getObjectName() {
	    return objectName;
	}

	/**
	 * オブジェクト名を設定します。
	 * @param objectName オブジェクト名
	 */
	public void setObjectName(String objectName) {
	    this.objectName = objectName;
	}

	/**
	 * @return editor
	 */
	public PropertyEditor getEditor() {
		return editor;
	}

	/**
	 * @param editor セットする editor
	 */
	public void setEditor(PropertyEditor editor) {
		this.editor = editor;
	}

	/**
	 * @return toEditor
	 */
	public PropertyEditor getToEditor() {
		return toEditor;
	}

	/**
	 * @param toEditor セットする toEditor
	 */
	public void setToEditor(PropertyEditor toEditor) {
		this.toEditor = toEditor;
	}

	/**
	 * @return toPropertyName
	 */
	public String getToPropertyName() {
		return toPropertyName;
	}

	/**
	 * @param toPropertyName セットする toPropertyName
	 */
	public void setToPropertyName(String toPropertyName) {
		this.toPropertyName = toPropertyName;
	}

	/**
	 * @return errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage セットする errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return localizedErrorMessageList
	 */
	public List<LocalizedStringDefinition> getLocalizedErrorMessageList() {
		return localizedErrorMessageList;
	}

	/**
	 * @param localizedErrorMessageList セットする localizedErrorMessageList
	 */
	public void setLocalizedErrorMessageList(List<LocalizedStringDefinition> localizedErrorMessageList) {
		this.localizedErrorMessageList = localizedErrorMessageList;
	}


}
