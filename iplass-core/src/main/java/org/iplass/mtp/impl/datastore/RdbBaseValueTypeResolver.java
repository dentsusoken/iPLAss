/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.query.value.RowValueList;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
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
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyService;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.properties.basic.DecimalType;
import org.iplass.mtp.impl.properties.basic.FloatType;
import org.iplass.mtp.impl.properties.basic.IntegerType;
import org.iplass.mtp.impl.properties.basic.StringType;
import org.iplass.mtp.impl.properties.extend.ExpressionType;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter.ArgumentTypeResolver;
import org.iplass.mtp.spi.ServiceRegistry;


public class RdbBaseValueTypeResolver implements ValueExpressionVisitor,ArgumentTypeResolver {

	private EntityHandler fromEntity;
	private EntityContext context;
	private PropertyService propService;
	private RdbAdapter rdb;

	private PropertyType type;

	public RdbBaseValueTypeResolver(EntityHandler fromEntity, EntityContext context, RdbAdapter rdb) {
		this.fromEntity = fromEntity;
		this.context = context;
		this.rdb = rdb;
		propService = ServiceRegistry.getRegistry().getService(PropertyService.class);
	}

	public PropertyType resolve(ValueExpression val) {
		type = null;
		val.accept(this);
		return type;
	}

	@Override
	public Class<?> resolveType(ValueExpression value) {
		type = null;
		value.accept(this);
		if (type == null) {
			return null;
		} else {
			return type.storeType();
		}
	}

	public boolean visit(Literal literal) {

		if (literal.getValue() == null) {
			type = null;
		} else {
			type = propService.getPropertyType(literal.getValue().getClass());
		}

		return false;
	}

	public boolean visit(EntityField entityField) {

		PropertyHandler ph = fromEntity.getPropertyCascade(entityField.getPropertyName(), context);
		if (ph instanceof PrimitivePropertyHandler) {
			type = ((MetaPrimitiveProperty) ph.getMetaData()).getType();
			if (type instanceof ExpressionType) {
				PropertyDefinitionType et = ((ExpressionType) type).getResultType();
				if (et == null) {
					type = propService.getPropertyType(String.class);
				} else {
					type = propService.getPropertyType(et);
				}
			}
		} else {
			type = null;
		}
		return false;
	}

	public boolean visit(Polynomial polynomial) {
		ArrayList<PropertyType> subTypeList = new ArrayList<PropertyType>();
		getSubTypeList(subTypeList, polynomial.getAddValues());
		getSubTypeList(subTypeList, polynomial.getSubValues());
		type = whichType(subTypeList);
		return false;
	}

	private void getSubTypeList(List<PropertyType> list, List<ValueExpression> subList) {
		if (subList != null) {
			for (ValueExpression v: subList) {
				v.accept(this);
				list.add(type);
			}
		}
	}

	//TODO 型の優先順位はPropertyType（RdbTypeMappingか）自身に持つべきか
	private PropertyType whichType(List<PropertyType> type) {

		//優先順位
		//Float > Decimal > Integer > String(必要?)

		if (type == null || type.size() == 0) {
			return null;
		}

		PropertyType res = null;
		for (int i = 0; i < type.size(); i++) {
			PropertyType c = type.get(i);
			if (c instanceof FloatType) {
				return c;
			}

			if (c instanceof DecimalType) {
				res = c;
			} else if (c instanceof IntegerType) {
				if (!(res instanceof DecimalType)) {
					res = c;
				}
			} else if (c instanceof StringType) {
				if (!(res instanceof DecimalType
						|| res instanceof IntegerType)) {
					res = c;
				}
			} else {
				if (!(res instanceof DecimalType
						|| res instanceof IntegerType
						|| res instanceof StringType)) {
					res = c;
				}
			}
		}
		return res;
	}

	public boolean visit(Term term) {
		ArrayList<PropertyType> subTypeList = new ArrayList<PropertyType>();
		getSubTypeList(subTypeList, term.getMulValues());
		getSubTypeList(subTypeList, term.getDivValues());
		type = whichType(subTypeList);
		return false;
	}

	public boolean visit(ParenValue bracketValue) {
		bracketValue.getNestedValue().accept(this);
		return false;
	}

	public boolean visit(MinusSign minusSign) {
		minusSign.getValue().accept(this);
		return false;
	}

