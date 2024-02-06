/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.Properties;

import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecodePreprocessor implements ConfigPreprocessor {
	
	private static Logger logger = LoggerFactory.getLogger(DecodePreprocessor.class);
	
	private PropertyValueCoder coder;
	
	public DecodePreprocessor() {
		String cryptConfigFileName = BootstrapProps.getInstance().getProperty(BootstrapProps.CRYPT_CONFIG_FILE_NAME);
		if (cryptConfigFileName != null) {
			coder = getPropertyValueCoder(cryptConfigFileName);
		}
	}

	@Override
	public ServiceDefinition preprocess(ServiceDefinition serviceDefinition) {
		if (coder != null && serviceDefinition.getService() != null) {
			for (ServiceConfig sc: serviceDefinition.getService()) {
				decode(sc, coder);
			}
		}
		return serviceDefinition;
	}
	
	private void decode(ServiceConfig sc, PropertyValueCoder coder) {
		if (sc.getProperty() != null) {
			for (NameValue p: sc.getProperty()) {
				decode(p, coder);
			}
		}
	}
	
	public void decode(NameValue nv, PropertyValueCoder coder) {
		if (nv.isEncrypted()) {
			if (nv.getValue() != null) {
				nv.setValue(coder.decode(nv.getValue()));
			}
			if (nv.getTextValue() != null) {
				nv.setTextValue(coder.decode(nv.getTextValue()));
			}
		}
		if (nv.getProperty() != null) {
			for (NameValue p: nv.getProperty()) {
				decode(p, coder);
			}
		}
		if (nv.getArg() != null) {
			for (NameValue p: nv.getArg()) {
				decode(p, coder);
			}
		}
	}
	
	private PropertyValueCoder getPropertyValueCoder(String fileName) {
		Properties prop = new Properties();
		try {
			String content = FileUtil.readContent(fileName);
			if (content == null) {
				logger.error("CryptConfigFile:" + fileName + " not found.Can not initialize ServiceRegistry.");
				throw new ServiceConfigrationException("Config File:" + fileName + " Not Found.");
			}
			prop.load(new StringReader(content));
		} catch (IOException e) {
			throw new ServiceConfigrationException("can not load CryptConfigFile", e);
		}
		
		try {
			String propertyValueCoderName = prop.getProperty(PropertyValueCoder.PROPERTY_VALUE_CODER, DefaultPropertyValueCoder.class.getName());
			PropertyValueCoder coder = (PropertyValueCoder) Class.forName(propertyValueCoderName).newInstance();
			coder.open(prop);
			return coder;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new ServiceConfigrationException(e);
		}
	}
}
