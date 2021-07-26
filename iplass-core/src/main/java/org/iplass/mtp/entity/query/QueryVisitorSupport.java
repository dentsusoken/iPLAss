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

package org.iplass.mtp.entity.query;

import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Not;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.expr.Paren;
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
import org.iplass.mtp.entity.query.hint.BindHint;
import org.iplass.mtp.entity.query.hint.CacheHint;
import org.iplass.mtp.entity.query.hint.FetchSizeHint;
import org.iplass.mtp.entity.query.hint.HintComment;
import org.iplass.mtp.entity.query.hint.IndexHint;
import org.iplass.mtp.entity.query.hint.NativeHint;
import org.iplass.mtp.entity.query.hint.NoBindHint;
import org.iplass.mtp.entity.query.hint.NoIndexHint;
import org.iplass.mtp.entity.query.hint.ReadOnlyHint;
import org.iplass.mtp.entity.query.hint.SuppressWarningsHint;
import org.iplass.mtp.entity.query.hint.TimeoutHint;
import org.iplass.mtp.entity.query.value.RowValueList;
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

public abstract class QueryVisitorSupport implements QueryVisitor {

	@Override
	public boolean visit(Query query) {
		return true;
	}

	@Override
	public boolean visit(Select select) {
		return true;
	}

	@Override
	public boolean visit(Where where) {
		return true;
	}

	@Override
	public boolean visit(From from) {
		return true;
	}

	@Override
	public boolean visit(And and) {
		return true;
	}

	@Override
	public boolean visit(Between between) {
		return true;
	}

	@Override
	public boolean visit(Paren paren) {
		return true;
	}

	@Override
	public boolean visit(Equals equals) {
		return true;
	}

	@Override
	public boolean visit(Greater greater) {
		return true;
	}

	@Override
	public boolean visit(GreaterEqual greaterEqual) {
		return true;
	}

	@Override
	public boolean visit(In in) {
		return true;
	}

	@Override
	public boolean visit(Lesser lesser) {
		return true;
	}

	@Override
	public boolean visit(LesserEqual lesserEqual) {
		return true;
	}

	@Override
	public boolean visit(Like like) {
		return true;
	}

	@Override
	public boolean visit(Not not) {
		return true;
	}

	@Override
	public boolean visit(NotEquals notEquals) {
		return true;
	}

	@Override
	public boolean visit(Or or) {
		return true;
	}

	@Override
	public boolean visit(IsNotNull isNotNull) {
		return true;
	}

	@Override
	public boolean visit(IsNull isNull) {
		return true;
	}

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
	public boolean visit(SubQuery subQuery) {
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
	public boolean visit(OrderBy orderBy) {
		return true;
	}

	@Override
	public boolean visit(SortSpec order) {
		return true;
	}

	@Override
	public boolean visit(GroupBy groupBy) {
		return true;
	}

	@Override
	public boolean visit(Limit limit) {
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
	public boolean visit(Refer refer) {
		return true;
	}

	@Override
	public boolean visit(Having having) {
		return true;
	}

	@Override
	public boolean visit(Contains contains) {
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
	public boolean visit(HintComment hintComment) {
		return true;
	}

	@Override
	public boolean visit(IndexHint indexHint) {
		return true;
	}

	@Override
	public boolean visit(NoIndexHint noIndexHint) {
		return true;
	}

	@Override
	public boolean visit(NativeHint nativeHint) {
		return true;
	}

	@Override
	public boolean visit(BindHint bindHint) {
		return true;
	}

	@Override
	public boolean visit(NoBindHint noBindHint) {
		return true;
	}

	@Override
	public boolean visit(AsOf asOf) {
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
	public boolean visit(CacheHint cacheHint) {
		return true;
	}

	@Override
	public boolean visit(FetchSizeHint fetchSizeHint) {
		return true;
	}

	@Override
	public boolean visit(TimeoutHint timeoutHint) {
		return true;
	}

	@Override
	public boolean visit(SuppressWarningsHint suppressWarningsHint) {
		return true;
	}

	@Override
	public boolean visit(ReadOnlyHint readOnlyHint) {
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
