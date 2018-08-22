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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryVisitor;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.ConditionVisitor;
import org.iplass.mtp.entity.query.value.RowValueList;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;


/**
 * IN条件文を表す。
 * ValueExpressionのリスト、もしくはサブクエリーを指定可能。
 * 
 * @author K.Higuchi
 *
 */
public class In extends Predicate {
	private static final long serialVersionUID = -3174824555455261230L;

	//TODO NOT INの対応。orクエリーが遅くなること想定されるから対応なしか？

	private List<ValueExpression> propertyList;
	
	private List<ValueExpression> value;
	private SubQuery subQuery;
	
	public In() {
	}
	
	public In(String propertyName, Object... literalValues) {
		setPropertyName(propertyName);
		List<ValueExpression> valList = new ArrayList<ValueExpression>();
		if (literalValues != null) {
			for (Object o: literalValues) {
				if (o instanceof Literal) {
					valList.add((Literal) o);
				} else {
					valList.add(new Literal(o));
				}
			}
		}
		this.value = valList;
	}
	
	public In(String[] propertyName, Object[]... literalRowValueLists) {
		setPropertyName(propertyName);
		List<ValueExpression> valList = new ArrayList<ValueExpression>();
		if (literalRowValueLists != null) {
			for (Object[] o: literalRowValueLists) {
				RowValueList rvl = new RowValueList(o);
				valList.add(rvl);
			}
		}
		this.value = valList;
	}
	
	
	public In(String propertyName, SubQuery subQuery) {
		setPropertyName(propertyName);
		this.subQuery = subQuery;
	}
	
	public In(String[] propertyName, SubQuery subQuery) {
		setPropertyName(propertyName);
		this.subQuery = subQuery;
	}
	
	public In(String propertyName, Query subQuery) {
		setPropertyName(propertyName);
		this.subQuery = new SubQuery(subQuery);
	}
	
	public In(String[] propertyName, Query subQuery) {
		setPropertyName(propertyName);
		this.subQuery = new SubQuery(subQuery);
	}
	
	public In(String[] propertyName, List<ValueExpression> value) {
		setPropertyName(propertyName);
		this.value = value;
	}
	
	public In(ValueExpression property, List<ValueExpression> value) {
		setProperty(property);
		this.value = value;
	}
	
	public In(ValueExpression property, Object... literalValues) {
		setProperty(property);
		List<ValueExpression> valList = new ArrayList<ValueExpression>();
		if (literalValues != null) {
			for (Object o: literalValues) {
				if (o instanceof Literal) {
					valList.add((Literal) o);
				} else {
					valList.add(new Literal(o));
				}
			}
		}
		this.value = valList;
	}

	public In(ValueExpression property, SubQuery subQuery) {
		setProperty(property);
		this.subQuery = subQuery;
	}
	
	public In(List<ValueExpression> property, SubQuery subQuery) {
		setPropertyList(property);
		this.subQuery = subQuery;
	}
	
	public In(ValueExpression property, Query subQuery) {
		setProperty(property);
		this.subQuery = new SubQuery(subQuery);
	}
	
	public In(List<ValueExpression> property, Query subQuery) {
		setPropertyList(property);
		this.subQuery = new SubQuery(subQuery);
	}
	
	public In(List<ValueExpression> property, List<ValueExpression> value) {
		setPropertyList(property);
		this.value = value;
	}
	
	public void setPropertyName(String... propertyName) {
		propertyList = new ArrayList<ValueExpression>();
		for (String pName: propertyName) {
			propertyList.add(new EntityField(pName));
		}
	}
	
	public void setProperty(ValueExpression... property) {
		propertyList = new ArrayList<ValueExpression>();
		for (ValueExpression p: property) {
			propertyList.add(p);
		}
	}
	
	public void setPropertyList(List<ValueExpression> propertyList) {
		this.propertyList = propertyList;
	}
	
	public List<ValueExpression> getPropertyList() {
		return propertyList;
	}
	
	public List<ValueExpression> getValue() {
		return value;
	}

	public void setValue(List<ValueExpression> value) {
		this.value = value;
	}
	
	public SubQuery getSubQuery() {
		return subQuery;
	}

	public void setSubQuery(SubQuery subQuery) {
		this.subQuery = subQuery;
	}

	public void accept(ConditionVisitor visitor) {
		if (visitor.visit(this)) {
			if (visitor instanceof ValueExpressionVisitor) {
				ValueExpressionVisitor vVisitor = (ValueExpressionVisitor) visitor;
				if (propertyList != null) {
					for (ValueExpression p: propertyList) {
						p.accept(vVisitor);
					}
				}
				if (value != null) {
					for (ValueExpression v: value) {
						v.accept(vVisitor);
					}
				}
			}
			if (visitor instanceof QueryVisitor) {
				if (subQuery != null) {
					subQuery.accept((QueryVisitor) visitor);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (propertyList == null) {
			sb.append("null");
		} else if (propertyList.size() == 1) {
			sb.append(propertyList.get(0));
		} else {
			sb.append("(");
			for (int i = 0; i < propertyList.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(propertyList.get(i));
			}
			sb.append(")");
			
		}
		sb.append(" in ");
		if (subQuery != null) {
			sb.append(subQuery.toString());
		} else {
			sb.append("(");
			if (value != null) {
				for (int i = 0; i < value.size(); i++) {
					if (i != 0) {
						sb.append(",");
					}
					sb.append(value.get(i));
				}
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((propertyList == null) ? 0 : propertyList.hashCode());
		result = prime * result
				+ ((subQuery == null) ? 0 : subQuery.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		In other = (In) obj;
		if (propertyList == null) {
			if (other.propertyList != null)
				return false;
		} else if (!propertyList.equals(other.propertyList))
			return false;
		if (subQuery == null) {
			if (other.subQuery != null)
				return false;
		} else if (!subQuery.equals(other.subQuery))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
