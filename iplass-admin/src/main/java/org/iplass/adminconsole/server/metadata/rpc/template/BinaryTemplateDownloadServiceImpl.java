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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.metadata.dto.template.BinaryTemplateDownloadProperty;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.template.definition.BinaryTemplateDefinition;
import org.iplass.mtp.web.template.definition.LocalizedBinaryDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinitionManager;


/**
 * BinaryTemplateDownload用Service実装クラス
 */
public class BinaryTemplateDownloadServiceImpl extends HttpServlet {

	private static final long serialVersionUID = 3472433353415494865L;

	public BinaryTemplateDownloadServiceImpl() {
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
		final int tenantId = Integer.parseInt(req.getParameter(BinaryTemplateDownloadProperty.TENANT_ID));

		AuthUtil.authCheckAndInvoke(getServletContext(), req, resp, tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {

				String templateName = req.getParameter(BinaryTemplateDownloadProperty.DEFINITION_NAME);
				String lang = req.getParameter(BinaryTemplateDownloadProperty.LANG);
				String strContentDispositionType = req.getParameter(BinaryTemplateDownloadProperty.CONTENT_DISPOSITION_TYPE);

				ContentDispositionType contentDispositionType = ContentDispositionType.INLINE;
				if (StringUtil.isNotEmpty(strContentDispositionType)) {
					contentDispositionType = ContentDispositionType.valueOf(strContentDispositionType);
				}

				binaryDownload(req, resp, templateName, lang, contentDispositionType);

				return null;
			}

		});
	}

	private void binaryDownload(final HttpServletRequest req, final HttpServletResponse resp, final String templateName, final String lang, final ContentDispositionType contentDispositionType) {

		TemplateDefinitionManager tdm = ManagerLocator.getInstance().getManager(TemplateDefinitionManager.class);
		TemplateDefinition template = tdm.get(templateName);

		try {
			if (template != null && template instanceof BinaryTemplateDefinition) {
				BinaryTemplateDefinition btd = (BinaryTemplateDefinition)template;

				if (template.getContentType() != null) {
					resp.setContentType(template.getContentType());
				}
				if (lang == null) {
					String fileName = btd.getFileName();
					if (StringUtil.isEmpty(fileName)) {
						//名前が未指定の場合は、テンプレート名(階層化されている場合は最後)
						if (template.getName().contains("/")) {
							fileName = template.getName().substring(template.getName().lastIndexOf("/") + 1);
						} else {
							fileName = template.getName();
						}
					}
					WebUtil.setContentDispositionHeader(req, resp, contentDispositionType, fileName);

					resp.getOutputStream().write(((BinaryTemplateDefinition)template).getBinary());
				} else {
					List<LocalizedBinaryDefinition> binList = btd.getLocalizedBinaryList();

					for (LocalizedBinaryDefinition locale : binList) {
						if (locale.getLocaleName().equals(lang)) {
							String fileName = locale.getFileName();
							if (StringUtil.isEmpty(fileName)) {
								//名前が未指定の場合は、テンプレート名(階層化されている場合は最後)
								if (template.getName().contains("/")) {
									fileName = template.getName().substring(template.getName().lastIndexOf("/") + 1);
								} else {
									fileName = template.getName();
								}
							}
							WebUtil.setContentDispositionHeader(req, resp, contentDispositionType, fileName);

							resp.getOutputStream().write(locale.getBinaryValue());
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			throw new DownloadRuntimeException(e);
		}

	}

}
