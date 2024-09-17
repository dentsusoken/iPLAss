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
 * PushNotificationResult の詳細キーを定義します
 *
 * <p>
 * FCM HTTP v1 API のプッシュ通知実行結果における、詳細情報を取得するためのキーです。
 * 本クラスで定義されているキーは、 {@link org.iplass.mtp.pushnotification.PushNotificationResult#getDetail(String)} のパラメータとして利用します。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class PushNotificationResultDetailKey {
	/**
	 * プライベートコンストラクタ
	 */
	private PushNotificationResultDetailKey() {
	}

	/**
	 * レスポンス詳細キー：詳細リスト
	 *
	 * <p>
	 * 本キーで取得した結果は <code>List&lt;PushNotificationResponseDetail&gt;</code>となります。
	 * </p>
	 */
	public static final String DETAIL_LIST = "detailList";
}
