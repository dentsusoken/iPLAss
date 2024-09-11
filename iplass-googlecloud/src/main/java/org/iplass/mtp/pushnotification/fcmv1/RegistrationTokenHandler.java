/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.pushnotification.fcmv1;

/**
 * FCM v1 API 利用時登録トークンハンドラ
 * <p>
 * トークン通知において、レスポンスに応じた処理を定義する。
 * </p>
 *
 * <p>
 * 本クラスは service-config 設定ファイルの FCM V1 API 用 PushNotificationService に設定します。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public interface RegistrationTokenHandler {
	/**
	 * デバイストークン未登録の場合に実行する処理
	 * @param target 未登録トークン情報
	 */
	void unregistered(PushNotificationTarget target);
}
