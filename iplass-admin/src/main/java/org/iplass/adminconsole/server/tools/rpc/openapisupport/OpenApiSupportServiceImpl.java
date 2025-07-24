/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.server.tools.rpc.openapisupport;

import java.util.List;

import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.tools.dto.openapisupport.OpenApiSupportTreeGridEntry;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportService;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinitionManager;
import org.iplass.mtp.webapi.definition.WebApiDefinitionManager;
import org.iplass.mtp.webapi.openapi.EntityWebApiType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;

/**
 * OpenAPIサポートのサービス実装クラス。
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportServiceImpl extends XsrfProtectedServiceServlet implements OpenApiSupportService {
	/** serialVersionUID */
	private static final long serialVersionUID = -4771479837379214005L;
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(OpenApiSupportServiceImpl.class);

	@Override
	public OpenApiSupportTreeGridEntry getWebApiTreeGridEntry(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, () -> {
			var manager = ManagerLocator.manager(WebApiDefinitionManager.class);
			var root = new OpenApiSupportTreeGridEntry();
			root.setName(OpenApiSupportRpcConstant.Service.RootNode.WEB_API);
			root.setFolder(true);

			var webapiDefPathList = manager.definitionList();
			for (var defPath : webapiDefPathList) {
				addTreeForWebApi(root, defPath, defPath.split("/"), 0);
			}

			return root;
		});
	}

	@Override
	public OpenApiSupportTreeGridEntry getEntityCrudApiTreeGridEntry(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, () -> {
			var ewdm = ManagerLocator.getInstance().getManager(EntityWebApiDefinitionManager.class);
			var edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);

			OpenApiSupportTreeGridEntry root = new OpenApiSupportTreeGridEntry();
			root.setName(OpenApiSupportRpcConstant.Service.RootNode.ENTITY_CRUD_API);
			root.setFolder(true);

			// Entity定義の取得
			List<DefinitionSummary> entityNameList = edm.definitionNameList();

			for (DefinitionSummary entitySummary : entityNameList) {
				var name = entitySummary.getName();

				if (EntityDefinition.SYSTEM_DEFAULT_DEFINITION_NAME.equals(name)) {
					continue;
				}

				// １つのEntity定義の不具合により取得できないことを避けるため、Catchする
				try {
					// EntityWebApiDefinition情報取得
					var ewDef = ewdm.get(name);

					if (null == ewDef) {
						// EntityWebApiDefinition の定義が存在しない場合は画面に表示しない
						continue;
					}

					if (!(ewDef.isInsert() || ewDef.isLoad() || ewDef.isQuery() || ewDef.isUpdate() || ewDef.isDelete())) {
						// EntityWebApiDefinition のCRUDが全てfalseの場合は画面に表示しない
						continue;
					}

					addTreeForEntityCrud(root, name, name.split("\\."), 0, ewDef);

				} catch (RuntimeException e) {
					logger.error("An exception was thrown when retrieving entity information. Entity: " + entitySummary.getName(), e);
				}

			}

			return root;
		});
	}


	/**
	 * WebAPIのパスを分割してツリー構造を追加する。
	 * <p>
	 * 分割されたWebAPIのパスで階層構造を作成します。
	 * 例えば、パスが"/path/to/webApiName"の場合、以下のような構造を追加します。
	 * </p>
	 * <pre>
	 * / (root folder)
	 *   + /path (folder)
	 *       + /to (folder)
	 *           + /webApiName (method)
	 * </pre>
	 *
	 * @param parent 親エントリ
	 * @param path WebAPIのパス
	 * @param splitPath 分割されたWebAPIのパス
	 * @param layer 現在の階層
	 */
	private void addTreeForWebApi(OpenApiSupportTreeGridEntry parent, String path, String[] splitPath, int layer) {
		var name = splitPath[layer];
		// 末端はメソッドとして定義
		var isMethod = (layer == splitPath.length - 1);

		if (isMethod) {
			// メソッドの場合は定義を作成して終了する
			var method = new OpenApiSupportTreeGridEntry();
			method.setName(name);
			method.setPath(path);
			method.setFolder(false);

			parent.addChild(method);

		} else {
			// 親フォルダ定義
			var folder = parent.getChildFolder(name);
			if (null == folder) {
				folder = new OpenApiSupportTreeGridEntry();
				folder.setName(name);
				folder.setFolder(true);
				parent.addChild(folder);
			}

			addTreeForWebApi(folder, path, splitPath, layer + 1);
		}
	}

	/**
	 * Entity CRUD APIのパスを分割してツリー構造を追加する。
	 * <p>
	 * 分割されたEntityのパスで階層構造を作成します。
	 * 例えば、パスが"path.to.entityName"で LOAD, INSERT, UPDATE 権限を保持する場合、以下のような構造を追加します。
	 * </p>
	 * <pre>
	 * / (root folder)
	 *   + /path (folder)
	 *       + /to (folder)
	 *           + /entityName (folder)
	 *               + /LOAD (webApiType)
	 *               + /INSERT (webApiType)
	 *               + /UPDATE (webApiType)
	 * </pre>
	 * @param parent 親エントリ
	 * @param path Entity 定義パス
	 * @param splitPath 分割されたEntityのパス
	 * @param layer 現在の階層
	 * @param entityWebApiDef EntityWebApiDefinition
	 */
	private void addTreeForEntityCrud(OpenApiSupportTreeGridEntry parent, String path, String[] splitPath, int layer, EntityWebApiDefinition entityWebApiDef) {
		var name = splitPath[layer];
		// エンティティ定義名の末端か判定
		var isTerminal = (layer == splitPath.length - 1);

		if (isTerminal) {
			// 末端の場合
			var entityCrudRoot = new OpenApiSupportTreeGridEntry();
			entityCrudRoot.setName(name);
			entityCrudRoot.setFolder(true);
			parent.addChild(entityCrudRoot);

			// WebAPIタイプを追加
			addEntityCrud(entityWebApiDef.isInsert(), entityCrudRoot, path, EntityWebApiType.INSERT);
			addEntityCrud(entityWebApiDef.isLoad(), entityCrudRoot, path, EntityWebApiType.LOAD);
			addEntityCrud(entityWebApiDef.isQuery(), entityCrudRoot, path, EntityWebApiType.QUERY);
			addEntityCrud(entityWebApiDef.isUpdate(), entityCrudRoot, path, EntityWebApiType.UPDATE);
			addEntityCrud(entityWebApiDef.isDelete(), entityCrudRoot, path, EntityWebApiType.DELETE);

		} else {
			// 末端ではない場合
			var folder = parent.getChildFolder(name);
			if (null == folder) {
				folder = new OpenApiSupportTreeGridEntry();
				folder.setName(name);
				folder.setFolder(true);
				parent.addChild(folder);
			}

			addTreeForEntityCrud(folder, path, splitPath, layer + 1, entityWebApiDef);
		}
	}

	/**
	 * Entity CRUD APIの情報を追加する。
	 * @param isAuthorized Entity CRUD APIの権限が許可されているか
	 * @param parent 親エントリ
	 * @param path Entity CRUD APIのエンティティ定義パス
	 * @param webApiTyype EntityWebApiType
	 */
	private void addEntityCrud(boolean isAuthorized, OpenApiSupportTreeGridEntry parent, String path, EntityWebApiType webApiTyype) {
		if (isAuthorized) {
			var authorized = new OpenApiSupportTreeGridEntry();
			authorized.setName(webApiTyype.name());
			authorized.setPath(path);
			authorized.setFolder(false);
			parent.addChild(authorized);
		}
	}
}
