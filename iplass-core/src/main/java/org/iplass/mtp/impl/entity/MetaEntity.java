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

package org.iplass.mtp.impl.entity;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityMapping;
import org.iplass.mtp.entity.definition.EventListenerDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.listeners.ScriptingEventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.SendNotificationEventListenerDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.impl.datastore.MetaEntityStore;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.entity.l10n.MetaDataLocalizationStrategy;
import org.iplass.mtp.impl.entity.listener.MetaJavaClassEventListener;
import org.iplass.mtp.impl.entity.listener.MetaScriptingEventListener;
import org.iplass.mtp.impl.entity.listener.MetaSendNotificationEventListener;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.MetaReferenceProperty;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.properties.basic.BasicType;
import org.iplass.mtp.impl.properties.extend.AutoNumberType;
import org.iplass.mtp.impl.properties.extend.SelectType;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.validation.MetaValidation;
import org.iplass.mtp.impl.validation.MetaValidationNotNull;


public class MetaEntity extends BaseRootMetaData implements DefinableMetaData<EntityDefinition> {
	private static final long serialVersionUID = 6349144477923315083L;

	//TODO カスタマイズしたやつの場合、カスタマイズ前のMetaEntityのその時点のスナップショットを内部に持つ。というか、MetaDataレベルでもってよいかも。
	//TODO カスタマイズ時点の、MetaDataと、最新のMetaDataの差分をチェックして、カスタマイズ版にマージする。
	//TODO Resourceのバージョンアップの場合、patchツールにて、全テナントを更新。ただし、共有テナントがある場合、共有テナントで当該MetaDataをカスタマイズしていた場合、共有テナントのみ更新
	//TODO 共有テナントのMetaDataのローカルへの反映は、各テナントの判断（でよいか？運用が手間かも。。。やっぱり自動かな。。。結局、各テナントに反映しなかったら、共通ロジックで落ちるかもだから。）
	//TODO 現行のものはカスタマイズ前のMetaData持ってないので、その場合の考慮もする


	/** oidとして使用するPropertyの定義名。未指定の場合は、自動生成されるIDが使用される */
	private List<String> oidPropertyId;

	/** nameとして仕様するPropertyの定義名。指定があった場合は、insert,updateの際、当該Propertyの値が自動的にnameに設定される。 */
	private String namePropertyId;

	/** crawl対象のPropertyの定義名 */
	private List<String> crawlPropertyId;

	/** 継承元の定義ID */
	private String inheritedEntityMetaDataId;

	/** 汎用モデルをjavaクラスへマッピングする場合のマッピング定義 */
	private MetaEntityMapping mapping;

	/** 汎用モデルに定義されるプロパティのリスト */
	private List<MetaProperty> declaredPropertyList = new ArrayList<MetaProperty>();

	private List<MetaEventListener> eventListenerList = new ArrayList<MetaEventListener>();

	/** 物理的なDataModel定義（ストアタイプ毎に自動生成） */
	private MetaEntityStore entityStoreDefinition;

	/** StoreとEntityのマッピングの定義 */
	private MetaStoreMapping storeMapping;

	/** バージョン管理方式 */
	private VersionControlType versionControlType;

	/** クロール対象の可否 */
	private boolean crawl;

	/** クエリー結果キャッシュの有効化有無 */
	private boolean queryCache;

	private MetaDataLocalizationStrategy dataLocalizationStrategy;

	public MetaEntity() {
	}

	public MetaDataLocalizationStrategy getDataLocalizationStrategy() {
		return dataLocalizationStrategy;
	}

