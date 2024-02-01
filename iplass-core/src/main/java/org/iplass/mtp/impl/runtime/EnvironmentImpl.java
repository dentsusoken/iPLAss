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
package org.iplass.mtp.impl.runtime;

import org.iplass.mtp.impl.core.config.ServerEnv;
import org.iplass.mtp.impl.util.PlatformUtil;
import org.iplass.mtp.runtime.Environment;

public class EnvironmentImpl implements Environment {

	@Override
	public boolean serverInRole(String serverRole) {
		String[] srs = ServerEnv.getInstance().getServerRoles();
		if (srs == null || srs.length == 0) {
			return true;
		}
		
		for (String sr: srs) {
			if (serverRole.equals(sr)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getServerId() {
		return ServerEnv.getInstance().getServerId();
	}

	@Override
	public String getVersion() {
		return PlatformUtil.getPlatformInformation().getVersion();
	}

}
