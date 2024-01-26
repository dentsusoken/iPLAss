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

package org.iplass.mtp.impl.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantResource;
import org.iplass.mtp.spi.ServiceRegistry;

public class LoggingContext implements TenantResource {

	//とりあえず、ローカルのLogレベルを変更できるように

	private static final String LOG_CONDITION = "mtp.loggingContext.logCondition";

	private static LogConditionRuntime NULL_LOG_CONDITION = new LogConditionRuntime(null, null, null, 0);


	private TenantContext tc;
	private volatile List<LogConditionRuntime> list;

	public List<LogCondition> list() {
		List<LogConditionRuntime> cList = list;
		if (cList == null) {
			return Collections.emptyList();
		}

		List<LogCondition> ret = new ArrayList<>();
		for (LogConditionRuntime lc: cList) {
			ret.add(lc.getCondition());
		}

		return ret;
	}

	public void apply(List<LogCondition> logConditions) {
		synchronized (this) {
			LoggingService loggingService = ServiceRegistry.getRegistry().getService(LoggingService.class);
			List<LogConditionRuntime> newList = null;
			if (logConditions != null) {
				long maxExpires;
				if (loggingService.getMaxAgeSecondsOfLogCondition() != null && loggingService.getMaxAgeSecondsOfLogCondition() >= 0) {
					maxExpires = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(loggingService.getMaxAgeSecondsOfLogCondition());
				} else {
					maxExpires = Long.MAX_VALUE;
				}

				newList = new ArrayList<>();
				for (int i = 0; i < logConditions.size(); i++) {
					LogCondition lc = logConditions.get(i);
					if (lc.getExpiresAt() > maxExpires) {
						throw new SystemException("The max age of log condition is " + loggingService.getMaxAgeSecondsOfLogCondition() + " seconds.");
					}
					
					newList.add(new LogConditionRuntime(lc, loggingService.createRuntime(lc), tc, i));
				}
			}
			list = newList;
		}
	}

	public void clear() {
		synchronized (this) {
			list = null;
		}
	}

	public void resetMatched(ExecuteContext ec) {
		ec.removeAttribute(LOG_CONDITION);
	}

	public LogConditionRuntime getMatched(ExecuteContext ec) {

		LogConditionRuntime cache = (LogConditionRuntime) ec.getAttribute(LOG_CONDITION);
		if (cache != null) {
			if (NULL_LOG_CONDITION == cache) {
				return null;
			}
			return cache;
		}

		List<LogConditionRuntime> cList = list;
		if (cList != null) {
			long time = System.currentTimeMillis();
			for (LogConditionRuntime lc: cList) {
				if (lc.getCondition().getExpiresAt() > time && lc.isConditionMatch(ec)) {
					ec.setAttribute(LOG_CONDITION, lc, false);
					return lc;
				}
			}
		}

		ec.setAttribute(LOG_CONDITION, NULL_LOG_CONDITION, false);
		return null;
	}


	@Override
	public void init(TenantContext tenantContext) {
		this.tc = tenantContext;
	}

	@Override
	public void destory() {
		this.tc = null;
		this.list = null;
	}

}
