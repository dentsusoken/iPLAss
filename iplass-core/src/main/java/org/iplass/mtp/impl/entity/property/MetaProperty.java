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

package org.iplass.mtp.impl.entity.property;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.impl.datastore.MetaPropertyStore;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.validation.MetaValidation;
import org.iplass.mtp.impl.validation.ValidationService;
import org.iplass.mtp.spi.ServiceRegistry;


@XmlSeeAlso({MetaPrimitiveProperty.class, MetaReferenceProperty.class})
public abstract class MetaProperty implements MetaData {
	private static final long serialVersionUID = 2022181816499023008L;

	private String id;

	/** プロパティ名 */
	private String name;

	/** 表示名 */
	private String displayName;

	private String description;

	/** デフォルト値 */
	private String defaultValue;
	//TODO デフォルト値の機能の実装。Scriptか？

	/** 変更可能項目か否か */
	private boolean updatable = true;

	/** 多重度 */
	private int multiplicity = 1;//Default:1

	private List<MetaValidation> validations = new ArrayList<MetaValidation>();

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedDisplayNameList = new ArrayList<MetaLocalizedString>();

	private IndexType indexType;

	/** ストア依存のプロパティに紐付く設定 */
	private MetaPropertyStore entityStoreProperty;

	public MetaPropertyStore getEntityStoreProperty() {
		return entityStoreProperty;
	}

	public void setEntityStoreProperty(
			MetaPropertyStore entityStoreProperty) {
		this.entityStoreProperty = entityStoreProperty;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(int multiplicity) {
		this.multiplicity = multiplicity;
	}

	public IndexType getIndexType() {
		return indexType;
	}

	public void setIndexType(IndexType indexType) {
		this.indexType = indexType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

//	public boolean isMultiple() {
//		return multiple;
//	}
//
//	public void setMultiple(boolean multiple) {
//		this.multiple = multiple;
//	}

	public List<MetaValidation> getValidations() {
		return validations;
	}

	public void setValidations(List<MetaValidation> validations) {
		if (validations == null) {
			new Exception("call setValidations to null").printStackTrace();
		}

		if (validations != null && validations.size() == 0) {
			new Exception("call setValidations").printStackTrace();
		}
		this.validations = validations;
	}

	public List<MetaLocalizedString> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	public void setLocalizedDisplayNameList(List<MetaLocalizedString> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	public abstract void applyConfig(PropertyDefinition pDef, EntityContext context);

	protected void fillFrom(PropertyDefinition pDef, EntityContext context) {

		name = pDef.getName();

		if (pDef.getDisplayName() != null && !pDef.getDisplayName().equals(pDef.getName())) {
			displayName = pDef.getDisplayName();
		} else {
			displayName = null;
		}

		multiplicity = pDef.getMultiplicity();
		description = pDef.getDescription();
		defaultValue = pDef.getDefaultValue();
		updatable = pDef.isUpdatable();

		indexType = pDef.getIndexType();

		ValidationService vService = ServiceRegistry.getRegistry().getService(ValidationService.class);
		validations = new ArrayList<MetaValidation>();
		if (pDef.getValidations() != null) {
			for (ValidationDefinition v: pDef.getValidations()) {
				validations.add(vService.createValidationMetaData(v));
			}
		}

		// 言語毎の文字情報設定
		localizedDisplayNameList = I18nUtil.toMeta(pDef.getLocalizedDisplayNameList());

//		entityStoreProperty = null;//TODO 残しておくべきか。。
	}



	public abstract PropertyDefinition currentConfig(EntityContext context);

	protected void fillTo(PropertyDefinition pd, EntityContext context) {
		pd.setName(name);
		if (displayName != null) {
			pd.setDisplayName(displayName);
		} else {
			pd.setDisplayName(name);
		}
		pd.setIndexType(indexType);
		pd.setMultiplicity(multiplicity);
		pd.setDescription(description);
		pd.setDefaultValue(defaultValue);
		pd.setUpdatable(updatable);

		ArrayList<ValidationDefinition> valis = new ArrayList<ValidationDefinition>();
		if (validations != null) {
			for (MetaValidation v: validations) {
				valis.add(v.currentConfig(context));
			}
		}
		pd.setValidations(valis);

		pd.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));
	}

	public abstract MetaProperty copy();

//	protected void copyTo(MetaProperty copy){
//		copy.id = id;
//		copy.name = name;
//		copy.displayName = displayName;
//		copy.multiple = multiple;
//		if (validations != null) {
//			copy.validations = new ArrayList<MetaValidation>();
//			for (MetaValidation v: validations) {
//				copy.validations.add(v.copy());
//			}
//		}
//		copy.indexType = indexType;
//		if (entityStoreProperty != null) {
//			copy.entityStoreProperty = entityStoreProperty.copy();
//		}
//	}

	public abstract PropertyHandler createRuntime(MetaEntity metaEntity);


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime
				* result
				+ ((entityStoreProperty == null) ? 0 : entityStoreProperty
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((indexType == null) ? 0 : indexType.hashCode());
		result = prime
				* result
				+ ((localizedDisplayNameList == null) ? 0 : localizedDisplayNameList
						.hashCode());
		result = prime * result + multiplicity;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (updatable ? 1231 : 1237);
		result = prime * result
				+ ((validations == null) ? 0 : validations.hashCode());
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
		MetaProperty other = (MetaProperty) obj;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (entityStoreProperty == null) {
			if (other.entityStoreProperty != null)
				return false;
		} else if (!entityStoreProperty.equals(other.entityStoreProperty))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (indexType != other.indexType)
			return false;
		if (localizedDisplayNameList == null) {
			if (other.localizedDisplayNameList != null)
				return false;
		} else if (!localizedDisplayNameList.equals(other.localizedDisplayNameList))
			return false;
		if (multiplicity != other.multiplicity)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (updatable != other.updatable)
			return false;
		if (validations == null) {
			if (other.validations != null)
				return false;
		} else if (!validations.equals(other.validations))
			return false;
		return true;
	}

}
