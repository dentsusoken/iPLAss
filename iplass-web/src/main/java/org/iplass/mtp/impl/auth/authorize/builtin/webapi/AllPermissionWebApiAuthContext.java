/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authorize.builtin.webapi;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.authenticate.AnonymousUserContext;
import org.iplass.mtp.impl.auth.authenticate.TemporaryUserContext;
import org.iplass.mtp.impl.webapi.auth.WebApiAuthContext;

class AllPermissionWebApiAuthContext implements WebApiAuthContext {

	private boolean withAnonymous;

	AllPermissionWebApiAuthContext(boolean withAnonymous) {
		this.withAnonymous = withAnonymous;
	}

	@Override
	public boolean isPermit(Permission permission, AuthContextHolder user) {
		if (withAnonymous) {
			return true;
		} else {
			if(user.getUserContext() instanceof AnonymousUserContext){
				return false;
			}else if(user.getUserContext() instanceof TemporaryUserContext){
				return false;
			}else{
				return true;
			}
		}
	}

	@Override
	public boolean isResultCacheable(Permission permission) {
		return true;
	}

}
