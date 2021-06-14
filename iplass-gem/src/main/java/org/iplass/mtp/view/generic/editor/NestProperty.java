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

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.TextAlign;
import org.iplass.mtp.view.generic.common.AutocompletionSetting;
import org.iplass.mtp.view.generic.element.CsvItem;

/**
 * 参照型でテーブル表示等の場合に表示する参照先のプロパティ定義
 * @author lis3wg
 */
public class NestProperty implements Refrectable, CsvItem {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7849466975195960549L;

	/** プロパティ名 */
	@MetaFieldInfo(
			displayName="プロパティ名",
			displayNameKey="generic_editor_NestProperty_propertyNameDisplaNameKey",
			displayOrder=100,
			inputType=InputType.PROPERTY,
			required=true,
			description="参照型のプロパティ名を指定してください",
			descriptionKey="generic_editor_NestProperty_propertyNameDescriptionKey",
			childEntityName=true
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String propertyName;

	/** プロパティエディタ */
	@MetaFieldInfo(
			displayName="プロパティエディタ",
			displayNameKey="generic_editor_NestProperty_editorDisplaNameKey",
			required=true,
			displayOrder=200,
			inputType=InputType.REFERENCE,
			referenceClass=PropertyEditor.class,
			description="プロパティの型にあわせたプロパティエディタを選択してください",
			descriptionKey="generic_editor_NestProperty_editorDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	@MultiLang(itemNameGetter = "getPropertyName", isMultiLangValue = false)
	private PropertyEditor editor;

	/** 編集画面非表示設定 */
	@MetaFieldInfo(
			displayName="編集画面で非表示",
			displayNameKey="generic_editor_NestProperty_hideDetailDisplaNameKey",
			displayOrder=300,
			inputType=InputType.CHECKBOX,
			description="編集画面でこの項目を非表示にします。<BR />" +
					"この設定は参照テーブル、参照セクション、大量データ用参照セクションで有効になります。",
			descriptionKey="generic_editor_NestProperty_hideDetailDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideDetail;

	/** 詳細画面非表示設定 */
	@MetaFieldInfo(
			displayName="詳細画面で非表示",
			displayNameKey="generic_editor_NestProperty_hideViewDisplaNameKey",
			displayOrder=310,
			inputType=InputType.CHECKBOX,
			description="詳細画面でこの項目を非表示にします。<BR />" +
					"この設定は参照テーブル、参照セクション、大量データ用参照セクションで有効になります。",
			descriptionKey="generic_editor_NestProperty_hideViewDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideView;

	/** 表示ラベル */
	@MetaFieldInfo(
			displayName="表示ラベル",
			displayNameKey="generic_editor_NestProperty_DisplaNameKey",
			displayOrder=400,
			description="未設定の場合はプロパティに定義された表示名が表示されます",
			descriptionKey="generic_editor_NestProperty_DescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedDisplayLabelList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	@MultiLang(itemNameGetter = "getPropertyName")
	private String displayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_editor_NestProperty_localizedDisplayLabelListDisplaNameKey",
			displayOrder=410,
			inputType=InputType.MULTI_LANG_LIST
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private List<LocalizedStringDefinition> localizedDisplayLabelList;

	/** 説明 */
	@MetaFieldInfo(
			displayName="説明",
			displayNameKey="generic_editor_NestProperty_descriptionDisplaNameKey",
			displayOrder=420,
			description="入力欄下部に表示する説明を設定します。",
			descriptionKey="generic_editor_NestProperty_descriptionDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedDescriptionList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	@MultiLang(itemNameGetter = "getPropertyName")
	private String description;

	/** 説明の多言語設定情報 */
	@MetaFieldInfo(
			displayName="説明の多言語設定",
			displayNameKey="generic_editor_NestProperty_localizedDescriptionListDisplaNameKey",
			displayOrder=430,
			inputType=InputType.MULTI_LANG_LIST
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private List<LocalizedStringDefinition> localizedDescriptionList;

	/** ツールチップ */
	@MetaFieldInfo(
			displayName="ツールチップ",
			displayNameKey="generic_editor_NestProperty_tooltipDisplaNameKey",
			displayOrder=440,
			description="ツールチップに表示する説明を設定します。",
			descriptionKey="generic_editor_NestProperty_tooltipDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTooltipList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	@MultiLang(itemNameGetter = "getPropertyName")
	private String tooltip;

	/** ツールチップの多言語設定情報 */
	@MetaFieldInfo(
			displayName="ツールチップの多言語設定",
			displayNameKey="generic_editor_NestProperty_localizedTooltipListDisplaNameKey",
			displayOrder=450,
			inputType=InputType.MULTI_LANG_LIST
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private List<LocalizedStringDefinition> localizedTooltipList;

	/** 列幅 */
	@MetaFieldInfo(
			displayName="列幅",
			displayNameKey="generic_editor_NestProperty_widthDisplaNameKey",
			displayOrder=500,
			inputType=InputType.NUMBER,
			description="検索結果に表示する際の列幅を指定します。",
			descriptionKey="generic_editor_NestProperty_widthDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT, FieldReferenceType.DETAIL}
	)
	private int width;

	/** テキストの配置 */
	@MetaFieldInfo(
			displayName="テキストの配置",
			displayNameKey="generic_editor_NestProperty_textAlignDisplaNameKey",
			displayOrder=510,
			inputType=InputType.ENUM,
			enumClass=TextAlign.class,
			description="検索結果でのテキストの配置を指定します。<br>" +
					"LEFT:左寄せ<br>" +
					"CENTER:中央寄せ<br>" +
					"RIGHT:右寄せ",
			descriptionKey="generic_editor_NestProperty_textAlignDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT}
	)
	private TextAlign textAlign;

	/** 必須属性表示タイプ */
	@MetaFieldInfo(
			displayName="必須属性表示",
			displayNameKey="generic_editor_NestProperty_requiredDisplayTypeDisplaNameKey",
			displayOrder=600,
			inputType=InputType.ENUM,
			enumClass=RequiredDisplayType.class,
			description="詳細画面で必須表示を行うかを設定します。<BR />" +
					"DEFAULT : プロパティ定義の必須設定に従って必須属性を表示<BR />" +
					"DISPLAY : 必須属性を表示<BR />" +
					"NONE    : 必須属性を表示しない",
			descriptionKey="generic_editor_NestProperty_requiredDisplayTypeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private RequiredDisplayType requiredDisplayType;

	/** 通常検索で必須条件にする */
	@MetaFieldInfo(
			displayName="通常検索で必須条件にする",
			displayNameKey="generic_editor_NestProperty_requiredNormalDisplaNameKey",
			displayOrder=610,
			inputType=InputType.CHECKBOX,
			description="通常検索で必須条件にするかを設定します。",
			descriptionKey="generic_editor_NestProperty_requiredNormalDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean requiredNormal;

	/** 詳細検索で必須条件にする */
	@MetaFieldInfo(
			displayName="詳細検索で必須条件にする",
			displayNameKey="generic_editor_NestProperty_requiredDetailDisplaNameKey",
			displayOrder=620,
			inputType=InputType.CHECKBOX,
			description="詳細検索で必須条件にするかを設定します。",
			descriptionKey="generic_editor_NestProperty_requiredDetailDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean requiredDetail;

	/** CSVの出力 */
	@MetaFieldInfo(
			displayName="CSVに出力する",
			displayNameKey="generic_editor_NestProperty_outputCsvDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=700,
			description="CSVに出力するかを設定します。",
			descriptionKey="generic_editor_NestProperty_outputCsvDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHRESULT}
	)
	private boolean outputCsv = true;

	/** 自動補完設定 */
	@MetaFieldInfo(
			displayName="自動補完設定",
			displayNameKey="generic_element_property_PropertyBase_autocompletionSettingDisplaNameKey",
			displayOrder=800,
			inputType=InputType.REFERENCE,
			referenceClass=AutocompletionSetting.class,
			description="自動補完設定を設定します。",
			descriptionKey="generic_element_property_PropertyBase_autocompletionSettingDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.SEARCHCONDITION}
	)
	private AutocompletionSetting autocompletionSetting;

	/**
	 * コンストラクタ
	 */
	public NestProperty() {
	}

	/**
	 * プロパティ名を取得します。
	 * @return プロパティ名
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * プロパティ名を設定します。
	 * @param propertyName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * 表示名を取得します。
	 * @return 表示名
	 */
	public String getDisplayLabel() {
		return displayLabel;
	}

	/**
	 * 表示名を設定します。
	 * @param displayLabel 表示名
	 */
	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	/**
	 * 説明を取得します。
	 * @return 説明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 説明を設定します。
	 * @param description 説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * ツールチップを取得します。
	 * @return ツールチップ
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * ツールチップを設定します。
	 * @param tooltip ツールチップ
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * 列幅を取得します。
	 * @return 列幅
	 */
	public int getWidth() {
	    return width;
	}

	/**
	 * 列幅を設定します。
	 * @param width 列幅
	 */
	public void setWidth(int width) {
	    this.width = width;
	}

	/**
	 * テキストの配置を取得します。
	 * @return テキストの配置
	 */
	public TextAlign getTextAlign() {
	    return textAlign;
	}

	/**
	 * テキストの配置を設定します。
	 * @param textAlign テキストの配置
	 */
	public void setTextAlign(TextAlign textAlign) {
	    this.textAlign = textAlign;
	}

	/**
	 * 詳細編集非表示設定を取得します。
	 * @return 詳細編集非表示設定
	 */
	public boolean isHideDetail() {
		return hideDetail;
	}

	/**
	 * 詳細編集非表示設定を設定します。
	 * @param hideDetail 詳細編集非表示設定
	 */
	public void setHideDetail(boolean hideDetail) {
		this.hideDetail = hideDetail;
	}

	/**
	 * 詳細表示非表示設定を取得します。
	 * @return 詳細表示非表示設定
	 */
	public boolean isHideView() {
		return hideView;
	}

	/**
	 * 詳細表示非表示設定を設定します。
	 * @param hideView 詳細表示非表示設定
	 */
	public void setHideView(boolean hideView) {
		this.hideView = hideView;
	}

	/**
	 * 必須属性表示タイプを取得します。
	 * @return 必須属性表示タイプ
	 */
	public RequiredDisplayType getRequiredDisplayType() {
		return requiredDisplayType;
	}

	/**
	 * 必須属性表示タイプを設定します。
	 * @param requiredDisplayTypel 必須属性表示タイプ
	 */
	public void setRequiredDisplayType(RequiredDisplayType requiredDisplayType) {
		this.requiredDisplayType = requiredDisplayType;
	}
	/**
	 * 通常検索で必須条件にするを取得します。
	 * @return 通常検索で必須条件にする
	 */
	public boolean isRequiredNormal() {
	    return requiredNormal;
	}

	/**
	 * 通常検索で必須条件にするを設定します。
	 * @param requiredNormal 通常検索で必須条件にする
	 */
	public void setRequiredNormal(boolean requiredNormal) {
	    this.requiredNormal = requiredNormal;
	}

	/**
	 * 詳細検索で必須条件にするを取得します。
	 * @return 詳細検索で必須条件にする
	 */
	public boolean isRequiredDetail() {
	    return requiredDetail;
	}

	/**
	 * 詳細検索で必須条件にするを設定します。
	 * @param requiredDetail 詳細検索で必須条件にする
	 */
	public void setRequiredDetail(boolean requiredDetail) {
	    this.requiredDetail = requiredDetail;
	}

	/**
	 * CSVに出力するかを取得します。
	 * @return CSVに出力するか
	 */
	@Override
	public boolean isOutputCsv() {
		return outputCsv;
	}

	/**
	 * CSVに出力するかを設定します。
	 * @param outputCsv CSVに出力するか
	 */
	public void setOutputCsv(boolean outputCsv) {
		this.outputCsv = outputCsv;
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
	 * 自動補完設定を取得します。
	 * @return autocompletionSetting 自動補完設定
	 */
	public AutocompletionSetting getAutocompletionSetting() {
		return autocompletionSetting;
	}

	/**
	 * 自動補完設定を設定します。
	 * @param autocompletionSetting 自動補完設定
	 */
	public void setAutocompletionSetting(AutocompletionSetting autocompletionSetting) {
		this.autocompletionSetting = autocompletionSetting;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedDisplayLabelList() {
		return localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayLabelList(List<LocalizedStringDefinition> localizedDisplayLabelList) {
		this.localizedDisplayLabelList = localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedTitle(LocalizedStringDefinition localizedTitle) {
		if (localizedDisplayLabelList == null) {
			localizedDisplayLabelList = new ArrayList<>();
		}

		localizedDisplayLabelList.add(localizedTitle);
	}

	/**
	 * 説明の多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedDescriptionList() {
		return localizedDescriptionList;
	}

	/**
	 * 説明の多言語設定情報を設定します。
	 * @param localizedDescriptionList リスト
	 */
	public void setLocalizedDescriptionList(List<LocalizedStringDefinition> localizedDescriptionList) {
		this.localizedDescriptionList = localizedDescriptionList;
	}

	/**
	 * 説明の多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedDescription(LocalizedStringDefinition localizedDescription) {
		if (localizedDescriptionList == null) {
			localizedDescriptionList = new ArrayList<>();
		}

		localizedDescriptionList.add(localizedDescription);
	}

	/**
	 * ツールチップの多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedTooltipList() {
		return localizedTooltipList;
	}

	/**
	 * ツールチップの多言語設定情報を設定します。
	 * @param localizedTooltipList リスト
	 */
	public void setLocalizedTooltipList(List<LocalizedStringDefinition> localizedTooltipList) {
		this.localizedTooltipList = localizedTooltipList;
	}

	/**
	 * ツールチップの多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedTooltip(LocalizedStringDefinition localizedTooltip) {
		if (localizedTooltipList == null) {
			localizedTooltipList = new ArrayList<>();
		}

		localizedTooltipList.add(localizedTooltip);
	}
}
