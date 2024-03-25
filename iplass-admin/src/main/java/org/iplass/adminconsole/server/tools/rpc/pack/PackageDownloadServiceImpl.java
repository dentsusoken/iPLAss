/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.pack;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.service.AdminEntityManager;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageDownloadProperty;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.tools.pack.PackageEntity;
import org.iplass.mtp.spi.ServiceRegistry;


/**
 * PackageDownload用Service実装クラス
 */
public class PackageDownloadServiceImpl extends AdminDownloadService {

	private static final long serialVersionUID = -3459617043325559477L;

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		final String fileOid = req.getParameter(PackageDownloadProperty.FILE_OID);
		if (fileOid == null) {
			throw new IllegalArgumentException(rs("tools.pack.PackageDownloadServiceImpl.canNotGetData"));
		}

		//Entityの取得
		EntityManager em = AdminEntityManager.getInstance();
		Entity entity = em.load(fileOid, PackageEntity.ENTITY_DEFINITION_NAME);

		if (entity == null) {
			throw new RuntimeException(rs("tools.pack.PackageDownloadServiceImpl.canNotGetUploadFileInfo", fileOid));
		}

		//Fileの取得
		BinaryReference bin = entity.getValue(PackageEntity.ARCHIVE);

		String fileName = entity.getName();

		//ファイル名設定
		if (!fileName.endsWith(".zip")) {
			fileName = fileName + ".zip";
		}

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("PackageDownload", fileName, "packageName:" + entity.getName() + " lobId:" + bin.getLobId() + " entityName:" + PackageEntity.ENTITY_DEFINITION_NAME + " propertyName:" + PackageEntity.ARCHIVE);

		try (InputStream is = em.getInputStream(bin)){

			if (is == null) {
				throw new RuntimeException(rs("tools.pack.PackageDownloadServiceImpl.canNotGetUploadFile", fileOid));
			}

			//ファイル名設定
			DownloadUtil.setZipResponseHeader(resp, fileName);

			//出力
			IOUtils.copy(is, resp.getOutputStream());

		} catch (IOException e) {
            throw new DownloadRuntimeException(e);
		}
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
