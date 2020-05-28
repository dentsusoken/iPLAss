/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic.element.section;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.HasNestProperty;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.PagingPosition;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.editor.NestProperty;

/**
 * 大量データ用参照セクション
 * @author lis3wg
 *
 */
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/section/MassReferenceSection.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class MassReferenceSection extends Section implements HasNestProperty {

	private static final long serialVersionUID = -5068125265820445100L;

	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum MassReferenceEditType {
		DETAIL,
		VIEW
	}

	/** 参照先Entity定義名 */
	private String defintionName;

	/** プロパティ名 */
	private String propertyName;

	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_section_MassReferenceSection_hideDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			description="詳細編集で非表示にするかを設定します。",
			descriptionKey="generic_element_section_MassReferenceSection_hideDetailDescriptionKey"
	)
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	@MetaFieldInfo(
			displayName="詳細表示非表示設定",
			displayNameKey="generic_element_section_MassReferenceSection_hideViewDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="詳細表示で非表示にするかを設定します。",
			descriptionKey="generic_element_section_MassReferenceSection_hideViewDescriptionKey"
	)
	private boolean hideView;




	/** タイトル */
	@MetaFieldInfo(
			displayName="タイトル",
			displayNameKey="generic_element_section_MassReferenceSection_titleDisplaNameKey",
			description="セクションのタイトルを設定します。",
			descriptionKey="generic_element_section_MassReferenceSection_titleDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTitleList",
			displayOrder=300
	)
	@MultiLang()
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_section_MassReferenceSection_localizedTitleListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	private List<LocalizedStringDefinition> localizedTitleList;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_element_section_MassReferenceSection_styleDisplaNameKey",
			displayOrder=320,
			description="スタイルシートのクラス名を指定します。複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_element_section_MassReferenceSection_styleDescriptionKey"
	)
	private String style;




	/** id */
	@MetaFieldInfo(
			displayName="id",
			displayNameKey="generic_element_section_MassReferenceSection_idDisplaNameKey",
			displayOrder=400,
			description="画面上で一意となるIDを設定してください。",
			descriptionKey="generic_element_section_MassReferenceSection_idDescriptionKey"
	)
	private String id;

	/** セクションの展開可否 */
	@MetaFieldInfo(
			displayName="初期表示時に展開",
			displayNameKey="generic_element_section_MassReferenceSection_expandableDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=410,
			description="セクションを初期展開するかを指定します。",
			descriptionKey="generic_element_section_MassReferenceSection_expandableDescriptionKey"
	)
	private boolean expandable;

	/** リンクを表示するか */
	@MetaFieldInfo(
			displayName="リンクを表示するか",
			displayNameKey="generic_element_section_MassReferenceSection_showLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=420,
			description="詳細画面でのページ内リンクを表示するかを指定します。",
			descriptionKey="generic_element_section_MassReferenceSection_showLinkDescriptionKey"
	)
	private boolean showLink;




	/** 上限値 */
	@MetaFieldInfo(
			displayName="上限値",
			displayNameKey="generic_element_section_MassReferenceSection_limitDisplaNameKey",
			inputType=InputType.NUMBER,
			minRange=1,
			displayOrder=1000,
			description="一度に表示する件数の上限値を設定します。",
			descriptionKey="generic_element_section_MassReferenceSection_limitDescriptionKey"
	)
	private int limit;

	/** 削除ボタン非表示設定 */
	@MetaFieldInfo(
			displayName="削除ボタン非表示",
			displayNameKey="generic_element_section_MassReferenceSection_hideDeleteButtonDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1010,
			description="データを削除するボタンを非表示にします。",
			descriptionKey="generic_element_section_MassReferenceSection_hideDeleteButtonDescriptionKey"
	)
	private boolean hideDeleteButton;

	/** 追加ボタン非表示設定 */
	@MetaFieldInfo(
			displayName="追加ボタン非表示",
			displayNameKey="generic_element_section_MassReferenceSection_hideAddButtonDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1020,
			description="データを追加するボタンを非表示にします。",
			descriptionKey="generic_element_section_MassReferenceSection_hideAddButtonDescriptionKey"
	)
	private boolean hideAddButton;




	/** ページング非表示設定 */
	@MetaFieldInfo(
			displayName="ページング非表示設定",
			displayNameKey="generic_element_section_MassReferenceSection_hidePagingDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2000,
			description="参照の一覧のページングを非表示にします。<br>" +
					"非表示にした場合はページングが行えないため、対象データを全件取得します。",
			descriptionKey="generic_element_section_MassReferenceSection_hidePagingDescriptionKey"
	)
	private boolean hidePaging;

	/** 件数非表示設定 */
	@MetaFieldInfo(
			displayName="件数非表示設定",
			displayNameKey="generic_element_section_MassReferenceSection_hideCountDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2010,
			description="参照の一覧のページングで件数を非表示にします。",
			descriptionKey="generic_element_section_MassReferenceSection_hideCountDescriptionKey"
	)
	private boolean hideCount;

	/** ページジャンプ非表示設定 */
	@MetaFieldInfo(
			displayName="ページジャンプ非表示設定",
			displayNameKey="generic_element_section_MassReferenceSection_hidePageJumpDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2020,
			description="参照の一覧のページングでページジャンプを非表示にします。",
			descriptionKey="generic_element_section_MassReferenceSection_hidePageJumpDescriptionKey"
	)
	private boolean hidePageJump;

	/** ページリンク非表示設定 */
	@MetaFieldInfo(
			displayName="ページリンク非表示設定",
			displayNameKey="generic_element_section_MassReferenceSection_hidePageLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2030,
			description="参照の一覧のページングでページリンクを非表示にします。",
			descriptionKey="generic_element_section_MassReferenceSection_hidePageLinkDescriptionKey"
	)
	private boolean hidePageLink;

	/** 検索アイコンを常に表示 */
	@MetaFieldInfo(
			displayName="検索アイコンを常に表示",
			displayNameKey="generic_element_section_MassReferenceSection_showSearchBtnDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2035,
			description="検索アイコンを常に表示にします。",
			descriptionKey="generic_element_section_MassReferenceSection_showSearchBtnDescriptionKey"
	)
	private boolean showSearchBtn;

	/** ページング表示位置 */
	@MetaFieldInfo(
			displayName="ページング表示位置",
			displayNameKey="generic_element_section_MassReferenceSection_pagingPositionDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=PagingPosition.class,
			displayOrder=2040,
			description="ページングの表示位置を指定します。<br>" +
					"<b>BOTH   :</b> グリッドの上下<br>" +
					"<b>TOP    :</b> グリッドの上部<br>" +
					"<b>BOTTOM :</b> グリッドの下部<br>",
			descriptionKey="generic_element_section_MassReferenceSection_pagingPositionDescriptionKey"
	)
	private PagingPosition pagingPosition;




	/** 表示プロパティ */
	@MetaFieldInfo(displayName="参照型の表示プロパティ",
			displayNameKey="generic_element_section_MassReferenceSection_propertiesDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=NestProperty.class,
			multiple=true,
			displayOrder=3000,
			description="参照セクションに表示するプロパティを設定します。",
			descriptionKey="generic_element_section_MassReferenceSection_propertiesDescriptionKey"
	)
	private List<NestProperty> properties;

	/** 編集タイプ */
	@MetaFieldInfo(
			displayName="編集タイプ",
			displayNameKey="generic_element_section_MassReferenceSection_editTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=MassReferenceEditType.class,
			displayOrder=3010,
			description="参照型の編集方法を指定します。<br>" +
					"<b>DETAIL :</b> 詳細編集画面で編集<br>" +
					"<b>VIEW   :</b> 詳細表示画面で編集<br>",
			descriptionKey="generic_element_section_MassReferenceSection_editTypeDescriptionKey"
	)
	private MassReferenceEditType editType;

	/** 編集リンクを詳細リンクに変更 */
	@MetaFieldInfo(
			displayName="編集リンクを詳細リンクに変更",
			displayNameKey="generic_element_section_MassReferenceSection_changeEditLinkToViewLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=3020,
			description="編集リンクを詳細リンクに変更します。",
			descriptionKey="generic_element_section_MassReferenceSection_changeEditLinkToViewLinkDescriptionKey"
	)
	private boolean changeEditLinkToViewLink;




	/** 絞り込み条件設定スクリプト */
	@MetaFieldInfo(
			displayName="絞り込み条件設定スクリプト",
			displayNameKey="generic_element_section_MassReferenceSection_filterConditionScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="groovy_script",
			displayOrder=4000,
			description="参照データ検索時に自動で絞り込みをする条件を指定するGroovyScriptです。",
			descriptionKey="generic_element_section_MassReferenceSection_filterConditionScriptDescriptionKey"
	)
	private String filterConditionScript;

	/** ソート設定 */
	@MetaFieldInfo(
			displayName="ソート設定",
			displayNameKey="generic_element_section_MassReferenceSection_sortSettingDisplayNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=SortSetting.class,
			multiple=true,
			displayOrder=4010,
			description="検索時にデフォルトで設定されるソート条件を設定します。<br>" +
					"検索画面でソートが行われた場合、設定された内容は2番目以降のソート条件として機能します。",
			descriptionKey="generic_element_section_MassReferenceSection_sortSettingDescriptionKey"
	)
	private List<SortSetting> sortSetting;




	/** ダイアログ表示アクション名 */
	@MetaFieldInfo(
			displayName="ダイアログ表示アクション名",
			displayNameKey="generic_element_section_MassReferenceSection_viewActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=5000,
			description="詳細表示時に表示リンククリックで実行されるアクションを設定します。",
			descriptionKey="generic_element_section_MassReferenceSection_viewActionNameDescriptionKey"
	)
	private String viewActionName;

	/** ダイアログ編集アクション名 */
	@MetaFieldInfo(
			displayName="ダイアログ編集アクション名",
			displayNameKey="generic_element_section_MassReferenceSection_detailActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=5010,
			description="詳細編集時に編集リンク・追加ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_element_section_MassReferenceSection_detailActionNameDescriptionKey"
	)
	private String detailActionName;

	/** ビュー定義名 */
	@MetaFieldInfo(
			displayName="ビュー定義名",
			displayNameKey="generic_element_section_MassReferenceSection_viewNameDisplaNameKey",
			displayOrder=5020,
			description="編集リンク押下で表示する画面のView定義名を設定します。<br>" +
					"未指定の場合はデフォルトのView定義を使用します。",
			descriptionKey="generic_element_section_MassReferenceSection_viewNameDescriptionKey"
	)
	private String viewName;




	/** 上部のコンテンツ */
	@MetaFieldInfo(
			displayName="上部のコンテンツ",
			displayNameKey="generic_element_section_MassReferenceSection_upperContentsDisplaNameKey",
			description="セクションの上部に表示するコンテンツを設定します。<br>" +
					"コンテンツの内容にHTMLタグを利用することも可能です。",
			descriptionKey="generic_element_section_MassReferenceSection_upperContentsDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="groovytemplate",
			displayOrder=6000
	)
	private String upperContents;

	/** 下部のコンテンツ */
	@MetaFieldInfo(
			displayName="下部のコンテンツ",
			displayNameKey="generic_element_section_MassReferenceSection_lowerContentsDisplaNameKey",
			description="セクションの下部に表示するコンテンツを設定します。<br>" +
					"コンテンツの内容にHTMLタグを利用することも可能です。",
			descriptionKey="generic_element_section_MassReferenceSection_lowerContentsDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="groovytemplate",
			displayOrder=6010
	)
	private String lowerContents;

	/** 絞り込み条件設定スクリプトのキー(内部用) */
	private String filterScriptKey;

	/** 上下コンテンツスクリプトのキー(内部用) */
	private String contentScriptKey;

	/**
	 * 参照先Entity定義名を取得します。
	 * @return Entity定義名
	 */
	public String getDefintionName() {
	    return defintionName;
	}

	/**
	 * 参照先Entity定義名を設定します。
	 * @param defintionName Entity定義名
	 */
	public void setDefintionName(String defintionName) {
	    this.defintionName = defintionName;
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
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedTitleList() {
	    return localizedTitleList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedTitleList 多言語設定情報
	 */
	public void setLocalizedTitleList(List<LocalizedStringDefinition> localizedTitleList) {
	    this.localizedTitleList = localizedTitleList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedTitleList(LocalizedStringDefinition localizedTitle) {
		if (localizedTitleList == null) {
			localizedTitleList = new ArrayList<>();
		}

		localizedTitleList.add(localizedTitle);
	}

	/**
	 * セクションの展開可否を取得します。
	 * @return セクションの展開可否
	 */
	public boolean isExpandable() {
	    return expandable;
	}

	/**
	 * セクションの展開可否を設定します。
	 * @param expandable セクションの展開可否
	 */
	public void setExpandable(boolean expandable) {
	    this.expandable = expandable;
	}

	/**
	 * idを取得します。
	 * @return id
	 */
	public String getId() {
	    return id;
	}

	/**
	 * idを設定します。
	 * @param id id
	 */
	public void setId(String id) {
	    this.id = id;
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
	 * リンクを表示するかを取得します。
	 * @return リンクを表示するか
	 */
	@Override
	public boolean isShowLink() {
	    return showLink;
	}

	/**
	 * リンクを表示するかを設定します。
	 * @param showLink リンクを表示するか
	 */
	public void setShowLink(boolean showLink) {
	    this.showLink = showLink;
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
	 * 上部のコンテンツを取得します。
	 * @return 上部のコンテンツ
	 */
	public String getUpperContents() {
	    return upperContents;
	}

	/**
	 * 上部のコンテンツを設定します。
	 * @param upperContents 上部のコンテンツ
	 */
	public void setUpperContents(String upperContents) {
	    this.upperContents = upperContents;
	}

	/**
	 * 下部のコンテンツを取得します。
	 * @return 下部のコンテンツ
	 */
	public String getLowerContents() {
	    return lowerContents;
	}

	/**
	 * 下部のコンテンツを設定します。
	 * @param lowerContents 下部のコンテンツ
	 */
	public void setLowerContents(String lowerContents) {
	    this.lowerContents = lowerContents;
	}

	/**
	 * 上限値を取得します。
	 * @return 上限値
	 */
	public int getLimit() {
	    return limit;
	}

	/**
	 * 上限値を設定します。
	 * @param limit 上限値
	 */
	public void setLimit(int limit) {
	    this.limit = limit;
	}

	/**
	 * ダイアログ表示アクション名を取得します。
	 * @return ダイアログ表示アクション名
	 */
	public String getViewActionName() {
	    return viewActionName;
	}

	/**
	 * ダイアログ表示アクション名を設定します。
	 * @param viewActionName ダイアログ表示アクション名
	 */
	public void setViewActionName(String viewActionName) {
	    this.viewActionName = viewActionName;
	}

	/**
	 * ダイアログ編集アクション名を取得します。
	 * @return ダイアログ編集アクション名
	 */
	public String getDetailActionName() {
	    return detailActionName;
	}

	/**
	 * ダイアログ編集アクション名を設定します。
	 * @param detailActionName ダイアログ編集アクション名
	 */
	public void setDetailActionName(String detailActionName) {
	    this.detailActionName = detailActionName;
	}

	/**
	 * ビュー定義名を取得します。
	 * @return ビュー定義名
	 */
	public String getViewName() {
	    return viewName;
	}

	/**
	 * ビュー定義名を設定します。
	 * @param viewName ビュー定義名
	 */
	public void setViewName(String viewName) {
	    this.viewName = viewName;
	}

	/**
	 * 編集リンクを詳細リンクに変更するかを取得します。
	 * @return 編集リンクを詳細リンクに変更するか
	 */
	public boolean isChangeEditLinkToViewLink() {
		return changeEditLinkToViewLink;
	}

	/**
	 * 編集リンクを詳細リンクに変更するかを設定します。
	 * @param changeEditLinkToViewLink 編集リンクを詳細リンクに変更するか
	 */
	public void setChangeEditLinkToViewLink(boolean changeEditLinkToViewLink) {
		this.changeEditLinkToViewLink = changeEditLinkToViewLink;
	}

	/**
	 * 削除ボタン非表示設定を取得します。
	 * @return 削除ボタン非表示設定
	 */
	public boolean isHideDeleteButton() {
	    return hideDeleteButton;
	}

	/**
	 * 削除ボタン非表示設定を設定します。
	 * @param hideDeleteButton 削除ボタン非表示設定
	 */
	public void setHideDeleteButton(boolean hideDeleteButton) {
	    this.hideDeleteButton = hideDeleteButton;
	}

	/**
	 * 追加ボタン非表示設定を取得します。
	 * @return 追加ボタン非表示設定
	 */
	public boolean isHideAddButton() {
	    return hideAddButton;
	}

	/**
	 * 追加ボタン非表示設定を設定します。
	 * @param hideAddButton 追加ボタン非表示設定
	 */
	public void setHideAddButton(boolean hideAddButton) {
	    this.hideAddButton = hideAddButton;
	}

	/**
	 * ページング非表示設定を取得します。
	 * @return ページング非表示設定
	 */
	public boolean isHidePaging() {
	    return hidePaging;
	}

	/**
	 * ページング非表示設定を設定します。
	 * @param hidePaging ページング非表示設定
	 */
	public void setHidePaging(boolean hidePaging) {
	    this.hidePaging = hidePaging;
	}

	/**
	 * 件数非表示設定を取得します。
	 * @return 件数非表示設定
	 */
	public boolean isHideCount() {
	    return hideCount;
	}

	/**
	 * 件数非表示設定を設定します。
	 * @param hideCount 件数非表示設定
	 */
	public void setHideCount(boolean hideCount) {
	    this.hideCount = hideCount;
	}

	/**
	 * ページジャンプ非表示設定を取得します。
	 * @return ページジャンプ非表示設定
	 */
	public boolean isHidePageJump() {
	    return hidePageJump;
	}

	/**
	 * ページジャンプ非表示設定を設定します。
	 * @param hidePageJump ページジャンプ非表示設定
	 */
	public void setHidePageJump(boolean hidePageJump) {
	    this.hidePageJump = hidePageJump;
	}

	/**
	 * 検索アイコンを常に表示設定を設定します。
	 * @return 検索アイコンを常に表示
	 */
	public boolean isShowSearchBtn() {
		return showSearchBtn;
	}

	/**
	 * 検索アイコンを常に表示設定を取得します。
	 * @param showSearchBtn 検索アイコンを常に表示
	 */
	public void setShowSearchBtn(boolean showSearchBtn) {
		this.showSearchBtn = showSearchBtn;
	}

	/**
	 * ページリンク非表示設定を取得します。
	 * @return ページリンク非表示設定
	 */
	public boolean isHidePageLink() {
	    return hidePageLink;
	}

	/**
	 * ページリンク非表示設定を設定します。
	 * @param hidePageLink ページリンク非表示設定
	 */
	public void setHidePageLink(boolean hidePageLink) {
	    this.hidePageLink = hidePageLink;
	}

	/**
	 * ページング表示位置を取得します。
	 * @return ページング表示位置
	 */
	public PagingPosition getPagingPosition() {
	    return pagingPosition;
	}

	/**
	 * ページング表示位置を設定します。
	 * @param pagingPosition ページング表示位置
	 */
	public void setPagingPosition(PagingPosition pagingPosition) {
	    this.pagingPosition = pagingPosition;
	}

	/**
	 * 編集タイプを取得します。
	 * @return 編集タイプ
	 */
	public MassReferenceEditType getEditType() {
	    return editType;
	}

	/**
	 * 編集タイプを設定します。
	 * @param editType 編集タイプ
	 */
	public void setEditType(MassReferenceEditType editType) {
	    this.editType = editType;
	}

	/**
	 * 表示プロパティを取得します。
	 * @return 表示プロパティ
	 */
	public List<NestProperty> getProperties() {
		if (properties == null) properties = new ArrayList<>();
	    return properties;
	}

	/**
	 * 表示プロパティを設定します。
	 * @param properties 表示プロパティ
	 */
	public void setProperties(List<NestProperty> properties) {
	    this.properties = properties;
	}

	/**
	 * 表示プロパティを追加します。
	 * @param property 表示プロパティ
	 */
	public void addProperty(NestProperty property) {
		getProperties().add(property);
	}

	/**
	 * ソート設定を取得します。
	 * @return ソート設定
	 */
	public List<SortSetting> getSortSetting() {
		if (sortSetting == null) sortSetting = new ArrayList<>();
		return sortSetting;
	}

	/**
	 * ソート設定を設定します。
	 * @param sortSetting ソート設定
	 */
	public void setSortSetting(List<SortSetting> sortSetting) {
		this.sortSetting = sortSetting;
	}

	public void addSortSetting(SortSetting setting) {
		getSortSetting().add(setting);
	}

	/**
	 * 絞り込み条件設定スクリプトを取得します。
	 * @return 絞り込み条件設定スクリプト
	 */
	public String getFilterConditionScript() {
	    return filterConditionScript;
	}

	/**
	 * 絞り込み条件設定スクリプトを設定します。
	 * @param filterConditionScript 絞り込み条件設定スクリプト
	 */
	public void setFilterConditionScript(String filterConditionScript) {
	    this.filterConditionScript = filterConditionScript;
	}

	/**
	 * 絞り込み条件設定スクリプトのキーを取得します。
	 * @return 絞り込み条件設定スクリプトのキー
	 */
	public String getFilterScriptKey() {
		return filterScriptKey;
	}

	/**
	 * 絞り込み条件設定スクリプトのキーを設定します。
	 * @param filterScriptKey 絞り込み条件設定スクリプトのキー
	 */
	public void setFilterScriptKey(String filterScriptKey) {
		this.filterScriptKey = filterScriptKey;
	}

	/**
	 * 上下コンテンツスクリプトのキーを取得します。
	 * @return 上下コンテンツスクリプトのキー
	 */
	public String getContentScriptKey() {
		return contentScriptKey;
	}

	/**
	 * 上下コンテンツスクリプトのキーを設定します。
	 * @param contentScriptKey 上下コンテンツスクリプトのキー
	 */
	public void setContentScriptKey(String contentScriptKey) {
		this.contentScriptKey = contentScriptKey;
	}

}
