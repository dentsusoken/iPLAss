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

package org.iplass.mtp.impl.tenant;

import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantConfig;

public abstract class MetaTenantConfig<C extends TenantConfig> implements MetaData {

	private static final long serialVersionUID = -7759766584935038978L;

	/**
	 * DefinitionをMetaDataに変換します。
	 * @param definition Definition
	 */
	public abstract void applyConfig(C definition);

	/**
	 * MetaDataをDefinitionに変換します。
	 * @return Definition
	 */
	public abstract C currentConfig();

	/**
	 * Runtimeを生成します。
	 *
	 * @param tenantRuntime TenantのRuntime
	 * @return Runtime
	 */
	public abstract MetaTenantConfigRuntime createRuntime(MetaTenantHandler tenantRuntime);

	public abstract class MetaTenantConfigRuntime extends BaseMetaDataRuntime {

		public abstract void applyMetaDataToTenant(Tenant tenant);
	}

}
