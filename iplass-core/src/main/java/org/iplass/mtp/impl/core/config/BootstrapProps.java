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

package org.iplass.mtp.impl.core.config;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.iplass.mtp.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootstrapProps {
	public static final String SERVER_ENV_PROP_FILE_NAME = "mtp.server.env";
	
	public static final String SERVER_ROLES = "mtp.server.myserverroles";
	public static final String SERVER_ID = "mtp.server.myserverid";
	public static final String SERVER_NAME = "mtp.server.myservername";
	public static final String NETWORK_INTERFACE_NAME = "mtp.server.myinterfacename";
	
	public static final String CONFIG_FILE_NAME = "mtp.config";
	public static final String CONFIG_LOADER_CLASS_NAME = "mtp.config.loader";
	public static final String CONFIG_PREPROCESSORS_CLASS_NAME = "mtp.config.preprocessors";
	public static final String CRYPT_CONFIG_FILE_NAME = "mtp.config.crypt";

	public static final String DEFAULT_CONFIG_FILE_NAME = "/mtp-service-config.xml";
	public static final String DEFAULT_CONFIG_PREPROCESSORS_CLASS_NAME = 
			GroovyPreprocessor.class.getName() + "," + DecodePreprocessor.class.getName();
	public static final String DEFAULT_CONFIG_LOADER_CLASS_NAME = LocalResourceConfigLoader.class.getName();
	
	
	private static Logger logger = LoggerFactory.getLogger(BootstrapProps.class);
	
	private static volatile BootstrapProps bootstrapProps;
	
	private final Map<String, String> props;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public static BootstrapProps getInstance() {
		if (bootstrapProps == null) {
			//dcl with volatile
			init(null);
		}
		return bootstrapProps;
	}
	
	public static synchronized boolean init(Map<String, String> props) {
		if (bootstrapProps != null) {
			return false;
		}
		bootstrapProps = new BootstrapProps(props);
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BootstrapProps(Map<String, String> initProps) {
		
		String serverEnvPropsFileName = null;
		if (initProps != null) {
			serverEnvPropsFileName = initProps.get(SERVER_ENV_PROP_FILE_NAME);
		}
		if (serverEnvPropsFileName == null) {
			serverEnvPropsFileName = System.getProperty(SERVER_ENV_PROP_FILE_NAME);
		}
		
		if (serverEnvPropsFileName != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("load bootstrap props from file:" + serverEnvPropsFileName);
			}
			try {
				String content = FileUtil.readContent(serverEnvPropsFileName);
				if (content == null) {
					throw new SystemException("Can't find bootstrap props file:" + serverEnvPropsFileName);
				}
				
				Properties p = new Properties();
				p.load(new StringReader(content));
				props = new HashMap(p);
			} catch (IOException e) {
				throw new SystemException("Can't read bootstrap props file:" + serverEnvPropsFileName, e);
			}
			
		} else {
			props = new HashMap(System.getProperties());
			if (initProps != null) {
				props.putAll(initProps);
			}
		}
	}
	
	public String getProperty(String key) {
		try {
			lock.readLock().lock();
			return props.get(key);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public String getProperty(String key, String defaultValue) {
		String ret = getProperty(key);
		if (ret == null) {
			return defaultValue;
		} else {
			return ret;
		}
	}
	
	public void setProperty(String key, String value) {
		try {
			lock.writeLock().lock();
			props.put(key, value);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	/**
	 * 既存に設定されている値が指定したvalueと異なる場合のみ値を置換する。
	 * 
	 * @param key
	 * @param value
	 * @return 置換が行われた場合、true
	 */
	public boolean replaceProperty(String key, String value) {
		try {
			lock.writeLock().lock();
			String oldVal = props.get(key);
			if (!value.equals(oldVal)) {
				logger.debug("replace " + key + " from " + oldVal + " to " + value);
				props.put(key, value);
				return true;
			} else {
				return false;
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

}
