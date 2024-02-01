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

package org.iplass.mtp.entity.query;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;


/**
 * GROUP BY句を表す。
 * 
 * @author K.Higuchi
 *
 */
public class GroupBy implements ASTNode {
	private static final long serialVersionUID = 6843313143662604447L;

	public enum RollType {
		/**
		 * 小計、合計を同時に集計する
		 */
		ROLLUP,
		
		/**
		 * group byに指定されている項目のすべての組み合わせの小計、合計を同時に集計する
		 */
		CUBE
	}
	
	private List<ValueExpression> groupingFieldList = new ArrayList<ValueExpression>();
	
	private RollType rollType;
	
	public List<ValueExpression> getGroupingFieldList() {
		return groupingFieldList;
	}

	public void setGroupingFieldList(List<ValueExpression> groupingFieldList) {
		this.groupingFieldList = groupingFieldList;
	}
	
	/**
	 * 集約関数利用する際の、
	 * group byする集約する項目毎の集計行（小計、合計）を同時に取得する場合は、
	 * rollTypeを指定する。<br>
	 * <b>
	 * ※このRollType指定は、SQL/2008のExtended grouping capabilities（Group by with ROLLUP/CUBE）にマッピングされる。
	 * 一部のRDBではSQLレベルでROLLUP/CUBEに対応していない。また、ORDER BYとの併用が出来なかったりする。
	 * その場合は、RollTypeを指定しても意図したとおりに動作しない。
	 * </b>
	 * <table border="1" >
	 * <tr><th>DB</th><th>サポートするタイプ</th><th>EQLで指定した際の動作</th><th>ORDER BYとの併用</th></tr>
	 * <tr><td>Oracle</td><td>ROLLUP/CUBE</td><td>意図したとおりに小計、合計を取得</td><td>可能</td></tr>
	 * <tr><td>MySQL</td><td>ROLLUP</td><td>CUBEを指定してもROLLUPとして動作</td><td>不可。ORDER BYを無視</td></tr>
	 * <tr><td>PostgreSQL</td><td>未サポート</td><td>指定されていないものとして動作</td><td>ROLLUP無視。ORDER BYのみ有効</td></tr>
	 * </table>
	 * 
	 * @param rollType RollType.ROLLUPを指定
	 */
	public void setRollType(RollType rollType) {
		this.rollType = rollType;
	}

	public RollType getRollType() {
		return rollType;
	}
	
	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
	public void accept(QueryVisitor visitor) {
		if (visitor.visit(this)) {
			if (groupingFieldList != null) {
				for (ValueExpression s: groupingFieldList) {
					s.accept(visitor);
				}
			}
		}
	}

	public GroupBy add(ValueExpression groupingField) {
		if (groupingField == null) {
			throw new NullPointerException("groupingField is null");
		}
		if (groupingFieldList == null) {
			groupingFieldList = new ArrayList<ValueExpression>();
		}
		groupingFieldList.add(groupingField);
		return this;
	}
	
	public GroupBy add(Object groupingField) {
		if (groupingField == null) {
			throw new NullPointerException("groupingField is null");
		}
		if (groupingFieldList == null) {
			groupingFieldList = new ArrayList<ValueExpression>();
		}
		ValueExpression v = null;
		if (groupingField instanceof ValueExpression) {
			v = (ValueExpression) groupingField;
		} else if (groupingField instanceof String) {
			v = new EntityField((String) groupingField);
		} else {
			throw new QueryException("groupingField is ValueExpression or String type required.");
		}
		
		return add(v);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("group by ");
		if (groupingFieldList != null) {
			for (int i = 0; i < groupingFieldList.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(groupingFieldList.get(i));
			}
		}
		if (rollType != null) {
			switch (rollType) {
			case ROLLUP:
				sb.append(" rollup");
				break;
			case CUBE:
				sb.append(" cube");
			default:
				break;
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((groupingFieldList == null) ? 0 : groupingFieldList
						.hashCode());
		result = prime * result
				+ ((rollType == null) ? 0 : rollType.hashCode());
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
		GroupBy other = (GroupBy) obj;
		if (groupingFieldList == null) {
			if (other.groupingFieldList != null)
				return false;
		} else if (!groupingFieldList.equals(other.groupingFieldList))
			return false;
		if (rollType != other.rollType)
			return false;
		return true;
	}

}
