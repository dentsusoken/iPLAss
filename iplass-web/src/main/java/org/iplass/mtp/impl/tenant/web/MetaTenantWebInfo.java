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

package org.iplass.mtp.impl.tenant.web;

import org.codehaus.groovy.GroovyBugError;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.tenant.MetaTenantConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.web.TenantWebInfo;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * テナント認証情報のメタデータ
 *
 * @author 藤田 義弘
 *
 */
public class MetaTenantWebInfo extends MetaTenantConfig<TenantWebInfo> {

	private static final long serialVersionUID = -8722898685664541128L;

	private static final Logger logger = LoggerFactory.getLogger(MetaTenantWebInfo.class);

	/** プレビュー機能を有効にするか */
	private boolean usePreview;

	/** ログインURLセレクター */
	private String loginUrlSelector;

	/** 再認証URLセレクター */
	private String reAuthUrlSelector;

	/** エラーURLセレクター（Template名） */
	private String errorUrlSelector;

	/** HOMEのURL */
	private String homeUrl;

	/** リクエストパス構築用のテナントURL。
	 * (HTTPサーバにて、/をtenantへマッピングしている場合などの場合に利用)
	 */
	private String urlForRequest;

	public MetaTenantWebInfo() {
	}

	public boolean isUsePreview() {
		return usePreview;
	}

	public void setUsePreview(boolean usePreview) {
		this.usePreview = usePreview;
	}

	public String getLoginUrlSelector() {
		return loginUrlSelector;
	}

	public void setLoginUrlSelector(String loginUrlSelector) {
		this.loginUrlSelector = loginUrlSelector;
	}

	public String getReAuthUrlSelector() {
		return reAuthUrlSelector;
	}

	public void setReAuthUrlSelector(String reAuthUrlSelector) {
		this.reAuthUrlSelector = reAuthUrlSelector;
	}

	public String getErrorUrlSelector() {
		return errorUrlSelector;
	}

	public void setErrorUrlSelector(String errorUrlSelector) {
		this.errorUrlSelector = errorUrlSelector;
	}

	/**
	 * HOMEのURLを設定します。
	 *
	 * @param HOMEのURL
	 */
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

	/**
	 * HOMEのURLを取得します。
	 *
	 * @return HOMEのURL
	 */
	public String getHomeUrl() {
		return homeUrl;
	}

	/**
	 * リクエストパス構築用のテナントURLを取得します。
	 * (HTTPサーバにて、/をtenantへマッピングしている場合などの場合に利用)
	 * @return リクエストパス構築用のテナントURL
	 */
	public String getUrlForRequest() {
		return urlForRequest;
	}

	/**
	 * リクエストパス構築用のテナントURLを設定します。。
	 * (HTTPサーバにて、/をtenantへマッピングしている場合などの場合に利用)
	 * @param urlForRequest リクエストパス構築用のテナントURL
	 */
	public void setUrlForRequest(String urlForRequest) {
		this.urlForRequest = urlForRequest;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TenantWebInfo definition) {
		setUsePreview(definition.isUsePreview());
		setLoginUrlSelector(definition.getLoginUrlSelector());
		setReAuthUrlSelector(definition.getReAuthUrlSelector());
		setErrorUrlSelector(definition.getErrorUrlSelector());
		setHomeUrl(definition.getHomeUrl());
		setUrlForRequest(definition.getUrlForRequest());
	}

	@Override
	public TenantWebInfo currentConfig() {
		TenantWebInfo definition = new TenantWebInfo();
		definition.setUsePreview(usePreview);
		definition.setLoginUrlSelector(loginUrlSelector);
		definition.setReAuthUrlSelector(reAuthUrlSelector);
		definition.setErrorUrlSelector(errorUrlSelector);
		definition.setHomeUrl(getHomeUrl());
		definition.setUrlForRequest(getUrlForRequest());
		return definition;
	}

	@Override
	public MetaTenantWebInfoRuntime createRuntime(MetaTenantHandler tenantRuntime) {
		return new MetaTenantWebInfoRuntime(tenantRuntime);
	}

