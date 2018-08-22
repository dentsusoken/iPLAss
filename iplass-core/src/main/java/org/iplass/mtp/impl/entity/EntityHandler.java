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

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityApplicationException;
import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.entity.EntityLockedByUserException;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateCondition.UpdateValue;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.ReferenceType;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.hint.FetchSizeHint;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.datastore.DataStore;
import org.iplass.mtp.impl.datastore.EntityStoreRuntime;
import org.iplass.mtp.impl.datastore.MetaEntityStore;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.strategy.EntityStoreStrategy;
import org.iplass.mtp.impl.datastore.strategy.RecycleBinIterator;
import org.iplass.mtp.impl.datastore.strategy.SearchResultIterator;
import org.iplass.mtp.impl.entity.MetaEventListener.EventListenerRuntime;
import org.iplass.mtp.impl.entity.builder.EntityBuilder;
import org.iplass.mtp.impl.entity.interceptor.EntityDeleteInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityLoadInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityQueryInvocationImpl;
import org.iplass.mtp.impl.entity.l10n.MetaDataLocalizationStrategy.DataLocalizationStrategyRuntime;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferenceSortSpec;
import org.iplass.mtp.impl.entity.versioning.DeleteTarget;
import org.iplass.mtp.impl.entity.versioning.VersionedQueryNormalizer;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.properties.extend.AutoNumberType;
import org.iplass.mtp.impl.properties.extend.BinaryType;
import org.iplass.mtp.impl.properties.extend.ComplexWrapperType;
import org.iplass.mtp.impl.properties.extend.LongTextType;
import org.iplass.mtp.impl.properties.extend.SelectType;
import org.iplass.mtp.impl.properties.extend.select.Value;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * データモデルの特定の型の管理を扱うクラス。
 * DataModelHandlerのインスタンスは、
 * 各データモデルかつテナント単位に1インスタンス生成され、
 * 複数のスレッドから同時に使用されるため、クライアント依存のデータは保持しない。
 *
 * @author K.Higuchi
 *
 */
public class EntityHandler extends BaseMetaDataRuntime {

	public static final String ROOT_ENTITY_ID = "/entity/Entity";

	private static Logger log = LoggerFactory.getLogger(EntityHandler.class);

	private boolean isRoot;

	private MetaEntity metaData;
	private DataStore dataStore;

	private EntityStoreRuntime entityStoreRuntime;

	private List<PropertyHandler> propertyHandlers;
	private Map<String, PropertyHandler> propertyHandlerMap;
	private Map<String, PropertyHandler> propertyHandlerMapById;
	private List<EventListenerRuntime> eventListenerHandlers;

	private Class<? extends Entity> mappingClass;

	private EntityService service;

	private MetaDataConfig metaDataConfig;

	/** 多言語化用文字情報リスト */
	private Map<String, String> localizedStringMap;

	private DataLocalizationStrategyRuntime dataLocalizationStrategyRuntime;

//	public EntityHandler() {
//	}

