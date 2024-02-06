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

package org.iplass.adminconsole.server.tools.rpc.metaexplorer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.service.AdminEntityManager;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.FILETYPE;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ConfigDownloadProperty;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.MetaDataExplorerRuntimeException;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.RepositoryType;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.TargetMode;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.tools.metaport.MetaDataTagEntity;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * MetaDataConfigExport用Service実装クラス
 */
public class MetaDataConfigDownloadServiceImpl extends AdminDownloadService {

	private static final long serialVersionUID = -3459617043325559477L;

	private static final Logger logger = LoggerFactory.getLogger(MetaDataConfigDownloadServiceImpl.class);

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		//パラメータの取得
		final String mode = req.getParameter(ConfigDownloadProperty.TARGET_MODE);

		TargetMode targetMode = TargetMode.valueOf(mode);
		if (TargetMode.LIVE.equals(targetMode)) {
			//Repository Download

			final String paths = req.getParameter(ConfigDownloadProperty.TARGET_PATH);
			final String fileType = req.getParameter(ConfigDownloadProperty.FILE_TYPE);
			final String repoType = req.getParameter(ConfigDownloadProperty.REPOSITORY_TYPE);

			repositoryDownload(tenantId, paths, fileType, repoType, resp);
		} else if (TargetMode.TAG.equals(targetMode)) {
			//Tag Download

			final String fileOid = req.getParameter(ConfigDownloadProperty.FILE_OID);
			if (fileOid == null) {
				throw new IllegalArgumentException(rs("tools.metaexplorer.MetaDataConfigDownloadServiceImpl.canNotGetData"));
			}

			tagDownload(tenantId, fileOid, resp);

		}
	}

	private void repositoryDownload(int tenantId, String paths, String fileType, String repoType, HttpServletResponse resp) {
		//ファイルタイプチェック
		//とりあえずXML以外は未サポート
		if (!FILETYPE.XML.equals(FILETYPE.valueOf(fileType))) {
			return;
		}

		xmlDownload(tenantId, getPathArray(paths), repoType, resp);
	}

	private String[] getPathArray(String paths) {
		if (paths == null) {
			throw new IllegalArgumentException(rs("tools.metaexplorer.MetaDataConfigDownloadServiceImpl.canNotGetPath"));
		}
		String[] pathArray = paths.split(",");

		//ソート
		Arrays.sort(pathArray, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});
		return pathArray;
	}

	private void xmlDownload(final int tenantId, final String[] paths, final String repoType, final HttpServletResponse resp) {

		//Repository種類取得
		RepositoryType repositoryType = RepositoryType.valueOfTypeName(repoType);

		List<String> entryPaths = new ArrayList<>();
		for (String path : paths) {
			if (path.endsWith("*")) {
				//ContextPathの全体選択の場合
				String contextPath = path.substring(0, path.length() - 1);

				List<MetaDataEntryInfo> nodes = MetaDataContext.getContext().definitionList(contextPath);
				entryPaths = new ArrayList<>();
				for (MetaDataEntryInfo node : nodes) {
					if (RepositoryType.SHARED.equals(repositoryType)
							&& !node.isSharable()) {
						continue;
					} else if (RepositoryType.LOCAL.equals(repositoryType)
						&& node.isSharable()) {
						continue;
					}
					entryPaths.add(node.getPath());
				}
			} else {
				//Entryの選択の場合
				entryPaths.add(path);
			}
		}

		String fileName = tenantId + "-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".xml";

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("MetaDataConfigDownload", fileName, "path:" + Arrays.toString(entryPaths.toArray()));

		//出力処理
		try (
			OutputStreamWriter os = new OutputStreamWriter(resp.getOutputStream(), "UTF-8");
			PrintWriter writer = new PrintWriter(os);
		) {
	        // ファイル名を設定
			DownloadUtil.setResponseHeader(resp, MediaType.TEXT_XML, fileName);

			//Export
			MetaDataPortingService metaService = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);
			metaService.write(writer, entryPaths);

        } catch (IOException e) {
            throw new DownloadRuntimeException(e);
		}
	}

	private void tagDownload(int tenantId, String fileOid, HttpServletResponse resp) {

		//Entityの取得
		EntityManager em = AdminEntityManager.getInstance();
		Entity entity = em.load(fileOid, MetaDataTagEntity.ENTITY_DEFINITION_NAME);
		if (entity == null) {
			throw new MetaDataExplorerRuntimeException(rs("tools.metaexplorer.MetaDataConfigDownloadServiceImpl.canNotGetTagFile", fileOid));
		}

		//Fileの取得
		BinaryReference binaryReference = entity.getValue(MetaDataTagEntity.METADATA);

		try (InputStream is = em.getInputStream(binaryReference)){

			if (is == null) {
				throw new MetaDataExplorerRuntimeException(rs("tools.metaexplorer.MetaDataConfigDownloadServiceImpl.canNotGetTagFile", fileOid));
			}

			String fileName = tenantId + "-" + entity.getName() + ".xml";

			AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
			aals.logDownload("MetaDataConfigDownload", fileName, "tagName:" + entity.getName());

			//出力
			try (
				OutputStreamWriter os = new OutputStreamWriter(resp.getOutputStream(), "UTF-8");
				PrintWriter writer = new PrintWriter(os);
			) {

				//ファイル名設定
				DownloadUtil.setResponseHeader(resp, MediaType.TEXT_XML, fileName);

				//出力
				IOUtils.copy(is, writer, "UTF-8");
			}

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
            throw new DownloadRuntimeException(e);
		}
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