	public void setDataLocalizationStrategy(
			MetaDataLocalizationStrategy dataLocalizationStrategy) {
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

	public String getNamePropertyId() {
		return namePropertyId;
	}

	public void setNamePropertyId(String namePropertyId) {
		this.namePropertyId = namePropertyId;
	}

	public VersionControlType getVersionControlType() {
		return versionControlType;
	}

	public void setVersionControlType(VersionControlType versionControlType) {
		this.versionControlType = versionControlType;
	}

	public List<String> getOidPropertyId() {
		return oidPropertyId;
	}

	public void setOidPropertyId(List<String> oidPropertyId) {
		this.oidPropertyId = oidPropertyId;
	}

	public List<String> getCrawlPropertyId() {
		return crawlPropertyId;
	}

	public void setCrawlPropertyId(List<String> crawlPropertyId) {
		this.crawlPropertyId = crawlPropertyId;
	}

	public String getInheritedEntityMetaDataId() {
		return inheritedEntityMetaDataId;
	}

	public void setInheritedEntityMetaDataId(String inheritedEntityMetaDataId) {
		this.inheritedEntityMetaDataId = inheritedEntityMetaDataId;
	}

	public List<MetaProperty> getDeclaredPropertyList() {
		return declaredPropertyList;
	}

	public void setDeclaredPropertyList(List<MetaProperty> propList) {
		this.declaredPropertyList = propList;
	}

	public MetaProperty getDeclaredProperty(String propName) {

		if (declaredPropertyList == null) {
			return null;
		}

		for (MetaProperty p: declaredPropertyList) {
			if (propName.equals(p.getName())) {
				return p;
			}
		}
		return null;
	}

	public MetaProperty getDeclaredPropertyById(String propId) {

		if (declaredPropertyList == null) {
			return null;
		}

		for (MetaProperty p: declaredPropertyList) {
			if (propId.equals(p.getId())) {
				return p;
			}
		}
		return null;
	}

	public List<MetaEventListener> getEventListenerList() {
		return eventListenerList;
	}

	public void setEventListenerList(List<MetaEventListener> eventListenerList) {
		this.eventListenerList = eventListenerList;
	}

	public MetaEntityMapping getMapping() {
		return mapping;
	}

	public void setMapping(MetaEntityMapping mapping) {
		this.mapping = mapping;
	}


	public MetaEntityStore getEntityStoreDefinition() {
		return entityStoreDefinition;
	}

	public void setEntityStoreDefinition(MetaEntityStore entityStoreDefinition) {
		this.entityStoreDefinition = entityStoreDefinition;
	}

	public MetaStoreMapping getStoreMapping() {
		return storeMapping;
	}

	public void setStoreMapping(MetaStoreMapping storeMapping) {
		this.storeMapping = storeMapping;
	}

//	public String getDataStoreType() {
//		return dataStoreType;
//	}
//
//	public void setDataStoreType(String dataStoreType) {
//		this.dataStoreType = dataStoreType;
//	}

	public void applyConfig(EntityDefinition definition, EntityContext context, KeyGenerator keyGene) {

		if (id == null) {
			id = keyGene.generateId();
		}

		name = convertName(definition.getName());

		if (!definition.getName().equals(definition.getDisplayName())) {
			displayName = definition.getDisplayName();
		}

		description = definition.getDescription();

		if (definition.getInheritedDefinition() != null) {

			EntityHandler superEntity = context.getHandlerByName(definition.getInheritedDefinition());
			if (superEntity == null) {
				throw new EntityRuntimeException("metaData is not find. " + definition.getInheritedDefinition());
			}
			inheritedEntityMetaDataId = superEntity.getMetaData().getId();
		} else {
			inheritedEntityMetaDataId = EntityHandler.ROOT_ENTITY_ID;
		}

		if (definition.getMapping() != null) {
			mapping = new MetaEntityMapping(definition.getMapping().getMappingModelClass());
		} else {
			mapping = null;
		}

		List<MetaProperty> newDeclaredPropertyList = new ArrayList<MetaProperty>();
		if (definition.getPropertyList() != null) {
			for (PropertyDefinition pDef: definition.getPropertyList()) {
				if (!pDef.isInherited()) {
					MetaProperty pMeta = getDeclaredProperty(pDef.getName());

					if (pMeta == null) {
						if (pDef instanceof ReferenceProperty) {
							pMeta = new MetaReferenceProperty();
						} else {
							pMeta = new MetaPrimitiveProperty();
						}
						pMeta.setId(keyGene.generateId());
					}
					pMeta.applyConfig(pDef, context);
					newDeclaredPropertyList.add(pMeta);
				}
			}
		}
		declaredPropertyList = newDeclaredPropertyList;

		if (definition.getEventListenerList() != null) {
			eventListenerList = new ArrayList<MetaEventListener>();
			for (EventListenerDefinition ed: definition.getEventListenerList()) {
				//TODO instanceofの判断してしまっている
				if (ed instanceof ScriptingEventListenerDefinition) {
					MetaScriptingEventListener ms = new MetaScriptingEventListener();
					ms.applyConfig(ed);
					eventListenerList.add(ms);
				} else if (ed instanceof SendNotificationEventListenerDefinition) {
					MetaSendNotificationEventListener msn = new MetaSendNotificationEventListener();
					msn.applyConfig(ed);
					eventListenerList.add(msn);
				} else {
					MetaJavaClassEventListener ms = new MetaJavaClassEventListener();
					ms.applyConfig(ed);
					eventListenerList.add(ms);
				}
			}
		} else {
			eventListenerList = null;
		}

		// 言語毎の文字情報設定
		localizedDisplayNameList = I18nUtil.toMeta(definition.getLocalizedDisplayNameList());

		//SuperのEntityプロパティ情報を取得
		List<MetaProperty> superPropertyList = null;
		if (!EntityHandler.ROOT_ENTITY_ID.equals(getId())) {
			EntityHandler thisHandler = context.getHandlerById(getId());
			if (thisHandler != null) {	//Create時はnullの可能性あり
				EntityHandler superEntityHandler = thisHandler.getSuperDataModelHandler(context);
				if (superEntityHandler != null) {
					MetaEntity superEntity = superEntityHandler.getMetaData();
					superPropertyList = superEntity.declaredPropertyList;
				}
			} else {
				EntityHandler superEntityHandler;
					superEntityHandler = context.getHandlerById(inheritedEntityMetaDataId);
				if (superEntityHandler != null) {
					MetaEntity superEntity = superEntityHandler.getMetaData();
					superPropertyList = superEntity.declaredPropertyList;
				}
			}
		}

		//oidPropertyの設定
		if (definition.getOidPropertyName() != null) {
			if (declaredPropertyList == null && superPropertyList == null) {
				throw new EntityRuntimeException("oidProperty not found:" + definition.getOidPropertyName());
			}
			oidPropertyId = new ArrayList<String>();
			for (String oidPropName: definition.getOidPropertyName()) {
				boolean match = false;
				match = validateOidProperty(declaredPropertyList, oidPropName);
				if (!match && superPropertyList != null) {
					match = validateOidProperty(superPropertyList, oidPropName);
				}
				if (!match) {
					throw new EntityRuntimeException("oidProperty not found:" + oidPropName);
				}
			}
		} else {
			oidPropertyId = null;
		}

		//nameProperty
		namePropertyId = null;
		if (definition.getNamePropertyName() != null) {
			if (declaredPropertyList == null && superPropertyList == null) {
				throw new EntityRuntimeException("nameProperty not found:" + definition.getNamePropertyName());
			}
			boolean match = false;
			match = validateNameProperty(declaredPropertyList, definition.getNamePropertyName());
			if (!match && superPropertyList != null) {
				match = validateNameProperty(superPropertyList, definition.getNamePropertyName());
			}
			if (!match) {
				throw new EntityRuntimeException("nameProperty not found:" + definition.getNamePropertyName());
			}
		}

		//CrawlPropertyの設定
		if (definition.getCrawlPropertyName() != null) {
			if (declaredPropertyList == null && superPropertyList == null) {
				throw new EntityRuntimeException("crawlProperty not found:" + definition.getCrawlPropertyName());
			}
			crawlPropertyId = new ArrayList<String>();
			for (String crawlPropName: definition.getCrawlPropertyName()) {
				boolean match = false;
				match = validateCrawlProperty(declaredPropertyList, crawlPropName);
				if (!match && superPropertyList != null) {
					match = validateCrawlProperty(superPropertyList, crawlPropName);
				}
				if (!match) {
					throw new EntityRuntimeException("crawlProperty not found:" + crawlPropName);
				}
			}
		} else {
			crawlPropertyId = null;
		}

		versionControlType = definition.getVersionControlType();
		crawl = definition.isCrawl();
		queryCache = definition.isQueryCache();

		if (definition.getStoreDefinition() != null) {
			storeMapping = MetaStoreMapping.newInstance(definition.getStoreDefinition());
			storeMapping.applyConfig(definition.getStoreDefinition(), this);
		} else {
			storeMapping = null;
		}

		if (definition.getDataLocalizationStrategy() != null) {
			dataLocalizationStrategy = MetaDataLocalizationStrategy.newInstance(definition.getDataLocalizationStrategy());
			dataLocalizationStrategy.applyConfig(definition.getDataLocalizationStrategy());
		} else {
			dataLocalizationStrategy = null;
		}

	}

	private boolean validateOidProperty(List<MetaProperty> properties, String oidPropName) {
		boolean match = false;
		if (properties != null) {
			for (MetaProperty p: properties) {
				if (p.getName().equals(oidPropName)) {
					if (p instanceof MetaPrimitiveProperty) {
						MetaPrimitiveProperty pp = (MetaPrimitiveProperty) p;
						if (pp.getMultiplicity() != 1) {
							throw new EntityRuntimeException("oidProperty:" + oidPropName + " must multiplicity=1.");
						}
						if (!(pp.getType() instanceof BasicType || pp.getType() instanceof AutoNumberType)) {
							throw new EntityRuntimeException("oidProperty:" + oidPropName + " can not use for oidProperty.");
						}
						if (pp.isUpdatable()) {
							throw new EntityRuntimeException("oidProperty:" + oidPropName + " must updatable=false.");
						}
						if (!isRequiredPrimitiveProperty(pp)) {
							throw new EntityRuntimeException("oidProperty:" + oidPropName + " must not null.(must required)");
						}
						oidPropertyId.add(pp.getId());
					} else {
						throw new EntityRuntimeException("oidProperty:" + oidPropName + " must primitive type.");
					}
					match = true;
					break;
				}
			}
		}
		return match;
	}

	private boolean validateNameProperty(List<MetaProperty> properties, String namePropName) {
		boolean match = false;
		if (properties != null) {
			for (MetaProperty p: properties) {
				if (p.getName().equals(namePropName)) {
					if (p instanceof MetaPrimitiveProperty) {
						MetaPrimitiveProperty pp = (MetaPrimitiveProperty) p;
						if (pp.getMultiplicity() != 1) {
							throw new EntityRuntimeException("nameProperty:" + namePropName + " must multiplicity=1.");
						}
						if (!(pp.getType() instanceof BasicType || pp.getType() instanceof AutoNumberType || pp.getType() instanceof SelectType)) {
							throw new EntityRuntimeException("nameProperty:" + namePropName + " can not use for nameProperty.");
						}
						if (!isRequiredPrimitiveProperty(pp)) {
							throw new EntityRuntimeException("nameProperty:" + namePropName + " must not null.(must required)");
						}
						namePropertyId = pp.getId();
					} else {
						throw new EntityRuntimeException("nameProperty:" + namePropName + " must primitive type.");
					}
					match = true;
					break;
				}
			}
		}
		return match;
	}

	private boolean validateCrawlProperty(List<MetaProperty> properties, String crawlPropName) {
		boolean match = false;
		if (properties != null) {
			for (MetaProperty p: properties) {
				if (p.getName().equals(crawlPropName)) {
					crawlPropertyId.add(p.getId());
					match = true;
					break;
				}
			}
		}
		return match;
	}

	private boolean isRequiredPrimitiveProperty(MetaPrimitiveProperty pp) {
		if (pp.getType() instanceof AutoNumberType) {
			return true;
		}

		if (pp.getValidations() == null) {
			return false;
		}
		boolean isNotNullDef = false;
		for (MetaValidation mv: pp.getValidations()) {
			if (mv instanceof MetaValidationNotNull) {
				isNotNullDef = true;
				break;
			}
		}
		if (!isNotNullDef) {
			return false;
		}
		return true;
	}

	public EntityDefinition currentConfig(EntityContext context) {

		EntityDefinition def = new EntityDefinition();
		def.setName(getName());

		if (getDisplayName() != null) {
			def.setDisplayName(getDisplayName());
		} else {
			def.setDisplayName(getName());
		}

		def.setDescription(getDescription());

		EntityDefinition superEntity = null;
		List<MetaProperty> superPropertyList = null;
		if (!EntityHandler.ROOT_ENTITY_ID.equals(getId())) {
			EntityHandler thisHandler = context.getHandlerById(getId());
			EntityHandler superEntityHandler = thisHandler.getSuperDataModelHandler(context);
			if (superEntityHandler == null) {
				throw new EntityRuntimeException("metaData is not find.");
			}
			superEntity = superEntityHandler.getMetaData().currentConfig(context);
			superPropertyList = superEntityHandler.getMetaData().declaredPropertyList;
//			if (!superEntity.getName().equals(EntityDefinition.SYSTEM_DEFAULT_DEFINITION_NAME)) {
				def.setInheritedDefinition(superEntity.getName());
//			}
		}

		List<PropertyDefinition> propertyDefList = new ArrayList<PropertyDefinition>();
		if (superEntity != null) {
			for (PropertyDefinition pd: superEntity.getPropertyList()) {
				pd.setInherited(true);
				propertyDefList.add(pd);
			}
		}
		if (declaredPropertyList != null) {
			for (MetaProperty pMeta: declaredPropertyList) {
				PropertyDefinition pDef = pMeta.currentConfig(context);
				if (pDef != null) {
					propertyDefList.add(pDef);
				}
			}
		}
		def.setPropertyList(propertyDefList);

		if (eventListenerList != null) {
			for (MetaEventListener me: eventListenerList) {
				def.addEventListener(me.currentConfig());
			}
		}

		def.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));

