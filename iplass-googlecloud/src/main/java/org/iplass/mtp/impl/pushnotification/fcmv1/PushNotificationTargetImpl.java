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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.iplass.mtp.pushnotification.fcmv1.PushNotificationTarget;
import org.iplass.mtp.pushnotification.fcmv1.PushNotificationTargetType;

/**
 * FCM v1 API 用 Push 通知対象
 *
 * @author SEKIGUCHI Naoya
 */
public class PushNotificationTargetImpl implements PushNotificationTarget {
	/** 対象タイプ */
	private PushNotificationTargetType type;
	/** 対象識別子 */
	private String target;

	/**
	 * コンストラクタ
	 * @param type 対象タイプ
	 * @param target 通知対象
	 */
	public PushNotificationTargetImpl(PushNotificationTargetType type, String target) {
		this.type = type;
		this.target = target;
	}

	/**
	 * 対象タイプを取得します
	 * @return 対象タイプ
	 */
	@Override
	public PushNotificationTargetType getType() {
		return type;
	}

	/**
	 * 通知対象を取得します
	 * @return 通知対象
	 */
	@Override
	public String getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return PushNotificationTargetImpl.class.getSimpleName() + "{type=" + type + ", id=" + target + "}";
	}

	/** トークン、トピック、条件のいずれかのプレフィックス付き宛先のパターン */
	private static final Pattern ANY_PREFIXED_VALUE_PATTERN = Pattern
			.compile("^("
					+ PushNotificationTargetType.TOKEN.getFieldName() + "|"
					+ PushNotificationTargetType.TOPIC.getFieldName() + "|"
					+ PushNotificationTargetType.CONDITION.getFieldName() + "):(.*)");

	/**
	 * 通知対象インスタンスを作成する
	 * @param anyPrefixedValue トークン、トピック、条件のいずれかのプレフィックス付き宛先
	 * @return 通知対象
	 */
	public static PushNotificationTarget create(String anyPrefixedValue) {
		Matcher matcher = ANY_PREFIXED_VALUE_PATTERN.matcher(anyPrefixedValue);
		if (!matcher.find()) {
			throw new IllegalArgumentException("The target specified does not match the pattern. (target = " + anyPrefixedValue + ")");
		}

		String prefixIsFieldName = matcher.group(1);
		String target = matcher.group(2);
		return new PushNotificationTargetImpl(PushNotificationTargetType.getTargetType(prefixIsFieldName), target);
	}
}
