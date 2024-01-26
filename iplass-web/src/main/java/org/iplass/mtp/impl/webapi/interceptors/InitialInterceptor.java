/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.interceptors;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.command.interceptor.CommandInvocation;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.transaction.LocalTransactionManager;
import org.iplass.mtp.impl.web.i18n.LangSelector;
import org.iplass.mtp.impl.web.preview.PreviewHandler;
import org.iplass.mtp.transaction.TransactionManager;

public class InitialInterceptor implements CommandInterceptor {
	
	private LangSelector lang = new LangSelector();
	private PreviewHandler preview = new PreviewHandler();
	
	@Override
	public String intercept(CommandInvocation invocation) {
		//initial processing for webapi
		try {
			ExecuteContext exec = ExecuteContext.getCurrentContext();
			lang.selectLangByRequest(invocation.getRequest(), exec);
			preview.init(invocation.getRequest());

			return invocation.proceedCommand();
		} finally {
			//transaction resource cleaning
			TransactionManager tm = ManagerLocator.getInstance().getManager(TransactionManager.class);
			if (tm instanceof LocalTransactionManager) {
				((LocalTransactionManager) tm).checkAndClean();
			}
		}
	}
	
}
