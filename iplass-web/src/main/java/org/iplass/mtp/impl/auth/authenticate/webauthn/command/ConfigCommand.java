/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.webauthn.command;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * WebAuthn の設定情報を取得するコマンド
 * @author SEKIGUCHI Naoya
 */
@WebApi(
		name = ConfigCommand.WEBAPI_NAME,
		accepts = { RequestType.REST_JSON },
		methods = MethodType.GET,
		cacheControlType = CacheControlType.NO_CACHE,
		results = {
				ConfigCommand.USE_WEB_AUTHN,
				ConfigCommand.WEB_AUTHN_DEFINITION_NAME
		})
@CommandClass(name = "mtp/webauthn/ConfigCommand", displayName = "Get WebAuthn Config")
public class ConfigCommand implements Command {
	/** WebAPI 名 */
	public static final String WEBAPI_NAME = "webauthn/config";
	/** WebAuthn 利用可能判定 */
	public static final String USE_WEB_AUTHN = "useWebAuthn";
	/** WebAuthn 定義名 */
	public static final String WEB_AUTHN_DEFINITION_NAME = "definitionName";

	/** コマンド結果 */
	private static final String STAT_SUCCESS = "ok";

	@Override
	public String execute(RequestContext request) {
		var tenantAuthInfo = ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantAuthInfo.class);
		boolean useWebAuthn = getUseWebAuthn(tenantAuthInfo);

		request.setAttribute(USE_WEB_AUTHN, useWebAuthn);
		if (useWebAuthn) {
			// WebAuthn が有効な場合だけ定義名を返却する
			request.setAttribute(WEB_AUTHN_DEFINITION_NAME, getWebAuthnDefinitionName(tenantAuthInfo));
		}

		return STAT_SUCCESS;
	}

	/**
	 * WebAuthn が利用可能か判定し結果を取得する
	 * <p>
	 * WebAuthn の利用判定は以下の情報を確認し、すべての条件に一致する場合利用可能と判定する。
	 * </p>
	 * <ul>
	 * <li>テナント設定: 「WebAuthn（PassKey）を利用するか」が「利用する」に設定されている</li>
	 * <li>認証ポリシー: 「WebAuthn（PassKey）定義」に「定義が設定されている」状態</li>
	 * </ul>
	 * @param tenantAuthInfo テナント認証設定
	 * @return WebAuthn が利用可能な場合、 true を返却する。
	 */
	private boolean getUseWebAuthn(TenantAuthInfo tenantAuthInfo) {
		var webAuthnDefinitionList = AuthContextHolder.getAuthContext().getPolicy().getMetaData().getWebAuthnDefinition();

		return tenantAuthInfo.isUseWebAuthn() && null != webAuthnDefinitionList && !webAuthnDefinitionList.isEmpty();
	}

	/**
	 * WebAuthn 定義名を取得する
	 * @param tenantAuthInfo テナント認証設定
	 * @return WebAuthn 定義名
	 */
	public String getWebAuthnDefinitionName(TenantAuthInfo tenantAuthInfo) {
		return tenantAuthInfo.getWebAuthnDefinitonName();
	}
}
