/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authorize.builtin;

import java.util.List;

import org.iplass.mtp.impl.cache.LoadingAdapter;

public abstract class AuthorizationContextCacheLogic implements LoadingAdapter<String, BuiltinAuthorizationContext> {
	
	protected final TenantAuthorizeContext authorizeContext;
	
	public AuthorizationContextCacheLogic(TenantAuthorizeContext authorizeContext) {
		this.authorizeContext = authorizeContext;
	}
	
	@Override
	public Object getIndexVal(int index, BuiltinAuthorizationContext val) {
		return null;
	}
	@Override
	public List<BuiltinAuthorizationContext> loadByIndex(int index, Object indexVal) {
		return null;
	}
	@Override
	public long getVersion(BuiltinAuthorizationContext value) {
		//TODO バージョンの定義と実装
		return 0;
	}
	@Override
	public String getKey(BuiltinAuthorizationContext val) {
		return val.getContextName();
	}
}
