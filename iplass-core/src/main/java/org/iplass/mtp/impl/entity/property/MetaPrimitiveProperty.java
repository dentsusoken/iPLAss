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

package org.iplass.mtp.impl.entity.property;


import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;

public class MetaPrimitiveProperty extends MetaProperty {
	private static final long serialVersionUID = 1566456340736914795L;
	
	/** プロパティの型 */
	private PropertyType type;
	
	public PropertyType getType() {
		return type;
	}

	public void setType(PropertyType type) {
		this.type = type;
	}
	
	@Override
	public void applyConfig(PropertyDefinition pDef, EntityContext context) {
		if (pDef instanceof ReferenceProperty) {
			throw new EntityRuntimeException("Illegal Type Convert. PrimitiveProperty to ReferenceProperty cannot support.");
		}
		
		fillFrom(pDef, context);
		PropertyService pService = ServiceRegistry.getRegistry().getService(PropertyService.class);
		type = pService.newPropertyType(pDef);
		if (type == null) {
			throw new EntityRuntimeException("Unsupported PropertyType." + pDef.getClass().getName());
		}
	}
	
	@Override
	public PropertyDefinition currentConfig(EntityContext context) {
		PropertyDefinition pd = type.createPropertyDefinitionInstance();
		fillTo(pd, context);
		return pd;
	}

	@Override
	public MetaPrimitiveProperty copy() {
		
		return ObjectUtil.deepCopy(this);
		
//		MetaPrimitiveProperty copy = new MetaPrimitiveProperty();
//		copyTo(copy);
//		copy.type = type.copy();
//		return copy;
	}

	@Override
	public PrimitivePropertyHandler createRuntime(MetaEntity metaEntity) {
		return new PrimitivePropertyHandler(this, metaEntity);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MetaPrimitiveProperty other = (MetaPrimitiveProperty) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
