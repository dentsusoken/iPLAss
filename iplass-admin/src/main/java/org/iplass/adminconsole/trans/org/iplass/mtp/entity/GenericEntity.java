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

package org.iplass.mtp.entity;

import java.io.Serializable;
//import java.lang.reflect.Array;
import java.sql.Timestamp;
//import java.util.Date;
import java.util.HashMap;
//import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.entity.jaxb.EntityPropertyXmlAdapter;


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

	/**
	 * 現状、未実装
	 *
	 * @param pojoObject
	 */
	public GenericEntity(Object pojoObject) {
		//TODO 実装
	}

	public GenericEntity(EntityDefinition dataModelDefinition) {
		this.definitionName = dataModelDefinition.getName();
	}

//	private void toString(StringBuilder sb, LinkedList<Entity> stack) {
//		if (stack.contains(this)) {
//			sb.append("{\"definitionName\":\"").append(definitionName).append("\"");
//			sb.append(",\"oid\":\"").append(getOid()).append("\"");
//			sb.append(",\"isLoop\":true}");
//		} else {
//			stack.push(this);
//			sb.append("{\"definitionName\":\"").append(definitionName).append("\"");
//			if (properties != null) {
//				if (properties.size() > 0) {
//					for (Map.Entry<String, Object> e: properties.entrySet()) {
//						Object key = e.getKey();
//						Object val = e.getValue();
//						sb.append(",");
//						sb.append("\"").append(key).append("\":");
//						if (val instanceof Object[]) {
//							Object[] valArray = (Object[]) val;
//							sb.append("[");
//							for (int i = 0; i < valArray.length; i++) {
//								if (i != 0) {
//									sb.append(",");
//								}
//								if (valArray[i] instanceof GenericEntity) {
//									((GenericEntity) valArray[i]).toString(sb, stack);
//								} else if (valArray[i] instanceof String) {
//									sb.append("\"").append(valArray[i]).append("\"");
//								} else {
//									sb.append(valArray[i]);
//								}
//							}
//							sb.append("]");
//						} else if (val instanceof GenericEntity) {
//							((GenericEntity) val).toString(sb, stack);
//						} else if (val instanceof String) {
//							sb.append("\"").append(val).append("\"");
//						} else {
//							sb.append(val);
//						}
//					}
//				}
//			}
//			sb.append("}");
//			stack.pop();
//		}
//	}
//
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		toString(sb, new LinkedList<Entity>());
//		return sb.toString();
//	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#getValue(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <P> P getValue(String propName) {
		if (properties != null) {
			int firstDotIndex = propName.indexOf('.');
			if (firstDotIndex > 0) {
				String topPropName = propName.substring(0, firstDotIndex);
				String subPropName = propName.substring(firstDotIndex + 1);
				Entity topEntity = (Entity) properties.get(topPropName);
				if (topEntity == null) {
					return null;
				} else {
					return topEntity.<P> getValue(subPropName);
				}
			} else {
				return (P) properties.get(propName);
			}
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#setValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setValue(String propName, Object value) {
		if (getValue(propName) == null && value == null) {
			return;
		}

		if (properties == null) {
			properties = new HashMap<String, Object>();
		}

		int firstDotIndex = propName.indexOf('.');
		if (firstDotIndex > 0) {
			String topPropName = propName.substring(0, firstDotIndex);
			String subPropName = propName.substring(firstDotIndex + 1);
			Entity topEntity = (Entity) properties.get(topPropName);
			if (topEntity == null) {//TODO EntityDefinitionManagerの参照をしない（チェックしない）ほうがよいかも、、、
//				EntityDefinitionManager edm = ServiceLocator.getInstance().getEntityDefinitionManager();
//				EntityDefinition myDef = edm.get(getDefinitionName());
//				if (myDef == null) {
//					throw new EntityRuntimeException("DefinitionName is unspecified.");
//				}
//				ReferenceProperty rp = (ReferenceProperty) myDef.getProperty(topPropName);
//				if (rp == null) {
//					throw new EntityRuntimeException(topPropName + " is undefined.");
//				}
//				EntityDefinition refDef = edm.get(rp.getObjectDefinitionName());
//				if (refDef.getMapping() != null) {
//					try {
//						topEntity = (Entity) Class.forName(refDef.getMapping().getMappingModelClass()).newInstance();
//					} catch (InstantiationException e) {
//						throw new EntityRuntimeException(e);
//					} catch (IllegalAccessException e) {
//						throw new EntityRuntimeException(e);
//					} catch (ClassNotFoundException e) {
//						throw new EntityRuntimeException(e);
//					}
//				} else {
//					topEntity = new GenericEntity();
//				}
//				topEntity.setDefinitionName(rp.getObjectDefinitionName());
//				properties.put(topPropName, topEntity);
//
//				//TODO 循環参照（相互参照）対応するか？？
//				//TODO 配列対応するか？？

			}
			topEntity.setValue(subPropName, value);

		} else {
			if (value == null) {
				properties.remove(propName);
			} else {
				properties.put(propName, value);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#getOid()
	 */
	@Override
	public String getOid() {
		return (String) getValue(Entity.OID);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#setOid(long)
	 */
	@Override
	public void setOid(String oid) {
		setValue(Entity.OID, oid);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#getName()
	 */
	@Override
	public String getName() {
		return (String) getValue(Entity.NAME);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		setValue(Entity.NAME, name);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#getCreateDate()
	 */
	@Override
	public Timestamp getCreateDate() {
		return (Timestamp) getValue(Entity.CREATE_DATE);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#setCreateDate(java.util.Date)
	 */
	@Override
	public void setCreateDate(Timestamp createDate) {
		setValue(Entity.CREATE_DATE, createDate);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#getUpdateDate()
	 */
	@Override
	public Timestamp getUpdateDate() {
		return (Timestamp) getValue(Entity.UPDATE_DATE);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#setUpdateDate(java.util.Date)
	 */
	@Override
	public void setUpdateDate(Timestamp updateDate) {
		setValue(Entity.UPDATE_DATE, updateDate);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#getCreateBy()
	 */
	@Override
	public String getCreateBy() {
		return (String) getValue(Entity.CREATE_BY);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#setCreateBy(long)
	 */
	@Override
	public void setCreateBy(String createBy) {
		setValue(Entity.CREATE_BY, createBy);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#getUpdateBy()
	 */
	@Override
	public String getUpdateBy() {
		return (String) getValue(Entity.UPDATE_BY);
	}

	/* (non-Javadoc)
	 * @see org.iplass.mtp.datamodel.Entity#setUpdateBy(long)
	 */
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

//	/**
//	 * shallow copyを行う。
//	 *
//	 * @return
//	 */
//	public GenericEntity copy() {
//		GenericEntity copy = null;
//		if (this.getClass() == GenericEntity.class) {
//			copy = new GenericEntity(definitionName);
//		} else {
//			try {
//				copy = (GenericEntity) getClass().newInstance();
//				copy.setDefinitionName(definitionName);
//			} catch (InstantiationException e) {
//				throw new EntityRuntimeException(e);
//			} catch (IllegalAccessException e) {
//				throw new EntityRuntimeException(e);
//			}
//		}
//		if (properties != null) {
//			copy.properties = new HashMap<String, Object>(properties);
//		}
//		return copy;
//	}
//
//	/**
//	 * 引数の型のインスタンスとして、shallow copyを行う。
//	 *
//	 * @return
//	 */
//	public <T extends GenericEntity> T copyAs(Class<T> type) {
//		try {
//			T copy = type.newInstance();
//			copy.setDefinitionName(definitionName);
//			if (properties != null) {
//				((GenericEntity) copy).properties = new HashMap<String, Object>(properties);
//			}
//			return copy;
//		} catch (InstantiationException e) {
//			throw new EntityRuntimeException(e);
//		} catch (IllegalAccessException e) {
//			throw new EntityRuntimeException(e);
//		}
//	}
//
//	/**
//	 * deep copyを行う（但し、Entityとしてのdeep copy）。
//	 * property値が、GenericEntityの場合は、deepCopy()を再帰呼び出し、
//	 * BinaryReference、SelectValueの場合は、copy()を呼び出し、
//	 * java.uti.Dateの場合は、clone()を呼び出し、
//	 * それ以外（プリミティブ型、immutable）の場合は、参照をそのまま保持。
//	 *
//	 * @return
//	 */
//	public GenericEntity deepCopy() {
//		Map<GenericEntity, GenericEntity> done = new HashMap<GenericEntity, GenericEntity>();
//		return copyInternal(done);
//	}
//
//	private GenericEntity copyInternal(Map<GenericEntity, GenericEntity> done) {
//		GenericEntity alreadyCopy = done.get(this);
//		if (alreadyCopy != null) {
//			return alreadyCopy;
//		}
//		GenericEntity copy = null;
//		if (this.getClass() == GenericEntity.class) {
//			copy = new GenericEntity(definitionName);
//		} else {
//			try {
//				copy = (GenericEntity) getClass().newInstance();
//				copy.setDefinitionName(definitionName);
//			} catch (InstantiationException e) {
//				throw new EntityRuntimeException(e);
//			} catch (IllegalAccessException e) {
//				throw new EntityRuntimeException(e);
//			}
//		}
//		done.put(this, copy);
//		if (properties != null) {
//			copy.properties = new HashMap<String, Object>();
//			for (Map.Entry<String, Object> e: properties.entrySet()) {
//				Object val = e.getValue();
//				if (val instanceof Object[]) {
//					Object[] valArray = (Object[]) val;
//					Object[] newValArray = (Object[]) Array.newInstance(valArray.getClass().getComponentType(), valArray.length);
//					for (int i = 0; i < valArray.length; i++) {
//						newValArray[i] = copyVal(valArray[i], done);
//					}
//					copy.properties.put(e.getKey(), newValArray);
//				} else {
//					copy.properties.put(e.getKey(), copyVal(val, done));
//				}
//			}
//		}
//
//		return copy;
//
//	}
//
//	private Object copyVal(Object val, Map<GenericEntity, GenericEntity> done) {
//		if (val instanceof GenericEntity) {
//			return ((GenericEntity) val).copyInternal(done);
//		}
//		if (val instanceof SelectValue) {
//			return ((SelectValue) val).copy();
//		}
//		if (val instanceof BinaryReference) {
//			return ((BinaryReference) val).copy();
//		}
//		if (val instanceof Date) {
//			return ((Date) val).clone();
//		}
//		return val;
//
//	}

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
//	/**
//	 * JSON形式の送受信にpropertiesを含めるためのメソッド(Jacksonへの対応)。
//	 * 利用箇所はなし。
//	 *
//	 * org.codehaus.jackson.annotate.JsonProperty
//	 * を指定することでgetter、setterは不要になるが、
//	 * Entityとしてはjacksonに依存したくないため、
//	 * privateメソッドとして定義。
//	 *
//	 * @return
//	 */
//	@SuppressWarnings("unused")
//	private Map<String, Object> getProperties() {
//		if (properties == null) {
//			properties = new HashMap<String, Object>();
//		}
//		return properties;
//	}

}
