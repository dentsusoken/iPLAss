/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.auth.authenticate.builtin.web.BasicAuthUtil;
import org.iplass.mtp.impl.auth.authenticate.builtin.web.WWWAuthenticateException;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthResourceServer.OAuthResourceServerRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.auth.oauth.OAuthClientService;
import org.iplass.mtp.impl.auth.oauth.OAuthConstants;
import org.iplass.mtp.impl.auth.oauth.OAuthResourceServerService;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.StateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebApi(name="oauth/introspect",
	accepts=RequestType.REST_FORM,
	methods=MethodType.POST,
	checkXRequestedWithHeader=false,
	privilaged=true,
	state=StateType.STATELESS,
	responseType="application/json"
)
@CommandClass(name="mtp/oauth/IntrospectCommand", displayName="OAuth2.0 Introspection Endpoint")
public class IntrospectCommand implements Command {

	static final String PARAM_TOKEN = "token";
	static final String PARAM_TOKEN_TYPE_HINT = "token_type_hint";
	
	static final String STAT_SUCCESS = "SUCCESS";
	
	private static Logger logger = LoggerFactory.getLogger(IntrospectCommand.class);
	
	private OAuthAuthorizationService authService = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
	private OAuthClientService clientService = ServiceRegistry.getRegistry().getService(OAuthClientService.class);
	private OAuthResourceServerService rsService = ServiceRegistry.getRegistry().getService(OAuthResourceServerService.class);
	
	@Override
	public String execute(RequestContext request) {
		OAuthResourceServerRuntime resourceServer = validateResourceServer(request);
		
		String token = request.getParam(PARAM_TOKEN);
		//一旦、tokenTypeはaccess_tokenに限定する（ResourceServerからの利用のみ想定。RefreshTokenはResourceServerに渡さない）
		//String tokenTypeHint = StringUtil.stripToNull(request.getParam(PARAM_TOKEN_TYPE_HINT));
		
		Object entity = introspect(request, token, resourceServer);
		ResponseBuilder res = Response.ok().type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8")).entity(entity);
		request.setAttribute(WebApiRequestConstants.DEFAULT_RESULT, res);
		return STAT_SUCCESS;
	}
	
	private Object introspect(RequestContext request, String tokenStr, OAuthResourceServerRuntime resourceServer) {
		try {
			AccessToken accessToken = authService.getAccessTokenStore().getAccessToken(tokenStr);
			if (accessToken == null) {
				return inactiveResponseEntity();
			}
			if (accessToken.getExpiresIn() <= 0) {
				return inactiveResponseEntity();
			}
			
			OAuthClientRuntime client = clientService.getRuntimeByName(accessToken.getClientId());
			if (client == null) {
				return inactiveResponseEntity();
			}
			OAuthAuthorizationRuntime server = client.getAuthorizationServer();
			if (server == null) {
				return inactiveResponseEntity();
			}
			
			return toResponseEntity(request, accessToken, server, resourceServer);
			
		} catch (RuntimeException e) {
			if (logger.isDebugEnabled()) {
				logger.error(e.toString(), e);
			} else {
				logger.error(e.toString());
			}
		}
		
		return inactiveResponseEntity();
	}
	
	
	private OAuthResourceServerRuntime validateResourceServer(RequestContext request) {
		IdPasswordCredential clientCredential = CommandUtil.clientCredential(request);
		if (clientCredential == null) {
			throw new WebApplicationException(CommandUtil.buildErrorResponse(OAuthConstants.ERROR_INVALID_CLIENT, null, null));
		}
		
		OAuthResourceServerRuntime resourceServer = rsService.getRuntimeByName(clientCredential.getId());
		if (resourceServer == null
				|| !resourceServer.validateCredential(clientCredential)) {
			if (clientCredential.getAuthenticationFactor(BasicAuthUtil.AUTH_SCHEME_BASIC) != null) {
				throw new WWWAuthenticateException(BasicAuthUtil.AUTH_SCHEME_BASIC, null, CommandUtil.errorMsg(OAuthConstants.ERROR_INVALID_CLIENT, null, null));
			} else {
				throw new WebApplicationException(CommandUtil.buildErrorResponse(OAuthConstants.ERROR_INVALID_CLIENT, null, null));
			}
		}
		
		return resourceServer;

	}
	
	private Object toResponseEntity(RequestContext request, AccessToken accessToken, OAuthAuthorizationRuntime authServer, OAuthResourceServerRuntime resourceServer) {
		Map<String, Object> res = new HashMap<>();
		res.put("active", true);
		res.put("token_type", OAuthConstants.TOKEN_TYPE_BEARER);
		res.put("scope", CommandUtil.scopeToStr(accessToken.getGrantedScopes()));
		res.put("client_id", accessToken.getClientId());
		res.put("username", accessToken.getUser().getName());
		res.put("sub", authServer.getSubjectIdentifierType().subjectId(accessToken.getUser(), clientService.getRuntimeByName(accessToken.getClientId())));
		res.put("exp", accessToken.getExpirationTime());
		res.put("iat", accessToken.getIssuedAt());
		res.put("nbf", accessToken.getNotbefore());
		res.put("aud", resourceServer.getMetaData().getName());
		res.put("iss", authServer.issuerId(request));
		
		//以下は現状未レスポンス
		//jti
		// OPTIONAL.  String identifier for the token, as defined in JWT
		// [RFC7519].	
		
		return res;
	}

	private Object inactiveResponseEntity() {
		Map<String, Object> res = new HashMap<>();
		res.put("active", false);
		return res;
	}

}
