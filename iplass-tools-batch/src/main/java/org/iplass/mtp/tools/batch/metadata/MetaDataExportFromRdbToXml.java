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

import static org.iplass.mtp.tools.batch.metadata.MetaDataExportParameter.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.iplass.mtp.impl.metadata.rdb.RdbMetaDataStore;
import org.iplass.mtp.impl.metadata.xmlfile.VersioningXmlFileMetaDataStore;
import org.iplass.mtp.impl.metadata.xmlfile.XmlFileMetaDataStore;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.tools.metaport.XMLEntryInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RDB管理のメタデータをローカルへ格納する
 *
 * <p>機能概要</p>
 * <ul>
 * <li>{@link org.iplass.mtp.tools.batch.metadata.MetaDataExport} を利用して、メタデータを抽出する。</li>
 * <li>抽出したメタデータが RDB 管理されている場合、ローカルに XML Metadata として保存する</li>
 * </ul>
 *
 * <p>備考<p>
 * <ul>
 * <li>{@link org.iplass.mtp.tools.batch.metadata.MetaDataExport} のメタデータ抽出ファイルは一時ファイルです。</li>
 * <li>MetaDataExport 機能を利用し、またその機能のパラメータだけで設定情報が足りるため専用のパラメータクラスは用意しない。</li>
 * </ul>
 * <p>
 *
 * 抽出したメタデータのうち RDB 管理されているものだけを、ローカルストアに XML Metadata として保存する。
 * </p>
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
 *   org.iplass.mtp.tools.batch.metadata.MetaDataExportFromRdbToXml WIZARD
 * </pre>
 *
 * <p>プロパティ利用の実行例</p>
 * <pre>
 * java
 *   -Dmtp.config=/path/to/service-config/mtp-service-config.xml
 *   -Dmeta.config=/path/to/config/config.properties
 *   org.iplass.mtp.tools.batch.metadata.MetaDataExportFromRdbToXml SILENT
 * </pre>
 */
public class MetaDataExportFromRdbToXml extends MtpCuiBase {
	/** ロガー */
	private static Logger LOGGER = LoggerFactory.getLogger(MetaDataExportFromRdbToXml.class);
	/** メタデータリポジトリハンドラ */
	private MetaDataRepositoryeHadler handler;

