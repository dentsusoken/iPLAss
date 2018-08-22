/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic;

import org.iplass.mtp.entity.query.Query;

/**
 * SearchQueryInterrupterの実行結果を保持するContextです。
 * @author lis3wg
 *
 */
public class SearchQueryContext {

	/**
	 * クエリ
	 */
	private Query query;

	/**
	 * 特権実行を行うか
	 */
	private boolean doPrivileged;

	/**
	 * コンストラクタ
	 * @param query クエリ
	 */
	public SearchQueryContext(Query query) {
		this.query = query;
		this.doPrivileged = false;
	}

	/**
	 * コンストラクタ
	 * @param query クエリ
	 * @param doPrivileged 特権実行を行うか
	 */
	public SearchQueryContext(Query query, boolean doPrivileged) {
		this.query = query;
		this.doPrivileged = doPrivileged;
	}

	/**
	 * クエリを取得します。
	 * @return クエリ
	 */
	public Query getQuery() {
	    return query;
	}

	/**
	 * クエリを設定します。
	 * @param query クエリ
	 */
	public void setQuery(Query query) {
	    this.query = query;
	}

	/**
	 * 特権実行を行うかを取得します。
	 * @return 特権実行を行うか
	 */
	public boolean isDoPrivileged() {
	    return doPrivileged;
	}

	/**
	 * 特権実行を行うかを設定します。
	 * @param doPrivileged 特権実行を行うか
	 */
	public void setDoPrivileged(boolean doPrivileged) {
	    this.doPrivileged = doPrivileged;
	}

}
