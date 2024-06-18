/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.tools.batch.metadata;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataStore;
import org.iplass.mtp.impl.metadata.composite.CompositeMetaDataStore;
import org.iplass.mtp.impl.metadata.composite.MetaDataStorePathMapping;
import org.iplass.mtp.impl.metadata.rdb.RdbMetaDataStore;
import org.iplass.mtp.impl.metadata.xmlfile.VersioningXmlFileMetaDataStore;
import org.iplass.mtp.impl.metadata.xmlfile.XmlFileMetaDataStore;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RDB管理のメタデータをローカルファイルへ格納する
 *
 * <p>機能概要</p>
 * <ul>
 * <li>RDB管理しているメタデータを、メタデータ単位に個別のローカルファイルへ保存する</li>
 * <li>初期移行の場合は、RDB管理しているメタデータすべてが対象となる</li>
 * <li>初期移行ではない場合は、Service-Configの設定で、RDB管理しているメタデータが対象となる</li>
 * </ul>
 *
 * <p>実行方法</p>
 * <ul>
 * <li>
 * プログラム引数
 * <ul>
 * <li>実行モード: SILENT|WIZARD</li>
 * </ul>
 * </li>
 * <li>
 * VM引数
 * <ul>
 * <li>mtp.config: service-config のファイルパスを指定する</li>
 * <li>meta.config: MetaDataExport 実行時の引数を指定する。実行モード SILENT の場合のみ利用</li>
 * </ul>
 * </li>
 * <li>
 * 注意点
 * <ul>
 * <li>
 * mtp.config で指定する service-config には以下の指定が必須
 * <ol>
 * <li>Service {@link org.iplass.mtp.impl.metadata.MetaDataRepository}#tenantLocalStore には、{@link org.iplass.mtp.impl.metadata.composite.CompositeMetaDataStore} を設定する</li>
 * <li>{@link org.iplass.mtp.impl.metadata.composite.CompositeMetaDataStore}#store には、{@link org.iplass.mtp.impl.metadata.xmlfile.XmlFileMetaDataStore} または {@link org.iplass.mtp.impl.metadata.xmlfile.VersioningXmlFileMetaDataStore} を設定する</li>
 * </ol>
 * </li>
 * <li>
 * meta.config で指定するプロパティファイルには以下の指定が必要<br>
 * - テナントID（ tenantId ）<br>
 * - 出力先ディレクトリ（ exportDir ）<br>
 * - ファイル名（ fileName ）
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * <p>ウィザード実行例</p>
 * <pre>
 * java
 *   -Dmtp.config=/path/to/service-config/mtp-service-config.xml
 *   org.iplass.mtp.tools.batch.metadata.MetaDataExportRdbToFile WIZARD
 * </pre>
 *
 * <p>プロパティ利用の実行例</p>
 * <pre>
 * java
 *   -Dmtp.config=/path/to/service-config/mtp-service-config.xml
 *   -Dmeta.config=/path/to/config/config.properties
 *   org.iplass.mtp.tools.batch.metadata.MetaDataExportRdbToFile SILENT
 * </pre>
 */
public class MetaDataExportRdbToFile extends MtpCuiBase {
	/**
	 * Silentモード用 プロパティファイルのキー
	 */
	private static final class PropertyKeys {
		/** テナントURL */
		public static final String TENANT_URL = "tenantUrl";
		/** テナントID */
		public static final String TENANT_ID = "tenantId";
		/** 初期移行フラグ */
		public static final String INITIAL_CONVERT = "initialConvert";
		/** メタデータ 対象Path、複数ある場合はカンマ区切り、未指定の場合は全て */
		public static final String META_SOURCE = "meta.source";
	}

	/** ロガー */
	private static Logger LOGGER = LoggerFactory.getLogger(MetaDataExportRdbToFile.class);
	/** メタデータリポジトリハンドラ */
	private MetaDataRepositoryeHadler handler;

	/**
	 * デフォルトコンストラクタ
	 */
	public MetaDataExportRdbToFile() {
		handler = getMetaDataRepositoryeHadler();
	}

