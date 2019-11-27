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

package org.iplass.mtp.impl.logging;

import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public abstract class LoggingService implements Service {
	
	private Long maxAgeSecondsOfLogCondition; 

	public Long getMaxAgeSecondsOfLogCondition() {
		return maxAgeSecondsOfLogCondition;
	}

	@Override
	public void init(Config config) {
		maxAgeSecondsOfLogCondition = config.getValue("maxAgeSecondsOfLogCondition", Long.class, -1L);
	}

	@Override
	public void destroy() {
	}
	
	protected abstract Object createRuntime(LogCondition logCondition);

	public LoggingContext getLoggingContext(TenantContext tenantContext) {
		return tenantContext.getResource(LoggingContext.class);
	}

}
