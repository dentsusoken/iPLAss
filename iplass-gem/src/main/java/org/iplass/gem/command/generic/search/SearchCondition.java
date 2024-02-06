/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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
 * 検索条件
 * @author lis3wg
 */
public class SearchCondition {

	/** 通常検索用の値を保持するEntity */
	private Entity entity;

	/** 条件式 */
	private String expr;

	/** 詳細検索条件 */
	private List<SearchConditionDetail> detailConditonList;

	/** 取得件数 */
	private Integer limit;

	/** 取得位置 */
	private Integer offset;

	/** 全てのバージョンを検索するか */
	private boolean versioned;

	/**
	 * 通常検索用の値を保持するEntityを取得します。
	 * @return 通常検索用の値を保持するEntity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * 通常検索用の値を保持するEntityを設定します。
	 * @param entity 通常検索用の値を保持するEntity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * 詳細検索条件を取得します。
	 * @return 詳細検索条件
	 */
	public List<SearchConditionDetail> getDetailConditonList() {
		if (this.detailConditonList == null) this.detailConditonList = new ArrayList<SearchConditionDetail>();
		return detailConditonList;
	}

	/**
	 * 詳細検索条件を設定します。
	 * @param detailConditonList 詳細検索条件
	 */
	public void setDetailConditonList(List<SearchConditionDetail> detailConditonList) {
		this.detailConditonList = detailConditonList;
	}

	/**
	 * 詳細検索条件を追加します。
	 * @param detail 詳細検索条件
	 */
	public void addDetailCondition(SearchConditionDetail detail) {
		getDetailConditonList().add(detail);
	}

	/**
	 * 条件式を取得します。
	 * @return 条件式
	 */
	public String getExpr() {
		return expr;
	}

	/**
	 * 条件式を設定します。
	 * @param expr 条件式
	 */
	public void setExpr(String expr) {
		this.expr = expr;
	}

	/**
	 * 取得件数を取得します。
	 * @return 取得件数
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * 取得件数を設定します。
	 * @param limit 取得件数
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * 取得位置を取得します。
	 * @return 取得位置
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * 取得位置を設定します。
	 * @param offset 取得位置
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * 全てのバージョンを検索するかを取得します。
	 * @return 全てのバージョンを検索するか
	 */
	public boolean isVersioned() {
		return versioned;
	}

	/**
	 * 全てのバージョンを検索するかを設定します。
	 * @param versioned 全てのバージョンを検索するか
	 */
	public void setVersioned(boolean versioned) {
		this.versioned = versioned;
	}

}