		if (oidPropertyId != null) {
			ArrayList<String> oidNames = new ArrayList<String>();
			for (String oidPropId: oidPropertyId) {
				boolean match = false;
				if (declaredPropertyList != null) {
					for (MetaProperty pMeta: declaredPropertyList) {
						if (pMeta.getId().equals(oidPropId)) {
							oidNames.add(pMeta.getName());
							match = true;
							break;
						}
					}
				}
				if (!match && superPropertyList != null) {
					for (MetaProperty pMeta: superPropertyList) {
						if (pMeta.getId().equals(oidPropId)) {
							oidNames.add(pMeta.getName());
							match = true;
							break;
						}
					}
				}

			}
			def.setOidPropertyName(oidNames);
		}

		if (namePropertyId != null) {
			boolean match = false;
			if (declaredPropertyList != null) {
				for (MetaProperty pMeta: declaredPropertyList) {
					if (pMeta.getId().equals(namePropertyId)) {
						def.setNamePropertyName(pMeta.getName());
						match = true;
						break;
					}
				}
			}
			if (!match && superPropertyList != null) {
				for (MetaProperty pMeta: superPropertyList) {
					if (pMeta.getId().equals(namePropertyId)) {
						def.setNamePropertyName(pMeta.getName());
						match = true;
						break;
					}
				}
			}
		}

