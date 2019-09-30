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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.RequestContextWrapper;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.impl.web.actionmapping.cache.CachableHttpServletResponse;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentCache;
import org.iplass.mtp.impl.web.actionmapping.cache.blocks.RenderContentBlock;
import org.iplass.mtp.impl.web.template.MetaTemplate;
import org.iplass.mtp.impl.web.template.MetaTemplate.TemplateRuntime;
import org.iplass.mtp.impl.web.template.TemplateService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.web.TenantWebInfo;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.permission.ActionPermission;
import org.iplass.mtp.web.actionmapping.permission.RequestContextActionParameter;

public class WebUtil {
//	private static final String TENANT_CONTEXT_PATH = "tenantContextPath";

	//FIXME アプリケーションが利用するメソッドのapi.webパッケージへの移動


//	/** 認証対象外のURLの判定結果 AttributeKey */
//	private static final String EXECLUDE_URL = "ececlude_url";
//	private static final Object EXECLUDE_URL_VALUE = new Object();
//
	private WebUtil() {
	}

	public static boolean isValidInternalUrl(String url) {

		if (StringUtil.startsWithAny(StringUtil.lowerCase(url), new String[]{"http:","https:","//","/\\","/\t", "\\\\"})) {
			return false;
		}

		//念のため、URI形式でパースしてみてチェック
		try {
			URI uri = new URI(url);
			if (uri.getHost() == null) {
				return true;
			}
		} catch (URISyntaxException e) {
		}

		return false;
	}

	public static RequestContext getRequestContext() {
		WebRequestStack src = WebRequestStack.getCurrent();
		if (src != null) {
			return src.getRequestContext();
		} else {
			return null;
		}
	}

