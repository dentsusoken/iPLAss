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

package org.iplass.mtp.impl.web;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.iplass.mtp.impl.web.fileupload.FileScanner;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.web.actionmapping.definition.ClientCacheType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebFrontendService implements Service {

	private static Logger logger = LoggerFactory.getLogger(WebFrontendService.class);

	private String tempFileDir;
	private String staticContentPath;
	private String defaultContentType;
	private long maxUploadFileSize;
	private ClientCacheType defaultClientCacheType;

	/** iPLAss管理対象外のパスの定義のPattern */
	private Pattern excludePathes;
	/** iPLAss配下で動くが、独自Servletを呼び出すパスのPattern */
	private Pattern throughPathes;
	/** WebAPI(REST)のURL */
	private List<String> restPath;

	/** ログアウト時にキックするURL */
	private String logoutUrl;

	/** エラー画面セレクター */
	private ErrorUrlSelector errorUrlSelector;
	/** Login画面セレクタ */
	private LoginUrlSelector loginUrlSelector;

	/** ContentDisposition設定 */
	private List<ContentDispositionPolicy> contentDispositionPolicies;

	/** ウィルススキャン実行 */
	private FileScanner uploadFileScanner;

	/** ExecMagicByteCheck実施 */
	private boolean isExecMagicByteCheck;

	/** ダイレクトアクセス時のポート */
	private String directAccessPort;

	private int transactionTokenMaxSize = 50;

	private List<String> welcomeAction;

	private boolean redirectAfterLogin = true;

	private boolean tenantAsDomain;
	private String fixedTenant;

	public boolean isExcludePath(String path) {
		return getExcluePathes() != null && getExcluePathes().matcher(path).matches();
	}

	public boolean isThroughPath(String path) {
		return getThroughPathes() != null && getThroughPathes().matcher(path).matches();
	}

	public boolean isRestPath(String path) {
		if (restPath != null) {
			for (String rp: restPath) {
				if (path.startsWith(rp)) {
					return true;
				}
			}
		}

		return false;
	}

	public Pattern getThroughPathes() {
		return throughPathes;
	}
	public String getFixedTenant() {
		return fixedTenant;
	}

	public boolean isTenantAsDomain() {
		return tenantAsDomain;
	}

	public boolean isRedirectAfterLogin() {
		return redirectAfterLogin;
	}

	public List<String> getWelcomeAction() {
		return welcomeAction;
	}

	public int getTransactionTokenMaxSize() {
		return transactionTokenMaxSize;
	}

	public boolean isExecMagicByteCheck() {
		return isExecMagicByteCheck;
	}

	public FileScanner getUploadFileScanner() {
		return uploadFileScanner;
	}

	public LoginUrlSelector getLoginUrlSelector() {
		return loginUrlSelector;
	}

	public ErrorUrlSelector getErrorUrlSelector() {
		return errorUrlSelector;
	}

	public String getTempFileDir() {
		return tempFileDir;
	}

	public String getStaticContentPath() {
		return staticContentPath;
	}

	@Override
	public void destroy() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		tempFileDir = config.getValue("tempFileDir");

		if (tempFileDir != null) {
			File tFile = new File(this.tempFileDir);
			if (!tFile.exists()) {
				logger.debug("tempFileDir not exist:" + tFile.getAbsolutePath());

				try {
					if (tFile.mkdirs()) {
						logger.info("create tempFileDir:" + tFile.getAbsolutePath());
						//権限設定（777）
						tFile.setReadable(true, false);
						tFile.setWritable(true, false);
						tFile.setExecutable(true, false);
					} else {
						throw new ServiceConfigrationException("tempFileDir create failed:" + tFile.getAbsolutePath());
					}
				} catch(SecurityException e) {
					throw new ServiceConfigrationException("tempFileDir create failed:" + tFile.getAbsolutePath());
				}
			} else {
				logger.debug("tempFileDir is existed:" + tFile.getAbsolutePath());
			}
		} else {
			logger.debug("tempFileDir is undefined");
		}

		staticContentPath = config.getValue("staticContentPath");
//		if (staticContentPath == null) {
//			staticContentPath = "";
//		}
//		this.staticContentPath = staticContentPath;

		String defaultContentType = config.getValue("defaultContentType");
		if (defaultContentType == null) {
			defaultContentType = "text/html; charset=utf-8";
		}
		this.defaultContentType = defaultContentType;

		String defaultClientCacheType = config.getValue("defaultClientCacheType");
		if (defaultClientCacheType != null) {
			if (defaultClientCacheType.equals("NO_CACHE")) {
				this.defaultClientCacheType = ClientCacheType.NO_CACHE;
			} else if (defaultClientCacheType.equals("CACHE")) {
				this.defaultClientCacheType = ClientCacheType.CACHE;
			} else {
				throw new ServiceConfigrationException("defaultClientCacheType incorrect:" + defaultClientCacheType);
			}
		}

		String maxUploadFileSize = config.getValue("maxUploadFileSize");
		if (maxUploadFileSize != null) {
			this.maxUploadFileSize = Long.parseLong(maxUploadFileSize);
		} else {
			this.maxUploadFileSize = -1;
		}

		errorUrlSelector = (ErrorUrlSelector) config.getBean("errorUrlSelector");

		loginUrlSelector = (LoginUrlSelector) config.getBean("loginUrlSelector");

		List<String> exclude = config.getValues("excludePath");
		if (exclude != null && exclude.size() > 0) {
			excludePathes = Pattern.compile(String.join("|", exclude));
		}

		List<String> throughPathList = config.getValues("throughPath");
		if (throughPathList != null && throughPathList.size() > 0) {
			throughPathes = Pattern.compile(String.join("|", throughPathList));
		}

		restPath = config.getValues("restPath");

		logoutUrl = config.getValue("logoutUrl");
		if(logoutUrl == null || logoutUrl.length() == 0) {
			throw new ServiceConfigrationException("ログアウトのURLが設定されていません");
		}

		contentDispositionPolicies = (List<ContentDispositionPolicy>) config.getBeans("contentDispositionPolicy");

		uploadFileScanner = (FileScanner) config.getBean("uploadFileScanner");

		isExecMagicByteCheck = Boolean.valueOf(config.getValue("isExecMagicByteCheck"));

		directAccessPort = config.getValue("directAccessPort");

		if (config.getValue("transactionTokenMaxSize") != null) {
			transactionTokenMaxSize = Integer.parseInt(config.getValue("transactionTokenMaxSize"));
		}

		welcomeAction = config.getValues("welcomeAction");

		if (config.getValue("redirectAfterLogin") != null) {
			redirectAfterLogin = Boolean.valueOf(config.getValue("redirectAfterLogin"));
		}

		if (config.getValue("tenantAsDomain") != null) {
			tenantAsDomain = Boolean.valueOf(config.getValue("tenantAsDomain"));
		}

		fixedTenant = config.getValue("fixedTenant");
	}

	public String getDefaultContentType() {
		return defaultContentType;
	}

	public ClientCacheType getDefaultClientCacheType() {
		return defaultClientCacheType;
	}

	public long getMaxUploadFileSize() {
		return maxUploadFileSize;
	}

	/**
	 * テナント、Auth、Dispatchで除外するURLの定義を取得します。
	 * @return テナント、Auth、Dispatchで除外するURLの定義
	 */
	public Pattern getExcluePathes() {
	    return excludePathes;
	}

	/**
	 * ログアウト時にキックするURLを取得します。
	 * @return ログアウト時にキックするURL
	 */
	public String getLogoutUrl() {
	    return logoutUrl;
	}

	public List<String> getRestPath() {
		return restPath;
	}

	public List<ContentDispositionPolicy> getContentDispositionPolicy() {
		return contentDispositionPolicies;
	}

	public String getDirectAccessPort() {
		return directAccessPort;
	}

}
