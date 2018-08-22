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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * 文字列型プロパティエディタ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({LongTextPropertyEditor.class})
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/StringPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/StringPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class StringPropertyEditor extends PrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -366476037921678501L;

	/** 表示タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum StringDisplayType {
		@XmlEnumValue("Text")TEXT,
		@XmlEnumValue("TextArea")TEXTAREA,
		@XmlEnumValue("RichText")RICHTEXT,
		@XmlEnumValue("Password")PASSWORD,
		@XmlEnumValue("Select")SELECT,
		@XmlEnumValue("Label")LABEL
	}

	/** 表示タイプ */
	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_StringPropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=StringDisplayType.class,
			required=true,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_StringPropertyEditor_displayTypeDescriptionKey"
	)
	private StringDisplayType displayType;

	/** RichText表示タグ許可設定 */
	@MetaFieldInfo(
			displayName="RichText表示タグ許可設定",
			displayNameKey="generic_editor_StringPropertyEditor_allowedContentDisplaNameKey",
			description="RichText選択時のみ有効となります。<br>チェック時は「ソース」ボタンクリックにてスクリプトも表示されるようになります。",
			inputType=InputType.CHECKBOX,
			descriptionKey="generic_editor_StringPropertyEditor_allowedContentDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean allowedContent;

	/** 最大文字数 */
	@MetaFieldInfo(
			displayName="最大文字数",
			displayNameKey="generic_editor_StringPropertyEditor_maxlengthDisplaNameKey",
			inputType=InputType.NUMBER,
			rangeCheck=true,
			minRange=0,
			description="テキストフィールドに入力可能な最大文字数を設定します。<br>" +
					"0の場合は適用されず、1以上の値を設定した場合に有効になります。",
			descriptionKey="generic_editor_StringPropertyEditor_maxlengthDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private int maxlength;

	/** 選択値 */
	@MetaFieldInfo(
			displayName="選択値",
			displayNameKey="generic_editor_StringPropertyEditor_valuesDisplaNameKey",
			inputType=InputType.REFERENCE,
			required=true,
			multiple=true,
			referenceClass=EditorValue.class
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	@MultiLang(isMultiLangValue = false)
	private List<EditorValue> values;

	/** 初期値 */
	@MetaFieldInfo(
			displayName="初期値",
			displayNameKey="generic_editor_StringPropertyEditor_defaultValueDisplaNameKey",
			description="新規作成時の初期値を設定します。",
			descriptionKey="generic_editor_StringPropertyEditor_defaultValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String defaultValue;

	/** 検索条件完全一致設定 */
	@MetaFieldInfo(
			displayName="検索完全一致設定",
			displayNameKey="generic_editor_StringPropertyEditor_searchExactMatchConditionDisplaNameKey",
			description="チェック時は完全一致検索します。<br>未チェック時はLike検索します。",
			inputType=InputType.CHECKBOX,
			descriptionKey="generic_editor_StringPropertyEditor_searchExactMatchConditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean searchExactMatchCondition;

	/** リッチテキストエディタオプション */
	@MetaFieldInfo(
			displayName="リッチテキストエディタオプション",
			displayNameKey="generic_editor_StringPropertyEditor_richtextEditorOptionDisplaNameKey",
			description="リッチテキストエディタを生成する際のオプションを指定します。<br>"
					+ "指定可能なオプションについては CKEDITOR config を参照してください。",
			descriptionKey="generic_editor_StringPropertyEditor_richtextEditorOptionDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="javascript"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String richtextEditorOption;

	/**
	 * コンストラクタ
	 */
	public StringPropertyEditor() {
	}

	@Override
	public StringDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを取得します
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(StringDisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * RichText表示タグ許可を設定します。
	 *
	 * @return allowedContent RichText表示タグ許可設定
	 */
	public boolean isAllowedContent() {
		return allowedContent;
	}

	/**
	 * RichText表示タグ許可設定を取得します。
	 *
	 * @param RichText表示タグ許可設定
	 */
	public void setAllowedContent(boolean allowedContent) {
		this.allowedContent = allowedContent;
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

	/**
	 * 選択値を取得します。
	 * @return 選択値
	 */
	public List<EditorValue> getValues() {
		if (this.values == null) this.values = new ArrayList<EditorValue>();
		return values;
	}

	/**
	 * 選択値を設定します。
	 * @param values 選択値
	 */
	public void setValues(List<EditorValue> values) {
		this.values = values;
	}

	/**
	 * 選択値を追加します。
	 * @param val 選択値
	 */
	public void addValue(EditorValue val) {
		getValues().add(val);
	}

	/**
	 * 検索条件完全一致を設定します。
	 *
	 * @return searchExactMatchCondition 検索条件完全一致設定
	 */
	public boolean isSearchExactMatchCondition() {
		return searchExactMatchCondition;
	}

	/**
	 * 検索条件完全一致設定を取得します。
	 *
	 * @param 検索条件完全一致設定
	 */
	public void setSearchExactMatchCondition(boolean searchExactMatchCondition) {
		this.searchExactMatchCondition = searchExactMatchCondition;
	}

	/**
	 * リッチテキストエディタオプションを取得します。
	 * @return リッチテキストエディタオプション
	 */
	public String getRichtextEditorOption() {
	    return richtextEditorOption;
	}

	/**
	 * リッチテキストエディタオプションを設定します。
	 * @param richtextEditorOption リッチテキストエディタオプション
	 */
	public void setRichtextEditorOption(String richtextEditorOption) {
	    this.richtextEditorOption = richtextEditorOption;
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
