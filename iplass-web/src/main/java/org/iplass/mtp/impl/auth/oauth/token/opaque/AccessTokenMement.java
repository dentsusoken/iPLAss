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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.oauth.AccessTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;

class AccessTokenMement implements Serializable {
	private static final long serialVersionUID = -7300870311238433196L;

	private String clientMetaDataId;
	private List<String> grantedScopes;
	private long expires;
	private String resouceOwnerId;
	private User user;
	
	//新規生成時の一時的なプレースホルダ
	private transient AuthToken refreshToken;
	
	void save(AccessTokenInfo info, long expires, String resouceOwnerId, User user) {
		OAuthClientRuntime client = OAuthServiceHolder.client.getRuntimeByName(info.getClientName());
		clientMetaDataId = client.getMetaData().getId();
		if (info.getGrantedScopes() != null) {
			grantedScopes = new ArrayList<>(info.getGrantedScopes());
		}
		
		this.expires = expires;
		this.resouceOwnerId = resouceOwnerId;
		this.user = user;
	}
	
	void fill(AccessTokenInfo info) {
		OAuthClientRuntime ocr = OAuthServiceHolder.client.getRuntimeById(clientMetaDataId);
		if (ocr != null) {
			info.setClientName(ocr.getMetaData().getName());
		}
		if (grantedScopes != null) {
			info.setGrantedScopes(new ArrayList<>(grantedScopes));
		}
	}
	
	public AuthToken getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(AuthToken refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getClientMetaDataId() {
		return clientMetaDataId;
	}

	long getExpires() {
		return expires;
	}
	
	public String getResouceOwnerId() {
		return resouceOwnerId;
	}

	User getUser() {
		return user;
	}
	
	List<String> getGrantedScopes() {
		return grantedScopes;
	}

}
