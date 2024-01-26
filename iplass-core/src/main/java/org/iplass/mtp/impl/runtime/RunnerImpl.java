/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.runtime;

import java.util.function.Supplier;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.command.CommandInvoker;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.runtime.Runner;
import org.iplass.mtp.spi.ServiceRegistry;

public class RunnerImpl implements Runner {
	
	private ServiceRegistry sr;
	
	private String tenantUrl;
	private Integer tenantId;
	private Credential credential;
	private String lang;
	
	
	RunnerImpl(ServiceRegistry sr) {
		this.sr = sr;
	}

	@Override
	public Runner withTenant(String tenantUrl) {
		this.tenantUrl = tenantUrl;
		this.tenantId = null;
		return this;
	}

	@Override
	public Runner withTenant(Integer tenantId) {
		this.tenantId = tenantId;
		this.tenantUrl = null;
		return this;
	}
	
	@Override
	public Runner withAuth(Credential credential) {
		this.credential = credential;
		return this;
	}

	@Override
	public Runner withLang(String lang) {
		this.lang = lang;
		return this;
	}
	
	@Override
	public void run(Runnable r) {
		run(() -> {
			r.run();
			return null;
			});
	}

	@Override
	public <T> T run(Supplier<T> s) {
		TenantContextService tcs = sr.getService(TenantContextService.class);
		TenantContext tc = null;
		if (tenantUrl != null) {
			tc = tcs.getTenantContext(tenantUrl);
			if (tc == null) {
				throw new SystemException("tenant not found:tenantUrl=" + tenantUrl);
			}
		} else if (tenantId != null) {
			tc = tcs.getTenantContext(tenantId);
			if (tc == null) {
				throw new SystemException("tenant not found:tenantId=" + tenantId);
			}
		} else {
			tc = tcs.getTenantContext(tcs.getSharedTenantId());
		}
		
		try {
			ExecuteContext ec = new ExecuteContext(tc);
			ExecuteContext.initContext(new ExecuteContext(tc));
			if (lang != null) {
				ec.setLanguage(lang);
			}
			
			if (credential == null) {
				return s.get();
			} else {
				AuthService as = sr.getService(AuthService.class);
				as.login(credential);
				return as.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
					return s.get();
				});
			}
			
		} finally {
			ExecuteContext.finContext();
		}
	}
	
	@Override
	public String run(String commandName, RequestContext request) {
		return run(() -> {
			CommandInvoker invoker = ManagerLocator.manager(CommandInvoker.class);
			return invoker.execute(commandName, request);
		});
	}

}
