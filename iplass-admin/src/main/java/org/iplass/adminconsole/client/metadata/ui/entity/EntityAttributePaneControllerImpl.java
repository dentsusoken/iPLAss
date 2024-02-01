/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityMapping;
import org.iplass.mtp.entity.definition.StoreDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.stores.SchemalessRdbStore;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;

public class EntityAttributePaneControllerImpl implements EntityAttributePaneController {

	private static LinkedHashMap<String, String> versionTypeMap;
	private static String[] storageSpaces;

	private String defName;

	private SelectItem versionControleType;
	private ComboBoxItem storageSpace;
	private CheckboxItem crawl;
	private CheckboxItem queryCache;
	private TextItem mappingClass;

	private static void setVersionTypes(final SelectItem select) {
		if (versionTypeMap == null) {
			versionTypeMap = new LinkedHashMap<String, String>();
			versionTypeMap.put(VersionControlType.NONE.name(), "NONE");
			versionTypeMap.put(VersionControlType.VERSIONED.name(), "NUMBER BASE");
			versionTypeMap.put(VersionControlType.TIMEBASE.name(), "TIME BASE");
			versionTypeMap.put(VersionControlType.SIMPLE_TIMEBASE.name(), "SIMPLE TIME BASE");
			versionTypeMap.put(VersionControlType.STATEBASE.name(), "STATE BASE");
		}
		select.setValueMap(versionTypeMap);
	}

	private static void setStorageSpaces(final ComboBoxItem select) {
		if (storageSpaces == null) {
			MetaDataServiceAsync service = MetaDataServiceFactory.get();
			service.getEntityStoreSpaceList(TenantInfoHolder.getId(), new AsyncCallback<List<String>>() {

				@Override
				public void onFailure(Throwable caught) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityAttributeEditPane_failed"), AdminClientMessageUtil.getString("ui_metadata_entity_EntityAttributeEditPane_failedGetScreenInfo"));

					GWT.log(caught.toString(), caught);
				}

				@Override
				public void onSuccess(List<String> result) {

					storageSpaces = result.toArray(new String[]{});
					select.setValueMap(storageSpaces);
				}
			});
		} else {
			select.setValueMap(storageSpaces);
		}
	}

	@Override
	public int getHeight() {
		return 60;
	}

	@Override
	public List<FormItem> getFormItems() {

		List<FormItem> items = new ArrayList<>();

		versionControleType = new SelectItem("versionControleType", "Versioning");
		versionControleType.setWidth(200);
		SmartGWTUtil.addHoverToFormItem(versionControleType,
				AdminClientMessageUtil.getString("ui_metadata_entity_EntityAttributeEditPane_versionControleTypeComment")
				);
		setVersionTypes(versionControleType);
		items.add(versionControleType);

		storageSpace = new ComboBoxItem("storageSpace", "Storage Space");
		storageSpace.setWidth(200);
		setStorageSpaces(storageSpace);
		SmartGWTUtil.addHoverToFormItem(storageSpace, AdminClientMessageUtil.getString("ui_metadata_entity_EntityAttributeEditPane_storageSpace"));
		items.add(storageSpace);

		crawl = new CheckboxItem("crawl", "crawl for full text search");
		crawl.setStartRow(true);
		SmartGWTUtil.addHoverToFormItem(crawl, AdminClientMessageUtil.getString("ui_metadata_entity_EntityAttributeEditPane_crawlComment"));
		items.add(crawl);

		queryCache = new CheckboxItem("queryCache", "cache query result");
		SmartGWTUtil.addHoverToFormItem(queryCache, AdminClientMessageUtil.getString("ui_metadata_entity_EntityAttributeEditPane_queryCacheComment"));
		items.add(queryCache);

		mappingClass = new TextItem("mappingClass", "Mapping Class");
		mappingClass.setWidth("100%");
		mappingClass.setColSpan(3);
		mappingClass.setStartRow(true);
		SmartGWTUtil.addHoverToFormItem(mappingClass, AdminClientMessageUtil.getString("ui_metadata_entity_EntityAttributeEditPane_mappingClassComment"));
		items.add(mappingClass);

		ButtonItem createMappginClassBtn = new ButtonItem("createMappginClass", "Create Java Class");
		createMappginClassBtn.setShowTitle(false);
		createMappginClassBtn.setStartRow(false);
		createMappginClassBtn.setEndRow(false);
		createMappginClassBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_entity_EntityAttributeEditPane_createMappginClassComment"));
		createMappginClassBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CreateJavaMappingClassDialog dialog = new CreateJavaMappingClassDialog(
						defName, SmartGWTUtil.getStringValue(mappingClass));
				dialog.show();
			}
		});
		items.add(createMappginClassBtn);

		return items;
	}

	@Override
	public void applyFrom(EntityDefinition definition) {
		//MappingClass作成用に名前を保持
		this.defName = definition.getName();

		crawl.setValue(definition.isCrawl());
		queryCache.setValue(definition.isQueryCache());
		if (definition.getMapping() == null) {
			mappingClass.clearValue();
		} else {
			mappingClass.setValue(definition.getMapping().getMappingModelClass());
		}
		if (definition.getVersionControlType() == null) {
			versionControleType.setValue(VersionControlType.NONE.name());
		} else {
			versionControleType.setValue(definition.getVersionControlType().name());
		}
		//SchemalessRdbStore固定
		if (definition.getStoreDefinition() == null) {
			storageSpace.setValue(SchemalessRdbStore.DEFAULT_STORAGE_SPACE);
		} else {
			storageSpace.setValue(((SchemalessRdbStore)definition.getStoreDefinition()).getStorageSpace());
		}

	}

	@Override
	public void applyTo(EntityDefinition definition) {

		definition.setCrawl(SmartGWTUtil.getBooleanValue(crawl));
		definition.setQueryCache(SmartGWTUtil.getBooleanValue(queryCache));

		//FIXME クラス名の妥当性どう保障する？ReflectionServiceでクラス生成試してみるか？
		EntityMapping mapping = null;
		String className = mappingClass.getValueAsString();
		if (className != null) {
			mapping = new EntityMapping();
			mapping.setMappingModelClass(className);
		}
		definition.setMapping(mapping);

		String versionType = versionControleType.getValueAsString();
		if (versionType == null) {
			definition.setVersionControlType(VersionControlType.NONE);
		} else {
			definition.setVersionControlType(VersionControlType.valueOf(versionType));
		}

		//SchemalessRdbStore固定
		StoreDefinition storeDefinition = null;
		String spaceName = SmartGWTUtil.getStringValue(storageSpace);
		if (spaceName != null) {
			storeDefinition = new SchemalessRdbStore(spaceName);
		}
		definition.setStoreDefinition(storeDefinition);

	}

	@Override
	public void setCrawlStatusChangedHandler(ChangedHandler handler) {
		crawl.addChangedHandler(handler);
	}

	@Override
	public boolean isCrawlEntity() {
		return crawl.getValueAsBoolean();
	}

}
