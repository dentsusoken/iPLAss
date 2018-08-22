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

package org.iplass.adminconsole.server.tools.rpc.entityexplorer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.AdminEntityManager;
import org.iplass.adminconsole.server.metadata.rpc.MetaDataTreeBuilder;
import org.iplass.adminconsole.shared.base.dto.entity.EntityDataTransferTypeList;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.CrawlEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.DefragEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataCountResultInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataDeleteResultInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityDataListResultInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityUpdateAllCondition;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityUpdateAllResultInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.RecycleBinEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.SimpleEntityInfo;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.SimpleEntityTreeNode;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.UpdateAllValue;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.UpdateAllValue.UpdateAllValueType;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerService;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchManager;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.async.AsyncTaskService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.query.OrderBySyntax;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.tools.clean.RecycleBinCleanService;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

/**
 * Entity用Service実装クラス
 */
public class EntityExplorerServiceImpl extends XsrfProtectedServiceServlet implements EntityExplorerService {

	private static final Logger logger = LoggerFactory.getLogger(EntityExplorerServiceImpl.class);

	/** シリアルバージョンNo */
	private static final long serialVersionUID = -3459617043325559477L;

	private static final String USER_ENTITY = "mtp.auth.User";

	private EntityManager em = AdminEntityManager.getInstance();
	private EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	private EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	private EntityService ehs = ServiceRegistry.getRegistry().getService(EntityService.class);
	private FulltextSearchManager fsm = ManagerLocator.getInstance().getManager(FulltextSearchManager.class);
	private RdbAdapter rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();

