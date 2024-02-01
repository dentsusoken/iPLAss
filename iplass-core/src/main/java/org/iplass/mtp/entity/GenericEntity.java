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

package org.iplass.mtp.entity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.jaxb.EntityPropertyXmlAdapter;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;


/**
 * Entityのデータを表現するクラス。
 * key-value形式で、Entityのプロパティの値を保持。
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement(name="entity")
@XmlType(name="entity")
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericEntity implements Entity, Serializable {

	//FIXME インタフェースとGenericEntityを統合

	private static final long serialVersionUID = 7059431419625229782L;

	@XmlJavaTypeAdapter(EntityPropertyXmlAdapter.class)
	private Map<String, Object> properties;

	@XmlAttribute
	private String definitionName;

	public GenericEntity() {
	}

	public GenericEntity(String definitionName) {
		this.definitionName = definitionName;
	}

	public GenericEntity(String definitionName, String oid, String name) {
		this.definitionName = definitionName;
		setOid(oid);
		setName(name);
	}

//	/**
//	 * 現状、未実装
//	 *
//	 * @param pojoObject
//	 */
//	public GenericEntity(Object pojoObject) {
//		//TODO 実装
//	}

	public GenericEntity(EntityDefinition dataModelDefinition) {
		this.definitionName = dataModelDefinition.getName();
	}

	public void applyProperties(Map<String, Object> properties) {
		this.properties = new HashMap<>(properties);
	}
	
	private void toString(StringBuilder sb, LinkedList<Entity> stack) {
		if (stack.contains(this)) {
			sb.append("{\"definitionName\":\"").append(definitionName).append("\"");
			sb.append(",\"oid\":\"").append(getOid()).append("\"");
			sb.append(",\"isLoop\":true}");
		} else {
			stack.push(this);
			sb.append("{\"definitionName\":\"").append(definitionName).append("\"");
			if (properties != null) {
				if (properties.size() > 0) {
					for (Map.Entry<String, Object> e: properties.entrySet()) {
						Object key = e.getKey();
						Object val = e.getValue();
						sb.append(",");
						sb.append("\"").append(key).append("\":");
						if (val instanceof Object[]) {
							Object[] valArray = (Object[]) val;
							sb.append("[");
							for (int i = 0; i < valArray.length; i++) {
								if (i != 0) {
									sb.append(",");
								}
								if (valArray[i] instanceof GenericEntity) {
									((GenericEntity) valArray[i]).toString(sb, stack);
								} else if (valArray[i] instanceof String) {
									sb.append("\"").append(valArray[i]).append("\"");
								} else {
									sb.append(valArray[i]);
								}
							}
							sb.append("]");
						} else if (val instanceof GenericEntity) {
							((GenericEntity) val).toString(sb, stack);
						} else if (val instanceof String) {
							sb.append("\"").append(val).append("\"");
						} else {
							sb.append(val);
						}
					}
				}
			}
			sb.append("}");
			stack.pop();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb, new LinkedList<Entity>());
		return sb.toString();
	}

	/**
	 * propNameで表現されるプロパティを取得します。<br>
	 * <br>
	 * <b>注意<br></b>
	 * {@link #getValue(String, boolean)}をenableExpression=trueで呼び出します。
	 * propNameはクライアントからの入力値を未検証のまま適用しないでください。
	 * 改竄された場合意図しないプロパティ値が取得される可能性があります。
	 * enableExpression=falseで呼び出したい場合は、明示的に{@link #getValue(String, boolean)}を利用してください。
	 */
	@Override
	public <P> P getValue(String propName) {
		return getValue(propName, true);
	}
	
	/**
	 * propNameで表現されるプロパティを取得します。
	 * 
	 * enableExpression=trueの場合、propNameには、"."にてネストされたプロパティ、
	 * "[index]"にて配列アクセスを指定可能。<br>
	 * 例えば、
	 * "role.condition[0].name"は、getValue("role").getValue("condition")[0].getValue("name")を示す。<br>
	 * <br>
	 * <b>注意<br>
	 * enableExpression=trueの場合、propNameはクライアントからの入力値を未検証のまま適用しないでください。
	 * 改竄された場合意図しないプロパティ値が取得される可能性があります。
	 * </b>
	 * 
	 * @param propName
	 * @param enableExpression
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <P> P getValue(String propName, boolean enableExpression) {
		if (properties != null) {
			if (!enableExpression) {
				return (P) properties.get(propName);
			}

			int firstDotIndex = propName.indexOf('.');
			if (firstDotIndex > 0) {
				String topPropName = propName.substring(0, firstDotIndex);
				String subPropName = propName.substring(firstDotIndex + 1);
				Entity topEntity = (Entity) getValueInternal(topPropName);
				if (topEntity == null) {
					return null;
				} else {
					return topEntity.<P> getValue(subPropName);
				}
			} else {
				return (P) getValueInternal(propName);
			}
		} else {
			return null;
		}
	}
	
	private Object getValueInternal(String propName) {
		int begin = propName.indexOf('[');
		if (begin > 0) {
			int end = propName.indexOf(']');
			if (end < 0) {
				throw new IllegalArgumentException("propName expression invalid:" + propName);
			}
			try {
				int index = Integer.parseInt(propName.substring(begin + 1, end));
				Object val = properties.get(propName.substring(0, begin));
				if (val == null) {
					return null;
				}
				if (val instanceof Object[]) {
					Object[] valArray = (Object[]) val;
					if (valArray.length <= index) {
						return null;
					}
					return valArray[index];
				}
				return null;
				
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("propName expression invalid:" + propName, e);
			}
			
		} else {
			return properties.get(propName);
		}
	}
	
	/**
	 * propNameで表現されるプロパティにvalueをセットします。<br>
	 * <br>
	 * <b>注意<br></b>
	 * {@link #setValue(String, Object, boolean)}をenableExpression=trueで呼び出します。
	 * propNameはクライアントからの入力値を未検証のまま適用しないでください。
	 * 改竄された場合意図しないプロパティに値ががセットされる可能性があります。<br>
	 * enableExpression=falseで呼び出したい場合は、明示的に{@link #setValue(String, Object, boolean)}を利用してください。
	 */
	@Override
	public void setValue(String propName, Object value) {
		setValue(propName, value, true);
	}
	
	/**
	 * propNameで表現されるプロパティにvalueをセットします。
	 * enableExpression=trueの場合、propNameには、"."にてネストされたプロパティ、
	 * "[index]"にて配列アクセスを指定可能です。<br>
	 * 例えば、
	 * "role.condition[0].name"は、getValue("role").getValue("condition")[0].getValue("name")を示します。<br>
	 * <br>
	 * <b>注意<br>
	 * enableExpression=trueの場合、propNameはクライアントからの入力値を未検証のまま適用しないでください。
	 * 改竄された場合意図しないプロパティに値がセットされる可能性があります。
	 * </b>
	 * 
	 * @param propName
	 * @param value
	 * @param enableExpression
	 */
	public void setValue(String propName, Object value, boolean enableExpression) {
		if (value == null && getValue(propName, enableExpression) == null) {
			return;
		}

		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		
		if (!enableExpression) {
			if (value == null) {
				properties.remove(propName);
			} else {
				properties.put(propName, value);
			}
		}

		int firstDotIndex = propName.indexOf('.');
		if (firstDotIndex > 0) {
			String topPropName = propName.substring(0, firstDotIndex);
			String subPropName = propName.substring(firstDotIndex + 1);
			Entity topEntity = (Entity) getValueInternal(topPropName);
			if (topEntity == null) {
				EntityContext ec = EntityContext.getCurrentContext();
				EntityHandler myDef = ec.getHandlerByName(getDefinitionName());
				if (myDef == null) {
					throw new EntityRuntimeException("DefinitionName is unspecified.");
				}
				PropertyHandler ph = myDef.getProperty(propNameOnly(topPropName), ec);
				if (ph == null || !(ph instanceof ReferencePropertyHandler)) {
					throw new EntityRuntimeException(topPropName + " is undefined or not ReferenceProperty.");
				}
				EntityHandler refDef = ((ReferencePropertyHandler) ph).getReferenceEntityHandler(ec);
				if (refDef == null) {
					throw new EntityRuntimeException(topPropName + "'s EntityDefinition is undefined.");
				}
				topEntity = refDef.newInstance();
				setValueInternal(topPropName, topEntity, refDef);
			}
			
			if (topEntity instanceof GenericEntity) {
				((GenericEntity) topEntity).setValue(subPropName, value, enableExpression);
			} else {
				topEntity.setValue(subPropName, value);
			}

		} else {
			setValueInternal(propName, value, null);
		}
	}

	private String propNameOnly(String propName) {
		int begin = propName.indexOf('[');
		if (begin > 0) {
			return propName.substring(0, begin);
		} else {
			return propName;
		}
	}
	
	private void setValueInternal(String propName, Object value, EntityHandler eh) {
		int begin = propName.indexOf('[');
		if (begin > 0) {
			int end = propName.indexOf(']');
			if (end < 0) {
				throw new IllegalArgumentException("propName expression invalid:" + propName);
			}
			try {
				int index = Integer.parseInt(propName.substring(begin + 1, end));
				String propNameOnly = propName.substring(0, begin);
				Object val = properties.get(propNameOnly);
				if (val == null) {
					if (value == null) {
						return;
					} else {
						Object[] valArray = (eh == null) ?
								(Object[]) Array.newInstance(value.getClass(), index + 1): eh.newArrayInstance(index + 1);
						valArray[index] = value;
						properties.put(propNameOnly, valArray);
						return;
					}
				}
				if (val instanceof Object[]) {
					Object[] valArray = (Object[]) val;
					if (value == null) {
						if (valArray.length > index) {
							valArray[index] = null;
						}
						return;
					} else {
						if (valArray.length <= index) {
							valArray = Arrays.copyOf(valArray, index + 1);
							properties.put(propNameOnly, valArray);
						}
						valArray[index] = val;
						return;
					}
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("propName expression invalid:" + propName, e);
			}
			
		} else {
			if (value == null) {
				//TODO 現状、null値のセットができないが、
				//次回以降のマイナーバージョンアップでnull値を明示的にセットできるようにする方向で検討する
				properties.remove(propName);
			} else {
				properties.put(propName, value);
			}
		}
	}
	
	@Override
	public String getOid() {
		return (String) getValue(Entity.OID);
	}

	@Override
	public void setOid(String oid) {
		setValue(Entity.OID, oid);
	}

	@Override
	public String getName() {
		return (String) getValue(Entity.NAME);
	}

	@Override
	public void setName(String name) {
		setValue(Entity.NAME, name);
	}

	@Override
	public Timestamp getCreateDate() {
		return (Timestamp) getValue(Entity.CREATE_DATE);
	}

	@Override
	public void setCreateDate(Timestamp createDate) {
		setValue(Entity.CREATE_DATE, createDate);
	}

	@Override
	public Timestamp getUpdateDate() {
		return (Timestamp) getValue(Entity.UPDATE_DATE);
	}

	@Override
	public void setUpdateDate(Timestamp updateDate) {
		setValue(Entity.UPDATE_DATE, updateDate);
	}

	@Override
	public String getCreateBy() {
		return (String) getValue(Entity.CREATE_BY);
	}

	@Override
	public void setCreateBy(String createBy) {
		setValue(Entity.CREATE_BY, createBy);
	}

	@Override
	public String getUpdateBy() {
		return (String) getValue(Entity.UPDATE_BY);
	}

	@Override
	public void setUpdateBy(String updateBy) {
		setValue(Entity.UPDATE_BY, updateBy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P> P getValueAs(Class<P> type, String propName) {
		//TODO 実装
		return (P)getValue(propName);
	}

//	public <P> List<P> getValueListAs(Class<P> type, String propName) {
//		//TODO タイプセーフのリストとして扱うためには必要か
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public String getDefinitionName() {
		return definitionName;
	}

	@Override
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	@Override
	public String getDescription() {
		return (String) getValue(Entity.DESCRIPTION);
	}

	@Override
	public void setDescription(String description) {
		setValue(Entity.DESCRIPTION, description);
	}

	@Override
	public Long getVersion() {
		return getValue(VERSION);
	}

	@Override
	public void setVersion(Long version) {
		setValue(VERSION, version);
	}

	@Override
	public SelectValue getState() {
		return getValue(STATE);
	}

	@Override
	public void setState(SelectValue state) {
		setValue(STATE, state);
	}

	@Override
	public String getLockedBy() {
		return getValue(LOCKED_BY);
	}

	@Override
	public void setLockedBy(String lockedBy) {
		setValue(LOCKED_BY, lockedBy);
	}

	@Override
	public Timestamp getStartDate() {
		return getValue(START_DATE);
	}

	@Override
	public void setStartDate(Timestamp startDate) {
		setValue(START_DATE, startDate);
	}

	@Override
	public Timestamp getEndDate() {
		return getValue(END_DATE);
	}

	@Override
	public void setEndDate(Timestamp endDate) {
		setValue(END_DATE, endDate);
	}

	@Override
	public Long getRecycleBinId() {
		return getValue(RECYCLE_BIN_ID);
	}

	@Override
	public void setRecycleBinId(Long recycleBinId) {
		setValue(RECYCLE_BIN_ID, recycleBinId);
	}

	/**
	 * shallow copyを行う。
	 *
	 * @return
	 */
	public GenericEntity copy() {
		GenericEntity copy = null;
		if (this.getClass() == GenericEntity.class) {
			copy = new GenericEntity(definitionName);
		} else {
			try {
				copy = (GenericEntity) getClass().newInstance();
				copy.setDefinitionName(definitionName);
			} catch (InstantiationException e) {
				throw new EntityRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new EntityRuntimeException(e);
			}
		}
		if (properties != null) {
			copy.properties = new HashMap<String, Object>(properties);
		}
		return copy;
	}

	/**
	 * 引数の型のインスタンスとして、shallow copyを行う。
	 *
	 * @return
	 */
	public <T extends GenericEntity> T copyAs(Class<T> type) {
		try {
			T copy = type.newInstance();
			copy.setDefinitionName(definitionName);
			if (properties != null) {
				((GenericEntity) copy).properties = new HashMap<String, Object>(properties);
			}
			return copy;
		} catch (InstantiationException e) {
			throw new EntityRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new EntityRuntimeException(e);
		}
	}

	/**
	 * deep copyを行う（但し、Entityとしてのdeep copy）。
	 * property値が、GenericEntityの場合は、deepCopy()を再帰呼び出し、
	 * BinaryReference、SelectValueの場合は、copy()を呼び出し、
	 * java.uti.Dateの場合は、clone()を呼び出し、
	 * それ以外（プリミティブ型、immutable）の場合は、参照をそのまま保持。
	 *
	 * @return
	 */
	public GenericEntity deepCopy() {
		Map<GenericEntity, GenericEntity> done = new HashMap<GenericEntity, GenericEntity>();
		return copyInternal(done);
	}

	private GenericEntity copyInternal(Map<GenericEntity, GenericEntity> done) {
		GenericEntity alreadyCopy = done.get(this);
		if (alreadyCopy != null) {
			return alreadyCopy;
		}
		GenericEntity copy = null;
		if (this.getClass() == GenericEntity.class) {
			copy = new GenericEntity(definitionName);
		} else {
			try {
				copy = (GenericEntity) getClass().newInstance();
				copy.setDefinitionName(definitionName);
			} catch (InstantiationException e) {
				throw new EntityRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new EntityRuntimeException(e);
			}
		}
		done.put(this, copy);
		if (properties != null) {
			copy.properties = new HashMap<String, Object>();
			for (Map.Entry<String, Object> e: properties.entrySet()) {
				Object val = e.getValue();
				if (val instanceof Object[]) {
					Object[] valArray = (Object[]) val;
					Object[] newValArray = (Object[]) Array.newInstance(valArray.getClass().getComponentType(), valArray.length);
					for (int i = 0; i < valArray.length; i++) {
						newValArray[i] = copyVal(valArray[i], done);
					}
					copy.properties.put(e.getKey(), newValArray);
				} else {
					copy.properties.put(e.getKey(), copyVal(val, done));
				}
			}
		}

		return copy;

	}

	private Object copyVal(Object val, Map<GenericEntity, GenericEntity> done) {
		if (val instanceof GenericEntity) {
			return ((GenericEntity) val).copyInternal(done);
		}
		if (val instanceof SelectValue) {
			return ((SelectValue) val).copy();
		}
		if (val instanceof BinaryReference) {
			return ((BinaryReference) val).copy();
		}
		if (val instanceof Date) {
			return ((Date) val).clone();
		}
		return val;

	}

	/**
	 * 保持しているPropertyの名前セットを返します。
	 *
	 * @return Property名のセット
	 */
	public Set<String> getPropertyNames() {
		if (properties == null) {
			return null;
		}

		return properties.keySet();
	}
	
	/**
	 * 保持しているPropertyをMap形式で返す。
	 * property値が、GenericEntityの場合は、再帰的にMapに変換する、
	 * GenericEntity[]の場合はMapの配列に変換する、
	 * BinaryReference、SelectValueの場合は、copy()を呼び出し、
	 * java.uti.Dateの場合は、clone()を呼び出し、
	 * それ以外（プリミティブ型、immutable）の場合は、参照をそのまま保持。
	 *  
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for (Map.Entry<String, Object> e : properties.entrySet()) {
			if (e.getValue() instanceof Object[]) {
				Object[] valArray = (Object[]) e.getValue();
				if (valArray instanceof Entity[]) {
					Map<String, Object>[] newValArray = (Map<String, Object>[]) Array.newInstance(HashMap.class, valArray.length);
					for (int i = 0; i < valArray.length; i++) {
						GenericEntity entity = (GenericEntity)valArray[i];
						newValArray[i] = entity.toMap();
					}
					map.put(e.getKey(), newValArray);
				} else {
					Object[] newValArray = (Object[]) Array.newInstance(valArray.getClass().getComponentType(), valArray.length);
					for (int i = 0; i < valArray.length; i++) {
						newValArray[i] = copyValForToMap(valArray[i]);
					}
					map.put(e.getKey(), newValArray);
				}
			} else {
				map.put(e.getKey(), copyValForToMap(e.getValue()));
			}
		}
		return map;
	}
	
	private Object copyValForToMap(Object val) {
		if (val instanceof GenericEntity) {
			return ((GenericEntity) val).toMap();
		}
		if (val instanceof SelectValue) {
			return ((SelectValue) val).copy();
		}
		if (val instanceof BinaryReference) {
			return ((BinaryReference) val).copy();
		}
		if (val instanceof Date) {
			return ((Date) val).clone();
		}
		return val;
	}


//	/**
//	 * JSON形式の送受信にpropertiesを含めるためのメソッド(Jacksonへの対応)。
//	 * 利用箇所はなし。
//	 *
//	 * org.codehaus.jackson.annotate.JsonProperty
//	 * を指定することでgetter、setterは不要になるが、
//	 * Entityとしてはjacksonに依存したくないため、
//	 * privateメソッドとして定義。
//	 *
//	 * @param properties
//	 */
//	@SuppressWarnings("unused")
//	private void setProperties(Map<String, Object> properties) {
//		this.properties = properties;
//	}
//
//  /**
//  * プロパティをMap形式で取得。
//  *
//  * @return
//  */
// public Map<String, Object> getProperties() {
//     if (properties == null) {
//         properties = new HashMap<String, Object>();
//     }
//     return properties;
// }
	
	
}
