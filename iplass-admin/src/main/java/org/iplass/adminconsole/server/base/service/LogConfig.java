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

package org.iplass.adminconsole.server.base.service;

import java.util.List;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;

public class LogConfig implements ServiceInitListener<AdminConsoleService> {

	private boolean enabled = false;

	private List<String> logHome;

	private List<String> fileFilter;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getLogHome() {
		return logHome;
	}

	public void setLogHome(List<String> logHome) {
		this.logHome = logHome;
	}

	public List<String> getFileFilter() {
		return fileFilter;
	}

	public void setFileFilter(List<String> fileFilter) {
		this.fileFilter = fileFilter;
	}

	@Override
	public void inited(AdminConsoleService service, Config config) {
	}

	@Override
	public void destroyed() {
	}

}
