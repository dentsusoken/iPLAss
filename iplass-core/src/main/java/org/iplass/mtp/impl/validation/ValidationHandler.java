/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.validation;

import java.util.List;
import java.util.regex.Pattern;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.message.MessageService;
import org.iplass.mtp.impl.message.MetaMessageCategory.MetaMessageCategoryHandler;
import org.iplass.mtp.impl.message.MetaMessageItem;
import org.iplass.mtp.spi.ServiceRegistry;

public abstract class ValidationHandler /*implements MetaDataRuntime*/ {

	static final Pattern namePattern = Pattern.compile("${name}", Pattern.LITERAL);
	static final Pattern entityNamePattern = Pattern.compile("${entityName}", Pattern.LITERAL);


	protected MetaValidation metaData;

	public ValidationHandler(MetaValidation metaData) {
		this.metaData = metaData;
		init();
	}

	public String getErrorMessage() {
		return metaData.getErrorMessage();
	}

	public void init() {
	}

	public abstract boolean validate(Object value, ValidationContext context);

	public boolean validateArray(Object[] values, ValidationContext context) {
		boolean res = true;
		if (values != null) {
			for (Object v: values) {
				res &= validate(v, context);
				if (res == false) {
					break;
				}
			}
		}
		return res;
	}

	public String generateErrorMessage(Object value, ValidationContext context, String propertyDisplayName, String entityDisplayName) {
		String msg = getErrorMessage();
		List<MetaLocalizedString> localizedMsg = metaData.getLocalizedErrorMessageList();

		// MesageItemからメッセージを作成
		if (msg == null || msg.length() == 0) {
			if (metaData.getMessageCategory() != null && metaData.getMessageId() != null) {
				MessageService ms = ServiceRegistry.getRegistry().getService(MessageService.class);
				MetaMessageCategoryHandler mmc = ms.getRuntimeByName(metaData.getMessageCategory());
				if (mmc != null) {
					MetaMessageItem mmi = mmc.getMetaData().getMessages().get(metaData.getMessageId());
					if (mmi != null) {
						msg = mmi.getMessage();
						localizedMsg = mmi.getLocalizedMessageList();
					}
				}
			}
		}
		
		msg = I18nUtil.stringMeta(msg, localizedMsg);

		//${name},${entityName}を置換
		if (msg != null) {
			if (msg.contains("${name}")) {
				msg = namePattern.matcher(msg).replaceAll(propertyDisplayName);
			}
			if (msg.contains("${entityName}")) {
				msg = entityNamePattern.matcher(msg).replaceAll(entityDisplayName);
			}
		}

		return msg;
	}

	public String getErrorCode() {
		String code = metaData.getErrorCode();
		if (code == null) {
			return "";
		} else {
			return code;
		}
	}

}
