/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.rpc.i18n;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.base.dto.i18n.MultiLangFieldInfo;
import org.iplass.adminconsole.shared.base.rpc.i18n.I18nService;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;

public class I18nServiceImpl extends XsrfProtectedServiceServlet implements I18nService {

	private static final long serialVersionUID = -5131858758974764635L;

	@Override
	public List<LocalizedStringDefinition> getLocalizedResourceList(int tenantId, String key) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<List<LocalizedStringDefinition>>() {

			@Override
			public List<LocalizedStringDefinition> call() {
				return GemResourceBundleUtil.resourceList(key);
			}
		});

	}

	@Override
	public Map<String, List<LocalizedStringDefinition>> getMultiLangItemInfoForDisp(int tenantId, Definition definition) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Map<String, List<LocalizedStringDefinition>>>() {

			@Override
			public Map<String, List<LocalizedStringDefinition>> call() {
				Map<String, List<LocalizedStringDefinition>> listGridFieldsMap = new LinkedHashMap<String, List<LocalizedStringDefinition>>();
				LangDataLogic logic = new LangDataLogic();
				logic.createMultiLangInfo(listGridFieldsMap, definition.getClass(), definition, null);
				return listGridFieldsMap;
			}
		});

	}

	@Override
	public Definition getMultiLangItemInfoForUpdate(int tenantId, Definition definition, Map<String, MultiLangFieldInfo> updateDefinitionInfo) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Definition>() {

			@Override
			public Definition call() {
				Class<?> cls = definition.getClass();
				LangDataLogic logic = new LangDataLogic();
				logic.createDefinitionInfo(cls, definition, updateDefinitionInfo, null);
				return definition;
			}
		});

	}

}
