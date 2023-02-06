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

package org.iplass.mtp.impl.view.generic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.element.section.MetaSection;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.element.section.Section;

/**
 * 検索画面用のレイアウト情報
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaSearchFormView extends MetaFormView {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1033584608508358891L;

	public static MetaSearchFormView createInstance(FormView view) {
		return new MetaSearchFormView();
	}

	/** ごみ箱を隠すか */
	private boolean hideTrash;

	/** ごみ箱操作をユーザー削除データに限定するか */
	private boolean allowTrashOperationToRecycleBy;

	/** 新規作成ボタンを隠すか */
	private boolean hideNew;

	/** 追加アクション名 */
	private String newActionName;

	/** 表示アクション名 */
	private String viewActionName;

	/** 編集アクション名 */
	private String editActionName;

	/** 一括削除WebAPI名 */
	private String deleteListWebapiName;

	/** 全削除WebAPI名 */
	private String deleteAllWebapiName;

	/** 検索WebAPI名 */
	private String searchWebapiName;

	/** CVSダウンロードアクション名 */
	private String downloadActionName;

	/** CSVアップロード画面表示アクション名 */
	private String viewUploadActionName;

	/** JavaScript */
	private String javaScript;

	/** 物理削除するかどうか */
	private boolean isPurge;

	/** 特定バージョンを削除するか */
	private boolean deleteSpecificVersion;
	
	/** 特権実行でユーザー名を表示 */
	private boolean showUserNameWithPrivilegedValue;

	/** Entity権限における限定条件の除外設定 */
	private List<String> withoutConditionReferenceName;

	/** EQLカスタム処理クラス名 */
	private String interrupterName;

	/** EQLカスタム処理クラスをCSVダウンロードで利用するか */
	private boolean useInterrupterForCsvDownload;

	/** 検索画面Handlerクラス名 */
	private List<String> searchFormViewHandlerName;

	/** ボタン上部のセクション */
	private MetaSection topSection1;

	/** ボタン下部のセクション */
	private MetaSection topSection2;

	/** 検索画面中央のセクション */
	private MetaSection centerSection;

	/** 検索画面下部のセクション */
	private MetaSection bottomSection;


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
	 * ごみ箱操作をユーザー削除データに限定するかを取得します。
	 * @return ごみ箱操作をユーザー削除データに限定するか
	 */
	public boolean isAllowTrashOperationToRecycleBy() {
		return allowTrashOperationToRecycleBy;
	}

	/**
	 * ごみ箱操作をユーザー削除データに限定するかを設定します。
	 * @param allowTrashOperationToRecycleBy ごみ箱操作をユーザー削除データに限定するか
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
	 * 追加アクション名を取得します。
	 * @return 追加アクション名
	 */
	public String getNewActionName() {
		return newActionName;
	}

	/**
	 * 追加アクション名を設定します。
	 * @param newActionName 追加アクション名
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
	 * CVSダウンロードアクション名を取得します。
	 * @return CVSダウンロードアクション名
	 */
	public String getDownloadActionName() {
		return downloadActionName;
	}

	/**
	 * CVSダウンロードアクション名を設定します。
	 * @param downloadActionName CVSダウンロードアクション名
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
	 * JavaScriptを取得します。
	 * @return JavaScript
	 */
	public String getJavaScript() {
		return javaScript;
	}

	/**
	 * JavaScriptを設定します。
	 * @param javaScript JavaScript
	 */
	public void setJavaScript(String javaScript) {
		this.javaScript = javaScript;
	}

	/**
	 * 物理削除するかどうかを取得します。
	 * @return 物理削除するかどうか
	 */
	public boolean isPurge() {
		return isPurge;
	}

	/**
	 * 物理削除するかどうかを設定します。
	 * @param isPurge 物理削除するかどうか
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
	public List<String> getWithoutConditionReferenceName() {
		return withoutConditionReferenceName;
	}

	/**
	 * Entity権限における限定条件の除外設定を設定します。
	 * @param withoutConditionReferenceName Entity権限における限定条件の除外設定
	 */
	public void setWithoutConditionReferenceName(List<String> withoutConditionReferenceName) {
		this.withoutConditionReferenceName = withoutConditionReferenceName;
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
	public MetaSection getTopSection1() {
	    return topSection1;
	}

	/**
	 * ボタン上部のセクションを設定します。
	 * @param topSection1 ボタン上部のセクション
	 */
	public void setTopSection1(MetaSection topSection1) {
	    this.topSection1 = topSection1;
	}

	/**
	 * ボタン下部のセクションを取得します。
	 * @return ボタン下部のセクション
	 */
	public MetaSection getTopSection2() {
	    return topSection2;
	}

	/**
	 * ボタン下部のセクションを設定します。
	 * @param topSection2 ボタン下部のセクション
	 */
	public void setTopSection2(MetaSection topSection2) {
	    this.topSection2 = topSection2;
	}

	/**
	 * 検索画面中央のセクションを取得します。
	 * @return 検索画面中央のセクション
	 */
	public MetaSection getCenterSection() {
	    return centerSection;
	}

	/**
	 * 検索画面中央のセクションを設定します。
	 * @param centerSection 検索画面中央のセクション
	 */
	public void setCenterSection(MetaSection centerSection) {
	    this.centerSection = centerSection;
	}

	/**
	 * 検索画面下部のセクションを取得します。
	 * @return 検索画面下部のセクション
	 */
	public MetaSection getBottomSection() {
	    return bottomSection;
	}

	/**
	 * 検索画面下部のセクションを設定します。
	 * @param bottomSection 検索画面下部のセクション
	 */
	public void setBottomSection(MetaSection bottomSection) {
	    this.bottomSection = bottomSection;
	}

	@Override
	public MetaSearchFormView copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * レイアウト情報の内容を自身に適用します。
	 * @param form レイアウト情報
	 * @param definitionId Entity定義のID
	 */
	@Override
	public void applyConfig(FormView form, String definitionId) {
		super.fillFrom(form, definitionId);

		SearchFormView sForm = (SearchFormView) form;
		hideTrash = sForm.isHideTrash();
		allowTrashOperationToRecycleBy = sForm.isAllowTrashOperationToRecycleBy();
		hideNew = sForm.isHideNew();
		newActionName = sForm.getNewActionName();
		viewActionName = sForm.getViewActionName();
		editActionName = sForm.getEditActionName();
		deleteListWebapiName = sForm.getDeleteListWebapiName();
		deleteAllWebapiName = sForm.getDeleteAllWebapiName();
		searchWebapiName = sForm.getSearchWebapiName();
		downloadActionName = sForm.getDownloadActionName();
		viewUploadActionName = sForm.getViewUploadActionName();
		javaScript = sForm.getJavaScript();
		isPurge = sForm.isPurge();
		deleteSpecificVersion = sForm.isDeleteSpecificVersion();
		showUserNameWithPrivilegedValue = sForm.isShowUserNameWithPrivilegedValue();
		withoutConditionReferenceName =
				sForm.getWithoutConditionReferenceName() == null ? null : new ArrayList<>(sForm.getWithoutConditionReferenceName());
		interrupterName = sForm.getInterrupterName();
		useInterrupterForCsvDownload = sForm.isUseInterrupterForCsvDownload();
		searchFormViewHandlerName =
				sForm.getSearchFormViewHandlerName() == null ? null : new ArrayList<>(sForm.getSearchFormViewHandlerName());
		if (sForm.getTopSection1() != null) {
			topSection1 = MetaSection.createInstance(sForm.getTopSection1());
			topSection1.applyConfig(sForm.getTopSection1(), definitionId);
		}
		if (sForm.getTopSection2() != null) {
			topSection2 = MetaSection.createInstance(sForm.getTopSection2());
			topSection2.applyConfig(sForm.getTopSection2(), definitionId);
		}
		if (sForm.getCenterSection() != null) {
			centerSection = MetaSection.createInstance(sForm.getCenterSection());
			centerSection.applyConfig(sForm.getCenterSection(), definitionId);
		}
		if (sForm.getBottomSection() != null) {
			bottomSection = MetaSection.createInstance(sForm.getBottomSection());
			bottomSection.applyConfig(sForm.getBottomSection(), definitionId);
		}
	}

	/**
	 * レイアウト情報の内容を自身に適用します。
	 * @param definitionId Entity定義のID
	 * @return レイアウト情報
	 */
	@Override
	public FormView currentConfig(String definitionId) {
		SearchFormView form = new SearchFormView();
		super.fillTo(form, definitionId);

		form.setHideTrash(hideTrash);
		form.setAllowTrashOperationToRecycleBy(allowTrashOperationToRecycleBy);
		form.setHideNew(hideNew);
		form.setNewActionName(newActionName);
		form.setViewActionName(viewActionName);
		form.setEditActionName(editActionName);
		form.setDeleteListWebapiName(deleteListWebapiName);
		form.setDeleteAllWebapiName(deleteAllWebapiName);
		form.setSearchWebapiName(searchWebapiName);
		form.setDownloadActionName(downloadActionName);
		form.setViewUploadActionName(viewUploadActionName);
		form.setJavaScript(javaScript);
		form.setPurge(isPurge);
		form.setDeleteSpecificVersion(deleteSpecificVersion);
		form.setShowUserNameWithPrivilegedValue(showUserNameWithPrivilegedValue);
		form.setWithoutConditionReferenceName(
				withoutConditionReferenceName == null ? null : new ArrayList<>(withoutConditionReferenceName));
		form.setInterrupterName(interrupterName);
		form.setUseInterrupterForCsvDownload(useInterrupterForCsvDownload);
		form.setSearchFormViewHandlerName(
				searchFormViewHandlerName == null ? null : new ArrayList<>(searchFormViewHandlerName));
		if (topSection1 != null) {
			form.setTopSection1((Section) topSection1.currentConfig(definitionId));
		}
		if (topSection2 != null) {
			form.setTopSection2((Section) topSection2.currentConfig(definitionId));
		}
		if (centerSection != null) {
			form.setCenterSection((Section) centerSection.currentConfig(definitionId));
		}
		if (bottomSection != null) {
			form.setBottomSection((Section) bottomSection.currentConfig(definitionId));
		}
		return form;
	}

	@Override
	public FormViewRuntime createRuntime(EntityViewRuntime entityView) {
		return new SearchFormViewRuntime(this, entityView);
	}
}
