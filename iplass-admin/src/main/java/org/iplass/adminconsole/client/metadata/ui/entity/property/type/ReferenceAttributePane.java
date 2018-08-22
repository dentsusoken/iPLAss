/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.entity.EntityDS;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.type.SortInfoListPane.SortInfoListPaneHandler;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceType;
import org.iplass.mtp.entity.definition.properties.VersionControlReferenceType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class ReferenceAttributePane extends VLayout implements PropertyAttributePane {

	private DynamicForm form = new DynamicForm();

	/** 参照先Entity */
	private SelectItem selReferenceName;
	/** 参照タイプ */
	private SelectItem selReferenceType;
	/** 被参照マッピングプロパティ */
	private SelectItem selMappedByProperty;
	/** 非参照AuditLog出力 */
	private CheckboxItem chkAuditLogMappedBy;

	private DynamicForm formVersionControl = new DynamicForm();

	/** バージョン管理方法 */
	private SelectItem selVersionControlType;
	/** バージョン管理で特定時点を指し示すExpression（例えば、基準日を格納するプロパティ名や日時のリテラルなど） */
	private TextItem txtVersionControlExpression;

	/** Sort情報 */
	private SortInfoListPane pnlSortList;

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public ReferenceAttributePane() {

		setWidth100();
//		setHeight100();
		setAutoHeight();

		selReferenceName = new SelectItem();
		selReferenceName.setTitle(rs("ui_metadata_entity_PropertyListGrid_referenceEntity"));
		selReferenceName.setWidth(200);
		EntityDS.setDataSource(selReferenceName);
		SmartGWTUtil.setRequired(selReferenceName);
		selReferenceName.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				//mappedByはクリア
				String refDefName = SmartGWTUtil.getStringValue(selReferenceName);
				boolean refAuditLog = SmartGWTUtil.getBooleanValue(chkAuditLogMappedBy);
				mappedTargetDataInit(refDefName, null, refAuditLog);
			}
		});
		selReferenceType = new SelectItem();
		selReferenceType.setTitle(rs("ui_metadata_entity_PropertyListGrid_referenceRelation"));
		selReferenceType.setWidth(200);
		selMappedByProperty = new SelectItem();
		selMappedByProperty.setTitle(rs("ui_metadata_entity_PropertyListGrid_referencedProperty"));
		selMappedByProperty.setWidth(200);
		selMappedByProperty.setDisabled(Boolean.TRUE);
		selVersionControlType = new SelectItem();
		selVersionControlType.setTitle(rs("ui_metadata_entity_PropertyListGrid_versionControle"));
		selVersionControlType.setWidth(200);
		selVersionControlType.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if (selVersionControlType.getValue() != null && selVersionControlType.getValue().equals(VersionControlReferenceType.AS_OF_EXPRESSION_BASE.name())) {
					txtVersionControlExpression.setDisabled(false);
				} else {
					txtVersionControlExpression.setDisabled(true);
				}
			}
		});
		txtVersionControlExpression = new TextItem();
		txtVersionControlExpression.setTitle(rs("ui_metadata_entity_PropertyListGrid_versionControlAsExpression"));
		txtVersionControlExpression.setWidth(200);
		txtVersionControlExpression.setDisabled(true);
		SmartGWTUtil.addHoverToFormItem(txtVersionControlExpression, rs("ui_metadata_entity_PropertyListGrid_versionControlAsExpressionComment"));
		chkAuditLogMappedBy = new CheckboxItem();
		chkAuditLogMappedBy.setTitle(rs("ui_metadata_entity_PropertyListGrid_operationHistorySave"));

		form.setMargin(5);
		form.setNumCols(6);
		form.setColWidths(100, 200, 60, 200, 100, "*");
		form.setWidth100();
		form.setHeight(60);
		form.setItems(selReferenceName, selReferenceType, selMappedByProperty
				, new SpacerItem(), new SpacerItem(), new SpacerItem(), new SpacerItem(), chkAuditLogMappedBy);

		formVersionControl.setPadding(5);
		formVersionControl.setNumCols(6);
		formVersionControl.setColWidths(100, 200, 60, 200, 100, "*");
		formVersionControl.setWidth100();
		formVersionControl.setHeight(30);
		formVersionControl.setGroupTitle(rs("ui_metadata_entity_PropertyListGrid_referenceVersion"));
		formVersionControl.setIsGroup(true);
		formVersionControl.setItems(selVersionControlType, txtVersionControlExpression, new SpacerItem(), new SpacerItem());

		pnlSortList = new SortInfoListPane(new SortInfoListPaneHandler() {

			@Override
			public String referenceName() {
				return SmartGWTUtil.getStringValue(selReferenceName);
			}
		});

		addMember(form);
		addMember(formVersionControl);
		addMember(pnlSortList);

		initialize();
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		ReferenceAttribute referenceAttribute = (ReferenceAttribute)typeAttribute;

		if (referenceAttribute.getObjectDefinitionName() != null) {
			selReferenceName.setValue(referenceAttribute.getObjectDefinitionName());
			//MappedByが設定されているかに関わらずチェック
			//（初期未設定で登録時に再度開くと有効にならないため）
			mappedTargetDataInit(referenceAttribute.getObjectDefinitionName(), referenceAttribute.getMappedBy(), referenceAttribute.isAuditLogMappedBy());
		}
		if (referenceAttribute.getReferenceType() != null) {
			selReferenceType.setValue(referenceAttribute.getReferenceType().name());
		}
		if (referenceAttribute.getVersionControlType() != null) {
			selVersionControlType.setValue(referenceAttribute.getVersionControlType().name());
		} else {
			selVersionControlType.setValue("");
		}
		if (referenceAttribute.getVersionControlAsOfExpression() != null) {
			txtVersionControlExpression.setValue(referenceAttribute.getVersionControlAsOfExpression());
		}

		txtVersionControlExpression.setDisabled(!VersionControlReferenceType.AS_OF_EXPRESSION_BASE.name().equals(selVersionControlType.getValueAsString()));

		pnlSortList.applyFrom(record, referenceAttribute);

	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		ReferenceAttribute referenceAttribute = (ReferenceAttribute)record.getTypeAttribute();

		ListGridRecord refRecord = selReferenceName.getSelectedRecord();
		if (refRecord != null) {
			referenceAttribute.setObjectDefinitionName(refRecord.getAttributeAsString(DataSourceConstants.FIELD_NAME));
		}
		if (selReferenceType.getValue() != null) {
			referenceAttribute.setReferenceType(ReferenceType.valueOf(SmartGWTUtil.getStringValue(selReferenceType)));
		}
		if (selMappedByProperty.getValue() != null) {
			referenceAttribute.setMappedBy(SmartGWTUtil.getStringValue(selMappedByProperty));
			referenceAttribute.setAuditLogMappedBy(SmartGWTUtil.getBooleanValue(chkAuditLogMappedBy));
		}
		if (selVersionControlType.getValue() != null) {
			String versionControlType = SmartGWTUtil.getStringValue(selVersionControlType);
			if (!versionControlType.isEmpty()) {
				referenceAttribute.setVersionControlType(VersionControlReferenceType.valueOf(versionControlType));
			} else {
				referenceAttribute.setVersionControlType(null);
			}
		}
		if (txtVersionControlExpression.getValue() != null) {
			referenceAttribute.setVersionControlAsOfExpression(SmartGWTUtil.getStringValue(txtVersionControlExpression));
		}

		pnlSortList.applyTo(referenceAttribute);
	}

	@Override
	public boolean validate() {

		boolean isValidate = true;
		//共通Formチェック
		if (!form.validate()) {
			isValidate = false;
		}
		if (!formVersionControl.validate()) {
			isValidate = false;
		}

		return isValidate;
	}

	@Override
	public int panelHeight() {
		return 300;
	}

	private void initialize() {

		LinkedHashMap<String, String> refTypeMap = new LinkedHashMap<String, String>();
		refTypeMap.put(ReferenceType.ASSOCIATION.name(), rs("ui_metadata_entity_PropertyListGrid_normalReference"));
		refTypeMap.put(ReferenceType.COMPOSITION.name(), rs("ui_metadata_entity_PropertyListGrid_parentChildRelation"));
		selReferenceType.setValueMap(refTypeMap);

		LinkedHashMap<String, String> verControlTypeMap = new LinkedHashMap<String, String>();
		verControlTypeMap.put("", "");
		verControlTypeMap.put(VersionControlReferenceType.RECORD_BASE.name(), rs("ui_metadata_entity_PropertyListGrid_versionAtTimeOfPres"));
		verControlTypeMap.put(VersionControlReferenceType.CURRENT_BASE.name(), rs("ui_metadata_entity_PropertyListGrid_vesionLatest"));
		verControlTypeMap.put(VersionControlReferenceType.AS_OF_EXPRESSION_BASE.name(), rs("ui_metadata_entity_PropertyListGrid_vesionSpecific"));
		selVersionControlType.setValueMap(verControlTypeMap);
	}

	private void mappedTargetDataInit(String refDefName, final String refMappedBy, final boolean refAuditLog) {

		service.getEntityDefinition(TenantInfoHolder.getId(), refDefName, new AdminAsyncCallback<EntityDefinition>() {
			public void onSuccess(EntityDefinition result) {
				LinkedHashMap<String, String> mappingPropertyMap = new LinkedHashMap<String, String>();
				selMappedByProperty.setValue("");
				selMappedByProperty.setDisabled(Boolean.TRUE);
				chkAuditLogMappedBy.setValue(false);
				chkAuditLogMappedBy.setDisabled(Boolean.TRUE);

				// Propertyを設定
				List<PropertyDefinition> lstProp = result.getDeclaredPropertyList();

				if (lstProp == null) { return; }

				//TODO DataSource化

				//任意選択のため、先頭に空白Itemを追加
				mappingPropertyMap.put("", "");

				for (PropertyDefinition prop : lstProp) {
					if (prop instanceof ReferenceProperty) {
						ReferenceProperty refProp = (ReferenceProperty)prop;
						if (refProp.getDisplayName() != null && !refProp.getDisplayName().isEmpty()) {
							if (refProp.getDisplayName().equals(refProp.getName())) {
								mappingPropertyMap.put(refProp.getName(), refProp.getName());
							} else {
								mappingPropertyMap.put(refProp.getName(),
										refProp.getName() + "(" + refProp.getDisplayName() + ")");
							}
						} else {
							mappingPropertyMap.put(refProp.getName(), refProp.getName());
						}
					}
				}

				//空白を考慮し2件以上の場合、Itemセット
				if (mappingPropertyMap.size() > 1) {
					selMappedByProperty.setValueMap(mappingPropertyMap);
					selMappedByProperty.setDisabled(Boolean.FALSE);
					chkAuditLogMappedBy.setDisabled(Boolean.FALSE);

					if (SmartGWTUtil.isNotEmpty(refMappedBy)) {
						selMappedByProperty.setValue(refMappedBy);
					}
					chkAuditLogMappedBy.setValue(refAuditLog);
				}
			}

		});
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}

}
