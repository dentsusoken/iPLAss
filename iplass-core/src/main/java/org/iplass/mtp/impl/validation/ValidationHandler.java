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

package org.iplass.mtp.impl.validation;

import java.util.List;
import java.util.regex.Pattern;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.message.MessageService;
import org.iplass.mtp.impl.message.MetaMessageCategory.MetaMessageCategoryHandler;
import org.iplass.mtp.impl.message.MetaMessageItem;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.spi.ServiceRegistry;

public abstract class ValidationHandler /* implements MetaDataRuntime */ {

	static final Pattern namePattern = Pattern.compile("${name}", Pattern.LITERAL);
	static final Pattern entityNamePattern = Pattern.compile("${entityName}", Pattern.LITERAL);

	static final String ENTITY_BINDING_NAME = "entity";
	static final String PROPERTY_NAME_BINDING_NAME = "propertyName";
	static final String VALUE_BINDING_NAME = "value";
	static final String CONTEXT_BINDING_NAME = "context";

	static final String VALIDATION_SKIP_SCRIPT = "ValidationSkipScript";

	protected MetaValidation metaData;

	protected MetaEntity entity;

	protected MetaProperty property;

	public ValidationHandler(MetaValidation metaData, MetaEntity entity, MetaProperty property) {
		this.metaData = metaData;
		this.entity = entity;
		this.property = property;
		init();
	}

	public String getErrorMessage() {
		return metaData.getErrorMessage();
	}

	public void init() {
	}

	public abstract boolean validate(Object value, ValidationContext context);

	public boolean validateSkipCheck(Object value, ValidationContext context) {
		if (metaData.getValidationSkipScript() != null && validationSkipScript(entity, property, value, context)) {
			// Validationスキップスクリプトが設定されている場合は、スクリプトでスキップを行う
			return true;
		}

		return false;
	}

	public boolean validateArray(Object[] values, ValidationContext context) {
		if (validateSkipCheck(values, context)) {
			// Validationスキップスクリプトが設定されている場合は、スクリプトでスキップを行う
			return true;
		}
		boolean res = true;
		if (values != null) {
			for (Object v : values) {
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

	private boolean validationSkipScript(MetaEntity entity, MetaProperty property, Object value, ValidationContext context) {
		TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
		ScriptEngine scriptEngine = tc.getScriptEngine();

		String scriptName = VALIDATION_SKIP_SCRIPT + "_" + entity.getId() + "_" + property.getId() + "_" + metaData.getMessageId();
		Script compiledScript = scriptEngine.createScript(metaData.getValidationSkipScript(), scriptName);
		ScriptContext sc = scriptEngine.newScriptContext();
		sc.setAttribute(ENTITY_BINDING_NAME, context.getEntity());
		sc.setAttribute(PROPERTY_NAME_BINDING_NAME, context.getPropertyName());
		sc.setAttribute(VALUE_BINDING_NAME, value);
		sc.setAttribute(CONTEXT_BINDING_NAME, context);
		Boolean retVal = (Boolean) compiledScript.eval(sc);
		if (retVal == null) {
			return false;
		}
		return retVal.booleanValue();
	}

}
