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

package org.iplass.mtp.impl.core;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.tenant.MetaTenantI18nInfo.MetaTenantI18nInfoRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public class ExecuteContext {

	private static final Logger logger = LoggerFactory.getLogger(ExecuteContext.class);

	private TenantContext tenantContext;

	private Tenant currentTenant;

	private String clientId;

	private Timestamp currentTimestamp;
	private Date currentLocalDate;
	private Time currentLocalTime;

	private ExecuteContext prevStacked;

	private String language;
	private Locale langLocale;
	private Timestamp defaultEndDate;
	private Locale locale;
	private TimeZone timeZone;
	private LocaleFormat localeFormat;

	private HashMap<String, AttributeEntry> contextMap;

//	private final TenantAuthType authType;
	private static ThreadLocal<ExecuteContext> context = new ThreadLocal<ExecuteContext>();

	public static final ExecuteContext getCurrentContext() {

		//FIXME 暫定実装
		if (context.get() == null) {
			logger.debug("no ExecuteContext... create dummy as tenant -1");
			TenantContext t = new TenantContext(-1, "_shared", "/", true);
			initContext(new ExecuteContext(t, "0", "0"));
		}


		return context.get();


//		return context.get();
	}

	public static boolean isInited() {
		return context.get() != null;
	}


	public static <T> T executeAs(TenantContext tenant, Executable<T> exec) {
		ExecuteContext current = context.get();
		String currentMdcTenant = MDC.get("tenant");
		String currentMdcTenantName = MDC.get("tenantName");
		ExecuteContext nested = null;
		try {
			if (current != null) {
				nested = new ExecuteContext(tenant);
				nested.prevStacked = current;
//				nested.clientId = current.clientId;
				if (current.contextMap != null) {
					HashMap<String, AttributeEntry> nestedContextMap = new HashMap<String, ExecuteContext.AttributeEntry>();
					for (Map.Entry<String, AttributeEntry> e: current.contextMap.entrySet()) {
						AttributeEntry val = e.getValue();
						if (val != null && val.isShare && !val.removed) {
							nestedContextMap.put(e.getKey(), val);
						}
					}
					nested.contextMap = nestedContextMap;
				}
				nested.currentTimestamp = current.currentTimestamp;
			} else {
				nested = new ExecuteContext(tenant);
			}

			context.set(nested);
			MDC.put("tenant", String.valueOf(nested.getClientTenantId()));
			MDC.put("tenantName", String.valueOf(nested.getTenantContext().getTenantName()));

			if(logger.isDebugEnabled()) {
				if (current == null) {
					logger.debug("execute as tenant:" + tenant.getTenantId() + ", context:" + nested);
				} else {
					logger.debug("execute as tenant:" + tenant.getTenantId() + ", prevStackTenant:" + current.getClientTenantId() + ", context:" + nested);
				}
			}

			return exec.execute();
		} finally {
			try {
				if (current != null && nested != null) {
					if (nested.contextMap != null) {
						if (current.contextMap == null) {
							current.contextMap = new HashMap<String, AttributeEntry>();
						}
						for (Map.Entry<String, AttributeEntry> e: nested.contextMap.entrySet()) {
							AttributeEntry val = e.getValue();
							if (val != null && val.isShare) {
								if (val.removed) {
									current.contextMap.remove(e.getKey());
								} else {
									current.contextMap.put(e.getKey(), val);
								}
							}
						}
					}
				}
			} catch (RuntimeException e) {
				logger.error("error on ExecuteContext#executeAs() finally process:" + e, e);
			} finally {
				if (nested != null) {
					nested.finallyProcess();
				}
				context.set(current);
				if (currentMdcTenant != null) {
					MDC.put("tenant", currentMdcTenant);
				} else {
					MDC.remove("tenant");
				}
				if (currentMdcTenantName != null) {
					MDC.put("tenantName", currentMdcTenantName);
				} else {
					MDC.remove("tenantName");
				}

			}
		}
	}

	public static void finContext() {
		ExecuteContext current = context.get();
		if (current != null) {
			current.finallyProcess();
			context.remove();
//			MDC.remove("tenant");
//			MDC.remove("user");
			MDC.clear();
		}
	}


	public static void initContext(ExecuteContext mtfContext) {

		ExecuteContext previous = context.get();
		if (previous != null) {
			logger.debug("previous ExecuteContext exists.. so cleanup before initContext:" + previous);
			previous.finallyProcess();
		}
		if(logger.isDebugEnabled()) {
			logger.debug("init execute context:" + mtfContext);
		}
		context.set(mtfContext);
		if (mtfContext != null && mtfContext.getTenantContext() != null) {
			MDC.put("tenant", String.valueOf(mtfContext.getClientTenantId()));
			MDC.put("tenantName", mtfContext.getTenantContext().getTenantName());
		} else {
			MDC.remove("tenant");
			MDC.remove("tenantName");
			MDC.remove("user");
		}
	}

	public static void setContext(ExecuteContext mtfContext) {

		if(logger.isDebugEnabled()) {
			logger.debug("set context:" + mtfContext);
		}
		context.set(mtfContext);
		if (mtfContext != null && mtfContext.getTenantContext() != null) {
			MDC.put("tenant", String.valueOf(mtfContext.getClientTenantId()));
			MDC.put("tenantName", mtfContext.getTenantContext().getTenantName());
		} else {
			MDC.remove("tenant");
			MDC.remove("tenantName");
			MDC.remove("user");
		}
	}

	/**
	 * FIXME 本コンストラクタは削除されます。
	 * @param tenantContext
	 * @param clientId
	 * @deprecated そのうち削除
	 */
	public ExecuteContext(TenantContext tenantContext, String clientId, String sessionId) {
		this.tenantContext = tenantContext;
		this.clientId = clientId;
//		this.sessionId = sessionId;
//		this.authType = TenantAuthType.URL;
	}

	public ExecuteContext(TenantContext tenantContext) {
		this.tenantContext = tenantContext;
//		this.authType = authType;
	}

	public Timestamp getDefaultEndDate() {
		if (defaultEndDate == null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				sdf.setTimeZone(getTimeZone());
				defaultEndDate = new Timestamp(sdf.parse("20991231").getTime());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return (Timestamp) defaultEndDate.clone();
	}

	public Locale getLocale() {
		initI18n();
		return locale;
	}

	public TimeZone getTimeZone() {
		initI18n();
		return timeZone;
	}

	public LocaleFormat getLocaleFormat() {
		initI18n();
		return localeFormat;
	}

	/**
	 * 言語を表すLocale。
	 *
	 * @return
	 */
	public Locale getLangLocale() {
		initI18n();
		return langLocale;
	}

	private void initI18n() {
		if (locale == null || langLocale == null) {
			I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);
			MetaTenantHandler mth = tenantContext.getTenantRuntime();
			MetaTenantI18nInfoRuntime tenanti18n = (mth != null ? mth.getConfigRuntime(MetaTenantI18nInfoRuntime.class) : null);
			if (locale == null) {
				if (tenanti18n != null) {
					locale = tenanti18n.getLocale();
					timeZone = tenanti18n.getTimeZone();
				} else {
					locale = i18n.getLocale();
					timeZone = i18n.getTimezone();
				}

				localeFormat = i18n.getLocaleFormat(locale.toString(), getCurrentTenant());
			}

			if (langLocale == null) {
				TenantI18nInfo t = getCurrentTenant().getTenantConfig(TenantI18nInfo.class);
				if (t.isUseMultilingual() && t.getUseLanguageList() != null) {
					User user = AuthContext.getCurrentContext().getUser();
					String lang = user.getLanguage();
					if (lang != null) {
						for (String tl: t.getUseLanguageList()) {
							if (lang.equals(tl)) {
								language = lang;
								langLocale = Locale.forLanguageTag(language);
								break;
							}
						}
					}
				}
				if (langLocale == null) {
					if (tenanti18n != null) {
						langLocale = tenanti18n.getLangLocale();
						language = langLocale.toLanguageTag();
					} else {
						langLocale = i18n.selectLangLocale(locale);
						language = langLocale.toLanguageTag();
					}
				}
			}
		}
	}

//	private void initLang() {
//		if (langLocale == null) {
//			Tenant t = getCurrentTenant();
//			if (t.isUseMultilingual() && t.getUseLanguageList() != null) {
//				User user = AuthContext.getCurrentContext().getUser();
//				String lang = user.getLanguage();
//				if (lang != null) {
//					for (String tl: t.getUseLanguageList()) {
//						if (lang.equals(tl)) {
//							language = lang;
//							langLocale = Locale.forLanguageTag(language);
//							break;
//						}
//					}
//				}
//			}
//			if (langLocale == null) {
//				langLocale = tenantContext.getTenantRuntime().getLangLocale();
//				language = langLocale.toLanguageTag();
//			}
//		}
//	}

	/**
	 * java内でResosurceBundleから文字列取得する際の処理では、getLangLocale()を使うように。
	 * じゃないと、毎回のString->Localeのパース処理が無駄なので。。。
	 *
	 * @return
	 */
	public String getLanguage() {
		initI18n();
		return language;
	}

	public void setLanguage(String language) {
		if (language != null) {
			if (!language.equals(this.language)) {
				this.language = language;
				langLocale = Locale.forLanguageTag(language);
			}
		}
	}

	public void clearLanguage() {
		language = null;
		langLocale = null;
	}

	public ExecuteContext getPrevStacked() {
		return prevStacked;
	}

	private void finallyProcess() {
		if(logger.isDebugEnabled()) {
			logger.debug("finalize execute context:" + this);
		}
		try {
			if (contextMap != null) {
				for (Map.Entry<String, AttributeEntry> e: contextMap.entrySet()) {
					AttributeEntry val = e.getValue();
					if (val != null && val.attribute instanceof Finalizable
							&& (!val.isShare || prevStacked == null)) {
						try {
							if (logger.isDebugEnabled()) {
								logger.debug("finalize " + e.getValue() + "...");
							}
							((Finalizable) e.getValue()).finallyProcess();
						} catch (Exception ex) {
							logger.error("execute context finalize error, mybe resource leak...", ex);
						}
					}
				}
			}
		} catch (RuntimeException e) {
			logger.error("error on ExecuteContext#finallyProcess():" + e, e);
		}
		contextMap = null;
	}

	public int getClientTenantId() {
		return tenantContext.getTenantId();
	}

	/**
	 * 現在実行中のコンテキストのテナント情報を取得する。
	 * {@link TenantContext#loadTenantInfo()}では、毎回MetaDataを元にTenantをnewする。
	 * このメソッドでは、1回だけnewされたTenantのインスタンスを使い回す。
	 *
	 * @return
	 */
	public Tenant getCurrentTenant() {
		if (currentTenant == null) {
			currentTenant = tenantContext.loadTenantInfo();
		}
		return currentTenant;
	}

	public TenantContext getTenantContext() {
		return tenantContext;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

//	public String getSessionId() {
//		return sessionId;
//	}
//
//	public void setSessionId(String sessionId) {
//		this.sessionId = sessionId;
//	}

	public Object getAttribute(String key) {
		if (contextMap == null) {
			return null;
		}
		AttributeEntry val = contextMap.get(key);
		if (val == null) {
			return null;
		}
		if (val.removed) {
			return null;
		}
		return val.attribute;
	}

	public void setAttribute(String key, Object value, boolean shareContext) {
		if (contextMap == null) {
			contextMap = new HashMap<String, AttributeEntry>();
		}
		contextMap.put(key, new AttributeEntry(value, shareContext));
	}

	public Timestamp getCurrentTimestamp() {
		if (currentTimestamp == null) {
			currentTimestamp = new Timestamp(System.currentTimeMillis());
		}
		return (Timestamp) currentTimestamp.clone();
	}

	public void setCurrentTimestamp(Timestamp currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
		currentLocalDate = null;
		currentLocalTime = null;
	}

	public void refreshCurrentTimestamp() {
		currentTimestamp = new Timestamp(System.currentTimeMillis());
		currentLocalDate = null;
		currentLocalTime = null;
	}

	public Date getCurrentLocalDate() {
		if (currentLocalDate == null) {
			Timestamp ts = getCurrentTimestamp();
			if (getCurrentTenant() != null && getCurrentTenant().getTenantConfig(TenantI18nInfo.class).getTimezone() != null) {
				TimeZone systemTz = TimeZone.getDefault();
				TimeZone localTz = getTimeZone();
				int offset = localTz.getOffset(ts.getTime()) - systemTz.getOffset(ts.getTime());
				ts.setTime(ts.getTime() + offset);
			}
			currentLocalDate = new Date(DateUtils.truncate(ts, Calendar.DAY_OF_MONTH).getTime());
		}
		return (Date) currentLocalDate.clone();
	}

	public Time getCurrentLocalTime() {
		if (currentLocalTime == null) {
			Timestamp ts = getCurrentTimestamp();
			if (getCurrentTenant() != null && getCurrentTenant().getTenantConfig(TenantI18nInfo.class).getTimezone() != null) {
				TimeZone systemTz = TimeZone.getDefault();
				TimeZone localTz = getTimeZone();
				int offset = localTz.getOffset(ts.getTime()) - systemTz.getOffset(ts.getTime());
				ts.setTime(ts.getTime() + offset);
			}
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTimeInMillis(ts.getTime());
			cal.set(1970, 0, 1);
			currentLocalTime = new Time(cal.getTimeInMillis());
		}
		return (Time) currentLocalTime.clone();
	}

//	public TenantAuthType getTenantAuthType() {
//		return authType;
//	}

	public void removeAttribute(String key) {
		if (contextMap != null) {
			AttributeEntry val = contextMap.get(key);
			if (val != null && val.isShare) {
				val.removed = true;
				val.attribute = null;
			} else {
				contextMap.remove(key);
			}
		}
	}

	private static class AttributeEntry {
		boolean isShare;
		Object attribute;

		boolean removed = false;

		AttributeEntry(Object attribute, boolean isShare) {
			this.attribute = attribute;
			this.isShare = isShare;
		}
	}

}
