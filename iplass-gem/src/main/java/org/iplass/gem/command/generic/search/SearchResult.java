/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.search;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.Entity;

/**
 * 検索結果
 * @author lis3wg
 */
public class SearchResult {

	/** 全件数 */
	private int count;

	/** 検索データ */
	private List<Entity> result;

	/**
	 * 全件数を取得します。
	 * @return 全件数
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 全件数を設定します。
	 * @param count 全件数
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 検索データを取得します。
	 * @return 検索データ
	 */
	public List<Entity> getResult() {
		if (this.result == null) this.result = new ArrayList<Entity>();
		return result;
	}

	/**
	 * 検索データを設定します。
	 * @param result 検索データ
	 */
	public void setResult(List<Entity> result) {
		this.result = result;
	}
}
