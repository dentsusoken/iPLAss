/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.tenant.web.MetaTenantWebInfo.MetaTenantWebInfoRuntime;
import org.iplass.mtp.impl.tenant.MetaTenantService;
import org.iplass.mtp.impl.web.ErrorUrlSelector;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.impl.web.actionmapping.WebInvocationImpl;
import org.iplass.mtp.impl.web.template.MetaTemplate.TemplateRuntime;
import org.iplass.mtp.impl.web.template.TemplateService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.interceptor.RequestInterceptor;
import org.iplass.mtp.web.interceptor.RequestInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExceptionInterceptor implements RequestInterceptor, ServiceInitListener<ActionMappingService> {
	private static Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

	private List<String> noHande;
	private List<String> eliminate;

	private List<Class<?>[]> noHandeClass;
	private List<Class<?>[]> eliminateClass;

	private WebFrontendService wfService = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
	private MetaTenantService metaTenantService = ServiceRegistry.getRegistry().getService(MetaTenantService.class);
	private TemplateService tService = ServiceRegistry.getRegistry().getService(TemplateService.class);

	@Override
	public void inited(ActionMappingService service, Config config) {
		if (noHande != null) {
			noHandeClass = toClassList(noHande);
		}
		if (eliminate != null) {
			eliminateClass = toClassList(eliminate);
		}
	}

	public static List<Class<?>[]> toClassList(List<String> classNames) {
		List<Class<?>[]> ret = new ArrayList<>(classNames.size());
		for (String cn: classNames) {
			String[] splits = cn.split(":");
			try {
				Class<?>[] cl = new Class<?>[splits.length];
				for (int i = 0; i < splits.length; i++) {
						cl[i] = Class.forName(splits[i]);
				}
				ret.add(cl);
			} catch (ClassNotFoundException e) {
				logger.warn("ignore cause ClassNotFoundException:" + cn);
			}
		}
		return ret;
	}

	@Override
	public void destroyed() {
	}

	public List<String> getEliminate() {
		return eliminate;
	}

	public void setEliminate(List<String> eliminate) {
		this.eliminate = eliminate;
	}

	public List<String> getNoHande() {
		return noHande;
	}

	public void setNoHande(List<String> noHande) {
		this.noHande = noHande;
	}


	public static boolean match(List<Class<?>[]> classList, Throwable e) {
		if (classList == null) {
			return false;
		}
		for (Class<?>[] nha: classList) {
			Throwable current = e;
			for (int i = 0; i < nha.length; i++) {
				if (current == null || !nha[i].isAssignableFrom(current.getClass())) {
					break;
				}
				if (i == nha.length - 1) {
					return true;
				}
				current = e.getCause();
			}
		}
		return false;

	}

	@Override
	public void intercept(RequestInvocation invocation) {

		try {
			invocation.proceedRequest();

		} catch (RuntimeException e) {
			WebInvocationImpl webInvocation = (WebInvocationImpl) invocation;
			if (!webInvocation.getRequestStack().isClientDirectRequest()) {
				throw e;
			}

			if (match(noHandeClass, e)) {
				throw e;
			}

			if (match(eliminateClass, e)) {
				logger.debug("eliminate exception:" + e);
				return;
			}

			//内部で読み込まれた場合(include)は、エラー画面が出せない。
			//また、既にレンダリングが開始されている場合（Template内での例外）はエラー画面出せない。
			//TODO 出せるように出力を一旦すべてメモリにためる？？
			try {
				if (!webInvocation.getRequestStack().getResponse().isCommitted()
						&& invocation.getRequest().getAttribute(WebRequestContext.MARK_USE_OUTPUT_STREAM) == null) {

					webInvocation.getRequestStack().getResponse().resetBuffer();
					if (!(e instanceof ApplicationException)) {
						webInvocation.getRequestStack().getResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					webInvocation.getRequest().setAttribute(WebRequestConstants.EXCEPTION, e);

					WebUtil.setCacheControlHeader(webInvocation.getRequestStack(), false, -1);

					Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
					MetaTenantHandler th = metaTenantService.getRuntimeByName(tenant.getName());
//					String errorTemplateName = th.errorUrlSelector(e, webInvocation.getRequest(), webInvocation.getRequestStack().getRequestPath().getTargetPath(true));
					MetaTenantWebInfoRuntime twebr = th.getConfigRuntime(MetaTenantWebInfoRuntime.class);
					String errorTemplateName = (twebr != null ?
							twebr.errorUrlSelector(e, webInvocation.getRequest(), webInvocation.getRequestStack().getRequestPath().getTargetPath(true)) : null);

					TemplateRuntime tr = null;
					if (StringUtil.isNotEmpty(errorTemplateName)) {
						//念のためTemplateの存在チェック
						tr = tService.getRuntimeByName(errorTemplateName);
						if (tr == null) {
							//指定Templateが見つからない場合は、エラーログを出力して、service-configで指定されているデフォルトを表示
							logger.error("can not find error template:" + errorTemplateName + ", so use default error template");
						}
					}

					if (tr == null) {
						String defaultErrorTemplate = null;

						//デフォルトのErrorUrlSelectorで指定するTemplateの取得
						ErrorUrlSelector defaultSelector = wfService.getErrorUrlSelector();
						if (defaultSelector != null) {
							defaultErrorTemplate = defaultSelector.getErrorTemplateName(e, webInvocation.getRequest(), webInvocation.getRequestStack().getRequestPath().getTargetPath(true));
						}
						if (defaultErrorTemplate != null) {
							tr = tService.getRuntimeByName(defaultErrorTemplate);
						}

						if (tr == null) {
							logger.error("can not find default error template:" + defaultErrorTemplate);
						}
					}

					if (tr == null) {
						logger.error("can not find error template, so throw exception.");
						throw e;
					} else {
						tr.handle(webInvocation.getRequestStack());
					}

				} else {
					logger.warn("response already commited, so can not show error page of " + e);
				}
			} catch (IOException | ServletException e1) {
				e1.addSuppressed(e);
				if (match(eliminateClass, e1)) {
					logger.debug("eliminate exception(at exception handling):" + e1);
				} else {
					logger.error(e1.getMessage(), e1);
				}
			}
		}
	}

}
