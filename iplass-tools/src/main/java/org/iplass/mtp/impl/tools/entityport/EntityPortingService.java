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

package org.iplass.mtp.impl.tools.entityport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.DeleteTargetVersion;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityApplicationException;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity.UpdateMethod;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;
import org.iplass.mtp.entity.query.hint.FetchSizeHint;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.csv.EntityCsvException;
import org.iplass.mtp.impl.entity.csv.EntityCsvReader;
import org.iplass.mtp.impl.entity.csv.EntitySearchCsvWriter;
import org.iplass.mtp.impl.entity.csv.EntityWriteOption;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.parser.SyntaxService;
import org.iplass.mtp.impl.query.OrderBySyntax;
import org.iplass.mtp.impl.query.QuerySyntaxRegister;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.metaport.MetaDataTagEntity;
import org.iplass.mtp.impl.tools.pack.PackageEntity;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EntityのExport/Import用Service
 */
public class EntityPortingService implements Service {

	private static Logger logger = LoggerFactory.getLogger(EntityPortingService.class);
	private static Logger auditLogger = LoggerFactory.getLogger("mtp.audit.porting.entity");
	private static Logger toolLogger = LoggerFactory.getLogger("mtp.tools.entity");

	/** LOBデータ格納パス */
	public static final String ENTITY_LOB_DIR = "lobs/";

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSXXX";
	private static final String TIME_FORMAT = "HH:mm:ss";

	/** 更新除外プロパティ */
	private static Set<String> UN_UPDATABLE_PROPERTY
		= Collections.unmodifiableSet(new HashSet<>(Arrays.asList(new String[]{
				//キー項目は除外
				Entity.OID, Entity.VERSION,
				//作成日、作成者、更新日、更新者は除外
				Entity.CREATE_BY, Entity.CREATE_DATE, Entity.UPDATE_BY, Entity.UPDATE_DATE
				})));

	private SyntaxService syntaxService;

	private EntityManager em;
	private EntityDefinitionManager edm;

	@Override
	public void init(Config config) {
		syntaxService = config.getDependentService(SyntaxService.class);

		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	}

	@Override
	public void destroy() {
	}

	/**
	 * EntityデータをCSV形式でExportします。
	 *
	 * @param os        出力先CSVファイル(Stream)
	 * @param entry     出力対象Entity
	 * @param condition Export条件
	 * @return 出力件数
	 * @throws IOException
	 */
	public int write(final OutputStream os, final MetaDataEntry entry, final EntityDataExportCondition condition) throws IOException {

		return writeWithBinary(os, entry, condition, null);
	}

	/**
	 * EntityデータをCSV形式でExportします。
	 *
	 * @param os        出力先CSVファイル(Stream)
	 * @param entry     出力対象Entity
	 * @param condition Export条件
	 * @param zos    Lobを追加するZipのOutputStream
	 * @param lobPrefixPath LobをZipに追加する際のPrefixPath
	 * @return 出力件数
	 * @throws IOException
	 */
	public int writeWithBinary(final OutputStream os, final MetaDataEntry entry, final EntityDataExportCondition condition, final ZipOutputStream zos) throws IOException {
		return writeWithBinary(os, entry, condition, zos, null);
	}

	/**
	 * EntityデータをCSV形式でExportします。
	 *
	 * @param os        出力先CSVファイル(Stream)
	 * @param entry     出力対象Entity
	 * @param condition Export条件
	 * @param zos    Lobを追加するZipのOutputStream
	 * @param lobPrefixPath LobをZipに追加する際のPrefixPath
	 * @param exportBinaryDataDir Binaryデータの出力先ディレクトリ
	 * @return 出力件数
	 * @throws IOException
	 */
	public int writeWithBinary(final OutputStream os, final MetaDataEntry entry, final EntityDataExportCondition condition, final ZipOutputStream zos, String exportBinaryDataDir) throws IOException {

		EntityDefinition definition = edm.get(entry.getMetaData().getName());

		Where where = null;
		if (StringUtil.isNotEmpty(condition.getWhereClause())) {
			where = Where.newWhere("where " + condition.getWhereClause());
		}
		OrderBy orderBy = null;
		if (StringUtil.isNotEmpty(condition.getOrderByClause())) {
			try {
				SyntaxContext sc = syntaxService.getSyntaxContext(QuerySyntaxRegister.QUERY_CONTEXT);
				orderBy = sc.getSyntax(OrderBySyntax.class).parse(new ParseContext("order by " + condition.getOrderByClause()));
			} catch(ParseException e) {
				throw new EntityDataPortingRuntimeException(e);
			}
		} else {
			orderBy = new OrderBy();
			orderBy.add(new SortSpec(Entity.OID, SortType.ASC));
			orderBy.add(new SortSpec(Entity.VERSION, SortType.ASC));
		}

		//Writer生成
		EntityWriteOption option = new EntityWriteOption()
				.withReferenceVersion(true)
				.withBinary(true)
				.exportBinaryDataDir(exportBinaryDataDir)
				.where(where)
				.orderBy(orderBy)
				.dateFormat(DATE_FORMAT)
				.datetimeSecFormat(DATE_TIME_FORMAT)
				.timeSecFormat(TIME_FORMAT);
		int count = 0;
		try (EntitySearchCsvWriter writer = new EntitySearchCsvWriter(os, definition.getName(), option, zos)) {
			count = writer.write();
		}

		return count;
	}

