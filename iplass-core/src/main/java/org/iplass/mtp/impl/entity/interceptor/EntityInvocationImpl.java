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

package org.iplass.mtp.impl.entity.interceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.entity.interceptor.EntityInvocation;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;


public abstract class EntityInvocationImpl<R> implements EntityInvocation<R> {

	public static EntityInterceptor[] NULL_ENTITY_INTERCEPTOR = new EntityInterceptor[0];

	private EntityInterceptor[] entityInterceptors;
	private EntityHandler entityHandler;
	private int index;
	private HashMap<String, Object> invocationContextAttributes;

	public EntityInvocationImpl(EntityInterceptor[] entityInterceptors, EntityHandler entityHandler) {
		if (entityInterceptors != null) {
			this.entityInterceptors = entityInterceptors;
		} else {
			this.entityInterceptors = NULL_ENTITY_INTERCEPTOR;
		}
		this.entityHandler = entityHandler;
		index = -1;
	}

	//サブクラスでimplementsするテンプレートメソッド
	protected abstract R callEntityHandler(EntityHandler eh);

	@SuppressWarnings("unchecked")
	public R proceed() {
		index++;
		try {
			if (index == entityInterceptors.length) {
				return callEntityHandler(entityHandler);
			} else {
				return (R) entityInterceptors[index].intercept(this);
			}
		} finally {
			index--;
		}
	}

	@Override
	public EntityDefinition getEntityDefinition() {
		return entityHandler.getMetaData().currentConfig(EntityContext.getCurrentContext());
	}

	public EntityHandler getEntityHandler() {
		return entityHandler;
	}

	public void setAttribute(String name, Object value) {
		if (invocationContextAttributes == null) {
			invocationContextAttributes = new HashMap<String, Object>();
		}
		invocationContextAttributes.put(name, value);
	}

	public Object getAttribute(String name) {
		if (invocationContextAttributes == null) {
			return null;
		}
		return invocationContextAttributes.get(name);
	}
	public Iterator<String> getAttributeNames() {
		if (invocationContextAttributes == null) {
			List<String> l = Collections.emptyList();
			return l.iterator();
		}
		return invocationContextAttributes.keySet().iterator();
	}


}
