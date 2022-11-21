/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.adminconsole.server.metadata.rpc;

import java.util.List;

import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAction;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAuditLogger;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.definition.DefinitionManager;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterManager;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuItemManager;
import org.iplass.mtp.view.menu.MenuTree;
import org.iplass.mtp.view.menu.MenuTreeManager;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GEMのEntity定義操作用Controller
 *
 * @author Y.Yasuda
 */
public class GemEntityDefinitionOperationController implements EntityDefinitionOperationController {

	private static final Logger logger = LoggerFactory.getLogger(GemEntityDefinitionOperationController.class);

	private DefinitionManager dm = ManagerLocator.getInstance().getManager(DefinitionManager.class);
	private EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	private EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	private EntityFilterManager efm = ManagerLocator.getInstance().getManager(EntityFilterManager.class);
	private EntityWebApiDefinitionManager ewm = ManagerLocator.getInstance().getManager(EntityWebApiDefinitionManager.class);
	private MenuItemManager mm = ManagerLocator.getInstance().getManager(MenuItemManager.class);
	private MenuTreeManager mtm = ManagerLocator.getInstance().getManager(MenuTreeManager.class);
	private MetaDataAuditLogger auditLogger = MetaDataAuditLogger.getLogger();

	@Override
	public AdminDefinitionModifyResult createMenuItem(EntityDefinition definition) {
		//EntityメニューItemを生成し、DEFAULTメニューに追加する

		//自動生成するEntityMenuItemの名前は「/」に変換
		String path = convertPath(definition.getName());

		//存在チェック(Entityが無効な場合、メタデータ上には存在してもMenuItemとしてはnullが返ってくるためDefinitionInfoでチェック)
		//MenuItem oldItem = mm.getMenuItem(path);
		DefinitionInfo oldItem = dm.getInfo(MenuItem.class, path);
		if (oldItem == null) {
			EntityMenuItem entityItem = new EntityMenuItem();
			entityItem.setName(path);
			if (definition.getDisplayName() != null && !definition.getDisplayName().isEmpty()) {
				entityItem.setDisplayName(definition.getDisplayName());
			} else {
				entityItem.setDisplayName(getEntitySimpleName(definition.getName()));
			}
			entityItem.setEntityDefinitionName(definition.getName());

			auditLogger.logMetadata(MetaDataAction.CREATE, MenuItem.class.getName(), "name:" + entityItem.getName());
			DefinitionModifyResult ret2 = mm.create(entityItem);
			if (!ret2.isSuccess()) {
				return new AdminDefinitionModifyResult(ret2.isSuccess(), ret2.getMessage());
			}

			MenuTree tree = mtm.get(DEFAULT);
			if(tree != null) {
				tree.addMenuItem(entityItem);
				auditLogger.logMetadata(MetaDataAction.UPDATE, MenuTree.class.getName(), "name:" + tree.getName());
				DefinitionModifyResult ret3 = mtm.update(tree);
				if (!ret3.isSuccess()) {
					return new AdminDefinitionModifyResult(ret3.isSuccess(), ret3.getMessage());
				}
			}
		} else {
			logger.info("{} 's entity menu item is not create. already exists for other menu item.", definition.getName());
		}
		return null;
	}

	@Override
	public AdminDefinitionModifyResult updateMenuItem(EntityDefinition definition) {
		MenuItem menuItem = mm.get(convertPath(definition.getName()));
		if(menuItem != null) {
			// 更新対象化チェックする
			String itemDispName = menuItem.getDisplayName();
			String entityDispName = definition.getDisplayName();
			if(itemDispName != null && !itemDispName.equals(entityDispName)) {
				// データは異なる。
				EntityDefinition oldEd = edm.get(definition.getName());
				if(itemDispName.equals(oldEd.getDisplayName())) {
					// メニューと同一(デフォルト)なので修正する
					menuItem.setDisplayName(definition.getDisplayName());

					auditLogger.logMetadata(MetaDataAction.UPDATE, MenuItem.class.getName(), "name:" + menuItem.getName());
					DefinitionModifyResult ret = mm.update(menuItem);
					if (!ret.isSuccess()) {
						return new AdminDefinitionModifyResult(ret.isSuccess(), ret.getMessage());
					}
				}
			}
		}
		return null;
	}

