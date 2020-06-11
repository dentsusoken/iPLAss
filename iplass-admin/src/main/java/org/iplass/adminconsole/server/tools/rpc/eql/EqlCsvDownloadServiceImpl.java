/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.eql;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.impl.entity.csv.QueryCsvWriter;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * EQL結果Export用Service実装クラス
 */
public class EqlCsvDownloadServiceImpl extends AdminDownloadService {

	/** シリアルバージョンNo */
	private static final long serialVersionUID = 5418054754659157087L;

	private static final Logger logger = LoggerFactory.getLogger(EqlCsvDownloadServiceImpl.class);

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		//パラメータの取得
		final String eql = req.getParameter("eql");
		final boolean isSearchAllVersion = Boolean.valueOf(req.getParameter("isSearchAllVersion"));

		final String filename = tenantId + "-eql-result-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".csv";

		Query checkQuery = null;
		try {
			//EQLの検証
			checkQuery = new PreparedQuery(eql).query(null);
		} catch (QueryException e) {
			//EQLエラー、CSVにエラーを出力
			logger.error("failed to export eql result.", e);

			try (QueryCsvWriter writer = new QueryCsvWriter(resp.getOutputStream(), null)){

				DownloadUtil.setCsvResponseHeader(resp, filename);

				writer.writeError(e.getMessage());

			} catch (IOException e2) {
				logger.error("failed to export eql result.", e2);
			}

			//エラー時はここで終了
			return;
		}

		final Query query = checkQuery;

		//実行Queryの設定
		query.setVersiond(isSearchAllVersion);

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("EqlWorkSheetCsvDownload", filename, query);

		try (QueryCsvWriter writer = new QueryCsvWriter(resp.getOutputStream(), query)){

			DownloadUtil.setCsvResponseHeader(resp, filename);

			try {
				int count = writer.write();

				logger.debug(count + " records eql result exported.");

			} catch (QueryException e) {
				//実行時のEQLエラー(プロパティがない場合など)
				logger.error("failed to export eql result.", e);
				writer.writeError(e.getMessage());
			}

		} catch (IOException e) {
			logger.error("failed to export eql result.", e);
        	throw new DownloadRuntimeException(e);
		}
	}

}
