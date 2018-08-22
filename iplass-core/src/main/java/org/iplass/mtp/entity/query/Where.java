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

package org.iplass.mtp.entity.query;

import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.query.WhereSyntax;

/**
 * WHERE句を表す。
 * 
 * @author K.Higuchi
 *
 */
public class Where implements ASTNode {
	private static final long serialVersionUID = -1522953660630044615L;

	public static Where newWhere(String where) {
		try {
			return QueryServiceHolder.getInstance().getQueryParser().parse(where, WhereSyntax.class);
		} catch (ParseException e) {
			throw new QueryException(e.getMessage(), e);
		}
	}
	
	private Condition condition;
	
	public Where() {
	}
	
	public Where(Condition condition) {
		this.condition = condition;
	}
	
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public Condition getCondition() {
		return condition;
	}

	@Override
	public String toString() {
		if (condition != null) {
			return "where " + condition.toString();
		}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
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
		Where other = (Where) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		return true;
	}

	public void accept(QueryVisitor visitor) {
		if (visitor.visit(this)) {
			if (condition != null) {
				condition.accept(visitor);
			}
		}
	}
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

}