	@Override
	public AdminDefinitionModifyResult copyViewDefinition(String sourceName, String newName, String displayName, String description,
			boolean isCopyEntityView, boolean isCopyEntityFilter, boolean isCopyEntityWebAPI) {
		if (isCopyEntityView) {
			EntityView ev = evm.get(sourceName);
			if (ev != null) {
				ev.setName(newName);
				ev.setDisplayName(displayName);
				ev.setDescription(description);
				ev.setDefinitionName(newName);

				auditLogger.logMetadata(MetaDataAction.CREATE, EntityView.class.getName(), "name:" + ev.getName());
				DefinitionModifyResult retEv = evm.create(ev);
				if (!retEv.isSuccess()) {
					return new AdminDefinitionModifyResult(retEv.isSuccess(), retEv.getMessage());
				}
			}
		}
		if (isCopyEntityFilter) {
			EntityFilter ef = efm.get(sourceName);
			if (ef != null) {
				ef.setName(newName);
				ef.setDisplayName(displayName);
				ef.setDescription(description);
				ef.setDefinitionName(newName);

				auditLogger.logMetadata(MetaDataAction.CREATE, EntityFilter.class.getName(), "name:" + ef.getName());
				DefinitionModifyResult retEf = efm.create(ef);
				if (!retEf.isSuccess()) {
					return new AdminDefinitionModifyResult(retEf.isSuccess(), retEf.getMessage());
				}
			}
		}
		if (isCopyEntityWebAPI) {
			EntityWebApiDefinition ew = ewm.get(sourceName);
			if (ew != null) {
				//ew.setName(newName);
				ew.setName(newName);
				ew.setDisplayName(displayName);
				//ew.setDescription(description);

				auditLogger.logMetadata(MetaDataAction.CREATE, EntityWebApiDefinition.class.getName(), "name:" + ew.getName());
				DefinitionModifyResult retEw = ewm.create(ew);
				if (!retEw.isSuccess()) {
					return new AdminDefinitionModifyResult(retEw.isSuccess(), retEw.getMessage());
				}
			}
		}
		return null;
	}

