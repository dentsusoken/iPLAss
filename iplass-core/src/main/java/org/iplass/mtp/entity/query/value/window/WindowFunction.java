/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.entity.query.value.window;

import org.iplass.mtp.entity.query.value.primary.PrimaryValue;

/**
 * Window関数を表す抽象クラス。
 * 
 * @author K.Higuchi
 *
 */
public abstract class WindowFunction extends PrimaryValue {
	private static final long serialVersionUID = 1570026824441319021L;

	private PartitionBy partitionBy;
	private WindowOrderBy orderBy;
	
	public PartitionBy getPartitionBy() {
		return partitionBy;
	}
	public void setPartitionBy(PartitionBy partitionBy) {
		this.partitionBy = partitionBy;
	}
	public WindowOrderBy getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(WindowOrderBy orderBy) {
		this.orderBy = orderBy;
	}
	
	public WindowFunction partitionBy(Object... partitionField) {
		if (partitionBy == null) {
			partitionBy = new PartitionBy();
		}
		for (Object v: partitionField) {
			partitionBy.add(v);
		}
		return this;
	}

	public WindowFunction orderBy(WindowSortSpec... sortSpec) {
		if (orderBy == null) {
			orderBy = new WindowOrderBy();
		}
		for (WindowSortSpec o: sortSpec) {
			orderBy.add(o);
		}
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderBy == null) ? 0 : orderBy.hashCode());
		result = prime * result
				+ ((partitionBy == null) ? 0 : partitionBy.hashCode());
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
		WindowFunction other = (WindowFunction) obj;
		if (orderBy == null) {
			if (other.orderBy != null)
				return false;
		} else if (!orderBy.equals(other.orderBy))
			return false;
		if (partitionBy == null) {
			if (other.partitionBy != null)
				return false;
		} else if (!partitionBy.equals(other.partitionBy))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		writeWindowFunctionType(sb);
		sb.append(" over(");
		if (partitionBy != null) {
			sb.append(partitionBy);
		}
		if (orderBy != null) {
			if (partitionBy != null) {
				sb.append(' ');
			}
			sb.append(orderBy);
		}
		sb.append(")");
		return sb.toString();
	}
	
	protected abstract void writeWindowFunctionType(StringBuilder sb);
	
	//partition by
	//order by
	//とりあえずここまでサポートか。
	
	/*
<window frame clause>  ::=   <window frame units> <window frame extent> [ <window frame exclusion> ] 
<window frame units>    ::=   ROWS | RANGE
<window frame extent>    ::=   <window frame start> | <window frame between>
<window frame start>    ::=   UNBOUNDED PRECEDING | <window frame preceding> | CURRENT ROW
<window frame preceding>    ::=   <unsigned value specification> PRECEDING
<window frame between>    ::=   BETWEEN <window frame bound 1> AND <window frame bound 2>
<window frame bound 1>    ::=   <window frame bound>
<window frame bound 2>    ::=   <window frame bound>
<window frame bound>    ::=
         <window frame start>
     |     UNBOUNDED FOLLOWING
     |     <window frame following>
<window frame following>    ::=   <unsigned value specification> FOLLOWING
<window frame exclusion>    ::=
         EXCLUDE CURRENT ROW
     |     EXCLUDE GROUP
     |     EXCLUDE TIES
     |     EXCLUDE NO OTHERS 
     */

}
