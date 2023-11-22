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

package org.iplass.mtp.impl.view.generic.editor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.HasMetaNestProperty;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.EditPage;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.InsertType;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefComboSearchType;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.UrlParameterActionType;

/**
 * 参照型プロパティエディタのメタデータ
 * @author lis3wg
 */
public class MetaReferencePropertyEditor extends MetaPropertyEditor implements HasMetaNestProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1640054951421530441L;

	public static MetaReferencePropertyEditor createInstance(PropertyEditor editor) {
		return new MetaReferencePropertyEditor();
	}

	/** 表示タイプ */
	private ReferenceDisplayType displayType;

	/** 選択ダイアログ利用可否 */
	private boolean useSearchDialog;

	/** 検索条件で単一選択 */
	private boolean singleSelect;

	/** プロパティと一緒にネスト項目を条件に利用するか */
	private boolean useNestConditionWithProperty;

	/** 参照先オブジェクトID */
	private String objectId;

	/** 参照元オブジェクトID */
	private String referenceFromObjectId;

	/** ルートオブジェクトID、NestPropertyの場合に利用 */
	private String rootObjectId;

	/** 参照型の表示プロパティ */
	private List<MetaNestProperty> nestProperties;

	/** 削除ボタン非表示設定 */
	private boolean hideDeleteButton;

	/** 新規ボタン非表示設定 */
	private boolean hideRegistButton;

	/** 新規ボタン非表示設定 */
	private boolean hideSelectButton;

	/** 参照リンク編集可否 */
	private boolean editableReference;

	/** 行追加方法 */
	private InsertType insertType;

	/** ダイアログ表示アクション名 */
	private String viewrefActionName;

	/** ダイアログ編集アクション名 */
	private String detailrefActionName;

	/** 選択アクション名 */
	private String selectActionName;

	/** 追加アクション名 */
	private String addActionName;

	/** ビュー定義名 */
	private String viewName;

	/** URLパラメータ */
	private String urlParameter;

	/** URLパラメータAction */
	private List<UrlParameterActionType> urlParameterAction;

	/** URLパラメータをコンパイルした際に生成したキー */
	@XmlTransient
	private String urlParameterScriptKey;

	/** ソートアイテム */
	private String sortItem;

	/** ソート種別 */
	private RefSortType sortType;

	/** 編集ページ */
	private EditPage editPage;

	/** 参照コンボ設定 */
	private MetaReferenceComboSetting referenceComboSetting;

	/** 選択アクションコールバックスクリプト */
	private String selectActionCallbackScript;

	/** 新規アクションコールバックスクリプト */
	private String insertActionCallbackScript;

	/** 行追加コールバックスクリプト */
	private String addRowCallbackScript;

	/** 検索条件 */
	private String condition;

	/** 参照コンボの検索方法 */
	private RefComboSearchType searchType;

	/** 参照コンボの親を表示するか */
	private boolean showRefComboParent;

	/** 再帰構造Entityのツリー設定 */
	private MetaReferenceRecursiveTreeSetting referenceRecursiveTreeSetting;

	/** 連動プロパティ */
	private MetaLinkProperty linkProperty;

	/** 特定バージョンの基準となるプロパティ */
	private String specificVersionPropertyName;

	/** ネストテーブルの表示順プロパティ */
	private String tableOrderPropertyId;

	/** 更新時に強制的に更新処理を行う */
	private boolean forceUpadte;

	/** 検索条件での全選択を許可 */
	private boolean permitConditionSelectAll = true;

	/** 選択画面でバージョン検索を許可 */
	private boolean permitVersionedSelect;

	private String displayLabelItem;

	/** ユニークプロパティ */
	private String uniqueItem;

	/** Label形式の場合の登録制御 */
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	private boolean updateWithLabelValue = false;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public ReferenceDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(ReferenceDisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * 参照先オブジェクトIDを取得します。
	 * @return 参照先オブジェクトID
	 */
	@Override
	public String getObjectId() {
		return objectId;
	}

	/**
	 * 参照先オブジェクトIDを設定します。
	 * @param objectName 参照先オブジェクトID
	 */
	public void setObjectId(String objectName) {
		this.objectId = objectName;
	}

	/**
	 * 参照元オブジェクトIDを取得します。
	 * @return 参照元オブジェクトID
	 */
	public String getReferenceFromObjectId() {
		return referenceFromObjectId;
	}

	/**
	 * 参照元オブジェクトIDを設定します。
	 * @param referenceFromObjectId 参照元オブジェクトID
	 */
	public void setReferenceFromObjectId(String referenceFromObjectId) {
		this.referenceFromObjectId = referenceFromObjectId;
	}

	/**
	 * ルートオブジェクトIDを取得します。
	 * NestProperty上のEditorで設定されます。
	 * @return ルートオブジェクトID
	 */
	public String getRootObjectId() {
		return rootObjectId;
	}

	/**
	 * ルートオブジェクトIDを設定します。
	 * @param rootObjectId ルートオブジェクトID
	 */
	public void setRootObjectId(String rootObjectId) {
		this.rootObjectId = rootObjectId;
	}

	/**
	 * 選択ダイアログ利用可否を取得します。
	 * @return 選択ダイアログ利用可否
	 */
	public boolean isUseSearchDialog() {
	    return useSearchDialog;
	}

	/**
	 * 選択ダイアログ利用可否を設定します。
	 * @param useSearchDialog 選択ダイアログ利用可否
	 */
	public void setUseSearchDialog(boolean useSearchDialog) {
	    this.useSearchDialog = useSearchDialog;
	}

	/**
	 * 検索条件で単一選択を取得します。
	 * @return 検索条件で単一選択
	 */
	public boolean isSingleSelect() {
	    return singleSelect;
	}

	/**
	 * 検索条件で単一選択を設定します。
	 * @param singleSelect 検索条件で単一選択
	 */
	public void setSingleSelect(boolean singleSelect) {
	    this.singleSelect = singleSelect;
	}

	/**
	 * プロパティと一緒にネスト項目を条件に利用するかを取得します。
	 * @return プロパティと一緒にネスト項目を条件に利用するか
	 */
	public boolean isUseNestConditionWithProperty() {
		return useNestConditionWithProperty;
	}

	/**
	 * プロパティと一緒にネスト項目を条件に利用するかを設定します。
	 * @param useNestConditionWithProperty プロパティと一緒にネスト項目を条件に利用するか
	 */
	public void setUseNestConditionWithProperty(boolean useNestConditionWithProperty) {
		this.useNestConditionWithProperty = useNestConditionWithProperty;
	}

	/**
	 * 参照型の表示プロパティを取得します。
	 * @return 参照型の表示プロパティ
	 */
	@Override
	public List<MetaNestProperty> getNestProperties() {
		if (nestProperties == null) nestProperties = new ArrayList<>();
		return nestProperties;
	}

	/**
	 * 参照型の表示プロパティを設定します。
	 * @param nestProperties 参照型の表示プロパティ
	 */
	public void setNestProperties(List<MetaNestProperty> nestProperties) {
		this.nestProperties = nestProperties;
	}

	public void addNestProperty(MetaNestProperty property) {
		getNestProperties().add(property);
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
	 * @param dispDeleteButton 削除ボタン非表示設定
	 */
	public void setHideDeleteButton(boolean dispDeleteButton) {
		this.hideDeleteButton = dispDeleteButton;
	}

	/**
	 * 新規ボタン非表示設定を取得します。
	 * @return 新規ボタン非表示設定
	 */
	public boolean isHideRegistButton() {
		return hideRegistButton;
	}

	/**
	 * 新規ボタン非表示設定を設定します。
	 * @param dispRegistButton 新規ボタン非表示設定
	 */
	public void setHideRegistButton(boolean dispRegistButton) {
		this.hideRegistButton = dispRegistButton;
	}

	/**
	 * 新規ボタン非表示設定を取得します。
	 * @return 新規ボタン非表示設定
	 */
	public boolean isHideSelectButton() {
	    return hideSelectButton;
	}

	/**
	 * 新規ボタン非表示設定を設定します。
	 * @param hideSelectButton 新規ボタン非表示設定
	 */
	public void setHideSelectButton(boolean hideSelectButton) {
	    this.hideSelectButton = hideSelectButton;
	}

	/**
	 * 参照リンク編集可否を取得します。
	 * @return 参照リンク編集可否
	 */
	public boolean isEditableReference() {
	    return editableReference;
	}

	/**
	 * 参照リンク編集可否を設定します。
	 * @param editableReference 参照リンク編集可否
	 */
	public void setEditableReference(boolean editableReference) {
	    this.editableReference = editableReference;
	}

	/**
	 * 行追加方法を取得します。
	 * @return 行追加方法
	 */
	public InsertType getInsertType() {
		return insertType;
	}

	/**
	 * 行追加方法を設定します。
	 * @param insertType 行追加方法
	 */
	public void setInsertType(InsertType insertType) {
		this.insertType = insertType;
	}

	/**
	 * ダイアログ表示アクション名を取得します。
	 * @return ダイアログ表示アクション名
	 */
	public String getViewrefActionName() {
		return viewrefActionName;
	}

	/**
	 * ダイアログ表示アクション名を設定します。
	 * @param viewrefActionName ダイアログ表示アクション名
	 */
	public void setViewrefActionName(String viewrefActionName) {
		this.viewrefActionName = viewrefActionName;
	}

	/**
	 * ダイアログ編集アクション名を取得します。
	 * @return ダイアログ編集アクション名
	 */
	public String getDetailrefActionName() {
	    return detailrefActionName;
	}

	/**
	 * ダイアログ編集アクション名を設定します。
	 * @param detailrefActionName ダイアログ編集アクション名
	 */
	public void setDetailrefActionName(String detailrefActionName) {
	    this.detailrefActionName = detailrefActionName;
	}

	/**
	 * 選択アクション名を取得します。
	 * @return 選択アクション名
	 */
	public String getSelectActionName() {
		return selectActionName;
	}

	/**
	 * 選択アクション名を設定します。
	 * @param selectActionName 選択アクション名
	 */
	public void setSelectActionName(String selectActionName) {
		this.selectActionName = selectActionName;
	}

	/**
	 * 追加アクション名を取得します。
	 * @return 追加アクション名
	 */
	public String getAddActionName() {
		return addActionName;
	}

	/**
	 * 追加アクション名を設定します。
	 * @param addActionName 追加アクション名
	 */
	public void setAddActionName(String addActionName) {
		this.addActionName = addActionName;
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
	 * URLパラメータを取得します。
	 * @return URLパラメータ
	 */
	public String getUrlParameter() {
		return urlParameter;
	}

	/**
	 * URLパラメータを設定します。
	 * @param urlParameter URLパラメータ
	 */
	public void setUrlParameter(String urlParameter) {
		this.urlParameter = urlParameter;
	}

	/**
	 * URLパラメータActionを取得します。
	 * @return URLパラメータAction
	 */
	public List<UrlParameterActionType> getUrlParameterAction() {
		return urlParameterAction;
	}

	/**
	 * URLパラメータActionを設定します。
	 * @param urlParameterAction URLパラメータAction
	 */
	public void setUrlParameterAction(List<UrlParameterActionType> urlParameterAction) {
		this.urlParameterAction = urlParameterAction;
	}

	/**
	 * ソートアイテムを取得します。
	 * @return ソートアイテム
	 */
	public String getSortItem() {
		return sortItem;
	}

	/**
	 * ソートアイテムを設定します。
	 * @param sortItem ソートアイテム
	 */
	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	/**
	 * ソート種別を取得します。
	 * @return ソート種別
	 */
	public RefSortType getSortType() {
		return sortType;
	}

	/**
	 * ソート種別を設定します。
	 * @param sortType ソート種別
	 */
	public void setSortType(RefSortType sortType) {
		this.sortType = sortType;
	}

	/**
	 * 編集ページを取得します。
	 * @return 編集ページ
	 */
	public EditPage getEditPage() {
	    return editPage;
	}

	/**
	 * 編集ページを設定します。
	 * @param editPage 編集ページ
	 */
	public void setEditPage(EditPage editPage) {
	    this.editPage = editPage;
	}

	/**
	 * 参照コンボ設定を取得します。
	 * @return 参照コンボ設定
	 */
	public MetaReferenceComboSetting getReferenceComboSetting() {
		return referenceComboSetting;
	}

	/**
	 * 参照コンボ設定を設定します。
	 * @param referenceComboSetting 参照コンボ設定
	 */
	public void setReferenceComboSetting(
			MetaReferenceComboSetting referenceComboSetting) {
		this.referenceComboSetting = referenceComboSetting;
	}

	/**
	 * 選択アクションコールバックスクリプトを取得します。
	 * @return 選択アクションコールバックスクリプト
	 */
	public String getSelectActionCallbackScript() {
		return selectActionCallbackScript;
	}

	/**
	 * 選択アクションコールバックスクリプトを設定します。
	 * @param selectActionCallbackScript 選択アクションコールバックスクリプト
	 */
	public void setSelectActionCallbackScript(String selectActionCallbackScript) {
		this.selectActionCallbackScript = selectActionCallbackScript;
	}

	/**
	 * 新規アクションコールバックスクリプトを取得します。
	 * @return 新規アクションコールバックスクリプト
	 */
	public String getInsertActionCallbackScript() {
		return insertActionCallbackScript;
	}

	/**
	 * 新規アクションコールバックスクリプトを設定します。
	 * @param insertActionCallbackScript 新規アクションコールバックスクリプト
	 */
	public void setInsertActionCallbackScript(String insertActionCallbackScript) {
		this.insertActionCallbackScript = insertActionCallbackScript;
	}

	/**
	 * 行追加コールバックスクリプトを取得します。
	 * @return 行追加コールバックスクリプト
	 */
	public String getAddRowCallbackScript() {
		return addRowCallbackScript;
	}

	/**
	 * 行追加コールバックスクリプトを設定します。
	 * @param addRowCallbackScript 行追加コールバックスクリプト
	 */
	public void setAddRowCallbackScript(String addRowCallbackScript) {
		this.addRowCallbackScript = addRowCallbackScript;
	}

	/**
	 * 検索条件を取得します。
	 * @return 検索条件
	 */
	public String getCondition() {
	    return condition;
	}

	/**
	 * 検索条件を設定します。
	 * @param condition 検索条件
	 */
	public void setCondition(String condition) {
	    this.condition = condition;
	}

	/**
	 * 参照コンボの検索方法を取得します。
	 * @return 参照コンボの検索方法
	 */
	public RefComboSearchType getSearchType() {
		return searchType;
	}

	/**
	 * 参照コンボの検索方法を設定します。
	 * @param searchType 参照コンボの検索方法
	 */
	public void setSearchType(RefComboSearchType searchType) {
		this.searchType = searchType;
	}

	/**
	 * 参照コンボの親を表示するかを取得します。
	 * @return 参照コンボの親を表示するか
	 */
	public boolean isShowRefComboParent() {
	    return showRefComboParent;
	}

	/**
	 * 参照コンボの親を表示するかを設定します。
	 * @param showRefComboParent 参照コンボの親を表示するか
	 */
	public void setShowRefComboParent(boolean showRefComboParent) {
	    this.showRefComboParent = showRefComboParent;
	}

	/**
	 * 再帰構造Entityのツリー設定を取得します。
	 * @return 再帰構造Entityのツリー設定
	 */
	public MetaReferenceRecursiveTreeSetting getReferenceRecursiveTreeSetting() {
	    return referenceRecursiveTreeSetting;
	}

	/**
	 * 再帰構造Entityのツリー設定を設定します。
	 * @param referenceRecursiveTreeSetting 再帰構造Entityのツリー設定
	 */
	public void setReferenceRecursiveTreeSetting(MetaReferenceRecursiveTreeSetting referenceRecursiveTreeSetting) {
	    this.referenceRecursiveTreeSetting = referenceRecursiveTreeSetting;
	}

	/**
	 * 連動プロパティ設定を取得します。
	 * @return 連動プロパティ設定
	 */
	public MetaLinkProperty getLinkProperty() {
		return linkProperty;
	}

	/**
	 * 連動プロパティ設定を設定します。
	 * @param linkProperty 連動プロパティ設定
	 */
	public void setLinkProperty(MetaLinkProperty linkProperty) {
		this.linkProperty = linkProperty;
	}

	/**
	 * 特定バージョンの基準となるプロパティを取得します。
	 * @return 特定バージョンの基準となるプロパティ
	 */
	public String getSpecificVersionPropertyName() {
	    return specificVersionPropertyName;
	}

	/**
	 * 特定バージョンの基準となるプロパティを設定します。
	 * @param specificVersionPropertyName 特定バージョンの基準となるプロパティ
	 */
	public void setSpecificVersionPropertyName(String specificVersionPropertyName) {
	    this.specificVersionPropertyName = specificVersionPropertyName;
	}

	/**
	 * ネストテーブルの表示順プロパティを取得します。
	 * @return ネストテーブルの表示順プロパティ
	 */
	public String getTableOrderPropertyId() {
	    return tableOrderPropertyId;
	}

	/**
	 * ネストテーブルの表示順プロパティを設定します。
	 * @param tableOrderPropertyId ネストテーブルの表示順プロパティ
	 */
	public void setTableOrderPropertyId(String tableOrderPropertyId) {
	    this.tableOrderPropertyId = tableOrderPropertyId;
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
	 * 検索条件での全選択を許可を取得します。
	 * @return 検索条件での全選択を許可
	 */
	public boolean isPermitConditionSelectAll() {
	    return permitConditionSelectAll;
	}

	/**
	 * 検索条件での全選択を許可を設定します。
	 * @param permitConditionSelectAll 検索条件での全選択を許可
	 */
	public void setPermitConditionSelectAll(boolean permitConditionSelectAll) {
	    this.permitConditionSelectAll = permitConditionSelectAll;
	}

	/**
	 * 選択画面でのバージョン検索の許可を取得します。
	 * @return 選択画面でバージョン検索を許可
	 */
	public boolean isPermitVersionedSelect() {
		return permitVersionedSelect;
	}

	/**
	 * 選択画面でのバージョン検索の許可を設定します。
	 * @param permitVersionedSelect 選択画面でバージョン検索を許可
	 */
	public void setPermitVersionedSelect(boolean permitVersionedSelect) {
		this.permitVersionedSelect = permitVersionedSelect;
	}

	public String getDisplayLabelItem() {
		return displayLabelItem;
	}

	public void setDisplayLabelItem(String displayLabelItem) {
		this.displayLabelItem = displayLabelItem;
	}

	/**
	 * ユニークキープロパティを取得します。
	 * @return ユニークキープロパティ
	 */
	public String getUniqueKeyItem() {
		return uniqueItem;
	}

	/**
	 * ユニークキープロパティを設定します。
	 * @param uniqueKeyItem ユニークキープロパティ
	 */
	public void setUniqueKeyItem(String uniqueKeyItem) {
		this.uniqueItem = uniqueKeyItem;
	}

	/**
	 * 表示タイプがLabel形式の場合に、登録時に登録対象にするかを返します。
	 *
	 * @return true：登録対象
	 */
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

	/**
	 * 表示タイプがLabel形式の場合に、更新時に更新対象にするかを返します。
	 *
	 * @return true：更新対象
	 */
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
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		ReferencePropertyEditor rpe = (ReferencePropertyEditor) editor;

		EntityContext context = EntityContext.getCurrentContext();

		EntityHandler referenceEntity = context.getHandlerByName(rpe.getObjectName());
		objectId = referenceEntity.getMetaData().getId();

		EntityHandler fromEntity = null;
		if (rpe.getReferenceFromObjectName() != null) {
			fromEntity = context.getHandlerByName(rpe.getReferenceFromObjectName());
		}
		if (fromEntity != null) {
			referenceFromObjectId = fromEntity.getMetaData().getId();
		} else {
			referenceFromObjectId = null;
		}

		EntityHandler rootEntity = null;
		if (rpe.getRootObjectName() != null) {
			rootEntity = context.getHandlerByName(rpe.getRootObjectName());
		}
		if (rootEntity != null) {
			rootObjectId = rootEntity.getMetaData().getId();
		} else {
			rootObjectId = null;
			// ルートを参照元Entityとする
			rootEntity = fromEntity;
		}

		displayType = rpe.getDisplayType();

		PropertyHandler sortProperty = null;
		if (rpe.getSortItem() != null) {
			sortProperty = referenceEntity.getProperty(rpe.getSortItem(), context);
		}
		PropertyHandler displayLabelProperty = null;
		if (rpe.getDisplayLabelItem() != null) {
			displayLabelProperty = referenceEntity.getProperty(rpe.getDisplayLabelItem(), context);
		}
		PropertyHandler uniqueProperty = null;
		if (rpe.getUniqueItem() != null) {
			uniqueProperty = referenceEntity.getProperty(rpe.getUniqueItem(), context);
		}

		useSearchDialog = rpe.isUseSearchDialog();
		singleSelect = rpe.isSingleSelect();
		useNestConditionWithProperty = rpe.isUseNestConditionWithProperty();
		hideDeleteButton  =rpe.isHideDeleteButton();
		hideRegistButton  =rpe.isHideRegistButton();
		hideSelectButton = rpe.isHideSelectButton();
		editableReference = rpe.isEditableReference();
		insertType = rpe.getInsertType();
		viewrefActionName = rpe.getViewrefActionName();
		detailrefActionName = rpe.getDetailrefActionName();
		selectActionName = rpe.getSelectActionName();
		addActionName = rpe.getAddActionName();
		viewName = rpe.getViewName();
		urlParameter = rpe.getUrlParameter();
		urlParameterAction = rpe.getUrlParameterAction() != null ? new ArrayList<>(rpe.getUrlParameterAction()) : null;
		sortItem = sortProperty != null ? sortProperty.getId() : null;
		sortType = rpe.getSortType();
		editPage = rpe.getEditPage();
		selectActionCallbackScript = rpe.getSelectActionCallbackScript();
		insertActionCallbackScript = rpe.getInsertActionCallbackScript();
		addRowCallbackScript = rpe.getAddRowCallbackScript();
		condition = rpe.getCondition();
		searchType = rpe.getSearchType();
		showRefComboParent = rpe.isShowRefComboParent();
		specificVersionPropertyName = rpe.getSpecificVersionPropertyName();
		permitConditionSelectAll = rpe.isPermitConditionSelectAll();
		permitVersionedSelect = rpe.isPermitVersionedSelect();
		displayLabelItem = displayLabelProperty != null ? displayLabelProperty.getId() : null;
		uniqueItem = uniqueProperty != null ? uniqueProperty.getId() : null;
		if (rpe.getTableOrderPropertyName() != null) {
			PropertyHandler tableOrderProperty = referenceEntity.getProperty(rpe.getTableOrderPropertyName(), context);
			tableOrderPropertyId = tableOrderProperty != null ? tableOrderProperty.getId() : null;
		}
		forceUpadte = rpe.isForceUpadte();
		for (NestProperty np : rpe.getNestProperties()) {
			MetaNestProperty mnp = new MetaNestProperty();
			mnp.applyConfig(np, referenceEntity, fromEntity, rootEntity);
			if (mnp.getPropertyId() != null) addNestProperty(mnp);
		}
		if (rpe.getReferenceComboSetting() != null && rpe.getReferenceComboSetting().getPropertyName() != null) {
			MetaReferenceComboSetting tmp = new MetaReferenceComboSetting();
			tmp.applyConfig(rpe.getReferenceComboSetting(), referenceEntity);

			//プロパティIDが設定されてない場合は保存しない(不正なプロパティ名や被参照でない場合等)
			if (tmp.getPropertyId() != null) referenceComboSetting = tmp;
		}
		if (rpe.getReferenceRecursiveTreeSetting() != null) {
			MetaReferenceRecursiveTreeSetting setting = new MetaReferenceRecursiveTreeSetting();
			setting.applyConfig(rpe.getReferenceRecursiveTreeSetting(), referenceEntity);

			//プロパティIDが設定されてない場合は保存しない
			if (setting.getChildPropertyId() != null) referenceRecursiveTreeSetting = setting;
		}
		if (rpe.getLinkProperty() != null && fromEntity != null) {
			//参照元Entityが設定されている場合のみ有効にする
			MetaLinkProperty link = new MetaLinkProperty();
			link.applyConfig(rpe.getLinkProperty(), referenceEntity, fromEntity, rootEntity);
			linkProperty = link;
		}
		insertWithLabelValue = rpe.isInsertWithLabelValue();
		updateWithLabelValue = rpe.isUpdateWithLabelValue();

	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		//対象Entityの存在チェック
		EntityContext context = EntityContext.getCurrentContext();

		EntityHandler referenceEntity = context.getHandlerById(objectId);
		if (referenceEntity == null) {
			return null;
		}

		ReferencePropertyEditor editor = new ReferencePropertyEditor();
		super.fillTo(editor);

		editor.setDisplayType(displayType);

		editor.setObjectName(referenceEntity.getMetaData().getName());

		EntityHandler fromEntity = null;
		if (referenceFromObjectId != null) {
			fromEntity = context.getHandlerById(referenceFromObjectId);
		}
		if (fromEntity != null) {
			editor.setReferenceFromObjectName(fromEntity.getMetaData().getName());
		}

		EntityHandler rootEntity = null;
		if (rootObjectId != null) {
			rootEntity = context.getHandlerById(rootObjectId);
		}
		if (rootEntity != null) {
			editor.setRootObjectName(rootEntity.getMetaData().getName());
		} else {
			// ルートを参照元Entityとする
			rootEntity = fromEntity;
		}

		PropertyHandler displayLabelProperty = null;
		if (displayLabelItem != null) {
			displayLabelProperty = referenceEntity.getPropertyById(displayLabelItem, context);
		}
		PropertyHandler uniqueProperty = null;
		if (uniqueItem != null) {
			uniqueProperty = referenceEntity.getPropertyById(uniqueItem, context);
		}
		PropertyHandler sortProperty = null;
		if (sortItem != null) {
			sortProperty = referenceEntity.getPropertyById(sortItem, context);
		}
		editor.setUseSearchDialog(useSearchDialog);
		editor.setSingleSelect(singleSelect);
		editor.setUseNestConditionWithProperty(useNestConditionWithProperty);
		editor.setHideDeleteButton(hideDeleteButton);
		editor.setHideRegistButton(hideRegistButton);
		editor.setHideSelectButton(hideSelectButton);
		editor.setEditableReference(editableReference);
		editor.setInsertType(insertType);
		editor.setViewrefActionName(viewrefActionName);
		editor.setDetailrefActionName(detailrefActionName);
		editor.setSelectActionName(selectActionName);
		editor.setAddActionName(addActionName);
		editor.setViewName(viewName);
		editor.setUrlParameter(urlParameter);
		editor.setUrlParameterAction(urlParameterAction != null ? new ArrayList<>(urlParameterAction) : null);
		editor.setUrlParameterScriptKey(urlParameterScriptKey);
		editor.setSortItem(sortProperty != null ? sortProperty.getName() : null);
		editor.setSortType(sortType);
		editor.setEditPage(editPage);
		editor.setSelectActionCallbackScript(selectActionCallbackScript);
		editor.setInsertActionCallbackScript(insertActionCallbackScript);
		editor.setAddRowCallbackScript(addRowCallbackScript);
		editor.setCondition(condition);
		editor.setSearchType(searchType);
		editor.setShowRefComboParent(showRefComboParent);
		editor.setSpecificVersionPropertyName(specificVersionPropertyName);
		editor.setPermitConditionSelectAll(permitConditionSelectAll);
		editor.setPermitVersionedSelect(permitVersionedSelect);
		editor.setDisplayLabelItem(displayLabelProperty != null ? displayLabelProperty.getName() : null);
		editor.setUniqueItem(uniqueProperty != null ? uniqueProperty.getName() : null);
		if (tableOrderPropertyId != null) {
			PropertyHandler tableOrderProperty = referenceEntity.getPropertyById(tableOrderPropertyId, context);
			editor.setTableOrderPropertyName(tableOrderProperty != null ? tableOrderProperty.getName() : null);
		}
		editor.setForceUpadte(forceUpadte);
		for (MetaNestProperty mnp : getNestProperties()) {
			NestProperty np = mnp.currentConfig(referenceEntity, fromEntity, rootEntity);
			if (np != null && np.getPropertyName() != null) editor.addNestProperty(np);
		}
		if (referenceComboSetting != null && referenceComboSetting.getPropertyId() != null) {
			editor.setReferenceComboSetting(referenceComboSetting.currentConfig(referenceEntity));
		}
		if (referenceRecursiveTreeSetting != null && referenceRecursiveTreeSetting.getChildPropertyId() != null) {
			editor.setReferenceRecursiveTreeSetting(referenceRecursiveTreeSetting.currentConfig(referenceEntity));
		}
		if (linkProperty != null && fromEntity != null) {
			//参照元Entityが設定されている場合のみ有効にする
			editor.setLinkProperty(linkProperty.currentConfig(referenceEntity, fromEntity, rootEntity));
		}
		editor.setInsertWithLabelValue(insertWithLabelValue);
		editor.setUpdateWithLabelValue(updateWithLabelValue);
		return editor;
	}

	@Override
	public MetaReferencePropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaDataRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
			MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
		return new ReferencePropertyEditorRuntime(entityView, formView, propertyLayout, context, eh);
	}

	public class ReferencePropertyEditorRuntime extends PropertyEditorRuntime {

		private static final String SCRIPT_PREFIX = "ReferencePropertyEditorHandler_urlParameter";

		private GroovyTemplate urlParameterScript;

		public ReferencePropertyEditorRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
				MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
			super(entityView, formView, propertyLayout, context, eh);

			if (StringUtil.isNotEmpty(urlParameter)) {
				urlParameterScriptKey = "ReferencePropertyEditor_UrlParameter_" + GroovyTemplateCompiler.randomName().replace("-", "_");
				ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				urlParameterScript = GroovyTemplateCompiler.compile(urlParameter,
						urlParameterScriptKey + "_" + SCRIPT_PREFIX,
						(GroovyScriptEngine) scriptEngine);

				entityView.addTemplate(urlParameterScriptKey, urlParameterScript);
			}

			if (nestProperties != null && !nestProperties.isEmpty()) {
				for (MetaNestProperty meta : nestProperties) {
					if (meta.getAutocompletionSetting()  != null) {
						entityView.addAutocompletionSetting(meta.getAutocompletionSetting().createRuntime(entityView));
					}
				}
			}

		}

		@Override
		protected boolean checkPropertyType(PropertyDefinition pd) {
			return pd == null || pd instanceof ReferenceProperty;
		}
	}
}
