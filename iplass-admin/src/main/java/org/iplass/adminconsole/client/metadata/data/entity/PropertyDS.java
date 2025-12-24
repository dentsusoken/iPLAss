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

package org.iplass.adminconsole.client.metadata.data.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.validations.NotNullValidation;
import org.iplass.mtp.entity.definition.validations.RangeValidation;
import org.iplass.mtp.entity.definition.validations.RegexValidation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PropertyDS extends AbstractAdminDataSource {

	private static final DataSourceField[] fields;

	static {
		DataSourceField name = new DataSourceField(
				DataSourceConstants.FIELD_NAME,
				FieldType.TEXT,
				DataSourceConstants.FIELD_NAME_TITLE);
		DataSourceField dispName = new DataSourceField(
				DataSourceConstants.FIELD_DISPLAY_NAME,
				FieldType.TEXT,
				DataSourceConstants.FIELD_DISPLAY_NAME_TITLE);
		DataSourceField javaType = new DataSourceField("javaType", FieldType.TEXT, "javaType");
		DataSourceField must = new DataSourceField("must", FieldType.BOOLEAN, "must");
		DataSourceField max = new DataSourceField("max", FieldType.TEXT, "max");
		DataSourceField min = new DataSourceField("min", FieldType.TEXT, "min");
		DataSourceField objRef = new DataSourceField("objRef", FieldType.TEXT, "objRef");

		fields = new DataSourceField[] {name, dispName, javaType, must, max, min, objRef};
	}

	public static void setDataSource(final SelectItem item, final String defName) {
		setup(item, defName, null, null);
    }
	public static void setDataSource(final SelectItem item, final String defName, final String refPropertyName) {
		setup(item, defName, refPropertyName, null);
    }
	public static void setDataSource(final ComboBoxItem item, final String defName) {
		setup(item, defName, null, null);
	}
	public static void setDataSource(final ComboBoxItem item, final String defName, final String refPropertyName) {
		setup(item, defName, refPropertyName, null);
	}
	public static void setDataSource(final ComboBoxItem item, final String defName, final String refPropertyName, String[] excludeTypes) {
		setup(item, defName, refPropertyName, excludeTypes);
    }

	private static void setup(final FormItem item, final String defName, final String refPropertyName, String[] excludeTypes) {

		item.setOptionDataSource(getInstance(defName, refPropertyName, excludeTypes));
    	item.setValueField(DataSourceConstants.FIELD_NAME);

    	ListGrid pickListProperties = new ListGrid();
    	pickListProperties.setShowFilterEditor(true);
    	if (item instanceof SelectItem) {
    		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME, DataSourceConstants.FIELD_NAME_TITLE);
    		ListGridField dispNameField = new ListGridField(DataSourceConstants.FIELD_DISPLAY_NAME, DataSourceConstants.FIELD_DISPLAY_NAME_TITLE);
    		((SelectItem)item).setPickListFields(nameField, dispNameField);
//    		((SelectItem)item).setPickListWidth(420);
    		((SelectItem)item).setPickListProperties(pickListProperties);
    	} else if (item instanceof ComboBoxItem) {
    		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME, DataSourceConstants.FIELD_NAME_TITLE);
    		ListGridField dispNameField = new ListGridField(DataSourceConstants.FIELD_DISPLAY_NAME, DataSourceConstants.FIELD_DISPLAY_NAME_TITLE);
    		((ComboBoxItem)item).setPickListFields(nameField, dispNameField);
//    		((ComboBoxItem)item).setPickListWidth(420);
    		((ComboBoxItem)item).setPickListProperties(pickListProperties);
    	}

    }

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public static PropertyDS getInstance(String defName) {
		return new PropertyDS(defName, null, null);
	}
	public static PropertyDS getInstance(String defName, String refPropertyName, String[] excludeTypes) {
		return new PropertyDS(defName, refPropertyName, excludeTypes);
	}

	/** Entity定義名 */
	private final String defName;
	/** 表示対象の参照Property定義名(ReferencePropertyのみ) */
	private final String refPropertyName;
	/** 除外するプロパティタイプのリスト */
	private final String[] excludePropertyType;

	private PropertyDS(String defName, String refPropertyName, String[] excludePropertyType) {
		this.defName = defName;
		this.refPropertyName = refPropertyName;
		this.excludePropertyType = excludePropertyType != null ? excludePropertyType : new String[] {};
		setFields(fields);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

		service.getEntityDefinition(TenantInfoHolder.getId(), defName, new AsyncCallback<EntityDefinition>() {

			@Override
			public void onFailure(Throwable caught) {
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(EntityDefinition ed) {

				if (ed == null) {
					response.setData(new ListGridRecord[0]);
					response.setTotalRows(0);
					processResponse(requestId, response);
					return;
				}

				if (refPropertyName != null) {
					//参照PropertyNameが指定されている場合は、Property定義から対象のEntityを取得
					PropertyDefinition pd = ed.getProperty(refPropertyName);
					if (pd instanceof ReferenceProperty) {
						ReferenceProperty rp = (ReferenceProperty)pd;
						String refDefName = rp.getObjectDefinitionName();
						service.getEntityDefinition(TenantInfoHolder.getId(), refDefName, new AsyncCallback<EntityDefinition>() {

							@Override
							public void onFailure(Throwable caught) {
								response.setStatus(RPCResponse.STATUS_FAILURE);
								processResponse(requestId, response);
							}

							@Override
							public void onSuccess(EntityDefinition red) {

								if (red == null) {
									response.setData(new ListGridRecord[0]);
									response.setTotalRows(0);
									processResponse(requestId, response);
									return;
								}

								response.setData(createRecord(red, null));
								response.setTotalRows(red.getPropertyList().size());
								processResponse(requestId, response);
							}
						});
					} else {
						response.setData(new ListGridRecord[0]);
						response.setTotalRows(0);
						processResponse(requestId, response);
					}
				} else {
					ListGridRecord[] records = createRecord(ed, excludePropertyType);
					response.setData(records);
					response.setTotalRows(records.length);
					processResponse(requestId, response);
				}
			}

		});

	}

	public String getDefinitionName() {
		return getAttribute("defName");
	}

	private ListGridRecord[] createRecord(EntityDefinition ed, String[] excludePropertyType) {
		// 除外するプロパティタイプを効率的に検索するためのセットを作成
		Set<String> excludePropertyTypeSet = (excludePropertyType == null || excludePropertyType.length == 0)
                ? java.util.Collections.emptySet()
                : new HashSet<String>(Arrays.asList(excludePropertyType));

		List<ListGridRecord> properties = new ArrayList<ListGridRecord>();
		for (PropertyDefinition pd : ed.getPropertyList()) {
			// 除外対象のタイプに一致するプロパティをスキップ
			if (pd.getType() != null && excludePropertyTypeSet.contains(pd.getType().name())) {
				continue;
			}
			ListGridRecord record = new ListGridRecord();
			copyValues(pd, record);
			properties.add(record);

		}
		return properties.toArray(new ListGridRecord[]{});
	}

	private void copyValues(PropertyDefinition from, Record to) {
		to.setAttribute(DataSourceConstants.FIELD_NAME, from.getName());
		to.setAttribute(DataSourceConstants.FIELD_DISPLAY_NAME, from.getDisplayName());
		to.setAttribute("propertyDefinition", from);
		to.setAttribute("javaType", from.getJavaType().getName());
		copyValidationValues(from.getValidations(), to);
		copyLocalizeStingValues(from.getLocalizedDisplayNameList(), to);
	}

	private void copyValidationValues(List<ValidationDefinition> vdList, Record to) {
		for (ValidationDefinition vd : vdList) {
			if (vd instanceof NotNullValidation) {
				to.setAttribute("must", true);
			} else if (vd instanceof RangeValidation) {
				RangeValidation rVd = (RangeValidation)vd;
				to.setAttribute("max", rVd.getMax());
				to.setAttribute("min", rVd.getMin());
				to.setAttribute("rangeValidation", rVd);
			} else if (vd instanceof RegexValidation) {
				to.setAttribute("regexValidation", (RegexValidation)vd);
			} else {
				continue;
			}
		}
	}

	private void copyLocalizeStingValues(List<LocalizedStringDefinition> lsdList, Record to) {
		to.setAttribute("localizedString", lsdList);
	}

}
