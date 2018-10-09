/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.entityexplorer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataListResultInfo;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridFieldIfFunction;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class EntitySearchResultDS extends AbstractAdminDataSource {

	public static final String WHERE_CRITERIA = "where";
	public static final String ORDERBY_CRITERIA = "orderby";
	public static final String VERSIONED_CRITERIA = "versioned";

	public static final String LIMIT_CRITERIA = "limit";
	public static final String OFFSET_CRITERIA = "offset";

	public static final String ENTITY_ATTRIBUTE_NAME = "entity";

	private static EntitySearchResultDS blankDS;

	private EntityDefinition definition;

	private List<ListGridField> gridFieldList;

	private EntityDataListResultInfo result;

	private String whereString;
	private String orderByString;

	private boolean showInheritedProperty = false;

	/**
	 * <p>空のDataSourceを返します。</p>
	 *
	 * @return 空のDataSource
	 */
	public static EntitySearchResultDS getBlankInstance() {
		if (blankDS == null) {
			blankDS = new EntitySearchResultDS();
		}
		return blankDS;
	}

	/**
	 * <p>{@link EntityDefinition} に対応するDataSourceを返します。</p>
	 *
	 * @return 対象 {@link EntityDefinition} 用のDataSource
	 */
	public static EntitySearchResultDS getInstance(EntityDefinition definition) {
		return new EntitySearchResultDS(definition);
	}

	public static boolean isBlankDS(EntitySearchResultDS ds) {
		if (blankDS == null || ds == null) {
			return false;
		}
		return ds.equals(blankDS);
	}

	public EntityDefinition getDefinition() {
		return definition;
	}

	public ListGridField[] getListGridFields() {
		if (gridFieldList != null) {
			return gridFieldList.toArray(new ListGridField[]{});
		}
		return null;
	}

	public EntityDataListResultInfo getResult() {
		return result;
	}

	public boolean isShowInheritedProperty() {
		return showInheritedProperty;
	}
	public void setShowInheritedProperty(boolean showInheritedProperty) {
		this.showInheritedProperty = showInheritedProperty;
	}
	public void setWhereString(String whereString) {
		this.whereString = whereString;
	}
	public void setOrderByString(String orderByString) {
		this.orderByString = orderByString;
	}

	/**
	 * コンストラクタ
	 *
	 */
	private EntitySearchResultDS() {
		//Fieldが未指定の場合に「neither this ListGrid nor its dataSource have fields」が
		//発生するためダミー列を設定
		setFields(new DataSourceTextField(Entity.OID, Entity.OID));
		gridFieldList = new ArrayList<ListGridField>(1);
		ListGridField gridField = new ListGridField(Entity.OID, Entity.OID);
		gridFieldList.add(gridField);
	}

	/**
	 * コンストラクタ
	 *
	 */
	private EntitySearchResultDS(EntityDefinition definition) {
		this.definition = definition;

		List<PropertyDefinition> properties = definition.getPropertyList();
		List<DataSourceField> dsFields = new ArrayList<DataSourceField>(properties.size());
		gridFieldList = new ArrayList<ListGridField>(properties.size());
		for (PropertyDefinition property : properties) {
//			String colTitle = property.getDisplayName();
			String colTitle = property.getName();
			//非参照の場合、列の後ろに非参照を表示
			if (property instanceof ReferenceProperty) {
				ReferenceProperty reference = (ReferenceProperty)property;
				colTitle = colTitle + "(" + reference.getObjectDefinitionName();
				if (reference.getMappedBy() != null && !reference.getMappedBy().isEmpty()) {
					colTitle = colTitle + "." + reference.getMappedBy();
				}
				colTitle = colTitle + ")";
			}
			//多重度が1より大きい場合、列の後ろに多重度を表示
			if (property.getMultiplicity() != 1) {
				colTitle = colTitle + "(*" + property.getMultiplicity() + ")";
			}
			DataSourceField dsField = new DataSourceTextField(property.getName(), colTitle);
			ListGridField gridField = new ListGridField(property.getName(), colTitle);
			//Filter設定
			if (property instanceof SelectProperty) {
				SelectProperty selectProperty = (SelectProperty)property;
				List<SelectValue> values = selectProperty.getSelectValueList();
				Map<String, String> valueMap = null;
				if (values != null) {
					valueMap = new LinkedHashMap<String, String>(values.size() + 1);
					for (SelectValue value : values) {
						valueMap.put(value.getValue(), value.getDisplayName());
					}
				} else {
					valueMap = new LinkedHashMap<String, String>(1);
				}
				valueMap.put("nullvalue", "NULL");	//値がnullだとうまくいかないので別の値を設定
				dsField.setValueMap(valueMap);
				gridField.setValueMap(valueMap);
			} else if (property instanceof BooleanProperty) {
				Map<String, String> valueMap = new LinkedHashMap<String, String>(3);
				valueMap.put("true", "TRUE");
				valueMap.put("false", "FALSE");
				valueMap.put("nullvalue", "NULL");	//値がnullだとうまくいかないので別の値を設定
				dsField.setValueMap(valueMap);
				gridField.setValueMap(valueMap);
			}
			//継承Propertyの場合は、表示/非表示の切り替え
			if (property.isInherited()
					&& !Entity.OID.equals(property.getName())
					&& !Entity.NAME.equals(property.getName())) {
				gridField.setShowIfCondition(new ListGridFieldIfFunction() {

					@Override
					public boolean execute(ListGrid grid, ListGridField field, int fieldNum) {
						return isShowInheritedProperty();
					}
				});
			}
//			//PrimaryKey設定(versionがあるのでPrimaryKEY指定しない)
//			if ("oid".equals(property.getName())) {
//				field.setPrimaryKey(true);
//			}
			dsFields.add(dsField);
			gridFieldList.add(gridField);
		}
		setFields(dsFields.toArray(new DataSourceField[]{}));
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		debug("executeFetch ◆◆◆呼び出し. definition -> " + (definition == null ? "null" : "exist"));

		this.result = null;

		if (this == blankDS) {
			final long starttime = System.currentTimeMillis();
			//ダミーグリッドの場合はそのまま終了
			processResponse(requestId, response);

			debug("executeFetch definition未指定のため終了. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");
			return;
		}

		Criteria criteria = request.getCriteria();

		if (criteria == null) {
			//条件未指定は終了
			final long starttime = System.currentTimeMillis();
			processResponse(requestId, response);

			debug("executeFetch criteria未指定のため終了. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");
			return;
		}

		final long starttime = System.currentTimeMillis();

		EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();
		service.search(TenantInfoHolder.getId(), definition.getName(),
//					criteria.getAttributeAsString(WHERE_CRITERIA),
//					criteria.getAttributeAsString(ORDERBY_CRITERIA),
					whereString,
					orderByString,
					criteria.getAttributeAsBoolean(VERSIONED_CRITERIA),
					criteria.getAttributeAsInt(LIMIT_CRITERIA),
					criteria.getAttributeAsInt(OFFSET_CRITERIA),
					new AsyncCallback<EntityDataListResultInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				//エラーの通知
				EntityDataListResultInfo result = new EntityDataListResultInfo();
				result.setDefinitionName(definition.getName());
				result.addLogMessage(AdminClientMessageUtil.getString("datasource_tools_entityexplorer_EntitySearchResultDS_errGetData", definition.getName()));
				result.addLogMessage(caught.getMessage());
				result.setError(true);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);

				debug("executeFetch searchAll結果からレコード作成でエラーが発生. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");
			}

			@Override
			public void onSuccess(EntityDataListResultInfo result) {
				debug("executeFetch search実行完了. exec time -> " + (System.currentTimeMillis() - starttime) + "ms");
				final long starttime2 = System.currentTimeMillis();

				EntitySearchResultDS.this.result = result;

				//レコードの作成
				if (result.isError()) {
					response.setData(new ListGridRecord[]{});
					response.setTotalRows(0);
				} else {
					List<ListGridRecord> records = createRecord(result);
					response.setData(records.toArray(new ListGridRecord[]{}));
					response.setTotalRows(records.size());
				}
				processResponse(requestId, response);

				debug("executeFetch search結果からレコード作成. exec time -> " + (System.currentTimeMillis() - starttime2) + "ms");
			}

		});

	}

