/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.web.staticresource.EntryPathTranslator;
import org.iplass.mtp.web.staticresource.definition.EntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.JavaClassEntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.PrefixEntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.ScriptingEntryPathTranslatorDefinition;

@XmlSeeAlso({MetaJavaClassEntryPathTranslator.class,
	MetaPrefixEntryPathTranslator.class,
	MetaScriptingEntryPathTranslator.class})
public abstract class MetaEntryPathTranslator implements MetaData {
	private static final long serialVersionUID = 4317005560481835285L;
	
	public static MetaEntryPathTranslator toMeta(EntryPathTranslatorDefinition def) {
		if  (def == null) {
			return null;
		}
		if (def instanceof JavaClassEntryPathTranslatorDefinition) {
			return new MetaJavaClassEntryPathTranslator(((JavaClassEntryPathTranslatorDefinition) def).getClassName());
		}
		if (def instanceof PrefixEntryPathTranslatorDefinition) {
			return new MetaPrefixEntryPathTranslator(((PrefixEntryPathTranslatorDefinition) def).getPrefix());
		}
		if (def instanceof ScriptingEntryPathTranslatorDefinition) {
			return new MetaScriptingEntryPathTranslator(((ScriptingEntryPathTranslatorDefinition) def).getScript());
		}
		return null;
	}

	public abstract EntryPathTranslator createEntryPathTranslator(String staticResourceName);

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	public abstract EntryPathTranslatorDefinition currentConfig();
	
}
