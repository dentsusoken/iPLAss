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
package org.iplass.mtp.impl.auth.oauth.token.remote;

import org.iplass.mtp.auth.User;

class IntroResponse {
	public Integer tenant_id;
	public String sub;
	public String tenant_name;
	public String iss;
	public boolean active;
	public String token_type;
	public String client_id;
	public String aud;
	public User resource_owner;
	public Long nbf;
	public String scope;
	public Long exp;
	public Long iat;
	public String username;
}
