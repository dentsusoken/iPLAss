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

package org.iplass.mtp.impl.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformerSupport;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.controlflow.Case;
import org.iplass.mtp.entity.query.value.primary.Cast;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.datastore.strategy.SearchResultIterator;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.properties.extend.ComplexWrapperType;
import org.iplass.mtp.impl.properties.extend.ComplexWrapperTypeLoadAdapter;
import org.iplass.mtp.impl.properties.extend.SelectType;
import org.iplass.mtp.impl.properties.extend.VirtualType;
import org.iplass.mtp.impl.properties.extend.select.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VirtualPropertyAdapter extends ASTTransformerSupport implements SearchResultIterator {
	private static final Logger logger = LoggerFactory.getLogger(VirtualPropertyAdapter.class);

	private class FieldMapping {
		boolean needTrans;
		ValueExpression field;
		ValueExpression actualField;
		PrimitivePropertyHandler ph;
		LoadAdapterMapping lam;
		
		boolean isSelectCast;

		public FieldMapping(ValueExpression field, ValueExpression actualField,
				PrimitivePropertyHandler ph) {
			this.field = field;
			this.actualField = actualField;
			this.ph = ph;
		}
		
		public FieldMapping(ValueExpression field, ValueExpression actualField,
				boolean isSelectCast) {
			this.field = field;
			this.actualField = actualField;
			this.isSelectCast = isSelectCast;
			this.needTrans = true;
		}

	}

	private class LoadAdapterMapping {
		ComplexWrapperType type;
		ComplexWrapperTypeLoadAdapter loadAdapter;
		List<FieldMapping> fields;

		public LoadAdapterMapping(ComplexWrapperType type,
				ComplexWrapperTypeLoadAdapter loadAdapter) {
			this.type = type;
			this.loadAdapter = loadAdapter;
		}

		void addField(FieldMapping field) {
			if (fields == null) {
				fields = new ArrayList<FieldMapping>();
			}
			fields.add(field);
			field.lam = this;
		}

	}

	private static class ThisRefNormalizer extends QueryVisitorSupport {
		private EntityContext ec;
		private EntityHandler eh;
		private ThisRefNormalizer parent;
		
		private boolean isOn;
		
		private ThisRefNormalizer(EntityContext ec, EntityHandler eh, ThisRefNormalizer parent) {
			this.ec = ec;
			this.eh = eh;
			this.parent = parent;
		}

		@Override
		public boolean visit(EntityField entityField) {
			//subqueryのon内での参照指定、THIS指定をoid指定に変換する
			if (isOn) {
				String pname = entityField.getPropertyName();
				ThisRefNormalizer cn = this;
				String prefix = null;
				for (int i = 0; i < pname.length(); i++) {
					if (pname.charAt(i) == '.') {
						if (parent == null) {
							throw new QueryException("can't unnest property:" + entityField);
						}
						cn = parent;
					} else {
						prefix = pname.substring(0, i);
						pname = pname.substring(i);
						break;
					}
				}
				
				if (pname.equalsIgnoreCase(SubQuery.THIS)) {
					pname = Entity.OID;
				} else {
					EntityHandler target = cn.eh;
					PropertyHandler ph = target.getPropertyCascade(pname, ec);
					if (ph == null) {
						throw new QueryException("can't find property:" + entityField);
					}
					if (ph instanceof ReferencePropertyHandler) {
						pname = pname + "." + Entity.OID;
					}
				}
				
				if (prefix.length() > 0) {
					pname = prefix + pname;
				}
				
				entityField.setPropertyName(pname);
			}
			return super.visit(entityField);
		}

		@Override
		public boolean visit(SubQuery subQuery) {
			
			EntityHandler subEh = ec.getHandlerByName(subQuery.getQuery().getFrom().getEntityName());
			if (subEh == null) {
				throw new QueryException(subQuery.getQuery().getFrom().getEntityName() + " not defined");
			}
			
			ThisRefNormalizer subNor = new ThisRefNormalizer(ec, subEh, this);
			subQuery.getQuery().accept(subNor);
			
			if (subQuery.getOn() != null) {
				subNor.isOn = true;
				subQuery.getOn().accept(subNor);
			}
			
			return false;
		}
	}

	private Map<ValueExpression, FieldMapping> selectFieldMap;
	private Map<ComplexWrapperType, LoadAdapterMapping> loadAdaptorMap;

	private Query query;
	private Query transformedQuery;
	private EntityContext ec;
	private EntityHandler eh;

	private boolean isSubQuery = false;
	
	private boolean isSelectField = false;

	private SearchResultIterator iterator;
	
	public VirtualPropertyAdapter(Query query, EntityContext ec, EntityHandler eh) {
		selectFieldMap = new HashMap<ValueExpression, FieldMapping>();
		this.query = query;
		this.ec = ec;
		this.eh = eh;
		loadAdaptorMap = new HashMap<>();
	}
	

	public Query getTransformedQuery() {
		if (transformedQuery == null) {
			transformedQuery = (Query) query.accept(this);
			
			//subqueryのon内のReference名指定（もしくはTHIS指定）をoidでの結合に変換する。
			transformedQuery.accept(new ThisRefNormalizer(ec, eh, null));
			
			if (logger.isDebugEnabled()) {
				logger.debug("translate to virtualProperty extracted query:" + transformedQuery);
			}
		}
		return transformedQuery;
	}

	public void setIterator(SearchResultIterator iterator) {
		this.iterator = iterator;
		for (Map.Entry<ComplexWrapperType, LoadAdapterMapping> e: loadAdaptorMap.entrySet()) {
			e.getValue().loadAdapter.setContext(ec);
		}
	}

	public boolean next() {
		if (iterator == null) {
			return false;
		}
		boolean isNext = iterator.next();
		if (isNext) {
			if (loadAdaptorMap.size() != 0) {
				for (Map.Entry<ComplexWrapperType, LoadAdapterMapping> e: loadAdaptorMap.entrySet()) {
					List<Object> values = new ArrayList<Object>();
					for (FieldMapping f: e.getValue().fields) {
						Object val = iterator.getValue(f.actualField);
						if (val != null) {
							if (val instanceof Object[]) {
								for (Object v: (Object[]) val) {
									values.add(v);
								}
							} else {
								values.add(val);
							}
						}
					}
					if (values.size() != 0) {
						e.getValue().loadAdapter.nextCalled(values);
					}
				}
			}
		}
		return isNext;
	}

	public Object getValue(ValueExpression propName) {
		FieldMapping mappedProp = selectFieldMap.get(propName);
		ValueExpression mappedPropName = (ValueExpression) mappedProp.actualField;
		Object value = iterator.getValue(mappedPropName);

		if (mappedProp.needTrans) {
			if (mappedProp.isSelectCast) {
				if (value != null) {
					value = new SelectValue(value.toString());
				}
			} else if (mappedProp.lam != null) {
				//ComplexTypeの変換
				if (mappedProp.ph.getMetaData().getMultiplicity() == 1) {
					value = mappedProp.lam.loadAdapter.toComplexWrapperTypeValue(value);
				} else {
					if (value != null) {
						Object[] valArray = (Object[]) value;
						Object[] extendValArray = mappedProp.lam.loadAdapter.newComplexWrapperTypeArray(valArray.length);
						for (int i = 0; i < valArray.length; i++) {
							extendValArray[i] = mappedProp.lam.loadAdapter.toComplexWrapperTypeValue(valArray[i]);
						}
						value = extendValArray;
					}
				}
			} else if (mappedProp.ph.getMetaData().getType() instanceof VirtualType) {
				VirtualType vt = (VirtualType) mappedProp.ph.getMetaData().getType();
				//VirtualTypeの変換
				if (mappedProp.ph.getMetaData().getMultiplicity() == 1) {
					value = vt.toVirtualTypeValue(value, mappedProp.ph);
				} else {
					if (value != null) {
						Object[] valArray = (Object[]) value;
						Object[] extendValArray = vt.newVirtualTypeArray(valArray.length);
						for (int i = 0; i < valArray.length; i++) {
							extendValArray[i] = vt.toVirtualTypeValue(valArray[i], mappedProp.ph);
						}
						value = extendValArray;
					}
				}
			}
		}
		return value;
	}

	public void close() {
		if (iterator != null) {
			iterator.close();
		}
	}

//FIXME リファクタリング！！！
//	
//	ここでやるべきこと
//クエリ変換
//・ExpressionPropertyの変換
//・サブクエリのON句の変換
//・SelectPropertyのOrder指定時の変換
//・
//
//型に合わせたロード時の変換、追加のクエリ実行
//・select項目と型・処理のマッピング
//・get時の変換処理
//
//クエリ変換と型マッピング処理は分けるべきか？



	//以下、query解析処理

	@Override
	public ASTNode visit(Select select) {
		List<ValueExpression> selectValues = new ArrayList<ValueExpression>();
		if (select.getSelectValues() != null) {
			for (ValueExpression v: select.getSelectValues()) {
				if (!isSubQuery && v instanceof EntityField) {
					isSelectField = true;
				}
				ValueExpression transV = (ValueExpression) v.accept(this);
				isSelectField = false;
				selectValues.add(transV);
				if (!isSubQuery) {
					if (v instanceof EntityField) {
						EntityHandler handler = null;
						if (eh.getMetaData().getName().equals(query.getFrom().getEntityName())) {
							handler = eh;
						} else {
							handler = ec.getHandlerByName(query.getFrom().getEntityName());
						}
						PropertyHandler ph = handler.getPropertyCascade(((EntityField) v).getPropertyName(), ec);
						if (ph instanceof PrimitivePropertyHandler) {
							FieldMapping fMap = new FieldMapping(v, transV, (PrimitivePropertyHandler) ph);
							selectFieldMap.put(v, fMap);
							PropertyType type = fMap.ph.getMetaData().getType();
							if (type instanceof ComplexWrapperType) {
								LoadAdapterMapping laMap = null;
								for (Map.Entry<ComplexWrapperType, LoadAdapterMapping> e: loadAdaptorMap.entrySet()) {
									if (e.getKey().equals(type)) {
										laMap = e.getValue();
										break;
									}
								}
								if (laMap == null) {
									laMap = new LoadAdapterMapping((ComplexWrapperType) type, ((ComplexWrapperType) type).createLoadAdapter());
									loadAdaptorMap.put(laMap.type, laMap);
								}
								laMap.addField(fMap);
								fMap.needTrans = true;
							} else if (type instanceof VirtualType) {
								fMap.needTrans = true;
							}
						}
					} else if (v instanceof Cast && ((Cast) v).getType() == PropertyDefinitionType.SELECT) {
						selectFieldMap.put(v, new FieldMapping(v, transV, true));
					} else {
						selectFieldMap.put(v, new FieldMapping(v, transV, null));
					}
				}
			}
		}
		Select copy = new Select(select.isDistinct(), selectValues);
		copy.setHintComment(select.getHintComment());
		return copy;
	}

	@Override
	public ASTNode visit(EntityField entityField) {
		ValueExpression v = null;
		
		if (entityField.getPropertyName().charAt(0) == '.') {
			//on句での指定と判断して、処理しない
			return (ValueExpression) super.visit(entityField);
		}
		
		PropertyHandler ph = getPh(entityField.getPropertyName());
		if (ph instanceof PrimitivePropertyHandler) {
			MetaPrimitiveProperty mpp = (MetaPrimitiveProperty) ph.getMetaData();
			if (mpp.getType() instanceof VirtualType) {
				v = ((VirtualType) mpp.getType()).translate(entityField);
				v = (ValueExpression) v.accept(this);
			} else if (!isSelectField && mpp.getType() instanceof ComplexWrapperType) {
				//FIXME LongTextTypeやBinaryTypeやSelectTypeって判断をここでやらなくてよい形にきれいにならないか？
				try {
					v = ((ComplexWrapperType) mpp.getType()).translate(entityField);
				} catch (EntityRuntimeException e) {
					logger.warn(e.getMessage() + " of eql: " + query);
				}
			}
		}
		if (v == null) {
			v = (ValueExpression) super.visit(entityField);
		}
		return v;
	}

	@Override
	public ASTNode visit(SubQuery sq) {

		boolean parentIsSubQuery = isSubQuery;
		boolean parentIsSelectField = isSelectField;
		Query parentQuery = query;

		isSubQuery = true;
		query = sq.getQuery();

		Query subQuery = null;
		Condition on = null;
		if (sq.getQuery() != null) {
			subQuery = (Query) sq.getQuery().accept(this);
		}
		if (sq.getOn() != null) {
			on = (Condition) sq.getOn().accept(this);
		}
		
		SubQuery transSubQuery = new SubQuery(subQuery, on);

		query = parentQuery;
		isSubQuery = parentIsSubQuery;
		isSelectField = parentIsSelectField;
		return transSubQuery;

	}
	
	
	protected PropertyHandler getPh(String propName) {
		EntityHandler handler = null;
		if (eh.getMetaData().getName().equals(query.getFrom().getEntityName())) {
			handler = eh;
		} else {
			handler = ec.getHandlerByName(query.getFrom().getEntityName());
		}
		return handler.getPropertyCascade(propName, ec);
	}
	
	@Override
	public ASTNode visit(Literal literal) {
		//FIXME SelectValueって判断をここでやらなくてよい形にきれいにならないか？
		if (literal.getValue() instanceof SelectValue) {
			return new Literal(((SelectValue) literal.getValue()).getValue(), literal.isBindable());
		} else {
			return super.visit(literal);
		}
	}

	@Override
	public ASTNode visit(Cast cast) {
		//FIXME SelectValueって判断をここでやらなくてよい形にきれいにならないか？
		if (cast.getType() == PropertyDefinitionType.SELECT) {
			ValueExpression val = cast.getValue();
			if (val != null) {
				val = (ValueExpression) val.accept(this);
			}
			return new Cast(val, PropertyDefinitionType.STRING);
		} else {
			return super.visit(cast);
		}
	}

	@Override
	public ASTNode visit(SortSpec order) {
		if (order.getSortKey() instanceof EntityField) {
			String propName = ((EntityField) order.getSortKey()).getPropertyName();
			PropertyHandler ph = eh.getPropertyCascade(propName, ec);
			//FIXME SelectValueって判断をここでやらなくてよい形にきれいにならないか？
			//検索条件、ソート、セレクト項目それぞれのハンドリングをPropertTypeのメソッドにデリゲートする形に
			if (ph != null && ph.getEnumType() == PropertyDefinitionType.SELECT) {
				SelectType type = (SelectType) ((PrimitivePropertyHandler) ph).getMetaData().getType();
				List<Value> sl = type.runtimeValues();
				if (sl != null && sl.size() > 0) {
					Case cs = new Case();
					for (int i = 0; i < sl.size(); i++) {
						cs.when(new Equals(propName, new Literal(sl.get(i).getValue(), false)), new Literal(Long.valueOf(i), false));
					}
					cs.elseClause(new Literal(null, false));
					return new SortSpec(cs, order.getType(), order.getNullOrderingSpec());
				}
			}
		}
		return super.visit(order);
	}
	
	
}
