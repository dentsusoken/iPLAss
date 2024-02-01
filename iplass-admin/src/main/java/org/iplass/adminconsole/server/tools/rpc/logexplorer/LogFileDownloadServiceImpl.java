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

package org.iplass.adminconsole.server.tools.rpc.logexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.service.AdminConsoleService;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.ENCODE;
import org.iplass.adminconsole.shared.tools.dto.logexplorer.LogFileDownloadProperty;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * LogFileExport用Service実装クラス
 */
public class LogFileDownloadServiceImpl extends AdminDownloadService {

	private static final long serialVersionUID = 3148092411762538617L;

	private static final Logger logger = LoggerFactory.getLogger(LogFileDownloadServiceImpl.class);

	/** web.xmlで指定されたLOG_HOME（service-configで指定されていない場合に利用） */
	private String initLogHome;

	private AdminConsoleService acs = ServiceRegistry.getRegistry().getService(AdminConsoleService.class);
	private AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);

	@Override
	public void init() throws ServletException {
		super.init();

		//configからLOGHOMEを取得
		ServletConfig servletConfig = getServletConfig();
		String loghome = servletConfig.getInitParameter("loghome");
		if (loghome != null) {
			this.initLogHome = loghome;
		}
	}

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		final String filePath = req.getParameter(LogFileDownloadProperty.TARGET_PATH);

		//利用可否チェック
		if (!acs.isLogDownloadEnabled()) {
			logger.debug("LogService is disabled.");
			throw new DownloadRuntimeException(rs("tools.logexplorer.LogFileDownloadServiceImpl.notAvailableLogServ"));
		}

		//パスの検証
		File logFile = validatePath(filePath);

		String fileName = tenantId + "-" + logFile.getName();

		//FileからContentTypeを取得
		String contentType = null;
		DataHandler hData = new DataHandler(new FileDataSource(logFile));
		if (hData != null) {
			//FIXME .logだと「application/octed-stream」になってしまう
			contentType = hData.getContentType();
		}
		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM;
		}

		//テキストファイルの場合は、エンコードを指定
		String encode = null;
		if (contentType.equals(MediaType.TEXT_PLAIN)) {
			encode = ENCODE.UTF8.getValue();
		}

		aals.logDownload("LogFileDownload", fileName, "path:" + filePath);

		//出力
		try (InputStream is = new FileInputStream(logFile)){

			//ファイル名設定
			DownloadUtil.setResponseHeader(resp, contentType, fileName, encode);

			//出力
			logger.debug("start download log file . path=" + filePath);

			IOUtils.copy(is, resp.getOutputStream());

		} catch (IOException e) {
            throw new DownloadRuntimeException(e);
		}
	}

	private File validatePath(String filePath) {

		// fix Path Manipulation
		if (filePath.contains("..")) {
			throw new DownloadRuntimeException("invalid path:" + filePath);
		}

		//LogHomeの取得
		List<String> logHomes = acs.getTenantLogHomes(initLogHome);

		//PrefixPathチェック
		final Path pathFile = Paths.get(filePath);
		Optional<String> validHome = logHomes.stream().filter(logHome -> {
			//*を含む場合は、そこまでの固定部分の抽出
			Path pathHome = null;
			if (logHome.contains("/*/")) {
				String fixPath = logHome.substring(0, logHome.indexOf("/*/") + 1);
				pathHome = Paths.get(fixPath);
			} else {
				pathHome = Paths.get(logHome);
			}
			return pathFile.startsWith(pathHome);
		}).findFirst();
		if (!validHome.isPresent()) {
			throw new DownloadRuntimeException("invalid path:" + filePath);
		}

		//存在チェック
		final File logFile = new File(filePath);
		if (!logFile.exists()) {
			throw new DownloadRuntimeException(rs("tools.logexplorer.LogFileDownloadServiceImpl.canNotGetSelectLogFile", filePath));
		}

		//LogFilterの取得
		List<String> fileFilters = acs.getTenantLogFileFilters();
		if (fileFilters != null) {
			// Filterチェック
			Optional<String> validFilter = fileFilters.stream().filter(filter -> {
				return logFile.getName().matches(filter);
			}).findFirst();
			if (!validFilter.isPresent()) {
				throw new DownloadRuntimeException("invalid path:" + filePath);
			}
		}

		return logFile;
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
