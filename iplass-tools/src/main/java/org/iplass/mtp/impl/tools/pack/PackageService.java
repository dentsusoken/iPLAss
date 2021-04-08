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

package org.iplass.mtp.impl.tools.pack;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.impl.async.AsyncTaskService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.tenant.MetaTenant;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.entityport.EntityDataExportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportResult;
import org.iplass.mtp.impl.tools.entityport.EntityPortingService;
import org.iplass.mtp.impl.tools.metaport.MetaDataImportResult;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.tools.metaport.MetaDataTagEntity;
import org.iplass.mtp.impl.tools.metaport.MetaDataWriteCallback;
import org.iplass.mtp.impl.tools.metaport.XMLEntryInfo;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackageService implements Service {

	private static Logger logger = LoggerFactory.getLogger(PackageService.class);
	private static final Logger auditLogger = LoggerFactory.getLogger("mtp.audit.porting.pack");
	private static Logger toolLogger = LoggerFactory.getLogger("mtp.tools.packaging");

	/** パッケージファイルのタイプ */
	private static final String PACKAGE_FILE_TYPE = "application/zip";

	/** メタデータ定義ファイル名 */
	private static final String META_DATA_FILE_NAME = "metadata.xml";

	/** 対象外のEntityのパスリスト */
	private List<String> nonSupportEntityPathList;

	private MetaDataPortingService metaService;
	private EntityPortingService entityService;
	private AsyncTaskService asyncService;
	private EntityManager em;

	@Override
	public void init(Config config) {

		metaService = config.getDependentService(MetaDataPortingService.class);
		entityService = config.getDependentService(EntityPortingService.class);
		asyncService = config.getDependentService(AsyncTaskService.class);

		em = ManagerLocator.getInstance().getManager(EntityManager.class);

		nonSupportEntityPathList = Arrays.asList("/entity/mtp/maintenance/Package");
	}

	@Override
	public void destroy() {
	}

	/**
	 * <p>登録済のPackage情報を解析し、含まれるPackage情報を返します。</p>
	 *
	 * @param packOid 対象PackageエンティティのOID
	 * @return Package情報
	 */
	public PackageInfo getPackageInfo(String packOid) {

		//対象ファイルの取得
		//ant版ではFileしかだめなのでFileに変換
		File archive = null;
		try {
			Entity packEntity = loadWithCheckExist(packOid);

			archive = getPackageArchiveFile(packEntity);

			//Package情報取得
			PackageInfo info = getPackageInfo(archive);
			info.setPackageName(packEntity.getName());
			return info;
		} finally {
			if (archive != null) {
				if (!archive.delete()) {
					logger.warn("Fail to delete temporary resource:" + archive.getPath());
				}
			}
		}
	}

	/**
	 * <p>Archiveファイルを解析し、含まれるPackage情報を返します。</p>
	 *
	 * @param archive Packageアーカイブファイル
	 * @return Package情報
	 */
	public PackageInfo getPackageInfo(File archive) {

		final PackageInfo info = new PackageInfo();

		try (ZipFile zip = new ZipFile(archive)){
			zip.stream()
				.filter(entry -> !entry.isDirectory())
				.forEach(entry -> {

					try {
						//CRCチェック
						if (!checkSum(zip, entry)) {
							throw new PackageRuntimeException(rs("pack.fileCorrupted", entry.getName()));
						}

						if (META_DATA_FILE_NAME.equals(entry.getName())) {
							//MetaData
							try (InputStream is = zip.getInputStream(entry)){
								PackageMetaDataInfo metaDataInfo = getMetaDataList(is);
								info.setMetaDataPaths(metaDataInfo.entryList);
								info.setTenant(metaDataInfo.tenant);
								info.setWarningTenant(metaDataInfo.warningTenant);
							}

						} else if (entry.getName().startsWith(EntityPortingService.ENTITY_LOB_DIR)) {
							//LOBデータ
							info.setHasLobData(true);
						} else if (entry.getName().endsWith("csv")) {
							//Entityデータ
							info.addEntityPaths(entry.getName());
						} else {
							throw new PackageRuntimeException(rs("pack.canNotParse", entry.getName()));
						}
					} catch (IOException e) {
						throw new PackageRuntimeException(rs("pack.canNotParseCorrupted"), e);
					}
				});
		} catch (IOException e) {
			throw new PackageRuntimeException(rs("pack.canNotParseCorrupted"), e);
		}

		if (info.getEntityPaths() != null) {
			info.getEntityPaths().sort((String o1, String o2) -> {
				//大文字・小文字区別しない
				return o1.compareToIgnoreCase(o2);
			});
		}

		return info;
	}

	/**
	 * <p>Archiveファイルを解析し、含まれるmetadata.xmlを返します。</p>
	 *
	 * @param archive Packageアーカイブファイル
	 * @return metadata.xml
	 */
	public InputStream getMetaDataInputStream(File archive) {

		try (ZipFile zip = new ZipFile(archive)){
			Optional<? extends ZipEntry> meta = zip.stream()
				.filter(entry -> !entry.isDirectory())
				.filter(entry -> META_DATA_FILE_NAME.equals(entry.getName()))
				.findFirst();

			if (meta.isPresent()) {
				//CRCチェック
				if (!checkSum(zip, meta.get())) {
					throw new PackageRuntimeException(rs("pack.fileCorrupted", meta.get().getName()));
				}

				return zip.getInputStream(meta.get());
			}
		} catch (IOException e) {
			throw new PackageRuntimeException(rs("pack.canNotParseCorrupted"), e);
		}
		return null;
	}

	/**
	 * <p>Archiveファイルに含まれるEntryファイルに対してサムチェックを行います。</p>
	 *
	 * @param zipFile Zipファイル
	 * @param zipEntry ZipEntry
	 * @return チェック結果(true：正常)
	 * @throws IOException
	 */
	private boolean checkSum(ZipFile zipFile, ZipEntry zipEntry) throws IOException {

		long expected = zipEntry.getCrc();
		//ZipEntryのCRCが不明の場合
		if (expected == -1) {
			return true; //判定できないけど、とりあえずtrue
		}

		try (CheckedInputStream is = new CheckedInputStream(zipFile.getInputStream(zipEntry), new CRC32())){
			byte[] buf = new byte[1024];
			while ((is.read(buf)) >= 0) {
			}
			if (is.getChecksum().getValue() == expected) {
				return true;
			}
			return false;
		}
	}

	/**
	 * <p>metadata.xmlを解析し、含まれるMetaData情報を返します。</p>
	 *
	 * @param is metadata.xml
	 * @return MetaData情報
	 */
	private PackageMetaDataInfo getMetaDataList(InputStream is) {

		XMLEntryInfo entryInfo = metaService.getXMLMetaDataEntryInfo(is);
		List<String> entryPathList = new ArrayList<>(entryInfo.getPathEntryMap().keySet());

		entryPathList.sort((String o1, String o2) -> {
			//大文字・小文字区別しない
			return o1.compareToIgnoreCase(o2);
		});

		//テナントのチェック
		Tenant tenant = null;
		boolean warningTenant = false;
		for (Map.Entry<String, MetaDataEntry> entry : entryInfo.getPathEntryMap().entrySet()) {
			if (metaService.isTenantMeta(entry.getKey())) {
				MetaTenant importMetaTenant = (MetaTenant) entry.getValue().getMetaData();
				tenant = new Tenant();
				importMetaTenant.applyToTenant(tenant);

				tenant.setId(-1); //IDはセットされない（不明なので未セット）
				tenant.setName(importMetaTenant.getName()); //nameはapplyToTenantでセットされないのでセット(DB側優先)
				tenant.setDescription(importMetaTenant.getDescription()); //descriptionはapplyToTenantでセットされないのでセット(DB側優先)

				Tenant currentTenant = ExecuteContext.getCurrentContext().getCurrentTenant();
				if (!currentTenant.getName().equals(importMetaTenant.getName())) {
					//名前が違う場合はWarning対象
					warningTenant = true;
				}
			}
		}

		PackageMetaDataInfo result = new PackageMetaDataInfo();
		result.entryList = entryPathList;
		result.tenant = tenant;
		result.warningTenant = warningTenant;

		return result;
	}

	/**
	 * <p>MetaData情報解析結果保持用内部クラス</p>
	 */
	private static class PackageMetaDataInfo {
		/** メタデータのPath */
		private List<String> entryList;
		/** Tenantメタデータ(特殊扱いのため) */
		private Tenant tenant;
		/** Tenantメタデータが警告対象か(名前不一致) */
		private boolean warningTenant;
	}

	/**
	 * <p>Packageファイルをアップロードします。</p>
	 * <p>アップロードされたArchiveファイルはPackageエンティティに登録します。</p>
	 *
	 * @param name 名前(またはファイル名)
	 * @param description 説明
	 * @param archive ファイル
	 * @return 登録OID
	 */
	public String uploadPackage(String name, String description, File archive) {
		return uploadPackage(name, description, archive, PackageEntity.TYPE_UPLOAD);
	}

	/**
	 * <p>Packageファイルをアップロードします。</p>
	 *
	 * <p>アップロードされたArchiveファイルはPackageエンティティに登録します。</p>
	 *
	 * @param name 名前(またはファイル名)
	 * @param description 説明
	 * @param archive ファイル
	 * @param type Packageのタイプ
	 * @return 登録OID
	 */
	public String uploadPackage(String name, String description, File archive, String type) {

		Entity entity = new GenericEntity(PackageEntity.ENTITY_DEFINITION_NAME);
		entity.setName(name);
		entity.setDescription(description);
		entity.setValue(PackageEntity.TYPE, new SelectValue(type));
		entity.setValue(PackageEntity.STATUS, new SelectValue(PackageEntity.STATUS_COMPLETED));

		entity.setValue(PackageEntity.TASK_COUNT, 1);
		entity.setValue(PackageEntity.COMPLETE_TASK_COUNT, 1);

		//実行開始・終了日設定
		Timestamp timestamp = ExecuteContext.getCurrentContext().getCurrentTimestamp();
		entity.setValue(PackageEntity.EXEC_START_DATE, timestamp);
		entity.setValue(PackageEntity.EXEC_END_DATE, timestamp);

		//Archive設定
		BinaryReference br = toBinaryReference(name, archive);
		entity.setValue(PackageEntity.ARCHIVE, br);

		//Entityに登録
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		String oid = em.insert(entity);

		return oid;
	}

	/**
	 * <p>ファイルをEntityのBinaryReferenceに変換します。</p>
	 *
	 * @param name バイナリ名
	 * @param file ファイル
	 * @return BinaryReference
	 */
	private BinaryReference toBinaryReference(String name, File file) {
		// マジックバイトチェックは特にしない

		try (FileInputStream is = new FileInputStream(file)){
			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			return em.createBinaryReference(name, PACKAGE_FILE_TYPE, is);
		} catch (IOException e) {
			throw new SystemException("not found import file.", e);
		}
	}

	/**
	 * <p>Package情報を登録します。</p>
	 *
	 * <p>Package作成条件をPackageエンティティに登録します。
	 * Packageファイルの作成は行いません。
	 * Packageの作成は、この登録データに対して、
	 * {@link #archivePackage(String, File)} または {@link #archivePackageAsync(String, File)}
	 * を実行してください。
	 * </p>
	 *
	 * @param condition Package作成条件
	 * @return 登録OID
	 */
	public String storePackage(PackageCreateCondition condition) {
		return storePackage(condition, PackageEntity.TYPE_LOCAL);
	}

	/**
	 * <p>Package情報を登録します。</p>
	 *
	 * <p>Package作成条件をPackageエンティティに登録します。
	 * Packageファイルの作成は行いません。
	 * Packageの作成は、この登録データに対して、
	 * {@link #archivePackage(String, File)} または {@link #archivePackageAsync(String, File)}
	 * を実行してください。
	 * </p>
	 *
	 * @param condition Package作成条件
	 * @param type Packageタイプ
	 * @return 登録OID
	 */
	public String storePackage(PackageCreateCondition condition, String type) {
		Entity entity = new GenericEntity(PackageEntity.ENTITY_DEFINITION_NAME);
		entity.setName(condition.getName());
		entity.setDescription(condition.getDescription());
		entity.setValue(PackageEntity.TYPE, new SelectValue(type));
		entity.setValue(PackageEntity.STATUS, new SelectValue(PackageEntity.STATUS_READY));

		//作成情報にSetting情報をセット
		entity.setValue(PackageEntity.CREATE_SETTING, toBinaryReference(condition));

		//タスク数の計算
		int taskCount = 0;
		if (condition.getMetaDataPaths() != null && !condition.getMetaDataPaths().isEmpty()) {
			taskCount++; //メタデータ生成は１タスク
		}
		if (condition.getEntityPaths() != null && !condition.getEntityPaths().isEmpty()) {
			taskCount += condition.getEntityPaths().size(); //Entityデータ生成はEntityごと
		}
		//Archiveするためタスクを１追加
		taskCount++;
		entity.setValue(PackageEntity.TASK_COUNT, taskCount);
		entity.setValue(PackageEntity.COMPLETE_TASK_COUNT, 0);

		//Entityに登録
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		String oid = em.insert(entity);

		return oid;
	}

	/**
	 * <p>Packageを作成します（同期）。</p>
	 *
	 * <p>同期処理のため、Packageの作成が完了するまで処理は完了しません。
	 * 大量データを対象にする場合は、
	 * {@linkplain #archivePackageAsync(String, File)}
	 * を利用して、非同期で実行することを検討してください。
	 * </p>
	 *
	 * <p>Package対象は、
	 * {@link #storePackage(PackageCreateCondition)} または {@link #storePackage(PackageCreateCondition, String)}
	 * により登録された条件です。</p>
	 *
	 * @param packOid PackageエンティティOID
	 * @return Package作成結果
	 */
	public PackageCreateResult archivePackage(final String packOid) {

		//対象の取得
		final Entity entity = em.load(packOid, PackageEntity.ENTITY_DEFINITION_NAME);
		if (entity == null) {
			throw new PackageRuntimeException("not found package setting. id=" + packOid);
		}
		final BinaryReference settingRef = entity.getValue(PackageEntity.CREATE_SETTING);
		final PackageCreateCondition condition = toCreatePackageCondition(settingRef);
		if (condition == null) {
			throw new PackageRuntimeException("failed to read package setting. id=" + packOid);
		}

		return doArchive(entity, condition);
	}

	/**
	 * <p>Packageを作成します。（非同期）</p>
	 *
	 * <p>非同期処理のため、Packageの作成が完了する前に処理を戻します。
	 * このためPackageの作成結果は返りません。
	 * 作成結果は
	 * {@linkplain #getPackageList()} または {@linkplain #load(String)}
	 * を利用して確認してください。
	 * </p>
	 *
	 * <p>Package対象は、
	 * {@link #storePackage(PackageCreateCondition)} または {@link #storePackage(PackageCreateCondition, String)}
	 * により登録された条件です。</p>
	 *
	 * @param packOid PackageエンティティOID
	 */
	public void archivePackageAsync(final String packOid) {
		//対象の取得
		final Entity entity = em.load(packOid, PackageEntity.ENTITY_DEFINITION_NAME);
		if (entity == null) {
			throw new PackageRuntimeException("not found package setting. id=" + packOid);
		}
		final BinaryReference settingRef = entity.getValue(PackageEntity.CREATE_SETTING);
		final PackageCreateCondition condition = toCreatePackageCondition(settingRef);
		if (condition == null) {
			throw new PackageRuntimeException("failed to read package setting. id=" + packOid);
		}

		//非同期実行
		asyncService.execute(() -> {

			//非同期なのでTransactionを開始
			return Transaction.required(transaction -> {
				return doArchive(entity, condition);
			});
		});
	}

	/**
	 * <p>サポート外のEntityのパスを返します。</p>
	 *
	 * @return サポート外のEntityのパス
	 */
	public List<String> getNonSupportEntityPathList() {
		return nonSupportEntityPathList;
	}

	/**
	 * <p>Packageを書きだします。</p>
	 *
	 * @param os 出力先
	 * @param condition 条件
	 */
	public void write(OutputStream os, final PackageCreateCondition condition) {
		write(os, condition, new PackageCreateResult(), null);
	}

	/**
	 * <p>Packageを書きだします。</p>
	 *
	 * @param os 出力先
	 * @param condition 条件
	 * @param result 実行結果格納用
	 * @param packEntity Package管理用Entity(不要の場合はnull)
	 */
	public void write(OutputStream os, final PackageCreateCondition condition, final PackageCreateResult result, final Entity packEntity) {

		try (
			ZipOutputStream zos = new ZipOutputStream(os, StandardCharsets.UTF_8);
		) {

			//Package更新(開始)
			if (packEntity != null) {
				Transaction.requiresNew(transaction -> {

					packEntity.setValue(PackageEntity.STATUS, new SelectValue(PackageEntity.STATUS_ACTIVE));
					packEntity.setValue(PackageEntity.EXEC_START_DATE, ExecuteContext.getCurrentContext().getCurrentTimestamp());
					UpdateOption option = new UpdateOption(false);
					option.setUpdateProperties(PackageEntity.STATUS, PackageEntity.EXEC_START_DATE);
					em.update(packEntity, option);
					return null;

				});
			}

			if (condition.getMetaDataPaths() != null && !condition.getMetaDataPaths().isEmpty()) {
				logger.debug("pack metadata path count : " + condition.getMetaDataPaths().size());

				result.addMessages(rs("pack.startExportMetaData"));

				//MetaDataをtempに出力
				File metadataFile = File.createTempFile("tmp", ".tmp");
				try {
					try (PrintWriter writer = new PrintWriter(metadataFile, "UTF-8")){

						metaService.write(writer, condition.getMetaDataPaths(), new MetaDataWriteCallback() {

							@Override
							public void onWrited(String path, String version) {
								result.addMessages(rs("pack.outputMetaData", path));
								auditLogger.info("create package metadata," + META_DATA_FILE_NAME + ",path:" + path + " packageOid:" + (packEntity != null ? packEntity.getOid() : "direct"));
							}

							@Override
							public boolean onWarning(String path, String message, String version) {
								result.addMessages(rs("pack.warningOutputMetaData", path));
								result.addMessages(message);
								return true;
							}

							@Override
							public void onStarted() {
							}

							@Override
							public void onFinished() {
							}

							@Override
							public boolean onErrored(String path, String message, String version) {
								result.addMessages(rs("pack.errorOutputMetaData", path));
								result.addMessages(message);
								return false;
							}
						});
					}

					//zipに追加
					addZipEntry(zos, metadataFile, META_DATA_FILE_NAME);

					//tempファイルを削除
					if (!metadataFile.delete()) {
						logger.warn("Fail to delete temporary resource:" + metadataFile.getPath());
					}
					metadataFile = null;

					//Package更新(完了タスク数)
					if (packEntity != null) {
						Transaction.requiresNew(transaction -> {

							//IntegerだとClassCastExceptionが発生するためLongで取得
							//int curCompleteCount = entity.getValueAs(Integer.class, PackageEntry.COMPLETE_TASK_COUNT);
							long curCompleteCount = packEntity.getValueAs(Long.class, PackageEntity.COMPLETE_TASK_COUNT);
							packEntity.setValue(PackageEntity.COMPLETE_TASK_COUNT, curCompleteCount + 1);
							UpdateOption option = new UpdateOption(false);
							option.setUpdateProperties(PackageEntity.COMPLETE_TASK_COUNT);
							em.update(packEntity, option);
							return null;

						});
					}

					result.addMessages(rs("pack.completedExportMetaData"));

				} finally {
					//エラーが発生して削除できていない可能性があるのでチェック
					if (metadataFile != null) {
						//tempファイルを削除
						if (!metadataFile.delete()) {
							logger.warn("Fail to delete temporary resource:" + metadataFile.getPath());
						}
						metadataFile = null;
					}
				}

			} else {
				result.addMessages(rs("pack.nonTargetMetaData"));
				logger.debug("pack metadata path count : 0");
			}

			if (condition.getEntityPaths() != null && !condition.getEntityPaths().isEmpty()) {
				logger.debug("pack entity path count : " + condition.getEntityPaths().size());

				Map<String, EntityDataExportCondition> entityConditions = condition.getEntityConditions() != null ? condition.getEntityConditions() : Collections.emptyMap();

				result.addMessages(rs("pack.startExportEntity"));

				//Entityデータをtempに出力
				for (String path : condition.getEntityPaths()) {
					if (nonSupportEntityPathList.contains(path)) {
						result.addMessages(rs("pack.skipNonSupportEntity", path));
						logger.warn("warning entity data write proccess. path = " + path + ". message = not support entity.");
						continue;
					}

					//MetaDataEntryの取得
					MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(path);
					if (entry == null) {
						result.addMessages(rs("pack.skipExportEntity", path));
						logger.warn("warning entity data write proccess. path = " + path + ". message = not found metadata configure.");
						continue;
					}

					String fileName = entry.getMetaData().getName() + ".csv";
					auditLogger.info("create package entity," + fileName + ",entityName:" + entry.getMetaData().getName() + " packageOid:" + (packEntity != null ? packEntity.getOid() : "direct"));

					EntityDataExportCondition entityCond = entityConditions.getOrDefault(entry.getMetaData().getName(), new EntityDataExportCondition());

					long count = 0;
					File entityCsvFile = File.createTempFile("tmp", ".tmp");
					try {
						try (OutputStream csvOS = new FileOutputStream(entityCsvFile)){
							count = entityService.writeWithBinary(csvOS, entry, entityCond, zos);
						}

						//zipに追加
						addZipEntry(zos, entityCsvFile, fileName);

						//tempファイルを削除
						if (!entityCsvFile.delete()) {
							logger.warn("Fail to delete temporary resource:" + entityCsvFile.getPath());
						}
						entityCsvFile = null;

						//Package更新(完了タスク数)
						if (packEntity != null) {
							Transaction.requiresNew(transaction -> {

								//IntegerだとClassCastExceptionが発生するためLongで取得
								//int curCompleteCount = entity.getValueAs(Integer.class, PackageEntry.COMPLETE_TASK_COUNT);
								long curCompleteCount = packEntity.getValueAs(Long.class, PackageEntity.COMPLETE_TASK_COUNT);
								packEntity.setValue(PackageEntity.COMPLETE_TASK_COUNT, curCompleteCount + 1);
								UpdateOption option = new UpdateOption(false);
								option.setUpdateProperties(PackageEntity.COMPLETE_TASK_COUNT);
								em.update(packEntity, option);
								return null;

							});
						}

						result.addMessages(rs("pack.outputEntity", entry.getMetaData().getName(), count));

					} finally {
						//エラーが発生して削除できていない可能性があるのでチェック
						if (entityCsvFile != null) {
							//tempファイルを削除
							if (!entityCsvFile.delete()) {
								logger.warn("Fail to delete temporary resource:" + entityCsvFile.getPath());
							}
							entityCsvFile = null;
						}
					}
				}

				result.addMessages(rs("pack.completedExportEntity"));

			} else {
				result.addMessages(rs("pack.nonTargetEntity"));
				logger.debug("pack entity path count : 0");
			}

		} catch (Exception e) {
			throw new PackageRuntimeException(e.getMessage() == null ? e.getClass().getName() : e.getMessage(), e);
		}
	}

	/**
	 * <p>Package作成条件をEntityのBinaryReferenceに変換します。</p>
	 *
	 * @param condition Package作成条件
	 * @return BinaryReference
	 */
	private BinaryReference toBinaryReference(PackageCreateCondition condition) {

		try (
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
		){
			out.writeObject(condition);

			try (ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray())) {
				return em.createBinaryReference(condition.getName(), PackageCreateCondition.class.getSimpleName(), byteIn);
			}
		} catch (IOException e) {
			//多分発生し得ない
			throw new RuntimeException(e);
		}
	}

	/**
	 * <p>BinaryReferenceからPackageCreateConditionに変換します。</p>
	 *
	 * @param binary BinaryReference
	 * @return Package作成条件
	 */
	private PackageCreateCondition toCreatePackageCondition(BinaryReference binary) {
		if (binary != null) {
			try (
				InputStream dis = em.getInputStream(binary);
				ObjectInputStream ois = new ObjectInputStream(dis);
			){
				return (PackageCreateCondition) ois.readObject();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * <p>Packageを作成して、Packageエンティティに登録します。</p>
	 *
	 * @param packEntity Packageエンティティ
	 * @param condition Package作成条件
	 * @return Package作成結果
	 */
	private PackageCreateResult doArchive(final Entity packEntity, final PackageCreateCondition condition) {

		final PackageCreateResult result = new PackageCreateResult();

		try {
			logger.info("start packaging " + condition.getName());

			//zip用BinaryReferenceを生成
			final String zipName = condition.getName() + ".zip";
			BinaryReference zipBin = createZipBinaryReference(zipName);

			try (
				OutputStream binos = getZipBinaryOutputStream(zipBin);
//				ZipOutputStream zos = new ZipOutputStream(binos, StandardCharsets.UTF_8);
			) {

				write(binos, condition, result, packEntity);

//				//Package更新(開始)
//				Transaction.requiresNew(transaction -> {
//
//					packEntity.setValue(PackageEntity.STATUS, new SelectValue(PackageEntity.STATUS_ACTIVE));
//					packEntity.setValue(PackageEntity.EXEC_START_DATE, ExecuteContext.getCurrentContext().getCurrentTimestamp());
//					UpdateOption option = new UpdateOption(false);
//					option.setUpdateProperties(PackageEntity.STATUS, PackageEntity.EXEC_START_DATE);
//					em.update(packEntity, option);
//					return null;
//
//				});
//
//				if (condition.getMetaDataPaths() != null && !condition.getMetaDataPaths().isEmpty()) {
//					logger.debug("pack metadata path count : " + condition.getMetaDataPaths().size());
//
//					result.addMessages(rs("pack.startExportMetaData"));
//
//					//MetaDataをtempに出力
//					File metadataFile = File.createTempFile("tmp", ".tmp");
//					try {
//						try (PrintWriter writer = new PrintWriter(metadataFile, "UTF-8")){
//
//							metaService.write(writer, condition.getMetaDataPaths(), new MetaDataWriteCallback() {
//
//								@Override
//								public void onWrited(String path, String version) {
//									result.addMessages(rs("pack.outputMetaData", path));
//									auditLogger.info("create package metadata," + META_DATA_FILE_NAME + ",path:" + path + " packageOid:" + packEntity.getOid());
//								}
//
//								@Override
//								public boolean onWarning(String path, String message, String version) {
//									result.addMessages(rs("pack.warningOutputMetaData", path));
//									result.addMessages(message);
//									return true;
//								}
//
//								@Override
//								public void onStarted() {
//								}
//
//								@Override
//								public void onFinished() {
//								}
//
//								@Override
//								public boolean onErrored(String path, String message, String version) {
//									result.addMessages(rs("pack.errorOutputMetaData", path));
//									result.addMessages(message);
//									return false;
//								}
//							});
//						}
//
//						//zipに追加
//						addZipEntry(zos, metadataFile, META_DATA_FILE_NAME);
//
//						//tempファイルを削除
//						if (!metadataFile.delete()) {
//							logger.warn("Fail to delete temporary resource:" + metadataFile.getPath());
//						}
//						metadataFile = null;
//
//						//Package更新(完了タスク数)
//						Transaction.requiresNew(transaction -> {
//
//							//IntegerだとClassCastExceptionが発生するためLongで取得
//							//int curCompleteCount = entity.getValueAs(Integer.class, PackageEntry.COMPLETE_TASK_COUNT);
//							long curCompleteCount = packEntity.getValueAs(Long.class, PackageEntity.COMPLETE_TASK_COUNT);
//							packEntity.setValue(PackageEntity.COMPLETE_TASK_COUNT, curCompleteCount + 1);
//							UpdateOption option = new UpdateOption(false);
//							option.setUpdateProperties(PackageEntity.COMPLETE_TASK_COUNT);
//							em.update(packEntity, option);
//							return null;
//
//						});
//
//						result.addMessages(rs("pack.completedExportMetaData"));
//
//					} finally {
//						//エラーが発生して削除できていない可能性があるのでチェック
//						if (metadataFile != null) {
//							//tempファイルを削除
//							if (!metadataFile.delete()) {
//								logger.warn("Fail to delete temporary resource:" + metadataFile.getPath());
//							}
//							metadataFile = null;
//						}
//					}
//
//				} else {
//					result.addMessages(rs("pack.nonTargetMetaData"));
//					logger.debug("pack metadata path count : 0");
//				}
//
//				if (condition.getEntityPaths() != null && !condition.getEntityPaths().isEmpty()) {
//					logger.debug("pack entity path count : " + condition.getEntityPaths().size());
//
//					Map<String, EntityDataExportCondition> entityConditions = condition.getEntityConditions() != null ? condition.getEntityConditions() : Collections.emptyMap();
//
//					result.addMessages(rs("pack.startExportEntity"));
//
//					//Entityデータをtempに出力
//					for (String path : condition.getEntityPaths()) {
//						if (nonSupportEntityPathList.contains(path)) {
//							result.addMessages(rs("pack.skipNonSupportEntity", path));
//							logger.warn("warning entity data write proccess. path = " + path + ". message = not support entity.");
//							continue;
//						}
//
//						//MetaDataEntryの取得
//						MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(path);
//						if (entry == null) {
//							result.addMessages(rs("pack.skipExportEntity", path));
//							logger.warn("warning entity data write proccess. path = " + path + ". message = not found metadata configure.");
//							continue;
//						}
//
//						String fileName = entry.getMetaData().getName() + ".csv";
//						auditLogger.info("create package entity," + fileName + ",entityName:" + entry.getMetaData().getName() + " packageOid:" + packEntity.getOid());
//
//						EntityDataExportCondition entityCond = entityConditions.getOrDefault(entry.getMetaData().getName(), new EntityDataExportCondition());
//
//						long count = 0;
//						File entityCsvFile = File.createTempFile("tmp", ".tmp");
//						try {
//							try (OutputStream os = new FileOutputStream(entityCsvFile)){
//								count = entityService.writeWithBinary(os, entry, entityCond, zos);
//							}
//
//							//zipに追加
//							addZipEntry(zos, entityCsvFile, fileName);
//
//							//tempファイルを削除
//							if (!entityCsvFile.delete()) {
//								logger.warn("Fail to delete temporary resource:" + entityCsvFile.getPath());
//							}
//							entityCsvFile = null;
//
//							//Package更新(完了タスク数)
//							Transaction.requiresNew(transaction -> {
//
//								//IntegerだとClassCastExceptionが発生するためLongで取得
//								//int curCompleteCount = entity.getValueAs(Integer.class, PackageEntry.COMPLETE_TASK_COUNT);
//								long curCompleteCount = packEntity.getValueAs(Long.class, PackageEntity.COMPLETE_TASK_COUNT);
//								packEntity.setValue(PackageEntity.COMPLETE_TASK_COUNT, curCompleteCount + 1);
//								UpdateOption option = new UpdateOption(false);
//								option.setUpdateProperties(PackageEntity.COMPLETE_TASK_COUNT);
//								em.update(packEntity, option);
//								return null;
//
//							});
//
//							result.addMessages(rs("pack.outputEntity", entry.getMetaData().getName(), count));
//
//						} finally {
//							//エラーが発生して削除できていない可能性があるのでチェック
//							if (entityCsvFile != null) {
//								//tempファイルを削除
//								if (!entityCsvFile.delete()) {
//									logger.warn("Fail to delete temporary resource:" + entityCsvFile.getPath());
//								}
//								entityCsvFile = null;
//							}
//						}
//					}
//
//					result.addMessages(rs("pack.completedExportEntity"));
//
//				} else {
//					result.addMessages(rs("pack.nonTargetEntity"));
//					logger.debug("pack entity path count : 0");
//				}
			}

			//zip用BinaryReferenceをPackageに登録
			packEntity.setValue(PackageEntity.ARCHIVE, zipBin);
			//IntegerだとClassCastExceptionが発生するためLongで取得
			//int curCompleteCount = entity.getValueAs(Integer.class, PackageEntry.COMPLETE_TASK_COUNT);
			long curCompleteCount = packEntity.getValueAs(Long.class, PackageEntity.COMPLETE_TASK_COUNT);
			packEntity.setValue(PackageEntity.COMPLETE_TASK_COUNT, curCompleteCount + 1);
			packEntity.setValue(PackageEntity.STATUS, new SelectValue(PackageEntity.STATUS_COMPLETED));
			packEntity.setValue(PackageEntity.EXEC_END_DATE, ExecuteContext.getCurrentContext().getCurrentTimestamp());
			UpdateOption option = new UpdateOption(false);
			option.setUpdateProperties(PackageEntity.ARCHIVE, PackageEntity.COMPLETE_TASK_COUNT, PackageEntity.STATUS, PackageEntity.EXEC_END_DATE);
			em.update(packEntity, option);

			result.addMessages(rs("pack.completedCreatePackage", zipName));

			logger.info("complete packaging " + condition.getName());

		} catch (Exception e) {
			//エラー処理
			logger.error("error packaging " + condition.getName(), e);

			//Package更新(エラー)
			Transaction.requiresNew(transaction -> {

				packEntity.setValue(PackageEntity.STATUS, new SelectValue(PackageEntity.STATUS_ERROR));
				packEntity.setValue(PackageEntity.EXEC_END_DATE, ExecuteContext.getCurrentContext().getCurrentTimestamp());
				UpdateOption option = new UpdateOption(false);
				option.setUpdateProperties(PackageEntity.STATUS, PackageEntity.EXEC_END_DATE);
				em.update(packEntity, option);

				return null;

			});

			result.setError(true);
			result.addMessages(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
		}

		return result;
	}

	/**
	 * <p>zip用BinaryReferenceを生成します。</p>
	 * <p>InputStreamは指定していないので、
	 * {@linkplain #getZipBinaryOutputStream(BinaryReference)}
	 * を利用して、OutputStreamを取得して書き込む必要があります。
	 * </p>
	 *
	 * @param name BinaryReferenceの名前
	 * @return BinaryReference
	 */
	private BinaryReference createZipBinaryReference(String name) {
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		return em.createBinaryReference(name, PACKAGE_FILE_TYPE, null);
	}

	/**
	 * <p>BinaryReferenceからOutputStreamを取得します。</p>
	 *
	 * @param bin BinaryReference
	 * @return OutputStream
	 */
	private OutputStream getZipBinaryOutputStream(BinaryReference bin) {
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		return em.getOutputStream(bin);
	}

	/**
	 * <p>PackageアーカイブにEntryを追加します。</p>
	 *
	 * @param zos zip用OutputStream
	 * @param file 追加対象のファイル
	 * @param entryName 追加名(Entry名)
	 * @throws IOException
	 */
	private void addZipEntry(ZipOutputStream zos, File file, String entryName) throws IOException {
		ZipEntry zentry = new ZipEntry(entryName);
		zos.putNextEntry(zentry);
		try (InputStream bis = new BufferedInputStream(new FileInputStream(file))){
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = bis.read(buf)) >= 0) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
		}
	}

	/**
	 * <p>Package情報を全件検索します。</p>
	 *
	 * @return 登録Package
	 */
	public SearchResult<Entity> getPackageList() {
		Query q = new Query().selectAll(PackageEntity.ENTITY_DEFINITION_NAME, false, true)
				.from(PackageEntity.ENTITY_DEFINITION_NAME)
				.order(new SortSpec(Entity.CREATE_DATE, SortType.DESC),
						new SortSpec(Entity.OID, SortType.DESC));
		return em.searchEntity(q);
	}

	/**
	 * <p>Package情報をロードします。</p>
	 *
	 * @param packOid PackageエンティティOID
	 * @return Packageエンティティ
	 */
	private Entity load(String packOid) {
		return em.load(packOid, PackageEntity.ENTITY_DEFINITION_NAME);
	}

	/**
	 * <p>Package情報を削除します。</p>
	 *
	 * @param packOids PackageエンティティのOIDリスト
	 */
	public void deletePackage(List<String> packOids) {
		for (String oid : packOids) {
			Entity entity = load(oid);
			if (entity != null) {
				//物理削除(purge=true)
				DeleteOption option = new DeleteOption(false);
				option.setPurge(true);
				em.delete(entity, option);
			}
		}
	}

	/**
	 * <p>Packageのメタデータをインポートします。</p>
	 *
	 * @param packOid PackageエンティティOID
	 * @param importTenant インポート対象Tenant
	 */
	public MetaDataImportResult importPackageMetaData(final String packOid, final Tenant importTenant) {

		Entity packEntity = null;
		try {
			//対象ファイルの取得
			packEntity = loadWithCheckExist(packOid);
		} catch (PackageRuntimeException e) {
			toolLogger.info("start package metadata import. {target:{}}", packOid);	//Entityが取得できないので、OID
			toolLogger.info("finish package metadata import. {target:{}, result:{}}", packOid, "failed");
			throw e;
		}

		toolLogger.info("start package metadata import. {target:{}}", packEntity.getName());
		auditLogger.info("import package metadata," + packEntity.getName() + ",packageOid:" + packOid);

		File archive = null;
		MetaDataImportResult result = null;
		try {
			archive = getPackageArchiveFile(packEntity);

			//メタデータ定義ファイルの取得
			try (ZipFile zf = new ZipFile(archive)) {
				ZipEntry ze = zf.getEntry(META_DATA_FILE_NAME);

				try (InputStream is = zf.getInputStream(ze)) {
					//ファイルに含まれるMetaData情報を取得
					XMLEntryInfo entryInfo = metaService.getXMLMetaDataEntryInfo(is);

					//インポート処理の実行
					result = metaService.importMetaData(packEntity.getName(), entryInfo, importTenant);

					return result;
				}
			}

		} catch (IOException e) {
			throw new PackageRuntimeException("failed to read metadata configure. id=" + packOid, e);
		} finally {
			toolLogger.info("finish package metadata import. {target:{}, result:{}}", packEntity.getName(),
					(result == null ? "failed" : result.isError() ? "failed" : "success"));
			if (archive != null) {
				if (!archive.delete()) {
					logger.warn("Fail to delete temporary resource:" + archive.getPath());
				}
			}
		}
	}

	/**
	 * <p>PackageのEntityデータをインポートします。</p>
	 *
	 * <p>Entityデータは1Entityずつインポートします。</p>
	 *
	 * @param packOid PackageエンティティOID
	 * @param path Entityパス
	 * @param condition インポート条件
	 * @return インポート結果
	 */
	public EntityDataImportResult importPackageEntityData(final String packOid, final String path,
			final EntityDataImportCondition condition) {

		Entity packEntity = null;
		try {
			//対象ファイルの取得
			packEntity = loadWithCheckExist(packOid);
		} catch (PackageRuntimeException e) {
			toolLogger.info("start package entity import. {target:{}, entity:{}}", packOid, path);	//Entityが取得できないので、OID
			toolLogger.info("finish package entity import. {target:{}, entity:{}, result:{}}", packOid, path, "failed");
			throw e;
		}

		toolLogger.info("start package entity import. {target:{}, entity:{}}", packEntity.getName(), path);
		auditLogger.info("import package entity," + packEntity.getName() + ",packageOid:" + packOid + " path:" + path);

		EntityDataImportResult result = null;
		try {
			//Entity定義の存在チェック
			MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(path);
			if (entry == null) {
				result = new EntityDataImportResult();
				result.setError(true);
				result.addMessages(rs("pack.notFoundEntityDef", path));
				return result;
			}

			//PackageEntityのチェック（PackageEntityは取り込ませない）
			if (PackageEntity.ENTITY_DEFINITION_NAME.equals(entry.getMetaData().getName())) {
				result = new EntityDataImportResult();
				result.addMessages(rs("pack.cantImportEntity", PackageEntity.ENTITY_DEFINITION_NAME));
				return result;
			}

			//MetaDataTagEntityのチェック（MetaDataTagEntityは取り込ませない）
			if (MetaDataTagEntity.ENTITY_DEFINITION_NAME.equals(entry.getMetaData().getName())) {
				result = new EntityDataImportResult();
				result.addMessages(rs("pack.cantImportEntity", MetaDataTagEntity.ENTITY_DEFINITION_NAME));
				return result;
			}

			//Entityのチェック（Entityは取り込ませない）
			if (EntityService.ENTITY_NAME.equals(entry.getMetaData().getName())) {
				result = new EntityDataImportResult();
				result.addMessages(rs("pack.cantImportEntity", EntityService.ENTITY_NAME));
				return result;
			}

			File archive = null;
			try {
				archive = getPackageArchiveFile(packEntity);

				//CSVファイルの取得
				try (ZipFile zf = new ZipFile(archive)) {
					ZipEntry ze = zf.getEntry(entry.getMetaData().getName() + ".csv");
					try (InputStream is = zf.getInputStream(ze)) {
						//インポート処理の実行
						result = entityService.importEntityData(packEntity.getName(), is, entry, condition, zf);

						return result;
					}
				}

			} catch (IOException e) {
				throw new PackageRuntimeException("failed to read entity data csv file. id=" + packOid, e);
			} finally {
				if (archive != null) {
					if (!archive.delete()) {
						logger.warn("Fail to delete temporary resource:" + archive.getPath());
					}
				}
			}
		} finally {
			toolLogger.info("finish package entity import. {target:{}, entity:{}, result:{}}", packEntity.getName(), path,
					(result == null ? "failed" : result.isError() ? "failed" : "success"));
		}
	}

	/**
	 * <p>Package情報をロードします。</p>
	 *
	 * @param packOid PackageエンティティOID
	 * @return Packageエンティティ
	 */
	private Entity loadWithCheckExist(String packOid) {
		//対象の取得
		final Entity entity = load(packOid);
		if (entity == null) {
			throw new PackageRuntimeException("not found package . id=" + packOid);
		}
		return entity;
	}

	/**
	 * <p>PackageエンティティからPackageアーカイブファイルを取得します。</p>
	 *
	 * <p>結果として戻されたFileは呼び出し元で削除する必要があります。</p>
	 *
	 * @param packEntity Packageエンティティ
	 * @return アーカイブファイル
	 */
	private File getPackageArchiveFile(Entity packEntity) {
		//対象の取得
		final BinaryReference archiveRef = packEntity.getValue(PackageEntity.ARCHIVE);
		if (archiveRef == null) {
			throw new PackageRuntimeException("not found package archive info. id=" + packEntity.getOid());
		}

		try {
			File archive = File.createTempFile("tmp", ".tmp");
			try (
				FileOutputStream fos = new FileOutputStream(archive);
				InputStream is = em.getInputStream(archiveRef);
			) {

				IOUtils.copy(is, fos);
			}
			return archive;
		} catch (IOException e) {
			throw new PackageRuntimeException("Fail to prepare to read package archive info. id=" + packEntity.getOid(), e);
		}
	}

	private String rs(String key, Object... arguments) {
		return ToolsResourceBundleUtil.resourceString(key, arguments);
	}

}
