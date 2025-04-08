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

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.token.AuthTokenInfoList;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.webauthn.store.CredentialRecordHandler;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.MediaType;

@WebApi(
		name = DeregistrationCommand.WEBAPI_NAME,
		accepts = { RequestType.REST_JSON },
		methods = MethodType.POST,
		restJson = @RestJson(parameterName = "param"),
		cacheControlType = CacheControlType.NO_CACHE,
		responseType = MediaType.APPLICATION_JSON
)
@CommandClass(name = "mtp/webauthn/DeregistrationCommand", displayName = "WebAuthn Deregistration Process")
public class DeregistrationCommand implements Command {
	public static final String WEBAPI_NAME = "webauthn/deregistration";
	public static final String PARAM_KEY_ID = "keyId";

	static final String STAT_SUCCESS = "ok";

	private static Logger logger = LoggerFactory.getLogger(DeregistrationCommand.class);

	@Override
	public String execute(RequestContext request) {
		if (!ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantAuthInfo.class).isUseWebAuthn()) {
			throw new WebAuthnRuntimeException("WebAuthn is not enabled");
		}

		String keyId = request.getParam(PARAM_KEY_ID);
		if (StringUtil.isEmpty(keyId)) {
			throw new WebAuthnRuntimeException("credentialId is required");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("deregister WebAuthn PublicKeyCredential:keyId=" + keyId);
		}

		AuthContext authContext = AuthContext.getCurrentContext();
		if (!authContext.isAuthenticated()) {
			throw new WebAuthnRuntimeException("not authenticated");
		}

		AuthTokenInfoList authTokenInfoList = authContext.getAuthTokenInfos();
		if (authTokenInfoList.get(CredentialRecordHandler.TYPE_WEBAUTHN_CREDENTIAL_DEFAULT, keyId) != null) {
			authTokenInfoList.remove(CredentialRecordHandler.TYPE_WEBAUTHN_CREDENTIAL_DEFAULT, keyId);
		}

		return STAT_SUCCESS;
	}

}
