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

package org.iplass.mtp.entity.query.value.aggregate;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;

/**
 * AVG関数を表す。
 * 
 * @author K.Higuchi
 *
 */
public class Avg extends Aggregate {
	private static final long serialVersionUID = -1850941158731600164L;

	public Avg() {
	}
	
	public Avg(String propertyName) {
		setValue(new EntityField(propertyName));
	}
	
	public Avg(ValueExpression value) {
		setValue(value);
	}

	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (getValue() != null) {
				getValue().accept(visitor);
			}
		}
	}

	@Override
	protected String getFuncName() {
		return "avg";
	}

	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
}
