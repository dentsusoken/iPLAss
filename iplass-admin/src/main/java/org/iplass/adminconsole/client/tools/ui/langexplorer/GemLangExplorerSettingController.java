/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.langexplorer;

import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.dto.i18n.MultiLangFieldInfo;
import org.iplass.adminconsole.shared.base.rpc.i18n.I18nServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.i18n.I18nServiceFactory;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceFactory;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.shared.tools.dto.langexplorer.OutputMode;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuTree;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.treeview.TreeView;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;

public class GemLangExplorerSettingController implements LangExplorerSettingController {

	/** メタデータサービス */
	private static final MetaDataServiceAsync service = MetaDataServiceFactory.get();
	private static final I18nServiceAsync i18nService = I18nServiceFactory.get();
	private static final TenantServiceAsync tenantService = TenantServiceFactory.get();

	@Override
	public void displayMultiLangInfo(final LangEditListPane langEditListPane, String definitionClassName, String definitionName, String path) {

		DefinitionMultiLangSettingManager manager = getDefinitionMultiLangSettingManager(definitionClassName);

		if (manager == null) {
			SC.say(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangExplorerSettingManager_invalidMeta"));
			GWT.log(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangExplorerSettingManager_invalidMeta"));
			return;
		} else {
			manager.disp(langEditListPane, definitionName, path);
			langEditListPane.enableButtons();
		}
	}

	@Override
	public void updateMultiLangInfo(final LangEditListPane langEditListPane, String definitionClassName, String definitionName, String path) {

		DefinitionMultiLangSettingManager manager = getDefinitionMultiLangSettingManager(definitionClassName);

		if (manager == null) {
			SC.say(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangExplorerSettingManager_invalidMeta"));
			GWT.log(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangExplorerSettingManager_invalidMeta"));
			return;
		} else {
			manager.update(langEditListPane, definitionName, path);
		}
	}

	@Override
	public void exportMultiLangInfo(String path, String defName) {

		if (SmartGWTUtil.isEmpty(defName)) {
			//空なのでNG
			SC.warn(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangExplorerSettingManager_invalidMeta"));
			return;
		}

		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + "service/langdownload")
			.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
			.addParameter("mode", OutputMode.SINGLE.name())
			.addParameter("definitionName", defName)
			.addParameter("path", path)
			.execute();
	}

	@Override
	public void exportMultiLangInfo(String[] paths, String repoType) {

		if (paths == null || paths.length == 0) {
			//空なのでNG
			SC.warn(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangExplorerSettingManager_invalidMeta"));
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (String path : paths) {
			if (sb.length() != 0) sb.append(",");
			sb.append(path);
		}

		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + "service/langdownload")
			.addParameter("tenantId", String.valueOf(TenantInfoHolder.getId()))
			.addParameter("mode", OutputMode.MULTI.name())
			.addParameter("repoType", repoType)
			.addParameter("paths", sb.toString())
			.execute();
	}

	protected DefinitionMultiLangSettingManager getDefinitionMultiLangSettingManager(String definitionClassName) {
		DefinitionMultiLangSettingManager manager = null;
		if (EntityDefinition.class.getName().equals(definitionClassName)) {
			manager = new EntityDefinitionMultiLangSettingManager();
		} else if (EntityView.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(EntityView.class);
		} else if (EntityFilter.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(EntityFilter.class);
		} else if (Tenant.class.getName().equals(definitionClassName)) {
			manager = new TenantMultiLangSettingManager();
		} else if (SelectValueDefinition.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(SelectValueDefinition.class);
		} else if (TopViewDefinition.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(TopViewDefinition.class);
		} else if (MenuItem.class.getName().equals(definitionClassName)) {
			manager = new MenuItemMultiLangSettingManager();
		} else if (TreeView.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(TreeView.class);
		} else if (EntityCalendar.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(EntityCalendar.class);
		} else if (ActionMappingDefinition.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(ActionMappingDefinition.class);
		} else if (CommandDefinition.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(CommandDefinition.class);
		} else if (TemplateDefinition.class.getName().equals(definitionClassName)) {
			manager = new TemplateDefinitionMultiLangSettingManager();
		} else if (MessageCategory.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(MessageCategory.class);
		} else if (AuthenticationPolicyDefinition.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(AuthenticationPolicyDefinition.class);
		} else if (MenuTree.class.getName().equals(definitionClassName)) {
			manager = new DefaultDefinitionMultiLangSettingManager(MenuTree.class);
		}

		return manager;
	}

	/**
	 * 定義の多言語設定操作用IF
	 */
	protected interface DefinitionMultiLangSettingManager {

		/**
		 * 定義の多言語設定を編集用のパネルに表示
		 * @param langEditListPane
		 * @param path
		 * @param definitionName
		 */
		void disp(LangEditListPane langEditListPane, String definitionName, String path);

		/**
		 * 定義の多言語設定を更新
		 * @param langEditListPane
		 * @param path
		 * @param definitionName
		 */
		void update(LangEditListPane langEditListPane, String definitionName, String path);
	}

	/**
	 * 定義取得時のコールバック
	 */
	protected interface GetDefinitionCallback {
		void execute(Definition definition);
	}

	/**
	 * Definition共通処理
	 */
	protected class DefaultDefinitionMultiLangSettingManager implements DefinitionMultiLangSettingManager {

		private Class<?> defType;

		public DefaultDefinitionMultiLangSettingManager(Class<?> defType) {
			this.defType = defType;
		}

		@Override
		public void disp(final LangEditListPane langEditListPane, String definitionName, String path) {

			getDefinition(definitionName, new GetDefinitionCallback() {

				@Override
				public void execute(final Definition definition) {
					// アノテーションから多言語項目を取得してlocalizedStringMapを作る
					i18nService.getMultiLangItemInfoForDisp(TenantInfoHolder.getId(), definition, new AsyncCallback<Map<String, List<LocalizedStringDefinition>>>() {

						@Override
						public void onFailure(Throwable caught) {
							_onFailure(caught);
						}

						@Override
						public void onSuccess(Map<String, List<LocalizedStringDefinition>> localizedStringMap) {
							langEditListPane.createGrid(localizedStringMap, defType.getName(), getDefinitionName(definition), path);
						}
					});
				}
			});
		}

		@Override
		public void update(final LangEditListPane langEditListPane, final String definitionName, final String path) {
			getDefinition(definitionName, new GetDefinitionCallback() {

				@Override
				public void execute(Definition definition) {
					Map<String, MultiLangFieldInfo> multiLangFieldsMap = langEditListPane.createUpdateInfo();

					// multiLangFieldsMapとdefinitionを引数に更新用definitionを作成
					i18nService.getMultiLangItemInfoForUpdate(TenantInfoHolder.getId(), definition, multiLangFieldsMap, new AsyncCallback<Definition>() {

						@Override
						public void onFailure(Throwable caught) {
							_onFailure(caught);
						}

						@Override
						public void onSuccess(Definition result) {
							updateDefinition(result, new UpdateDefinitionCallback(defType.getName(), definitionName, path, langEditListPane));
						}
					});
				}
			});
		}

		/**
		 * エラー処理
		 * @param caught
		 */
		protected void _onFailure(Throwable caught) {
			SC.say(caught.getMessage());
			GWT.log(caught.toString(), caught);
		}

		/**
		 * 定義名取得
		 * @param definition
		 * @return
		 */
		protected String getDefinitionName(Definition definition) {
			return definition.getName();
		};

		/**
		 * 定義取得
		 * @param callback
		 */
		protected void getDefinition(String definitionName, GetDefinitionCallback callback) {
			service.getDefinition(TenantInfoHolder.getId(), defType.getName(), definitionName, new AsyncCallback<Definition>() {

				@Override
				public void onFailure(Throwable caught) {
					_onFailure(caught);
				}

				@Override
				public void onSuccess(Definition definition) {
					callback.execute(definition);
				}
			});
		}

		/**
		 * 定義更新
		 * @param definition
		 * @param callback
		 */
		protected void updateDefinition(Definition definition, UpdateDefinitionCallback callback) {
			service.updateDefinition(TenantInfoHolder.getId(), definition, -1, false, callback);
		}

		/**
		 * 定義更新時のコールバック
		 */
		protected final class UpdateDefinitionCallback implements AsyncCallback<AdminDefinitionModifyResult> {
			private final String definitionClassName;
			private final String definitionName;
			private final String path;
			private final LangEditListPane langEditListPane;

			public UpdateDefinitionCallback(String definitionClassName, String definitionName, String path, LangEditListPane langEditListPane) {
				this.definitionClassName = definitionClassName;
				this.definitionName = definitionName;
				this.path = path;
				this.langEditListPane = langEditListPane;
			}

			@Override
			public void onFailure(Throwable caught) {
				_onFailure(caught);
			}

			@Override
			public void onSuccess(AdminDefinitionModifyResult result) {
				SC.say(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangExplorerSettingManager_completion"));
				displayMultiLangInfo(langEditListPane, definitionClassName, definitionName, path);
			}
		}

	}

	/**
	 * Tenant用Manager
	 */
	protected class TenantMultiLangSettingManager extends DefaultDefinitionMultiLangSettingManager {

		TenantMultiLangSettingManager() {
			super(Tenant.class);
		}

		@Override
		protected void getDefinition(String definitionName, final GetDefinitionCallback callback) {
			tenantService.getTenant(TenantInfoHolder.getId(), new AsyncCallback<Tenant>() {

				@Override
				public void onFailure(Throwable caught) {
					_onFailure(caught);
				}

				@Override
				public void onSuccess(Tenant definition) {
					callback.execute(definition);
				}
			});
		}

		@Override
		protected void updateDefinition(Definition definition, final UpdateDefinitionCallback callback) {
			//callbackの型が違うので中のパラメータだけ利用
			tenantService.updateTenant(TenantInfoHolder.getId(), (Tenant) definition, -1, false, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					_onFailure(caught);
				}

				@Override
				public void onSuccess(Boolean result) {
					SC.say(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangExplorerSettingManager_completion"));
					displayMultiLangInfo(callback.langEditListPane, callback.definitionClassName, callback.definitionName, callback.path);
				}
			});
		}
	}

	/**
	 * EntityDefinition用Manager
	 */
	protected class EntityDefinitionMultiLangSettingManager extends DefaultDefinitionMultiLangSettingManager {

		EntityDefinitionMultiLangSettingManager() {
			super(EntityDefinition.class);
		}

		@Override
		protected void updateDefinition(Definition definition, UpdateDefinitionCallback callback) {
			// service側固有ロジックのため・・・
			service.updateEntityDefinition(TenantInfoHolder.getId(), (EntityDefinition) definition, -1, false, callback);
		}
	}

	/**
	 * MenuItem用Manager
	 */
	protected class MenuItemMultiLangSettingManager extends DefaultDefinitionMultiLangSettingManager {

		MenuItemMultiLangSettingManager() {
			super(MenuItem.class);
		}

		@Override
		protected void updateDefinition(Definition definition, UpdateDefinitionCallback callback) {
			// service側固有ロジックのため・・・
			service.updateMenuItem(TenantInfoHolder.getId(), (MenuItem) definition, callback);
		}
	}

	/**
	 * Template用Manager
	 */
	protected class TemplateDefinitionMultiLangSettingManager extends DefaultDefinitionMultiLangSettingManager {

		TemplateDefinitionMultiLangSettingManager() {
			super(TemplateDefinition.class);
		}

		@Override
		protected void updateDefinition(Definition definition, UpdateDefinitionCallback callback) {
			// service側固有ロジックのため・・・
			service.updateTemplateDefinition(TenantInfoHolder.getId(), (TemplateDefinition) definition, -1, false, callback);
		}
	}

}
