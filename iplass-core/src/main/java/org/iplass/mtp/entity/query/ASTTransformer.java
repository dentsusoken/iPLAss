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

public interface ASTTransformer {

	public ASTNode visit(Literal literal);
	public ASTNode visit(EntityField entityField);
	public ASTNode visit(ParenValue parenthesizedValue);
	public ASTNode visit(Avg avg);
	public ASTNode visit(Count count);
	public ASTNode visit(Max max);
	public ASTNode visit(Min min);
	public ASTNode visit(Sum sum);
	public ASTNode visit(StdDevPop stdDevPop);
	public ASTNode visit(StdDevSamp stdDevSamp);
	public ASTNode visit(VarPop varPop);
	public ASTNode visit(VarSamp varSamp);
	public ASTNode visit(Mode mode);
	public ASTNode visit(Median median);
	public ASTNode visit(MinusSign minusSign);
	public ASTNode visit(Polynomial polynomial);
	public ASTNode visit(Term term);
	public ASTNode visit(ScalarSubQuery scalarSubQuery);
	public ASTNode visit(SubQuery subQuery);
	public ASTNode visit(From from);
	public ASTNode visit(AsOf asOf);
	public ASTNode visit(SortSpec order);
	public ASTNode visit(Query query);
	public ASTNode visit(Select select);
	public ASTNode visit(Where where);
	public ASTNode visit(And and);
	public ASTNode visit(Paren paren);
	public ASTNode visit(Not not);
	public ASTNode visit(Or or);
	public ASTNode visit(Between between);
	public ASTNode visit(Equals equals);
	public ASTNode visit(Greater greater);
	public ASTNode visit(GreaterEqual greaterEqual);
	public ASTNode visit(In in);
	public ASTNode visit(IsNotNull isNotNull);
	public ASTNode visit(IsNull isNull);
	public ASTNode visit(Lesser lesser);
	public ASTNode visit(LesserEqual lesserEqual);
	public ASTNode visit(Like like);
	public ASTNode visit(NotEquals notEquals);
	public ASTNode visit(ArrayValue arrayValue);
	public ASTNode visit(GroupBy groupBy);
	public ASTNode visit(Having having);
	public ASTNode visit(OrderBy orderBy);
	public ASTNode visit(Limit limit);
	public ASTNode visit(Function function);
	public ASTNode visit(Cast cast);
	public ASTNode visit(Refer refer);
	public ASTNode visit(Contains contains);
	public ASTNode visit(Case caseClause);
	public ASTNode visit(Else elseClause);
	public ASTNode visit(When when);
	
	public ASTNode visit(RowValueList rowValueList);
	
	public ASTNode visit(WindowAggregate windowAggregateFunction);
	public ASTNode visit(RowNumber rowNumber);
	public ASTNode visit(Rank rank);
	public ASTNode visit(DenseRank denseRank);
	public ASTNode visit(PercentRank percentRank);
	public ASTNode visit(CumeDist cumeDist);
	
	public ASTNode visit(PartitionBy partitionBy);
	public ASTNode visit(WindowOrderBy orderBy);
	public ASTNode visit(WindowSortSpec sortSpec);
	
	public ASTNode visit(HintComment hintComment);
	public ASTNode visit(IndexHint indexHint);
	public ASTNode visit(NoIndexHint noIndexHint);
	public ASTNode visit(NativeHint nativeHint);
	public ASTNode visit(BindHint bindHint);
	public ASTNode visit(CacheHint cacheHint);
	public ASTNode visit(FetchSizeHint fetchSizeHint);
	public ASTNode visit(TimeoutHint timeoutHint);
	public ASTNode visit(NoBindHint noBindHint);

}
