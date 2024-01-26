/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.util.random;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class SecureRandomService implements Service {
	
	public static final String DEFAULT_CONFIG_NAME = "default";
	
	private Map<String, SecureRandomGeneratorConfig> generatorConfigs;
	private SecureRandomGeneratorConfig defaultGeneratorConfig;

	@Override
	public void init(Config config) {
		generatorConfigs = new HashMap<>();
		for (String n: config.getNames()) {
			generatorConfigs.put(n, config.getValue(n, SecureRandomGeneratorConfig.class));
		}
		
		defaultGeneratorConfig = generatorConfigs.get(DEFAULT_CONFIG_NAME);
		if (defaultGeneratorConfig == null) {
			defaultGeneratorConfig = new SecureRandomGeneratorConfig();
		}
	}

	@Override
	public void destroy() {
	}
	
	public SecureRandomGenerator createGenerator() {
		return defaultGeneratorConfig.createGenerator();
	}
	
	public SecureRandomGenerator createGenerator(String name) {
		SecureRandomGeneratorConfig c = generatorConfigs.get(name);
		if (c == null) {
			c = defaultGeneratorConfig;
		}
		return c.createGenerator();
	}


}
