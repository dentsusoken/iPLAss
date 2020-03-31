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
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionStatus;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.WebHookManager;
import org.iplass.mtp.webhook.WebHookResponseHandler;
import org.iplass.mtp.webhook.responsehandler.DefaultWebHookResponseHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.iplass.mtp.impl.webhook.WebHookRuntimeException;

public class WebHookManagerImpl implements WebHookManager {
	
	private static Logger logger = LoggerFactory.getLogger(WebHookManagerImpl.class);

	private WebHookService webHookService = ServiceRegistry.getRegistry().getService(WebHookService.class);
	
	private void setRollbackOnly() {
		Transaction t = ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager().currentTransaction();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
			t.setRollbackOnly();
		}
	}

	@Override
	public WebHook createWebHook(String webHookDefinitionName, Map<String, Object> binding, String endPointDefinitionName) {
		return webHookService.generateWebHook(webHookDefinitionName, binding, endPointDefinitionName);
	}

	@Override
	public void sendWebHookAsync(WebHook webHook) {
		try { 
			webHookService.sendWebHookAsync(webHook);
		} catch (ApplicationException e) {
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			logger.error(e.getMessage(), e);
			throw new WebHookRuntimeException("開始処理でエラーが発生しました。", e);
		} catch (Error e) {
			setRollbackOnly();
			logger.error(e.getMessage(), e);
			throw new WebHookRuntimeException("開始処理でエラーが発生しました。", e);
		}
	}

	@Override
	public void sendWebHookSync(WebHook webHook) {
		try { 
			webHookService.sendWebHookSync(webHook);
		} catch (ApplicationException e) {
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			logger.error(e.getMessage(), e);
			throw new WebHookRuntimeException("開始処理でエラーが発生しました。", e);
		} catch (Error e) {
			setRollbackOnly();
			logger.error(e.getMessage(), e);
			throw new WebHookRuntimeException("開始処理でエラーが発生しました。", e);
		}
	}

	@Override
	public WebHookResponseHandler getResponseHandler(String handlerName) {
		WebHookResponseHandler whrh;
		if (handlerName==null||handlerName.isEmpty()) {
			whrh= new DefaultWebHookResponseHandlerImpl();
		} else {
			try {
				whrh= (WebHookResponseHandler) Class.forName(handlerName).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("The response handler: "+handlerName+" does not exist. Creating DefaultWebHookResponseHandler.");
				}
				whrh= new DefaultWebHookResponseHandlerImpl();
			}
		}
		return whrh;
	}

	@Override
	public WebHook getEmptyWebHook() {
		return new WebHook();
	}



	
}
