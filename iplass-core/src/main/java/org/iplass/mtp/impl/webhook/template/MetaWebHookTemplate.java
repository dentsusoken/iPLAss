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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.template.definition.WebHookHeader;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class MetaWebHookTemplate extends BaseRootMetaData implements DefinableMetaData<WebHookTemplateDefinition> {

	private static final long serialVersionUID = 6383360434482999137L;
	private static Logger logger = LoggerFactory.getLogger(MetaWebHookTemplate.class);
	
	/** webHook 内容部分 */
	private String contentType;
	private String webHookContent;

	private String sender;
	private String addressUrl;
	private String httpMethod;

	private ArrayList<WebHookHeader> headers;
	private String tokenHeader;
	
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
		tokenHeader = definition.getTokenHeader();
		
		httpMethod = definition.getHttpMethod();
		synchronous = definition.isSynchronous();
		
		headers = definition.getHeaders();
		
		if (definition.getMetaDataId()!=this.getId()) {
			logger.warn("Definition<->Meta id mismatch. template:"+definition.getMetaDataId()+"; Meta:"+this.getId()+"\n");
		}
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
		
		definition.setAddressUrl(addressUrl);
		definition.setSender(sender);
		definition.setTokenHeader(tokenHeader);

		definition.setHttpMethod(httpMethod);
		definition.setSynchronous(synchronous);

		definition.setHeaders(headers);
		definition.setMetaDataId(id);
		
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

	public ArrayList<WebHookHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<WebHookHeader> headers) {
		this.headers = headers;
	}
	
	public String getTokenHeader() {
		return tokenHeader;
	}

	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
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

	public void setWebHookContent(String webHookContent) {
		this.webHookContent = webHookContent;
	}

	public class WebHookTemplateRuntime extends BaseMetaDataRuntime {

		public WebHookTemplateRuntime() {
			super();
			try {
				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				contentTemplate = GroovyTemplateCompiler.compile(getWebHookContent(), "WebHookTemplate_Text" + getName(), (GroovyScriptEngine) se);
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		private GroovyTemplate contentTemplate;
		
		@Override
		public MetaWebHookTemplate getMetaData() {
			return MetaWebHookTemplate.this;
		}
		
		public WebHook createWebHook(Map<String, Object> parameter) {
			checkState();
//			WebHookService ws = ServiceRegistry.getRegistry().getService(WebHookService.class);
//			ExecuteContext ex = ExecuteContext.getCurrentContext();
//			WebHookTemplate webHook = ws.createWebHook(ex.getCurrentTenant(), _charset);
			
			//fill up the info to webhooktemplate
			WebHook webHook = new WebHook(); 
			//webHook.setContent(contentBody); TODO modify with contentTemplate
			webHook.setName(name);

			webHook.setHeaders(headers);
			webHook.setTokenHeader(tokenHeader);

			webHook.setHttpMethod(httpMethod);
			webHook.setSynchronous(synchronous);
			
			//common binding
			Map<String, Object> bindings = new HashMap<String, Object>();
			if (parameter != null) {
				for (Map.Entry<String, Object> e: parameter.entrySet()) {
					bindings.put(e.getKey(), e.getValue());
				}
			}
			bindings.put("webHook", webHook);

			//groovy template
			if (contentTemplate != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,bindings);
				try {
					contentTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				webHook.setContentType(contentType);
				webHook.setWebHookContent(sw.toString());
			}
			webHook.setMetaDataId(id);
			return webHook;
		}
		
		
		
	} 
}
