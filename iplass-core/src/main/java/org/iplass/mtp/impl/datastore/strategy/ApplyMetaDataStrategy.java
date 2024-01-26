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

package org.iplass.mtp.impl.datastore.strategy;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;

public interface ApplyMetaDataStrategy extends StoreStrategy {
	public void create(MetaEntity definition, EntityContext context);
//	public void create(MetaEntity definition, EntityContext context, int version);
//	public void createSystemDefault(MetaEntity definition);

	public boolean isLocked(MetaEntity definition, EntityContext context);
	public boolean prepare(MetaEntity newOne, MetaEntity previous, EntityContext context);
	public boolean modify(MetaEntity newOne, MetaEntity previous, EntityContext context, int[] targetTenantIds);
	public void finish(boolean modifyResult, MetaEntity newOne, MetaEntity previous, EntityContext context);

	public void patchData(MetaEntity newOne, MetaEntity previous, EntityContext context, int targetTenantId);

	public boolean defrag(MetaEntity target, EntityContext context, int[] targetTenantIds);

}
