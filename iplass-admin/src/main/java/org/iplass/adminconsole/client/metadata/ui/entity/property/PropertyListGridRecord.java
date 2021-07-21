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

package org.iplass.adminconsole.client.metadata.ui.entity.property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.CommonAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributeController;
import org.iplass.adminconsole.client.metadata.ui.entity.property.type.PropertyTypeAttributeController;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.validations.NotNullValidation;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PropertyListGridRecord extends ListGridRecord {

	private static final String INSERT = "I";
	private static final String UPDATE = "U";
	private static final String DELETE = "D";


	/** 登録状態 */
	public static final String STATUS = "status";

	/** Custom OIDかどうか(Boolean) */
	public static final String CUSTOM_OID = "customOid";

	/** Custom Nameかどうか(Boolean) */
	public static final String CUSTOM_NAME = "customName";

	/** Crawl対象かどうか(Boolean) */
	public static final String CRAWL_PROP = "crawlProp";

	/** 名前 */
	public static final String NAME = "name";

	/** 表示名 */
	public static final String DISPNAME = "dispName";

	/** Property型(PropertyType)(Grid表示用) */
	public static final String TYPE_DISP_VAL = "typeDispValue";

	/** 多重度(Grid表示用) */
	public static final String MULTIPLE_DISP_VAL = "multiplicityDispValue";

	/** 必須設定(Grid表示用) */
	public static final String NOT_NULL_DISP_VAL = "notNullDispValue";

	/** 変更可能設定(Grid表示用) */
	public static final String UPDATABLE_DISP_VAL = "updatableDispValue";

	/** Indexタイプ(Grid表示用) */
	public static final String INDEXTYPE_DISP_VAL = "indexTypeDispValue";

	/** 保存時のマッピング列名 */
	public static final String STORE_COLUMN_MAPPING_NAME = "storeColumnName";

	/** 備考(Grid表示用) */
	public static final String REMARKS = "remarks";

	/** ReferencePropery、SelectProperty(Global)のリンク表示 */
	public static final String SHOW_ICON = "showIcon";

	/** 保存されているProperty名(登録済データのみ、名前変更用) */
	private static final String SAVED_NAME = "savedName";

	/** 共通属性 */
	private CommonAttribute commonAttribute;

	/** タイプ別属性 */
	private PropertyAttribute typeAttribute;

	/** Validation */
	private List<ValidationDefinition> validationList;

	/** Normalizer */
	private List<NormalizerDefinition> normalizerList;

	private PropertyTypeAttributeController typeController = GWT.create(PropertyTypeAttributeController.class);
	private PropertyCommonAttributeController commonController = GWT.create(PropertyCommonAttributeController.class);

	public PropertyListGridRecord() {

		setSavedName(null);
		setStatusInsert();

		commonAttribute = commonController.createCommonAttribute();
	}

	public String getStatus() {
		String status = getAttribute(STATUS);
		return status == null ? "" : status;
	}

	public boolean isInsert() {
		return INSERT.equals(getStatus());
	}

	public boolean isUpdate() {
		return UPDATE.equals(getStatus());
	}

	public boolean isDelete() {
		return DELETE.equals(getStatus());
	}

	public void setStatus(String status) {
		setAttribute(STATUS, status);
	}

	public void setStatusInsert() {
		setStatus(INSERT);
	}

	public void setStatusUpdate() {
		setStatus(UPDATE);
	}

	public void setStatusDelete() {
		setStatus(DELETE);
	}

	public boolean isCustomOid() {
		return getAttributeAsBoolean(CUSTOM_OID);
	}

	public void setCustomOid(Boolean value) {
		setAttribute(CUSTOM_OID, value);
	}

	public boolean isCustomName() {
		return getAttributeAsBoolean(CUSTOM_NAME);
	}

	public void setCustomName(Boolean value) {
		setAttribute(CUSTOM_NAME, value);
	}

	public boolean isCrawlProp() {
		return getAttributeAsBoolean(CRAWL_PROP);
	}

	public void setCrawlProp(Boolean value) {
		setAttribute(CRAWL_PROP, value);
	}

	public String getRecordName() {
		return getAttribute(NAME);
	}

	public void setRecordName(String value) {
		setAttribute(NAME, value);
	}

	public String getRecordDispName() {
		return getAttribute(DISPNAME);
	}

	public void setRecordDispName(String value) {
		setAttribute(DISPNAME, value);
	}

	public String getRecordTypeDispValue() {
		return getAttribute(TYPE_DISP_VAL);
	}

	public void setRecordTypeDispValue(String value) {
		setAttribute(TYPE_DISP_VAL, value);
	}

	public String getRecordMultipleDispValue() {
		return getAttribute(MULTIPLE_DISP_VAL);
	}

	public void setRecordMultipleDispValue(String value) {
		setAttribute(MULTIPLE_DISP_VAL, value);
	}

	public String getRecordNotNullDispValue() {
		return getAttribute(NOT_NULL_DISP_VAL);
	}

	public void setRecordNotNullDispValue(String value) {
		setAttribute(NOT_NULL_DISP_VAL, value);
	}

	public String getRecordUpdatableDispValue() {
		return getAttribute(UPDATABLE_DISP_VAL);
	}

	public void setRecordUpdatableDispValue(String value) {
		setAttribute(UPDATABLE_DISP_VAL, value);
	}

	public String getRecordIndexTypeDispValue() {
		return getAttribute(INDEXTYPE_DISP_VAL);
	}

	public void setRecordIndexTypeDispValue(String value) {
		setAttribute(INDEXTYPE_DISP_VAL, value);
	}

	public String getRecordStoreColumnMappingName() {
		return getAttribute(STORE_COLUMN_MAPPING_NAME);
	}

	public void setRecordStoreColumnMappingName(String value) {
		setAttribute(STORE_COLUMN_MAPPING_NAME, value);
	}

	public String getRemarks() {
		return getAttribute(REMARKS);
	}

	public void setRemarks(String value) {
		setAttribute(REMARKS, value);
	}

	public String getSavedName() {
		return getAttribute(SAVED_NAME);
	}

	public void setSavedName(String value) {
		setAttribute(SAVED_NAME, value);
	}

	public PropertyAttribute getCommonAttribute() {
		return commonAttribute;
	}

	public PropertyAttribute getTypeAttribute() {
		return typeAttribute;
	}

	public void setTypeAttribute(PropertyAttribute typeAttribute) {
		this.typeAttribute = typeAttribute;
	}

	public List<ValidationDefinition> getValidationList() {
		if (validationList == null) validationList = new ArrayList<>();
		return validationList;
	}

	public void setValidationList(List<ValidationDefinition> validationList) {
		this.validationList = validationList;
	}

	public List<NormalizerDefinition> getNormalizerList() {
		if (normalizerList == null) normalizerList = new ArrayList<>();
		return normalizerList;
	}

	public void setNormalizerList(List<NormalizerDefinition> normalizerList) {
		this.normalizerList = normalizerList;
	}

	public boolean isInherited() {
		return commonAttribute.isInherited();
	}

	public boolean isUpdatable() {
		return commonAttribute.isUpdatable();
	}

	public PropertyDefinitionType getRecordType() {
		return commonAttribute.getType();
	}

	public void applyFrom(PropertyDefinition property, EntityDefinition entity, Map<String, String> colMap) {

		setSavedName(property.getName());
		setStatus(null);

		if (entity.getOidPropertyName() != null
				&& entity.getOidPropertyName().contains(property.getName())) {
			setCustomOid(true);
		}
		if (Entity.OID.equals(property.getName()) && entity.getOidPropertyName() == null) {
			setCustomOid(true);
		}
		if (entity.getNamePropertyName() != null
				&& entity.getNamePropertyName().equals(property.getName())) {
			setCustomName(true);
		}
		if (Entity.NAME.equals(property.getName()) && entity.getNamePropertyName() == null) {
			setCustomName(true);
		}
		if (entity.getCrawlPropertyName() != null
				&& entity.getCrawlPropertyName().contains(property.getName())) {
			setCrawlProp(true);
		}

		//共通属性を反映
		commonAttribute.applyFrom(property, entity);
		//カラムマッピングは別で設定
		if (colMap != null && colMap.containsKey(property.getName())) {
			commonAttribute.setStoreColumnMappingName(colMap.get(property.getName()));
		}

		//個別属性を反映
		PropertyAttribute typeAttribute = typeController.createTypeAttribute(property.getType());
		setTypeAttribute(typeAttribute);
		typeAttribute.applyFrom(property, entity);

		//Validation
		List<ValidationDefinition> list = new ArrayList<>();
		if (property.getValidations() != null) {
			for (ValidationDefinition vd : property.getValidations()) {
				if (vd instanceof NotNullValidation) {
					commonAttribute.setNotNull(true);
				}
				list.add(vd);
			}
		}

		commonAttribute.applyTo(this);
		typeAttribute.applyTo(this);

		setValidationList(list);

		setNormalizerList(property.getNormalizers());
	}

	public void applyTo(PropertyDefinition property, EntityDefinition entity) {

		//共通属性を反映
		commonAttribute.applyTo(property, entity);

		//個別属性を反映
		typeAttribute.applyTo(property, entity);

		//Validation
		List<ValidationDefinition> validationList = getValidationList();
		if (SmartGWTUtil.isNotEmpty(validationList)) {
			property.setValidations(validationList);
		}

		//Normalizer
		List<NormalizerDefinition> normalizerList = getNormalizerList();
		if (SmartGWTUtil.isNotEmpty(normalizerList)) {
			property.setNormalizers(normalizerList);
		}
	}

}
