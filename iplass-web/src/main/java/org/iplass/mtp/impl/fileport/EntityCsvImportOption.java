/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.fileport;

import java.io.Serializable;

/**
 * EntityのImport条件
 */
public class EntityCsvImportOption implements Serializable {

	private static final long serialVersionUID = 8945973269620341690L;

	/** オプションの有無 */
	private boolean withOption = false;

	/** 既存データをすべて削除する */
	private boolean truncate = false;

	/** BulkUpdateで更新する */
	private boolean bulkUpdate = false;

	/** エラーデータはSkipし処理を続行する */
	private boolean errorSkip = false;

	/** 存在しないプロパティは無視してデータを取込む */
	private boolean ignoreNotExistsProperty = true;

	/** Listnerを実行する */
	private boolean notifyListeners = false;

	/** Validationを実行する(更新不可項目を対象にする場合はfalseに強制設定) */
	private boolean withValidation = false;

	/** 更新不可項目を更新対象にする */
	private boolean updateDisupdatableProperty = false;

	/** InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定 */
	private boolean insertEnableAuditPropertySpecification = false;

	/** Import時にOIDに付与するPrefix */
	private String prefixOid = "";

	/** Commit単位(件数) */
	private Integer commitLimit = -1;

	/** 強制的に更新を実行 */
	private boolean fourceUpdate = false;

	/** 更新時にキーとするUniqueKey */
	private String uniqueKey = null;

	/** Locale(未指定時はデフォルト) */
	private String locale;

	/** Timezone(未指定時はデフォルト) */
	private String timezone;

	/**
	 * オプションの有無を取得
	 * @return オプションの有無
	 */
	public boolean isWithOption() {
		return withOption;
	}

	/**
	 * オプションの有無を設定
	 * @param withOption オプションの有無
	 */
	public void setWithOption(boolean withOption) {
		this.withOption = withOption;
	}

	/**
	 * オプションの有無を有に設定
	 */
	public void withOption() {
		setWithOption(true);
	}
	
	/**
	 * 既存データをすべて削除するかを取得
	 * @return 既存データをすべて削除するか
	 */
	public boolean isTruncate() {
		return truncate;
	}