	/**
	 * メイン処理
	 *
	 * <p>プログラム引数は、以下のいずれかを設定する。指定が無い場合は WIZARD で動作する。</p>
	 * <ul>
	 * <li>WIZARD : 標準入力から設定値を入力し実行</li>
	 * <li>SILENT : コンフィグファイルに設定値を入力し実行</li>
	 * </ul>
	 *
	 * @param args プログラム引数
	 */
	public static void main(String[] args) {
		boolean isSuccess = false;
		try {
			//
			ExecMode mode = ExecMode.WIZARD;
			if (1 <= args.length) {
				ExecMode argMode = ExecMode.valueOf(args[0].toUpperCase());
				if (null != argMode) {
					mode = argMode;
				}
			}

			MetaDataExportRdbToFile instance = new MetaDataExportRdbToFile();
			isSuccess = instance.execute(mode);

		} finally {
			ExecuteContext.finContext();
			MtpBatchResourceDisposer.disposeResource();
		}
		System.exit(isSuccess ? 0 : -1);
	}

	/**
	 * 処理エントリポイント
	 *
	 * @param execMode 実行モード
	 * @return 実行結果（true: 正常終了、false: 異常終了）
	 */
	public boolean execute(ExecMode execMode) {
		return executeTask(null, unused -> {
			return Transaction.required(t -> {
				switchLog(false, true);

				if (null == execMode) {
					throw new NullPointerException(rs("MetaDataExportRdbToFile.incorrecArg.execMode"));
				}

				MetaDataExportRdbToFileParameter parameter = null;
				if (execMode == ExecMode.WIZARD) {
					switchLog(true, false);
					parameter = new WizardParamterFactory(this).create(handler);
					switchLog(false, true);

				} else if (execMode == ExecMode.SILENT) {
					parameter = new SilentParameterFactory(this).create(handler);

				}

				TenantContext context = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(parameter.getTenantId());

				final MetaDataExportRdbToFileParameter finalParameter = parameter;

				return ExecuteContext.executeAs(context, () -> {
					exportRdbToFile(finalParameter);
					logInfo(rs("MetaDataExportRdbToFile.finishLog", handler.getFileStoreAbsolutePath()));
					return true;
				});
			});
		});
	}

	/**
	 * Rdbからメタデータファイルを抽出し、ファイルへ保存する。
	 * @param parameter プログラムパラメータ
	 */
	public void exportRdbToFile(MetaDataExportRdbToFileParameter parameter) {
		for (String path : parameter.getExportMetaDataPathList()) {
			MetaDataEntry entry = handler.getRdbMetaDataStore().load(parameter.getTenantId(), path);
			if (null == entry) {
				logWarn(rs("MetaDataExportRdbToFile.notFoundMetaLog", path));
			} else {
				logDebug("store metadata: " + path + ".");
				handler.store(parameter.getTenantId(), entry);
			}
		}
	}

	@Override
	protected Logger loggingLogger() {
		return LOGGER;
	}

	/**
	 * MetaDataRepositoryeHadler 操作インスタンスを取得する
	 *
	 * @return MetaDataRepositoryeHadler 操作インスタンス
	 */
	private MetaDataRepositoryeHadler getMetaDataRepositoryeHadler() {
		MetaDataRepository metadataRepository = ServiceRegistry.getRegistry().getService(MetaDataRepository.class);
		MetaDataStore tenantLocalStore = metadataRepository.getTenantLocalStore();

		if (!(tenantLocalStore instanceof CompositeMetaDataStore)) {
			throw new RuntimeException(rs("MetaDataExportRdbToFile.incorrectServiceConfig.tenantLocalStoreProperty"));
		}

		CompositeMetaDataStore casted = (CompositeMetaDataStore) tenantLocalStore;

		XmlFileMetaDataStore xmlStore = casted.getStore(XmlFileMetaDataStore.class);
		if (null != xmlStore) {
			return new XmlFileMetaDataStoreHandler(this, casted, xmlStore);
		}

		VersioningXmlFileMetaDataStore versioningXmlStore = casted.getStore(VersioningXmlFileMetaDataStore.class);
		if (null != versioningXmlStore) {
			return new VersioningXmlFileMetaDataStoreHandler(this, casted, versioningXmlStore);
		}

		throw new RuntimeException(rs("MetaDataExportRdbToFile.incorrectServiceConfig.storeProperty"));
	}

