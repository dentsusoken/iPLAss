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

import net.logstash.logback.argument.StructuredArguments;


public class LoggingInterceptor implements RequestInterceptor, ServiceInitListener<ActionMappingService> {
	
	private static final String MDC_ACTION = "action";
	
	//TODO カテゴリ名を設定可能に
	private static Logger actionLogger = LoggerFactory.getLogger("mtp.action");
	private static Logger partsLogger = LoggerFactory.getLogger("mtp.action.parts");
	
	private boolean actionTrace = true;//infoで出力
	private boolean partsTrace = true;//debugで出力

	private int warnLogThresholdOfSqlExecutionCount = -1;
	private long warnLogThresholdOfExecutionTimeMillis = -1;

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

	public long getWarnLogThresholdOfExecutionTimeMillis() {
		return warnLogThresholdOfExecutionTimeMillis;
	}

	public void setWarnLogThresholdOfExecutionTimeMillis(long warnLogThresholdOfExecutionTimeMillis) {
		this.warnLogThresholdOfExecutionTimeMillis = warnLogThresholdOfExecutionTimeMillis;
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
			
			MessagePattern mp = MessagePattern.getInstance(executionTime, sqlCount, exp);
			if (exp != null && !(exp instanceof ApplicationException)) {
				Logger log = null;
				if (invocation.isInclude()) {
					log = partsLogger;
				} else {
					log = actionLogger;
				}
				log.error(mp.format(), mp.arguments(getRequestPathAndParam(invocation), executionTime, sqlCount, exp, !ExceptionInterceptor.match(noStackTraceClass, exp)));
			} else {
				if (actionTrace && !invocation.isInclude()) {
					if (isWarnLog(executionTime, sqlCount)) {
						actionLogger.warn(mp.format(), mp.arguments(getRequestPathAndParam(invocation), executionTime, sqlCount, exp, actionLogger.isDebugEnabled()));
					} else {
						actionLogger.info(mp.format(), mp.arguments(getRequestPathAndParam(invocation), executionTime, sqlCount, exp, actionLogger.isDebugEnabled()));
					}
				} else if (partsTrace && invocation.isInclude()) {
					if (partsLogger.isDebugEnabled()) {
						partsLogger.debug(mp.format(), mp.arguments(getRequestPathAndParam(invocation), executionTime, sqlCount, exp, false));
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

	private boolean isWarnLog(long executionTime, int sqlCount) {
		if (warnLogThresholdOfSqlExecutionCount >= 0 && sqlCount > warnLogThresholdOfSqlExecutionCount) {
			return true;
		}
		if (warnLogThresholdOfExecutionTimeMillis >= 0 && executionTime > warnLogThresholdOfExecutionTimeMillis) {
			return true;
		}
		return false;
	}
	
	
	//message pattern
	public enum MessagePattern {
		ALL("{},{}ms,{}times(sql),{},{}") {
			@Override
			public Object[] arguments(CharSequence requestPath, long executionTime, int sqlCount, Throwable exp, boolean withStackTrace) {
				Object[] args;
				if (withStackTrace) {
					args = new Object[6];
					args[5] = exp;
				} else {
					args = new Object[5];
				}
				args[0] = StructuredArguments.value(ARG_REQUEST_PATH, requestPath);
				args[1] = StructuredArguments.value(ARG_EXECUTION_TIME, executionTime);
				args[2] = StructuredArguments.value(ARG_SQL_EXECUTION_COUNT, sqlCount);
				if (exp instanceof ApplicationException) {
					args[3] = StructuredArguments.value(ARG_ERROR_TYPE, TYPE_APP_ERROR);
				} else {
					args[3] = StructuredArguments.value(ARG_ERROR_TYPE, TYPE_ERROR);
				}
				args[4] = StructuredArguments.value(ARG_ERROR_DESCRIPTION, exp.toString());
				return args;
			}
		},
		WITHOUT_EXP("{},{}ms,{}times(sql)") {
			@Override
			public Object[] arguments(CharSequence requestPath, long executionTime, int sqlCount, Throwable exp, boolean withStackTrace) {
				Object[] args = new Object[3];
				args[0] = StructuredArguments.value(ARG_REQUEST_PATH, requestPath);
				args[1] = StructuredArguments.value(ARG_EXECUTION_TIME, executionTime);
				args[2] = StructuredArguments.value(ARG_SQL_EXECUTION_COUNT, sqlCount);
				return args;
			}
		},
		WITHOUT_EXP_SQL("{},{}ms") {
			@Override
			public Object[] arguments(CharSequence requestPath, long executionTime, int sqlCount, Throwable exp, boolean withStackTrace) {
				Object[] args = new Object[2];
				args[0] = StructuredArguments.value(ARG_REQUEST_PATH, requestPath);
				args[1] = StructuredArguments.value(ARG_EXECUTION_TIME, executionTime);
				return args;
			}
		},
		WITHOUT_EXP_TIME("{},{}times(sql)") {
			@Override
			public Object[] arguments(CharSequence requestPath, long executionTime, int sqlCount, Throwable exp, boolean withStackTrace) {
				Object[] args = new Object[2];
				args[0] = StructuredArguments.value(ARG_REQUEST_PATH, requestPath);
				args[1] = StructuredArguments.value(ARG_SQL_EXECUTION_COUNT, sqlCount);
				return args;
			}
		},
		WITHOUT_SQL("{},{}ms,{},{}") {
			@Override
			public Object[] arguments(CharSequence requestPath, long executionTime, int sqlCount, Throwable exp, boolean withStackTrace) {
				Object[] args;
				if (withStackTrace) {
					args = new Object[5];
					args[4] = exp;
				} else {
					args = new Object[4];
				}
				args[0] = StructuredArguments.value(ARG_REQUEST_PATH, requestPath);
				args[1] = StructuredArguments.value(ARG_EXECUTION_TIME, executionTime);
				if (exp instanceof ApplicationException) {
					args[2] = StructuredArguments.value(ARG_ERROR_TYPE, TYPE_APP_ERROR);
				} else {
					args[2] = StructuredArguments.value(ARG_ERROR_TYPE, TYPE_ERROR);
				}
				args[3] = StructuredArguments.value(ARG_ERROR_DESCRIPTION, exp.toString());
				return args;
			}
		},
		WITHOUT_TIME("{},{}times(sql),{},{}") {
			@Override
			public Object[] arguments(CharSequence requestPath, long executionTime, int sqlCount, Throwable exp, boolean withStackTrace) {
				Object[] args;
				if (withStackTrace) {
					args = new Object[5];
					args[4] = exp;
				} else {
					args = new Object[4];
				}
				args[0] = StructuredArguments.value(ARG_REQUEST_PATH, requestPath);
				args[1] = StructuredArguments.value(ARG_SQL_EXECUTION_COUNT, sqlCount);
				if (exp instanceof ApplicationException) {
					args[2] = StructuredArguments.value(ARG_ERROR_TYPE, TYPE_APP_ERROR);
				} else {
					args[2] = StructuredArguments.value(ARG_ERROR_TYPE, TYPE_ERROR);
				}
				args[3] = StructuredArguments.value(ARG_ERROR_DESCRIPTION, exp.toString());
				return args;
			}
		},
		WITHOUT_TIME_SQL("{},{},{}") {
			@Override
			public Object[] arguments(CharSequence requestPath, long executionTime, int sqlCount, Throwable exp, boolean withStackTrace) {
				Object[] args;
				if (withStackTrace) {
					args = new Object[4];
					args[3] = exp;
				} else {
					args = new Object[3];
				}
				args[0] = StructuredArguments.value(ARG_REQUEST_PATH, requestPath);
				if (exp instanceof ApplicationException) {
					args[1] = StructuredArguments.value(ARG_ERROR_TYPE, TYPE_APP_ERROR);
				} else {
					args[1] = StructuredArguments.value(ARG_ERROR_TYPE, TYPE_ERROR);
				}
				args[2] = StructuredArguments.value(ARG_ERROR_DESCRIPTION, exp.toString());
				return args;
			}
		},
		WITHOUT_ALL("{}") {
			@Override
			public Object[] arguments(CharSequence requestPath, long executionTime, int sqlCount, Throwable exp, boolean withStackTrace) {
				Object[] args = new Object[1];
				args[0] = StructuredArguments.value(ARG_REQUEST_PATH, requestPath);
				return args;
			}
		};
		
		private static final String TYPE_APP_ERROR = "AppError";
		private static final String TYPE_ERROR = "Error";
		
		private static final String ARG_REQUEST_PATH = "request_path";
		private static final String ARG_EXECUTION_TIME = "execution_time";
		private static final String ARG_SQL_EXECUTION_COUNT = "sql_execution_count";
		private static final String ARG_ERROR_TYPE = "error_type";
		private static final String ARG_ERROR_DESCRIPTION = "error_description";
		
		public static MessagePattern getInstance(long executionTime, int sqlCount, Throwable exp) {
			if (exp == null) {
				if (executionTime < 0) {
					if (sqlCount < 0) {
						return WITHOUT_ALL;
					} else {
						return WITHOUT_EXP_TIME;
					}
				} else {
					if (sqlCount < 0) {
						return WITHOUT_EXP_SQL;
					} else {
						return WITHOUT_EXP;
					}
				}
			} else {
				if (executionTime < 0) {
					if (sqlCount < 0) {
						return WITHOUT_TIME_SQL;
					} else {
						return WITHOUT_TIME;
					}
				} else {
					if (sqlCount < 0) {
						return WITHOUT_SQL;
					} else {
						return ALL;
					}
				}
			}
		}

		private String format;
		
		private MessagePattern(String format) {
			this.format = format;
		}
		
		public String format() {
			return format;
		}
		
		public abstract Object[] arguments(CharSequence requestPath, long executionTime, int sqlCount, Throwable exp, boolean withStackTrace);
	}

	private CharSequence getRequestPathAndParam(RequestInvocation invocation) {
		if (invocation.isInclude()) {
			return invocation.getActionName();
		}

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
