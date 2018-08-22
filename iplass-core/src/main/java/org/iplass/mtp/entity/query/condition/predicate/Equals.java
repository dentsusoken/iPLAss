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

/**
 * =条件文を表す。
 * 
 * @author K.Higuchi
 *
 */
public class Equals extends ComparisonPredicate {
	private static final long serialVersionUID = -3054232373708885486L;

	public Equals() {
	}
	
	public Equals(String propertyName, Object literalValue) {
		super(propertyName, literalValue);
	}
	
	public Equals(ValueExpression property, ValueExpression value) {
		super(property, value);
	}

	public void accept(ConditionVisitor visitor) {
		if (visitor.visit(this)) {
			if (visitor instanceof ValueExpressionVisitor) {
				if (getProperty() != null) {
					getProperty().accept((ValueExpressionVisitor) visitor);
				}
				if (getValue() != null) {
					getValue().accept((ValueExpressionVisitor) visitor);
				}
			}
		}
	}

	@Override
	protected String getOpString() {
		return "=";
	}
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}


}
