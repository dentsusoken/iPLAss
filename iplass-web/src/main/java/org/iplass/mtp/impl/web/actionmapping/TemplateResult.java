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

import jakarta.servlet.ServletException;

import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.impl.web.template.MetaTemplate;
import org.iplass.mtp.impl.web.template.MetaTemplate.TemplateRuntime;
import org.iplass.mtp.impl.web.template.TemplateService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.TemplateResultDefinition;


public class TemplateResult extends Result {
	private static final long serialVersionUID = 8382753736577498165L;

	private String templateId;
	private String templateName;
	private boolean resolveByName;

	private boolean useContentDisposition;
	private ContentDispositionType contentDispositionType;
	private String fileNameAttributeName;

	//レイアウト用ActiomMapping
	private String layoutId;
	private String layoutName;
	private boolean layoutResolveByName;

	public TemplateResult() {
	}

	public TemplateResult(String cmdStatus, String templateId) {
		this(cmdStatus, false, templateId);
	}

	public TemplateResult(String cmdStatus, boolean resolveByName, String templateIdOrName) {
		setCommandResultStatus(cmdStatus);
		this.resolveByName = resolveByName;
		if (resolveByName) {
			this.templateName = templateIdOrName;
		} else {
			this.templateId = templateIdOrName;
		}
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public boolean isResolveByName() {
		return resolveByName;
	}

	public void setResolveByName(boolean resolveByName) {
		this.resolveByName = resolveByName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public boolean isLayoutResolveByName() {
		return layoutResolveByName;
	}

	public void setLayoutResolveByName(boolean layoutResolveByName) {
		this.layoutResolveByName = layoutResolveByName;
	}

	@Override
	public ResultRuntime createRuntime() {
		return new TemplateResultRuntime();
	}

	@Override
	public void applyConfig(ResultDefinition definition) {
		fillFrom(definition);
		TemplateResultDefinition def = (TemplateResultDefinition) definition;
		TemplateService ts = ServiceRegistry.getRegistry().getService(TemplateService.class);
		templateId = ts.getRuntimeByName(def.getTemplateName()).getMetaData().getId();
		resolveByName = false;
		templateName = null;

		useContentDisposition = def.isUseContentDisposition();
		contentDispositionType = def.getContentDispositionType();
		fileNameAttributeName = def.getFileNameAttributeName();

		//name -> id 変換
		if (def.getLayoutActionName() != null && !def.getLayoutActionName().isEmpty()) {
			ActionMappingService service = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
			ActionMappingRuntime runtime = service.getRuntimeByName(def.getLayoutActionName());
			layoutId = runtime.getMetaData().getId();
			layoutName = null;
			layoutResolveByName = false;
		}

	}

	@Override
	public ResultDefinition currentConfig() {
		TemplateResultDefinition definition = new TemplateResultDefinition();
		fillTo(definition);
		TemplateService ts = ServiceRegistry.getRegistry().getService(TemplateService.class);
		TemplateRuntime t;
		if (resolveByName) {
			t = ts.getRuntimeByName(templateName);
			if (t == null) {
				t = ts.getRuntimeById(templateName);
			}
		} else {
			t = ts.getRuntimeById(templateId);
		}
		if (t != null) {
			MetaTemplate meta = t.getMetaData();
			definition.setTemplateName(meta.getName());
		}

		definition.setUseContentDisposition(useContentDisposition);
		definition.setContentDispositionType(contentDispositionType);
		definition.setFileNameAttributeName(fileNameAttributeName);

		if (StringUtil.isNotEmpty(layoutId)) {
			//id -> name 変換
			ActionMappingService service = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
			ActionMappingRuntime runtime = service.getRuntimeById(layoutId);
			if (runtime != null) {
				definition.setLayoutActionName(runtime.getMetaData().getName());
			}
		} else if (StringUtil.isNotEmpty(layoutName)) {
			//存在チェック
			ActionMappingService service = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
			ActionMappingRuntime runtime = service.getRuntimeByName(layoutName);
			if (runtime != null) {
				definition.setLayoutActionName(runtime.getMetaData().getName());
			}
		}

		return definition;
	}

	public class TemplateResultRuntime extends ResultRuntime {

		@Override
		public TemplateResult getMetaData() {
			return TemplateResult.this;
		}

		private TemplateRuntime getTemplate() {
			TemplateService ts = ServiceRegistry.getRegistry().getService(TemplateService.class);

			TemplateRuntime tr = null;;
			if (resolveByName) {
				tr = ts.getRuntimeByName(templateName);
				if (tr == null) {
					tr = ts.getRuntimeById(templateName);
				}
			} else {
				tr = ts.getRuntimeById(templateId);
			}
			return tr;
		}

		@Override
		public void handle(WebRequestStack requestStack) throws ServletException, IOException {
			//TODO Templateは毎回IdからLookupで良いか？TemplateRuntimeのインスタンスで保持がよいか？
			//     ちゃんとキャッシュ削除で整合性がとれれば、TemplateRuntimeで保持のほうがよいか？
			TemplateRuntime tr = getTemplate();
			if (tr == null) {
				if (resolveByName) {
					throw new WebProcessRuntimeException("can not find template... name:" + templateName);
				} else {
					throw new WebProcessRuntimeException("can not find template... id:" + templateId);
				}
			}

			if (useContentDisposition) {
				String fileName = null;
				if (StringUtil.isNotEmpty(fileNameAttributeName)) {
					fileName = (String)requestStack.getRequestContext().getAttribute(fileNameAttributeName);
				}
				if (fileName == null) {
					//名前が未指定の場合は、テンプレート名(階層化されている場合は最後)
					fileName = getLastTemplateName(tr.getMetaData().getName());
				}
				WebUtil.setContentDispositionHeader(requestStack, getContentDispositionType(), fileName);
			}

			if (layoutResolveByName && StringUtil.isNotEmpty(layoutName)) {
				requestStack.setAttribute(MetaTemplate.LAYOUT_ACTION_NAME, layoutName);
			} else if (StringUtil.isNotEmpty(layoutId)) {
				requestStack.setAttribute(MetaTemplate.LAYOUT_ACTION_ID, layoutId);
			}

			tr.handle(requestStack);
		}

		public String getTemplateName() {
			TemplateRuntime tr = getTemplate();
			if (tr == null) {
				return null;
			}
			return tr.getMetaData().getName();
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
