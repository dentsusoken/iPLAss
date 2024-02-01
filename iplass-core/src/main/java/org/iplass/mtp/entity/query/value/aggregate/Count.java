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
 * COUNT関数を表す。
 * 集計対象の指定は任意（未指定の場合は、件数をカウント）。
 * distinct指定した場合は、ユニーク数のカウント。
 * 
 * @author K.Higuchi
 *
 */
public class Count extends Aggregate {
	private static final long serialVersionUID = -4428126857034145763L;

	private boolean isDistinct;
	
	public Count() {
	}
	
	public Count(String propertyName) {
		setValue(new EntityField(propertyName));
	}
	
	public Count(String propertyName, boolean isDistinct) {
		setValue(new EntityField(propertyName));
		this.isDistinct = isDistinct;
	}
	
	public Count(ValueExpression value) {
		setValue(value);
	}
	
	public Count(ValueExpression value, boolean isDistinct) {
		setValue(value);
		this.isDistinct = isDistinct;
	}

	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (getValue() != null) {
				getValue().accept(visitor);
			}
		}
	}
	
	public boolean isDistinct() {
		return isDistinct;
	}

	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}

	@Override
	public String toString() {
		if (getValue() == null) {
			return "count()";
		} else {
			if (isDistinct) {
				return getFuncName() + "(distinct " + getValue() + ")";
			} else {
				return getFuncName() + "(" + getValue() + ")";
			}
		}
	}

	@Override
	protected String getFuncName() {
		return "count";
	}
	
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (isDistinct ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Count other = (Count) obj;
		if (isDistinct != other.isDistinct)
			return false;
		return true;
	}

}
