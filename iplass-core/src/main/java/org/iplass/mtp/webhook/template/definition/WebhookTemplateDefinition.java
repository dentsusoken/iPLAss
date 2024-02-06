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
package org.iplass.mtp.webhook.template.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import org.iplass.mtp.definition.Definition;

@XmlRootElement
public class WebhookTemplateDefinition implements Definition {

	private static final long serialVersionUID = 4835431145639526016L;
	
	private String name;
	private String displayName;
	private String description;
	
	/** webhook 内容部分 */
	private String contentType;
	private String webhookContent;

	private String pathAndQuery;
	private String httpMethod;

	private List<WebhookHeaderDefinition> headers;
	
	public WebhookTemplateDefinition() {
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

	public List<WebhookHeaderDefinition> getHeaders() {
		if (headers==null) {
			this.headers = new ArrayList<WebhookHeaderDefinition>();
		}
		return headers;
	}

	public void setHeaders(List<WebhookHeaderDefinition> headers) {
		this.headers = headers;
	}
	
	public void addHeaders(WebhookHeaderDefinition entry) {
		if (headers == null) {
			headers = new ArrayList<WebhookHeaderDefinition>();
		}
		this.headers.add(entry);
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

	public String getWebhookContent() {
		if (webhookContent == null) {
			webhookContent = "";
		}
		return webhookContent;
	}

	public void setWebhookContent(String webhookContent) {
		this.webhookContent = webhookContent;
	}
	
	public String getPathAndQuery() {
		return pathAndQuery;
	}

	public void setPathAndQuery(String pathAndQuery) {
		this.pathAndQuery = pathAndQuery;
	}

}