		if (crawlPropertyId != null) {
			ArrayList<String> crawlNames = new ArrayList<String>();
			for (String crawlPropId: crawlPropertyId) {
				boolean match = false;
				if (declaredPropertyList != null) {
					for (MetaProperty pMeta: declaredPropertyList) {
						if (pMeta.getId().equals(crawlPropId)) {
							crawlNames.add(pMeta.getName());
							match = true;
							break;
						}
					}
				}
				if (!match && superPropertyList != null) {
					for (MetaProperty pMeta: superPropertyList) {
						if (pMeta.getId().equals(crawlPropId)) {
							crawlNames.add(pMeta.getName());
							match = true;
							break;
						}
					}
				}

			}
			def.setCrawlPropertyName(crawlNames);
		}

		if (mapping != null) {
			def.setMapping(new EntityMapping(mapping.getMappingClass()));
		}

		if (versionControlType == null) {
			def.setVersionControlType(VersionControlType.NONE);
		} else {
			def.setVersionControlType(versionControlType);
		}
		def.setCrawl(crawl);
		def.setQueryCache(queryCache);

		if (storeMapping != null) {
			def.setStoreDefinition(storeMapping.currentConfig(this));
		}

		if (dataLocalizationStrategy != null) {
			def.setDataLocalizationStrategy(dataLocalizationStrategy.currentConfig());
		}

