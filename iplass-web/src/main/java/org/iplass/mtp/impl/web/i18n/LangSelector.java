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
package org.iplass.mtp.impl.web.i18n;

import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LangSelector {
	private static Logger logger = LoggerFactory.getLogger(LangSelector.class);

	public static final String LANG_ATTRIBUTE_NAME = "language";

	private I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);

	public void selectLangByUser(RequestContext reqContext, ExecuteContext exec) {
		String lang = null;
		TenantI18nInfo i18n  = exec.getCurrentTenant().getTenantConfig(TenantI18nInfo.class);
		if (i18n.isUseMultilingual() && i18n.getUseLanguageList() != null) {
			User user = AuthContext.getCurrentContext().getUser();
			String ulang = user.getLanguage();
			if (ulang != null) {
				for (String tl: i18n.getUseLanguageList()) {
					if (ulang.equals(tl)) {
						lang = ulang;
						break;
					}
				}
			}
		}

		if (lang != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("set lang by user to " + lang);
			}
			exec.setLanguage(lang);
		}
	}

	public void selectLangByRequest(RequestContext reqContext, ExecuteContext exec) {
		SessionContext sc = reqContext.getSession(false);
		String lang = null;
		if (sc != null) {
			lang = (String) sc.getAttribute(LANG_ATTRIBUTE_NAME);
		}
		if (lang == null) {
			//HeaderのAccept-Languageから。
			lang = getLangFromHeader(WebRequestStack.getCurrent().getRequest(), exec);
		}

		if (lang != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("set lang by request to " + lang);
			}
			exec.setLanguage(lang);
		}
	}

	private String getLangFromHeader(HttpServletRequest req, ExecuteContext ec) {
		TenantI18nInfo i18n = ec.getCurrentTenant().getTenantConfig(TenantI18nInfo.class);
		if (!i18n.isUseMultilingual()) {
			return null;
		}

		List<String> langList = i18n.getUseLanguageList();
		Enumeration<Locale> ls = req.getLocales();
		while (ls.hasMoreElements()) {
			Locale l = ls.nextElement();
			String lang = mapLang(l.toLanguageTag());
			if (langList.contains(lang)) {
				return lang;
			}
		}

		return null;
	}

	private String mapLang(String lang) {
		String mapped = i18n.toValidLanguageTag(lang);
		if (mapped == null) {
			return lang;
		} else {
			return mapped;
		}
	}

}
