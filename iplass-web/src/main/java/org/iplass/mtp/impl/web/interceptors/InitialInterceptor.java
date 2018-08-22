/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.web.interceptors;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.transaction.LocalTransactionManager;
import org.iplass.mtp.impl.web.i18n.LangSelector;
import org.iplass.mtp.impl.web.preview.PreviewHandler;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.web.interceptor.RequestInterceptor;
import org.iplass.mtp.web.interceptor.RequestInvocation;

public class InitialInterceptor implements RequestInterceptor {

	private LangSelector lang = new LangSelector();
	private PreviewHandler preview = new PreviewHandler();

	@Override
	public void intercept(RequestInvocation invocation) {
		//initial processing
		try {
			ExecuteContext exec = ExecuteContext.getCurrentContext();
			lang.selectLangByRequest(invocation.getRequest(), exec);
			preview.init(invocation.getRequest());
			
			invocation.proceedRequest();
		} finally {
			//transaction resource cleaning
			TransactionManager tm = ManagerLocator.getInstance().getManager(TransactionManager.class);
			if (tm instanceof LocalTransactionManager) {
				((LocalTransactionManager) tm).checkAndClean();
			}
		}
	}
	
}
