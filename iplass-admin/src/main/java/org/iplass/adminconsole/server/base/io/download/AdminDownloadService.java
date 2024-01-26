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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.metadata.dto.entity.EntityJavaMappingClassDownloadProperty;

/**
 * Download用基底クラス
 * 
 * 各種処理は {@link #doDownload(HttpServletRequest, HttpServletResponse, int)} を実装する。
 */
public abstract class AdminDownloadService extends HttpServlet {

	private static final long serialVersionUID = -4465947965629579502L;

	public AdminDownloadService() {
		super();
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		//エンコード指定
		req.setCharacterEncoding(StandardCharsets.UTF_8.name());
		download(req, resp);
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		//エンコード指定
		req.setCharacterEncoding(StandardCharsets.UTF_8.name());
		download(req, resp);
	}

	/**
	 * 権限チェックを実行したのち、{@link #doDownload(HttpServletRequest, HttpServletResponse, int)} を実行します。
	 * 
	 * @param req リクエスト
	 * @param resp レスポンス
	 */
	protected void download(final HttpServletRequest req, final HttpServletResponse resp) {

		//パラメータの取得
		final int tenantId = Integer.parseInt(req.getParameter(EntityJavaMappingClassDownloadProperty.TENANT_ID));

		AuthUtil.authCheckAndInvoke(getServletContext(), req, resp, tenantId, ()-> {
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
