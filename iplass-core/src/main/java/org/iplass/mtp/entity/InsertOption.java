/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.entity;

/**
 * Entity追加処理時に指定可能なオプションです。
 * 
 * @author K.Higuchi
 *
 */
public class InsertOption {

	private boolean regenerateOid = true;
	private boolean regenerateAutoNumber = false;
	private boolean versionSpecified = false;
	private boolean withValidation = true;
	private boolean notifyListeners = true;
	private boolean enableAuditPropertySpecification = false;
	
	private boolean localized = false;
	
	public InsertOption() {
	}
	
	public InsertOption copy() {
		InsertOption copy = new InsertOption();
		copy.regenerateOid = regenerateOid;
		copy.regenerateAutoNumber = regenerateAutoNumber;
		copy.versionSpecified = versionSpecified;
		copy.withValidation = withValidation;
		copy.notifyListeners = notifyListeners;
		copy.enableAuditPropertySpecification = enableAuditPropertySpecification;
		copy.localized = localized;
		
		return copy;
	}
	
	public boolean isEnableAuditPropertySpecification() {
		return enableAuditPropertySpecification;
	}

	/**
	 * インサートするEntityにcreateBy,createDate,updateBy,updateDateの値を
	 * 指定してその値のまま登録する場合にtrueを指定します。
	 * デフォルトはfalseです。
	 * このフラグを利用する場合、
	 * 当該処理を呼び出すユーザーがadmin権限を保持している必要があります。
	 * 
	 * @param enableAuditPropertySpecification
	 */
	public void setEnableAuditPropertySpecification(boolean enableAuditPropertySpecification) {
		this.enableAuditPropertySpecification = enableAuditPropertySpecification;
	}

	public boolean isVersionSpecified() {
		return versionSpecified;
	}

	/**
	 * バージョン管理されているEntityをインサートする際に、
	 * 指定したバージョン番号のデータとしてインサートする場合にtrueをセットします。
	 * falseの場合はバージョン番号は0としてインサートされます。
	 * デフォルトはfalseです。
	 * 
	 * @param versionSpecified
	 */
	public void setVersionSpecified(boolean versionSpecified) {
		this.versionSpecified = versionSpecified;
	}

	public boolean isRegenerateOid() {
		return regenerateOid;
	}

	/**
	 * 常に（oidがEntityに指定してあった場合でも）oidを新規生成するかどうかを設定します。
	 * デフォルトはtrueです。
	 * 
	 * @param regenerateOid
	 */
	public void setRegenerateOid(boolean regenerateOid) {
		this.regenerateOid = regenerateOid;
	}

	public boolean isRegenerateAutoNumber() {
		return regenerateAutoNumber;
	}

	/**
	 * 常に（autoNumber項目がセットされていた場合でも）autoNumber項目を新規生成するかどうかを設定します。
	 * デフォルトはfalseです。
	 * 
	 * @param regenerateAutoNumber
	 */
	public void setRegenerateAutoNumber(boolean regenerateAutoNumber) {
		this.regenerateAutoNumber = regenerateAutoNumber;
	}

	public boolean isWithValidation() {
		return withValidation;
	}

	/**
	 * 追加時、バリデーションを行うか否かをセットします。
	 * デフォルトはtrueです。
	 * 
	 * @param withValidation
	 */
	public void setWithValidation(boolean withValidation) {
		this.withValidation = withValidation;
	}

	public boolean isNotifyListeners() {
		return notifyListeners;
	}

	/**
	 * 追加時、 {@link EntityEventListener}に通知するか否かをセットします。
	 * デフォルトはtrueです。
	 * 
	 * @param notifyListeners
	 */
	public void setNotifyListeners(boolean notifyListeners) {
		this.notifyListeners = notifyListeners;
	}
	
	public boolean isLocalized() {
		return localized;
	}

	/**
	 * localized項目を更新対象とするか否かをセットします。
	 * デフォルトはfalseです。
	 * 
	 * @param localized
	 */
	public void setLocalized(boolean localized) {
		this.localized = localized;
	}

	/**
	 * 追加時にバリデーションを行わないように設定します。
	 * @return
	 */
	public InsertOption withoutValidation() {
		this.withValidation = false;
		return this;
	}
	
	/**
	 * 追加時に{@link EntityEventListener}に通知しないように設定します。
	 * @return
	 */
	public InsertOption unnotifyListeners() {
		this.notifyListeners = false;
		return this;
	}
	
	/**
	 * oidがnullの場合のみ生成し、指定されていた場合はそれをそのまま利用するように設定します。
	 * @return
	 */
	public InsertOption generateOidIfNull() {
		this.regenerateOid = false;
		return this;
	}
	
	/**
	 * 常にautoNumber項目の値を再生成するように設定します。
	 * @return
	 */
	public InsertOption regenerateAutoNumber() {
		this.regenerateAutoNumber = true;
		return this;
	}
	
	/**
	 * Entityに指定されたバージョンとしてインサートするように設定します。
	 * （Entityがバージョン管理されている場合）
	 * @return
	 */
	public InsertOption versionSpecified() {
		this.versionSpecified = true;
		return this;
	}
	
	/**
	 * インサートするEntityにcreateBy,createDate,updateBy,updateDateの値を
	 * 指定してその値のまま登録するように設定します。
	 * このフラグを利用する場合、
	 * 当該処理を呼び出すユーザーがadmin権限を保持している必要があります。
	 * 
	 * @return
	 */
	public InsertOption auditPropertySpecified() {
		this.enableAuditPropertySpecification = true;
		return this;
	}
	
	/**
	 * localized=trueに設定します。
	 * @return
	 */
	public InsertOption localized() {
		this.localized = true;
		return this;
	}

	@Override
	public String toString() {
		return "InsertOption [regenerateOid=" + regenerateOid + ", regenerateAutoNumber=" + regenerateAutoNumber
				+ ", versionSpecified=" + versionSpecified + ", withValidation=" + withValidation
				+ ", notifyListeners=" + notifyListeners + ", enableAuditPropertySpecification=" + enableAuditPropertySpecification
				+ ", localized=" + localized + "]";
	}

}
