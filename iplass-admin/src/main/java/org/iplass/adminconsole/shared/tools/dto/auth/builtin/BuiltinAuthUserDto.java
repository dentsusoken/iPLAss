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

package org.iplass.adminconsole.shared.tools.dto.auth.builtin;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class BuiltinAuthUserDto implements Serializable {

	private static final long serialVersionUID = -7468050075120175140L;

	private boolean isAccountExist;
	private boolean isUserExist;

	/** OID */
	private String oid;
	/** ユーザID */
	private String accountId;
	/** policy名 */
	private String policyName;

	/** 名 */
	private String name;
	/** メールアドレス */
	private String mail;
	/** 管理者 */
	private boolean admin;
	/** 有効期間開始日時 */
	private Timestamp startDate;
	/** 有効期間終了日時 */
	private Timestamp endDate;

	/** 最終ログイン日時 */
	private Timestamp lastLoginOn;
	/** ログインエラー回数 */
	private int loginErrorCnt;
	/** ログインエラー日時 */
	private Timestamp loginErrorDate;
	/** 最終パスワード更新日付 */
	private Date lastPasswordChange;
	/** パスワード有効残日数 */
	private Integer passwordRemainDays;

	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	/**
	 * @return accountId
	 */
	public String getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId セットする accountId
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return mail
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail セットする mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	/**
	 * @return admin
	 */
	public boolean isAdmin() {
		return admin;
	}
	/**
	 * @param admin セットする admin
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	/**
	 * @return startDate
	 */
	public Timestamp getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate セットする startDate
	 */
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return endDate
	 */
	public Timestamp getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate セットする endDate
	 */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return lastLoginOn
	 */
	public Timestamp getLastLoginOn() {
		return lastLoginOn;
	}
	/**
	 * @param lastLoginOn セットする lastLoginOn
	 */
	public void setLastLoginOn(Timestamp lastLoginOn) {
		this.lastLoginOn = lastLoginOn;
	}
	/**
	 * @return loginErrorCnt
	 */
	public int getLoginErrorCnt() {
		return loginErrorCnt;
	}
	/**
	 * @param loginErrorCnt セットする loginErrorCnt
	 */
	public void setLoginErrorCnt(int loginErrorCnt) {
		this.loginErrorCnt = loginErrorCnt;
	}
	public Timestamp getLoginErrorDate() {
		return loginErrorDate;
	}
	public void setLoginErrorDate(Timestamp loginErrorDate) {
		this.loginErrorDate = loginErrorDate;
	}
	/**
	 * @return lastPasswordChange
	 */
	public Date getLastPasswordChange() {
		return lastPasswordChange;
	}
	/**
	 * @param lastPasswordChange セットする lastPasswordChange
	 */
	public void setLastPasswordChange(Date lastPasswordChange) {
		this.lastPasswordChange = lastPasswordChange;
	}
	/**
	 * @return passwordRemainDays
	 */
	public Integer getPasswordRemainDays() {
		return passwordRemainDays;
	}
	/**
	 * @param passwordRemainDays セットする passwordRemainDays
	 */
	public void setPasswordRemainDays(Integer passwordRemainDays) {
		this.passwordRemainDays = passwordRemainDays;
	}

	public boolean isUserExist() {
		return isUserExist;
	}
	public void setUserExist(boolean isUserExist) {
		this.isUserExist = isUserExist;
	}
	public boolean isAccountExist() {
		return isAccountExist;
	}
	public void setAccountExist(boolean isAccountExist) {
		this.isAccountExist = isAccountExist;
	}

}
