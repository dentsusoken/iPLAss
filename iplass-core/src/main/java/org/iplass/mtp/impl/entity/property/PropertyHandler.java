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
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.impl.datastore.PropertyStoreHandler;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.validation.MetaValidation;
import org.iplass.mtp.impl.validation.ValidationContext;
import org.iplass.mtp.impl.validation.ValidationHandler;
import org.iplass.mtp.spi.ServiceRegistry;


public abstract class PropertyHandler /* implements MetaDataRuntime*/ {

	protected MetaProperty metaData;
	private List<ValidationHandler> validators;
	private EntityHandler parent;
	private PropertyStoreHandler entityStorePropertyHandler;

	/** 多言語化用文字情報リスト */
	private Map<String, String> localizedStringMap;

	public PropertyHandler(MetaProperty metaproperty, MetaEntity metaEntity) {
		localizedStringMap = new HashMap<String, String>();
		this.metaData = metaproperty;
		List<MetaValidation> vDefs = metaproperty.getValidations();
		if (vDefs != null) {
			validators = new ArrayList<ValidationHandler>();
			for (MetaValidation vDef: vDefs) {
				validators.add(vDef.createRuntime(metaEntity, metaproperty));
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

	public ValidateError validate(Entity model) {
		ValidationContext context = new ValidationContext(model, metaData.getName());

		if (validators != null) {
			String entityDispName = null;

			for (ValidationHandler v: validators) {
				Object value = model.getValue(metaData.getName());
				if (getMetaData().getMultiplicity() != 1
						&& value != null
						&& value.getClass().isArray()) {
					if (!v.validateArray((Object[]) value, context)) {
						ValidateError res = new ValidateError();
						res.setPropertyName(metaData.getName());
						res.setPropertyDisplayName(getLocalizedDisplayName());
						if (entityDispName == null) {
							entityDispName = getEntityDispName(model);
						}
						res.addErrorMessage(v.generateErrorMessage(value, context, getDisplayName(), entityDispName), v.getErrorCode());
						return res;
					}
				} else {
					if (!v.validate(value, context)) {
						ValidateError res = new ValidateError();
						res.setPropertyName(metaData.getName());
						res.setPropertyDisplayName(getLocalizedDisplayName());
						if (entityDispName == null) {
							entityDispName = getEntityDispName(model);
						}
						res.addErrorMessage(v.generateErrorMessage(value, context,getDisplayName(), entityDispName), v.getErrorCode());
						return res;
					}
				}
			}
		}

		return null;
	}

	private String getEntityDispName(Entity model) {

		String defName = model.getDefinitionName();
		EntityHandler eh = ServiceRegistry.getRegistry().getService(EntityService.class).getRuntimeByName(defName);
		String dispName = null;
		if (eh != null) {
			dispName = eh.getMetaData().getDisplayName();
		}
		if (dispName == null) {
			dispName = defName;
		}
		return dispName;
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
