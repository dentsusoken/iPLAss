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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnumValue;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.HasNestProperty;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 *
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/JoinPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class JoinPropertyEditor extends CustomPropertyEditor implements HasNestProperty {

	/** SerialVersionUID */
	private static final long serialVersionUID = 8692587638693854180L;

	/** 表示タイプ */
	public enum JoinDisplayType {
		@XmlEnumValue("Join")JOIN
	}

	/** オブジェクト名 */
	private String objectName;

	/** プロパティエディタ */
	@MetaFieldInfo(
			displayName="プロパティエディタ",
			displayNameKey="generic_editor_JoinPropertyEditor_editorDisplaNameKey",
			required=true,
			inputType=InputType.REFERENCE,
//			referenceClass=PropertyEditor.class,
			fixedReferenceClass={
				UserPropertyEditor.class,
				AutoNumberPropertyEditor.class,
				BinaryPropertyEditor.class,
				BooleanPropertyEditor.class,
				DatePropertyEditor.class,
				TimePropertyEditor.class,
				TimestampPropertyEditor.class,
				ExpressionPropertyEditor.class,
				DecimalPropertyEditor.class,
				FloatPropertyEditor.class,
				IntegerPropertyEditor.class,
				SelectPropertyEditor.class,
				StringPropertyEditor.class,
				LongTextPropertyEditor.class,
				ReferencePropertyEditor.class
			},
			displayOrder=100,
			description="プロパティの型にあわせたプロパティエディタを選択してください",
			descriptionKey="generic_editor_JoinPropertyEditor_editorDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private PropertyEditor editor;

	/** プロパティ */
	@MetaFieldInfo(displayName="プロパティ",
			displayNameKey="generic_editor_JoinPropertyEditor_propertiesDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=NestProperty.class,
			multiple=true,
			displayOrder=110,
			description="このプロパティと組み合わせて表示する他のプロパティを指定します。",
			descriptionKey="generic_editor_JoinPropertyEditor_propertiesDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private List<NestProperty> properties;

	/** フォーマット */
	@MetaFieldInfo(
			displayName="フォーマット",
			displayNameKey="generic_editor_JoinPropertyEditor_formatDisplaNameKey",
			displayOrder=120,
			description="複数のプロパティを組み合わせて表示するためのフォーマットです。<br>" +
					"プロパティを指定する際は「${プロパティ名}」のように指定します。",
			descriptionKey="generic_editor_JoinPropertyEditor_formatDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private String format;

	@MetaFieldInfo(
			displayName = "ネストプロパティの検証エラーメッセージをまとめて表示",
			displayNameKey = "generic_editor_JoinPropertyEditor_showNestPropertyErrorsDisplaNameKey",
			inputType = InputType.CHECKBOX,
			displayOrder = 130,
			description = "チェックの時はネストプロパティの検証エラーメッセージをまとめて表示します。<br>" +
					"未チェックの時はネストプロパティの検証エラーメッセージをまとめて表示しません。",
			descriptionKey = "generic_editor_JoinPropertyEditor_showNestPropertyErrorsDescriptionKey"
	)
	@EntityViewField(
			referenceTypes= {FieldReferenceType.DETAIL, FieldReferenceType.BULK}
	)
	private boolean showNestPropertyErrors;

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
	 * フォーマットを取得します。
	 * @return フォーマット
	 */
	public String getFormat() {
	    return format;
	}

	/**
	 * フォーマットを設定します。
	 * @param format フォーマット
	 */
	public void setFormat(String format) {
	    this.format = format;
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

	/**
	 * ネストプロパティの検証エラーメッセージをまとめて表示を取得します。
	 * @return ネストプロパティの検証エラーメッセージをまとめて表示
	 */
	public boolean isShowNestPropertyErrors() {
		return showNestPropertyErrors;
	}

	/**
	 * ネストプロパティの検証エラーメッセージをまとめて表示を設定します。
	 * @param showNestPropertyErrors ネストプロパティの検証エラーメッセージをまとめて表示
	 */
	public void setShowNestPropertyErrors(boolean showNestPropertyErrors) {
		this.showNestPropertyErrors = showNestPropertyErrors;
	}

	/**
	 * プロパティを取得します。
	 * @return プロパティ
	 */
	public List<NestProperty> getProperties() {
		if (properties == null) properties = new ArrayList<NestProperty>();
	    return properties;
	}

	/**
	 * プロパティを設定します。
	 * @param properties プロパティ
	 */
	public void setProperties(List<NestProperty> properties) {
	    this.properties = properties;
	}

	public void addProperty(NestProperty property) {
		getProperties().add(property);
	}

	@Override
	public Enum<?> getDisplayType() {
		return JoinDisplayType.JOIN;
	}

	public List<NestProperty> getJoinProperties() {
		List<NestProperty> nestProperties = new ArrayList<NestProperty>();
		NestProperty property = new NestProperty();
		property.setPropertyName(propertyName);
		property.setEditor(getEditor());

		nestProperties.add(property);
		nestProperties.addAll(properties);
		return nestProperties;
	}

	@Override
	public String getEntityName() {
		return objectName;
	}
}
