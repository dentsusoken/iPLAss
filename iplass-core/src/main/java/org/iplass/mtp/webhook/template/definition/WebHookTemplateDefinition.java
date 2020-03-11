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
package org.iplass.mtp.webhook.template.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import org.iplass.mtp.definition.Definition;

@XmlRootElement
public class WebHookTemplateDefinition implements Definition {

	private static final long serialVersionUID = 4835431145639526016L;
	private String metaDataId;
	
	private String name;
	private String displayName;
	private String description;
	
	/** webHook 内容部分 */
	private String contentType;
	private String webHookContent;

	private String urlQuery;

	private String sender;
	private String addressUrl;
	private String tokenHeader;//セキュリテぃトークンのヘッダー名を設置
	private String httpMethod;

	private boolean synchronous;

	/** headers */
	private List<WebHookHeaderDefinition> headers;
	
	public WebHookTemplateDefinition() {
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

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
	}
	
	public List<WebHookHeaderDefinition> getHeaders() {
		if (headers==null) {
			this.headers = new ArrayList<WebHookHeaderDefinition>();
		}
		return headers;
	}

	public void setHeaders(List<WebHookHeaderDefinition> headers) {
		this.headers = headers;
	}
	
	public void addHeaders(WebHookHeaderDefinition entry) {
		if (headers == null) {
			headers = new ArrayList<WebHookHeaderDefinition>();
		}
		this.headers.add(entry);
	}
	
	public String getMetaDataId() {
		return metaDataId;
	}

	public void setMetaDataId(String id) {
		this.metaDataId = id;
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
	
	public String getUrlQuery() {
		return urlQuery;
	}

	public void setUrlQuery(String urlQuery) {
		this.urlQuery = urlQuery;
	}

}

