/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.tenant.create;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.tenant.TenantCreateParameter;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;

public class CreateAppAdminRoleProcess extends AbstractCreateRoleProcess {

	@Override
	public boolean execute(TenantCreateParameter param, LogHandler logHandler) {

		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

		//ロール
		Entity role = insertGroupRole("AppAdmin", em);

		//Action権限
		insertActionPermission(role, em);

		//Entity権限
		insertEntityPermission(role, em);

		//WebApi権限
		insertWebApiPermission(role, em);

		logHandler.info(ToolsResourceBundleUtil.resourceString(param.getLoggerLanguage(), "tenant.create.createdAppAdminRole"));

		return true;
	}

	private void insertActionPermission(Entity role, EntityManager em) {

		//gem/*
		GenericEntity permission = createActionPermission("gem/*", role);
		//GemAuthで判定
		permission.setValue("conditionExpression", "org.iplass.gem.auth.GemAuth.isPermitAction(action, parameter)");
		em.insert(permission);

	}

	private void insertEntityPermission(Entity role, EntityManager em) {

		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);

		//mtp.auth.*
		//参照更新可能
		edm.definitionSummaryList("mtp.auth.", true).forEach(ed -> {
			GenericEntity authPermission = createEntityPermission(ed.getName(), role, true, true, true, true);
			em.insert(authPermission);
		});

		//mtp.maintenance.*
		//権限なし
		edm.definitionSummaryList("mtp.maintenance.", true).forEach(ed -> {
			GenericEntity maintenancePermission = createEntityPermission(ed.getName(), role, false, false, false, false);
			em.insert(maintenancePermission);
		});

		//mtp.Information
		//参照更新可能
		Entity permission = createEntityPermission("mtp.Information", role, true, true, true, true);
		em.insert(permission);

	}

	private void insertWebApiPermission(Entity role, EntityManager em) {

		//gem/*
		GenericEntity permission = createWebApiPermission("gem/*", role);
		//GemAuthで判定
		permission.setValue("conditionExpression", "org.iplass.gem.auth.GemAuth.isPermitWebApi(webApi, parameter)");
		em.insert(permission);

	}

}
