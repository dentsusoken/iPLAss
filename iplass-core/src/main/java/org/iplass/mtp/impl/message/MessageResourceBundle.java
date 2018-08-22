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
package org.iplass.mtp.impl.message;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.message.MetaMessageCategory.MetaMessageCategoryHandler;
import org.iplass.mtp.spi.ServiceRegistry;

public class MessageResourceBundle extends ResourceBundle {
	
	private String baseBundleName;
	String lang;
	private ResourceBundle wrapped;
	private MessageService service = ServiceRegistry.getRegistry().getService(MessageService.class);
	
	public MessageResourceBundle(String baseBundleName, Locale locale, ResourceBundle wrapped) {
		this.baseBundleName = baseBundleName;
		I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);
		Locale langLocale = i18n.selectLangLocale(locale);
		lang = langLocale.toLanguageTag();
		this.wrapped = wrapped;
	}

	@Override
	public String getBaseBundleName() {
		return baseBundleName;
	}

	@Override
	protected Object handleGetObject(String key) {
		MetaMessageCategoryHandler handler = service.getRuntimeByName(baseBundleName);
		if (handler != null) {
			return handler.getMessageString(key, lang);
		} else if (wrapped != null) {
			return wrapped.getObject(key);
		} else {
			return null;
		}
	}

	@Override
	public Enumeration<String> getKeys() {
		MetaMessageCategoryHandler handler = service.getRuntimeByName(baseBundleName);
		if (handler != null) {
			Map<String,MetaMessageItem> map = handler.getMetaData().getMessages();
			if (map == null) {
				return Collections.emptyEnumeration();
			}
			Iterator<String> keys = map.keySet().iterator();
			return new Enumeration<String>() {
				@Override
				public boolean hasMoreElements() {
					return keys.hasNext();
				}
				@Override
				public String nextElement() {
					return keys.next();
				}
			};
		} else if (wrapped != null) {
			return wrapped.getKeys();
		} else {
			return Collections.emptyEnumeration();
		}
	}

}
