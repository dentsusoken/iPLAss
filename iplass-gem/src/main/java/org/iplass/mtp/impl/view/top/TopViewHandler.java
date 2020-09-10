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

package org.iplass.mtp.impl.view.top;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.view.top.parts.MetaTopViewParts;
import org.iplass.mtp.impl.view.top.parts.TopViewPartsHandler;

/**
 * TOP画面ランタイム
 * @author lis3wg
 */
public class TopViewHandler extends BaseMetaDataRuntime {

	private MetaTopView metadata;
	private List<TopViewPartsHandler> parts;
	private List<TopViewPartsHandler> widgets;
	private Map<String, TopViewPartsHandler> handlerMap;

	/**
	 * コンストラクタ
	 */
	public TopViewHandler(MetaTopView metadata) {
		try {
			this.metadata = metadata;
			handlerMap = new HashMap<>();
			parts = new ArrayList<>();
			for (MetaTopViewParts meta : metadata.getParts()) {
				TopViewPartsHandler handler = meta.createRuntime(this);
				parts.add(handler);
				if (handler.getHandlerKey() != null) {
					handlerMap.put(handler.getHandlerKey(), handler);
				}
			}
			widgets = new ArrayList<>();
			for (MetaTopViewParts meta : metadata.getWidgets()) {
				TopViewPartsHandler handler = meta.createRuntime(this);
				widgets.add(handler);
				if (handler.getHandlerKey() != null) {
					handlerMap.put(handler.getHandlerKey(), handler);
				}
			}

		} catch (RuntimeException e) {
			setIllegalStateException(e);
		}
	}

	@Override
	public MetaTopView getMetaData() {
		return metadata;
	}

	/**
	 * パーツを読み込みます。
	 * @param req HTTPリクエスト
	 * @param res HTTPレスポンス
	 * @param application サーバコンテキスト
	 * @param page ページコンテキスト
	 */
	public void loadParts(HttpServletRequest req, HttpServletResponse res,
			ServletContext application, PageContext page) {
		checkState();

		int cnt = 0;
		for (TopViewPartsHandler handler : parts) {
			try {
				req.setAttribute("partsType", "Parts");
				req.setAttribute("partsCnt", cnt);
				handler.setAttribute(req);
				handler.loadParts(req, res, application, page);
				handler.clearAttribute(req);
				req.removeAttribute("partsType");
				cnt = (Integer) req.getAttribute("partsCnt") + 1;
				req.removeAttribute("partsCnt");
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (ServletException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * ウィジェットを読み込みます。
	 * @param req HTTPリクエスト
	 * @param res HTTPレスポンス
	 * @param application サーバコンテキスト
	 * @param page ページコンテキスト
	 */
	public void loadWidgets(HttpServletRequest req, HttpServletResponse res,
			ServletContext application, PageContext page) {
		checkState();

		for (TopViewPartsHandler handler : widgets) {
			try {
				req.setAttribute("partsType", "Widget");
				handler.setAttribute(req);
				handler.loadWidgets(req, res, application, page);
				handler.clearAttribute(req);
				req.removeAttribute("partsType");
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (ServletException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * パーツを返します。
	 *
	 * @return パーツのリスト
	 */
	public List<TopViewPartsHandler> getParts() {
		return parts;
	}

	/**
	 * ウィジェットを返します。
	 *
	 * @return ウィジェットのリスト
	 */
	public List<TopViewPartsHandler> getWidgets() {
		return widgets;
	}

	/**
	 * 指定のパーツもしくはウィジェットを返します。
	 * @param key パーツもしくはウィジェットを特定するキー
	 * @return パーツもしくはウィジェット
	 */
	public TopViewPartsHandler getHandler(String key) {
		return handlerMap.get(key);
	}
}
