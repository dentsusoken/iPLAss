/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webhook.endpoint;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.endpoint.WebhookAuthenticationType;
import org.iplass.mtp.webhook.endpoint.WebhookEndpoint;
import org.iplass.mtp.webhook.endpoint.definition.WebhookEndpointDefinition;

@XmlRootElement
public class MetaWebhookEndpoint extends BaseRootMetaData implements DefinableMetaData<WebhookEndpointDefinition>{

	private static final long serialVersionUID = 7029271819447338103L;
	private WebhookAuthenticationType headerAuthType;
	private String headerAuthCustomTypeName;

	/** 送り先 */
	private String url;

	private boolean hmacEnabled;
	private String hmacHashHeader;
	/** セキュリティ関連部分はtemplateManagerによってdbで管理しています */
	
	//Definition → Meta
	@Override
	public void applyConfig(WebhookEndpointDefinition definition) {
		this.name = definition.getName();
		this.displayName = definition.getDisplayName();
		this.description = definition.getDescription();
		this.url = definition.getUrl();
		this.headerAuthCustomTypeName = definition.getHeaderAuthCustomTypeName();
		this.hmacHashHeader = definition.getHmacHashHeader();
		this.hmacEnabled = definition.isHmacEnabled();
		this.headerAuthType = getByTypeCode(definition.getHeaderAuthType());
	}
	
	//Meta → Definition
	@Override
	public WebhookEndpointDefinition currentConfig() {
		WebhookEndpointDefinition definition = new WebhookEndpointDefinition();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		definition.setHeaderAuthType(getTypeCodeString(headerAuthType));
		definition.setHeaderAuthCustomTypeName(headerAuthCustomTypeName);
		definition.setUrl(url);
		definition.setHmacHashHeader(hmacHashHeader);
		definition.setHmacEnabled(hmacEnabled);
		return definition;
	}
	public MetaWebhookEndpoint() {
		
	}
	
	public MetaWebhookEndpoint(String url) {
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
	
	public WebhookAuthenticationType getHeaderAuthType() {
		return headerAuthType;
	}

	public void setHeaderAuthType(WebhookAuthenticationType headerAuthType) {
		this.headerAuthType = headerAuthType;
	}

	public String getHeaderAuthCustomTypeName() {
		return headerAuthCustomTypeName;
	}

	public void setHeaderAuthCustomTypeName(String headerAuthCustomTypeName) {
		this.headerAuthCustomTypeName = headerAuthCustomTypeName;
	}

	@Override
	public WebhookEndpointRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new WebhookEndpointRuntime();
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

	private WebhookAuthenticationType getByTypeCode(String typeCode) {
		if ("WHBA".equals(typeCode)) {
			return WebhookAuthenticationType.BASIC;
		}
		if ("WHBT".equals(typeCode)) {
			return WebhookAuthenticationType.BEARER;
		}
		if ("WHCT".equals(typeCode)) {
			return WebhookAuthenticationType.CUSTOM;
		}
		return null;
	}
	
	private String getTypeCodeString(WebhookAuthenticationType type) {
		if (type==null) {
			return null;
		} else {
			if (WebhookAuthenticationType.BASIC.equals(type)) {
				return "WHBA";
			}
			if (WebhookAuthenticationType.BEARER.equals(type)) {
				return "WHBT";
			}
			if (WebhookAuthenticationType.CUSTOM.equals(type)) {
				return "WHCT";
			}
			return null;
		}
	}
	public class WebhookEndpointRuntime extends BaseMetaDataRuntime{
		private GroovyTemplate urlTemplate;
		private String hmacKey;
		private String headerAuthToken;

		public WebhookEndpointRuntime() {
			super();
			
			WebhookEndpointService service = ServiceRegistry.getRegistry().getService(WebhookEndpointService.class);
			int tenantId = ExecuteContext.getCurrentContext().getTenantContext().getTenantId();

			hmacKey = service.getHmacTokenById(tenantId, getId());
			if (WebhookAuthenticationType.BASIC.equals(getHeaderAuthType())) {
				headerAuthToken = service.getBasicTokenById(tenantId, getId());
			} else if (WebhookAuthenticationType.BEARER.equals(getHeaderAuthType())) {
				headerAuthToken = service.getBearerTokenById(tenantId, getId());
			} else if (WebhookAuthenticationType.CUSTOM.equals(getHeaderAuthType())) {
				headerAuthToken = service.getCustomTokenById(tenantId, getId());
			}

			try {
				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				urlTemplate = GroovyTemplateCompiler.compile(getUrl(), "WebhookEndpointTemplate_" + getName() + "_UrlTemplate", (GroovyScriptEngine) se);
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}
		public WebhookEndpoint createWebhookEndpoint(Map<String, Object> parameter) {
			Map<String, Object> binding = new HashMap<String, Object>();
			if (parameter != null) {
				for (Map.Entry<String, Object> e: parameter.entrySet()) {
					binding.put(e.getKey(), e.getValue());
				}
			}
			String resultUrl = "";
			if (urlTemplate!=null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,binding);
				try {
					urlTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				resultUrl = sw.toString();
			}
			WebhookEndpoint endpoint = new WebhookEndpoint(resultUrl, getHeaderAuthType(), headerAuthToken, hmacKey);
			if (!hmacEnabled) {
				hmacKey = null;
			}
			if (hmacHashHeader!=null && !hmacHashHeader.replaceAll("\\s","").isEmpty()) {
				endpoint.setHmacHashHeader(hmacHashHeader.replaceAll("\\s",""));
			}
			if (headerAuthCustomTypeName!=null && !headerAuthCustomTypeName.replaceAll("\\s","").isEmpty()) {
				endpoint.setHeaderAuthCustomTypeName(headerAuthCustomTypeName.replaceAll("\\s",""));
			}
			return endpoint;
		}

		@Override
		public MetaWebhookEndpoint getMetaData() {
			return MetaWebhookEndpoint.this;
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
		public WebhookAuthenticationType getAuthType() {
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
