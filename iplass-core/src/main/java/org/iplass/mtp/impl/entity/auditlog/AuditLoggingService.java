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

package org.iplass.mtp.impl.entity.auditlog;

import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.spi.Service;

public interface AuditLoggingService extends Service {

	public static final String ACTION_INSERT = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_UPDATE_ALL = "updateAll";
	public static final String ACTION_DELETE = "delete";
	public static final String ACTION_DELETE_ALL = "deleteAll";
	public static final String ACTION_PURGE = "purge";
	public static final String ACTION_RESTORE = "restore";
	public static final String ACTION_QUERY = "query";
	public static final String ACTION_COUNT_QUERY = "countQuery";
	public static final String ACTION_BULK_UPDATE = "bulkUpdate";


	public void logQuery(Query query, boolean isCount);
	public void logInsert(Entity entity);
	public boolean isLogBeforeEntity(String definitionName);
	public void logUpdate(Entity beforeEntity, Entity entity, UpdateOption option);
	public void logUpdateAll(UpdateCondition cond);
	public void logDelete(Entity entity, DeleteOption option);
	public void logDeleteAll(DeleteCondition cond);
	public void logPurge(Long rbid);
	public void logRestore(String oid, String defName, Long rbid);
	public void logBulkUpdate(String defName);

}
