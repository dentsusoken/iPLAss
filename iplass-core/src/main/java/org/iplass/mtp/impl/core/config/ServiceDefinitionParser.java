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
package org.iplass.mtp.impl.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	private ConfigPreprocessor[] prepros;
	
	public ServiceDefinitionParser(ConfigPreprocessor[] prepros) {
		try {
			context = JAXBContext.newInstance(NameValue.class, ServiceConfig.class, ServiceDefinition.class);
		} catch (JAXBException e) {
			logger.error("JAXBContext can not initialize.", e);
			throw new ServiceConfigrationException(e);
		}
		this.prepros = prepros;
	}
	
	public ServiceDefinition read(String fileName) {
		
		String content = readContent(fileName);
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

	private String readContent(String fileName) {
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream(fileName);
			if (is == null) {
				File file = new File(fileName);
				if (file.exists()) {
					try {
						is = new FileInputStream(file);
					} catch (FileNotFoundException e) {
						if (logger.isDebugEnabled()) {
							logger.debug("ConfigFile:" + fileName + " not found.", e);
						}
					}
				}
			}
			if (is == null) {
				logger.error("ConfigFile:" + fileName + " not found.Can not initialize ServiceRegistry.");
				throw new ServiceConfigrationException("Config File:" + fileName + " Not Found.");
			}
			
			InputStreamReader r = new InputStreamReader(is, "utf-8");
			StringBuffer str = new StringBuffer();
			char[] buf = new char[1024];
			int length = 0;
			while ((length = r.read(buf)) != -1) {
				str.append(buf, 0, length);
			}
			return str.toString();
		} catch (IOException e) {
			logger.error("Cant read ConfigFile:" + fileName + ".Can not initialize ServiceRegistry.", e);
			throw new ServiceConfigrationException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("resource close failed. Maybe Resource Leak.", e);
				}
			}
		}
	}

}
