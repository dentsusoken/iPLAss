/*
 * Copyright 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.cleaner;

import org.iplass.mtp.impl.cache.store.builtin.RdbCacheStoreFactory;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpSilentBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RdbCacheStoreの無効データのメンテナンス用バッチ
 */
public class RdbCacheCleaner extends MtpSilentBatch {

	private static Logger logger = LoggerFactory.getLogger(RdbCacheCleaner.class);

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);

	public static void main(String[] args) throws Exception {
		try {
			new RdbCacheCleaner().clean();
		} finally {
			// リソース破棄
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	public RdbCacheCleaner() {
	}

	/**
	 *
	 * @return boolean 成功：true 失敗：false
	 * @throws Exception
	 */
	public boolean clean() throws Exception {

		setSuccess(false);

		clearLog();

		return executeTask(null, (param) -> {

			return ExecuteContext.executeAs(tenantContextService.getSharedTenantContext(), () -> {

				RdbCacheStoreFactory.deleteInvalidRecord();
				setSuccess(true);
				return isSuccess();
			});
		});
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
