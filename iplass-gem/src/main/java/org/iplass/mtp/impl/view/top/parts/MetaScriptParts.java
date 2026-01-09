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

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.top.TopViewHandler;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.template.MetaGroovyTemplate;
import org.iplass.mtp.view.top.parts.ScriptParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.jsp.PageContext;

/**
 * スクリプトパーツ
 * @author lis3wg
 */
public class MetaScriptParts extends MetaTopViewParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 8631794496813180936L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaScriptParts createInstance(TopViewParts parts) {
		return new MetaScriptParts();
	}

	/** スクリプト */
	private String script;

	/** テンプレートのキー */
	private String key;

	/**
	 * スクリプトを取得します。
	 * @return スクリプト
	 */
	public String getScript() {
	    return script;
	}

	/**
	 * スクリプトを設定します。
	 * @param script スクリプト
	 */
	public void setScript(String script) {
	    this.script = script;
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		ScriptParts s = (ScriptParts) parts;
		fillFrom(s);
		this.script = s.getScript();
	}

	@Override
	public TopViewParts currentConfig() {
		ScriptParts parts = new ScriptParts();
		fillTo(parts);
		parts.setScript(script);
		return parts;
	}

	@Override
	public TopViewPartsHandler createRuntime(TopViewHandler topView) {
		return new ScriptPartsHandler(this);
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * スクリプトパーツランタイム
	 * @author lis3wg
	 */
	public class ScriptPartsHandler extends TopViewPartsHandler {

		/** テンプレート */
		private GroovyTemplate template;

		/**
		 * コンストラクタ
		 */
		public ScriptPartsHandler(MetaScriptParts meta) {
			super(meta);
			if (meta.script != null && meta.key == null) {
				meta.key = "Jsp_" + GroovyTemplateCompiler.randomName().replace("-", "_");
				template = compile(key, meta);
			}
		}

		/**
		 * スクリプトをコンパイルしてテンプレートを作成します。
		 * @param key テンプレートのキー
		 * @return テンプレート
		 */
		private GroovyTemplate compile(String key, MetaScriptParts meta) {
			TenantContext tenant = ExecuteContext.getCurrentContext().getTenantContext();
			return GroovyTemplateCompiler.compile(
					meta.script, key, (GroovyScriptEngine) tenant.getScriptEngine());
		}

		@Override
		public void setAttribute(HttpServletRequest req) {
		}

		@Override
		public void clearAttribute(HttpServletRequest req) {
		}

		@Override
		public void loadParts(HttpServletRequest req, HttpServletResponse res,
				ServletContext application, PageContext page)
				throws IOException, ServletException {
			try {
				renderTemplateWithMaxHeight(req, res, application, page);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void loadWidgets(HttpServletRequest req,
				HttpServletResponse res, ServletContext application,
				PageContext page) throws IOException, ServletException {
			try {
				renderTemplateWithMaxHeight(req, res, application, page);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * maxHeight 指定がある場合に div でラップしてテンプレートを出力します。
		 */
		private void renderTemplateWithMaxHeight(
				HttpServletRequest req,
				HttpServletResponse res,
				ServletContext application,
				PageContext page) throws IOException, ServletException {

			Integer mh = this.getMetaData()
					.getMaxHeight();

			boolean useMaxHeight = (mh != null && mh > 0);

			if (useMaxHeight) {
				page.getOut()
						.write(
								String.format(
										"<div class=\"topview-parts\" style=\"max-height:%dpx;overflow-y:auto;\">\n",
										mh));
			}

			template.doTemplate(
					new MetaGroovyTemplate.WebGroovyTemplateBinding(
							WebUtil.getRequestContext(), req, res, application, page));

			if (useMaxHeight) {
				page.getOut()
						.write("</div>\n");
			}
		}
	}
}
