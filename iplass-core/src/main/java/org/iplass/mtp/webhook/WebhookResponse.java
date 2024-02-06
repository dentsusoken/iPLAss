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
package org.iplass.mtp.webhook;

import java.util.List;
/**
 * <% if (doclang == "ja") {%>
 * Webhookを送った後の返信を表するクラス。
 * <%} else {%>
 * The class representing the response of a dispatched Webhook.
 * <%}%>
 */
public class WebhookResponse {

	private int statusCode;
	private String reasonPhrase;
	private List<WebhookHeader> headers;
	private String contentType;
	private String contentEncoding;//nullable
	private String responseBody;
	/**
	 * <% if (doclang == "ja") {%>
	 * ステータスコードを取得.
	 * <%} else {%>
	 * Get the status code.
	 * <%}%>
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * ステータスコードを設置する。
	 * <%} else {%>
	 * Set the status code.
	 * <%}%>
	 * @param statusCode <%=doclang == 'ja' ? '適切な数字(200なと)を入れてください': 'Enter the appropriate numbers(Such as 200)'%>
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * ステータスコードに応じる説明内容。例:200 ok から「ok」の部分。
	 * <%} else {%>
	 * The reason phrase of the response. Example: the [ok] part of a typical response 200 ok.
	 * <%}%>
	 */
	public String getReasonPhrase() {
		return reasonPhrase;
	}
	/**
	 * <% if (doclang == "ja") {%>
	 * ステータスコードに応じる説明内容を設置する。
	 * <%} else {%>
	 * Set the reason phrase of the response.
	 * <%}%>
	 * @param reasonPhrase <%=doclang == 'ja' ? '説明内容': 'reason phrase'%>
	 */
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 返事のヘッダーを取得する。
	 * <%} else {%>
	 * Get the response headers.
	 * <%}%>
	 */
	public List<WebhookHeader> getHeaders() {
		return headers;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 返事のヘッダーを設置する。
	 * <%} else {%>
	 * Set the response headers.
	 * <%}%>
	 * @param headers <%=doclang == 'ja' ? 'WebhookHeaderのリスト': 'List of WebhookHeader'%>
	 */
	public void setHeaders(List<WebhookHeader> headers) {
		this.headers = headers;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 返事の内容のコンテンツタイプを取得する。
	 * <%} else {%>
	 * Get the content type of the response body.
	 * <%}%>
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 返事の内容のコンテンツタイプを設置する。
	 * <%} else {%>
	 * Set the content type of the response body.
	 * <%}%>
	 * @param contentType <%=doclang == 'ja' ? 'タイプ': 'Type'%>
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 返事の内容のコンテンツを取得する。
	 * <%} else {%>
	 * Get the content of the response body.
	 * <%}%>
	 */
	public String getResponseBody() {
		return responseBody;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 返事の内容のコンテンツを設置する。
	 * <%} else {%>
	 * Set the content of the response body.
	 * <%}%>
	 * @param responseBody <%=doclang == 'ja' ? 'コンテンツ': 'contents'%>
	 */
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 返事の内容のコンテンツのエンコードタイプを取得する。
	 * <%} else {%>
	 * Get the encode type of the response body.
	 * <%}%>
	 */
	public String getContentEncoding() {
		return contentEncoding;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * 返事の内容のコンテンツのエンコードタイプを設置する。
	 * <%} else {%>
	 * Set the encode type of the response body.
	 * <%}%>
	 * @param contentEncoding <%=doclang == 'ja' ? 'エンコード名': 'Encode type name'%>
	 */
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	
	
}
