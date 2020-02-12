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
import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate.WebHookTemplateRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionStatus;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.WebHookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.iplass.mtp.impl.webhook.WebHookRuntimeException;

public class WebHookManagerImpl implements WebHookManager {
	
	private static Logger logger = LoggerFactory.getLogger(WebHookManagerImpl.class);

	private WebHookService webHookService = ServiceRegistry.getRegistry().getService(WebHookService.class);
	
	private WebHookTemplateRuntime getWebHookTemplateRuntme(String defName) {
		WebHookTemplateRuntime runtime = webHookService.getRuntimeByName(defName);
		if (runtime == null) {
			throw new WebHookRuntimeException(defName+"is undefined.");
		}
		return runtime;
	}

	@Override
	public void sendWebHook(String webHookDefinitionName, Map<String, Object> parameters) {
		AuthContextHolder account = AuthContextHolder.getAuthContext();//check if is permit, TODO:
		//boolean isPermitted = account.checkPermission(whPermission);
		//if permited
		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		WebHookTemplateRuntime runtime = getWebHookTemplateRuntme(webHookDefinitionName);
		if (runtime == null) {
			throw new SystemException("WebHookTemplate:" + webHookDefinitionName + " not found");
		}
		try {
			WebHook temp = runtime.createWebHook(parameters);
//			if (temp.getSubscribers() == null || temp.getSubscribers().isEmpty()) {//テストや、新規の時よくあるケース
//				logger.warn("The WebHook:"+ webHookDefinitionName + " was attempted without valid receiver url.");
//				return;
//			}
			temp.setTemplateName(webHookDefinitionName);
			webHookService.sendWebHook(tenant, temp);
			
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
	private void setRollbackOnly() {
		Transaction t = ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager().currentTransaction();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
			t.setRollbackOnly();
		}
	}

	@Override
	public WebHook createWebHook(String webHookDefinitionName, Map<String, Object> binding) {
		WebHookTemplateRuntime runtime = getWebHookTemplateRuntme(webHookDefinitionName);
		if (runtime == null) {
			throw new SystemException("WebHookTemplate:" + webHookDefinitionName + " not found");
		}
		WebHook temp = runtime.createWebHook(binding);
		temp.setTemplateName(webHookDefinitionName);
		return temp;
		
	}

	@Override
	public void sendWebHook(WebHook webHook) {
		try { 
//			if (webHook.getSubscribers() == null || webHook.getSubscribers().isEmpty()) {//テストや、新規の時よくあるケース
//				logger.warn("The WebHook: was attempted without valid receiver url.");
//				return;
//			}
			Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			if (webHook.isSynchronous()) {
				webHookService.sendWebHook(tenant, webHook);
			} else {
				webHookService.sendWebHookAsync(tenant, webHook);
			}
			
			
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

}
