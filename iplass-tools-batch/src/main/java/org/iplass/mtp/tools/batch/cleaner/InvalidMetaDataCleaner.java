/*
 * Copyright 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.cleaner;

import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpSilentBatch;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 無効なメタデータを物理削除します。
 * Entityについては、関連データも全て物理削除します。
 */
public class InvalidMetaDataCleaner extends MtpSilentBatch {

	private static Logger logger = LoggerFactory.getLogger(InvalidMetaDataCleaner.class);

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private static MetaDataRepository repository = ServiceRegistry.getRegistry().getService(MetaDataRepository.class);
	private static EntityService ehService = ServiceRegistry.getRegistry().getService(EntityService.class);

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
		if (tenantId >= 0) {
			(new InvalidMetaDataCleaner(tenantId)).clean();
		} else {
			//全テナントを対象
			List<TenantInfo> tenants = getValidTenantInfoList();
			if (tenants != null) {
				for (TenantInfo t: tenants) {
					(new InvalidMetaDataCleaner(t.getId())).clean();
				}
			}
		}
	}

	/**
	 * 対象のテナントIDを指定します。
	 *
	 * @param tenantId 対象のテナントID
	 */
	public InvalidMetaDataCleaner(int tenantId) {
		setTenantId(tenantId);
	}

	/**
	 * 無効なメタデータを物理削除します。
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

				Transaction.required(t -> {
					List<MetaDataEntryInfo> invalidEntries = MetaDataContext.getContext().invalidDefinitionList("/");
					if (invalidEntries.size() > 0) {
						logInfo("purge " + invalidEntries.size() + " invalid metadata.");
					} else {
						logInfo("There is no invalid metadata.");
					}

					invalidEntries.forEach(entry -> {

						if (entry.getPath() != null && entry.getPath().startsWith(EntityService.ENTITY_META_PATH)) {
							//Entityの判断はPathの先頭一致で行う
							//(当初OBJ_DATAの件数チェックで行おうとしたが件数が多い場合のレスポンスを考慮)
							ehService.purgeById(entry.getId());
						}

						//メタデータ側を物理削除(Entityデータが正常に削除された後に実施。エラー時にリトライできるように)
						repository.purgeById(tenantId, entry.getId());
					});
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
