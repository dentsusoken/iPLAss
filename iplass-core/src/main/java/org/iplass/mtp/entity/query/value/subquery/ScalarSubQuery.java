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

package org.iplass.mtp.entity.query.value.subquery;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryVisitor;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.PrimaryValue;

/**
 * スカラサブクエリを表す。
 * スカラサブクエリは単一値を返却するValueExpressionと定義される。
 * 
 * @author K.Higuchi
 *
 */
public class ScalarSubQuery extends PrimaryValue {
	private static final long serialVersionUID = -7160903530841092122L;

	private SubQuery subQuery;//ValueExpressionが1つと言う制約あり。
	
	public ScalarSubQuery() {
	}
	
	public ScalarSubQuery(Query query) {
		this.subQuery = new SubQuery(query);
	}
	
	public ScalarSubQuery(Query query, Condition on) {
		this.subQuery = new SubQuery(query, on);
	}
	
	public ScalarSubQuery(SubQuery subQuery) {
		this.subQuery = subQuery;
	}
	
	public Query getQuery() {
		if (subQuery == null) {
			return null;
		}
		return subQuery.getQuery();
	}
	
	public Condition getOn() {
		if (subQuery == null) {
			return null;
		}
		return subQuery.getOn();
	}
	
	public SubQuery getSubQuery() {
		return subQuery;
	}

	public void setSubQuery(SubQuery subQuery) {
		this.subQuery = subQuery;
	}
	
	
	public ScalarSubQuery on(Condition on) {
		if (subQuery == null) {
			subQuery = new SubQuery();
		}
		subQuery.on(on);
		return this;
	}
	
	public ScalarSubQuery on(EntityField mainQueryProperty, EntityField subQueryProperty, int unnestCount) {
		if (subQuery == null) {
			subQuery = new SubQuery();
		}
		subQuery.on(mainQueryProperty, subQueryProperty, unnestCount);
		return this;
	}
	
	public ScalarSubQuery on(String mainQueryProperyName, String subQueryPropertyName, int unnestCount) {
		if (subQuery == null) {
			subQuery = new SubQuery();
		}
		subQuery.on(mainQueryProperyName, subQueryPropertyName, unnestCount);
		return this;
	}
	
	public ScalarSubQuery on(String mainQueryProperyName, String subQueryPropertyName) {
		if (subQuery == null) {
			subQuery = new SubQuery();
		}
		subQuery.on(mainQueryProperyName, subQueryPropertyName);
		return this;
	}
	
	@Override
	public String toString() {
		if (subQuery == null) {
			return "(null)";
		} else {
			return subQuery.toString();
		}
	}
	
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (visitor instanceof QueryVisitor && subQuery != null) {
				subQuery.accept((QueryVisitor) visitor);
			}
		}
	}
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subQuery == null) ? 0 : subQuery.hashCode());
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
		ScalarSubQuery other = (ScalarSubQuery) obj;
		if (subQuery == null) {
			if (other.subQuery != null)
				return false;
		} else if (!subQuery.equals(other.subQuery))
			return false;
		return true;
	}
}
