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

package org.iplass.mtp.impl.web.actionmapping;

import java.io.IOException;

import javax.servlet.ServletException;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.template.MetaTemplate;
import org.iplass.mtp.impl.web.template.MetaTemplate.TemplateRuntime;
import org.iplass.mtp.impl.web.template.TemplateService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.definition.result.DynamicTemplateResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;


public class DynamicTemplateResult extends Result {

	//TODO DynamicResultへ。テンプレートだけでなくその他のResultも動的に選択可能にする。

	private static final long serialVersionUID = 3667959224517917109L;

	private String templatePathAttributeName;

	private boolean useContentDisposition;
	private ContentDispositionType contentDispositionType;
	private String fileNameAttributeName;

	private String layoutActionAttributeName;

	public DynamicTemplateResult() {
	}

	public DynamicTemplateResult(String cmdStatus, String templatePathAttributeName) {
		setCommandResultStatus(cmdStatus);
		this.templatePathAttributeName = templatePathAttributeName;
	}

	public String getTemplatePathAttributeName() {
		return templatePathAttributeName;
	}

	public void setTemplatePathAttributeName(String templatePathAttributeName) {
		this.templatePathAttributeName = templatePathAttributeName;
	}

	public boolean isUseContentDisposition() {
		return useContentDisposition;
	}

	public void setUseContentDisposition(boolean useContentDisposition) {
		this.useContentDisposition = useContentDisposition;
	}

	public ContentDispositionType getContentDispositionType() {
		return contentDispositionType;
	}

	public void setContentDispositionType(ContentDispositionType contentDispositionType) {
		this.contentDispositionType = contentDispositionType;
	}

	public String getFileNameAttributeName() {
		return fileNameAttributeName;
	}

	public void setFileNameAttributeName(String fileNameAttributeName) {
		this.fileNameAttributeName = fileNameAttributeName;
	}

	public String getLayoutActionAttributeName() {
		return layoutActionAttributeName;
	}

	public void setLayoutActionAttributeName(String layoutActionAttributeName) {
		this.layoutActionAttributeName = layoutActionAttributeName;
	}

	@Override
	public DynamicTemplateResultRuntime createRuntime() {
		return new DynamicTemplateResultRuntime();
	}

	@Override
	public void applyConfig(ResultDefinition definition) {
		fillFrom(definition);
		DynamicTemplateResultDefinition def = (DynamicTemplateResultDefinition) definition;
		templatePathAttributeName = def.getTemplatePathAttributeName();

		useContentDisposition = def.isUseContentDisposition();
		contentDispositionType = def.getContentDispositionType();
		fileNameAttributeName = def.getFileNameAttributeName();
	}

	@Override
	public ResultDefinition currentConfig() {
		DynamicTemplateResultDefinition definition = new DynamicTemplateResultDefinition();
		fillTo(definition);
		definition.setTemplatePathAttributeName(templatePathAttributeName);

		definition.setUseContentDisposition(useContentDisposition);
		definition.setContentDispositionType(contentDispositionType);
		definition.setFileNameAttributeName(fileNameAttributeName);
		definition.setLayoutActionAttributeName(layoutActionAttributeName);

		return definition;
	}

	public class DynamicTemplateResultRuntime extends ResultRuntime {

		@Override
		public DynamicTemplateResult getMetaData() {
			return DynamicTemplateResult.this;
		}

		@Override
		public void handle(WebRequestStack requestStack)
				throws ServletException, IOException {

			RequestContext cmdRequestContext = requestStack.getRequestContext();
			String templatePath = (String) cmdRequestContext.getAttribute(templatePathAttributeName);

			TemplateService ts = ServiceRegistry.getRegistry().getService(TemplateService.class);
			TemplateRuntime tr = ts.getRuntimeByName(templatePath);

			if (tr == null) {
				throw new WebProcessRuntimeException(templatePath + " not found");
			}

			if (useContentDisposition) {
				String fileName = null;
				if (StringUtil.isNotEmpty(fileNameAttributeName)) {
					fileName = (String)cmdRequestContext.getAttribute(fileNameAttributeName);
				}
				if (fileName == null) {
					//名前が未指定の場合は、テンプレート名(階層化されている場合は最後)
					fileName = getLastTemplateName(tr.getMetaData().getName());
				}
				WebUtil.setContentDispositionHeader(requestStack, getContentDispositionType(), fileName);
			}

			if (StringUtil.isNotEmpty(layoutActionAttributeName)) {
				String layoutActionName = (String)cmdRequestContext.getAttribute(layoutActionAttributeName);
				if (StringUtil.isNotEmpty(layoutActionName)) {
					requestStack.setAttribute(MetaTemplate.LAYOUT_ACTION_NAME, layoutActionName);
				}
			}

			tr.handle(requestStack);
		}

		@Override
		public void finallyProcess(WebRequestStack requestContext) {
		}

		private String getLastTemplateName(String templateName) {
			if (templateName.contains("/")) {
				return templateName.substring(templateName.lastIndexOf("/") + 1);
			} else {
				return templateName;
			}
		}

	}

}
