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

package org.iplass.mtp.impl.view.top.parts;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.top.parts.LastLoginParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.web.template.TemplateUtil;

public class MetaLastLoginParts extends MetaTemplateParts {

	private static final long serialVersionUID = 4013739635261402238L;

	public static MetaLastLoginParts createInstance(TopViewParts parts) {
		return new MetaLastLoginParts();
	}
	
	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		LastLoginParts login = (LastLoginParts) parts;
		fillFrom(login);
	}

	@Override
	public TopViewParts currentConfig() {
		LastLoginParts parts = new LastLoginParts();
		fillTo(parts);
		
		return parts;
	}

	@Override
	public TemplatePartsHandler createRuntime() {
		return new TemplatePartsHandler(this) {

			private final String TEMPLATE_PATH = "gem/auth/LastLoginParts";

			@Override
			public void setAttribute(HttpServletRequest req) {
				RequestContext request = TemplateUtil.getRequestContext();
				request.setAttribute("lastLoginParts", currentConfig());
			}

			@Override
			public void clearAttribute(HttpServletRequest req) {
				RequestContext request = TemplateUtil.getRequestContext();
				request.setAttribute("lastLoginParts", null);
			}

			@Override
			public boolean isWidget() {
				return false;
			}

			@Override
			public boolean isParts() {
				return true;
			}

			@Override
			public String getTemplatePathForWidget(HttpServletRequest req) {
				return null;
			}

			@Override
			public String getTemplatePathForParts(HttpServletRequest req) {
				return TEMPLATE_PATH;
			}
		};
	}
}
