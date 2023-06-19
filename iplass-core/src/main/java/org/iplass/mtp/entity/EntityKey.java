/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.entity;

/**
 * EntityのKEY情報
 */
public class EntityKey {

	/** Entityを一意に識別するID */
	private String oid;

	/** EntityのバージョンNo */
	private Long version;

	public EntityKey() {
	}

	/**
	 * 指定したIDのKEY情報を生成します。
	 *
	 * @param oid ID
	 */
	public EntityKey(String oid) {
		this(oid, null);
	}

	/**
	 * 指定したID、バージョンのKEY情報を生成します。
	 *
	 * @param oid ID
	 * @param version バージョン
	 */
	public EntityKey(String oid, Long version) {
		this.oid = oid;
		this.version = version;
	}

	/**
	 * IDを返します。
	 *
	 * @return ID
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * IDを設定します。
	 *
	 * @param oid ID
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * バージョンを返します。
	 *
	 * @return バージョン
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * バージョンを設定します。
	 *
	 * @param version バージョン
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "{\"oid\":\"" + oid + "\"" + ",\"version\":\"" + version + "\"}";
	}

}