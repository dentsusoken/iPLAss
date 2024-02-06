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

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.impl.command.RequestContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogConditionRuntime {
	
	private static Logger logger = LoggerFactory.getLogger(LogConditionRuntime.class);
	
	private LogCondition condition;
	private Object concreteServiceRuntime;
	private Script conditionScript;
	private Pattern loggerNamePattern;
	private ConcurrentHashMap<String, Boolean> loggerNameCache;

	
	public LogConditionRuntime(LogCondition condition, Object concreteServiceRuntime, TenantContext tc, int index) {
		this.condition = condition;
		this.concreteServiceRuntime = concreteServiceRuntime;
		
		if (condition != null) {
			if (condition.getCondition() != null) {
				conditionScript = tc.getScriptEngine().createScript(condition.getCondition(), "LogCondition_" + index);
			}
			if (condition.getLoggerNamePattern() != null) {
				loggerNamePattern = Pattern.compile(condition.getLoggerNamePattern());
				loggerNameCache = new ConcurrentHashMap<String, Boolean>();
			}
		}
	}
	
	public boolean hasConditionScript() {
		return conditionScript != null;
	}
	
	public boolean isConditionMatch(ExecuteContext ec) {
		if (condition == null) {
			return false;
		}
		
		if (condition.getCondition() == null) {
			return true;
		}
		
		ScriptContext sc = ec.getTenantContext().getScriptEngine().newScriptContext();
		sc.setAttribute(LogCondition.BIND_NAME_MDC, new MdcAdapter());
		sc.setAttribute(LogCondition.BIND_NAME_REQUEST, RequestContextHolder.getCurrent());
		sc.setAttribute(LogCondition.BIND_NAME_AUTH_CONTEXT, AuthContext.getCurrentContext());
		
		try {
			Boolean ret = (Boolean) conditionScript.eval(sc);
			if (ret == null) {
				return false;
			}
			return ret;
		} catch (RuntimeException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("LogCondition script eval failed:" + condition.getCondition(), e);
			}
		}
		return false;
	}
	
	public boolean isTargetLogger(String loggerName) {
		if (loggerNamePattern == null) {
			return true;
		}
		
		Boolean ret = loggerNameCache.get(loggerName);
		if (ret == null) {
			if (loggerNamePattern.matcher(loggerName).matches()) {
				ret = Boolean.TRUE;
			} else {
				ret = Boolean.FALSE;
			}
			
			loggerNameCache.put(loggerName, ret);
		}
		
		return ret;
	}
	
	
	public LogCondition getCondition() {
		if (condition == null) {
			return null;
		}
		return condition.copy();
	}
	
	public Object getConcreteServiceRuntime() {
		return concreteServiceRuntime;
	}

}
