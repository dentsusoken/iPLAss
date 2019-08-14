/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.Query;

/**
 * 汎用詳細画面、大規模参照プロパティ検索時にカスタムで処理を行わせるインターフェース
 */
public interface MassReferenceSearchQueryInterrupter {

	/**
	 * 検索前処理を行います。
	 *
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param query 検索用クエリ
	 * @param outputType 出力タイプ(VIEWまたはEDIT)
	 * @return 実行結果
	 */
	default public SearchQueryContext beforeSearch(RequestContext request, DetailFormView view, Query query, OutputType outputType) {
		return new SearchQueryContext(query);
	}

	/**
	 * 検索後処理を行います。
	 *
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param query 検索用クエリ
	 * @param entity 検索結果
	 * @param outputType 出力タイプ(VIEWまたはEDIT)
	 */
	default public void afterSearch(RequestContext request, DetailFormView view, Query query, Entity entity, OutputType outputType) {
	}

}
