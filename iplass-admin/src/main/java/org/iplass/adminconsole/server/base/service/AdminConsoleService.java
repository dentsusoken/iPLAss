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

package org.iplass.adminconsole.server.base.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.iplass.adminconsole.server.base.service.screen.ScreenModuleBasedClassFactoryGenerator;
import org.iplass.adminconsole.server.base.service.screen.ScreenModuleBasedClassFactoryHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.tenant.Tenant;

public class AdminConsoleService implements Service {

	/** デフォルトのLOG_HOME（service-config, web.xmlで指定されていない場合に利用） */
	private static final String DEFAULT_LOG_HOME = "/logs/";
	/** Log設定に指定可能なテナントIDに置換する文字列 */
	private static final String LOG_TENANT_ID_FILTER_KEY = "${tenantId}";
	/** Log設定に指定可能なテナント名に置換する文字列 */
	private static final String LOG_TENANT_NAME_FILTER_KEY = "${tenantName}";
	/** Log設定に指定可能なテナントIDに置換する文字パターン */
	private static final Pattern logTenantIdPattern = Pattern.compile(LOG_TENANT_ID_FILTER_KEY, Pattern.LITERAL);
	/** Log設定に指定可能なテナント名に置換する文字パターン */
	private static final Pattern logTenantNamePattern = Pattern.compile(LOG_TENANT_NAME_FILTER_KEY, Pattern.LITERAL);

	/** ファイルアップロード時のデフォルトMAXファイルサイズ(Byte) */
	private static final long DEFAULT_MAX_FILE_SIZE = 1024 * 1024 * 1024;

	/** GWTのリソース取得用URLのPrefix */
	private String resourcePrefixPath;

	/** サーバ情報を出力するか */
	private boolean showServerInfo;

	/** ログ設定情報 */
	private LogConfig logDownloadConfig;

	/** 画面モジュールに依存した実装クラスを生成するFactoryのGenerator */
	private ScreenModuleBasedClassFactoryGenerator screenBasedFactoryGenerator;

	/** ファイルアップロード時のMAXファイルサイズ(Byte) */
	private long maxUploadFileSize;

	@Override
	public void init(Config config) {
		resourcePrefixPath = config.getValue("resourcePrefixPath", String.class, "");
		showServerInfo = config.getValue("showServerInfo", Boolean.class, false);
		logDownloadConfig = config.getValue("logDownloadConfig", LogConfig.class);
		maxUploadFileSize = config.getValue("maxUploadFileSize", Long.class, DEFAULT_MAX_FILE_SIZE);
		screenBasedFactoryGenerator = config.getValue("screenBasedFactoryGenerator",
				ScreenModuleBasedClassFactoryGenerator.class);
		ScreenModuleBasedClassFactoryHolder.init(screenBasedFactoryGenerator.generate());
	}

	@Override
	public void destroy() {

	}

	/**
	 * GWTのリソース取得用URLのPrefixを返します。
	 *
	 * @return GWTのリソース取得用URLのPrefix
	 */
	public String getResourcePrefixPath() {
		return resourcePrefixPath;
	}

	/**
	 * サーバ情報を出力するかを返します。
	 *
	 * @return true:サーバ情報を出力
	 */
	public boolean isShowServerInfo() {
		return showServerInfo;
	}

	/**
	 * ログファイルのダウンロードを許可するかを返します。
	 *
	 * @return true:ダウンロードを許可
	 */
	public boolean isLogDownloadEnabled() {
		return getLogDownloadConfig().isEnabled();
	}

	/**
	 * テナントでダウンロード可能なログファイルのパスを返します。
	 * service-configで指定していない場合は、引数で渡されたパス(web.xml指定を想定)を返します。
	 * テナントID、テナント名はログイン情報で置き換えます。 引数でも未指定の場合は、内部で定義されたデフォルトのパスを返します。
	 *
	 * @param webSpecifiedPath web.xmlで指定された場合のパス
	 * @return ダウンロード可能なログファイルのパス
	 */
	public List<String> getTenantLogHomes(String webSpecifiedPath) {
		List<String> logHomes = getLogDownloadConfig().getLogHome();
		if (logHomes == null || logHomes.isEmpty()) {
			String initPath = convLogTenantPath(webSpecifiedPath != null ? webSpecifiedPath : DEFAULT_LOG_HOME);
			// 最後に/を追加
			if (!initPath.endsWith("/")) {
				initPath += "/";
			}
			return Arrays.asList(initPath);
		}

		return logHomes.stream().map(logHome -> {
			String path = convLogTenantPath(logHome);
			// 最後に/を追加
			if (!path.endsWith("/")) {
				path += "/";
			}
			return path;
		}).collect(Collectors.toList());
	}

	/**
	 * テナントでダウンロード可能なログファイル名に対するフィルターを返します。 テナントID、テナント名はログイン情報で置き換えます。
	 *
	 * @return ダウンロード可能なログファイル名に対するフィルター
	 */
	public List<String> getTenantLogFileFilters() {
		List<String> filters = getLogDownloadConfig().getFileFilter();
		if (filters == null || filters.isEmpty()) {
			return Collections.emptyList();
		}

		return filters.stream().map(filter -> {
			return convLogTenantPath(filter);
		}).collect(Collectors.toList());
	}

	private String convLogTenantPath(String path) {
		if (path == null) {
			return null;
		}
		String ret = path;
		if (path.contains(LOG_TENANT_ID_FILTER_KEY)) {
			Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			ret = logTenantIdPattern.matcher(path).replaceAll(String.valueOf(tenant.getId()));
		}
		if (path.contains(LOG_TENANT_NAME_FILTER_KEY)) {
			Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			ret = logTenantNamePattern.matcher(path).replaceAll(String.valueOf(tenant.getName()));
		}
		return ret;
	}

	private LogConfig getLogDownloadConfig() {
		return logDownloadConfig;
	}

	public long getMaxUploadFileSize() {
		return maxUploadFileSize;
	}

	public ScreenModuleBasedClassFactoryGenerator getScreenBasedFactoryGenerator() {
		return screenBasedFactoryGenerator;
	}

}