	/**
	 * EntityデータをImportします。
	 *
	 * @param targetName インポート対象の名前(Package名またはCSVファイル名)
	 * @param is CSVファイル(Stream)
	 * @param entry 対象Entity
	 * @param condition Import条件
	 * @param zipFile LOBファイルが格納されているzipファイル(nullの場合、LOBファイルは取り込みません)
	 * @return Import結果
	 */
	public EntityDataImportResult importEntityData(String targetName, final InputStream is, final MetaDataEntry entry, final EntityDataImportCondition condition, final ZipFile zipFile) {
		return importEntityData(targetName, is, entry, condition, zipFile, null);
	}

	/**
	 * EntityデータをImportします。
	 *
	 * @param targetName インポート対象の名前(Package名またはCSVファイル名)
	 * @param is CSVファイル(Stream)
	 * @param entry 対象Entity
	 * @param condition Import条件
	 * @param zipFile LOBファイルが格納されているzipファイル(nullの場合、LOBファイルは取り込みません)
	 * @param importBinaryDataDir LOBファイルが格納されているディレクトリ(nullの場合、LOBファイルは取り込みません)
	 * @return Import結果
	 */
	public EntityDataImportResult importEntityData(String targetName, final InputStream is, final MetaDataEntry entry, final EntityDataImportCondition condition, final ZipFile zipFile, final String importBinaryDataDir) {

		toolLogger.info("start entity data import. {target:{}, entity:{}}", targetName, entry.getPath());

		EntityDataImportResult result = new EntityDataImportResult();
		try {
			if (PackageEntity.ENTITY_DEFINITION_NAME.equals(entry.getMetaData().getName())) {
				result.addMessages(rs("entityport.cantImportEntity", PackageEntity.ENTITY_DEFINITION_NAME));
				return result;
			}
			if (MetaDataTagEntity.ENTITY_DEFINITION_NAME.equals(entry.getMetaData().getName())) {
				result.addMessages(rs("entityport.cantImportEntity", MetaDataTagEntity.ENTITY_DEFINITION_NAME));
				return result;
			}

			EntityDefinition definition = edm.get(entry.getMetaData().getName());

			//全件削除
			if (condition.isTruncate()) {
				toolLogger.info("start entity data truncate. {target:{}, entity:{}}", targetName, entry.getPath());
				int delCount = truncateEntity(definition, condition, result);
				toolLogger.info("finish entity data truncate. {target:{}, entity:{}, count:{}}", targetName, entry.getPath(), delCount);
			}

			//インポート
			if (condition.isBulkUpdate()) {
				bulkImportCSV(is, definition, condition, zipFile, importBinaryDataDir, result);
			} else {
				sequenceImportCSV(is, definition, condition, zipFile, importBinaryDataDir, result);
			}

			return result;
		} finally {
			toolLogger.info("finish entity data import. {target:{}, entity:{}, result:{}}", targetName, entry.getPath(), (result.isError() ? "failed" : "success"));
		}
	}