	/**
	 * 直接テンプレートをinclude（Commandは実行されない）。
	 *
	 *
	 * @param templateName テンプレート名
	 * @param req
	 * @param resp
	 * @param context
	 * @param page
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void includeTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp, ServletContext context, PageContext page)
			throws IOException, ServletException {
		includeTemplate(templateName, req, resp, context, page, null);
	}

	/**
	 * 直接テンプレートをinclude（Commandは実行されない）。
	 * その際、引数のRequestContextWrapperで入力値を上書きする。
	 *
	 * @param templateName テンプレート名
	 * @param req
	 * @param resp
	 * @param context
	 * @param page
	 * @param requestContext
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void includeTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp, ServletContext context, PageContext page, RequestContextWrapper requestContext)
			throws IOException, ServletException {

		WebRequestStack src = new WebRequestStack(requestContext, context, req, resp, page);
//		} else {
//			//Login画面は、ActionMapping経由せず呼び出される、、、
//			//TODO この処理はいるのか？？
//			src = new WebRequestStack(createApplicationUrl(req), context, req, resp, page);
//		}

		src.setIncludeTemplateStack(true);
		try {
			TemplateService tmplService = ServiceRegistry.getRegistry().getService(TemplateService.class);
			TemplateRuntime tmpl = tmplService.getRuntimeByName(templateName);
			if (tmpl != null) {
				tmpl.handle(src);
			} else {
				new WebProcessRuntimeException("template:" + templateName + " not defined.");
			}
		} finally {
			src.finallyProcess();
		}
	}

	/**
	 * actionNameで指定される別のActionをinclude。
	 *
	 * @param actionName
	 * @param req
	 * @param resp
	 * @param context
	 * @param page
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void include(String actionName, HttpServletRequest req, HttpServletResponse resp, ServletContext context, PageContext page)
			throws IOException, ServletException {
		include(actionName, req, resp, context, page, null);
	}

	/**
	 * actionNameで指定される別のActionをinclude。
	 * その際、引数のRequestContextWrapperで引数を上書きする。
	 *
	 * @param actionName
	 * @param req
	 * @param resp
	 * @param context
	 * @param page
	 * @param requestContext
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void include(String actionName, HttpServletRequest req, HttpServletResponse resp, ServletContext context, PageContext page, RequestContextWrapper requestContext)
			throws IOException, ServletException {

		//JSPのバッファをフラッシュ
		if (page != null) {
			page.getOut().flush();
		}

		WebRequestStack current = WebRequestStack.getCurrent();
		RequestContext reqContext = current.getRequestContext();

		ActionMappingService amService = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
		ActionMappingRuntime am = amService.getByPathHierarchy(actionName);
		if (am == null) {
			new WebProcessRuntimeException("path:" + actionName + " not defined.");
		}

		if (!am.getMetaData().isPublicAction()
				&& !AuthContext.getCurrentContext().checkPermission(new ActionPermission(actionName, new RequestContextActionParameter(reqContext)))) {
			throw new SecurityException(actionName + "の実行権限がありません");
		}

		WebRequestStack src = new WebRequestStack(new RequestPath(actionName, current.getRequestPath()), requestContext, context, req, resp, page);

		src.setIncludeStack(true);
		try {
			am.executeCommand(src);
		} finally {
			if (src != null) {
				src.finallyProcess();
			}
		}

		//JSPのバッファをフラッシュ
		if (page != null) {
			page.getOut().flush();
		}

		//TODO RequestDispatcherを介さない方が高速か？
//		if (page != null) {
//			page.include("/" + templateName);
//		} else {
//			req.getRequestDispatcher("/" + templateName).include(req, resp);
//		}

//		TemplateService ts = ServiceRegistry.getRegistry().getService(TemplateService.class);
//		TemplateRuntime tmpl = ts.getTemplate(MTFContext.getCurrentContext().getClientTenantId(), templateName);
//		tmpl.handle(req, resp, context);
	}

//	public static void cacheTenantContextPath(HttpServletRequest req) {
//		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
//
//		String tenantPath = null;
//		if (!isDirectAccess(req)) {
//			//TenantのRoot対応
//			//テナント名//action名とかになるので、/のみの場合は/を付けないようにする
//			if (tenant.getTenantDisplayInfo() != null) {
//				tenantPath = tenant.getTenantDisplayInfo().getUrlForRequest();
//			}
//
//			if (tenantPath != null) {
//				if ("/".equals(tenantPath)) {
//					req.setAttribute(TENANT_CONTEXT_PATH, "");
//				} else {
//					req.setAttribute(TENANT_CONTEXT_PATH, tenantPath);
//				}
//			}
//		}
//
//		if (tenantPath == null) {
//			String tenantUrl = tenant.getUrl();
//			String tenantContextPath = null;
//			if ("/".equals(tenantUrl)) {
//				tenantContextPath = req.getContextPath();
//			} else {
//				tenantContextPath = req.getContextPath() + tenantUrl;
//			}
//			req.setAttribute(TENANT_CONTEXT_PATH, tenantContextPath);
//		}
//
//	}

	public static String getTenantContextPath(HttpServletRequest req) {

		//check cache
		WebRequestStack reqStack = WebRequestStack.getCurrent();
		if (reqStack != null) {
			return reqStack.getRequestPath().getTenantContextPath(req);
		}

		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();

		if (!isDirectAccess(req)) {
			//TenantのRoot対応
			//テナント名//action名とかになるので、/のみの場合は/を付けないようにする
			String tenantPath = getTenantWebInfo(tenant).getUrlForRequest();

			if (tenantPath != null) {
				if ("/".equals(tenantPath)) {
					return "";
				} else {
					return tenantPath;
				}
			}
		}

		String tenantUrl = tenant.getUrl();
		String tenantContextPath = null;
		if ("/".equals(tenantUrl)) {
			tenantContextPath = req.getContextPath();
		} else {
			tenantContextPath = req.getContextPath() + tenantUrl;
		}
		return tenantContextPath;
	}

	public static boolean isDirectAccess(HttpServletRequest req) {
		WebFrontendService ss = ServiceRegistry.getRegistry().getService(WebFrontendService.class);

		if (ss.getDirectAccessPort() != null) {
			return ss.getDirectAccessPort().equals(String.valueOf(req.getServerPort()));
		}
		return false;
	}

	public static String getStaticContentPath() {
		String path = ServiceRegistry.getRegistry().getService(WebFrontendService.class).getStaticContentPath();
		if (path == null) {
			WebRequestStack reqStack = WebRequestStack.getCurrent();
			if (reqStack != null) {
				path = reqStack.getRequest().getContextPath();
			}
		}
		if (path == null) {
			path = "";
		}
		return path;
	}

	/**
	 * レイアウトテンプレートで、コンテンツをレンダリングする場所で呼び出す。
	 *
	 * @param req
	 * @param resp
	 * @param context
	 * @param page
	 */
	public static void renderContent(HttpServletRequest req, HttpServletResponse resp, ServletContext context, PageContext page)
		throws IOException, ServletException {

		//JSPのバッファをフラッシュ
		if (page != null) {
			page.getOut().flush();
		}

		//TODO キャッシュのロジックがここに入ってしまう。。。汚い感じ。。。

		WebRequestStack layoutStack = WebRequestStack.getCurrent();
		//呼び出しのスタックは、実際のコンテンツテンプレート呼び出しのものにする
		WebRequestStack contentsStack = layoutStack.getPrevStack();

		Object content = layoutStack.getAttribute(MetaTemplate.CONTENT_TEMPLATE);
		if (content instanceof TemplateRuntime) {
			TemplateRuntime contentTemplate = (TemplateRuntime) content;


			//ContentCacheを実際のコンテンツのもに
			CachableHttpServletResponse response = (CachableHttpServletResponse) layoutStack.getRequest().getAttribute(CachableHttpServletResponse.CHSR_NAME);
			boolean prevDoCache = false;
			ContentCache layoutContentCache = null;
			if (response != null) {
				response.flushToContentCache();
				prevDoCache = response.isDoCache();
				layoutContentCache = response.getCurrentContentCache();
				if (layoutContentCache != null) {
					layoutContentCache.addContent(new RenderContentBlock());
				}
				ContentCache cc = (ContentCache) layoutStack.getAttribute(CachableHttpServletResponse.CONTENT_CACHE_NAME);
				if (cc != null) {
					response.setDoCache(true);
				} else {
					response.setDoCache(false);
				}
				response.setCurrentContentCache(cc);
			}

			WebRequestStack src = new WebRequestStack(contentsStack.getRequestPath(), null, context, req, resp, page);
			src.shareStackAttributeContext(contentsStack);
			src.setRenderContentStack(true);
			try {
				contentTemplate.handleContent(src);
			} finally {
				if (src != null) {
					src.finallyProcess();
				}
			}

			//JSPのバッファをフラッシュ
			if (page != null) {
				page.getOut().flush();
			}

			if (response != null) {
				response.flushToContentCache();
				response.setDoCache(prevDoCache);
				response.setCurrentContentCache(layoutContentCache);
			}
		} else if (content instanceof ContentCache) {
			ContentCache cc = (ContentCache) content;
			WebRequestStack src = new WebRequestStack(contentsStack.getRequestPath(), null, context, req, resp, page);
			src.shareStackAttributeContext(contentsStack);
			src.setRenderContentStack(true);
			try {
				CachableHttpServletResponse response = (CachableHttpServletResponse) layoutStack.getRequest().getAttribute(CachableHttpServletResponse.CHSR_NAME);
				boolean prevDoCache = response.isDoCache();

				ContentCache layoutContentCache = null;
				response.flushToContentCache();
				prevDoCache = response.isDoCache();
				layoutContentCache = response.getCurrentContentCache();
				if (layoutContentCache != null) {
					layoutContentCache.addContent(new RenderContentBlock());
				}

				ContentCache ccc = (ContentCache) layoutStack.getAttribute(CachableHttpServletResponse.CONTENT_CACHE_NAME);
				if (ccc != null) {
					response.setDoCache(true);
				} else {
					response.setDoCache(false);
				}
				response.setCurrentContentCache(ccc);

				cc.writeContent(src);

				//JSPのバッファをフラッシュ
				if (page != null) {
					page.getOut().flush();
				}

				response.flushToContentCache();
				response.setDoCache(prevDoCache);
				response.setCurrentContentCache(layoutContentCache);
			} finally {
				if (src != null) {
					src.finallyProcess();
				}
			}
		}
	}

