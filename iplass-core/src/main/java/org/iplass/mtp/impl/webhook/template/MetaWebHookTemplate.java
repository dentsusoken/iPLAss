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
package org.iplass.mtp.impl.webhook.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.WebHookHeader;
import org.iplass.mtp.webhook.template.definition.WebHookHeaderDefinition;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;

@XmlRootElement
public class MetaWebHookTemplate extends BaseRootMetaData implements DefinableMetaData<WebHookTemplateDefinition> {

	private static final long serialVersionUID = 6383360434482999137L;
	
	/** webHook 内容部分 */
	private String contentType;
	private String webHookContent;

	private String sender;
	private String addressUrl;
	private String httpMethod;

	private List<MetaWebHookHeader> headers;
	
	private String urlQuery;

	/**　同期非同期　*/
	private boolean synchronous;


	
	@Override
	public WebHookTemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new WebHookTemplateRuntime();
	}

	@Override
	public MetaWebHookTemplate copy() {
		return ObjectUtil.deepCopy(this);
	}

	//Definition → Meta
	@Override
	public void applyConfig(WebHookTemplateDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		description = definition.getDescription();
		
		contentType = definition.getContentType();
		webHookContent = definition.getWebHookContent();
		
		addressUrl = definition.getAddressUrl();
		sender = definition.getSender();
		
		httpMethod = definition.getHttpMethod();
		synchronous = definition.isSynchronous();
		urlQuery = definition.getUrlQuery();
		
		ArrayList<MetaWebHookHeader> newHeaders = new ArrayList<MetaWebHookHeader>();
		if (definition.getHeaders()!=null) {
			for (WebHookHeaderDefinition headerDefinition: definition.getHeaders()) {
				MetaWebHookHeader temp = new MetaWebHookHeader();
				temp.applyConfig(headerDefinition);
				newHeaders.add(temp);
			}
		}
		headers = newHeaders;
	}



	//Meta → Definition
	@Override
	public WebHookTemplateDefinition currentConfig() {
		WebHookTemplateDefinition definition = new WebHookTemplateDefinition();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		
		definition.setContentType(contentType);
		definition.setWebHookContent(webHookContent);
		definition.setUrlQuery(urlQuery);
		
		definition.setAddressUrl(addressUrl);
		definition.setSender(sender);

		definition.setHttpMethod(httpMethod);
		definition.setSynchronous(synchronous);
		
		ArrayList<WebHookHeaderDefinition> newHeaders = new ArrayList<WebHookHeaderDefinition>();
		if (headers!=null) {
			for (MetaWebHookHeader metaHeader: headers) {
				newHeaders.add(metaHeader.currentConfig());
			}
		}
		definition.setHeaders(newHeaders);
		
		return definition;
	}
	
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getAddressUrl() {
		return addressUrl;
	}

	public void setAddressUrl(String addressUrl) {
		this.addressUrl = addressUrl;
	}

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
	}

	public List<MetaWebHookHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<MetaWebHookHeader> headers) {
		this.headers = headers;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	public String getContentType() {
		if (contentType == null) {
			contentType = "";
		}
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getWebHookContent() {
		if (webHookContent == null) {
			webHookContent = "";
		}
		return webHookContent;
	}

	public String getUrlQuery() {
		if (urlQuery == null) {
			urlQuery = "";
		}
		return urlQuery;
	}

	public void setUrlQuery(String urlQuery) {
		this.urlQuery = urlQuery;
	}

	public void setWebHookContent(String webHookContent) {
		this.webHookContent = webHookContent;
	}

	public class WebHookTemplateRuntime extends BaseMetaDataRuntime {
		private GroovyTemplate contentTemplate;
		private GroovyTemplate urlQueryTemplate;
		
		public WebHookTemplateRuntime() {
			super();
			try {
				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				contentTemplate = GroovyTemplateCompiler.compile(getWebHookContent(), "WebHookTemplate_Text" + getName(), (GroovyScriptEngine) se);
				urlQueryTemplate = GroovyTemplateCompiler.compile(getUrlQuery(), "WebHookUrlQueryTemplate_Text" + getName(), (GroovyScriptEngine) se);
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}


		public GroovyTemplate getUrlQueryTemplate() {
			return urlQueryTemplate;
		}
		
		public GroovyTemplate getContentTemplate() {
			return contentTemplate;
		}
		@Override
		public MetaWebHookTemplate getMetaData() {
			return MetaWebHookTemplate.this;
		}
		
		public WebHook createWebHook(Map<String, Object> parameter) {
			checkState();
			
			//fill up the info to webhooktemplate
			WebHook webHook = new WebHook(); 
			webHook.setName(name);
			
			ArrayList<WebHookHeader> newHeaders = new ArrayList<WebHookHeader>();
			if (headers !=null) {
				for (MetaWebHookHeader metaHeader: headers) {
					newHeaders.add(new WebHookHeader(metaHeader.getKey(),metaHeader.getValue()));
				}
			}
			webHook.setHeaders(newHeaders);
			webHook.setHttpMethod(httpMethod);
			webHook.setSynchronous(synchronous);
			webHook.setContentType(contentType);
			webHook.setUrlQuery(urlQuery);
			//common binding
			Map<String, Object> bindings = new HashMap<String, Object>();
			if (parameter != null) {
				for (Map.Entry<String, Object> e: parameter.entrySet()) {
					bindings.put(e.getKey(), e.getValue());
				}
			}
			webHook.setContentType(contentType);
			bindings.put("webHook", webHook);

			//template
			
			return webHook;
		}
		
		
		
	} 
}
