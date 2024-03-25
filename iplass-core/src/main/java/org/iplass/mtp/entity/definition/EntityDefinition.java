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

package org.iplass.mtp.entity.definition;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.stores.SchemalessRdbStore;



/**
 * <p>
 * Entityの論理定義。
 * </p>
 *
 * <h5>※注意</h5>
 * <p>
 * Entityの定義名は、他の定義（Action、Templateなど）と異なり、"/"をパッケージ区切りとして利用できない。
 * Entity定義名は、"/"の代わりに、"."をパッケージ区切りとなる。
 * </p>
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityDefinition implements Definition {
	private static final long serialVersionUID = 8760071361206854130L;

	public static final String SYSTEM_DEFAULT_DEFINITION_NAME = "Entity";

	/** Entityの定義名 */
	private String name;

	/** 表示名 */
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;

	/** 説明 */
	private String description;

	/** 継承元の定義名 */
	private String inheritedDefinition;//TODO 継承の概念のサポート

	/** oid（オブジェクトID）として使用するPropertyの定義名。未指定の場合は、自動生成されるIDが使用される */
	private List<String> oidPropertyName;

	/** name（名前）として使用するPropertyの定義名。指定されている場合、自動的にそのPropertyの値がnameにセットされる */
	private String namePropertyName;

	/** Crawl対象のPropertyの定義名 */
	private List<String> crawlPropertyName;

	/** スーパーモデルでのプロパティ定義も含んだプロパティのリスト */
	@MultiLang(isMultiLangValue = false, itemKey = "property", itemGetter = "getDeclaredPropertyList", itemSetter = "setPropertyList")
	private List<PropertyDefinition> propertyList;

