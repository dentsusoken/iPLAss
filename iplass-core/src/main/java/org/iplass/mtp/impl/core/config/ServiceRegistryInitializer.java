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

package org.iplass.mtp.impl.core.config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRegistryInitializer {

	private static Logger logger = LoggerFactory.getLogger(ServiceRegistryInitializer.class);

	public static final String CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME = "mtp.config";
	public static final String CRYPT_CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME = "mtp.config.crypt";
	public static final String SERVER_ENV_PROP_FILE_NAME = "mtp.server.env";

	public static final String DEFAULT_CONFIG_FILE_NAME = "/mtp-service-config.xml";

	private static volatile String configFileName;
	private static volatile String cryptConfigFileName;
	private static volatile String serverEnvFileName;
	private static volatile CopyOnWriteArrayList<String> configPreprocessorClassNames
			= new CopyOnWriteArrayList<>(new String[]{GroovyPreprocessor.class.getName(), DecodePreprocessor.class.getName()});
	
	public static List<String> getConfigPreprocessorClassNames() {
		return configPreprocessorClassNames;
	}
	
	public static void setConfigPreprocessorClassNames(List<String> configPreprocessorClassNames) {
		if (configPreprocessorClassNames == null) {
			ServiceRegistryInitializer.configPreprocessorClassNames = null;
		} else {
			ServiceRegistryInitializer.configPreprocessorClassNames = new CopyOnWriteArrayList<>(configPreprocessorClassNames);
		}
	}
	
	public static String getServerEnvFileName() {
		synchronized (ServiceRegistryInitializer.class) {
			if (serverEnvFileName == null) {
				serverEnvFileName = System.getProperty(SERVER_ENV_PROP_FILE_NAME);
			}
			return serverEnvFileName;
		}
	}

	public static void setServerEnvFileName(String serverEnvFileName) {
		synchronized (ServiceRegistryInitializer.class) {
			if (ServiceRegistryInitializer.serverEnvFileName != null
					&& !ServiceRegistryInitializer.serverEnvFileName.equals(serverEnvFileName)) {
				logger.warn("ServerEnvFileName already set to " + ServiceRegistryInitializer.serverEnvFileName + ". If ServiceRegistry already inited, you need to call ServiceRegistry's reInit method.");
			}
			ServiceRegistryInitializer.serverEnvFileName = serverEnvFileName;
		}
	}

	public static boolean replaceConfigFileName(String configFileName) {
		synchronized (ServiceRegistryInitializer.class) {
			if (!configFileName.equals(ServiceRegistryInitializer.configFileName)) {
				logger.debug("replace ServiceConfigFileName from " + ServiceRegistryInitializer.configFileName + " to " + configFileName);
				ServiceRegistryInitializer.configFileName = configFileName;
				return true;
			} else {
				return false;
			}
		}
	}

	public static void setConfigFileName(String configFileName) {
		synchronized (ServiceRegistryInitializer.class) {
			if (ServiceRegistryInitializer.configFileName != null
					&& !ServiceRegistryInitializer.configFileName.equals(configFileName)) {
				logger.warn("ServiceConfigFileName already set to " + ServiceRegistryInitializer.configFileName + ". If ServiceRegistry already inited, you need to call ServiceRegistry's reInit method.");
			}
			logger.debug("set ServiceConfigFileName to " + configFileName);
			ServiceRegistryInitializer.configFileName = configFileName;
		}
	}

	public static String getConfigFileName() {
		synchronized (ServiceRegistryInitializer.class) {
			if (configFileName == null) {
				configFileName = ServerEnv.getInstance().getProperty(CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME, DEFAULT_CONFIG_FILE_NAME);
				logger.debug("set ServiceConfigFileName to " + configFileName);
			}
			return configFileName;
		}
	}

	public static boolean isSetConfigFileName() {
		synchronized (ServiceRegistryInitializer.class) {
			return configFileName != null && configFileName.length() != 0;
		}
	}

	public static String getCryptoConfigFileName() {
		synchronized (ServiceRegistryInitializer.class) {
			if (cryptConfigFileName == null) {
				cryptConfigFileName = ServerEnv.getInstance().getProperty(CRYPT_CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME);
			}
			return cryptConfigFileName;
		}
	}

	public static void setCryptoConfigFileName(String cryptConfigFileName) {
		synchronized (ServiceRegistryInitializer.class) {
			if (ServiceRegistryInitializer.cryptConfigFileName != null
					&& !ServiceRegistryInitializer.cryptConfigFileName.equals(cryptConfigFileName)) {
				logger.warn("CryptoConfigFileName already set to " + ServiceRegistryInitializer.cryptConfigFileName + ". If ServiceRegistry already inited, you need to call ServiceRegistry's reInit method.");
			}
			ServiceRegistryInitializer.cryptConfigFileName = cryptConfigFileName;
		}
	}

}