	/**
	 * MetaDataRepositoryeHadler 操作インターフェース
	 */
	private static interface MetaDataRepositoryeHadler {
		/**
		 * MetaDataRepositoryeHadler に設定されているテナントローカルストアを取得する
		 * @return テナントローカルストア
		 */
		CompositeMetaDataStore getTenantLocalStore();

		/**
		 * RdbMetaDataStore を取得する
		 * @return RdbStore RdbMetaDataStoreインスタンス
		 */
		RdbMetaDataStore getRdbMetaDataStore();

		/**
		 * メタデータを CompositeMetaDataStore に格納する際、当該パスは RdbMetaDataStore に格納されるかを判定する。
		 *
		 * @param path 格納パス
		 * @return 判定結果（true: RdbMetaDataStore に格納される）
		 */
		boolean isRdbMetaDataStore(String path);

		/**
		 * 設定を検証する
		 * @param tenantId プログラムパラメータ
		 * @return バリデーション結果（true: 正常終了、 false: 異常終了）
		 */
		boolean validate(int tenantId);

		/**
		 * 設定を検証する
		 *
		 * <p>
		 * 設定に問題がある場合、例外をスローする
		 * </p>
		 *
		 * @param tenantId プログラムパラメータ
		 */
		void validateIfErrorThrow(int tenantId);

		/**
		 * メタデータを保存する
		 * @param tenantId テナントID
		 * @param metaDataEntry メタデータエントリ
		 */
		void store(int tenantId, MetaDataEntry metaDataEntry);

		/**
		 * XMLファイル格納先パスを取得します
		 * @return XMLファイル格納先パス
		 */
		String getFileStorePath();

		/**
		 * XMLファイル格納先絶対パスを取得します
		 * @return XMLファイル格納先絶対パス
		 */
		String getFileStoreAbsolutePath();
	}

	/**
	 * 抽象ハンドラ
	 */
	private static abstract class AbstractRepositoryHandler implements MetaDataRepositoryeHadler {
		/** テナントローカストア */
		private CompositeMetaDataStore tenantLocalStore;
		/** テナントローカストアに設定されているRDBメタデータストア */
		private RdbMetaDataStore rdbMetaDataStore;
		/** 実行中インスタンス */
		private MetaDataExportRdbToFile instance;

		/** パスマッピング先がRdbMetaDataStore であるかの判定情報マップ */
		private Map<String, Boolean> pathMappingIsRdbMetaDataStore;
		/** デフォルトストアが RdbMetaDataStore であるか */
		private boolean isDefaultRdbMetaDataStore;

		/**
		 * コンストラクタ
		 * @param instance 実行中インスタンス
		 * @param tenantLocalStore テナントローカルストア
		 */
		public AbstractRepositoryHandler(MetaDataExportRdbToFile instance, CompositeMetaDataStore tenantLocalStore) {
			this.instance = instance;
			this.tenantLocalStore = tenantLocalStore;
			this.rdbMetaDataStore = tenantLocalStore.getStore(RdbMetaDataStore.class);

			pathMappingIsRdbMetaDataStore = new HashMap<>();
			for (MetaDataStorePathMapping mapping : tenantLocalStore.getPathMapping()) {
				// mapping 情報の store が RdbMetaDataStore であれば、value に true を設定
				pathMappingIsRdbMetaDataStore.put(mapping.getPathPrefix(), RdbMetaDataStore.class.getName().equals(mapping.getStore()));
			}

			// デフォルトストアが、RdbMetaDataStore である場合、true を設定
			isDefaultRdbMetaDataStore = RdbMetaDataStore.class.getName().equals(tenantLocalStore.getDefaultStoreClass());
		}

