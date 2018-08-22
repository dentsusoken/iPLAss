/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.async;

import org.iplass.mtp.async.AsyncTaskContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.tenant.Tenant;

public class AsyncTaskContextImpl extends AsyncTaskContext {
	
	public static final String EXE_CONTEXT_ATTR_NAME = "mtp.async.AsyncTaskContext";
	
	private final long taskId;
	private final String queueName;
	
	public AsyncTaskContextImpl(long taskId, String queueName) {
		this.taskId = taskId;
		this.queueName = queueName;
	}

	@Override
	public long getTaskId() {
		return taskId;
	}

	@Override
	public String getQueueName() {
		return queueName;
	}

	@Override
	public Tenant getTenant() {
		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		return ObjectUtil.deepCopy(tenant);
	}
	
}
