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

package org.iplass.mtp.impl.entity;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.definition.EventListenerDefinition;
import org.iplass.mtp.impl.entity.listener.MetaJavaClassEventListener;
import org.iplass.mtp.impl.entity.listener.MetaScriptingEventListener;
import org.iplass.mtp.impl.entity.listener.MetaSendNotificationEventListener;
import org.iplass.mtp.impl.metadata.MetaData;

@XmlSeeAlso({MetaJavaClassEventListener.class, MetaScriptingEventListener.class, MetaSendNotificationEventListener.class})
public abstract class MetaEventListener implements MetaData {
	private static final long serialVersionUID = 3146246621777839273L;
	
	private boolean withoutMappedByReference;
	
	public boolean isWithoutMappedByReference() {
		return withoutMappedByReference;
	}

	public void setWithoutMappedByReference(boolean withoutMappedByReference) {
		this.withoutMappedByReference = withoutMappedByReference;
	}
	
	protected void copyTo(MetaEventListener l) {
		l.withoutMappedByReference = withoutMappedByReference;
	}
	
	protected void fillFrom(EventListenerDefinition def) {
		this.withoutMappedByReference = def.isWithoutMappedByReference();
	}
	
	protected void fillTo(EventListenerDefinition def) {
		def.setWithoutMappedByReference(withoutMappedByReference);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (withoutMappedByReference ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaEventListener other = (MetaEventListener) obj;
		if (withoutMappedByReference != other.withoutMappedByReference)
			return false;
		return true;
	}

	public abstract MetaEventListener copy();
	
	public abstract void applyConfig(EventListenerDefinition def);
	
	public abstract EventListenerDefinition currentConfig();
	
	public abstract EventListenerRuntime createRuntime(MetaEntity entity);
	
	public abstract class EventListenerRuntime /*implements MetaDataRuntime*/ {
		
		public abstract void handleAfterDelete(Entity entity, EntityEventContext context);
		public abstract void handleAfterInsert(Entity entity, EntityEventContext context);
		public abstract void handleAfterUpdate(Entity entity, EntityEventContext context);
		public abstract boolean handleBeforeDelete(Entity entity, EntityEventContext context);
		public abstract boolean handleBeforeInsert(Entity entity, EntityEventContext context);
		public abstract boolean handleBeforeUpdate(Entity entity, EntityEventContext context);
		public abstract void handleOnLoad(Entity entity);
		public abstract void handleBeforeValidate(Entity entity, EntityEventContext context);
		//TODO insertとupdateのvalidate分ける
		public abstract void handleAfterRestore(Entity entity);
		public abstract void handleAfterPurge(Entity entity);
		
		public MetaEventListener getMetaData() {
			return MetaEventListener.this;
		}
	}
}
