/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.tenant;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.builtin.BuiltinAuthenticationProvider;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.tenant.MetaTenant;
import org.iplass.mtp.impl.tenant.MetaTenantService;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.tenant.create.TenantCreateProcess;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.impl.tools.tenant.rdb.TenantRdbManager;
import org.iplass.mtp.impl.tools.tenant.rdb.TenantRdbManagerFactory;
import org.iplass.mtp.impl.tools.tenant.rdb.TenantRdbManagerParameter;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.tenant.TenantMailInfo;
import org.iplass.mtp.tenant.gem.TenantGemInfo;
import org.iplass.mtp.tenant.web.TenantWebInfo;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantToolService implements Service {

	private static Logger logger = LoggerFactory.getLogger(TenantToolService.class);

	/** テナント作成処理 */
	private List<TenantCreateProcess> createProcesses;

	private TenantContextService tenantContextService;
	private TenantService tenantService;
	private MetaTenantService metaTenantService;
	private AuthService authService;

	// メタ設定(mtp-core-metadata.xml)のmetaTenantI18NInfoに合わせること
	private String defaultEnableLanguages = "ja,en,zh-CN,zh-TW,th";

	private TenantRdbManager rdbManager;

	@Override
	public void init(Config config) {
		tenantContextService = config.getDependentService(TenantContextService.class);
		tenantService = config.getDependentService(TenantService.class);
		metaTenantService = config.getDependentService(MetaTenantService.class);
		authService = config.getDependentService(AuthService.class);

		RdbAdapter rdbAdapter = config.getDependentService(RdbAdapterService.class).getRdbAdapter();

		createProcesses = config.getValues("createProcesses", TenantCreateProcess.class);

		// TenantRdbManager のパラメータを取得
		TenantRdbManagerParameter parameter = config.getValue("tenantRdbManagerParameter", TenantRdbManagerParameter.class);
		// TenantRdbManager インスタンス生成
		rdbManager = new TenantRdbManagerFactory().createManager(rdbAdapter, parameter);
	}

	@Override
	public void destroy() {
	}

	public List<TenantInfo> getValidTenantInfoList() {
		return Transaction.required(t -> {
				return rdbManager.getValidTenantInfoList();
		});

	}

	public List<TenantInfo> getAllTenantInfoList() {
		return Transaction.required(t -> {
				return rdbManager.getAllTenantInfoList();
		});
	}

	public boolean existsURL(final String url) {
		return Transaction.required(t -> {
				return rdbManager.existsURL(url);
		});
	}

	public TenantInfo getTenantInfo(final String url) {
		return Transaction.required(t -> {
				return rdbManager.getTenantInfo(url);
		});
	}

	public String getDefaultEnableLanguages() {
		return defaultEnableLanguages;
	}

	/**
	 * テナントを作成します。
	 *
	 * @param param 作成パラメータ
	 * @return
	 */
	public boolean create(final TenantCreateParameter param) {
		return create(param, new LogHandler() {});
	}

	/**
	 * テナントを作成します。
	 *
	 * @param param 作成パラメータ
	 * @param logHandler ログハンドラー
	 * @return
	 */
	public boolean create(final TenantCreateParameter param, final LogHandler logHandler) {

		LogHandler wrapLogger = new WrappedLogger(logHandler);

		boolean isSuccess = Transaction.requiresNew(t -> {

			//TenantDB情報の作成
			final Tenant tenant = createTenantData(param, wrapLogger);

			//Partitionの作成
			boolean ret = createPartition(param, tenant, wrapLogger);
			if (!ret) {
				return false;
			}

			TenantContext tContext = tenantContextService.getTenantContext(tenant.getId());

			ExecuteContext.executeAs(tContext, () -> {
				//TenantMeta情報の更新
				updateMetaTenantData(param, tenant, wrapLogger);
				return null;
			});

			tContext = tenantContextService.getTenantContext(tenant.getId());

			ExecuteContext.executeAs(tContext, () -> {

				for (TenantCreateProcess process : createProcesses) {
					logger.debug("execute " + process.getClass().getSimpleName());
					if (!process.execute(param, logHandler)) {
						break;
					}
				};

				return null;
			});

			return true;
		});

		return isSuccess;
	}

	/**
	 * テナント情報（DB）を作成します。
	 */
	private Tenant createTenantData(final TenantCreateParameter param, LogHandler logHandler) {

		//存在チェック
		if (rdbManager.existsURL(param.getTenantUrl())) {
			throw new IllegalArgumentException(ToolsResourceBundleUtil.resourceString(param.getLoggerLanguage(), "tenant.create.existsURLMsg", param.getTenantUrl()));
		}

		Tenant tenant = new Tenant();
		tenant.setName(param.getTenantName());
		tenant.setUrl(param.getTenantUrl());
		tenant.setDescription(param.getTenantDisplayName());
		tenant.setFrom(InternalDateUtil.getNowForSqlDate());
		tenant.setTo(InternalDateUtil.getYukoDateTo());

		tenantService.registTenant(tenant, param.getRegistId());

		// 登録したデータの検索(ID取得)
		tenant = tenantService.getTenant(tenant.getUrl());

		logHandler.info(ToolsResourceBundleUtil.resourceString(param.getLoggerLanguage(), "tenant.create.createdTenantInfoMsg", tenant.getId()));

		return tenant;
	}

	/**
	 * DB上にPartitionを作成します。
	 */
	private boolean createPartition(final TenantCreateParameter param, final Tenant tenant, LogHandler logHandler) {

		if (!rdbManager.isSupportPartition()) {
			return true;
		}

		PartitionCreateParameter partitionParam = new PartitionCreateParameter();
		partitionParam.setTenantId(tenant.getId());
		partitionParam.setSubPartitionSize(param.getSubPartitionSize());
		partitionParam.setOnlyPartitionCreate(false);
		partitionParam.setLoggerLanguage(param.getLoggerLanguage());

		return createPartition(partitionParam, logHandler);
	}

	/**
	 * テナントMeta情報を更新します。
	 */
	private void updateMetaTenantData(final TenantCreateParameter param, final Tenant tenant, LogHandler logHandler) {
		boolean metaUpdate = false;

		if (StringUtil.isNotBlank(param.getTenantDisplayName())) {
			tenant.setDisplayName(param.getTenantDisplayName());
			metaUpdate = true;
		}

		if (StringUtil.isNotBlank(param.getTopUrl())) {
			setDefaultTenantWebInfo(param, tenant);
			metaUpdate = true;
		}

		if (metaUpdate || !defaultEnableLanguages.equals(param.getUseLanguages())) {
			setDefaultTenantI18nInfo(param, tenant);
			metaUpdate = true;
		}

		if (metaUpdate) {
			MetaTenant metaTenant = new MetaTenant();
			metaTenant.applyConfig(tenant);

			metaTenantService.updateMetaData(metaTenant);

			logHandler.info(ToolsResourceBundleUtil.resourceString(param.getLoggerLanguage(), "tenant.create.updatedTenantMetaMsg", tenant.getId()));
		}
	}

	/**
	 * テナントMeta情報にデフォルト値を設定します。
	 */
	private void setDefaultTenantInfo(final TenantCreateParameter param, final Tenant tenant) {

		TenantAuthInfo authInfo = new TenantAuthInfo();
		tenant.setTenantConfig(authInfo);

		TenantMailInfo mailInfo = new TenantMailInfo();
		mailInfo.setSendMailEnable(false);
		tenant.setTenantConfig(mailInfo);

		TenantI18nInfo i18Info = new TenantI18nInfo();
		i18Info.setUseMultilingual(true);
		if (param.getUseLanguages() != null && !param.getUseLanguages().isEmpty()) {
			String[] langArray = param.getUseLanguages().split(",");
			List<String> useLanguageList = new ArrayList<>(langArray.length);
			for (String lang : langArray) {
				useLanguageList.add(lang);
			}
			i18Info.setUseLanguageList(useLanguageList);
		}
		tenant.setTenantConfig(i18Info);

		TenantWebInfo webInfo = new TenantWebInfo();
		webInfo.setHomeUrl(param.getTopUrl());
		tenant.setTenantConfig(webInfo);

		TenantGemInfo gemInfo = new TenantGemInfo();
		gemInfo.setIconUrl(param.getIconUrl());
		tenant.setTenantConfig(gemInfo);

	}

	/**
	 * テナントMeta情報に多言語のデフォルト値を設定します。
	 */
	private void setDefaultTenantI18nInfo(final TenantCreateParameter param, final Tenant tenant) {
		TenantI18nInfo i18Info = new TenantI18nInfo();
		if (param.getUseLanguages() != null && !param.getUseLanguages().isEmpty()) {
			String[] langArray = param.getUseLanguages().split(",");
			List<String> useLanguageList = new ArrayList<>(langArray.length);
			for (String lang : langArray) {
				useLanguageList.add(lang);
			}
			i18Info.setUseLanguageList(useLanguageList);
			i18Info.setUseMultilingual(true);
		}
		tenant.setTenantConfig(i18Info);
	}

	/**
	 * テナントMeta情報にWebのデフォルト値を設定します。
	 */
	private void setDefaultTenantWebInfo(final TenantCreateParameter param, final Tenant tenant) {
		TenantWebInfo webInfo = new TenantWebInfo();
		webInfo.setHomeUrl(param.getTopUrl());
		tenant.setTenantConfig(webInfo);
	}

	/**
	 * テナントを削除します。
	 *
	 * @param param 削除パラメータ
	 * @return
	 */
	public boolean remove(final TenantDeleteParameter param) {
		return remove(param, new LogHandler() {});
	}

	/**
	 * テナントを削除します。
	 *
	 * @param param 削除パラメータ
	 * @param logHandler ログリスナー
	 * @return
	 */
	public boolean remove(final TenantDeleteParameter param, final LogHandler logHandler) {

		LogHandler wrapLogger = new WrappedLogger(logHandler);

		boolean isSuccess = Transaction.requiresNew(t -> {

				boolean deleteAccount = false;
				AuthenticationProvider provider = authService.getAuthenticationProvider();
				if (provider instanceof BuiltinAuthenticationProvider) {
					deleteAccount = true;
				}

				//テナントの削除
				rdbManager.deleteTenant(param, deleteAccount, wrapLogger);

				return true;
		});

		return isSuccess;
	}

	public List<PartitionInfo> getPartitionInfo() {

		if (rdbManager.isSupportPartition()) {

			return Transaction.required(t -> {
					return rdbManager.getPartitionInfo();
			});
		}

		return null;
	}

	public boolean createPartition(final PartitionCreateParameter param, LogHandler logHandler) {

		if (rdbManager.isSupportPartition()) {
			return Transaction.required(t -> {
					return rdbManager.createPartition(param, logHandler);
			});
		}

		return false;
	}

	private static class WrappedLogger implements LogHandler {

		private LogHandler logHandler;

		public WrappedLogger(LogHandler logHandler) {
			this.logHandler = logHandler;
		}

		@Override
		public void debug(String message) {
			logger.debug(message);
			logHandler.debug(message);
		}
		@Override
		public void debug(String message, Throwable e) {
			logger.debug(message, e);
			logHandler.debug(message, e);
		}
		@Override
		public void info(String message) {
			logger.info(message);
			logHandler.info(message);
		}
		@Override
		public void info(String message, Throwable e) {
			logger.info(message, e);
			logHandler.info(message, e);
		}
		@Override
		public void warn(String message) {
			logger.warn(message);
			logHandler.warn(message);
		}
		@Override
		public void warn(String message, Throwable e) {
			logger.warn(message, e);
			logHandler.warn(message, e);
		}
		@Override
		public void error(String message) {
			logger.error(message);
			logHandler.error(message);
		}
		@Override
		public void error(String message, Throwable e) {
			logger.error(message, e);
			logHandler.error(message, e);
		}
	}

}
