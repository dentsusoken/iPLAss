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

package org.iplass.mtp.impl.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.rdb.connection.ResourceHolder;
import org.iplass.mtp.impl.web.RequestPath.PathType;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DispatcherFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(DispatcherFilter.class);
	private static Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal");

	private TenantContextService tenantContextService;

	private ServletContext servletContext;
	private ActionMappingService amService;
	private WebFrontendService webFrontendService;

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
		tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
		servletContext = config.getServletContext();
		amService = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
		webFrontendService = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		RequestPath path = new RequestPath(req, webFrontendService);

		if (!path.isValid()) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		//ConnectionをThreadに紐づけ
		ResourceHolder.init();//TODO ExecuteContextに紐付け

		try {
			//exclude path
			if (path.getPathType() == PathType.UNKNOWN) {
				if(logger.isTraceEnabled()) {
					logger.trace("excluded URL:" + req.getRequestURI());
				}
				chain.doFilter(req, res);
				return;
			}

			req.setCharacterEncoding("utf-8");//TODO 設定可能に,EncodingFilterに移動する。
			res.setCharacterEncoding("utf-8");//TODO responseのCharactersetが変わる可能性あり。

			TenantContext tc = getTenantContext(path.getTenantUrl());

			// テナントを確定できないので404を返す。
			if (tc == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("can not determine tenant.URL=" + req.getServletPath());
				}
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			try {
				ExecuteContext.initContext(new ExecuteContext(tc));

				if(logger.isDebugEnabled()) {
					logger.debug("do " + req.getRequestURL().toString() + " " + req.getMethod());
				}

				//tenantContextPathより後を処理
				switch (path.getPathType()) {
				case REJECT:
					if(logger.isDebugEnabled()) {
						logger.debug("reject URL:" + req.getRequestURI());
					}
					res.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
				case REST:
					req.setAttribute(RequestPath.ATTR_NAME, path);
					servletContext.getRequestDispatcher(path.getTargetPath()).forward(req, res);
					break;
				case THROUGH:
					req.setAttribute(RequestPath.ATTR_NAME, path);
					chain.doFilter(req, res);
					break;
				case ACTION:
					doAction(path, req, res, chain);
					break;
				default:
					break;
				}

			} finally {
				ExecuteContext.finContext();
			}
		} catch (Throwable e) {
			if (e instanceof Error) {
				fatalLogger.error("Exception occurred while processing URI:" + req.getRequestURI() + " " + e.getMessage(), e);
			} else {
				logger.error("Exception occurred while processing URI:" + req.getRequestURI() + " " + e.getMessage(), e);
			}
			try {
				if (!res.isCommitted()) {
					res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		} finally {
			//FIXME 本当はもっとConnectionをつかみっぱなしになる期間は短い方がよい、、、
			//Theadに紐づいているConnectionを開放
			ResourceHolder.fin();
		}
	}

	private void doAction(RequestPath path, HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		if (WebUtil.isDirectAccess(req)) {
			logger.debug("process as direct access mode. port={}", req.getServerPort());
		}

		// テナント直下の場合は設定されているトップ画面URLへリダイレクトする
		String actionPath = path.getTargetPath(true);
		if (actionPath.length() == 0) {

			Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			String menuUrl = WebUtil.getTenantWebInfo(tenant).getHomeUrl();
			if (!StringUtil.isEmpty(menuUrl) && !menuUrl.equals("/")) {
				String redirectPath;
				if (menuUrl.startsWith("/")) {
					redirectPath = path.getTenantContextPath(req) + menuUrl;
				} else {
					redirectPath = path.getTenantContextPath(req) + "/" + menuUrl;
				}
				logger.debug("redirects to top URL.RedirectURL=" + redirectPath);
				res.sendRedirect(redirectPath);
				return;
			}
		}

		WebRequestStack requestStack = new WebRequestStack(path, servletContext, req, res);

		try {
			ActionMappingRuntime actionMapping = amService.getByPathHierarchy(actionPath);
			if (actionMapping == null
					&& webFrontendService.getWelcomeAction() != null
					&& (actionPath.length() == 0 || actionPath.endsWith("/"))) {
				for (String wa: webFrontendService.getWelcomeAction()) {
					actionMapping = amService.getByPathHierarchy(actionPath + wa);
					if (actionMapping != null) {
						break;
					}
				}
			}

			if (actionMapping != null) {
				logger.debug("call actionMapping:" + actionMapping.getMetaData().getName());
				actionMapping.executeCommand(requestStack);
			} else {
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
			}

		} finally {
			//テンポラリで使用したリソースを削除。（内部でのincludeの時は削除しない）
			if (requestStack != null) {
				requestStack.finallyProcess();
			}
		}
	}

	private TenantContext getTenantContext(String value) {
		TenantContext context = tenantContextService.getTenantContext(value);
		return context;
	}

}
