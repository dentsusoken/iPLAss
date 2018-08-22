/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.codehaus.groovy.runtime.MethodClosure;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.auth.authorize.builtin.group.GroupContext;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.query.prepared.PreparedQueryBinding;

public class AuthLimitConditionBinding extends PreparedQueryBinding {
	
	private TenantAuthorizeContext authContext;

	public AuthLimitConditionBinding(Timestamp date,
			UserBinding user, SessionBinding sessionBinding, TenantAuthorizeContext authContext) {
		super(date, user, null);
		this.authContext = authContext;
		setVariable("toGroupOid", new MethodClosure(this, "toGroupOid"));
		setVariable("session", sessionBinding);
	}

	//Entity限定条件にバインド用に
	public List<String> toGroupOid(Object values) {
		if (values == null) {
			return null;
		}
		if (values instanceof Collection<?>) {
			Collection<?> l = (Collection<?>) values;
			if (l.size() == 0) {
				return Collections.emptyList();
			}
			ArrayList<String> ret = new ArrayList<>();
			for (Object o: l) {
				GroupContext gc = authContext.getGroupContext(o.toString());
				if (gc != null) {
					ret.add(gc.getOid());
				}
			}
			return ret;
		} else if (values instanceof Object[]) {
			Object[] l = (Object[]) values;
			if (l.length == 0) {
				return Collections.emptyList();
			}
			ArrayList<String> ret = new ArrayList<>();
			for (Object o: l) {
				GroupContext gc = authContext.getGroupContext(o.toString());
				if (gc != null) {
					ret.add(gc.getOid());
				}
			}
			return ret;
		} else {
			GroupContext gc = authContext.getGroupContext(values.toString());
			if (gc != null) {
				return Arrays.asList(gc.getOid());
			} else {
				return null;
			}
		}
	}
	
	

}
