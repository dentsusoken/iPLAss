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
 * FCM V1 API 用 Push 通知レスポンス詳細
 *
 * <p>
 * FCM へ Push 通知の要求した結果の詳細情報です。
 * 実行結果のステータスや、レスポンス詳細等を確認することが可能です。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class PushNotificationResponseDetail {
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
	 * デフォルトコンストラクタ
	 */
	PushNotificationResponseDetail(PushNotificationStatus status) {
		this.status = status;
	}

	/**
	 * FCM V1 API 用 Push 通知レスポンス詳細ビルダー
	 */
	public static class Builder {
		private PushNotificationResponseDetail instance;

		Builder(PushNotificationResponseDetail instance) {
			this.instance = instance;
		}

		/**
		 * 成功インスタンスを生成する
		 * @return ビルダーインスタンス
		 */
		public static Builder success() {
			return new Builder(new PushNotificationResponseDetail(PushNotificationStatus.SUCCESS));
		}

		/**
		 * 失敗インスタンスを生成する
		 * @param status 失敗ステータス
		 * @return ビルダーインスタンス
		 */
		public static Builder fail(PushNotificationStatus status) {
			return new Builder(new PushNotificationResponseDetail(status));
		}

		/**
		 * インスタンスの再設定
		 * @param detail 詳細
		 * @return ビルダーインスタンス
		 */
		public static Builder of(PushNotificationResponseDetail detail) {
			return new Builder(detail);
		}

		/**
		 * 通知対象を設定する
		 * @param target 通知対象
		 * @return ビルダーインスタンス
		 */
		public Builder setTarget(PushNotificationTarget target) {
			instance.target = target;
			return this;
		}

		/**
		 * レスポンス本体を設定する
		 * @param response レスポンス本体
		 * @return ビルダーインスタンス
		 */
		public Builder setResponse(String response) {
			instance.response = response;
			return this;
		}

		/**
		 * メッセージの識別子を設定する
		 * @param messageId メッセージの識別子
		 * @return ビルダーインスタンス
		 */
		public Builder setMessageId(String messageId) {
			instance.messageId = messageId;
			return this;
		}

		/**
		 * 再実行間隔（秒）を設定する
		 * @param retryAfterSeconds 再実行間隔（秒）
		 * @return ビルダーインスタンス
		 */
		public Builder setRetryAfterSeconds(long retryAfterSeconds) {
			instance.retryAfterSeconds = retryAfterSeconds;
			return this;
		}

		/**
		 * リトライ回数を設定する
		 * @param retryCount リトライ回数
		 * @return ビルダーインスタンス
		 */
		public Builder setRetryCount(int retryCount) {
			instance.retryCount = retryCount;
			return this;
		}

		/**
		 * エラーメッセージを設定する
		 * @param errorMessage エラーメッセージ
		 * @return ビルダーインスタンス
		 */
		public Builder setErrorMessage(String errorMessage) {
			instance.errorMessage = errorMessage;
			return this;
		}

		/**
		 * 例外を設定する
		 * @param cause 例外
		 * @return ビルダーインスタンス
		 */
		public Builder setCause(Throwable cause) {
			instance.cause = cause;
			return this;
		}

		/**
		 * インスタンスを返却する
		 * @return インスタンス
		 */
		public PushNotificationResponseDetail build() {
			return instance;
		}
	}

	/**
	 * ステータスを取得する
	 * @return ステータス
	 */
	public PushNotificationStatus getStatus() {
		return status;
	}

	/**
	 * 通知対象を取得する
	 * @return target 通知対象
	 */
	public PushNotificationTarget getTarget() {
		return target;
	}

	/**
	 * レスポンス本体を取得する
	 * @return レスポンス本体
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * 送信されたメッセージの識別子を取得する。正常時のみ設定される。
	 * <p>
	 * projects/&ast;/messages/&#x7b;message_id&#x7d; 形式
	 * </p>
	 * @return メッセージの識別子
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * 再実行間隔（秒）を取得する。リトライ可能な場合のみ設定される。
	 * @return 再実行間隔
	 */
	public long getRetryAfterSeconds() {
		return retryAfterSeconds;
	}

	/**
	 * リトライ回数を取得する。
	 * @return リトライ回数
	 */
	public int getRetryCount() {
		return retryCount;
	}

	/**
	 * エラーメッセージを取得する。エラー終了時のみ設定される。
	 * @return エラーメッセージ
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * 例外を取得します。通信実行に際して例外が発生した場合に設定される。
	 * @return 例外
	 */
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
