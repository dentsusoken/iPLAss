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

package org.iplass.mtp.view.generic.element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.FieldOrder;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * テンプレート要素
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/TemplateElement.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
@FieldOrder(manual=true)
public class TemplateElement extends Element {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6221150116523804929L;

	/**
	 * デフォルトコンストラクタ
	 */
	public TemplateElement() {
	}

	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_TemplateElement_hideDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			description="詳細編集の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_TemplateElement_hideDetailDescriptionKey"
	)
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	@MetaFieldInfo(
			displayName="詳細表示非表示設定",
			displayNameKey="generic_element_TemplateElement_hideViewDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="詳細表示の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_TemplateElement_hideViewDescriptionKey"
	)
	private boolean hideView;




	/** タイトル */
	@MetaFieldInfo(
			displayName="タイトル",
			description="ヘッダに表示するタイトルを設定します。",
			displayNameKey="generic_element_TemplateElement_titleDisplaNameKey",
			descriptionKey="generic_element_TemplateElement_titleDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTitleList",
			displayOrder=300
	)
	@MultiLang()
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_TemplateElement_localizedTitleListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	private List<LocalizedStringDefinition> localizedTitleList;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_element_TemplateElement_styleDisplaNameKey",
			displayOrder=320,
			description="スタイルシートのクラス名を指定します。" +
					"複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_element_TemplateElement_styleDescriptionKey"
	)
	private String style;

	/** ツールチップ */
	@MetaFieldInfo(
			displayName="ツールチップ",
			displayNameKey="generic_element_TemplateElement_tooltipDisplaNameKey",
			description="ツールチップに表示する説明を設定します。",
			descriptionKey="generic_element_TemplateElement_tooltipDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTooltipList",
			displayOrder=420
	)
	@MultiLang()
	private String tooltip;

	/** ツールチップの多言語設定情報 */
	@MetaFieldInfo(
			displayName="ツールチップの多言語設定",
			displayNameKey="generic_element_TemplateElement_localizedTooltipListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=430
	)
	private List<LocalizedStringDefinition> localizedTooltipList;

	/** 必須属性表示タイプ */
	@MetaFieldInfo(
			displayName="必須属性表示",
			displayNameKey="generic_element_TemplateElement_requiredDisplayTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=RequiredDisplayType.class,
			displayOrder=440,
			description="詳細画面で必須表示を行うかを設定します。<BR />" +
					"DEFAULT : プロパティ定義の必須設定に従って必須属性を表示<BR />" +
					"DISPLAY : 必須属性を表示<BR />" +
					"NONE    : 必須属性を表示しない",
			descriptionKey="generic_element_TemplateElement_requiredDisplayTypeDescriptionKey"
	)
	private RequiredDisplayType requiredDisplayType;




	/** テンプレート名 */
	@MetaFieldInfo(
			displayName="テンプレート名",
			displayNameKey="generic_element_TemplateElement_templateNameDisplaNameKey",
			inputType=InputType.TEMPLATE,
			required=true,
			displayOrder=1000,
			description="表示時に読み込むテンプレートの名前を設定します",
			descriptionKey="generic_element_TemplateElement_templateNameDescriptionKey"
	)
	private String templateName;

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
	 * タイトルを取得します。
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * タイトルを設定します。
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedTitleList() {
		return localizedTitleList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedTitleList(List<LocalizedStringDefinition> localizedTitleList) {
		this.localizedTitleList = localizedTitleList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedTitle(LocalizedStringDefinition localizedTitle) {
		if (localizedTitleList == null) {
			localizedTitleList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedTitleList.add(localizedTitle);
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
	 * テンプレート名を取得します。
	 * @return テンプレート名
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * テンプレート名を設定します。
	 * @param templateName テンプレート名
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
