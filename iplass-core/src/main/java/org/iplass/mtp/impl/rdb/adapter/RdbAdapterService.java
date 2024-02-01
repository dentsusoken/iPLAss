/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.adapter;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RdbAdapterService implements Service {
	private static Logger logger = LoggerFactory.getLogger(RdbAdapterService.class);
	
	private RdbAdapter adapter;
	private Map<String, RdbAdapter> adapterMap;

	public RdbAdapter getRdbAdapter() {
		return adapter;
	}
	
	public RdbAdapter getRdbAdapter(String name) {
		if (name == null) {
			return adapter;
		}
		return adapterMap.get(name);
	}
	
	public void destroy() {
		adapter = null;
		adapterMap = null;
	}
	
	public void init(Config config) {
		adapterMap = new HashMap<>();
		if (config.getNames() != null) {
			for (String name: config.getNames()) {
				if (name.equals("adapter")) {
					adapter = (RdbAdapter) config.getBean("adapter");
				} else {
					adapterMap.put(name, (RdbAdapter) config.getBean(name));
				}
			}
		}
		if (adapter == null) {
			throw new RuntimeException("adapter not specify.");
		}
		if (adapter instanceof NoopRdbAdapter) {
			logger.error("It is necessary to set a value other than NoopRdbAdapter to adapter.");
		}
	}

}