	/**
	 * デフォルトコンストラクタ
	 */
	public MetaDataExportFromRdbToXml() {
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

			MetaDataExportFromRdbToXml instance = new MetaDataExportFromRdbToXml();
			isSuccess = instance.execute(mode);

		} finally {
			ExecuteContext.finContext();
			MtpBatchResourceDisposer.disposeResource();
		}
		System.exit(isSuccess ? 0 : -1);
	}

	/**
	 * 処理エントリポイント
	 * @param execMode 実行モード
	 * @return 実行結果（true: 正常終了、false: 異常終了）
	 */
	public boolean execute(ExecMode execMode) {
		return executeTask(null, unused -> {
			File tempMetadataFile = null;
			try {
				switchLog(false, true);

				if (null == execMode) {
					throw new NullPointerException(rs("MetaDataExportFromRdbToXml.incorrecArg.execMode"));
				}

				MetaDataExportParameter parameter = null;
				if (execMode == ExecMode.WIZARD) {
					switchLog(true, false);
					parameter = new WizardParamterFactory(this).create(handler);
					switchLog(false, true);

				} else if (execMode == ExecMode.SILENT) {
					parameter = new SilentParameterFactory(this).create(handler);

				}

				tempMetadataFile = parameter.getExportDir();

				executeMetaDataExport(parameter);
				executeExportXmlMetadata(parameter);

				logInfo(rs("MetaDataExportFromRdbToXml.finishLog", handler.getFileStoreAbsolutePath()));

				return true;

			} finally {
				// ファイルが存在したら削除
				if (null != tempMetadataFile && tempMetadataFile.exists()) {
					tempMetadataFile.delete();
				}
			}
		});
	}


	/**
	 * tools-batch MetaDataExport を利用して、メタデータファイルを抽出する
	 * @param parameter プログラムパラメータ
	 */
	public void executeMetaDataExport(MetaDataExportParameter parameter) {
		// 実行パラメータ作成済みなので、コンストラクタの引数は無し
		MetaDataExport metadataExport = new MetaDataExport();
		boolean success = metadataExport.exportMeta(parameter);
		if (!success) {
			throw new RuntimeException(rs("MetaDataExportFromRdbToXml.failMetaDataExport"));
		}
	}

	/**
	 * 抽出したメタデータファイルから、RDB管理のメタデータをXMLファイルに保存する
	 * @param parameter プログラムパラメータ
	 */
	public void executeExportXmlMetadata(MetaDataExportParameter parameter) {
		File metadataFile = Paths.get(parameter.getExportDirName(), parameter.getFileName() + ".xml").toFile();
		Map<String, MetaDataEntry> metadata = readMetadataFile(metadataFile);

		for (Map.Entry<String, MetaDataEntry> e : metadata.entrySet()) {
			MetaDataEntry metaDataEntry = e.getValue();
			if (metaDataEntry != null) {
				MetaDataStore store = handler.getTenantLocalStore().resolveStore(metaDataEntry.getPath());

				// RDB管理しているファイルを、XMLへ出力。既にローカル管理されているファイルは何もしない
				if (store instanceof RdbMetaDataStore) {
					handler.store(parameter.getTenantId(), metaDataEntry);
				}
			}
		}
	}

	@Override
	protected Logger loggingLogger() {
		return LOGGER;
	}

	/**
	 * {@link #executeMetaDataExport()} で作成したメタデータファイルを読み取る
	 * @param file メタデータファイルパス
	 * @return メタデータエントリ情報
	 */
	private Map<String, MetaDataEntry> readMetadataFile(File file) {
		try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
			MetaDataPortingService service = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);
			XMLEntryInfo xmlEntryInfo = service.getXMLMetaDataEntryInfo(input);
			return xmlEntryInfo.getPathEntryMap();

		} catch (IOException e) {
			throw new RuntimeException(rs("MetaDataExportFromRdbToXml.failReadMetadataFile", file.getAbsolutePath()), e);
		}
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
			throw new RuntimeException(rs("MetaDataExportFromRdbToXml.incorrectServiceConfig.tenantLocalStoreProperty"));
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

		throw new RuntimeException(rs("MetaDataExportFromRdbToXml.incorrectServiceConfig.storeProperty"));
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
		/** 実行中インスタンス */
		private MetaDataExportFromRdbToXml instance;

		/**
		 * コンストラクタ
		 * @param instance 実行中インスタンス
		 * @param tenantLocalStore テナントローカルストア
		 */
		public AbstractRepositoryHandler(MetaDataExportFromRdbToXml instance, CompositeMetaDataStore tenantLocalStore) {
			this.instance = instance;
			this.tenantLocalStore = tenantLocalStore;
		}

		@Override
		public CompositeMetaDataStore getTenantLocalStore() {
			return tenantLocalStore;
		}

		/**
		 * 実行中 MetaDataExportFromRdbToXml インスタンスを取得する
		 * @return インスタンス
		 */
		protected MetaDataExportFromRdbToXml getInstance() {
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
		public XmlFileMetaDataStoreHandler(MetaDataExportFromRdbToXml instance, CompositeMetaDataStore tenantLocalStore, XmlFileMetaDataStore store) {
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
				return getInstance().rs("MetaDataExportFromRdbToXml.incorrecConfig.tenantId", store.getLocalTenantId(), tenantId);
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
		public VersioningXmlFileMetaDataStoreHandler(MetaDataExportFromRdbToXml instance, CompositeMetaDataStore tenantLocalStore,
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
		protected MetaDataExportFromRdbToXml instance;
		/** テナントサービス */
		protected TenantService ts = ServiceRegistry.getRegistry().getService(TenantService.class);
		/** テナントコンテキストサービス */
		protected TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);

		/**
		 * コンストラクタ
		 * @param instance 実行中インスタンス
		 */
		public AbstractParameterFactory(MetaDataExportFromRdbToXml instance) {
			this.instance = instance;
		}

		/**
		 * パラメータを生成する
		 * @param handler MetaDataRepositoryeHadler
		 * @return パラメータ
		 */
		public abstract MetaDataExportParameter create(MetaDataRepositoryeHadler handler);

		/**
		 * MetaDataExport情報を出力します。
		 */
		protected void logArguments(final MetaDataExportParameter param, MetaDataRepositoryeHadler handler) {
			instance.logInfo("-----------------------------------------------------------");
			instance.logInfo("+ Execute Argument");
			instance.logInfo("  tenant name :" + param.getTenantName());
			String metaTarget = null;
			if (param.isExportAllMetaData()) {
				metaTarget = "ALL";
				if (param.isExportTenantMetaData()) {
					metaTarget += "(include Tenant)";
				} else {
					metaTarget += "(exclude Tenant)";
				}
			} else {
				metaTarget = param.getExportMetaDataPathStr();
			}
			metaTarget += "(" + param.getExportMetaDataPathList().size() + ")";

			instance.logInfo("  metadata target :" + metaTarget);
			instance.logInfo("  xml metadata output to : " + handler.getFileStorePath());
			instance.logInfo("  output absolute path : " + new File(handler.getFileStorePath()).getAbsolutePath());
			instance.logInfo("-----------------------------------------------------------");
			instance.logInfo("");
		}

		// TODO MetaDataExport と同一ロジック
		/**
		 * Export対象のメタデータパスを設定します。
		 * @param param パラメータ
		 */
		protected List<String> getMetaDataPathList(final MetaDataExportParameter param) {
			MetaDataPortingService mdps = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);

			List<String> paths = new ArrayList<>();
			if (param.isExportAllMetaData()) {
				List<MetaDataEntryInfo> allMeta = MetaDataContext.getContext().definitionList("/");
				for (MetaDataEntryInfo info : allMeta) {
					if (param.isExportLocalMetaDataOnly() && info.getRepositryType() == RepositoryType.SHARED) {
						continue;
					}
					if (!param.isExportTenantMetaData() && mdps.isTenantMeta(info.getPath())) {
						continue;
					}
					paths.add(info.getPath());
				}

			} else {
				//個別指定

				Set<String> directPathSet = new HashSet<>(); //重複を避けるためSetに保持
				String[] pathStrArray = param.getExportMetaDataPathStr().split(",");

				for (String pathStr : pathStrArray) {
					//,,などの阻止
					if (StringUtil.isEmpty(pathStr)) {
						continue;
					}

					if (pathStr.endsWith("*")) {
						//アスタリスク指定
						List<MetaDataEntryInfo> allMeta = MetaDataContext.getContext().definitionList(pathStr.substring(0, pathStr.length() - 1));
						for (MetaDataEntryInfo info : allMeta) {
							if (param.isExportLocalMetaDataOnly() && info.getRepositryType() == RepositoryType.SHARED) {
								continue;
							}
							directPathSet.add(info.getPath());
						}

					} else {
						//直接指定
						MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(pathStr);
						if (entry != null) {
							if (param.isExportLocalMetaDataOnly() && entry.getRepositryType() == RepositoryType.SHARED) {
								instance.logWarn(instance.rs("MetaDataExportFromRdbToXml.excludeNotLocalMetaLog", pathStr));
								continue;
							}
							directPathSet.add(entry.getPath());
						} else {
							instance.logWarn(instance.rs("MetaDataExportFromRdbToXml.notFoundMetaLog", pathStr));
							continue;
						}
					}
				}
				paths.addAll(directPathSet);
			}

			//ソートして返す
			return paths.stream().sorted().collect(Collectors.toList());
		}

		/**
		 * Export対象のメタデータパスを出力します。
		 * @param param パラメータ
		 */
		protected void showMetaDataPathList(final MetaDataExportParameter param) {

			instance.logInfo("-----------------------------------------------------------");
			instance.logInfo("+ MetaData List");
			for (String path : param.getExportMetaDataPathList()) {
				instance.logInfo(path);
			}
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
		public WizardParamterFactory(MetaDataExportFromRdbToXml instance) {
			super(instance);
		}

		@Override
		public MetaDataExportParameter create(MetaDataRepositoryeHadler handler) {
			//テナントURL
			Tenant tenant = readTenantRequire(handler);
			MetaDataExportParameter param = new MetaDataExportParameter(tenant.getId(), tenant.getName());


			TenantContext tc = tcs.getTenantContext(param.getTenantId());
			// 一時ディレクトリを設定
			param.setExportDir(getTempDir(MetaDataExportFromRdbToXml.class.getSimpleName()));
			param.setExportDirName(param.getExportDir().getAbsolutePath());

			return ExecuteContext.executeAs(tc, () -> {
				ExecuteContext.getCurrentContext().setLanguage(instance.getLanguage());

				boolean isExportLocalOnly = instance.readConsoleBoolean(
						instance.rs("MetaDataExportFromRdbToXml.Wizard.confirmTargetLocalMetaMsg"), param.isExportLocalMetaDataOnly());
				param.setExportLocalMetaDataOnly(isExportLocalOnly);

				boolean isExportAllMeta = instance.readConsoleBoolean(
						instance.rs("MetaDataExportFromRdbToXml.Wizard.confirmExportAllMetaMsg"), param.isExportAllMetaData());
				param.setExportAllMetaData(isExportAllMeta);

				if (isExportAllMeta) {
					//全メタデータ出力

					//テナントを含めるかを確認
					boolean isExportTenantMetaData = instance.readConsoleBoolean(instance.rs("MetaDataExportFromRdbToXml.Wizard.confirmIncludeTenantMetaMsg"),
							param.isExportTenantMetaData());
					param.setExportTenantMetaData(isExportTenantMetaData);

				} else {
					//個別指定
					String exportMetaDataPathStr = readSpecifiedMetaDataPathRequire();

					param.setExportMetaDataPathStr(exportMetaDataPathStr);
				}

				//Pathの取得
				param.setExportMetaDataPathList(getMetaDataPathList(param));

				showMetaDataPathList(param);

				do {
					//実行情報出力
					logArguments(param, handler);

					if (instance.readConsoleBoolean(instance.rs("MetaDataExportFromRdbToXml.Wizard.confirmExecuteMsg"), false)) {
						break;
					}

					//defaultがfalseなので念のため再度確認
					if (instance.readConsoleBoolean(instance.rs("MetaDataExportFromRdbToXml.Wizard.confirmRetryMsg"), true)) {
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
				String value = instance.readConsole(instance.rs("MetaDataExportFromRdbToXml.Wizard.inputMetaPathMsg"));
				if (StringUtil.isEmpty(value)) {
					//未指定なのでContinue
					instance.logWarn(instance.rs("MetaDataExportFromRdbToXml.Wizard.requiredMetaPathMsg"));
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

		/**
		 * 一時ディレクトリを作成する
		 * @param prefix 一時ディレクトリprefix
		 * @return 一時ディレクトリ
		 */
		private File getTempDir(String prefix) {
			try {
				return Files.createTempDirectory(prefix).toFile();
			} catch (IOException e) {
				throw new RuntimeException(instance.rs("MetaDataExportFromRdbToXml.failCreateTempDir"), e);
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
		public SilentParameterFactory(MetaDataExportFromRdbToXml instance) {
			super(instance);
		}

		@Override
		public MetaDataExportParameter create(MetaDataRepositoryeHadler handler) {
			//プロパティファイルの取得
			String configFileName = System.getProperty(MetaDataExport.KEY_CONFIG_FILE);
			if (StringUtil.isEmpty(configFileName)) {
				throw new RuntimeException(instance.rs("MetaDataExportFromRdbToXml.Silent.requiredConfigFileMsg", MetaDataExport.KEY_CONFIG_FILE));
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
			String propTenantId = prop.getProperty(PROP_TENANT_ID);
			if (StringUtil.isNotEmpty(propTenantId)) {
				tenant = ts.getTenant(Integer.parseInt(propTenantId));
				if (tenant == null) {
					throw new RuntimeException(instance.rs("Common.notExistsTenantIdMsg", propTenantId));
				}
			}
			if (tenant == null) {
				//URL
				String propTenantUrl = prop.getProperty(PROP_TENANT_URL);
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
				throw new RuntimeException(instance.rs("Common.requiredMsg", PROP_TENANT_ID + " or " + PROP_TENANT_URL));
			}

			instance.logInfo("target tenant:[" + tenant.getId() + "]" + tenant.getName());
			// 検証
			handler.validateIfErrorThrow(tenant.getId());

			MetaDataExportParameter param = new MetaDataExportParameter(tenant.getId(), tenant.getName());

			TenantContext tc = tcs.getTenantContext(param.getTenantId());
			return ExecuteContext.executeAs(tc, () -> {
				ExecuteContext.getCurrentContext().setLanguage(instance.getLanguage());

				String exportDirName = prop.getProperty(PROP_EXPORT_DIR);
				if (StringUtil.isNotEmpty(exportDirName)) {
					param.setExportDirName(exportDirName);
				}
				File exportDir = new File(param.getExportDirName());
				param.setExportDir(exportDir);

				String fileName = prop.getProperty(PROP_FILE_NAME);
				if (StringUtil.isNotEmpty(fileName)) {
					param.setFileName(fileName);
				}

				String localOnly = prop.getProperty(PROP_META_LOCAL_ONLY);
				if (StringUtil.isNotEmpty(localOnly)) {
					param.setExportLocalMetaDataOnly(Boolean.valueOf(localOnly));
				}

				String source = prop.getProperty(PROP_META_SOURCE);
				if (StringUtil.isEmpty(source)) {
					//全対象
					param.setExportAllMetaData(true);

					String excludeTenant = prop.getProperty(PROP_META_EXCLUDE_TENANT);
					if (StringUtil.isNotEmpty(excludeTenant)) {
						param.setExportTenantMetaData(!Boolean.valueOf(excludeTenant));
					}

				} else {
					param.setExportAllMetaData(false);
					param.setExportMetaDataPathStr(source);
				}
				param.setExportMetaDataPathList(getMetaDataPathList(param));

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

			throw new FileNotFoundException(instance.rs("MetaDataExportFromRdbToXml.fileResourceNotFound", file));
		}
	}
}
