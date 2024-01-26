/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.builtin;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * T_ACCOUNTテーブルに対応するDTO
 * 
 * @author K.Higuchi
 *
 */
public class BuiltinAccount implements Serializable {
	private static final long serialVersionUID = -1820557220682685853L;
	
	public static final int MAX_LOGIN_ERROR_COUNT = 99;//TODO 固定値でいいか？DB定義上NUMBER(2,0)と定義されている


	/** テナントID */
	private int tenantId;
	/** policy名 */
	private String policyName;
	/** アカウントID */
	private String accountId;
	/** パスワード */
	private String password;
	/** パスワードソルト */
	private String salt;
	/** oid */
	private String oid;
	/** 最終ログイン日時 */
	private Timestamp lastLoginOn;
	/** ログインエラー回数 */
	private int loginErrorCnt;
	/** ログインエラー日時 */
	private Timestamp loginErrorDate;
	/** 最終パスワード更新日付 */
	private Date lastPasswordChange;
	/** 作成者 */
	private String createUser;
	/** 作成日時 */
	private Timestamp createDate;
	/** 更新者 */
	private String updateUser;
	/** 更新日時 */
	private Timestamp updateDate;

	/**
	 * Constractor
	 */
	public BuiltinAccount() {
	}
	
	
	public void loginFail() {
		if (loginErrorCnt < MAX_LOGIN_ERROR_COUNT) {
			loginErrorCnt++;
		}
		loginErrorDate = new Timestamp(System.currentTimeMillis());
	}
	
	public void resetLoginErrorCount() {
		loginErrorCnt = 0;
		loginErrorDate = null;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public Timestamp getLoginErrorDate() {
		return loginErrorDate;
	}

	public void setLoginErrorDate(Timestamp loginErrorDate) {
		this.loginErrorDate = loginErrorDate;
	}

	/**
	 * テナントIDを取得します。
	 * @return テナントID
	 */
	public int getTenantId() {
	    return tenantId;
	}


	/**
	 * テナントIDを設定します。
	 * @param tenantId テナントID
	 */
	public void setTenantId(int tenantId) {
	    this.tenantId = tenantId;
	}


	/**
	 * アカウントIDを取得します。
	 * @return アカウントID
	 */
	public String getAccountId() {
	    return accountId;
	}


	/**
	 * アカウントIDを設定します。
	 * @param accountId アカウントID
	 */
	public void setAccountId(String accountId) {
	    this.accountId = accountId;
	}


	/**
	 * パスワードを取得します。
	 *
	 * @return パスワード
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * パスワードを設定します。
	 *
	 * @param password
	 *            パスワード
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * パスワードソルトを取得します。
	 *
	 * @return salt
	 */
	public String getSalt() {
		if(salt == null){
			return "";
		}
		return salt;
	}

	/**
	 * パスワードソルトを設定します。
	 *
	 * @param  salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * oidを取得します。
	 * @return oid
	 */
	public String getOid() {
	    return oid;
	}


	/**
	 * oidを設定します。
	 * @param oid oid
	 */
	public void setOid(String oid) {
	    this.oid = oid;
	}

	public Timestamp getLastLoginOn() {
		return lastLoginOn;
	}

	public void setLastLoginOn(Timestamp lastLoginOn) {
		this.lastLoginOn = lastLoginOn;
	}

	/**
	 * ログインエラー回数を取得します。
	 *
	 * @return ログインエラー回数
	 */
	public int getLoginErrorCnt() {
		return loginErrorCnt;
	}

	/**
	 * ログインエラー回数を設定します。
	 *
	 * @param loginErrorCnt
	 *            ログインエラー回数
	 */
	public void setLoginErrorCnt(int loginErrorCnt) {
		this.loginErrorCnt = loginErrorCnt;
	}

	/**
	 * 最終パスワード更新日付を取得します。
	 *
	 * @return 最終パスワード更新日付
	 */
	public Date getLastPasswordChange() {
		return lastPasswordChange;
	}

	/**
	 * 最終パスワード更新日付を設定します。
	 *
	 * @param lastPasswordChange
	 *            最終パスワード更新日付
	 */
	public void setLastPasswordChange(Date lastPasswordChange) {
		this.lastPasswordChange = lastPasswordChange;
	}

	/**
	 * 作成者を取得します。
	 *
	 * @return 作成者
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * 作成者を設定します。
	 *
	 * @param createUser
	 *            作成者
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * 作成日時を取得します。
	 *
	 * @return 作成日時
	 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/**
	 * 作成日時を設定します。
	 *
	 * @param createDate
	 *            作成日時
	 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 更新者を取得します。
	 *
	 * @return 更新者
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * 更新者を設定します。
	 *
	 * @param updateUser
	 *            更新者
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * 更新日時を取得します。
	 *
	 * @return 更新日時
	 */
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	/**
	 * 更新日時を設定します。
	 *
	 * @param updateDate
	 *            更新日時
	 */
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result
				+ ((createUser == null) ? 0 : createUser.hashCode());
		result = prime * result
				+ ((lastLoginOn == null) ? 0 : lastLoginOn.hashCode());
		result = prime
				* result
				+ ((lastPasswordChange == null) ? 0 : lastPasswordChange
						.hashCode());
		result = prime * result + loginErrorCnt;
		result = prime * result
				+ ((loginErrorDate == null) ? 0 : loginErrorDate.hashCode());
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((policyName == null) ? 0 : policyName.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		result = prime * result + tenantId;
		result = prime * result
				+ ((updateDate == null) ? 0 : updateDate.hashCode());
		result = prime * result
				+ ((updateUser == null) ? 0 : updateUser.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuiltinAccount other = (BuiltinAccount) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
			return false;
		if (lastLoginOn == null) {
			if (other.lastLoginOn != null)
				return false;
		} else if (!lastLoginOn.equals(other.lastLoginOn))
			return false;
		if (lastPasswordChange == null) {
			if (other.lastPasswordChange != null)
				return false;
		} else if (!lastPasswordChange.equals(other.lastPasswordChange))
			return false;
		if (loginErrorCnt != other.loginErrorCnt)
			return false;
		if (loginErrorDate == null) {
			if (other.loginErrorDate != null)
				return false;
		} else if (!loginErrorDate.equals(other.loginErrorDate))
			return false;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (policyName == null) {
			if (other.policyName != null)
				return false;
		} else if (!policyName.equals(other.policyName))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		if (tenantId != other.tenantId)
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		if (updateUser == null) {
			if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BuiltinAccount [tenantId=" + tenantId + ", policyName="
				+ policyName + ", accountId=" + accountId + ", password="
				+ "***" + ", salt=" + "***" + ", oid=" + oid
				+ ", lastLoginOn=" + lastLoginOn + ", loginErrorCnt="
				+ loginErrorCnt + ", loginErrorDate=" + loginErrorDate
				+ ", lastPasswordChange=" + lastPasswordChange
				+ ", createUser=" + createUser + ", createDate=" + createDate
				+ ", updateUser=" + updateUser + ", updateDate=" + updateDate
				+ "]";
	}

}
