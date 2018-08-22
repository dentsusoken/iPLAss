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

package org.iplass.mtp.tenant;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;

/**
 * テナント定義を管理するクラスのインタフェース。
 *
 */
public interface TenantManager extends TypedDefinitionManager<Tenant> {

	/**
	 * テナント情報を取得します。
	 *
	 * <p>対象とするテナントは {@link ExecuteContext#getClientTenantId()} より取得します。</p>
	 *
	 * @return {@link Tenant}
	 */
	public Tenant getTenant();

	/**
	 * テナント情報を更新します。
	 *
	 * @param tenant {@link Tenant}
	 */
	public DefinitionModifyResult updateTenant(Tenant tenant);
	public DefinitionModifyResult updateTenant(Tenant tenant, final boolean forceUpdate);


}
