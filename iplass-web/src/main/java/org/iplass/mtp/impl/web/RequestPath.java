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
package org.iplass.mtp.impl.web;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.web.TenantWebInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestPath {
	private static Logger logger = LoggerFactory.getLogger(RequestPath.class);
	private static final String TENANT_NAME_HEADER = "X-Tenant-Name";
	public static final String ATTR_NAME = "mtp.requestPath";

	public enum PathType {
		REJECT,
		UNKNOWN,
		REST,
		ACTION,
		THROUGH
	}

	public enum TenantAuthType {
		//TODO ドメインとサブドメインのパターンを分ける
		DOMAIN,
		FIXED,
		HEADER,
		PATH
	}

	private WebFrontendService wfService;

	private PathType pathType;
	private TenantAuthType tenantAuthType;

	private String tenantUrl;

	private String targetPath;//not decoded
	private String targetPathWithoutHeadSlash;//not decoded
	private String tenantContextPath;

	private boolean isValid;

	private Boolean isDirectAccess;

	//FIXME AdminConsoleから呼び出し用。adminConsoleもパス（やDomain）で解決する形にした場合、このコンストラクタ削除
	public RequestPath(String tenantUrl) {
		this.tenantUrl = tenantUrl;
	}

	public RequestPath(String targetPath, String tenantUrl, RequestPath parent) {
		this.wfService = parent.wfService;
		this.pathType = parent.pathType;
		this.tenantAuthType = parent.tenantAuthType;
		this.tenantUrl = tenantUrl;
		if (targetPath.startsWith("/")) {
			this.targetPath = targetPath;
		} else {
			this.targetPath = "/" + targetPath;
			this.targetPathWithoutHeadSlash = targetPath;
		}
	}

	public RequestPath(String targetPath, RequestPath parent) {
		this.wfService = parent.wfService;
		this.pathType = parent.pathType;
		this.tenantAuthType = parent.tenantAuthType;
		this.tenantUrl = parent.tenantUrl;
		if (targetPath.startsWith("/")) {
			this.targetPath = targetPath;
		} else {
			this.targetPath = "/" + targetPath;
			this.targetPathWithoutHeadSlash = targetPath;
		}
	}

	protected RequestPath(PathType pathType, String targetPath, TenantAuthType tenantAuthType, String tenantUrl, WebFrontendService wfService) {
		this.wfService = wfService;
		this.pathType = pathType;
		this.tenantAuthType = tenantAuthType;
		this.tenantUrl = tenantUrl;
		if (targetPath.startsWith("/")) {
			this.targetPath = targetPath;
		} else {
			this.targetPath = "/" + targetPath;
			this.targetPathWithoutHeadSlash = targetPath;
		}
	}

	public RequestPath(HttpServletRequest req, WebFrontendService wfService) {
		this.wfService = wfService;
		String pathWithoutServletContext = requestURIWithoutContext(req);
		if (pathWithoutServletContext.contains("..")) {
			String normReqPath = normalize(pathWithoutServletContext);
			if (logger.isDebugEnabled()) {
				logger.debug("request uri contains '..', so normalize uri from " + pathWithoutServletContext + " to " + normReqPath);
			}
			if (normReqPath == null) {
				pathWithoutServletContext = null;
			} else {
				pathWithoutServletContext = normReqPath;
				isValid = true;
			}
		} else {
			isValid = true;
		}

		if (isValid) {
			if (wfService.isExcludePath(pathWithoutServletContext)) {
				pathType = PathType.UNKNOWN;
			} else {
				//tenant url(domain or path or header or fixed)
				if (wfService.isTenantAsDomain()) {
					tenantUrl = req.getServerName();
					tenantAuthType = TenantAuthType.DOMAIN;
					targetPath = pathWithoutServletContext;
				} else if (wfService.getFixedTenant() != null) {
					tenantUrl = wfService.getFixedTenant();
					tenantAuthType = TenantAuthType.FIXED;
					targetPath = pathWithoutServletContext;
				} else {
					String headerVal = req.getHeader(TENANT_NAME_HEADER);
					if (headerVal != null) {
						if (!headerVal.equals("/")) {
							headerVal = "/" + headerVal;
						}
						tenantUrl = headerVal;
						tenantAuthType = TenantAuthType.HEADER;
						targetPath = pathWithoutServletContext;
					} else {
						if (pathWithoutServletContext.equals("/")) {
							tenantUrl = "/";
							targetPath = "/";
						} else {
							int index = pathWithoutServletContext.indexOf('/', 1);
							if (index < 0) {
								tenantUrl = "/";
								targetPath = pathWithoutServletContext;
							} else {
								tenantUrl = pathWithoutServletContext.substring(0, index);
								targetPath = pathWithoutServletContext.substring(index);
							}
						}
						tenantAuthType = TenantAuthType.PATH;
					}

					//ReqType
					if (!wfService.isAcceptPathes(targetPath)) {
						pathType = PathType.REJECT;
					} else if (wfService.isThroughPath(targetPath)) {
						pathType = PathType.THROUGH;
					} else if (wfService.isRestPath(targetPath)) {
						pathType = PathType.REST;
					} else {
						pathType = PathType.ACTION;
					}
				}
			}
		}
	}

	public String getTargetSubPath(String prefixPath, boolean withoutHeadSlash) {
		String path = getTargetPath(withoutHeadSlash);
		if (path.length() > prefixPath.length() && path.startsWith(prefixPath)) {
			return path.substring(prefixPath.length());
		} else {
			return null;
		}
	}

	public String getTenantContextPath(HttpServletRequest req) {
		if (tenantContextPath == null) {
			Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			if (isDirectAccess(req)) {
				switch (tenantAuthType) {
				case DOMAIN:
					tenantContextPath = req.getContextPath() + "/" + tenant.getUrl();
					break;
				case FIXED:
					tenantContextPath = req.getContextPath();
					break;
				case HEADER:
					if (wfService.isTenantAsDomain()) {
						tenantContextPath = req.getContextPath() + "/" + tenant.getUrl();
					} else {
						if ("/".equals(tenant.getUrl())) {
							tenantContextPath = req.getContextPath();
						} else {
							tenantContextPath = req.getContextPath() + tenant.getUrl();
						}
					}
					break;
				case PATH:
					if ("/".equals(tenant.getUrl())) {
						tenantContextPath = req.getContextPath();
					} else {
						tenantContextPath = req.getContextPath() + tenant.getUrl();
					}
					break;
				default:
					tenantContextPath = req.getContextPath();
					break;
				}
			} else {
				TenantWebInfo tweb = WebUtil.getTenantWebInfo(tenant);
				if (tweb.getUrlForRequest() != null) {
					tenantContextPath = tweb.getUrlForRequest();
					if ("/".equals(tenantContextPath)) {
						tenantContextPath = "";
					}
				} else {
					switch (tenantAuthType) {
					case DOMAIN:
					case FIXED:
						tenantContextPath = req.getContextPath();
						break;
					case HEADER:
						if (wfService.isTenantAsDomain()) {
							tenantContextPath = req.getContextPath();
						} else {
							if ("/".equals(tenant.getUrl())) {
								tenantContextPath = req.getContextPath();
							} else {
								tenantContextPath = req.getContextPath() + tenant.getUrl();
							}
						}
						break;
					case PATH:
						if ("/".equals(tenant.getUrl())) {
							tenantContextPath = req.getContextPath();
						} else {
							tenantContextPath = req.getContextPath() + tenant.getUrl();
						}
						break;
					default:
						tenantContextPath = req.getContextPath();
						break;
					}
				}
			}
		}

		return tenantContextPath;
	}

	public boolean isDirectAccess(HttpServletRequest req) {
		if (isDirectAccess == null) {
			if (wfService.getDirectAccessPort() != null) {
				isDirectAccess = wfService.getDirectAccessPort().equals(String.valueOf(req.getServerPort()));
			} else {
				isDirectAccess = Boolean.FALSE;
			}
		}
		return isDirectAccess.booleanValue();
	}

	public PathType getPathType() {
		return pathType;
	}

	public TenantAuthType getTenantAuthType() {
		return tenantAuthType;
	}

	public String getTenantUrl() {
		return tenantUrl;
	}

	public String getTargetPath() {
		return targetPath;
	}
	public String getTargetPath(boolean withoutHeadSlash) {
		if (withoutHeadSlash) {
			if (targetPathWithoutHeadSlash == null) {
				if (targetPath.startsWith("/")) {
					targetPathWithoutHeadSlash = targetPath.substring(1);
				} else {
					targetPathWithoutHeadSlash = targetPath;
				}
			}
			return targetPathWithoutHeadSlash;
		} else {
			return targetPath;
		}
	}

	public boolean isValid() {
		return isValid;
	}

	private String requestURIWithoutContext(HttpServletRequest req) {
		String reqUri = req.getRequestURI();
		int contextPathLength = req.getContextPath().length();
		if (contextPathLength == 0) {// "/"の場合
			return reqUri;
		} else {
			return reqUri.substring(contextPathLength);
		}
	}

	//from org.apache.tomcat.util.http.RequestUtil#normalize
	private String normalize(String path) {

        if (path == null)
            return null;

        // Create a place for the normalized path
        String normalized = path;

        if (normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');

        if (normalized.equals("/."))
            return "/";

        // Add a leading "/" if necessary
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                normalized.substring(index + 3);
        }

        // Return the normalized path that we have completed
        return (normalized);
    }

}
