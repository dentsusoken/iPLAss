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

package org.iplass.mtp.impl.web.actionmapping;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import jakarta.servlet.ServletException;

import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.actionmapping.definition.result.RedirectResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedirectResult extends Result {

	private static final long serialVersionUID = 5366716507713343514L;
	private static Logger logger = LoggerFactory.getLogger(RedirectResult.class);

	private String redirectPathAttributeName;
	private boolean allowExternalLocation = false;

	public boolean isAllowExternalLocation() {
		return allowExternalLocation;
	}

	public void setAllowExternalLocation(boolean allowExternalLocation) {
		this.allowExternalLocation = allowExternalLocation;
	}

	public RedirectResult() {
	}

	public RedirectResult(String cmdStatus, String redirectPathAttributeName, boolean allowExternalLocation) {
		setCommandResultStatus(cmdStatus);
		this.redirectPathAttributeName = redirectPathAttributeName;
		this.allowExternalLocation = allowExternalLocation;
	}

	public String getRedirectPathAttributeName() {
		return redirectPathAttributeName;
	}

	public void setRedirectPathAttributeName(String redirectPathAttributeName) {
		this.redirectPathAttributeName = redirectPathAttributeName;
	}

	@Override
	public ResultRuntime createRuntime() {
		return new RedirectResultRuntime();
	}

	@Override
	public void applyConfig(ResultDefinition definition) {
		fillFrom(definition);
		RedirectResultDefinition def = (RedirectResultDefinition) definition;
		redirectPathAttributeName = def.getRedirectPath();
		allowExternalLocation = def.isAllowExternalLocation();
	}

	@Override
	public ResultDefinition currentConfig() {
		RedirectResultDefinition definition = new RedirectResultDefinition();
		fillTo(definition);
		definition.setRedirectPath(redirectPathAttributeName);
		definition.setAllowExternalLocation(allowExternalLocation);
		return definition;
	}

	private class RedirectResultRuntime extends ResultRuntime {

		@Override
		public RedirectResult getMetaData() {
			return RedirectResult.this;
		}

		@Override
		public void handle(WebRequestStack requestContext)
				throws ServletException, IOException {
			String redirectPath = (String) requestContext.getRequestContext().getAttribute(redirectPathAttributeName);
			if (redirectPath == null) {
				throw new ActionMappingRuntimeException("redirectPath can not specify by attributeName:" + redirectPathAttributeName);
			}

			// リダイレクト先に外部サイトが指定されていた場合はエラー
			if (!allowExternalLocation && !WebUtil.isValidInternalUrl(redirectPath)) {

				String serverName = requestContext.getRequest().getServerName();
				String redirectServerName = "";
				
				try {
					URI uri = new URI(redirectPath);
					redirectServerName = uri.getHost();
				} catch (URISyntaxException e) {
					throw new ActionMappingRuntimeException("Invalid redirect URL:" + redirectPath, e);
				}

				if (serverName.equals(redirectServerName)) {
					if (logger.isDebugEnabled()) {
						logger.debug("redirect to URL:" + redirectPath);
					}
					requestContext.getResponse().sendRedirect(StringUtil.removeLineFeedCode(redirectPath));
				} else {
					throw new ActionMappingRuntimeException("Invalid redirect URL:" + redirectPath);
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("redirect to URL:" + redirectPath);
				}
				requestContext.getResponse().sendRedirect(StringUtil.removeLineFeedCode(redirectPath));
			}
		}

		@Override
		public void finallyProcess(WebRequestStack requestContext) {
		}

	}

}
