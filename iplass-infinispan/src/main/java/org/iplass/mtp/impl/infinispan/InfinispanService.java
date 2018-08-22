/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.infinispan;

import java.io.IOException;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class InfinispanService implements Service {
	
	private String configurationFile;
	private EmbeddedCacheManager cm;
	//クラスタ管理用（forming用、通信用）Cache
	private Cache<Object, Object> defaultCache;
	
	@Override
	public void init(Config config) {
		
		configurationFile = config.getValue("configurationFile");
		
		if (configurationFile == null) {
			cm = new DefaultCacheManager();
		} else {
			try {
				cm = new DefaultCacheManager(configurationFile);
			} catch (IOException e) {
				throw new ServiceConfigrationException(e);
			}
		}
		defaultCache = cm.getCache();
	}

	@Override
	public void destroy() {
		if (cm != null) {
			cm.stop();
			cm = null;
		}
	}
	
	public EmbeddedCacheManager getCacheManager() {
		return cm;
	}
	
	public Cache<Object, Object> getDefaultCache() {
		return defaultCache;
	}

}
