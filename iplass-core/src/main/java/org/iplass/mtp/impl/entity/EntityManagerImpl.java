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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.DeepCopyOption;
import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.entity.EntityKey;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.EntityValidationException;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.SearchResult.ResultMode;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.ValidateResult;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceType;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchOption;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.ReadOnlyHint;
import org.iplass.mtp.entity.query.value.RowValueList;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.interceptor.EntityBulkUpdateInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityCountInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityDeleteAllInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityDeleteInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityGetRecycleBinInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityInsertInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityLoadInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityLockByUserInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityPurgeInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityQueryInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityRestoreInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityUnlockByUserInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityUpdateAllInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityUpdateInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityValidateInvocationImpl;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.fulltextsearch.FulltextSearchService;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.lob.Lob;
import org.iplass.mtp.impl.lob.LobHandler;
import org.iplass.mtp.impl.properties.extend.BinaryType;
import org.iplass.mtp.impl.session.SessionService;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionOption;
import org.iplass.mtp.transaction.TransactionStatus;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EntityManagerImpl implements EntityManager {

	private static Logger logger = LoggerFactory.getLogger(EntityManagerImpl.class);

	private EntityService ehService;
	private SessionService sessionService;
	private FulltextSearchService fulltextSearchService;

	public EntityManagerImpl() {
		ehService = ServiceRegistry.getRegistry().getService(EntityService.class);
		sessionService = ServiceRegistry.getRegistry().getService(SessionService.class);
		fulltextSearchService = ServiceRegistry.getRegistry().getService(FulltextSearchService.class);
	}

	private EntityHandler getEntityHandler(String defName) {
		EntityHandler handler = ehService.getRuntimeByName(defName);
		if (handler == null) {
			throw new EntityRuntimeException(defName + " is undefined.");
		}
		return handler;
	}

	private void setRollbackOnly() {
		Transaction t = ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager().currentTransaction();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
			t.setRollbackOnly();
		}
	}

	private <R> R withReadOnlyCheck(Query q, ResultMode resultMode, Function<Transaction, R> func) {
		Transaction t = Transaction.getCurrent();
		if (!t.isReadOnly() && resultMode == ResultMode.AT_ONCE && hasReadOnlyHint(q)) {
			return Transaction.with(new TransactionOption(Propagation.REQUIRES_NEW).readOnly().throwExceptionIfSetRollbackOnly(), func);
		} else {
			return func.apply(t);
		}
	}

	private boolean hasReadOnlyHint(Query q) {
		if (q.getSelect().getHintComment() == null) {
			return false;
		}
		List<Hint> hintList = q.getSelect().getHintComment().getHintList();
		if (hintList == null) {
			return false;
		}
		for (Hint h: hintList) {
			if (h instanceof ReadOnlyHint) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int count(Query query) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("count():" + query);
			}

			return withReadOnlyCheck(query, ResultMode.AT_ONCE, t -> {
				//検索処理実行
				EntityHandler handler = getEntityHandler(query.getFrom().getEntityName());
				return new EntityCountInvocationImpl(query, ehService.getInterceptors(), handler).proceed();
			});

		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public void delete(Entity entity, DeleteOption option) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("delete():" + entity + ", option=" + option);
			}

			if (entity.getDefinitionName() == null) {
				//データモデル定義名が設定されていない
				throw new EntityRuntimeException("no definitionName");//TODO メッセージを埋める（メッセージ管理）
			}

			//データを削除
			EntityHandler handler = getEntityHandler(entity.getDefinitionName());
			new EntityDeleteInvocationImpl(entity, option, ehService.getInterceptors(), handler).proceed();
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public Entity load(String oid, String definitionName) {
		return load(oid, null, definitionName);
	}

	@Override
	public Entity load(String oid, Long version, String definitionName) {
		return load(oid, version, definitionName, null);
	}

	@Override
	public Entity load(String oid, String definitionName, LoadOption option) {
		return load(oid, null, definitionName, option);
	}

	@Override
	public Entity load(String oid, Long version, String definitionName, LoadOption option) {
		if (option == null) {
			option = new LoadOption();
		}
		try {

			if (logger.isDebugEnabled()) {
				logger.debug("load():oid=" + oid + ", version=" + version + ", definitionName=" + definitionName + ", option=" + option);
			}

			EntityHandler handler = getEntityHandler(definitionName);
			return new EntityLoadInvocationImpl(oid, version, option, false, ehService.getInterceptors(), handler).proceed();
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public Entity loadAndLock(String oid, String definitionName) {
		return loadAndLock(oid, definitionName, null);
	}

	@Override
	public Entity loadAndLock(String oid, String definitionName, LoadOption option) {
		if (option == null) {
			option = new LoadOption();
		}
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("loadAndLock():oid=" + oid + ", definitionName=" + definitionName + ", option=" + option);
			}
			EntityHandler handler = getEntityHandler(definitionName);
			return new EntityLoadInvocationImpl(oid, null, option, true, ehService.getInterceptors(), handler).proceed();
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public List<Entity> batchLoad(List<EntityKey> keys, String definitionName) {
		return batchLoad(keys, definitionName, null);
	}

	@Override
	public List<Entity> batchLoad(List<EntityKey> keys, String definitionName, LoadOption option) {
		final LoadOption loadOption = option != null ? option: new LoadOption();

		EntityContext entityContext = EntityContext.getCurrentContext();
		EntityDefinition ed = getEntityHandler(definitionName).getMetaData().currentConfig(entityContext);

		Select select = new Select();
		ed.getPropertyList().forEach(pd -> {
			if (pd instanceof ReferenceProperty) {
				ReferenceProperty rp = (ReferenceProperty)pd;
				if (CollectionUtil.isNotEmpty(loadOption.getLoadReferences())) {
					if (!loadOption.getLoadReferences().contains(rp.getName())) {
						return;
					}
				} else {
					if (StringUtil.isNotEmpty(rp.getMappedBy())) {
						if (!loadOption.isWithMappedByReference()) {
							return;
						}
					} else {
						if (!loadOption.isWithReference()) {
							return;
						}
					}
				}
				select.add(new EntityField(pd.getName() + "." + Entity.OID));
				select.add(new EntityField(pd.getName() + "." + Entity.VERSION));
			} else {
				select.add(pd.getName());
			}
		});

		Condition condition = null;
		OrderBy orderBy = new OrderBy();
		if (ed.getVersionControlType() != VersionControlType.NONE) {
			List<ValueExpression> values = keys.stream()
					.map(key -> new RowValueList(key.getOid(), key.getVersion() != null ? key.getVersion() : 0))
					.collect(Collectors.toList());
			condition = new In(new String[] {Entity.OID, Entity.VERSION}, values);
			orderBy.add(new SortSpec(Entity.OID, SortType.DESC));
			orderBy.add(new SortSpec(Entity.VERSION, SortType.DESC));
		} else {
			List<ValueExpression> values = keys.stream()
					.map(key -> new Literal(key.getOid()))
					.collect(Collectors.toList());
			condition = new In(new EntityField(Entity.OID), values);
			orderBy.add(new SortSpec(Entity.OID, SortType.DESC));
		}

		Query query = new Query();
		query.setSelect(select);
		query.from(definitionName);
		query.where(condition);
		query.setOrderBy(orderBy);

		SearchOption searchOption = new SearchOption();
		searchOption.setCountTotal(false);
		searchOption.setNotifyListeners(loadOption.isNotifyListeners());
		searchOption.setReturnStructuredEntity(true);

		return searchEntity(query, searchOption).getList();
	}

	@Override
	public SearchResult<Object[]> search(Query query) {
		return search(query, new SearchOption());
	}

	@Override
	public <T extends Entity> SearchResult<T> searchEntity(Query query) {
		return searchEntity(query, new SearchOption());
	}

	@Override
	public SearchResult<Object[]> search(Query query, SearchOption option) {
		final SearchOption finalOption;
		if (option == null) {
			finalOption = new SearchOption();
		} else {
			finalOption = option;
		}

		try {
			return withReadOnlyCheck(query, finalOption.getResultMode(), t -> {

				int counts = -1;
				if (finalOption.isCountTotal()) {
					Query countQuery = query.copy();
					countQuery.setLimit(null);
					counts = count(countQuery);
				}

				//検索処理実行
				EntityHandler eh = getEntityHandler(query.getFrom().getEntityName());
				if (finalOption.getResultMode() == ResultMode.AT_ONCE) {
					final List<Object[]> list = new ArrayList<Object[]>();
					search(eh, query, finalOption, new Predicate<Object[]>() {
							@Override
							public boolean test(Object[] val) {
								list.add(val);
								return true;
							}
						}, null);
					return new SearchResult<Object[]>(counts, list);
				} else {
					EntityStreamSearchHandler<Object[]> ssh = new EntityStreamSearchHandler<>(eh, Object[].class);
					ssh.setTotalCount(counts);
					search(eh, query, finalOption, null, ssh);
					return ssh.getStreamSearchResult();
				}
			});

		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> SearchResult<T> searchEntity(Query query, SearchOption option) {
		final SearchOption finalOption;
		if (option == null) {
			finalOption = new SearchOption();
		} else {
			finalOption = option;
		}

		try {
			return withReadOnlyCheck(query, finalOption.getResultMode(), t -> {

				//TODO select * from の対応する？（*をどこまでと考える？別Entityの参照属性は？）

				int counts = -1;
				if (finalOption.isCountTotal()) {
					Query countQuery = query.copy();
					countQuery.setLimit(null);
					counts = count(countQuery);
				}

				//検索処理実行
				EntityHandler eh = getEntityHandler(query.getFrom().getEntityName());
				if (finalOption.getResultMode() == ResultMode.AT_ONCE) {
					final List<T> list = new ArrayList<T>();
					searchEntity(eh, query, finalOption, new Predicate<T>() {
						@Override
						public boolean test(T val) {
							list.add(val);
							return true;
						}
					}, null);
					return new SearchResult<T>(counts, list);
				} else {
					EntityStreamSearchHandler<Entity> ssh = new EntityStreamSearchHandler<>(eh, Entity.class);
					ssh.setTotalCount(counts);
					searchEntity(eh, query, finalOption, null, ssh);
					return (SearchResult<T>) ssh.getStreamSearchResult();
				}
			});

		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public <T extends Entity> void searchEntity(Query query, final Predicate<T> callback) {
		searchEntity(getEntityHandler(query.getFrom().getEntityName()), query, new SearchOption(), callback, null);
	}

	@Override
	public <T extends Entity> void searchEntity(Query query, SearchOption option, Predicate<T> callback) {
		if (option == null) {
			option = new SearchOption();
		}
		searchEntity(getEntityHandler(query.getFrom().getEntityName()), query, option, callback, null);
	}

	private <T extends Entity> void searchEntity(EntityHandler handler, Query query, SearchOption option,
			final Predicate<T> callback, EntityStreamSearchHandler<T> streamSearchHandler) {
		long time = 0;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("searchEntity():" + query);
				time = System.currentTimeMillis();
			}

			withReadOnlyCheck(query, option.getResultMode(), t -> {
				//検索処理実行
				if (streamSearchHandler == null) {
					new EntityQueryInvocationImpl(query,
							option,
							callback,
							InvocationType.SEARCH_ENTITY,
							ehService.getInterceptors(),
							handler).proceed();
				} else {
					new EntityQueryInvocationImpl(query,
							option,
							streamSearchHandler,
							InvocationType.SEARCH_ENTITY,
							ehService.getInterceptors(),
							handler).proceed();
				}
				return null;
			});

		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				if (streamSearchHandler == null) {
					logger.debug("searchEntity() end:time=" + (System.currentTimeMillis() - time) + "ms. query=" + query);
				} else {
					logger.debug("searchEntity() end (without resultset iterate):time=" + (System.currentTimeMillis() - time) + "ms. query=" + query);
				}
			}
		}
	}

	@Override
	public void search(Query query, final Predicate<Object[]> callback) {
		search(getEntityHandler(query.getFrom().getEntityName()), query, new SearchOption(), callback, null);
	}

	@Override
	public void search(Query query, SearchOption option, Predicate<Object[]> callback) {
		if (option == null) {
			option = new SearchOption();
		}
		search(getEntityHandler(query.getFrom().getEntityName()), query, option, callback, null);

	}

	private void search(EntityHandler handler, Query query, SearchOption option,
			Predicate<Object[]> callback, EntityStreamSearchHandler<Object[]> streamSearchHandler) {
		long time = 0;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("search():" + query);
				time = System.currentTimeMillis();
			}

			withReadOnlyCheck(query, option.getResultMode(), t -> {
				//検索処理実行
				if (streamSearchHandler == null) {
					new EntityQueryInvocationImpl(query,
							option,
							callback,
							InvocationType.SEARCH,
							ehService.getInterceptors(),
							handler).proceed();
				} else {
					new EntityQueryInvocationImpl(query,
							option,
							streamSearchHandler,
							InvocationType.SEARCH,
							ehService.getInterceptors(),
							handler).proceed();
				}
				return null;
			});
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		} finally {
			if (logger.isDebugEnabled()) {
				if (streamSearchHandler == null) {
					logger.debug("search() end:time=" + (System.currentTimeMillis() - time) + "ms. query=" + query);
				} else {
					logger.debug("search() end (without resultset iterate):time=" + (System.currentTimeMillis() - time) + "ms. query=" + query);
				}
			}
		}
	}

	@Override
	public String insert(Entity entity) {
		return insert(entity, new InsertOption());
	}

	@Override
	public String insert(Entity entity, InsertOption option) {
		if (option == null) {
			option = new InsertOption();
		}
		try {
			//TODO チェック処理の順番、要検討。クライアントにストレスのない順番に
			//FIXME 更新可能な項目以外を指定していないかどうかなどチェック
			if (logger.isDebugEnabled()) {
				logger.debug("insert():" + entity + ", option=" + option);
			}

			if (entity.getDefinitionName() == null) {
				//データモデル定義名が設定されていない
				throw new EntityRuntimeException("no definitionName");//TODO メッセージを埋める（メッセージ管理）
			}

			EntityHandler handler = getEntityHandler(entity.getDefinitionName());

			if (option.isEnableAuditPropertySpecification()) {
				//adminのみ可能
				AuthContext auth = AuthContext.getCurrentContext();
				User user = auth.getUser();
				if (user == null || !auth.getUser().isAdmin()) {
					throw new EntityRuntimeException("Only admin user can set enableAuditPropertySpecification to true.");
				}
			}

			//バリデーション
			if (option.isWithValidation()) {
				ValidateResult validateResult = new EntityValidateInvocationImpl(entity, option, ehService.getInterceptors(), handler).proceed();
				if (validateResult.hasError()) {
					throw new EntityValidationException("valiation error.", validateResult.getErrors());
				}
			}

			//追加
			EntityInsertInvocationImpl invocation = new EntityInsertInvocationImpl(entity, option, ehService.getInterceptors(), handler);

			return invocation.proceed();

		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public void update(Entity entity, UpdateOption option) {
		try {
			//TODO チェック処理の順番、要検討。クライアントにストレスのない順番に
			//FIXME 更新可能な項目以外を指定していないかどうかなどチェック
			if (logger.isDebugEnabled()) {
				logger.debug("update():" + entity + ", option=" + option);
			}

			if (entity.getDefinitionName() == null) {
				//データモデル定義名が設定されていない
				throw new EntityRuntimeException("no definitionName");//TODO メッセージを埋める（メッセージ管理）
			}
			if (option == null) {
				throw new EntityRuntimeException("updateOption is null");//TODO メッセージを埋める（メッセージ管理）
			}


			EntityHandler handler = getEntityHandler(entity.getDefinitionName());

			//バリデーション
			if (option.isWithValidation()) {
				ValidateResult validateResult = new EntityValidateInvocationImpl(entity, option.getUpdateProperties(), option, ehService.getInterceptors(), handler).proceed();
				if (validateResult.hasError()) {
					throw new EntityValidationException("valiation error.", validateResult.getErrors());
				}
			}

			//更新項目が現在値と同値の場合は更新しない
			if (!option.isForceUpdate()) {
				//TargetVersion.NEWの場合は、引数のEntityにversionが指定されているかどうかで比較対象を変える
				//TODO TargetVersion.NEWの場合はこの形でよいか？？
				//TODO タイムスタンプ使ってない場合、事前にロックしないと、項目単位で別ユーザに上書きされてる可能性ありなので気を付ける旨マニュアルなどに記述必要
				Entity currentStore = null;
				LoadOption lop = new LoadOption(true, false);
				if (option.isLocalized()) {
					lop.localized();
				}
				if (option.getTargetVersion() == TargetVersion.SPECIFIC) {
					if (entity.getVersion() == null) {
						throw new EntityRuntimeException("target version not specified:" + entity);
					}
					currentStore = load(entity.getOid(), entity.getVersion(), entity.getDefinitionName(), lop);
				} else if (option.getTargetVersion() == TargetVersion.NEW) {
					if (entity.getVersion() != null) {
						currentStore = load(entity.getOid(), entity.getVersion(), entity.getDefinitionName(), lop);
					} else {
						currentStore = load(entity.getOid(), entity.getDefinitionName(), lop);
					}
				} else {
					currentStore = load(entity.getOid(), entity.getDefinitionName(), lop);
				}
				if (currentStore == null) {
					throw new EntityConcurrentUpdateException(resourceString("impl.core.EntityManagerImpl.alreadyDeleted"));
				}

				UpdateOption toRealyUpate = checkSame(handler, currentStore, entity, option);
				if (toRealyUpate.getUpdateProperties().size() == 0) {
					logger.debug("no value is changed. so skip update process:" + entity.getDefinitionName() + "(oid=" + entity.getOid() + ")");
					return;
				}

				option = toRealyUpate;
			} else {
				option = option.copy();
			}

			//更新
			new EntityUpdateInvocationImpl(entity, option, ehService.getInterceptors(), handler).proceed();
			//TODO 何か結果が必要か
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	private UpdateOption checkSame(EntityHandler handler, Entity currentStore,
			Entity entity, UpdateOption option) {

		ArrayList<String> toReturn;

		if (!option.isForceUpdate()) {
			toReturn = new ArrayList<String>();
//			ExecuteContext ctx = ExecuteContext.getCurrentContext();
			EntityContext entityContext = EntityContext.getCurrentContext();
			for (String propName: option.getUpdateProperties()) {
				if (!propName.equals(Entity.UPDATE_BY)) {//更新者は内部で自動的に更新されるので比較対象外
					PropertyHandler ph = handler.getProperty(propName, entityContext);
					Object currentValue = currentStore.getValue(propName);
					Object newValue = entity.getValue(propName);

					if (ph instanceof PrimitivePropertyHandler) {
						//プリミティブ型の場合
						if (currentValue == null) {
							if (newValue != null) {
								if (newValue instanceof Object[]) {
									//長さ0の配列はnullと同値とする
									if (((Object[]) newValue).length != 0) {
										toReturn.add(propName);
									}
								} else {
									toReturn.add(propName);
								}
							}
						} else {
							if (currentValue instanceof Object[]) {
								if (!(newValue instanceof Object[])) {
									if (newValue == null) {
										//長さ0の配列はnullと同値とする
										if (((Object[]) currentValue).length != 0) {
											toReturn.add(propName);
										}
									} else {
										toReturn.add(propName);
									}
								} else if (!Arrays.equals((Object[]) currentValue, (Object[]) newValue)) {
									Object[] ca = (Object[]) currentValue;
									Object[] na = (Object[]) newValue;
									if (ca != na) {
										if (ca.length != na.length) {
											toReturn.add(propName);
										} else {
											for (int i = 0; i < ca.length; i++) {
												boolean isDiff = false;
												//BigDecimalの場合はcompareTo
												if (ca[i] instanceof BigDecimal && na[i] instanceof BigDecimal) {
													if (((BigDecimal) ca[i]).compareTo((BigDecimal) na[i]) != 0) {
														isDiff = true;
													}
												} else if (ca[i] instanceof SelectValue && na[i] instanceof String) {
													//SelectValue型に文字列型の値が設定された場合はvalue値と比較する
													String cv = ((SelectValue) ca[i]).getValue();
													if (cv == null || !cv.equals(na[i])) {
														isDiff = true;
													}
												} else {
													if (ca[i] == null ? na[i] != null : !ca[i].equals(na[i])) {
														isDiff = true;
													}
												}
												if (isDiff) {
													toReturn.add(propName);
													break;
												}
											}
										}
									}
								}
							} else {
								if (currentValue instanceof BigDecimal && newValue instanceof BigDecimal) {
									//BigDecimalの場合はcompareTo
									if (((BigDecimal) currentValue).compareTo((BigDecimal) newValue) != 0) {
										toReturn.add(propName);
									}
								} else if (currentValue instanceof SelectValue && newValue instanceof String) {
									//SelectValue型に文字列型の値が設定された場合はvalue値と比較する
									String cv = ((SelectValue) currentValue).getValue();
									if (cv == null || !cv.equals(newValue)) {
										toReturn.add(propName);
									}
								} else {
									if (!currentValue.equals(newValue)) {
										toReturn.add(propName);
									}
								}
							}
						}
					} else {
						//参照型の場合
						ReferencePropertyHandler rh = (ReferencePropertyHandler) ph;
						//mappedByでない参照のみチェック（mappedByは最終的に更新されない）
						if (rh.getMappedByPropertyHandler(entityContext) == null) {
							if (currentValue == null) {
								if (newValue != null) {
									if (newValue instanceof Object[]) {
										//長さ0の配列はnullと同値とする
										if (((Object[]) newValue).length != 0) {
											toReturn.add(propName);
										}
									} else {
										toReturn.add(propName);
									}
								}
							} else {
								if (newValue == null) {
									toReturn.add(propName);
								} else if (rh.getMetaData().getMultiplicity() != 1) {
									Entity[] currentRef = (Entity[]) currentValue;
									Entity[] newRef = (Entity[]) newValue;
							        int length = currentRef.length;
							        if (newRef.length != length) {
										toReturn.add(propName);
							        } else {
							        	boolean isChange = false;
								        for (int i = 0; i < length; i++) {
								        	Entity e1 = currentRef[i];
								        	Entity e2 = newRef[i];
								            if (!(e1 == null ?
								            		e2 == null
								            		: e1.getOid().equals(e2.getOid())
								            		&& (e2.getVersion() == null || e2.getVersion().equals(e1.getVersion())))) {
								            	isChange = true;
								            	break;
								            }
								        }
								        if (isChange) {
											toReturn.add(propName);
								        }
							        }
								} else {
									Entity currentRef = (Entity) currentValue;
									Entity newRef = (Entity) newValue;
									if (!(currentRef.getOid().equals(newRef.getOid())
											&& (newRef.getVersion() == null || newRef.getVersion().equals(currentRef.getVersion())))) {
										toReturn.add(propName);
									}
								}
							}
						}
					}
				}
			}
		} else {
			toReturn = new ArrayList<>(option.getUpdateProperties());
		}

		UpdateOption realyUpdate = new UpdateOption(option.isCheckTimestamp(), option.getTargetVersion());
		realyUpdate.setCheckLockedByUser(option.isCheckLockedByUser());
		realyUpdate.setUpdateProperties(toReturn);
		realyUpdate.setPurgeCompositionedEntity(option.isPurgeCompositionedEntity());
		realyUpdate.setForceUpdate(option.isForceUpdate());
		realyUpdate.setWithValidation(option.isWithValidation());
		realyUpdate.setNotifyListeners(option.isNotifyListeners());
		realyUpdate.setLocalized(option.isLocalized());
		return realyUpdate;
	}

	@Override
	public int deleteAll(DeleteCondition cond) {
		try {
			//TODO チェック処理の順番、要検討。クライアントにストレスのない順番に

			if (logger.isDebugEnabled()) {
				logger.debug("deleteAll():" + cond);
			}

			if (cond.getDefinitionName() == null) {
				//データモデル定義名が設定されていない
				throw new EntityRuntimeException("no definitionName");//TODO メッセージを埋める（メッセージ管理）
			}

			EntityHandler handler = getEntityHandler(cond.getDefinitionName());
			return new EntityDeleteAllInvocationImpl(cond, ehService.getInterceptors(), handler).proceed();
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public int updateAll(UpdateCondition cond) {
		try {
			//TODO チェック処理の順番、要検討。クライアントにストレスのない順番に
			//FIXME 更新可能な項目以外を指定していないかどうかなどチェック

			if (logger.isDebugEnabled()) {
				logger.debug("updateAll():" + cond);
			}

			if (cond.getDefinitionName() == null) {
				//データモデル定義名が設定されていない
				throw new EntityRuntimeException("no definitionName");//TODO メッセージを埋める（メッセージ管理）
			}

			EntityHandler handler = getEntityHandler(cond.getDefinitionName());
			return new EntityUpdateAllInvocationImpl(cond, ehService.getInterceptors(), handler).proceed();
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public ValidateResult validate(Entity entity, List<String> validatePropertyList) {
		try {
			if (entity.getDefinitionName() == null) {
				//データモデル定義名が設定されていない
				throw new EntityRuntimeException("no definitionName");//TODO メッセージを埋める（メッセージ管理）
			}
			EntityHandler handler = getEntityHandler(entity.getDefinitionName());
			return new EntityValidateInvocationImpl(entity, validatePropertyList, null, ehService.getInterceptors(), handler).proceed();
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public ValidateResult validate(Entity entity) {
		return validate(entity, null);
	}

	@Override
	public BinaryReference loadBinaryReference(long lobId) {
		try {
			//FIXME Lobを参照する時のSQL発行回数が多い。。。要対応


//			ExecuteContext ctx = ExecuteContext.getCurrentContext();
			EntityContext entityContext = EntityContext.getCurrentContext();

			LobHandler lm = LobHandler.getInstance(BinaryType.LOB_STORE_NAME);
			Lob bin = lm.getBinaryData(lobId);
			if (bin == null) {
				return null;
			}

			if (!lm.canAccess(bin)) {
				return null;
			}

			BinaryReference br = lm.toBinaryReference(bin, entityContext);
			return br;
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}


	@Override
	public BinaryReference createBinaryReference(String name, String type,
			InputStream is) {
		try {
			long start = 0L;
			if (logger.isDebugEnabled()) {
				start = System.currentTimeMillis();
			}

			try {
				LobHandler lm = LobHandler.getInstance(BinaryType.LOB_STORE_NAME);
				Lob bin = lm.crateBinaryDataTemporary(name, type, sessionService.getSession(true).getId());
				if (is != null) {
					byte[] buf = new byte[8192];
					int count;
					try (OutputStream os = bin.getBinaryOutputStream()) {
						while ((count = is.read(buf)) != -1) {
							os.write(buf, 0, count);
						}
						os.flush();
					} catch (IOException e) {
						throw new EntityRuntimeException(e);
					}
				}
				return lm.toBinaryReference(bin, EntityContext.getCurrentContext());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logger.warn("can not close inputstream resource:" + is, e);
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("createBinaryReference done.time:" + (System.currentTimeMillis() - start));
				}
			}
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public BinaryReference createBinaryReference(File file, String name, String type) {
		try {
			long start = 0L;
			if (logger.isDebugEnabled()) {
				start = System.currentTimeMillis();
			}

			if (!file.exists()) {
				throw new EntityRuntimeException("file is not exists:" + file.getPath());
			}

			if (file.isDirectory()) {
				throw new EntityRuntimeException("file is directory:" + file.getPath());
			}

			if (type == null) {
				try {
					type = Files.probeContentType(file.toPath());
				} catch (IOException e) {
					logger.warn("can't determine the MIME type due to IOException: " + file.getName(), e);
					type = "application/octet-stream";
				}
			}
			if (name == null) {
				name = file.getName();
			}

			try {
				LobHandler lm = LobHandler.getInstance(BinaryType.LOB_STORE_NAME);
				Lob bin = lm.crateBinaryDataTemporary(name, type, sessionService.getSession(true).getId());
				bin.transferFrom(file);
				return lm.toBinaryReference(bin, EntityContext.getCurrentContext());
			} catch (IOException e) {
				throw new EntityRuntimeException(e.getMessage(), e);
			} finally {
				if (logger.isDebugEnabled()) {
					logger.debug("createBinaryReference done.time:" + (System.currentTimeMillis() - start));
				}
			}
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public OutputStream getOutputStream(BinaryReference binaryReference) {
		try {
			LobHandler lm = LobHandler.getInstance(BinaryType.LOB_STORE_NAME);
			Lob bin = lm.getBinaryData(binaryReference.getLobId());
			if (bin == null) {
				return null;
			}
			if (!lm.canAccess(bin)) {
				return null;
			}

			return bin.getBinaryOutputStream();
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public InputStream getInputStream(BinaryReference binaryReference) {
		try {
			LobHandler lm = LobHandler.getInstance(BinaryType.LOB_STORE_NAME);
			Lob bin = lm.getBinaryData(binaryReference.getLobId());
			if (bin == null) {
				return null;
			}
			if (!lm.canAccess(bin)) {
				return null;
			}

			return bin.getBinaryInputStream();
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public boolean lockByUser(String oid, String definitionName) {
		try {
			EntityHandler handler = getEntityHandler(definitionName);
			return new EntityLockByUserInvocationImpl(oid, ExecuteContext.getCurrentContext().getClientId(), false, ehService.getInterceptors(), handler).proceed();
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public boolean unlockByUser(String oid, String definitionName) {
		try {
			AuthContext authContext = AuthContext.getCurrentContext();
			User user = authContext.getUser();
			EntityHandler handler = getEntityHandler(definitionName);
			if ((user != null && user.isAdmin()) || authContext.isPrivileged()) {
				return new EntityUnlockByUserInvocationImpl(oid, ExecuteContext.getCurrentContext().getClientId(), true, ehService.getInterceptors(), handler).proceed();
			} else {
				return new EntityUnlockByUserInvocationImpl(oid, ExecuteContext.getCurrentContext().getClientId(), false, ehService.getInterceptors(), handler).proceed();
			}
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public void purge(long rbid, String definitionName) {
		try {
			new EntityPurgeInvocationImpl(rbid, ehService.getInterceptors(), getEntityHandler(definitionName)).proceed();
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public Entity restore(long rbid, String definitionName) {
		try {
			return new EntityRestoreInvocationImpl(rbid, ehService.getInterceptors(), getEntityHandler(definitionName)).proceed();
		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public void getRecycleBin(String definitionName,
			Predicate<Entity> callback) {
		try {
			new EntityGetRecycleBinInvocationImpl(callback, null, ehService.getInterceptors(), getEntityHandler(definitionName)).proceed();
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public Entity getRecycleBin(long rbid, String definitionName) {
		try {

			Entity[] ret = new Entity[1];

			new EntityGetRecycleBinInvocationImpl(e -> {
				ret[0] = e;
				return false;
			}, rbid, ehService.getInterceptors(), getEntityHandler(definitionName)).proceed();
			return ret[0];
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@Override
	public Timestamp getCurrentTimestamp() {
		return ExecuteContext.getCurrentContext().getCurrentTimestamp();
	}

	@Override
	public Entity deepCopy(String oid, String definitionName) {
		return deepCopy(oid, definitionName, new DeepCopyOption());
	}

	@Override
	public Entity deepCopy(String oid, String definitionName, DeepCopyOption option) {
		try {
			ArrayList<EntityProcessCallback> callbacks = new ArrayList<EntityProcessCallback>();
			Entity entity = load(oid, definitionName);
			resetProperty(entity, callbacks, option.isShallowCopyLobData());
			String copyOid = insert(entity);

			for (EntityProcessCallback callback : callbacks) {
				callback.handle(entity);
			}

			return load(copyOid, definitionName);
		} catch (ApplicationException e) {
			//アプリケーション例外は自動的にsetRollbackOnly()はしない
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	private void resetProperty(Entity entity, ArrayList<EntityProcessCallback> callbacks, boolean shallowCopyLobData) {
//		ExecuteContext ctx = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();
		EntityDefinition ed = getEntityHandler(entity.getDefinitionName()).getMetaData().currentConfig(entityContext);

		entity.setOid(null);
		for (PropertyDefinition pd : ed.getPropertyList()) {
			if (pd instanceof AutoNumberProperty) {
				//AutoNumberは一応初期化
				entity.setValue(pd.getName(), null);
			} else if (pd instanceof BinaryProperty) {
				//Binaryはストリームをコピー
				Object value = null;
				if (pd.getMultiplicity() == 1) {
					BinaryReference br = entity.getValue(pd.getName());
					if (br != null) value = shallowCopyLobData ? br.copy() : createBinaryReference(br.getName(), br.getType(), getInputStream(br));
				} else {
					BinaryReference[] br = entity.getValue(pd.getName());
					if (br != null && br.length > 0) {
						BinaryReference[] _br = new BinaryReference[br.length];
						for (int i = 0; i < br.length; i++) {
							_br[i] = shallowCopyLobData ? br[i].copy() : createBinaryReference(br[i].getName(), br[i].getType(), getInputStream(br[i]));
						}
						value = _br;
					}
				}
				entity.setValue(pd.getName(), value);
			} else if (pd instanceof ReferenceProperty) {
				//Referenceはケースにより判断
				ReferenceProperty rp = (ReferenceProperty) pd;
				EntityDefinition red = getEntityHandler(rp.getObjectDefinitionName()).getMetaData().currentConfig(entityContext);
				String mappingClass = red.getMapping() != null ? red.getMapping().getMappingModelClass() : null;
				Object value = null;
				if (pd.getMultiplicity() == 1) {
					Entity ref = entity.getValue(pd.getName());
					try {
						value = copyReference(ref, rp, callbacks, shallowCopyLobData);
					} catch (EntityValidationException e) {
						setParentPropNameToValidateResult(e, rp);
						throw e;
					}
				} else {
					ArrayList<Entity> array = new ArrayList<Entity>();
					Entity[] _ref = entity.getValue(pd.getName());
					if (_ref != null) {
						try {
							for (Entity ref : _ref) {
								Entity ret = copyReference(ref, rp, callbacks, shallowCopyLobData);
								if (ret != null)
									array.add(ret);
							}
						} catch (EntityValidationException e) {
							setParentPropNameToValidateResult(e, rp);
							throw e;
						}
						if (mappingClass == null) {
							//Entityの配列作成
							value = array.toArray(new Entity[array.size()]);
						} else {
							//MappingClassの配列作成
							Class<Entity> cls;
							try {
								cls = (Class<Entity>) Class.forName(mappingClass);
								Object[] newArray = (Object[]) Array.newInstance(cls, array.size());
								for (int i = 0; i < array.size(); i++) {
									newArray[i] = array.get(i);
								}
								value = newArray;
							} catch (ClassNotFoundException e) {
								logger.error("mappingClass : " + mappingClass + "is Not Found.", e);
								value = array.toArray(new Entity[array.size()]);
							}
						}
					}
				}
				entity.setValue(pd.getName(), value);
			} else if (pd instanceof StringProperty) {
				if (pd.getIndexType() == IndexType.UNIQUE) {
					if (pd.getMultiplicity() == 1) {
						entity.setValue(pd.getName(), "copy of " + entity.getValue(pd.getName()));
					}
				} else if (pd.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL) {
					//null重複許可の場合は、nullをセット
					if (pd.getMultiplicity() == 1) {
						entity.setValue(pd.getName(), null);
					}
				}
			}
		}
	}

	private Entity copyReference(final Entity entity, final ReferenceProperty rp, ArrayList<EntityProcessCallback> callbacks, boolean shallowCopyLobData) {
		if (entity == null) return null;
		Entity ret = null;
		EntityDefinition ed = getEntityHandler(rp.getObjectDefinitionName()).getMetaData().currentConfig(EntityContext.getCurrentContext());
		final String mappingClass = ed.getMapping() != null ? ed.getMapping().getMappingModelClass() : null;
		if (rp.getReferenceType() == ReferenceType.ASSOCIATION) {
			//通常の参照
			if (rp.getMappedBy() == null || rp.getMappedBy().isEmpty()) {
				//参照、そのまま保持
				ret = entity;
			} else {
				//被参照、初期化する（必要なら参照先から参照する）
				ret = null;
			}
		} else {
			//親子関係
			if (rp.getMappedBy() == null || rp.getMappedBy().isEmpty()) {
				//参照、この時点で登録を行う
				ret = deepCopy(entity.getOid(), entity.getDefinitionName(), new DeepCopyOption(shallowCopyLobData));
			} else {
				//被参照、参照元が登録された後に登録する
				//戻りはnull、callback内で追加後に親にデータを設定
				//→参照を必須にしてる場合に落ちるため、コピー前のデータを保持させておく
				ret = entity;
				EntityProcessCallback callback = new EntityProcessCallback() {

					@SuppressWarnings("unchecked")
					@Override
					public void handle(Entity dataModel) {
						//参照先の再設定があるので直接deepCopyを呼べない
						ArrayList<EntityProcessCallback> callbacks = new ArrayList<EntityProcessCallback>();
						Entity ref = load(entity.getOid(), entity.getDefinitionName());
						resetProperty(ref, callbacks, shallowCopyLobData);
						ref.setValue(rp.getMappedBy(), dataModel);
						try {
							insert(ref);
							for (EntityProcessCallback callback : callbacks) {
								callback.handle(ref);
							}
						} catch (EntityValidationException e) {
							setParentPropNameToValidateResult(e, rp);
							throw e;
						}

						//親のデータを上書き
						if (rp.getMultiplicity() == 1) {
							dataModel.setValue(rp.getName(), ref);
						} else {
							Object value = dataModel.getValue(rp.getName());
							ArrayList<Entity> array = new ArrayList<Entity>();
							if (value instanceof Entity) {
								array.add((Entity) value);
							} else if (value instanceof Entity[]) {
								array.addAll(Arrays.asList((Entity[]) value));
							} else if (value instanceof Object[]) {
								for (Object obj : (Object[]) value) {
									array.add((Entity) obj);
								}
							}
							array.add(ref);
							if (mappingClass == null) {
								//Entityの配列作成
								dataModel.setValue(rp.getName(), array.toArray(new Entity[array.size()]));
							} else {
								//MappingClassの配列作成
								Class<Entity> cls;
								try {
									cls = (Class<Entity>) Class.forName(mappingClass);
									Object[] newArray = (Object[]) Array.newInstance(cls, array.size());
									for (int i = 0; i < array.size(); i++) {
										newArray[i] = array.get(i);
									}
									dataModel.setValue(rp.getName(), newArray);
								} catch (ClassNotFoundException e) {
									logger.error("mappingClass : " + mappingClass + "is Not Found.", e);
									dataModel.setValue(rp.getName(), array.toArray(new Entity[array.size()]));
								}
							}
						}
					}
				};
				callbacks.add(callback);
			}
		}
		return ret;
	}

	/**
	 * 親子関係を持つエンティティをディープコピーする場合、再帰的に参照先エンティティがコピーされるので、
	 * バリデーションエラーが発生した場合、親エンティティの参照プロパティ情報を設定する。
	 *
	 * @param e バリデーションエラー
	 * @param referenceProperty 親の参照プロパティ
	 */
	private void setParentPropNameToValidateResult(EntityValidationException e, ReferenceProperty referenceProperty) {
		ValidateError err = new ValidateError();
		err.setPropertyName(referenceProperty.getName());
		err.setPropertyDisplayName(I18nUtil.stringDef(referenceProperty.getDisplayName(), referenceProperty.getLocalizedDisplayNameList()));
		e.getValidateResults().add(err);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String defName, String fulltext) {
		return fulltextSearchService.fulltextSearchEntity(defName, fulltext);
	}

	@Override
	public List<String> fulltextSearchOidList(String defName, String fulltext) {
		return fulltextSearchService.fulltextSearchOidList(defName, fulltext);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(Map<String, List<String>> entityProperties,
			String fulltext) {
		return fulltextSearchService.fulltextSearchEntity(entityProperties, fulltext);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String fulltext, FulltextSearchOption option) {
		return fulltextSearchService.fulltextSearchEntity(fulltext, option);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(Query query, String fulltext, SearchOption option) {
		String defName = query.getFrom().getEntityName();
		List<String> oids = fulltextSearchService.fulltextSearchOidList(defName, fulltext);
		if (oids == null || oids.size() == 0) {
			if (option == null || !option.isCountTotal()) {
				return new SearchResult<T>(-1, null);
			}
			// SearchOptionでisCountTotalがtrueで0件を返します。
			return new SearchResult<T>(0, null);
		}
		Map<String, String> oidMap = oids.stream().collect(Collectors.toMap(oid -> oid, oid -> oid));

		Query cpQuery = query.copy();
		cpQuery.setLimit(null);

		final Limit limit = query.getLimit();
		final List<T> entityList = new ArrayList<>();
		final int[] count = new int[1];
		searchEntity(cpQuery, new Predicate<T>() {

			@Override
			public boolean test(T t) {
				if (oidMap.containsKey(t.getOid())) {
					count[0]++;
					if (limit == null) {
						entityList.add(t);
					} else {
						if (limit.getOffset() != Limit.UNSPECIFIED && count[0] <= limit.getOffset()) {
							return true;
						}
						if (limit.getLimit() != Limit.UNSPECIFIED && entityList.size() >= limit.getLimit()) {
							return option == null ? false : option.isCountTotal();
						}
						entityList.add(t);
					}
				}
				return true;
			}
		});

		if (option == null || !option.isCountTotal()) {
			return new SearchResult<T>(-1, entityList);
		}
		return new SearchResult<T>(count[0], entityList);
	}

	@Override
	public void bulkUpdate(BulkUpdatable bulkUpdatable) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("bulkUpdate():" + bulkUpdatable.getDefinitionName());
			}

			if (bulkUpdatable.getDefinitionName() == null) {
				//データモデル定義名が設定されていない
				throw new EntityRuntimeException("no definitionName");//TODO メッセージを埋める（メッセージ管理）
			}

			EntityHandler handler = getEntityHandler(bulkUpdatable.getDefinitionName());

			if (bulkUpdatable.isEnableAuditPropertySpecification()) {
				//adminのみ可能
				AuthContext auth = AuthContext.getCurrentContext();
				User user = auth.getUser();
				if (user == null || !auth.getUser().isAdmin()) {
					throw new EntityRuntimeException("Only admin user can set enableAuditPropertySpecification to true.");
				}
			}

			//追加
			EntityBulkUpdateInvocationImpl invocation = new EntityBulkUpdateInvocationImpl(bulkUpdatable, ehService.getInterceptors(), handler);

			invocation.proceed();

		} catch (ApplicationException e) {
			//更新操作が行われた可能性があるので、
			setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			setRollbackOnly();
			throw e;
		} catch (Error e) {
			setRollbackOnly();
			throw e;
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}

}