	private int truncateEntity(final EntityDefinition definition, final EntityDataImportCondition cond, final EntityDataImportResult result) {

		return Transaction.requiresNew(new Function<Transaction, Integer>() {

			private boolean isUserEntity = false;
			private int delCount = 0;

			@Override
			public Integer apply(Transaction transaction) {

				try {
					final ExecuteContext executeContext = ExecuteContext.getCurrentContext();
					final EntityContext entityContext = EntityContext.getCurrentContext();
					final EntityHandler entityHandler = entityContext.getHandlerByName(definition.getName());

					auditLogger.info("deleteAll entity," + definition.getName());

					//ユーザーEntity以外の場合、deleteAll可能かチェック(deleteAllではListnerが実行されないため)
					boolean canDeleteAll = false;
					if (User.DEFINITION_NAME.equals(definition.getName())) {
						isUserEntity = true;
					} else {
						canDeleteAll = entityHandler.canDeleteAll();
					}

					if (canDeleteAll) {
						delCount = em.deleteAll(new DeleteCondition(definition.getName()));
					} else {
						//DeleteAll不可の場合、OIDを検索して1件1件削除
						Query query = new Query().select(Entity.OID).from(definition.getName());

						DeleteOption option = new DeleteOption(false);

						//物理削除(固定)
						option.setPurge(true);
						//Lockチェックしない(固定)
						option.setCheckLockedByUser(false);

						if (isUserEntity) {
							//ユーザーEntityの場合、実行者を除外する
							query.where(new NotEquals(User.OID, executeContext.getClientId()));

							//ユーザーEntityの場合、Listenerを実行してt_account削除
							option.setNotifyListeners(true);
						} else {
							//ユーザーEntity以外の場合、Listenerの実行は指定されたもの
							option.setNotifyListeners(cond.isNotifyListeners());
						}

						em.searchEntity(query, new Predicate<Entity>() {

							@Override
							public boolean test(Entity entity) {
								em.delete(entity, option);

								delCount++;
								return true;
							}
						});
					}

					result.addMessages(rs("entityport.truncateData", definition.getName(), delCount));
				} catch (EntityApplicationException e) {
					logger.error("An error occurred in the process of remove the data.", e);
					transaction.setRollbackOnly();
					result.setError(true);
					result.addMessages(e.getMessage());
				} catch (EntityRuntimeException e) {
					logger.error("An error occurred in the process of remove the data.", e);
					transaction.setRollbackOnly();
					result.setError(true);
					result.addMessages(e.getMessage());
				}
				return delCount;
			}

		});
	}

	private int sequenceImportCSV(final InputStream is, final EntityDefinition definition, final EntityDataImportCondition condition,
			final ZipFile zipFile, final String importBinaryDataDir, final EntityDataImportResult result) {

		return Transaction.required(new Function<Transaction, Integer>() {

			private int currentCount = 0;
			private int registCount = 0;

			@Override
			public Integer apply(Transaction transaction) {

				try (EntityCsvReader reader = new EntityCsvReader(definition, is)){

					reader.withReferenceVersion(true)
						.prefixOid(condition.getPrefixOid())
						.ignoreNotExistsProperty(condition.isIgnoreNotExistsProperty())
						.enableAuditPropertySpecification(condition.isInsertEnableAuditPropertySpecification());

					final Iterator<Entity> iterator = reader.iterator();
					final List<String> headerProperties = reader.properties();
					final boolean useCtrl = reader.isUseCtrl();

					while (iterator.hasNext()) {

						Transaction.requiresNew(new Consumer<Transaction>() {

							private int storeCount = 0;

							@Override
							public void accept(Transaction transaction) {

								try {
									while (iterator.hasNext()) {
										storeCount++;

										//Commit件数チェック
										if (condition.getCommitLimit() > 0
												&& (storeCount % (condition.getCommitLimit() + 1) == 0)) {
											break;
										}

										currentCount++;

										Entity entity = null;
										try {
											entity = iterator.next();

											//バイナリファイルの登録
											registBinaryReference(definition, entity, zipFile, importBinaryDataDir);

											//Entityの登録
											if (registEntity(condition, entity, definition, useCtrl, headerProperties, currentCount, result)){
												registCount++;
											}
										} catch (EntityDataPortingRuntimeException e) {
											String message = rs("entityport.updateErrMessage", definition.getName(), currentCount, e.getMessage(), getOidStatus(entity));
											if (condition.isErrorSkip()) {
												result.addMessages(message);
												result.errored();
											} else {
												result.errored();
												throw new EntityDataPortingRuntimeException(message , e);
											}
										} catch (EntityApplicationException e) {
											String message = rs("entityport.updateErrMessage", definition.getName(), currentCount, e.getMessage(), getOidStatus(entity));
											if (condition.isErrorSkip()) {
												result.addMessages(message);
												result.errored();
											} else {
												result.errored();
												throw new EntityDataPortingRuntimeException(message , e);
											}
										} catch (EntityRuntimeException e) {
											String message = rs("entityport.updateErrMessage", definition.getName(), currentCount, e.getMessage(), getOidStatus(entity));
											if (condition.isErrorSkip()) {
												result.addMessages(message);
												result.errored();
											} else {
												result.errored();
												throw new EntityDataPortingRuntimeException(message , e);
											}
										}
									}

								} catch (Throwable e) {
									logger.error("An error occurred in the process of import the entity data", e);
									transaction.setRollbackOnly();
									result.setError(true);
									if (e.getMessage() != null) {
										result.addMessages(e.getMessage());
									} else {
										result.addMessages(rs("entityport.importErrMessage", definition.getName(), e.getClass().getName()));
									}
								}

							}

						});

						//Loop内でエラー終了していた場合は抜ける
						if (result.isError()) {
							break;
						}

						if (logger.isDebugEnabled()) {
							logger.debug("commit " + definition.getName() + " data. currentCount=" + currentCount);
						}
						result.addMessages(rs("entityport.commitData", definition.getName(), currentCount));
					}

					if (result.getErrorCount() != 0) {
						result.setError(true);
					}

					String message = rs("entityport.resultInfo", definition.getName(), result.getInsertCount(), result.getUpdateCount(), result.getDeleteCount(), result.getErrorCount());
					if (condition.isNotifyListeners()) {
						message += "(Listner)";
					}
					if (condition.isWithValidation()) {
						message += "(Validation)";
					}
					result.addMessages(message);

				} catch (EntityDataPortingRuntimeException | EntityCsvException | IOException e) {
					logger.error("An error occurred in the process of import the entity data", e);
					transaction.setRollbackOnly();
					result.setError(true);
					if (e.getMessage() != null) {
						result.addMessages(e.getMessage());
					} else {
						result.addMessages(rs("entityport.importErrMessage", definition.getName(), e.getClass().getName()));
					}
				}
				return registCount;
			}

		});
	}

