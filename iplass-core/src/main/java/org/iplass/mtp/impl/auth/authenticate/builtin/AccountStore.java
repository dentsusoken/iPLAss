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
import java.util.List;

import org.iplass.mtp.spi.Config;

public interface AccountStore {
	
	public void inited(BuiltinAuthenticationProvider provider, Config config);

	public void updateAccountLoginStatus(BuiltinAccount account);

	public void updatePassword(Password pass, String updateUser);

	public void registAccount(BuiltinAccount account, String registId);

	public void updateAccount(BuiltinAccount account, String updateUser);

	public void removeAccount(int tenantId, String accountId);

	public BuiltinAccount getAccount(int tenantId, String accountId);

	public BuiltinAccount getAccountFromOid(int tenantId, String oid);
	
	public void addPasswordHistory(Password hi);
	
	public List<Password> getPasswordHistory(int tenantId, String accountId);
	
	public void deletePasswordHistory(int tenantId, String accountId);
	
	public void deletePasswordHistory(int tenantId, String accountId, Timestamp dateBefore);
	
	public void updatePasswordHistoryAccountId(int tenantId, String oldAccountId, String newAccountId);

	public void resetLoginErrorCnt(BuiltinAccount account);
	
}
