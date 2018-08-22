/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecodePreprocessor implements ConfigPreprocessor {
	
	private static Logger logger = LoggerFactory.getLogger(DecodePreprocessor.class);
	
	private PropertyValueCoder coder;
	
	public DecodePreprocessor() {
		String cryptConfigFileName = ServiceRegistryInitializer.getCryptoConfigFileName();
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
		Path path = Paths.get(fileName);
		if (Files.exists(path)) {
			if (logger.isDebugEnabled()) {
				logger.debug("load CryptConfigFile from file path:" + fileName);
			}
			try (InputStreamReader is = new InputStreamReader(new FileInputStream(path.toFile()), "utf-8")) {
				prop.load(is);
			} catch (IOException e) {
				throw new ServiceConfigrationException(e);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("load CryptConfigFile from classpath:" + fileName);
			}
			try (InputStream is = getClass().getResourceAsStream(fileName)) {
				if (is == null) {
					logger.error("CryptConfigFile:" + fileName + " not found.Can not initialize ServiceRegistry.");
					throw new ServiceConfigrationException("Config File:" + fileName + " Not Found.");
				}

				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				prop.load(isr);

			} catch (IOException e) {
				throw new ServiceConfigrationException(e);
			}
		}

		try {

			String passphraseSupplierName = prop.getProperty(PropertyValueCoder.PASSPHRASE_SUPPLIER, PropertyFilePassphraseSupplier.class.getName());
			try (PassphraseSupplier passphraseSupplier = (PassphraseSupplier) Class.forName(passphraseSupplierName).newInstance()) {
				passphraseSupplier.open(prop);
				String keySalt = prop.getProperty(PropertyValueCoder.KEY_SALT);
				byte[] saltByte = null;
				if (keySalt != null) {
					saltByte = MessageDigest.getInstance("SHA-256").digest(keySalt.getBytes("utf-8"));
				}

				PropertyValueCoder coder = new PropertyValueCoder(
						prop.getProperty(PropertyValueCoder.KEY_ALGORITHM),
						toInt(prop.getProperty(PropertyValueCoder.KEY_LENGTH)),
						prop.getProperty(PropertyValueCoder.CIPHER_ALGORITHM),
						saltByte,
						toInt(prop.getProperty(PropertyValueCoder.KEY_STRETCH)),
						passphraseSupplier
						);
				return coder;
			}


		} catch (IOException | NoSuchAlgorithmException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new ServiceConfigrationException(e);
		}
	}

	private int toInt(String val) {
		if (val == null) {
			return -1;
		} else {
			return Integer.parseInt(val);
		}
	}


}
