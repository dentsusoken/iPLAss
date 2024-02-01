/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.auth.oauth;

import java.sql.Timestamp;
import java.util.List;

import org.iplass.mtp.auth.token.AuthTokenInfo;

/*
 * OAuth2のアクセストークンを表現するAuthTokenInfoです。
 * 
 */
public class AccessTokenInfo implements AuthTokenInfo {
	
	private String type;
	private String key;
	private String clientName;
	private Timestamp startDate;
	private List<String> grantedScopes;
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<String> getGrantedScopes() {
		return grantedScopes;
	}

	public void setGrantedScopes(List<String> grantedScopes) {
		this.grantedScopes = grantedScopes;
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
		//TODO 多言語化
		StringBuilder sb = new StringBuilder();
		sb.append("OAuth2.0 Access Token for client:" + clientName);
		if (grantedScopes != null && !grantedScopes.isEmpty()) {
			sb.append(" and scopes:");
			for (int i = 0; i < grantedScopes.size(); i++) {
				if (i != 0) {
					sb.append(", ");
				}
				sb.append(grantedScopes.get(i));
			}
			
		}
		return sb.toString();
	}

}
