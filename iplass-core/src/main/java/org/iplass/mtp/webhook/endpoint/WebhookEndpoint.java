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
package org.iplass.mtp.webhook.endpoint;

/**
 * <% if (doclang == "ja") {%>
 * Webhookの宛先、エンドポイントを表するクラス。
 * <%} else {%>
 * The class representing the destination of Webhook.
 * <%}%>
 */
public class WebhookEndpoint {
	private String url;
	private WebhookAuthenticationType headerAuthorizationType;
	private String headerAuthorizationContent;
	private String hmacKey;
	private String hmacHashHeader = null;
	private String headerAuthCustomTypeName = null;

	/**
	 * <% if (doclang == "ja") {%>
	 * Webhookに付けるエンドポイントを生成する。
	 * <%} else {%>
	 * Create the WebhookEndpoint that can be set to Webhook.
	 * <%}%>
	 * @param url <%=doclang == 'ja' ? '宛先のURL': 'The URL of the destination'%>
	 */
	public WebhookEndpoint(String url){
		this.url = url;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Webhookに付けるエンドポイントを生成する。
	 * <%} else {%>
	 * Create the WebhookEndpoint that can be set to Webhook.
	 * <%}%>
	 * @param url <%=doclang == 'ja' ? '宛先のURL': 'The URL of the destination'%>
	 * @param hmacKey <%=doclang == 'ja' ? 'HMACハッシュに使う秘密キー': 'The secret key to generate HMAC hashes'%>
	 */
	public WebhookEndpoint(String url, String hmacKey){
		this.url = url;
		this.hmacKey = hmacKey;
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * Webhookに付けるエンドポイントを生成する。
	 * <%} else {%>
	 * Create the WebhookEndpoint that can be set to Webhook.
	 * <%}%>
	 * @param url <%=doclang == 'ja' ? '宛先のURL': 'The URL of the destination'%>
	 * @param headerAuthorizationType <%=doclang == 'ja' ? 'Authorizationヘッダーに付ける内容のタイプ': 'Type of the content in Authorization Header'%>
	 * @param headerAuthorizationContent <%=doclang == 'ja' ? 'Authorizationヘッダーに付ける内容、BASICの場合username:passwordの形にしてください': 'Content in Authorization Header, if it is BASIC, it should be a string in format of username:password'%>
	 */
	public WebhookEndpoint(String url, WebhookAuthenticationType headerAuthorizationType, String headerAuthorizationContent){
		this.url = url;
		this.headerAuthorizationType = headerAuthorizationType;
		this.headerAuthorizationContent = headerAuthorizationContent;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Webhookに付けるエンドポイントを生成する。
	 * <%} else {%>
	 * Create the WebhookEndpoint that can be set to Webhook.
	 * <%}%>
	 * @param url <%=doclang == 'ja' ? '宛先のURL': 'The URL of the destination'%>
	 * @param headerAuthorizationType <%=doclang == 'ja' ? 'Authorizationヘッダーに付ける内容のタイプ': 'Type of the content in Authorization Header'%>
	 * @param headerAuthorizationContent <%=doclang == 'ja' ? 'Authorizationヘッダーに付ける内容、BASICの場合username:passwordの形にしてください': 'Content in Authorization Header, if it is BASIC, it should be a string in format of username:password'%>
	 * @param hmacKey <%=doclang == 'ja' ? 'HMACハッシュに使う秘密キー': 'The secret key to generate HMAC hashes'%>
	 */
	public WebhookEndpoint(String url, WebhookAuthenticationType headerAuthorizationType, String headerAuthorizationContent, String hmacKey){
		this.url = url;
		this.headerAuthorizationType = headerAuthorizationType;
		this.headerAuthorizationContent = headerAuthorizationContent;
		this.hmacKey = hmacKey;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * BASIC認証を設置する。既に設置したAuthorization内容は破棄される。
	 * <%} else {%>
	 * Set the BASIC Authorization. the existing Authorization content will be dropped.
	 * <%}%>
	 * @param userName <%=doclang == 'ja' ? 'ユーザー名': 'User name'%>	 
	 * @param password <%=doclang == 'ja' ? 'パスワード': 'Password'%>
	 */
	public void setBasicAuthorization(String userName,String password) {
		this.headerAuthorizationType = WebhookAuthenticationType.BASIC;
		this.headerAuthorizationContent = userName + ":" + password;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * BEARER認証を設置する。既に設置したAuthorization内容は破棄される。
	 * <%} else {%>
	 * Set the BEARER Authorization. the existing Authorization content will be dropped.
	 * <%}%>
	 * @param bearerToken <%=doclang == 'ja' ? 'BEARERトークン': 'BEARER token'%>
	 */
	public void setBearerAuthorization(String bearerToken) {
		this.headerAuthorizationType = WebhookAuthenticationType.BEARER;
		this.headerAuthorizationContent = bearerToken;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * カスタム認証を設置する。既に設置したAuthorization内容は破棄される。
	 * <%} else {%>
	 * Set the Custom Authorization. the existing Authorization content will be dropped.
	 * <%}%>
	 * @param schemeName <%=doclang == 'ja' ? 'Authorizationヘッダーの内容に使うスキーム名': 'The scheme name to be used in Authorization header'%>
	 * @param customToken <%=doclang == 'ja' ? 'カスタムトークン': 'Custom Token'%>
	 */
	public void setCustomAuthorization(String schemeName, String customToken) {
		this.headerAuthorizationType = WebhookAuthenticationType.CUSTOM;
		this.headerAuthorizationContent = customToken;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Urlを取得する。
	 * <%} else {%>
	 * Get the Url.
	 * <%}%>
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Urlを設置する。
	 * <%} else {%>
	 * Set the custom headers.
	 * <%}%>
	 * @param url <%=doclang == 'ja' ? 'URL': 'URL'%>
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Authorizationヘッダーのタイプを取得する。
	 * <%} else {%>
	 * Get the type used in Authorization header.
	 * <%}%>
	 */
	public WebhookAuthenticationType getHeaderAuthorizationType() {
		return headerAuthorizationType;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Authorizationヘッダーのタイプを設置する。
	 * <%} else {%>
	 * Set the type used in Authorization header.
	 * <%}%>
	 * @param headerAuthorizationType <%=doclang == 'ja' ? 'タイプ': 'Type'%>
	 */
	public void setHeaderAuthorizationType(WebhookAuthenticationType headerAuthorizationType) {
		this.headerAuthorizationType = headerAuthorizationType;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Authorizationヘッダーの内容を取得する。BASICの場合はusername:passwordの形になります。
	 * <%} else {%>
	 * Get the content from Authrization header.If it is BASIC, it will be in the format of username:password.
	 * <%}%>
	 */
	public String getHeaderAuthorizationContent() {
		return headerAuthorizationContent;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Authorizationヘッダーの内容を設置する。BASICの場合はusername:passwordの形にしてください。
	 * <%} else {%>
	 * Set the content from Authrization header.If it is BASIC, it Should be in the format of username:password.
	 * <%}%>
	 * @param headerAuthorizationContent <%=doclang == 'ja' ? '内容': 'Content'%>
	 */
	public void setHeaderAuthorizationContent(String headerAuthorizationContent) {
		this.headerAuthorizationContent = headerAuthorizationContent;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * HMAC 秘密キーを取得する。
	 * <%} else {%>
	 * Get the HMAC secret key.
	 * <%}%>
	 */
	public String getHmacKey() {
		return hmacKey;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * HMAC 秘密キーを設置する。
	 * <%} else {%>
	 * Set the HMAC secret key.
	 * <%}%>
	 * @param hmacKey <%=doclang == 'ja' ? '秘密キー': 'Secret key'%>
	 */
	public void setHmacKey(String hmacKey) {
		this.hmacKey = hmacKey;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * HMAC 認証を置くヘッダーの名前を取得する。
	 * <%} else {%>
	 * Get the header name for HMAC Verification.
	 * <%}%>
	 */
	public String getHmacHashHeader() {
		return hmacHashHeader;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * HMAC 認証を置くヘッダーの名前を設置する。
	 * <%} else {%>
	 * Set the header name for HMAC Verification.
	 * <%}%>
	 * @param hmacHashHeader <%=doclang == 'ja' ? 'ヘッダー名': 'Header name'%>
	 */
	public void setHmacHashHeader(String hmacHashHeader) {
		this.hmacHashHeader = hmacHashHeader;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * カスタムトークンを使用する場合のスキーム名を取得する。
	 * <%} else {%>
	 * Set the custom headers.
	 * <%}%>
	 */
	public String getHeaderAuthCustomTypeName() {
		return headerAuthCustomTypeName;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * カスタムトークンを使用する場合のスキーム名を設置する。例:BASIC username:passwordの「BASIC」部分をカスタマイズする時に使う
	 * <%} else {%>
	 * Set the custom Token Scheme. It is the "BASIC" part of [Authorization: BASIC "username:password"]
	 * <%}%>
	 * @param headerAuthCustomTypeName <%=doclang == 'ja' ? 'Authorizationヘッダーの内容に使うスキーム名': 'The scheme name to be used in Authorization header'%>
	 */
	public void setHeaderAuthCustomTypeName(String headerAuthCustomTypeName) {
		this.headerAuthCustomTypeName = headerAuthCustomTypeName;
	}
	
}
