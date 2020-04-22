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
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionStatus;
import org.iplass.mtp.webhook.Webhook;
import org.iplass.mtp.webhook.WebhookManager;
import org.iplass.mtp.webhook.WebhookResponseHandler;
import org.iplass.mtp.webhook.endpoint.WebhookEndpoint;
import org.iplass.mtp.webhook.endpoint.definition.WebhookEndpointDefinitionManager;
import org.iplass.mtp.webhook.responsehandler.DefaultWebhookResponseHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebhookManagerImpl implements WebhookManager {
	
	private static Logger logger = LoggerFactory.getLogger(WebhookManagerImpl.class);

	private WebhookService webhookService = ServiceRegistry.getRegistry().getService(WebhookService.class);
	
	private void setRollbackOnly() {
		Transaction t = ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager().currentTransaction();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
			t.setRollbackOnly();
		}
	}

	@Override
	public Webhook createWebhook(String webhookDefinitionName, Map<String, Object> binding, String endpointDefinitionName) {
		return webhookService.generateWebhook(webhookDefinitionName, binding, endpointDefinitionName);
	}

	@Override
	public void sendWebhookAsync(Webhook webhook) {
		try { 
			webhookService.sendWebhookAsync(webhook);
		} catch (ApplicationException e) {
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			logger.error(e.getMessage(), e);
			throw new WebhookRuntimeException("開始処理でエラーが発生しました。", e);
		} catch (Error e) {
			setRollbackOnly();
			logger.error(e.getMessage(), e);
			throw new WebhookRuntimeException("開始処理でエラーが発生しました。", e);
		}
	}

	@Override
	public void sendWebhookSync(Webhook webhook) {
		try { 
			webhookService.sendWebhookSync(webhook);
		} catch (ApplicationException e) {
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			logger.error(e.getMessage(), e);
			throw new WebhookRuntimeException("開始処理でエラーが発生しました。", e);
		} catch (Error e) {
			setRollbackOnly();
			logger.error(e.getMessage(), e);
			throw new WebhookRuntimeException("開始処理でエラーが発生しました。", e);
		}
	}

	@Override
	public WebhookResponseHandler getResponseHandler(String handlerName) {
		WebhookResponseHandler whrh;
		if (handlerName==null||handlerName.isEmpty()) {
			whrh= new DefaultWebhookResponseHandlerImpl();
		} else {
			try {
				whrh= (WebhookResponseHandler) Class.forName(handlerName).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("The response handler: "+handlerName+" does not exist. Creating DefaultWebhookResponseHandler.");
				}
				whrh= new DefaultWebhookResponseHandlerImpl();
			}
		}
		return whrh;
	}

	@Override
	public Webhook getEmptyWebhook() {
		return new Webhook();
	}

	@Override
	public Webhook getWebhookByName(String webhookDefinitionName, Map<String, Object> binding) {
		return webhookService.getWebhookByName(webhookDefinitionName, binding);
	}

	@Override
	public WebhookEndpoint getEndpointByName(String definitionName, Map<String, Object> binding) {
		WebhookEndpointDefinitionManager manager = ManagerLocator.getInstance().getManager(WebhookEndpointDefinitionManager.class);
		return manager.getEndpointByDefinitionName(definitionName, binding);
	}
}
