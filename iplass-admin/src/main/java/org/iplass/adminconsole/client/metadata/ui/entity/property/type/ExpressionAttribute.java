/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;

public class ExpressionAttribute implements PropertyAttribute {

	private ExpressionProperty attribute;
	private String globalSelectName;

	public ExpressionAttribute() {
		attribute = new ExpressionProperty();
	}

	@Override
	public void applyFrom(PropertyDefinition property, EntityDefinition entity) {

		ExpressionProperty expression = (ExpressionProperty) property;

		setExpression(expression.getExpression());
		if (expression.getResultType() != null) {
			setResultType(expression.getResultType());
		} else {
			setResultType(null);
		}
		if (PropertyDefinitionType.SELECT == expression.getResultType()) {
			if (expression.getResultTypeSpec() != null
					&& expression.getResultTypeSpec() instanceof SelectProperty) {
				SelectProperty sp = (SelectProperty)expression.getResultTypeSpec();
				setGlobalSelectName(sp.getSelectValueDefinitionName());
			} else {
				setGlobalSelectName(null);
			}
		} else {
			setGlobalSelectName(null);
		}

	}

	@Override
	public void applyTo(PropertyDefinition property, EntityDefinition entity) {

		ExpressionProperty expression = (ExpressionProperty)property;

		expression.setExpression(getExpression());
		if (getResultType() != null) {
			expression.setResultType(getResultType());
		}
		if (PropertyDefinitionType.SELECT == getResultType()) {
			if (SmartGWTUtil.isNotEmpty(getGlobalSelectName())) {
				SelectProperty sp = new SelectProperty();
				sp.setSelectValueDefinitionName(getGlobalSelectName());
				expression.setResultTypeSpec(sp);
			} else {
				expression.setResultTypeSpec(null);
			}
		} else {
			expression.setResultTypeSpec(null);
		}

	}

	@Override
	public void applyTo(PropertyListGridRecord record) {
		record.setRemarks(getExpression());
	}

	public String getExpression() {
		return attribute.getExpression();
	}

	public void setExpression(String value) {
		attribute.setExpression(value);
	}

	public PropertyDefinitionType getResultType() {
		return attribute.getResultType();
	}

	public void setResultType(PropertyDefinitionType value) {
		attribute.setResultType(value);
	}

	public String getGlobalSelectName() {
		return globalSelectName;
	}

	public void setGlobalSelectName(String value) {
		globalSelectName = value;
	}

}
