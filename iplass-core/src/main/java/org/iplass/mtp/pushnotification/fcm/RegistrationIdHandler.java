/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.pushnotification.fcm;

/**
 * Firebase Cloud Messaging利用時、
 * レスポンスにてregistrationIdのリフレッシュ、削除が通知された場合に処理を行う場合のハンドラです。
 * RegistrationIdHandlerは、設定ファイルのFCMPushNotificationServiceに設定します。
 *
 * @author K.Higuchi
 *
 */
@Deprecated
public interface RegistrationIdHandler {

	/**
	 * currentIdで示される登録トークンの正規の登録トークンをnewIdで通知します。
	 *
	 * @param currentId 現在の登録トークン
	 * @param newId　正規の新規の登録トークン
	 */
	public void refreshRegistrationId(String currentId, String newId);

	/**
	 * registrationIdで指定される登録トークンは無効であることを通知します。
	 * ユーザーの端末から、アプリケーションがアンインストールされた場合などに、通知されます。
	 *
	 * @param registrationId
	 */
	public void removeRegistrationId(String registrationId);

}
