/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.listener;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.EntityEventListener;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.EventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.JavaClassEventListenerDefinition;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.MetaEventListener;

public class MetaJavaClassEventListener extends MetaEventListener {
	private static final long serialVersionUID = 817043706754815712L;
	
	private String className;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public void applyConfig(EventListenerDefinition def) {
		fillFrom(def);
		JavaClassEventListenerDefinition d = (JavaClassEventListenerDefinition) def;
		className = d.getClassName();
	}

	@Override
	public MetaEventListener copy() {
		MetaJavaClassEventListener copy = new MetaJavaClassEventListener();
		copyTo(copy);
		copy.className = className;
		return copy;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaJavaClassEventListener other = (MetaJavaClassEventListener) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		return true;
	}

	@Override
	public EventListenerDefinition currentConfig() {
		JavaClassEventListenerDefinition d = new JavaClassEventListenerDefinition();
		fillTo(d);
		d.setClassName(className);
		return d;
	}

	@Override
	public JavaClassEventListenerRuntime createRuntime(MetaEntity entity) {
		return new JavaClassEventListenerRuntime();
	}
	
	
	
	public class JavaClassEventListenerRuntime extends EventListenerRuntime {
		
		private EntityEventListener listener;
		
		public JavaClassEventListenerRuntime() {
			try {
				listener = (EntityEventListener) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				throw new EntityRuntimeException("can not instantiate " + className, e);
			} catch (IllegalAccessException e) {
				throw new EntityRuntimeException("can not instantiate " + className, e);
			} catch (ClassNotFoundException e) {
				throw new EntityRuntimeException("class not found:" + className, e);
			}
		}

		@Override
		public void handleAfterDelete(Entity entity, EntityEventContext context) {
			listener.afterDelete(entity, context);
		}

		@Override
		public void handleAfterInsert(Entity entity, EntityEventContext context) {
			listener.afterInsert(entity, context);
		}

		@Override
		public void handleAfterUpdate(Entity entity, EntityEventContext context) {
			listener.afterUpdate(entity, context);
		}

		@Override
		public boolean handleBeforeDelete(Entity entity, EntityEventContext context) {
			return listener.beforeDelete(entity, context);
		}

		@Override
		public boolean handleBeforeInsert(Entity entity, EntityEventContext context) {
			return listener.beforeInsert(entity, context);
		}

		@Override
		public boolean handleBeforeUpdate(Entity entity, EntityEventContext context) {
			return listener.beforeUpdate(entity, context);
		}

		@Override
		public void handleOnLoad(Entity entity) {
			listener.onLoad(entity);
		}

		public MetaJavaClassEventListener getMetaData() {
			return MetaJavaClassEventListener.this;
		}

		@Override
		public void handleBeforeValidate(Entity entity, EntityEventContext context) {
			listener.beforeValidate(entity, context);
		}

		@Override
		public void handleAfterRestore(Entity entity) {
			listener.afterRestore(entity);
		}
		
		@Override
		public void handleAfterPurge(Entity entity) {
			listener.afterPurge(entity);
		}
	}
	

}
