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

import org.iplass.mtp.webapi.permission.WebApiParameter;

public class WebApiParameterBinding {

	private WebApiParameter webApiParameter;

	public WebApiParameterBinding(WebApiParameter webApiParameter) {
		this.webApiParameter = webApiParameter;
	}

	public Object getValue(String name) {
		if (webApiParameter == null) {
			return null;
		}
		return webApiParameter.getValue(name);
	}

	public boolean in(String name, Object... value) {
		Object paramValue = null;
		if (webApiParameter != null) {
			paramValue = webApiParameter.getValue(name);
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