		return def;
	}

	public MetaEntity copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (crawl ? 1231 : 1237);
		result = prime * result
				+ ((crawlPropertyId == null) ? 0 : crawlPropertyId.hashCode());
		result = prime
				* result
				+ ((dataLocalizationStrategy == null) ? 0
						: dataLocalizationStrategy.hashCode());
		result = prime
				* result
				+ ((declaredPropertyList == null) ? 0 : declaredPropertyList
						.hashCode());
		result = prime
				* result
				+ ((entityStoreDefinition == null) ? 0 : entityStoreDefinition
						.hashCode());
		result = prime
				* result
				+ ((eventListenerList == null) ? 0 : eventListenerList
						.hashCode());
		result = prime
				* result
				+ ((inheritedEntityMetaDataId == null) ? 0
						: inheritedEntityMetaDataId.hashCode());
		result = prime * result + ((mapping == null) ? 0 : mapping.hashCode());
		result = prime * result
				+ ((namePropertyId == null) ? 0 : namePropertyId.hashCode());
		result = prime * result
				+ ((oidPropertyId == null) ? 0 : oidPropertyId.hashCode());
		result = prime * result + (queryCache ? 1231 : 1237);
		result = prime * result
				+ ((storeMapping == null) ? 0 : storeMapping.hashCode());
		result = prime
				* result
				+ ((versionControlType == null) ? 0 : versionControlType
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaEntity other = (MetaEntity) obj;
		if (crawl != other.crawl)
			return false;
		if (crawlPropertyId == null) {
			if (other.crawlPropertyId != null)
				return false;
		} else if (!crawlPropertyId.equals(other.crawlPropertyId))
			return false;
		if (dataLocalizationStrategy == null) {
			if (other.dataLocalizationStrategy != null)
				return false;
		} else if (!dataLocalizationStrategy
				.equals(other.dataLocalizationStrategy))
			return false;
		if (declaredPropertyList == null) {
			if (other.declaredPropertyList != null)
				return false;
		} else if (!declaredPropertyList.equals(other.declaredPropertyList))
			return false;
		if (entityStoreDefinition == null) {
			if (other.entityStoreDefinition != null)
				return false;
		} else if (!entityStoreDefinition.equals(other.entityStoreDefinition))
			return false;
		if (eventListenerList == null) {
			if (other.eventListenerList != null)
				return false;
		} else if (!eventListenerList.equals(other.eventListenerList))
			return false;
		if (inheritedEntityMetaDataId == null) {
			if (other.inheritedEntityMetaDataId != null)
				return false;
		} else if (!inheritedEntityMetaDataId
				.equals(other.inheritedEntityMetaDataId))
			return false;
		if (mapping == null) {
			if (other.mapping != null)
				return false;
		} else if (!mapping.equals(other.mapping))
			return false;
		if (namePropertyId == null) {
			if (other.namePropertyId != null)
				return false;
		} else if (!namePropertyId.equals(other.namePropertyId))
			return false;
		if (oidPropertyId == null) {
			if (other.oidPropertyId != null)
				return false;
		} else if (!oidPropertyId.equals(other.oidPropertyId))
			return false;
		if (queryCache != other.queryCache)
			return false;
		if (storeMapping == null) {
			if (other.storeMapping != null)
				return false;
		} else if (!storeMapping.equals(other.storeMapping))
			return false;
		if (versionControlType != other.versionControlType)
			return false;
		return true;
	}

	public EntityHandler createRuntime(MetaDataConfig metaDataConfig) {

		return new EntityHandler(this, metaDataConfig);
	}

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

	@Override
	public void applyConfig(EntityDefinition definition) {
		// FIXME #apllyConfig(EntityDefinition, EntityConetxt, KeyGenerator)

	}

	@Override
	public EntityDefinition currentConfig() {
		EntityContext ec = EntityContext.getCurrentContext();
		return currentConfig(ec);
	}
}
