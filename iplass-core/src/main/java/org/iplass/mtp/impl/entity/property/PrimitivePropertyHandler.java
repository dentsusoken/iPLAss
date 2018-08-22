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

import java.lang.reflect.Array;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;


public class PrimitivePropertyHandler extends PropertyHandler {

	private Object typeSpecificRuntime;

	public PrimitivePropertyHandler(MetaPrimitiveProperty metaData, MetaEntity metaEntity) {
		super(metaData, metaEntity);
		typeSpecificRuntime = metaData.getType().createRuntime(metaData, metaEntity);
	}

	@Override
	public MetaPrimitiveProperty getMetaData() {
		return (MetaPrimitiveProperty) metaData;
	}

	public Object getTypeSpecificRuntime() {
		return typeSpecificRuntime;
	}

	@Override
	public PropertyDefinitionType getEnumType() {
		return getMetaData().getType().getEnumType();
	}

	@Override
	public Object[] newArrayInstance(int size, EntityContext ec) {
		return (Object[]) Array.newInstance(getMetaData().getType().storeType(), size);
	}

}
