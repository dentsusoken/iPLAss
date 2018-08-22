/*
 * Copyright 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.cleaner;

import java.util.List;

import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;

/**
 * 認証モジュールが保持するデータのメンテナンス用バッチ
 */
public class AuthProviderCleaner extends MtpCuiBase {

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private static AuthService authService = ServiceRegistry.getRegistry().getService(AuthService.class);

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
			(new AuthProviderCleaner(tenantId)).clean();
		} else {
			//全テナントを対象
			List<TenantInfo> tenants = getValidTenantInfoList();
			if (tenants != null) {
				for (TenantInfo t: tenants) {
					(new AuthProviderCleaner(t.getId())).clean();
				}
			}
		}
	}

	/**
	 * 対象のテナントIDを指定します。
	 *
	 * @param tenantId 対象のテナントID
	 */
	public AuthProviderCleaner(int tenantId) {
		setTenantId(tenantId);

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
			ExecuteContext.initContext(new ExecuteContext(tenantContextService.getTenantContext(tenantId)));

			logArguments();

			AuthenticationProvider[] aps = authService.getAuthenticationProviders();
			if (aps != null) {
				for (AuthenticationProvider ap: aps) {
					final AuthenticationProvider apFinal = ap;
					Transaction.required(t -> {
							apFinal.cleanupData();
							return null;
					});
				}
			}

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
}
