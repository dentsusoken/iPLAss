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

import java.sql.Timestamp;
import java.util.List;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.auth.webauthn.WebAuthnAuthenticatorInfo;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnService;
import org.iplass.mtp.impl.auth.authenticate.webauthn.store.CredentialRecordHandler;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;

import jakarta.ws.rs.core.MediaType;

@WebApi(name=KeyListCommand.WEBAPI_NAME,
		methods = MethodType.GET,
		cacheControlType = CacheControlType.NO_CACHE,
		responseType = MediaType.APPLICATION_JSON,
		results = {
				KeyListCommand.RESULT_LIST,
				KeyListCommand.RESULT_RGISTRATION_LIMIT
		}
)
@CommandClass(name = "mtp/webauthn/KeyListCommand", displayName = "WebAuthn PublicKey Credential List")
public class KeyListCommand implements Command {
	public static final String WEBAPI_NAME = "webauthn/keys";
	static final String STAT_SUCCESS = "ok";
	public static final String RESULT_LIST = "list";
	public static final String RESULT_RGISTRATION_LIMIT = "registrationLimit";

	private WebAuthnService webAuthnService = ServiceRegistry.getRegistry().getService(WebAuthnService.class);

	@Override
	public String execute(RequestContext request) {
		if (!ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantAuthInfo.class).isUseWebAuthn()) {
			throw new WebAuthnRuntimeException("WebAuthn is not enabled");
		}

		AuthContext authContext = AuthContext.getCurrentContext();
		if (!authContext.isAuthenticated()) {
			throw new WebAuthnRuntimeException("not authenticated");
		}
		List<AuthTokenInfo> authTokenInfoList = authContext.getAuthTokenInfos().getList(CredentialRecordHandler.TYPE_WEBAUTHN_CREDENTIAL_DEFAULT);
		List<WebAuthnAuthenticatorInfoResponse> list = authTokenInfoList.stream().map(WebAuthnAuthenticatorInfoResponse::new).toList();

		request.setAttribute(RESULT_LIST, list);
		request.setAttribute(RESULT_RGISTRATION_LIMIT, webAuthnService.getRegistrationLimitOfAuthenticatorPerUser());

		return STAT_SUCCESS;
	}

	static class WebAuthnAuthenticatorInfoResponse implements WebAuthnAuthenticatorInfo {

		private WebAuthnAuthenticatorInfo info;

		WebAuthnAuthenticatorInfoResponse(AuthTokenInfo info) {
			this.info = (WebAuthnAuthenticatorInfo) info;
		}

		@Override
		public String getType() {
			return info.getType();
		}

		@Override
		public String getKey() {
			return info.getKey();
		}

		@Override
		public String getDescription() {
			return info.getDescription();
		}

		@Override
		public Timestamp getStartDate() {
			return info.getStartDate();
		}

		@Override
		public String getAuthenticatorDisplayName() {
			return info.getAuthenticatorDisplayName();
		}

		@Override
		public Timestamp getLastLoginDate() {
			return info.getLastLoginDate();
		}
	}

}
