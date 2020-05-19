/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.dto.pack;

import java.io.Serializable;

public class PackageImportCondition implements Serializable {

	private static final long serialVersionUID = 1008967394774875801L;

	/** Truncate */
	private boolean truncate = false;

	/** BulkUpdateで更新 */
	private boolean bulkUpdate = false;

	/** エラーデータはSkipし処理を続行 */
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

	/** Locale(未指定時はデフォルト) */
	private String locale;
	/** Timezone(未指定時はデフォルト) */
	private String timezone;

	public boolean isTruncate() {
		return truncate;
	}
	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}

	public boolean isBulkUpdate() {
		return bulkUpdate;
	}
	public void setBulkUpdate(boolean bulkUpdate) {
		this.bulkUpdate = bulkUpdate;
	}

	public boolean isErrorSkip() {
		return errorSkip;
	}
	public void setErrorSkip(boolean errorSkip) {
		this.errorSkip = errorSkip;
	}

	public boolean isIgnoreNotExistsProperty() {
		return ignoreNotExistsProperty;
	}
	public void setIgnoreNotExistsProperty(boolean ignoreNotExistsProperty) {
		this.ignoreNotExistsProperty = ignoreNotExistsProperty;
	}

	public boolean isNotifyListeners() {
		return notifyListeners;
	}
	public void setNotifyListeners(boolean notifyListeners) {
		this.notifyListeners = notifyListeners;
	}

	public boolean isWithValidation() {
		return withValidation;
	}
	public void setWithValidation(boolean withValidation) {
		this.withValidation = withValidation;
	}

	public boolean isUpdateDisupdatableProperty() {
		return updateDisupdatableProperty;
	}
	public void setUpdateDisupdatableProperty(boolean updateDisupdatableProperty) {
		this.updateDisupdatableProperty = updateDisupdatableProperty;
	}

	public boolean isInsertEnableAuditPropertySpecification() {
		return insertEnableAuditPropertySpecification;
	}
	public void setInsertEnableAuditPropertySpecification(boolean insertEnableAuditPropertySpecification) {
		this.insertEnableAuditPropertySpecification = insertEnableAuditPropertySpecification;
	}

	public Integer getCommitLimit() {
		return commitLimit;
	}
	public void setCommitLimit(Integer commitLimit) {
		this.commitLimit = commitLimit;
	}

	public String getPrefixOid() {
		return prefixOid;
	}
	public void setPrefixOid(String prefixOid) {
		this.prefixOid = prefixOid;
	}

	public boolean isFourceUpdate() {
		return fourceUpdate;
	}
	public void setFourceUpdate(boolean fourceUpdate) {
		this.fourceUpdate = fourceUpdate;
	}

	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

}
