/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic.element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.FieldOrder;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.TextAlign;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.FloatPropertyEditor;
import org.iplass.mtp.view.generic.editor.IntegerPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.TemplatePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyElement;

/**
 * 詳細表示で表示可能な仮想プロパティ
 * @author lis3wg
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/VirtualProperty.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
@FieldOrder(manual=true)
public class VirtualPropertyItem extends Element implements PropertyElement, CsvItem {

	private static final long serialVersionUID = 8040377351280643403L;

	/** プロパティ名 */
	@MetaFieldInfo(
			displayName="プロパティ名",
			displayNameKey="generic_element_VirtualPropertyItem_propertyNameDisplaNameKey",
			description="プロパティ名を設定します。実際に存在するプロパティ名は指定しないでください。",
			descriptionKey="generic_element_VirtualPropertyItem_propertyNameDescriptionKey",
			required=true,
			displayOrder=190
	)
	private String propertyName;




	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_VirtualPropertyItem_hideDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			description="詳細編集の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_VirtualPropertyItem_hideDetailDescriptionKey"
	)
	@EntityViewField(referenceTypes=FieldReferenceType.DETAIL)
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	@MetaFieldInfo(
			displayName="詳細表示非表示設定",
			displayNameKey="generic_element_VirtualPropertyItem_hideViewDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="詳細表示の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_VirtualPropertyItem_hideViewDescriptionKey"
	)
	@EntityViewField(referenceTypes=FieldReferenceType.DETAIL)
	private boolean hideView;




	/** 画面表示時のラベル */
	@MetaFieldInfo(
			displayName="表示ラベル",
			displayNameKey="generic_element_VirtualPropertyItem_displayLabelDisplaNameKey",
			description="画面に表示するラベルを設定します。",
			descriptionKey="generic_element_VirtualPropertyItem_displayLabelDescriptionKey",
			required=true,
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedDisplayLabelList",
			displayOrder=300
	)
	@MultiLang(itemNameGetter = "getPropertyName")
	private String displayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_VirtualPropertyItem_localizedDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	private List<LocalizedStringDefinition> localizedDisplayLabelList;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_element_VirtualPropertyItem_styleDisplaNameKey",
			displayOrder=320,
			description="スタイルシートのクラス名を指定します。" +
					"複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_element_VirtualPropertyItem_styleDescriptionKey"
	)
	private String style;




	/** 説明 */
	@MetaFieldInfo(
			displayName="説明",
			displayNameKey="generic_element_VirtualPropertyItem_descriptionDisplaNameKey",
			description="入力欄下部表示する説明を設定します。",
			descriptionKey="generic_element_VirtualPropertyItem_descriptionDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedDescriptionList",
			displayOrder=400
	)
	@EntityViewField(referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.SEARCHCONDITION})
	@MultiLang(itemNameGetter = "getPropertyName")
	private String description;

	/** 説明の多言語設定情報 */
	@MetaFieldInfo(
			displayName="説明の多言語設定",
			displayNameKey="generic_element_VirtualPropertyItem_localizedDescriptionListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=410
	)
	@EntityViewField(referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.SEARCHCONDITION})
	private List<LocalizedStringDefinition> localizedDescriptionList;

	/** ツールチップ */
	@MetaFieldInfo(
			displayName="ツールチップ",
			displayNameKey="generic_element_VirtualPropertyItem_tooltipDisplaNameKey",
			description="ツールチップに表示する説明を設定します。",
			descriptionKey="generic_element_VirtualPropertyItem_tooltipDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTooltipList",
			displayOrder=420
	)
	@EntityViewField(referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.SEARCHCONDITION})
	@MultiLang(itemNameGetter = "getPropertyName")
	private String tooltip;

	/** ツールチップの多言語設定情報 */
	@MetaFieldInfo(
			displayName="ツールチップの多言語設定",
			displayNameKey="generic_element_VirtualPropertyItem_localizedTooltipListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=430
	)
	@EntityViewField(referenceTypes={FieldReferenceType.DETAIL, FieldReferenceType.SEARCHCONDITION})
	private List<LocalizedStringDefinition> localizedTooltipList;

	/** 必須属性表示タイプ */
	@MetaFieldInfo(
			displayName="必須属性表示",
			displayNameKey="generic_element_VirtualPropertyItem_requiredDisplayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=RequiredDisplayType.class,
			displayOrder=440,
			description="詳細画面で必須表示を行うかを設定します。<BR />" +
					"DEFAULT : 必須属性を表示しない<BR />" +
					"DISPLAY : 必須属性を表示<BR />" +
					"NONE    : 必須属性を表示しない",
			descriptionKey="generic_element_VirtualPropertyItem_requiredDisplayTypeDescriptionKey"
	)
	@EntityViewField(referenceTypes={FieldReferenceType.DETAIL})
	private RequiredDisplayType requiredDisplayType;

	/** 列幅 */
	@MetaFieldInfo(
			displayName="列幅",
			displayNameKey="generic_element_property_PropertyColumn_widthDisplaNameKey",
			inputType=InputType.NUMBER,
			displayOrder=450,
			description="列幅を指定します。",
			descriptionKey="generic_element_property_PropertyColumn_widthDescriptionKey"
	)
	@EntityViewField(referenceTypes=FieldReferenceType.SEARCHRESULT)
	private int width;

	/** テキストの配置 */
	@MetaFieldInfo(
			displayName="テキストの配置",
			displayNameKey="generic_element_property_PropertyColumn_textAlignDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=TextAlign.class,
			displayOrder=460,
			description="テキストの配置を指定します。<br>" +
					"LEFT:左寄せ<br>" +
					"CENTER:中央寄せ<br>" +
					"RIGHT:右寄せ",
			descriptionKey="generic_element_property_PropertyColumn_textAlignDescriptionKey"
	)
	@EntityViewField(referenceTypes=FieldReferenceType.SEARCHRESULT)
	private TextAlign textAlign;


	/** CSVの出力 */
	@MetaFieldInfo(
			displayName="CSVに出力する",
			displayNameKey="generic_element_property_PropertyColumn_outputCsvDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=500,
			description="CSVに出力するかを設定します。",
			descriptionKey="generic_element_property_PropertyColumn_outputCsvDescriptionKey"
	)
	@EntityViewField(referenceTypes=FieldReferenceType.SEARCHRESULT)
	private boolean outputCsv = false;


	//参照とかバイナリとか、文字列から直接変換可能な値以外は扱わない
	/** プロパティエディタ */
	@MetaFieldInfo(
			displayName="プロパティエディタ",
			displayNameKey="generic_element_VirtualPropertyItem_editorDisplaNameKey",
			required=true,
			inputType=InputType.REFERENCE,
			referenceClass=PropertyEditor.class,
			fixedReferenceClass={
				BooleanPropertyEditor.class, DatePropertyEditor.class, TimePropertyEditor.class,
				TimestampPropertyEditor.class, DecimalPropertyEditor.class, IntegerPropertyEditor.class,
				FloatPropertyEditor.class, SelectPropertyEditor.class, StringPropertyEditor.class,
				TemplatePropertyEditor.class, UserPropertyEditor.class},
			displayOrder=1000,
			description="表示したい形式に合わせたプロパティエディタを指定してください。",
			descriptionKey="generic_element_VirtualPropertyItem_editorDescriptionKey"
	)
	@MultiLang(itemNameGetter = "getPropertyName")
	private PropertyEditor editor;

	/**
	 * プロパティ名を取得します。
	 * @return プロパティ名
	 */
	@Override
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
	 * 画面表示時のラベルを取得します。
	 * @return 画面表示時のラベル
	 */
	@Override
	public String getDisplayLabel() {
	    return displayLabel;
	}

	/**
	 * 画面表示時のラベルを設定します。
	 * @param displayLabel 画面表示時のラベル
	 */
	public void setDisplayLabel(String displayLabel) {
	    this.displayLabel = displayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	@Override
	public List<LocalizedStringDefinition> getLocalizedDisplayLabelList() {
	    return localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedDisplayLabelList(List<LocalizedStringDefinition> localizedDisplayLabelList) {
	    this.localizedDisplayLabelList = localizedDisplayLabelList;
	}

	/**
	 * クラス名を取得します。
	 * @return クラス名
	 */
	public String getStyle() {
	    return style;
	}

	/**
	 * クラス名を設定します。
	 * @param style クラス名
	 */
	public void setStyle(String style) {
	    this.style = style;
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
	 * 説明の多言語設定情報を取得します。
	 * @return 説明の多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedDescriptionList() {
	    return localizedDescriptionList;
	}

	/**
	 * 説明の多言語設定情報を設定します。
	 * @param localizedDescriptionList 説明の多言語設定情報
	 */
	public void setLocalizedDescriptionList(List<LocalizedStringDefinition> localizedDescriptionList) {
	    this.localizedDescriptionList = localizedDescriptionList;
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
	 * ツールチップの多言語設定情報を取得します。
	 * @return ツールチップの多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedTooltipList() {
	    return localizedTooltipList;
	}

	/**
	 * ツールチップの多言語設定情報を設定します。
	 * @param localizedTooltipList ツールチップの多言語設定情報
	 */
	public void setLocalizedTooltipList(List<LocalizedStringDefinition> localizedTooltipList) {
	    this.localizedTooltipList = localizedTooltipList;
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
	 * @param requiredDisplayType 必須属性表示タイプ
	 */
	public void setRequiredDisplayType(RequiredDisplayType requiredDisplayType) {
	    this.requiredDisplayType = requiredDisplayType;
	}

	/**
	 * プロパティエディタを取得します。
	 * @return プロパティエディタ
	 */
	@Override
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
	 * 多言語設定情報を追加します。
	 * @param localizedDisplayLabelList 多言語設定情報
	 */
	public void addLocalizedDisplayLabel(LocalizedStringDefinition localizedDisplayLabel) {
		if (this.localizedDisplayLabelList == null) this.localizedDisplayLabelList = new ArrayList<>();
	    this.localizedDisplayLabelList.add(localizedDisplayLabel);
	}

	/**
	 * 説明の多言語設定情報を追加します。
	 * @param localizedDescriptionList 説明の多言語設定情報
	 */
	public void addLocalizedDescription(LocalizedStringDefinition localizedDescription) {
		if (this.localizedDescriptionList == null) this.localizedDescriptionList = new ArrayList<>();
	    this.localizedDescriptionList.add(localizedDescription);
	}

	/**
	 * ツールチップの多言語設定情報を追加します。
	 * @param localizedTooltipList ツールチップの多言語設定情報
	 */
	public void addLocalizedTooltip(LocalizedStringDefinition localizedTooltip) {
		if (this.localizedTooltipList == null) this.localizedTooltipList = new ArrayList<>();
	    this.localizedTooltipList.add(localizedTooltip);
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

}
