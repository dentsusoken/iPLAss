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
 * FCM v1 API 用 Push 通知状態
 *
 * <p>
 * push 通知の実行結果の状態を現します。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public enum PushNotificationStatus {
	/**
	 * 成功
	 * <p>
	 * FCM サービスまでのリクエスト成功です。
	 * 対象デバイスへの通知状態は、FCM サービスを確認してください。
	 * </p>
	 */
	SUCCESS,
	/**
	 * 失敗：通信タイムアウト
	 * <p>
	 * 通信のタイムアウトに関連する例外です。
	 * </p>
	 */
	FAIL_TIMEOUT,
	/**
	 * 失敗：デバイスが登録されていない
	 * <p>
	 * 通知先に設定したデバイストークンは登録されていない為、
	 * 対象デバイスへの送信をすることができません。
	 * </p>
	 */
	FAIL_DEVICE_UNREGISTERED,
	/**
	 * 失敗：リトライ可能
	 * <p>
	 * 通知は失敗しましたが、通知サービス要因のエラーです。
	 * 再試行を推奨します。
	 *
	 * <a href="https://firebase.google.com/docs/cloud-messaging/scale-fcm#handling-retries">再試行の処理</a>を確認し、通知を再送信することを検討してください。
	 * </p>
	 */
	FAIL_RETRYABLE,
	/**
	 * 失敗
	 * <p>
	 * カテゴリ分けされない一般的な失敗です。エラー内容を確認してください。
	 * </p>
	 */
	FAIL;
}
