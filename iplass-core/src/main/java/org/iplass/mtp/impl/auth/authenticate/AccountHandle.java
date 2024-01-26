/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate;

import java.io.Serializable;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;

/**
 * 認証情報を表すインタフェース。
 * ユーザーが認証されている間、ユーザー毎にセッションに保持される。
 * セッションの間、キャッシュしときたい情報などはここに保持する。
 *
 * @author K.Higuchi
 *
 */
public interface AccountHandle extends Serializable {

	//FIXME ここに持つべきではない BuiltInの場合の情報
	/** パスワード変更日Attribute key */
	public static final String LAST_PASSWORD_CHANGE = "lastPasswordChange";
	public static final String LAST_LOGIN_ON = "lastLoginOn";
	
	//TODO 明示的にgetterを定義したほうがよいか？
	public static final String GROUP_CODE = "groupCode";

	public boolean isAccountLocked();

	public boolean isExpired();

	public boolean isInitialLogin();

	public Credential getCredential();
	
	public String getUnmodifiableUniqueKey();
	
//	public User getUser();
	public Map<String, Object> getAttributeMap();

	//FIXME ここに持つべきではない
	public void setAuthenticationProviderIndex(int authenticationProviderIndex);
	public int getAuthenticationProviderIndex();
}
