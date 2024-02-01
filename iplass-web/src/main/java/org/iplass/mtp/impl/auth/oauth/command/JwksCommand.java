/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.action.cache.CacheCriteria;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.auth.oauth.jwt.CertificateKeyPair;
import org.iplass.mtp.impl.auth.oauth.jwt.JwtKeyStore;
import org.iplass.mtp.impl.auth.oauth.jwt.JwtProcessor;
import org.iplass.mtp.spi.ServiceRegistry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@ActionMapping(name="oauth/jwks",
	publicAction=true,
	clientCacheType=ClientCacheType.CACHE,
	clientCacheMaxAge=3600,
	result={
		@Result(type=Type.STREAM, contentTypeAttributeName="contentType")
	},
	cacheCriteria = @CacheCriteria(
			type = CacheCriteria.Type.PARAMETER_MATCH,
			timeToLive=3600000
			)
)
@CommandClass(name="mtp/oauth/JwksCommand", displayName="JWK Set Uri", readOnly=true)
public class JwksCommand implements Command {

	static final String STAT_SUCCESS = "SUCCESS";
	
	private OAuthAuthorizationService service = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	
	@Override
	public String execute(RequestContext request) {
		
		Map<String, Object> res = new HashMap<>();
		ArrayList<Map<String, Object>> keys = new ArrayList<>();
		res.put("keys", keys);
		
		String jwkSet = null;
		JwtKeyStore keyStore = service.getJwtKeyStore();
		if (keyStore != null) {
			JwtProcessor jwtProcessor = service.getJwtProcessor();
			List<CertificateKeyPair> keyList = keyStore.list();
			for (CertificateKeyPair ckp: keyList) {
				keys.add(ckp.toPublicJwkMap(jwtProcessor.preferredAlgorithm(ckp)));
			}
			
			try {
				jwkSet = objectMapper.writeValueAsString(res);
			} catch (JsonProcessingException e) {
				throw new SystemException(e);
			}
		} else {
			jwkSet ="{\"keys\": []}";
		}
		
		try {
			request.setAttribute("streamData", jwkSet.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new SystemException(e);
		}
		request.setAttribute("contentType", "application/json;charset=utf-8");
		return STAT_SUCCESS;
	}

}
