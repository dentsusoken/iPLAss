/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.auth.token;

import java.util.List;

import org.iplass.mtp.auth.login.Credential;

/**
 * ユーザーに紐付く認証トークンの一覧を表すインタフェースです。
 * 
 * @author K.Higuchi
 *
 */
public interface AuthTokenInfoList {
	
	public List<AuthTokenInfo> getList();
	public AuthTokenInfo get(String type, String key);
	public void remove(String type);
	public void remove(String type, String key);
	public Credential generateNewToken(AuthTokenInfo newTokenInfo);
}
