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

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.DetailFormView.CopyTarget;
import org.iplass.mtp.view.generic.FormView;

/**
 * 編集画面用のレイアウト情報
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaDetailFormView extends MetaFormView {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1033584608508358891L;

	public static MetaDetailFormView createInstance(FormView view) {
		return new MetaDetailFormView();
	}

	/** 編集アクション名 */
	private String editActionName;

	/** 参照時編集アクション名 */
	private String refEditActionName;

	/** 編集ボタン表示ラベル */
	private String editDisplayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedEditDisplayLabelList = new ArrayList<>();

	/** コピーアップボタン表示ラベル */
	private String copyDisplayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedCopyDisplayLabelList = new ArrayList<>();

	/** バージョンアップボタン表示ラベル */
	private String versionupDisplayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedVersionupDisplayLabelList = new ArrayList<>();

	/** 追加アクション名 */
	private String insertActionName;

	/** 参照時追加アクション名 */
	private String refInsertActionName;

	/** 追加ボタン表示ラベル */
	private String insertDisplayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedInsertDisplayLabelList = new ArrayList<>();

	/** 更新アクション名 */
	private String updateActionName;

	/** 参照時更新アクション名 */
	private String refUpdateActionName;

	/** 更新ボタン表示ラベル */
	private String updateDisplayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedUpdateDisplayLabelList = new ArrayList<>();

	/** 削除アクション名 */
	private String deleteActionName;

	/** 削除ボタン表示ラベル */
	private String deleteDisplayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedDeleteDisplayLabelList = new ArrayList<>();

	/** バージョン削除ボタン表示ラベル */
	private String deleteSpecificVersionDisplayLabel;

	/** バージョン削除ボタン表示ラベル多言語設定情報 */
	private List<MetaLocalizedString> localizedDeleteSpecificVersionDisplayLabelList = new ArrayList<>();

	/** キャンセルアクション名 */
	private String cancelActionName;

	/** Javascriptコード有効可否(編集) */
	private Boolean validJavascriptDetailPage;

	/** Javascriptコード有効可否(表示) */
	private Boolean validJavascriptViewPage;

	/** 編集ボタン非表示 */
	private boolean hideDetail;

	/** ロックボタン非表示設定 */
	private boolean hideLock;

	/** コピーボタン非表示設定 */
	private boolean isNoneDispCopyButton;

	/** 削除ボタン非表示 */
	private boolean hideDelete;

	/** バージョン削除ボタン表示 */
	private boolean showDeleteSpecificVersion;

	/** Entity権限の可能範囲条件でボタン表示を制御 */
	private boolean checkEntityPermissionLimitConditionOfButton;

	/** 物理削除するかどうか */
	private boolean isPurge;

	/** 親子関係の参照を物理削除するか */
	private boolean purgeCompositionedEntity;

	/** 定義されている参照プロパティのみを取得 */
	private boolean loadDefinedReferenceProperty;

	/** 更新時に強制的に更新処理を行う */
	private boolean forceUpadte;

	/** 特権実行でユーザー名を表示 */
	private boolean showUserNameWithPrivilegedValue;

	/** コピー対象 */
	private CopyTarget copyTarget;

	/** カスタム登録処理クラス名 */
	private String interrupterName;

	/** カスタムロード処理クラス名 */
	private String loadEntityInterrupterName;

	/** 詳細編集画面Handlerクラス名 */
	private List<String> detailFormViewHandlerName;

	/** JavaScript */
	private String javaScript;

	/** カスタムコピースクリプト */
	private String customCopyScript;

	/** 初期化スクリプト */
	private String initScript;

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
	 * 参照時編集アクション名を取得します。
	 * @return 参照時編集アクション名
	 */
	public String getRefEditActionName() {
	    return refEditActionName;
	}

	/**
	 * 参照時編集アクション名を設定します。
	 * @param refEditActionName 参照時編集アクション名
	 */
	public void setRefEditActionName(String refEditActionName) {
	    this.refEditActionName = refEditActionName;
	}

	/**
	 * 編集ボタン表示ラベルを取得します。
	 * @return 編集ボタン表示ラベル
	 */
	public String getEditDisplayLabel() {
	    return editDisplayLabel;
	}

	/**
	 * 編集ボタン表示ラベルを設定します。
	 * @param editDisplayLabel 編集ボタン表示ラベル
	 */
	public void setEditDisplayLabel(String editDisplayLabel) {
	    this.editDisplayLabel = editDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedEditDisplayLabelList() {
	    return localizedEditDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedEditDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedEditDisplayLabelList(List<MetaLocalizedString> localizedEditDisplayLabelList) {
	    this.localizedEditDisplayLabelList = localizedEditDisplayLabelList;
	}

	/**
	 * コピーアップボタン表示ラベルを取得します。
	 * @return コピーアップボタン表示ラベル
	 */
	public String getCopyDisplayLabel() {
	    return copyDisplayLabel;
	}

	/**
	 * コピーアップボタン表示ラベルを設定します。
	 * @param copyDisplayLabel コピーアップボタン表示ラベル
	 */
	public void setCopyDisplayLabel(String copyDisplayLabel) {
	    this.copyDisplayLabel = copyDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedCopyDisplayLabelList() {
	    return localizedCopyDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedCopyDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedCopyDisplayLabelList(List<MetaLocalizedString> localizedCopyDisplayLabelList) {
	    this.localizedCopyDisplayLabelList = localizedCopyDisplayLabelList;
	}

	/**
	 * バージョンアップボタン表示ラベルを取得します。
	 * @return バージョンアップボタン表示ラベル
	 */
	public String getVersionupDisplayLabel() {
	    return versionupDisplayLabel;
	}

	/**
	 * バージョンアップボタン表示ラベルを設定します。
	 * @param versionupDisplayLabel バージョンアップボタン表示ラベル
	 */
	public void setVersionupDisplayLabel(String versionupDisplayLabel) {
	    this.versionupDisplayLabel = versionupDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedVersionupDisplayLabelList() {
	    return localizedVersionupDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedVersionupDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedVersionupDisplayLabelList(List<MetaLocalizedString> localizedVersionupDisplayLabelList) {
	    this.localizedVersionupDisplayLabelList = localizedVersionupDisplayLabelList;
	}

	/**
	 * 追加アクション名を取得します。
	 * @return 追加アクション名
	 */
	public String getInsertActionName() {
		return insertActionName;
	}

	/**
	 * 追加アクション名を設定します。
	 * @param insertActionName 追加アクション名
	 */
	public void setInsertActionName(String insertActionName) {
		this.insertActionName = insertActionName;
	}

	/**
	 * 参照時追加アクション名を取得します。
	 * @return 参照時追加アクション名
	 */
	public String getRefInsertActionName() {
	    return refInsertActionName;
	}

	/**
	 * 参照時追加アクション名を設定します。
	 * @param refInsertActionName 参照時追加アクション名
	 */
	public void setRefInsertActionName(String refInsertActionName) {
	    this.refInsertActionName = refInsertActionName;
	}

	/**
	 * 追加ボタン表示ラベルを取得します。
	 * @return 追加ボタン表示ラベル
	 */
	public String getInsertDisplayLabel() {
	    return insertDisplayLabel;
	}

	/**
	 * 追加ボタン表示ラベルを設定します。
	 * @param insertDisplayLabel 追加ボタン表示ラベル
	 */
	public void setInsertDisplayLabel(String insertDisplayLabel) {
	    this.insertDisplayLabel = insertDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedInsertDisplayLabelList() {
	    return localizedInsertDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedInsertDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedInsertDisplayLabelList(List<MetaLocalizedString> localizedInsertDisplayLabelList) {
	    this.localizedInsertDisplayLabelList = localizedInsertDisplayLabelList;
	}

	/**
	 * 更新アクション名を取得します。
	 * @return 更新アクション名
	 */
	public String getUpdateActionName() {
		return updateActionName;
	}

	/**
	 * 更新アクション名を設定します。
	 * @param updateActionName 更新アクション名
	 */
	public void setUpdateActionName(String updateActionName) {
		this.updateActionName = updateActionName;
	}

	/**
	 * 参照時更新アクション名を取得します。
	 * @return 参照時更新アクション名
	 */
	public String getRefUpdateActionName() {
	    return refUpdateActionName;
	}

	/**
	 * 参照時更新アクション名を設定します。
	 * @param refUpdateActionName 参照時更新アクション名
	 */
	public void setRefUpdateActionName(String refUpdateActionName) {
	    this.refUpdateActionName = refUpdateActionName;
	}

	/**
	 * 更新ボタン表示ラベルを取得します。
	 * @return 更新ボタン表示ラベル
	 */
	public String getUpdateDisplayLabel() {
	    return updateDisplayLabel;
	}

	/**
	 * 更新ボタン表示ラベルを設定します。
	 * @param updateDisplayLabel 更新ボタン表示ラベル
	 */
	public void setUpdateDisplayLabel(String updateDisplayLabel) {
	    this.updateDisplayLabel = updateDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedUpdateDisplayLabelList() {
	    return localizedUpdateDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedUpdateDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedUpdateDisplayLabelList(List<MetaLocalizedString> localizedUpdateDisplayLabelList) {
	    this.localizedUpdateDisplayLabelList = localizedUpdateDisplayLabelList;
	}

	/**
	 * 削除アクション名を取得します。
	 * @return 削除アクション名
	 */
	public String getDeleteActionName() {
		return deleteActionName;
	}

	/**
	 * 削除アクション名を設定します。
	 * @param deleteActionName 削除アクション名
	 */
	public void setDeleteActionName(String deleteActionName) {
		this.deleteActionName = deleteActionName;
	}

	/**
	 * 削除ボタン表示ラベルを取得します。
	 * @return 削除ボタン表示ラベル
	 */
	public String getDeleteDisplayLabel() {
	    return deleteDisplayLabel;
	}

	/**
	 * 削除ボタン表示ラベルを設定します。
	 * @param deleteDisplayLabel 削除ボタン表示ラベル
	 */
	public void setDeleteDisplayLabel(String deleteDisplayLabel) {
	    this.deleteDisplayLabel = deleteDisplayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedDeleteDisplayLabelList() {
	    return localizedDeleteDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedDeleteDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedDeleteDisplayLabelList(List<MetaLocalizedString> localizedDeleteDisplayLabelList) {
	    this.localizedDeleteDisplayLabelList = localizedDeleteDisplayLabelList;
	}

	/**
	 * バージョン削除ボタン表示ラベルを取得します。
	 * @return 削除ボタン表示ラベル
	 */
	public String getDeleteSpecificVersionDisplayLabel() {
	    return deleteSpecificVersionDisplayLabel;
	}

	/**
	 * バージョン削除ボタン表示ラベルを設定します。
	 * @param deleteSpecificVersionDisplayLabel 削除ボタン表示ラベル
	 */
	public void setDeleteSpecificVersionDisplayLabel(String deleteSpecificVersionDisplayLabel) {
	    this.deleteSpecificVersionDisplayLabel = deleteSpecificVersionDisplayLabel;
	}

	/**
	 * バージョン削除ボタン表示ラベル多言語設定情報を取得します。
	 * @return バージョン削除ボタン表示ラベル多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedDeleteSpecificVersionDisplayLabelList() {
	    return localizedDeleteSpecificVersionDisplayLabelList;
	}

	/**
	 * バージョン削除ボタン表示ラベル多言語設定情報を設定します。
	 * @param localizedDeleteSpecificVersionDisplayLabelList バージョン削除ボタン表示ラベル多言語設定情報
	 */
	public void setLocalizedDeleteSpecificVersionDisplayLabelList(List<MetaLocalizedString> localizedDeleteSpecificVersionDisplayLabelList) {
	    this.localizedDeleteSpecificVersionDisplayLabelList = localizedDeleteSpecificVersionDisplayLabelList;
	}

	/**
	 * キャンセルアクション名を取得します。
	 * @return キャンセルアクション名
	 */
	public String getCancelActionName() {
		return cancelActionName;
	}

	/**
	 * キャンセルアクション名を設定します。
	 * @param cancelActionName キャンセルアクション名
	 */
	public void setCancelActionName(String cancelActionName) {
		this.cancelActionName = cancelActionName;
	}

	/**
	 * 編集ボタン非表示を取得します。
	 * @return 編集ボタン非表示
	 */
	public boolean isHideDetail() {
	    return hideDetail;
	}

	/**
	 * 編集ボタン非表示を設定します。
	 * @param hideDetail 編集ボタン非表示
	 */
	public void setHideDetail(boolean hideDetail) {
	    this.hideDetail = hideDetail;
	}

	/**
	 * ロックボタン非表示設定を取得します。
	 * @return ロックボタン非表示設定
	 */
	public boolean isHideLock() {
	    return hideLock;
	}

	/**
	 * ロックボタン非表示設定を設定します。
	 * @param hideLock ロックボタン非表示設定
	 */
	public void setHideLock(boolean hideLock) {
	    this.hideLock = hideLock;
	}

	/**
	 * コピーボタン非表示設定を取得します。
	 * @return コピーボタン非表示設定
	 */
	public boolean isNoneDispCopyButton() {
		return isNoneDispCopyButton;
	}

	/**
	 * コピーボタン非表示設定を設定します。
	 * @param isNoneDispCopyButton コピーボタン非表示設定
	 */
	public void setIsNoneDispCopyButton(boolean isNoneDispCopyButton) {
		this.isNoneDispCopyButton = isNoneDispCopyButton;
	}

	/**
	 * 削除ボタン非表示を取得します。
	 * @return 削除ボタン非表示
	 */
	public boolean isHideDelete() {
	    return hideDelete;
	}

	/**
	 * 削除ボタン非表示を設定します。
	 * @param hideDelete 削除ボタン非表示
	 */
	public void setHideDelete(boolean hideDelete) {
	    this.hideDelete = hideDelete;
	}

	/**
	 * バージョン削除ボタン表示を取得します。
	 * @return バージョン削除ボタン表示
	 */
	public boolean isShowDeleteSpecificVersion() {
		return showDeleteSpecificVersion;
	}

	/**
	 * バージョン削除ボタン表示を設定します。
	 * @param showDeleteSpecificVersion バージョン削除ボタン表示
	 */
	public void setShowDeleteSpecificVersion(boolean showDeleteSpecificVersion) {
		this.showDeleteSpecificVersion = showDeleteSpecificVersion;
	}

	/**
	 * Entity権限の可能範囲条件でボタン表示を制御設定を取得します。
	 * @return Entity権限の可能範囲条件でボタン表示を制御設定
	 */
	public boolean isCheckEntityPermissionLimitConditionOfButton() {
		return checkEntityPermissionLimitConditionOfButton;
	}

	/**
	 * Entity権限の可能範囲条件でボタン表示を制御設定を設定します。
	 * @param checkEntityPermissionLimitConditionOfButton Entity権限の可能範囲条件でボタン表示を制御設定
	 */
	public void setCheckEntityPermissionLimitConditionOfButton(boolean checkEntityPermissionLimitConditionOfButton) {
		this.checkEntityPermissionLimitConditionOfButton = checkEntityPermissionLimitConditionOfButton;
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
	 * 定義されている参照プロパティのみを取得を取得します。
	 * @return 定義されている参照プロパティのみを取得
	 */
	public boolean isLoadDefinedReferenceProperty() {
	    return loadDefinedReferenceProperty;
	}

	/**
	 * 定義されている参照プロパティのみを取得を設定します。
	 * @param loadDefinedReferenceProperty 定義されている参照プロパティのみを取得
	 */
	public void setLoadDefinedReferenceProperty(boolean loadDefinedReferenceProperty) {
	    this.loadDefinedReferenceProperty = loadDefinedReferenceProperty;
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
	 * カスタムコピースクリプトを取得します。
	 * @return カスタムコピースクリプト
	 */
	public String getCustomCopyScript() {
	    return customCopyScript;
	}

	/**
	 * カスタムコピースクリプトを設定します。
	 * @param customCopyScript カスタムコピースクリプト
	 */
	public void setCustomCopyScript(String customCopyScript) {
	    this.customCopyScript = customCopyScript;
	}

	/**
	 * Javascriptコード有効可否(編集)を取得します。
	 * @return Javascriptコード有効可否(編集)
	 */
	public Boolean getValidJavascriptDetailPage() {
	    return validJavascriptDetailPage;
	}

	/**
	 * Javascriptコード有効可否(編集)を設定します。
	 * @param validJavascriptDetailPage Javascriptコード有効可否(編集)
	 */
	public void setValidJavascriptDetailPage(Boolean validJavascriptDetailPage) {
	    this.validJavascriptDetailPage = validJavascriptDetailPage;
	}

	/**
	 * Javascriptコード有効可否(表示)を取得します。
	 * @return Javascriptコード有効可否(表示)
	 */
	public Boolean getValidJavascriptViewPage() {
	    return validJavascriptViewPage;
	}

	/**
	 * Javascriptコード有効可否(表示)を設定します。
	 * @param validJavascriptViewPage Javascriptコード有効可否(表示)
	 */
	public void setValidJavascriptViewPage(Boolean validJavascriptViewPage) {
	    this.validJavascriptViewPage = validJavascriptViewPage;
	}

	/**
	 * コピー対象を取得します。
	 * @return コピー対象
	 */
	public CopyTarget getCopyTarget() {
	    return copyTarget;
	}

	/**
	 * コピー対象を設定します。
	 * @param copyTarget コピー対象
	 */
	public void setCopyTarget(CopyTarget copyTarget) {
	    this.copyTarget = copyTarget;
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
	 * 詳細編集画面Handlerクラス名を取得します。
	 * @return 詳細編集画面Handlerクラス名
	 */
	public List<String> getDetailFormViewHandlerName() {
		return detailFormViewHandlerName;
	}

	/**
	 * 詳細編集画面Handlerクラス名を設定します。
	 * @param detailFormViewHandlerName 詳細編集画面Handlerクラス名
	 */
	public void setDetailFormViewHandlerName(List<String> detailFormViewHandlerName) {
		this.detailFormViewHandlerName = detailFormViewHandlerName;
	}

	/**
	 * 初期化スクリプトを取得します。
	 * @return 初期化スクリプト
	 */
	public String getInitScript() {
	    return initScript;
	}

	/**
	 * 初期化スクリプトを設定します。
	 * @param initScript 初期化スクリプト
	 */
	public void setInitScript(String initScript) {
	    this.initScript = initScript;
	}

	@Override
	public MetaDetailFormView copy() {
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

		DetailFormView dForm = (DetailFormView) form;
		hideLock = dForm.isHideLock();
		editActionName = dForm.getEditActionName();
		refEditActionName = dForm.getRefEditActionName();
		editDisplayLabel = dForm.getEditDisplayLabel();
		localizedEditDisplayLabelList = I18nUtil.toMeta(dForm.getLocalizedEditDisplayLabelList());
		copyDisplayLabel = dForm.getCopyDisplayLabel();
		localizedCopyDisplayLabelList = I18nUtil.toMeta(dForm.getLocalizedCopyDisplayLabelList());
		versionupDisplayLabel = dForm.getVersionupDisplayLabel();
		localizedVersionupDisplayLabelList = I18nUtil.toMeta(dForm.getLocalizedVersionupDisplayLabelList());
		insertActionName = dForm.getInsertActionName();
		refInsertActionName = dForm.getRefInsertActionName();
		insertDisplayLabel = dForm.getInsertDisplayLabel();
		localizedInsertDisplayLabelList = I18nUtil.toMeta(dForm.getLocalizedInsertDisplayLabelList());
		updateActionName = dForm.getUpdateActionName();
		refUpdateActionName = dForm.getRefUpdateActionName();
		updateDisplayLabel = dForm.getUpdateDisplayLabel();
		localizedUpdateDisplayLabelList = I18nUtil.toMeta(dForm.getLocalizedUpdateDisplayLabelList());
		deleteActionName = dForm.getDeleteActionName();
		deleteDisplayLabel = dForm.getDeleteDisplayLabel();
		localizedDeleteDisplayLabelList = I18nUtil.toMeta(dForm.getLocalizedDeleteDisplayLabelList());
		deleteSpecificVersionDisplayLabel = dForm.getDeleteSpecificVersionDisplayLabel();
		localizedDeleteSpecificVersionDisplayLabelList = I18nUtil.toMeta(dForm.getLocalizedDeleteSpecificVersionDisplayLabelList());
		cancelActionName = dForm.getCancelActionName();
		hideDetail = dForm.isHideDetail();
		isNoneDispCopyButton = dForm.isNoneDispCopyButton();
		hideDelete = dForm.isHideDelete();
		showDeleteSpecificVersion = dForm.isShowDeleteSpecificVersion();
		checkEntityPermissionLimitConditionOfButton = dForm.isCheckEntityPermissionLimitConditionOfButton();
		isPurge = dForm.isPurge();
		purgeCompositionedEntity = dForm.isPurgeCompositionedEntity();
		loadDefinedReferenceProperty = dForm.isLoadDefinedReferenceProperty();
		forceUpadte = dForm.isForceUpadte();
		showUserNameWithPrivilegedValue = dForm.isShowUserNameWithPrivilegedValue();
		javaScript = dForm.getJavaScript();
		validJavascriptDetailPage = dForm.isValidJavascriptDetailPage();
		validJavascriptViewPage = dForm.isValidJavascriptViewPage();
		copyTarget = dForm.getCopyTarget();
		interrupterName = dForm.getInterrupterName();
		loadEntityInterrupterName = dForm.getLoadEntityInterrupterName();
		detailFormViewHandlerName = dForm.getDetailFormViewHandlerName() == null ? null : new ArrayList<>(dForm.getDetailFormViewHandlerName());
		customCopyScript = dForm.getCustomCopyScript();
		initScript = dForm.getInitScript();
	}

	/**
	 * レイアウト情報の内容を自身に適用します。
	 * @param definitionId Entity定義のID
	 * @return レイアウト情報
	 */
	@Override
	public FormView currentConfig(String definitionId) {
		DetailFormView form = new DetailFormView();
		super.fillTo(form, definitionId);

		form.setHideLock(hideLock);
		form.setEditActionName(editActionName);
		form.setRefEditActionName(refEditActionName);
		form.setEditDisplayLabel(editDisplayLabel);
		form.setLocalizedEditDisplayLabelList(I18nUtil.toDef(localizedEditDisplayLabelList));
		form.setCopyDisplayLabel(copyDisplayLabel);
		form.setLocalizedCopyDisplayLabelList(I18nUtil.toDef(localizedCopyDisplayLabelList));
		form.setVersionupDisplayLabel(versionupDisplayLabel);
		form.setLocalizedVersionupDisplayLabelList(I18nUtil.toDef(localizedVersionupDisplayLabelList));
		form.setInsertActionName(insertActionName);
		form.setRefInsertActionName(refInsertActionName);
		form.setInsertDisplayLabel(insertDisplayLabel);
		form.setLocalizedInsertDisplayLabelList(I18nUtil.toDef(localizedInsertDisplayLabelList));
		form.setUpdateActionName(updateActionName);
		form.setRefUpdateActionName(refUpdateActionName);
		form.setUpdateDisplayLabel(updateDisplayLabel);
		form.setLocalizedUpdateDisplayLabelList(I18nUtil.toDef(localizedUpdateDisplayLabelList));
		form.setDeleteActionName(deleteActionName);
		form.setDeleteDisplayLabel(deleteDisplayLabel);
		form.setLocalizedDeleteDisplayLabelList(I18nUtil.toDef(localizedDeleteDisplayLabelList));
		form.setDeleteSpecificVersionDisplayLabel(deleteSpecificVersionDisplayLabel);
		form.setLocalizedDeleteSpecificVersionDisplayLabelList(I18nUtil.toDef(localizedDeleteSpecificVersionDisplayLabelList));
		form.setCancelActionName(cancelActionName);
		form.setHideDetail(hideDetail);
		form.setIsNoneDispCopyButton(isNoneDispCopyButton);
		form.setHideDelete(hideDelete);
		form.setShowDeleteSpecificVersion(showDeleteSpecificVersion);
		form.setCheckEntityPermissionLimitConditionOfButton(checkEntityPermissionLimitConditionOfButton);
		form.setPurge(isPurge);
		form.setPurgeCompositionedEntity(purgeCompositionedEntity);
		form.setLoadDefinedReferenceProperty(loadDefinedReferenceProperty);
		form.setForceUpadte(forceUpadte);
		form.setShowUserNameWithPrivilegedValue(showUserNameWithPrivilegedValue);
		form.setJavaScript(javaScript);
		form.setInterrupterName(interrupterName);
		form.setLoadEntityInterrupterName(loadEntityInterrupterName);
		form.setDetailFormViewHandlerName(detailFormViewHandlerName == null ? null : new ArrayList<>(detailFormViewHandlerName));
		form.setCustomCopyScript(customCopyScript);
		form.setInitScript(initScript);
		if (validJavascriptDetailPage != null) {
			form.setValidJavascriptDetailPage(validJavascriptDetailPage);
		} else {
			//未設定なら有効扱い
			form.setValidJavascriptDetailPage(true);
		}
		if (validJavascriptViewPage != null) {
			form.setValidJavascriptViewPage(validJavascriptViewPage);
		} else {
			//未設定なら無効扱い
			form.setValidJavascriptViewPage(false);
		}
		form.setCopyTarget(copyTarget);

		return form;
	}

	@Override
	public FormViewRuntime createRuntime(EntityViewRuntime entityView) {
		return new DetailFormViewRuntime(this, entityView);
	}
}
