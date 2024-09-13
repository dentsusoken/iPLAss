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
 * FCM V1 API 用 Push 通知レスポンス詳細公開インターフェース
 *
 * <p>
 * FCM へ Push 通知の要求した結果の詳細情報です。
 * 実行結果のステータスや、レスポンス詳細等を確認することが可能です。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public interface PushNotificationResponseDetail {

	/**
	 * ステータスを取得する
	 * @return ステータス
	 */
	PushNotificationStatus getStatus();

	/**
	 * 通知対象を取得する
	 * @return target 通知対象
	 */
	PushNotificationTarget getTarget();

	/**
	 * レスポンス本体を取得する
	 * @return レスポンス本体
	 */
	String getResponse();

	/**
	 * 送信されたメッセージの識別子を取得する。正常時のみ設定される。
	 * <p>
	 * projects/&ast;/messages/&#x7b;message_id&#x7d; 形式
	 * </p>
	 * @return メッセージの識別子
	 */
	String getMessageId();

	/**
	 * 再実行間隔（秒）を取得する。リトライ可能な場合のみ設定される。
	 * @return 再実行間隔
	 */
	long getRetryAfterSeconds();

	/**
	 * リトライ回数を取得する。
	 * @return リトライ回数
	 */
	int getRetryCount();

	/**
	 * エラーメッセージを取得する。エラー終了時のみ設定される。
	 * @return エラーメッセージ
	 */
	String getErrorMessage();

	/**
	 * 例外を取得します。通信実行に際して例外が発生した場合に設定される。
	 * @return 例外
	 */
	Throwable getCause();

}
