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

import static org.iplass.mtp.impl.web.WebResourceBundleUtil.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.NeedTrustedAuthenticationException;
import org.iplass.mtp.auth.NoPermissionException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.RequestContextWrapper;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AnonymousUserContext;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginHandler;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginInstruction;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.tenant.web.MetaTenantWebInfo.MetaTenantWebInfoRuntime;
import org.iplass.mtp.impl.tenant.MetaTenantService;
import org.iplass.mtp.impl.web.ErrorUrlSelector;
import org.iplass.mtp.impl.web.LoginUrlSelector;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.impl.web.actionmapping.WebInvocationImpl;
import org.iplass.mtp.impl.web.i18n.LangSelector;
import org.iplass.mtp.impl.web.template.MetaTemplate.TemplateRuntime;
import org.iplass.mtp.impl.web.template.TemplateService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;
import org.iplass.mtp.web.actionmapping.permission.ActionPermission;
import org.iplass.mtp.web.actionmapping.permission.RequestContextActionParameter;
import org.iplass.mtp.web.interceptor.RequestInterceptor;
import org.iplass.mtp.web.interceptor.RequestInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthInterceptor implements RequestInterceptor,ServiceInitListener<ActionMappingService> {

	private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	public static final String LOGOUT_FLAG = "mtp.auth.loggedout";
	public static final String TEMPLATE_AFTER_LOGOUT = "templateAfterLogout";
	public static final String REDIRECT_PATH_AFTER_LOGOUT = "redirectPathAfterLogout";
	public static final String REDIRECT_BY_AUTH_INTERCEPTOR = "mtp.auth.redirectByAuthInterceptor";

	private static final String AUTO_LOGIN_PROCESSED_FLAG = "mtp.auth.AutoLoginProcessed";

	private LangSelector lang = new LangSelector();
	private ActionMappingService amService;
	private WebFrontendService wfService = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
	private AuthService authService = ServiceRegistry.getRegistry().getService(AuthService.class);
	private MetaTenantService metaTenantService = ServiceRegistry.getRegistry().getService(MetaTenantService.class);
	private TemplateService ts = ServiceRegistry.getRegistry().getService(TemplateService.class);

	@Override
	public void inited(ActionMappingService service, Config config) {
		amService = service;
	}

	@Override
	public void destroyed() {
	}
	private AuthContextHolder getAuthContextHolder(ActionMappingRuntime action) {
		if (action.getMetaData().isPrivileged()) {
			if (logger.isDebugEnabled()) {
				logger.debug("do as Privileged action:" + action.getMetaData().getName());
			}
			return AuthContextHolder.getAuthContext().privilegedAuthContextHolder();
		} else if (action.getMetaData().isPrivilaged()) {
			if (logger.isDebugEnabled()) {
				logger.debug("do as Privilaged action:" + action.getMetaData().getName());
			}
			return AuthContextHolder.getAuthContext().privilegedAuthContextHolder();
		} else {
			return AuthContextHolder.getAuthContext();
		}
	}

	private void processAutoLogin(RequestInvocation invocation, AuthService authService) {
		UserContext user = authService.getCurrentSessionUserContext();

		//process auto login...

		if (user != null && !(user instanceof AnonymousUserContext)) {
			AuthenticationProvider ap = authService.getAuthenticationProvider();
			AutoLoginHandler alh = ap.getAutoLoginHandler();
			if (alh != null) {
				AutoLoginInstruction inst = alh.handle(invocation.getRequest(), true, user);
				switch (inst.getInstruction()) {
				case DO_AUTH:
					try {
						authService.login(inst.getCredential());
						alh.handleSuccess(inst, invocation.getRequest(), authService.getCurrentSessionUserContext());
					} catch (ApplicationException e) {
						Exception he = alh.handleException(inst, e, invocation.getRequest(), true, user);
						if (he instanceof ApplicationException) {
							throw (ApplicationException) he;
						} else if (he != null) {
							throw new WebProcessRuntimeException("auto login fail. cause:" + he ,he);
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("auto login fail. cause:" + e);
							}
						}
					}
					return;

				case LOGOUT:
					authService.logout();
					return;

				case ERROR:
					authService.logout();
					throw new ApplicationException(resourceString("auth.Login.noLogin"));

				case THROUGH:
					return;

				default:
					return;
				}
			}
		} else {
			for (AuthenticationProvider ap: authService.getAuthenticationProviders()) {
				AutoLoginHandler autoLoginHandler = ap.getAutoLoginHandler();
				if (autoLoginHandler != null) {
					AutoLoginInstruction inst = autoLoginHandler.handle(invocation.getRequest(), false, null);
					switch (inst.getInstruction()) {
					case DO_AUTH:
						try {
							authService.login(inst.getCredential());
							autoLoginHandler.handleSuccess(inst, invocation.getRequest(), authService.getCurrentSessionUserContext());
							
							return;
						} catch (ApplicationException e) {
							Exception he = autoLoginHandler.handleException(inst, e, invocation.getRequest(), false, null);
							if (he instanceof ApplicationException) {
								throw (ApplicationException) he;
							} else if (he != null) {
								throw new WebProcessRuntimeException("auto login fail. cause:" + he ,he);
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("auto login fail. cause:" + e);
								}
							}
						}
						//check next AuthenticationProvider
						break;

					case LOGOUT:
						authService.logout();
						return;

					case ERROR:
						authService.logout();
						throw new ApplicationException(resourceString("auth.Login.noLogin"));

					case THROUGH:
						//check next AuthenticationProvider
						break;

					default:
						//check next AuthenticationProvider
						break;
					}
				}
			}
		}
	}

	@Override
	public void intercept(final RequestInvocation invocation) {
		final WebInvocationImpl webInvocation = (WebInvocationImpl) invocation;

		//AutoLogin processing
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		if (ec.getAttribute(AUTO_LOGIN_PROCESSED_FLAG) == null) {
			try {
				
				ec.setAttribute(AUTO_LOGIN_PROCESSED_FLAG, true, false);
				processAutoLogin(webInvocation, authService);
			} catch (ApplicationException e) {
				//notify to client...
				invocation.getRequest().setAttribute(WebRequestConstants.EXCEPTION, e);
				try {
					
					showLoginForm(webInvocation, wfService);
				} catch (ServletException | IOException ee) {
					throw new WebProcessRuntimeException("can not forword to login form:" + ee.getMessage(), ee);
				}
				
				return;
			}
		}
		
		final AuthContextHolder account = getAuthContextHolder(webInvocation.getAction());

		//当該権限にて処理実行
		authService.doSecuredAction(account, () -> {
			//set Lang if user has Lang Setting
			lang.selectLangByUser(webInvocation.getRequest(), ExecuteContext.getCurrentContext());

			boolean isPermitted;
			if (webInvocation.getAction().getMetaData().isPublicAction()) {
				isPermitted = true;
				if (logger.isDebugEnabled()) {
					logger.debug("do as public action:" + webInvocation.getAction().getMetaData().getName());
				}
			} else {
				ActionPermission permission = new ActionPermission(invocation.getActionName(), new RequestContextActionParameter(invocation.getRequest()));
				isPermitted = account.checkPermission(permission);
			}

			if (!isPermitted) {
				//パーツの場合は、当該パーツ(Action)を表示しない
				if (webInvocation.isInclude()) {
					return null;
				}

				if (account.getUserContext() instanceof AnonymousUserContext) {
					//未ログインの場合は、ログイン画面へ
					try {

						showLoginForm(webInvocation, wfService);

					} catch (ServletException e) {
						throw new WebProcessRuntimeException("can not forword to login form:" + e.getMessage(), e);
					} catch (IOException e) {
						throw new WebProcessRuntimeException("can not forword to login form:" + e.getMessage(), e);
					}
				} else {
					//権限エラー画面へ
					try {
						showPermissionError(webInvocation, wfService);
					} catch (ServletException e) {
						throw new WebProcessRuntimeException("can not forword to permission error page:" + e.getMessage(), e);
					} catch (IOException e) {
						throw new WebProcessRuntimeException("can not forword to permission error page:" + e.getMessage(), e);
					}
				}
				return null;
			}

			try {

				//信頼された認証を必要とするか否か
				if (webInvocation.getAction().getMetaData().isNeedTrustedAuthenticate()) {
					if (!authService.checkCurrentSessionTrusted().isTrusted()) {
						throw new NeedTrustedAuthenticationException();
					}
				}

				//Action実行（後続処理へ）
				invocation.proceedRequest();
				return null;

			} catch (NeedTrustedAuthenticationException ne) {
				//再認証が必要
				if (account.getUserContext() instanceof AnonymousUserContext) {
					//未ログインの場合は、ログイン画面へ
					try {

						showLoginForm(webInvocation, wfService);

					} catch (ServletException e) {
						throw new WebProcessRuntimeException("can not forword to login form:" + e.getMessage(), e);
					} catch (IOException e) {
						throw new WebProcessRuntimeException("can not forword to login form:" + e.getMessage(), e);
					}
				} else {
					//再認証画面へ
					try {
						showReAuthForm(webInvocation, authService);
					} catch (ServletException e) {
						throw new WebProcessRuntimeException("can not forword to permission error page:" + e.getMessage(), e);
					} catch (IOException e) {
						throw new WebProcessRuntimeException("can not forword to permission error page:" + e.getMessage(), e);
					}
				}
				return null;
			}
		});
	}

	private void showLoginForm(WebInvocationImpl webInvocation, WebFrontendService wfService) throws ServletException, IOException {

		ExecuteContext exec = ExecuteContext.getCurrentContext();
		if (wfService.isRedirectAfterLogin() && webInvocation.getAction().getRequestRestriction().isAllowedMethod(HttpMethodType.GET.toString())) {
			//redirectAfterLoginがonかつ、当該ActionがGETを許可する場合
			String reReqPath = createReRequestPath(webInvocation.getRequestStack().getRequest());
			webInvocation.getRequest().setAttribute(WebRequestConstants.REDIRECT_PATH, reReqPath);
		}

		WebUtil.setCacheControlHeader(webInvocation.getRequestStack(), false, -1);

		//Tenantで指定されているログインActionを取得
		Tenant tenant = exec.getCurrentTenant();
		MetaTenantHandler handler = metaTenantService.getRuntimeByName(tenant.getName());
		MetaTenantWebInfoRuntime twebr = handler.getConfigRuntime(MetaTenantWebInfoRuntime.class);
		RequestContext request = new RequestContextWrapper(webInvocation.getRequest(), RequestContextWrapper.Mode.SHARED);
		String loginActionName = (twebr != null ?
				twebr.loginUrlSelector(request, webInvocation.getRequestStack().getRequestPath().getTargetPath(true)) : null);
		if (StringUtil.isNotEmpty(loginActionName)) {
			if (amService.getByPathHierarchy(loginActionName) == null) {
				//指定Actionが見つからない場合は、エラーログを出力して、service-configで指定されているデフォルトを表示
				logger.error("can not find login action:" + loginActionName + ", so use default login action");
				loginActionName = null;
			}
		}

		if (StringUtil.isEmpty(loginActionName)) {
			LoginUrlSelector defaultSelector = wfService.getLoginUrlSelector();
			if (defaultSelector == null) {
				logger.error("LoginUrlSelector must specified on WebFrontendService");
				throw new SystemException("LoginUrlSelector must specified on WebFrontendService");
			}
			loginActionName = defaultSelector.getLoginActionName(request, webInvocation.getRequestStack().getRequestPath().getTargetPath(true));
			if (StringUtil.isEmpty(loginActionName)) {
				throw new NullPointerException("LoginUrlSelector's loginActionName is null or blank");
			}
		}

		try {
			request.setAttribute(REDIRECT_BY_AUTH_INTERCEPTOR, Boolean.TRUE);
			webInvocation.redirectAction(loginActionName, request);
		} catch (Exception e) {
			logger.error("can not proceed login action:" + loginActionName + ", cause:" + e, e);
			throw e;
		}
	}

	private void showReAuthForm(WebInvocationImpl webInvocation, AuthService authService) throws ServletException, IOException {

		ExecuteContext exec = ExecuteContext.getCurrentContext();
		if (webInvocation.getAction().getRequestRestriction().isAllowedMethod(HttpMethodType.GET.toString())) {
			//当該ActionがGETを許可する場合
			String reReqPath = createReRequestPath(webInvocation.getRequestStack().getRequest());
			webInvocation.getRequest().setAttribute(WebRequestConstants.REDIRECT_PATH, reReqPath);
		}

		WebUtil.setCacheControlHeader(webInvocation.getRequestStack(), false, -1);

		//Tenantで指定されているreAuthActionを取得
		Tenant tenant = exec.getCurrentTenant();
		MetaTenantHandler handler = metaTenantService.getRuntimeByName(tenant.getName());
		MetaTenantWebInfoRuntime twebr = handler.getConfigRuntime(MetaTenantWebInfoRuntime.class);
		RequestContext request = new RequestContextWrapper(webInvocation.getRequest(), RequestContextWrapper.Mode.SHARED);
		String tenantReAuthActionName = (twebr != null ?
				twebr.reAuthUrlSelector(request, webInvocation.getRequestStack().getRequestPath().getTargetPath(true)) : null);

		if (StringUtil.isEmpty(tenantReAuthActionName)) {
			LoginUrlSelector defaultSelector = wfService.getLoginUrlSelector();
			if (defaultSelector == null) {
				logger.error("LoginUrlSelector must specified on WebFrontendService");
				throw new SystemException("LoginUrlSelector must specified on WebFrontendService");
			}
			tenantReAuthActionName = defaultSelector.getReAuthActionName(request, webInvocation.getRequestStack().getRequestPath().getTargetPath(true));
		}

		request.setAttribute(REDIRECT_BY_AUTH_INTERCEPTOR, Boolean.TRUE);
		webInvocation.redirectAction(tenantReAuthActionName, request);
	}

	private void showPermissionError(WebInvocationImpl webInvocation, WebFrontendService wfService) throws ServletException, IOException {

		ExecuteContext exec = ExecuteContext.getCurrentContext();
		WebUtil.setCacheControlHeader(webInvocation.getRequestStack(), false, -1);

		//例外をセット
		NoPermissionException exp = new NoPermissionException(resourceString("impl.web.interceptors.AuthInterceptor.noPermission"));
		webInvocation.getRequest().setAttribute(WebRequestConstants.EXCEPTION, exp);

		//Tenantで指定されている権限エラーTemplateを取得
		Tenant tenant = exec.getCurrentTenant();
		MetaTenantHandler handler = metaTenantService.getRuntimeByName(tenant.getName());
//		String tenantPermErrorTemplate = handler.errorUrlSelector(exp, webInvocation.getRequest(), webInvocation.getRequestStack().getRequestPath().getTargetPath(true));
		MetaTenantWebInfoRuntime twebr = handler.getConfigRuntime(MetaTenantWebInfoRuntime.class);
		String tenantPermErrorTemplate = (twebr != null ?
				twebr.errorUrlSelector(exp, webInvocation.getRequest(), webInvocation.getRequestStack().getRequestPath().getTargetPath(true)) : null);

		TemplateRuntime tr = null;
		if (StringUtil.isNotEmpty(tenantPermErrorTemplate)) {
			//念のためTemplateの存在チェック
			tr = ts.getRuntimeByName(tenantPermErrorTemplate);
			if (tr == null) {
				//指定Templateが見つからない場合は、エラーログを出力して、service-configで指定されているデフォルトを表示
				logger.error("can not find permission error template:" + tenantPermErrorTemplate + ", so use default permission error template");
			}
		}

		if (tr == null) {
			//Tenantの権限エラーTemplate未指定時はservice-configレベルから取得

			String defaultPermErrorTemplate = null;

			//デフォルトのErrorUrlSelectorで指定するTemplateの取得
			ErrorUrlSelector defaultSelector = wfService.getErrorUrlSelector();
			if (defaultSelector != null) {
				defaultPermErrorTemplate = defaultSelector.getErrorTemplateName(exp, webInvocation.getRequest(), webInvocation.getRequestStack().getRequestPath().getTargetPath(true));
			}
			if (defaultPermErrorTemplate != null) {
				tr = ts.getRuntimeByName(defaultPermErrorTemplate);
			}

			if (tr == null) {
				logger.error("can not find default permission error template:" + defaultPermErrorTemplate);
			}
		}

		if (tr == null) {
			logger.error("can not find permission error template, so throw exception.");
			throw exp;
		} else {
			tr.handle(webInvocation.getRequestStack());
		}
	}

	private String createReRequestPath(HttpServletRequest req) {
		StringBuilder buffer =  createParameter(req);
		if(buffer.length() > 0) {
			buffer.insert(0, "?");
		}

		WebRequestStack reqStack = WebRequestStack.getCurrent();
		buffer.insert(0, reqStack.getRequestPath().getTargetPath());
		buffer.insert(0, reqStack.getRequestPath().getTenantContextPath(req));
		return buffer.toString();
	}

	/**
	 * 現在のリクエスト情報すべてのパラメータをGET形式のParameterに構築する。
	 *
	 * @param request
	 *            リクエスト
	 * @return 生成した文字列Buffer
	 */
	private StringBuilder createParameter(HttpServletRequest request) {
		Map<String, String[]> m = request.getParameterMap();
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> entry : m.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			// 配列
			int length = values.length;
			try {
				if (length == 1) {
					sb.append(key).append("=").append(URLEncoder.encode(values[0], "UTF-8")).append("&");
				} else if (length != 0) {
					for (int i = 0; i < length; i++) {
						sb.append(key).append("=").append(URLEncoder.encode(values[i], "UTF-8")).append("&");
					}
				}
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException(e);
			}
		}
		return sb;
	}

	@Override
	public void interceptResult(RequestInvocation invocation) {

		RequestContext request = invocation.getRequest();
		Boolean logoutFlag = (Boolean) request.getAttribute(LOGOUT_FLAG);
		if (logoutFlag != null && logoutFlag) {
			String template = (String) request.getAttribute(TEMPLATE_AFTER_LOGOUT);
			if (template != null) {
				doTemplate(template, invocation);
				return;
			}

			String redirectPath = (String) request.getAttribute(REDIRECT_PATH_AFTER_LOGOUT);
			if (redirectPath != null) {
				doRedirect(redirectPath, invocation);
				return;
			}
		}

		invocation.proceedResult();
	}

	private void doRedirect(String redirectPath, RequestInvocation invocation) {
		if (logger.isDebugEnabled()) {
			logger.debug("after loggedout redirect URL specified, so redirect to " + redirectPath);
		}
		try {
			((WebInvocationImpl) invocation).getRequestStack().getResponse().sendRedirect(StringUtil.removeLineFeedCode(redirectPath));
		} catch (IOException e) {
			throw new WebProcessRuntimeException(e);
		}
	}

	private void doTemplate(String template, RequestInvocation invocation) {
		if (logger.isDebugEnabled()) {
			logger.debug("after loggedout template specified, so do template:" + template);
		}
		TemplateService ts = ServiceRegistry.getRegistry().getService(TemplateService.class);
		TemplateRuntime tr = ts.getRuntimeByName(template);
		if (tr == null) {
			throw new WebProcessRuntimeException("after loggedout template specified, but " + template + " not defined.");
		}

		try {
			tr.handle(((WebInvocationImpl) invocation).getRequestStack());
		} catch (ServletException | IOException e) {
			throw new WebProcessRuntimeException(e);
		}
	}

}
