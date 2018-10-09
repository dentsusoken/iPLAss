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

import org.iplass.adminconsole.shared.base.dto.tenant.AdminPlatformInfo;
import org.iplass.adminconsole.shared.base.dto.tenant.TenantEnv;
import org.iplass.adminconsole.shared.metadata.dto.tenant.TenantInfo;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * テナント用AsyncService
 */
public interface TenantServiceAsync {

	/**
	 * テナント情報を取得します。
	 *
	 * @param tenantId テナントID
	 * @param callback  Callbackクラス
	 */
	void getTenant(int tenantId, AsyncCallback<Tenant> callback);

	/**
	 * テナント情報を更新します。
	 *
	 * @param tenantId テナントID
	 * @param tenant {@link Tenant}
	 * @param callback  Callbackクラス
	 */
	void updateTenant(int tenantId, final Tenant tenant, final int currentVersion, final boolean checkVersion, AsyncCallback<Boolean> callback);
	void updateTenant(int tenantId, final Tenant tenant, final int currentVersion, final boolean checkVersion, final boolean forceUpdate, AsyncCallback<Boolean> callback);

	void getPlatformInformation(int tenantId, AsyncCallback<AdminPlatformInfo> callback);

	void getTenantEnv(int tenantId, AsyncCallback<TenantEnv> callback);

	void authLogin(int tenantId, String id, String password, AsyncCallback<Void> callback);

	void authLogoff(int tenantId, AsyncCallback<Void> callback);

	void setLanguage(int tenantId, String lang, AsyncCallback<Void> callback);

	/**
	 * テナント情報を取得します。
	 *
	 * @param tenantId テナントID
	 * @param doGetOption テナント情報以外にOption情報を取得するか
	 * @param callback  Callbackクラス
	 */
	void getTenantDefinitionEntry(int tenantId, boolean doGetOption, AsyncCallback<TenantInfo> callback);
}
