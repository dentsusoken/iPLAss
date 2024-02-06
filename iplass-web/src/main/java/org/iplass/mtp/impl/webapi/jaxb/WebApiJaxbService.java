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

package org.iplass.mtp.impl.webapi.jaxb;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebApiJaxbService implements Service {

	private static Logger logger = LoggerFactory.getLogger(WebApiJaxbService.class);

	private JAXBContext context;
	private List<String> classesToBeBound;

	@Override
	public void init(Config config) {
		try {
			classesToBeBound = config.getValues("classToBeBound");
			Class<?>[] classes = new Class<?>[classesToBeBound.size()];
			for (int i = 0; i < classesToBeBound.size(); i++) {
				classes[i] = Class.forName(classesToBeBound.get(i));
				logger.debug("Add to JAXBContext:" + classes[i]);
			}

			context = JAXBContext.newInstance(classes);
		} catch (JAXBException e) {
			throw new MetaDataRuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new MetaDataRuntimeException(e);
		}
	}

	@Override
	public void destroy() {
		context = null;
	}

	public JAXBContext getJAXBContext() {
		return context;
	}

//	public JAXBContext createJAXBContext(Class<?>... additionalClass) {
//		try {
//			Class<?>[] classes = new Class<?>[classesToBeBound.size() + additionalClass.length];
//			for (int i = 0; i < classesToBeBound.size(); i++) {
//				classes[i] = Class.forName(classesToBeBound.get(i));
//				logger.debug("Add to JAXBContext:" + classes[i]);
//			}
//			for (int i = 0; i < additionalClass.length; i++) {
//				classes[classesToBeBound.size() + i] = additionalClass[i];
//			}
//			return JAXBContext.newInstance(classes);
//		} catch (JAXBException e) {
//			throw new MetaDataRuntimeException(e);
//		} catch (ClassNotFoundException e) {
//			throw new MetaDataRuntimeException(e);
//		}
//	}

}