	public class MetaTenantWebInfoRuntime extends MetaTenantConfigRuntime {

		private static final String SCRIPT_PREFIX_LOGIN_URL_SELECTOR = "MetaTenantHandler_loginUrlSelector";
		private static final String SCRIPT_PREFIX_REAUTH_URL_SELECTOR = "MetaTenantHandler_reAuthUrlSelector";
		private static final String SCRIPT_PREFIX_ERROR_URL_SELECTOR = "MetaTenantHandler_errorUrlSelector";

		private Script loginUrlSelectorScript;
		private Script reAuthUrlSelectorScript;
		private Script errorUrlSelectorScript;

		public MetaTenantWebInfoRuntime(MetaTenantHandler tenantRuntime) {

			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			ScriptEngine scriptEngine = tc.getScriptEngine();

			try {
				// ログインURLセレクター
				if (!StringUtil.isEmpty(loginUrlSelector)) {
					loginUrlSelectorScript = scriptEngine.createScript(loginUrlSelector,
							SCRIPT_PREFIX_LOGIN_URL_SELECTOR + "_" + tenantRuntime.getMetaData().getId());
				}
				// 再認証URLセレクター
				if (!StringUtil.isEmpty(reAuthUrlSelector)) {
					reAuthUrlSelectorScript = scriptEngine.createScript(reAuthUrlSelector,
							SCRIPT_PREFIX_REAUTH_URL_SELECTOR + "_" + tenantRuntime.getMetaData().getId());
				}

				if (!StringUtil.isEmpty(errorUrlSelector)) {
					errorUrlSelectorScript = scriptEngine.createScript(errorUrlSelector,
							SCRIPT_PREFIX_ERROR_URL_SELECTOR + "_" + tenantRuntime.getMetaData().getId());
				}

			} catch (GroovyBugError | NoClassDefFoundError e) {
				setIllegalStateException(new RuntimeException(e));
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		@Override
		public MetaData getMetaData() {
			return MetaTenantWebInfo.this;
		}

		@Override
		public void applyMetaDataToTenant(Tenant tenant) {
		}

		public String loginUrlSelector(RequestContext context, String path) {
			if (loginUrlSelectorScript == null) {
				return null;
			} else {
				ScriptContext sc = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine().newScriptContext();
				sc.setAttribute("request", context);
				sc.setAttribute("path", path);

				Object res = null;
				try {
					res = loginUrlSelectorScript.eval(sc);
				} catch (RuntimeException e) {
					logger.error("fail on LoginUrlSelector: " + e.getMessage(), e);
				}

				if (res == null) {
					return null;
				} else {
					return res.toString();
				}
			}
		}

		public String reAuthUrlSelector(RequestContext context, String path) {
			if (reAuthUrlSelectorScript == null) {
				return null;
			} else {
				ScriptContext sc = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine().newScriptContext();
				sc.setAttribute("request", context);
				sc.setAttribute("path", path);

				Object res = null;
				try {
					res = reAuthUrlSelectorScript.eval(sc);
				} catch (RuntimeException e) {
					logger.error("fail on ReAuthUrlSelector: " + e.getMessage(), e);
				}

				if (res == null) {
					return null;
				} else {
					return res.toString();
				}
			}
		}

		public String errorUrlSelector(Throwable exp, RequestContext context, String path) {
			if (errorUrlSelectorScript == null) {
				return null;
			} else {
				ScriptContext sc = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine().newScriptContext();
				sc.setAttribute("request", context);
				sc.setAttribute("path", path);
				sc.setAttribute("exception", exp);

				Object res = null;
				try {
					res = errorUrlSelectorScript.eval(sc);
				} catch (RuntimeException e) {
					logger.error("fail on ErrorUrlSelectorScript: " + e.getMessage(), e);
				}

				if (res == null) {
					return null;
				} else {
					return res.toString();
				}
			}
		}

	}

}
