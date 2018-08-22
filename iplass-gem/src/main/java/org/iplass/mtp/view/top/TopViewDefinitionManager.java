/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.top;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.iplass.mtp.definition.TypedDefinitionManager;

/**
 * TOP画面定義を管理するクラスのインターフェース
 * @author lis3wg
 */
public interface TopViewDefinitionManager extends TypedDefinitionManager<TopViewDefinition> {

	/**
	 * TOP画面定義を削除します。
	 * @param name TOP画面定義名
	 * @return 更新結果
	 * @deprecated {@link #remove(String)} を使用してください。
	 */
	@Deprecated
	public TopViewDefinitionModifyResult delete(String name);

	/**
	 * パーツを読み込みます。
	 * @param 定義名
	 * req リクエスト
	 * res レスポンス
	 * application サーブレットコンテキスト
	 * page ページコンテキスト
	 */
	public void loadParts(HttpServletRequest req, HttpServletResponse res,
			ServletContext application, PageContext page);

	/**
	 * ウィジェットを読み込みます。
	 * @param 定義名
	 * req リクエスト
	 * res レスポンス
	 * application サーブレットコンテキスト
	 * page ページコンテキスト
	 */
	public void loadWidgets(HttpServletRequest req, HttpServletResponse res,
			ServletContext application, PageContext page);
}
