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

package org.iplass.mtp.impl.auth.authorize.builtin.action;

import org.iplass.mtp.web.actionmapping.permission.ActionParameter;

public class ActionParameterBinding {
	
	private ActionParameter actionParameter;
	
	public ActionParameterBinding(ActionParameter actionParameter) {
		this.actionParameter = actionParameter;
	}
	
	public Object getValue(String name) {
		if (actionParameter == null) {
			return null;
		}
		return actionParameter.getValue(name);
	}
	
	public boolean in(String name, Object... value) {
		Object paramValue = null;
		if (actionParameter != null) {
			paramValue = actionParameter.getValue(name);
		}
		if (value != null) {
			for (Object v: value) {
				if (v == null) {
					if (paramValue == null) {
						return true;
					}
				} else {
					if (v.equals(paramValue)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
