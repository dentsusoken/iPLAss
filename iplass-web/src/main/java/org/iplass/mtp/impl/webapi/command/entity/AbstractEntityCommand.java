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
package org.iplass.mtp.impl.webapi.command.entity;

import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.webapi.EntityWebApiService;
import org.iplass.mtp.impl.webapi.MetaEntityWebApi.EntityWebApiHandler;
import org.iplass.mtp.impl.webapi.command.Constants;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;

public abstract class AbstractEntityCommand implements Command, Constants {

	EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
	EntityWebApiService entityWebApiService = ServiceRegistry.getRegistry().getService(EntityWebApiService.class);
	
	protected abstract String executeImpl(RequestContext request, String[] subPath);
	
	@Override
	public String execute(RequestContext request) {
		String subPath = (String) request.getAttribute(WebRequestConstants.SUB_PATH);
		if (subPath != null && subPath.startsWith("/")) {
			subPath = subPath.substring(1);
		}
		String[] splitSubPath = null;
		if (subPath != null) {
			splitSubPath = subPath.split("/");
		}
		
		return executeImpl(request, splitSubPath);
	}
	
	protected void checkPermission(String defName, Predicate<EntityWebApiHandler> predicate) {
		EntityWebApiHandler def = entityWebApiService.getRuntimeByName(defName);
		if (def == null || !predicate.test(def)) {
			throw new IllegalArgumentException("Operation not permitted. EntityWebApiDefinition: " + defName);
		}
	}
	
}
