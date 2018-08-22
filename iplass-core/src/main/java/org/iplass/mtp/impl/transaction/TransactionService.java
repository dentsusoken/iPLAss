/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.transaction;

import org.iplass.mtp.Manager;
import org.iplass.mtp.impl.core.ManagerConstructor;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.TransactionManager;

public abstract class TransactionService implements Service {
	
	public static class TransactionManagerConstructor implements ManagerConstructor {
		@Override
		public Manager construct() {
			return ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager();
		}
		
	}
	
	public abstract TransactionManager getTransacitonManager();
	
}