	private void registBinaryReference(final EntityDefinition definition, final Entity entity, final ZipFile zipFile, final String importBinaryDataDir) {

		if (zipFile == null && StringUtil.isEmpty(importBinaryDataDir)) {
			return;
		}

		definition.getPropertyList().forEach(property -> {

			Object value = entity.getValue(property.getName());
			if (value != null) {
				if (value instanceof BinaryReference) {
					BinaryReference br = registBinaryReference(definition, (BinaryReference)value, zipFile, importBinaryDataDir);
					entity.setValue(property.getName(), br);
				} else if (value instanceof BinaryReference[]) {
					BinaryReference[] brArray = (BinaryReference[])value;
					for (int i = 0; i < brArray.length; i++) {
						brArray[i] = registBinaryReference(definition, brArray[i], zipFile, importBinaryDataDir);
					}
					entity.setValue(property.getName(), brArray);
				}
			}
		});
	}

	private BinaryReference registBinaryReference(final EntityDefinition definition, final BinaryReference br, final ZipFile zipFile, final String importBinaryDataDir) {

		if (br != null) {
			String lobId = Long.toString(br.getLobId());
			String entryPath = ENTITY_LOB_DIR + definition.getName() + "." + lobId;

			if(zipFile != null) {
				ZipEntry zipEntry = zipFile.getEntry(entryPath);

				if (zipEntry == null) {
					logger.warn("Fail to find binary data. path = " + entryPath);
				} else {
					try (InputStream is = zipFile.getInputStream(zipEntry)){
						return em.createBinaryReference(br.getName(), br.getType(), is);
					} catch (IOException e) {
						logger.warn("Fail to create binary data. path = " + entryPath);
					}
				}
			} else if(StringUtil.isNotEmpty(importBinaryDataDir)) {
				File lobFile = Paths.get(importBinaryDataDir, entryPath).toFile();
				try (InputStream is = new FileInputStream(lobFile)){
					return em.createBinaryReference(br.getName(), br.getType(), is);
				} catch (IOException e) {
					logger.warn("Fail to create binary data. path = " + lobFile.getName());
				}
			}
		}
		return br;
	}

