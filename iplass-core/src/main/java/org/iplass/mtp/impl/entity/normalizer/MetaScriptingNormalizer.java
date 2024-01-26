/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.normalizer;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.ScriptingNormalizer;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaScriptingNormalizer extends MetaNormalizer {
	private static final long serialVersionUID = 767053580735072850L;

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
	public MetaScriptingNormalizer copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (asArray ? 1231 : 1237);
		result = prime * result + ((script == null) ? 0 : script.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaScriptingNormalizer other = (MetaScriptingNormalizer) obj;
		if (asArray != other.asArray)
			return false;
		if (script == null) {
			if (other.script != null)
				return false;
		} else if (!script.equals(other.script))
			return false;
		return true;
	}

	@Override
	public void applyConfig(NormalizerDefinition definition) {
		ScriptingNormalizer d = (ScriptingNormalizer) definition;
		script = d.getScript();
		asArray = d.isAsArray();
	}

	@Override
	public ScriptingNormalizer currentConfig(EntityContext context) {
		ScriptingNormalizer d = new ScriptingNormalizer();
		d.setScript(script);
		d.setAsArray(asArray);
		return d;
	}

	@Override
	public NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property) {
		return new ScriptingNormalizerRuntime(entity, property);
	}
	
	public class ScriptingNormalizerRuntime extends NormalizerRuntime {

		private static final String SCRIPT_PREFIX = "ScriptingNormalizer_script";

		private Script compiledScript;
		private ScriptEngine scriptEngine;

		ScriptingNormalizerRuntime(MetaEntity entity, MetaProperty property) {
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptEngine = tc.getScriptEngine();

			String scriptName = null;
			for (int i = 0; i < property.getNormalizers().size(); i++) {
				if (MetaScriptingNormalizer.this == property.getNormalizers().get(i)) {
					scriptName = SCRIPT_PREFIX + "_" + entity.getId() + "_" + property.getId() + "_" + i;
					break;
				}
			}
			compiledScript = scriptEngine.createScript(script, scriptName);
		}

		@Override
		public Object normalize(Object value, ValidationContext context) {
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute(ENTITY_BINDING_NAME, context.getEntity());
			sc.setAttribute(PROPERTY_NAME_BINDING_NAME, context.getPropertyName());
			sc.setAttribute(VALUE_BINDING_NAME, value);
			sc.setAttribute(CONTEXT_BINDING_NAME, context);
			return compiledScript.eval(sc);
		}

		@Override
		public Object normalizeArray(Object[] values, ValidationContext context) {
			if (asArray) {
				return normalize(values, context);
			} else {
				return super.normalizeArray(values, context);
			}
		}
	}

}
