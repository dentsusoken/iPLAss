/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.token;

import java.sql.Timestamp;

public class AuthToken {
	
	public static final String SEPARATOR = ".";
	
	private int tenantId;
	private String uniqueKey;
	private String series;
	private String token;
	
	private String policyName;
	private Timestamp startDate;
	
	private String type;
	private Object details;
	
	public AuthToken() {
	}
	
	public AuthToken(String tokenEncoded) {
		if (tokenEncoded != null) {
			setTokenEncoded(tokenEncoded);
		}
	}
	
	public AuthToken(int tenantId, String type, String uniqueKey, String series, String token, String policyName, Timestamp startDate, Object details) {
		this.tenantId = tenantId;
		this.type = type;
		this.uniqueKey = uniqueKey;
		this.series = series;
		this.token = token;
		this.policyName = policyName;
		this.startDate = startDate;
		this.details = details;
	}
	
	public String getTokenEncoded() {
		return type + SEPARATOR + series + SEPARATOR + token;
	}
	
	public void setTokenEncoded(String seriesToken) {
		int indexOf = seriesToken.indexOf(SEPARATOR);
		if (indexOf < 0) {
			return;
		}
		type = seriesToken.substring(0, indexOf);
		
		seriesToken = seriesToken.substring(indexOf + SEPARATOR.length());
		indexOf = seriesToken.indexOf(SEPARATOR);
		if (indexOf < 0) {
			return;
		}
		
		series = seriesToken.substring(0, indexOf);
		token = seriesToken.substring(indexOf + SEPARATOR.length());
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getDetails() {
		return details;
	}

	public void setDetails(Object details) {
		this.details = details;
	}
	public int getTenantId() {
		return tenantId;
	}
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

}
