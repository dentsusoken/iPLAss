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

package org.iplass.mtp.impl.web.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.definition.JspTemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MetaJspTemplate extends MetaTemplate {

	private static final long serialVersionUID = -2563831318096568436L;

	private static final Logger logger = LoggerFactory.getLogger(MetaJspTemplate.class);

	//TODO hashcode equals

	private String path;

	private List<MetaLocalizedString> localizedPathList = new ArrayList<MetaLocalizedString>();

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<MetaLocalizedString> getLocalizedPathList() {
		return localizedPathList;
	}

	public void setLocalizedPathList(List<MetaLocalizedString> localizedPathList) {
		this.localizedPathList = localizedPathList;
	}

	@Override
	public JspTemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new JspTemplateRuntime();
	}

	@Override
	public void applyConfig(TemplateDefinition definition) {
		fillFrom(definition);
		JspTemplateDefinition def = (JspTemplateDefinition) definition;
		path = def.getPath();
		localizedPathList = I18nUtil.toMeta(def.getLocalizedPathList());
	}

	@Override
	public JspTemplateDefinition currentConfig() {
		JspTemplateDefinition definition = new JspTemplateDefinition();
		fillTo(definition);
		definition.setPath(path);
		definition.setLocalizedPathList(I18nUtil.toDef(localizedPathList));
		return definition;
	}

	public class JspTemplateRuntime extends TemplateRuntime {

		public MetaJspTemplate getMetaData() {
			return MetaJspTemplate.this;
		}

		@Override
		public void handleContent(WebRequestStack requestStack)
				throws IOException, ServletException {
			checkState();

			String lang = ExecuteContext.getCurrentContext().getLanguage();
			String tempPath = MetaJspTemplate.this.getPath();
			if (localizedPathList != null) {
				for (MetaLocalizedString mls : localizedPathList) {
					if (mls.getLocaleName().equals(lang)) {
						if (StringUtil.isNotEmpty(mls.getStringValue())) {
							tempPath = mls.getStringValue();
						}
					}
				}
			}

//			requestStack.getRequest().setAttribute("org.iplass.mtp.requestPath", tempPath);

//			//FIXME JSPでの記述が無駄
//			requestContext.getResponse().setContentType("text/html; charset=utf-8");

//			if (requestContext.getResponse().isCommitted()) {
				if (requestStack.getPageContext() != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("include(pageContext) " + tempPath + "...");
					}
					requestStack.getPageContext().include(tempPath);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("include(request) " + tempPath + "...");
					}
					HttpServletRequest req = requestStack.getRequest();
					if (!(req instanceof JspTemplateHttpServletRequest)) {
						req = new JspTemplateHttpServletRequest(requestStack.getRequest(), requestStack.getRequestContext());
					}
//					req = new JspTemplateHttpServletRequest(requestStack.getRequest(), requestStack.getRequestContext());
					requestStack.getRequest().getRequestDispatcher(tempPath).include(req, requestStack.getResponse());
				}
//			} else {
//				if (requestContext.getPageContext() != null) {
//					requestContext.getPageContext().forward(MetaJspTemplate.this.getPath());
//				} else {
//					requestContext.getRequest().getRequestDispatcher(
//							MetaJspTemplate.this.getPath()).forward(requestContext.getRequest(), requestContext.getResponse());
//				}
//
//			}
		}
	}

}
