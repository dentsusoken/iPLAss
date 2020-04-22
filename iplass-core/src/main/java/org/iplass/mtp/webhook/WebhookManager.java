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

/**
 * @author lisf06
 *
 */
public interface WebhookManager extends Manager {
	
	/** Webhookオブジェを取得（作る） */
	Webhook createWebhook(String webhookDefinitionName, Map<String, Object> binding, String endpointDefinitionName);
	Webhook getEmptyWebhook();
	Webhook getWebhookByName(String webhookDefinitionName, Map<String, Object> binding);

	/** Endpointオブジェを取得（作る） */
	WebhookEndpoint getEndpointByName(String definitionName, Map<String, Object> binding);
	
	/** 送る */
	void sendWebhookAsync(Webhook wh);
	void sendWebhookSync(Webhook wh);

	/** resultHandlerを取得（作る） */
	WebhookResponseHandler getResponseHandler(String handlerName);
}
