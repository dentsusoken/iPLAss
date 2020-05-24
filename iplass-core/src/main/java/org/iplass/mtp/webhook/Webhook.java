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
 * <% if (doclang == "ja") {%>
 * 送るウェッブフックを表します。
 * <%} else {%>
 * Representing the Webhook to be dispatched.
 * <%}%>
 * 
 */
public class Webhook  {

	private String contentType;
	private String payloadContent;
	private String httpMethod;
	private WebhookResponseHandler resultHandler;
	private String urlQuery;
	private List<WebhookHeader> headers;
	private WebhookEndpoint webhookEndpoint;

	/**
	 * <% if (doclang == "ja") {%>
	 * カスタムヘッダーを取得する。
	 * <%} else {%>
	 * Get the custom headers.
	 * <%}%>
	 * 
	 */
	public List<WebhookHeader> getHeaders() {
		return headers;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * カスタムヘッダーを設置する。
	 * <%} else {%>
	 * Set the custom headers.
	 * <%}%>
	 * @param headers <%=doclang == 'ja' ? 'WebhookHeaderのリスト': 'List of WebhookHeader'%>
	 */
	public void setHeaders(List<WebhookHeader> headers) {
		this.headers = headers;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * カスタムヘッダーを追加する。
	 * <%} else {%>
	 * Add additional custom headers.
	 * <%}%>
	 * @param headers <%=doclang == 'ja' ? 'WebhookHeader object': 'WebhookHeader object'%>
	 */
	public void addHeader(WebhookHeader header) {
		headers.add(header);
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 設定したHTTP接続メソッド名を取得する。
	 * <%} else {%>
	 * Get the name of the HTTP connection method in use.
	 * <%}%>
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * HTTP connectionのメソッドをセットする、未設定ならPOSTになります。
	 * <%} else {%>
	 * Specify the HTTP connection method, if undefined, POST will be used.
	 * <%}%>
	 * @param httpMethod <%=doclang == 'ja' ? '大文字でメソッド名を設定する。例：POST、GETなと': 'Method name in uppercase. i.e. POST,GET'%>
	 *   
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 送る内容のタイプを取得する。
	 * <%} else {%>
	 * Get the content Type.
	 * <%}%>
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * <% if (doclang == "ja") {%>
	 * 送る内容のタイプを設定する。
	 * <%} else {%>
	 * Set the content Type of the dispatched content.
	 * <%}%>
	 * @param contentType <%=doclang == 'ja' ? 'Content-Typeヘッダに置く内容': 'The value for Content-Type Header'%>
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Url クエリ―を取得します。
	 * <%} else {%>
	 * Get the UrlQuery.
	 * <%}%>
	 */
	public String getUrlQuery() {
		return urlQuery;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * Url クエリ―を設置します。最初に"?" を付けてください。
	 * <%} else {%>
	 * Set the UrlQuery. Start with "?"
	 * <%}%>
	 * @param urlQuery <%=doclang == 'ja' ? 'クエリ―内容': 'Query content'%>
	 */
	public void setUrlQuery(String urlQuery) {
		this.urlQuery = urlQuery;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 結果処理のオブジェクトを取得する。
	 * <%} else {%>
	 * Get the result handler object.
	 * <%}%>
	 */
	public WebhookResponseHandler getResultHandler() {
		return resultHandler;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 結果処理のオブジェクトを設置する。
	 * <%} else {%>
	 * Set the result handler object.
	 * <%}%>
	 * @param resultHandler <%=doclang == 'ja' ? 'WebhookResponseHandlerオブジェクト': 'WebhookResponseHandler object'%>
	 */
	public void setResultHandler(WebhookResponseHandler resultHandler) {
		this.resultHandler = resultHandler;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 送る内容を取得する。
	 * <%} else {%>
	 * Get the payload content.
	 * <%}%>
	 */
	public String getPayloadContent() {
		return payloadContent;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 送る内容を設置する。
	 * <%} else {%>
	 * Set the payload content.
	 * <%}%>
	 * @param payloadContent <%=doclang == 'ja' ? 'Content-Typeフォーマットに応じた内容': 'The payload with the format indicated in Content-Type'%>
	 */
	public void setPayloadContent(String payloadContent) {
		this.payloadContent = payloadContent;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 宛先クラスWebhookEndpointオブジェクトを取得する。
	 * <%} else {%>
	 * Get the Destination class WebhookEndpoint object.
	 * <%}%>
	 */
	public WebhookEndpoint getWebhookEndpoint() {
		return webhookEndpoint;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 宛先を設置する。
	 * <%} else {%>
	 * Set the Destination.
	 * <%}%>
	 * @param WebhookEndpoint <%=doclang == 'ja' ? 'WebhookEndpoint オブジェクト': 'WebhookEndpoint object'%>
	 */
	public void setWebhookEndpoint(WebhookEndpoint webhookEndpoint) {
		this.webhookEndpoint = webhookEndpoint;
	}
}
