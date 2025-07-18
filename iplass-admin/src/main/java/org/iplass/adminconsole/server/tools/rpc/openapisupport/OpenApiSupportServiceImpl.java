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
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportService;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.DefinitionManager;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinitionManager;
import org.iplass.mtp.webapi.openapi.EntityWebApiType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;

/**
 *
 */
public class OpenApiSupportServiceImpl extends XsrfProtectedServiceServlet implements OpenApiSupportService {
	private Logger logger = LoggerFactory.getLogger(OpenApiSupportServiceImpl.class);

	@Override
	public OpenApiSupportTreeGridEntry getWebApiTreeGridEntry(int tenantId) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, () -> {
			var manager = ManagerLocator.manager(WebApiDefinitionManager.class);
			var root = new OpenApiSupportTreeGridEntry();
			root.setName("WebApi");
			root.setFolder(true);

			var webapiDefPathList = manager.definitionList();
			for (var defPath : webapiDefPathList) {
				addChild(root, defPath, defPath.split("/"), 0);
			}

			return root;
		});
	}

	// TODO ここの処理が改善できないか検討する。ちょっと遅い。
	@Override
	public OpenApiSupportTreeGridEntry getEntityCrudApiTreeGridEntry(int tenantId) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, () -> {
//			EntityWebApiDefinitionManager ewdm = ManagerLocator.getInstance().getManager(EntityWebApiDefinitionManager.class);
			DefinitionManager dm = ManagerLocator.getInstance().getManager(DefinitionManager.class);
			EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);

			OpenApiSupportTreeGridEntry root = new OpenApiSupportTreeGridEntry();
			root.setName("EntityCRUDApi");
			root.setFolder(true);

			// Entity定義の取得
			List<DefinitionSummary> entityNameList = edm.definitionNameList();

			for (DefinitionSummary entityName : entityNameList) {
				if (EntityDefinition.SYSTEM_DEFAULT_DEFINITION_NAME.equals(entityName.getName())) {
					continue;
				}

				// １つのEntity定義の不具合により取得できないことを避けるため、Catchする
				try {

					// Definition取得
					EntityDefinition definition = edm.get(entityName.getName());

					// EntityWebApiDefinition情報取得
					DefinitionEntry entry = dm.getDefinitionEntry(EntityWebApiDefinition.class, entityName.getName());

					if (null == entry) {
						// EntityWebApiDefinition の定義が存在しない場合は画面に表示しない
						continue;
					}

					EntityWebApiDefinition ewDefinition = (EntityWebApiDefinition) entry.getDefinition();

					if (!ewDefinition.isInsert() && !ewDefinition.isLoad() && !ewDefinition.isQuery() && !ewDefinition.isUpdate()
							&& !ewDefinition.isDelete()) {
						// EntityWebApiDefinition のCRUDが全てfalseの場合は画面に表示しない
						continue;
					}

					addChildEntityCrud(root, definition.getName(), definition.getName().split("\\."), 0, ewDefinition);


				} catch (Exception e) {
					logger.error("Entity情報の取得でエラーが発生しました。", e);
				}

			}

			return root;
		});
	}


	// "/path/to/name"
	// /
	//  + /path
	//    + /path/to
	//      + /path/to/name
	private void addChild(OpenApiSupportTreeGridEntry parent, String path, String[] splitPath, int layer) {
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

			addChild(folder, path, splitPath, layer + 1);
		}
	}

	private void addChildEntityCrud(OpenApiSupportTreeGridEntry parent, String path, String[] splitPath, int layer, EntityWebApiDefinition entityWebApiDef) {
		var name = splitPath[layer];
		// 末端はメソッドとして定義
		var isTerminate = (layer == splitPath.length - 1);

		if (isTerminate) {
			// 末端の場合は
			var entityCrudRoot = new OpenApiSupportTreeGridEntry();
			entityCrudRoot.setName(name);
			entityCrudRoot.setFolder(true);
			parent.addChild(entityCrudRoot);

			// TODO リテラル
			addEntityCrudAuthorized(entityWebApiDef.isInsert(), entityCrudRoot, path, EntityWebApiType.INSERT.name());
			addEntityCrudAuthorized(entityWebApiDef.isLoad(), entityCrudRoot, path, EntityWebApiType.LOAD.name());
			addEntityCrudAuthorized(entityWebApiDef.isQuery(), entityCrudRoot, path, EntityWebApiType.QUERY.name());
			addEntityCrudAuthorized(entityWebApiDef.isUpdate(), entityCrudRoot, path, EntityWebApiType.UPDATE.name());
			addEntityCrudAuthorized(entityWebApiDef.isDelete(), entityCrudRoot, path, EntityWebApiType.DELETE.name());

		} else {
			var folder = parent.getChildFolder(name);
			if (null == folder) {
				folder = new OpenApiSupportTreeGridEntry();
				folder.setName(name);
				folder.setFolder(true);
				parent.addChild(folder);
			}

			addChildEntityCrud(folder, path, splitPath, layer + 1, entityWebApiDef);
		}
	}

	private void addEntityCrudAuthorized(boolean isAuthorized, OpenApiSupportTreeGridEntry parent, String path, String name) {
		if (isAuthorized) {
			var authorized = new OpenApiSupportTreeGridEntry();
			authorized.setName(name);
			authorized.setPath(path);
			authorized.setFolder(false);
			parent.addChild(authorized);
		}
	}
}
