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

package org.iplass.gem.command.generic.search.handler;

import org.iplass.gem.command.common.SearchResultData;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.view.generic.AbstractFormViewEvent;

/**
 * 汎用検索画面の検索結果生成イベント
 *
 */
public class CreateSearchResultEvent extends AbstractFormViewEvent {

	/** 検索結果 */
	private SearchResultData resultData;

	public CreateSearchResultEvent(RequestContext request, String entityName, String viewName,
			SearchResultData resultData) {
		super(request, entityName, viewName);

		this.resultData = resultData;
	}

	/**
	 * 検索結果を返します。
	 *
	 * @return 検索結果
	 */
	public SearchResultData getResultData() {
		return resultData;
	}

}
