/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webhook;

import java.sql.Timestamp;

import org.iplass.mtp.auth.token.AuthTokenInfo;

public class WebhookAuthTokenInfo implements AuthTokenInfo {
	private String type;
	private String key;
	private Timestamp startDate;
	public WebhookAuthTokenInfo(){
		
	}
	public WebhookAuthTokenInfo(String type, String key){
		this.type=type;
		this.key=key;
	}
	@Override
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public Timestamp getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	
	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("The ");
		sb.append(type);
		sb.append(" Token for WebhookTemplate:");
		sb.append(key);
		return sb.toString();
	}

}
