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

package org.iplass.mtp.impl.web;

import java.util.ArrayList;
import java.util.List;

public class CorsConfig {
	private List<String> allowOrigin;
	private boolean allowCredentials;
	
	public CorsConfig copy() {
		CorsConfig copy = new CorsConfig();
		if (allowOrigin != null) {
			copy.allowOrigin = new ArrayList<>(allowOrigin);
		}
		copy.allowCredentials = allowCredentials;
		return copy;
	}
	
	public List<String> getAllowOrigin() {
		return allowOrigin;
	}
	public void setAllowOrigin(List<String> allowOrigin) {
		this.allowOrigin = allowOrigin;
	}
	public boolean isAllowCredentials() {
		return allowCredentials;
	}
	public void setAllowCredentials(boolean allowCredentials) {
		this.allowCredentials = allowCredentials;
	}

}
