/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.auth.policy;

import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;

/**
 * アカウントの作成、パスワードリセット時、ロックアウト時、ログイン時などの通知を受け取るListenerのインタフェースです。。
 * 
 * @author K.Higuchi
 *
 */
public interface AccountNotificationListener {
	
	/**
	 * AccountNotificationListener自体の初期化処理を記述可能です。
	 * AccountNotificationListenerのインスタンスが生成された後、呼び出されます。
	 * @param policy AccountNotificationListenerが定義されているAuthenticationPolicyDefinition
	 */
	public default void init(AuthenticationPolicyDefinition policy) {
	}
	
	/**
	 * ユーザーアカウントが作成された後呼び出されます。
	 *
	 * @param notification 通知の本体
	 */
	public default void created(PasswordNotification notification) {
	}

	/**
	 * ユーザーアカウントのパスワードがリセットされた後呼び出されます。
	 *
	 * @param notification 通知の本体
	 */
	public default void credentialReset(PasswordNotification notification) {
	}
	
	/**
	 * ユーザーアカウントがロックアウトされた後呼び出されます。
	 * 
	 * @param notification 通知の本体
	 */
	public default void rockedout(AccountNotification notification) {
	}
	
	
	/**
	 * パスワードが更新された後呼び出されます。
	 * 
	 * @param notification
	 */
	public default void credentialUpdated(PasswordNotification notification) {
	}
	
	/**
	 * パスワード以外の属性が更新された後呼び出されます。
	 * 
	 * @param notification
	 */
	public default void propertyUpdated(PropertyNotification notification) {
	}
	
	/**
	 * ユーザーアカウントが削除される場合呼び出されます。
	 * （※他のメソッドと異なり、実際の削除処理前に呼び出されます）
	 * 
	 * @param notification
	 */
	public default void remove(AccountNotification notification) {
	}
	
	/**
	 * ログインに成功した場合呼び出されます。
	 * 
	 * @param notification
	 */
	public default void loginSuccess(LoginNotification notification) {
	}
	
	/**
	 * ログインに失敗した場合呼び出されます。
	 * ログイン失敗時には適用する認証ポリシーが確定できないため、
	 * DEFAULTポリシーにに定義されるListenerに対して通知されます。
	 * また、notificationに保持されるuserOidは確定できないためnullです。
	 * 
	 * @param notification
	 */
	public default void loginFailed(LoginNotification notification) {
	}

}
