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
 * Push 宛先タイプ
 *
 * @author SEKIGUCHI Naoya
 */
public enum PushNotificationTargetType {
	/**
	 * デバイス登録トークン
	 * <p>
	 * HTTP v1 API: token
	 * </p>
	 */
	TOKEN("token"),
	/**
	 * トピック名
	 * <p>
	 * HTTP v1 API: topic
	 * </p>
	 */
	TOPIC("topic"),
	/**
	 * 送信先の条件
	 * <p>
	 * HTTP v1 API: condition
	 * </p>
	 */
	CONDITION("condition");

	/** フィールド名 */
	private String fieldName;
	/** 宛先プレフィックス */
	private String prefix;

	private PushNotificationTargetType(String fieldName) {
		this.fieldName = fieldName;
		this.prefix = fieldName + ":";
	}

	/**
	 * Http V1 API のフィールド名
	 * @return フィールド名
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * プレフィックス付き宛先を取得する
	 * <p>
	 * {@link org.iplass.mtp.impl.pushnotification.fcmv1.PushNotificationService} を利用する場合、
	 * 実行時の引数 {@link org.iplass.mtp.pushnotification.PushNotification} の to に設定する値には、宛先を現す接頭辞が必要となる。<br>
	 * 本メソッドでは、接頭辞付きの宛先情報を取得する。
	 * </p>
	 *
	 * @param target 宛先（FCM HTP v1 API の token, topic, condition として設定する値）
	 * @return プレフィックス付きの宛先情報
	 */
	public String getPrefixedValue(String target) {
		return prefix + target;
	}

	/**
	 * 通知先タイプのフィールド名に一致する通知先タイプを取得する
	 * @param fieldName 通知先タイプのフィールド名
	 * @return 宛先タイプ
	 */
	public static PushNotificationTargetType getTargetType(String fieldName) {
		for (PushNotificationTargetType targetType : PushNotificationTargetType.values()) {
			if (targetType.fieldName.equals(fieldName)) {
				return targetType;
			}
		}
		// 想定外のフィールド名
		throw new IllegalArgumentException(
				"The argument fieldName must be " + TOKEN.fieldName + " or " + TOPIC.fieldName + " or " + CONDITION.fieldName + ".");
	}
}

