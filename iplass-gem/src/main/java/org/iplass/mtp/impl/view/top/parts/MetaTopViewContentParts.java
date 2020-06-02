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

package org.iplass.mtp.impl.view.top.parts;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.view.top.parts.ActionParts;
import org.iplass.mtp.view.top.parts.CalendarParts;
import org.iplass.mtp.view.top.parts.EntityListParts;
import org.iplass.mtp.view.top.parts.LastLoginParts;
import org.iplass.mtp.view.top.parts.SeparatorParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.TreeViewParts;

/**
 * 画面表示パーツ
 * @author li3369
 */
@XmlSeeAlso({MetaActionParts.class, MetaEntityListParts.class, MetaCalendarParts.class, 
	MetaLastLoginParts.class, MetaTreeViewParts.class, MetaSeparatorParts.class})
public abstract class MetaTopViewContentParts extends MetaTopViewParts{

	/** SerialVersionUID */
	private static final long serialVersionUID = 7929105536750599630L;
	
	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaTopViewContentParts createInstance(TopViewParts parts) {
		if (parts instanceof ActionParts) {
			return MetaActionParts.createInstance(parts);
		} else if (parts instanceof EntityListParts) {
			return MetaEntityListParts.createInstance(parts);
		} else if (parts instanceof CalendarParts) {
			return MetaCalendarParts.createInstance(parts);
		} else if (parts instanceof LastLoginParts) {
			return MetaLastLoginParts.createInstance(parts);
		} else if (parts instanceof TreeViewParts) {
			return MetaTreeViewParts.createInstance(parts);
		} else if (parts instanceof SeparatorParts) {
			return MetaSeparatorParts.createInstance(parts);
		}
		return null;
	}
	
	/** スタイルシートのクラス名 */
	protected String style;

	/**
	 * スタイルシートのクラス名を取得します。
	 * @return スタイルシートのクラス名
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * スタイルシートのクラス名を設定します。
	 * @param style スタイルシートのクラス名
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	
	/**
	 * 画面表示パーツランタイム
	 * @author li3369
	 */
	public abstract class TopViewContentPartsHandler extends TopViewPartsHandler {

		/**
		 * コンストラクタ
		 * @param metadata
		 */
		public TopViewContentPartsHandler(MetaTopViewParts metadata) {
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
		 * パーツのテンプレートパスを取得します。
		 * @return パーツのテンプレートパス
		 */
		public abstract String getTemplatePathForParts(HttpServletRequest req);

		/**
		 * ウィジェットのテンプレートパスを取得します。
		 * @return ウィジェットのテンプレートパス
		 */
		public abstract String getTemplatePathForWidget(HttpServletRequest req);
		
		@Override
		public void loadParts(HttpServletRequest req, HttpServletResponse res,
				ServletContext application, PageContext page) throws IOException, ServletException {
			if (isParts()) {
				String path = getTemplatePathForParts(req);
				if (path != null) {
					WebUtil.includeTemplate(path, req, res, application, page);
				}
			}
		}

		@Override
		public void loadWidgets(HttpServletRequest req, HttpServletResponse res,
				ServletContext application, PageContext page) throws IOException, ServletException {
			if (isWidget()) {
				String path = getTemplatePathForWidget(req);
				if (path != null) {
					WebUtil.includeTemplate(path, req, res, application, page);
				}
			}
		}
	}
}
