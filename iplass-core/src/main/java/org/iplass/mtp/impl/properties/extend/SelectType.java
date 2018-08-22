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

import java.util.List;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.properties.basic.StringType;
import org.iplass.mtp.impl.properties.extend.select.SelectValueService;
import org.iplass.mtp.impl.properties.extend.select.Value;
import org.iplass.mtp.impl.properties.extend.select.MetaSelectValue.SelectValueRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;


public class SelectType extends WrapperType {
	private static final long serialVersionUID = 9042652773091106209L;

	private static StringType actualType = new StringType();
	
	private static SelectValueService svService = ServiceRegistry.getRegistry().getService(SelectValueService.class);

	private List<Value> values;
	
	private String selectValueMetaDataId;

	public String getSelectValueMetaDataId() {
		return selectValueMetaDataId;
	}

	public void setSelectValueMetaDataId(String selectValueMetaDataId) {
		this.selectValueMetaDataId = selectValueMetaDataId;
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}
	
	public List<Value> runtimeValues() {
		if (selectValueMetaDataId != null) {
			SelectValueRuntime r = svService.getRuntimeById(selectValueMetaDataId);
			if (r == null) {
				return null;
			}
			return r.getMetaData().getValues();
		} else {
			return values;
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((selectValueMetaDataId == null) ? 0 : selectValueMetaDataId
						.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		SelectType other = (SelectType) obj;
		if (selectValueMetaDataId == null) {
			if (other.selectValueMetaDataId != null)
				return false;
		} else if (!selectValueMetaDataId.equals(other.selectValueMetaDataId))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	@Override
	public SelectType copy() {

		return ObjectUtil.deepCopy(this);

//		SelectType copy = new SelectType();
//		if (values != null) {
//			copy.values = new ArrayList<Value>();
//			for (Value v: values) {
//				copy.values.add(new Value(v.value, v.displayName));
//			}
//		}
//		return copy;
	}

	@Override
	public SelectProperty createPropertyDefinitionInstance() {
		SelectProperty def = new SelectProperty();
		
		if (selectValueMetaDataId != null) {
			SelectValueRuntime r = svService.getRuntimeById(selectValueMetaDataId);
			if (r != null) {
				//grobal設定を適用
				def.setSelectValueDefinitionName(r.getMetaData().getName());
				SelectValueDefinition svd = Value.toSelectValueDefinition(r.getMetaData().getValues());
				def.setSelectValueList(svd.getSelectValueList());
				def.setLocalizedSelectValueList(svd.getLocalizedSelectValueList());
			}
		} else {
			//local設定
			SelectValueDefinition svd = Value.toSelectValueDefinition(values);
			def.setSelectValueList(svd.getSelectValueList());
			def.setLocalizedSelectValueList(svd.getLocalizedSelectValueList());
		}
		
		return def;
	}
	
	@Override
	public void applyDefinition(PropertyDefinition def) {
		super.applyDefinition(def);
		
		SelectProperty spd = (SelectProperty) def;
		
		if (spd.getSelectValueDefinitionName() != null) {
			SelectValueRuntime r = svService.getRuntimeByName(spd.getSelectValueDefinitionName());
			if (r == null) {
				throw new EntityRuntimeException("SelectValue definition:" + spd.getSelectValueDefinitionName() + " is undefined.");
			} else {
				selectValueMetaDataId = r.getMetaData().getId();
			}
			values = null;
		} else {
			selectValueMetaDataId = null;
			SelectValueDefinition svd = new SelectValueDefinition();
			svd.setSelectValueList(spd.getSelectValueList());
			svd.setLocalizedSelectValueList(spd.getLocalizedSelectValueList());
			values = Value.toValues(svd);
		}
	}
	

	@Override
	public Object createRuntime(
			MetaProperty metaProperty, MetaEntity metaEntity) {
		return null;
	}

	@Override
	public Class<?> storeType() {
		return SelectValue.class;
	}

	@Override
	public PropertyType actualType() {
		return actualType;
	}
	
	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.SELECT;
	}
	
	@Override
	public PropertyDefinitionType getDataStoreEnumType() {
		return PropertyDefinitionType.STRING;
	}

	//FIXME VirtualTypeにしてtoVirtualTypeValueを利用
	@Override
	public Object fromDataStore(Object fromDataStore) {
		if (fromDataStore == null) {
			return null;
		}

		String value = (String) fromDataStore;
		List<Value> runtimeValues = runtimeValues();
		if (runtimeValues != null) {
			for (Value v: runtimeValues) {
				if (v.getValue().equals(value)) {
					//多言語対応
					String dispName = I18nUtil.stringMeta(v.getDisplayName(), v.getLocalizedDisplayNameList());
					SelectValue select = new SelectValue(v.getValue(), dispName);
					return select;
				}
			}
		}

		//定義されていないvalueの場合はnull
		return null;
	}

	@Override
	public Object toDataStore(Object toDataStore) {
		if (toDataStore == null) {
			return null;
		}
		
		//Stringが指定されても動作するように。
		String value = null;
		if (toDataStore instanceof String) {
			value = (String) toDataStore;
		} else {
			value = ((SelectValue) toDataStore).getValue();
		}
		List<Value> runtimeValues = runtimeValues();
		if (runtimeValues != null) {
			for (Value v: runtimeValues) {
				if (v.getValue().equals(value)) {
					return value;
				}
			}
		}

		throw new EntityRuntimeException("SelectValue:" + value + " not defined.");
	}

	@Override
	public boolean isCompatibleTo(PropertyType another) {
		if (another instanceof SelectType) {
			return true;
		}

		return super.isCompatibleTo(another);
	}

	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return (String) value;
		} else {
			return ((SelectValue) value).getValue();
		}
	}

	@Override
	public Object fromString(String strValue) {
		if (strValue == null) {
			return null;
		}
		List<Value> runtimeValues = runtimeValues();
		if (runtimeValues != null) {
			for (Value v: runtimeValues) {
				if (v.getValue().equals(strValue)) {
					//多言語対応
					String dispName = I18nUtil.stringMeta(v.getDisplayName(), v.getLocalizedDisplayNameList());
					SelectValue select = new SelectValue(v.getValue(), dispName);
					return select;
				}
			}
		}

		return null;
	}



}
