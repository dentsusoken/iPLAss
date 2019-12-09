/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.logging.logback;

import org.iplass.mtp.impl.logging.LogCondition;
import org.iplass.mtp.impl.logging.LoggingService;
import org.iplass.mtp.spi.Config;

import ch.qos.logback.classic.Level;

public class LogbackLoggingService extends LoggingService {
	
	@Override
	public void init(Config config) {
		super.init(config);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	protected Object createRuntime(LogCondition logCondition) {
		if (logCondition.getLevel() != null) {
			return Level.toLevel(logCondition.getLevel());
		}
		
		return false;
	}

}
