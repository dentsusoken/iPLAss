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

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;


public abstract class ComplexWrapperType extends WrapperType {
	private static final long serialVersionUID = 1990458928686520536L;

	public abstract ComplexWrapperTypeLoadAdapter createLoadAdapter();

	public abstract boolean isNeedPrevStoreTypeValueOnToStoreTypeValue();
	public abstract Object toStoreTypeValue(Object extendTypeValue, Object prevStoreTypeValue, PropertyHandler ph, EntityHandler eh, String oid, Long version, Entity entity);

	public abstract void notifyAfterDelete(Object storeTypeValue, PropertyHandler ph, EntityHandler eh, String oid, Long rbid);
	public abstract void notifyAfterPurge(EntityHandler eh, Long rbid);
	public abstract void notifyAfterRestore(EntityHandler eh, Long rbid);

	@Override
	public Object fromDataStore(Object fromDataStore) {
		return actualType().fromDataStore(fromDataStore);
	}

	@Override
	public Object toDataStore(Object toDataStore) {
		return actualType().toDataStore(toDataStore);
	}

	@Override
	public Class<?> storeType() {
		return actualType().storeType();
	}

	//FIXME 利用してる？？
	public abstract Class<?> extendType();

	//FIXME Vertual Typeとやってること似てる。うまく統合できないか？？
	public abstract ValueExpression translate(EntityField field);

}