		@Override
		public CompositeMetaDataStore getTenantLocalStore() {
			return tenantLocalStore;
		}

		@Override
		public RdbMetaDataStore getRdbMetaDataStore() {
			return rdbMetaDataStore;
		}

		@Override
		public boolean isRdbMetaDataStore(String path) {
			for (Map.Entry<String, Boolean> entry : pathMappingIsRdbMetaDataStore.entrySet()) {
				if (path.startsWith(entry.getKey())) {
					return entry.getValue();
				}
			}

			return isDefaultRdbMetaDataStore;
		}

		/**
		 * 実行中 MetaDataExportRdbToFile インスタンスを取得する
		 * @return インスタンス
		 */
		protected MetaDataExportRdbToFile getInstance() {
			return this.instance;
		}

		@Override
		public String getFileStoreAbsolutePath() {
			return new File(getFileStorePath()).getAbsolutePath();
		}

	}

	/**
	 * XmlFileMetaDataStore を利用する MetaDataRepositoryeHadler
	 */
	private static class XmlFileMetaDataStoreHandler extends AbstractRepositoryHandler {
		/** ローカル保存用MetadataStore */
		private XmlFileMetaDataStore store;

		/**
		 * コンストラクタ
		 * @param instance 実行中インスタンス
		 * @param tenantLocalStore テナントローカルストア
		 * @param store ローカル保存用MetadataStore
		 */
		public XmlFileMetaDataStoreHandler(MetaDataExportRdbToFile instance, CompositeMetaDataStore tenantLocalStore, XmlFileMetaDataStore store) {
			super(instance, tenantLocalStore);
			this.store = store;
		}

		@Override
		public boolean validate(int tenantId) {
			String errorMessage = validateInner(tenantId);
			if (null != errorMessage) {
				getInstance().logError(errorMessage);
				return false;
			}
			return true;
		}

		@Override
		public void validateIfErrorThrow(int tenantId) {
			String errorMessage = validateInner(tenantId);
			if (null != errorMessage) {
				throw new RuntimeException(errorMessage);
			}
		}

		private String validateInner(int tenantId) {
			// XmlFileMetaDataStore に設定されているテナントIDと、プログラムパラメータのテナントIDが同一チェック
			if (store.getLocalTenantId() != tenantId) {
				return getInstance().rs("MetaDataExportRdbToFile.incorrecConfig.tenantId", store.getLocalTenantId(), tenantId);
			}

			// エラーなしの場合は null 返却
			return null;
		}

		@Override
		public void store(int tenantId, MetaDataEntry metaDataEntry) {
			store.store(tenantId, metaDataEntry, metaDataEntry.getVersion());
		}

		@Override
		public String getFileStorePath() {
			return store.getFileStorePath();
		}
	}

	/**
	 * VersioningXmlFileMetaDataStore を利用する MetaDataRepositoryeHadler
	 */
	private static class VersioningXmlFileMetaDataStoreHandler extends AbstractRepositoryHandler {
		/** ローカル保存用MetadataStore */
		private VersioningXmlFileMetaDataStore store;

		/**
		 * コンストラクタ
		 * @param instance 実行中インスタンス
		 * @param tenantLocalStore テナントローカルストア
		 * @param store ローカル保存用MetadataStore
		 */
		public VersioningXmlFileMetaDataStoreHandler(MetaDataExportRdbToFile instance, CompositeMetaDataStore tenantLocalStore,
				VersioningXmlFileMetaDataStore store) {
			super(instance, tenantLocalStore);
			this.store = store;
		}

		@Override
		public boolean validate(int tenantId) {
			return true;
		}

		@Override
		public void validateIfErrorThrow(int tenantId) {
		}

		@Override
		public void store(int tenantId, MetaDataEntry metaDataEntry) {
			store.store(tenantId, metaDataEntry);
		}

		@Override
		public String getFileStorePath() {
			return store.getFileStorePath();
		}
	}

