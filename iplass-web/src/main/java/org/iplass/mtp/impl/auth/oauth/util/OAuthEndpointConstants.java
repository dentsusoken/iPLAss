/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.oauth.util;

public interface OAuthEndpointConstants {
	static final String PARAM_RESPONSE_TYPE = "response_type";
	static final String PARAM_CLIENT_ID = "client_id";
	static final String PARAM_CLIENT_SECRET = "client_secret";
	static final String PARAM_REDIRECT_URI ="redirect_uri";
	static final String PARAM_SCOPE = "scope";
	static final String PARAM_STATE = "state";
	static final String PARAM_CODE = "code";
	static final String PARAM_RESPONSE_MODE = "response_mode";
	static final String PARAM_CODE_CHALLENGE = "code_challenge";
	static final String PARAM_CODE_CHALLENGE_METHOD = "code_challenge_method";

	static final String PARAM_ERROR = "error";
	static final String PARAM_ERROR_DESCRIPTION = "error_description";
	static final String PARAM_ERROR_URI = "error_uri";
	
	static final String PARAM_NONCE = "nonce";
	static final String PARAM_PROMPT = "prompt";
	static final String PARAM_MAX_AGE = "max_age";
	//static final String PARAM_REQUEST = "request";//そのうちサポート。JARMと同時に
	
	static final String PARAM_GRANT_TYPE = "grant_type";
	static final String PARAM_CODE_VERIFIER = "code_verifier";
	static final String PARAM_ACCESS_TOKEN = "access_token";
	static final String PARAM_TOKEN_TYPE = "token_type";
	static final String PARAM_EXPIRES_IN = "expires_in";
	static final String PARAM_REFRESH_TOKEN = "refresh_token";
	static final String PARAM_ID_TOKEN = "id_token";
	
	//custom parameter for refresh token expiration
	static final String PARAM_REFRESH_TOKEN_EXPIRES_IN = "refresh_token_expires_in";
	
	//for Introspection Endpoint/Token Revocation Endpoint
	static final String PARAM_TOKEN = "token";
	static final String PARAM_TOKEN_TYPE_HINT = "token_type_hint";
	
	//RFC 9207: OAuth 2.0 Authorization Server Issuer Identification
	static final String PARAM_ISS = "iss";
}
