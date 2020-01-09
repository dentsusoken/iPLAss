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

package org.iplass.mtp.impl.web.actionmapping;

import java.io.IOException;

import javax.servlet.ServletException;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.impl.command.InvocationImpl;
import org.iplass.mtp.impl.command.MetaCommand.CommandRuntime;
import org.iplass.mtp.impl.web.RequestPath;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.impl.web.actionmapping.Result.ResultRuntime;
import org.iplass.mtp.impl.web.actionmapping.TemplateResult.TemplateResultRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.interceptor.RequestInterceptor;
import org.iplass.mtp.web.interceptor.RequestInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebInvocationImpl extends InvocationImpl implements RequestInvocation {

	//TODO 後から追加したので、形がいびつ、要リファクタリング

	private static final Logger logger = LoggerFactory.getLogger(WebInvocationImpl.class);

	public static RequestInterceptor[] NULL_REQUEST_INTERCEPTOR = new RequestInterceptor[0];

	private RequestInterceptor[] requestInterceptors;
	private int reqIndex;
	private int resIndex;
	private String status;
	private Throwable exception;
	private boolean isInclude;
	private ActionMappingRuntime action;

	private WebRequestStack requestStack;

	public WebInvocationImpl(RequestInterceptor[] requestInterceptors, CommandInterceptor[] commandInterceptors, CommandRuntime cmd, RequestContext request, WebRequestStack requestStack, ActionMappingRuntime action) {
		super(commandInterceptors, cmd, request);
		if (requestInterceptors != null) {
			this.requestInterceptors = requestInterceptors;
		} else {
			this.requestInterceptors = NULL_REQUEST_INTERCEPTOR;
		}
		this.requestStack = requestStack;
		this.isInclude = !requestStack.isClientDirectRequest();
		reqIndex = -1;
		resIndex = -1;
		this.action = action;
	}

	public ActionMappingRuntime getAction() {
		return action;
	}

	public WebRequestStack getRequestStack() {
		return requestStack;
	}

	private void doAction() {
		requestStack.setAttribute(WebRequestConstants.ACTION_NAME, action.getMetaData().getName());
		// Command未設定の場合はCommand実行せず、デフォルトのテンプレートを表示
		//Command実行
		if (getCommand() != null) {
			try {
				proceedCommand();
			} catch (Throwable t) {
				//exception = t;
			}
		}
		
		proceedResult();
	}
	
	private void doResult() {
		requestStack.setAttribute(WebRequestConstants.ACTION_NAME, action.getMetaData().getName());
		ResultRuntime result = null;
		//Resultマッピング
		if (exception == null) {
			result = action.getResult(status);

			if (logger.isDebugEnabled()) {
				if (result instanceof TemplateResultRuntime) {
					logger.debug("commandResultStatus:" + status + "  -> Template:" + ((TemplateResultRuntime) result).getTemplateName());
				} else {
					logger.debug("commandResultStatus:" + status + " -> Result:" + result.getMetaData());
				}
			}

			requestStack.setAttribute(WebRequestConstants.EXECUTED_COMMAND, getCommand());
			requestStack.setAttribute(WebRequestConstants.COMMAND_RESULT, status);
			
		} else {
			result = action.getResult(exception);
			if (result == null) {
				//例外マッピングなし
				if (exception instanceof RuntimeException) {
					throw (RuntimeException) exception;
				}
				if (exception instanceof Error) {
					throw (Error) exception;
				}
				//それ以外はありえない
				throw new RuntimeException(exception);
			}
			
			if (logger.isDebugEnabled()) {
				if (result instanceof TemplateResultRuntime) {
					logger.debug("commandResultException:" + exception.getClass().getName() + " -> Template:" + ((TemplateResultRuntime) result).getTemplateName());
				} else {
					logger.debug("commandResultException:" + exception.getClass().getName() + " -> Result:" + result.getMetaData());
				}
			}

			requestStack.setAttribute(WebRequestConstants.EXECUTED_COMMAND, getCommand());
			requestStack.setAttribute(WebRequestConstants.EXCEPTION, exception);
		}
		
		try {
			result.handle(requestStack);
		} catch (ServletException | IOException e) {
			throw new WebProcessRuntimeException(e);
		} finally {
			if (result != null) {
				result.finallyProcess(requestStack);
			}
		}
	}
	

	@Override
	public void proceedRequest() {

		reqIndex++;
		try {
			if (reqIndex == requestInterceptors.length) {
				doAction();
			} else {
				requestInterceptors[reqIndex].intercept(this);
			}
		} finally {
			reqIndex--;
		}
	}

	public String proceedCommand() {
		try {
			status = super.proceedCommand();
			return status;
		} catch (Throwable e) {
			status = null;
			exception = e;
			throw e;
		}
	}
	
	@Override
	public void proceedResult() {
		resIndex++;
		try {
			if (resIndex == requestInterceptors.length) {
				doResult();
			} else {
				requestInterceptors[resIndex].interceptResult(this);
			}
		} finally {
			resIndex--;
		}
	}

	@Override
	public String getStatus() {
		return status;
	}
	
	@Override
	public Throwable getException() {
		return exception;
	}

	@Override
	public void getStatus(String status) {
		if (logger.isDebugEnabled()) {
			logger.debug("set status to " + status);
		}
		this.status = status;
	}

	@Override
	public void setException(Throwable exception) {
		if (logger.isDebugEnabled()) {
			logger.debug("set exception to " + exception);
		}
		this.exception = exception;
	}

	@Override
	public boolean isInclude() {
		return isInclude;
	}

	@Override
	public String getActionName() {
		return action.getMetaData().getName();
	}

	@Override
	public void redirectAction(String actionName) {
		ActionMappingService amService = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
		ActionMappingRuntime am = amService.getByPathHierarchy(actionName);
		if (am != null) {
			try {
				am.executeCommand(requestStack);
			} catch (IOException e) {
				throw new WebProcessRuntimeException(e);
			} catch (ServletException e) {
				throw new WebProcessRuntimeException(e);
			}
		} else {
			throw new WebProcessRuntimeException("Can not find Action:" + actionName);
		}
	}

	@Override
	public RequestPath getRequestPath() {
		return requestStack.getRequestPath();
	}

}
