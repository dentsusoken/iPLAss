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
package org.iplass.mtp.impl.auth.oauth.token.opaque;

import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.token.RefreshToken;

public class OpaqueRefreshToken extends RefreshToken {
	
	private String series;
	private String tokenEncoded;
	private long expires;
	private long expiresIn;
	private String clientId;
	
	public OpaqueRefreshToken(OAuthClientRuntime client, RefreshTokenMement mement, String series, String tokenEncoded) {
		this.series = series;
		this.tokenEncoded = tokenEncoded;
		this.expires = mement.getExpires();
		expiresIn = (this.expires - System.currentTimeMillis()) / 1000;
		clientId = client.getMetaData().getName();
	}
	
	public String getSeries() {
		return series;
	}

	@Override
	public String getTokenEncoded() {
		return tokenEncoded;
	}

	@Override
	public long getExpiresIn() {
		return expiresIn;
	}

	@Override
	public String getClientId() {
		return clientId;
	}


}
