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

import java.util.ArrayList;
import java.util.Map;

import org.iplass.mtp.webhook.template.definition.WebHookHeader;

/**
 * @author lisf06
 */

public class WebHook  {
	
	/** このwebhookの名前 */
	private String name;

	/** 送る内容 */
	private String contentType;
	private String webHookContent;
	
	/** 送るメソッド */
	private String httpMethod;

	/**definition name, 後でserviceで情報をとる*/
	private ArrayList<String> endPoints;
	private String resultHandler;

	
	/** headers */
	private ArrayList<WebHookHeader> headers;
	private String tokenHeader;

	Map<String, Object> binding;

	String responseHandler;
	//---------------------------------
	/**　同期非同期　*/
	private boolean synchronous;
	
	/**
	 * 記録用
	 * */
	private String TemplateName;
	
	/**
	 * ｄｂ接続用
	 * */
	private String metaDataId;
	
	/** 記録用のid */
	private int webHookId;
	//----------------------------------
	public String getMetaDataId() {
		return metaDataId;
	}

	public void setMetaDataId(String metaDataId) {
		this.metaDataId = metaDataId;
	}

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

	public ArrayList<WebHookHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<WebHookHeader> headers) {
		this.headers = headers;
	}

	public Map<String, Object> getBinding() {
		return binding;
	}

	public void setBinding(Map<String, Object> binding) {
		this.binding = binding;
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

	public String getWebHookContent() {
		return webHookContent;
	}

	public void setWebHookContent(String webHookContent) {
		this.webHookContent = webHookContent;
	}

	public ArrayList<String> getEndPoints() {
		return endPoints;
	}

	public void setEndPoints(ArrayList<String> endPoints) {
		this.endPoints = endPoints;
	}

	public String getResultHandler() {
		return resultHandler;
	}

	public void setResultHandler(String resultHandler) {
		this.resultHandler = resultHandler;
	}

}
