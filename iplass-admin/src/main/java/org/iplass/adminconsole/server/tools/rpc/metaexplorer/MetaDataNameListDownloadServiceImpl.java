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

package org.iplass.adminconsole.server.tools.rpc.metaexplorer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.NameListDownloadProperty;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.RepositoryType;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.TargetMode;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.tools.metaport.MetaDataNameListCsvWriter;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * MetaDataNameListExport用Service実装クラス
 */
public class MetaDataNameListDownloadServiceImpl extends HttpServlet {

	private static final long serialVersionUID = 4158040186896564682L;

	private static final Logger logger = LoggerFactory.getLogger(MetaDataNameListDownloadServiceImpl.class);

	public MetaDataNameListDownloadServiceImpl() {
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
		final int tenantId = Integer.parseInt(req.getParameter(NameListDownloadProperty.TENANT_ID));
		final String mode = req.getParameter(NameListDownloadProperty.TARGET_MODE);

		AuthUtil.authCheckAndInvoke(getServletContext(), req, resp, tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {

				TargetMode targetMode = TargetMode.valueOf(mode);
				if (TargetMode.LIVE.equals(targetMode)) {
					//Repository Download

					String paths = req.getParameter(NameListDownloadProperty.TARGET_PATH);
					String repoType = req.getParameter(NameListDownloadProperty.REPOSITORY_TYPE);

					repositoryDownload(tenantId, paths, repoType, resp);
				} else if (TargetMode.TAG.equals(targetMode)) {
					//Tag Download

					String fileOid = req.getParameter(NameListDownloadProperty.FILE_OID);
					if (fileOid == null) {
						throw new IllegalArgumentException(rs("tools.metaexplorer.MetaDataNameListDownloadServiceImpl.canNotGetData"));
					}

					tagDownload(tenantId, fileOid, resp);

				}

				return null;
			}

		});
	}

	private void repositoryDownload(int tenantId, String pathString, String repoType, HttpServletResponse resp) {

		String[] paths = getPathArray(pathString);

		//Repository種類取得
		RepositoryType repositoryType = RepositoryType.valueOfTypeName(repoType);

		//パスの取得
		List<String> entryPaths = new ArrayList<String>();
		for (String path : paths) {
			if (path.endsWith("*")) {
				//ContextPathの全体選択の場合
				String contextPath = path.substring(0, path.length() - 1);

				List<MetaDataEntryInfo> nodes = MetaDataContext.getContext().definitionList(contextPath);
				entryPaths = new ArrayList<String>();
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

		String fileName = tenantId + "-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".csv";

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("MetaDataNameListDownload", fileName, "path:" + Arrays.toString(entryPaths.toArray()));

		try (MetaDataNameListCsvWriter writer = new MetaDataNameListCsvWriter(resp.getOutputStream())) {

			// ファイル名を設定
			DownloadUtil.setCsvResponseHeader(resp, fileName);

			//ヘッダ出力
			writer.writeHeader();

			//出力処理(多言語に対応するため、MetaDataEntryを取得)
			for (String path : entryPaths) {
				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(path);
				if (entry != null) {
					writer.writeEntry(entry);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
            throw new DownloadRuntimeException(e);
		}
	}

	private String[] getPathArray(String paths) {
		if (paths == null) {
			throw new IllegalArgumentException(rs("tools.metaexplorer.MetaDataNameListDownloadServiceImpl.canNotGetPath"));
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

	private void tagDownload(int tenantId, String tagOid, HttpServletResponse resp) {

		MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();

		//Tagのメタデータ情報取得
		TagEntryInfo entryInfo = logic.getTagMetaDataEntry(tagOid);

		String fileName = tenantId + "-" + entryInfo.getTagName() + ".csv";

		//ソート
		List<MetaDataEntry> entries = new ArrayList<MetaDataEntry>(entryInfo.getEntryInfo().getPathEntryMap().values());
		Collections.sort(entries, new Comparator<MetaDataEntry>() {
			@Override
			public int compare(MetaDataEntry o1, MetaDataEntry o2) {
				return o1.getPath().toLowerCase().compareTo(o2.getPath().toLowerCase());
			}
		});

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("MetaDataConfigDownload", fileName, "tagName:" + entryInfo.getTagName());

		try (MetaDataNameListCsvWriter writer = new MetaDataNameListCsvWriter(resp.getOutputStream())) {

	        // ファイル名を設定
			DownloadUtil.setCsvResponseHeader(resp, fileName);

			//ヘッダ出力
			writer.writeHeader();

			//出力
			for (MetaDataEntry entry : entries) {
				writer.writeEntry(entry);
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
