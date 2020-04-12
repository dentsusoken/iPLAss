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
 * WebHookを表すクラス、WebHookの送り情報などを記録する
 */
package org.iplass.mtp.webhook;

import java.util.List;

import org.iplass.mtp.webhook.endpoint.WebhookEndPoint;

/**
 * @author lisf06
 */

public class WebHook  {

	/** 送る内容 */
	private String contentType;
	private String payloadContent;

	/** 送るメソッド */
	private String httpMethod;

	private WebHookResponseHandler resultHandler;
	private String urlQuery;
	private String headerAuthTypeName;

	private List<WebHookHeader> headers;

	/** エンドポイント */
	private WebhookEndPoint webhookEndPoint;


	public List<WebHookHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<WebHookHeader> headers) {
		this.headers = headers;
	}
	
	public void addHeader(WebHookHeader header) {
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
	public WebHookResponseHandler getResultHandler() {
		return resultHandler;
	}

	public void setResultHandler(WebHookResponseHandler resultHandler) {
		this.resultHandler = resultHandler;
	}

	public String getPayloadContent() {
		return payloadContent;
	}

	public void setPayloadContent(String payloadContent) {
		this.payloadContent = payloadContent;
	}

	public WebhookEndPoint getWebhookEndPoint() {
		return webhookEndPoint;
	}

	public void setWebhookEndPoint(WebhookEndPoint webhookEndPoint) {
		this.webhookEndPoint = webhookEndPoint;
	}
}