	/**
	 * 既存データをすべて削除するかを設定
	 * @param truncate 既存データをすべて削除するか
	 */
	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}

	/**
	 * 既存データをすべて削除するかを設定
	 * @param truncate 既存データをすべて削除するか
	 * @return インスタンス
	 */
	public EntityCsvImportOption truncate(boolean truncate) {
		this.truncate = truncate;
		return this;
	}

	/**
	 * BulkUpdateで更新するかを取得
	 * @return BulkUpdateで更新するか
	 */
	public boolean isBulkUpdate() {
		return bulkUpdate;
	}

	/**
	 * BulkUpdateで更新するかを設定
	 * @param bulkUpdate BulkUpdateで更新するか
	 */
	public void setBulkUpdate(boolean bulkUpdate) {
		this.bulkUpdate = bulkUpdate;
	}

	/**
	 * BulkUpdateで更新するかを設定
	 * @param bulkUpdate BulkUpdateで更新するか
	 * @return インスタンス
	 */
	public EntityCsvImportOption bulkUpdate(boolean bulkUpdate) {
		this.bulkUpdate = bulkUpdate;
		return this;
	}

	/**
	 * エラーデータはSkipし処理を続行するかを取得
	 * @return エラーデータはSkipし処理を続行する
	 */
	public boolean isErrorSkip() {
		return errorSkip;
	}

	/**
	 * エラーデータはSkipし処理を続行するかを設定
	 * @param errorSkip エラーデータはSkipし処理を続行する
	 */
	public void setErrorSkip(boolean errorSkip) {
		this.errorSkip = errorSkip;
	}

	/**
	 * エラーデータはSkipし処理を続行するかを設定
	 * @param errorSkip エラーデータはSkipし処理を続行する
	 * @return インスタンス
	 */
	public EntityCsvImportOption errorSkip(boolean errorSkip) {
		this.errorSkip = errorSkip;
		return this;
	}

	/**
	 * 存在しないプロパティは無視してデータを取込むかを取得
	 * @return 存在しないプロパティは無視してデータを取込む
	 */
	public boolean isIgnoreNotExistsProperty() {
		return ignoreNotExistsProperty;
	}

	/**
	 * 存在しないプロパティは無視してデータを取込むかを設定
	 * @param ignoreNotExistsProperty セットする ignoreNotExistsProperty
	 */
	public void setIgnoreNotExistsProperty(boolean ignoreNotExistsProperty) {
		this.ignoreNotExistsProperty = ignoreNotExistsProperty;
	}

	/**
	 * 存在しないプロパティは無視してデータを取込むかを設定
	 * @param ignoreNotExistsProperty セットする ignoreNotExistsProperty
	 * @return インスタンス
	 */
	public EntityCsvImportOption ignoreNotExistsProperty(boolean ignoreNotExistsProperty) {
		this.ignoreNotExistsProperty = ignoreNotExistsProperty;
		return this;
	}

	/**
	 * Listnerを実行するかを取得
	 * @return Listnerを実行するか
	 */
	public boolean isNotifyListeners() {
		return notifyListeners;
	}

	/**
	 * Listnerを実行するかを設定
	 * @param notifyListeners Listnerを実行するか
	 */
	public void setNotifyListeners(boolean notifyListeners) {
		this.notifyListeners = notifyListeners;
	}

	/**
	 * Listnerを実行するかを設定
	 * @param notifyListeners Listnerを実行するか
	 * @return インスタンス
	 */
	public EntityCsvImportOption notifyListeners(boolean notifyListeners) {
		this.notifyListeners = notifyListeners;
		return this;
	}

	/**
	 * Validationを実行するかを取得
	 * @return Validationを実行するか
	 */
	public boolean isWithValidation() {
		return withValidation;
	}

	/**
	 * Validationを実行するかを設定
	 * @param withValidation Validationを実行するか
	 */
	public void setWithValidation(boolean withValidation) {
		this.withValidation = withValidation;
	}

	/**
	 * Validationを実行するかを設定
	 * @param withValidation Validationを実行するか
	 * @return インスタンス
	 */
	public EntityCsvImportOption withValidation(boolean withValidation) {
		this.withValidation = withValidation;
		return this;
	}

	/**
	 * 更新不可項目を更新対象にするかを取得
	 * @return 更新不可項目を更新対象にするか
	 */
	public boolean isUpdateDisupdatableProperty() {
		return updateDisupdatableProperty;
	}

	/**
	 * 更新不可項目を更新対象にするかを設定
	 * @param updateDisupdatableProperty 更新不可項目を更新対象にするか
	 */
	public void setUpdateDisupdatableProperty(boolean updateDisupdatableProperty) {
		this.updateDisupdatableProperty = updateDisupdatableProperty;
	}

	/**
	 * 更新不可項目を更新対象にするかを設定
	 * @param updateDisupdatableProperty 更新不可項目を更新対象にするか
	 * @return インスタンス
	 */
	public EntityCsvImportOption updateDisupdatableProperty(boolean updateDisupdatableProperty) {
		this.updateDisupdatableProperty = updateDisupdatableProperty;
		return this;
	}

	/**
	 * InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定するかを取得
	 * @return InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定するか
	 */
	public boolean isInsertEnableAuditPropertySpecification() {
		return insertEnableAuditPropertySpecification;
	}

	/**
	 * InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定かを設定
	 * @param insertEnableAuditPropertySpecification InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定するか
	 */
	public void setInsertEnableAuditPropertySpecification(boolean insertEnableAuditPropertySpecification) {
		this.insertEnableAuditPropertySpecification = insertEnableAuditPropertySpecification;
	}

	/**
	 * InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定かを設定
	 * @param insertEnableAuditPropertySpecification InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定するか
	 * @return インスタンス
	 */
	public EntityCsvImportOption insertEnableAuditPropertySpecification(boolean insertEnableAuditPropertySpecification) {
		this.insertEnableAuditPropertySpecification = insertEnableAuditPropertySpecification;
		return this;
	}

	/**
	 * Import時にOIDに付与するPrefixを取得
	 * @return Import時にOIDに付与するPrefix
	 */
	public String getPrefixOid() {
		return prefixOid;
	}

	/**
	 * Import時にOIDに付与するPrefixを設定
	 * @param prefixOid Import時にOIDに付与するPrefix
	 */
	public void setPrefixOid(String prefixOid) {
		this.prefixOid = prefixOid;
	}

	/**
	 * Import時にOIDに付与するPrefixを設定
	 * @param prefixOid Import時にOIDに付与するPrefix
	 * @return インスタンス
	 */
	public EntityCsvImportOption prefixOid(String prefixOid) {
		this.prefixOid = prefixOid;
		return this;
	}

	/**
	 * Commit単位(件数)を取得
	 * @return Commit単位(件数)
	 */
	public Integer getCommitLimit() {
		return commitLimit;
	}

	/**
	 * Commit単位(件数)を設定
	 * @param commitLimit Commit単位(件数)
	 */
	public void setCommitLimit(Integer commitLimit) {
		this.commitLimit = commitLimit;
	}

	/**
	 * Commit単位(件数)を設定
	 * @param commitLimit Commit単位(件数)
	 * @return インスタンス
	 */
	public EntityCsvImportOption commitLimit(Integer commitLimit) {
		this.commitLimit = commitLimit;
		return this;
	}

	/**
	 * 強制的に更新を実行するかを取得
	 * @return 強制的に更新を実行するか
	 */
	public boolean isFourceUpdate() {
		return fourceUpdate;
	}

	/**
	 * 強制的に更新を実行するかを設定
	 * @param fourceUpdate 強制的に更新を実行するか
	 */
	public void setFourceUpdate(boolean fourceUpdate) {
		this.fourceUpdate = fourceUpdate;
	}

	/**
	 * 強制的に更新を実行するかを設定
	 * @param fourceUpdate 強制的に更新を実行するか
	 * @return インスタンス
	 */
	public EntityCsvImportOption fourceUpdate(boolean fourceUpdate) {
		this.fourceUpdate = fourceUpdate;
		return this;
	}

	/**
	 * 更新時にキーとするUniqueKeyを取得
	 * @return 更新時にキーとするUniqueKey
	 */
	public String getUniqueKey() {
		return uniqueKey;
	}

	/**
	 * 更新時にキーとするUniqueKeyを設定
	 * @param uniqueKey 更新時にキーとするUniqueKey
	 */
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	/**
	 * 更新時にキーとするUniqueKeyを設定
	 * @param uniqueKey 更新時にキーとするUniqueKey
	 * @return インスタンス
	 */
	public EntityCsvImportOption uniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
		return this;
	}

	/**
	 * Localeを取得
	 * @return Locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Localeを設定
	 * @param locale Locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Localeを設定
	 * @param locale Locale
	 * @return インスタンス
	 */
	public EntityCsvImportOption locale(String locale) {
		this.locale = locale;
		return this;
	}

	/**
	 * Timezoneを取得
	 * @return Timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * Timezoneを設定
	 * @param timezone Timezone
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * Timezoneを設定
	 * @param timezone Timezone
	 * @return インスタンス
	 */
	public EntityCsvImportOption timezone(String timezone) {
		this.timezone = timezone;
		return this;
	}

}
