/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.grdb.sql.queryconvert;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.From;
import org.iplass.mtp.entity.query.GroupBy;
import org.iplass.mtp.entity.query.Having;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.Refer;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
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
import org.iplass.mtp.entity.query.condition.predicate.Like.CaseType;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;
import org.iplass.mtp.entity.query.hint.BindHint;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.HintComment;
import org.iplass.mtp.entity.query.hint.IndexHint;
import org.iplass.mtp.entity.query.hint.NativeHint;
import org.iplass.mtp.entity.query.hint.NoBindHint;
import org.iplass.mtp.entity.query.hint.NoIndexHint;
import org.iplass.mtp.entity.query.value.RowValueList;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.aggregate.Aggregate;
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
import org.iplass.mtp.entity.query.value.window.WindowRankFunction;
import org.iplass.mtp.entity.query.value.window.WindowSortSpec;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.ToSqlResult.BindValue;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlQueryContext.Clause;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlQueryContext.QueryBindValue;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjIndexTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.AggregateFunctionAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter.FunctionContext;

public class SqlConverter extends QueryVisitorSupport {
	//TODO 全体的にReferenceを指定された場合の対応がされていない
	
	//TODO sqlインジェクション対策の再確認（sql文（みたいな構文）自体をapiから指定可能。項目値だけでなく、sql文自体にインジェクションの糸口がないかどうか）

	private static final String INDEX_TABLE_ALIAS = "it";

	private RdbAdapter rdbAdaptor;
	
	private SqlQueryContext context;
	
	private ArrayList<ASTNode> astNodeStack;//existsが使えない場合、INDEXテーブルの結合可能かどうかを判断する用のAstNodeのstack
	
	private boolean treatBindHint;//BINDヒントを処理するか否か

	public SqlConverter(SqlQueryContext context, boolean treatBindHint) {
		this.rdbAdaptor = context.getRdb();
		this.context = context;
		this.treatBindHint = treatBindHint && rdbAdaptor.isEnableBindHint();
		if (!rdbAdaptor.isUseSubQueryForIndexJoin()) {
			astNodeStack = new ArrayList<ASTNode>();
		}
	}
	
	private void push(ASTNode astNode) {
		if (!rdbAdaptor.isUseSubQueryForIndexJoin()) {
			astNodeStack.add(astNode);
		}
	}
	
	private void pop(ASTNode astNode) {
		if (!rdbAdaptor.isUseSubQueryForIndexJoin()) {
			ASTNode removed = astNodeStack.remove(astNodeStack.size() - 1);
			if (removed != astNode) {
				throw new IllegalStateException("astNode stack is confused...:" + astNode);
			}
		}
	}

	@Override
	public boolean visit(From from) {
		push(from);
		try {
			context.setFrom(from.getEntityName());
			return true;
		} finally {
			pop(from);
		}
	}

	@Override
	public boolean visit(Query query) {
		push(query);
		try {
			return super.visit(query);
		} finally {
			pop(query);
		}
	}

	@Override
	public boolean visit(ArrayValue arrayValue) {
		push(arrayValue);
		try {
			return super.visit(arrayValue);
		} finally {
			pop(arrayValue);
		}
	}

	@Override
	public boolean visit(Contains contains) {
		push(contains);
		try {
			return super.visit(contains);
		} finally {
			pop(contains);
		}
	}

