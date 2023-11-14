/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.gem.command.generic.detail;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;

/**
 * DetailCommandContextのサブクラス
 * ネストテーブル表示時にパラメータを書き換える
 * @author EDS Y.Yasuda
 *
 */
public class NestTableCommandContext extends DetailCommandContext {

	/**
	 * コンストラクタ
	 * @param request
	 * @param entityLoader
	 * @param definitionLoader
	 */
	public NestTableCommandContext(RequestContext request, EntityManager entityLoader,
			EntityDefinitionManager definitionLoader) {
		super(request, entityLoader, definitionLoader);

		init();
	}

	/**
	 * コンストラクタ
	 * @param request
	 * @param defName
	 * @param viewName
	 * @param entityLoader
	 * @param definitionLoader
	 */
	public NestTableCommandContext(RequestContext request, String defName, String viewName, EntityManager entityLoader,
			EntityDefinitionManager definitionLoader) {
		super(request, defName, viewName, entityLoader, definitionLoader);

		init();
	}

	/**
	 * 初期化処理
	 */
	private void init() {
		// Command側でパラメータを指定された場合は置き換え
		String defName = (String) request.getAttribute(Constants.MAINTENANCE_DEF_NAME);
		if (defName != null) {
			setDefName(defName);
		}

		String viewName = (String) request.getAttribute(Constants.MAINTENANCE_VIEW_NAME);
		if (viewName != null) {
			setViewName(viewName);
		}
	}

	/**
	 * エンティティ定義名をセット
	 * @param defName
	 */
	private void setDefName(String defName) {
		this.defName = defName;
	}

	/**
	 * ビュー名をセット
	 * @param viewName
	 */
	private void setViewName(String viewName) {
		this.viewName = viewName;
	}
}
