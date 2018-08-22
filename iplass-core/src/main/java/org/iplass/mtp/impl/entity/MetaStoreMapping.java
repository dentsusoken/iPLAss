/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.definition.StoreDefinition;
import org.iplass.mtp.entity.definition.stores.SchemalessRdbStore;
import org.iplass.mtp.impl.metadata.MetaData;

@XmlSeeAlso(value={MetaSchemalessRdbStoreMapping.class})
public abstract class MetaStoreMapping implements MetaData {
	private static final long serialVersionUID = 8801893617236966214L;
	
	public static MetaStoreMapping newInstance(StoreDefinition def) {
		if (def instanceof SchemalessRdbStore) {
			return new MetaSchemalessRdbStoreMapping();
		}
		return null;
	}
	
	public abstract StoreDefinition currentConfig(MetaEntity metaEntity);
	public abstract void applyConfig(StoreDefinition def, MetaEntity metaEntity);

}
