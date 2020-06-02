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

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.view.top.parts.TemplateParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

/**
 * テンプレート系のパーツ
 * @author lis3wg
 */
public class MetaTemplateParts extends MetaTopViewParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = -940660706127581642L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaTemplateParts createInstance(TopViewParts parts) {
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
		templatePath = t.getTemplatePath();
	}

	@Override
	public TopViewParts currentConfig() {
		TemplateParts parts = new TemplateParts();
		parts.setTemplatePath(templatePath);
		return parts;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public TemplatePartsHandler createRuntime() {
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
