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

package org.iplass.mtp.impl.entity.versioning;

import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

public interface VersionController {
	
	public void normalizeForInsert(Entity entity, InsertOption option, EntityContext entityContext);
	
	public Entity[] normalizeRefEntity(Entity[] refEntity, ReferencePropertyHandler rph, EntityContext context);
	
	public void update(Entity entity, UpdateOption option, EntityHandler eh, EntityContext entityContext);
	
	public Entity[] getCascadeDeleteTargetForUpdate(Entity[] refEntity, Entity[] beforeRefEntity, ReferencePropertyHandler rph, Entity beforeEntity, EntityHandler eh, EntityContext entityContext);
	
	public DeleteTarget[] getDeleteTarget(Entity entity, DeleteOption option, EntityHandler eh, EntityContext entityContext);
	
	public String[] getCascadeDeleteTarget(Entity entity, EntityHandler eh, ReferencePropertyHandler rph, EntityContext entityContext);

	public Condition mainQueryCondition(EntityHandler eh, AsOf asOf, EntityContext context);
	
	public Condition refEntityQueryCondition(String refPropPath, ReferencePropertyHandler rph, AsOf asOf, EntityContext context);

	//いまのとこ必要なしか？
//	public int updateAll(EntityHandler eh);
//	public int deleteAll(EntityHandler eh);


}
