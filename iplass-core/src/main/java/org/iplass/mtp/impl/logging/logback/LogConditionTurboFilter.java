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

package org.iplass.mtp.impl.logging.logback;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.logging.LogConditionRuntime;
import org.iplass.mtp.impl.logging.LoggingContext;
import org.slf4j.MDC;
import org.slf4j.Marker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;

public class LogConditionTurboFilter extends TurboFilter {
	
	@Override
	public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
		
		//tenantが確定していない場合は、適用しない
		String tenantIdString = MDC.get(ExecuteContext.MDC_TENANT);
		if (tenantIdString == null) {
			return FilterReply.NEUTRAL;
		}
		
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		LoggingContext lc = ec.getTenantContext().getResource(LoggingContext.class);
		if (lc == null) {
			return FilterReply.NEUTRAL;
		}
		
		LogConditionRuntime cond = lc.getMatched(ec);
		if (cond == null) {
			return FilterReply.NEUTRAL;
		}
		Level levelCond = (Level) cond.getConcreteServiceRuntime();
		if (levelCond == null) {
			return FilterReply.NEUTRAL;
		}
		if (!cond.isTargetLogger(logger.getName())) {
			return FilterReply.NEUTRAL;
		}
		
		if (level.isGreaterOrEqual(levelCond)) {
			return FilterReply.ACCEPT;
		} else {
			return FilterReply.DENY;
		}
	}

}
