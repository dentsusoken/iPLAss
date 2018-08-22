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

package org.iplass.mtp.impl.script.template;

import java.io.Writer;
import java.util.Map;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;

import groovy.lang.Binding;

/**
 * GroovyTemplate用のBinding。
 * デフォルトのバインディングとして以下のものを持つ。<br>
 * out : Writer<br>
 * em : EntityManager<br>
 * edm : EntityDefinitionManager<br>
 * auth : AuthContext<br>
 *
 * @author K.Higuchi
 *
 */
public class GroovyTemplateBinding extends Binding {
	
	public GroovyTemplateBinding(Writer out) {
		setVariable("out", out);
		ManagerLocator sl = ManagerLocator.getInstance();
		setVariable("em", sl.getManager(EntityManager.class));
		setVariable("edm", sl.getManager(EntityDefinitionManager.class));
		setVariable("auth", AuthContext.getCurrentContext());
	}

	public GroovyTemplateBinding(Writer out, Map<String, Object> bindings) {
		this(out);
		if (bindings != null) {
			for (Map.Entry<String, Object> e: bindings.entrySet()) {
				setVariable(e.getKey(), e.getValue());
			}
		}
	}
}
