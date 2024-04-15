/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.rpc.tenant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.AdminConsoleService;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAction;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAuditLogger;
import org.iplass.adminconsole.server.metadata.rpc.MetaDataVersionCheckUtil;
import org.iplass.adminconsole.shared.base.dto.auth.TenantNotFoundException;
import org.iplass.adminconsole.shared.base.dto.tenant.AdminPlatformInfo;
import org.iplass.adminconsole.shared.base.dto.tenant.TenantEnv;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantService;
import org.iplass.adminconsole.shared.metadata.dto.tenant.TenantInfo;
import org.iplass.gem.GemConfigService;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.DefinitionManager;
import org.iplass.mtp.impl.command.RequestContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.core.config.ServerEnv;
import org.iplass.mtp.impl.i18n.EnableLanguages;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.util.PlatformUtil;
import org.iplass.mtp.impl.util.PlatformUtil.PlatformInfo;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantManager;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;

/**
 * テナント用Service実装クラス
 */
public class TenantServiceImpl extends XsrfProtectedServiceServlet implements TenantService {

	private static final Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);

	private static final long serialVersionUID = -5127640151715444338L;

	private static final String RESOUCE_NOTICE = "/META-INF/NOTICE";
	private static final String RESOUCE_LICENSE = "/META-INF/LICENSE";

	private TenantManager tm = ManagerLocator.manager(TenantManager.class);
	private DefinitionManager dm = ManagerLocator.manager(DefinitionManager.class);

	private AdminConsoleService acs = ServiceRegistry.getRegistry().getService(AdminConsoleService.class);
	private TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);

	private MetaDataAuditLogger auditLogger = MetaDataAuditLogger.getLogger();

	private Map<String, String> timezoneMap;

	@Override
	public void init() throws ServletException {
		super.init();

		//Client側で利用するTimezone情報のMapを生成する
		Map<String, String> timezones = new HashMap<>();
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			is = this.getClass().getResourceAsStream("/TimeZoneConstants.properties");
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			for (String s; (s = br.readLine()) != null;) {
				// using a regex to grab the id to use as a key to the hashmap
				// a full json parser here would be overkill
				//TODO namesでもマッチさせるか
				Pattern pattern = Pattern.compile("^[A-Za-z]+ = (.*\"id\": \"([A-Za-z_/]+)\".*)$");
				Matcher matcher = pattern.matcher(s);
				if (matcher.matches()) {
					String id = matcher.group(2);
					String json = matcher.group(1);
					if (!timezones.containsKey(id)) {
						timezones.put(id, json);
					}
				}
			}
			timezoneMap = timezones;

		} catch (IOException e) {
			logger.error("failed to read timezone properties.");
			throw new ServletException("failed to read timezone properties.", e);
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					logger.error("failed to read timezone properties.");
					throw new ServletException("failed to read timezone properties.", e);
				} finally {
					if (isr != null) {
						try {
							isr.close();
							isr = null;
						} catch (IOException e) {
							logger.error("failed to read timezone properties.");
							throw new ServletException("failed to read timezone properties.", e);
						} finally {
							if (is != null) {
								try {
									is.close();
									is = null;
								} catch (IOException e) {
									logger.error("failed to read timezone properties.");
									throw new ServletException("failed to read timezone properties.", e);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Tenant getTenant(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Tenant>() {
			@Override
			public Tenant call() {
				return tm.getTenant();
				//下の方式だとTenant更新時に更新日時が変更されないので続けて更新時に排他エラーになる
				//return ExecuteContext.getCurrentContext().getTenantContext().loadTenantInfo();
			}
		});
	}

	@Override
	public boolean updateTenant(int tenantId, final Tenant tenant, final int currentVersion, final boolean checkVersion) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Boolean>() {
			@Override
			public Boolean call() {

				// バージョンの最新チェック
				MetaDataVersionCheckUtil.versionCheck(checkVersion, tenant.getClass(), tenant.getName(), currentVersion);

				auditLogger.logMetadata(MetaDataAction.UPDATE, Tenant.class.getName(), "name:" + tenant.getName());
				tm.updateTenant(tenant, false);
				return true;
			}
		});
	}

	@Override
	public boolean updateTenant(int tenantId, final Tenant tenant, final int currentVersion, final boolean checkVersion, final boolean forceUpdate) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Boolean>() {
			@Override
			public Boolean call() {

				// バージョンの最新チェック
				MetaDataVersionCheckUtil.versionCheck(checkVersion, tenant.getClass(), tenant.getName(), currentVersion);

				auditLogger.logMetadata(MetaDataAction.UPDATE, Tenant.class.getName(), "name:" + tenant.getName());
				tm.updateTenant(tenant, forceUpdate);
				return true;
			}
		});
	}

	@Override
	public AdminPlatformInfo getPlatformInformation(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AdminPlatformInfo>() {
			@Override
			public AdminPlatformInfo call() {
				AdminPlatformInfo ret = new AdminPlatformInfo();

				PlatformInfo info = PlatformUtil.getPlatformInformation();

				//PlatformInfo -> AdminPlatformInfo
				if (info.isError()) {
					ret.setPlatformErrorMessage(info.getErrorMessage());
				} else {
					for (Entry<String, String> entry : info.getInfomations().entrySet()) {
						if (entry.getKey().equals(PlatformUtil.BUILD_KEY)) {
							//作成日付はLocaleに合わせてFormat
							String strBuildDate = entry.getValue();
							try {
								DateFormat inFormatter = DateUtil.getSimpleDateFormat("yyyy/MM/dd HH:mm:ss", true);
								Date buildDate = inFormatter.parse(strBuildDate);
								DateFormat outFormatter = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true);

								ret.addPlatformInfomation(entry.getKey(), outFormatter.format(buildDate));
							} catch (ParseException e) {
								ret.addPlatformInfomation(entry.getKey(), strBuildDate);
							}
						} else {
							ret.addPlatformInfomation(entry.getKey(), entry.getValue());
						}
					}
				}

				if (acs.isShowServerInfo()) {
					ret.setShowServerInfo(true);
					try {
						//Server情報の取得
						String serverName = ServerEnv.getInstance().getServerId();
						ret.addSeverInfomation("Connect Server", serverName);
					} catch (ServiceConfigrationException e) {
						//エラー時はエラーメッセージをセット(バージョンは取得できているのでエラーにしない)
						ret.addSeverInfomation("Connect Server", e.getMessage());
					}

					Locale locale = Locale.getDefault();
					ret.addSeverInfomation("Server Locale", locale.toString());
					TimeZone timeZone = TimeZone.getDefault();
					ret.addSeverInfomation("Server TimeZone", timeZone.getID() + " (" + timeZone.getDisplayName(false, TimeZone.LONG, locale) + ")");
				} else {
					ret.setShowServerInfo(false);
				}

				List<String> noticeLines = getResourceLines(getServletContext(), RESOUCE_NOTICE);
				if (noticeLines.isEmpty()) {
					noticeLines.add("not found notice resource.");
				}
				ret.setNoticeLines(noticeLines);

				List<String> licenseLines = getResourceLines(getServletContext(), RESOUCE_LICENSE);
				if (licenseLines.isEmpty()) {
					licenseLines.add("not found license resource.");
				}
				ret.setLicenseLines(licenseLines);

				return ret;
			}

		});
	}

	private List<String> getResourceLines(ServletContext sc, String path) {

		URL resource;
		try {
			resource = sc.getResource(path);
		} catch (MalformedURLException e) {
			throw new ApplicationException(e);
		}

		List<String> lines = new ArrayList<>();

		if (resource != null) {
			try (InputStreamReader isr = new InputStreamReader(resource.openStream(), "UTF8");
					BufferedReader br = new BufferedReader(isr)) {
				String line = br.readLine();
				while (line != null){
					lines.add(line);
					line = br.readLine();
				}
			} catch (IOException e) {
				throw new ApplicationException(e);
			}
		}

		return lines;
	}

	@Override
	public TenantEnv getTenantEnv(int tenantId) {
		TenantContext tc = tcs.getTenantContext(tenantId);
		if (tc != null) {
			return createTenantEnv(tc);
		} else {
			logger.error(rs("TenantServiceImpl.canNotGetTenantInfoId", tenantId));
			throw new TenantNotFoundException(rs("TenantServiceImpl.canNotGetTenantInfo"));
		}
	}

	private TenantEnv createTenantEnv(TenantContext tc) {

		final TenantEnv env = new TenantEnv();
		env.setTenant(tc.loadTenantInfo());

		try {
			ExecuteContext ec = new ExecuteContext(tc);
			ExecuteContext.initContext(ec);

			Locale tenantLocale = TemplateUtil.getLocale();
			env.setTenantLocale(I18nUtil.getLocaleString(tenantLocale));

			//テナントのTimeZoneに該当するTimeZone情報を取得
			TimeZone tenantTimeZone = TemplateUtil.getTimeZone();
			if (timezoneMap.containsKey(tenantTimeZone.getID())) {
				env.setTenantTimeZoneInfo(timezoneMap.get(tenantTimeZone.getID()));
			} else {
				//IDがマッチしない場合はoffsetで求める（この場合サマータイムに対応できない）
				int offsetMinutes = (tenantTimeZone.getRawOffset() / (1000 * 60)) * -1;	//分に変換してマイナス
				if (tenantTimeZone.useDaylightTime()) {
					logger.warn("not found timezone matched data. id=" + tenantTimeZone.getID() + ". not support daylight time.");
				}
				logger.debug("not found timezone matched data. id=" + tenantTimeZone.getID() + ". use offset pattern. offset(min)=" + offsetMinutes);
				env.setTenantTimeZoneOffsetInMinutes(offsetMinutes);
			}

			//デフォルトのTimeZone情報を取得(Date、Timeの変換用)
			TimeZone defaultTimeZone = TimeZone.getDefault();
			if (timezoneMap.containsKey(defaultTimeZone.getID())) {
				env.setServerTimeZoneInfo(timezoneMap.get(defaultTimeZone.getID()));
			} else {
				//IDがマッチしない場合はoffsetで求める（この場合サマータイムに対応できない）
				int offsetMinutes = (defaultTimeZone.getRawOffset() / (1000 * 60)) * -1;	//分に変換してマイナス
				if (defaultTimeZone.useDaylightTime()) {
					logger.warn("not found timezone matched data. id=" + defaultTimeZone.getID() + ". not support daylight time.");
				}
				logger.debug("not found timezone matched data. id=" + defaultTimeZone.getID() + ". use offset pattern. offset(min)=" + offsetMinutes);
				env.setServerTimeZoneOffsetInMinutes(offsetMinutes);
			}

			//Inputは3種類あるが、とりあえずja_JPかen_USに分類（他はヨーロッパ系）
			String inputLocale = env.getTenantLocale();
			if (inputLocale.equalsIgnoreCase("ja")){
				//localeがjaの場合はja_JPに変更
				inputLocale = "ja_JP";
			}
			if (!inputLocale.equalsIgnoreCase("ja_JP")){
				//ja_JP以外の場合はen_US
				inputLocale = "en_US";
			}
			env.setInputLocale(inputLocale);

			env.setOutputDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat());
			env.setOutputTimeSecFormat(TemplateUtil.getLocaleFormat().getOutputTimeSecFormat());
			env.setOutputDateTimeSecFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat());

			RdbAdapterService ras = ServiceRegistry.getRegistry().getService(RdbAdapterService.class);
			RdbAdapter adapter = ras.getRdbAdapter();
			env.setRdbAdapterName(adapter != null ? adapter.getClass().getSimpleName() : "");

		} finally {
			ExecuteContext.finContext();
		}

		return env;
	}

	@Override
	public void authLogin(int tenantId, String id, String password) {
		AuthUtil.authLogin(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, id, password);
	}

	@Override
	public void authLogoff(int tenantId) {
		AuthUtil.authLogoff(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId);
	}

	@Override
	public void setLanguage(int tenantId, final String language) {

		AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				RequestContextHolder.getCurrent().getSession().setAttribute("language", language);

				return null;
			}

		});
	}

	@Override
	public TenantInfo getTenantDefinitionEntry(int tenantId, final boolean doGetOption) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<TenantInfo>() {
			@Override
			public TenantInfo call() {

				DefinitionEntry entry = dm.getDefinitionEntry(Tenant.class, ExecuteContext.getCurrentContext().getCurrentTenant().getName());

				TenantInfo info = new TenantInfo();
				info.setTenantEntry(entry);

				if (doGetOption) {
					GemConfigService gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);
					info.setSkins(gcs.getSkins());
					info.setThemes(gcs.getThemes());

					LinkedHashMap<String, String> enableLanguageMap = new LinkedHashMap<>();
					for (EnableLanguages lang : i18n.getEnableLanguages()) {
						enableLanguageMap.put(lang.getLanguageKey(), lang.getLanguageName());
					}
					info.setEnableLanguageMap(enableLanguageMap);
				}

				return info;
			}
		});
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
