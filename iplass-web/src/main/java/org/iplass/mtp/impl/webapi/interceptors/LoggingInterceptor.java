/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.interceptors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.interceptor.CommandInvocation;
import org.iplass.mtp.impl.command.InterceptorService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.connection.ConnectionFactory;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.interceptors.ExceptionInterceptor;
import org.iplass.mtp.impl.webapi.rest.RestRequestContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public class LoggingInterceptor extends org.iplass.mtp.impl.command.interceptors.LoggingInterceptor implements ServiceInitListener<InterceptorService> {
	
	private static Logger webapiLogger = LoggerFactory.getLogger("mtp.webapi");
	
	private static final String MDC_WEBAPI ="webapi";
	
	private boolean webapiTrace = true;//infoで出力

	private int warnLogThresholdOfSqlExecutionCount = -1;
	private long warnLogThresholdOfExecutionTimeMillis = -1;

	private ConnectionFactory rdbConFactory;

	private List<String> noStackTrace;
	private List<Class<?>[]> noStackTraceClass;
	
	private List<String> logParamName;
	private List<String> logAttributeName;
	
	public List<String> getLogParamName() {
		return logParamName;
	}

	public void setLogParamName(List<String> logParamName) {
		this.logParamName = logParamName;
	}

	public List<String> getLogAttributeName() {
		return logAttributeName;
	}

	public void setLogAttributeName(List<String> logAttributeName) {
		this.logAttributeName = logAttributeName;
	}

	public boolean isWebapiTrace() {
		return webapiTrace;
	}

	public void setWebapiTrace(boolean webapiTrace) {
		this.webapiTrace = webapiTrace;
	}

	public List<String> getNoStackTrace() {
		return noStackTrace;
	}

	public void setNoStackTrace(List<String> noStackTrace) {
		this.noStackTrace = noStackTrace;
	}
	
	public int getWarnLogThresholdOfSqlExecutionCount() {
		return warnLogThresholdOfSqlExecutionCount;
	}

	public void setWarnLogThresholdOfSqlExecutionCount(int warnLogThresholdOfSqlExecutionCount) {
		this.warnLogThresholdOfSqlExecutionCount = warnLogThresholdOfSqlExecutionCount;
	}

	public long getWarnLogThresholdOfExecutionTimeMillis() {
		return warnLogThresholdOfExecutionTimeMillis;
	}

	public void setWarnLogThresholdOfExecutionTimeMillis(long warnLogThresholdOfExecutionTimeMillis) {
		this.warnLogThresholdOfExecutionTimeMillis = warnLogThresholdOfExecutionTimeMillis;
	}

	@Override
	public void inited(InterceptorService service, Config config) {
		if (noStackTrace != null) {
			noStackTraceClass = ExceptionInterceptor.toClassList(noStackTrace);
		}
		rdbConFactory = ServiceRegistry.getRegistry().getService(ConnectionFactory.class);

	}

	@Override
	public void destroyed() {
	}
	
	@Override
	public String intercept(CommandInvocation invocation) {
		long start = -1;
		if (webapiTrace) {
			start = System.currentTimeMillis();
		}
		String webApiName = (String) invocation.getRequest().getAttribute(WebApiRequestConstants.API_NAME);
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		ec.mdcPut(MDC_WEBAPI, webApiName);
		RuntimeException exp = null;
		AtomicInteger sqlCounter = rdbConFactory.getCounterOfSqlExecution();

		try {
			return super.intercept(invocation);
		} catch (RuntimeException t) {
			exp = t;
			throw t;
		} finally {
			long executionTime;
			if (start == -1) {
				executionTime = start;
			} else {
				executionTime = System.currentTimeMillis() - start;
			}
			
			int sqlCount;
			if (sqlCounter == null) {
				sqlCount = -1;
			} else {
				sqlCount = sqlCounter.get();
			}

			if (exp != null && !(exp instanceof ApplicationException)) {
				if (ExceptionInterceptor.match(noStackTraceClass, exp)) {
					webapiLogger.error(logStr(invocation, executionTime, sqlCount, exp));
				} else {
					webapiLogger.error(logStr(invocation, executionTime, sqlCount, exp), exp);
				}
			} else {
				if (isWarnLog(executionTime, sqlCount)) {
					if (exp != null && webapiLogger.isDebugEnabled()) {
						webapiLogger.warn(logStr(invocation, executionTime, sqlCount, exp), exp);
					} else {
						webapiLogger.warn(logStr(invocation, executionTime, sqlCount, exp));
					}
				} else {
					if (exp != null && webapiLogger.isDebugEnabled()) {
						webapiLogger.info(logStr(invocation, executionTime, sqlCount, exp), exp);
					} else {
						webapiLogger.info(logStr(invocation, executionTime, sqlCount, exp));
					}
				}
			}
			
			MDC.remove(MDC_WEBAPI);
		}
	}

	private boolean isWarnLog(long executionTime, int sqlCount) {
		if (warnLogThresholdOfSqlExecutionCount >= 0 && sqlCount > warnLogThresholdOfSqlExecutionCount) {
			return true;
		}
		if (warnLogThresholdOfExecutionTimeMillis >= 0 && executionTime > warnLogThresholdOfExecutionTimeMillis) {
			return true;
		}
		return false;
	}

	private String logStr(CommandInvocation invocation, long executionTime, int sqlCount, Throwable exp) {
		
		CharSequence requestPath = makeWebApiName((RestRequestContext) invocation.getRequest());
		
		StringBuilder sb = new StringBuilder();
		sb.append(requestPath);
		if (executionTime >= 0) {
			sb.append(',');
			sb.append(executionTime).append("ms");
		}
		if (sqlCount >= 0) {
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
	

	@SuppressWarnings("rawtypes")
	private CharSequence makeWebApiName(RestRequestContext req) {
		String webApiName = WebRequestStack.getCurrent().getRequestPath().getTargetPath(true);

		RequestType requestType = req.requestType();
		MethodType methodType = req.methodType();
		
		StringBuilder sb = new StringBuilder();
		sb.append(webApiName);
		if (req != null) {
			//param
			if (getLogParamName() != null) {
				boolean isFirst = true;
				for (String p: getLogParamName()) {
					Object val = req.getParam(p);
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
			}

			//request attribute
			if (getLogAttributeName() != null) {
				sb.append("[");
				boolean isFirst = true;
				for (String p: getLogAttributeName()) {
					Object val = req.getAttribute(p);
					if (val != null) {
						if (isFirst) {
							isFirst = false;
						} else {
							sb.append(",");
						}
						sb.append(p).append("=");
						if (val instanceof Object[]) {
							sb.append(Arrays.toString((Object[]) val));
						} else if (val instanceof Map) {
							appendMap(sb, (Map) val, null);
						} else {
							sb.append(val);
						}
					}
				}
				sb.append("]");
			}
		}

		sb.append("(").append(requestType).append("/").append(methodType).append(")");
		return sb;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void appendMap(StringBuilder sb, Map map, HashSet<Map> looped) {
		//MapのtoStirng()を参考に、配列をArrays.deepToString()するように修正したもの
        Iterator<Entry> i = map.entrySet().iterator();
		if (! i.hasNext()) {
			sb.append("{}");
			return;
		}

		sb.append('{');
		for (;;) {
			Entry e = i.next();
			Object key = e.getKey();
			Object value = e.getValue();
			sb.append(key == map ? "(this Map)" : key);
			sb.append('=');
			if (value == map) {
		    	sb.append("(this Map)");
			} else if (value instanceof Object[]) {
				sb.append(Arrays.deepToString((Object[]) value));
			} else if (value instanceof Map) {
				if (looped == null) {
					looped = new HashSet();
				}
				looped.add(map);
				if (looped.contains(value)) {
					sb.append("(looped Map)");
				} else {
					appendMap(sb, (Map) value, looped);
				}
			} else {
		    	sb.append(value);
			}
			if (!i.hasNext()) {
				sb.append('}');
				return;
			}
			sb.append(',').append(' ');
		}
	}

}