	@Override
	public boolean visit(Select select) {
		push(select);
		try {
			if (select.getHintComment() != null) {
				select.getHintComment().accept(this);
			}
			
			context.changeCurrentClause(Clause.SELECT);
			int colCount = 1;
			
			if (select.isDistinct()) {
				context.append("DISTINCT ");
			}
			boolean isFirst = true;
			for (ValueExpression selectVal : select.getSelectValues()) {
				if (isFirst) {
					isFirst = false;
				} else {
					context.append(",");
				}
				
				//special logic for EntityField.配列対応のため。（別名付けたい）
				if (selectVal instanceof EntityField) {
					EntityField entityField = (EntityField) selectVal;
					PropertyHandler pDef = context.getProperty(entityField.getPropertyName());
					if (pDef == null) {
						throw new QueryException("not define property:" + entityField);
					}
					context.notifyUsedPropertyName(entityField.getPropertyName());
					GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) pDef.getStoreSpecProperty();
					if (col == null) {
						throw new QueryException("Reference property:" + entityField + " itself can not be specified in select clause. Specify " + entityField + ".oid or other.");
					}

					col = targetColByIndexIfMulti(col, entityField);
					String[] castExp = castExp(pDef);
					List<GRdbPropertyStoreHandler> colList = col.asList();
					for (int i = 0; i < colList.size(); i++) {
						if (i != 0) {
							context.append(",");
						}
						if (castExp != null) {
							context.getCurrentSb().append(castExp[0]);
						}
						colExp(context.getCurrentSb(), entityField.getPropertyName(), colList.get(i), context.isTreatSelectAsRawValue());
						if (castExp != null) {
							context.getCurrentSb().append(castExp[1]);
						}
						context.append(" as c" + colCount);//limit且つ、joinしていると別名をつけないとエラーとなってしまう。
						colCount++;
					}
				} else {
					
					if (context.isTreatSelectAsRawValue()) {
						PropertyType type = context.getValueTypeResolver().resolve(selectVal);
						BaseRdbTypeAdapter typeAdapter = context.getRdb().getRdbTypeAdapter(type);
						typeAdapter.appendToTypedCol(context.getCurrentSb(), rdbAdaptor,
								() -> selectVal.accept(this));
					} else {
						selectVal.accept(this);
					}
					
					context.append(" as c" + colCount);//limit且つ、joinしていると別名をつけないとエラーとなってしまう。
					colCount++;
				}
			}
			return false;
		} finally {
			pop(select);
		}
	}

	@Override
	public boolean visit(Where where) {
		push(where);
		try {
			context.changeCurrentClause(Clause.WHERE);
			return true;
		} finally {
			pop(where);
		}
	}

	@Override
	public boolean visit(Literal literal) {
		push(literal);
		try {
			BaseRdbTypeAdapter type = rdbAdaptor.getRdbTypeAdapter(literal.getValue());
			if (literal.getValue() != null && context.isEnableBindVariable() && literal.isBindable()) {
				type.appendParameterPlaceholder(context.getCurrentSb(), rdbAdaptor);
				context.addBindVariable(literal.getValue(), type);
			} else {
				type.appendToSqlAsRealType(literal.getValue(), context.getCurrentSb(), rdbAdaptor);
			}
			return true;
		} finally {
			pop(literal);
		}
	}
	
	private void colExp(StringBuilder sb, String propName, GRdbPropertyStoreHandler col, boolean treatAsRawValue) {
		if (treatAsRawValue || col.isNative()) {
			String colPrefix = context.getColPrefix(propName, col);
			sb.append(colPrefix);
			if (col.getIndexColName() != null && context.checkIndexHint(propName, false)) {
				sb.append(col.getIndexColName());
			} else {
				sb.append(col.getMetaData().getColumnName());
			}
		} else {
			col.getSingleColumnRdbTypeAdapter().appendFromTypedCol(sb, rdbAdaptor,
					() -> colExp(sb, propName, col, true));
		}
	}
	
	private String[] castExp(PropertyHandler pDef) {
		String[] castExp = null;
		if ((context.getCurrentClause() == Clause.SELECT || context.getCurrentClause() == Clause.ORDERBYGROUPBY)
				&& context.getStringTypeLengthOnQuery() != null) {
			GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) pDef.getStoreSpecProperty();
			if (!col.isNative()) {
				if (col.getSingleColumnRdbTypeAdapter().sqlType() == Types.VARCHAR
						&& pDef.getEnumType() != PropertyDefinitionType.BINARY
						&& pDef.getEnumType() != PropertyDefinitionType.LONGTEXT) {
					//binary型以外の文字列カラムの値を指定長で切る
					castExp = context.getRdb().castExp(Types.VARCHAR, context.getStringTypeLengthOnQuery(), null);
				}
			}
		}
		return castExp;

	}
	
	@Override
	public boolean visit(EntityField entityField) {
		push(entityField);
		try {
			PropertyHandler pDef = context.getProperty(entityField.getPropertyName());
			if (pDef == null) {
				throw new QueryException("not define property:" + entityField);
			}

			context.notifyUsedPropertyName(entityField.getPropertyName());
			
			GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) pDef.getStoreSpecProperty();
			if (col == null) {
				throw new QueryException("Reference property:" + entityField + " itself can not be specified. Specify " + entityField + ".oid or other.");
			}
			
			col = targetColByIndexIfMulti(col, entityField);
			String[] castExp = castExp(pDef);
			List<GRdbPropertyStoreHandler> cols = col.asList();
			for (int i = 0; i < cols.size(); i++) {
				if (i != 0) {
					context.append(",");
				}
				if (castExp != null) {
					context.getCurrentSb().append(castExp[0]);
				}
				colExp(context.getCurrentSb(), entityField.getPropertyName(), cols.get(i), false);
				if (castExp != null) {
					context.getCurrentSb().append(castExp[1]);
				}
			}
			
			return true;
		} finally {
			pop(entityField);
		}
	}
	
	@Override
	public boolean visit(RowValueList rowValueList) {
		push(rowValueList);
		try {
			context.append("(");
			boolean isFirst = true;
			for (ValueExpression e: rowValueList.getRowValues()) {
				if (isFirst) {
					isFirst = false;
				} else {
					context.append(",");
				}
				e.accept(this);
			}
			context.append(")");

			return false;
		} finally {
			pop(rowValueList);
		}
	}

	@Override
	public boolean visit(Count count) {
		push(count);
		try {
			aggregateFunction(count);
			return false;
		} finally {
			pop(count);
		}
	}
	
	private void aggregateFunction(Aggregate agg) {
		@SuppressWarnings("unchecked")
		AggregateFunctionAdapter<Aggregate> fa = (AggregateFunctionAdapter<Aggregate>) rdbAdaptor.resolveAggregateFunction(agg.getClass());
		if (fa == null) {
			throw new QueryException(agg.getClass().getSimpleName() + " not supported.");
		}
		
		fa.toSQL(new FunctionContext() {
			@Override
			public void appendArgument(ValueExpression arg) {
				arg.accept(SqlConverter.this);
			}
			
			@Override
			public void append(String str) {
				context.append(str);
			}
		}, agg, rdbAdaptor);
	}
	
	private void simpleAggregateFunction(Aggregate agg) {
		String funcName = rdbAdaptor.aggregateFunctionName(agg);
		context.append(funcName);
		context.append("(");
		agg.getValue().accept(this);
		context.append(")");
	}
	
	@Override
	public boolean visit(Sum sum) {
		push(sum);
		try {
			simpleAggregateFunction(sum);
			return false;
		} finally {
			pop(sum);
		}
	}
	
	@Override
	public boolean visit(Avg avg) {
		push(avg);
		try {
			simpleAggregateFunction(avg);
			return false;
		} finally {
			pop(avg);
		}
	}

	@Override
	public boolean visit(Max max) {
		push(max);
		try {
			simpleAggregateFunction(max);
			return false;
		} finally {
			pop(max);
		}
	}

	@Override
	public boolean visit(Min min) {
		push(min);
		try {
			simpleAggregateFunction(min);
			return false;
		} finally {
			pop(min);
		}
	}

	@Override
	public boolean visit(StdDevPop stdDevPop) {
		push(stdDevPop);
		try {
			simpleAggregateFunction(stdDevPop);
			return false;
		} finally {
			pop(stdDevPop);
		}
	}

	@Override
	public boolean visit(StdDevSamp stdDevSamp) {
		push(stdDevSamp);
		try {
			simpleAggregateFunction(stdDevSamp);
			return false;
		} finally {
			pop(stdDevSamp);
		}
	}

	@Override
	public boolean visit(VarPop varPop) {
		push(varPop);
		try {
			simpleAggregateFunction(varPop);
			return false;
		} finally {
			pop(varPop);
		}
	}

	@Override
	public boolean visit(VarSamp varSamp) {
		push(varSamp);
		try {
			simpleAggregateFunction(varSamp);
			return false;
		} finally {
			pop(varSamp);
		}
	}

	@Override
	public boolean visit(Mode mode) {
		push(mode);
		try {
			simpleAggregateFunction(mode);
			return false;
		} finally {
			pop(mode);
		}
	}

	@Override
	public boolean visit(Median median) {
		push(median);
		try {
			simpleAggregateFunction(median);
			return false;
		} finally {
			pop(median);
		}
	}
	
	@Override
	public boolean visit(Listagg listagg) {
		push(listagg);
		try {
			aggregateFunction(listagg);
			return false;
		} finally {
			pop(listagg);
		}
	}

	@Override
	public boolean visit(WithinGroup withinGroup) {
		return false;
	}

	@Override
	public boolean visit(WithinGroupSortSpec sortSpec) {
		return false;
	}

	@Override
	public boolean visit(Polynomial polynomial) {
		push(polynomial);
		try {
			if (polynomial.getAddValues() != null) {
				for (int i = 0; i < polynomial.getAddValues().size(); i++) {
					if (i != 0) {
						context.append("+");
					}
					ValueExpression child = polynomial.getAddValues().get(i);
					child.accept(this);
				}
			}
			if (polynomial.getSubValues() != null) {
				for (int i = 0; i < polynomial.getSubValues().size(); i++) {
					context.append("-");
					ValueExpression child = polynomial.getSubValues().get(i);
					child.accept(this);
				}
			}
			return false;
		} finally {
			pop(polynomial);
		}
	}

	@Override
	public boolean visit(Term term) {
		push(term);
		try {
			if (term.getMulValues() != null) {
				for (int i = 0; i < term.getMulValues().size(); i++) {
					if (i != 0) {
						context.append("*");
					}
					ValueExpression child = term.getMulValues().get(i);
					child.accept(this);
				}
			} else {
				context.append("1");
			}
			if (term.getDivValues() != null) {
				for (int i = 0; i < term.getDivValues().size(); i++) {
					context.append("/");
					ValueExpression child = term.getDivValues().get(i);
					child.accept(this);
				}
			}
			return false;
		} finally {
			pop(term);
		}
	}
	
	@Override
	public boolean visit(MinusSign minusSign) {
		push(minusSign);
		try {
			if (minusSign.getValue() != null) {
				context.append("-");
				minusSign.getValue().accept(this);
	
				return false;
			} else {
				throw new QueryException(
						"Cannt convert MinusSign because value is null.");
			}
		} finally {
			pop(minusSign);
		}
	}

	@Override
	public boolean visit(ParenValue parenthesizedValue) {
		push(parenthesizedValue);
		try {
			if (parenthesizedValue.getNestedValue() != null) {
				context.append("(");
				parenthesizedValue.getNestedValue().accept(this);
				context.append(")");
	
				return false;
			} else {
				throw new QueryException(
						"Cannt convert BracketValue because nestedValue  is null.");
			}
		} finally {
			pop(parenthesizedValue);
		}
	}

	@Override
	public boolean visit(And andExpression) {
		push(andExpression);
		try {
	
			if (andExpression.getChildExpressions() != null) {
				for (int i = 0; i < andExpression.getChildExpressions().size(); i++) {
					if (i != 0) {
						context.append(" AND ");
					}
					Condition child = andExpression.getChildExpressions().get(i);
					child.accept(this);
				}
				return false;
			} else {
				throw new QueryException(
						"Cannt convert AndExpression because child is null.");
			}
		} finally {
			pop(andExpression);
		}
	}

	@Override
	public boolean visit(Or orExpression) {
		push(orExpression);
		try {
			if (orExpression.getChildExpressions() != null) {
				for (int i = 0; i < orExpression.getChildExpressions().size(); i++) {
					if (i != 0) {
						context.append(" OR ");
					}
					Condition child = orExpression.getChildExpressions().get(i);
					child.accept(this);
				}
				return false;
			} else {
				throw new QueryException(
						"Cannt convert OrExpression because child is null.");
			}
		} finally {
			pop(orExpression);
		}
	}

	@Override
	public boolean visit(Equals equalsExpression) {
		push(equalsExpression);
		try {
			if (!(equalsExpression.getProperty() instanceof EntityField)
					&& equalsExpression.getValue() instanceof EntityField) {
				simpleOp(equalsExpression.getValue(), "=", equalsExpression
						.getProperty(), false);
			} else {
				simpleOp(equalsExpression.getProperty(), "=", equalsExpression
						.getValue(), false);
			}
			return false;
		} finally {
			pop(equalsExpression);
		}
	}

	@Override
	public boolean visit(NotEquals notEqualsExpression) {
		push(notEqualsExpression);
		try {
			if (!(notEqualsExpression.getProperty() instanceof EntityField)
					&& notEqualsExpression.getValue() instanceof EntityField) {
				simpleOp(notEqualsExpression.getValue(), "!=",
						notEqualsExpression.getProperty(), true);
			} else {
				simpleOp(notEqualsExpression.getProperty(), "!=",
						notEqualsExpression.getValue(), true);
			}
			return false;
		} finally {
			pop(notEqualsExpression);
		}
	}

	@Override
	public boolean visit(Greater greaterExpression) {
		push(greaterExpression);
		try {
			if (!(greaterExpression.getProperty() instanceof EntityField)
					&& greaterExpression.getValue() instanceof EntityField) {
				simpleOp(greaterExpression.getValue(), "<", greaterExpression
						.getProperty(), false);
			} else {
				simpleOp(greaterExpression.getProperty(), ">", greaterExpression
						.getValue(), false);
			}
			return false;
		} finally {
			pop(greaterExpression);
		}
	}

	@Override
	public boolean visit(GreaterEqual greaterEqualExpression) {
		push(greaterEqualExpression);
		try {
			if (!(greaterEqualExpression.getProperty() instanceof EntityField)
					&& greaterEqualExpression.getValue() instanceof EntityField) {
				simpleOp(greaterEqualExpression.getValue(), "<=",
						greaterEqualExpression.getProperty(), false);
			} else {
				simpleOp(greaterEqualExpression.getProperty(), ">=",
						greaterEqualExpression.getValue(), false);
			}
			return false;
		} finally {
			pop(greaterEqualExpression);
		}
	}

	@Override
	public boolean visit(Lesser lesserExpression) {
		push(lesserExpression);
		try {
			if (!(lesserExpression.getProperty() instanceof EntityField)
					&& lesserExpression.getValue() instanceof EntityField) {
				simpleOp(lesserExpression.getValue(), ">", lesserExpression
						.getProperty(), false);
			} else {
				simpleOp(lesserExpression.getProperty(), "<", lesserExpression
						.getValue(), false);
			}
			return false;
		} finally {
			pop(lesserExpression);
		}
	}

	@Override
	public boolean visit(LesserEqual lesserEqualExpression) {
		push(lesserEqualExpression);
		try {
			if (!(lesserEqualExpression.getProperty() instanceof EntityField)
					&& lesserEqualExpression.getValue() instanceof EntityField) {
				simpleOp(lesserEqualExpression.getValue(), ">=",
						lesserEqualExpression.getProperty(), false);
			} else {
				simpleOp(lesserEqualExpression.getProperty(), "<=",
						lesserEqualExpression.getValue(), false);
			}
			return false;
		} finally {
			pop(lesserEqualExpression);
		}
	}

	private void simpleOp(ValueExpression propVal, final String op,
			final ValueExpression val, boolean isNeq) {
		
		if (propVal instanceof EntityField) {//special logic for EntityField
			
			String propName = ((EntityField) propVal).getPropertyName();
			
			// JOIN対応
			context.notifyUsedPropertyName(propName);

			final PropertyHandler prop = context.getProperty(propName);
			if (prop == null) {
				throw new NullPointerException(context.getFromEntity().getMetaData().getName() + "." + propName + " is not defined..");
			}
			final GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) prop.getStoreSpecProperty();
			if (col == null) {
				throw new QueryException("Reference property:" + propName + " itself can not be specified. Specify " + propName + ".oid or other.");
			}
			
			//配列対応（※配列はindex不可とする）
			if (prop.getMetaData().getMultiplicity() > 1
					&& ((EntityField) propVal).getArrayIndex() == EntityField.ARRAY_INDEX_UNSPECIFIED) {
				context.append("(");
				if (val instanceof ArrayValue) {
					//ValueExpressionがArrayValueの場合は、配列内の各要素単位で比較する
					//!=の場合は、全体にNOT
					String opToUse = op;
					if (isNeq) {
						context.append("NOT(");
						opToUse = "=";
					}
					ArrayValue array = (ArrayValue) val;
					if (array.getValues() != null && array.getValues().size() > prop.getMetaData().getMultiplicity()) {
						throw new QueryException("Array size of ArrayValue is larger than property multiplicity. property:" + propName
								+ ", multiplicity:" + prop.getMetaData().getMultiplicity() + ", array size:" + array.getValues().size());
					}
					int loops = prop.getMetaData().getMultiplicity();
					List<GRdbPropertyStoreHandler> cols = col.asList();
					for (int i = 0; i < loops; i++) {
						if (i != 0) {
							context.append(" AND ");
						}
						colExp(context.getCurrentSb(), propName, cols.get(i), false);
						if (array.getValues() == null || array.getValues().size() <= i) {
							//配列要素が足りない場合はnullとみなす
							context.append(" IS NULL");
						} else {
							ValueExpression ve = array.getValues().get(i);
							if (ve instanceof Literal && ((Literal) ve).getValue() == null) {
								//nullリテラルの場合
								context.append(" IS NULL");
							} else {
								context.append(opToUse);
								valueConvert(ve, col.getSingleColumnRdbTypeAdapter(), false);
								context.append(" AND ");
								colExp(context.getCurrentSb(), propName, cols.get(i), false);
								context.append(" IS NOT NULL");
							}
						}
					}
					if (isNeq) {
						context.append(")");
					}
				} else {
					//!=を除き、ANYとみなす（プロパティの配列要素の内どれかひとつでもtrueの場合、trueとみなす）
					//!= の場合は、ALL（プロパティの配列要素の内全てがtrueの場合、trueとみなす）
					List<GRdbPropertyStoreHandler> cols = col.asList();
					for (int i = 0; i < cols.size(); i++) {
						if (i != 0) {
							if (!isNeq) {
								context.append(" OR ");
							} else {
								context.append(" AND ");
							}
						}
						context.append("(");
						colExp(context.getCurrentSb(), propName, cols.get(i), false);
						context.append(op);
						valueConvert(val, col.getSingleColumnRdbTypeAdapter(), false);
						if (!isNeq) {
							context.append(" AND ");
						} else {
							context.append(" OR ");
						}
						colExp(context.getCurrentSb(), propName, cols.get(i), false);
						if (!isNeq) {
							context.append(" IS NOT NULL)");
						} else {
							context.append(" IS NULL)");
						}
					}
				}
				context.append(")");
			} else if (useExternalIndex(prop, propName)) {
				//外部インデックスされているプロパティの場合
				externalIndexedSql(propName, (PrimitivePropertyHandler) prop,
						(tableName) -> {
							// EXISTSによる条件文を構築。
							context.append(tableName).append(".VAL").append(op);
							valueConvert(val, col.getSingleColumnRdbTypeAdapter(), true);
						});
			} else {
				//内部インデックス、単純プロパティ、配列インデックス指定の場合
				final GRdbPropertyStoreRuntime targetCol = targetColByIndexIfMulti(col, (EntityField) propVal);
				internalIndexedSql(propName, (PrimitivePropertyHandler) prop, false,
						() -> {
							if (val instanceof EntityField) {
								String valPropName = ((EntityField) val).getPropertyName();
								
								// JOIN対応
								context.notifyUsedPropertyName(valPropName);

								final PropertyHandler valProp = context.getProperty(valPropName);
								if (valProp == null) {
									throw new NullPointerException(context.getFromEntity().getMetaData().getName() + "." + valPropName + " is not defined..");
								}
								final GRdbPropertyStoreRuntime valCol = (GRdbPropertyStoreRuntime) valProp.getStoreSpecProperty();
								if (valCol == null) {
									throw new QueryException("Reference property:" + valPropName + " itself can not be specified. Specify " + valPropName + ".oid or other.");
								}
								
								final GRdbPropertyStoreRuntime targetValCol = targetColByIndexIfMulti(valCol, (EntityField) val);
								internalIndexedSql(valPropName, (PrimitivePropertyHandler) valProp, false,
										() -> {
											colExp(context.getCurrentSb(), propName, (GRdbPropertyStoreHandler) targetCol, true);
											context.append(op);
											colExp(context.getCurrentSb(), valPropName, (GRdbPropertyStoreHandler) targetValCol, true);
										});
							} else {
								colExp(context.getCurrentSb(), propName, (GRdbPropertyStoreHandler) targetCol, true);
								context.append(op);
								if (targetCol.isNative()) {
									//nativeの場合は変換なし
									valueConvert(val, targetCol.getSingleColumnRdbTypeAdapter(), false);
								} else {
									valueConvert(val, targetCol.getSingleColumnRdbTypeAdapter(), true);
								}
							}
						});
			}
		} else {
			propVal.accept(this);
			context.append(op);
			PropertyType type = context.getValueTypeResolver().resolve(propVal);
			valueConvert(val, context.getRdb().getRdbTypeAdapter(type), false);
		}
	}
	
	private GRdbPropertyStoreRuntime targetColByIndexIfMulti(GRdbPropertyStoreRuntime col, EntityField specifier) {
		if (col.isMulti() && specifier.getArrayIndex() != EntityField.ARRAY_INDEX_UNSPECIFIED) {
			//配列インデックス指定の場合
			List<GRdbPropertyStoreHandler> valCols = col.asList();
			if (specifier.getArrayIndex() < 0
					|| specifier.getArrayIndex() >= valCols.size()) {
				throw new QueryException(
						"Array index out of range. property:" + specifier.getPropertyName() + ", index:" + specifier.getArrayIndex());
			}
			return valCols.get(specifier.getArrayIndex());
		} else {
			return col;
		}
	}

	private void internalIndexedSql(String propName,
			PrimitivePropertyHandler prop, boolean isNullOp, Runnable callback) {
		
		if (prop.getMetaData().getMultiplicity() > 1) {
			callback.run();
		} else {
			//単純プロパティの場合、内部index利用可能
			GRdbPropertyStoreHandler col = (GRdbPropertyStoreHandler) prop.getStoreSpecProperty();

			//内部index利用時
			boolean useIndexCol = col.getIndexColName() != null && context.checkIndexHint(propName, false) && !isNullOp
					&& context.getCurrentClause() == Clause.WHERE;
			if (useIndexCol) {
				context.append("(");
				String colPrefix = context.getColPrefix(propName, col);
				context.append(colPrefix);
				context.append(col.getIndexColName());
				context.append(ObjStoreTable.INDEX_TD_POSTFIX);

				context.append("='");
				context.append(MetaGRdbPropertyStore.makeInternalIndexKey(
						context.getMetaContext().getTenantId(prop.getParent()), prop.getParent().getMetaData().getId(),
						col.getMetaData().getIndexPageNo()));
				context.append("'");
				context.append(" AND ");
			}

			callback.run();
			
			if (useIndexCol) {
				context.append(")");
			}
		}
	}
	
	private boolean canUseIndexTableJoin() {
		
		for (int i = astNodeStack.size() - 1; i >= 0; i--) {
			ASTNode n = astNodeStack.get(i);
			if (n instanceof Where) {
				break;
			}
			if (n instanceof Or
					|| n instanceof Not) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean useExternalIndex(PropertyHandler prop, String fullPropName) {
		return prop.isIndexed()
				&& prop instanceof PrimitivePropertyHandler
				&& context.isUseIndexTable()
				&& !((GRdbPropertyStoreRuntime) prop.getStoreSpecProperty()).isNative()
				&& ((GRdbPropertyStoreRuntime) prop.getStoreSpecProperty()).isExternalIndex()
				&& context.checkIndexHint(fullPropName, true)
				&& (rdbAdaptor.isUseSubQueryForIndexJoin() || canUseIndexTableJoin());
	}
	
	private void externalIndexedSql(String propName,
			PrimitivePropertyHandler prop, Consumer<String> callback) {
		//not multiなものだけ想定
		GRdbPropertyStoreHandler propertyStoreHanlder = (GRdbPropertyStoreHandler) prop.getStoreSpecProperty();
		String tableName = null;
		if (prop.getMetaData().getIndexType() == IndexType.NON_UNIQUE) {
			tableName = ((GRdbEntityStoreRuntime) prop.getParent().getEntityStoreRuntime()).OBJ_INDEX(propertyStoreHanlder.getSingleColumnRdbTypeAdapter().getColOfIndex());
		} else {
			tableName = ((GRdbEntityStoreRuntime) prop.getParent().getEntityStoreRuntime()).OBJ_UNIQUE(propertyStoreHanlder.getSingleColumnRdbTypeAdapter().getColOfIndex());
		}

		//Existsによる相関副問い合わせ or Indexテーブルを利用した結合（可能な場合） 
		if (rdbAdaptor.isUseSubQueryForIndexJoin()) {
			//Existsによる相関副問い合わせ
			String colPrefix = context.getColPrefix(propName, propertyStoreHanlder);
			context.append(" EXISTS(SELECT * FROM ");
			context.append(tableName);
			context.append(" WHERE ");
			context.append(colPrefix);
			context.append(ObjStoreTable.TENANT_ID + "=").append(tableName).append("." + ObjIndexTable.TENANT_ID);
			context.append(" AND ");
			context.append(colPrefix);
			context.append(ObjStoreTable.OBJ_DEF_ID + "=").append(tableName).append("." + ObjIndexTable.OBJ_DEF_ID);
			context.append(" AND ");
			context.append(colPrefix);
			context.append(ObjStoreTable.OBJ_ID + "=").append(tableName).append("." + ObjIndexTable.OBJ_ID);
			if (prop.getMetaData().getIndexType() == IndexType.NON_UNIQUE) {
				context.append(" AND ");
				context.append(colPrefix);
				context.append(ObjStoreTable.OBJ_VER + "=").append(tableName).append("." + ObjIndexTable.OBJ_VER);
			}
			context.append(" AND ");
			context.append(tableName).append("." + ObjIndexTable.OBJ_DEF_ID + "='").append(prop.getParent().getMetaData().getId()).append("'");
			context.append(" AND ");
			context.append(tableName).append("." + ObjIndexTable.COL_NAME + "='").append(propertyStoreHanlder.getExternalIndexColName());
			context.append("' AND ");
			callback.accept(tableName);
			context.append(")");

		} else {
			//Indexテーブルを利用した内部結合（可能な場合） 
			
			//一時Index Tableを作成
			StringBuilder indexTableSb = new StringBuilder();
			indexTableSb.append("(SELECT ").append(INDEX_TABLE_ALIAS).append(".* FROM ").append(tableName).append(" ").append(INDEX_TABLE_ALIAS);
			indexTableSb.append(" WHERE ");
			indexTableSb.append(INDEX_TABLE_ALIAS).append("." + ObjIndexTable.TENANT_ID + "=").append(context.getMetaContext().getTenantId(prop.getParent()));
			indexTableSb.append(" AND ");
			indexTableSb.append(INDEX_TABLE_ALIAS).append("." + ObjIndexTable.OBJ_DEF_ID + "='").append(prop.getParent().getMetaData().getId()).append("'");
			indexTableSb.append(" AND ");
			indexTableSb.append(INDEX_TABLE_ALIAS).append("." + ObjIndexTable.COL_NAME + "='").append(propertyStoreHanlder.getExternalIndexColName());
			indexTableSb.append("' AND (");
			
			//JoinPath,Aliases,bindVariablesは共有
			SqlQueryContext referContext = new SqlQueryContext(
					context.getFromEntity(),
					context.getMetaContext(),
					rdbAdaptor,
					context.getPrefix(),
					context.getAliases(),
					context.getJoinPath(),
					context.getIndexTable(),
					context.isEnableBindVariable());
			referContext.setUseIndexTable(false);
			
			SqlQueryContext mainContext = context;
			context = referContext;
			context.changeCurrentClause(Clause.WHERE);
			
			callback.accept(INDEX_TABLE_ALIAS);

			if (referContext.getCurrentClause() != Clause.WHERE) {
				referContext.changeCurrentClause(Clause.WHERE);
			}
			context = mainContext;
			
			indexTableSb.append(referContext.getCurrentSb().toString());
			indexTableSb.append("))");
			
			String indexTableAlias = context.addIndexTable(indexTableSb.toString());
			String colPrefix = context.getColPrefix(propName, propertyStoreHanlder);

			context.append(" (");
			context.append(colPrefix);
			context.append(ObjStoreTable.TENANT_ID + "=").append(indexTableAlias).append("." + ObjIndexTable.TENANT_ID);
			context.append(" AND ");
			context.append(colPrefix);
			context.append(ObjStoreTable.OBJ_DEF_ID + "=").append(indexTableAlias).append("." + ObjIndexTable.OBJ_DEF_ID);
			context.append(" AND ");
			context.append(colPrefix);
			context.append(ObjStoreTable.OBJ_ID + "=").append(indexTableAlias).append("." + ObjIndexTable.OBJ_ID);
			if (prop.getMetaData().getIndexType() == IndexType.NON_UNIQUE) {
				context.append(" AND ");
				context.append(colPrefix);
				context.append(ObjStoreTable.OBJ_VER + "=").append(indexTableAlias).append("." + ObjIndexTable.OBJ_VER);
			}
			context.append(")");
			
			List<BindValue> indexTableBindVarialbes = referContext.toOrderedBindVariables(false);
			if (indexTableBindVarialbes != null && indexTableBindVarialbes.size() > 0) {
				context.setEnableBindVariable(true);
				for (BindValue b: indexTableBindVarialbes) {
					((QueryBindValue) b).inIndexTable = true;
					((QueryBindValue) b).clause = context.getCurrentClause();
					context.getBindVariables().add(b);
				}
			}
		}
	}

	private void valueConvert(final ValueExpression val, final BaseRdbTypeAdapter typeAdapter, boolean adaptToRawColValue) {
		if (adaptToRawColValue) {
			typeAdapter.appendToTypedCol(context.getCurrentSb(), rdbAdaptor,
					() -> val.accept(this));
		} else {
			val.accept(this);
		}
	}

	@Override
	public boolean visit(final Between betweenExpression) {
		push(betweenExpression);
		try {
			
			ValueExpression propVal = betweenExpression.getProperty();
			
			if (propVal instanceof EntityField) {//special logic for EntityField
				String propName = betweenExpression.getPropertyName();
				// JOIN対応
				context.notifyUsedPropertyName(propName);
	
				final PropertyHandler prop = context.getProperty(propName);
				if (prop == null) {
					throw new NullPointerException(propName + " is not defined..");
				}
				final GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) prop.getStoreSpecProperty();
				if (col == null) {
					throw new QueryException("Reference property:" + propName + " itself can not be specified. Specify " + propName + ".oid or other.");
				}
				
				//配列対応（※配列はindex不可とする）
				//TODO 配列対応、とりあえずANYで実装しているが、ALLも用途がありそう、、、 どっちも微妙か、、、
				if (prop.getMetaData().getMultiplicity() > 1
						&& ((EntityField) propVal).getArrayIndex() == EntityField.ARRAY_INDEX_UNSPECIFIED) {
					context.append("(");
					List<GRdbPropertyStoreHandler> cols = col.asList();
					for (int i = 0; i < cols.size(); i++) {
						if (i != 0) {
							context.append(" OR ");
						}
						context.append("(");
						colExp(context.getCurrentSb(), propName, cols.get(i), false);
						context.append(" BETWEEN ");
						valueConvert(betweenExpression.getFrom(), col.getSingleColumnRdbTypeAdapter(), false);
						context.append(" AND ");
						valueConvert(betweenExpression.getTo(), col.getSingleColumnRdbTypeAdapter(), false);
						context.append(" AND ");
						colExp(context.getCurrentSb(), propName, cols.get(i), false);
						context.append(" IS NOT NULL)");
					}
					context.append(")");
				} else {
					if (useExternalIndex(prop, propName)) {
						externalIndexedSql(propName, (PrimitivePropertyHandler) prop,
								(tableName) -> {
								// EXISTSによる条件文を構築。
								context.append(tableName).append(".VAL");
								context.append(" BETWEEN ");
								valueConvert(betweenExpression.getFrom(), col.getSingleColumnRdbTypeAdapter(), true);
								context.append(" AND ");
								valueConvert(betweenExpression.getTo(), col.getSingleColumnRdbTypeAdapter(), true);
						});
					} else {
						//内部インデックス、単純プロパティ、配列インデックス指定の場合
						GRdbPropertyStoreRuntime targetCol = targetColByIndexIfMulti(col, (EntityField) propVal);
						internalIndexedSql(propName, (PrimitivePropertyHandler) prop, false,
								() -> {
									colExp(context.getCurrentSb(), propName, (GRdbPropertyStoreHandler) targetCol, true);
									context.append(" BETWEEN ");
									//nativeの場合は変換なし
									valueConvert(betweenExpression.getFrom(), targetCol.getSingleColumnRdbTypeAdapter(), !targetCol.isNative());
									context.append(" AND ");
									//nativeの場合は変換なし
									valueConvert(betweenExpression.getTo(), targetCol.getSingleColumnRdbTypeAdapter(), !targetCol.isNative());
								});
					}
				}
			} else {
				propVal.accept(this);
				context.append(" BETWEEN ");
				PropertyType type = context.getValueTypeResolver().resolve(propVal);
				BaseRdbTypeAdapter rdbType = context.getRdb().getRdbTypeAdapter(type);
				
				valueConvert(betweenExpression.getFrom(), rdbType, false);
				context.append(" AND ");
				valueConvert(betweenExpression.getTo(), rdbType, false);
			}
	
			return false;
		} finally {
			pop(betweenExpression);
		}
	}

	@Override
	public boolean visit(Paren paren) {
		push(paren);
		try {
			if (paren.getNestedExpression() != null) {
				context.append("(");
				paren.getNestedExpression().accept(this);
				context.append(")");
	
				return false;
			} else {
				throw new QueryException(
						"Cannt convert ParenExpression because nestedExpression  is null.");
			}
		} finally {
			pop(paren);
		}
	}

	@Override
	public boolean visit(final In in) {
		push(in);
		try {
			
			//FIXME かなり冗長なコードになっているので、そのうちリファクタリング
			
			List<ValueExpression> propList = in.getPropertyList();
			
			if (propList.size() == 1) {
				ValueExpression propVal = propList.get(0);
				
				if (propVal instanceof EntityField) {
					String propName = ((EntityField) propVal).getPropertyName();
					// JOIN対応
					context.notifyUsedPropertyName(propName);
	
					final PropertyHandler prop = context.getProperty(propName);
					if (prop == null) {
						throw new QueryException("not define property:" + propName);
					}
					final GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) prop.getStoreSpecProperty();
					if (col == null) {
						throw new QueryException("Reference property:" + propName + " itself can not be specified in IN clause. Specify " + propName + ".oid or other.");
					}
					
					//配列対応（※配列はindex不可とする）
					if (prop.getMetaData().getMultiplicity() > 1
							&& ((EntityField) propVal).getArrayIndex() == EntityField.ARRAY_INDEX_UNSPECIFIED) {
						context.append("(");
						List<GRdbPropertyStoreHandler> cols = col.asList();
						for (int i = 0; i < cols.size(); i++) {
							if (i != 0) {
								context.append(" OR ");
							}
							
							context.append("(");
							if (in.getSubQuery() != null) {
								//subquery
								colExp(context.getCurrentSb(), propName, cols.get(i), false);
								context.append(" IN");
								in.getSubQuery().accept(SqlConverter.this);
							} else {
								//list
								boolean enableInPart = rdbAdaptor.isEnableInPartitioning() && in.getValue().size() > rdbAdaptor.getInPartitioningSize();
								if (enableInPart) {
									context.append("(");
								}
								boolean isFirst = true;
								int partLimit = 0;
								int offset = 0;
								while (partLimit < in.getValue().size()) {
									if (isFirst) {
										isFirst = false;
										if (enableInPart) {
											partLimit = rdbAdaptor.getInPartitioningSize();
										} else {
											partLimit = in.getValue().size();
										}
									} else {
										context.append(" OR ");
										offset = partLimit;
										partLimit = partLimit += rdbAdaptor.getInPartitioningSize();
										if (partLimit > in.getValue().size()) {
											partLimit = in.getValue().size();
										}
									}
									colExp(context.getCurrentSb(), propName, cols.get(i), false);
									context.append(" IN");
									context.append("(");
									for (int j = offset; j < partLimit; j++) {
										if (j != offset) {
											context.append(",");
										}
										valueConvert(in.getValue().get(j), col.getSingleColumnRdbTypeAdapter(), false);
									}
									context.append(")");
								}
								if (enableInPart) {
									context.append(")");
								}
							}
							context.append(" AND ");
							colExp(context.getCurrentSb(), propName, cols.get(i), false);
							context.append(" IS NOT NULL)");
						}
						context.append(")");
					} else {
						if (useExternalIndex(prop, propName)) {
							//外部index利用
							externalIndexedSql(propName, (PrimitivePropertyHandler) prop,
									(tableName) -> {
										// EXISTSによる条件文を構築。
										if (in.getSubQuery() != null) {
											//subquery
											context.append(tableName).append(".VAL");
											context.append(" IN");
											//indexを有効化するため
											visit(in.getSubQuery(), true);
										} else {
											//list
											boolean enableInPart = rdbAdaptor.isEnableInPartitioning() && in.getValue().size() > rdbAdaptor.getInPartitioningSize();
											if (enableInPart) {
												context.append("(");
											}
											boolean isFirst = true;
											int partLimit = 0;
											int offset = 0;
											while (partLimit < in.getValue().size()) {
												if (isFirst) {
													isFirst = false;
													if (enableInPart) {
														partLimit = rdbAdaptor.getInPartitioningSize();
													} else {
														partLimit = in.getValue().size();
													}
												} else {
													context.append(" OR ");
													offset = partLimit;
													partLimit = partLimit += rdbAdaptor.getInPartitioningSize();
													if (partLimit > in.getValue().size()) {
														partLimit = in.getValue().size();
													}
												}
												context.append(tableName).append(".VAL");
												context.append(" IN");
												context.append("(");
												for (int i = offset; i < partLimit; i++) {
													if (i != offset) {
														context.append(",");
													}
													valueConvert(in.getValue().get(i), col.getSingleColumnRdbTypeAdapter(), true);
												}
												context.append(")");
											}
											if (enableInPart) {
												context.append(")");
											}
										}
									});
						} else {
							//外部index未利用
							//内部インデックス、単純プロパティ、配列インデックス指定の場合
							GRdbPropertyStoreRuntime targetCol = targetColByIndexIfMulti(col, (EntityField) propVal);
							internalIndexedSql(propName, (PrimitivePropertyHandler) prop, false,
									() -> {
										if (in.getSubQuery() != null) {
											//subquery
											colExp(context.getCurrentSb(), propName, (GRdbPropertyStoreHandler) targetCol, true);
											context.append(" IN");
											if (targetCol.isNative()) {
												//nativeの場合は変換なし
												in.getSubQuery().accept(SqlConverter.this);
											} else {
												visit(in.getSubQuery(), true);
											}
										} else {
											//list
											boolean enableInPart = rdbAdaptor.isEnableInPartitioning() && in.getValue().size() > rdbAdaptor.getInPartitioningSize();
											if (enableInPart) {
												context.append("(");
											}
											boolean isFirst = true;
											int partLimit = 0;
											int offset = 0;
											while (partLimit < in.getValue().size()) {
												if (isFirst) {
													isFirst = false;
													if (enableInPart) {
														partLimit = rdbAdaptor.getInPartitioningSize();
													} else {
														partLimit = in.getValue().size();
													}
												} else {
													context.append(" OR ");
													offset = partLimit;
													partLimit = partLimit += rdbAdaptor.getInPartitioningSize();
													if (partLimit > in.getValue().size()) {
														partLimit = in.getValue().size();
													}
												}
												colExp(context.getCurrentSb(), propName, (GRdbPropertyStoreHandler) targetCol, true);
												context.append(" IN");
												context.append("(");
												for (int i = offset; i < partLimit; i++) {
													if (i != offset) {
														context.append(",");
													}
													//nativeの場合は変換なし
													valueConvert(in.getValue().get(i), targetCol.getSingleColumnRdbTypeAdapter(),
															!targetCol.isNative());
												}
												context.append(")");
											}
											if (enableInPart) {
												context.append(")");
											}
										}
									});
						}
					}
				} else {
					//property以外
					if (in.getSubQuery() != null) {
						//subquery
						propVal.accept(this);
						context.append(" IN");
						in.getSubQuery().accept(SqlConverter.this);
					} else {
						//list
						PropertyType type = context.getValueTypeResolver().resolve(propVal);
						BaseRdbTypeAdapter rdbType = context.getRdb().getRdbTypeAdapter(type);
						boolean enableInPart = rdbAdaptor.isEnableInPartitioning() && in.getValue().size() > rdbAdaptor.getInPartitioningSize();
						if (enableInPart) {
							context.append("(");
						}
						boolean isFirst = true;
						int partLimit = 0;
						int offset = 0;
						while (partLimit < in.getValue().size()) {
							if (isFirst) {
								isFirst = false;
								if (enableInPart) {
									partLimit = rdbAdaptor.getInPartitioningSize();
								} else {
									partLimit = in.getValue().size();
								}
							} else {
								context.append(" OR ");
								offset = partLimit;
								partLimit = partLimit += rdbAdaptor.getInPartitioningSize();
								if (partLimit > in.getValue().size()) {
									partLimit = in.getValue().size();
								}
							}
							propVal.accept(this);
							context.append(" IN");
							context.append("(");
							for (int i = offset; i < partLimit; i++) {
								if (i != offset) {
									context.append(",");
								}
								valueConvert(in.getValue().get(i), rdbType, false);
							}
							context.append(")");
						}
						if (enableInPart) {
							context.append(")");
						}

					}
				}
			} else {
				//inの複数カラムに対する問い合わせ。
				//複数カラムに対するものなので、Indexテーブルは使えない
				if (in.getSubQuery() != null) {
					//subquery
					if (rdbAdaptor.isSupportRowValueConstructor()) {
						context.append("(");
						for (int i = 0; i < propList.size(); i++) {
							if (i != 0) {
								context.append(",");
							}
							ValueExpression propVal = propList.get(i);
							if (propVal instanceof EntityField) {
								String propName = ((EntityField) propVal).getPropertyName();
								PropertyHandler prop = context.getProperty(propName);
								if (prop.getMetaData().getMultiplicity() != 1
										&& ((EntityField) propVal).getArrayIndex() == EntityField.ARRAY_INDEX_UNSPECIFIED) {
									throw new QueryException(
									"multi column IN clause can not use multiple valued property:" + propName);
								}
							}
							propVal.accept(this);
						}
						context.append(") IN");
						in.getSubQuery().accept(SqlConverter.this);
					} else {
						// RDBが行値構成子(行値式)を未サポート
						context.append("EXISTS");
						SubQuery copySubQuery = new SubQuery(in.getSubQuery().getQuery().copy());

						List<Condition> onCondList = new ArrayList<>();
						if (in.getSubQuery().getOn() != null) {
							onCondList.add(in.getSubQuery().getOn());
						}

						List<Condition> havingCondList = new ArrayList<>();
						if (in.getSubQuery().getQuery().getHaving() != null) {
							havingCondList.add(in.getSubQuery().getQuery().getHaving().getCondition());
						}

						for (int i = 0; i < propList.size(); i++) {
							Condition cond = null;
							ValueExpression propVal = propList.get(i);
							ValueExpression selectVal = in.getSubQuery().getQuery().select().getSelectValues().get(i);
							if (propVal instanceof EntityField) {
								String propName = ((EntityField) propVal).getPropertyName();
								PropertyHandler prop = context.getProperty(propName);
								if (prop.getMetaData().getMultiplicity() != 1
										&& ((EntityField) propVal).getArrayIndex() == EntityField.ARRAY_INDEX_UNSPECIFIED) {
									throw new QueryException(
									"multi column IN clause can not use multiple valued property:" + propName);
								}
								cond = new Equals(new EntityField("." + propName, ((EntityField) propVal).getArrayIndex()), selectVal);
							} else {
								cond = new Equals(propVal, selectVal);
							}
							if (selectVal instanceof Aggregate) {
								// 集計関数の場合はHAVING句とする
								havingCondList.add(cond);
							} else {
								onCondList.add(cond);
							}
						}

						Condition onCond = null;
						if (!onCondList.isEmpty()) {
							onCond = onCondList.size() > 1 ? new And(onCondList) : onCondList.get(0);
						}
						copySubQuery.on(onCond);

						Condition havingCond = null;
						if (!havingCondList.isEmpty()) {
							havingCond = havingCondList.size() > 1 ? new And(havingCondList) : havingCondList.get(0);
						}
						copySubQuery.getQuery().setHaving(havingCond != null ? new Having(havingCond) : null);

						visit(copySubQuery, false, true);
					}
				} else {
					//rowValueList
					boolean enableInPart = rdbAdaptor.isEnableInPartitioning() && in.getValue().size() > rdbAdaptor.getInPartitioningSize();
					if (enableInPart) {
						context.append("(");
					}
					boolean isFirst = true;
					int partLimit = 0;
					int offset = 0;
					while (partLimit < in.getValue().size()) {
						if (isFirst) {
							isFirst = false;
							if (enableInPart) {
								partLimit = rdbAdaptor.getInPartitioningSize();
							} else {
								partLimit = in.getValue().size();
							}
						} else {
							context.append(" OR ");
							offset = partLimit;
							partLimit = partLimit += rdbAdaptor.getInPartitioningSize();
							if (partLimit > in.getValue().size()) {
								partLimit = in.getValue().size();
							}
						}
						
						if (rdbAdaptor.isSupportRowValueConstructor()) {
							context.append("(");
							for (int i = 0; i < propList.size(); i++) {
								if (i != 0) {
									context.append(",");
								}
								ValueExpression propVal = propList.get(i);
								if (propVal instanceof EntityField) {
									String propName = ((EntityField) propVal).getPropertyName();
									PropertyHandler prop = context.getProperty(propName);
									if (prop.getMetaData().getMultiplicity() != 1
											&& ((EntityField) propVal).getArrayIndex() == EntityField.ARRAY_INDEX_UNSPECIFIED) {
										throw new QueryException(
										"multi column IN clause can not use multiple valued property:" + propName);
									}
								}
								propVal.accept(this);
							}
							context.append(") IN(");
							for (int i = offset; i < partLimit; i++) {
								if (i != offset) {
									context.append(",");
								}
								in.getValue().get(i).accept(this);
							}
							context.append(")");
						} else {
							// RDBが行値構成子(行値式)を未サポート
							context.append("(");
							for (int i = offset; i < partLimit; i++) {
								if (i != 0) {
									context.append(" OR ");
								}
								ValueExpression rvl = in.getValue().get(i);
								context.append("(");
								for (int j = 0; j < propList.size(); j++) {
									if (j != 0) {
										context.append(" AND ");
									}
									ValueExpression propVal = propList.get(j);
									if (propVal instanceof EntityField) {
										String propName = ((EntityField) propVal).getPropertyName();
										PropertyHandler prop = context.getProperty(propName);
										if (prop.getMetaData().getMultiplicity() != 1
												&& ((EntityField) propVal).getArrayIndex() == EntityField.ARRAY_INDEX_UNSPECIFIED) {
											throw new QueryException(
											"multi column IN clause can not use multiple valued property:" + propName);
										}
									}
									new Equals(propVal, ((RowValueList) rvl).getRowValues().get(j)).accept(this);
								}
								context.append(")");
							}
							context.append(")");
						}
					}
					if (enableInPart) {
						context.append(")");
					}
				}
				
			}
			return false;
		} finally {
			pop(in);
		}
	}

	private void forLike(Literal pattern, CaseType cs) {
		String patternStr;
		if (cs == CaseType.CS) {
			patternStr = pattern.getValue().toString();
		} else {
			patternStr = pattern.getValue().toString().toUpperCase();
		}
		patternStr = rdbAdaptor.likePattern(patternStr);

		BaseRdbTypeAdapter type = rdbAdaptor.getRdbTypeAdapter(patternStr);
		if (context.isEnableBindVariable() && pattern.isBindable()) {
			type.appendParameterPlaceholder(context.getCurrentSb(), rdbAdaptor);
			context.addBindVariable(patternStr, type);
		} else {
			type.appendToSqlAsRealType(patternStr, context.getCurrentSb(), rdbAdaptor);
		}
	}

	@Override
	public boolean visit(final Like like) {
		push(like);
		try {
			
			ValueExpression propVal = like.getProperty();
			
			if (propVal instanceof EntityField) {
				String propName = like.getPropertyName();
				// JOIN対応
				context.notifyUsedPropertyName(propName);
	
				final PropertyHandler prop = context.getProperty(propName);
				if (prop == null) {
					throw new QueryException("not define property:" + propName);
				}
				final GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) prop.getStoreSpecProperty();
				if (col == null) {
					throw new QueryException("Reference property:" + propName + " itself can not be specified in Like clause. Specify " + propName + ".oid or other.");
				}
				
				//配列対応（※配列はindex不可とする）
				if (prop.getMetaData().getMultiplicity() > 1
						&& ((EntityField) propVal).getArrayIndex() == EntityField.ARRAY_INDEX_UNSPECIFIED) {
					context.append("(");
					//ANYとみなす（プロパティの配列要素の内どれかひとつでもtrueの場合、trueとみなす）
					List<GRdbPropertyStoreHandler> cols = col.asList();
					for (int i = 0; i < cols.size(); i++) {
						if (i != 0) {
							context.append(" OR ");
						}
						context.append("(");
						if (like.getCaseType() == CaseType.CS) {
							colExp(context.getCurrentSb(), propName, cols.get(i), false);
							context.append(" LIKE ");
							forLike(like.getPatternAsLiteral(), like.getCaseType());
							context.append(" ").append(rdbAdaptor.escape());
						} else {
							//#900 大文字/小文字区別をなくす
							context.append(rdbAdaptor.upperFunctionName());
							context.append("(");
							colExp(context.getCurrentSb(), propName, cols.get(i), false);
							context.append(")");
							context.append(" LIKE ");
							forLike(like.getPatternAsLiteral(), like.getCaseType());
							context.append(" ").append(rdbAdaptor.escape());
						}
						context.append(" AND ");
						colExp(context.getCurrentSb(), propName, cols.get(i), false);
						context.append(" IS NOT NULL)");
					}
					context.append(")");
				} else {
					if (like.getCaseType() == CaseType.CS
							&& !like.getPattern().startsWith(Like.PS)
							&& !like.getPattern().startsWith(Like.US)) {
						//インデックス利用可能性あり
						if (useExternalIndex(prop, propName)) {
							externalIndexedSql(propName, (PrimitivePropertyHandler) prop,
									(tableName) -> {
										// EXISTSによる条件文を構築。
										context.append(tableName).append(".VAL");
										context.append(" LIKE ");
										forLike(like.getPatternAsLiteral(), like.getCaseType());
										context.append(" ").append(rdbAdaptor.escape());
									});
						} else {
							//内部インデックス、単純プロパティ、配列インデックス指定の場合
							GRdbPropertyStoreRuntime targetCol = targetColByIndexIfMulti(col, (EntityField) propVal);
							internalIndexedSql(propName, (PrimitivePropertyHandler) prop, false,
									() -> {
										colExp(context.getCurrentSb(), propName, (GRdbPropertyStoreHandler) targetCol, true);
										context.append(" LIKE ");
										forLike(like.getPatternAsLiteral(), like.getCaseType());
										context.append(" ").append(rdbAdaptor.escape());
									});
						}
					} else {
						//単純プロパティ、配列インデックス指定の場合
						GRdbPropertyStoreRuntime targetCol = targetColByIndexIfMulti(col, (EntityField) propVal);
						if (like.getCaseType() == CaseType.CS) {
							colExp(context.getCurrentSb(), propName, (GRdbPropertyStoreHandler) targetCol, false);
							context.append(" LIKE ");
							forLike(like.getPatternAsLiteral(), like.getCaseType());
							context.append(" ").append(rdbAdaptor.escape());
						} else {
							//#900 大文字/小文字区別をなくす
							context.append(rdbAdaptor.upperFunctionName());
							context.append("(");
							colExp(context.getCurrentSb(), propName, (GRdbPropertyStoreHandler) targetCol, false);
							context.append(")");
							context.append(" LIKE ");
							forLike(like.getPatternAsLiteral(), like.getCaseType());
							context.append(" ").append(rdbAdaptor.escape());
						}
					}
				}
			} else {
				if (like.getCaseType() == CaseType.CS) {
					propVal.accept(this);
					context.append(" LIKE ");
					forLike(like.getPatternAsLiteral(), like.getCaseType());
					context.append(" " + rdbAdaptor.escape());
				} else {
					//#900 大文字/小文字区別をなくす
					context.append(rdbAdaptor.upperFunctionName());
					context.append("(");
					propVal.accept(this);
					context.append(")");
					context.append(" LIKE ");
					forLike(like.getPatternAsLiteral(), like.getCaseType());
					context.append(" " + rdbAdaptor.escape());
				}
			}
	
			return false;
		} finally {
			pop(like);
		}
	}

	@Override
	public boolean visit(Not not) {
		push(not);
		try {
			if (not.getNestedExpression() != null) {
				context.append("NOT ");
				not.getNestedExpression().accept(this);
				return false;
			} else {
				throw new QueryException(
						"Cannt convert NotExpression because nestedExpression  is null.");
			}
		} finally {
			pop(not);
		}
	}

	@Override
	public boolean visit(IsNotNull isNotNull) {
		push(isNotNull);
		try {
			nullOp(isNotNull.getProperty(), false, true);
			return false;
		} finally {
			pop(isNotNull);
		}
	}

	@Override
	public boolean visit(IsNull isNull) {
		push(isNull);
		try {
			nullOp(isNull.getProperty(), true, false);
			return false;
		} finally {
			pop(isNull);
		}
	}

	private void nullOp(ValueExpression propVal, boolean isNullOp, boolean isAny) {
		String op = isNullOp ? "IS NULL": "IS NOT NULL";
		
		if (propVal instanceof EntityField) {
			String propName = ((EntityField) propVal).getPropertyName();
			
			// JOIN対応
			context.notifyUsedPropertyName(propName);

			PropertyHandler prop = context.getProperty(propName);
			if (prop == null) {
				throw new NullPointerException(propName + " is not defined..");
			}
			GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) prop.getStoreSpecProperty();
			if (col == null) {
				throw new QueryException("Reference property:" + propName + " itself can not be specified. Specify " + propName + ".oid or other.");
			}
			
			//配列対応（※配列はindex不可とする）
			if (prop.getMetaData().getMultiplicity() > 1
					&& ((EntityField) propVal).getArrayIndex() == EntityField.ARRAY_INDEX_UNSPECIFIED) {
				context.append("(");
				List<GRdbPropertyStoreHandler> cols = col.asList();
				for (int i = 0; i < cols.size(); i++) {
					if (i != 0) {
						if (isAny) {
							context.append(" OR ");
						} else {
							context.append(" AND ");
						}
					}
					colExp(context.getCurrentSb(), propName, cols.get(i), false);
					context.append(" ").append(op);
				}
				context.append(")");
			} else {
				//外部index使わない。内部Index or 通常カラムのみ
				//内部インデックス、単純プロパティ、配列インデックス指定の場合
				GRdbPropertyStoreRuntime targetCol = targetColByIndexIfMulti(col, (EntityField) propVal);
				internalIndexedSql(propName, (PrimitivePropertyHandler) prop, isNullOp,
						() -> {
							colExp(context.getCurrentSb(), propName, (GRdbPropertyStoreHandler) targetCol, true);
							context.append(" ").append(op);
						});
			}
		} else {
			propVal.accept(this);
			context.append(" ").append(op);
		}
	}
	
	

	@Override
	public boolean visit(ScalarSubQuery scalarSubQuery) {
		push(scalarSubQuery);
		try {
			if (scalarSubQuery.getQuery() == null ||
					scalarSubQuery.getQuery().getSelect() == null ||
					scalarSubQuery.getQuery().getSelect().getSelectValues() == null ||
							scalarSubQuery.getQuery().getSelect().getSelectValues().size() != 1) {
				throw new QueryException("ScalarSubQuery must specify only one column.");
			}
			return true;
		} finally {
			pop(scalarSubQuery);
		}
	}
	
	@Override
	public boolean visit(SubQuery subQuery) {
		return visit(subQuery, false);
	}
	
	private boolean visit(SubQuery subQuery, boolean treatSelectAsRawColType) {
		return visit(subQuery, treatSelectAsRawColType, false);
	}

	private boolean visit(SubQuery subQuery, boolean treatSelectAsRawColType, boolean groupingCorrelation) {
		push(subQuery);
		try {
			
			EntityContext ec = context.getMetaContext();
			SqlQueryContext subContext = new SqlQueryContext(
					ec.getHandlerByName(subQuery.getQuery().getFrom().getEntityName()),
					context, treatSelectAsRawColType);
			
			context = subContext;
			
			if (subQuery.getQuery().getSelect() != null) {
				subQuery.getQuery().getSelect().accept(this);
			}
			if (subQuery.getQuery().getFrom() != null) {
				subQuery.getQuery().getFrom().accept(this);
			}
			if (subQuery.getQuery().getRefer() != null) {
				for (Refer r: subQuery.getQuery().getRefer()) {
					r.accept(this);
				}
			}
			
			//special logic for correlation
			if (subQuery.getOn() != null) {
				
				subContext.changeCurrentClause(Clause.WHERE);
				
				if (subQuery.getQuery().getWhere() != null) {
					subContext.append("(");
					subQuery.getQuery().getWhere().getCondition().accept(this);
					subContext.append(") AND ");
				}
				
				subContext.setEnableCorrelation(true);
				subContext.setUseIndexTable(false);
				
				if (subQuery.getOn() instanceof Or) {
					subContext.append("(");
				}
				
				subQuery.getOn().accept(this);
				
				if (subQuery.getOn() instanceof Or) {
					subContext.append(")");
				}
				
				subContext.setEnableCorrelation(false);
				subContext.setUseIndexTable(true);
				
			} else {
				if (subQuery.getQuery().getWhere() != null) {
					subQuery.getQuery().getWhere().accept(this);
				}
			}
			
			if (groupingCorrelation) {
				subContext.setEnableCorrelation(true);
				subContext.setUseIndexTable(false);
			}
			if (subQuery.getQuery().getGroupBy() != null) {
				subQuery.getQuery().getGroupBy().accept(this);
			}
			if (subQuery.getQuery().getHaving() != null) {
				subQuery.getQuery().getHaving().accept(this);
			}
			if (groupingCorrelation) {
				subContext.setEnableCorrelation(false);
				subContext.setUseIndexTable(true);
			}
			if (subQuery.getQuery().getOrderBy() != null) {
				subQuery.getQuery().getOrderBy().accept(this);
			}
			if (subQuery.getQuery().getLimit() != null) {
				subQuery.getQuery().getLimit().accept(this);
			}
			
			context = context.getParentContext();
			
			//メインContextへappend
			context.append("(");
			context.append(subContext.toSelectSql());
			context.append(")");
			
			List<BindValue> subqueryBindVarialbes = subContext.toOrderedBindVariables(true);
			if (subqueryBindVarialbes != null && subqueryBindVarialbes.size() > 0) {
				context.setEnableBindVariable(true);
				for (BindValue b: subqueryBindVarialbes) {
					((QueryBindValue) b).inIndexTable = false;
					((QueryBindValue) b).clause = context.getCurrentClause();
					context.getBindVariables().add(b);
				}
			}
			
			return false;
		} finally {
			pop(subQuery);
		}
	}

	@Override
	public boolean visit(SortSpec order) {
		push(order);
		try {
			int start = context.getCurrentSb().length();
			order.getSortKey().accept(this);
			CharSequence sortValue = context.getCurrentSb().subSequence(start, context.getCurrentSb().length());
			context.getCurrentSb().delete(start, context.getCurrentSb().length());
			context.getRdb().appendSortSpecExpression(context.getCurrentSb(), sortValue, order.getType(), order.getNullOrderingSpec());
			return false;
		} finally {
			pop(order);
		}
	}

	@Override
	public boolean visit(OrderBy orderBy) {
		push(orderBy);
		try {
			
			//mysqlが group by rollup と order byを同時利用できないので
			if (!context.isUseRollup()
					|| rdbAdaptor.isSupportGroupingExtentionWithOrderBy()) {
				context.changeCurrentClause(Clause.ORDERBYGROUPBY);
				context.append(" ORDER BY ");
				List<SortSpec> orderSpec = orderBy.getSortSpecList();
				for (int i = 0; i < orderSpec.size(); i++) {
					if (i != 0) {
						context.append(",");
					}
					orderSpec.get(i).accept(this);
				}
			}
			
			return false;
		} finally {
			pop(orderBy);
		}
	}

	@Override
	public boolean visit(GroupBy groupBy) {
		push(groupBy);
		try {
			
			context.changeCurrentClause(Clause.ORDERBYGROUPBY);
			context.append(" GROUP BY ");
			
			if (groupBy.getRollType() != null && rdbAdaptor.isSupportGroupingExtention()) {
				context.setUseRollup(true);
				context.append(rdbAdaptor.rollUpStart(groupBy.getRollType()));
			}
			
			List<ValueExpression> valueList = groupBy.getGroupingFieldList();
			for (int i = 0; i < valueList.size(); i++) {
				if (i != 0) {
					context.append(",");
				}
				valueList.get(i).accept(this);
			}
			
			if (groupBy.getRollType() != null && rdbAdaptor.isSupportGroupingExtention()) {
				context.append(rdbAdaptor.rollUpEnd(groupBy.getRollType()));
			}
	
			return false;
		} finally {
			pop(groupBy);
		}
	}

	@Override
	public boolean visit(Limit limit) {
		push(limit);
		try {
			context.setLimitCount(limit.getLimit());
			context.setLimitOffset(limit.getOffset());
			context.setLimitBind(limit.isBindable());
			return false;
		} finally {
			pop(limit);
		}
	}

	@Override
	public boolean visit(Cast cast) {
		push(cast);
		try {
			String[] castExp = null;
			
			switch (cast.getType()) {
			case BOOLEAN:
				castExp = rdbAdaptor.castExp(Types.VARCHAR, 32, null);
				break;
			case DATE:
				castExp = rdbAdaptor.castExp(Types.DATE, null, null);
				break;
			case DATETIME:
				castExp = rdbAdaptor.castExp(Types.TIMESTAMP, null, null);
				break;
			case DECIMAL:
				castExp = rdbAdaptor.castExp(Types.DECIMAL, null, cast.getTypeArgument(0));
				break;
			case FLOAT:
				castExp = rdbAdaptor.castExp(Types.DOUBLE, null, null);
				break;
			case INTEGER:
				castExp = rdbAdaptor.castExp(Types.BIGINT, null, null);
				break;
			case STRING:
				castExp = rdbAdaptor.castExp(Types.VARCHAR, cast.getTypeArgument(0), null);
				break;
			case TIME:
				castExp = rdbAdaptor.castExp(Types.TIME, null, null);
				break;

			default:
				throw new QueryException(
						"not support type of cast expression:" + cast.getType());
			}
			context.append(castExp[0]);
			cast.getValue().accept(this);
			context.append(castExp[1]);
			
		} finally {
			pop(cast);
		}
		return false;
	}
	
	@Override
	public boolean visit(Function function) {
		push(function);
		try {
			
			FunctionAdapter<Function> fa = rdbAdaptor.resolveFunction(function.getName());
			if (fa == null) {
				throw new QueryException(function.getName() + " not supported.");
			}
			
			fa.toSQL(new FunctionContext() {
				@Override
				public void appendArgument(ValueExpression arg) {
					arg.accept(SqlConverter.this);
				}
				
				@Override
				public void append(String str) {
					context.append(str);
				}
			}, function, rdbAdaptor);
			
			return false;
		} finally {
			pop(function);
		}
	}

	@Override
	public boolean visit(Refer refer) {
		push(refer);
		try {
			if (refer.getCondition() != null) {//without permission conditionのみ指定の場合があるので、、、
				EntityField refPropName = refer.getReferenceName();
				String oidName = refPropName.getPropertyName() + "." + Entity.OID;
				String verName = refPropName.getPropertyName() + "." + Entity.VERSION;
				context.notifyUsedPropertyName(oidName);
				
				Condition refCond = refer.getCondition();
				
				MultiPageChecker mpc = new MultiPageChecker(context.getFromEntity(), context.getMetaContext(), refPropName.getPropertyName());
				refCond.accept(mpc);
				if (mpc.isMultiPage()) {
					//複数ページ跨ぐ場合、oidのサブクエリに変換
					ReferencePropertyHandler refPh = (ReferencePropertyHandler) context.getFromEntity().getPropertyCascade(refPropName.getPropertyName(), context.getMetaContext());
					EntityHandler refEh = refPh.getReferenceEntityHandler(context.getMetaContext());
					
					Query subq = new Query();
					if (mpc.isAllPropertyUnderRef()) {
						//サブクエリの深さを刈り取る
						if (context.getFromEntity().isVersioned()) {
							subq.select(new EntityField(Entity.OID), new EntityField(Entity.VERSION));
						} else {
							subq.select(new EntityField(Entity.OID));
						}
						subq.from(refEh.getMetaData().getName());
						
						RefTrimmer refTrimmer = new RefTrimmer(refPropName.getPropertyName());
						subq.where((Condition) refer.getCondition().accept(refTrimmer));
						
					} else {
						if (context.getFromEntity().isVersioned()) {
							subq.select(new EntityField(oidName), new EntityField(verName));
						} else {
							subq.select(new EntityField(oidName));
						}
						subq.from(context.getFromEntity().getMetaData().getName());
						subq.where(refer.getCondition());
					}

					if (context.getFromEntity().isVersioned()) {
						ArrayList<ValueExpression> p = new ArrayList<>();
						p.add(new EntityField(oidName));
						p.add(new EntityField(verName));
						refCond = new In(p, subq);
					} else {
						refCond = new And(new In(new EntityField(oidName), subq), new Equals(new EntityField(verName), new Literal(0L)));
					}
				}
				
				//JoinPath,Aliasesは共有
				SqlQueryContext referContext = new SqlQueryContext(
						context.getFromEntity(),
						context.getMetaContext(),
						rdbAdaptor,
						context.getPrefix(),
						context.getAliases(),
						context.getJoinPath(),
						context.getIndexTable(),
						context.isEnableBindVariable());
				
				referContext.setUseIndexTable(false);
				
				SqlQueryContext mainContext = context;
				
				context = referContext;
				context.changeCurrentClause(Clause.WHERE);
				refCond.accept(this);
				if (referContext.getCurrentClause() != Clause.WHERE) {
					referContext.changeCurrentClause(Clause.WHERE);
				}
				
				List<BindValue> referBindVarialbes = referContext.toOrderedBindVariables(false);
				if (referBindVarialbes != null && referBindVarialbes.size() > 0) {
					mainContext.setEnableBindVariable(true);
					for (BindValue b: referBindVarialbes) {
						((QueryBindValue) b).inIndexTable = false;
						((QueryBindValue) b).clause = Clause.REFER;
					}
				}
	
				mainContext.getJoinPath().getJoinPath(refPropName.getPropertyName()).setAdditionalCondition(referContext.getCurrentSb().toString(), referBindVarialbes);
				if (mainContext.getIndexTable() == null) {
					mainContext.setIndexTable(referContext.getIndexTable());
				}
				context = mainContext;
			}
			return false;
		} finally {
			pop(refer);
		}
	}

	@Override
	public boolean visit(Having having) {
		push(having);
		try {
			context.changeCurrentClause(Clause.ORDERBYGROUPBY);
			context.append(" HAVING ");
			return true;
		} finally {
			pop(having);
		}
	}

	@Override
	public boolean visit(Case caseClause) {
		push(caseClause);
		try {
			context.append("CASE");
			for (When w: caseClause.getWhen()) {
				context.append(" ");
				w.accept(this);
			}
			if (caseClause.getElseClause() != null) {
				context.append(" ");
				caseClause.getElseClause().accept(this);
			}
			
			context.append(" END");
			return false;
		} finally {
			pop(caseClause);
		}
	}

	@Override
	public boolean visit(Else elseClause) {
		push(elseClause);
		try {
			context.append("ELSE ");
			elseClause.getResult().accept(this);
			return false;
		} finally {
			pop(elseClause);
		}
	}

	@Override
	public boolean visit(When when) {
		push(when);
		try {
			context.append("WHEN ");
			boolean stackedUseIndex = context.isUseIndexTable();
			context.setUseIndexTable(false);
			when.getCondition().accept(this);
			context.setUseIndexTable(stackedUseIndex);
			context.append(" THEN ");
			when.getResult().accept(this);
			return false;
		} finally {
			pop(when);
		}
	}

	@Override
	public boolean visit(HintComment hintComment) {
		push(hintComment);
		try {
			if (hintComment.getHintList() != null) {
				for (Hint hc: hintComment.getHintList()) {
					hc.accept(this);
				}
			}
			return false;
		} finally {
			pop(hintComment);
		}
	}

	@Override
	public boolean visit(IndexHint indexHint) {
		push(indexHint);
		try {
			if (indexHint.getPropertyNameList() != null) {
				for (String prop: indexHint.getPropertyNameList()) {
					context.addIndexHint(prop);
				}
			}
			return false;
		} finally {
			pop(indexHint);
		}
	}

	@Override
	public boolean visit(NoIndexHint noIndexHint) {
		push(noIndexHint);
		try {
			if (noIndexHint.getPropertyNameList() != null) {
				for (String prop: noIndexHint.getPropertyNameList()) {
					context.addNoIndexHint(prop);
				}
			}
			return false;
		} finally {
			pop(noIndexHint);
		}
	}

	@Override
	public boolean visit(NativeHint nativeHint) {
		push(nativeHint);
		try {
			if (nativeHint.getTable() == null) {
				context.changeCurrentClause(Clause.HINT);
				context.append(nativeHint.getHintExpression());
			} else {
				context.addTableHint(nativeHint.getTable(), nativeHint.getHintExpression());
			}
			return false;
		} finally {
			pop(nativeHint);
		}
	}

	@Override
	public boolean visit(BindHint bindHint) {
		push(bindHint);
		try {
			if (treatBindHint) {
				context.setEnableBindVariable(true);
			}
			return false;
		} finally {
			pop(bindHint);
		}
	}

	@Override
	public boolean visit(NoBindHint noBindHint) {
		push(noBindHint);
		try {
			context.setEnableBindVariable(false);
			return false;
		} finally {
			pop(noBindHint);
		}
	}

	@Override
	public boolean visit(WindowAggregate windowAggregate) {
		push(windowAggregate);
		try {
			if (rdbAdaptor.isSupportWindowFunction()) {
				if (windowAggregate.getAggregate() != null) {
					windowAggregate.getAggregate().accept(this);
				}
				
				context.append(" OVER (");
				if (windowAggregate.getPartitionBy() != null) {
					windowAggregate.getPartitionBy().accept(this);
				}
				if (windowAggregate.getOrderBy() != null) {
					if (windowAggregate.getPartitionBy() != null) {
						context.append(" ");
					}
					windowAggregate.getOrderBy().accept(this);
				}
				context.append(")");
			} else {
				throw new QueryException("window function not supported.");
			}
			return false;
		} finally {
			pop(windowAggregate);
		}
	}
	
	
	private void windowRankFunction(WindowRankFunction wrf) {
		if (rdbAdaptor.isSupportWindowFunction()) {
			String funcName = rdbAdaptor.windowRankFunctionName(wrf);
			context.append(funcName);
			context.append("() OVER(");
			if (wrf.getPartitionBy() != null) {
				wrf.getPartitionBy().accept(this);
			}
			if (wrf.getOrderBy() != null) {
				if (wrf.getPartitionBy() != null) {
					context.append(" ");
				}
				wrf.getOrderBy().accept(this);
			}
			context.append(")");
		} else {
			throw new QueryException("window function not supported.");
		}
	}

	@Override
	public boolean visit(RowNumber rowNumber) {
		push(rowNumber);
		try {
			windowRankFunction(rowNumber);
			return false;
		} finally {
			pop(rowNumber);
		}
	}

	@Override
	public boolean visit(Rank rank) {
		push(rank);
		try {
			windowRankFunction(rank);
			return false;
		} finally {
			pop(rank);
		}
	}

	@Override
	public boolean visit(DenseRank denseRank) {
		push(denseRank);
		try {
			windowRankFunction(denseRank);
			return false;
		} finally {
			pop(denseRank);
		}
	}

	@Override
	public boolean visit(PercentRank percentRank) {
		push(percentRank);
		try {
			windowRankFunction(percentRank);
			return false;
		} finally {
			pop(percentRank);
		}
	}

	@Override
	public boolean visit(CumeDist cumeDist) {
		push(cumeDist);
		try {
			windowRankFunction(cumeDist);
			return false;
		} finally {
			pop(cumeDist);
		}
	}

	@Override
	public boolean visit(PartitionBy partitionBy) {
		push(partitionBy);
		try {
			context.append("PARTITION BY ");
			List<ValueExpression> valueList = partitionBy.getPartitionFieldList();
			for (int i = 0; i < valueList.size(); i++) {
				if (i != 0) {
					context.append(",");
				}
				valueList.get(i).accept(this);
			}
			
			return false;
		} finally {
			pop(partitionBy);
		}
	}

	@Override
	public boolean visit(WindowOrderBy orderBy) {
		push(orderBy);
		try {
			context.append("ORDER BY ");
			List<WindowSortSpec> orderSpec = orderBy.getSortSpecList();
			for (int i = 0; i < orderSpec.size(); i++) {
				if (i != 0) {
					context.append(",");
				}
				orderSpec.get(i).accept(this);
			}
			
			return false;
		} finally {
			pop(orderBy);
		}
	}

	@Override
	public boolean visit(WindowSortSpec sortSpec) {
		push(sortSpec);
		try {
			int start = context.getCurrentSb().length();
			sortSpec.getSortKey().accept(this);
			CharSequence sortValue = context.getCurrentSb().subSequence(start, context.getCurrentSb().length());
			context.getCurrentSb().delete(start, context.getCurrentSb().length());
			context.getRdb().appendSortSpecExpression(context.getCurrentSb(), sortValue, sortSpec.getType(), sortSpec.getNullOrderingSpec());
			return false;
		} finally {
			pop(sortSpec);
		}
	}

}
