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

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

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
public class StringPropertyEditor extends PrimitivePropertyEditor implements LabelablePropertyEditor {

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
		@XmlEnumValue("Label")LABEL,
		@XmlEnumValue("Hidden")HIDDEN
	}

	/** RichTextライブラリ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum RichTextLibrary {
		@XmlEnumValue("Quill")QUILL,
		@XmlEnumValue("CKEditor")CKEDITOR
	}

	/** 表示タイプ */
	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_editor_StringPropertyEditor_displayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=StringDisplayType.class,
			required=true,
			displayOrder=100,
			description="画面に表示する方法を選択します。",
			descriptionKey="generic_editor_StringPropertyEditor_displayTypeDescriptionKey"
	)
	private StringDisplayType displayType;

	/** 入力タイプ */
	@MetaFieldInfo(
			displayName="入力タイプ",
			displayNameKey="generic_editor_StringPropertyEditor_inputTypeDisplaNameKey",
			inputType=InputType.TEXT,
			displayOrder=102,
			rangeCheck=true,
			minRange=0,
			description="表示タイプがTEXTの場合の、inputのtype属性を設定します。<br>" +
					"未指定の場合はtextになります。",
			descriptionKey="generic_editor_StringPropertyEditor_inputTypeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String inputType;

	/** 入力パターン */
	@MetaFieldInfo(
			displayName="入力パターン",
			displayNameKey="generic_editor_StringPropertyEditor_inputPatternDisplaNameKey",
			inputType=InputType.TEXT,
			displayOrder=104,
			rangeCheck=true,
			minRange=0,
			description="表示タイプがTEXT、PASSWORDの場合の、inputのpattern属性を設定します。",
			descriptionKey="generic_editor_StringPropertyEditor_inputPatternDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String inputPattern;

	/** HTML入力エラーメッセージ */
	@MetaFieldInfo(
			displayName="HTML入力エラーメッセージ",
			displayNameKey="generic_editor_StringPropertyEditor_htmlValidationMessageDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=HtmlValidationMessage.class,
			displayOrder=106,
			description="inputのtype、patternに不一致の場合のメッセージを設定します。",
			descriptionKey="generic_editor_StringPropertyEditor_htmlValidationMessageDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	@MultiLang(isMultiLangValue=false)
	private HtmlValidationMessage htmlValidationMessage;

	/** 最大文字数 */
	@MetaFieldInfo(
			displayName="最大文字数",
			displayNameKey="generic_editor_StringPropertyEditor_maxlengthDisplaNameKey",
			inputType=InputType.NUMBER,
			displayOrder=110,
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
			multiple=true,
			displayOrder=120,
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
			displayOrder=130,
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
			inputType=InputType.CHECKBOX,
			displayOrder=140,
			description="チェック時は完全一致検索します。<br>未チェック時はLike検索します。",
			descriptionKey="generic_editor_StringPropertyEditor_searchExactMatchConditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean searchExactMatchCondition;
	
	/** 「値なし」を検索条件の選択肢に追加するか */
	@MetaFieldInfo(
			displayName="「値なし」を検索条件の選択肢に追加するか",
			displayNameKey="generic_editor_StringPropertyEditor_isNullSearchEnabledDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=145,	
			description="「値なし」を検索条件の選択肢に追加するかを指定します。値なしが選択された場合、IS NULLを検索条件として指定します。",
			descriptionKey="generic_editor_StringPropertyEditor_isNullSearchEnabledDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean isNullSearchEnabled;

	/** RichTextライブラリ */
	@MetaFieldInfo(displayName="RichTextライブラリ",
			displayNameKey="generic_editor_StringPropertyEditor_richTextLibraryDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=RichTextLibrary.class,
			displayOrder=145,
			description="RichText選択時のみ有効となります。RichTextライブラリを選択します。",
			descriptionKey="generic_editor_StringPropertyEditor_richTextLibraryDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private RichTextLibrary richTextLibrary;

	/** RickTextで表示モードの場合、ツールバーなどを表示しないか */
	@MetaFieldInfo(
			displayName="RickTextで表示モードの場合、ツールバーなどを表示しないか",
			displayNameKey="generic_editor_StringPropertyEditor_hideRichtextEditorToolBarDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=150,
			description="RickTextで表示モードの場合、ツールバーなどを表示しないかを設定します。",
			descriptionKey="generic_editor_StringPropertyEditor_hideRichtextEditorToolBarDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideRichtextEditorToolBar = true;

	/** リッチテキストエディタオプション */
	@MetaFieldInfo(
			displayName="リッチテキストエディタオプション",
			displayNameKey="generic_editor_StringPropertyEditor_richtextEditorOptionDisplaNameKey",
			inputType=InputType.SCRIPT,
			displayOrder=155,
			mode="javascript",
			description="リッチテキストエディタを生成する際のオプションを指定します。<br>"
					+ "指定可能なオプションについては CKEDITOR config を参照してください。",
			descriptionKey="generic_editor_StringPropertyEditor_richtextEditorOptionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String richtextEditorOption;


	/** RichText表示タグ許可設定 */
	@MetaFieldInfo(
			displayName="RichText表示時にタグを許可",
			displayNameKey="generic_editor_StringPropertyEditor_allowedContentDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=160,
			description="RichText選択時のみ有効となります。<br>チェック時は「ソース」ボタンクリックにてスクリプトも表示されるようになります。",
			descriptionKey="generic_editor_StringPropertyEditor_allowedContentDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean allowedContent;

	/** RickTextで表示モードの場合、リンク動作許可 */
	@MetaFieldInfo(
			displayName="RichText表示時にリンク動作を許可",
			displayNameKey="generic_editor_StringPropertyEditor_allowRichTextEditorLinkActionDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=165,
			description="RichText選択時のみ有効となります。リッチテキストエディタで許可されていないリンク動作を利用できるようにします。",
			descriptionKey="generic_editor_StringPropertyEditor_allowRichTextEditorLinkActionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean allowRichTextEditorLinkAction = true;

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

	@Override
	public boolean isHide() {
		return displayType == StringDisplayType.HIDDEN;
	}

	/**
	 * 入力タイプを取得します。
	 * @return 入力タイプ
	 */
	public String getInputType() {
		return inputType;
	}

	/**
	 * 入力タイプを設定します。
	 * @param inputType 入力タイプ
	 */
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	/**
	 * 入力パターンを取得します。
	 * @return 入力パターン
	 */
	public String getInputPattern() {
		return inputPattern;
	}

	/**
	 * 入力パターンを設定します。
	 * @param inputPattern 入力パターン
	 */
	public void setInputPattern(String inputPattern) {
		this.inputPattern = inputPattern;
	}

	/**
	 * HTML入力エラーメッセージを取得します。
	 * @return HTML入力エラーメッセージ
	 */
	public HtmlValidationMessage getHtmlValidationMessage() {
		return htmlValidationMessage;
	}

	/**
	 * HTML入力エラーメッセージを設定します。
	 * @param htmlValidationMessage HTML入力エラーメッセージ
	 */
	public void setHtmlValidationMessage(HtmlValidationMessage htmlValidationMessage) {
		this.htmlValidationMessage = htmlValidationMessage;
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
		if (this.values == null) {
			this.values = new ArrayList<>();
		}
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
	 * RichTextライブラリを取得します。
	 *
	 * @return RichTextライブラリ
	 */
	public RichTextLibrary getRichTextLibrary() {
		return richTextLibrary;
	}

	/**
	 * RichTextライブラリを設定します。
	 *
	 * @param richTextLibrary RichTextライブラリ
	 */
	public void setRichTextLibrary(RichTextLibrary richTextLibrary) {
		this.richTextLibrary = richTextLibrary;
	}

	/**
	 * RichText表示タグ許可を取得します。
	 *
	 * @return RichText表示タグ許可設定
	 */
	public boolean isAllowedContent() {
		return allowedContent;
	}

	/**
	 * RichText表示タグ許可設定を設定します。
	 *
	 * @param allowedContent RichText表示タグ許可設定
	 */
	public void setAllowedContent(boolean allowedContent) {
		this.allowedContent = allowedContent;
	}

	/**
	 * RickTextで表示モードの場合、リンク動作許可を取得します。
	 *
	 * @return RickTextで表示モードの場合、リンク動作許可
	 */
	public boolean isAllowRichTextEditorLinkAction() {
		return allowRichTextEditorLinkAction;
	}

	/**
	 * RickTextで表示モードの場合、リンク動作許可を設定します。
	 *
	 * @param allowRichTextEditorLinkAction RickTextで表示モードの場合、リンク動作許可
	 */
	public void setAllowRichTextEditorLinkAction(boolean allowRichTextEditorLinkAction) {
		this.allowRichTextEditorLinkAction = allowRichTextEditorLinkAction;
	}

	/**
	 * RickTextで表示モードの場合、ツールバーなどを表示しないかを取得します。
	 * @return RickTextで表示モードの場合、ツールバーなどを表示しないか
	 */
	public boolean isHideRichtextEditorToolBar() {
		return hideRichtextEditorToolBar;
	}

	/**
	 * RickTextで表示モードの場合、ツールバーなどを表示しないかを設定します。
	 * @param hideRichtextEditorToolBar RickTextで表示モードの場合、ツールバーなどを表示しないか
	 */
	public void setHideRichtextEditorToolBar(boolean hideRichtextEditorToolBar) {
		this.hideRichtextEditorToolBar = hideRichtextEditorToolBar;
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
	public boolean isLabel() {
		return displayType == StringDisplayType.LABEL;
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
	 * 「値なし」を検索条件の選択肢に追加するかを取得します。
	 * @return 「値なし」を検索条件の選択肢に追加するか
	 */
	public boolean isIsNullSearchEnabled() {
		return isNullSearchEnabled;
	}

	/**
	 * 「値なし」を検索条件の選択肢に追加するかを設定します。
	 * @param isNullSearchEnabled 「値なし」を検索条件の選択肢に追加するか
	 */
	public void setIsNullSearchEnabled(boolean isNullSearchEnabled) {
		this.isNullSearchEnabled = isNullSearchEnabled;
	}
}
