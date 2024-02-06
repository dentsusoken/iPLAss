/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * HistoryDialogのconfig export用Service実装クラス
 */
public class MetaDataHistoryConfigDownloadServiceImpl extends AdminDownloadService {

	private static final long serialVersionUID = -5549154268159682022L;

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		//パラメータの取得
		final String versionStr = req.getParameter("versions");
		final String definitionId = req.getParameter("metadataPath");

		if (versionStr == null || versionStr.isEmpty()) {
			throw new IllegalArgumentException(rs("MetaDataHistoryConfigDownloadServiceImpl.canNotGetExportTargetVersion"));
		}
		final String[] versions =  versionStr.split(",");

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

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
