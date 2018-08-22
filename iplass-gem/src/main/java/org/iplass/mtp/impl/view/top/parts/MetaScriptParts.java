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

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.template.MetaGroovyTemplate;
import org.iplass.mtp.view.top.parts.ScriptParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

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
		this.script = s.getScript();
	}

	@Override
	public TopViewParts currentConfig() {
		ScriptParts parts = new ScriptParts();
		parts.setScript(script);
		return parts;
	}

	@Override
	public TopViewPartsHandler createRuntime() {
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
				template.doTemplate(new MetaGroovyTemplate.WebGroovyTemplateBinding(WebUtil.getRequestContext(), req, res, application, page));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void loadWidgets(HttpServletRequest req,
				HttpServletResponse res, ServletContext application,
				PageContext page) throws IOException, ServletException {
			try {
				template.doTemplate(new MetaGroovyTemplate.WebGroovyTemplateBinding(WebUtil.getRequestContext(), req, res, application, page));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
