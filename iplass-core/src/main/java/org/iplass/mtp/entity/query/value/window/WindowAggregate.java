/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.aggregate.Aggregate;

/**
 * Window集計関数を表すクラス。
 * 
 * @author K.Higuchi
 *
 */
public class WindowAggregate extends WindowFunction {
	private static final long serialVersionUID = -4147882213737684279L;

	private Aggregate aggregate;
	
	public WindowAggregate() {
	}
	
	public WindowAggregate(Aggregate aggregate) {
		this.aggregate = aggregate;
	}
	
	public WindowAggregate(Aggregate aggregate, PartitionBy partitionBy, WindowOrderBy orderBy) {
		this.aggregate = aggregate;
		setPartitionBy(partitionBy);
		setOrderBy(orderBy);
	}
	
	public Aggregate getAggregate() {
		return aggregate;
	}

	public void setAggregate(Aggregate aggregate) {
		this.aggregate = aggregate;
	}
	
	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
	@Override
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (aggregate != null) {
				aggregate.accept(visitor);
			}
			if (getPartitionBy() != null) {
				getPartitionBy().accept(visitor);
			}
			if (getOrderBy() != null) {
				getOrderBy().accept(visitor);
			}
		}
	}
	
	@Override
	public WindowAggregate partitionBy(Object... partitionField) {
		super.partitionBy(partitionField);
		return this;
	}

	@Override
	public WindowAggregate orderBy(WindowSortSpec... sortSpec) {
		super.orderBy(sortSpec);
		return this;
	}

	@Override
	protected void writeWindowFunctionType(StringBuilder sb) {
		if (aggregate != null) {
			sb.append(aggregate);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((aggregate == null) ? 0 : aggregate.hashCode());
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
		WindowAggregate other = (WindowAggregate) obj;
		if (aggregate == null) {
			if (other.aggregate != null)
				return false;
		} else if (!aggregate.equals(other.aggregate))
			return false;
		return true;
	}
	
}
