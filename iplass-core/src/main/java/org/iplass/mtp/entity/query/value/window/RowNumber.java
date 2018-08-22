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

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;

/**
 * ROW_NUMBER Window関数を表す。
 * 
 * @author K.Higuchi
 *
 */
public class RowNumber extends WindowRankFunction {
	private static final long serialVersionUID = 3141070077849958533L;

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
	@Override
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (getPartitionBy() != null) {
				getPartitionBy().accept(visitor);
			}
			if (getOrderBy() != null) {
				getOrderBy().accept(visitor);
			}
		}
	}
	
	@Override
	public RowNumber partitionBy(Object... partitionField) {
		super.partitionBy(partitionField);
		return this;
	}

	@Override
	public RowNumber orderBy(WindowSortSpec... sortSpec) {
		super.orderBy(sortSpec);
		return this;
	}
	
	@Override
	protected String getFuncName() {
		return "row_number";
	}
}
