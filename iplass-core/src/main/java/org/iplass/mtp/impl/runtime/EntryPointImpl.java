/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.Map;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.config.BootstrapProps;
import org.iplass.mtp.runtime.EntryPoint;
import org.iplass.mtp.runtime.Runner;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPointImpl implements EntryPoint {
	
	private static Logger logger = LoggerFactory.getLogger(EntryPointImpl.class);
	
	private static volatile EntryPointImpl instance;
	
	private ServiceRegistry serviceRegistry;
	
	public static boolean isInited() {
		return instance != null;
	}
	
	static EntryPointImpl newEntryPointImpl(Map<String, String> customProps) {
		synchronized (EntryPointImpl.class) {
			if (instance != null) {
				throw new SystemException("EntryPoint already initialized");
			}
			
			try {
				if (!BootstrapProps.init(customProps)) {
					logger.warn("BootstrapProps already initialized, so can not apply customProps:" + customProps);
				}
				instance = new EntryPointImpl();
				
			} finally {
				ExecuteContext.finContext();
			}
			
			return instance;
		}
	}
	
	private EntryPointImpl() {
		serviceRegistry = ServiceRegistry.getRegistry();
	}

	@Override
	public Runner runner() {
		return new RunnerImpl(serviceRegistry);
	}

	@Override
	public void destroy() {
		synchronized (EntryPointImpl.class) {
			if (instance == this) {
				instance = null;
				serviceRegistry.destroyAllService();
			}
		}
	}

}