	/**
	 * 抽象パラメータ生成機能
	 *
	 * <p>
	 * パラメータ生成における共通的な機能を実装する。
	 * </p>
	 */
	private static abstract class AbstractParameterFactory {
		/** 実行中インスタンス */
		protected MetaDataExportRdbToFile instance;
		/** テナントサービス */
		protected TenantService ts = ServiceRegistry.getRegistry().getService(TenantService.class);
		/** テナントコンテキストサービス */
		protected TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);

		/**
		 * コンストラクタ
		 * @param instance 実行中インスタンス
		 */
		public AbstractParameterFactory(MetaDataExportRdbToFile instance) {
			this.instance = instance;
		}

		/**
		 * パラメータを生成する
		 * @param handler MetaDataRepositoryeHadler
		 * @return パラメータ
		 */
		public abstract MetaDataExportRdbToFileParameter create(MetaDataRepositoryeHadler handler);

		/**
		 * MetaDataExport情報を出力します。
		 */
		protected void logArguments(final MetaDataExportRdbToFileParameter param, MetaDataRepositoryeHadler handler) {
			instance.logInfo("-----------------------------------------------------------");
			instance.logInfo("+ Execute Argument");
			instance.logInfo("  tenant name :" + param.getTenantName());
			String metaTarget = param.isInitaialConvert() ? "Initial (include Tenant)"
					: param.isExportAllMetaData() ? "RDB ALL" : param.getExportMetaDataPath();

			metaTarget += "(" + param.getExportMetaDataPathList().size() + ")";

			instance.logInfo("  metadata target :" + metaTarget);
			instance.logInfo("  xml metadata output to : " + handler.getFileStorePath());
			instance.logInfo("  output absolute path : " + new File(handler.getFileStorePath()).getAbsolutePath());
			instance.logInfo("-----------------------------------------------------------");
			instance.logInfo("");
		}

		/**
		 * RDB管理しているメタデータのパスを全て取得する。
		 *
		 * <p>
		 * 情報抽出には RdbMetaDataStore を利用する。
		 * </p>
		 *
		 * @param param プログラムパラメータ
		 * @return メタデータパス
		 */
		protected List<String> getRdbManagedMetaDataPathList(final MetaDataExportRdbToFileParameter param) {
			RdbMetaDataStore rdbStore = instance.handler.getRdbMetaDataStore();
			// RDBより抽出
			List<MetaDataEntryInfo> rdbAllMeta = rdbStore.definitionList(param.getTenantId(), "/");
			return rdbAllMeta.stream()
					// 共有メタデータは除外
					.filter(m -> RepositoryType.SHARED != m.getRepositryType())
					.map(m -> m.getPath())
					.sorted()
					.collect(Collectors.toList());
		}

		/**
		 * RDB管理しているメタデータパスを取得する。
		 *
		 * <p>
		 * Service-Config にしたがったRDB管理対象のメタデータを抽出する。
		 * </p>
		 *
		 * @param param プログラムパラメータ
		 * @return メタデータパス
		 */
		protected List<String> getMetaDataPathList(MetaDataExportRdbToFileParameter param) {
			// MetaDataContext より抽出
			List<MetaDataEntryInfo> allMeta = MetaDataContext.getContext().definitionList("/");
			return allMeta.stream()
					// 共有メタデータは除外
					.filter(m -> RepositoryType.SHARED != m.getRepositryType())
					// RdbMetaDataStore に格納するメタデータのみ
					.filter(m -> instance.handler.isRdbMetaDataStore(m.getPath()))
					.map(m -> m.getPath())
					.sorted()
					.collect(Collectors.toList());
		}

