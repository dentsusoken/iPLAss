/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.log;

import java.util.List;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class AuthLoggerService implements Service {

	private AuthLogger[] loggers;
	private AuthLogger defaultLogger;
	
	public AuthLogger[] getLoggers() {
		return loggers;
	}
	
	public AuthLogger getAuthLogger(String name) {
		if (name == null) {
			return defaultLogger;
		}
		if (loggers != null) {
			for (AuthLogger al: loggers) {
				if (al.getLoggerName().equals(name)) {
					return al;
				}
			}
		}
		return defaultLogger;
	}

	@Override
	public void init(Config config) {
		List<?> loggerList = config.getBeans("logger");
		if (loggerList != null) {
			loggers = loggerList.toArray(new AuthLogger[loggerList.size()]);
		}
		if (loggers != null) {
			for (AuthLogger al: loggers) {
				if ("default".equals(al.getLoggerName())) {
					defaultLogger = al;
					break;
				}
			}
		}
		if (defaultLogger == null) {
			defaultLogger = new Slf4jAuthLogger();
			defaultLogger.inited(this, config);
		}
	}

	@Override
	public void destroy() {
	}

}
