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

package org.iplass.mtp.impl.core;

import java.util.HashMap;

import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptService;
import org.iplass.mtp.impl.tenant.MetaTenant;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.tenant.MetaTenantService;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;

public class TenantContext {

	private static MetaTenantService mtService = ServiceRegistry.getRegistry().getService(MetaTenantService.class);

	private final Tenant baseTenantInfo;//Rdb上の不変の定義情報のみ
	private final String tenantIdString;

	private final boolean isNoMeta;

	private final ScriptEngine scriptEngine;
	private final MetaDataContext metaDataContext;

	private HashMap<Class<?>, TenantResource> resourceMap;

	/**
	 * コンストラクタ
	 * @param tenantId テナントID
	 * @param tenantUrl テナントURL
	 */
	public TenantContext(int tenantId, String tenantName, String tenantUrl, boolean isNoMeta) {
		this(new Tenant(tenantId, tenantName, tenantUrl), isNoMeta);
	}

	/**
	 * キャッシュクリア時のために、tenantIdのみ保持するTenantContext。
	 * このコンストラクタで生成したTenantContextは、動作しない。
	 *
	 * @param tenantId
	 */
	TenantContext(int tenantId) {
		Tenant t = new Tenant();
		t.setId(tenantId);
		t.setName("___dummy");
		t.setUrl("___dummy");
		tenantIdString = String.valueOf(tenantId);
		this.baseTenantInfo = t;
		this.isNoMeta = true;

		this.scriptEngine = null;
		this.metaDataContext = null;

	}

	public TenantContext(Tenant tenant, boolean isNoMeta) {
		this.isNoMeta = isNoMeta;
		this.baseTenantInfo = tenant;
		tenantIdString = String.valueOf(tenant.getId());
		this.scriptEngine = ServiceRegistry.getRegistry().getService(ScriptService.class).createScriptEngine();
		this.metaDataContext = new MetaDataContext(baseTenantInfo.getId());
		initResource();
	}

	private void initResource() {
		resourceMap = new HashMap<>();
		TenantContextService tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
		for (Class<?> trClass: tcService.getTenantResourceClasses()) {
			try {
				TenantResource tr = (TenantResource) trClass.newInstance();
				tr.init(this);
				resourceMap.put(trClass, tr);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ServiceConfigrationException(trClass.getName() + " can't instanceate.");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends TenantResource> T getResource(Class<T> resourceClass) {
		return (T) resourceMap.get(resourceClass);
	}

	public int getTenantId() {
		return baseTenantInfo.getId();
	}
	
	public String getTenantIdString() {
		return tenantIdString;
	}

	public String getTenantName() {
		return baseTenantInfo.getName();
	}

	public ScriptEngine getScriptEngine() {
		return scriptEngine;
	}

	public MetaDataContext getMetaDataContext() {
		return metaDataContext;
	}

	/**
	 * @return tenantUrl
	 */
	public String getTenantUrl() {
		return baseTenantInfo.getUrl();
	}

	public MetaTenantHandler getTenantRuntime() {
		MetaTenantHandler tr = mtService.getRuntimeByName(baseTenantInfo.getName());
		if (tr == null) {
			//メタのないtestcodeや、 -1の共有テナントではnullはあり得る。。。
			tr = new MetaTenant().createRuntime(null);
		}
		return tr;
	}

//	public String getTenantUrlForRequest() {
//		if (getTenant().getTenantDisplayInfo() == null) {
//			return null;
//		}
//		return getTenant().getTenantDisplayInfo().getUrlForRequest();
//	}

	/**
	 * Rdb上の不変のテナント情報（id,url,name）と、メタデータ上のTenant情報を元に、
	 * Tenantを作成して返却する。
	 *
	 * @return
	 */
	public Tenant loadTenantInfo() {
		if (isNoMeta) {
			return ObjectUtil.deepCopy(baseTenantInfo);
		}

		//このTenantContextのテナントとして実行する
		return ExecuteContext.executeAs(this, new Executable<Tenant>() {
			@Override
			public Tenant execute() {
				Tenant copy = ObjectUtil.deepCopy(baseTenantInfo);

				//テナント情報のメタデータ設定
				MetaTenantHandler handler = ServiceRegistry.getRegistry().getService(MetaTenantService.class).getRuntimeByName(copy.getName());
				if (handler != null) {
					handler.applyMetaDataToTenant(copy);
				}
				return copy;
			}
		});
	}

	public void invalidate() {
		if (resourceMap != null) {
			for (TenantResource r: resourceMap.values()) {
				r.destory();
			}
		}
		scriptEngine.invalidate();
		metaDataContext.invalidate();
	}

}
