/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroovyObjectInputStream extends ObjectInputStream {
	private static Logger log = LoggerFactory.getLogger(GroovyObjectInputStream.class);
	
	private final GroovyScriptEngine se;

	public GroovyObjectInputStream(InputStream in) throws IOException {
		super(in);
		se = (GroovyScriptEngine) ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
	}
	
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc)
			throws IOException, ClassNotFoundException {
		try {
			return se.getSharedClassLoader().loadClass(desc.getName());
		} catch (Exception e) {
			log.debug("cant deserialize GroovyObject..., so resolve class from default class loader. cause:" + e.toString());
			return super.resolveClass(desc);
		}
	}

}
