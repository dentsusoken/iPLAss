/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc.message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.message.MessageItem;
import org.iplass.mtp.message.MessageManager;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageItemCsvDownloadServiceImpl extends AdminDownloadService {

	private static final long serialVersionUID = 6972374237721771295L;

	private static final Logger logger = LoggerFactory.getLogger(MessageItemCsvDownloadServiceImpl.class);

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		//パラメータの取得
		final String definitionName = req.getParameter("definitionName");

		String fileName = tenantId + "-message-data-" + definitionName + "_" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".csv";

		//CSV出力
		writeCsv(resp, definitionName, fileName);
	}

	private void writeCsv(HttpServletResponse resp, String definitionName, String fileName) {

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("MessageItemCsvDownload", fileName, "name:" + fileName);

		MessageManager mm = ManagerLocator.getInstance().getManager(MessageManager.class);
		MessageCategory category = mm.get(definitionName);


		//Writerの生成
		try (MessageItemCsvWriter writer = new MessageItemCsvWriter(resp.getOutputStream())){

			DownloadUtil.setCsvResponseHeader(resp, fileName);

			//ヘッダ出力
			writer.writeHeader();

			if (category != null) {
				if (category.getMessageItems() != null) {
					for (MessageItem item : category.getMessageItems().values()) {
						writer.writeMessageItem(item);
					}
				}
			} else {
				logger.error("failed to export message item. not found definition. name=" + definitionName);
			}

		} catch (IOException e) {
			logger.error("failed to export message item.", e);
			throw new DownloadRuntimeException(e);
		}
	}

}
