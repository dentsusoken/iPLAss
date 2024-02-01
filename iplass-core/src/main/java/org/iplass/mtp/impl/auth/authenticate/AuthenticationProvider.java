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

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.trust.TrustedAuthValidator;
import org.iplass.mtp.impl.auth.log.AuthLogger;
import org.iplass.mtp.spi.ServiceInitListener;

/**
 * ログイン認証Provider
 *
 * @author 片野　博之
 * @author K.Higuchi
 *
 */
public interface AuthenticationProvider extends ServiceInitListener<AuthService> {

	/**
	 * ログイン処理を行い、認証されたユーザーへの参照を示すUserHandleを返す。
	 *
	 * @param credential
	 * @return
	 */
	public AccountHandle login(Credential credential);
	
	/**
	 * 指定のユーザーのログアウト処理をする。
	 *
	 * @param user
	 */
	public void logout(AccountHandle user);
	
	/**
	 * ログアウト処理が完了（セッションも無効化）した後のコールバック
	 */
	public default void afterLogout(AccountHandle user) {
	}
	
	/**
	 * ログイン処理が成功した後のコールバック
	 * @param credential
	 * @param user
	 */
	public default void afterLoginSuccess(AccountHandle user) {
	}
	
	public TrustedAuthValidator getTrustedAuthValidator();
	
	/**
	 * ハウスキーピング処理から呼び出される。ゴミデータを消す。
	 */
	public void cleanupData();

	/**
	 * AuthenticationProviderがアカウントの登録、更新、削除機能を保持する場合、
	 * その操作インタフェースであるAccountManagementModuleを返却する。
	 * 登録、更新、削除ができない場合は、nullを返却する。
	 *
	 * @return
	 */
	public AccountManagementModule getAccountManagementModule();
	
	/**
	 * Service-Configに設定された認証プロバイダ名を取得する。
	 *
	 * @return String
	 */
	public String getProviderName();

	/**
	 * このProviderで利用するAuthLoggerを取得。
	 * nullを返却した場合は、デフォルトのAuthLoggerが利用される。
	 * @return
	 */
	@Deprecated
	public AuthLogger getAuthLogger();
	
	public UserEntityResolver getUserEntityResolver();
	
	public Class<? extends Credential> getCredentialType();
	
	public default AutoLoginHandler getAutoLoginHandler() {
		return null;
	}
	
	public boolean isSelectableOnAuthPolicy();

}
