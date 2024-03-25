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

import jakarta.validation.MessageInterpolator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.iplass.mtp.impl.validation.bean.MessageInterpolatorFactory;

public class HibernateMessageInterpolatorFactory implements MessageInterpolatorFactory {
	
	private ResourceBundleLocator resourceBundleLocator;
	private boolean cachingEnabled = true;
	
	public boolean isCachingEnabled() {
		return cachingEnabled;
	}

	public void setCachingEnabled(boolean cachingEnabled) {
		this.cachingEnabled = cachingEnabled;
	}

	public ResourceBundleLocator getResourceBundleLocator() {
		return resourceBundleLocator;
	}
	
	public void setResourceBundleLocator(ResourceBundleLocator resourceBundleLocator) {
		this.resourceBundleLocator = resourceBundleLocator;
	}
	
	@Override
	public MessageInterpolator newMessageInterpolator(int tenantId) {
		return new ResourceBundleMessageInterpolator(resourceBundleLocator, cachingEnabled);
	}

}
