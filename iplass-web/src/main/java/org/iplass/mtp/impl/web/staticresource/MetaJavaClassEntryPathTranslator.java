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

import org.iplass.mtp.web.staticresource.EntryPathTranslator;
import org.iplass.mtp.web.staticresource.definition.EntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.JavaClassEntryPathTranslatorDefinition;

public class MetaJavaClassEntryPathTranslator extends MetaEntryPathTranslator {
	private static final long serialVersionUID = -1806091437752501895L;

	private String className;
	
	public MetaJavaClassEntryPathTranslator() {
	}
	
	public MetaJavaClassEntryPathTranslator(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	@Override
	public EntryPathTranslator createEntryPathTranslator(String staticResourceName) {
		try {
			return (EntryPathTranslator) Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public EntryPathTranslatorDefinition currentConfig() {
		JavaClassEntryPathTranslatorDefinition def = new JavaClassEntryPathTranslatorDefinition();
		def.setClassName(className);
		return def;
	}

}
