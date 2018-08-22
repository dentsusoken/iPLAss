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

package org.iplass.mtp.impl.properties.extend;

import org.iplass.mtp.impl.entity.property.PropertyType;

public abstract class WrapperType extends ExtendType {
	private static final long serialVersionUID = 6993499753695522323L;

	public abstract PropertyType actualType();

	@Override
	public boolean isVirtual() {
		return false;
	}
	
	
	//public abstract ValueExpression translate(EntityField field, VirtualPropertyNormalizer normalizer);
	//FIXME 変換メソッド次の3つ定義
	//select句での変換メソッド
	//where句での変換メソッド
	//Sortでの変換メソッド
	
	
	
}
