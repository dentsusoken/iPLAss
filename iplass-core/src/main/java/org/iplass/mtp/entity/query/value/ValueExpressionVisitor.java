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

package org.iplass.mtp.entity.query.value;

import org.iplass.mtp.entity.query.value.aggregate.Avg;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.entity.query.value.aggregate.Listagg;
import org.iplass.mtp.entity.query.value.aggregate.Max;
import org.iplass.mtp.entity.query.value.aggregate.Median;
import org.iplass.mtp.entity.query.value.aggregate.Min;
import org.iplass.mtp.entity.query.value.aggregate.Mode;
import org.iplass.mtp.entity.query.value.aggregate.StdDevPop;
import org.iplass.mtp.entity.query.value.aggregate.StdDevSamp;
import org.iplass.mtp.entity.query.value.aggregate.Sum;
import org.iplass.mtp.entity.query.value.aggregate.VarPop;
import org.iplass.mtp.entity.query.value.aggregate.VarSamp;
import org.iplass.mtp.entity.query.value.aggregate.WithinGroup;
import org.iplass.mtp.entity.query.value.aggregate.WithinGroupSortSpec;
import org.iplass.mtp.entity.query.value.controlflow.Case;
import org.iplass.mtp.entity.query.value.controlflow.Else;
import org.iplass.mtp.entity.query.value.controlflow.When;
import org.iplass.mtp.entity.query.value.expr.MinusSign;
import org.iplass.mtp.entity.query.value.expr.Polynomial;
import org.iplass.mtp.entity.query.value.expr.Term;
import org.iplass.mtp.entity.query.value.primary.ArrayValue;
import org.iplass.mtp.entity.query.value.primary.Cast;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.entity.query.value.primary.ParenValue;
import org.iplass.mtp.entity.query.value.subquery.ScalarSubQuery;
import org.iplass.mtp.entity.query.value.window.CumeDist;
import org.iplass.mtp.entity.query.value.window.DenseRank;
import org.iplass.mtp.entity.query.value.window.PartitionBy;
import org.iplass.mtp.entity.query.value.window.PercentRank;
import org.iplass.mtp.entity.query.value.window.Rank;
import org.iplass.mtp.entity.query.value.window.RowNumber;
import org.iplass.mtp.entity.query.value.window.WindowAggregate;
import org.iplass.mtp.entity.query.value.window.WindowOrderBy;
import org.iplass.mtp.entity.query.value.window.WindowSortSpec;


public interface ValueExpressionVisitor {

	public boolean visit(Literal literal);
	public boolean visit(EntityField entityField);
	public boolean visit(Polynomial polynomial);
	public boolean visit(Term term);
	public boolean visit(ParenValue parenthesizedValue);
	public boolean visit(MinusSign minusSign);
	public boolean visit(Function function);
	public boolean visit(ArrayValue arrayValue);
	public boolean visit(Cast cast);
	
	// row value list
	public boolean visit(RowValueList rowValueList);
	
	//aggregate
	public boolean visit(Count count);
	public boolean visit(Sum sum);
	public boolean visit(Avg avg);
	public boolean visit(Max max);
	public boolean visit(Min min);
	public boolean visit(StdDevPop stdDevPop);
	public boolean visit(StdDevSamp stdDevSamp);
	public boolean visit(VarPop varPop);
	public boolean visit(VarSamp varSamp);
	public boolean visit(Mode mode);
	public boolean visit(Median median);
	public boolean visit(Listagg listagg);
	public boolean visit(WithinGroup withinGroup);
	public boolean visit(WithinGroupSortSpec sortSpec);
	
	//for scalar sub query
	public boolean visit(ScalarSubQuery scalarSubQuery);
	
	//for case
	public boolean visit(Case caseClause);
	public boolean visit(Else elseClause);
	public boolean visit(When when);
	
	//for window function
	public boolean visit(WindowAggregate windowAggregate);
	public boolean visit(RowNumber rowNumber);
	public boolean visit(Rank rank);
	public boolean visit(DenseRank denseRank);
	public boolean visit(PercentRank percentRank);
	public boolean visit(CumeDist cumeDist);
	public boolean visit(PartitionBy partitionBy);
	public boolean visit(WindowOrderBy orderBy);
	public boolean visit(WindowSortSpec sortSpec);
	
}
