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

package org.iplass.adminconsole.server.metadata.rpc.entity;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.metadata.dto.entity.EntityJavaMappingClassDownloadProperty;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.tools.entity.EntityJavaMappingClassWriter;
import org.iplass.mtp.util.StringUtil;


/**
 * EntityのJavaMappingクラスDownload用Service実装クラス
 */
public class EntityJavaMappingClassDownloadServiceImpl extends HttpServlet {

	private static final long serialVersionUID = -7736565251710725231L;

	public EntityJavaMappingClassDownloadServiceImpl() {
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
		final int tenantId = Integer.parseInt(req.getParameter(EntityJavaMappingClassDownloadProperty.TENANT_ID));
		final String entityName = req.getParameter(EntityJavaMappingClassDownloadProperty.ENTITY_NAME);
		final String className = req.getParameter(EntityJavaMappingClassDownloadProperty.CLASS_NAME);

		AuthUtil.authCheckAndInvoke(getServletContext(), req, resp, tenantId, new AuthUtil.Callable<Void>() {

			@Override
			public Void call() {

				download(entityName, className, req, resp);

				return null;
			}

		});
	}

	private void download(final String entityName, final String className, final HttpServletRequest req, final HttpServletResponse resp) {

		try {
	        // ファイル名を設定
			DownloadUtil.setResponseHeader(resp, MediaType.TEXT_PLAIN, getEntityMappingClassName(entityName, className) + ".java");

			writeEntityMappingClass(resp.getOutputStream(), entityName, className);

		} catch (IOException e) {
			throw new DownloadRuntimeException(e);
		}

	}

	/**
	 * Entity定義に対応するJavaMappingクラス名を返します。（ResponseのHeader指定用）
	 *
	 * @param entityName		出力対象Entity定義名
	 * @param directClassName	出力クラス名(直接指定)
	 * @throws IOException
	 */
	private String getEntityMappingClassName(final String entityName, final String directClassName) {

		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityDefinition definition = edm.get(entityName);
		if (definition == null) {
			return "";
		}

		String classFullName = null;
		if (StringUtil.isNotBlank(directClassName)) {
			//直接Classが指定されている場合はその値
			classFullName = directClassName;
		} else if (definition.getMapping() != null && StringUtil.isNotBlank(definition.getMapping().getMappingModelClass())) {
			//Mappingクラスが指定されている場合はその値
			classFullName = definition.getMapping().getMappingModelClass();
		} else {
			//Mappingクラスが指定されていない場合はname
			classFullName = definition.getName();
		}
		String className = null;
		if (classFullName.lastIndexOf(".") >= 0) {
			className = classFullName.substring(classFullName.lastIndexOf(".") + 1);
		} else {
			className = classFullName;
		}
		return StringUtil.capitalize(className);
	}

	/**
	 * Entity定義に対応するJavaMappingクラスを生成し、Exportします。
	 *
	 * @param os				出力先ファイル(Stream)
	 * @param entityName		出力対象Entity定義名
	 * @param directClassName	出力クラス名(直接指定)
	 * @throws IOException
	 */
	private void writeEntityMappingClass(final OutputStream os, final String entityName, final String directClassName) throws IOException {

		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityDefinition definition = edm.get(entityName);
		if (definition == null) {
			return;
		}

		try (EntityJavaMappingClassWriter writer = new EntityJavaMappingClassWriter(os, definition, directClassName)){
			writer.writeJavaClass();
		}

	}
}
