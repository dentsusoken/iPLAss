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
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.endpoint.definition.WebHookEndPointDefinition;

@XmlRootElement
public class MetaWebHookEndPointDefinition extends BaseRootMetaData implements DefinableMetaData<WebHookEndPointDefinition>{

	private static final long serialVersionUID = 7029271819447338103L;
	/** 固有id、metaのidと同じ内容になるはずです */
	private String headerAuthType;
	private String headerAuthCustomTypeName;

	/** 送り先 */
	private String url;

	private boolean hmacEnabled;
	private String hmacHashHeader;
	/** セキュリティ関連部分はtemplateManagerによってdbで管理しています */
	
	//Definition → Meta
	@Override
	public void applyConfig(WebHookEndPointDefinition definition) {
		this.name = definition.getName();
		this.displayName = definition.getDisplayName();
		this.description = definition.getDescription();
		this.headerAuthType = definition.getHeaderAuthType();
		this.url = definition.getUrl();
		this.headerAuthCustomTypeName = definition.getHeaderAuthCustomTypeName();
		this.hmacHashHeader = definition.getHmacHashHeader();
		this.hmacEnabled = definition.isHmacEnabled();
	}
	
	//Meta → Definition
	@Override
	public WebHookEndPointDefinition currentConfig() {
		WebHookEndPointDefinition definition = new WebHookEndPointDefinition();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		definition.setHeaderAuthType(headerAuthType);
		definition.setHeaderAuthCustomTypeName(headerAuthCustomTypeName);
		definition.setUrl(url);
		definition.setHmacHashHeader(hmacHashHeader);
		definition.setHmacEnabled(hmacEnabled);
		return definition;
	}
	public MetaWebHookEndPointDefinition() {
		
	}
	
	public MetaWebHookEndPointDefinition(String url) {
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
	
	public String getHeaderAuthCustomTypeName() {
		return headerAuthCustomTypeName;
	}

	public void setHeaderAuthCustomTypeName(String headerAuthCustomTypeName) {
		this.headerAuthCustomTypeName = headerAuthCustomTypeName;
	}

	@Override
	public WebHookEndPointRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new WebHookEndPointRuntime();
	}

	@Override
	public RootMetaData copy() {
		return ObjectUtil.deepCopy(this);
	}


	public String getHmacHashHeader() {
		return hmacHashHeader;
	}

	public void setHmacHashHeader(String hmacHashHeader) {
		this.hmacHashHeader = hmacHashHeader;
	}


	public boolean isHmacEnabled() {
		return hmacEnabled;
	}

	public void setHmacEnabled(boolean hmacEnabled) {
		this.hmacEnabled = hmacEnabled;
	}


	public class WebHookEndPointRuntime extends BaseMetaDataRuntime{
		private GroovyTemplate urlTemplate;
		private String hmacKey;
		private String headerAuthToken;

		public WebHookEndPointRuntime() {
			super();
			
			WebHookEndPointService service = ServiceRegistry.getRegistry().getService(WebHookEndPointService.class);
			int tenantId = ExecuteContext.getCurrentContext().getTenantContext().getTenantId();

			hmacKey = service.getHmacTokenById(tenantId, getId());
			if ("WHBA".equals(getHeaderAuthType())) {
				headerAuthToken = service.getBasicTokenById(tenantId, getId());
			} else if ("WHBT".equals(getHeaderAuthType())) {
				headerAuthToken = service.getBearerTokenById(tenantId, getId());
			} else if ("WHCT".equals(getHeaderAuthType())) {
				headerAuthToken = service.getCustomTokenById(tenantId, getId());
			}

			try {
				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				urlTemplate = GroovyTemplateCompiler.compile(getUrl(), "WebHookTemplate_Subscriber_" + getName() + "_" + id, (GroovyScriptEngine) se);
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}
		
		@Override
		public MetaWebHookEndPointDefinition getMetaData() {
			return MetaWebHookEndPointDefinition.this;
		}
		
		public GroovyTemplate getUrlTemplate() {
			return urlTemplate;
		}
		public String getHmacKey() {
			return hmacKey;
		}
		public String getHeaderAuthToken() {
			return headerAuthToken;
		}
		public String getAuthType() {
			return headerAuthType;
		}
		public String getHmacHashHeader() {
			return hmacHashHeader;
		}
		public String getHeaderAuthCustomTypeName() {
			return headerAuthCustomTypeName;
		}
		public boolean isHmacEnabled() {
			return hmacEnabled;
		}
	}


}
