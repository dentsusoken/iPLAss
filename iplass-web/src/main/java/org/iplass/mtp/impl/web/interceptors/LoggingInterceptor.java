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

package org.iplass.mtp.impl.web.interceptors;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.connection.ConnectionFactory;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.interceptor.RequestInterceptor;
import org.iplass.mtp.web.interceptor.RequestInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public class LoggingInterceptor implements RequestInterceptor, ServiceInitListener<ActionMappingService> {
	
	private static final String MDC_ACTION = "action";
	
	//TODO カテゴリ名を設定可能に
	private static Logger actionLogger = LoggerFactory.getLogger("mtp.action");
	private static Logger partsLogger = LoggerFactory.getLogger("mtp.action.parts");
	
	private boolean actionTrace = true;//infoで出力
	private boolean partsTrace = true;//debugで出力

	private int warnLogThresholdOfSqlExecutionCount = -1;
	private ConnectionFactory rdbConFactory;

	private String[] paramName;
	
	private List<String> noStackTrace;
	private List<Class<?>[]> noStackTraceClass;
	
	@Override
	public void inited(ActionMappingService service, Config config) {
		if (noStackTrace != null) {
			noStackTraceClass = ExceptionInterceptor.toClassList(noStackTrace);
		}
		rdbConFactory = ServiceRegistry.getRegistry().getService(ConnectionFactory.class);
	}

	@Override
	public void destroyed() {
	}
	
	public List<String> getNoStackTrace() {
		return noStackTrace;
	}

	public void setNoStackTrace(List<String> noStackTrace) {
		this.noStackTrace = noStackTrace;
	}

	public String[] getParamName() {
		return paramName;
	}

	public void setParamName(String[] paramName) {
		this.paramName = paramName;
	}

	public boolean isPartsTrace() {
		return partsTrace;
	}

	public void setPartsTrace(boolean partsTrace) {
		this.partsTrace = partsTrace;
	}

	public int getWarnLogThresholdOfSqlExecutionCount() {
		return warnLogThresholdOfSqlExecutionCount;
	}

	public void setWarnLogThresholdOfSqlExecutionCount(int warnLogThresholdOfSqlExecutionCount) {
		this.warnLogThresholdOfSqlExecutionCount = warnLogThresholdOfSqlExecutionCount;
	}

	@Override
	public void intercept(RequestInvocation invocation) {
		
		long start = -1;
		
		if (actionTrace) {
			start = System.currentTimeMillis();
		}
		
		String prev = MDC.get(MDC_ACTION);
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		ec.mdcPut(MDC_ACTION, invocation.getActionName());
		Throwable exp = null;
		AtomicInteger sqlCounter = rdbConFactory.getCounterOfSqlExecution();

		try {
			invocation.proceedRequest();
			exp = (Throwable) invocation.getRequest().getAttribute(WebRequestConstants.EXCEPTION);
		} catch (RuntimeException t) {
			exp = t;
			throw t;
		} finally {
			int sqlCount;
			if (sqlCounter == null) {
				sqlCount = -1;
			} else {
				sqlCount = sqlCounter.get();
			}
			
			if (exp != null && !(exp instanceof ApplicationException)) {
				Logger log = null;
				if (invocation.isInclude()) {
					log = partsLogger;
				} else {
					log = actionLogger;
				}
				if (ExceptionInterceptor.match(noStackTraceClass, exp)) {
					log.error(logStr(invocation, start, sqlCount, exp));
				} else {
					log.error(logStr(invocation, start, sqlCount, exp), exp);
				}
			} else {
				if (actionTrace && !invocation.isInclude()) {
					if (warnLogThresholdOfSqlExecutionCount >= 0 && sqlCount > warnLogThresholdOfSqlExecutionCount) {
						actionLogger.warn(logStr(invocation, start, sqlCount, exp));
					} else {
						actionLogger.info(logStr(invocation, start, sqlCount, exp));
					}
				} else if (partsTrace && invocation.isInclude()) {
					if (partsLogger.isDebugEnabled()) {
						partsLogger.debug(logStr(invocation, start, sqlCount, exp));
					}
				}
			}
			
			if (prev == null) {
				MDC.remove(MDC_ACTION);
			} else {
				ec.mdcPut(MDC_ACTION, prev);
			}
		}
	}
	
	private String logStr(RequestInvocation invocation, long startTime, int sqlCount, Throwable exp) {
		
		CharSequence requestPath;
		if (invocation.isInclude()) {
			requestPath = invocation.getActionName();
		} else {
			requestPath = getRequestPathAndParam(invocation);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(requestPath);
		if (startTime >= 0) {
			sb.append(',');
			sb.append((System.currentTimeMillis() - startTime)).append("ms");
		}
		if (sqlCount >= 0 && !invocation.isInclude()) {
			sb.append(',');
			sb.append(sqlCount).append("times(sql)");
		}
		if (exp != null) {
			if (exp instanceof ApplicationException) {
				sb.append(",AppError,");
			} else {
				sb.append(",Error,");
			}
			sb.append(exp.toString());
		}
		
		return sb.toString();
	}
	
	private CharSequence getRequestPathAndParam(RequestInvocation invocation) {
		String actionName = invocation.getRequestPath().getTargetPath(true);
		if (paramName == null) {
			return actionName;
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(actionName);
			boolean isFirst = true;
			for (String p: paramName) {
				Object val = invocation.getRequest().getParam(p);
				if (val != null) {
					if (isFirst) {
						sb.append("?");
						isFirst = false;
					} else {
						sb.append("&");
					}
					if (val instanceof String[]) {
						String[] valArray = (String[]) val;
						for (int i = 0; i < valArray.length; i++) {
							if (i != 0) {
								sb.append("&");
							}
							sb.append(p).append("=").append(valArray[i]);
						}
					} else {
						sb.append(p).append("=").append(val);
					}
				}
			}
			return sb;
		}
	}

}
