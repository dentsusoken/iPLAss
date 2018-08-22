/*
 * Copyright 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.cleaner;

import org.iplass.mtp.impl.cache.store.builtin.RdbCacheStoreFactory;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpCuiBase;

/**
 * RdbCacheStoreの無効データのメンテナンス用バッチ
 */
public class RdbCacheCleaner extends MtpCuiBase {

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);

	/**
	 * <p>引数について</p>
	 * <ol>
	 * <li>テナントID：対象テナントID（-1の場合、全テナントが対象になります）</li>
	 * </ol>
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new RdbCacheCleaner().clean();
	}

	/**
	 * 対象のテナントIDを指定します。
	 *
	 * @param tenantId 対象のテナントID
	 */
	public RdbCacheCleaner() {

		LogListner loggingListner = getLoggingLogListner();
		addLogListner(loggingListner);
	}

	/**
	 *
	 * @return boolean 成功：true 失敗：false
	 * @throws Exception
	 */
	public boolean clean() throws Exception {

		setSuccess(false);

		clearLog();

		try {
			ExecuteContext.initContext(new ExecuteContext(tenantContextService.getSharedTenantContext()));
			RdbCacheStoreFactory.deleteInvalidRecord();
			setSuccess(true);
		} catch (Throwable e) {
			logError("An error has occurred. : " + e.getMessage());
			e.printStackTrace();
		} finally {
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));

			ExecuteContext.initContext(null);
		}
		return isSuccess();

	}

}
