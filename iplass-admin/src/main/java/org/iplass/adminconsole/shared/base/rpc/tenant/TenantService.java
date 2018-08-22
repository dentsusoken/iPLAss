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

package org.iplass.adminconsole.shared.base.rpc.tenant;

import org.iplass.adminconsole.shared.base.dto.auth.LoginFailureException;
import org.iplass.adminconsole.shared.base.dto.auth.TenantNotFoundException;
import org.iplass.adminconsole.shared.base.dto.auth.UnauthorizedAccessException;
import org.iplass.adminconsole.shared.base.dto.tenant.AdminPlatformInfo;
import org.iplass.adminconsole.shared.base.dto.tenant.TenantEnv;
import org.iplass.adminconsole.shared.metadata.dto.MetaVersionCheckException;
import org.iplass.adminconsole.shared.metadata.dto.tenant.TenantInfo;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * テナント用Service
 */
@RemoteServiceRelativePath("service/tenant")
public interface TenantService extends XsrfProtectedService {

	/**
	 * テナント情報を取得します。
	 *
	 * @param tenantId テナントID
	 * @return {@link Tenant}
	 */
	public Tenant getTenant(int tenantId);

	/**
	 * テナント情報を更新します。
	 *
	 * @param tenantId テナントID
	 * @param tenant {@link Tenant}
	 * @return 更新結果
	 */
	public boolean updateTenant(int tenantId, final Tenant tenant, final int currentVersion, final boolean checkVersion) throws MetaVersionCheckException;
	public boolean updateTenant(int tenantId, final Tenant tenant, final int currentVersion, final boolean checkVersion, final boolean forceUpdate) throws MetaVersionCheckException;
	public AdminPlatformInfo getPlatformInformation(int tenantId);

	public TenantEnv getTenantEnv(int tenantId) throws TenantNotFoundException;

	public void authLogin(int tenantId, String id, String password) throws LoginFailureException, UnauthorizedAccessException;

	public void authLogoff(int tenantId);

	public void setLanguage(int tenantId, String lang);

//	public AvailableStatus getTenantAvailableStatus(int tenantId);
//
//	public boolean updateTenantAvailableStatus(int tenantId, AvailableStatus status);
//
//	public TenantAvailable getTenantAvailable(int tenantId);
//
//	public AdminDefinitionModifyResult updateTenantAvailable(int tenantId, TenantAvailable definition);
//
//	public List<String> getOnetimeCodeGeneratorNames(int tenantId);

	/**
	 * テナント情報を取得します。
	 *
	 * @param tenantId テナントID
	 * @param doGetOption テナント情報以外にOption情報を取得するか
	 * @return {@link TenantInfo}
	 */
	public TenantInfo getTenantDefinitionEntry(int tenantId, boolean doGetOption);
}
