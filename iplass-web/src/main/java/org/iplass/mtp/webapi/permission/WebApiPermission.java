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

package org.iplass.mtp.webapi.permission;

import org.iplass.mtp.auth.Permission;

/**
 * WebAPIの実行権限。
 * WebAPI名と、その際のパラメータで権限を表現。
 *
 * @author K.Higuchi
 *
 */
public class WebApiPermission extends Permission {

	private final String webApiName;
	private final WebApiParameter parameter;

	public WebApiPermission(String webApiName, WebApiParameter parameter) {
		this.webApiName = webApiName;
		this.parameter = parameter;
	}

	public final String getWebApiName() {
		return webApiName;
	}

	public final WebApiParameter getParameter() {
		return parameter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((webApiName == null) ? 0 : webApiName.hashCode());
		result = prime * result
				+ ((parameter == null) ? 0 : parameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebApiPermission other = (WebApiPermission) obj;
		if (webApiName == null) {
			if (other.webApiName != null)
				return false;
		} else if (!webApiName.equals(other.webApiName))
			return false;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WebApiPermission [webApiName=" + webApiName + ", parameter=" + parameter + "]";
	}

}
