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

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceDefinitionParser {
	private static final Logger logger = LoggerFactory.getLogger(ServiceDefinitionParser.class);
	
	private JAXBContext context;
	private ConfigLoader loader;
	private ConfigPreprocessor[] prepros;
	
	public ServiceDefinitionParser() {
		try {
			context = JAXBContext.newInstance(NameValue.class, ServiceConfig.class, ServiceDefinition.class);
		} catch (JAXBException e) {
			logger.error("JAXBContext can not initialize.", e);
			throw new ServiceConfigrationException(e);
		}
		this.loader = newConfigLoader();
		this.prepros = newConfigPreprocessor();
	}
	
	private ConfigLoader newConfigLoader() {
		String clsName = BootstrapProps.getInstance().getProperty(BootstrapProps.CONFIG_LOADER_CLASS_NAME, BootstrapProps.DEFAULT_CONFIG_LOADER_CLASS_NAME);
		try {
			ConfigLoader cls = (ConfigLoader) Class.forName(clsName).newInstance();
			return cls;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new ServiceConfigrationException("Can not instanceate ConfigLoader:" + clsName, e);
		}
	}
	
	private ConfigPreprocessor[] newConfigPreprocessor() {
		String cpsProp = BootstrapProps.getInstance().getProperty(BootstrapProps.CONFIG_PREPROCESSORS_CLASS_NAME, BootstrapProps.DEFAULT_CONFIG_PREPROCESSORS_CLASS_NAME);
		String[] cnames = cpsProp.trim().split("\\s*,\\s*");
		ConfigPreprocessor[] cps = null;
		if (cnames != null) {
			cps = new ConfigPreprocessor[cnames.length];
			for (int i = 0; i < cps.length; i++) {
				try {
					cps[i] = (ConfigPreprocessor) Class.forName(cnames[i]).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					throw new ServiceConfigrationException("Can not instanceate ConfigPreprocessor:" + cnames[i], e);
				}
			}
		}
		return cps;
	}

	public ServiceDefinition read(String fileName) {
		
		String content = loader.load(fileName);
		if (prepros != null) {
			for (ConfigPreprocessor p: prepros) {
				content = p.preprocess(content, fileName);
			}
		}

		try {
			Unmarshaller um = context.createUnmarshaller();
			ServiceDefinition sd = (ServiceDefinition) um.unmarshal(new StringReader(content));
			if (prepros != null) {
				for (ConfigPreprocessor p: prepros) {
					sd = p.preprocess(sd);
				}
			}
			
			if (sd.getInherits() != null) {
				//merge multi inherits
				if (logger.isDebugEnabled()) {
					logger.debug(fileName + " inherited " + sd.getInherits()[0]);
				}
				ServiceDefinition inhSd = read(sd.getInherits()[0]);
				for (int i = 1; i < sd.getInherits().length; i++) {
					if (logger.isDebugEnabled()) {
						logger.debug(fileName + " inherited " + sd.getInherits()[i]);
					}
					inhSd.include(read(sd.getInherits()[i]));
				}
				
				sd.inherit(inhSd);
			}

			if (sd.getIncludes() != null) {
				for (String inc: sd.getIncludes()) {
					ServiceDefinition incSd = read(inc);
					sd.include(incSd);
					if (logger.isDebugEnabled()) {
						logger.debug(fileName + " included " + inc);
					}
				}
			}

			if (logger.isTraceEnabled()) {
				Marshaller m = context.createMarshaller();
				m.setProperty("jaxb.formatted.output", true);
				StringWriter w = new StringWriter();
				m.marshal(sd, w);
				logger.trace("configration of " + fileName + "\n=============\n" + w.toString() + "\n=============");
			}

			return sd;
		} catch (JAXBException e) {
			logger.error("Parse failed ConfigFile:" + fileName + ".Can not initialize ServiceRegistry.", e);
			throw new ServiceConfigrationException(e);
		}
	}

}
