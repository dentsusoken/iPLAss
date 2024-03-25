/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.web.actionmapping.cache.blocks;

import java.io.IOException;

import jakarta.servlet.ServletException;

import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.impl.web.actionmapping.WebInvocationImpl;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentBlock;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentCache;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentCacheContext;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaCacheCriteria.CacheCriteriaRuntime;
import org.iplass.mtp.spi.ServiceRegistry;

public class IncludeActionBlock implements ContentBlock {
	private static final long serialVersionUID = -894755257590110956L;

	private final String actionName;

	public IncludeActionBlock(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	@Override
	public void writeTo(WebRequestStack request) throws IOException,
			ServletException {
		WebUtil.include(actionName, request.getRequest(), request.getResponse(), request.getServletContext(), request.getPageContext());
	}

	@Override
	public long lastModified(long lastModified, WebInvocationImpl invocation, ContentCacheContext cc, String lang) {

		ActionMappingRuntime amr = ServiceRegistry.getRegistry().getService(ActionMappingService.class).getRuntimeByName(actionName);
		if (amr == null) {
			return Long.MAX_VALUE;
		}

		CacheCriteriaRuntime ccr = amr.getCacheCriteria();
		if (ccr == null) {
			return Long.MAX_VALUE;
		}

		String key = ccr.createContentCacheKey(invocation.getRequest());

		ContentCache incCache = cc.get(actionName, lang, key);
		if (incCache == null) {
			return Long.MAX_VALUE;
		}

		long incLastMod = incCache.getLastModified(invocation, cc, lang);
		if (lastModified > incLastMod) {
			return lastModified;
		} else {
			return incLastMod;
		}
	}

}
