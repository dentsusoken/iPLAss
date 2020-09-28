/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 検索結果データ。
 * 検索結果行データを保持し、検索結果のレスポンスを返します。
 */
public class SearchResultData {

	/** 行データ */
	private List<SearchResultRow> rows;

	public SearchResultData() {
		rows = new ArrayList<>();
	}

	/**
	 * 行データを返します。
	 *
	 * @return 行データ
	 */
	public List<SearchResultRow> getRows() {
		return rows;
	}

	/**
	 * 行データを設定します。
	 *
	 * @param rows 行データ
	 */
	public void setRows(List<SearchResultRow> rows) {
		this.rows = rows;
	}

	/**
	 * 行データを追加します。
	 *
	 * @param row
	 */
	public void addRow(SearchResultRow row) {
		rows.add(row);
	}

	/**
	 * レスポンスを返します。
	 *
	 * @return レスポンス
	 */
	public List<Map<String, String>> toResponse() {

		if (rows == null || rows.isEmpty()) {
			return Collections.emptyList();
		}

		return rows.stream().map(row -> row.getResponse()).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		List<Map<String, String>> response = toResponse();
		return response != null? response.toString() : null;
	}
}