	@Override
	public AdminDefinitionModifyResult deleteViewDefinition(String name) {

		//EntityViewの削除
		DefinitionInfo view = dm.getInfo(EntityView.class, name);
		if(view != null && !view.isShared()) {

			auditLogger.logMetadata(MetaDataAction.DELETE, EntityView.class.getName(), "name:" + name);
			DefinitionModifyResult retEv = evm.remove(name);
			if (!retEv.isSuccess()) {
				return new AdminDefinitionModifyResult(retEv.isSuccess(), retEv.getMessage());
			}
		}
		//EntityFilterの削除
		DefinitionInfo filter = dm.getInfo(EntityFilter.class, name);
		if (filter != null && !filter.isShared()) {
			auditLogger.logMetadata(MetaDataAction.DELETE, EntityFilter.class.getName(), "name:" + name);
			DefinitionModifyResult retEf = efm.remove(name);
			if (!retEf.isSuccess()) {
				return new AdminDefinitionModifyResult(retEf.isSuccess(), retEf.getMessage());
			}
		}
		//EntityWebAPIの削除
		DefinitionInfo webapi = dm.getInfo(EntityWebApiDefinition.class, name);
		if (webapi != null && !webapi.isShared()) {
			auditLogger.logMetadata(MetaDataAction.DELETE, EntityWebApiDefinition.class.getName(), "name:" + name);
			DefinitionModifyResult retEw = ewm.remove(name);
			if (!retEw.isSuccess()) {
				return new AdminDefinitionModifyResult(retEw.isSuccess(), retEw.getMessage());
			}
		}

		// メニューTreeの更新
		for (String menuTreeName : mtm.definitionList()) {
			MenuTree menuTree = mtm.get(menuTreeName);
			if(menuTree != null && menuTree.getMenuItems() != null) {
				List<MenuItem> items = menuTree.getMenuItems();
				boolean update = false;
				for (int i = (items.size() -1 ) ; i >= 0; i--) {
					MenuItem menuItem = items.get(i);
					if(menuItem instanceof EntityMenuItem) {
						if(name.equals(((EntityMenuItem) menuItem).getEntityDefinitionName())) {
							items.remove(i);
							update = true;
						}
					}
				}
				if(update) {
					auditLogger.logMetadata(MetaDataAction.UPDATE, MenuTree.class.getName(), "name:" + menuTree.getName());
					DefinitionModifyResult ret2 = mtm.update(menuTree);
					if (!ret2.isSuccess()) {
						return new AdminDefinitionModifyResult(ret2.isSuccess(), ret2.getMessage());
					}
				}
			}

		}

		//メニュItemの削除(万が一メニューツリーに一つも紐づいていない場合もあり得るので全Itemをチェック)
		for (String menuItemName : mm.definitionList()) {
			DefinitionEntry menuItem = dm.getDefinitionEntry(MenuItem.class, menuItemName);
			if (menuItem != null && !menuItem.getDefinitionInfo().isShared()) {
				if(menuItem.getDefinition() instanceof EntityMenuItem) {
					if(name.equals(((EntityMenuItem)menuItem.getDefinition()).getEntityDefinitionName())) {
						auditLogger.logMetadata(MetaDataAction.DELETE, MenuItem.class.getName(), "name:" + menuItemName);
						DefinitionModifyResult ret3 = mm.remove(menuItemName);
						if (!ret3.isSuccess()) {
							return new AdminDefinitionModifyResult(ret3.isSuccess(), ret3.getMessage());
						}
					}
				}
			}
		}

		return null;
	}

	@Override
	public void renameViewDefinition(String fromName, String toName) {
		//Entityの場合はEntityView、EntityFilter、EntityWebAPIも更新
		if (dm.getInfo(EntityView.class, fromName) != null) {
			auditLogger.logMetadata(MetaDataAction.UPDATE, EntityView.class.getName(), "fromName:" + fromName + " toName:" + toName);
			dm.rename(EntityView.class, fromName, toName);
		}
		if (dm.getInfo(EntityFilter.class, fromName) != null) {
			auditLogger.logMetadata(MetaDataAction.UPDATE, EntityFilter.class.getName(), "fromName:" + fromName + " toName:" + toName);
			dm.rename(EntityFilter.class, fromName, toName);
		}
		if (dm.getInfo(EntityWebApiDefinition.class, fromName) != null) {
			auditLogger.logMetadata(MetaDataAction.UPDATE, EntityWebApiDefinition.class.getName(), "fromName:" + fromName + " toName:" + toName);
			dm.rename(EntityWebApiDefinition.class, fromName, toName);
		}
		String _fromName = convertPath(fromName);
		String _toName = convertPath(toName);
		if (dm.getInfo(MenuItem.class, _fromName) != null) {
			auditLogger.logMetadata(MetaDataAction.UPDATE, MenuItem.class.getName(), "fromName:" + _fromName + " toName:" + _toName);
			dm.rename(MenuItem.class, _fromName, _toName);
		}
	}

	/**
	 * <p>Pathを有効な値に変換します。</p>
	 *
	 * Entityのnameは「.」区切りなので、nameをそのまま渡された場合などを考慮して変換
	 *
	 * @param path パス
	 * @return 有効なパス
	 */
	private String convertPath(String path) {
		return path.replace(".","/");
	}

	private String getEntitySimpleName(String path) {
		if (path.contains(".")) {
			if (path.lastIndexOf(".") < path.length()) {
				return path.substring(path.lastIndexOf(".") + 1);
			} else {
				return path.substring(path.lastIndexOf("."));
			}
		} else {
			return path;
		}
	}

}
