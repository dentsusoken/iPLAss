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

package org.iplass.mtp.impl.core.config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.iplass.mtp.runtime.EntryPointBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @deprecated use {@link EntryPointBuilder}
 */
@Deprecated
public class ServiceRegistryInitializer {

	private static Logger logger = LoggerFactory.getLogger(ServiceRegistryInitializer.class);

	public static final String CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME = BootstrapProps.CONFIG_FILE_NAME;
	public static final String CRYPT_CONFIG_FILE_NAME_SYSTEM_PROPERTY_NAME = BootstrapProps.CRYPT_CONFIG_FILE_NAME;
	public static final String SERVER_ENV_PROP_FILE_NAME = BootstrapProps.SERVER_ENV_PROP_FILE_NAME;

	public static final String DEFAULT_CONFIG_FILE_NAME = BootstrapProps.DEFAULT_CONFIG_FILE_NAME;

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
		return BootstrapProps.getInstance().getProperty(BootstrapProps.SERVER_ENV_PROP_FILE_NAME);
	}

	public static void setServerEnvFileName(String serverEnvFileName) {
		synchronized (ServiceRegistryInitializer.class) {
			String val = getServerEnvFileName();
			if (val != null && !val.equals(serverEnvFileName)) {
				logger.warn("ServerEnvFileName already set to " + val + ". If ServiceRegistry already inited, you need to call ServiceRegistry's reInit method.");
			}
			BootstrapProps.getInstance().setProperty(BootstrapProps.SERVER_ENV_PROP_FILE_NAME, serverEnvFileName);
		}
	}

	public static boolean replaceConfigFileName(String configFileName) {
		synchronized (ServiceRegistryInitializer.class) {
			String val = getConfigFileName();
			if (!configFileName.equals(val)) {
				logger.debug("replace ServiceConfigFileName from " + val + " to " + configFileName);
				BootstrapProps.getInstance().setProperty(BootstrapProps.CONFIG_FILE_NAME, configFileName);
				return true;
			} else {
				return false;
			}
		}
	}

	public static void setConfigFileName(String configFileName) {
		synchronized (ServiceRegistryInitializer.class) {
			String val = getConfigFileName();
			if (val != null && !val.equals(configFileName)) {
				logger.warn("ServiceConfigFileName already set to " + val + ". If ServiceRegistry already inited, you need to call ServiceRegistry's reInit method.");
			}
			logger.debug("set ServiceConfigFileName to " + configFileName);
			BootstrapProps.getInstance().setProperty(BootstrapProps.CONFIG_FILE_NAME, configFileName);
		}
	}

	public static String getConfigFileName() {
		return BootstrapProps.getInstance().getProperty(BootstrapProps.CONFIG_FILE_NAME, BootstrapProps.DEFAULT_CONFIG_FILE_NAME);
	}

	public static boolean isSetConfigFileName() {
		String val = BootstrapProps.getInstance().getProperty(BootstrapProps.CONFIG_FILE_NAME);
		return val != null && val.length() != 0;
	}

	public static String getCryptoConfigFileName() {
		return BootstrapProps.getInstance().getProperty(BootstrapProps.CRYPT_CONFIG_FILE_NAME);
	}

	public static void setCryptoConfigFileName(String cryptConfigFileName) {
		synchronized (ServiceRegistryInitializer.class) {
			String val = getCryptoConfigFileName();

			if (val != null
					&& !val.equals(cryptConfigFileName)) {
				logger.warn("CryptoConfigFileName already set to " + val + ". If ServiceRegistry already inited, you need to call ServiceRegistry's reInit method.");
			}
			BootstrapProps.getInstance().setProperty(BootstrapProps.CRYPT_CONFIG_FILE_NAME, cryptConfigFileName);
		}
	}

}
