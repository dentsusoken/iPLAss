/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.io.download;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.iplass.adminconsole.server.base.io.XsrfProtectedServlet;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.base.io.XsrfProtectedConstant;
import org.iplass.adminconsole.shared.metadata.dto.entity.EntityJavaMappingClassDownloadProperty;
import org.iplass.mtp.util.StringUtil;

import com.google.gwt.user.client.rpc.XsrfToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Download用基底クラス
 *
 * <p>
 * 各種処理は doDownload を実装する。
 * </p>
 */
public abstract class AdminDownloadService extends XsrfProtectedServlet<Void> {

	private static final long serialVersionUID = -4465947965629579502L;

	@Override
	protected final Void prepareRequest(final HttpServletRequest req) throws ServletException, IOException {
		req.setCharacterEncoding(StandardCharsets.UTF_8.name());
		return null;
	}

	@Override
	protected final XsrfToken extractXsrfToken(final HttpServletRequest req, final Void context) {
		String token = req.getParameter(XsrfProtectedConstant.RequestParameterName.XSRF_TOKEN_KEY);
		return StringUtil.isNotBlank(token) ? new XsrfToken(token) : null;
	}

	@Override
	protected final void doProtectedService(final HttpServletRequest req, final HttpServletResponse resp,
			final Void context) throws ServletException, IOException {
		final int tenantId = Integer.parseInt(req.getParameter(EntityJavaMappingClassDownloadProperty.TENANT_ID));

		AuthUtil.authCheckAndInvoke(getServletContext(), req, resp, tenantId, () -> {
			doDownload(req, resp, tenantId);
			return null;
		});
	}

	/**
	 * Download処理
	 *
	 * @param req リクエスト
	 * @param resp レスポンス
	 * @param tenantId テナントID
	 */
	protected abstract void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId);
}
