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

package org.iplass.adminconsole.shared.metadata.dto.staticresource;

import org.iplass.mtp.web.staticresource.definition.EntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.JavaClassEntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.PrefixEntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.ScriptingEntryPathTranslatorDefinition;

public enum EntryPathType {
	JAVA("Java", JavaClassEntryPathTranslatorDefinition.class),
	PREFIX("Prefix", PrefixEntryPathTranslatorDefinition.class),
	SCRIPT("Script", ScriptingEntryPathTranslatorDefinition.class);

	private final String displayName;
	private final Class<? extends EntryPathTranslatorDefinition> definitionClass;

	private EntryPathType(String displayName, Class<? extends EntryPathTranslatorDefinition> definitionClass) {
		this.displayName = displayName;
		this.definitionClass = definitionClass;
	}

	public String displayName() {
		return displayName;
	}

	public Class<? extends EntryPathTranslatorDefinition> definitionClass() {
		return definitionClass;
	}

	public static EntryPathType valueOf(EntryPathTranslatorDefinition definition) {
		for (EntryPathType type : values()) {
			if (definition.getClass().getName().equals(type.definitionClass().getName())) {
				return type;
			}
		}
		return null;
	}

	public static EntryPathTranslatorDefinition typeOfDefinition(EntryPathType type) {
		if (type.definitionClass().equals(JavaClassEntryPathTranslatorDefinition.class)) {
			return new JavaClassEntryPathTranslatorDefinition();
		} else if (type.definitionClass().equals(PrefixEntryPathTranslatorDefinition.class)) {
			return new PrefixEntryPathTranslatorDefinition();
		} else if (type.definitionClass().equals(ScriptingEntryPathTranslatorDefinition.class)) {
			return new ScriptingEntryPathTranslatorDefinition();
		}
		return null;
	}

	public static String getEntryPath(EntryPathTranslatorDefinition definition) {
		EntryPathType type = valueOf(definition);
		switch (type) {
		case JAVA:
			return ((JavaClassEntryPathTranslatorDefinition) definition).getClassName();
		case PREFIX:
			return ((PrefixEntryPathTranslatorDefinition) definition).getPrefix();
		case SCRIPT:
			return ((ScriptingEntryPathTranslatorDefinition) definition).getScript();
		}
		return null;
	}
}