	private boolean registEntity(final EntityDataImportCondition cond, final Entity entity,
			final EntityDefinition definition, final boolean useCtrl, final List<String> headerProperties,
			int index, final EntityDataImportResult result) {

		ExecuteContext executeContext = ExecuteContext.getCurrentContext();
		EntityContext entityContext = EntityContext.getCurrentContext();
		EntityHandler entityHandler = entityContext.getHandlerByName(definition.getName());

		String uniqueKey = cond.getUniqueKey() != null ? cond.getUniqueKey() : Entity.OID;
		String uniqueValue = entity.getValue(uniqueKey);

		//更新の判断(指定されたUniqueKeyでチェック)
		TargetVersion updateTargetVersion = null;
		DeleteTargetVersion deleteTargetVersion = null;
		InsertOption insertOption = null;
		if (StringUtil.isNotEmpty(uniqueValue)) {

			if (definition.getVersionControlType() != VersionControlType.NONE) {
				//バージョン管理している場合
				if (entity.getVersion() != null) {
					//バージョン管理かつバージョンが指定されている場合はバージョンで検索
					String storedOid = getVersionEntityOid(definition.getName(), uniqueKey, uniqueValue, entity.getVersion(), entityHandler);

					if (StringUtil.isNotEmpty(storedOid)) {
						//指定されたUniqueKey、Versionが存在するので、そのデータをUpdate
						updateTargetVersion = TargetVersion.SPECIFIC;

						//削除の場合はversion指定で削除
						deleteTargetVersion = DeleteTargetVersion.SPECIFIC;

						//UniqueKeyで検索している可能性があるので登録済のOIDをセット
						entity.setOid(storedOid);
					} else {
						//指定されたUniqueKey、Versionが存在しないが、Versionは指定されているので指定バージョンでInsert
						insertOption = new InsertOption();
						insertOption.setVersionSpecified(true);

						//バージョンなしで検索(UniqueKeyで検索している可能性があるので登録済のOIDを取得)
						storedOid = getNoneVersionEntityOid(definition.getName(), uniqueKey, uniqueValue, entityHandler);
						if (StringUtil.isNotEmpty(storedOid)) {
							//UniqueKeyで検索している可能性があるので登録済のOIDをセット
							//見つからない場合は、CSVのデータをそのまま利用
							entity.setOid(storedOid);
						}
					}
				} else {
					//バージョンが未指定のため、バージョンなしで検索
					String storedOid = getNoneVersionEntityOid(definition.getName(), uniqueKey, uniqueValue, entityHandler);

					if (StringUtil.isNotEmpty(storedOid)) {
						//登録済のOidが存在する場合は、新しいVersionとしてUpdate
						//登録済のOidが存在しない場合は、更新できないので通常Insert
						updateTargetVersion = TargetVersion.NEW;

						//削除の場合は全version削除
						deleteTargetVersion = DeleteTargetVersion.ALL;

						//UniqueKeyで検索している可能性があるので登録済のOIDをセット
						entity.setOid(storedOid);
					}
				}
			} else {
				//バージョン管理していない場合
				//バージョンなしで検索
				String storedOid = getNoneVersionEntityOid(definition.getName(), uniqueKey, uniqueValue, entityHandler);

				if (StringUtil.isNotEmpty(storedOid)) {
					//指定されたUniqueKeyが存在するのでUpdate
					updateTargetVersion = TargetVersion.CURRENT_VALID;

					//削除の場合は全version削除
					deleteTargetVersion = DeleteTargetVersion.ALL;

					//UniqueKeyで検索している可能性があるので登録済のOIDをセット
					entity.setOid(storedOid);
				}
			}
		}

		//Userエンティティの場合の実行ユーザーチェック
		if (entity.getOid() != null && User.DEFINITION_NAME.equals(definition.getName())) {
			if (entity.getOid().equals(executeContext.getClientId())) {
				//UserEntityの場合、実行ユーザーは更新不可
				result.addMessages(rs("entityport.importUserSkipMessage", definition.getName(), index, entity.getOid(), entity.getValue(User.ACCOUNT_ID)));
				return false;
			}
		}

		//Controlフラグに対する整合性チェック
		if (useCtrl) {
			String ctrlCode = entity.getValue(EntityCsvReader.CTRL_CODE_KEY);
			if (StringUtil.isNotEmpty(ctrlCode)) {
				if (EntityCsvReader.CTRL_INSERT.equals(ctrlCode)) {
					if (updateTargetVersion != null) {
						//既に登録済
						throw new EntityDataPortingRuntimeException(rs("entityport.alreadyExists", definition.getName(), index, entity.getOid(), uniqueKey + "(" + uniqueValue + ")", ctrlCode));
					}
				} else if (EntityCsvReader.CTRL_UPDATE.equals(ctrlCode)) {
					if (updateTargetVersion == null) {
						//更新対象がない
						throw new EntityDataPortingRuntimeException(rs("entityport.notExistsForUpdate", definition.getName(), index, entity.getOid(), uniqueKey + "(" + uniqueValue + ")", ctrlCode));
					}
				} else if (EntityCsvReader.CTRL_DELETE.equals(ctrlCode)) {
					if (deleteTargetVersion == null) {
						//削除対象がない
						throw new EntityDataPortingRuntimeException(rs("entityport.notExistsForDelete", definition.getName(), index, entity.getOid(), uniqueKey + "(" + uniqueValue + ")", ctrlCode));
					}

					//削除処理
					DeleteOption option = new DeleteOption(false, deleteTargetVersion);
					option.setNotifyListeners(cond.isNotifyListeners());

					em.delete(entity, option);
					auditLogger.info("delete entity," + definition.getName() + ",oid:" + entity.getOid());

					if (logger.isDebugEnabled()) {
						logger.debug("delete " + definition.getName() + " data. oid=" + entity.getOid() + ", csv line no=" + index);
					}

					result.deleted();

					return true;
				}
			}
		}

		//登録、更新処理
		if (updateTargetVersion != null) {
			UpdateOption option = getUpdateOption(cond, headerProperties, updateTargetVersion, entityContext, entityHandler);
			em.update(entity, option);
			auditLogger.info("update entity," + definition.getName() + ",oid:" + entity.getOid() + " " + option);

			if (logger.isDebugEnabled()) {
				logger.debug("update " + definition.getName() + " data. oid=" + entity.getOid() + ", csv line no=" + index);
			}

			result.updated();
		} else {
			if (insertOption == null) {
				insertOption = new InsertOption();
			}
			//OID,AutoNumberは指定されていればそれを利用するためfalse
			insertOption.setRegenerateOid(false);
			insertOption.setRegenerateAutoNumber(false);

			insertOption.setEnableAuditPropertySpecification(cond.isInsertEnableAuditPropertySpecification());
			insertOption.setNotifyListeners(cond.isNotifyListeners());
			insertOption.setWithValidation(cond.isWithValidation());

			String oid = em.insert(entity, insertOption);
			auditLogger.info("insert entity," + definition.getName() + ",oid:" + oid + " " + insertOption);

			if (logger.isDebugEnabled()) {
				logger.debug("insert " + definition.getName() + " data. oid=" + oid + ", csv line no=" + index);
			}

			result.inserted();
		}

		return true;
	}

