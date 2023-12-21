/*
 * Copyright 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.cleaner;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.tools.clean.RecycleBinCleanService;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpSilentBatch;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ごみ箱データ削除ツール
 *
 * ごみ箱内のデータを定期的に削除するために使用する。
 * ServiceConfigに設定されたpurgeTargetDateの日付よりも前の更新日をもつごみ箱内データを削除する。
 *
 * @author lis71n
 *
 */
public class RbCleaner extends MtpSilentBatch {

	private static Logger logger = LoggerFactory.getLogger(RbCleaner.class);

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private static EntityService entityHandlerService = ServiceRegistry.getRegistry().getService(EntityService.class);
	private RecycleBinCleanService recycleBinCleanService = ServiceRegistry.getRegistry().getService(RecycleBinCleanService.class);
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

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -entityHandlerService.getPurgeTargetDate());
		Timestamp purgeTargetDate = new Timestamp(cal.getTimeInMillis());

		if (args != null && args.length > 0 && StringUtil.isNotEmpty(args[0])) {
			tenantId = Integer.parseInt(args[0]);
		}
		
		try {
			if (tenantId >= 0) {
				(new RbCleaner(tenantId)).clean(purgeTargetDate);
			} else {
				List<TenantInfo> tenants = getValidTenantInfoList();
				if (tenants != null) {
					for (TenantInfo t: tenants) {
						(new RbCleaner(t.getId())).clean(purgeTargetDate);
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
	public RbCleaner(int tenantId) {
		setTenantId(tenantId);
	}

	/**
	 * ごみ箱内のデータを削除します。
	 *
	 * @return boolean 成功：true 失敗：false
	 * @throws Exception
	 */
	public boolean clean(final Timestamp purgeTargetDate) throws Exception {
		setSuccess(false);

		clearLog();

		return executeTask(null, (param) -> {

			return ExecuteContext.executeAs(tenantContextService.getTenantContext(tenantId), () -> {

				logArguments();

				recycleBinCleanService.clean(purgeTargetDate, null);

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
