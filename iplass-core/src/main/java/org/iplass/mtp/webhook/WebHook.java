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
import org.iplass.mtp.impl.webhook.endpointaddress.MetaWebEndPointDefinition.WebEndPointRuntime;
import org.iplass.mtp.webhook.template.definition.WebHookHeaderDefinition;

/**
 * @author lisf06
 */

public class WebHook  {

	/** このwebhookの名前 */
	private String name;

	/** 送る内容 */
	private String contentType;
	
	/** 送るメソッド */
	private String httpMethod;

	private List<WebEndPointRuntime> webHookEndPointRuntimeList;

	private WebHookResponseHandler resultHandler;
	private String urlQuery;
	private String headerAuthTypeName;

	/** headers */
	private List<WebHookHeaderDefinition> headers;
	private String tokenHeader;
	

	//---------------------------------
	/**　同期非同期　*/
	private boolean synchronous;
	
	/**
	 * 記録用
	 * */
	private String TemplateName;
	
	/** 記録用のid */
	private int webHookId;
	//----------------------------------

	public String getTemplateName() {
		if (TemplateName == null) {
			return "";
		}
		return TemplateName;
	}

	public void setTemplateName(String templateName) {
		TemplateName = templateName;
	}

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWebHookId() {
		return webHookId;
	}

	public void setWebHookId(int webHookId) {
		this.webHookId = webHookId;
	}

	public List<WebHookHeaderDefinition> getHeaders() {
		return headers;
	}

	public void setHeaders(List<WebHookHeaderDefinition> headers) {
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

	public List<WebEndPointRuntime> getWebHookEndPointRuntimeList() {
		return webHookEndPointRuntimeList;
	}

	public void setWebHookEndPointRuntimeList(List<WebEndPointRuntime> webHookEndPointRuntimeList) {
		this.webHookEndPointRuntimeList = webHookEndPointRuntimeList;
	}

}
