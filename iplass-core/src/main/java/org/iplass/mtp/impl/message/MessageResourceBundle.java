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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.message.MetaMessageCategory.MetaMessageCategoryHandler;
import org.iplass.mtp.spi.ServiceRegistry;

public class MessageResourceBundle extends ResourceBundle {

	private String baseBundleName;
	String lang;
	private MessageService service = ServiceRegistry.getRegistry().getService(MessageService.class);

	public MessageResourceBundle(String baseBundleName, Locale locale, ResourceBundle wrapped) {
		this.baseBundleName = baseBundleName;
		I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);
		Locale langLocale = i18n.selectLangLocale(locale);
		lang = langLocale.toLanguageTag();
		setParent(wrapped);
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
		} else {
			return null;
		}
	}

	@Override
	protected Set<String> handleKeySet() {
		MetaMessageCategoryHandler handler = service.getRuntimeByName(baseBundleName);
		if (handler != null) {
			Map<String,MetaMessageItem> map = handler.getMetaData().getMessages();
			if (map == null) {
				return Collections.emptySet();
			}
			return map.keySet();
		} else {
			return Collections.emptySet();
		}
	}

	@Override
	public Enumeration<String> getKeys() {
		MetaMessageCategoryHandler handler = service.getRuntimeByName(baseBundleName);
		if (handler != null) {
			Map<String,MetaMessageItem> map = handler.getMetaData().getMessages();
			if (map == null && parent == null) {
				return Collections.emptyEnumeration();
			}
			Set<String> keySet = new HashSet<>();
			if (map != null) {
				keySet.addAll(map.keySet());
			}
			if (parent != null) {
				keySet.addAll(parent.keySet());
			}
			Iterator<String> keys = keySet.iterator();
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
		} else if (parent != null){
			return parent.getKeys();
		} else {
			return Collections.emptyEnumeration();
		}
	}

}
