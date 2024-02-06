/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.Query;

/**
 * 汎用検索時にカスタムで処理を行わせるインターフェース
 * @author lis3wg
 *
 */
public interface SearchQueryInterrupter {

	/**
	 * 検索処理の種類
	 */
	public enum SearchQueryType {
		/** 検索処理 */
		SEACH,
		/** CSVダウンロード */
		CSV
	}

	/**
	 * 検索前処理を行います。
	 * @param request リクエスト
	 * @param view 検索画面定義
	 * @param query 検索用クエリ
	 * @return 実行結果
	 *
	 * @deprecated use beforeSearch(RequestContext, SearchFormView, Query, SearchQueryType)
	 */
	@Deprecated
	default public SearchQueryContext beforeSearch(RequestContext request, SearchFormView view, Query query) {
		return new SearchQueryContext(query);
	}

	/**
	 * 検索前処理を行います。
	 * @param request リクエスト
	 * @param view 検索画面定義
	 * @param query 検索用クエリ
	 * @param type 検索処理の種類
	 * @return 実行結果
	 */
	default public SearchQueryContext beforeSearch(RequestContext request, SearchFormView view, Query query, SearchQueryType type) {
		SearchQueryContext searchQueryContext = beforeSearch(request, view, query); // 既存向けに旧メソッドを呼び出す
		List<String> withoutConditionReferenceName = view.getWithoutConditionReferenceName();
		if (searchQueryContext.getWithoutConditionReferenceName() == null && withoutConditionReferenceName != null) {
			searchQueryContext.setWithoutConditionReferenceName(
					withoutConditionReferenceName.toArray(new String[withoutConditionReferenceName.size()]));
		}
		return searchQueryContext;
	}

	/**
	 * 検索後処理を行います。
	 * @param request リクエスト
	 * @param view 画面定義
	 * @param query 検索用クエリ
	 * @param entity 検索結果
	 * @param type 検索処理の種類
	 */
	default public void afterSearch(RequestContext request, SearchFormView view, Query query, Entity entity, SearchQueryType type) {
	}
}
