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
package org.iplass.mtp.impl.auth.oauth.token.opaque;

import java.io.Serializable;
import java.sql.Timestamp;

import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;

class RefreshTokenMement implements Serializable {
	private static final long serialVersionUID = 7426470500545040379L;

	static class RefreshTokenInfo implements AuthTokenInfo {
		//for internal use

		private String type;
		private String key;
		private String clientName;
		private Timestamp startDate;
		
		public RefreshTokenInfo() {
		}
		
		public RefreshTokenInfo(String clientName) {
			this.clientName = clientName;
		}
		
		public String getClientName() {
			return clientName;
		}

		public void setClientName(String clientName) {
			this.clientName = clientName;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setKey(String key) {
			this.key = key;
		}
		
		public void setStartDate(Timestamp startDate) {
			this.startDate = startDate;
		}

		@Override
		public String getType() {
			return type;
		}

		@Override
		public String getKey() {
			return key;
		}
		
		@Override
		public Timestamp getStartDate() {
			return startDate;
		}

		@Override
		public String getDescription() {
			StringBuilder sb = new StringBuilder();
			sb.append("OAuth2.0 Refresh Token for client:" + clientName);
			return sb.toString();
		}
	}
	
	private String clientMetaDataId;
	private long expires;
	private String userUniqueId;
	
	void save(RefreshTokenInfo info, long expires, String userUniqueId) {
		OAuthClientRuntime client = OAuthServiceHolder.client.getRuntimeByName(info.getClientName());
		clientMetaDataId = client.getMetaData().getId();
		this.expires = expires;
		this.userUniqueId = userUniqueId;
	}
	
	public String getClientMetaDataId() {
		return clientMetaDataId;
	}

	long getExpires() {
		return expires;
	}

	String getUserUniqueId() {
		return userUniqueId;
	}

	void fill(RefreshTokenInfo info) {
		OAuthClientRuntime ocr = OAuthServiceHolder.client.getRuntimeById(clientMetaDataId);
		if (ocr != null) {
			info.setClientName(ocr.getMetaData().getName());
		}
	}
	
}
