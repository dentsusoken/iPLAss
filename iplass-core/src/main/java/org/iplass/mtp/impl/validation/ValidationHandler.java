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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.message.MessageService;
import org.iplass.mtp.impl.message.MetaMessageItem;
import org.iplass.mtp.impl.message.MetaMessageCategory.MetaMessageCategoryHandler;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;

public abstract class ValidationHandler /*implements MetaDataRuntime*/ {

	private static final Pattern namePattern = Pattern.compile("${name}", Pattern.LITERAL);
	private static final Pattern entityNamePattern = Pattern.compile("${entityName}", Pattern.LITERAL);

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

		// MesageItemからメッセージを作成
		if (msg == null || msg.length() == 0) {
			if (metaData.getMessageCategory() != null && metaData.getMessageId() != null) {
				MessageService ms = ServiceRegistry.getRegistry().getService(MessageService.class);
				MetaMessageCategoryHandler mmc = ms.getRuntimeByName(metaData.getMessageCategory());
				if (mmc != null) {
					MetaMessageItem mmi = mmc.getMetaData().getMessages().get(metaData.getMessageId());

					String temp = mmc.createMessage(mmi, context, propertyDisplayName, entityDisplayName);
					if (StringUtil.isNotEmpty(temp)) {
						msg = temp;

						return msg;
					}
				}
			}
		}

		// MetaValidationからメッセージを作成
		Map<String, String> localizedStringMap = new HashMap<String, String>();
		if (metaData.getLocalizedErrorMessageList() != null) {
			for (MetaLocalizedString mls : metaData.getLocalizedErrorMessageList()) {
				localizedStringMap.put(mls.getLocaleName(), mls.getStringValue());
			}
		}

		String lang = ExecuteContext.getCurrentContext().getLanguage();

		if (StringUtil.isNotEmpty(localizedStringMap.get(lang))) {
			msg = localizedStringMap.get(lang);
		}

		//${name},${entityName}限定で置換
		Entity entity = context.getValidatingDataModel();
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(("/entity/" + entity.getDefinitionName()).replace(".","/"));
		EntityHandler eHandl = (EntityHandler) entry.getRuntime();
		Map<String, String> entityLangMap = eHandl.getLocalizedStringMap();
		eHandl.getMetaData().getDisplayName();

		PropertyHandler pHandl = eHandl.getProperty(context.getValidatePropertyName(), EntityContext.getCurrentContext());
		Map<String, String> propLangMap = pHandl.getLocalizedStringMap();

		if (msg != null) {
			if (msg.contains("${name}")) {
				String replaceName = propertyDisplayName;
				if (propLangMap.get(lang) != null) {
					replaceName = propLangMap.get(lang);
				}
				msg = namePattern.matcher(msg).replaceAll(replaceName);
			}
			if (msg.contains("${entityName}")) {
				String replaceName = entityDisplayName;
				if (entityLangMap.get(lang) != null) {
					replaceName = entityLangMap.get(lang);
				}
				msg = entityNamePattern.matcher(msg).replaceAll(replaceName);
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
