/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.entity.query.value.aggregate.Max;
import org.iplass.mtp.entity.query.value.aggregate.Median;
import org.iplass.mtp.entity.query.value.aggregate.Min;
import org.iplass.mtp.entity.query.value.aggregate.Mode;
import org.iplass.mtp.entity.query.value.aggregate.StdDevPop;
import org.iplass.mtp.entity.query.value.aggregate.StdDevSamp;
import org.iplass.mtp.entity.query.value.aggregate.Sum;
import org.iplass.mtp.entity.query.value.aggregate.VarPop;
import org.iplass.mtp.entity.query.value.aggregate.VarSamp;
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

public class ValueExpressionVisitorSupport implements ValueExpressionVisitor {

	@Override
	public boolean visit(Literal literal) {
		return true;
	}

	@Override
	public boolean visit(EntityField entityField) {
		return true;
	}

	@Override
	public boolean visit(Count count) {
		return true;
	}

	@Override
	public boolean visit(Sum sum) {
		return true;
	}

	@Override
	public boolean visit(Polynomial polynomial) {
		return true;
	}

	@Override
	public boolean visit(Term term) {
		return true;
	}

	@Override
	public boolean visit(ParenValue parenthesizedValue) {
		return true;
	}

	@Override
	public boolean visit(MinusSign minusSign) {
		return true;
	}

	@Override
	public boolean visit(ScalarSubQuery scalarSubQuery) {
		return true;
	}

	@Override
	public boolean visit(Avg avg) {
		return true;
	}

	@Override
	public boolean visit(Max max) {
		return true;
	}

	@Override
	public boolean visit(Min min) {
		return true;
	}

	@Override
	public boolean visit(ArrayValue arrayValue) {
		return true;
	}

	@Override
	public boolean visit(Function function) {
		return true;
	}

	@Override
	public boolean visit(Cast cast) {
		return true;
	}

	@Override
	public boolean visit(Case caseClause) {
		return true;
	}

	@Override
	public boolean visit(Else elseClause) {
		return true;
	}

	@Override
	public boolean visit(When when) {
		return true;
	}

	@Override
	public boolean visit(StdDevPop stdDevPop) {
		return true;
	}

	@Override
	public boolean visit(StdDevSamp stdDevSamp) {
		return true;
	}

	@Override
	public boolean visit(VarPop varPop) {
		return true;
	}

	@Override
	public boolean visit(VarSamp varSamp) {
		return true;
	}

	@Override
	public boolean visit(Mode mode) {
		return true;
	}

	@Override
	public boolean visit(Median median) {
		return true;
	}

	@Override
	public boolean visit(WindowAggregate windowAggregate) {
		return true;
	}

	@Override
	public boolean visit(RowNumber rowNumber) {
		return true;
	}

	@Override
	public boolean visit(Rank rank) {
		return true;
	}

	@Override
	public boolean visit(DenseRank denseRank) {
		return true;
	}

	@Override
	public boolean visit(PercentRank percentRank) {
		return true;
	}

	@Override
	public boolean visit(CumeDist cumeDist) {
		return true;
	}

	@Override
	public boolean visit(PartitionBy partitionBy) {
		return true;
	}

	@Override
	public boolean visit(WindowOrderBy orderBy) {
		return true;
	}

	@Override
	public boolean visit(WindowSortSpec sortSpec) {
		return true;
	}

	@Override
	public boolean visit(RowValueList rowValueList) {
		return true;
	}
}
