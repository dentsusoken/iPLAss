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

package org.iplass.adminconsole.server.metadata.rpc.template;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.shared.metadata.dto.template.ReportTemplateDownloadProperty;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinitionManager;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;


/**
 * ReportTemplateDownload用Service実装クラス
 */
public class ReportTemplateDownloadServiceImpl extends AdminDownloadService {

	private static final long serialVersionUID = 1769813278942863059L;

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		//パラメータの取得
		final String templateName = req.getParameter(ReportTemplateDownloadProperty.DEFINITION_NAME);
		final String lang = req.getParameter(ReportTemplateDownloadProperty.LANG);

		TemplateDefinitionManager tdm = ManagerLocator.getInstance().getManager(TemplateDefinitionManager.class);
		TemplateDefinition template = tdm.get(templateName);

		try {
			if (template != null ){
				if(template instanceof ReportTemplateDefinition){
					ReportTemplateDefinition rtd = (ReportTemplateDefinition)template;
					if (rtd.getContentType() != null) {
						resp.setContentType(rtd.getContentType());
					}

			        if (lang == null) {
				        String downloadName = new String(rtd.getFileName().getBytes("Shift_JIS"), "ISO-8859-1");
				        resp.setHeader("Content-Disposition", "attachment; filename=" + downloadName);
				        resp.getOutputStream().write(rtd.getBinary());
					} else {
						List<LocalizedReportDefinition> binList = rtd.getLocalizedReportList();

						for (LocalizedReportDefinition def : binList) {
							if (def.getLocaleName().equals(lang)) {
						        String downloadName = new String(def.getFileName().getBytes("Shift_JIS"), "ISO-8859-1");
						        resp.setHeader("Content-Disposition", "attachment; filename=" + downloadName);
								resp.getOutputStream().write(def.getBinary());
								break;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			throw new DownloadRuntimeException(e);
		}
	}

}
