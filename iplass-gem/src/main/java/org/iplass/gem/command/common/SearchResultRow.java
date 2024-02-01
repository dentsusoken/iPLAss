/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.common;

import java.util.Map;

import org.iplass.mtp.entity.Entity;

/**
 * 検索結果の行データ。
 * 対象のEntityと、それに対するレスポンス値を保持します。
 */
public class SearchResultRow {

	/** OID */
	public static final String OID = "orgOid";
	/** VERSION */
	public static final String VERSION = "orgVersion";
	/** TIMESTAMP */
	public static final String TIMESTAMP = "orgTimestamp";
	/** SCORE(for FullTextSearch) */
	public static final String SCORE = "score";
	/** 編集可 */
	public static final String CAN_EDIT = "@canEdit";
	/** 削除可 */
	public static final String CAN_DELETE = "@canDelete";

	/** 対象Entity */
	private Entity entity;

	/** レスポンスとして返す値 */
	private Map<String, String> response;

	public SearchResultRow() {
	}

	/**
	 *
	 * @param entity 対象Entity
	 * @param response レスポンスとして返す値
	 */
	public SearchResultRow(Entity entity, Map<String, String> response) {
		this.entity = entity;
		this.response = response;
	}

	/**
	 * 対象Entityを返します。
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * 対象Entityを設定します。
	 *
	 * @param entity 対象Entity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * レスポンスを返します。
	 * 列名と値のMAP形式で返します。
	 */
	public Map<String, String> getResponse() {
		return response;
	}

	/**
	 * レスポンスを設定します。
	 *
	 * @param response レスポンス
	 */
	public void setResponse(Map<String, String> response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return response != null? response.toString() : null;
	}

}