		/**
		 * 設定されたパスにしたがってメタデータを取得する。
		 *
		 * <p>
		 * Service-Config にしたがったRDB管理対象のメタデータのうち、指定されたパスに一致するメタデータを抽出する。
		 * </p>
		 *
		 * @param param パラメータ
		 */
		protected List<String> getMetaDataPathListByPath(final MetaDataExportRdbToFileParameter param) {

			Set<String> directPathSet = new HashSet<>(); //重複を避けるためSetに保持
			String[] pathStrArray = param.getExportMetaDataPath().split(",");

			for (String pathStr : pathStrArray) {
				//,,などの阻止
				if (StringUtil.isEmpty(pathStr)) {
					continue;
				}

				if (pathStr.endsWith("*")) {
					//アスタリスク指定
					List<MetaDataEntryInfo> allMeta = MetaDataContext.getContext().definitionList(pathStr.substring(0, pathStr.length() - 1));
					allMeta.stream()
					// 共有メタデータは除外
					.filter(m -> RepositoryType.SHARED != m.getRepositryType())
					// RdbMetaDataStore に格納するメタデータのみ
					.filter(m -> instance.handler.isRdbMetaDataStore(m.getPath()))
					.forEach(m -> directPathSet.add(m.getPath()));

				} else {
					//直接指定
					MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(pathStr);

					if (entry == null) {
						// エンティティ無し
						instance.logWarn(instance.rs("MetaDataExportRdbToFile.notFoundMetaLog", pathStr));
						continue;

					} else if (entry.getRepositryType() == RepositoryType.SHARED && instance.handler.isRdbMetaDataStore(entry.getPath())) {
						// 対象外
						instance.logWarn(instance.rs("MetaDataExportRdbToFile.notCoveredLog", pathStr));
						continue;
					}

					directPathSet.add(entry.getPath());
				}
			}

			return directPathSet.stream().sorted().collect(Collectors.toList());
		}

