/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.entity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.SearchResult.ResultMode;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.internal.InternalCredential;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.csv.QueryCsvWriter;
import org.iplass.mtp.impl.entity.csv.QueryWriteOption;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.entity.UpdateAllValue.UpdateAllValueType;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityToolService implements Service {
	
	private static Logger logger = LoggerFactory.getLogger(EntityToolService.class);
	
	private static final String USER_ENTITY = "mtp.auth.User";

	private AuthService as = null;
	
	private EntityManager em;
	private EntityDefinitionManager edm;

	@Override
	public void init(Config config) {
		as = ServiceRegistry.getRegistry().getService(AuthService.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	}

	@Override
	public void destroy() {
	}

	public void createJavaMappingClass(File file, EntityDefinition definition, String basePackage) {
		Path path = Paths.get(file.getPath().substring(0, file.getPath().length() - file.getName().length()));
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new EntityToolRuntimeException(getRS("createDirectoriesError"), e);
			}
		}

		String directClassName = StringUtil.isNotBlank(basePackage) ? basePackage + '.' + definition.getName() : null; 
		try (EntityJavaMappingClassWriter writer = 
				new EntityJavaMappingClassWriter(Files.newOutputStream(file.toPath()), definition, directClassName)) {
			writer.writeJavaClass();
		} catch (IOException e) {
			throw new EntityToolRuntimeException(getRS("unexpectedError"), e);
		}
	}

	public void createViewDDL(Path path, EntityDefinition... definitions) {
		try (EntityViewDDLWriter writer = new EntityViewDDLWriter(path)) {
			writer.write(definitions);
		} catch (IOException e) {
			throw new EntityToolRuntimeException(getRS("unexpectedError"), e);
		}
	}

	public int executeEQL(String eql, boolean isSearchAllVersion, boolean isCount) {
		Query query = new PreparedQuery(eql).query(null);
		if (!query.isVersioned() && isSearchAllVersion) {
			query.setVersioned(true);
		}

		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

		int count = 0;
		try (SearchResult<Object[]> result = em.search(query, new SearchOption(ResultMode.STREAM).unnotifyListeners())) {
			if (!isCount) {
				result.getFirst();
				return -1;
			}
			for (Object[] row : result) {
				if (row != null) {
					count++;
				}
			}
		}
		return count;
	}

	public int executeEQL(OutputStream out, String charset, String eql, boolean isSearchAllVersion) {
		Query query = null;

		try {
			query = new PreparedQuery(eql).query(null);
		} catch (QueryException e) {
			throw new EntityToolRuntimeException(getRS("invalidEQLStatement", eql), e);
		}

		if (!query.isVersioned() && isSearchAllVersion) {
			query.setVersioned(true);
		}

		try (QueryCsvWriter writer = new QueryCsvWriter(out, query, new QueryWriteOption().charset(charset))) {
			return writer.write();
		} catch (IOException e) {
			throw new EntityToolRuntimeException(getRS("errorOutputSearchResult"), e);
		}
	}

	public int executeEQLWithAuth(String eql, boolean isSearchAllVersion, boolean isCount) {
		return as.doSecuredAction(AuthContextHolder.getAuthContext().privilegedAuthContextHolder(), () -> {
			return executeEQL(eql, isSearchAllVersion, isCount);
		}).intValue();
	}

	public int executeEQLWithAuth(OutputStream out, String charset, String eql, boolean isSearchAllVersion) {
		return as.doSecuredAction(AuthContextHolder.getAuthContext().privilegedAuthContextHolder(), () -> {
			return executeEQL(out, charset, eql, isSearchAllVersion);
		}).intValue();
	}

	public int executeEQLWithAuth(String eql, boolean isSearchAllVersion, boolean isCount, String userId, String password) {
		try {
			as.login(StringUtil.isNotEmpty(password) ? new IdPasswordCredential(userId, password) : new InternalCredential(userId));
			return as.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
				return executeEQL(eql, isSearchAllVersion, isCount);
			}).intValue();
		} finally {
			as.logout();
		}
	}

	public int executeEQLWithAuth(OutputStream out, String charset, String eql, boolean isSearchAllVersion, String userId, String password) {
		try {
			as.login(StringUtil.isNotEmpty(password) ? new IdPasswordCredential(userId, password) : new InternalCredential(userId));
			return as.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
				return executeEQL(out, charset, eql, isSearchAllVersion);
			}).intValue();
		} finally {
			as.logout();
		}
	}
	
	public EntityUpdateAllResultInfo updateAll(final EntityUpdateAllCondition cond) {

		EntityUpdateAllResultInfo result = new EntityUpdateAllResultInfo();

		int updateCount = 0;
		try {
			UpdateCondition updateCond = new UpdateCondition(cond.getDefinitionName());

			EntityDefinition definition = getEntityDefinition(cond.getDefinitionName());

			List<String> validateErrors = new ArrayList<>();
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

			//更新不可の項目かをチェックするかを設定
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
	
	private EntityDefinition getEntityDefinition(final String defName) {
		return edm.get(defName);
	}
	
	public EntityDataDeleteResultInfo deleteAll(final int tenantId, final String defName, final String whereClause, final boolean isNotifyListeners, final int commitLimit) {
		return new Callable<EntityDataDeleteResultInfo>() {
			
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

						final List<Entity> entities = new ArrayList<>();
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
		}.call();
	}
	
	public EntityDataDeleteResultInfo deleteAllByOid(final int tenantId, final String defName, final List<String> oids, final boolean isNotifyListeners, final int commitLimit) {
		return new Callable<EntityDataDeleteResultInfo>() {
			int allCount = 0;

			@Override
			public EntityDataDeleteResultInfo call() {

				final EntityDataDeleteResultInfo result = new EntityDataDeleteResultInfo();

				try {
					LoadOption loadOption = new LoadOption(false, false);
					final List<Entity> entities = new ArrayList<>();
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
		}.call();
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

	private String getRS(String suffix, Object... arguments) {
		return ToolsResourceBundleUtil.resourceString("entitytool." + suffix, arguments);
	}

}
