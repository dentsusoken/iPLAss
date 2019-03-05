/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.element.property.validation;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.HasEntityProperty;
import org.iplass.mtp.view.generic.element.property.validation.PropertyValidationCondition;

/**
 * 入力チェックの条件となるプロパティ
 * @author lis3wg
 */
public class MetaPropertyValidationCondition implements MetaData, HasEntityProperty {

	private static final long serialVersionUID = 8091733440414624844L;

	/** プロパティID */
	private String propertyId;

	/**
	 * プロパティIDを取得します。
	 * @return プロパティID
	 */
	public String getPropertyId() {
	    return propertyId;
	}

	/**
	 * プロパティIDを設定します。
	 * @param propertyId プロパティID
	 */
	public void setPropertyId(String propertyId) {
	    this.propertyId = propertyId;
	}

	/**
	 * メタに保持する内容を設定する
	 * @param condition 条件
	 */
	public void applyConfig(PropertyValidationCondition condition, String definitionId) {
		EntityContext context = EntityContext.getCurrentContext();
		EntityHandler entity = context.getHandlerById(definitionId);

		this.propertyId = convertId(condition.getPropertyName(), context, entity);
	}

	/**
	 * 設定内容を条件に反映。
	 * @return 条件
	 */
	public PropertyValidationCondition currentConfig(String definitionId) {
		EntityContext context = EntityContext.getCurrentContext();
		EntityHandler entity = context.getHandlerById(definitionId);

		PropertyValidationCondition condition = null;
		String propertyName = convertName(propertyId, context, entity);
		if (propertyName != null) {
			condition = new PropertyValidationCondition();
			condition.setPropertyName(propertyName);
		}
		return condition;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
