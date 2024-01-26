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

/**
 * <% if (doclang == "ja") {%>
 * sendWebhookが成功したらこのHandlerを呼んでResponseを処理する
 * <%} else {%>
 * Once the Webhook was sent, this handler will be called to handle the response.
 * <%}%>
 */
public interface WebhookResponseHandler {
	void handleResponse(WebhookResponse response);
}
