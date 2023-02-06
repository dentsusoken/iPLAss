/*
 * Copyright 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.cleaner;

import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpSilentBatch;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * テンポラリーユーザー削除ツール
 *
 * アプリにて作成されたテンポラリーユーザーを定期的削除するために使用する。
 *
 * @author lis71n
 *
 */
public class TempUserCleaner extends MtpSilentBatch {

	private static Logger logger = LoggerFactory.getLogger(TempUserCleaner.class);

	private TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
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
			(new TempUserCleaner(tenantId)).clean();
		} else {
			List<TenantInfo> tenants = getValidTenantInfoList();
			if (tenants != null) {
				for (TenantInfo t: tenants) {
					(new TempUserCleaner(t.getId())).clean();
				}
			}
		}
	}

	/**
	 * 対象のテナントIDを指定します。
	 *
	 * @param tenantId 対象のテナントID
	 */
	public TempUserCleaner(int tenantId) {
		setTenantId(tenantId);
	}

	/**
	 * テナント内のテンポラリーユーザーを削除します。
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
					//有効期限切れのテンポラリーユーザー情報検索(有効終了日が現在日付より小さい場合且つ、テンポラリーフラグがtrueの場合)
					Query query = new Query()
										.selectAll(User.DEFINITION_NAME, true, false)
										.where(new And()
												.eq(User.TEMPORARY_FLG, true)
												.lt(User.END_DATE, new java.sql.Date(ExecuteContext.getCurrentContext().getCurrentTimestamp().getTime())));
					org.iplass.mtp.entity.SearchResult<Entity> user = em.searchEntity(query);

					//テンポラリーユーザー物理削除
					for(Entity entity : user.getList() ){
						em.delete(entity, new DeleteOption(false));
					}
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
