/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc.staticresource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceDownloadProperty;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.staticresource.definition.LocalizedStaticResourceDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinitionManager;

public class StaticResourceDownloadServiceImpl extends HttpServlet {

	private static final long serialVersionUID = -1151175483761938624L;

	public StaticResourceDownloadServiceImpl() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		download(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		download(req, resp);
	}

	private void download(final HttpServletRequest req, final HttpServletResponse resp) {
		//パラメータの取得
		final int tenantId = Integer.parseInt(req.getParameter(StaticResourceDownloadProperty.TENANT_ID));

		AuthUtil.authCheckAndInvoke(getServletContext(), req, resp, tenantId, new AuthUtil.Callable<Void>() {
			@Override
			public Void call() {

				String defName = req.getParameter(StaticResourceDownloadProperty.DEFINITION_NAME);
				String lang = req.getParameter(StaticResourceDownloadProperty.LANG);
				String strContentDispositionType = req.getParameter(StaticResourceDownloadProperty.CONTENT_DISPOSITION_TYPE);

				ContentDispositionType contentDispositionType = ContentDispositionType.INLINE;
				if (StringUtil.isNotEmpty(strContentDispositionType)) {
					contentDispositionType = ContentDispositionType.valueOf(strContentDispositionType);
				}

				binaryDownload(req, resp, defName, lang, contentDispositionType);
				return null;
			}
		});
	}

	private void binaryDownload(final HttpServletRequest req, final HttpServletResponse resp, final String defName, final String lang, final ContentDispositionType contentDispositionType) {
		StaticResourceDefinitionManager dm = ManagerLocator.getInstance().getManager(StaticResourceDefinitionManager.class);
		StaticResourceDefinition definition = dm.get(defName);

		try {
			if (definition != null && definition instanceof StaticResourceDefinition) {
				if (definition.getContentType() != null) {
					resp.setContentType(definition.getContentType());
				}
				if (lang == null) {
					String fileName = definition.getResource().getName();
					if (StringUtil.isEmpty(fileName)) {
						//ファイル名が未指定の場合は、定義名(階層化されている場合は最後)
						if (definition.getName().contains("/")) {
							fileName = definition.getName().substring(definition.getName().lastIndexOf("/") + 1);
						} else {
							fileName = definition.getName();
						}
					}
					//2.1.34までパスが設定されていたので除去
					fileName = FilenameUtils.getName(fileName);
					WebUtil.setContentDispositionHeader(req, resp, contentDispositionType, fileName);

					try (BufferedInputStream bis = new BufferedInputStream(definition.getResource().getInputStream())) {
						int n = bis.read();
						while (n != -1) {
							resp.getOutputStream().write(n);
							n = bis.read();
						}
					}
				} else {
					List<LocalizedStaticResourceDefinition> localeDefList = ((StaticResourceDefinition) definition).getLocalizedResourceList();
					for (LocalizedStaticResourceDefinition localeDef : localeDefList) {
						if (localeDef.getLocaleName().equals(lang)) {
							String fileName = localeDef.getResource().getName();
							if (StringUtil.isEmpty(fileName)) {
								//ファイル名が未指定の場合は、定義名(階層化されている場合は最後)
								if (definition.getName().contains("/")) {
									fileName = definition.getName().substring(definition.getName().lastIndexOf("/") + 1);
								} else {
									fileName = definition.getName();
								}
							}
							//2.1.34までパスが設定されていたので除去
							fileName = FilenameUtils.getName(fileName);
							WebUtil.setContentDispositionHeader(req, resp, contentDispositionType, fileName);

							try (BufferedInputStream bis = new BufferedInputStream(localeDef.getResource().getInputStream())) {
								int n = bis.read();
								while (n != -1) {
									resp.getOutputStream().write(n);
									n = bis.read();
								}
							}
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
