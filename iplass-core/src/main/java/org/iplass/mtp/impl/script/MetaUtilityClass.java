/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.script;

import org.codehaus.groovy.GroovyBugError;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataIllegalStateException;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinition;

import groovy.lang.GroovyClassLoader;

/**
 * 汎用のScript（クラス）登録用のメタデータ。
 *
 * @author K.Higuchi
 */
public class MetaUtilityClass extends BaseRootMetaData implements DefinableMetaData<UtilityClassDefinition> {
	private static final long serialVersionUID = 3358601853577028302L;

	private String script;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public MetaUtilityClass copy() {
		MetaUtilityClass copy = new MetaUtilityClass();
		copy.id = id;
		copy.name = name;
		copy.displayName = displayName;
		copy.description = description;
		copy.script = script;
		return copy;
	}

	public UtilityClassDefinition currentConfig() {
		UtilityClassDefinition def = new UtilityClassDefinition();
		def.setName(name);
		def.setDisplayName(displayName);
		def.setDescription(description);
		def.setScript(script);
		return def;
	}

	public void applyConfig(UtilityClassDefinition definition) {
		name = convertName(definition.getName());
		displayName = definition.getDisplayName();
		description = definition.getDescription();
		script = definition.getScript();
	}

	@Override
	public UtilityClassRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new UtilityClassRuntime();
	}

	public class UtilityClassRuntime extends BaseMetaDataRuntime {

		public UtilityClassRuntime() {
		}


		@Override
		public void checkState() throws MetaDataIllegalStateException {
			//test compile
			try {
				if (script != null) {
					TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
					GroovyScriptEngine gse = (GroovyScriptEngine) tc.getScriptEngine();
					GroovyClassLoader tempcl = new GroovyClassLoader(gse.getSharedClassLoader());

					new GroovyScript(tempcl, script, name);
					tempcl.clearCache();
				}
			} catch (RuntimeException e) {
				throw new MetaDataIllegalStateException(e.getMessage(), e);
			} catch (GroovyBugError | NoClassDefFoundError e) {
				throw new MetaDataIllegalStateException(e.getMessage(), e);
			}
		}


//		public void compile(GroovyClassLoader cl) {
//			checkState();
//			if (script != null) {
//				GroovyCodeSource codeSource = new GroovyCodeSource(script, name + ".groovy", GroovyScript.CODE_BASE);
//				codeSource.setCachable(false);
//				cl.parseClass(codeSource);
//			}
//		}

		@Override
		public MetaUtilityClass getMetaData() {
			return MetaUtilityClass.this;
		}

	}

	/**
	 * UtilityClassのname区切りは「/」ではなく「.」とする。
	 * MetaUtilityClass、UtilityClassDefinitionのnameセット時にチェックし変換する。（念のため）
	 *
	 * @param name
	 * @return
	 */
	private String convertName(String name) {
		return name.replace("/", ".");
	}

}
