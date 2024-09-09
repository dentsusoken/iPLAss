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

package org.iplass.mtp.impl.tenant;

import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.tenant.rdb.RdbTenantStore;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.tenant.Tenant;

/**
 * テナント情報管理サービス
 *
 * @author 片野　博之
 *
 */
public class TenantService implements Service {

	//FIXME TenantServiceとMetaTenantServiceの統合

	private TenantStore store;
	
	@Override
	public void init(Config config) {
		store = config.getValueWithSupplier("store", TenantStore.class, () -> new RdbTenantStore());
	}

	@Override
	public void destroy() {
	}

	/**
	 * テナント情報を取得する。（テナントURL指定）
	 *
	 * @param url
	 *            テナントURL
	 * @return テナント情報（存在しない場合はNull）
	 */
	public Tenant getTenant(final String url) {
		return store.getTenant(url);
	}

	/**
	 * テナント情報を取得する。（ID指定）
	 *
	 * @param id
	 *            テナントID
	 * @return テナント情報（存在しない場合はNull）
	 */
	public Tenant getTenant(final int id) {
		return store.getTenant(id);
	}

	/**
	 * テナントを作成する。<br>
	 *
	 * @param tenant
	 *            登録するテナント情報
	 */
	public void registTenant(final Tenant tenant, final String registId) {
		store.registTenant(tenant, registId);
	}

	/**
	 * テナントを更新する。<br>
	 * 基本情報は更新できない(ID,HostName,URLは変更できない。)
	 *
	 * @param tenant テナント情報
	 */
	public void updateTenant(final Tenant tenant) {
		updateTenant(tenant, ExecuteContext.getCurrentContext().getClientId());
	}

	public void updateTenant(Tenant tenant, String updateId) {
		updateTenant(tenant, updateId, false);
	}
	/**
	 * テナントを更新する。<br>
	 * 基本情報は更新できない(ID,HostName,URLは変更できない。)
	 *
	 * @param tenant
	 *            テナント情報
	 */
	public void updateTenant(final Tenant tenant, final String updateId, final boolean forceUpdate) {
		store.updateTenant(tenant, updateId, forceUpdate);
	}

	public List<Integer> getAllTenantIdList() {
		return store.getAllTenantIdList();
	}
}
