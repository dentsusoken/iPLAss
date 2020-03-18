/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webhook.endpointaddress;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webhook.template.endpointaddress.WebEndPointDefinition;
import org.iplass.mtp.webhook.template.endpointaddress.WebHookEndPoint;

@XmlRootElement
public class MetaWebEndPointDefinition extends BaseRootMetaData implements DefinableMetaData<WebEndPointDefinition>{

	private static final long serialVersionUID = 7029271819447338103L;
	/** 固有id、metaのidと同じ内容になるはずです */
	private String headerAuthType;
	private String headerAuthTypeName;

	/** 送り先 */
	private String url;
	
	/** セキュリティ関連部分はtemplateManagerによってdbで管理しています */
	
	//Definition → Meta
	@Override
	public void applyConfig(WebEndPointDefinition definition) {
		this.name = definition.getName();
		this.displayName = definition.getDisplayName();
		this.description = definition.getDescription();
		this.headerAuthType = definition.getHeaderAuthType();
		this.url = definition.getUrl();
		this.headerAuthTypeName = definition.getHeaderAuthTypeName();
	}
	
	//Meta → Definition
	@Override
	public WebEndPointDefinition currentConfig() {
		WebEndPointDefinition definition = new WebEndPointDefinition();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		definition.setHeaderAuthType(headerAuthType);
		definition.setHeaderAuthTypeName(headerAuthTypeName);
		definition.setUrl(url);
		return definition;
	}
	public MetaWebEndPointDefinition() {
		
	}
	
	public MetaWebEndPointDefinition(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		if (url == null) {
			url = "";
		}
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHeaderAuthType() {
		return headerAuthType;
	}

	public void setHeaderAuthType(String headerAuthType) {
		this.headerAuthType = headerAuthType;
	}
	
	public String getHeaderAuthTypeName() {
		return headerAuthTypeName;
	}

	public void setHeaderAuthTypeName(String headerAuthTypeName) {
		this.headerAuthTypeName = headerAuthTypeName;
	}


	@Override
	public WebEndPointRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new WebEndPointRuntime();
	}

	@Override
	public RootMetaData copy() {
		return ObjectUtil.deepCopy(this);
	}


	public class WebEndPointRuntime extends BaseMetaDataRuntime{
		private GroovyTemplate urlTemplate;

		
		public WebEndPointRuntime() {
			super();
			try {
				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				urlTemplate = GroovyTemplateCompiler.compile(getUrl(), "WebHookTemplate_Subscriber_" + getName() + "_" + id, (GroovyScriptEngine) se);
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}
		public WebHookEndPoint createWebHookEndPoint() {
			WebHookEndPoint webHookEndPoint = new WebHookEndPoint();
			webHookEndPoint.setUrl(getUrl());
			webHookEndPoint.setHeaderAuthSchemeName(getHeaderAuthTypeName());
			webHookEndPoint.setHeaderAuthType(getHeaderAuthType());
			webHookEndPoint.setEndPointName(getName());
			return webHookEndPoint;
		}
		@Override
		public MetaWebEndPointDefinition getMetaData() {
			return MetaWebEndPointDefinition.this;
		}
		public GroovyTemplate getUrlTemplate() {
			return urlTemplate;
		}
	}


}
