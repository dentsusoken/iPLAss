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
package org.iplass.mtp.impl.pushnotification.fcmv1;

import org.iplass.mtp.pushnotification.fcmv1.PushNotificationResponseDetail;
import org.iplass.mtp.pushnotification.fcmv1.PushNotificationStatus;
import org.iplass.mtp.pushnotification.fcmv1.PushNotificationTarget;

/**
 * FCM V1 API 用 Push 通知レスポンス詳細
 *
 * @author SEKIGUCHI Naoya
 */
public class PushNotificationResponseDetailImpl implements PushNotificationResponseDetail {
	/** 実行状態 */
	private PushNotificationStatus status;
	/** 通知対象 */
	private PushNotificationTarget target;
	/** レスポンス本体 */
	private String response;
	/**
	 * 送信されたメッセージの識別子。正常時のみ設定される。
	 * <p>
	 * projects/&ast;/messages/&#x7b;message_id&#x7d; 形式
	 * </p>
	 */
	private String messageId;
	/** リトライ間隔（秒） */
	private long retryAfterSeconds;
	/** リトライ回数 */
	private int retryCount;
	/** エラーメッセージ */
	private String errorMessage;
	/** 原因例外 */
	private Throwable cause;

	/**
	 * コンストラクタ
	 * @param status 実行状態
	 */
	PushNotificationResponseDetailImpl(PushNotificationStatus status) {
		this.status = status;
	}

	/**
	 * コピーコンストラクタ
	 * @param src コピー元
	 */
	public PushNotificationResponseDetailImpl(PushNotificationResponseDetail src) {
		status = src.getStatus();
		target = src.getTarget();
		response = src.getResponse();
		messageId = src.getMessageId();
		retryAfterSeconds = src.getRetryAfterSeconds();
		retryCount = src.getRetryCount();
		errorMessage = src.getErrorMessage();
		cause = src.getCause();
	}

	/**
	 * ステータスを設定する
	 * @param status ステータス
	 */
	public void setStatus(PushNotificationStatus status) {
		this.status = status;
	}

	/**
	 * ステータスを取得する
	 * @return ステータス
	 */
	@Override
	public PushNotificationStatus getStatus() {
		return status;
	}

	/**
	 * 通知対象を設定する
	 * @param target 通知対象
	 */
	public void setTarget(PushNotificationTarget target) {
		this.target = target;
	}

	/**
	 * 通知対象を取得する
	 * @return target 通知対象
	 */
	@Override
	public PushNotificationTarget getTarget() {
		return target;
	}

	/**
	 * レスポンス本体を設定する
	 * @param response レスポンス本体
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * レスポンス本体を取得する
	 * @return レスポンス本体
	 */
	@Override
	public String getResponse() {
		return response;
	}

	/**
	 * メッセージの識別子を設定する
	 * @param messageId メッセージの識別子
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * 送信されたメッセージの識別子を取得する。正常時のみ設定される。
	 * <p>
	 * projects/&ast;/messages/&#x7b;message_id&#x7d; 形式
	 * </p>
	 * @return メッセージの識別子
	 */
	@Override
	public String getMessageId() {
		return messageId;
	}

	/**
	 * 再実行間隔（秒）を設定する
	 * @param retryAfterSeconds 再実行間隔（秒）
	 */
	public void setRetryAfterSeconds(long retryAfterSeconds) {
		this.retryAfterSeconds = retryAfterSeconds;
	}

	/**
	 * 再実行間隔（秒）を取得する。リトライ可能な場合のみ設定される。
	 * @return 再実行間隔
	 */
	@Override
	public long getRetryAfterSeconds() {
		return retryAfterSeconds;
	}

	/**
	 * リトライ回数を設定する
	 * @param retryCount リトライ回数
	 */
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	/**
	 * リトライ回数を取得する。
	 * @return リトライ回数
	 */
	@Override
	public int getRetryCount() {
		return retryCount;
	}

	/**
	 * エラーメッセージを設定する
	 * @param errorMessage エラーメッセージ
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * エラーメッセージを取得する。エラー終了時のみ設定される。
	 * @return エラーメッセージ
	 */
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * 例外を設定する
	 * @param cause 例外
	 */
	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	/**
	 * 例外を取得します。通信実行に際して例外が発生した場合に設定される。
	 * @return 例外
	 */
	@Override
	public Throwable getCause() {
		return cause;
	}

	@Override
	public String toString() {
		return new StringBuilder(this.getClass().getSimpleName())
				.append("{")
				.append("status=").append(status)
				.append(toStringValue("target", target))
				.append(toStringValue("response", response))
				.append(toStringValue("messageId", messageId))
				.append(toStringValue("retryAfterSeconds", retryAfterSeconds))
				.append(toStringValue("retryCount", retryCount))
				.append(toStringValue("errorMessage", errorMessage))
				.append(toStringValue("cause", cause))
				.append("}")
				.toString();
	}

	private String toStringValue(String key, Object value) {
		return null == value ? "" : ", " + key + "=" + value;
	}
}
