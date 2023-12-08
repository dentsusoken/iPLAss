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

package org.iplass.mtp.view.generic.element.section;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.IgnoreField;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.PagingPosition;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.element.Element;

/**
 * 検索結果を保持するセクション
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/section/SearchResultSection.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
@IgnoreField({"dispFlag", "displayScript"})
public class SearchResultSection extends Section {

	/** 一括更新の排他制御起点 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum ExclusiveControlPoint {

		/** 更新ダイアログが開く時 */
		WHEN_DIALOG_OPEN,

		/** 検索実行時 */
		WHEN_SEARCH
	}

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -700624023196624864L;

	/** 要素 */
	private List<Element> elements;

	/** 一括削除コミットトランザクション制御設定 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum DeleteAllCommandTransactionType {
		ONCE, DIVISION
	}

	/** 一括更新コミットトランザクション制御設定 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum BulkUpdateAllCommandTransactionType {
		ONCE, DIVISION
	}

	/** 検索結果の表示行数 */
	@MetaFieldInfo(
			displayName="検索結果の表示件数",
			displayNameKey="generic_element_section_SearchResultSection_dispRowCountDisplaNameKey",
			inputType=InputType.NUMBER,
			rangeCheck=true,
			minRange=0,
			displayOrder=200,
			description="検索結果の一覧に表示する件数を指定します。",
			descriptionKey="generic_element_section_SearchResultSection_dispRowCountDescriptionKey"
	)
	private Integer dispRowCount;

	@MetaFieldInfo(
			displayName="検索結果の高さ",
			displayNameKey="generic_element_section_SearchResultSection_dispHeightDisplaNameKey",
			inputType=InputType.NUMBER,
			rangeCheck=true,
			minRange=0,
			displayOrder=210,
			description="検索結果の高さを指定します。<br>" +
						"0を指定した場合、高さを自動とします。",
			descriptionKey="generic_element_section_SearchResultSection_dispHeightDescriptionKey"
	)
	private int dispHeight;

	/** 検索結果をまとめる */
	@MetaFieldInfo(
			displayName="検索結果をまとめる設定",
			displayNameKey="generic_element_section_SearchResultSection_groupingDataDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=215,
			description="検索結果をまとめて表示します。<br>" +
					"OIDとVersionが同じであるレコードをまとめます。",
			descriptionKey="generic_element_section_SearchResultSection_groupingDataDescriptionKey"
	)
	private boolean groupingData;

	/** 編集リンク非表示設定 */
	@MetaFieldInfo(
			displayName="編集リンク非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hideDetailLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=220,
			description="検索結果の編集リンクを非表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_hideDetailLinkDescriptionKey"
	)
	private boolean hideDetailLink;

	/** Entity権限の可能範囲条件で編集リンク表示を制御 */
	@MetaFieldInfo(
			displayName="Entity権限の可能範囲条件で編集リンク表示を制御",
			displayNameKey="generic_element_section_SearchResultSection_checkEntityPermissionLimitConditionOfEditLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=225,
			description="レコード単位でEntity権限の可能範囲条件をチェックし編集リンクの表示を制御します。",
			descriptionKey="generic_element_section_SearchResultSection_checkEntityPermissionLimitConditionOfEditLinkDescriptionKey"
	)
	private boolean checkEntityPermissionLimitConditionOfEditLink;

	/** 削除ボタン非表示設定 */
	@MetaFieldInfo(
			displayName="削除ボタン非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hideDeleteDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=230,
			description="削除ボタンと検索結果のチェックボックスを非表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_hideDeleteDescriptionKey"
	)
	private boolean hideDelete;

	/** ページング非表示設定 */
	@MetaFieldInfo(
			displayName="ページング非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hidePagingDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=240,
			description="参照の一覧のページングを非表示にします。<br>" +
					"非表示にした場合はページングが行えないため、対象データを全件取得します。",
			descriptionKey="generic_element_section_SearchResultSection_hidePagingDescriptionKey"
	)
	private boolean hidePaging;

	/** 件数非表示設定 */
	@MetaFieldInfo(
			displayName="件数非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hideCountDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=250,
			description="検索結果のページングで件数を非表示にします。<br>" +
					"非表示にした場合、全件数はカウントされません。<br>" +
					"また、ページジャンプ、ページリンクも非表示になります。",
			descriptionKey="generic_element_section_SearchResultSection_hideCountDescriptionKey"
	)
	private boolean hideCount;

	/** ページジャンプ非表示設定 */
	@MetaFieldInfo(
			displayName="ページジャンプ非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hidePageJumpDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=260,
			description="検索結果のページングでページジャンプを非表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_hidePageJumpDescriptionKey"
	)
	private boolean hidePageJump;

	/** ページリンク非表示設定 */
	@MetaFieldInfo(
			displayName="ページリンク非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hidePageLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=270,
			description="検索結果のページングでページリンクを非表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_hidePageLinkDescriptionKey"
	)
	private boolean hidePageLink;

	/** ページング表示位置 */
	@MetaFieldInfo(
			displayName="ページング表示位置",
			displayNameKey="generic_element_section_SearchResultSection_pagingPositionDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=PagingPosition.class,
			displayOrder=280,
			description="ページングの表示位置を指定します。<br>" +
					"<b>BOTH   :</b> グリッドの上下<br>" +
					"<b>TOP    :</b> グリッドの上部<br>" +
					"<b>BOTTOM :</b> グリッドの下部<br>",
			descriptionKey="generic_element_section_SearchResultSection_pagingPositionDescriptionKey"
	)
	private PagingPosition pagingPosition;

	/** 一括更新ボタン表示設定 */
	@MetaFieldInfo(
			displayName="一括更新ボタン表示設定",
			displayNameKey="generic_element_section_SearchResultSection_showBulkUpdateDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=290,
			description="一括更新ボタンを表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_showBulkUpdateDescriptionKey"
	)
	private boolean showBulkUpdate;

	@MetaFieldInfo(
			displayName="Bulk Viewの定義を利用",
			displayNameKey="generic_element_section_SearchResultSection_useBulkViewDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=295,
			description="Bulk Viewの定義を利用します。",
			descriptionKey="generic_element_section_SearchResultSection_useBulkViewDescriptionKey"
	)
	private boolean useBulkView;

	@MetaFieldInfo(
			displayName="一括更新の排他制御起点",
			displayNameKey="generic_element_section_SearchResultSection_exclusiveControlPointDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=ExclusiveControlPoint.class,
			displayOrder=296,
			description="一括更新の排他制御起点。<br>" +
					"<b>WHEN_DIALOG_OPEN :</b> 更新ダイアログが開く時<br>" +
					"<b>WHEN_SEARCH      :</b> 検索実行時<br>",
			descriptionKey="generic_element_section_SearchResultSection_exclusiveControlPointDescriptionKey"
	)
	private ExclusiveControlPoint exclusiveControlPoint = ExclusiveControlPoint.WHEN_DIALOG_OPEN;

	/** 一括更新ボタン表示ラベル */
	@MetaFieldInfo(
			displayName="一括更新ボタン表示ラベル",
			displayNameKey="generic_element_section_SearchResultSection_bulkUpdateDisplayLabelDisplaNameKey",
			description="一括更新ボタンに表示されるラベルを設定します。",
			descriptionKey="generic_element_section_SearchResultSection_bulkUpdateDisplayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedBulkUpdateDisplayLabel",
			displayOrder=300
	)
	@MultiLang(
			multiLangGetter="getLocalizedBulkUpdateDisplayLabel",
			multiLangSetter = "setLocalizedBulkUpdateDisplayLabel"
	)
	private String bulkUpdateDisplayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_section_SearchResultSection_localizedBulkUpdateDisplayLabelDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	private List<LocalizedStringDefinition> localizedBulkUpdateDisplayLabel;

	/** 一括更新デフォルト選択項目 */
	@MetaFieldInfo(
			displayName="一括更新デフォルト選択項目",
			displayNameKey="generic_element_section_SearchResultSection_bulkUpdateDefaultSelectionDisplaNameKey",
			inputType=InputType.PROPERTY,
			displayOrder=320,
			description="<b>一括更新デフォルト選択項目</b><br>" +
					"BulkViewレイアウト定義を利用しない場合、デフォルト選択項目を指定します。",
			descriptionKey="generic_element_section_SearchResultSection_bulkUpdateDefaultSelectionDescriptionKey"
	)
	private String bulkUpdateDefaultSelection;

	/** タイトル */
	@MetaFieldInfo(
			displayName="タイトル",
			displayNameKey="generic_element_section_SearchResultSection_titleDisplayNameKey",
			description="セクションのタイトルを設定します。",
			descriptionKey="generic_element_section_SearchResultSection_titleDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTitleList",
			displayOrder=330
	)
	@MultiLang()
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_section_SearchResultSection_localizedTitleListDisplayNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=340
	)
	private List<LocalizedStringDefinition> localizedTitleList;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_element_section_SearchResultSection_styleDisplayNameKey",
			description="スタイルシートのクラス名を指定します。複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_element_section_SearchResultSection_styleDescriptionKey",
			displayOrder=350
	)
	private String style;

	/** id */
	@MetaFieldInfo(
			displayName="id",
			displayNameKey="generic_element_section_SearchResultSection_idDisplayNameKey",
			displayOrder=400,
			description="画面上で一意となるIDを設定してください。",
			descriptionKey="generic_element_section_SearchResultSection_idDescriptionKey"
	)
	private String id;

	/** 親子関係の参照を物理削除するか */
	@MetaFieldInfo(
			displayName="親子関係の参照を物理削除するか",
			displayNameKey="generic_element_section_SearchResultSection_purgeCompositionedEntityDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1000,
			description="親子関係の参照を物理削除するかを設定します",
			descriptionKey="generic_element_section_SearchResultSection_purgeCompositionedEntityDescriptionKey"
	)
	private boolean purgeCompositionedEntity;

	/** 更新時に強制的に更新処理を行う */
	@MetaFieldInfo(
			displayName="更新時に強制的に更新処理を行う",
			displayNameKey="generic_element_section_SearchResultSection_forceUpadteDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1010,
			description="変更項目が一つもなくとも、強制的に更新処理（更新日時、更新者が更新される）を行います。",
			descriptionKey="generic_element_section_SearchResultSection_forceUpadteDescriptionKey"
	)
	private boolean forceUpadte;

	/** カスタム登録処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタム登録処理クラス名",
			displayNameKey="generic_element_section_SearchResultSection_interrupterNameDisplaNameKey",
			displayOrder=1020,
			description="データ登録時に行うカスタム登録処理のクラス名を指定します。<br>" +
					"RegistrationInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_element_section_SearchResultSection_interrupterNameDescriptionKey"
	)
	private String interrupterName;

	/** カスタムロード処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタムロード処理クラス名",
			displayNameKey="generic_element_section_SearchResultSection_loadEntityInterrupterNameDisplaNameKey",
			displayOrder=1030,
			description="Entityロード処理実行前にロード用のオプションをカスタマイズするためのクラス名を指定します。<br>" +
					"LoadEntityInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_element_section_SearchResultSection_loadEntityInterrupterNameDescriptionKey"
	)
	private String loadEntityInterrupterName;

	/** 一括削除コミットトランザクション制御設定 */
	@MetaFieldInfo(
			displayName="一括削除コミットトランザクション制御設定",
			displayNameKey="generic_element_section_SearchResultSection_deleteAllCommandTransactionTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=DeleteAllCommandTransactionType.class,
			displayOrder=1040,
			description="一括削除時に、全部一斉で処理するか、設定した件数で分けて処理かを指定します。",
			descriptionKey="generic_element_section_SearchConditionSection_deleteAllCommandTransactionTypeDescriptionKey"
	)
	private DeleteAllCommandTransactionType deleteAllCommandTransactionType = DeleteAllCommandTransactionType.DIVISION;

	/** カスタム削除処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタム削除処理クラス名",
			displayNameKey="generic_element_section_SearchResultSection_deleteInterrupterNameDisplaNameKey",
			displayOrder=1045,
			description="データ削除時に行うカスタム削除処理のクラス名を指定します。<br>" +
					"BulkOperationInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_element_section_SearchResultSection_deleteInterrupterNameDescriptionKey"
	)
	private String deleteInterrupterName;

	/** 一括更新コミットトランザクション制御設定 */
	@MetaFieldInfo(
			displayName="一括更新コミットトランザクション制御設定",
			displayNameKey="generic_element_section_SearchResultSection_bulkUpdateAllCommandTransactionTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=BulkUpdateAllCommandTransactionType.class,
			displayOrder=1050,
			description="一括更新時に、全部一斉で処理するか、設定した件数で分けて処理かを指定します。",
			descriptionKey="generic_element_section_SearchConditionSection_bulkUpdateAllCommandTransactionTypeDescriptionKey"
	)
	private BulkUpdateAllCommandTransactionType bulkUpdateAllCommandTransactionType = BulkUpdateAllCommandTransactionType.DIVISION;

	/** カスタム一括更新処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタム一括更新処理クラス名",
			displayNameKey="generic_element_section_SearchResultSection_bulkUpdateInterrupterNameDisplaNameKey",
			displayOrder=1055,
			description="データ一括更新時に行うカスタム更新処理のクラス名を指定します。<br>" +
					"BulkOperationInterrupterインターフェースを実装するクラスを指定してください。",

			descriptionKey="generic_element_section_SearchResultSection_bulkUpdateInterrupterNameDescriptionKey"
	)
	private String bulkUpdateInterrupterName;

	/** カスタムスタイルキー */
	private String scriptKey;


	/**
	 * デフォルトコンストラクタ
	 */
	public SearchResultSection() {
	}

	/**
	 * 要素を取得します。
	 * @return 要素
	 */
	public List<Element> getElements() {
		if (elements == null) elements = new ArrayList<>();
		return elements;
	}

	/**
	 * 要素を設定します。
	 * @param elements 要素
	 */
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	/**
	 * 検索結果の表示行数を取得します。
	 * @return 検索結果の表示行数
	 */
	public Integer getDispRowCount() {
		return dispRowCount;
	}

	/**
	 * 検索結果の表示行数を設定します。
	 * @param dispRowCount 検索結果の表示行数
	 */
	public void setDispRowCount(Integer dispRowCount) {
		this.dispRowCount = dispRowCount;
	}


	/**
	 * 検索結果の高さを取得します。
	 * @return 検索結果の高さ
	 */
	public int getDispHeight() {
		return dispHeight;
	}

	/**
	 * 検索結果の高さを設定します。
	 * @param dispHeight 検索結果の高さ
	 */
	public void setDispHeight(int dispHeight) {
		this.dispHeight = dispHeight;
	}

	/**
	 * 検索結果をまとめる設定を取得します。
	 * @return 検索結果をまとめる設定
	 */
	public boolean isGroupingData() {
		return groupingData;
	}

	/**
	 * 検索結果をまとめる設定を設定します。
	 * @param groupingData 検索結果をまとめる設定
	 */
	public void setGroupingData(boolean groupingData) {
		this.groupingData = groupingData;
	}

	/**
	 * 編集リンク非表示設定を取得します。
	 * @return 編集リンク非表示設定
	 */
	public boolean isHideDetailLink() {
	    return hideDetailLink;
	}

	/**
	 * 編集リンク非表示設定を設定します。
	 * @param hideDetailLink 編集リンク非表示設定
	 */
	public void setHideDetailLink(boolean hideDetailLink) {
	    this.hideDetailLink = hideDetailLink;
	}

	/**
	 * Entity権限の可能範囲条件で編集リンク表示を制御設定を取得します。
	 * @return Entity権限の可能範囲条件で編集リンク表示を制御設定
	 */
	public boolean isCheckEntityPermissionLimitConditionOfEditLink() {
		return checkEntityPermissionLimitConditionOfEditLink;
	}

	/**
	 * Entity権限の可能範囲条件で編集リンク表示を制御設定を設定します。
	 * @param checkEntityPermissionLimitConditionOfEditLink Entity権限の可能範囲条件で編集リンク表示を制御設定
	 */
	public void setCheckEntityPermissionLimitConditionOfEditLink(boolean checkEntityPermissionLimitConditionOfEditLink) {
		this.checkEntityPermissionLimitConditionOfEditLink = checkEntityPermissionLimitConditionOfEditLink;
	}

	/**
	 * 削除ボタン非表示設定を取得します。
	 * @return 削除ボタン非表示設定
	 */
	public boolean isHideDelete() {
	    return hideDelete;
	}

	/**
	 * 削除ボタン非表示設定を設定します。
	 * @param hideDelete 削除ボタン非表示設定
	 */
	public void setHideDelete(boolean hideDelete) {
	    this.hideDelete = hideDelete;
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
	 * 要素を追加します。
	 * @param val プロパティ情報
	 */
	public void addElement(Element val) {
		getElements().add(val);
	}

	@Override
	public boolean isShowLink() {
		return false;
	}

	/**
	 * 一括更新ボタン表示設定を取得します。
	 * @return 一括更新ボタン表示設定
	 */
	public boolean isShowBulkUpdate() {
		return showBulkUpdate;
	}

	/**
	 * 一括更新ボタン表示設定を設定します。
	 * @param showBulkUpdate 一括更新ボタン表示設定
	 */
	public void setShowBulkUpdate(boolean showBulkUpdate) {
		this.showBulkUpdate = showBulkUpdate;
	}

	/**
	 * Bulk Viewの定義を利用を取得します。
	 * @return Bulk Viewの定義を利用
	 */
	public boolean isUseBulkView() {
		return useBulkView;
	}

	/**
	 * Bulk Viewの定義を利用を設定します。
	 * @param useBulkView Bulk Viewの定義を利用
	 */
	public void setUseBulkView(boolean useBulkView) {
		this.useBulkView = useBulkView;
	}

	/**
	 * 一括更新の排他制御起点を取得します。
	 * @return 一括更新の排他制御起点
	 */
	public ExclusiveControlPoint getExclusiveControlPoint() {
		return exclusiveControlPoint;
	}

	/**
	 * 一括更新の排他制御起点を設定します。
	 * @param exclusiveControlPoint 一括更新の排他制御起点
	 */
	public void setExclusiveControlPoint(ExclusiveControlPoint exclusiveControlPoint) {
		this.exclusiveControlPoint = exclusiveControlPoint;
	}

	/**
	 * 一括更新ボタン表示ラベルを取得します。
	 * @return 一括更新ボタンボタン表示ラベル
	 */
	public String getBulkUpdateDisplayLabel() {
		return bulkUpdateDisplayLabel;
	}

	/**
	 * 一括更新ボタンボタン表示ラベルを設定します。
	 * @param bulkUpdateDisplayLabel 一括更新ボタンボタン表示ラベル
	 */
	public void setBulkUpdateDisplayLabel(String bulkUpdateDisplayLabel) {
		this.bulkUpdateDisplayLabel = bulkUpdateDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedBulkUpdateDisplayLabel() {
		return localizedBulkUpdateDisplayLabel;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedBulkUpdateDisplayLabel 多言語設定情報
	 */
	public void setLocalizedBulkUpdateDisplayLabel(List<LocalizedStringDefinition> localizedBulkUpdateDisplayLabel) {
		this.localizedBulkUpdateDisplayLabel = localizedBulkUpdateDisplayLabel;
	}

	/**
	 * 一括更新デフォルト選択項目を取得します。
	 * @return 一括更新デフォルト選択項目
	 */
	public String getBulkUpdateDefaultSelection() {
		return bulkUpdateDefaultSelection;
	}

	/**
	 * 一括更新デフォルト選択項目を設定します。
	 * @param bulkUpdateDefaultSelection 一括更新デフォルト選択項目
	 */
	public void setBulkUpdateDefaultSelection(String bulkUpdateDefaultSelection) {
		this.bulkUpdateDefaultSelection = bulkUpdateDefaultSelection;
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
	 * @return タイトル
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
	 * 親子関係の参照を物理削除するかを取得します。
	 * @return 親子関係の参照を物理削除するか
	 */
	public boolean isPurgeCompositionedEntity() {
	    return purgeCompositionedEntity;
	}

	/**
	 * 親子関係の参照を物理削除するかを設定します。
	 * @param purgeCompositionedEntity 親子関係の参照を物理削除するか
	 */
	public void setPurgeCompositionedEntity(boolean purgeCompositionedEntity) {
	    this.purgeCompositionedEntity = purgeCompositionedEntity;
	}

	/**
	 * 更新時に強制的に更新処理を行うかを取得します。
	 * @return forceUpdate 更新時に強制的に更新処理を行うか
	 */
	public boolean isForceUpadte() {
		return forceUpadte;
	}

	/**
	 * 更新時に強制的に更新処理を行うかを設定します。
	 * @param forceUpadte 更新時に強制的に更新処理を行うか
	 */
	public void setForceUpadte(boolean forceUpadte) {
		this.forceUpadte = forceUpadte;
	}

	/**
	 * カスタムスタイルのキーを取得します。
	 * @return カスタムスタイルのキー
	 */
	public String getScriptKey() {
		return scriptKey;
	}

	/**
	 * カスタムスタイルのキーを設定します。
	 * @param scriptKey カスタムスタイルのキー
	 */
	public void setScriptKey(String scriptKey) {
		this.scriptKey = scriptKey;
	}

	/**
	 * カスタム登録処理クラス名を取得します。
	 * @return カスタム登録処理クラス名
	 */
	public String getInterrupterName() {
	    return interrupterName;
	}

	/**
	 * カスタム登録処理クラス名を設定します。
	 * @param interrupterName カスタム登録処理クラス名
	 */
	public void setInterrupterName(String interrupterName) {
	    this.interrupterName = interrupterName;
	}

	/**
	 * カスタムロード処理クラス名を取得します。
	 * @return カスタムロード処理クラス名
	 */
	public String getLoadEntityInterrupterName() {
	    return loadEntityInterrupterName;
	}

	/**
	 * カスタムロード処理クラス名を設定します。
	 * @param loadEntityInterrupterName カスタムロード処理クラス名
	 */
	public void setLoadEntityInterrupterName(String loadEntityInterrupterName) {
	    this.loadEntityInterrupterName = loadEntityInterrupterName;
	}

	/**
	 * カスタム削除処理クラス名を取得します。
	 * @return カスタム削除処理クラス名
	 */
	public String getDeleteInterrupterName() {
		return deleteInterrupterName;
	}

	/**
	 * カスタム削除処理クラス名を設定します。
	 * @param deleteInterrupterName カスタム削除処理クラス名
	 */
	public void setDeleteInterrupterName(String deleteInterrupterName) {
		this.deleteInterrupterName = deleteInterrupterName;
	}

	/**
	 * カスタム一括更新処理クラス名を取得します。
	 * @return カスタム一括更新処理クラス名
	 */
	public String getBulkUpdateInterrupterName() {
		return bulkUpdateInterrupterName;
	}

	/**
	 * カスタム一括更新処理クラス名を設定します。
	 * @param bulkUpdateInterrupterName カスタム一括更新処理クラス名
	 */
	public void setBulkUpdateInterrupterName(String bulkUpdateInterrupterName) {
		this.bulkUpdateInterrupterName = bulkUpdateInterrupterName;
	}

	public DeleteAllCommandTransactionType getDeleteAllCommandTransactionType() {
		return deleteAllCommandTransactionType;
	}

	public void setDeleteAllCommandTransactionType(DeleteAllCommandTransactionType deleteAllCommandTransactionType) {
		this.deleteAllCommandTransactionType = deleteAllCommandTransactionType;
	}

	public BulkUpdateAllCommandTransactionType getBulkUpdateAllCommandTransactionType() {
		return bulkUpdateAllCommandTransactionType;
	}

	public void setBulkUpdateAllCommandTransactionType(
			BulkUpdateAllCommandTransactionType bulkUpdateAllCommandTransactionType) {
		this.bulkUpdateAllCommandTransactionType = bulkUpdateAllCommandTransactionType;
	}

}
