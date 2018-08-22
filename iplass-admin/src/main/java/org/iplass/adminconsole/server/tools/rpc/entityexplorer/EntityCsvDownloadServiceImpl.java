/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.tools.entityport.EntityDataExportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityPortingService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Entity CSV Export用Service実装クラス
 */
public class EntityCsvDownloadServiceImpl extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(EntityCsvDownloadServiceImpl.class);

	/** シリアルバージョンNo */
	private static final long serialVersionUID = -3459617043325559477L;

	public EntityCsvDownloadServiceImpl() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		download(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		download(req, resp);
	}

	private void download(final HttpServletRequest req, final HttpServletResponse resp) {

		//パラメータの取得
		final int tenantId = Integer.parseInt(req.getParameter("tenantId"));
		final String defName = req.getParameter("definitionName");
		final String whereClause = req.getParameter("whereClause");
		final String orderByClause = req.getParameter("orderByClause");
		final boolean isSearchAllVersion = Boolean.valueOf(req.getParameter("isSearchAllVersion"));

		final String fileName = tenantId + "-" + defName + ".csv";

		final EntityDataExportCondition condition = new EntityDataExportCondition();
		condition.setWhereClause(whereClause);
		condition.setOrderByClause(orderByClause);
		condition.setVersioned(isSearchAllVersion);

		AuthUtil.authCheckAndInvoke(getServletContext(), req, resp, tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {
				AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
				aals.logDownload("EntityExplorerCsvDownload", fileName, condition);

				//MetaDataEntryの取得
				String entityPath =  EntityService.ENTITY_META_PATH + defName.replace(".", "/");
				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(entityPath);

				//Export
				try {
					DownloadUtil.setCsvResponseHeader(resp, fileName);

					EntityPortingService entityService = ServiceRegistry.getRegistry().getService(EntityPortingService.class);
					entityService.write(resp.getOutputStream(), entry, condition);
				} catch (IOException e) {
					logger.error("failed to export entity. path =" + entityPath, e);
		        	throw new DownloadRuntimeException(e);
				}

				return null;
			}

		});

	}

}
