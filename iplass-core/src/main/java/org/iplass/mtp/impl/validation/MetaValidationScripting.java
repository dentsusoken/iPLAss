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

import java.util.Iterator;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.ScriptingValidation;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaValidationScripting extends MetaValidation {
	private static final long serialVersionUID = 8226573142915272860L;

	public static final String ENTITY_BINDING_NAME = "entity";
	public static final String PROPERTY_NAME_BINDING_NAME = "propertyName";
	public static final String VALUE_BINDING_NAME = "value";
	public static final String CONTEXT_BINDING_NAME = "context";

	private String script;
	private boolean asArray = false;

	public boolean isAsArray() {
		return asArray;
	}

	public void setAsArray(boolean asArray) {
		this.asArray = asArray;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public void applyConfig(ValidationDefinition definition) {
		fillFrom(definition);
		ScriptingValidation def = (ScriptingValidation) definition;
		script = def.getScript();
		asArray = def.isAsArray();
	}

	@Override
	public MetaValidationScripting copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public ValidationHandler createRuntime(MetaEntity entity, MetaProperty property) {
		return new ValidationHandlerScripting(entity, property);
	}

	@Override
	public ScriptingValidation currentConfig(EntityContext context) {
		ScriptingValidation def = new ScriptingValidation();
		fillTo(def);
		def.setScript(script);
		def.setAsArray(asArray);
		return def;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (asArray ? 1231 : 1237);
		result = prime * result + ((script == null) ? 0 : script.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaValidationScripting other = (MetaValidationScripting) obj;
		if (asArray != other.asArray)
			return false;
		if (script == null) {
			if (other.script != null)
				return false;
		} else if (!script.equals(other.script))
			return false;
		return true;
	}

	private class ValidationHandlerScripting extends ValidationHandler {

		private static final String SCRIPT_PREFIX = "ValidationHandlerScripting_script";

		private Script compiledScript;
		private ScriptEngine scriptEngine;

		ValidationHandlerScripting(MetaEntity entity, MetaProperty property) {
			super(MetaValidationScripting.this, entity, property);

			//TODO tenantIDの決定は、このメソッドを呼び出した際のスレッドに紐付いているテナントIDとなる。これでセキュリティ的、動作的に大丈夫か？
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptEngine = tc.getScriptEngine();

			String scriptName = null;
			for (int i = 0; i < property.getValidations().size(); i++) {
				if (MetaValidationScripting.this == property.getValidations().get(i)) {
					scriptName = SCRIPT_PREFIX + "_" + entity.getId() + "_" + property.getId() + "_" + i;
					break;
				}
			}
			compiledScript = scriptEngine.createScript(script, scriptName);

		}

		@Override
		public String generateErrorMessage(Object value,
				ValidationContext context, String propertyDisplayName,
				String entityDisplayName) {
			String msg = super.generateErrorMessage(value, context, propertyDisplayName,
					entityDisplayName);
			Iterator<String> it = context.getAttributeNames();
			while (it.hasNext()) {
				String key = it.next();
				String target = "${" + key + "}";
				msg = msg.replace(target, String.valueOf(context.getAttribute(key)));
			}
			return msg;
		}

		@Override
		public boolean validate(Object value, ValidationContext context) {
			if (validateSkipCheck(value, context)) {
				return true;
			}
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

		@Override
		public boolean validateArray(Object[] values, ValidationContext context) {
			if (asArray) {
				return validate(values, context);
			} else {
				return super.validateArray(values, context);
			}
		}

		//		@Override
		//		public MetaData getMetaData() {
		//			return MetaValidationScripting.this;
		//		}

	}

}
