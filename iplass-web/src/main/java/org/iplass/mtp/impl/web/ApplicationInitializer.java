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

package org.iplass.mtp.impl.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import org.iplass.mtp.impl.async.rdb.RdbQueueService;
import org.iplass.mtp.impl.cache.store.builtin.CacheEntryCleaner;
import org.iplass.mtp.impl.core.config.BootstrapProps;
import org.iplass.mtp.runtime.EntryPoint;
import org.iplass.mtp.runtime.EntryPointBuilder;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationInitializer implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);
	
	protected EntryPoint entryPoint;

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
		if (entryPoint != null) {
			logger.info("destroy all service");
			entryPoint.destroy();
			
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
	}
	
	protected EntryPointBuilder createEntryPointBuilder(ServletContext sc) {
		EntryPointBuilder builder = EntryPoint.builder();
		
		String serverEnvFileName = sc.getInitParameter(BootstrapProps.SERVER_ENV_PROP_FILE_NAME);
		if (serverEnvFileName != null) {
			logger.info("use " + serverEnvFileName + " as ServerEnvFile.");
			builder.serverEnvFile(serverEnvFileName);
		}
		String serviceConfigFileName = sc.getInitParameter(BootstrapProps.CONFIG_FILE_NAME);
		if (serviceConfigFileName != null) {
			logger.info("initialize with config:" + serviceConfigFileName);
			builder.config(serviceConfigFileName);
		}
		String cryptConfigFileName = sc.getInitParameter(BootstrapProps.CRYPT_CONFIG_FILE_NAME);
		if (cryptConfigFileName != null) {
			logger.debug("use cryptConfigFile:" + cryptConfigFileName);
			builder.crypt(cryptConfigFileName);
		}
		String serverRole = sc.getInitParameter(BootstrapProps.SERVER_ROLES);
		if (serverRole != null) {
			logger.debug("use serverRoles:" + serverRole);
			builder.serverRole(serverRole);
		}
		String loader = sc.getInitParameter(BootstrapProps.CONFIG_LOADER_CLASS_NAME);
		if (loader != null) {
			logger.debug("use configLoader:" + loader);
			builder.loader(loader);
		}
		
		//TODO get all other InitParameters?
		
		return builder;
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		if (!EntryPoint.isInited()) {
			ServletContext sc = e.getServletContext();
			EntryPointBuilder builder = createEntryPointBuilder(sc);
			entryPoint = builder.build();
		}
		
		//starting queue service or not
		ServiceRegistry sr = ServiceRegistry.getRegistry();
		if (sr.exists(RdbQueueService.class)) {
			RdbQueueService qs = sr.getService(RdbQueueService.class);
		}

	}

}
