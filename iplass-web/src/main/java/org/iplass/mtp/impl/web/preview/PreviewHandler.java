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
package org.iplass.mtp.impl.web.preview;

import java.sql.Timestamp;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.WebUtil;

public class PreviewHandler {
	public static final String CURRENT_TIMESTAMP ="org.iplass.mtp.currentTimestamp";

	public void init(RequestContext requestCotnext) {
		ExecuteContext exec = ExecuteContext.getCurrentContext();
//		if (exec.getCurrentTenant() != null && exec.getCurrentTenant().isUsePreview()) {
		if (isUsePreview(exec)) {
			SessionContext session = requestCotnext.getSession(false);
			if (session != null) {
				Timestamp ts = (Timestamp) session.getAttribute(CURRENT_TIMESTAMP);
				if (ts != null) {
					exec.setCurrentTimestamp(ts);
				}
			}
		}
	}

	public boolean isPreview(RequestContext requestCotnext) {
//		ExecuteContext exec = ExecuteContext.getCurrentContext();
//		if (exec.getCurrentTenant() != null && exec.getCurrentTenant().isUsePreview()) {
		if (isUsePreview(ExecuteContext.getCurrentContext())) {
			SessionContext session = requestCotnext.getSession(false);
			if (session != null) {
				Timestamp ts = (Timestamp) session.getAttribute(CURRENT_TIMESTAMP);
				if (ts != null) {
					return true;
				}
			}
		}

		return false;
	}

	public Timestamp getPreviewDate(RequestContext requestContext) {
		SessionContext session = requestContext.getSession(false);
		if (session != null) {
			return (Timestamp) session.getAttribute(CURRENT_TIMESTAMP);
		}

		return null;
	}

	public void setPreviewDate(Timestamp ts, RequestContext requestContext) {
//		ExecuteContext exec = ExecuteContext.getCurrentContext();
//		if (exec.getCurrentTenant() != null && exec.getCurrentTenant().isUsePreview()) {
		if (isUsePreview(ExecuteContext.getCurrentContext())) {
			SessionContext session = requestContext.getSession();
			session.setAttribute(CURRENT_TIMESTAMP, ts);
		}
	}

	private boolean isUsePreview(ExecuteContext exec) {

		if (exec.getCurrentTenant() != null) {
			return WebUtil.getTenantWebInfo(exec.getCurrentTenant()).isUsePreview();
		}
		return false;
	}

}
