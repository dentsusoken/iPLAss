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

package org.iplass.mtp.impl.auth.authenticate.builtin;

import java.sql.Timestamp;

public class Password {
	private final int tenantId;
	private final String uid;
	private final String convertedPassword;
	private final String salt;
	private final Timestamp updateDate;
	
	public Password(int tenantId, String uid, String convertedPassword,
			String salt, Timestamp updateDate) {
		super();
		this.tenantId = tenantId;
		this.uid = uid;
		this.convertedPassword = convertedPassword;
		this.salt = salt;
		this.updateDate = updateDate;
	}
	
	public int getTenantId() {
		return tenantId;
	}
	public String getUid() {
		return uid;
	}
	public String getConvertedPassword() {
		return convertedPassword;
	}
	public String getSalt() {
		return salt;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	
}
