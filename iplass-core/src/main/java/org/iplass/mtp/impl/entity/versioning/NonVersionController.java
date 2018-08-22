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

package org.iplass.mtp.impl.entity.versioning;

import java.util.ArrayList;

import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.interceptor.EntityLoadInvocationImpl;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

public class NonVersionController implements VersionController {
	
	@Override
	public void normalizeForInsert(Entity entity, EntityContext entityContext) {
		entity.setVersion(Long.valueOf(0));//※Insertはバージョン方式に関係なく0
	}

	@Override
	public void update(Entity entity, UpdateOption option, EntityHandler eh, EntityContext entityContext) {
		entity.setVersion(Long.valueOf(0));
		eh.updateDirect(entity, option, entityContext);
	}

	@Override
	public DeleteTarget[] getDeleteTarget(Entity entity, DeleteOption option, EntityHandler eh, EntityContext entityContext) {
		return new DeleteTarget[] {
				new DeleteTarget(entity.getOid(), Long.valueOf(0), entity.getUpdateDate())};
	}

	@Override
	public String[] getCascadeDeleteTarget(Entity entity,
			EntityHandler eh, ReferencePropertyHandler rph,
			EntityContext entityContext) {
		//キャッシュされているもの取得
		Entity loadedEntity = new EntityLoadInvocationImpl(entity.getOid(), Long.valueOf(0), null, false, eh.getService().getInterceptors(), eh).proceed();
		if (loadedEntity != null) {
			if (rph.getMetaData().getMultiplicity() == 1) {
				Entity cascade = loadedEntity.getValue(rph.getName());
				if (cascade != null) {
					return new String[]{
							cascade.getOid()
					};
				}
			} else {
				Entity[] cascade = loadedEntity.getValue(rph.getName());
				if (cascade != null && cascade.length != 0) {
					String[] oids = new String[cascade.length];
					for (int i = 0; i < cascade.length; i++) {
						oids[i] = cascade[i].getOid();
					}
					return oids;
				}
			}
		}
		return null;
	}
	
	@Override
	public Condition mainQueryCondition(EntityHandler eh, AsOf asOf, EntityContext context) {
		return new Equals(Entity.VERSION, Long.valueOf(0));
	}
	
	@Override
	public Condition refEntityQueryCondition(String refPropPath, ReferencePropertyHandler rph, AsOf asOf, EntityContext context) {
		return null;
	}

	@Override
	public Entity[] normalizeRefEntity(Entity[] refEntity,
			ReferencePropertyHandler rph, EntityContext context) {
		if (refEntity == null) {
			return null;
		}
		Long zero = Long.valueOf(0);
		for (Entity e: refEntity) {
			if (e != null) {
				e.setVersion(zero);
			}
		}
		return refEntity;
	}

	@Override
	public Entity[] getCascadeDeleteTargetForUpdate(Entity[] refEntity,
			Entity[] beforeRefEntity, ReferencePropertyHandler rph,
			Entity beforeEntity, EntityHandler eh, EntityContext entityContext) {
		
		ArrayList<Entity> ret = new ArrayList<Entity>();
		for (Entity e: beforeRefEntity) {
			if (e != null) {
				if (refEntity == null) {
					ret.add(e);
				} else {
					boolean match = false;
					for (Entity newE: refEntity) {
						if (newE != null && newE.getOid().equals(e.getOid())) {
							match = true;
							break;
						}
					}
					if (!match) {
						ret.add(e);
					}
				}
			}
		}
		
		if (ret.size() == 0) {
			return null;
		} else {
			return ret.toArray(new Entity[ret.size()]);
		}
	}


}
