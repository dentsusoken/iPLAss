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

import javax.servlet.ServletException;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.impl.web.template.report.MetaReportTemplate;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.definition.BinaryTemplateDefinition;
import org.iplass.mtp.web.template.definition.GroovyTemplateDefinition;
import org.iplass.mtp.web.template.definition.HtmlTemplateDefinition;
import org.iplass.mtp.web.template.definition.JspTemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;


@XmlSeeAlso({MetaGroovyTemplate.class, MetaHtmlTemplate.class, MetaJspTemplate.class, MetaBinaryTemplate.class, MetaReportTemplate.class})
public abstract class MetaTemplate extends BaseRootMetaData implements DefinableMetaData<TemplateDefinition> {

	/** LayoutAction内で実行するTemplateのattribute名 */
	public static final String CONTENT_TEMPLATE = "org.iplass.mtp.contentTemplate";

	/** Templateに適用するLayoutActionIdのattribute名 */
	public static final String LAYOUT_ACTION_ID = "org.iplass.mtp.layoutActionId";

	/** Templateに適用するLayoutAction名のattribute名 */
	public static final String LAYOUT_ACTION_NAME = "org.iplass.mtp.layoutActionName";

	//TODO hashcode equals

	//TODO キャッシュ有無など

	private static final long serialVersionUID = -3123289156066341158L;

	private String contentType;

	private String layoutId;
	private String layoutName;
	private boolean layoutResolveByName;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public MetaTemplate copy() {
		return ObjectUtil.deepCopy(this);
	}

	//Definition → Meta インスタンス
	public static MetaTemplate createInstance(TemplateDefinition definition) {
		if (definition instanceof GroovyTemplateDefinition) {
			return new MetaGroovyTemplate();
		} else if (definition instanceof HtmlTemplateDefinition) {
			return new MetaHtmlTemplate();
		} else if (definition instanceof JspTemplateDefinition) {
			return new MetaJspTemplate();
		} else if (definition instanceof BinaryTemplateDefinition) {
			return new MetaBinaryTemplate();
		} else if (definition instanceof ReportTemplateDefinition) {
			return new MetaReportTemplate();
		}
		return null;
	}

	//Definition → Meta
	public abstract void applyConfig(TemplateDefinition definition);

	//Meta → Definition
	public abstract TemplateDefinition currentConfig();

	//Definition → Meta共通項目
	protected void fillFrom(TemplateDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		localizedDisplayNameList = I18nUtil.toMeta(definition.getLocalizedDisplayNameList());

		description = definition.getDescription();
		contentType = definition.getContentType();

		//name -> id 変換
		if (definition.getLayoutActionName() != null && !definition.getLayoutActionName().isEmpty()) {
			ActionMappingService service = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
			ActionMappingRuntime runtime = service.getRuntimeByName(definition.getLayoutActionName());
			layoutId = runtime.getMetaData().getId();
			layoutName = null;
			layoutResolveByName = false;
		}
	}

	//Meta共通項目 → Definition
	protected void fillTo(TemplateDefinition definition) {
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));
		definition.setDescription(description);
		definition.setContentType(contentType);

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

	}

	public abstract TemplateRuntime createRuntime(MetaDataConfig metaDataConfig);

	public abstract class TemplateRuntime extends BaseMetaDataRuntime {

		public TemplateRuntime() {
		}

		public void handle(WebRequestStack requestStack) throws IOException, ServletException {
			checkState();

			ActionMappingRuntime layout = null;

			//Resultで指定されているLayoutAction情報を取得
			String resultLayoutActionName = (String)requestStack.getAttribute(LAYOUT_ACTION_NAME);
			String resultLayoutActionId = (String)requestStack.getAttribute(LAYOUT_ACTION_ID);

			if (resultLayoutActionName != null || (layoutResolveByName && StringUtil.isNotEmpty(layoutName))) {
				ActionMappingService ams = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
				String executeLayoutActionName = resultLayoutActionName != null ? resultLayoutActionName : layoutName;
				layout = ams.getRuntimeByName(executeLayoutActionName);
				if (layout == null) {
					layout = ams.getRuntimeById(executeLayoutActionName);
				}
			} else if (resultLayoutActionId != null || StringUtil.isNotEmpty(layoutId)) {
				ActionMappingService ams = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
				String executeLayoutActionId = resultLayoutActionId != null ? resultLayoutActionId : layoutId;
				layout = ams.getRuntimeById(executeLayoutActionId);
			}

			if (layout != null) {
				WebRequestStack newStack = null;
				try {
					newStack = new WebRequestStack();
					newStack.setLayoutStack(true);
					newStack.setAttribute(CONTENT_TEMPLATE, this);
					layout.executeCommand(newStack);
				} finally {
					if (newStack != null) {
						newStack.finallyProcess();
					}
				}
			} else {
				if (requestStack.getResponse().getContentType() == null) {
					if (getContentType() != null) {
						requestStack.getResponse().setContentType(getContentType());
					}
				}
				handleContent(requestStack);
			}

		}

		public abstract void handleContent(WebRequestStack requestStack) throws IOException, ServletException;

		public abstract MetaTemplate getMetaData();

	}

}
