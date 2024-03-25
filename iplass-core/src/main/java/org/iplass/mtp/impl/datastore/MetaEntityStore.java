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

package org.iplass.mtp.impl.datastore;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.definition.StoreDefinition;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaData;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value={MetaGRdbEntityStore.class})
public abstract class MetaEntityStore implements MetaData {
	private static final long serialVersionUID = -7869335212218718713L;
	
//	private Map<String, EntityStorePropertyDefinition> propMap;

	public MetaEntityStore() {
//		propMap = new HashMap<String, EntityStorePropertyDefinition>();
	}

	public abstract MetaEntityStore copy();
	
	public abstract EntityStoreRuntime createRuntime(EntityHandler eh);

	@Deprecated
	public abstract void applyConfig(StoreDefinition storeDefinition, MetaEntity metaEntity);

	@Deprecated
	public abstract StoreDefinition currentConfig(MetaEntity metaEntity);
	
//	public EntityStorePropertyDefinition getPropertyDefinitionBy(String name) {
//		return propMap.get(name);
//	}
//	
//	public void addPropertyDefinition(String name, EntityStorePropertyDefinition propDef) {
//		propMap.put(name, propDef);
//	}
//	
//	public List<EntityStorePropertyDefinition> getPropertyDefinitionList() {
//		//TODO 毎回コピーするのはいかがなものか。。。
//		return new ArrayList<EntityStorePropertyDefinition>(propMap.values());
//	}

}
