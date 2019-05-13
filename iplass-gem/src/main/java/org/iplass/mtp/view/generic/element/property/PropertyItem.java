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

package org.iplass.mtp.view.generic.element.property;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.element.property.validation.ViewValidatorBase;

/**
 * プロパティ情報
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/property/Property.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class PropertyItem extends PropertyBase {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 9163069385726194724L;

	/** 説明 */
	@MetaFieldInfo(
			displayName="説明",
			displayNameKey="generic_element_property_PropertyItem_descriptionDisplaNameKey",
			description="入力欄下部表示する説明を設定します。",
			descriptionKey="generic_element_property_PropertyItem_descriptionDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedDescriptionList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	@MultiLang(itemNameGetter = "getPropertyName", isUseSuperForItemName = true)
	private String description;

	/** 説明の多言語設定情報 */
	@MetaFieldInfo(
			displayName="説明の多言語設定",
			displayNameKey="generic_element_property_PropertyItem_localizedDescriptionListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private List<LocalizedStringDefinition> localizedDescriptionList;

	/** ツールチップ */
	@MetaFieldInfo(
			displayName="ツールチップ",
			displayNameKey="generic_element_property_PropertyItem_tooltipDisplaNameKey",
			description="ツールチップに表示する説明を設定します。",
			descriptionKey="generic_element_property_PropertyItem_tooltipDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTooltipList"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	@MultiLang(itemNameGetter = "getPropertyName", isUseSuperForItemName = true)
	private String tooltip;

	/** ツールチップの多言語設定情報 */
	@MetaFieldInfo(
			displayName="ツールチップの多言語設定",
			displayNameKey="generic_element_property_PropertyItem_localizedTooltipListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private List<LocalizedStringDefinition> localizedTooltipList;

	/** 通常検索非表示設定 */
	@MetaFieldInfo(
			displayName="通常検索非表示設定",
			displayNameKey="generic_element_property_PropertyItem_hideNormalConditionDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="通常検索の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_property_PropertyItem_hideNormalConditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean hideNormalCondition;

	/** 詳細検索非表示設定 */
	@MetaFieldInfo(
			displayName="詳細検索非表示設定",
			displayNameKey="generic_element_property_PropertyItem_hideDetailConditionDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="詳細検索の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_property_PropertyItem_hideDetailConditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean hideDetailCondition;

	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_property_PropertyItem_hideDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="詳細編集の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_property_PropertyItem_hideDetailDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	@MetaFieldInfo(
			displayName="詳細表示非表示設定",
			displayNameKey="generic_element_property_PropertyItem_hideViewDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="詳細表示の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_property_PropertyItem_hideViewDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideView;

	/** 必須属性表示タイプ */
	@MetaFieldInfo(
			displayName="必須属性表示",
			displayNameKey="generic_element_property_PropertyItem_requiredDisplayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=RequiredDisplayType.class,
			description="詳細画面で必須表示を行うかを設定します。<BR />" +
					"DEFAULT : プロパティ定義の必須設定に従って必須属性を表示<BR />" +
					"DISPLAY : 必須属性を表示<BR />" +
					"NONE    : 必須属性を表示しない",
			descriptionKey="generic_element_property_PropertyItem_requiredDisplayTypeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private RequiredDisplayType requiredDisplayType;

	/** 通常検索でブランク項目扱いにするか */
	private boolean isBlank;

	/** 通常検索で必須条件にする */
	@MetaFieldInfo(
			displayName="通常検索で必須条件にする",
			displayNameKey="generic_element_property_PropertyItem_requiredNormalDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="通常検索で必須条件にするかを設定します。",
			descriptionKey="generic_element_property_PropertyItem_requiredNormalDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean requiredNormal;

	/** 詳細検索で必須条件にする */
	@MetaFieldInfo(
			displayName="詳細検索で必須条件にする",
			displayNameKey="generic_element_property_PropertyItem_requiredDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="詳細検索で必須条件にするかを設定します。",
			descriptionKey="generic_element_property_PropertyItem_requiredDetailDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean requiredDetail;

	/** 入力チェック */
	@MetaFieldInfo(
			displayName="入力チェック",
			displayNameKey="generic_element_property_PropertyItem_validatorDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=ViewValidatorBase.class,
			description="入力チェックを設定します。",
			descriptionKey="generic_element_property_PropertyItem_validatorDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	@MultiLang(isMultiLangValue=false)
	private ViewValidatorBase validator;

	/**
	 * デフォルトコンストラクタ
	 */
	public PropertyItem() {
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
	 * 通常検索非表示設定を取得します。
	 * @return 通常検索非表示設定
	 */
	public boolean isHideNormalCondition() {
		return hideNormalCondition;
	}

	/**
	 * 通常検索非表示設定を設定します。
	 * @param hideNormalCondition 通常検索非表示設定
	 */
	public void setHideNormalCondition(boolean hideNormalCondition) {
		this.hideNormalCondition = hideNormalCondition;
	}

	/**
	 * 詳細検索非表示設定を取得します。
	 * @return 詳細検索非表示設定
	 */
	public boolean isHideDetailCondition() {
		return hideDetailCondition;
	}

	/**
	 * 詳細検索非表示設定を設定します。
	 * @param hideDetailCondition 詳細検索非表示設定
	 */
	public void setHideDetailCondition(boolean hideDetailCondition) {
		this.hideDetailCondition = hideDetailCondition;
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
	 * 通常検索でブランク項目扱いにするかを取得します。
	 * @return 通常検索でブランク項目扱いにするか
	 */
	public boolean isBlank() {
		return isBlank;
	}

	/**
	 * 通常検索でブランク項目扱いにするかを設定します。
	 * @param isBlank 通常検索でブランク項目扱いにするか
	 */
	public void setBlank(boolean isBlank) {
		this.isBlank = isBlank;
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
			localizedDescriptionList = new ArrayList<LocalizedStringDefinition>();
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
			localizedTooltipList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedTooltipList.add(localizedTooltip);
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
	 * 入力チェックを取得します。
	 * @return 入力チェック
	 */
	public ViewValidatorBase getValidator() {
	    return validator;
	}

	/**
	 * 入力チェックを設定します。
	 * @param validator 入力チェック
	 */
	public void setValidator(ViewValidatorBase validator) {
	    this.validator = validator;
	}

}
