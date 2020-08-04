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

import java.util.Map;
import org.iplass.mtp.Manager;
import org.iplass.mtp.webhook.endpoint.WebhookEndpoint;

public interface WebhookManager extends Manager {
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 定義からWebhookを作る。
	 * <%} else {%>
	 * Create the Webhook from definition.
	 * <%}%>
	 * @param webhookDefinitionName <%=doclang == 'ja' ? 'Webhook定義名': 'Webhook definition name'%>
	 * @param binding <%=doclang == 'ja' ? 'binding内容': 'Content bindings'%>
	 * @param endpointDefinitionName <%=doclang == 'ja' ? 'WebhookEndpoint定義名': 'WebhookEndpoint definition name'%>
	 */
	Webhook createWebhook(String webhookDefinitionName, Map<String, Object> binding, String endpointDefinitionName);

	/**
	 * <% if (doclang == "ja") {%>
	 * 定義からWebhookを作る。
	 * <%} else {%>
	 * Create the Webhook from definition.
	 * <%}%>
	 * @param webhookDefinitionName <%=doclang == 'ja' ? 'Webhook定義名': 'Webhook definition name'%>
	 * @param binding <%=doclang == 'ja' ? 'binding内容': 'Content bindings'%>
	 */
	Webhook createWebhook(String webhookDefinitionName, Map<String, Object> binding);

	/**
	 * <% if (doclang == "ja") {%>
	 * 名前からエンドポイントを取得する。
	 * <%} else {%>
	 * Create the WebhookEndpoint class by name.
	 * <%}%>
	 * @param definitionName <%=doclang == 'ja' ? 'WebhookEndpoint定義名': 'WebhookEndpoint definition name'%>
	 * @param binding <%=doclang == 'ja' ? 'binding内容': 'Content bindings'%>
	 */
	WebhookEndpoint getEndpoint(String definitionName, Map<String, Object> binding);

	/**
	 * <% if (doclang == "ja") {%>
	 * 非同期でWebhookを送る。
	 * <%} else {%>
	 * Send Webhook asynchronously
	 * <%}%>
	 * @param wh <%=doclang == 'ja' ? 'Webhookインスタンス': 'Webhook instance'%>
	 */
	void sendWebhookAsync(Webhook wh);

	/**
	 * <% if (doclang == "ja") {%>
	 * 同期でWebhookを送る。
	 * <%} else {%>
	 * Send Webhook synchronously
	 * <%}%>
	 * @param wh <%=doclang == 'ja' ? 'Webhookインスタンス': 'Webhook instance'%>
	 */
	void sendWebhookSync(Webhook wh);
}
