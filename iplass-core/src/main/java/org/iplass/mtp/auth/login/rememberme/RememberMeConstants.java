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

package org.iplass.mtp.auth.login.rememberme;

/**
 * RememberMe機能を利用する場合に利用する定数です。
 * 
 * @author K.Higuchi
 *
 */
public interface RememberMeConstants {
	/**
	 * このフラグはRememberMeTokenCredential自体にセットするのではなく、
	 * IdPasswordCredentialなどで認証成功時に認証状態を保持したい場合に、当該フラグ（Boolean型）をセットします。
	 */
	public static final String FACTOR_REMEMBER_ME_FLAG ="rememberMe";
	
}
