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

package org.iplass.adminconsole.server.tools.rpc.langexplorer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.rpc.i18n.LangDataLogic;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.adminconsole.shared.tools.dto.langexplorer.OutputMode;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.RepositoryType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.tools.lang.LangDataPortingInfo;
import org.iplass.mtp.impl.tools.lang.LangDataPortingService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 多言語情報Export用Service実装クラス
 */
public class LangCsvDownloadServiceImpl extends AdminDownloadService {

	/** シリアルバージョンNo */
	private static final long serialVersionUID = 5784071407365401948L;

	private static final Logger logger = LoggerFactory.getLogger(LangCsvDownloadServiceImpl.class);

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		//パラメータの取得
		final String mode = req.getParameter("mode");

		// LangDataを取得
		if (OutputMode.SINGLE.name().equals(mode)) {
			String path = req.getParameter("path");
			String definitionName = req.getParameter("definitionName");
			String fileName = tenantId + "-lang-data-" + definitionName + "_" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".csv";

			outputSingleMeta(resp, tenantId, path, definitionName, fileName);
		} else {
			String repoType = req.getParameter("repoType");
			String paths = req.getParameter("paths");
			String fileName = tenantId + "-lang-data" + "_" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".csv";

			outputMultiMeta(resp, tenantId, getPathArray(paths), fileName, repoType);
		}
	}

	private String[] getPathArray(String paths) {
		if (paths == null) {
			throw new IllegalArgumentException(rs("tools.langexplorer.LangCsvDownloadServiceImpl.canNotGetPath"));
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

	private void outputSingleMeta(HttpServletResponse resp, int tenantId, String path, String definitionName, String fileName) {
		//Pathの取得
		List<String> entryPaths = new ArrayList<>();
		entryPaths.add(path);

		//CSV出力
		writeCsv(resp, fileName, entryPaths);
	}

	private void outputMultiMeta(HttpServletResponse resp, int tenantId, String[] paths, String fileName, String repoType) {
		//Repository種類取得
		RepositoryType repositoryType = RepositoryType.valueOfTypeName(repoType);

		//Pathの取得
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

		//CSV出力
		writeCsv(resp, fileName, entryPaths);
	}

	private void writeCsv(HttpServletResponse resp, String fileName, List<String> entryPaths) {

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("LangCsvDownload", fileName, "path:" + Arrays.toString(entryPaths.toArray()));

		//定義の取得
		//パスから取得するので、Manager経由ではなくMedaDataRuntimeから定義を生成
		LangDataPortingService service = ServiceRegistry.getRegistry().getService(LangDataPortingService.class);
		List<LangDataPortingInfo> infoList = service.getDefinitionInfo(entryPaths);

		if (infoList != null && !infoList.isEmpty()) {
			LangDataLogic logic = new LangDataLogic();

			try (LangCsvWriter writer = new LangCsvWriter(resp.getOutputStream())){

				DownloadUtil.setCsvResponseHeader(resp, fileName);

				//ヘッダ出力
				writer.writeHeader();

				// 多言語情報出力
				for (LangDataPortingInfo langDataInfo : infoList) {
					Map<String, List<LocalizedStringDefinition>> localizedStringMap = new LinkedHashMap<>();
					logic.createMultiLangInfo(localizedStringMap, langDataInfo.getDefinition().getClass(), langDataInfo.getDefinition(), null);
					writer.writeRecord(localizedStringMap, langDataInfo.getContextPath() + langDataInfo.getName());
				}

			} catch (IOException e) {
				logger.error("failed to export eql result.", e);
	        	throw new DownloadRuntimeException(e);
			}

		}
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
