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

package org.iplass.mtp.entity.definition.properties;

import java.math.BigDecimal;

import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

/**
 * 数式により、プロパティの演算結果を返す、仮想的なプロパティを表す定義。
 * 数式はEQL形式（ValueExpressionとして有効な式）にて記述。
 * 自Entityの属性、関連定義されたEntityの属性を参照可能。
 * また、相関サブクエリの指定も可能。
 * 
 * @author K.Higuchi
 *
 */
public class ExpressionProperty extends PropertyDefinition {
	private static final long serialVersionUID = -8100457099996385877L;
	
	private String expression;//数式表現（abs(x)みたいな）
	
	private PropertyDefinitionType resultType;
	private PropertyDefinition resultTypeSpec;
	
	public ExpressionProperty() {
	}
	
	public ExpressionProperty(String name) {
		setName(name);
	}
	
	public ExpressionProperty(String name, String expression) {
		setName(name);
		setExpression(expression);
	}
	
	public ExpressionProperty(String name, String expression, PropertyDefinitionType resultType) {
		setName(name);
		setExpression(expression);
		setResultType(resultType);
	}

	public PropertyDefinition getResultTypeSpec() {
		return resultTypeSpec;
	}

	public void setResultTypeSpec(PropertyDefinition resultTypeSpec) {
		this.resultTypeSpec = resultTypeSpec;
	}

	public PropertyDefinitionType getResultType() {
		return resultType;
	}

	public void setResultType(PropertyDefinitionType resultType) {
		this.resultType = resultType;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Override
	public Class<?> getJavaType() {
		if (resultType == null) {
			return Object.class;
		} else {
			switch (resultType) {
			case BOOLEAN:
				return Boolean.class;
			case DATE:
				return java.sql.Date.class;
			case DATETIME:
				return java.sql.Timestamp.class;
			case DECIMAL:
				return BigDecimal.class;
			case FLOAT:
				return Double.class;
			case INTEGER:
				return Long.class;
			case SELECT:
				return SelectValue.class;
			case STRING:
				return String.class;
			case TIME:
				return java.sql.Time.class;
			default:
				return Object.class;
			}
		}
	}
	
	@Override
	public PropertyDefinitionType getType() {
		return PropertyDefinitionType.EXPRESSION;
	}

}
