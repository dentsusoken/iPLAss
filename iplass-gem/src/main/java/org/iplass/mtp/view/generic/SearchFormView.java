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

package org.iplass.mtp.view.generic;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.view.generic.element.section.Section;

/**
 * 検索画面用のFormレイアウト情報
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchFormView extends FormView {

	/** シリアルバージョンID */
	private static final long serialVersionUID = -8342414954265792060L;

	/** 新規作成ボタンを隠すか */
	@MetaFieldInfo(
			displayName="新規作成ボタンを非表示",
			displayNameKey="generic_SearchFormView_hideNewDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			description="新規作成ボタンを非表示にするかを設定します。",
			descriptionKey="generic_SearchFormView_hideNewDescriptionKey"
	)
	private boolean hideNew;

	/** ごみ箱を隠すか */
	@MetaFieldInfo(
			displayName="ごみ箱を非表示",
			displayNameKey="generic_SearchFormView_hideTrashDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="ごみ箱へのリンクを非表示にするかを設定します。",
			descriptionKey="generic_SearchFormView_hideTrashDescriptionKey"
	)
	private boolean hideTrash;

	/** ごみ箱操作をユーザ削除データに限定するか */
	@MetaFieldInfo(
			displayName="ごみ箱操作をユーザ削除データに限定",
			displayNameKey="generic_SearchFormView_allowTrashOperationToRecycleByDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=220,
			description="ユーザが操作可能なごみ箱データを自分が削除したデータのみに制限するかを設定します。",
			descriptionKey="generic_SearchFormView_allowTrashOperationToRecycleByDescriptionKey"
	)
	private boolean allowTrashOperationToRecycleBy;




	/** 検索WebAPI名 */
	@MetaFieldInfo(
			displayName="検索WebAPI名",
			displayNameKey="generic_SearchFormView_searchWebapiNameDisplaNameKey",
			inputType=InputType.WEBAPI,
			displayOrder=500,
			description="検索ボタンクリックで実行されるWebAPIを設定します。",
			descriptionKey="generic_SearchFormView_searchWebapiNameDescriptionKey"
	)
	private String searchWebapiName;

	/** CSVダウンロードアクション名 */
	@MetaFieldInfo(
			displayName="CSVダウンロードアクション名",
			displayNameKey="generic_SearchFormView_downloadActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=510,
			description="CSVダウンロードボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_SearchFormView_downloadActionNameDescriptionKey"
	)
	private String downloadActionName;

	/** CSVアップロード画面表示アクション名 */
	@MetaFieldInfo(
			displayName="CSVアップロード画面表示アクション名",
			displayNameKey="generic_SearchFormView_viewUploadActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=520,
			description="CSVアップロードボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_SearchFormView_viewUploadActionNameDescriptionKey"
	)
	private String viewUploadActionName;

	/** 新規追加アクション名 */
	@MetaFieldInfo(
			displayName="新規追加アクション名",
			displayNameKey="generic_SearchFormView_newActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=530,
			description="新規追加ボタンクリックで実行されるアクションを設定します。",
			descriptionKey="generic_SearchFormView_newActionNameDescriptionKey"
	)
	private String newActionName;

	/** 表示アクション名 */
	@MetaFieldInfo(
			displayName="表示アクション名",
			displayNameKey="generic_SearchFormView_viewActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=540,
			description="詳細リンククリックで実行されるアクションを設定します。",
			descriptionKey="generic_SearchFormView_viewActionNameDescriptionKey"
	)
	private String viewActionName;

	/** 編集アクション名 */
	@MetaFieldInfo(
			displayName="編集アクション名",
			displayNameKey="generic_SearchFormView_editActionNameDisplaNameKey",
			inputType=InputType.ACTION,
			displayOrder=550,
			description="編集リンククリックで実行されるアクションを設定します。",
			descriptionKey="generic_SearchFormView_editActionNameDescriptionKey"
	)
	private String editActionName;

	/** 一括削除WebAPI名 */
	@MetaFieldInfo(
			displayName="一括削除WebAPI名",
			displayNameKey="generic_SearchFormView_deleteListWebapiNameDisplaNameKey",
			inputType=InputType.WEBAPI,
			displayOrder=560,
			description="削除ボタンクリックで実行されるWebAPIを設定します。",
			descriptionKey="generic_SearchFormView_deleteListWebapiNameDescriptionKey"
	)
	private String deleteListWebapiName;

	/** 全削除WebAPI名 */
	@MetaFieldInfo(
			displayName="全削除WebAPI名",
			displayNameKey="generic_SearchFormView_deleteAllWebapiNameDisplaNameKey",
			inputType=InputType.WEBAPI,
			displayOrder=570,
			description="全選択チェック時に削除ボタンクリックで実行されるWebAPIを設定します。",
			descriptionKey="generic_SearchFormView_deleteAllWebapiNameDescriptionKey"
	)
	private String deleteAllWebapiName;




	/** JavaScriptコード */
	@MetaFieldInfo(
			displayName="JavaScriptコード",
			displayNameKey="generic_SearchFormView_javaScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			displayOrder=1100,
			mode="javascript",
			description="SCRIPTタグ内に出力するJavaScriptコードを設定します。",
			descriptionKey="generic_SearchFormView_javaScriptDescriptionKey"
	)
	private String javaScript;




	/** 物理削除するか */
	@MetaFieldInfo(
			displayName="物理削除するか",
			inputType=InputType.CHECKBOX,
			displayOrder=1510,
			description="物理削除を行うかを設定します。",
			displayNameKey="generic_SearchFormView_isPurgeDisplaNameKey",
			descriptionKey="generic_SearchFormView_isPurgeDescriptionKey")
	private boolean isPurge;

	/** 特定バージョンを削除するか */
	@MetaFieldInfo(
			displayName="特定バージョンを削除するか",
			inputType=InputType.CHECKBOX,
			displayOrder=1520,
			description="バージョン管理されているEntityの削除、CSVアップロード時に指定されたバージョンのみを削除します。親子関係の参照を持つ場合はエラーになります。",
			displayNameKey="generic_SearchFormView_deleteSpecificVersionDisplaNameKey",
			descriptionKey="generic_SearchFormView_deleteSpecificVersionDescriptionKey")
	private boolean deleteSpecificVersion;

	/** 特権実行でユーザー名を表示 */
	@MetaFieldInfo(
			displayName="特権実行でユーザー名を表示する",
			inputType=InputType.CHECKBOX,
			displayOrder=1520,
			description="ユーザー情報の参照権限が無くてもユーザー名を表示するかを指定します。",
			displayNameKey="generic_SearchFormView_showUserNameWithPrivilegedDisplayNameKey",
			descriptionKey="generic_SearchFormView_showUserNameWithPrivilegedDescriptionKey"
	)
	private boolean showUserNameWithPrivilegedValue;

	/** Entity権限における限定条件の除外設定 */
	@MetaFieldInfo(
			displayName="Entity権限における限定条件の除外設定",
			displayNameKey="generic_SearchFormView_withoutConditionReferenceNameKeyDisplaNameKey",
			inputType=InputType.MULTI_TEXT,
			displayOrder=1600,
			description="Entity権限における限定条件を除外する参照先を指定します。<br>" +
					"参照がネストされている場合は最下層の参照先Entity名を指定してください。。",
			descriptionKey="generic_SearchFormView_withoutConditionReferenceNameKeyDescriptionKey"
	)
	private List<String> withoutConditionReferenceNameKey;

	/** EQLカスタム処理クラス名 */
	@MetaFieldInfo(
			displayName="EQLカスタム処理クラス名",
			displayNameKey="generic_SearchFormView_interrupterNameDisplaNameKey",
			displayOrder=1610,
			description="検索実行前にクエリをカスタマイズするためのクラス名を指定します。<br>" +
					"SearchQueryInterrupterインターフェースを実装するクラスを指定してください。",
			descriptionKey="generic_SearchFormView_interrupterNameDescriptionKey"
	)
	private String interrupterName;

	/** EQLカスタム処理クラスをCSVダウンロードで利用するか */
	@MetaFieldInfo(
			displayName="EQLカスタム処理クラスをCSVダウンロードで利用するか",
			displayNameKey="generic_SearchFormView_useInterrupterForCsvDownloadDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1620,
			description="EQLカスタム処理クラスをCSVダウンロードで利用するかを指定します。",
			descriptionKey="generic_SearchFormView_useInterrupterForCsvDownloadDescriptionKey"
	)
	private boolean useInterrupterForCsvDownload;

	/** 検索画面Handlerクラス名 */
	@MetaFieldInfo(
			displayName="検索画面Handlerクラス名",
			displayNameKey="generic_SearchFormView_searchFormViewHandlerNameDisplaNameKey",
			inputType=InputType.MULTI_TEXT,
			displayOrder=1630,
			description="検索画面の制御クラス名を指定します。<br>" +
					"SearchFormViewHandlerインターフェースを実装するクラスを指定してください。",
			descriptionKey="generic_SearchFormView_searchFormViewHandlerNameDescriptionKey"
	)
	private List<String> searchFormViewHandlerName;

	/** ボタン上部のセクション */
	@MultiLang(isMultiLangValue = false)
	private Section topSection1;

	/** ボタン下部のセクション */
	@MultiLang(isMultiLangValue = false)
	private Section topSection2;

	/** 検索画面中央のセクション */
	@MultiLang(isMultiLangValue = false)
	private Section centerSection;

	/** 検索画面下部のセクション */
	@MultiLang(isMultiLangValue = false)
	private Section bottomSection;

	/**
	 * デフォルトコンストラクタ
	 */
	public SearchFormView() {
	}

	/**
	 * 検索条件セクションを取得します。
	 * @return 検索条件セクション
	 */
	public SearchConditionSection getCondSection() {
		SearchConditionSection condSection = null;
		for (Section section : super.getSections()) {
			if (section instanceof SearchConditionSection) {
				condSection = (SearchConditionSection) section;
			}
		}
		return condSection;
	}

	/**
	 * 検索結果セクションを取得します。
	 * @return 検索結果セクション
	 */
	public SearchResultSection getResultSection() {
		SearchResultSection resultSectoin = null;
		for (Section section : super.getSections()) {
			if (section instanceof SearchResultSection) {
				resultSectoin = (SearchResultSection) section;
			}
		}
		return resultSectoin;
	}

	/**
	 * ごみ箱を隠すかを取得します。
	 * @return ごみ箱を隠すか
	 */
	public boolean isHideTrash() {
	    return hideTrash;
	}

	/**
	 * ごみ箱を隠すかを設定します。
	 * @param showTrash ごみ箱を隠すか
	 */
	public void setHideTrash(boolean showTrash) {
	    this.hideTrash = showTrash;
	}

	/**
	 * ごみ箱操作をユーザ削除データに限定するかを取得します。
	 * @return ごみ箱操作をユーザ削除データに限定するか
	 */
	public boolean isAllowTrashOperationToRecycleBy() {
		return allowTrashOperationToRecycleBy;
	}

	/**
	 * ごみ箱操作をユーザ削除データに限定するかを設定します。
	 * @param allowTrashOperationToRecycleBy ごみ箱操作をユーザ削除データに限定するか
	 */
	public void setAllowTrashOperationToRecycleBy(boolean allowTrashOperationToRecycleBy) {
		this.allowTrashOperationToRecycleBy = allowTrashOperationToRecycleBy;
	}

	/**
	 * 新規作成ボタンを隠すかを取得します。
	 * @return 新規作成ボタンを隠すか
	 */
	public boolean isHideNew() {
	    return hideNew;
	}

	/**
	 * 新規作成ボタンを隠すかを設定します。
	 * @param hideNew 新規作成ボタンを隠すか
	 */
	public void setHideNew(boolean hideNew) {
	    this.hideNew = hideNew;
	}

	/**
	 * 新規追加アクション名を取得します。
	 * @return 新規追加アクション名
	 */
	public String getNewActionName() {
		return newActionName;
	}

	/**
	 * 新規追加アクション名を設定します。
	 * @param newActionName 新規追加アクション名
	 */
	public void setNewActionName(String newActionName) {
		this.newActionName = newActionName;
	}

	/**
	 * 表示アクション名を取得します。
	 * @return 表示アクション名
	 */
	public String getViewActionName() {
		return viewActionName;
	}

	/**
	 * 表示アクション名を設定します。
	 * @param viewActionName 表示アクション名
	 */
	public void setViewActionName(String viewActionName) {
		this.viewActionName = viewActionName;
	}

	/**
	 * 編集アクション名を取得します。
	 * @return 編集アクション名
	 */
	public String getEditActionName() {
		return editActionName;
	}

	/**
	 * 編集アクション名を設定します。
	 * @param editActionName 編集アクション名
	 */
	public void setEditActionName(String editActionName) {
		this.editActionName = editActionName;
	}

	/**
	 * 一括削除WebAPI名を取得します。
	 * @return 一括削除WebAPI名
	 */
	public String getDeleteListWebapiName() {
	    return deleteListWebapiName;
	}

	/**
	 * 一括削除WebAPI名を設定します。
	 * @param deleteListWebapiName 一括削除WebAPI名
	 */
	public void setDeleteListWebapiName(String deleteListWebapiName) {
	    this.deleteListWebapiName = deleteListWebapiName;
	}

	/**
	 * 全削除WebAPI名を取得します。
	 * @return 全削除WebAPI名
	 */
	public String getDeleteAllWebapiName() {
	    return deleteAllWebapiName;
	}

	/**
	 * 全削除WebAPI名を設定します。
	 * @param deleteAllWebapiName 全削除WebAPI名
	 */
	public void setDeleteAllWebapiName(String deleteAllWebapiName) {
	    this.deleteAllWebapiName = deleteAllWebapiName;
	}

	/**
	 * 検索WebAPI名を取得します。
	 * @return 検索WebAPI名
	 */
	public String getSearchWebapiName() {
		return searchWebapiName;
	}

	/**
	 * 検索WebAPI名を設定します。
	 * @param searchWebapiName 検索WebAPI名
	 */
	public void setSearchWebapiName(String searchWebapiName) {
		this.searchWebapiName = searchWebapiName;
	}

	/**
	 * CSVダウンロードアクション名を取得します。
	 * @return CSVダウンロードアクション名
	 */
	public String getDownloadActionName() {
		return downloadActionName;
	}

	/**
	 * CSVダウンロードアクション名を設定します。
	 * @param downloadActionName CSVダウンロードアクション名
	 */
	public void setDownloadActionName(String downloadActionName) {
		this.downloadActionName = downloadActionName;
	}

	/**
	 * CSVアップロード画面表示アクション名を取得します。
	 * @return CSVアップロード画面表示アクション名
	 */
	public String getViewUploadActionName() {
	    return viewUploadActionName;
	}

	/**
	 * CSVアップロード画面表示アクション名を設定します。
	 * @param viewUploadActionName CSVアップロード画面表示アクション名
	 */
	public void setViewUploadActionName(String viewUploadActionName) {
	    this.viewUploadActionName = viewUploadActionName;
	}

	/**
	 * JavaScriptコードを取得します。
	 * @return JavaScriptコード
	 */
	public String getJavaScript() {
		return javaScript;
	}

	/**
	 * JavaScriptコードを設定します。
	 * @param javaScript JavaScriptコード
	 */
	public void setJavaScript(String javaScript) {
		this.javaScript = javaScript;
	}

	/**
	 * 物理削除するかを取得します。
	 * @return 物理削除するか
	 */
	public boolean isPurge() {
	    return isPurge;
	}

	/**
	 * 物理削除するかを設定します。
	 * @param isPurge 物理削除するか
	 */
	public void setPurge(boolean isPurge) {
	    this.isPurge = isPurge;
	}

	/**
	 * 特定バージョンを削除するかを取得します。
	 * @return 特定バージョンを削除するか
	 */
	public boolean isDeleteSpecificVersion() {
		return deleteSpecificVersion;
	}

	/**
	 * 特定バージョンを削除するかを設定します。
	 * @param deleteSpecificVersion 特定バージョンを削除するか
	 */
	public void setDeleteSpecificVersion(boolean deleteSpecificVersion) {
		this.deleteSpecificVersion = deleteSpecificVersion;
	}

	/**
	 * 特権実行でユーザー名を表示を取得します。
	 * @return 特権実行でユーザー名を表示
	 */
	public boolean isShowUserNameWithPrivilegedValue() {
		return showUserNameWithPrivilegedValue;
	}

	/**
	 * 特権実行でユーザー名を表示を設定します。
	 * @param showUserNameWithPrivilegedValue 特権実行でユーザー名を表示
	 */
	public void setShowUserNameWithPrivilegedValue(boolean showUserNameWithPrivilegedValue) {
		this.showUserNameWithPrivilegedValue = showUserNameWithPrivilegedValue;
	}

	/**
	 * Entity権限における限定条件の除外設定を取得します。
	 * @return Entity権限における限定条件の除外設定
	 */
	public List<String> getWithoutConditionReferenceNameKey() {
		return withoutConditionReferenceNameKey;
	}

	/**
	 * Entity権限における限定条件の除外設定を設定します。
	 * @param withoutConditionReferenceNameKey Entity権限における限定条件の除外設定
	 */
	public void setWithoutConditionReferenceNameKey(List<String> withoutConditionReferenceNameKey) {
		this.withoutConditionReferenceNameKey = withoutConditionReferenceNameKey;
	}

	/**
	 * EQLカスタム処理クラス名を取得します。
	 * @return EQLカスタム処理クラス名
	 */
	public String getInterrupterName() {
	    return interrupterName;
	}

	/**
	 * EQLカスタム処理クラス名を設定します。
	 * @param interrupterName EQLカスタム処理クラス名
	 */
	public void setInterrupterName(String interrupterName) {
	    this.interrupterName = interrupterName;
	}

	/**
	 * EQLカスタム処理クラスをCSVダウンロードで利用するかを取得します。
	 * @return EQLカスタム処理クラスをCSVダウンロードで利用するか
	 */
	public boolean isUseInterrupterForCsvDownload() {
	    return useInterrupterForCsvDownload;
	}

	/**
	 * EQLカスタム処理クラスをCSVダウンロードで利用するかを設定します。
	 * @param useInterrupterForCsvDownload EQLカスタム処理クラスをCSVダウンロードで利用するか
	 */
	public void setUseInterrupterForCsvDownload(boolean useInterrupterForCsvDownload) {
	    this.useInterrupterForCsvDownload = useInterrupterForCsvDownload;
	}

	/**
	 * 検索画面Handlerクラス名を取得します。
	 * @return 検索画面Handlerクラス名
	 */
	public List<String> getSearchFormViewHandlerName() {
		return searchFormViewHandlerName;
	}

	/**
	 * 検索画面Handlerクラス名を設定します。
	 * @param searchFormViewHandlerName 検索画面Handlerクラス名
	 */
	public void setSearchFormViewHandlerName(List<String> searchFormViewHandlerName) {
		this.searchFormViewHandlerName = searchFormViewHandlerName;
	}

	/**
	 * ボタン上部のセクションを取得します。
	 * @return ボタン上部のセクション
	 */
	public Section getTopSection1() {
	    return topSection1;
	}

	/**
	 * ボタン上部のセクションを設定します。
	 * @param topSection1 ボタン上部のセクション
	 */
	public void setTopSection1(Section topSection1) {
	    this.topSection1 = topSection1;
	}

	/**
	 * ボタン下部のセクションを取得します。
	 * @return ボタン下部のセクション
	 */
	public Section getTopSection2() {
	    return topSection2;
	}

	/**
	 * ボタン下部のセクションを設定します。
	 * @param topSection2 ボタン下部のセクション
	 */
	public void setTopSection2(Section topSection2) {
	    this.topSection2 = topSection2;
	}

	/**
	 * 検索画面中央のセクションを取得します。
	 * @return 検索画面中央のセクション
	 */
	public Section getCenterSection() {
	    return centerSection;
	}

	/**
	 * 検索画面中央のセクションを設定します。
	 * @param centerSection 検索画面中央のセクション
	 */
	public void setCenterSection(Section centerSection) {
	    this.centerSection = centerSection;
	}

	/**
	 * 検索画面下部のセクションを取得します。
	 * @return 検索画面下部のセクション
	 */
	public Section getBottomSection() {
	    return bottomSection;
	}

	/**
	 * 検索画面下部のセクションを設定します。
	 * @param bottomSection 検索画面下部のセクション
	 */
	public void setBottomSection(Section bottomSection) {
	    this.bottomSection = bottomSection;
	}

}
