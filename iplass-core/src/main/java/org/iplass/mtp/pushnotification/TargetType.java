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
package org.iplass.mtp.pushnotification;

/**
 * Push 通知先タイプ
 *
 * @author SEKIGUCHI Naoya
 */
public enum TargetType {
	/**
	 * デバイス登録トークン
	 * <p>
	 * regacy API： registration_ids (単一であれば to も利用可能）<br>
	 * HTTP v1 API: token
	 * </p>
	 */
	TOKEN("registration_ids", "token"),
	/**
	 * トピック名
	 * <p>
	 * regacy API： to<br>
	 * HTTP v1 API: topic
	 * </p>
	 */
	TOPIC("to", "topic"),
	/**
	 * 送信先の条件
	 * <p>
	 * regacy API： condition<br>
	 * HTTP v1 API: condition
	 * </p>
	 */
	CONDITION("condition", "condition");

	private String legacyFieldName;
	private String v1FieldName;

	private TargetType(String legacyFieldName, String v1FieldName) {
		this.legacyFieldName = legacyFieldName;
		this.v1FieldName = v1FieldName;
	}

	/**
	 * Legacy API のフィールド名
	 * @return フィールド名
	 */
	public String getLegacyApiFieldName() {
		return legacyFieldName;
	}

	/**
	 * Http V1 API のフィールド名
	 * @return フィールド名
	 */
	public String getV1ApiFieldName() {
		return v1FieldName;
	}
}
