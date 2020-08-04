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
package org.iplass.mtp.webhook;

/**
 * <% if (doclang == "ja") {%>
 * カスタムヘッダーを表するクラス。
 * <%} else {%>
 * Class of the custom headers.
 * <%}%>
 */
public class WebhookHeader {
	private String key;
	private String value;
	
	public WebhookHeader () {
		
	}

	/**
	 * @param key <%=doclang == 'ja' ? 'ヘッダー名': 'Header Name'%>
	 * @param value <%=doclang == 'ja' ? 'ヘッダーに置く内容': 'Content for this header'%>
	 */
	public WebhookHeader(String key, String value) {
		this.key=key;
		this.value=value;
	}
	/**
	 * <% if (doclang == "ja") {%>
	 * ヘッダー名を取得する。
	 * <%} else {%>
	 * Get the header name.
	 * <%}%>
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * ヘッダー名を設置する。
	 * <%} else {%>
	 * Set the header name.
	 * <%}%>
	 * @param key <%=doclang == 'ja' ? 'ヘッダー名': 'Header Name'%>
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * ヘッダーに置く内容を取得する。
	 * <%} else {%>
	 * Get the content for this header.
	 * <%}%>
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <% if (doclang == "ja") {%>
	 * ヘッダーに置く内容を設置する。
	 * <%} else {%>
	 * Set the content for this header.
	 * <%}%>
	 * @param value <%=doclang == 'ja' ? 'ヘッダー内容': 'Header value'%>
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
