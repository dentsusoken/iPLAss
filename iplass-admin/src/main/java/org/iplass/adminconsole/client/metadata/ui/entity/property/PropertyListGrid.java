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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyEditDialog.PropertyEditDialogHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.type.PropertyTypeAttributeController;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.StoreDefinition;
import org.iplass.mtp.entity.definition.stores.ColumnMapping;
import org.iplass.mtp.entity.definition.stores.SchemalessRdbStore;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;

/**
 * 1Entityに対するPropertyを表示するListGrid
 *
 * @author lis2s8
 *
 */
public class PropertyListGrid extends ListGrid {

	/** 対象Entity */
	private EntityDefinition curDefinition;

	/** 利用可能言語 */
	private Map<String, String> enableLangMap;

	private boolean isShowInheritedProperty;
	private List<PropertyListGridRecord> inheritedProps;

	private PropertyTypeAttributeController typeController = GWT.create(PropertyTypeAttributeController.class);


	/**
	 * コンストラクタ
	 */
	public PropertyListGrid(boolean isShowInheritedProperty) {
		this.isShowInheritedProperty = isShowInheritedProperty;

		setWidth100();
		setHeight(1);

		setShowAllColumns(true);	// 列を全て表示
		setShowAllRecords(true);	// レコードを全て表示

		setOverflow(Overflow.VISIBLE);
		setBodyOverflow(Overflow.VISIBLE);

		setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される

		setCanResizeFields(true);	//列幅変更可
		setCanSort(false);			//ソート不可
		setCanGroupBy(false);		//Group化不可
		setCanPickFields(false);	//列の選択不可
		setCanAutoFitFields(false);	//列幅の自動調整不可(崩れるので)

		setCanDragRecordsOut(true);				//grid内でのD&Dでの並べ替えを許可
		setCanAcceptDroppedRecords(true);
		setCanReorderRecords(true);

		setCanEdit(true);						//編集可
		setEditEvent(ListGridEditEvent.CLICK);	//Clickで編集開始
		setEditByCell(true);					//Cell単位で編集

		// 各フィールド初期化
		ListGridField statusField = new ListGridField(PropertyListGridRecord.STATUS, "*");
		statusField.setWidth(30);
		statusField.setAlign(Alignment.CENTER);
		statusField.setCanEdit(false);

		ListGridField customOidField = new ListGridField(PropertyListGridRecord.CUSTOM_OID, "OID");
		customOidField.setType(ListGridFieldType.BOOLEAN);
		customOidField.setWidth(40);
		customOidField.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
				valueChanged(PropertyListGridRecord.CUSTOM_OID, event.getRowNum(), event.getValue());
			}
		});
		ListGridField customNameField = new ListGridField(PropertyListGridRecord.CUSTOM_NAME, "Name");
		customNameField.setType(ListGridFieldType.BOOLEAN);
		customNameField.setWidth(40);
		customNameField.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
				valueChanged(PropertyListGridRecord.CUSTOM_NAME, event.getRowNum(), event.getValue());
			}
		});
		ListGridField crawlPropField = new ListGridField(PropertyListGridRecord.CRAWL_PROP, "Crawl");
		crawlPropField.setType(ListGridFieldType.BOOLEAN);
		crawlPropField.setWidth(40);
		crawlPropField.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
				valueChanged(PropertyListGridRecord.CRAWL_PROP, event.getRowNum(), event.getValue());
			}
		});
		ListGridField nameField = new ListGridField(PropertyListGridRecord.NAME, "Name");
		nameField.setCanEdit(false);
		nameField.setShowHover(true);
		ListGridField dispNameField = new ListGridField(PropertyListGridRecord.DISPNAME, "Display Name");
		dispNameField.setCanEdit(false);
		dispNameField.setShowHover(true);
		dispNameField.setHoverWrap(false);
		ListGridField typeField = new ListGridField(PropertyListGridRecord.TYPE_DISP_VAL, "Type");
		typeField.setCanEdit(false);
		typeField.setShowHover(true);
		typeField.setHoverWrap(false);
		typeField.setWidth(90);
		ListGridField multiplicity = new ListGridField(PropertyListGridRecord.MULTIPLE_DISP_VAL, "Multi");
		multiplicity.setWidth(35);
		multiplicity.setAlign(Alignment.RIGHT);
		multiplicity.setCanEdit(false);
		ListGridField mustField = new ListGridField(PropertyListGridRecord.NOT_NULL_DISP_VAL, "Required");
		mustField.setWidth(60);
		mustField.setAlign(Alignment.CENTER);
		mustField.setCanEdit(false);
		ListGridField updatableField = new ListGridField(PropertyListGridRecord.UPDATABLE_DISP_VAL, "CanEdit");
		updatableField.setWidth(55);
		updatableField.setAlign(Alignment.CENTER);
		updatableField.setCanEdit(false);
		ListGridField indexField = new ListGridField(PropertyListGridRecord.INDEXTYPE_DISP_VAL, "Index");
		indexField.setWidth(40);
		indexField.setAlign(Alignment.CENTER);
		indexField.setCanEdit(false);
		ListGridField columnMappingNameField = new ListGridField(PropertyListGridRecord.STORE_COLUMN_MAPPING_NAME, "Col Mapping");
		columnMappingNameField.setWidth(70);
		columnMappingNameField.setCanEdit(false);
		columnMappingNameField.setShowHover(true);
		columnMappingNameField.setHoverWrap(false);
		ListGridField gpField = new ListGridField(PropertyListGridRecord.REMARKS, " ");
		gpField.setCanEdit(false);
		gpField.setShowHover(true);
		gpField.setHoverWrap(false);

		// 各フィールドをListGridに設定
		setFields(statusField, customOidField, customNameField, crawlPropField,
				nameField, dispNameField, typeField, multiplicity, mustField, updatableField, indexField, columnMappingNameField, gpField);

		// レコード編集イベント設定
		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				showPropertyEditDialog((PropertyListGridRecord)getRecord(event.getRecordNum()));
			}
		});

		//
		addRecordDropHandler(new RecordDropHandler() {

			@Override
			public void onRecordDrop(RecordDropEvent event) {
				checkRecordDroped(event);
			}
		});

		initialize();
	}

	@Override
    protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
		PropertyListGridRecord propertyRecord = (PropertyListGridRecord)record;
		if (propertyRecord.isInherited()) {
			return "entityInheritedPropertyGridRow";
		} else if (propertyRecord.isDelete()) {
				return "entityDeletePropertyGridRow";
		} else {
			return super.getBaseStyle(record, rowNum, colNum);
		}
	}

	public void setDefinition(EntityDefinition definition) {
		this.curDefinition = definition;
		setGridData();
	}

	public void setEnableLangMap(Map<String, String> enableLangMap) {
		this.enableLangMap = enableLangMap;
		setGridData();
	}

	public void setShowInheritedProperty(boolean isShowInheritedProperty) {
		if (this.isShowInheritedProperty != isShowInheritedProperty) {
			this.isShowInheritedProperty = isShowInheritedProperty;

			List<PropertyListGridRecord> showProps = new ArrayList<PropertyListGridRecord>();
			//inheritedPropsは変数から
			if (isShowInheritedProperty) {
				showProps.addAll(inheritedProps);	//inheritedを先頭に
			}
			//declaredPropsはレコードから
			for (ListGridRecord record : getRecords()) {
				PropertyListGridRecord propertyRecord = (PropertyListGridRecord)record;
				if (!propertyRecord.isInherited()) {
					showProps.add(propertyRecord);
				}
			}
			setData(showProps.toArray(new PropertyListGridRecord[]{}));
		}
	}

	public void setCrawlStatusChanged(boolean isCrawlEntity) {
		List<PropertyListGridRecord> showProps = new ArrayList<PropertyListGridRecord>();
		//inheritedPropsは変数から
		for (ListGridRecord record : inheritedProps) {
			PropertyListGridRecord propertyRecord = (PropertyListGridRecord)record;
			//共通項目はデフォルト未選択
			propertyRecord.setCrawlProp(false);
		}
		if (isShowInheritedProperty) {
			showProps.addAll(inheritedProps);	//inheritedを先頭に
		}

		//declaredPropsはレコードから
		for (ListGridRecord record : getRecords()) {
			PropertyListGridRecord propertyRecord = (PropertyListGridRecord)record;
			if (!propertyRecord.isInherited()) {
				if (!propertyRecord.isDelete()) {
					if (isCrawlEntity) {
						//Reference以外をデフォルト選択
						if (PropertyDefinitionType.REFERENCE != propertyRecord.getRecordType()) {
							propertyRecord.setCrawlProp(true);
						} else {
							propertyRecord.setCrawlProp(false);
						}
					} else {
						propertyRecord.setCrawlProp(false);
					}
				}
				showProps.add(propertyRecord);
			}
		}
		setData(showProps.toArray(new PropertyListGridRecord[]{}));
	}

	public EntityDefinition getEditDefinition(EntityDefinition definition) {

		definition.setNamePropertyName(null);
		List<String> oidProps = new ArrayList<String>();
		List<String> crawlProps = new ArrayList<String>();
		List<ColumnMapping> colMapList = new ArrayList<ColumnMapping>();

		if (!isShowInheritedProperty) {
			//InheritedPropertyがGridに表示されていないので変数から
			for (ListGridRecord record : inheritedProps) {
				PropertyListGridRecord propRecord = (PropertyListGridRecord)record;
				if (propRecord.isCustomOid()) {
					oidProps.add(propRecord.getRecordName());
				}

				if (propRecord.isCrawlProp()) {
					crawlProps.add(propRecord.getRecordName());
				}

				if (propRecord.isCustomName() && !Entity.NAME.equals(propRecord.getRecordName())) {
					definition.setNamePropertyName(propRecord.getRecordName());
				}

				if (!SmartGWTUtil.isEmpty(propRecord.getRecordStoreColumnMappingName())) {
					ColumnMapping colMap = new ColumnMapping();
					colMap.setPropertyName(propRecord.getRecordName());
					colMap.setColumnName(propRecord.getRecordStoreColumnMappingName());
					colMapList.add(colMap);
				}
			}
		}

		List<PropertyDefinition> properties = new ArrayList<PropertyDefinition>();

		for (ListGridRecord record : getRecords()) {
			PropertyListGridRecord propRecord = (PropertyListGridRecord)record;

			if (propRecord.isDelete()) {
				continue;
			}

			PropertyDefinition prop = getEditPropertyDefinition(propRecord, definition);

			//Inheritedプロパティは追加対象外
			if (!propRecord.isInherited()) {
				properties.add(prop);
			}

			if (propRecord.isCustomOid()) {
				oidProps.add(prop.getName());
			}

			if (propRecord.isCrawlProp()) {
				crawlProps.add(prop.getName());
			}

			if (propRecord.isCustomName() && !Entity.NAME.equals(prop.getName())) {
				definition.setNamePropertyName(prop.getName());
			}

			if (!SmartGWTUtil.isEmpty(propRecord.getRecordStoreColumnMappingName())) {
				ColumnMapping colMap = new ColumnMapping();
				colMap.setPropertyName(propRecord.getRecordName());
				colMap.setColumnName(propRecord.getRecordStoreColumnMappingName());
				colMapList.add(colMap);
			}
		}
		definition.setPropertyList(properties);

		if (oidProps.isEmpty()) {
			definition.setOidPropertyName(null);
		} else if (oidProps.size() == 1 && Entity.OID.equals(oidProps.get(0))){
			definition.setOidPropertyName(null);
		} else {
			definition.setOidPropertyName(oidProps);
		}
		if (crawlProps.isEmpty()) {
			definition.setCrawlPropertyName(null);
		} else {
			definition.setCrawlPropertyName(crawlProps);
		}

		SchemalessRdbStore rdbStore = null;
		if (definition.getStoreDefinition() == null) {
			//実際にはあり得ない(EntityAttributeEditPane#getEditDefinitionで設定される)
			rdbStore = new SchemalessRdbStore(SchemalessRdbStore.DEFAULT_STORAGE_SPACE);
		} else {
			if (definition.getStoreDefinition() instanceof SchemalessRdbStore) {
				rdbStore = (SchemalessRdbStore)definition.getStoreDefinition();
			} else {
				//現状あり得ない
			}
		}
		if (rdbStore != null) {
			if (colMapList.isEmpty()) {
				rdbStore.setColumnMappingList(null);
			} else {
				rdbStore.setColumnMappingList(colMapList);
			}
			definition.setStoreDefinition(rdbStore);
		}

		return definition;
	}

	/**
	 * 名前が変更されたPropertyのMap(from, to)を返します。
	 *
	 * @return 名前が変更されたPropertyのMap(from, to)
	 */
	public Map<String, String> getRenamePropertyMap() {

		Map<String, String> renamePropertyMap = new HashMap<String, String>();

		ListGridRecord[] records = getRecords();
		for (ListGridRecord record : records) {
			PropertyListGridRecord propRecord = (PropertyListGridRecord)record;

			if (propRecord.isDelete()) {
				continue;
			}

			//Inheritedプロパティは追加対象外
			if (propRecord.isInherited()) {
				continue;
			}

			if (propRecord.getSavedName() != null) {
				if (!propRecord.getSavedName().equals(propRecord.getRecordName())) {
					renamePropertyMap.put(propRecord.getSavedName(), propRecord.getRecordName());
				}
			}
		}

		return renamePropertyMap.isEmpty() ? null : renamePropertyMap;
	}

	/**
	 * 名前が重複したProperty名を返します。
	 *
	 * @return 重複したProperty名(複数ある場合は先頭。ない場合はnull)
	 */
	public String getDuplicatePropertyName() {
		ListGridRecord[] records = getRecords();
		Set<String> names = new HashSet<String>();
		//InheritedPropertyを取得
		for (ListGridRecord record : inheritedProps) {
			PropertyListGridRecord propRecord = (PropertyListGridRecord)record;
			names.add(propRecord.getRecordName());
		}
		for (ListGridRecord record : records) {
			PropertyListGridRecord propRecord = (PropertyListGridRecord)record;

			if (propRecord.isDelete()) {
				continue;
			}

			//Inheritedプロパティは追加対象外
			if (propRecord.isInherited()) {
				continue;
			}

			if (names.contains(propRecord.getRecordName())) {
				return propRecord.getRecordName();
			} else {
				names.add(propRecord.getRecordName());
			}
		}

		return null;
	}

	public void addProperty() {
		showPropertyEditDialog(null);
	}

	public void removeSelectedProperty() {
		ListGridRecord[] records = getSelectedRecords();
		if (records == null || records.length == 0) {
			return;
		}

		for (ListGridRecord record : records) {
			PropertyListGridRecord propRecord = (PropertyListGridRecord)record;
			//Inheritedは対象外
			if (propRecord.isInherited()) {
				continue;
			}

			if (propRecord.isInsert()) {
				removeData(propRecord);
			} else {
				propRecord.setStatusDelete();
				refreshRow(getRowNum(propRecord));
			}
		}
	}

	private void initialize() {
	}

	private void setGridData() {
		if (curDefinition != null && enableLangMap != null) {

			List<PropertyDefinition> lstProp = curDefinition.getPropertyList();

			List<PropertyListGridRecord> declaredProps = new ArrayList<PropertyListGridRecord>();
			inheritedProps = new ArrayList<PropertyListGridRecord>();

			//StoreDefinitionのColumnMap情報取得
			Map<String, String> colMap = new HashMap<String, String>();
			if (curDefinition.getStoreDefinition() != null) {
				StoreDefinition storeDefinition = curDefinition.getStoreDefinition();
				if (storeDefinition instanceof SchemalessRdbStore) {
					SchemalessRdbStore rdbStoreDefinition  = (SchemalessRdbStore)storeDefinition;
					if (rdbStoreDefinition.getColumnMappingList() != null) {
						for (ColumnMapping mapping : rdbStoreDefinition.getColumnMappingList()) {
							colMap.put(mapping.getPropertyName(), mapping.getColumnName());
						}
					}
				}
			}

			for (PropertyDefinition property : lstProp) {
				PropertyListGridRecord record = new PropertyListGridRecord();
				record.applyFrom(property, curDefinition, colMap);

				if (property.isInherited()) {
					inheritedProps.add(record);
				} else {
					declaredProps.add(record);
				}
			}
			List<PropertyListGridRecord> showProps = new ArrayList<PropertyListGridRecord>();
			if (isShowInheritedProperty) {
				showProps.addAll(inheritedProps);	//inheritedを先頭に
			}
			showProps.addAll(declaredProps);

			setData(showProps.toArray(new PropertyListGridRecord[]{}));
		}
	}

	private void showPropertyEditDialog(final PropertyListGridRecord record) {
		PropertyEditDialog dialog = new PropertyEditDialog(curDefinition.getName(), record, enableLangMap, new PropertyEditDialogHandler() {

			@Override
			public void onSaved(PropertyListGridRecord updated) {
				if (record == null) {
					//追加
					addData(updated);
				}
				updateData(updated);
				refreshFields();
			}
		});
		dialog.show();
	}

	private PropertyDefinition getEditPropertyDefinition(PropertyListGridRecord record, EntityDefinition entity) {
		String name = record.getRecordName();

		if (SmartGWTUtil.isNotEmpty(name) && record.getRecordType() != null) {
			PropertyDefinitionType type = record.getRecordType();

			PropertyDefinition property = typeController.createTypeDefinition(type);
			record.applyTo(property, entity);

			return property;
		}

		return null;
	}

	private void valueChanged(String colName, int row, Object value) {
		PropertyListGridRecord target = (PropertyListGridRecord)getRecord(row);
		if (PropertyListGridRecord.CUSTOM_OID.equals(colName)) {
			target.setCustomOid((Boolean)value);

			//OIDのみか、OID以外の複数の組み合わせのみ
			ListGridRecord[] records = getAllRecords();
			if (Entity.OID.equals(target.getRecordName())) {
				//OIDが選択or解除されたらすべて解除
				for (ListGridRecord record : records) {
					PropertyListGridRecord propRecord = (PropertyListGridRecord)record;
					if (!Entity.OID.equals(propRecord.getRecordName()) && propRecord.isCustomOid()) {
						propRecord.setCustomOid(false);
						refreshRow(getRecordIndex(propRecord));
					}
				}
			} else {
				boolean checkCustomOid = false;
				for (ListGridRecord record : records) {
					PropertyListGridRecord propRecord = (PropertyListGridRecord)record;
					if (!propRecord.isDelete() && !Entity.OID.equals(propRecord.getRecordName()) && propRecord.isCustomOid()) {
						checkCustomOid = true;
						break;
					}
				}
				for (ListGridRecord record : records) {
					PropertyListGridRecord propRecord = (PropertyListGridRecord)record;
					if (Entity.OID.equals(propRecord.getRecordName())) {
						if (checkCustomOid) {
							//OIDのチェック解除
							propRecord.setCustomOid(false);
						} else {
							//OIDをチェック
							propRecord.setCustomOid(true);
						}
						refreshRow(getRecordIndex(propRecord));
						break;
					}
				}
			}
		} else if (PropertyListGridRecord.CUSTOM_NAME.equals(colName)) {
			//1つのみ許可
			ListGridRecord[] records = getRecords();

			for (ListGridRecord record : records) {
				PropertyListGridRecord propRecord = (PropertyListGridRecord)record;
				if (propRecord.isCustomName()) {
					propRecord.setCustomName(false);
					refreshRow(getRecordIndex(propRecord));
					break;
				}
			}
			target.setCustomName((Boolean)value);
		}
	}

	private ListGridRecord[] getAllRecords() {
		if (isShowInheritedProperty) {
			return getRecords();
		} else {
			List<ListGridRecord> list = new ArrayList<ListGridRecord>();
			list.addAll(inheritedProps);
			list.addAll(Arrays.asList(getRecords()));
			return list.toArray(new ListGridRecord[list.size()]);
		}
	}

	private void checkRecordDroped(RecordDropEvent event) {
		//DropされたRecordがPropertyListGridRecordでない場合は無効（念のためチェック）
		if (!(event.getDropRecords()[0] instanceof PropertyListGridRecord)) {
			event.cancel();
			return;
		}

		//DropされたレコードにInheritedプロパティが含まれていたら無効
		for (ListGridRecord record : event.getDropRecords()) {
			PropertyListGridRecord plgr = (PropertyListGridRecord)record;
			if (plgr.isInherited()) {
				event.cancel();
				return;
			}
		}

		//Drop先が最終レコードより前の場合、次のレコードがInheritedだったら無効
		//（Inheritedの間にDropは不可）
		if (event.getIndex() < getRecordList().getLength() - 1) {
			PropertyListGridRecord plgr = (PropertyListGridRecord)getRecord(event.getIndex());
			if (plgr.isInherited()) {
				event.cancel();
				return;
			}
		}
	}

}
