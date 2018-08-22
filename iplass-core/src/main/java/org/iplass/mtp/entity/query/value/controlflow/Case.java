/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity.query.value.controlflow;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.PrimaryValue;

/**
 * CASE文を表す。
 * 
 * @author K.Higuchi
 *
 */
public class Case extends PrimaryValue {
	private static final long serialVersionUID = 7079355836108192160L;

	private List<When> when;
	private Else elseClause;
	
	public Case() {
	}
	
	public Case(List<When> when, Else elseClause) {
		this.when = when;
		this.elseClause = elseClause;
	}
	
	public Case (Else elseClause, When... when) {
		this.elseClause = elseClause;
		if (when != null) {
			ArrayList<When> whenList = new ArrayList<When>();
			for (When w: when) {
				whenList.add(w);
			}
			this.when = whenList;
		}
	}

	public List<When> getWhen() {
		return when;
	}

	public void setWhen(List<When> when) {
		this.when = when;
	}

	public Else getElseClause() {
		return elseClause;
	}

	public void setElseClause(Else elseClause) {
		this.elseClause = elseClause;
	}
	
	public Case when(Condition condition, ValueExpression result) {
		if (when == null) {
			when = new ArrayList<When>();
		}
		when.add(new When(condition, result));
		return this;
	}
	
	public Case elseClause(ValueExpression result) {
		elseClause = new Else(result);
		return this;
	}
	

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (when != null) {
				for (When w: when) {
					w.accept(visitor);
				}
			}
			if (elseClause != null) {
				elseClause.accept(visitor);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("case");
		if (when != null) {
			for (When w: when) {
				sb.append(" ");
				sb.append(w);
			}
		}
		if (elseClause != null) {
			sb.append(" ");
			sb.append(elseClause);
		}
		sb.append(" end");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elseClause == null) ? 0 : elseClause.hashCode());
		result = prime * result + ((when == null) ? 0 : when.hashCode());
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
		Case other = (Case) obj;
		if (elseClause == null) {
			if (other.elseClause != null)
				return false;
		} else if (!elseClause.equals(other.elseClause))
			return false;
		if (when == null) {
			if (other.when != null)
				return false;
		} else if (!when.equals(other.when))
			return false;
		return true;
	}


}
