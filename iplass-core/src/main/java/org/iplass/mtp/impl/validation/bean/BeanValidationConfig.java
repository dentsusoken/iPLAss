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
package org.iplass.mtp.impl.validation.bean;

import java.util.Locale;
import java.util.Map;

import javax.validation.Configuration;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.iplass.mtp.impl.core.ExecuteContext;

public class BeanValidationConfig {
	@SuppressWarnings("rawtypes")
	private Class providerClass;
	private MessageInterpolator messageInterpolator;
	private Map<String, String> properties;
	
	public Class<?> getProviderClass() {
		return providerClass;
	}

	public void setProviderClass(Class<?> providerClass) {
		this.providerClass = providerClass;
	}

	public MessageInterpolator getMessageInterpolator() {
		return messageInterpolator;
	}

	public void setMessageInterpolator(MessageInterpolator messageInterpolator) {
		this.messageInterpolator = messageInterpolator;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@SuppressWarnings("unchecked")
	public ValidatorFactory getValidatorFactory() {
		
		Configuration<?> config;
		if (this.providerClass != null) {
			config = Validation.byProvider(this.providerClass).configure();
		} else {
			config = Validation.byDefaultProvider().configure();
		}
		
		if (messageInterpolator != null) {
			config.messageInterpolator(new TenantLocaleMessageInterpolator(messageInterpolator));
		} else {
			config.messageInterpolator(new TenantLocaleMessageInterpolator(config.getDefaultMessageInterpolator()));
		}
		
		if (properties != null) {
			properties.forEach((name, value) -> config.addProperty(name, value));
		}

		return config.buildValidatorFactory();
	}
	
	private static class TenantLocaleMessageInterpolator implements MessageInterpolator {
		
		private MessageInterpolator wrapped;
		
		private TenantLocaleMessageInterpolator(MessageInterpolator wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public String interpolate(String messageTemplate, Context context) {
			return wrapped.interpolate(messageTemplate, context, ExecuteContext.getCurrentContext().getLangLocale());
		}

		@Override
		public String interpolate(String messageTemplate, Context context, Locale locale) {
			return wrapped.interpolate(messageTemplate, context, locale);
		}

	}

}
