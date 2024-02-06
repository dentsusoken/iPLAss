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

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * テナント定義を管理するクラスの実装クラス。
 *
 */
public class TenantManagerImpl extends AbstractTypedDefinitionManager<Tenant> implements TenantManager {

	private static final Logger logger = LoggerFactory.getLogger(TenantManagerImpl.class);

	/** テナントService */
	private TenantService tenantService = ServiceRegistry.getRegistry().getService(TenantService.class);
	/** MetaテナントService */
	private MetaTenantService metaTenantService = ServiceRegistry.getRegistry().getService(MetaTenantService.class);

	@Override
	public Tenant getTenant() {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();

		//Tenant情報の検索
		Tenant tenant = tenantService.getTenant(tenantId);
		if(tenant == null) {
			return null;
		}

		//MetaTenant情報の検索
		MetaTenantHandler handler = metaTenantService.getRuntimeByName(tenant.getName());
		if(handler == null){
			return null;
			//ハンドラの取得ができない場合は、デフォルトを取得
			//handler = metaTenantService.getHandlerByMetaTenantName(tenant.getId(), metaTenantService.DEFAULT_TENANT);
		}
		MetaTenant metaTenant = handler.getMetaData();

		//Meta情報のセット
		metaTenant.applyToTenant(tenant);

		return tenant;
	}

	@Override
	public Tenant get(String definitionName) {
		return getTenant();
	}

	@Override
	public DefinitionModifyResult updateTenant(Tenant tenant) {
		return updateTenant(tenant, false);
	}

	@Override
	public DefinitionModifyResult updateTenant(Tenant tenant, boolean forceUpdate) {

		//変更前Tenant情報の検索（テナント名変更対応）
		Tenant oldTenant = tenantService.getTenant(tenant.getId());
		if(oldTenant == null) {
			logger.error("exception occured during tenant definition update:"
					+ "指定のテナントは存在しません。テナントID=" + tenant.getId());
			return new DefinitionModifyResult(false, "exception occured during tenant definition update:"
					+ "指定のテナントは存在しません。テナントID=" + tenant.getId());
		}

		//更新対象のMetaTenantにIDを設定するため検索（この際、変更前テナントのNameで検索）
		MetaTenantHandler handler = metaTenantService.getRuntimeByName(oldTenant.getName());
		if(handler == null){
			logger.error("exception occured during tenant definition update:"
					+ "指定のテナントは存在しません。テナント名=" + oldTenant.getName());
			return new DefinitionModifyResult(false, "exception occured during tenant definition update:"
					+ "指定のテナントは存在しません。テナント名=" + oldTenant.getName());
		}
		MetaTenant updateMetaTenant = new MetaTenant(tenant);
		updateMetaTenant.setId(handler.getMetaData().getId());

		try {
			//DB側の更新
			String clientId = ExecuteContext.getCurrentContext().getClientId();
			tenantService.updateTenant(tenant, clientId, forceUpdate);

			//Meta側の更新
			metaTenantService.updateMetaData(updateMetaTenant);

			//更新後のTenantの取得
			tenant = tenantService.getTenant(tenant.getId());
		} catch (Exception e) {
			setRollbackOnly();
			if (e.getCause() != null) {
				logger.error("exception occured during tenant definition update:" + e.getCause().getMessage(), e.getCause());
				return new DefinitionModifyResult(false, "exception occured during tenant definition update:" + e.getCause().getMessage());
			} else {
				logger.error("exception occured during tenant definition update:" + e.getMessage(), e);
				return new DefinitionModifyResult(false, "exception occured during tenant definition update:" + e.getMessage());
			}
		}

//		//テナントコンテキストのキャッシュをクリアする
//		TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
//		tenantContextService.clearCacheTenantContext(tenant);

		return new DefinitionModifyResult(true);
	}

	@Override
	public DefinitionModifyResult update(Tenant definition) {
		return updateTenant(definition);
	}

	@Override
	public Class<Tenant> getDefinitionType() {
		return Tenant.class;
	}

	@Override
	protected RootMetaData newInstance(Tenant definition) {
		return new MetaTenant();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return metaTenantService;
	}
}
