/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;

/**
 * LISTAGG関数を表す。
 * 
 * @author K.Higuchi
 *
 */
public class Listagg extends OrderedSetFunction {
	private static final long serialVersionUID = -175289204129911812L;
	
	private boolean distinct;
	private Literal separator;
	
	public Listagg() {
	}
	
	public Listagg(String propertyName) {
		this(new EntityField(propertyName));
	}

	public Listagg(String propertyName, boolean isDistinct, String separator) {
		this(new EntityField(propertyName), isDistinct, separator);
	}

	public Listagg(ValueExpression value) {
		setValue(value);
	}

	public Listagg(ValueExpression value, boolean isDistinct, String separator) {
		setValue(value);
		setDistinct(isDistinct);
		setSeparator(new Literal(separator));
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public Literal getSeparator() {
		return separator;
	}

	public void setSeparator(Literal separator) {
		if (!(separator.getValue() instanceof String)) {
			throw new QueryException("separator must be type String.");
		}
		this.separator = separator;
	}

	public Listagg orderBy(WithinGroupSortSpec... sortSpec) {
		if (getWithinGroup() == null) {
			setWithinGroup(new WithinGroup());
		}
		for (WithinGroupSortSpec o: sortSpec) {
			getWithinGroup().add(o);
		}
		return this;
	}

	@Override
	public String toString() {
		if (separator == null && !distinct) {
			return super.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(getFuncName());
			sb.append("(");
			if (distinct) {
				sb.append("distinct ");
			}
			sb.append(getValue());
			if (separator != null) {
				sb.append(",").append(separator);
			}
			sb.append(")");
			if (getWithinGroup() != null) {
				sb.append(" ").append(getWithinGroup());
			}
			return sb.toString();
		}
	}

	@Override
	protected String getFuncName() {
		return "listagg";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (distinct ? 1231 : 1237);
		result = prime * result + ((separator == null) ? 0 : separator.hashCode());
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
		Listagg other = (Listagg) obj;
		if (distinct != other.distinct)
			return false;
		if (separator == null) {
			if (other.separator != null)
				return false;
		} else if (!separator.equals(other.separator))
			return false;
		return true;
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (getValue() != null) {
				getValue().accept(visitor);
			}
			if (separator != null) {
				separator.accept(visitor);
			}
			if (getWithinGroup() != null) {
				getWithinGroup().accept(visitor);
			}
		}
	}

}
