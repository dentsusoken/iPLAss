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

import org.iplass.mtp.pushnotification.TargetType;

/**
 * FCM v1 API 用 Push 通知対象
 *
 * @author SEKIGUCHI Naoya
 */
public class PushNotificationTarget {
	/** 対象タイプ */
	private TargetType type;
	/** 対象識別子 */
	private String id;

	/**
	 * コンストラクタ
	 * @param type 対象タイプ
	 * @param id 対象識別子
	 */
	public PushNotificationTarget(TargetType type, String id) {
		this.type = type;
		this.id = id;
	}

	/**
	 * 対象タイプを取得します
	 * @return type 対象タイプ
	 */
	public TargetType getType() {
		return type;
	}

	/**
	 * 対象識別子を取得します
	 * @return id 対象識別子
	 */
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return PushNotificationTarget.class.getSimpleName() + "{type=" + type + ", id=" + id + "}";
	}
}
