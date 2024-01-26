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

package org.iplass.gem.command.generic.search;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;

/**
 * カスタム検索処理ハンドラ
 * @author lis3wg
 */
public class SearchQueryInterrupterHandler {
	/** リクエスト */
	private RequestContext request;
	/** 検索処理クラス */
	private SearchQueryInterrupter interrupter;
	/** 検索画面Context */
	private SearchContextBase context;

	/**
	 * コンストラクタ
	 * @param request リクエスト
	 * @param context 詳細画面Context
	 * @param interrupter 登録処理クラス
	 */
	public SearchQueryInterrupterHandler(RequestContext request, SearchContextBase context, SearchQueryInterrupter interrupter) {
		this.request = request;
		this.interrupter = interrupter;
		this.context = context;
	}

	/**
	 * 検索前処理を行います。
	 * @param query Query
	 * @return 処理結果Context
	 * @param type 検索処理の種類
	 */
	public SearchQueryContext beforeSearch(Query query, SearchQueryType type) {
		SearchFormView formView = context.getForm();
		query.setLocalized(formView.isLocalizationData());
		SearchQueryContext ret = interrupter.beforeSearch(request, formView, query, type);
		return ret;
	}

	/**
	 * 検索後処理を行います。
	 * @param query Query
	 * @param resultList 検索結果
	 * @param type 検索処理の種類
	 */
	public void afterSearch(Query query, Entity entity, SearchQueryType type) {
		SearchFormView formView = context.getForm();
		interrupter.afterSearch(request, formView, query, entity, type);
	}

	public RequestContext getRequest() {
		return request;
	}

	public SearchQueryInterrupter getInterrupter() {
		return interrupter;
	}

	public SearchContextBase getContext() {
		return context;
	}
}
