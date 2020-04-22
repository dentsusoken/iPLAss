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
package org.iplass.mtp.webhook.endpoint;

public class WebhookEndpoint {
	private String url;
	private WebhookAuthenticationType headerAuthorizationType;
	private String headerAuthorizationContent;
	private String hmacKey;
	private String hmacHashHeader = null;
	private String headerAuthCustomTypeName = null;

	public WebhookEndpoint(String url){
		this.url = url;
	}
	
	public WebhookEndpoint(String url, String hmacKey){
		this.url = url;
		this.hmacKey = hmacKey;
	}
	
	/**
	 * headerAuthorizationTypeは具体的なインスタンスにしてくださいheaderAuthorizationType.BASICなと
	 * */
	public WebhookEndpoint(String url, WebhookAuthenticationType headerAuthorizationType, String headerAuthorizationContent){
		this.url = url;
		this.headerAuthorizationType = headerAuthorizationType;
		this.headerAuthorizationContent = headerAuthorizationContent;
	}
	/**
	 * headerAuthorizationTypeは具体的なインスタンスにしてくださいheaderAuthorizationType.BASICなと
	 * */
	public WebhookEndpoint(String url, WebhookAuthenticationType headerAuthorizationType, String headerAuthorizationContent, String hmacKey){
		this.url = url;
		this.headerAuthorizationType = headerAuthorizationType;
		this.headerAuthorizationContent = headerAuthorizationContent;
		this.hmacKey = hmacKey;
	}
	
	public void setBasicAuthorization(String userName,String password) {
		this.headerAuthorizationType = WebhookAuthenticationType.BASIC;
		this.headerAuthorizationContent = userName + ":" + password;
	}

	public void setBearerAuthorization(String bearerToken) {
		this.headerAuthorizationType = WebhookAuthenticationType.BEARER;
		this.headerAuthorizationContent = bearerToken;
	}

	public void setCustomAuthorization(String schemeName, String customToken) {
		this.headerAuthorizationType = WebhookAuthenticationType.CUSTOM;
		this.headerAuthorizationContent = customToken;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public WebhookAuthenticationType getHeaderAuthorizationType() {
		return headerAuthorizationType;
	}

	public void setHeaderAuthorizationType(WebhookAuthenticationType headerAuthorizationType) {
		this.headerAuthorizationType = headerAuthorizationType;
	}

	public String getHeaderAuthorizationContent() {
		return headerAuthorizationContent;
	}

	public void setHeaderAuthorizationContent(String headerAuthorizationContent) {
		this.headerAuthorizationContent = headerAuthorizationContent;
	}

	public String getHmacKey() {
		return hmacKey;
	}

	public void setHmacKey(String hmacKey) {
		this.hmacKey = hmacKey;
	}

	public String getHmacHashHeader() {
		return hmacHashHeader;
	}

	public void setHmacHashHeader(String hmacHashHeader) {
		this.hmacHashHeader = hmacHashHeader;
	}

	public String getHeaderAuthCustomTypeName() {
		return headerAuthCustomTypeName;
	}

	/**
	 * scheme name. 例:BASIC username:passwordの「BASIC」部分をカスタマイズする時に使う
	 * */
	public void setHeaderAuthCustomTypeName(String headerAuthCustomTypeName) {
		this.headerAuthCustomTypeName = headerAuthCustomTypeName;
	}
	
}
