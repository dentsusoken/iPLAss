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

package org.iplass.mtp.impl.web;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.script.GroovyObjectSerializeWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.GroovyObject;


public class SimpleSessionContext implements SessionContext, Serializable {
	private static final long serialVersionUID = -8791351188424167826L;
	
	public static final String KEY_FOR_HTTP_SERVLET_REQUEST = "org.iplass.mtp.sessionContext";
	
	private static Logger logger = LoggerFactory.getLogger(SimpleSessionContext.class);
	
	private Map<String, Object> values = new ConcurrentHashMap<>();
	private transient volatile boolean isUpdate = false;

	@Override
	public Object getAttribute(String name) {
		Object val = values.get(name);
		if (val instanceof GroovyObjectSerializeWrapper) {
			return ((GroovyObjectSerializeWrapper) val).getObject();
		} else {
			return val;
		}
	}

	@Override
	public void setAttribute(String name, Object value) {
		if (value == null) {
			removeAttribute(name);
			return;
		}
		if (!(value instanceof Serializable)
				&& !(value instanceof Externalizable)) {
			logger.warn("name:" + name + ", value:" + value + " is not Serializable/Externalizable.");
		}
		
		if (value instanceof GroovyObject) {
			values.put(name, new GroovyObjectSerializeWrapper((GroovyObject) value));
		} else {
			values.put(name, value);
		}
		isUpdate = true;
	}

	@Override
	public void removeAttribute(String name) {
		Object val = values.remove(name);
		if (val != null) {
			isUpdate = true;
		}
	}

	@Override
	public Iterator<String> getAttributeNames() {
		return values.keySet().iterator();
	}

	public boolean isUpdate() {
		return isUpdate;
	}
	
	public void resetUpdateFlag() {
		isUpdate = false;
	}

	@Override
	public BinaryReference loadFromTemporary(long lobId) {
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		BinaryReference br = em.loadBinaryReference(lobId);
		if (br.getDefinitionName() != null) {
			//すでに永続化されている
			return null;
		} else {
			return br;
		}
	}
}
