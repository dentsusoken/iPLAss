/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * HistoryDialogのconfig export用Service実装クラス
 */
public class MetaDataHistoryConfigDownloadServiceImpl extends HttpServlet {

	private static final long serialVersionUID = -5549154268159682022L;

	public MetaDataHistoryConfigDownloadServiceImpl() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		export(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		export(req, resp);
	}

	public void export(final HttpServletRequest req, final HttpServletResponse resp) {

		//パラメータの取得
		final int tenantId = Integer.parseInt(req.getParameter("tenantId"));
		final String versions = req.getParameter("versions");
		final String defName = req.getParameter("metadataPath");

		AuthUtil.authCheckAndInvoke(getServletContext(), req, resp, tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {

				xmlDownload(tenantId, getVersions(versions), defName, resp);

				return null;
			}

		});
	}

	private String[] getVersions(String versions) {

		if (versions == null || versions.isEmpty()) {
			throw new IllegalArgumentException(resourceString("canNotGetExportTargetVersion"));
		}
		return versions.split(",");
	}

	private void xmlDownload(final int tenantId, final String[] versions, final String definitionId, final HttpServletResponse resp) {

		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntryById(definitionId);
		String defName = entry.getMetaData().getName();

		String fileName = tenantId + "-" + defName + "_PropertyConfig" + ".xml";

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("MetaDataHistoryConfigDownload", fileName, "path:" + entry.getPath() + " version:" + Arrays.toString(versions));

		OutputStreamWriter osWriter = null;
		PrintWriter pWriter = null;
		try {
			//出力処理
			osWriter = new OutputStreamWriter(resp.getOutputStream(), "UTF-8");
			pWriter = new PrintWriter(osWriter);

	        // ファイル名を設定
			DownloadUtil.setResponseHeader(resp, MediaType.TEXT_XML, fileName);

			//Export
			MetaDataPortingService metaService = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);
			metaService.writeHistory(pWriter, definitionId, versions);

		} catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
		} finally {
			if (pWriter != null) {
				try {
					pWriter.close();
					pWriter = null;
				} finally {
					if (osWriter != null) {
						try {
							osWriter.close();
						} catch (IOException e) {
				            throw new RuntimeException(e);
						}
					}
				}
			}
		}
	}

	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("MetaDataHistoryConfigDownloadServiceImpl." + suffix, arguments);
	}

}
