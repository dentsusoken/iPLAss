/*
 * Copyright 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.cleaner;

import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.lob.LobHandler;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpSilentBatch;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TempLobCleaner extends MtpSilentBatch {

	private static Logger logger = LoggerFactory.getLogger(TempLobCleaner.class);

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);

	private int tenantId;

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
		int tenantId = -1;
		if (args != null && args.length > 0 && StringUtil.isNotEmpty(args[0])) {
			tenantId = Integer.parseInt(args[0]);
		}

		try {
			if (tenantId >= 0) {
				(new TempLobCleaner(tenantId)).clean();
			} else {
				List<TenantInfo> tenants = getValidTenantInfoList();
				if (tenants != null) {
					for (TenantInfo t: tenants) {
						(new TempLobCleaner(t.getId())).clean();
					}
				}
			}
		} finally {
			// リソース破棄
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	/**
	 * 対象のテナントIDを指定します。
	 *
	 * @param tenantId 対象のテナントID
	 */
	public TempLobCleaner(int tenantId) {
		setTenantId(tenantId);
	}

	/**
	 * テナント内のテンポラリーLOBデータを削除します。
	 *
	 * @return boolean 成功：true 失敗：false
	 * @throws Exception
	 */
	public boolean clean() throws Exception {

		setSuccess(false);

		clearLog();

		return executeTask(null, (param) -> {

			return ExecuteContext.executeAs(tenantContextService.getTenantContext(tenantId), () -> {

				logArguments();

				//テンポラリのBinaryReference削除
				Transaction.required(t -> {
						LobHandler.cleanTemporaryBinaryData();
				});

				//参照カウント0のバイナリデータ削除
				Transaction.required(t -> {
						LobHandler.cleanLobData();
				});

				setSuccess(true);
				return isSuccess();
			});
		});
	}

	/**
	 * パラメータ値ログ出力
	 *
	 */
	private void logArguments() {
		logInfo("■Execute Argument");
		logInfo("\ttenant id :" + getTenantId());
		logInfo("");
	}

	/**
	 * tenantIdを取得します。
	 * @return tenantId
	 */
	public int getTenantId() {
	    return tenantId;
	}

	/**
	 * tenantIdを設定します。
	 * @param tenantId tenantId
	 */
	public void setTenantId(int tenantId) {
	    this.tenantId = tenantId;
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
