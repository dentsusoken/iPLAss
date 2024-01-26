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

package org.iplass.mtp.impl.auth.authenticate.oidc.jwks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.iplass.mtp.impl.auth.authenticate.oidc.OpenIdConnectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteJwks extends Jwks {
	
	private static Logger logger = LoggerFactory.getLogger(RemoteJwks.class);
	
	private String jwksEndpoint;
	private volatile JwksHolder jwks;

	public RemoteJwks(String jwksEndpoint, OpenIdConnectService service) {
		super(service);
		this.jwksEndpoint = jwksEndpoint;
	}

	@Override
	public Map<String, Object> get(String kid) {
		Map<String, Object> ret = getJwk(kid);
		if (ret == null) {
			synchronized (this) {
				ret = getJwk(kid);
				if (ret == null) {
					refreshJwks();
					ret = getJwk(kid);
				}
			}
		}
		return ret;
	}
	
	private void refreshJwks() {
		HttpClient client = service.getHttpClient();
		try {
			String content = null;
			HttpGet get = new HttpGet(jwksEndpoint);
			HttpResponse res = client.execute(get);
			try {
				if (res.getStatusLine().getStatusCode() != 200) {
					throw new IllegalStateException("http response error:" + res.getStatusLine().toString());
				}
				HttpEntity entity = res.getEntity();
				content = EntityUtils.toString(entity);
			} finally {
				get.releaseConnection();
			}
			JwksHolder newJwks = new JwksHolder();
			newJwks.jwks = toJwksMap(content);
			if (service.getJwksCacheLifetimeMinutes() >= 0) {
				newJwks.ttl = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(service.getJwksCacheLifetimeMinutes());
			}
			jwks = newJwks;
		} catch (IOException | RuntimeException e) {
			logger.error("can not get jwks from endpoint:" + jwksEndpoint + " may be retry after a while...", e);
		}
	}
	
	private Map<String, Map<String, Object>> getJwksMap() {
		JwksHolder jwksref = jwks;
		if (jwksref == null || (jwksref.ttl != -1 && jwksref.ttl < System.currentTimeMillis())) {
			return null;
		}
		return jwksref.jwks;
	}
	
	private Map<String, Object> getJwk(String kid) {
		Map<String, Map<String, Object>> jwksMap = getJwksMap();
		if (jwksMap == null) {
			return null;
		} else {
			return jwksMap.get(kid);
		}
	}
	
	@Override
	public List<String> kidList() {
		ArrayList<String> ret = new ArrayList<>();
		Map<String, Map<String, Object>> map = getJwksMap();
		if (map == null) {
			synchronized (this) {
				map = getJwksMap();
				if (map == null) {
					refreshJwks();
					map = getJwksMap();
				}
			}
		}
		
		map.forEach((k, v) -> {
			ret.add((String) v.get(JWK_PARAM_KID));
		});
		return ret;
	}
	
	class JwksHolder {
		Map<String, Map<String, Object>> jwks;
		long ttl = -1L;
	}

}
