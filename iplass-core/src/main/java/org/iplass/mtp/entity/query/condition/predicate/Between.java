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

package org.iplass.mtp.entity.query.condition.predicate;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.condition.ConditionVisitor;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;

/**
 * BETWEEN条件文を表す。 
 * 
 * @author K.Higuchi
 *
 */
public class Between extends Predicate {
	private static final long serialVersionUID = 6957341207431475353L;

	private ValueExpression from;
	private ValueExpression to;
	private ValueExpression property;
	
	public Between() {
	}
	
	
	
	public Between(String propertyName, Object fromLiteral, Object toLiteral) {
		setPropertyName(propertyName);
		if (fromLiteral instanceof ValueExpression) {
			this.from = (ValueExpression) fromLiteral;
		} else {
			this.from = new Literal(fromLiteral);
		}
		if (toLiteral instanceof ValueExpression) {
			this.to = (ValueExpression) toLiteral;
		} else {
			this.to = new Literal(toLiteral);
		}
	}
	
	public Between(String propertyName, ValueExpression from, ValueExpression to) {
		setPropertyName(propertyName);
		this.from = from;
		this.to = to;
	}
	
	public Between(ValueExpression property, ValueExpression from, ValueExpression to) {
		setProperty(property);
		this.from = from;
		this.to = to;
	}
	
	public String getPropertyName() {
		if (property == null) {
			return null;
		}
		return property.toString();
	}
	
	public void setPropertyName(String propertyName) {
		property = new EntityField(propertyName);
	}
	
	public void setProperty(ValueExpression property) {
		this.property = property;
	}
	
	public ValueExpression getProperty() {
		return property;
	}
	
	public ValueExpression getFrom() {
		return from;
	}

	public void setFrom(ValueExpression from) {
		this.from = from;
	}

	public ValueExpression getTo() {
		return to;
	}

	public void setTo(ValueExpression to) {
		this.to = to;
	}

	public void accept(ConditionVisitor visitor) {
		if (visitor.visit(this)) {
			if (visitor instanceof ValueExpressionVisitor) {
				if (property != null) {
					property.accept((ValueExpressionVisitor) visitor);
				}
				if (from != null) {
					from.accept((ValueExpressionVisitor) visitor);
				}
				if (to != null) {
					to.accept((ValueExpressionVisitor) visitor);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPropertyName()).append(" between ").append(from).append(" and ").append(to);
		return sb.toString();
	}
	
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result
				+ ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Between other = (Between) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}


}
