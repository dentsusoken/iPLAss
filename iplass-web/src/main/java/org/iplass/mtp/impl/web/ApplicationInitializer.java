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

package org.iplass.mtp.impl.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.iplass.mtp.impl.async.rdb.RdbQueueService;
import org.iplass.mtp.impl.cache.store.builtin.CacheEntryCleaner;
import org.iplass.mtp.impl.core.config.ServiceRegistryInitializer;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationInitializer implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("destroy all service");
		ServiceRegistry.getRegistry().destroyAllService();

		//Builtin CacheEntryCleaner
		CacheEntryCleaner.shutdown();
		
		//logback memory leak prevention
		ILoggerFactory lf = LoggerFactory.getILoggerFactory();
		try {
			Class<?> logbackLoggerContextClass = Class.forName("ch.qos.logback.classic.LoggerContext");
			if (logbackLoggerContextClass.isAssignableFrom(lf.getClass())) {
				Method stopMethod = logbackLoggerContextClass.getMethod("stop");
				if (stopMethod != null) {
					stopMethod.invoke(lf);
				}
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			//ignore
			e.printStackTrace();
		}
		
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		ServletContext sc = e.getServletContext();

		String serverEnvFileName = sc.getInitParameter("mtp.server.env");
		if (serverEnvFileName != null) {
			logger.info("use " + serverEnvFileName + " as ServerEnvFile.");
			ServiceRegistryInitializer.setServerEnvFileName(serverEnvFileName);
		}

		String serviceConfigFileName = sc.getInitParameter("mtp.config");
		if (serviceConfigFileName != null) {
			boolean isFinalServiceConfig = Boolean.parseBoolean(sc.getInitParameter("mtp.config.isfinal"));
			if (!ServiceRegistryInitializer.isSetConfigFileName() || !isFinalServiceConfig) {
				logger.info("initialize with config:" + serviceConfigFileName);
				ServiceRegistryInitializer.setConfigFileName(serviceConfigFileName);
			}
		}
		String cryptConfigFileName = sc.getInitParameter("mtp.config.crypt");
		if (cryptConfigFileName != null) {
			logger.debug("use cryptConfigFile:" + cryptConfigFileName);
			ServiceRegistryInitializer.setCryptoConfigFileName(cryptConfigFileName);
		}


		//starting queue service or not
		ServiceRegistry sr = ServiceRegistry.getRegistry();
		if (sr.exists(RdbQueueService.class)) {
			RdbQueueService qs = sr.getService(RdbQueueService.class);
		}

	}

}
