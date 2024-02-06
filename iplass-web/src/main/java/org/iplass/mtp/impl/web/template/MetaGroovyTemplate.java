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

package org.iplass.mtp.impl.web.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.codehaus.groovy.runtime.MethodClosure;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.RequestContextWrapper;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.template.groovy.WebGTmplBase;
import org.iplass.mtp.web.template.definition.GroovyTemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import groovy.lang.MissingPropertyException;


public class MetaGroovyTemplate extends MetaTemplate {

	private static final long serialVersionUID = -8575704321521962011L;

	//TODO hashcode、equals

	private String source;

	private List<MetaLocalizedString> localizedSourceList = new ArrayList<MetaLocalizedString>();

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<MetaLocalizedString> getLocalizedSourceList() {
		return localizedSourceList;
	}

	public void setLocalizedSourceList(List<MetaLocalizedString> localizedSourceList) {
		this.localizedSourceList = localizedSourceList;
	}

	@Override
	public GroovyTemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new GroovyTemplateRuntime();
	}

	@Override
	public void applyConfig(TemplateDefinition definition) {
		fillFrom(definition);
		GroovyTemplateDefinition def = (GroovyTemplateDefinition) definition;
		source = def.getSource();
		localizedSourceList = I18nUtil.toMeta(def.getLocalizedSourceList());
	}

	@Override
	public GroovyTemplateDefinition currentConfig() {
		GroovyTemplateDefinition definition = new GroovyTemplateDefinition();
		fillTo(definition);
		definition.setSource(source);
		definition.setLocalizedSourceList(I18nUtil.toDef(localizedSourceList));
		return definition;
	}

	private GroovyTemplate compileScript(String source) {
		if (source != null) {
			//TODO 全部"_"へ変換しているがこれでよい？
			String templateName = "_" + getId().replace("-", "_").replace("/", "_").replace(".", "_");
			TenantContext tenantContext = ExecuteContext.getCurrentContext().getTenantContext();
			return GroovyTemplateCompiler.compile(source, templateName, WebGTmplBase.class.getName(), (GroovyScriptEngine) tenantContext.getScriptEngine());
		}
		return null;
	}

	public class GroovyTemplateRuntime extends TemplateRuntime {

		private GroovyTemplate template;
		private Map<String, GroovyTemplate> templateMap = new HashMap<String, GroovyTemplate>();

		public GroovyTemplateRuntime() {

			try {
				template = compileScript(source);

				if (localizedSourceList != null) {
					for (MetaLocalizedString mls : localizedSourceList) {
						templateMap.put(mls.getLocaleName(), compileScript(mls.getStringValue()));
					}
				}

			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
//			if (source != null) {
//				//TODO 全部"_"へ変換しているがこれでよい？
//				String templateName = "_" + getId().replace("-", "_").replace("/", "_").replace(".", "_");
//				TenantContext tenantContext = ExecuteContext.getCurrentContext().getTenant();
//				template = GroovyTemplateCompiler.compile(source, templateName, (GroovyScriptEngine) tenantContext.getScriptEngine());
//			}
		}

		public MetaGroovyTemplate getMetaData() {
			return MetaGroovyTemplate.this;
		}

		@Override
		public void handleContent(WebRequestStack requestContext)
				throws IOException, ServletException {
			checkState();

			GroovyTemplate _template = template;
			String lang = ExecuteContext.getCurrentContext().getLanguage();

			if (templateMap.get(lang) != null) {
				_template = templateMap.get(lang);
			}

			if (_template != null) {
				WebGroovyTemplateBinding bind = new WebGroovyTemplateBinding(requestContext);
				_template.doTemplate(bind);
			}
		}

	}

	/**
	 * 遅延初期化するSessionContext
	 * @author K.Higuchi
	 *
	 */
	private static class LazySessionContext implements SessionContext {

		private final RequestContext req;
		private SessionContext sess;

		private LazySessionContext(RequestContext req) {
			this.req = req;
		}

		private void initSess() {
			if (sess == null) {
				sess = req.getSession();
			}
		}

		@Override
		public Object getAttribute(String name) {
			initSess();
			return sess.getAttribute(name);
		}

		@Override
		public void setAttribute(String name, Object value) {
			initSess();
			sess.setAttribute(name, value);
		}

		@Override
		public Iterator<String> getAttributeNames() {
			initSess();
			return sess.getAttributeNames();
		}

		@Override
		public void removeAttribute(String name) {
			initSess();
			sess.removeAttribute(name);
		}

		@Override
		public BinaryReference loadFromTemporary(long lobId) {
			initSess();
			return sess.loadFromTemporary(lobId);
		}

	}

	public static class WebGroovyTemplateBinding extends GroovyTemplateBinding {

		private RequestContext reqCon;
		private HttpServletRequest req;
		private HttpServletResponse res;
		private ServletContext application;
		private PageContext page;

		public WebGroovyTemplateBinding(RequestContext reqCon, HttpServletRequest req,
				HttpServletResponse res, ServletContext application, PageContext page) throws IOException {
			super(page != null ? page.getOut() : res.getWriter());
			this.reqCon = reqCon;
			this.req = req;
			this.res = res;
			this.application = application;
			this.page = page;

			setVariable("request", reqCon);
			setVariable("session", new LazySessionContext(reqCon));

			//TODO その他暗黙オブジェクト
			//     User情報
			//     ヘッダー情報
			//     Cookie情報とか、必要性も検討

			//include系メソッドのバインド
			setVariable("include", new MethodClosure((WebGroovyTemplateBinding) this, "include"));
			setVariable("includeTemplate", new MethodClosure((WebGroovyTemplateBinding) this, "includeTemplate"));
			setVariable("renderContent", new MethodClosure((WebGroovyTemplateBinding) this, "renderContent"));
		}

		public WebGroovyTemplateBinding(WebRequestStack requestStack) throws IOException {
			this(requestStack.getRequestContext(), requestStack.getRequest(), requestStack.getResponse(),
					requestStack.getServletContext(), requestStack.getPageContext());
		}

		@Override
		public Object getVariable(String name) {
			try {
				return super.getVariable(name);
			} catch (MissingPropertyException e) {
				Object val = reqCon.getAttribute(name);
				if (val == null && reqCon.getSession(false) != null) {
					val = reqCon.getSession(false).getAttribute(name);
				}
				if (val == null) {
					throw e;
				}
				return val;
			}
		}

		@Override
		public boolean hasVariable(String name) {
			boolean ret = super.hasVariable(name);
			if (!ret) {
				ret = reqCon.getAttribute(name) != null;
			}
			if (!ret && reqCon.getSession(false) != null) {
				ret = reqCon.getSession(false).getAttribute(name) != null;
			}
			return ret;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Map getVariables() {
			//TODO ラップ必要？？
			return super.getVariables();
		}

		public void include(String path) throws ServletException, IOException {
			WebUtil.include(path, req, res, application, page);
		}

		public void include(String path, RequestContextWrapper request) throws ServletException, IOException {
			WebUtil.include(path, req, res, application, page, request);
		}

		public void includeTemplate(String templateName) throws ServletException, IOException {
			WebUtil.includeTemplate(templateName, req, res, application, page);
		}

		public void includeTemplate(String templateName, RequestContextWrapper request) throws ServletException, IOException {
			WebUtil.includeTemplate(templateName, req, res, application, page, request);
		}

		public void renderContent() throws IOException, ServletException {
			WebUtil.renderContent(req, res, application, page);
		}

	}

}
