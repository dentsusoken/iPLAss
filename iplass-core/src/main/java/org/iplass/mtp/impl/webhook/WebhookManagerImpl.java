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
package org.iplass.mtp.impl.webhook;

import java.util.Map;
import org.iplass.mtp.impl.webhook.endpointaddress.WebhookEndpointService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.Webhook;
import org.iplass.mtp.webhook.WebhookManager;
import org.iplass.mtp.webhook.endpoint.WebhookEndpoint;

public class WebhookManagerImpl implements WebhookManager {

	private WebhookService webhookService = ServiceRegistry.getRegistry().getService(WebhookService.class);
	private WebhookEndpointService webhookEndpointService = ServiceRegistry.getRegistry().getService(WebhookEndpointService.class);


	@Override
	public Webhook createWebhook(String webhookDefinitionName, Map<String, Object> binding, String endpointDefinitionName) {
		return webhookService.generateWebhook(webhookDefinitionName, binding, endpointDefinitionName);
	}

	@Override
	public void sendWebhookAsync(Webhook webhook) {
		webhookService.sendWebhookAsync(webhook);
	}

	@Override
	public void sendWebhookSync(Webhook webhook) {
		webhookService.sendWebhookSync(webhook);
	}

	@Override
	public Webhook createWebhook(String webhookDefinitionName, Map<String, Object> binding) {
		return webhookService.getWebhookByName(webhookDefinitionName, binding);
	}

	@Override
	public WebhookEndpoint getEndpoint(String definitionName, Map<String, Object> binding) {
		return webhookEndpointService.getWebhookEndpointByDefinitionName(definitionName, binding);
	}
}