	public boolean visit(ScalarSubQuery scalarSubQuery) {
		EntityHandler subEntity = context.getHandlerByName(scalarSubQuery.getQuery().getFrom().getEntityName());
		RdbBaseValueTypeResolver subResolver = new RdbBaseValueTypeResolver(subEntity, context, rdb);
		type = subResolver.resolve(scalarSubQuery.getQuery().getSelect().getSelectValues().get(0));
		return false;
	}

	public boolean visit(Count count) {
		type = propService.getPropertyType(Long.class);
		return false;
	}

	public boolean visit(Sum sum) {
		sum.getValue().accept(this);
		return false;
	}

	public boolean visit(Avg avg) {
		type = propService.getPropertyType(Double.class);
//		avg.getValue().accept(this);
		return false;
	}

	public boolean visit(Max max) {
		max.getValue().accept(this);
		return false;
	}

	public boolean visit(Min min) {
		min.getValue().accept(this);
		return false;
	}

	@Override
	public boolean visit(StdDevPop stdDevPop) {
		type = propService.getPropertyType(Double.class);
		return false;
	}

	@Override
	public boolean visit(StdDevSamp stdDevSamp) {
		type = propService.getPropertyType(Double.class);
		return false;
	}

	@Override
	public boolean visit(VarPop varPop) {
		type = propService.getPropertyType(Double.class);
		return false;
	}

	@Override
	public boolean visit(VarSamp varSamp) {
		type = propService.getPropertyType(Double.class);
		return false;
	}

	@Override
	public boolean visit(Mode mode) {
		mode.getValue().accept(this);
		return false;
	}

	@Override
	public boolean visit(Median median) {
		median.getValue().accept(this);
		return false;
	}

	@Override
	public boolean visit(ArrayValue arrayValue) {
		ArrayList<PropertyType> subTypeList = new ArrayList<PropertyType>();
		getSubTypeList(subTypeList, arrayValue.getValues());
		type = whichType(subTypeList);
		return false;
	}

	@Override
	public boolean visit(Function function) {
		FunctionAdapter fa = rdb.resolveFunction(function.getName());
		if (fa != null) {
			Class<?> funcType = fa.getType(function, this);
			type = propService.getPropertyType(funcType);
		}
		return false;
	}

	@Override
	public boolean visit(Case caseClause) {

		for (When w: caseClause.getWhen()) {
			w.accept(this);
			if (type != null) {
				break;
			}
		}
		if (type == null && caseClause.getElseClause() != null) {
			caseClause.getElseClause().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(Else elseClause) {
		elseClause.getResult().accept(this);
		return false;
	}

	@Override
	public boolean visit(When when) {
		when.getResult().accept(this);
		return false;
	}

	@Override
	public boolean visit(Cast cast) {
		if (cast.getType() == PropertyDefinitionType.DECIMAL
				&& cast.getTypeArgument(0) != null) {
			type = new DecimalType(cast.getTypeArgument(0), RoundingMode.HALF_EVEN);
		} else {
			type = propService.getPropertyType(cast.getType());
		}
		return false;
	}

	@Override
	public boolean visit(WindowAggregate windowAggregate) {
		windowAggregate.getAggregate().accept(this);
		return false;
	}

	@Override
	public boolean visit(RowNumber rowNumber) {
		type = propService.getPropertyType(Long.class);
		return false;
	}

	@Override
	public boolean visit(Rank rank) {
		type = propService.getPropertyType(Long.class);
		return false;
	}

	@Override
	public boolean visit(DenseRank denseRank) {
		type = propService.getPropertyType(Long.class);
		return false;
	}

	@Override
	public boolean visit(PercentRank percentRank) {
		type = propService.getPropertyType(Double.class);
		return false;
	}

	@Override
	public boolean visit(CumeDist cumeDist) {
		type = propService.getPropertyType(Double.class);
		return false;
	}

	@Override
	public boolean visit(PartitionBy partitionBy) {
		return false;
	}

	@Override
	public boolean visit(WindowOrderBy orderBy) {
		return false;
	}

	@Override
	public boolean visit(WindowSortSpec sortSpec) {
		return false;
	}

	@Override
	public boolean visit(RowValueList rowValueList) {
		return false;
	}

}
