/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.configfile;

import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.UserEntityResolver;

public class ConfigFileUserEntityResolver implements UserEntityResolver {
	
	@Override
	public void inited(AuthService service, AuthenticationProvider provider) {
	}

	@Override
	public User searchUser(AccountHandle account) {
		User user = new User();
		user.setOid(account.getUnmodifiableUniqueKey());
		user.setAccountId(account.getCredential().getId());
		user.setName(account.getCredential().getId());
		if (account.getAttributeMap() != null) {
			for (Map.Entry<String, Object> e: account.getAttributeMap().entrySet()) {
				user.setValue(e.getKey(), e.getValue());
			}
		}
		return user;
	}

	@Override
	public String getUnmodifiableUniqueKeyProperty() {
		return Entity.OID;
	}

}
