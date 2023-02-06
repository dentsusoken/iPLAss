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

package org.iplass.mtp.tenant;

import java.util.List;

/**
 * テナントの認証情報
 *
 * @author 藤田 義弘
 *
 */
public class TenantAuthInfo extends TenantConfig {

	/** Serial Version UID	 */
	private static final long serialVersionUID = -4034214288539846347L;

	/** Remember Me 利用有無*/
	private boolean useRememberMe;

	private List<String> userAdminRoles;

	/**
	 * Constractor
	 */
	public TenantAuthInfo() {
	}

	/**
	 * ユーザーを管理可能とするロールを取得します。
	 *
	 * @return
	 */
	public List<String> getUserAdminRoles() {
		return userAdminRoles;
	}

	/**
	 * ユーザーを管理可能（パスワードリセット、accountPlicyの設定、adminフラグの設定など）とするロールを指定します。
	 * adminフラグを更新不可としたい場合は、別途当該ロールにEntity権限を設定します。
	 *
	 * @param userAdminRoles
	 */
	public void setUserAdminRoles(List<String> userAdminRoles) {
		this.userAdminRoles = userAdminRoles;
	}

	/**
	 * RememberMe機能を利用するか否かを取得します。
	 * @return
	 */
	public boolean isUseRememberMe() {
		return useRememberMe;
	}

	/**
	 * RememberMe機能を利用するか否かを設定します。
	 * @param useRememberMe
	 */
	/**
	 * @param useRememberMe
	 */
	public void setUseRememberMe(boolean useRememberMe) {
		this.useRememberMe = useRememberMe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (useRememberMe ? 1231 : 1237);
		result = prime * result
				+ ((userAdminRoles == null) ? 0 : userAdminRoles.hashCode());
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
		TenantAuthInfo other = (TenantAuthInfo) obj;
		if (useRememberMe != other.useRememberMe)
			return false;
		if (userAdminRoles == null) {
			if (other.userAdminRoles != null)
				return false;
		} else if (!userAdminRoles.equals(other.userAdminRoles))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TenantAuthInfo [useRememberMe=" + useRememberMe
				+ ", userAdminRoles=" + userAdminRoles + "]";
	}

}
