/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.entityexplorer;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.tools.entityport.EntityDataExportCondition;
import org.iplass.mtp.impl.tools.pack.PackageCreateCondition;
import org.iplass.mtp.impl.tools.pack.PackageService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Entity Package Export用Service実装クラス
 */
public class EntityPackageDownloadServiceImpl extends AdminDownloadService {

	private static final long serialVersionUID = -959701477334971414L;

	private static final Logger logger = LoggerFactory.getLogger(EntityPackageDownloadServiceImpl.class);

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		//パラメータの取得
		final String defName = req.getParameter("definitionName");
		final String whereClause = req.getParameter("whereClause");
		final String orderByClause = req.getParameter("orderByClause");
		final boolean isSearchAllVersion = Boolean.valueOf(req.getParameter("isSearchAllVersion"));

		//MetaDataEntryの取得
		String entityPath =  EntityService.ENTITY_META_PATH + defName.replace(".", "/");
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(entityPath);
		if (entry == null) {
			logger.error("failed to export entity package. not found entity definition. path =" + entityPath);
        	throw new DownloadRuntimeException("not found entity definition. path =" + entityPath);
		}

		final String fileName = tenantId + "-" + defName + ".zip";

		final EntityDataExportCondition entityCondition = new EntityDataExportCondition();
		entityCondition.setWhereClause(whereClause);
		entityCondition.setOrderByClause(orderByClause);
		entityCondition.setVersioned(isSearchAllVersion);

		PackageCreateCondition packCondition = new PackageCreateCondition();
		packCondition.setEntityPaths(Arrays.asList(entityPath));
		packCondition.addEntityCondition(defName, entityCondition);

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("EntityExplorerPackageDownload", fileName, entityCondition);

		//Export
		try {
			DownloadUtil.setZipResponseHeader(resp, fileName);

			PackageService packageService = ServiceRegistry.getRegistry().getService(PackageService.class);
			packageService.write(resp.getOutputStream(), packCondition);
		} catch (IOException e) {
			logger.error("failed to export entity package. path =" + entityPath, e);
        	throw new DownloadRuntimeException(e);
		}

	}

}
