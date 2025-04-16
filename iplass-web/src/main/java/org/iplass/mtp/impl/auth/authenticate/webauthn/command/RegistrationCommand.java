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

import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiParamMapping;
import org.iplass.mtp.impl.auth.authenticate.webauthn.MetaWebAuthn.WebAuthnRuntime;
import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.MediaType;

@WebApi(
		name = RegistrationCommand.WEBAPI_NAME,
		accepts = { RequestType.REST_JSON },
		methods = MethodType.POST,
		cacheControlType = CacheControlType.NO_CACHE,
		paramMapping = {
				@WebApiParamMapping(name = RegistrationOptionsCommand.PARAM_DEFINITION_NAME, mapFrom = WebApiParamMapping.PATHS)
		},
		restJson = @RestJson(
				parameterName = RegistrationCommand.PARAM_PUBLIC_KEY_CREDENTIAL,
				parameterType = Reader.class),
		responseType = MediaType.APPLICATION_JSON
)
@CommandClass(name = "mtp/webauthn/RegistrationCommand", displayName = "WebAuthn Registration Process")
public class RegistrationCommand implements Command {
	public static final String WEBAPI_NAME = "webauthn/registration";
	static final String PARAM_DEFINITION_NAME = "defName";
	static final String PARAM_PUBLIC_KEY_CREDENTIAL = "publicKeyCredential";

	static final String STAT_SUCCESS = "ok";
	
	private static Logger logger = LoggerFactory.getLogger(RegistrationCommand.class);

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

		String publicKeyCredential;
		try (Reader r = (Reader) request.getAttribute(PARAM_PUBLIC_KEY_CREDENTIAL)) {
			publicKeyCredential = IOUtils.toString(r);
		} catch (Exception e) {
			throw new WebAuthnRuntimeException("Failed to read publicKeyCredential", e);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("register PublicKeyCredential: " + publicKeyCredential);
		}

		AuthContext authContext = AuthContext.getCurrentContext();
		webauthn.checkEnableRegistrationRequest(authContext);

		DefaultWebAuthnServer server = new DefaultWebAuthnServer(request, RegistrationOptionsCommand.SESSION_WEBAUTHN_STATE_REGISTRATION);
		webauthn.registrationRequest(publicKeyCredential, authContext.getUser(), authContext.getPolicyName(), server);

		return STAT_SUCCESS;
	}
}
