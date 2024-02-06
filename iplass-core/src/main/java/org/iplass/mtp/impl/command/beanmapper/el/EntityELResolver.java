/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.command.beanmapper.el;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Array;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

public class EntityELResolver extends ELResolver {
	
	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (!checkArgs(context, base, property)) {
			return null;
		}
		
		Entity entity = (Entity) base;
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(entity.getDefinitionName());
		if (eh == null) {
			return null;
		}
		
		String propName = property.toString();
		PropertyHandler ph = eh.getProperty(propName, ec);
		if (ph != null) {
			context.setPropertyResolved(base, property);
			
			BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
			Object value = entity.getValue(propName);
			if (value == null) {
				if (bmc.getElMapper().isAutoGrow()) {
					if (ph instanceof ReferencePropertyHandler) {
						ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
						if (rph.getMetaData().getMultiplicity() == 1) {
							value = rph.getReferenceEntityHandler(ec).newInstance();
						} else {
							value = rph.newArrayInstance(0, ec);
						}
					}
					
					if (value != null) {
						entity.setValue(propName, value);
					}
					
				}
			}
			
			if (bmc.getElMapper().isAutoGrow() && ph instanceof ReferencePropertyHandler) {
				bmc.setPropertyRef(base, (ReferencePropertyHandler) ph, value);
			}
			
			return value;
		} else {
            throw new PropertyNotFoundException("Entity:" + entity.getDefinitionName() + " property:" + propName + " not defined.");
		}
	}
	
	private boolean checkArgs(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (base == null || property == null){
			return false;
		}
		if (!(base instanceof Entity)) {
			return false;
		}
		Entity entity = (Entity) base;
		if (entity.getDefinitionName() == null) {
			return false;
		}
		
		return true;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (!checkArgs(context, base, property)) {
			return null;
		}
		
		Entity entity = (Entity) base;
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(entity.getDefinitionName());
		if (eh == null) {
			return null;
		}
		
		String propName = property.toString();
		PropertyHandler ph = eh.getProperty(propName, ec);
		if (ph != null) {
			context.setPropertyResolved(base, property);
			Class<?> t = ph.getEnumType().getJavaType();
			if (ph.getMetaData().getMultiplicity() != 1) {
				t = Array.newInstance(t, 0).getClass();
			}
			return t;
		} else {
            throw new PropertyNotFoundException("Entity:" + entity.getDefinitionName() + " property:" + propName + " not defined.");
		}
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		if (!checkArgs(context, base, property)) {
			return;
		}
		
		Entity entity = (Entity) base;
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(entity.getDefinitionName());
		if (eh == null) {
			return;
		}
		
		String propName = property.toString();
		PropertyHandler ph = eh.getProperty(propName, ec);
		if (ph != null) {
			entity.setValue(propName, value);
			context.setPropertyResolved(base, property);
		} else {
            throw new PropertyNotFoundException("Entity:" + entity.getDefinitionName() + " property:" + propName + " not defined.");
		}
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		return false;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if (base == null) {
			return null;
		}
		return Object.class;
	}

}
