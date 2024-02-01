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

package org.iplass.mtp.impl.entity.property;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.rdb.adapter.RdbTypeAdapter;

//public class MetaMetaProperty extends BaseRootMetaData { FIXME 未使用、一旦MetaDataに
public class MetaMetaProperty implements MetaData {
	private static final long serialVersionUID = 5255856138532385269L;

	private Class<?> propertyType;
	public Class<?> getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}
	private RdbTypeAdapter rdbTypeMapping;

	public RdbTypeAdapter getRdbTypeMapping() {
		return rdbTypeMapping;
	}
	public void setRdbTypeMapping(RdbTypeAdapter rdbTypeMapping) {
		this.rdbTypeMapping = rdbTypeMapping;
	}

	public MetaMetaProperty copy() {
		// TODO Auto-generated method stub
		return null;
	}
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		// TODO Auto-generated method stub
		return null;
	}

}