	private String getVersionEntityOid(String defName, String uniqueKey, String uniqueValue, Long version, EntityHandler entityHandler) {

		Query query = new Query().select(Entity.OID).from(defName);
		query.where(new And(new Equals(uniqueKey, uniqueValue), new Equals(Entity.VERSION, version)));
		query.versioned(true);
		query.getSelect().addHint(new FetchSizeHint(1));

		final String[] storedOid = new String[1];
		entityHandler.search(query, null, new Predicate<Object[]>() {
			@Override
			public boolean test(Object[] dataModel) {
				storedOid[0] = (String)dataModel[0];
				return false;	//1件でいい（OIDは一意）
			}
		});
		return storedOid[0];
	}

	private String getNoneVersionEntityOid(String defName, String uniqueKey, String uniqueValue, EntityHandler entityHandler) {
		Query query = new Query().select(Entity.OID).from(defName);
		query.where(new Equals(uniqueKey, uniqueValue));
		query.getSelect().addHint(new FetchSizeHint(1));

		final String[] storedOid = new String[1];
		entityHandler.search(query, null, new Predicate<Object[]>() {
			@Override
			public boolean test(Object[] dataModel) {
				storedOid[0] = (String)dataModel[0];
				return false;	//1件でいい（OIDは一意）
			}
		});
		return storedOid[0];
	}

	private UpdateOption getUpdateOption(final EntityDataImportCondition cond, final List<String> headerProperties,
			final TargetVersion updateTargetVersion, final EntityContext entityContext, final EntityHandler entityHandler) {

		UpdateOption option = new UpdateOption(false);
		option.setNotifyListeners(cond.isNotifyListeners());
		if (cond.isUpdateDisupdatableProperty()) {
			//更新不可項目も対象にする場合はValidationを実行しない
			option.setWithValidation(false);
		} else {
			//更新不可項目を対象にしない場合はValidationの実行は指定されたもの
			option.setWithValidation(cond.isWithValidation());
		}

		option.setUpdateProperties(getUpdateProperty(cond, headerProperties, entityContext, entityHandler));
		option.setTargetVersion(updateTargetVersion);
		option.setForceUpdate(cond.isFourceUpdate());

		return option;
	}

