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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.impl.datastore.PropertyStoreHandler;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.normalizer.MetaNormalizer;
import org.iplass.mtp.impl.entity.normalizer.MetaNormalizer.NormalizerRuntime;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.validation.MetaValidation;
import org.iplass.mtp.impl.validation.ValidationHandler;


public abstract class PropertyHandler /* implements MetaDataRuntime*/ {

	protected MetaProperty metaData;
	private List<ValidationHandler> validators;
	private List<NormalizerRuntime> normalizers;
	private EntityHandler parent;
	private PropertyStoreHandler entityStorePropertyHandler;

	/** 多言語化用文字情報リスト */
	private Map<String, String> localizedStringMap;

	public PropertyHandler(MetaProperty metaproperty, MetaEntity metaEntity) {
		localizedStringMap = new HashMap<String, String>();
		this.metaData = metaproperty;
		List<MetaValidation> vDefs = metaproperty.getValidations();
		if (vDefs != null && !vDefs.isEmpty()) {
			validators = new ArrayList<ValidationHandler>();
			for (MetaValidation vDef: vDefs) {
				validators.add(vDef.createRuntime(metaEntity, metaproperty));
			}
		}
		if (metaproperty.getNormalizers() != null && !metaproperty.getNormalizers().isEmpty()) {
			normalizers = new ArrayList<>(metaproperty.getNormalizers().size());
			for (MetaNormalizer nDef: metaproperty.getNormalizers()) {
				normalizers.add(nDef.createRuntime(metaEntity, metaproperty));
			}
		}
		if (metaproperty.getEntityStoreProperty() != null) {
			entityStorePropertyHandler = metaproperty.getEntityStoreProperty().createRuntime(this, metaEntity);
		}
		if (metaData.getLocalizedDisplayNameList() != null) {
			for (MetaLocalizedString mls : metaData.getLocalizedDisplayNameList()) {
				localizedStringMap.put(mls.getLocaleName(), mls.getStringValue());
			}
		}

	}

	public boolean isIndexed() {
		if (getMetaData().getIndexType() == IndexType.NON_UNIQUE
				|| getMetaData().getIndexType() == IndexType.UNIQUE
				|| getMetaData().getIndexType() == IndexType.UNIQUE_WITHOUT_NULL) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isUniqueIndexed() {
		if (getMetaData().getIndexType() == IndexType.UNIQUE
				|| getMetaData().getIndexType() == IndexType.UNIQUE_WITHOUT_NULL) {
			return true;
		} else {
			return false;
		}
	}

	public EntityHandler getParent() {
		return parent;
	}

	public void setParent(EntityHandler parent) {
		this.parent = parent;
	}

	public String getId() {
		if (metaData == null) {
			return null;
		}
		return metaData.getId();
	}

	public String getName() {
		return metaData.getName();
	}

	public String getDisplayName() {
		String name = metaData.getDisplayName();
		if (name == null) {
			name = metaData.getName();
		}
		return name;
	}

	public Map<String, String> getLocalizedStringMap() {
		return localizedStringMap;
	}

	public void setLocalizedStringMap(Map<String, String> localizedStringMap) {
		this.localizedStringMap = localizedStringMap;
	}
	
	public void normalize(ValidationContext context) {
		if (normalizers != null) {
			Object value = context.getEntity().getValue(metaData.getName());
			for (NormalizerRuntime n: normalizers) {
				Object valueAfter;
				if (getMetaData().getMultiplicity() != 1
						&& value != null
						&& value.getClass().isArray()) {
					valueAfter = n.normalizeArray((Object[]) value, context);
				} else {
					valueAfter = n.normalize(value, context);
				}
				if (value != valueAfter) {
					context.getEntity().setValue(metaData.getName(), valueAfter);
					value = valueAfter;
				}
			}
		}
		
	}

	public ValidateError validate(ValidationContext context) {
		
		if (validators != null) {
			String entityDispName = parent.getLocalizedDisplayName();
			String propDispName = getLocalizedDisplayName();

			for (ValidationHandler v: validators) {
				Object value = context.getEntity().getValue(metaData.getName());
				if (getMetaData().getMultiplicity() != 1
						&& value != null
						&& value.getClass().isArray()) {
					if (!v.validateArray((Object[]) value, context)) {
						ValidateError res = new ValidateError();
						res.setPropertyName(metaData.getName());
						res.setPropertyDisplayName(propDispName);
						res.addErrorMessage(v.generateErrorMessage(value, context, propDispName, entityDispName), v.getErrorCode());
						return res;
					}
				} else {
					if (!v.validate(value, context)) {
						ValidateError res = new ValidateError();
						res.setPropertyName(metaData.getName());
						res.setPropertyDisplayName(propDispName);
						res.addErrorMessage(v.generateErrorMessage(value, context, propDispName, entityDispName), v.getErrorCode());
						return res;
					}
				}
			}
		}

		return null;
	}

	public String getLocalizedDisplayName() {
		return I18nUtil.stringMeta(getDisplayName(), getMetaData().getLocalizedDisplayNameList());
	}

	public PropertyStoreHandler getStoreSpecProperty() {
		return entityStorePropertyHandler;
	}

	public abstract PropertyDefinitionType getEnumType();
	
	public abstract MetaProperty getMetaData();
	
	public abstract Object[] newArrayInstance(int size, EntityContext ec);

}
