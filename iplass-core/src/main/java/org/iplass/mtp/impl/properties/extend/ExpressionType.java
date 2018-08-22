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

package org.iplass.mtp.impl.properties.extend;

import java.math.BigDecimal;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyService;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.entity.versioning.VersionedQueryNormalizer;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.spi.ServiceRegistry;

public class ExpressionType extends VirtualType {
	private static final long serialVersionUID = -7890554839217349016L;
	
	private String expression;
	private PropertyDefinitionType resultType;
	private PropertyType resultTypeSpec;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expression == null) ? 0 : expression.hashCode());
		result = prime * result
				+ ((resultType == null) ? 0 : resultType.hashCode());
		result = prime * result
				+ ((resultTypeSpec == null) ? 0 : resultTypeSpec.hashCode());
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
		ExpressionType other = (ExpressionType) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (resultType != other.resultType)
			return false;
		if (resultTypeSpec == null) {
			if (other.resultTypeSpec != null)
				return false;
		} else if (!resultTypeSpec.equals(other.resultTypeSpec))
			return false;
		return true;
	}

	public PropertyType getResultTypeSpec() {
		return resultTypeSpec;
	}

	public void setResultTypeSpec(PropertyType resultTypeSpec) {
		this.resultTypeSpec = resultTypeSpec;
	}

	public PropertyDefinitionType getResultType() {
		return resultType;
	}

	public void setResultType(PropertyDefinitionType resultType) {
		this.resultType = resultType;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Override
	public PropertyType copy() {
		ExpressionType copy = new ExpressionType();
		copy.expression = expression;
		copy.resultType = resultType;
		if (resultTypeSpec != null) {
			copy.resultTypeSpec = resultTypeSpec.copy();
		}
		return copy;
	}

	@Override
	public PropertyDefinition createPropertyDefinitionInstance() {
		ExpressionProperty prop = new ExpressionProperty();
		prop.setExpression(expression);
		if (resultType != null) {
			prop.setResultType(resultType);
		}
		if (resultTypeSpec != null) {
			prop.setResultTypeSpec(resultTypeSpec.createPropertyDefinitionInstance());
		}
		
		return prop;
	}

	@Override
	public void applyDefinition(PropertyDefinition def) {
		super.applyDefinition(def);
		ExpressionProperty exDef = (ExpressionProperty) def;
		expression = exDef.getExpression();
		resultType = exDef.getResultType();
		if (resultType != null && exDef.getResultTypeSpec() != null) {
			resultTypeSpec = ServiceRegistry.getRegistry().getService(PropertyService.class).newPropertyType(exDef.getResultTypeSpec());
		} else {
			resultTypeSpec = null;
		}
	}

	@Override
	public Object createRuntime(
			MetaProperty metaProperty, MetaEntity metaEntity) {
		return null;
	}

	@Override
	public Object fromDataStore(Object fromDataStore) {
		return fromDataStore;
	}

	@Override
	public Class<?> storeType() {
//		return resultType.getType();
		return null;
	}

	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.EXPRESSION;
	}
	
	@Override
	public Object toDataStore(Object toDataStore) {
		throw new UnsupportedOperationException("ExpressionType is ReadOnly");
	}

	public ValueExpression translate(EntityField field) {
		
		ValueExpression ve;
		try {
			ve = QueryServiceHolder.getInstance().getQueryParser().parse(expression, PolynomialSyntax.class);
		} catch (ParseException e) {
			throw new QueryException(e.getMessage(), e);
		}
		
		final String parentPath = field.getPropertyName().contains(".") 
				? field.getPropertyName().substring(0, field.getPropertyName().lastIndexOf('.'))
				: null;
		
		ExpressionVisitor qvs = new ExpressionVisitor(parentPath);
		ve.accept(qvs);
		
		return ve;
	}
	
	//FIXME バージョン対応したクエリー変換は、サブクエリだけでなく、通常のEntityFieldにも必要と思われる。。。
	//		処理の順番として、仮想カラムの展開→バージョン用に変換の順番がよいが、今は逆。
	
	
	private class ExpressionVisitor extends QueryVisitorSupport {
		ExpressionVisitor upper;
		String parentPath;
		
		boolean inSubqueryOnClause;
		
		ExpressionVisitor(String parentPath) {
			this.parentPath = parentPath;
		}
		
		@Override
		public boolean visit(EntityField entityField) {
			if (inSubqueryOnClause) {
				ExpressionVisitor forMain = this;
				int unnestCount = entityField.unnestCount();
				
				if (unnestCount > 0) {
					for (int i = 0; i < unnestCount; i++) {
						if (forMain == null) {
							throw new QueryException("subQuery's on clause invalid. not found correlation property:" + entityField);
						}
						forMain = forMain.upper;
					}
					if (forMain == null && parentPath != null) {
						//一番外側と結合なので結合用対象を変換する。
						String pre = entityField.getPropertyName().substring(0, unnestCount);
						String prop = entityField.getPropertyName().substring(unnestCount);
						if (!SubQuery.THIS.equalsIgnoreCase(prop)) {
							entityField.setPropertyName(pre + parentPath + "." + prop);
						} else {
							entityField.setPropertyName(pre + parentPath);
						}
					}
					
					return super.visit(entityField);
				}
			}
			
			if (upper == null && parentPath != null) {
				entityField.setPropertyName(parentPath + "." + entityField.getPropertyName());
			}
			return super.visit(entityField);
		}
		
		@Override
		public boolean visit(SubQuery subQuery) {
			//一番外側の条件だけ変更すればよいので、
			//ScalerSubQuery内は、変換しない。
			//ただし、Onの外側は必要。
			
			Query q = subQuery.getQuery();
			
			//FIXME ここでバージョンクエリー変換しているのはカッコ悪い。。。
			if (upper == null) {
				if (!q.isVersiond()) {
					EntityContext context = EntityContext.getCurrentContext();
					EntityService ehService = ServiceRegistry.getRegistry().getService(EntityService.class);
					EntityHandler eh = ehService.getRuntimeByName(q.getFrom().getEntityName());
					if (eh == null) {
						throw new EntityRuntimeException(q.getFrom().getEntityName() + " is undefined. at expression property of " + expression);
					}
					q = (Query) new VersionedQueryNormalizer(
							ehService.getVersionController(eh), eh, context, null).visit(q);
					subQuery.setQuery(q);
				}
			}
			
			ExpressionVisitor nest = new ExpressionVisitor(parentPath);
			nest.upper = this;
			q.accept(nest);
			
			Condition on = subQuery.getOn();
			if (on != null) {
				inSubqueryOnClause = true;
				subQuery.getOn().accept(nest);
				inSubqueryOnClause = false;
			}
			
			return false;
		}
	}
	

	@Override
	public boolean isVirtual() {
		return true;
	}

	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		} else {
			return value.toString();
		}
	}

	@Override
	public Object toVirtualTypeValue(Object value, PrimitivePropertyHandler ph) {
		if (resultType == null) {
			return value;
		}
		switch (resultType) {
		case BOOLEAN:
			return ConvertUtil.convert(Boolean.class, value);
		case DATE:
			return ConvertUtil.convert(java.sql.Date.class, value);
		case DATETIME:
			return ConvertUtil.convert(java.sql.Timestamp.class, value);
		case DECIMAL:
			return ConvertUtil.convert(BigDecimal.class, value);
		case FLOAT:
			return ConvertUtil.convert(Double.class, value);
		case INTEGER:
			return ConvertUtil.convert(Long.class, value);
		case SELECT:
			//現状Select型のみ詳細指定が必要
			if (resultTypeSpec != null) {
				SelectType st = (SelectType) resultTypeSpec;
				if (value == null) {
					return null;
				} else if (value instanceof SelectValue) {
					return st.fromDataStore(((SelectValue) value).getValue());
				} else {
					return st.fromDataStore(value.toString());
				}
			} else {
				return ConvertUtil.convert(SelectValue.class, value);
			}
		case STRING:
			return ConvertUtil.convert(String.class, value);
		case TIME:
			return ConvertUtil.convert(java.sql.Time.class, value);
		default:
			return value;
		}
	}

	@Override
	public Object[] newVirtualTypeArray(int size) {
		if (resultType == null) {
			return new Object[size];
		}
		switch (resultType) {
		case BOOLEAN:
			return new Boolean[size];
		case DATE:
			return new java.sql.Date[size];
		case DATETIME:
			return new java.sql.Timestamp[size];
		case DECIMAL:
			return new BigDecimal[size];
		case FLOAT:
			return new Double[size];
		case INTEGER:
			return new Long[size];
		case SELECT:
			return new SelectValue[size];
		case STRING:
			return new String[size];
		case TIME:
			return new java.sql.Time[size];
		default:
			return new Object[size];
		}
	}

	@Override
	public Object fromString(String strValue) {
		if (resultType == null) {
			return strValue;
		}
		switch (resultType) {
		case BOOLEAN:
			return ConvertUtil.convert(Boolean.class, strValue);
		case DATE:
			return ConvertUtil.convert(java.sql.Date.class, strValue);
		case DATETIME:
			return ConvertUtil.convert(java.sql.Timestamp.class, strValue);
		case DECIMAL:
			return ConvertUtil.convert(BigDecimal.class, strValue);
		case FLOAT:
			return ConvertUtil.convert(Double.class, strValue);
		case INTEGER:
			return ConvertUtil.convert(Long.class, strValue);
		case SELECT:
			//現状Select型のみ詳細指定が必要
			if (resultTypeSpec != null) {
				SelectType st = (SelectType) resultTypeSpec;
				if (strValue == null) {
					return null;
				} else {
					return st.fromDataStore(strValue);
				}
			} else {
				return ConvertUtil.convert(SelectValue.class, strValue);
			}
		case STRING:
			return strValue;
		case TIME:
			return ConvertUtil.convert(java.sql.Time.class, strValue);
		default:
			return strValue;
		}
	}
	
	

}