	private List<String> getUpdateProperty(final EntityDataImportCondition cond, final List<String> headerProperties,
			final EntityContext entityContext, final EntityHandler entityHandler) {

		//除外対象のプロパティチェック
		List<String> updateProperties = new ArrayList<>();
		//headerはmultipleの場合、同じものが含まれるためdistinct(set化で対応)
		for (String propName : headerProperties.stream().collect(Collectors.toSet())) {
			PropertyHandler ph = entityHandler.getProperty(propName, entityContext);
			if (ph != null) {
				if (cond.isUpdateDisupdatableProperty()) {
					//更新不可項目も含む場合

					//除外対象
					if (UN_UPDATABLE_PROPERTY.contains(propName)) {
						continue;
					}
					//被参照項目は除外
					if (ph instanceof ReferencePropertyHandler
							&& ((ReferencePropertyHandler) ph).getMetaData().getMappedByPropertyMetaDataId() != null) {
						continue;
					}
					//仮想項目は除外
					if (ph instanceof PrimitivePropertyHandler
							&& ((MetaPrimitiveProperty) ph.getMetaData()).getType().isVirtual()) {
						continue;
					}
					updateProperties.add(propName);
				} else {
					//更新不可項目を含まない場合
					if (ph.getMetaData().isUpdatable()) {
						updateProperties.add(propName);
					}
				}
			}
		}
		return updateProperties;
	}

	private String getOidStatus(final Entity entity) {
		return (entity == null ? "UnRead" : entity.getOid() != null ? entity.getOid() : "New");
	}

	private int bulkImportCSV(final InputStream is, final EntityDefinition definition, final EntityDataImportCondition condition,
			final ZipFile zipFile, final String importBinaryDataDir, final EntityDataImportResult result) {

		return Transaction.required(new Function<Transaction, Integer>() {

			private int registCount = 0;

			@Override
			public Integer apply(Transaction transaction) {

				try (EntityCsvReader reader = new EntityCsvReader(definition, is)){

					reader.withReferenceVersion(true)
						.prefixOid(condition.getPrefixOid())
						.ignoreNotExistsProperty(condition.isIgnoreNotExistsProperty())
						.enableAuditPropertySpecification(condition.isInsertEnableAuditPropertySpecification());

					final Iterator<Entity> iterator = reader.iterator();

					final EntityContext entityContext = EntityContext.getCurrentContext();
					final EntityHandler entityHandler = entityContext.getHandlerByName(definition.getName());
					final List<String> updateProperties = getUpdateProperty(condition, reader.properties(), entityContext, entityHandler);

					while (iterator.hasNext()) {

						int count = Transaction.requiresNew(new Function<Transaction, Integer>() {

							@Override
							public Integer apply(Transaction transaction) {

								try {
									PortingBulkUpdatable bulkUpdatable = new PortingBulkUpdatable(
											iterator, updateProperties, definition, condition, zipFile, importBinaryDataDir, result);
									em.bulkUpdate(bulkUpdatable);

									return bulkUpdatable.getRegistCount();

								} catch (Throwable e) {
									logger.error("An error occurred in the process of import the entity data", e);
									transaction.setRollbackOnly();
									result.setError(true);
									if (e.getMessage() != null) {
										result.addMessages(e.getMessage());
									} else {
										result.addMessages(rs("entityport.importErrMessage", definition.getName(), e.getClass().getName()));
									}

									return 0;
								}
							}
						});

						registCount += count;

						//Loop内でエラー終了していた場合は抜ける
						if (result.isError()) {
							break;
						}

						if (logger.isDebugEnabled()) {
							logger.debug("commit " + definition.getName() + " data. currentCount=" + count);
						}
						result.addMessages(rs("entityport.commitData", definition.getName(), count));
					}

					if (result.getErrorCount() != 0) {
						result.setError(true);
					}

					String message = rs("entityport.bulkResultInfo", definition.getName(), result.getInsertCount(), result.getUpdateCount(), result.getDeleteCount(), result.getMergeCount());
					result.addMessages(message);

				} catch (EntityDataPortingRuntimeException | EntityCsvException | IOException e) {
					logger.error("An error occurred in the process of import the entity data", e);
					transaction.setRollbackOnly();
					result.setError(true);
					if (e.getMessage() != null) {
						result.addMessages(e.getMessage());
					} else {
						result.addMessages(rs("entityport.importErrMessage", definition.getName(), e.getClass().getName()));
					}
				}
				return registCount;
			}

		});
	}

