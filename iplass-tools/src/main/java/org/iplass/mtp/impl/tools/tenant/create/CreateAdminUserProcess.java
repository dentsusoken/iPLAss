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
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.tenant.TenantCreateParameter;
import org.iplass.mtp.impl.tools.tenant.TenantToolService;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.spi.Config;

public class CreateAdminUserProcess implements TenantCreateProcess {

	private AuthenticationPolicyService authPolicyService;

	@Override
	public void inited(TenantToolService service, Config config) {
		authPolicyService = config.getDependentService(AuthenticationPolicyService.class);
	}

	@Override
	public boolean execute(TenantCreateParameter param, LogHandler logHandler) {

		User user = new User();
		user.setValue(User.ACCOUNT_ID, param.getAdminUserId());
		user.setValue(User.FIRST_NAME,"Admin");
		user.setValue(User.LAST_NAME, "User");
		user.setValue(User.LAST_NAME_KANA, null);
		user.setValue(User.FIRST_NAME_KANA, null);

		user.setValue(User.ADMIN_FLG,true);

		//ver3.0 ではAdmin作成時にはDEFAULTのポリシーを利用
		final AuthenticationPolicyRuntime pol = authPolicyService.getRuntimeByName(AuthenticationPolicyService.DEFAULT_NAME);
		if (pol != null) {
			pol.getMetaData().getPasswordPolicy().setCreateAccountWithSpecificPassword(true);
		}
		user.setPassword(param.getAdminPassword());

		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		em.insert(user);

		logHandler.info(ToolsResourceBundleUtil.resourceString(param.getLoggerLanguage(), "tenant.create.createdAdminMsg", param.getAdminUserId()));

		return true;
	}

}
