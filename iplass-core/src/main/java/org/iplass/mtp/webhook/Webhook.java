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

/**
 * Webhookを表すクラス
 */
package org.iplass.mtp.webhook;

import java.util.List;

import org.iplass.mtp.webhook.endpoint.WebhookEndpoint;

/**
 * @author lisf06
 */

public class Webhook  {

	/** 送る内容 */
	private String contentType;
	private String payloadContent;

	/** 送るメソッド */
	private String httpMethod;

	private WebhookResponseHandler resultHandler;
	private String urlQuery;
	private String headerAuthTypeName;

	private List<WebhookHeader> headers;

	/** エンドポイント */
	private WebhookEndpoint webhookEndpoint;


	public List<WebhookHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<WebhookHeader> headers) {
		this.headers = headers;
	}
	
	public void addHeader(WebhookHeader header) {
		headers.add(header);
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUrlQuery() {
		return urlQuery;
	}

	public void setUrlQuery(String urlQuery) {
		this.urlQuery = urlQuery;
	}
	public String getHeaderAuthTypeName() {
		return headerAuthTypeName;
	}

	public void setHeaderAuthTypeName(String headerAuthTypeName) {
		this.headerAuthTypeName = headerAuthTypeName;
	}
	public WebhookResponseHandler getResultHandler() {
		return resultHandler;
	}

	public void setResultHandler(WebhookResponseHandler resultHandler) {
		this.resultHandler = resultHandler;
	}

	public String getPayloadContent() {
		return payloadContent;
	}

	public void setPayloadContent(String payloadContent) {
		this.payloadContent = payloadContent;
	}

	public WebhookEndpoint getWebhookEndpoint() {
		return webhookEndpoint;
	}

	public void setWebhookEndpoint(WebhookEndpoint webhookEndpoint) {
		this.webhookEndpoint = webhookEndpoint;
	}
}
