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

package org.iplass.mtp.impl.view.top.parts;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.view.top.parts.InformationParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

/**
 * アクション系のパーツ
 * @author lis3wg
 */
@XmlSeeAlso({MetaInformationParts.class})
public abstract class MetaActionParts extends MetaTopViewContentParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 8063378979184525848L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaActionParts createInstance(TopViewParts parts) {
		if (parts instanceof InformationParts) {
			return MetaInformationParts.createInstance(parts);
		}
		return null;
	}

	@Override
	public abstract ActionPartsHandler createRuntime();

	/**
	 * アクション系パーツランタイム
	 * @author lis3wg
	 */
	public abstract class ActionPartsHandler extends TopViewPartsHandler {

		/**
		 * コンストラクタ
		 * @param metadata
		 */
		public ActionPartsHandler(MetaActionParts metadata) {
			super(metadata);
		}

		/**
		 * パーツかどうか
		 * @return パーツかどうか
		 */
		public abstract boolean isParts();

		/**
		 * ウィジェットかどうか
		 * @return ウィジェットかどうか
		 */
		public abstract boolean isWidget();

		/**
		 * パーツのアクション名を取得します。
		 * @return パーツのアクション名
		 */
		public abstract String getActionNameForParts(HttpServletRequest req);

		/**
		 * ウィジェットのアクション名を取得します。
		 * @return ウィジェットのアクション名
		 */
		public abstract String getActionNameForWidget(HttpServletRequest req);

		@Override
		public void loadParts(HttpServletRequest req, HttpServletResponse res,
				ServletContext application, PageContext page) throws IOException, ServletException {
			if (isParts()) {
				try {
					WebUtil.include(getActionNameForParts(req), req, res, application, page);
				} catch (Exception e) {
					//権限エラー等
				}
			}
		}

		@Override
		public void loadWidgets(HttpServletRequest req,
				HttpServletResponse res, ServletContext application,
				PageContext page) throws IOException, ServletException {
			if (isWidget()) {
				try {
					WebUtil.include(getActionNameForWidget(req), req, res, application, page);
				} catch (Exception e) {
					//権限エラー等
				}
			}
		}
	}
}
