/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.csv;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.async.AsyncTaskInfo;
import org.iplass.mtp.async.AsyncTaskInfoSearchCondtion;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.async.AsyncTaskOption;
import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.async.TaskTimeoutException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.DeleteTargetVersion;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityDuplicateValueException;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityValidationException;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.entity.csv.EntityCsvException;
import org.iplass.mtp.impl.entity.csv.EntityCsvReader;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebResourceBundleUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvUploadService implements Service {

	private static Logger logger = LoggerFactory.getLogger(CsvUploadService.class);

	/** CSVアップロードキュー名 */
	private static final String CSV_UPLOAD_QUEUE = "csvupload";

	/** ファイルエンコード */
	private static final String ENCODE = "UTF-8";

	/** CSVアップロードエラー表示件数上限値 */
	private int showErrorLimitCount;

	/** CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定する。SQLServer対応。 */
	private boolean mustOrderByWithLimit;

	private EntityManager em = null;
	private EntityDefinitionManager edm = null;

	private enum ExecType {
		INSERT,				// version=0でインサート
		UPDATE_SPECIFIC,	// 特定バージョンでアップデート
		UPDATE_VALID,		// 現在有効なバージョンでアップデート
		UPDATE_NEW,			// 新しいバージョンでアップデート
	}

	@Override
	public void init(Config config) {

		em = ManagerLocator.manager(EntityManager.class);
		edm = ManagerLocator.manager(EntityDefinitionManager.class);

		showErrorLimitCount = config.getValue("showErrorLimitCount", Integer.class, 100);
		mustOrderByWithLimit = config.getValue("mustOrderByWithLimit", Boolean.class, false);
	}

	@Override
	public void destroy() {
	}

	/**
	 * CSVアップロードエラー表示件数の上限値を取得します。
	 * @return CSVアップロードエラー表示件数上限値
	 */
	public int getShowErrorLimitCount() {
		return showErrorLimitCount;
	}

	/**
	 * CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定するかを取得します。
	 * SQLServerでのダウロード処理用のフラグです。
	 * @return CSVダウンロード時にLimitが指定されている場合にOrderByを必ず指定するか
	 */
	public boolean isMustOrderByWithLimit() {
		return mustOrderByWithLimit;
	}

	/**
	 * Uploadファイルを検証します。
	 */
	public void validate(InputStream is, String defName, final boolean withReferenceVersion) {
		validate(is, defName, withReferenceVersion, showErrorLimitCount);
	}

	/**
	 * Uploadファイルを検証します。
	 */
	public void validate(InputStream is, String defName, final boolean withReferenceVersion, final int errorLimit) {

		EntityDefinition ed  = edm.get(defName);

		try (EntityCsvReaderForCheck reader = new EntityCsvReaderForCheck(ed, is, ENCODE, errorLimit)){
			reader.withReferenceVersion(withReferenceVersion);
			reader.check();
		} catch (UnsupportedEncodingException e) {
			throw new EntityCsvException("CE0001", resourceString("impl.csv.CsvUploadService.invalidFileMsg"));
		} catch (EntityCsvException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (ApplicationException e) {
			logger.error(e.getMessage(), e);
			throw new EntityCsvException("CE9000", e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new EntityCsvException("CE9000", resourceString("impl.csv.CsvUploadService.errReadFile"));
		}


	}

	/**
	 * Csvファイルをアップロードします。
	 *
	 */
	@Deprecated
	public CsvUploadStatus upload(final InputStream is, final String defName, final String uniqueKey,
			final TransactionType transactionType, final int commitLimit,
			final boolean withReferenceVersion, final boolean deleteSpecificVersion) {
		return upload(is, defName, uniqueKey, false, false, false, null, null, transactionType, commitLimit, withReferenceVersion, deleteSpecificVersion);
	}

	/**
	 * Csvファイルをアップロードします。
	 *
	 */
	public CsvUploadStatus upload(final InputStream is, final String defName, final String uniqueKey,
			final boolean isDenyInsert, final boolean isDenyUpdate, final boolean isDenyDelete,
			final Set<String> insertProperties, final Set<String> updateProperties,
			final TransactionType transactionType, final int commitLimit,
			final boolean withReferenceVersion, final boolean deleteSpecificVersion) {

		final CsvUploadStatus result = new CsvUploadStatus();
		final EntityDefinition ed  = edm.get(defName);

		try (EntityCsvReader reader = new EntityCsvReader(ed, is, ENCODE)){
			reader.withReferenceVersion(withReferenceVersion);

			Transaction.with(Propagation.SUPPORTS, t -> {

				int readCount = 0;
				int insertCount = 0;
				int updateCount = 0;
				int deleteCount = 0;

				List<String> properties = reader.properties();
				boolean useCtrl = reader.isUseCtrl();
				Set<String> updatablePropperties = null;
				Map<Object, String> keyValueMap = new HashMap<>();

				Iterator<Entity> iterator = reader.iterator();

				while(iterator.hasNext()) {

					final ImportFunction func = new ImportFunction(em)
							.ed(ed)
							.iterator(iterator)
							.isDenyInsert(isDenyInsert)
							.isDenyUpdate(isDenyUpdate)
							.isDenyDelete(isDenyDelete)
							.insertProperties(insertProperties)
							.updateProperties(updateProperties)
							.transactionType(transactionType)
							.commitLimit(commitLimit)
							.useCtrl(useCtrl)
							.uniqueKey(uniqueKey)
							.properties(properties)
							.updatablePropperties(updatablePropperties)
							.keyValueMap(keyValueMap)
							.deleteSpecificVersion(deleteSpecificVersion);

					ApplicationException ex = null;
					try {
						//別トランザクションで取込
						Transaction.requiresNew(func);

						//更新対象Propertyを保持
						updatablePropperties = func.updatablePropperties();

					} catch (EntityDuplicateValueException e) {
						ex = new ApplicationException(resourceString("impl.csv.CsvUploadService.rowMsg", readCount + func.readCount())
								+ resourceString("impl.csv.CsvUploadService.overlapMsg"));
					} catch (EntityValidationException e) {
						String errorMessage = " ";
						if (e.getValidateResults() != null) {
							for (ValidateError ve : e.getValidateResults()) {
								errorMessage = errorMessage + ve.getPropertyName() + " : " + ve.getErrorMessages() + ", ";
							}
						}
						ex = new ApplicationException(resourceString("impl.csv.CsvUploadService.rowMsg", readCount + func.readCount())
								+ resourceString("impl.csv.CsvUploadService.validationMsg") + errorMessage);
					} catch (Exception e) {
						ex = new ApplicationException(resourceString("impl.csv.CsvUploadService.rowMsg", readCount + func.readCount()) + e.getMessage());
					} finally {
						//件数を更新
						readCount+= func.readCount();
						insertCount+= func.insertCount();
						updateCount+= func.updateCount();
						deleteCount+= func.deleteCount();
					}

					if (ex != null) {
						result.setInsertCount(insertCount);
						result.setUpdateCount(updateCount);
						result.setDeleteCount(deleteCount);
						throw ex;
					}

				};

				result.setInsertCount(insertCount);
				result.setUpdateCount(updateCount);
				result.setDeleteCount(deleteCount);
				result.setStatus(TaskStatus.COMPLETED);
			});

			return result;

		} catch (UnsupportedEncodingException e) {
			return asFailedResult(result, "CE0001", resourceString("impl.csv.CsvUploadService.invalidFileMsg"));
		} catch (EntityCsvException e) {
			logger.error(e.getMessage(), e);
			return asFailedResult(result, e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return asFailedResult(result, "CE9000", e.getMessage());
		}
	}

	private CsvUploadStatus asFailedResult(CsvUploadStatus result, String code, String message) {
		result.setCode(code);
		result.setMessage(message);
		result.setStatus(TaskStatus.ABORTED);
		return result;
	}

	/**
	 * Csvファイルを非同期でアップロードします。
	 */
	@Deprecated
	public void asyncUpload(final InputStream is, final String fileName, final String defName, final String parameter, final String uniqueKey,
			final TransactionType transactionType, final int commitLimit,
			final boolean withReferenceVersion, final boolean deleteSpecificVersion) {
		asyncUpload(is, fileName, defName, parameter, uniqueKey, false, false, false, null, null, transactionType, commitLimit, withReferenceVersion, deleteSpecificVersion);
	}

	/**
	 * Csvファイルを非同期でアップロードします。
	 */
	public void asyncUpload(final InputStream is, final String fileName, final String defName, final String parameter, final String uniqueKey,
			final boolean isDenyInsert, final boolean isDenyUpdate, final boolean isDenyDelete,
			final Set<String> insertProperties, final Set<String> updateProperties,
			final TransactionType transactionType, final int commitLimit,
			final boolean withReferenceVersion, final boolean deleteSpecificVersion) {

		// 非同期で実行するので、リクエスト処理完了時にアップロードファイルは削除されるため
		// 一時ファイルとしてコピーしておく
		Path destPath = copyUploadFile(is);

		//非同期実行用のタスクを定義
		CsvUploadTask task = new CsvUploadTask(
				destPath.toString(),
				fileName,
				DateUtil.getCurrentTimestamp().getTime(),
				defName,
				parameter,
				uniqueKey,
				isDenyInsert,
				isDenyUpdate,
				isDenyDelete,
				insertProperties,
				updateProperties,
				transactionType,
				commitLimit,
				withReferenceVersion,
				deleteSpecificVersion);

		AsyncTaskOption option = new AsyncTaskOption();
		option.setExceptionHandlingMode(ExceptionHandlingMode.ABORT);
		option.setGroupingKey(AuthContext.getCurrentContext().getUser().getOid());
		option.setQueue(CSV_UPLOAD_QUEUE);
		option.setReturnResult(true);

		ManagerLocator.manager(AsyncTaskManager.class).execute(option, task);

	}

	private Path copyUploadFile(InputStream is) {

		// 一時ディレクトリ取得
		WebFrontendService webFront = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
		File tempDir = null;
		if (webFront.getTempFileDir() == null) {
			WebRequestStack reqStack = WebRequestStack.getCurrent();
			if (reqStack != null && reqStack.getRequest() != null) {
				tempDir = (File) reqStack.getRequest().getServletContext().getAttribute(ServletContext.TEMPDIR);
			}
		} else {
			tempDir = new File(webFront.getTempFileDir());
		}

		Path distFile = null;
		try {
			distFile = Files.createTempFile(tempDir.toPath(), "mtp", ".csv");
			Files.copy(is, distFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (RuntimeException | IOException e) {
			if (distFile != null) {
				try {
					Files.deleteIfExists(distFile);
				} catch (IOException e1) {
					// 一時ファイルが削除出来なかった場合でもエラーとしない
					logger.warn("Fail to delete a Temporary's File.", e1);
				}
			}
			throw new EntityCsvException(e);
		}
		return distFile;
	}

	/**
	 * 非同期アップロード処理状況を返します。
	 *
	 * @return アップロード処理状況
	 */
	public List<CsvUploadStatus> getStatus() {
		List<CsvUploadStatus> taskStatusList = new ArrayList<>();

		AsyncTaskInfoSearchCondtion cond = new AsyncTaskInfoSearchCondtion();
		cond.setQueue(CSV_UPLOAD_QUEUE);
		cond.setGroupingKey(AuthContext.getCurrentContext().getUser().getOid());
		cond.setWithHistory(true);

		AsyncTaskManager atm = ManagerLocator.manager(AsyncTaskManager.class);
		List<AsyncTaskInfo> infoList = atm.searchAsyncTaskInfo(cond);

		for (AsyncTaskInfo info : infoList) {
			AsyncTaskInfo detail = atm.loadAsyncTaskInfo(info.getTaskId(), CSV_UPLOAD_QUEUE);
			CsvUploadStatus result = null;

			if (TaskStatus.RETURNED.equals(info.getStatus())) {
				try {
					Future<CsvUploadStatus> future = atm.getResult(info.getTaskId(), CSV_UPLOAD_QUEUE);
					result = future.get();
				} catch (Exception e) {
					throw new SystemException(e);
				}
			} else if (detail.getResult() != null) {
				if (detail.getResult() instanceof CsvUploadStatus) {
					result = (CsvUploadStatus) detail.getResult();
				} else if (detail.getResult() instanceof TaskTimeoutException) {
					result = new CsvUploadStatus();
					result.setStatus(TaskStatus.ABORTED);
					result.setMessage(resourceString("impl.csv.CsvUploadService.timeoutMsg"));
				}
			}

			if (result == null) {
				result = new CsvUploadStatus();
				result.setStatus(info.getStatus());
			}

			CsvUploadTask task = (CsvUploadTask) detail.getTask();
			result.setFileName(task.getFileName());
			result.setUploadDateTime(task.getUploadDateTime());
			result.setDefName(task.getDefName());
			result.setParameter(task.getParameter());

			taskStatusList.add(result);
		}

		return taskStatusList;
	}

	private static String resourceString(String key, Object... arguments) {
		return WebResourceBundleUtil.resourceString(key, arguments);
	}

	private static class ImportFunction implements Consumer<Transaction> {

		private int readCount = 0;
		private int insertCount = 0;
		private int updateCount = 0;
		private int deleteCount = 0;

		private EntityManager em;
		private EntityDefinition ed;
		private Iterator<Entity> iterator;
		private TransactionType transactionType;
		private boolean useCtrl;
		private boolean isDenyInsert;
		private boolean isDenyUpdate;
		private boolean isDenyDelete;
		private Set<String> insertProperties;
		private Set<String> updateProperties;
		private int commitLimit;
		private String uniqueKey = Entity.OID;
		private List<String> properties;
		private Set<String> updatablePropperties;
		private Map<Object, String> keyValueMap;
		private boolean deleteSpecificVersion;

		public ImportFunction(EntityManager em) {
			this.em = em;
		}

		public ImportFunction ed(EntityDefinition ed) {
			this.ed = ed;
			return this;
		}
		public ImportFunction iterator(Iterator<Entity> iterator) {
			this.iterator = iterator;
			return this;
		}
		public ImportFunction isDenyInsert(boolean isDenyInsert) {
			this.isDenyInsert = isDenyInsert;
			return this;
		}
		public ImportFunction isDenyUpdate(boolean isDenyUpdate) {
			this.isDenyUpdate = isDenyUpdate;
			return this;
		}
		public ImportFunction isDenyDelete(boolean isDenyDelete) {
			this.isDenyDelete = isDenyDelete;
			return this;
		}
		public ImportFunction insertProperties(Set<String> insertProperties) {
			this.insertProperties = insertProperties;
			return this;
		}
		public ImportFunction updateProperties(Set<String> updateProperties) {
			this.updateProperties = updateProperties;
			return this;
		}
		public ImportFunction transactionType(TransactionType transactionType) {
			this.transactionType = transactionType;
			return this;
		}
		public ImportFunction commitLimit(int commitLimit) {
			this.commitLimit = commitLimit;
			return this;
		}
		public ImportFunction useCtrl(boolean useCtrl) {
			this.useCtrl = useCtrl;
			return this;
		}
		public ImportFunction uniqueKey(String uniqueKey) {
			if (uniqueKey != null) {
				this.uniqueKey = uniqueKey;
			}
			return this;
		}
		public ImportFunction properties(List<String> properties) {
			this.properties = properties;
			return this;
		}
		public ImportFunction updatablePropperties(Set<String> updatablePropperties) {
			this.updatablePropperties = updatablePropperties;
			return this;
		}
		public ImportFunction keyValueMap(Map<Object, String> keyValueMap) {
			this.keyValueMap = keyValueMap;
			return this;
		}
		public ImportFunction deleteSpecificVersion(boolean deleteSpecificVersion) {
			this.deleteSpecificVersion = deleteSpecificVersion;
			return this;
		}

		public int readCount() {
			return readCount;
		}
		public int insertCount() {
			return insertCount;
		}
		public int updateCount() {
			return updateCount;
		}
		public int deleteCount() {
			return deleteCount;
		}
		public Set<String> updatablePropperties() {
			return updatablePropperties;
		}

		@Override
		public void accept(Transaction transaction) {

			try {
				while(iterator.hasNext()) {

					if (transactionType == TransactionType.DIVISION && readCount == commitLimit) {
						break;
					}

					Entity entity = iterator.next();
					readCount ++;

					Object uniqueKeyValue = entity.getValue(uniqueKey);
					String ctrlCode = entity.getValue(EntityCsvReader.CTRL_CODE_KEY);
					if (StringUtil.isEmpty(ctrlCode)) {
						ctrlCode = EntityCsvReader.CTRL_MERGE;
					}

					if (useCtrl && ctrlCode.equals(EntityCsvReader.CTRL_DELETE)) {
						if(isDenyDelete) {
							throw new ApplicationException(resourceString("impl.csv.CsvUploadService.denyDeleteError"));
						}
						DeleteTargetVersion deleteTargetVersion = DeleteTargetVersion.ALL;

						SearchResult<Entity> searchResult = null;

						// 特定versionを削除するか
						if (ed.getVersionControlType() != VersionControlType.NONE
								&& deleteSpecificVersion && entity.getVersion() != null) {

							searchResult = em.searchEntity(onVersionQuery(ed.getName(), uniqueKey, uniqueKeyValue, entity.getVersion()));

							//特定versionのみ削除
							deleteTargetVersion = DeleteTargetVersion.SPECIFIC;

						} else {
							searchResult = em.searchEntity(noVersionQuery(ed.getName(), uniqueKey, uniqueKeyValue));
						}

						if (searchResult.getFirst() == null) {
							continue;
						}

						entity.setOid(searchResult.getFirst().getOid());
						em.delete(entity, new DeleteOption(false, deleteTargetVersion));
						deleteCount ++;
						continue;
					}

					ExecType execType = execType(ed, uniqueKey, uniqueKeyValue, entity, keyValueMap);

					if (useCtrl) {
						if (execType == ExecType.INSERT) {
							if (!(ctrlCode.equals(EntityCsvReader.CTRL_INSERT) || ctrlCode.equals(EntityCsvReader.CTRL_MERGE))) {
								throw new ApplicationException(resourceString("impl.csv.CsvUploadService.ctrlFlgUpdateError"));
							}
						}

						if (execType == ExecType.UPDATE_SPECIFIC
								|| execType == ExecType.UPDATE_VALID
								|| execType == ExecType.UPDATE_NEW) {
							if (!(ctrlCode.equals(EntityCsvReader.CTRL_UPDATE) || ctrlCode.equals(EntityCsvReader.CTRL_MERGE))) {
								throw new ApplicationException(resourceString("impl.csv.CsvUploadService.ctrlFlgInsertError"));
							}
						}
					}

					switch (execType) {
					case INSERT:
						if(isDenyInsert) {
							throw new ApplicationException(resourceString("impl.csv.CsvUploadService.denyInsertError"));
						}
						if(insertProperties != null) {
							properties.stream()
								.filter(property -> !insertProperties.contains(property))
								.forEach(property -> entity.setValue(property, null));
						}
						entity.setLockedBy(null);	//lockedByは指定されていても無視
						String insertOid = em.insert(entity);
						keyValueMap.put(uniqueKeyValue, insertOid);
						insertCount ++;
						break;
					case UPDATE_SPECIFIC:
						if(isDenyUpdate) {
							throw new ApplicationException(resourceString("impl.csv.CsvUploadService.denyUpdateError"));
						}
						em.update(entity, updateOption(TargetVersion.SPECIFIC));
						keyValueMap.put(uniqueKeyValue, entity.getOid());
						updateCount++;
						break;
					case UPDATE_VALID:
						if(isDenyUpdate) {
							throw new ApplicationException(resourceString("impl.csv.CsvUploadService.denyUpdateError"));
						}
						em.update(entity, updateOption(TargetVersion.CURRENT_VALID));
						keyValueMap.put(uniqueKeyValue, entity.getOid());
						updateCount++;
						break;
					case UPDATE_NEW:
						if(isDenyUpdate) {
							throw new ApplicationException(resourceString("impl.csv.CsvUploadService.denyUpdateError"));
						}
						em.update(entity, updateOption(TargetVersion.NEW));
						keyValueMap.put(uniqueKeyValue, entity.getOid());
						updateCount++;
						break;
					default:
						throw new ApplicationException(resourceString("impl.csv.CsvUploadService.invalid"));
					}
				}

			} catch(Exception e) {
				//トランザクション分割の場合はここでコミット
				if (transactionType == TransactionType.DIVISION && !transaction.isRollbackOnly()) {
					transaction.commit();
				} else {
					//ロールバックするので更新件数を初期化
					insertCount = 0;
					updateCount = 0;
					deleteCount = 0;
				}
				throw e;
			}
		}

		private ExecType execType(EntityDefinition ed, String uniqueKey, Object uniqueKeyValue, Entity entity, Map<Object, String> keyValueMap) {

			ExecType execType = null;

			if (uniqueKeyValue == null) {
				execType = ExecType.INSERT;
			} else {
				if (ed.getVersionControlType() != VersionControlType.NONE) {
					// バージョン管理している

					if (keyValueMap.containsKey(uniqueKeyValue)) {
						// 既にOIDはCSVの前行までに登録、更新済の状態

						// UniqueKeyで指定している場合、またはINSERT時のOID採番値で置き換え(INSERT時はCSVのOIDとは異なる値で登録される)
						entity.setOid(keyValueMap.get(uniqueKeyValue));

						// UniqueKeyを登録されているOIDにスイッチ
						uniqueKey = Entity.OID;
						uniqueKeyValue = entity.getOid();
					}

					if (entity.getVersion() != null) {
						// 全バージョン含めて登録済のバージョンデータを検索
						Query versionedQuery = onVersionQuery(ed.getName(), uniqueKey, uniqueKeyValue, entity.getVersion());
						SearchResult<Entity> versionedResult = em.searchEntity(versionedQuery, new SearchOption().countTotal());
						if (versionedResult.getTotalCount() > 0) {
							// 指定バージョンが存在する場合は対象バージョンを更新
							execType = ExecType.UPDATE_SPECIFIC;

							// UniqueKeyで検索している可能性があるので登録済のOIDをセット
							entity.setOid(versionedResult.getFirst().getOid());
						} else {
							// Oidの登録状態で実行タイプを決定
							execType = versionedExecType(ed, uniqueKey, uniqueKeyValue, entity);
						}
					} else {
						// Oidの登録状態で実行タイプを決定
						execType = versionedExecType(ed, uniqueKey, uniqueKeyValue, entity);
					}
				} else {
					// バージョン管理していない
					SearchResult<Entity> searchResult
						= em.searchEntity(noVersionQuery(ed.getName(), uniqueKey, uniqueKeyValue), new SearchOption().countTotal());
					if (searchResult.getTotalCount() > 0) {
						execType = ExecType.UPDATE_VALID;
						// UniqueKeyで検索している可能性があるので登録済のOIDをセット
						entity.setOid(searchResult.getFirst().getOid());
					} else {
						execType = ExecType.INSERT;
					}
				}
			}

			return execType;
		}

		private ExecType versionedExecType(EntityDefinition ed, String uniqueKey, Object uniqueKeyValue, Entity entity) {

			ExecType execType = null;

			// 有効データに登録済OIDが存在するか検索
			Query validQuery = noVersionQuery(ed.getName(), uniqueKey, uniqueKeyValue);
			SearchResult<Entity> validResult = em.searchEntity(validQuery, new SearchOption().countTotal());
			if (validResult.getTotalCount() > 0) {
				// 有効データに登録済OIDデータが存在する場合は、新しいバージョンとして追加
				execType = ExecType.UPDATE_NEW;

				// バージョン元として有効データを指定するためバージョンをクリア
				entity.setVersion(null);

				// UniqueKeyで検索している可能性があるので登録済のOIDをセット
				entity.setOid(validResult.getFirst().getOid());
			} else {
				// 有効データに登録済OIDデータが存在しない場合は、全バージョン含めて登録済OIDデータを検索
				Query versionedOidQuery = onVersionQuery(ed.getName(), uniqueKey, uniqueKeyValue, null);
				SearchResult<Entity> versionedOidResult = em.searchEntity(versionedOidQuery, new SearchOption().countTotal());
				if (versionedOidResult.getTotalCount() > 0) {
					// 全バージョンに登録済OIDデータが存在する場合は、新しいバージョンとして追加
					execType = ExecType.UPDATE_NEW;

					// バージョン元として登録済データのMAXバージョンを指定
					entity.setVersion(versionedOidResult.getFirst().getVersion());

					// UniqueKeyで検索している可能性があるので登録済のOIDをセット
					entity.setOid(versionedOidResult.getFirst().getOid());

				} else {
					// 登録済OIDデータが存在しない場合は、新規追加
					execType = ExecType.INSERT;
				}
			}

			return execType;
		}

		private Query noVersionQuery(String defName, String uniqueKey, Object uniqueKeyValue) {
			return new Query()
				.select(Entity.OID)
				.from(defName)
				.where(new Equals(uniqueKey, uniqueKeyValue));
		}

		private Query onVersionQuery(String defName, String uniqueKey, Object uniqueKeyValue, Long version) {
			Query query = new Query()
					.select(Entity.OID, Entity.VERSION)
					.from(defName)
					.order(new SortSpec(Entity.VERSION, SortType.DESC))
					.versioned(true);
			if (version != null) {
				query.where(new And(new Equals(uniqueKey, uniqueKeyValue), new Equals(Entity.VERSION, version)));
			} else {
				query.where(new Equals(uniqueKey, uniqueKeyValue));
			}
			return query;
		}

		private UpdateOption updateOption(TargetVersion targetVersion) {

			UpdateOption option = new UpdateOption(false);
			if (updatablePropperties == null) {
				if(updateProperties != null) {
					updatablePropperties = updateProperties;
				} else {
					updatablePropperties = filterPropperties();
				}
			}
			option.setUpdateProperties(new ArrayList<>(updatablePropperties));

			option.setTargetVersion(targetVersion);

			return option;
		}

		private Set<String> filterPropperties() {

			//多重度複数の場合に重複するのでsetで返す
			return properties.stream()
					.filter(s -> !s.equals(Entity.OID)
						&& !s.equals(Entity.VERSION)
						&& !s.equals(Entity.UPDATE_BY)
						&& !s.equals(Entity.UPDATE_DATE)
						&& !s.equals(Entity.LOCKED_BY)
						&& !s.equals(Entity.CREATE_BY)
						&& !s.equals(Entity.CREATE_DATE)
						&& !s.equals(EntityCsvReader.CTRL_CODE_KEY))
					.map(s -> ed.getProperty(s))
					.filter(pd -> pd != null)
					.filter(pd -> pd.isUpdatable())
					.filter(pd -> !(pd instanceof ExpressionProperty) && !(pd instanceof BinaryProperty))
					.filter(pd -> !(pd instanceof ReferenceProperty) || ((ReferenceProperty)pd).getMappedBy() == null)
					.map(PropertyDefinition :: getName)
					.collect(Collectors.toSet());
		}

	}

}