	/**
	 * ResponceHeaderにキャッシュの設定をする。
	 * ただし、クライアント直リクエストかつレスポンスが未コミットの場合にのみ設定。
	 *
	 * @param req
	 * @param cache
	 */
	public static void setCacheControlHeader(WebRequestStack req, boolean cache, long maxAge) {
		setCacheControlHeader(req, cache, false, maxAge);
	}

	/**
	 * ResponceHeaderにキャッシュの設定をする。
	 * ただし、クライアント直リクエストかつレスポンスが未コミットの場合にのみ設定。
	 *
	 * @param req
	 * @param cache
	 * @param shared 共有キャッシュであるか
	 */
	public static void setCacheControlHeader(WebRequestStack req, boolean cache, boolean shared, long maxAge) {
		// クライアントキャッシュの設定
		if (req.isClientDirectRequest()
				&& !req.getResponse().isCommitted()) {
			if (cache) {
				if (maxAge < 0) {
					if (shared) {
						req.getResponse().setHeader("Cache-Control", "public");
					} else {
						req.getResponse().setHeader("Cache-Control", "private");
					}
				} else {
					if (shared) {
						req.getResponse().setHeader("Cache-Control", "public, max-age=" + maxAge);
					} else {
						req.getResponse().setHeader("Cache-Control", "private, max-age=" + maxAge);
					}
				}
			} else {
				req.getResponse().setHeader("Cache-Control",
							"no-store,no-cache");
				if ("HTTP/1.0".equals(req.getRequest().getProtocol())) {
					req.getResponse().setHeader("Pragma", "no-cache");
				}
			}
		}
	}

