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
import java.util.List;

import javax.servlet.ServletException;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.definition.HtmlTemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;


public class MetaHtmlTemplate extends MetaTemplate {

	private static final long serialVersionUID = 4007608678165728446L;

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
	public HtmlTemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new HtmlTemplateRuntime();
	}

	@Override
	public void applyConfig(TemplateDefinition definition) {
		fillFrom(definition);
		HtmlTemplateDefinition def = (HtmlTemplateDefinition) definition;
		source = def.getSource();
		localizedSourceList = I18nUtil.toMeta(def.getLocalizedSourceList());
	}

	@Override
	public HtmlTemplateDefinition currentConfig() {
		HtmlTemplateDefinition definition = new HtmlTemplateDefinition();
		fillTo(definition);
		definition.setSource(source);
		definition.setLocalizedSourceList(I18nUtil.toDef(localizedSourceList));
		return definition;
	}

	public class HtmlTemplateRuntime extends TemplateRuntime {

		public MetaHtmlTemplate getMetaData() {
			return MetaHtmlTemplate.this;
		}

		@Override
		public void handleContent(WebRequestStack requestContext)
				throws IOException, ServletException {
			checkState();

//			if (requestContext.getResponse().getContentType() == null) {
//				if (getContentType() != null) {
//					requestContext.getResponse().setContentType(getContentType());
//				}
//			}

			String lang = ExecuteContext.getCurrentContext().getLanguage();
			String tempSource = source;
			if (localizedSourceList != null) {
				for (MetaLocalizedString mls : localizedSourceList) {
					if (mls.getLocaleName().equals(lang)) {
						if (StringUtil.isNotEmpty(mls.getStringValue())) {
							tempSource = mls.getStringValue();
						}
					}
				}
			}

			if (requestContext.getPageContext() != null) {
				requestContext.getPageContext().getOut().write(tempSource);
			} else {
				requestContext.getResponse().getWriter().write(tempSource);
			}
		}
	}
}
