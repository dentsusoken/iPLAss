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

package org.iplass.mtp.impl.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplass.mtp.impl.cache.CacheController;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.LoadingAdapter;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.builtin.TransactionLocalCacheStoreFactory.TransactionLocalCacheStore;
import org.iplass.mtp.impl.cluster.ClusterEventListener;
import org.iplass.mtp.impl.cluster.ClusterService;
import org.iplass.mtp.impl.cluster.Message;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContextService implements Service {
	public static final String TENANT_CONTEXT_CACHE_NAMESPACE = "mtp.tenant.tenantContext";
	private static final int CACHE_INDEX_URL = 0;

	private static final String CLUSTER_EVENT_NAME_TENANT_RELOAD = "mtp.tenant.rl";
	private static final String CLUSTER_MESSAGE_TENANT_ID = "tenantId";

	private static Logger logger = LoggerFactory.getLogger(TenantContextService.class);

	private TenantService tenantService;

	private String defaultTenantName = "";

	private String defaultPasswordPatternErrorMessage = "";
	private String defaultMailFrom = "";
	private String defaultMailFromName = "";

	private int sharedTenantId = -1;
	private List<Integer> localTenantIds;

	private CacheController<Integer, TenantContext> cache;

	private List<Class<?>> tenantResourceClasses;

	private volatile boolean destroyed = false;

	List<Class<?>> getTenantResourceClasses() {
		return tenantResourceClasses;
	}

	public int getSharedTenantId() {
		return sharedTenantId;
	}

	public List<Integer> getLocalTenantIds() {
		return localTenantIds;
	}

	public String getDefaultTenantName() {
		return defaultTenantName;
	}

	public String getPasswordPatternErrorMessage() {
		return defaultPasswordPatternErrorMessage;
	}

	public String getDefaultMailFrom() {
		return defaultMailFrom;
	}

	public String getDefaultMailFromName() {
		return defaultMailFromName;
	}

	public TenantContext getSharedTenantContext() {
		return cache.get(sharedTenantId);
	}

	public TenantContext getTenantContext(int tenantId) {
		return cache.get(tenantId);
	}

	/**
	 *
	 * @param url
	 * @return 指定のテナントが存在しない場合はNullが戻る
	 */
	public TenantContext getTenantContext(String url) {
		return cache.getByIndex(CACHE_INDEX_URL, url);
	}

//	public void clearCacheTenantContext(Tenant tenant) {
//		TenantContext tc = toTenantContext(tenant);
//		cache.notifyUpdate(tc);
//		logger.debug("update tenant:{} in cache.", tc.getTenantId());
//	}


	public void destroy() {
		destroyed = true;
		CacheStore cs = cache.getStore();
		if (cs instanceof TransactionLocalCacheStore) {
			cs = ((TransactionLocalCacheStore) cs).getBackendCacheStore();
		}
		if (cs != null) {
			List<Object> keyList = cs.keySet();
			for (Object k: keyList) {
				CacheEntry lce = cs.get(k);
				if (lce != null) {
					TenantContext tc = (TenantContext) lce.getValue();
					if (tc != null) {
						tc.invalidate();
					}
				}
			}
		}
		ServiceRegistry.getRegistry().getService(CacheService.class).invalidate(TENANT_CONTEXT_CACHE_NAMESPACE);
	}

	public void init(Config config) {

		tenantService = config.getDependentService(TenantService.class);

		if (config.getValue("sharedTenantId") != null) {
			sharedTenantId = Integer.parseInt(config.getValue("sharedTenantId"));
		}

		if (config.getValues("localTenantIds") != null) {
			localTenantIds = new ArrayList<Integer>();
			for (String ltid: config.getValues("localTenantIds")) {
				localTenantIds.add(Integer.parseInt(ltid));
			}
		}


		CacheService cs = config.getDependentService(CacheService.class);

		//TODO バージョン情報の定義と取得
		cache = new CacheController<Integer, TenantContext>(cs.getCache(TENANT_CONTEXT_CACHE_NAMESPACE), false, 1, new LoadingAdapter<Integer, TenantContext>() {

			@Override
			public TenantContext load(Integer key) {

				if (destroyed) {
					return null;
				}

				Tenant tenant = tenantService.getTenant(key);
				if (tenant == null) {
					//check shared
					if (key.intValue() == sharedTenantId) {
						logger.debug("load and cache TenantContext as Shared Tenant:{}.", key);
						return new TenantContext(sharedTenantId, "_shared", null, true);
					} else {
						return null;
					}
				} else {
					logger.debug("load and cache TenantContext:{}.", key);
					return new TenantContext(tenant, false);
				}
			}

			@Override
			public List<TenantContext> loadByIndex(int indexType,
					Object indexVal) {

				if (destroyed) {
					return null;
				}

				if (indexType != CACHE_INDEX_URL) {
					throw new IllegalArgumentException("not support indexType");
				}
				Tenant tenant = tenantService.getTenant((String) indexVal);
				if (tenant == null) {
					return null;
				} else {
					return Arrays.asList(new TenantContext(tenant, false));
				}
			}

			@Override
			public long getVersion(TenantContext value) {
				return 0;
			}

			@Override
			public Object getIndexVal(int indexType, TenantContext value) {
				if (indexType != CACHE_INDEX_URL) {
					throw new IllegalArgumentException("not support indexType");
				}
				return value.getTenantUrl();
			}

			@Override
			public Integer getKey(TenantContext val) {
				return val.getTenantId();
			}
		}, false, true);


		ServiceRegistry.getRegistry().getService(ClusterService.class).registerListener(
				new String[]{CLUSTER_EVENT_NAME_TENANT_RELOAD},
				new ClusterEventListener() {
					@Override
					public void onMessage(Message msg) {
						String tenantIdStr = msg.getParameter(CLUSTER_MESSAGE_TENANT_ID);
						if (tenantIdStr != null) {
							int tenantId = Integer.parseInt(tenantIdStr);
							reloadTenantContext(tenantId, true);
						}
					}
				});

		List<String> trcNames = config.getValues("tenantResource");
		if (trcNames != null) {
			tenantResourceClasses = new ArrayList<>();
			for (String trcName: trcNames) {
				try {
					tenantResourceClasses.add(Class.forName(trcName));
				} catch (ClassNotFoundException e) {
					throw new ServiceConfigrationException("tenantResource:" + trcName + " class not found.");
				}
			}
		}

		defaultTenantName = config.getValue("defaultTenantName");
		defaultPasswordPatternErrorMessage = config.getValue("defaultPasswordPatternErrorMessage");
		defaultMailFrom = config.getValue("defaultMailFrom");
		defaultMailFromName = config.getValue("defaultMailFromName");
	}

	public List<Integer> getAllTenantIdList() {
		if (localTenantIds == null) {
			return tenantService.getAllTenantIdList();
		} else {
			ArrayList<Integer> ret = new ArrayList<Integer>();
			ret.addAll(localTenantIds);
			if (sharedTenantId != -1) {
				//デフォルトでない場合は、共有テナントが設定されている
				ret.add(sharedTenantId);
			}
			return ret;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getCurrentLoadedTenantIdList() {
		List<?> keySet = cache.getStore().keySet();
		return (List<Integer>) keySet;
	}

	public void reloadTenantContext(int tenantId, boolean notifyOnlyLocal) {
		CacheEntry ce = cache.getStore().get(tenantId);
		if (ce != null) {
			TenantContext tc = (TenantContext) ce.getValue();
			if (tc != null) {
				tc.invalidate();
			}
			cache.notifyInvalid(tc);
		} else {
			cache.notifyInvalid(new TenantContext(tenantId));
		}

		//共有テナントの場合
		if (tenantId == sharedTenantId) {
			if (localTenantIds != null) {
				for (Integer ltid: localTenantIds) {
					//ローカルテナントが宣言されている場合は、その対象のみクリア
					CacheEntry lce = cache.getStore().get(ltid);
					if (lce != null) {
						TenantContext tc = (TenantContext) lce.getValue();
						if (tc != null) {
							tc.invalidate();
						}
						cache.notifyInvalid(tc);
					} else {
						cache.notifyInvalid(new TenantContext(ltid));
					}
				}
			} else {
				//すべてのテナントに影響あるので全クリア。
				List<Object> keyList = cache.getStore().keySet();
				for (Object k: keyList) {
					CacheEntry lce = cache.getStore().get(k);
					if (lce != null) {
						TenantContext tc = (TenantContext) lce.getValue();
						if (tc != null) {
							tc.invalidate();
						}
					}
				}
				cache.clearAll();
			}
		}

		if (!notifyOnlyLocal) {
			Message msg = new Message(CLUSTER_EVENT_NAME_TENANT_RELOAD);
			msg.addParameter(CLUSTER_MESSAGE_TENANT_ID, Integer.toString(tenantId));
			ServiceRegistry.getRegistry().getService(ClusterService.class).sendMessage(msg);
		}
	}

}

