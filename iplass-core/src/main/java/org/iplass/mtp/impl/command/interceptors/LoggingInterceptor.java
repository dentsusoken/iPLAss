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
package org.iplass.mtp.impl.command.interceptors;

import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.command.interceptor.CommandInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LoggingInterceptor implements CommandInterceptor {
	
	private static final String MDC_CMD = "command";
	
	private static Logger commandLogger = LoggerFactory.getLogger("mtp.command");
	
	@Override
	public String intercept(CommandInvocation invocation) {
		String prev = MDC.get(MDC_CMD);
		MDC.put(MDC_CMD, invocation.getCommandName());
		try {
			
			if (commandLogger.isDebugEnabled()) {
				long start = System.currentTimeMillis();
				String res = null;
				try {
					res = invocation.proceedCommand();
					return res;
				} catch (RuntimeException e) {
					res = e.toString();
					throw e;
				} finally {
					String cmd = "";
					if (invocation.getCommand() != null) {
						cmd = invocation.getCommand().toString();
					}
					commandLogger.debug(cmd + "," + res + "," + (System.currentTimeMillis() - start) + "ms");
				}
			} else {
				return invocation.proceedCommand();
			}
			
		} finally {
			if (prev == null) {
				MDC.remove(MDC_CMD);
			} else {
				MDC.put(MDC_CMD, prev);
			}
		}
	}

}