//	/** 汎用モデルに定義されるプロパティのリスト */
//	private List<PropertyDefinition> declaredPropertyList;

	private List<EventListenerDefinition> eventListenerList;

	private List<LocalizedStringDefinition> localizedDisplayNameList;

	/** EntityをPOJOへマッピングする場合のマッピング定義 */
	private EntityMapping mapping;

	private StoreDefinition storeDefinition = new SchemalessRdbStore();

	private VersionControlType versionControlType;

	private boolean crawl;

	/** クエリー結果キャッシュの有効化有無 */
	private boolean queryCache;

	private DataLocalizationStrategy dataLocalizationStrategy;

	public EntityDefinition() {
	}

	public EntityDefinition(String name) {
		setName(name);
	}

	public DataLocalizationStrategy getDataLocalizationStrategy() {
		return dataLocalizationStrategy;
	}

	public void setDataLocalizationStrategy(
			DataLocalizationStrategy dataLocalizationStrategy) {
		this.dataLocalizationStrategy = dataLocalizationStrategy;
	}

	public boolean isQueryCache() {
		return queryCache;
	}

	public void setQueryCache(boolean queryCache) {
		this.queryCache = queryCache;
	}

	public boolean isCrawl() {
		return crawl;
	}

	public void setCrawl(boolean crawl) {
		this.crawl = crawl;
	}

	public String getNamePropertyName() {
		return namePropertyName;
	}

	public void setNamePropertyName(String namePropertyName) {
		this.namePropertyName = namePropertyName;
	}

	public VersionControlType getVersionControlType() {
		return versionControlType;
	}

	public void setVersionControlType(VersionControlType versionControlType) {
		this.versionControlType = versionControlType;
	}

	public List<String> getOidPropertyName() {
		return oidPropertyName;
	}

	public void setOidPropertyName(List<String> oidPropertyName) {
		this.oidPropertyName = oidPropertyName;
	}

	public List<String> getCrawlPropertyName() {
		return crawlPropertyName;
	}

	public void setCrawlPropertyName(List<String> crawlPropertyName) {
		this.crawlPropertyName = crawlPropertyName;
	}

	public String getName() {
		return convertName(name);
	}

	public void setName(String name) {
		this.name = convertName(name);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInheritedDefinition() {
		return inheritedDefinition;
	}

	public void setInheritedDefinition(String inheritedDefinition) {
		this.inheritedDefinition = inheritedDefinition;
	}

	public List<PropertyDefinition> getPropertyList() {
		return propertyList;
//		ArrayList<DataModelProperty<?>> allPropList = new ArrayList<DataModelProperty<?>>();
//		if (inheritedDefinition == null) {
//			allPropList.addAll(SYSTEM_DEFAULT_PROPERTIY);
//		} else {
//			DataModelDefinitionManager dmdm = ServiceLocator.getInstance().getDataModelDefinitionManager();
//			DataModelDefinition superDef = dmdm.get(inheritedDefinition);
//			if (superDef != null) {
//				allPropList.addAll(superDef.getPropertyList());
//			}
//		}
//		if (declaredPropertyList != null) {
//			allPropList.addAll(declaredPropertyList);
//		}
//		return allPropList;
	}

	public void setPropertyList(List<PropertyDefinition> propertyList) {
		this.propertyList = propertyList;
	}

	public List<PropertyDefinition> getDeclaredPropertyList() {
		ArrayList<PropertyDefinition> decList = new ArrayList<PropertyDefinition>();
		if (propertyList != null) {
			for (PropertyDefinition pd: propertyList) {
				if (!pd.isInherited()) {
					decList.add(pd);
				}
			}
		}
		return decList;
	}

//	public void setDeclaredPropertyList(List<PropertyDefinition> propList) {
//		this.declaredPropertyList = propList;
//	}

	public void addProperty(PropertyDefinition property) {
		if (propertyList == null) {
			propertyList = new ArrayList<PropertyDefinition>();
		}

		propertyList.add(property);
	}

//	public PropertyDefinition getDeclaredProperty(String propName) {
//
//		if (declaredPropertyList == null) {
//			return null;
//		}
//
//		for (PropertyDefinition p: declaredPropertyList) {
//			if (propName.equals(p.getName())) {
//				return p;
//			}
//		}
//		return null;
//	}

	public PropertyDefinition getProperty(String propName) {
		if (propertyList == null) {
			return null;
		}

		for (PropertyDefinition p: propertyList) {
			if (propName.equals(p.getName())) {
				return p;
			}
		}
		return null;
	}

	public EntityMapping getMapping() {
		return mapping;
	}

	public void setMapping(EntityMapping mapping) {
		this.mapping = mapping;
	}

	public List<EventListenerDefinition> getEventListenerList() {
		return eventListenerList;
	}

	public void setEventListenerList(List<EventListenerDefinition> eventListenerList) {
		this.eventListenerList = eventListenerList;
	}

	public void addEventListener(EventListenerDefinition eventListener) {
		if (eventListenerList == null) {
			eventListenerList = new ArrayList<EventListenerDefinition>();
		}

		eventListenerList.add(eventListener);
	}

	public StoreDefinition getStoreDefinition() {
		return storeDefinition;
	}

	public void setStoreDefinition(StoreDefinition storeDefinition) {
		this.storeDefinition = storeDefinition;
	}

	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	public void addLocalizedDisplayName(LocalizedStringDefinition localizedDisplayName) {
		if (localizedDisplayNameList == null) {
			localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedDisplayNameList.add(localizedDisplayName);
	}

//	public EntityDefinition copy() {
//		EntityDefinition copy = new EntityDefinition();
//		copy.name = name;
//		copy.displayName = displayName;
//		copy.inheritedDefinition = inheritedDefinition;
//		if (mapping != null) {
//			copy.mapping = mapping.copy();
//		}
//		if (declaredPropertyList != null) {
//			copy.declaredPropertyList = new ArrayList<PropertyDefinition>();
//			for (PropertyDefinition p: declaredPropertyList) {
//				copy.declaredPropertyList.add(p.copy());
//			}
//		}
//		if (propertyList != null) {
//			copy.propertyList = new ArrayList<PropertyDefinition>();
//			for (PropertyDefinition p: propertyList) {
//				copy.propertyList.add(p.copy());
//			}
//		}
//		if (eventListenerList != null) {
//			copy.eventListenerList = new ArrayList<EventListenerDefinition>();
//			for (EventListenerDefinition e: eventListenerList) {
//				copy.eventListenerList.add(e.copy());
//			}
//		}
//		return copy;
//	}

	/**
	 * Entityのname区切りは「/」ではなく「.」とする（EQL制約）。
	 * MetaEntity、EntityDefinitionのnameセット時にチェックし変換する。（念のため）
	 *
	 * @param name
	 * @return
	 */
	private String convertName(String name) {
		return name != null ? name.replace("/", ".") : null;
	}
}