	public EntityHandler(MetaEntity metaData, MetaDataConfig metaDataConfig) {
		try {
			this.metaData = metaData;

			this.metaDataConfig = metaDataConfig;

			propertyHandlers = new ArrayList<PropertyHandler>();
			propertyHandlerMap = new HashMap<String, PropertyHandler>();
			propertyHandlerMapById = new HashMap<String, PropertyHandler>();
			eventListenerHandlers = new ArrayList<EventListenerRuntime>();
			localizedStringMap = new HashMap<String, String>();

			dataStore = ServiceRegistry.getRegistry().getService(StoreService.class).getDataStore();

			MetaEntityStore mes = metaData.getEntityStoreDefinition();
			if (mes != null) {
				if (!mes.getClass().equals(dataStore.getEntityStoreType())) {
					throw new IllegalStateException("missmatch dataStore type in config:" + dataStore.getEntityStoreType() + " and metadata(" + metaData.getName() + "):" + mes.getClass());
				}
				entityStoreRuntime = mes.createRuntime(this);
			}

			if (metaData.getDeclaredPropertyList() != null) {
				for (MetaProperty pDef: metaData.getDeclaredPropertyList()) {
					PropertyHandler pHandler = pDef.createRuntime(metaData);
					propertyHandlers.add(pHandler);
					propertyHandlerMap.put(pDef.getName(), pHandler);
					propertyHandlerMapById.put(pDef.getId(), pHandler);
					pHandler.setParent(this);
				}
			}

			if (metaData.getId().equals(ROOT_ENTITY_ID)) {
				isRoot = true;
			}

			if (metaData.getEventListenerList() != null) {
				for (MetaEventListener e: metaData.getEventListenerList()) {
					eventListenerHandlers.add(e.createRuntime(metaData));
				}
			}

			if (metaData.getMapping() != null) {
				mappingClass = classForName(metaData.getMapping().getMappingClass());
			}

			if (metaData.getLocalizedDisplayNameList() != null) {
				for (MetaLocalizedString mls : metaData.getLocalizedDisplayNameList()) {
					localizedStringMap.put(mls.getLocaleName(), mls.getStringValue());
				}
			}

			if (metaData.getDataLocalizationStrategy() != null) {
				dataLocalizationStrategyRuntime = metaData.getDataLocalizationStrategy().createDataLocalizationStrategyRuntime(this);
			}

			service = ServiceRegistry.getRegistry().getService(EntityService.class);
		} catch (RuntimeException e) {
			setIllegalStateException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Entity> classForName(String className) {
		Class<? extends Entity> c = null;
		try {
			c = (Class<? extends Entity>) Class.forName(className);
			if (!Entity.class.isAssignableFrom(c)) {
				log.error("Illegal definition on " + metaData.getName() + ". Entity Mapping Class:" +  className + " must implements Entity interface.");
			}
		} catch (ClassNotFoundException e) {
			log.error("MappingClass:" + className + " Not Found on " + metaData.getName());
		}
		return c;
	}

	public DataLocalizationStrategyRuntime getDataLocalizationStrategyRuntime() {
		return dataLocalizationStrategyRuntime;
	}

	public EntityStoreRuntime getEntityStoreRuntime() {
		return entityStoreRuntime;
	}

	public boolean isUseSharedPermission() {
		return metaDataConfig.isPermissionSharable();
	}

	public boolean isUseSharedMetaData() {
		if (!metaDataConfig.isSharable()) {
			return false;
		}
		MetaDataEntry ent = MetaDataContext.getContext().getMetaDataEntryById(getMetaData().getId());
		if (ent.getRepositryType() == RepositoryType.SHARED) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isUseSharedData() {
		if (!metaDataConfig.isDataSharable()) {
			return false;
		}

		MetaDataEntry ent = MetaDataContext.getContext().getMetaDataEntryById(getMetaData().getId());
		if (ent.getRepositryType() == RepositoryType.SHARED) {
			return true;
		} else {
			return false;
		}
	}

	public Map<String, String> getLocalizedStringMap() {
		return localizedStringMap;
	}

	public void setLocalizedStringMap(Map<String, String> localizedStringMap) {
		this.localizedStringMap = localizedStringMap;
	}

	public Entity newInstance() {
		checkState();

		Entity res = null;
		if (mappingClass != null) {
			try {
				res = mappingClass.newInstance();
			} catch (InstantiationException e) {
				throw new EntityRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new EntityRuntimeException(e);
			}
		} else {
			res = new GenericEntity();
		}
		res.setDefinitionName(getMetaData().getName());
		return res;
	}

	public Entity[] newArrayInstance(int size) {
		checkState();

		Entity[] res = null;
		if (mappingClass != null) {
			res = (Entity[]) Array.newInstance(mappingClass, size);
		} else {
			res = new Entity[size];
		}
		return res;
	}

	public EntityService getService() {
		return service;
	}
	public List<EventListenerRuntime> getEventListenerHandlers() {
		return eventListenerHandlers;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public MetaEntity getMetaData() {
		return metaData;
	}

	public PropertyHandler getDeclaredProperty(String propName) {
		checkState();

		if (propertyHandlerMap == null) {
			return null;
		}
		return propertyHandlerMap.get(propName);
	}

	public List<PropertyHandler> getDeclaredPropertyList() {
		checkState();

		if (propertyHandlers == null) {
			return Collections.emptyList();
		}
		return propertyHandlers;
	}

	public EntityHandler getSuperDataModelHandler(EntityContext context) {
//		checkState();

		if (isRoot) {
			return null;
		}

		String superEntityMetaDataId = metaData.getInheritedEntityMetaDataId();
		if (superEntityMetaDataId == null) {
			return context.getHandlerById(ROOT_ENTITY_ID);
		}

		EntityHandler superHandler = context.getHandlerById(superEntityMetaDataId);
		if (superHandler == null) {
			throw new EntityRuntimeException("super entity definition is not defined.");
		}
		return superHandler;
	}


	public PropertyHandler getProperty(String propName,
			EntityContext context) {
		checkState();

		PropertyHandler p = getDeclaredProperty(propName);
		if (p == null) {
			EntityHandler superHandler = getSuperDataModelHandler(context);
			if (superHandler != null) {
				p = superHandler.getProperty(propName, context);
			}
		}
		return p;
	}

	public PropertyHandler getPropertyCascade(String propName,
			EntityContext context) {
		checkState();

		int indexOfDot = propName.indexOf('.');
		if (indexOfDot > -1) {
			String objPropName = propName.substring(0, indexOfDot);
			String subPropPath = propName.substring(indexOfDot + 1, propName.length());

			PropertyHandler prop = getProperty(objPropName, context);
			if (!(prop instanceof ReferencePropertyHandler)) {
				throw new IllegalArgumentException("path is invalid:" + objPropName + " is not ObjectReferenceProperty of " + getMetaData().getName());//TODO 例外クラスの検討
			}
			ReferencePropertyHandler refProp = (ReferencePropertyHandler) prop;
			EntityHandler refHandler = refProp.getReferenceEntityHandler(context);
			if (refHandler == null) {
				throw new EntityRuntimeException(objPropName +  "'s Entity is not defined.");//TODO 例外クラスの検討
			}
			return refHandler.getPropertyCascade(subPropPath, context);

		} else {
			return getProperty(propName, context);
		}
	}

	public List<PropertyHandler> getPropertyListByPropertyType(Class<? extends PropertyType> type, EntityContext context) {
		checkState();

		ArrayList<PropertyHandler> propList = null;
		EntityHandler superHandler = getSuperDataModelHandler(context);
		if (superHandler != null) {
			List<PropertyHandler> parentPropList = superHandler.getPropertyListByPropertyType(type, context);
			if (parentPropList.size() > 0) {
				if (propList == null) {
					propList = new ArrayList<>();
				}
				propList.addAll(parentPropList);
			}
		}
		for (PropertyHandler ph: getDeclaredPropertyList()) {
			if (ph instanceof PrimitivePropertyHandler
					&& type.isAssignableFrom(((PrimitivePropertyHandler) ph).getMetaData().getType().getClass())) {
				if (propList == null) {
					propList = new ArrayList<>();
				}
				propList.add(ph);
			}
		}
		if (propList == null) {
			return Collections.emptyList();
		} else {
			return propList;
		}
	}

	public List<PrimitivePropertyHandler> getIndexedPropertyList(EntityContext context) {
		checkState();

		ArrayList<PrimitivePropertyHandler> propList = null;
		EntityHandler superHandler = getSuperDataModelHandler(context);
		if (superHandler != null) {
			List<PrimitivePropertyHandler> parentPropList = superHandler.getIndexedPropertyList(context);
			if (parentPropList.size() > 0) {
				if (propList == null) {
					propList = new ArrayList<>();
				}
				propList.addAll(parentPropList);
			}
		}
		for (PropertyHandler ph: getDeclaredPropertyList()) {
			if (ph instanceof PrimitivePropertyHandler
					&& !((PrimitivePropertyHandler) ph).getMetaData().getType().isVirtual()
					&& ((PrimitivePropertyHandler) ph).getMetaData().getIndexType() != null
					&& ((PrimitivePropertyHandler) ph).getMetaData().getIndexType() != IndexType.NON_INDEXED) {
				if (propList == null) {
					propList = new ArrayList<>();
				}
				propList.add((PrimitivePropertyHandler) ph);
			}
		}
		if (propList == null) {
			return Collections.emptyList();
		} else {
			return propList;
		}
	}

	public List<ReferencePropertyHandler> getReferencePropertyList(ReferenceType type, EntityContext context) {
		checkState();

		ArrayList<ReferencePropertyHandler> propList = null;
		EntityHandler superHandler = getSuperDataModelHandler(context);
		if (superHandler != null) {
			List<ReferencePropertyHandler> parentPropList = superHandler.getReferencePropertyList(type, context);
			if (parentPropList.size() > 0) {
				if (propList == null) {
					propList = new ArrayList<>();
				}
				propList.addAll(parentPropList);
			}
		}
		for (PropertyHandler ph: getDeclaredPropertyList()) {
			if (ph instanceof ReferencePropertyHandler) {
				ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
				if (type == null
						|| type == rph.getMetaData().getReferenceType()) {
					if (propList == null) {
						propList = new ArrayList<>();
					}
					propList.add(rph);
				}
			}
		}
		if (propList == null) {
			return Collections.emptyList();
		} else {
			return propList;
		}
	}

	public List<ReferencePropertyHandler> getReferencePropertyList(boolean withoutMappedBy, EntityContext context) {
		checkState();

		ArrayList<ReferencePropertyHandler> propList = null;
		EntityHandler superHandler = getSuperDataModelHandler(context);
		if (superHandler != null) {
			List<ReferencePropertyHandler> parentPropList = superHandler.getReferencePropertyList(withoutMappedBy, context);
			if (parentPropList.size() > 0) {
				if (propList == null) {
					propList = new ArrayList<>();
				}
				propList.addAll(parentPropList);
			}
		}
		for (PropertyHandler ph: getDeclaredPropertyList()) {
			if (ph instanceof ReferencePropertyHandler) {
				ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
				if (!withoutMappedBy || rph.getMetaData().getMappedByPropertyMetaDataId() == null) {
					if (propList == null) {
						propList = new ArrayList<>();
					}
					propList.add(rph);
				}
			}
		}
		if (propList == null) {
			return Collections.emptyList();
		} else {
			return propList;
		}
	}

	public List<PropertyHandler> getPropertyList(EntityContext context) {
		checkState();

		EntityHandler superHandler = getSuperDataModelHandler(context);
		if (superHandler == null) {
			return getDeclaredPropertyList();
		} else {
			ArrayList<PropertyHandler> propList = new ArrayList<PropertyHandler>();
			propList.addAll(superHandler.getPropertyList(context));
			propList.addAll(getDeclaredPropertyList());
			return propList;
		}
	}

	public List<ValidateError> validate(Entity model, List<String> updateValue) {
		return validateInternal(model, updateValue, getMetaData().getNamePropertyId() != null);
	}

	private List<ValidateError> validateInternal(Entity model, List<String> updateValue, boolean isNamePropSpecify) {
		checkState();

		EntityContext context = EntityContext.getCurrentContext();

		//4.メタデータの定義に従い、プロパティのバリデーションの実施
		//  [エラーの場合]例外をスロー（例外内部にエラー内容を含める）

		ArrayList<ValidateError> validateResults = new ArrayList<ValidateError>();

		//プロパティのチェック
		EntityHandler superHandler = getSuperDataModelHandler(context);
		if (superHandler != null) {
			validateResults.addAll(superHandler.validateInternal(model, updateValue, isNamePropSpecify));
		}

		if (propertyHandlers != null) {
			for (PropertyHandler propertyHandler: propertyHandlers) {
				if (updateValue == null
						|| updateValue.contains(propertyHandler.getName())) {
					if (propertyHandler.getName().equals(Entity.NAME)) {
						//nameの場合、自動で設定する場合は、必須チェックしない
						if (!isNamePropSpecify) {
							ValidateError valRes = propertyHandler.validate(model);
							if (valRes != null) {
								validateResults.add(valRes);
							}
						}
					} else {
						ValidateError valRes = propertyHandler.validate(model);
						if (valRes != null) {
							validateResults.add(valRes);
						}
					}
				}
			}
		}
		return validateResults;
	}

	private void checkLimitOfReferences(Entity e, ReferencePropertyHandler rh, EntityContext entityContext) {
		if (rh.getMetaData().getMappedByPropertyMetaDataId() == null
				&& rh.getMetaData().getMultiplicity() != 1) {
			Entity[] refList = (Entity[]) e.getValue(rh.getName());
			if (refList != null) {
				if (refList.length > service.getLimitOfReferences()) {
					throw new EntityRuntimeException("reference count is over the " + service.getLimitOfReferences() + " limit. EntityDefinition:" + e.getDefinitionName() + ", ReferenceName:" + rh.getName());
				}
			}
		}
	}

	public String insertDirect(Entity entity, EntityContext entityContext) {
		checkState();

		//参照数のチェック
		for (PropertyHandler ph: getDeclaredPropertyList()) {
			if (ph instanceof ReferencePropertyHandler) {
				checkLimitOfReferences(entity, (ReferencePropertyHandler) ph, entityContext);
			}
		}

		List<PropertyHandler> propList = getPropertyListByPropertyType(ComplexWrapperType.class, entityContext);
		preprocessInsertDirect(entity, entityContext, propList);

		EntityStoreStrategy insertStrategy = getStrategy();
		String oid = insertStrategy.insert(entityContext, this, entity);

		return oid;
	}

	/**
	 * Entityデータを追加します。
	 *
	 * @param entity Entityデータ
	 * @param option
	 * @return 登録OID
	 */
	public String insert(Entity entity, InsertOption option) {
		checkState();

		ExecuteContext mtfContext = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();

		Entity copyEntity = ((GenericEntity) entity).copy();

		//システム設定項目のセット
		copyEntity.setCreateBy(mtfContext.getClientId());
		copyEntity.setUpdateBy(mtfContext.getClientId());
		if (copyEntity.getState() == null) {
			copyEntity.setState(new SelectValue(Entity.STATE_VALID_VALUE));
		}
		copyEntity.setCreateDate(null);
		copyEntity.setUpdateDate(null);

		if (option.isRegenerateOid()) {
			//自動採番するためnullセット
			copyEntity.setOid(null);
		}

		List<PropertyHandler> pList = getPropertyListByPropertyType(AutoNumberType.class, entityContext);
		if (option.isRegenerateAutoNumber()) {
			//自動採番するためnullセット
			for (PropertyHandler ph: pList) {
				copyEntity.setValue(ph.getName(), null);
			}
		}

		//バージョンコントロール種別毎の追加処理
		service.getVersionController(this).normalizeForInsert(copyEntity, entityContext);

		//参照先のバージョンのチェック＆セット
		for (PropertyHandler ph: getDeclaredPropertyList()) {
			if (ph instanceof ReferencePropertyHandler
					&& ((ReferencePropertyHandler) ph).getMetaData().getMappedByPropertyMetaDataId() == null) {
				if (copyEntity.getValue(ph.getName()) != null) {
					ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
					if (rph.getMetaData().getMultiplicity() != 1) {
						copyEntity.setValue(rph.getName(),
								service.getVersionController(rph.getReferenceEntityHandler(entityContext)).normalizeRefEntity((Entity[]) copyEntity.getValue(rph.getName()), rph, entityContext));
					} else {
						Entity[] holder = new Entity[1];
						holder[0] = (Entity) copyEntity.getValue(rph.getName());
						if (holder[0] != null) {
							copyEntity.setValue(rph.getName(),
									service.getVersionController(rph.getReferenceEntityHandler(entityContext)).normalizeRefEntity(holder, rph, entityContext)[0]);
						}
					}
				}
			}
		}

		String oid = insertDirect(copyEntity, entityContext);

		//TODO oidだけでよいか？？？timestampは？
		entity.setOid(oid);
		entity.setVersion(0L);

		//auto numberを通知
		if (pList != null) {
			for (PropertyHandler ph: pList) {
				entity.setValue(ph.getName(), copyEntity.getValue(ph.getName()));
			}
		}

		return oid;
	}

	//WapperTypeの変換前の状態で取得するためのメソッド
	private Entity getEntityWithPropList(String oid, Long version, List<PropertyHandler> pList, EntityContext entityContext) {
		Query q = new Query();
		q.select().add(Entity.OID);
		for (PropertyHandler p: pList) {
			if (p instanceof PrimitivePropertyHandler) {
				q.select().add(p.getName());
			}
		}

		if (q.getSelect() != null && q.getSelect().getSelectValues() != null && q.getSelect().getSelectValues().size() != 0) {
			q.from(getMetaData().getName());
			q.where(new And(new Equals(Entity.OID, oid), new Equals(Entity.VERSION, version)));
			q.select().addHint(new FetchSizeHint(1));
			SearchResultIterator it = getStrategy().search(entityContext, q, this);
			try {
				if (it.next()) {
					Entity ret = getAsEntity(this, it, q);
					return ret;
				} else {
					return null;
				}
			} finally {
				it.close();
			}
		} else {
			return null;
		}
	}

	private String toName(Object val, PrimitivePropertyHandler pph) {
		PropertyType type = pph.getMetaData().getType();
		if (type instanceof SelectType) {
			SelectType st = (SelectType) type;
			List<Value> stvList = st.runtimeValues();
			if (stvList != null) {
				for (Value vDef: stvList) {
					if (vDef.getValue().equals(((SelectValue) val).getValue())) {
						//多言語対応
						String lang = ExecuteContext.getCurrentContext().getLanguage();
						if (lang != null && vDef.getLocalizedDisplayNameList() != null) {
							for (MetaLocalizedString mls: vDef.getLocalizedDisplayNameList()) {
								if (mls.getLocaleName().equals(lang)) {
									return mls.getStringValue();
								}
							}
						}
						return vDef.getDisplayName();
					}
				}
			}
			throw new EntityRuntimeException(((SelectValue) val).getValue() + " not define at " + pph.getName());
		} else {
			return val.toString();
		}
	}

	void preprocessInsertDirect(Entity entity, EntityContext entityContext, List<PropertyHandler> complexWrapperTypePropList) {
		//AutoNumberTypeが、name,oidに利用されている場合、このタイミングで採番
		if (entity.getOid() == null) {
			if (getMetaData().getOidPropertyId() == null || getMetaData().getOidPropertyId().size() == 0) {
				String oid = getStrategy().newOid(entityContext, this);
				entity.setOid(oid);
			} else {
				//oidに生成されたidをセット
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < getMetaData().getOidPropertyId().size(); i++) {
					if (i != 0) {
						sb.append("-");
					}
					PrimitivePropertyHandler pph = (PrimitivePropertyHandler) getDeclaredPropertyById(getMetaData().getOidPropertyId().get(i));
					Object oidVal = entity.getValue(pph.getName());
					if (oidVal == null) {
						//special logic for AutoNumber,,
						if (pph.getMetaData().getType() instanceof AutoNumberType) {
							AutoNumberType autoNum = (AutoNumberType) pph.getMetaData().getType();
							oidVal = autoNum.toStoreTypeValue(null, null, pph, this, null, entity.getVersion(), entity);
							entity.setValue(pph.getName(), oidVal);
						} else {
							throw new EntityRuntimeException(pph.getName() + " must not null.");
						}
					}
					sb.append(pph.getMetaData().getType().toString(oidVal));
				}
				entity.setOid(sb.toString());
			}
		}

		if (getMetaData().getNamePropertyId() != null) {
			PrimitivePropertyHandler pph = (PrimitivePropertyHandler) getDeclaredPropertyById(getMetaData().getNamePropertyId());
			Object nameVal = entity.getValue(pph.getName());
			//special logic for AutoNumber,,
			if (nameVal == null) {
				if (pph.getMetaData().getType() instanceof AutoNumberType) {
					AutoNumberType autoNum = (AutoNumberType) pph.getMetaData().getType();
					nameVal = autoNum.toStoreTypeValue(null, null, pph, this, null, entity.getVersion(), entity);
					entity.setValue(pph.getName(), nameVal);
				} else {
					throw new EntityRuntimeException(pph.getName() + " must not null.");
				}
			}
			entity.setName(toName(nameVal, pph));
		}

		if (entity.getVersion() == null) {
			entity.setVersion(Long.valueOf(0));
		}
		//WrapperTypeの事前変換
		for (PropertyHandler ph: complexWrapperTypePropList) {
			ComplexWrapperType type = (ComplexWrapperType) ((PrimitivePropertyHandler) ph).getMetaData().getType();
			Object val = entity.getValue(ph.getName());
			if (val == null
					|| ph.getMetaData().getMultiplicity() == 1) {
				entity.setValue(ph.getName(), type.toStoreTypeValue(val, null, ph, this, entity.getOid(), entity.getVersion(), entity));
			} else {
				Object[] newArrayVal = (Object[]) Array.newInstance(type.storeType(), ((Object[]) val).length);
				for (int i = 0; i < newArrayVal.length; i++) {
					newArrayVal[i] = type.toStoreTypeValue(((Object[]) val)[i], null, ph, this, entity.getOid(), entity.getVersion(), entity);
				}
				entity.setValue(ph.getName(), newArrayVal);
			}
		}
	}

	/**
	 *
	 * @param entity
	 * @param option
	 * @param entityContext
	 * @param complexWrapperTypePropList
	 * @param checkPrevExists
	 * @return 確実に既存Entityが存在する場合true、存在するか否か不明な場合false
	 */
	boolean preprocessUpdateDirect(Entity entity, UpdateOption option, EntityContext entityContext, List<PropertyHandler> complexWrapperTypePropList, boolean checkPrevExists) {

		boolean prevExists = false;

		//既存（tool）はversionセットしてない可能性あるので、、
		if (entity.getVersion() == null) {
			entity.setVersion(Long.valueOf(0));
		}

		//更新対象のものに絞る。
		if (option != null) {
			//TODO できれば、copyEntityからも、updateしない値は取り除いておいた方が無難か
			List<PropertyHandler> updatePropList = new ArrayList<PropertyHandler>();
			for (String pName: option.getUpdateProperties()) {
				for (PropertyHandler ph: complexWrapperTypePropList) {
					if (ph.getName().equals(pName)) {
						updatePropList.add(ph);
					}
				}
			}
			complexWrapperTypePropList = updatePropList;
		}


		//更新前のLobの保存状況を取得
		if (complexWrapperTypePropList.size() > 0) {

			//更新前のEntityが必要かチェック
			boolean needPrevEntity = false;
			for (PropertyHandler ph: complexWrapperTypePropList) {
				ComplexWrapperType type = (ComplexWrapperType) ((PrimitivePropertyHandler) ph).getMetaData().getType();
				needPrevEntity = needPrevEntity || type.isNeedPrevStoreTypeValueOnToStoreTypeValue();
			}

			Entity beforeUpdateEntity = null;
			if (needPrevEntity) {
				beforeUpdateEntity = getEntityWithPropList(entity.getOid(), entity.getVersion(), complexWrapperTypePropList, entityContext);
				if (beforeUpdateEntity == null && checkPrevExists) {
					throw new EntityConcurrentUpdateException(resourceString("impl.core.EntityHandler.alreadyOperated", getMetaData().getDisplayName()));
				}
				prevExists = beforeUpdateEntity != null;
			}

			for (PropertyHandler ph: complexWrapperTypePropList) {
				ComplexWrapperType type = (ComplexWrapperType) ((PrimitivePropertyHandler) ph).getMetaData().getType();
				Object val = entity.getValue(ph.getName());
				Object beforeVal = null;
				if (type.isNeedPrevStoreTypeValueOnToStoreTypeValue()) {
					beforeVal = beforeUpdateEntity.getValue(ph.getName());
				}
				if (ph.getMetaData().getMultiplicity() == 1) {
					entity.setValue(ph.getName(), type.toStoreTypeValue(val, beforeVal, ph, this, entity.getOid(), entity.getVersion(), entity));
				} else {//FIXME ロジックみなおし！！！！
					int count = 0;
					Object[] newArrayVal = null;
					if (val != null) {
						newArrayVal = (Object[]) Array.newInstance(type.storeType(), ((Object[]) val).length);
						count = newArrayVal.length;
					}

					Object[] oldArrayVal = (Object[]) beforeVal;
					if (oldArrayVal != null && count < oldArrayVal.length) {
						count = oldArrayVal.length;
					}
					for (int i = 0; i < count; i++) {
						Object newVal = null;
						if (val != null && newArrayVal != null && i < newArrayVal.length) {
							newVal = ((Object[]) val)[i];
						}
						Object oldVal = null;
						if (oldArrayVal != null && i < oldArrayVal.length) {
							oldVal = oldArrayVal[i];
						}
						Object ret = type.toStoreTypeValue(newVal, oldVal, ph, this, entity.getOid(), entity.getVersion(), entity);
						if (newArrayVal != null && i < newArrayVal.length) {
							newArrayVal[i] = ret;
						}
					}
					entity.setValue(ph.getName(), newArrayVal);
				}
			}
		}

		//namePropertyが更新される場合、その値をnameにコピー
		if (getMetaData().getNamePropertyId() != null) {
			PrimitivePropertyHandler pph = (PrimitivePropertyHandler) getDeclaredPropertyById(getMetaData().getNamePropertyId());
			if (option == null ||
					option.getUpdateProperties() != null && option.getUpdateProperties().contains(pph.getName())) {
				Object val = entity.getValue(pph.getName());
				if (val == null) {
					throw new EntityRuntimeException(pph.getName() + " must not null.");
				}
				entity.setName(toName(val, pph));
				if (option != null && !option.getUpdateProperties().contains(Entity.NAME)) {
					option.getUpdateProperties().add(Entity.NAME);
				}
			}
		}

		return prevExists;

	}

	public void updateDirect(Entity entity, UpdateOption option, EntityContext entityContext) {
		checkState();

		if (entity.getOid() == null) {
			throw new NullPointerException("oid is null");
		}
		//参照数のチェック
		for (PropertyHandler ph: getDeclaredPropertyList()) {
			if (ph instanceof ReferencePropertyHandler
					&& option.getUpdateProperties().contains(ph.getName())) {
				checkLimitOfReferences(entity, (ReferencePropertyHandler) ph, entityContext);
			}
		}

		//WrapperTypeの事前変換
		List<PropertyHandler> propList = getPropertyListByPropertyType(ComplexWrapperType.class, entityContext);
		preprocessUpdateDirect(entity, option, entityContext, propList, true);

		EntityStoreStrategy updateStrategy = getStrategy();
		updateStrategy.update(entityContext, this, entity, option);

	}

	public void update(Entity entity, UpdateOption option) {
		checkState();

		ExecuteContext context = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();


		//5.メタデータの定義に従い、データを保存。
		//6.オブジェクトIDをリターン

		//更新可能項目かどうかチェック
		for (String propName: option.getUpdateProperties()) {
			PropertyHandler ph = getProperty(propName, entityContext);
			if (ph == null) {
				throw new EntityRuntimeException(propName + " is not defined on " + getMetaData().getName());
			}
			if (ph instanceof ReferencePropertyHandler
					&& ((ReferencePropertyHandler) ph).getMetaData().getMappedByPropertyMetaDataId() != null) {
				throw new EntityApplicationException(resourceString("impl.core.EntityHandler.notChange", getProperty(propName, entityContext).getMetaData().getDisplayName()));
			}
			if (ph instanceof PrimitivePropertyHandler && ((MetaPrimitiveProperty) ph.getMetaData()).getType().isVirtual()) {
				throw new EntityApplicationException(resourceString("impl.core.EntityHandler.notChange", getProperty(propName, entityContext).getMetaData().getDisplayName()));
			}

			if (option.isWithValidation()) {
				if (!ph.getMetaData().isUpdatable()) {
					throw new EntityApplicationException(resourceString("impl.core.EntityHandler.notChange", getProperty(propName, entityContext).getMetaData().getDisplayName()));
				}
			} else {
				//withValidation=falseの場合は、キー項目のみ更新不可
				if (ph instanceof PrimitivePropertyHandler) {
					if (propName.equals(Entity.OID) || propName.equals(Entity.VERSION)) {
						throw new EntityApplicationException(resourceString("impl.core.EntityHandler.notChange", getProperty(propName, entityContext).getMetaData().getDisplayName()));
					}
				}
			}
		}

//FIXME 一括削除、更新との競合を考慮し、ここでロックを取得する。1件削除の時も。

		Entity beforeEntity = null;
		List<ReferencePropertyHandler> cascadeDelTarget = cascadeDeleteTarget(option, entityContext);
		if (option.isCheckLockedByUser() || cascadeDelTarget != null) {
			if (cascadeDelTarget == null) {
				beforeEntity = new EntityLoadInvocationImpl(entity.getOid(), entity.getVersion(), new LoadOption(false, false), false, service.getInterceptors(), this).proceed();
			} else {
				List<String> refs = new ArrayList<>(cascadeDelTarget.size());
				for (ReferencePropertyHandler rph: cascadeDelTarget) {
					refs.add(rph.getName());
				}
				beforeEntity = new EntityLoadInvocationImpl(entity.getOid(), entity.getVersion(), new LoadOption(refs), false, service.getInterceptors(), this).proceed();
			}
			if (beforeEntity == null) {
				//FIXME すべてが無効データの場合の考慮が必要
				throw new EntityConcurrentUpdateException(resourceString("impl.core.EntityHandler.alreadyOperated", getMetaData().getDisplayName()));
			}
		}

		//当該ユーザがロックしているもの、もしくは他ユーザにロックされていないことを確認
		String lockedByUser = null;
		if (option.isCheckLockedByUser()) {
			//oid単位でロックがされる前提で、、
			lockedByUser = beforeEntity.getLockedBy();
			if (lockedByUser != null) {
				if (!lockedByUser.equals(context.getClientId())) {
					throw new EntityLockedByUserException(resourceString("impl.core.EntityHandler.locked"));
				}
			}
		}

		Entity copyEntity = ((GenericEntity) entity).copy();

		//システム項目をセット
		//TODO これをセットする場所を再検討
		if (!option.getUpdateProperties().contains(Entity.UPDATE_BY)) {
			option.getUpdateProperties().add(Entity.UPDATE_BY);
		}
		copyEntity.setUpdateBy(context.getClientId());
		if (copyEntity.getState() == null) {
			copyEntity.setState(new SelectValue(Entity.STATE_VALID_VALUE));
		}
		//TODO セット必要か？？
		if (lockedByUser != null) {
			copyEntity.setLockedBy(lockedByUser);
		}

		//参照先のバージョンのチェック＆セット
		for (String propName: option.getUpdateProperties()) {
			PropertyHandler ph = getProperty(propName, entityContext);
			if (ph instanceof ReferencePropertyHandler
					&& ((ReferencePropertyHandler) ph).getMetaData().getMappedByPropertyMetaDataId() == null) {
				ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
				if (rph.getMetaData().getMultiplicity() != 1) {
					copyEntity.setValue(rph.getName(),
							service.getVersionController(rph.getReferenceEntityHandler(entityContext)).normalizeRefEntity((Entity[]) copyEntity.getValue(rph.getName()), rph, entityContext));
				} else {
					Entity[] holder = new Entity[1];
					holder[0] = (Entity) copyEntity.getValue(rph.getName());
					if (holder[0] != null) {
						copyEntity.setValue(rph.getName(),
								service.getVersionController(rph.getReferenceEntityHandler(entityContext)).normalizeRefEntity(holder, rph, entityContext)[0]);
					}
				}
			}
		}

		//バージョン制御別の処理
		service.getVersionController(this).update(copyEntity, option, this, entityContext);

		//COMPOSITIONの参照Entityのカスケードデリート
		if (cascadeDelTarget != null) {
			DeleteOption cascadeDelOption = new DeleteOption(false);
			cascadeDelOption.setPurge(option.isPurgeCompositionedEntity());
			for (ReferencePropertyHandler rph: cascadeDelTarget) {
				if (rph.getMetaData().getReferenceType() == ReferenceType.COMPOSITION) {
					EntityHandler reh = rph.getReferenceEntityHandler(entityContext);
					Object beforeVal = beforeEntity.getValue(rph.getName());
					if (beforeVal != null) {
						Entity[] delEntities = null;
						if (rph.getMetaData().getMultiplicity() != 1) {
							delEntities = service.getVersionController(reh).getCascadeDeleteTargetForUpdate((Entity[]) copyEntity.getValue(rph.getName()), (Entity[]) beforeVal, rph, beforeEntity, this, entityContext);
						} else {
							Entity[] holder = new Entity[1];
							holder[0] = (Entity) copyEntity.getValue(rph.getName());
							if (holder[0] == null) {
								holder = null;
							}
							Entity[] beforeHolder = new Entity[1];
							beforeHolder[0] = (Entity) beforeVal;
							delEntities = service.getVersionController(reh).getCascadeDeleteTargetForUpdate(holder, beforeHolder, rph, beforeEntity, this, entityContext);
						}
						if (delEntities != null) {
							for (Entity val: delEntities) {
								//カスケードデリート先のEventLisnterなどのInterceptorを呼び出したいので
								EntityDeleteInvocationImpl deleteInvocation =
									new EntityDeleteInvocationImpl(val,
											cascadeDelOption,
											service.getInterceptors(),
											reh);
								deleteInvocation.proceed();
							}
						}
					}
				}
			}
		}

		//バージョンを通知
		entity.setVersion(copyEntity.getVersion());
	}

	private List<ReferencePropertyHandler> cascadeDeleteTarget(UpdateOption option, EntityContext entityContext) {
		List<ReferencePropertyHandler> ret = null;
		for (String propName: option.getUpdateProperties()) {
			PropertyHandler ph = getProperty(propName, entityContext);
			if (ph instanceof ReferencePropertyHandler
					&& ((ReferencePropertyHandler) ph).getMetaData().getMappedByPropertyMetaDataId() == null) {
				ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
				if (rph.getMetaData().getReferenceType() == ReferenceType.COMPOSITION) {
					if (ret == null) {
						ret = new LinkedList<>();
					}
					ret.add(rph);
				}
			}
		}

		return ret;
	}

	public void purge(Long rbid) {
		checkState();

		ExecuteContext context = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();

		final String[] oid = new String[1];
		getRecycleBin(
				new Predicate<Entity>() {
					@Override
					public boolean test(Entity dataModel) {
						oid[0] = dataModel.getOid();
						return false;
					}
				}, rbid);


		if (oid[0] == null) {
			throw new EntityConcurrentUpdateException(resourceString("impl.core.EntityHandler.alreadyRestored"));
		}

		getStrategy().deleteFromRecycleBin(entityContext, this, rbid, context.getClientId());

		//WrapperTypeへパージ通知
		//oid単位に1回だけ
		List<PropertyHandler> wrapperPropList = getPropertyListByPropertyType(ComplexWrapperType.class, entityContext);
		if (wrapperPropList.size() > 0) {
			HashSet<Class<? extends ComplexWrapperType>> called = new HashSet<Class<? extends ComplexWrapperType>>();
			for (PropertyHandler ph: wrapperPropList) {
				ComplexWrapperType type = (ComplexWrapperType) ((PrimitivePropertyHandler) ph).getMetaData().getType();
				if (!called.contains(type.getClass())) {
					type.notifyAfterPurge(this, rbid);
					called.add(type.getClass());
				}
			}
		}
	}

	public Entity restore(Long rbid) {
		checkState();

		ExecuteContext context = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();

		final String[] oid = new String[1];
		getRecycleBin(
				new Predicate<Entity>() {
					@Override
					public boolean test(Entity dataModel) {
						oid[0] = dataModel.getOid();
						return false;
					}
				}, rbid);


		if (oid[0] == null) {
			throw new EntityConcurrentUpdateException(resourceString("impl.core.EntityHandler.alreadyRestored"));
		}

		getStrategy().copyFromRecycleBin(entityContext, this, rbid, context.getClientId());
		getStrategy().deleteFromRecycleBin(entityContext, this, rbid, context.getClientId());
		//WrapperTypeへリストア通知
		//oid単位に1回だけ
		List<PropertyHandler> wrapperPropList = getPropertyListByPropertyType(ComplexWrapperType.class, entityContext);
		if (wrapperPropList.size() > 0) {
			HashSet<Class<? extends ComplexWrapperType>> called = new HashSet<Class<? extends ComplexWrapperType>>();
			for (PropertyHandler ph: wrapperPropList) {
				ComplexWrapperType type = (ComplexWrapperType) ((PrimitivePropertyHandler) ph).getMetaData().getType();
				if (!called.contains(type.getClass())) {
					type.notifyAfterRestore(this, rbid);
					called.add(type.getClass());
				}
			}
		}
		return new EntityLoadInvocationImpl(oid[0], null, null, false, service.getInterceptors(), this).proceed();
	}

	public void getRecycleBin(Predicate<Entity> callback, Long rbid) {
		checkState();

		EntityContext entityContext = EntityContext.getCurrentContext();
		RecycleBinIterator it = getStrategy().getRecycleBin(entityContext, this, rbid);
		try {
			while (it.next()) {
				Entity e = newInstance();
				e.setOid(it.getOid());
				e.setName(it.getName());
				e.setRecycleBinId(it.getRbid());
				e.setUpdateDate(it.getRbDate());
				e.setUpdateBy(it.getRbUser());
				if (!callback.test(e)) {
					break;
				}
			}
		} finally {
			it.close();
		}
	}

	public int countRecycleBin(Timestamp ts) {
		checkState();

		EntityContext entityContext = EntityContext.getCurrentContext();
		return getStrategy().countRecycleBin(entityContext, this, ts);
	}

	public void delete(Entity entity, DeleteOption option) {
		checkState();

		//FIXME ほかのオブジェクトから参照されていないかをチェックする。逆参照をたどらなければなので、逆参照を素早く検出可能な仕組みを考える。（逆参照の情報をテーブルandキャッシュで保持か？）

		ExecuteContext execContext = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();

		boolean isLocked = getStrategy().lock(entityContext, this, entity.getOid());
		if (!isLocked) {
			//既に存在しないoid
			if (option.isCheckTimestamp()) {
				throw new EntityConcurrentUpdateException(resourceString("impl.core.EntityHandler.alreadyOperated", getMetaData().getDisplayName()));
			} else {
				return;
			}
		}

		//当該ユーザがロックしているもの、もしくは他ユーザにロックされていないことを確認
		//oid単位でロックがされる前提で、、
		//TODO ReferencePropertyはカスケードデリート対象のみをロードする形に
		Entity beforeEntity = new EntityLoadInvocationImpl(entity.getOid(), entity.getVersion(), new LoadOption(true, false), false, service.getInterceptors(), this).proceed();
		if (beforeEntity == null) {
			//FIXME すべてが無効データの場合の考慮が必要
			throw new EntityConcurrentUpdateException(resourceString("impl.core.EntityHandler.alreadyOperated", getMetaData().getDisplayName()));
		}
		String lockedByUser = beforeEntity.getLockedBy();
		if (lockedByUser != null && option.isCheckLockedByUser()) {
			if (!lockedByUser.equals(execContext.getClientId())) {
				throw new EntityLockedByUserException(resourceString("impl.core.EntityHandler.locked"));
			}
		}

		List<ReferencePropertyHandler> refPropList = getReferencePropertyList(ReferenceType.COMPOSITION, entityContext);
		Map<String, Set<String>> cascadeDelMap = new HashMap<String, Set<String>>();
		if (refPropList.size() > 0) {
			for (ReferencePropertyHandler rph: refPropList) {
				EntityHandler refEh = rph.getReferenceEntityHandler(entityContext);
				String[] cascadeOids = service.getVersionController(refEh).getCascadeDeleteTarget(entity, this, rph, entityContext);
				if (cascadeOids != null) {
					Set<String> oids = cascadeDelMap.get(refEh.getMetaData().getName());
					if (oids == null) {
						oids = new HashSet<String>();
						cascadeDelMap.put(refEh.getMetaData().getName(), oids);
					}
					oids.addAll(Arrays.asList(cascadeOids));
				}
			}
		}

		//更新者をセット
		entity.setUpdateBy(execContext.getClientId());
		entity.setLockedBy(beforeEntity.getLockedBy());

		deleteDirect(entity, option, entityContext);

		//カスケード削除対象のEntityを削除。タイムスタンプチェックは行わない。
		if (cascadeDelMap.size() > 0) {
			DeleteOption cascadeDelOption = new DeleteOption(false);
			cascadeDelOption.setPurge(option.isPurge());

			for (Map.Entry<String, Set<String>> oidsEntry: cascadeDelMap.entrySet()) {
				if (oidsEntry.getValue().size() > 0) {
					for (String oid: oidsEntry.getValue()) {
						Entity val = new GenericEntity(oidsEntry.getKey(), oid, null);
						//カスケードデリート先のEventLisnterなどのInterceptorを呼び出したいので
						EntityDeleteInvocationImpl deleteInvocation =
							new EntityDeleteInvocationImpl(val,
									cascadeDelOption,
									service.getInterceptors(),
									entityContext.getHandlerByName(oidsEntry.getKey()));
						deleteInvocation.proceed();
					}
				}
			}
		}
	}

	Entity preporcessDeleteDirect(Entity entity, EntityContext entityContext, List<PropertyHandler> complexWrapperTypePropList) {
		Entity beforeDeleteEntity = null;
		//更新前のEntityが必要かチェック
		boolean needPrevEntity = false;
		for (PropertyHandler ph: complexWrapperTypePropList) {
			ComplexWrapperType type = (ComplexWrapperType) ((PrimitivePropertyHandler) ph).getMetaData().getType();
			needPrevEntity = needPrevEntity || type.isNeedPrevStoreTypeValueOnToStoreTypeValue();
		}

		if (needPrevEntity) {
			beforeDeleteEntity = getEntityWithPropList(entity.getOid(), entity.getVersion(), complexWrapperTypePropList, entityContext);
		}
		return beforeDeleteEntity;

	}

	void postporcessDeleteDirect(Entity beforeDeleteEntity, EntityContext entityContext, List<PropertyHandler> complexWrapperTypePropList, Long rbid) {
		//WrapperTypeへの削除通知
		if (complexWrapperTypePropList.size() > 0) {
			for (PropertyHandler ph: complexWrapperTypePropList) {
				ComplexWrapperType type = (ComplexWrapperType) ((PrimitivePropertyHandler) ph).getMetaData().getType();

				if (beforeDeleteEntity != null) {
					Object val = beforeDeleteEntity.getValue(ph.getName());
					if (val == null || ph.getMetaData().getMultiplicity() == 1) {
						type.notifyAfterDelete(val, ph, this, beforeDeleteEntity.getOid(), rbid);
					} else {
						Object[] arrayVal = (Object[]) val;
						for (int i = 0; i < arrayVal.length; i++) {
							type.notifyAfterDelete(arrayVal[i], ph, this, beforeDeleteEntity.getOid(), rbid);
						}
					}
				}
			}
		}

	}

	private void deleteDirect(Entity entity, DeleteOption option, EntityContext entityContext) {
		checkState();

		//TODO 複数バージョン分ループしている。効率はよくない。そのうち、パフォーマンス対策が必要になるか、、
		DeleteTarget[] delTarget = service.getVersionController(this).getDeleteTarget(entity, option, this, entityContext);

		Long rbid = null;
		if (delTarget != null && delTarget.length != 0) {
			//ごみ箱に保存
			if(!option.isPurge()) {
				rbid = getStrategy().copyToRecycleBin(entityContext, this, entity.getOid(), entity.getUpdateBy());
			}
		}

		List<PropertyHandler> complexWrapperTypePropList = getPropertyListByPropertyType(ComplexWrapperType.class, entityContext);

		for (DeleteTarget dt: delTarget) {
			Entity delEntity = new GenericEntity(getMetaData().getName());
			delEntity.setOid(dt.oid);
			delEntity.setVersion(dt.version);
			delEntity.setUpdateDate(dt.updateDate);

			//更新者をセット
			delEntity.setUpdateBy(entity.getUpdateBy());

			Entity beforeDeleteEntity = preporcessDeleteDirect(delEntity, entityContext, complexWrapperTypePropList);

			//データを削除
			EntityStoreStrategy deleteStrategy = getStrategy();
			deleteStrategy.delete(entityContext, delEntity, this, option);

			postporcessDeleteDirect(beforeDeleteEntity, entityContext, complexWrapperTypePropList, rbid);
		}
	}

	public boolean lock(String oid) {
		checkState();

		EntityContext entityContext = EntityContext.getCurrentContext();
		return getStrategy().lock(entityContext, this, oid);
	}

	public Entity load(final String oid, final Long version, final LoadOption option, boolean withLock) {
		checkState();

		final ExecuteContext mtfContext = ExecuteContext.getCurrentContext();
		final EntityContext entityContext = EntityContext.getCurrentContext();

		//TODO セキュリティ上、更新可能なデータかをチェックする必要ある
		if (withLock) {
			if (!getStrategy().lock(entityContext, this, oid)) {
				throw new EntityConcurrentUpdateException(resourceString("impl.core.EntityHandler.alreadyDeleted"));
			}
		}

		//TODO loadの子のインスタンスを取得する処理は、Managerへ移動する。そうするとセキュリティ処理もMaangerで完結する。

		//件数が読めない（複数の参照定義があると掛け算になる）ので、multipleな参照先のEntityは個別のsqlで持ってくる。

		ArrayList<String> select = new ArrayList<String>();
		ArrayList<String> refSingle = new ArrayList<String>();
		final ArrayList<ReferencePropertyHandler> refList = new ArrayList<ReferencePropertyHandler>();

		Query searchCond = new Query();
		searchCond.from(getMetaData().getName());
		for (PropertyHandler p: getPropertyList(entityContext)) {
			//LoadOptionで、loadPropertiesが指定されている場合は、そのpropertyのみ取得
			if (p instanceof ReferencePropertyHandler) {
				ReferencePropertyHandler rp = (ReferencePropertyHandler) p;
				//optionにより、参照先をロードするかを決定
				if (option == null
						|| (option.getLoadReferences() != null && option.getLoadReferences().contains(p.getName()))
						|| option.getLoadReferences() == null
						&& option.isWithReference()
						&& (rp.getMetaData().getMappedByPropertyMetaDataId() == null
							|| option.isWithMappedByReference())) {
					//check reference valid
					if (rp.getReferenceEntityHandler(entityContext) != null) {
						if (rp.getMetaData().getMultiplicity() != 1) {
							refList.add((ReferencePropertyHandler) p);
						} else {
							refSingle.add(p.getName());
							select.add(rp.getName() + "." + Entity.OID);
							select.add(rp.getName() + "." + Entity.NAME);
							select.add(rp.getName() + "." + Entity.VERSION);
						}
					}
				}
			} else {
				select.add(p.getName());
			}
		}
		searchCond.select(select.toArray(new Object[select.size()]));

		if (version == null) {
			searchCond.where(new Equals(Entity.OID, oid));
		} else {
			searchCond.where(new And(
					new Equals(Entity.OID, oid),
					new Equals(Entity.VERSION, version)));
		}

		searchCond.select().addHint(new FetchSizeHint(1));

		//インナークラスとの受け渡し用
		final Entity[] result = new Entity[]{null};
		//セキュリティロジックを通したいので、Invocation経由でQuery呼び出し
		new EntityQueryInvocationImpl(searchCond, new SearchOption().unnotifyListeners(), new Predicate<Entity>() {
			@Override
			public boolean test(Entity val) {
				result[0] = val;

				if (refList.size() != 0) {
					for (ReferencePropertyHandler refProp: refList) {
						setLoadRef(val, refProp, mtfContext, entityContext);
					}
				}
				return true;
			}
		}, InvocationType.SEARCH_ENTITY, getService().getInterceptors(), this).proceed();

		return result[0];
	}

	private void setLoadRef(final Entity e, final ReferencePropertyHandler refProp,
			final ExecuteContext mtfContext, final EntityContext entityContext) {


		Query searchCond = new Query();
		searchCond.from(getMetaData().getName());
		searchCond.select(refProp.getName() + "." + Entity.OID, refProp.getName() + "." + Entity.VERSION, refProp.getName() + "." + Entity.NAME);

		//TODO このあたりも、結合条件をQueryにて指定できるようにして、それによってリファクタリングする
		//refrerenceの設定（レコードタイムか最新かにより、条件（versionを付与するか、）を変更）
		//StoreStorategyでは、
		//VersionControlReferenceType.CURRENT_BASE（デフォルト）の場合oidのみで結合、
		//VersionControlReferenceType.RECORD_BASEの場合oidとversionで結合の形。
		//CURRENT_BASEの場合は、バージョン制御種別により、絞りこみ条件が必要となる。
		And whereCond = new And(new Equals(Entity.OID, e.getOid()), new Equals(Entity.VERSION, e.getVersion()));
//		if (refProp.getMetaData().getVersionControlType() == null
//				|| refProp.getMetaData().getVersionControlType() == VersionControlReferenceType.CURRENT_BASE) {
//			Condition refLimitingCond = service.getVersionController(refProp.getReferenceEntityHandler(entityContext)).refEntityQueryCondition(refProp.getName(), refProp, entityContext);
//			if (refLimitingCond != null) {
//				whereCond.addExpression(refLimitingCond);
//			}
//		}
		searchCond.where(whereCond);
		if (refProp.getMetaData().getOrderBy() != null && refProp.getMetaData().getOrderBy().size() > 0) {
			//TODO 事前にパース可能か？
			OrderBy q = new OrderBy();
			for (ReferenceSortSpec rss: refProp.getMetaData().getOrderBy()) {
				PropertyHandler ph = refProp.getReferenceEntityHandler(entityContext).getPropertyById(rss.getSortPropertyMetaDataId(), entityContext);
				if (ph != null && !(ph instanceof ReferencePropertyHandler)) {
					searchCond.getSelect().add(refProp.getName() + "." + ph.getName());
					if (rss.getSortType() == ReferenceSortSpec.SortType.DESC) {
						q.add(new SortSpec(refProp.getName() + "." + ph.getName(), SortType.DESC));
					} else {
						q.add(new SortSpec(refProp.getName() + "." + ph.getName(), SortType.ASC));
					}
				}
			}
			if (q.getSortSpecList() != null && q.getSortSpecList().size() > 0) {
				searchCond.setOrderBy(q);
			}
		}
//		searchCond.setVersiond(true);

		final ArrayList<Entity> refList = new ArrayList<Entity>();
		//セキュリティロジックを通したいので、Invocation経由でQuery
		new EntityQueryInvocationImpl(searchCond, new SearchOption().unnotifyListeners(), new Predicate<Object[]>() {
			@Override
			public boolean test(Object[] val) {
				//1件もない場合、参照間はOUTER JOINなので、1件だけnullの値が返ってくる。。。。
				if (val[0] != null) {
					Entity refEntity = refProp.getReferenceEntityHandler(entityContext).newInstance();
					refEntity.setValue(Entity.OID, val[0]);
					refEntity.setValue(Entity.VERSION, val[1]);
					refEntity.setValue(Entity.NAME, val[2]);
					refList.add(refEntity);
				}
				return true;
			}
		}, InvocationType.SEARCH, service.getInterceptors(), this).proceed();
		if (refList.size() != 0) {
			e.setValue(refProp.getName(), refList.toArray(refProp.getReferenceEntityHandler(entityContext).newArrayInstance(refList.size())));
		}
	}

	private VirtualPropertyAdapter createExtendPropertyAdapter(Query cond, EntityContext entityContext) {
		return service.getExtendPropertyAdapterFactory().create(cond, entityContext, this);
	}

	public void search(Query cond, EntityStreamSearchHandler<Object[]> streamSearchHandler, Predicate<Object[]> callback) {
		checkState();

		EntityContext entityContext = EntityContext.getCurrentContext();

		//バージョン制御用にクエリー変換
		if (!cond.isVersiond()) {
			cond = (Query) new VersionedQueryNormalizer(service.getVersionController(this), this, entityContext, null).visit(cond);
			if (log.isDebugEnabled()) {
				log.debug("translate to versioned query:" + cond);
			}
		}

		VirtualPropertyAdapter vpa = createExtendPropertyAdapter(cond, entityContext);
		if (streamSearchHandler != null) {
			//検索結果をセット
			vpa.setIterator(getStrategy().search(entityContext, vpa.getTransformedQuery(), this));
			streamSearchHandler.setStreamSearchResult(cond, vpa, callback);
		} else {
			//検索結果をiterate
			try {
				vpa.setIterator(getStrategy().search(entityContext, vpa.getTransformedQuery(), this));

				while (vpa.next()) {

					List<ValueExpression> select = cond.getSelect().getSelectValues();
					Object[] row = new Object[select.size()];
					for (int i = 0; i < row.length; i++) {
						row[i] = vpa.getValue(select.get(i));
					}

					if (!callback.test(row)) {
						break;
					}

				}
			} finally {

				log.trace("iterate done");

				if (vpa != null) {
					vpa.close();
				}
			}
		}
	}

	public void searchEntity(Query query, boolean structuredEntity, EntityStreamSearchHandler<Entity> streamSearchHandler, Predicate<Entity> searchCallback) {
		checkState();

		ExecuteContext mtfContext = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();
		searchEntity(query, structuredEntity, mtfContext, entityContext, streamSearchHandler, searchCallback);
	}

	public int count(Query query) {
		checkState();

		EntityContext entityContext = EntityContext.getCurrentContext();

		//バージョン制御用にクエリー変換
		if (!query.isVersiond()) {
			query = (Query) new VersionedQueryNormalizer(service.getVersionController(this), this, entityContext, null).visit(query);
			if (log.isDebugEnabled()) {
				log.debug("translate to versioned query:" + query);
			}
		}

		VirtualPropertyAdapter vpa = createExtendPropertyAdapter(query, entityContext);
		return getStrategy().count(entityContext, vpa.getTransformedQuery());
	}

	private void searchEntity(Query cond, boolean structuredEntity, ExecuteContext mtfContext, EntityContext entityContext, EntityStreamSearchHandler<Entity> streamSearchHandler, Predicate<Entity> callback) {

		//バージョン制御用にクエリー変換
		if (!cond.isVersiond()) {
			cond = (Query) new VersionedQueryNormalizer(service.getVersionController(this), this, entityContext, null).visit(cond);
			if (log.isDebugEnabled()) {
				log.debug("translate to versioned query:" + cond);
			}
		}

		VirtualPropertyAdapter vpa = createExtendPropertyAdapter(cond, entityContext);
		if (streamSearchHandler != null) {
			//検索結果をセット
			vpa.setIterator(getStrategy().search(entityContext, vpa.getTransformedQuery(), this));
			streamSearchHandler.setStreamSearchResult(cond, vpa, callback);
		} else {
			//検索結果をiterate
			try {
				vpa.setIterator(getStrategy().search(entityContext, vpa.getTransformedQuery(), this));

				EntityHandler handler = null;
				if (getMetaData().getName().equals(cond.getFrom().getEntityName())) {
					handler = this;
				} else {
					handler = entityContext.getHandlerByName(cond.getFrom().getEntityName());
				}

				if (structuredEntity) {
					EntityBuilder builder = new EntityBuilder(this, entityContext, cond.getSelect().getSelectValues());
					while (vpa.next()) {
						List<ValueExpression> select = cond.getSelect().getSelectValues();
						Object[] row = new Object[select.size()];
						for (int i = 0; i < row.length; i++) {
							row[i] = vpa.getValue(select.get(i));
						}

						builder.handle(row);
					}
					builder.finished();
					for (Entity entity: builder.getCollection()) {
						if (!callback.test(entity)) {
							break;
						}
					}

				} else {
					while (vpa.next()) {

						Entity entity = getAsEntity(handler, vpa, cond);

						if (!callback.test(entity)) {
							break;
						}
					}
				}

			} finally {
				log.trace("iterate done");

				if (vpa != null) {
					vpa.close();
				}
			}
		}
	}

	public PropertyHandler getDeclaredPropertyById(String id) {
		checkState();

		if (propertyHandlerMap == null) {
			return null;
		}
		return propertyHandlerMapById.get(id);
	}

	public PropertyHandler getPropertyById(String id, EntityContext context) {
		checkState();

		PropertyHandler p = getDeclaredPropertyById(id);
		if (p == null) {
			EntityHandler superHandler = getSuperDataModelHandler(context);
			if (superHandler != null) {
				p = superHandler.getPropertyById(id, context);
			}
		}
		return p;
	}

	//TODO entityStoreRuntimeに移動。
	public EntityStoreStrategy getStrategy() {
		checkState();

		EntityStoreStrategy strategy = dataStore.getEntityStoreStrategy();
		if (strategy == null) {
			throw new EntityRuntimeException("operation not supported");
		}
		return strategy;
	}


	static Entity getAsEntity(EntityHandler eh, SearchResultIterator it, Query query) {

		Entity model = eh.newInstance();

		for (ValueExpression propName: query.getSelect().getSelectValues()) {
			if (propName instanceof EntityField) {
				EntityField ef = (EntityField) propName;
				model.setValue(ef.getPropertyName(), it.getValue(ef));
			}
		}
		return model;
	}

	public Integer updateAll(UpdateCondition cond) {
		checkState();

		ExecuteContext context = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();
		//定義上、参照、又はLOBへの参照を持っている場合は、一括更新対象外とする。

		for (UpdateValue uv: cond.getValues()) {
			PropertyHandler ph = getProperty(uv.getEntityField(), entityContext);
			if (ph instanceof PrimitivePropertyHandler) {
				//更新可能項目かどうかチェック
				if (cond.isCheckUpdatable()) {
					if (((PrimitivePropertyHandler) ph).getMetaData().getType() instanceof ComplexWrapperType) {
						throw new EntityRuntimeException("can not updateAll because not support of Reference type or LOB type or AutoNumber type");
					}
					if (getMetaData().getNamePropertyId() != null &&
							ph.getName().equals(getMetaData().getNamePropertyId())) {
						throw new EntityRuntimeException("can not updateAll because not support of nameProperty-ed property");
					}
					if (ph instanceof PrimitivePropertyHandler && ((MetaPrimitiveProperty) ph.getMetaData()).getType().isVirtual()) {
						throw new EntityRuntimeException("can not updateAll because not support of Expression Type");
					}
					if (!ph.getMetaData().isUpdatable()) {
						throw new EntityApplicationException(resourceString("impl.core.EntityHandler.notChange", ph.getMetaData().getDisplayName()));
					}
				} else {
					if (((PrimitivePropertyHandler) ph).getMetaData().getType() instanceof ComplexWrapperType
							&& !(((PrimitivePropertyHandler) ph).getMetaData().getType() instanceof AutoNumberType)) {
						//AutoNumはOK
						throw new EntityRuntimeException("can not updateAll because not support of Reference type or LOB type");
					}
					if (ph instanceof PrimitivePropertyHandler && ((MetaPrimitiveProperty) ph.getMetaData()).getType().isVirtual()) {
						throw new EntityRuntimeException("can not updateAll because not support of Expression Type");
					}

					//checkUpdatable=falseの場合は、キー項目のみ更新不可
					if (ph.getName().equals(Entity.OID) || ph.getName().equals(Entity.VERSION)) {
						throw new EntityApplicationException(resourceString("impl.core.EntityHandler.notChange", ph.getMetaData().getDisplayName()));
					}
				}

			} else {
				throw new EntityRuntimeException("can not updateAll because not support of Reference");
			}
		}

		return getStrategy().updateAll(cond, entityContext, this, context.getClientId());
	}

	public boolean canDeleteAll() {
		checkState();

		EntityContext entityContext = EntityContext.getCurrentContext();

		//定義上、カスケードデリート、又はLOBへの参照を持っている場合は、一括削除対象外とする。
		List<PropertyHandler> pList = getPropertyList(entityContext);
		for (PropertyHandler ph: pList) {
			if (ph instanceof PrimitivePropertyHandler) {
				PropertyType pType = ((PrimitivePropertyHandler) ph).getMetaData().getType();
				if (pType instanceof BinaryType || pType instanceof LongTextType) {
					return false;
				}
			} else if (((ReferencePropertyHandler) ph).getMetaData().getReferenceType() == ReferenceType.COMPOSITION) {
				return false;
			}
		}

		return true;
	}

	public Integer deleteAll(DeleteCondition cond) {
		checkState();

		//FIXME 現状のdeleteの実装だと、メタデータの定義が変更された場合を考慮していない。変更前のLOB、カスケードデリート先が残ってしまう可能性がある。

		ExecuteContext context = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();
		if (!canDeleteAll()) {
			throw new EntityRuntimeException("can not deleteAll because not support of COMPOSITION type or LOB type");
		}

		return getStrategy().deleteAll(cond, entityContext, this, context.getClientId());
	}

	public boolean unlockEntityByUser(String oid, final String userId, boolean force) {
		checkState();

		if (!lock(oid)) {
			return false;
		}

		if (!force) {
			Query q = new Query()
					.select(Entity.LOCKED_BY)
					.from(getMetaData().getName())
					.where(new Equals(Entity.OID, oid))
					.versioned(true);
			final boolean[] res = new boolean[]{true};
			search(q, null, new Predicate<Object[]>() {
				@Override
				public boolean test(Object[] dataModel) {
					String lockedUser = (String) dataModel[0];
					if (lockedUser != null
							&& !lockedUser.equals(userId)) {
						res[0] = false;
						return false;
					}
					return true;
				}
			});

			if (res[0] == false) {
				return false;
			}
		}

		UpdateCondition cond = new UpdateCondition(getMetaData().getName())
				.value(Entity.LOCKED_BY, null)
				.where(new Equals(Entity.OID, oid));
		int count = updateAll(cond);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean lockEntityByUser(String oid, final String userId, boolean force) {
		checkState();

		if (!lock(oid)) {
			return false;
		}

		if (!force) {
			Query q = new Query()
					.select(Entity.LOCKED_BY)
					.from(getMetaData().getName())
					.where(new Equals(Entity.OID, oid))
					.versioned(true);
			final boolean[] res = new boolean[]{true};
			search(q, null, new Predicate<Object[]>() {
				@Override
				public boolean test(Object[] dataModel) {
					String lockedUser = (String) dataModel[0];
					if (lockedUser != null
							&& !lockedUser.equals(userId)) {
						res[0] = false;
						return false;
					}
					return true;
				}
			});

			if (res[0] == false) {
				return false;
			}
		}

		UpdateCondition cond = new UpdateCondition(getMetaData().getName())
				.value(Entity.LOCKED_BY, userId)
				.where(new Equals(Entity.OID, oid));
		int count = updateAll(cond);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getLocalizedDisplayName() {
		String dispName = getMetaData().getDisplayName();
		if (dispName == null) {
			dispName = getMetaData().getName();
		}

		return I18nUtil.stringMeta(dispName, getMetaData().getLocalizedDisplayNameList());
	}

	public void bulkUpdate(BulkUpdatable bulkUpdatable) {
		ExecuteContext context = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();
		try (BulkUpdatable bu = bulkUpdatable;
				BulkUpdatable target = new BulkUpdateAdapter(bu, this, entityContext)) {
			getStrategy().bulkUpdate(target, entityContext, this, context.getClientId());
		}
	}

	public boolean isVersioned() {
		return metaData.getVersionControlType() != null
				&& metaData.getVersionControlType() != VersionControlType.NONE;
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
