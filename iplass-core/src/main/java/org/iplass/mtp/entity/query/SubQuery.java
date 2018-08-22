/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.entity.query.condition.ConditionVisitor;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.value.primary.EntityField;

/**
 * サブクエリを表す。
 * on句にて、上位クエリとの結合条件を指定し相関サブクエリとすることが可能。<br>
 * 
 * たとえば、
 * <pre>
 * select dept.name, lastName, salary from Employee where salary > (select avg(salary) from Employee on .dept=dept)
 * </pre>
 * とした場合、Employeeエンティティに定義される参照dept（のoid）で結合された相関サブクエリを表すことができる。<br>
 * on句において、.（ドット）は、上位クエリの項目参照であることを示す。
 * 2階層上位クエリの項目を指定したい場合は、..とする。<br>
 * on句に指定可能な結合条件としては、通常の条件（where）句と異なり、EntityFieldとして参照を指定可能。
 * 参照が指定された場合は、そのoidでの結合と同じ意味となる。
 * また、自己への参照を示す特別なリテラル定数として、"this"を利用可能。
 * <pre>
 * select name, (select max(amount) from Detail on .this=parent where status=1) from Master
 * </pre>
 * とした場合、.thisは上位クエリのMasterのエンティティレコード自体を示す。
 * 
 * @author K.Higuchi
 *
 */
public class SubQuery implements ASTNode {
	private static final long serialVersionUID = -5969806128437230832L;

	/**
	 * 相関サブクエリ利用時、結合条件（ON句）にて自分自身への参照を表現する定数。
	 */
	public static final String THIS = "this";

	private Query query;
	private Condition on;
	
	public SubQuery() {
	}
	
	public SubQuery(Query query) {
		this.query = query;
	}
	
	public SubQuery(Query query, Condition on) {
		this.query = query;
		this.on = on;
	}
	
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public Condition getOn() {
		return on;
	}

	public void setOn(Condition on) {
		this.on = on;
	}
	
	/**
	 * 相関サブクエリとする場合の結合条件を指定。
	 * 
	 * @param on
	 * @return
	 */
	public SubQuery on(Condition on) {
		setOn(on);
		return this;
	}
	
	/**
	 * 相関サブクエリとする場合の結合条件を指定。
	 * 指定のunnestCount分上位のクエリと指定のプロパティで結合する。
	 * 
	 * @param mainQueryProperty 上位クエリ側のプロパティ
	 * @param subQueryProperty サブクエリ側のプロパティ
	 * @param unnestCount 何階層上位のクエリと結合するかを指定
	 * @return
	 */
	public SubQuery on(EntityField mainQueryProperty, EntityField subQueryProperty, int unnestCount) {
		on = new Equals(withDot(mainQueryProperty, unnestCount), subQueryProperty);
		return this;
	}
	
	private EntityField withDot(EntityField propName, int unnestCount) {
		if (propName == null || propName.getPropertyName() == null) {
			propName = new EntityField(THIS);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < unnestCount; i++) {
			sb.append('.');
		}
		sb.append(propName.getPropertyName());
		return new EntityField(sb.toString());
	}
	
	/**
	 * 相関サブクエリとする場合の結合条件を指定。
	 * 指定のunnestCount分上位のクエリと指定のプロパティで結合する。
	 * 
	 * @param mainQueryProperyName 上位クエリ側のプロパティ名
	 * @param subQueryPropertyName サブクエリ側のプロパティ名
	 * @param unnestCount 何階層上位のクエリと結合するかを指定
	 * @return
	 */
	public SubQuery on(String mainQueryProperyName, String subQueryPropertyName, int unnestCount) {
		return on(new EntityField(mainQueryProperyName), new EntityField(subQueryPropertyName), unnestCount);
	}
	
	/**
	 * 相関サブクエリとする場合の結合条件を指定。
	 * ひとつ上位のクエリと指定のプロパティで結合する。
	 * 
	 * @param mainQueryProperyName 上位クエリ側のプロパティ名
	 * @param subQueryPropertyName サブクエリ側のプロパティ名
	 * @return
	 */
	public SubQuery on(String mainQueryProperyName, String subQueryPropertyName) {
		return on(mainQueryProperyName, subQueryPropertyName, 1);
	}	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(query);
		if (on != null) {
			sb.append(" on ");
			sb.append(on);
		}
		sb.append(")");
		return sb.toString();
	}
	
	public void accept(QueryVisitor visitor) {
		if (visitor.visit(this)) {
			if (visitor instanceof QueryVisitor && query != null) {
				query.accept((QueryVisitor) visitor);
			}
			if (on != null) {
				if (visitor instanceof ConditionVisitor) {
					on.accept((ConditionVisitor) visitor);
				}
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
		result = prime * result + ((on == null) ? 0 : on.hashCode());
		result = prime * result + ((query == null) ? 0 : query.hashCode());
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
		SubQuery other = (SubQuery) obj;
		if (on == null) {
			if (other.on != null)
				return false;
		} else if (!on.equals(other.on))
			return false;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		return true;
	}

}
