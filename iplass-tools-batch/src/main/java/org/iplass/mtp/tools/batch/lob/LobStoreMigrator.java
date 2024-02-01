/*
 * Copyright 2016 DENTSU SOKEN INC. All Rights Reserved.
 */
package org.iplass.mtp.tools.batch.lob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.core.config.ConfigImpl;
import org.iplass.mtp.impl.lob.Lob;
import org.iplass.mtp.impl.lob.LobDao;
import org.iplass.mtp.impl.lob.LobStoreService;
import org.iplass.mtp.impl.lob.lobstore.LobData;
import org.iplass.mtp.impl.lob.lobstore.LobStore;
import org.iplass.mtp.impl.lob.lobstore.file.FileLobData;
import org.iplass.mtp.impl.lob.lobstore.file.FileLobStore;
import org.iplass.mtp.impl.lob.lobstore.rdb.RdbLobStore;
import org.iplass.mtp.impl.lob.sql.BlobSearchSql;
import org.iplass.mtp.impl.properties.extend.LongTextType;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpSilentBatch;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LobStoreMigrator extends MtpSilentBatch {

	private static Logger logger = LoggerFactory.getLogger(LobStoreMigrator.class);

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);

	private int tenantId;

	private String rootDir;

	private MigrateMode migrateMode;

	private MigrateTarget migrateTarget;

	/**
	 * <p>引数について</p>
	 * <ol>
	 * <li>移行モード：'F2R'（FileからRDBに移行）、'R2F'（RDBからFileに移行）</li>
	 * <li>移行対象：'ALL'（BinaryとLongTextの両方）、'BINARY'（Binaryのみ）、'LONGTEXT'（LongTextのみ）</li>
	 * <li>ルートディレクトリ：FileLobStoreのRootDir</li>
	 * <li>テナントID：対象テナントID（-1の場合、全テナントが対象になります）</li>
	 * </ol>
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args == null || args.length < 4) {
			throw new IllegalArgumentException("Argument is not specified, or the number of the argument is invalid.");
		}

		MigrateMode mode = MigrateMode.valueOf(args[0].toUpperCase());
		MigrateTarget target = MigrateTarget.valueOf(args[1].toUpperCase());
		String rootDir = args[2];
		int tenantId = Integer.parseInt(args[3]);

		try {
			if (tenantId >= 0) {
				(new LobStoreMigrator(mode, target, rootDir, tenantId)).execute();
			} else {
				List<TenantInfo> tenants = getValidTenantInfoList();
				if (tenants != null) {
					for (TenantInfo t: tenants) {
						(new LobStoreMigrator(mode, target, rootDir, t.getId())).execute();
					}
				}
			}
		} finally {
			// リソース破棄
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	public LobStoreMigrator(MigrateMode mode, MigrateTarget target, String rootDir, int tenantId) {
		this.tenantId = tenantId;

		this.rootDir = rootDir;
		this.migrateMode = mode;
		this.migrateTarget = target;
	}

	/**
	 * Lobデータの移行を行います。
	 *
	 * @return boolean 成功：true 失敗：false
	 * @throws Exception
	 */
	public boolean execute() throws Exception {
		setSuccess(false);

		clearLog();

		return executeTask(null, (param) -> {

			return ExecuteContext.executeAs(tenantContextService.getTenantContext(tenantId), () -> {

				logArguments();

				Transaction.required(t -> {
						ServiceRegistry sr = ServiceRegistry.getRegistry();
						LobStoreService lobStoreService = sr.getService(LobStoreService.class);

						ConfigImpl config = new ConfigImpl("lobStoreMigrator", null, null);
						config.addDependentService(RdbAdapterService.class.getName(), sr.getService(RdbAdapterService.class));

						RdbLobStore rdbLobStore = new RdbLobStore();
						FileLobStore fileLobStore = new FileLobStore();
						fileLobStore.setRootDir(rootDir);

						rdbLobStore.inited(lobStoreService, config);
						fileLobStore.inited(lobStoreService, config);

						LobStore lobStore = MigrateMode.F2R.equals(migrateMode) ? fileLobStore : rdbLobStore;

						RdbAdapter rdb = sr.getService(RdbAdapterService.class).getRdbAdapter();
						LobDao dao = lobStoreService.getLobDao();

						SqlExecuter<Void> exec = new SqlExecuter<Void>() {
							@Override
							public Void logic() throws SQLException {
								BlobSearchSql sqlCreator = rdb.getQuerySqlCreator(BlobSearchSql.class);
								String sql = sqlCreator.toSqlForMigrate(rdb, tenantId);

								try (ResultSet rs = getStatement().executeQuery(sql)) {
									while (rs.next()) {
										Lob lob = sqlCreator.toBinaryData(rs, lobStore, dao, lobStoreService.isManageLobSizeOnRdb());

										if ((MigrateTarget.BINARY.equals(migrateTarget) && LongTextType.LOB_NAME.equals(lob.getName())) ||
												(MigrateTarget.LONGTEXT.equals(migrateTarget) && !LongTextType.LOB_NAME.equals(lob.getName()))) {
											continue;
										}

										// 移行処理
										switch (migrateMode) {
										case R2F:	// RDB to File
											migrateRdbToFile(lob, fileLobStore);
											break;
										case F2R:	// File to RDB
											migrateFileToRdb(lob, rdbLobStore);
											break;
										}
									}
									return null;
								}
							}
						};
						exec.execute(rdb, true);

						return null;
				});

				setSuccess(true);

				return isSuccess();
			});
		});
	}

	/**
	 * パラメータ値ログ出力
	 *
	 */
	private void logArguments() {
		logInfo("■Execute Argument");
		logInfo("\ttenant id :" + tenantId);
		logInfo("");
	}

	private void migrateRdbToFile(Lob lob, FileLobStore fileLobStore) {
		if (lob.getSize() > 0) {
			FileLobData fileLobData = (FileLobData) fileLobStore.create(tenantId, lob.getLobDataId());
			Path path = Paths.get(fileLobData.getFilePath());

			try (InputStream in = lob.getBinaryInputStream()) {
				Files.createDirectories(path.getParent());
				Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				logError("An error has occurred. : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void migrateFileToRdb(Lob lob, RdbLobStore rdbLobStore) {
		FileLobData fileLobData = (FileLobData) lob.getLobData();

		if (Files.exists(Paths.get(fileLobData.getFilePath()))) {
			// LobStore削除
			rdbLobStore.remove(tenantId, lob.getLobDataId());

			// LobStore作成
			LobData rdbLobData = rdbLobStore.create(tenantId, lob.getLobDataId());

			try (OutputStream out = rdbLobData.getBinaryOutputStream()) {
				Files.copy(Paths.get(fileLobData.getFilePath()), out);
			} catch (IOException e) {
				logError("An error has occurred. : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 移行モード
	 * @author lis9cb
	 *
	 */
	public enum MigrateMode {
		/** File to RDB */
		F2R,
		/** RDB to File */
		R2F,
	}

	/**
	 * 移行対象
	 * @author lis9cb
	 *
	 */
	public enum MigrateTarget {
		/** All */
		ALL,
		/** Binary only */
		BINARY,
		/** LongText only */
		LONGTEXT,
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