		/**
		 * Export対象のメタデータパスを出力します。
		 * @param param パラメータ
		 */
		protected void showMetaDataPathList(final MetaDataExportRdbToFileParameter param) {
			instance.logInfo("-----------------------------------------------------------");
			instance.logInfo("+ MetaData List");
			param.getExportMetaDataPathList().stream().forEach(path -> instance.logInfo(path));
			instance.logInfo("-----------------------------------------------------------");
		}

	}

	/**
	 * WIZARD パターンのパラメータ生成機能
	 *
	 * <p>
	 * 標準入力よりパラメータを生成する。
	 * </p>
	 */
	private static class WizardParamterFactory extends AbstractParameterFactory {
		/**
		 * コンストラクタ
		 * @param instance
		 */
		public WizardParamterFactory(MetaDataExportRdbToFile instance) {
			super(instance);
		}

		@Override
		public MetaDataExportRdbToFileParameter create(MetaDataRepositoryeHadler handler) {
			//テナントURL
			Tenant tenant = readTenantRequire(handler);
			MetaDataExportRdbToFileParameter param = new MetaDataExportRdbToFileParameter();
			param.setTenantId(tenant.getId());
			param.setTenantName(tenant.getName());

			TenantContext tc = tcs.getTenantContext(param.getTenantId());

			return ExecuteContext.executeAs(tc, () -> {
				ExecuteContext.getCurrentContext().setLanguage(instance.getLanguage());

				boolean isInitialConvert = instance.readConsoleBoolean(instance.rs("MetaDataExportRdbToFile.Wizard.confirmInitialConvertMsg"),
						param.isInitaialConvert());
				param.setInitaialConvert(isInitialConvert);

				if (isInitialConvert) {
					// 初期移行
					param.setExportMetaDataPathList(getRdbManagedMetaDataPathList(param));

				} else {
					boolean isExportAllMeta = instance.readConsoleBoolean(
							instance.rs("MetaDataExportRdbToFile.Wizard.confirmExportAllMetaMsg"), param.isExportAllMetaData());
					param.setExportAllMetaData(isExportAllMeta);

					if (isExportAllMeta) {
						// 全メタデータ対象
						param.setExportMetaDataPathList(getMetaDataPathList(param));

					} else {
						//個別指定メタデータ
						String exportMetaDataPath = readSpecifiedMetaDataPathRequire();

						param.setExportMetaDataPath(exportMetaDataPath);
						//Pathの取得
						param.setExportMetaDataPathList(getMetaDataPathListByPath(param));
					}
				}

				showMetaDataPathList(param);

				do {
					//実行情報出力
					logArguments(param, handler);

					if (instance.readConsoleBoolean(instance.rs("MetaDataExportRdbToFile.Wizard.confirmExecuteMsg"), false)) {
						break;
					}

					//defaultがfalseなので念のため再度確認
					if (instance.readConsoleBoolean(instance.rs("MetaDataExportRdbToFile.Wizard.confirmRetryMsg"), true)) {
						//再度実行
						return create(handler);
					}
				} while (true);

				return param;
			});

		}

		/**
		 * テナント情報を読み取る
		 *
		 * <p>
		 * 正しい入力が行われるまで処理を継続する。
		 * </p>
		 *
		 * @param handler MetaDataRepositoryeHadler
		 * @return テナント情報
		 */
		private Tenant readTenantRequire(MetaDataRepositoryeHadler handler) {
			return readRequire(() -> {
				String tenantUrl = instance.readConsole(instance.rs("Common.inputTenantUrlMsg"));

				if (StringUtil.isEmpty(tenantUrl)) {
					instance.logWarn(instance.rs("Common.requiredTenantUrlMsg"));
					return null;
				}
				if (tenantUrl.equalsIgnoreCase("-show")) {
					//一覧を出力
					instance.showValidTenantList();
					return null;
				}
				if (tenantUrl.equalsIgnoreCase("-env")) {
					//環境情報を出力
					instance.logEnvironment();
					return null;
				}

				//URL存在チェック
				String url = tenantUrl.startsWith("/") ? tenantUrl : "/" + tenantUrl;
				Tenant tenant = ts.getTenant(url);
				if (tenant == null) {
					instance.logWarn(instance.rs("Common.notExistsTenantMsg", tenantUrl));
					return null;
				}

				// 検証
				if (!handler.validate(tenant.getId())) {
					return null;
				}

				return tenant;
			});
		}

		/**
		 * 個別メタデータパスを読み取る
		 *
		 * <p>
		 * 正しい入力が行われるまで処理を継続する。
		 * </p>
		 *
		 * @return 個別メタデータパス
		 */
		private String readSpecifiedMetaDataPathRequire() {
			return readRequire(() -> {
				String value = instance.readConsole(instance.rs("MetaDataExportRdbToFile.Wizard.inputMetaPathMsg"));
				if (StringUtil.isEmpty(value)) {
					//未指定なのでContinue
					instance.logWarn(instance.rs("MetaDataExportRdbToFile.Wizard.requiredMetaPathMsg"));
					instance.logInfo("");
					return null;
				}
				return value;
			});
		}

		/**
		 * 値を読み取ることができるまで処理を継続する
		 *
		 * <p>
		 * supplier では正しい入力が行われた場合のみ値を返却する。
		 * 不正な値が設定された場合は、null を返却することで処理を再実行する。
		 * </p>
		 *
		 * @param <T> 読み取りデータ型
		 * @param supplier データ読み取りロジック
		 * @return 読み取りデータ
		 */
		private <T> T readRequire(Supplier<T> supplier) {
			while (true) {
				T value = supplier.get();
				if (null != value) {
					return value;
				}
			}
		}
	}

	/**
	 * SILENT パターンのパラメータ生成機能
	 *
	 * <p>
	 * プロパティファイルよりパラメータを生成する。
	 * </p>
	 */
	private static class SilentParameterFactory extends AbstractParameterFactory {
		/**
		 * コンストラクタ
		 * @param instance 実行中インスタンス
		 */
		public SilentParameterFactory(MetaDataExportRdbToFile instance) {
			super(instance);
		}

		@Override
		public MetaDataExportRdbToFileParameter create(MetaDataRepositoryeHadler handler) {
			//プロパティファイルの取得
			String configFileName = System.getProperty(MetaDataExport.KEY_CONFIG_FILE);
			if (StringUtil.isEmpty(configFileName)) {
				throw new RuntimeException(instance.rs("MetaDataExportRdbToFile.Silent.requiredConfigFileMsg", MetaDataExport.KEY_CONFIG_FILE));
			}

			//プロパティの取得
			Properties prop = new Properties();
			try (InputStream input = getInputStreamFromFileOrResource(configFileName);
					InputStreamReader reader = new InputStreamReader(input, "UTF-8");) {
				prop.load(reader);
			} catch (IOException e) {
				throw new SystemException(e);
			}

			//テナントの取得
			Tenant tenant = null;
			//プロパティから取得
			//ID
			String propTenantId = prop.getProperty(PropertyKeys.TENANT_ID);
			if (StringUtil.isNotEmpty(propTenantId)) {
				tenant = ts.getTenant(Integer.parseInt(propTenantId));
				if (tenant == null) {
					throw new RuntimeException(instance.rs("Common.notExistsTenantIdMsg", propTenantId));
				}
			}
			if (tenant == null) {
				//URL
				String propTenantUrl = prop.getProperty(PropertyKeys.TENANT_URL);
				if (StringUtil.isNotEmpty(propTenantUrl)) {
					if (!propTenantUrl.startsWith("/")) {
						propTenantUrl = "/" + propTenantUrl;
					}
					tenant = ts.getTenant(propTenantUrl);
					if (tenant == null) {
						throw new RuntimeException(instance.rs("Common.notExistsTenantMsg", propTenantUrl));
					}
				}
			}
			if (tenant == null) {
				throw new RuntimeException(instance.rs("Common.requiredMsg", PropertyKeys.TENANT_ID + " or " + PropertyKeys.TENANT_URL));
			}

			instance.logInfo("target tenant:[" + tenant.getId() + "]" + tenant.getName());
			// 検証
			handler.validateIfErrorThrow(tenant.getId());

			MetaDataExportRdbToFileParameter param = new MetaDataExportRdbToFileParameter();
			param.setTenantId(tenant.getId());
			param.setTenantName(tenant.getName());

			TenantContext tc = tcs.getTenantContext(param.getTenantId());
			return ExecuteContext.executeAs(tc, () -> {
				ExecuteContext.getCurrentContext().setLanguage(instance.getLanguage());

				String initialConvert = prop.getProperty(PropertyKeys.INITIAL_CONVERT);
				if (StringUtil.isNotEmpty(initialConvert)) {
					param.setInitaialConvert(Boolean.valueOf(initialConvert));
				}

				String source = prop.getProperty(PropertyKeys.META_SOURCE);

				if (param.isInitaialConvert()) {
					// 初期移行
					param.setExportMetaDataPathList(getRdbManagedMetaDataPathList(param));

				} else if (StringUtil.isEmpty(source)) {
					// 全対象
					param.setExportAllMetaData(true);
					param.setExportMetaDataPathList(getMetaDataPathList(param));

				} else {
					// 対象指定
					param.setExportAllMetaData(false);
					param.setExportMetaDataPath(source);
					param.setExportMetaDataPathList(getMetaDataPathListByPath(param));
				}

				//実行情報出力
				logArguments(param, handler);

				return param;
			});
		}

		/**
		 * パラメータで指定されたファイルを、ファイルパスもしくはClasspathリソースから取得する
		 *
		 * <ul>
		 * <li>どちらにもファイルが存在する場合は、ファイルパスを優先する。</li>
		 * <li>どちらにもファイルが存在しない場合は、例外をスローする</li>
		 * </ul>
		 *
		 * @param file 読み取りパス
		 * @return ファイルもしくはリソースの InputStream インスタンス
		 * @throws IOException 入出力例外
		 */
		private InputStream getInputStreamFromFileOrResource(String file) throws IOException {
			if (Files.exists(Paths.get(file))) {
				// ファイル
				instance.logDebug("load file from file path: " + file);
				return new BufferedInputStream(new FileInputStream(file));
			} else {
				// classpath resource
				instance.logDebug("load file from classpath: " + file);
				InputStream input = getClass().getResourceAsStream(file);
				if (null != input) {
					return input;
				}
			}

			throw new FileNotFoundException(instance.rs("MetaDataExportRdbToFile.fileResourceNotFound", file));
		}
	}
}
