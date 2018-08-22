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

/**
 *
 */
package org.iplass.mtp.impl.entity.property;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;

public class ReferencePropertyHandler extends PropertyHandler {
	
	private PreparedQuery asOfExpression;

	public ReferencePropertyHandler(MetaReferenceProperty meta, MetaEntity metaEntity) {
		super(meta, metaEntity);
		if (meta.getVersionControlAsOfExpression() != null) {
			asOfExpression = new PreparedQuery(meta.getVersionControlAsOfExpression());
		}
	}

	public EntityHandler getReferenceEntityHandler(EntityContext ctx) {
		return ctx.getHandlerById(((MetaReferenceProperty) metaData).getReferenceEntityMetaDataId());
	}
	
	public ValueExpression getAsOfExpression() {
		if (asOfExpression == null) {
			return null;
		}
		return asOfExpression.value(null);
	}

	/**
	 * Mapped byされた参照定義のほうから、メインの参照を取得する
	 * @param ctx
	 * @return
	 */
	public ReferencePropertyHandler getMappedByPropertyHandler(EntityContext ctx) {
		MetaReferenceProperty meta = (MetaReferenceProperty) metaData;
		if (meta.getMappedByPropertyMetaDataId() == null) {
			return null;
		}

		EntityHandler mappedByEntity = ctx.getHandlerById(meta.getReferenceEntityMetaDataId());
		if (mappedByEntity == null) {
			//参照Entityが取得できない場合はnull
			return null;
		}
		for (PropertyHandler ph: mappedByEntity.getDeclaredPropertyList()) {
			if (ph instanceof ReferencePropertyHandler) {
				if (ph.getId().equals(meta.getMappedByPropertyMetaDataId())) {
					return (ReferencePropertyHandler) ph;
				}
			}
		}
		return null;
	}

	/**
	 * メインの参照定義のほうから、逆参照を取得する。
	 * @param ctx
	 * @return
	 */
	public ReferencePropertyHandler getMappedByPropertyHandlerFromMain(EntityContext ctx) {
		MetaReferenceProperty meta = (MetaReferenceProperty) metaData;

		EntityHandler mappedByEntity = ctx.getHandlerById(meta.getReferenceEntityMetaDataId());
		if (mappedByEntity == null) {
			//参照Entityが取得できない場合はnull
			return null;
		}
		for (PropertyHandler ph: mappedByEntity.getDeclaredPropertyList()) {
			if (ph instanceof ReferencePropertyHandler) {
				if (getId().equals(((ReferencePropertyHandler) ph).getMetaData().getMappedByPropertyMetaDataId())) {
					return (ReferencePropertyHandler) ph;
				}
			}
		}
		return null;
	}

	@Override
	public MetaReferenceProperty getMetaData() {
		return (MetaReferenceProperty) metaData;
	}

	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.REFERENCE;
	}

	@Override
	public Object[] newArrayInstance(int size, EntityContext ec) {
		return getReferenceEntityHandler(ec).newArrayInstance(size);
	}

}