	private String rs(String key, Object... args) {
		return ToolsResourceBundleUtil.resourceString(key, args);
	}

	private class PortingBulkUpdatable implements BulkUpdatable {

		private Iterator<Entity> internal;
		private List<String> updateProperties;

		private EntityDefinition definition;
		private EntityDataImportCondition condition;
		private ZipFile zipFile;
		String importBinaryDataDir;
		private EntityDataImportResult result;

		private int registCount = 0;

		public PortingBulkUpdatable(Iterator<Entity> internal, List<String> updateProperties,
				EntityDefinition definition, EntityDataImportCondition condition, ZipFile zipFile, String importBinaryDataDir, EntityDataImportResult result) {
			this.internal = internal;
			this.updateProperties = updateProperties;
			this.definition = definition;
			this.condition = condition;
			this.zipFile = zipFile;
			this.importBinaryDataDir = importBinaryDataDir;
			this.result = result;
		}

		@Override
		public Iterator<BulkUpdateEntity> iterator() {
			return new BulkIterator(internal, definition, condition, zipFile, importBinaryDataDir);
		}

		@Override
		public String getDefinitionName() {
			return definition.getName();
		}

		@Override
		public List<String> getUpdateProperties() {
			return updateProperties;
		}

		@Override
		public boolean isEnableAuditPropertySpecification() {
			return condition.isInsertEnableAuditPropertySpecification();
		}

		@Override
		public void updated(BulkUpdateEntity updatedEntity) {
			registCount = getRegistCount() + 1;

			switch (updatedEntity.getMethod()) {
			case DELETE:
				result.deleted();
				break;
			case INSERT:
				result.inserted();
				break;
			case UPDATE:
				result.updated();
				break;
			case MERGE:
				//InsertかUpdateか不明のため
				result.merged();
				break;
			default:
			}
		}

		public int getRegistCount() {
			return registCount;
		}

	}

	private class BulkIterator implements Iterator<BulkUpdateEntity> {

		private Iterator<Entity> internal;
		private EntityDefinition definition;
		private EntityDataImportCondition condition;
		private ZipFile zipFile;
		private String importBinaryDataDir;

		private int storeCount = 0;

		private BulkIterator(Iterator<Entity> internal, EntityDefinition definition,
				EntityDataImportCondition condition, ZipFile zipFile, String importBinaryDataDir) {
			this.internal = internal;
			this.definition = definition;
			this.condition = condition;
			this.zipFile = zipFile;
			this.importBinaryDataDir = importBinaryDataDir;
		}

		@Override
		public boolean hasNext() {
			if (internal.hasNext()) {
				storeCount++;

				//Commit件数チェック
				if (condition.getCommitLimit() > 0
						&& (storeCount % (condition.getCommitLimit() + 1) == 0)) {
					return false;
				}

				return true;
			} else {
				return false;
			}
		}

		@Override
		public BulkUpdateEntity next() {
			Entity entity = internal.next();

			String ctrl = entity.getValue(EntityCsvReader.CTRL_CODE_KEY);
			if (condition.isTruncate()) {
				//全件削除している場合は無条件でINSERTに変更
				ctrl = EntityCsvReader.CTRL_INSERT;
			} else {
				if (ctrl == null) {
					if (entity.getValue(Entity.OID) == null) {
						ctrl = EntityCsvReader.CTRL_INSERT;
					} else {
						ctrl = EntityCsvReader.CTRL_MERGE;
					}
				}
			}

			//バイナリファイルの登録
			registBinaryReference(definition, entity, zipFile, importBinaryDataDir);

			switch (ctrl) {
			case EntityCsvReader.CTRL_DELETE:
				return new BulkUpdateEntity(UpdateMethod.DELETE, entity);
			case EntityCsvReader.CTRL_INSERT:
				return new BulkUpdateEntity(UpdateMethod.INSERT, entity);
			case EntityCsvReader.CTRL_MERGE:
				return new BulkUpdateEntity(UpdateMethod.MERGE, entity);
			case EntityCsvReader.CTRL_UPDATE:
				return new BulkUpdateEntity(UpdateMethod.UPDATE, entity);
			default:
				throw new IllegalArgumentException("unsupprot controll flag");
			}
		}
	}
}
