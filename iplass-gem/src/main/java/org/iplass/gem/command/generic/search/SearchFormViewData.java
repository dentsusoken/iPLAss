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

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.filter.EntityFilterItem;
import org.iplass.mtp.view.generic.SearchFormView;

/**
 * 検索画面表示用データ
 * @author lis3wg
 */
public class SearchFormViewData {

	/** Entity定義 */
	private EntityDefinition entityDefinition;

	/** 検索条件 */
	private SearchCondition condition;

	/** 検索結果 */
	private SearchResult result;

	/** 検索画面用のFormレイアウト情報 */
	private SearchFormView view;

	/** 検索タイプ */
	private String searchType;

	/** フィルタ設定 */
	private List<EntityFilterItem> filters;

	/**
	 * コンストラクタ
	 */
	public SearchFormViewData() {
	}

	/**
	 * Entity定義を取得します。
	 * @return Entity定義
	 */
	public EntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

	/**
	 * Entity定義を設定します。
	 * @param entityDefinition Entity定義
	 */
	public void setEntityDefinition(EntityDefinition entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	/**
	 * 検索条件を取得します。
	 * @return 検索条件
	 */
	public SearchCondition getCondition() {
		return condition;
	}

	/**
	 * 検索条件を設定します。
	 * @param condition 検索条件
	 */
	public void setCondition(SearchCondition condition) {
		this.condition = condition;
	}

	/**
	 * 検索結果を取得します。
	 * @return 検索結果
	 */
	public SearchResult getResult() {
		return result;
	}

	/**
	 * 検索結果を設定します。
	 * @param result 検索結果
	 */
	public void setResult(SearchResult result) {
		this.result = result;
	}

	/**
	 * 検索画面用のFormレイアウト情報を取得します。
	 * @return 検索画面用のFormレイアウト情報
	 */
	public SearchFormView getView() {
		return view;
	}

	/**
	 * 検索画面用のFormレイアウト情報を設定します。
	 * @param view 検索画面用のFormレイアウト情報
	 */
	public void setView(SearchFormView view) {
		this.view = view;
	}

	/**
	 * 検索タイプを取得します。
	 * @return 検索タイプ
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * 検索タイプを設定します。
	 * @param searchType 検索タイプ
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * フィルタ設定を取得します。
	 * @return フィルタ設定
	 */
	public List<EntityFilterItem> getFilters() {
		if (filters == null) filters = new ArrayList<EntityFilterItem>();
		return filters;
	}

	/**
	 * フィルタ設定を設定します。
	 * @param filters フィルタ設定
	 */
	public void setFilters(List<EntityFilterItem> filters) {
		this.filters = filters;
	}

}
