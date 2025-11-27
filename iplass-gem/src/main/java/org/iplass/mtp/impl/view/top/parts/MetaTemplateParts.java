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

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.top.TopViewHandler;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.view.top.parts.CalendarParts;
import org.iplass.mtp.view.top.parts.EntityListParts;
import org.iplass.mtp.view.top.parts.LastLoginParts;
import org.iplass.mtp.view.top.parts.TemplateParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.TreeViewParts;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.jsp.PageContext;
import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 * テンプレート系のパーツ
 * @author lis3wg
 */
@XmlSeeAlso({MetaCalendarParts.class, MetaEntityListParts.class,
	MetaLastLoginParts.class, MetaTreeViewParts.class})
public class MetaTemplateParts extends MetaTopViewContentParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = -940660706127581642L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaTemplateParts createInstance(TopViewParts parts) {
		if (parts instanceof CalendarParts) {
			return MetaCalendarParts.createInstance(parts);
		} else if (parts instanceof EntityListParts) {
			return MetaEntityListParts.createInstance(parts);
		} else if (parts instanceof LastLoginParts) {
			return MetaLastLoginParts.createInstance(parts);
		} else if (parts instanceof TreeViewParts) {
			return MetaTreeViewParts.createInstance(parts);
		}
		return new MetaTemplateParts();
	}

	/** テンプレートパス */
	private String templatePath;

	/**
	 * テンプレートパスを取得します。
	 * @return テンプレートパス
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * テンプレートパスを設定します。
	 * @param templatePath テンプレートパス
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		TemplateParts t = (TemplateParts) parts;
		fillFrom(t);
		templatePath = t.getTemplatePath();
	}

	@Override
	public TopViewParts currentConfig() {
		TemplateParts parts = new TemplateParts();
		fillTo(parts);
		parts.setTemplatePath(templatePath);
		return parts;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public TemplatePartsHandler createRuntime(TopViewHandler topView) {
		return new TemplatePartsHandler(this) {

			@Override
			public void setAttribute(HttpServletRequest req) {
			}

			@Override
			public void clearAttribute(HttpServletRequest req) {
			}

			@Override
			public boolean isWidget() {
				return true;
			}

			@Override
			public boolean isParts() {
				return true;
			}

			@Override
			public String getTemplatePathForWidget(HttpServletRequest req) {
				return templatePath;
			}

			@Override
			public String getTemplatePathForParts(HttpServletRequest req) {
				return templatePath;
			}
		};
	}

	/**
	 * テンプレート系パーツランタイム
	 * @author lis3wg
	 */
	public abstract class TemplatePartsHandler extends TopViewPartsHandler {

		/**
		 * コンストラクタ
		 * @param metadata
		 */
		public TemplatePartsHandler(MetaTopViewParts metadata) {
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
					includeTemplateWithWrapper(path, this.getMetaData()
							.getMaxHeight(), req, res, application, page);
				}
			}
		}

		@Override
		public void loadWidgets(HttpServletRequest req, HttpServletResponse res,
				ServletContext application, PageContext page) throws IOException, ServletException {
			if (isWidget()) {
				String path = getTemplatePathForWidget(req);
				if (path != null) {
					includeTemplateWithWrapper(path, this.getMetaData().getMaxHeight(), req, res, application, page);
				}
			}
		}

		/**
		 * 共通テンプレート出力処理
		 */
		private void includeTemplateWithWrapper(String path, Integer maxHeight,
				HttpServletRequest req,
				HttpServletResponse res,
				ServletContext application,
				PageContext page) throws IOException, ServletException {
			// CSS を出力、1 回だけ
			Boolean cssInjected = (Boolean) req.getAttribute("_mtop_template_css_injected");
			if (cssInjected == null || !cssInjected) {
				page.getOut()
						.write("<style>" +
								".mtop-template-container {" +
								"display: block;" +
								"width: 100%;" +
								"box-sizing: border-box;" +
								"}" +
								"</style>");
				req.setAttribute("_mtop_template_css_injected", true);
			}

			// コンテナを出力
			String wrapperStart = "<div class='mtop-template-container'";
			if (maxHeight != null && maxHeight > 0) {
				wrapperStart += " style='max-height:" + maxHeight.intValue() + "px; overflow:auto;'";
			}
			wrapperStart += ">";
			page.getOut()
					.write(wrapperStart);

			// 実際のテンプレートを include
			WebUtil.includeTemplate(path, req, res, application, page);

			// コンテナ閉じ
			page.getOut()
					.write("</div>");
		}
	}
}