	@Override
	public EntityDataTransferTypeList entityDataTypeWhiteList(EntityDataTransferTypeList param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<SimpleEntityInfo> getSimpleEntityList(final int tenantId, final boolean isGetDataCount) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<SimpleEntityInfo>>() {

			@Override
			public List<SimpleEntityInfo> call() {

				//DefinitionのList取得
				List<MetaDataEntryInfo> entityList = ehs.list();

				EntityContext ec = EntityContext.getCurrentContext();

				List<SimpleEntityInfo> infoList = new ArrayList<SimpleEntityInfo>();
				for (MetaDataEntryInfo entryInfo : entityList) {

					//１つのEntity定義の不具合により取得できないことを避けるため、Catchする
					EntityDefinition definition = null;
					int listenerCount = 0;
					EntityView view = null;
					int totalCount = -1;
					boolean isError = false;
					String errorMessage = null;
					try {
						//Definition取得
						definition = ehs.getRuntimeById(entryInfo.getId()).getMetaData().currentConfig(ec);

						//View取得
						view = evm.get(definition.getName());

						//件数取得
						if (isGetDataCount) {
							Query query = new Query().select(Entity.OID).from(definition.getName());
							totalCount = em.count(query);
						}
					} catch (Exception e) {
						logger.error(resourceString("errGetEntityInfo"), e);
						isError = true;
						errorMessage = (e.getMessage() != null ? e.getMessage() : e.getClass().getName());
					}

					SimpleEntityInfo info = new SimpleEntityInfo();
					if (definition != null) {
						info.setName(definition.getName());
						//Definitionが取得できた場合は、多言語を考慮した表示名の取得
						info.setDisplayName(I18nUtil.stringDef(definition.getDisplayName(), definition.getLocalizedDisplayNameList()));

						if (definition.getEventListenerList() != null) {
							//Listner件数取得
							listenerCount = definition.getEventListenerList().size();
						}
					} else {
						//Definitionが取得できなかったらとりあえずPathをセット
						info.setName(entryInfo.getPath());
						info.setDisplayName(entryInfo.getDisplayName());
					}
					info.setListenerCount(listenerCount);
					info.setCount(totalCount);
					if (view != null) {
						info.setDetailFormViewCount(view.getDetailFormViewNames() != null ? view.getDetailFormViewNames().length : 0);
						info.setSearchFormViewCount(view.getSearchFormViewNames() != null ? view.getSearchFormViewNames().length : 0);
					}
					info.setRepository(entryInfo.getRepository());
					info.setError(isError);
					if (isError) {
						info.setErrorMessage(errorMessage);
					}
					infoList.add(info);
				}

				return infoList;
			}

		});
	}

	@Override
	public SimpleEntityTreeNode getSimpleEntityTree(final int tenantId, final boolean isGetDataCount) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<SimpleEntityTreeNode>() {

			@Override
			public SimpleEntityTreeNode call() {

				MetaTreeNode metaRoot = new MetaDataTreeBuilder().type(EntityDefinition.class).build();

				SimpleEntityTreeNode pathRoot = convertNode(metaRoot, isGetDataCount);

				SimpleEntityTreeNode root = new SimpleEntityTreeNode();
				root.setName("EntityList");
				root.setChildren(pathRoot.getChildren());
				root.setItems(pathRoot.getItems());

				return root;
			}

		});
	}

	private SimpleEntityTreeNode convertNode(MetaTreeNode metaRoot, boolean isGetDataCount) {

		SimpleEntityTreeNode root = new SimpleEntityTreeNode();
		convertNode(root, metaRoot, isGetDataCount);
		return root;
	}

	private void convertNode(SimpleEntityTreeNode entityNode, MetaTreeNode metaNode, boolean isGetDataCount) {
		entityNode.setPath(metaNode.getPath());
		entityNode.setName(metaNode.getName());
//		entityNode.setContextPath(metaNode.getContextPath());

		if (metaNode.getChildren() != null) {
			List<SimpleEntityTreeNode> children = new ArrayList<SimpleEntityTreeNode>();
			for (MetaTreeNode metaChild : metaNode.getChildren()) {
				SimpleEntityTreeNode child = new SimpleEntityTreeNode();
				convertNode(child, metaChild, isGetDataCount);
				children.add(child);
			}
			entityNode.setChildren(children);
		}
		if (metaNode.getItems() != null) {
			List<SimpleEntityTreeNode> items = new ArrayList<SimpleEntityTreeNode>();
			for (MetaTreeNode metaItem : metaNode.getItems()) {
				if (metaItem.getName().equals(EntityDefinition.SYSTEM_DEFAULT_DEFINITION_NAME)) {
					continue;
				}

				SimpleEntityTreeNode item = new SimpleEntityTreeNode();
				item.setPath(metaItem.getPath());
				item.setName(metaItem.getName());
//				item.setContextPath(metaItem.getContextPath());

				item.setDisplayName(metaItem.getDisplayName());
				item.setRepository(metaItem.getRepository());

				item.setError(metaItem.isError());
				item.setErrorMessage(metaItem.getErrorMessage());

				if (!metaItem.isError()) {
					//１つのEntity定義の不具合により取得できないことを避けるため、Catchする
					EntityDefinition definition = null;
					int listenerCount = 0;
					EntityView view = null;
					int totalCount = -1;
					boolean isError = false;
					String errorMessage = null;
					try {
						//Definition取得
						definition = edm.get(metaItem.getName());
						if (definition != null) {
							//Definitionが取得できた場合は、表示名を再取得
							item.setDisplayName(I18nUtil.stringDef(definition.getDisplayName(), definition.getLocalizedDisplayNameList()));

							if (definition.getEventListenerList() != null) {
								//Listner件数取得
								listenerCount = definition.getEventListenerList().size();
							}
						}
						//View取得
						view = evm.get(metaItem.getName());

						//件数取得
						if (isGetDataCount) {
							Query query = new Query().select(Entity.OID).from(metaItem.getName());
							totalCount = em.count(query);
						}
					} catch (Exception e) {
						logger.error(resourceString("errGetEntityInfo"), e);
						isError = true;
						errorMessage = (e.getMessage() != null ? e.getMessage() : e.getClass().getName());
					}
					item.setCount(totalCount);
					item.setListenerCount(listenerCount);
					if (view != null) {
						item.setDetailFormViewCount(view.getDetailFormViewNames() != null ? view.getDetailFormViewNames().length : 0);
						item.setSearchFormViewCount(view.getSearchFormViewNames() != null ? view.getSearchFormViewNames().length : 0);
					}
					item.setError(isError);
					if (isError) {
						item.setErrorMessage(errorMessage);
					}
				}

				items.add(item);
			}
			entityNode.setItems(items);
		}
	}


	@Override
	public EntityDataListResultInfo search(int tenantId, final String defName, final String whereClause, final String orderByClause, final boolean isSearcgAllVersion, final int limit, final int offset) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EntityDataListResultInfo>() {

			@Override
			public EntityDataListResultInfo call() {

				EntityDataListResultInfo result = new EntityDataListResultInfo();
				result.setDefinitionName(defName);

				EntityDefinition definition = getEntityDefinition(defName);
				result.setDefinition(definition);

				//データの取得
				Query query = null;
				try {
					query = new Query();
					query.select(getListSelectColumns(definition, result)).from(defName);

					if (whereClause != null && !whereClause.isEmpty()) {
						query.where(whereClause);
						result.addLogMessage("Where : " + whereClause);
					} else {
						result.addLogMessage("Where : ");
					}

					if (orderByClause != null && !orderByClause.isEmpty()) {
						OrderBy orderBy = QueryServiceHolder.getInstance().getQueryParser().parse("order by " + orderByClause, OrderBySyntax.class);
						query.setOrderBy(orderBy);
						result.addLogMessage("Order By : " + orderByClause);
					} else {
						query.order(new SortSpec(Entity.OID, SortType.ASC), new SortSpec(Entity.VERSION, SortType.ASC));
						result.addLogMessage("Order By : oid asc, version asc (order by is not specified, so added [oid asc, version asc] for paging control.)");
					}

					query.limit(limit, offset);
					result.addLogMessage("Limit : limit = " + limit + ", offset = " + offset);

					if (isSearcgAllVersion) {
						query.setVersiond(isSearcgAllVersion);
						result.addLogMessage("Versioned : versioned");
					}
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addLogMessage(defName + resourceString("errEqlAnalysis"));
					result.addLogMessage(e.getMessage());
					return result;
				}

				long start = System.nanoTime();
				List<Entity> searchList = null;
				try {
					searchList = searchEntity(definition, query);
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addLogMessage(defName + resourceString("errGetData"));
					result.addLogMessage(e.getMessage());
					return result;
				}
				result.setRecords(searchList);

				long total = System.nanoTime() - start;
				result.addLogMessage("search exec time：" + toStringExecuteTime(total));

				if (searchList == null || searchList.isEmpty()) {
					result.addLogMessage("Count ：" + "not found data.");
				} else {
					result.addLogMessage("Count ：" + searchList.size());
				}

				return result;
			}

		});
	}

	@Override
	public EntityDataListResultInfo count(int tenantId, final String defName, final String whereClause, final boolean isSearcgAllVersion) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EntityDataListResultInfo>() {

			@Override
			public EntityDataListResultInfo call() {

				EntityDataListResultInfo result = new EntityDataListResultInfo();
				result.setDefinitionName(defName);

				//件数の取得
				Query counter = null;
				try {
					counter = new Query().select(Entity.OID).from(defName);
					if (whereClause != null && !whereClause.isEmpty()) {
						counter.where(whereClause);
						result.addLogMessage("Where : " + whereClause);
					} else {
						result.addLogMessage("Where : ");
					}

					if (isSearcgAllVersion) {
						counter.setVersiond(isSearcgAllVersion);
						result.addLogMessage("Versioned : versioned");
					}

				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addLogMessage(defName + resourceString("errEqlAnalysis"));
					result.addLogMessage(e.getMessage());
					return result;
				}

				long start = System.nanoTime();
				int count = -1;
				try {
					count = em.count(counter);
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addLogMessage(defName + resourceString("errGetData"));
					result.addLogMessage(e.getMessage());
					return result;
				}

				long total = System.nanoTime() - start;
				result.addLogMessage("search exec time：" + toStringExecuteTime(total));

				result.addLogMessage("Count ：" + count);

				return result;
			}
		});
	}


	@Override
	public EntityDataListResultInfo validateCriteria(int tenantId, final String defName, final String whereClause, final String orderByClause, final boolean isSearcgAllVersion) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EntityDataListResultInfo>() {

			@Override
			public EntityDataListResultInfo call() {

				EntityDataListResultInfo result = new EntityDataListResultInfo();
				result.setDefinitionName(defName);

				//件数の取得
				Query counter = null;
				try {
					counter = new Query().select(Entity.OID).from(defName);
					if (whereClause != null && !whereClause.isEmpty()) {
						//counter.where(whereClause);
						result.addLogMessage("Where : " + whereClause);
					} else {
						result.addLogMessage("Where : ");
					}
					//EQLの検証が目的なのでOrderByも指定
					if (orderByClause != null && !orderByClause.isEmpty()) {
						OrderBy orderBy = QueryServiceHolder.getInstance().getQueryParser().parse("order by " + orderByClause, OrderBySyntax.class);
						counter.setOrderBy(orderBy);
						result.addLogMessage("Order By : " + orderByClause);
					} else {
						result.addLogMessage("Order By : ");
					}

					if (isSearcgAllVersion) {
						counter.setVersiond(isSearcgAllVersion);
					}
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addLogMessage(defName + resourceString("errEqlAnalysis"));
					result.addLogMessage(e.getMessage());
					return result;
				}

				try {
					//あくまでも検証目的なので、大量件数を考慮して条件にOIDを付加
					String execWhereClause = null;
					if (StringUtil.isEmpty(whereClause)) {
						execWhereClause = Entity.OID + " = 'xxx'";
					} else {
						execWhereClause = whereClause + " and " + Entity.OID + " = 'xxx'";
					}
					counter.where(execWhereClause);

					em.count(counter);
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addLogMessage(defName + resourceString("errGetData"));
					result.addLogMessage(e.getMessage());
					return result;
				}
				result.addLogMessage("EQL is validate.");

				return result;
			}
		});
	}

	@Override
	public EntityUpdateAllResultInfo updateAll(final int tenantId, final EntityUpdateAllCondition cond) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EntityUpdateAllResultInfo>() {

			@Override
			public EntityUpdateAllResultInfo call() {

				EntityUpdateAllResultInfo result = new EntityUpdateAllResultInfo();

				int updateCount = 0;
				try {
					UpdateCondition updateCond = new UpdateCondition(cond.getDefinitionName());

					EntityDefinition definition = getEntityDefinition(cond.getDefinitionName());

					List<String> validateErrors = new ArrayList<String>();
					for (UpdateAllValue entry : cond.getValues()) {
						PropertyDefinition pd = definition.getProperty(entry.getPropertyName());
						if (pd == null) {
							validateErrors.add(entry.getPropertyName() + " property not found.");
						}

						//String値を型変換する
						String stringValue = entry.getValue();
						Object storevalue = null;
						if (stringValue != null) {
							//SelectPropertyは除外
							if (pd instanceof SelectProperty) {
								storevalue = stringValue;
							} else {
								if (entry.getValueType() == UpdateAllValueType.LITERAL) {
									try {
										storevalue = ConvertUtil.convertFromString(pd.getJavaType(), stringValue);
									} catch (NumberFormatException e) {
										logger.error(e.getMessage(), e);
										validateErrors.add(entry.getPropertyName() + " property value is unsupport format. cannot be parsed number. value = " + stringValue);
									} catch (IllegalArgumentException e) {
										logger.error(e.getMessage(), e);
										validateErrors.add(entry.getPropertyName() + " property value is unsupport format. " + e.getMessage() + " value = " + stringValue);
									}
								} else {
									try {
										storevalue = ValueExpression.newValue(stringValue);
									} catch (QueryException e) {
										logger.error(e.getMessage(), e);
										validateErrors.add(entry.getPropertyName() + " property value is unsupport format. " + e.getMessage() + " value = " + stringValue);
									}
								}
							}
						}
						updateCond.value(entry.getPropertyName(), storevalue);
					}

					if (!validateErrors.isEmpty()) {
						result.setError(true);
						result.addLogMessage("Result : FAILURE");
						result.addLogMessage("Cause : validate error");
						result.addLogMessage(validateErrors);
						return result;
					}

					if (cond.getWhere() != null && !cond.getWhere().isEmpty()) {
						updateCond.where(cond.getWhere());
					}

					//更新不可の項目化をチェックするかを設定
					updateCond.setCheckUpdatable(cond.isCheckUpdatable());

					result.addLogMessage("Update Condition : " + updateCond.toString());

					updateCount = em.updateAll(updateCond);

					result.addLogMessage("Result : SUCCESS");
					result.addLogMessage("Update Count : " + updateCount);

				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addLogMessage("Result : FAILURE");
					result.addLogMessage("Cause : " + e.getMessage());
					return result;
				}

				result.setUpdateCount(updateCount);

				return result;
			}

		});
	}

	@Override
	public List<Entity> searchReferenceEntity(int tenantId, final String defName, final String refName, final String oid, final Long version) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<Entity>>() {

			@Override
			public List<Entity> call() {
				Query query = new Query();
				query.select(refName + "." + Entity.OID, refName + "." + Entity.NAME, refName + "." + Entity.VERSION)
					.from(defName)
					.where(new And(
							new Equals(Entity.OID, oid),
							new Equals(Entity.VERSION, version)))
					.order(new SortSpec(refName + "." + Entity.OID, SortType.ASC), new SortSpec(refName + "." + Entity.VERSION, SortType.ASC));

				return searchEntity(defName, query);
			}
		});
	}

	private EntityDefinition getEntityDefinition(final String defName) {
		return edm.get(defName);
	}

	private Object[] getListSelectColumns(EntityDefinition definition, EntityDataListResultInfo result) {
		//EntityManager#searchEntityで検索した場合に、oidに対して複数のレコードが返る項目を除外する。

		List<String> columns = new ArrayList<String>();
		for (PropertyDefinition property: definition.getPropertyList()) {
			if (property instanceof ReferenceProperty) {
				ReferenceProperty reference = (ReferenceProperty)property;
				if (reference.getMappedBy() != null && !reference.getMappedBy().isEmpty()) {

					//検索対象外(多重度が1でも複数の可能性あり。ex.出版社情報#雑誌情報)
					result.addLogMessage(resourceString("notSearchspeRefced", property.getDisplayName(), property.getName(), reference.getObjectDefinitionName(), reference.getMappedBy()));
				} else if (property.getMultiplicity() != 1) {

					//検索対象外
					result.addLogMessage(resourceString("notSearchMultipl", property.getDisplayName(), property.getName(), property.getMultiplicity()));
				} else {
					columns.add(property.getName() + "." + Entity.OID);
					columns.add(property.getName() + "." + Entity.NAME);
				}
			} else {
				columns.add(property.getName());
			}
		}
		return columns.toArray(new String[]{});
	}

	/**
	 * <p>Entityを検索します。</p>
	 *
	 * <p>{@link EntityManager#searchEntity(Query)} の場合、
	 * マッピングが指定されているとそのクラスが返ってきてしまうため
	 * GWT側でエラーとなる（Superにないため）。<br/>
	 * マッピング指定がある場合は、通常のEQLで取得し、
	 * GenericEntityを生成して返すように検索処理をラップする。</p>
	 *
	 * @param defName
	 * @param query
	 * @return
	 */
	private List<Entity> searchEntity(final String defName, Query query) {
		return searchEntity(getEntityDefinition(defName), query);
	}

	private List<Entity> searchEntity(EntityDefinition definition, Query query) {
		//Referenceプロパティでかつ多重度１の場合も画面表示時にエラーとなるため、
		//em.searchEntity方式での検索から、em.search方式に全面移行
//		if (definition.getMapping() != null) {
			SearchResult<Object[]> searchResult = em.search(query);
			List<Object[]> resultObjectList = searchResult.getList();
			if (resultObjectList == null) {
				return Collections.emptyList();
			}

			List<Entity> resultList = new ArrayList<Entity>(resultObjectList.size());

			List<ValueExpression> colValues = query.getSelect().getSelectValues();
			for (Object[] record : resultObjectList) {
				GenericEntity entity = new GenericEntity(definition.getName());
				int i = 0;
				for (ValueExpression colValue : colValues) {
					setValue(definition, entity, colValue, record[i++]);
				}
				resultList.add(entity);
			}
			return resultList;
//		} else {
//			SearchResult<Entity> searchResult = em.searchEntity(query, true);
//			return searchResult.getList();
//		}
	}

	private void setValue(EntityDefinition definition, GenericEntity entity, ValueExpression colValue, Object value) {
		//セットするvalueがない場合はなにもしない
		if (value == null) {
			return;
		}
		if (isReferenceProperty(definition, colValue)) {
			String refName = colValue.toString().substring(0, colValue.toString().lastIndexOf("."));
			String propertyName = colValue.toString().substring(colValue.toString().lastIndexOf(".") + 1);
			GenericEntity refEntity = entity.getValue(refName);
			if (refEntity == null) {
				refEntity = createReferenceEntity(definition, refName);
			}
			refEntity.setValue(propertyName, value);
			entity.setValue(refName, refEntity);
		} else {
			entity.setValue(colValue.toString(), value);
		}
	}

	private boolean isReferenceProperty(EntityDefinition definition, ValueExpression colValue) {
		//後ろに「.oid」「.name」が付いていたらReferencePropertyの検索
		return colValue.toString().endsWith("." + Entity.OID)
				|| colValue.toString().endsWith("." + Entity.NAME);

	}

	private GenericEntity createReferenceEntity(EntityDefinition definition, String refName) {
		ReferenceProperty property = (ReferenceProperty)definition.getProperty(refName);
		return new GenericEntity(property.getObjectDefinitionName());
	}

	@Override
	public Entity load(final int tenantId, final String defName, final String oid, final Long version) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Entity>() {

			@Override
			public Entity call() {

				GenericEntity returnEntity = null;

				//検索（MappedByは対象外）
				LoadOption option = new LoadOption(true, false);
				Entity loadEntity = em.load(oid, version, defName, option);
				if (loadEntity != null) {
					//このままだとMapping指定されている場合にGWTでエラーになるので置き換え

					returnEntity = new GenericEntity(defName);
					EntityDefinition definition = getEntityDefinition(defName);

					List<PropertyDefinition> propertyList = definition.getPropertyList();
					for (PropertyDefinition pd: propertyList) {
						if (pd instanceof ReferenceProperty) {
							ReferenceProperty rp = (ReferenceProperty)pd;
							if (rp.getMappedBy() == null) {
								Object refLoadEntityValue = loadEntity.getValue(pd.getName());
								if (refLoadEntityValue != null) {
									if (refLoadEntityValue.getClass().isArray()) {
										//複数
										Entity[] refLoadEntities = (Entity[])refLoadEntityValue;
										Entity[] refEntities = new GenericEntity[refLoadEntities.length];
										for (int i = 0; i < refLoadEntities.length; i++) {
											Entity refLoadEntity = refLoadEntities[i];
											refEntities[i] = new GenericEntity(rp.getObjectDefinitionName());
											refEntities[i].setOid(refLoadEntity.getOid());
											refEntities[i].setName(refLoadEntity.getName());
											refEntities[i].setVersion(refLoadEntity.getVersion());
										}
										returnEntity.setValue(pd.getName(), refEntities);
									} else {
										//単発
										Entity refLoadEntity = (Entity)refLoadEntityValue;
										GenericEntity refEntity = new GenericEntity(rp.getObjectDefinitionName());
										refEntity.setOid(refLoadEntity.getOid());
										refEntity.setName(refLoadEntity.getName());
										refEntity.setVersion(refLoadEntity.getVersion());
										returnEntity.setValue(pd.getName(), refEntity);
									}
								}
							}
						} else {
							returnEntity.setValue(pd.getName(), loadEntity.getValue(pd.getName()));
						}
					}
				}

				return returnEntity;
			}

		});
	}

	@Override
	public EntityDataDeleteResultInfo deleteAll(final int tenantId, final String defName, final String whereClause, final boolean isNotifyListeners, final int commitLimit) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EntityDataDeleteResultInfo>() {

			int allCount = 0;

			EntityHandler eh = null;
			String execCond;

			@Override
			public EntityDataDeleteResultInfo call() {

				final EntityDataDeleteResultInfo result = new EntityDataDeleteResultInfo();

				try {
					execCond = whereClause;

					//Userエンティティの場合、実行ユーザは削除しない
					ExecuteContext ec = ExecuteContext.getCurrentContext();
					if (USER_ENTITY.equals(defName)) {
						String execUserOid = ec.getClientId();
						if (StringUtil.isNotEmpty(whereClause)) {
							execCond += " and " + Entity.OID + " != '" + execUserOid + "'";
						} else {
							execCond = Entity.OID + " != '" + execUserOid + "'";
						}
					}

					boolean canDeleteAll = false;
					if (!isNotifyListeners) {
						//Listnerを実行しない場合はdeleteAll可能かをチェック
						EntityContext entityContext = EntityContext.getCurrentContext();
						eh = entityContext.getHandlerByName(defName);
						if (eh == null) {
							throw new RuntimeException("not found entity definition. name = " + defName);
						}
						canDeleteAll = eh.canDeleteAll();
					}

					if (canDeleteAll) {
						//一括削除
						Transaction.with(Propagation.REQUIRED, t -> {
							try {
								DeleteCondition cond = new DeleteCondition(defName);

								if (StringUtil.isNotEmpty(execCond)) {
									cond.where(execCond);
									result.addMessages("Delete Condition : " + execCond);
								} else {
									result.addMessages("Delete Condition : " + "none");
								}

								allCount = em.deleteAll(cond);

								return null;
							} catch (RuntimeException e) {
								logger.error("delete entity data failed.", e);
								throw e;
							}
						});
					} else {
						//１件ずつ削除
						//全件検索（削除するので1度に全件のOIDをとらないとNG）
						Query query = new Query().select(Entity.OID).from(defName);
						if (StringUtil.isNotEmpty(execCond)) {
							query.where(execCond);
							result.addMessages("Delete Condition : " + execCond);
						} else {
							result.addMessages("Delete Condition : " + "none");
						}

						final List<Entity> entities = new ArrayList<Entity>();
						em.searchEntity(query, new Predicate<Entity>() {

							@Override
							public boolean test(Entity entity) {
								entities.add(entity);
								if (commitLimit != -1) {
									if (entities.size() == commitLimit) {
										int count = deleteData(defName, entities, isNotifyListeners);
										allCount += count;
										result.addMessages(count + " data is deleted and comitted.");
										entities.clear();
									}
								}
								return true;
							}
						});

						if (entities.size() > 0) {
							int count = deleteData(defName, entities, isNotifyListeners);
							allCount += count;
							result.addMessages(count + " data is deleted and comitted.");
						}
					}

					result.setError(false);

					result.addMessages("Result : SUCCESS");
					result.addMessages("Delete Count : " + allCount);

					return result;
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addMessages("Result : FAILURE");
					result.addMessages("Cause : " + (e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
					return result;
				}
			}
		});
	}

	@Override
	public EntityDataDeleteResultInfo deleteAllByOid(final int tenantId, final String defName, final List<String> oids, final boolean isNotifyListeners, final int commitLimit) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EntityDataDeleteResultInfo>() {

			int allCount = 0;

			@Override
			public EntityDataDeleteResultInfo call() {

				final EntityDataDeleteResultInfo result = new EntityDataDeleteResultInfo();

				try {
					LoadOption loadOption = new LoadOption(false, false);
					final List<Entity> entities = new ArrayList<Entity>();
					boolean isUserEntity = false;
					String execUserOid = null;
					if (USER_ENTITY.equals(defName)) {
						isUserEntity = true;
						ExecuteContext executeContext = ExecuteContext.getCurrentContext();
						execUserOid = executeContext.getClientId();
					}
					for (String oid : oids) {
						//Load(存在チェック)
						Entity entity = em.load(oid, defName, loadOption);
						//Userエンティティの場合、実行ユーザは削除しない
						if (isUserEntity && oid.equals(execUserOid)) {
							continue;
						}
						if (entity != null) {
							entities.add(entity);
							if (commitLimit != -1) {
								if (entities.size() == commitLimit) {
									int count = deleteData(defName, entities, isNotifyListeners);
									allCount += count;
									result.addMessages(count + " data is deleted and comitted.");
									entities.clear();
								}
							}
						}
					}

					if (entities.size() > 0) {
						int count = deleteData(defName, entities, isNotifyListeners);
						allCount += count;
						result.addMessages(count + " data is deleted and comitted.");
					}

					result.setError(false);
//					if (commitLimit != -1) {
//						result.addMessages("total delete count is " + allCount);
//					}
					result.addMessages("Result : SUCCESS");
					result.addMessages("Delete Count : " + allCount);
					return result;
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addMessages("Result : FAILURE");
					result.addMessages("Cause : " + (e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
					return result;
				}
			}
		});
	}

	/**
	 * Entityデータの削除（1トランザクション分）
	 *
	 * @param defName Entity定義名
	 * @param entities 削除対象Entity
	 * @param isNotifyListeners Listenerを実行するか
	 * @return 削除件数
	 */
	private int deleteData(final String defName, final List<Entity> entities, final boolean isNotifyListeners) {

		return Transaction.requiresNew(new Function<Transaction, Integer>() {

			private int delCount = 0;

			@Override
			public Integer apply(Transaction transaction) {

				try {

					DeleteOption option = new DeleteOption(false);
					option.setPurge(true);
					option.setCheckLockedByUser(false);
					option.setNotifyListeners(isNotifyListeners);

					for (Entity entity : entities) {
						em.delete(entity, option);
						delCount++;
					}

					return delCount;
				} catch (RuntimeException e) {
					logger.error("delete entity data failed.", e);
					throw e;
				}
			}

		});

	}

	@Override
	public EntityDataCountResultInfo getConditionDataCount(final int tenantId, final String defName, final String whereClause) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EntityDataCountResultInfo>() {

			@Override
			public EntityDataCountResultInfo call() {

				final EntityDataCountResultInfo result = new EntityDataCountResultInfo();

				try {
					Query cond = new Query().select(Entity.OID).from(defName);
					int allCount = em.count(cond);

					int targetCount = 0;
					if (whereClause != null && !whereClause.isEmpty()) {
						cond.where(whereClause);
						result.addMessages("Condition : " + whereClause);

						targetCount = em.count(cond);
					} else {
						result.addMessages("Condition : " + "none");

						targetCount = allCount;
					}

					result.setAllCount(allCount);
					result.setTargetCount(targetCount);

					result.setError(false);
					result.addMessages("Result : SUCCESS");
					result.addMessages("Target Count : " + targetCount + " / " + allCount);
					return result;
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					result.setError(true);
					result.addMessages("Result : FAILURE");
					result.addMessages("Cause : " + (e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
					return result;
				}
			}

		});
	}

	@Override
	public List<DefragEntityInfo> getDefragEntityList(final int tenantId, final boolean isGetDataCount) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<DefragEntityInfo>>() {

			@Override
			public List<DefragEntityInfo> call() {

				//DefinitionのList取得
				List<MetaDataEntryInfo> entityList = ehs.list();

				EntityContext ec = EntityContext.getCurrentContext();

				List<DefragEntityInfo> infoList = new ArrayList<DefragEntityInfo>();
				for (MetaDataEntryInfo entryInfo : entityList) {

					//１つのEntity定義の不具合により取得できないことを避けるため、Catchする
					EntityDefinition definition = null;
					int totalCount = -1;
					boolean isError = false;
					String errorMessage = null;
					try {
						//Definition取得
						definition = ehs.getRuntimeById(entryInfo.getId()).getMetaData().currentConfig(ec);

						//件数取得
						if (isGetDataCount) {
							Query query = new Query().select(Entity.OID).from(definition.getName());
							totalCount = em.count(query);
						}
					} catch (Exception e) {
						logger.error("Error getting the defrag target entity list.", e);
						isError = true;
						errorMessage = (e.getMessage() != null ? e.getMessage() : e.getClass().getName());
					}

					DefragEntityInfo info = new DefragEntityInfo();
					if (definition != null) {
						info.setName(definition.getName());
						//Definitionが取得できた場合は、多言語を考慮した表示名の取得
						info.setDisplayName(I18nUtil.stringDef(definition.getDisplayName(), definition.getLocalizedDisplayNameList()));
					} else {
						//Definitionが取得できなかったらとりあえずPathをセット
						info.setName(entryInfo.getPath());
						info.setDisplayName(entryInfo.getDisplayName());
					}
					info.setCurrentVersion(entryInfo.getVersion());
					info.setUpdateDate(entryInfo.getUpdateDate());
					info.setCount(totalCount);
					info.setError(isError);
					if (isError) {
						info.setErrorMessage(errorMessage);
					}
					infoList.add(info);
				}

				return infoList;
			}

		});
	}

	@Override
	public List<String> defragEntity(final int tenantId, final String defName) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<String>>() {

			@Override
			public List<String> call() {

				List<String> messages = new ArrayList<>();

				//非同期で実行する
				try {
					AsyncTaskService asyncService = ServiceRegistry.getRegistry().getService(AsyncTaskService.class);
					asyncService.execute(new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							doDefragEntity(defName);
							return null;
						}
					});

					messages.add("start defrag. name = " + defName);
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					messages.add("error start defrag. name = " + defName);
				}

				return messages;
			}
		});
	}

	private void doDefragEntity(final String defName) {
		EntityService service = ServiceRegistry.getRegistry().getService(EntityService.class);
		service.defragByName(defName);
	}

	private String toStringExecuteTime(long nanoTime) {

		if (nanoTime > 1000000000) {
			return TimeUnit.SECONDS.convert(nanoTime, TimeUnit.NANOSECONDS) + "sec";
		} else {
			return TimeUnit.MILLISECONDS.convert(nanoTime, TimeUnit.NANOSECONDS) + "ms";
		}
	}

	@Override
	public void execCrawlEntity(int tenantId, String[] defNames) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				for (String defName : defNames) {
					fsm.crawlEntity(defName);
				}
				return null;
			}

		});
	}

	@Override
	public void execReCrawlEntity(int tenantId) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				fsm.recrawlAllEntity();
				return null;
			}
		});

	}


	@Override
	public void execRefresh(int tenantId) {
		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				fsm.refresh();
				return null;
			}
		});

	}

	@Override
	public List<CrawlEntityInfo> getCrawlEntityList(final int tenantId, final boolean isGetDataCount) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<CrawlEntityInfo>>() {

			@Override
			public List<CrawlEntityInfo> call() {
				Map<String, Timestamp> map = getLastCrawlTimestamp(tenantId);

				//Entity定義の取得
				List<MetaDataEntryInfo> entityList = ehs.list();

				EntityContext ctx = EntityContext.getCurrentContext();

				List<CrawlEntityInfo> infoList = new ArrayList<CrawlEntityInfo>();
				for (MetaDataEntryInfo entryInfo : entityList) {

					//１つのEntity定義の不具合により取得できないことを避けるため、Catchする
					EntityDefinition definition = null;
					int totalCount = -1;
					boolean isError = false;
					String errorMessage = null;
					try {
						//Definition取得
						definition = ehs.getRuntimeById(entryInfo.getId()).getMetaData().currentConfig(ctx);

						// クロール対象ではない場合はリストに表示しない
						if (!definition.isCrawl()) {
							continue;
						}

						//件数取得
						if (isGetDataCount) {
							Query query = new Query().select(Entity.OID).from(definition.getName());
							totalCount = em.count(query);
						}
					} catch (Exception e) {
						logger.error(resourceString("errGetEntityInfo"), e);
						isError = true;
						errorMessage = (e.getMessage() != null ? e.getMessage() : e.getClass().getName());
					}

					CrawlEntityInfo info = new CrawlEntityInfo();
					if (definition != null) {
						info.setName(definition.getName());
						//Definitionが取得できた場合は、多言語を考慮した表示名の取得
						info.setDisplayName(I18nUtil.stringDef(definition.getDisplayName(), definition.getLocalizedDisplayNameList()));
					} else {
						//Definitionが取得できなかったらとりあえずPathをセット
						info.setName(entryInfo.getPath());
						info.setDisplayName(entryInfo.getDisplayName());
					}
					info.setCount(totalCount);
					info.setError(isError);
					if (isError) {
						info.setErrorMessage(errorMessage);
					}

					info.setUpdateDate(entryInfo.getUpdateDate());

					if (map.containsKey(entryInfo.getId())) {
						info.setLastCrawlDate(map.get(entryInfo.getId()));
					}
					infoList.add(info);
				}

				return infoList;
			}

		});
	}

	private Map<String, Timestamp> getLastCrawlTimestamp(int tenantId) {
		final String sql = "select obj_def_id, up_date from crawl_log where tenant_id=" + tenantId;

		SqlExecuter<Map<String, Timestamp>> exec = new SqlExecuter<Map<String, Timestamp>>() {

			@Override
			public Map<String, Timestamp> logic() throws SQLException {
				// SQL実行
				ResultSet rs = getStatement().executeQuery(sql);
				Map<String, Timestamp> map = new LinkedHashMap<String, Timestamp>();
				try {
					while(rs.next()) {
						map.put(rs.getString(1), rs.getTimestamp(2));
					}
				} finally {
					rs.close();
				}

				return map;
			}
		};
		return exec.execute(rdb, true);
	}

	@Override
	public boolean isUseFulltextSearch(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Boolean>() {
			@Override
			public Boolean call() {
				return fsm.isUseFulltextSearch();
			}
		});
	}
	
	@Override
	public List<RecycleBinEntityInfo> getRecycleBinInfoList(int tenantId, Timestamp ts, boolean isGetCount) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<RecycleBinEntityInfo>>() {

			@Override
			public List<RecycleBinEntityInfo> call() {

				//Definitionのlist取得
				List<MetaDataEntryInfo> entityList = ehs.list();

				EntityContext ctx = EntityContext.getCurrentContext();

				List<RecycleBinEntityInfo> infoList = new ArrayList<RecycleBinEntityInfo>();
				for(MetaDataEntryInfo entryInfo: entityList) {

					//１つのEntity定義の不具合により取得できないことを避けるため、Catchする
					EntityDefinition definition = null;
					EntityHandler handler = null;
					int totalCount = 0;
					boolean isError = false;
					String errorMessage = null;

					try {
						handler = ehs.getRuntimeById(entryInfo.getId());
						definition = handler.getMetaData().currentConfig(ctx);

						// 件数取得
						if (isGetCount) {
							totalCount = handler.countRecycleBin(ts);
						}
					}  catch (Exception e) {
						logger.error("Error getting the recycle bin list.", e);
						isError = true;
						errorMessage = (e.getMessage() != null ? e.getMessage() : e.getClass().getName());
					}

					RecycleBinEntityInfo info = new RecycleBinEntityInfo();
					if (definition != null) {
						info.setName(definition.getName());
						info.setDisplayName(definition.getDisplayName());
					} else {
						//Definitionが取得できなかったらとりあえずPathをセット
						info.setName(entryInfo.getPath());
						info.setDisplayName(entryInfo.getDisplayName());
					}
					info.setCount(totalCount);
					info.setError(isError);
					if (isError) {
						info.setErrorMessage(errorMessage);
					}
					infoList.add(info);
				}

				return infoList;
			}
		});
	}



	@Override
	public List<String> cleanRecycleBin(int tenantId, String defName, Timestamp ts) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), getThreadLocalRequest(), getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<String>>() {
			@Override
			public List<String> call() {
				List<String> messages = new ArrayList<String>();
				AsyncTaskService asyncService = ServiceRegistry.getRegistry().getService(AsyncTaskService.class);

				try {
					asyncService.execute(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							doCleanRecycleBinInfoList(defName, ts);
							return null;
						}
					});
					messages.add("start clean recycle bin. defName = " + defName);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					messages.add("error start clean recycle bin. defName = " + defName);
				}
				return messages;
			}
		});
	}

	private void doCleanRecycleBinInfoList(String defName, Timestamp ts) {
		RecycleBinCleanService rbcs = ServiceRegistry.getRegistry().getService(RecycleBinCleanService.class);
		rbcs.clean(ts, new String[] { defName });
	}



	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("tools.entityexplorer.EntityExplorerServiceImpl." + suffix, arguments);
	}

}