//	@SuppressWarnings("rawtypes")
//	private String getCriteriaString(Criteria criteria, String key) {
//
//		if (criteria != null && criteria.getValues() != null) {
//			Map newMap = criteria.getValues();
//			Set entrySet = newMap.entrySet();
//			for (Iterator entryIte = entrySet.iterator(); entryIte.hasNext();) {
//				Map.Entry entry = (Map.Entry)entryIte.next();
//
//				//バージョン検索用のパラメータは無視
//				if (key.equals((String)entry.getKey())) {
//					return (String)entry.getValue();
//				}
//			}
//		}
//
//		return null;
//	}

	/**
	 * レコードを作成します。
	 *
	 * @param request リクエスト情報
	 * @return {@link ListGridRecord} のリスト
	 */
	private List<ListGridRecord> createRecord(EntityDataListResultInfo result) {
		if (result == null || result.getDefinition() == null || result.isError()) {
			return new ArrayList<ListGridRecord>(0);
		}


		List<ListGridRecord> list = new ArrayList<ListGridRecord>(result.getRecords().size());

		List<PropertyDefinition> properties = definition.getPropertyList();

		for (Entity entity : result.getRecords()) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(ENTITY_ATTRIBUTE_NAME, entity);

			for (PropertyDefinition property : properties) {
				Object value = entity.getValue(property.getName());
				record.setAttribute(property.getName(), getDisplayValue(value));
			}
			list.add(record);
		}
		return list;
	}

	private String getDisplayValue(Object value) {
		if (value == null) {
			return null;
		}
		String retValue = null;
//		if (value.getClass().isArray()) {
		if (SmartGWTUtil.isArray(value)) {
			retValue = getArrayDisplayValue((Object[])value);
		} else if (value instanceof SelectValue) {
			SelectValue tmp = (SelectValue)value;
			retValue = tmp.getDisplayName() + "(" + tmp.getValue() + ")";
		} else if (value instanceof BinaryReference) {
			BinaryReference tmp = (BinaryReference)value;
			retValue = tmp.getName() + "(" + tmp.getLobId() + ")";
		} else if (value instanceof Entity) {
			Entity tmp = (Entity)value;
			retValue = tmp.getName() + "(" + tmp.getOid() + ")";
		} else if (value instanceof Timestamp) {
			//Timestamp型
			Timestamp tmp = (Timestamp)value;
			retValue = SmartGWTUtil.formatTimestamp(tmp);
		} else if (value instanceof Time) {
			//Time型
			Time tmp = (Time)value;
			retValue = SmartGWTUtil.formatTime(tmp);
		} else if (value instanceof Date) {
			//Date型
			Date tmp = (Date)value;
			retValue = SmartGWTUtil.formatDate(tmp);
		} else if (value instanceof BigDecimal) {
			//BigDecimal型
			BigDecimal tmp = (BigDecimal)value;
			retValue = tmp.toPlainString();
		} else if (value instanceof Double) {
			//Double型
			BigDecimal tmp = BigDecimal.valueOf((Double)value);
			retValue = tmp.toPlainString();
		} else {
			retValue =  value.toString();
		}
		return SafeHtmlUtils.htmlEscape(retValue);
	}

	private String getArrayDisplayValue(Object[] arrayValue) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (Object value : arrayValue) {
			builder.append(getDisplayValue(value) + ",");
		}
		if (builder.length() > 1) {
			builder.deleteCharAt(builder.length() - 1);
		}
		builder.append("]");
		return builder.toString();
	}

	public boolean isPopupColumn(String fieldName) {
		if (result == null || result.getDefinition() == null) {
			return false;
		}

		EntityDefinition definition = result.getDefinition();
		PropertyDefinition property = definition.getProperty(fieldName);
		if (property instanceof ReferenceProperty) {
			ReferenceProperty reference = (ReferenceProperty)property;
			if (reference.getMappedBy() != null && !reference.getMappedBy().isEmpty()) {
				return true;
			} else if (property.getMultiplicity() != 1) {
				return true;
			}
		}

		return false;
	}

	private void debug(String message) {
		GWT.log("EntityExplorerDS DEBUG " + message);
	}

}
