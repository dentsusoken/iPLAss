/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.auth.login;

/**
 * 認証処理の種類を表すenum。
 * 
 * AuthenticationProviderのlogin()呼び出し時に
 * CredentialのauthenticationFactorにセットされる。
 * 通常のログインなのか、もしくは信頼された認証を必要とするかを判断可能。
 * 
 * ログイン処理時に明示的に信頼された認証を必要とする場合は、
 * AuthenticationProcessType.TRUSTED_LOGINをCredentialにセットしてログイン処理を呼び出す。
 * 
 * @author K.Higuchi
 *
 */
public enum AuthenticationProcessType {
	LOGIN,
	TRUSTED_LOGIN
}
