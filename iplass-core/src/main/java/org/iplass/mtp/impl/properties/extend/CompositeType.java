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

package org.iplass.mtp.impl.properties.extend;

import org.iplass.mtp.impl.entity.property.PropertyType;

public abstract class CompositeType extends ExtendType {
	
	//TODO 実装
	private static final long serialVersionUID = -360288591367002480L;

	public abstract String[] fieldNames();
	
	public abstract PropertyType[] actualType();
	
	public abstract Object composite(Object[] values);
	
	public abstract Object[] extract(Object value);

}
