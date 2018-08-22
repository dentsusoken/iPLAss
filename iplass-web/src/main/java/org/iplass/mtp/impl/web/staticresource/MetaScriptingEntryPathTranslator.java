/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.web.staticresource;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.web.staticresource.EntryPathTranslator;
import org.iplass.mtp.web.staticresource.definition.EntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.ScriptingEntryPathTranslatorDefinition;

public class MetaScriptingEntryPathTranslator extends MetaEntryPathTranslator {
	private static final long serialVersionUID = -9124402289926024011L;

	private String script;
	
	public MetaScriptingEntryPathTranslator() {
	}
	
	public MetaScriptingEntryPathTranslator(String script) {
		this.script = script;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public EntryPathTranslator createEntryPathTranslator(String staticResourceName) {
		ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
		Script s = se.createScript(script, "ScriptingEntryPathTranslator_" + staticResourceName);
		if (s.isInstantiateAs(EntryPathTranslator.class)) {
			ScriptContext sc = se.newScriptContext();
			return s.createInstanceAs(EntryPathTranslator.class, sc);
		} else {
			return new ScriptingEntryPathTranslator(s, se);
		}
	}
	
	@Override
	public EntryPathTranslatorDefinition currentConfig() {
		ScriptingEntryPathTranslatorDefinition def = new ScriptingEntryPathTranslatorDefinition();
		def.setScript(script);
		return def;
	}
	
	private class ScriptingEntryPathTranslator implements EntryPathTranslator {
		private Script s;
		private ScriptEngine se;
		
		private ScriptingEntryPathTranslator(Script s, ScriptEngine se) {
			this.s = s;
			this.se = se;
		}

		@Override
		public String translate(String requestPath) {
			ScriptContext sc = se.newScriptContext();
			sc.setAttribute("requestPath", requestPath);
			return (String) s.eval(sc);
		}
		
	}

}
