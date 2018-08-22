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

package org.iplass.mtp.entity.query.condition.expr;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.ConditionVisitor;
import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.entity.query.condition.predicate.Contains;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.IsNotNull;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.Lesser;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;
import org.iplass.mtp.entity.query.condition.predicate.Like.MatchPattern;


/**
 * And条件を表す。
 * 
 * @author K.Higuchi
 *
 */
public class And extends Condition {
	private static final long serialVersionUID = 6770787599085842938L;

	protected List<Condition> childExpressions;
	
	
	public And() {
	}
	
	public And(Condition... condition) {
		if (condition != null) {
			ArrayList<Condition> condList = new ArrayList<Condition>();
			for (Condition c: condition) {
				//expressionがORの場合、()を付ける
				if (c instanceof Or) {
					c = new Paren(c);
				}
				condList.add(c);
			}
			childExpressions = condList;
		}
	}
	
	public And(List<Condition> childExpressions) {
		this.childExpressions = childExpressions;
	}
	
	public void setConditions(List<Condition> conditions) {
		this.childExpressions = conditions;
		
	}
	
	//TODO アクセッサメソッド名変更
	public void addExpression(Condition expression) {
		if (childExpressions == null) {
			childExpressions = new ArrayList<Condition>();
		}
		
		//expressionがORの場合、()を付ける
		if (expression instanceof Or) {
			expression = new Paren(expression);
		}
		childExpressions.add(expression);
	}
	
	public List<Condition> getChildExpressions() {
		return childExpressions;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((childExpressions == null) ? 0 : childExpressions.hashCode());
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
		And other = (And) obj;
		if (childExpressions == null) {
			if (other.childExpressions != null)
				return false;
		} else if (!childExpressions.equals(other.childExpressions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		if (childExpressions != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < childExpressions.size(); i++) {
				if (i != 0) {
					sb.append(" and ");
				}
				sb.append(childExpressions.get(i).toString());
			}
			return sb.toString();
		}
		
		return null;
	}

	public void accept(ConditionVisitor visitor) {
		if (visitor.visit(this)) {
			if (childExpressions != null) {
				for (Condition exp: childExpressions) {
					exp.accept(visitor);
				}
			}
		}
	}
	
	public Condition strip() {
		if (childExpressions != null && childExpressions.size() == 1) {
			return childExpressions.get(0);
		} else {
			return this;
		}
	}
	
	/**
	 * 指定の条件を追加する。
	 * 
	 * @param expression
	 * @return
	 */
	public And and(Condition expression) {
		addExpression(expression);
		return this;
	}
	
	/**
	 * Equals条件を追加する。
	 * 
	 * @param propName
	 * @param value
	 * @return
	 */
	public And eq(String propName, Object value) {
		Equals eq = new Equals(propName, value);
		addExpression(eq);
		return this;
	}
	
	/**
	 * NotEquals条件を追加する。
	 * 
	 * @param propName
	 * @param value
	 * @return
	 */
	public And neq(String propName, Object value) {
		NotEquals eq = new NotEquals(propName, value);
		addExpression(eq);
		return this;
	}
	
	/**
	 * Lesser条件を追加する。
	 * 
	 * @param propName
	 * @param value
	 * @return
	 */
	public And lt(String propName, Object value) {
		Lesser less = new Lesser(propName, value);
		addExpression(less);
		return this;
	}
	
	/**
	 * LesserEqual条件を追加する。
	 * 
	 * @param propName
	 * @param value
	 * @return
	 */
	public And lte(String propName, Object value) {
		LesserEqual lessEq = new LesserEqual(propName, value);
		addExpression(lessEq);
		return this;
	}
	
	/**
	 * Greater条件を追加する。
	 * 
	 * @param propName
	 * @param value
	 * @return
	 */
	public And gt(String propName, Object value) {
		Greater great = new Greater(propName, value);
		addExpression(great);
		return this;
	}
	
	/**
	 * GreaterEqual条件を追加する。
	 * 
	 * @param propName
	 * @param value
	 * @return
	 */
	public And gte(String propName, Object value) {
		GreaterEqual greatEq = new GreaterEqual(propName, value);
		addExpression(greatEq);
		return this;
	}
	
	/**
	 * In条件を追加する。
	 * 
	 * @param propName
	 * @param values
	 * @return
	 */
	public And in(String propName, Object... values) {
		In in = new In(propName, values);
		addExpression(in);
		return this;
	}
	
	/**
	 * In条件を追加する。
	 * 
	 * @param propName
	 * @param subQuery
	 * @return
	 */
	public And in(String propName, SubQuery subQuery) {
		In in = new In(propName, subQuery);
		addExpression(in);
		return this;
	}
	
	/**
	 * ※なるべく{@link #like(String, String, MatchPattern)}を利用すること。
	 * 
	 * <b>このメソッドのpatternは、自動的にエスケープ処理されない点、注意</b>
	 * Like条件を追加する。
	 * 
	 * @param propName
	 * @param value
	 * @return
	 */
	@Deprecated
	public And like(String propName, String pattern)  {
		Like like = new Like(propName, pattern);
		addExpression(like);
		return this;
	}
	
	/**
	 * Like条件を追加する。
	 * 
	 * @param propName
	 * @param str
	 * @param matchPatternType
	 * @return
	 */
	public And like(String propName, String str, MatchPattern matchPatternType)  {
		Like like = new Like(propName, str, matchPatternType);
		addExpression(like);
		return this;
	}
	
	/**
	 * Between条件を追加する。
	 * 
	 * @param propName
	 * @param from
	 * @param to
	 * @return
	 */
	public And between(String propName, Object from, Object to) {
		Between bet = new Between(propName, from, to);
		addExpression(bet);
		return this;
	}
	
	/**
	 * IsNull条件を追加する。
	 * 
	 * @param propName
	 * @return
	 */
	public And isNull(String propName)  {
		IsNull isNull = new IsNull(propName);
		addExpression(isNull);
		return this;
	}

	/**
	 * IsNotNull条件を追加する。
	 * 
	 * @param propName
	 * @return
	 */
	public And isNotNull(String propName)  {
		IsNotNull isNotNull = new IsNotNull(propName);
		addExpression(isNotNull);
		return this;
	}
	
	/**
	 * Contains条件を追加する。
	 * 
	 * @param fullTextSearchExpression
	 * @return
	 */
	public And contains(String fullTextSearchExpression) {
		Contains contains = new Contains(fullTextSearchExpression);
		addExpression(contains);
		return this;
	}

	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
}
