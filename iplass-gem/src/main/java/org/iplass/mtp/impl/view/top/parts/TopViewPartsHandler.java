/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.view.top.parts;

import java.io.IOException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.jsp.PageContext;

/**
 * TOP画面パーツランタイム
 * @author lis3wg
 */
public abstract class TopViewPartsHandler {

	private MetaTopViewParts metadata;

	/**
	 * コンストラクタ
	 */
	public TopViewPartsHandler(MetaTopViewParts metadata) {
		this.metadata = metadata;
	}

	/**
	 * メタデータを取得します。
	 * @return
	 */
	public MetaTopViewParts getMetaData() {
		return metadata;
	}

	/**
	 * リクエストに属性をセットします。
	 * @param req
	 */
	public abstract void setAttribute(HttpServletRequest req);

	/**
	 * リクエストの属性をクリアします。
	 * @param req
	 */
	public abstract void clearAttribute(HttpServletRequest req);

	/**
	 * パーツを読み込みます。
	 * @param req HTTPリクエスト
	 * @param res HTTPレスポンス
	 * @param application サーバコンテキスト
	 * @param page ページコンテキスト
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract void loadParts(HttpServletRequest req, HttpServletResponse res,
			ServletContext application, PageContext page) throws IOException, ServletException;

	/**
	 * ウィジェットを読み込みます。
	 * @param req HTTPリクエスト
	 * @param res HTTPレスポンス
	 * @param application サーバコンテキスト
	 * @param page ページコンテキスト
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract void loadWidgets(HttpServletRequest req, HttpServletResponse res,
			ServletContext application, PageContext page) throws IOException, ServletException;

	/**
	 * 特定のパーツを取得するためのキー
	 * @return
	 */
	public String getHandlerKey() {
		return null;
	}
}
