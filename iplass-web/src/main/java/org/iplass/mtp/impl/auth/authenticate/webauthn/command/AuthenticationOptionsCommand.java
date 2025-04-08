/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.command.annotation.webapi.WebApiParamMapping;
import org.iplass.mtp.impl.auth.authenticate.webauthn.MetaWebAuthn.WebAuthnRuntime;
import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@WebApi(
		name = AuthenticationOptionsCommand.WEBAPI_NAME,
		accepts = { RequestType.REST_FORM, RequestType.REST_JSON },
		methods = MethodType.POST,
		cacheControlType = CacheControlType.NO_CACHE,
		publicWebApi = true,
		paramMapping = {
				@WebApiParamMapping(name = AuthenticationOptionsCommand.PARAM_DEFINITION_NAME, mapFrom = WebApiParamMapping.PATHS)
		},
		responseType = MediaType.APPLICATION_JSON
)
@CommandClass(name = "mtp/webauthn/AuthenticationOptionsCommand", displayName = "WebAuthn Authentication Options")
public class AuthenticationOptionsCommand implements Command {
	public static final String WEBAPI_NAME = "webauthn/authopt";
	static final String PARAM_DEFINITION_NAME = "defName";
	static final String STAT_SUCCESS = "ok";
	public static final String SESSION_WEBAUTHN_STATE_AUTH = "org.iplass.mtp.webauthn.auth.state";

	private static Logger logger = LoggerFactory.getLogger(AuthenticationOptionsCommand.class);

	private WebAuthnService service = ServiceRegistry.getRegistry().getService(WebAuthnService.class);

	@Override
	public String execute(RequestContext request) {
		if (!ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantAuthInfo.class).isUseWebAuthn()) {
			throw new WebAuthnRuntimeException("WebAuthn is not enabled");
		}
		String defName = StringUtil.stripToNull(request.getParam(PARAM_DEFINITION_NAME));
		WebAuthnRuntime webauthn = service.getOrDefault(defName);
		if (webauthn == null) {
			throw new WebAuthnRuntimeException("No WebAuthn Definition:" + defName);
		}

		//認証のためなのでUserVerificationは必須とする
		DefaultWebAuthnServer serverParam = new DefaultWebAuthnServer(request, SESSION_WEBAUTHN_STATE_AUTH);
		String options = webauthn.publicKeyCredentialRequestOptions(serverParam, true);
		ResponseBuilder builder = Response.ok(options).type(MediaType.APPLICATION_JSON);
		request.setAttribute(WebApiRequestConstants.DEFAULT_RESULT, builder);

		if (logger.isDebugEnabled()) {
			logger.debug("PublicKeyCredentialGetOptions: " + options);
		}

		return STAT_SUCCESS;
	}

}
