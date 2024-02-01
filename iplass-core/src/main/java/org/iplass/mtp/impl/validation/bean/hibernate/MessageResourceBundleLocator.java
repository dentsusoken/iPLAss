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
package org.iplass.mtp.impl.validation.bean.hibernate;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.iplass.mtp.impl.util.ResourceBundleWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageResourceBundleLocator implements ResourceBundleLocator {
	public static final String DEFAULT_BUNDLE_NAME = "ValidationMessages";
	
	private static Logger logger = LoggerFactory.getLogger(MessageResourceBundleLocator.class);
	
	private String bundleName = DEFAULT_BUNDLE_NAME;
	
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		try {
			return ResourceBundleWrapper.getResourceBundle(bundleName, locale);
		} catch (MissingResourceException e) {
			logger.debug("can't find resource bundle:" + bundleName);
			return null;
		}
	}

}