	/**
	 * ResponseHeaderにContentDispositionの設定をする。
	 *
	 * @param req リクエスト
	 * @param type ContentDispositionType
	 * @param fileName ファイル名
	 * @throws IOException
	 */
	public static void setContentDispositionHeader(WebRequestStack req, ContentDispositionType type, String fileName) throws IOException {
		setContentDispositionHeader(req.getRequest(), req.getResponse(), type, fileName);
	}

	/**
	 * ResponseHeaderにContentDispositionの設定をする。
	 *
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @param type ContentDispositionType
	 * @param fileName ファイル名
	 * @throws IOException
	 */
	public static void setContentDispositionHeader(HttpServletRequest req, HttpServletResponse resp, ContentDispositionType type, String fileName) throws IOException {

		String userAgent = req.getHeader("User-Agent");

		boolean noPoricy = true;
		boolean isMatch = false;
		if (userAgent != null) {
			WebFrontendService wfeService = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
			List<ContentDispositionPolicy> policies = wfeService.getContentDispositionPolicy();
			ContentDispositionPolicy defaultPolicy = null;
			if (policies != null) {
				noPoricy = false;
				for (ContentDispositionPolicy policy : policies) {
					if (policy.isDefault()) {
						defaultPolicy = policy;
						continue;
					}
					if (policy.match(userAgent, type)) {
						isMatch = true;
						resp.addHeader("Content-Disposition", StringUtil.removeLineFeedCode(policy.getContentDisposition(type, fileName)));
						break;
					}
				}
				if (!isMatch && defaultPolicy != null) {
					//デフォルト設定がある場合
					isMatch = true;
					resp.addHeader("Content-Disposition", StringUtil.removeLineFeedCode(defaultPolicy.getContentDisposition(type, fileName)));
				}
			}
		}

		if (noPoricy || !isMatch) {
			//指定がない場合はそのまま出力
			resp.addHeader("Content-Disposition", "attachment; " + StringUtil.removeLineFeedCode(fileName));
		}
	}

	public static TenantWebInfo getTenantWebInfo(Tenant tenant) {
		return (tenant.getTenantConfig(TenantWebInfo.class) != null ?
				tenant.getTenantConfig(TenantWebInfo.class) : new TenantWebInfo());
	}
}
