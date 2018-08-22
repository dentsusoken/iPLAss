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

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.entity.interceptor.EntityLoadInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.impl.entity.EntityHandler;

public class EntityLoadInvocationImpl extends EntityInvocationImpl<Entity> implements EntityLoadInvocation {
	
	private String oid;
	private Long version;
	private LoadOption loadOption;
	private boolean withLock;
	
	public EntityLoadInvocationImpl(String oid, Long version, LoadOption loadOption, boolean withLock, EntityInterceptor[] entityInterceptors,
			EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.oid = oid;
		this.version = version;
		this.loadOption = loadOption;
		this.withLock = withLock;
	}
	
	public LoadOption getLoadOption() {
		return loadOption;
	}

	public void setLoadOption(LoadOption loadOption) {
		this.loadOption = loadOption;
	}

	public Long getVersion() {
		return version;
	}
	
	public void setVersion(Long version) {
		this.version = version;
	}

	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public boolean withLock() {
		return withLock;
	}

	@Override
	protected Entity callEntityHandler(EntityHandler eh) {
		return eh.load(oid, version, loadOption, withLock);
	}

	@Override
	public InvocationType getType() {
		return InvocationType.LOAD;
	}
	
}
