/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui;

import java.util.List;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.RefreshMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.plugin.AdminPlugin;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeGrid;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;
import org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.dto.i18n.I18nMetaDisplayInfo;
import org.iplass.adminconsole.shared.base.dto.tenant.TenantEnv;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceFactory;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * <p>MetaDataSetting用メニューグリッドです。</p>
 *
 * <p>{@link RefreshClickHandler} を実装しています。</p>
 *
 * @author lis70i
 */
public class MetaDataPluginTreeGrid extends AdminMenuTreeGrid {

	private static MetaDataPluginTreeGrid instance;

	private TenantServiceAsync service = TenantServiceFactory.get();
	private MetaDataServiceAsync metaService = MetaDataServiceFactory.get();

	/**
	 * インスタンスを返します。
	 *
	 * @return AdminMenuTreeGrid
	 */
	public static MetaDataPluginTreeGrid getInstance() {
		if (instance == null) {
			instance = new MetaDataPluginTreeGrid(MainWorkspaceTab.getInstance());
		}
		return instance;
	}

	/**
	 * コンストラクタ
	 *
	 * @param mainPane
	 */
	private MetaDataPluginTreeGrid(MainWorkspaceTab mainPane) {
		super(mainPane);

		//Dragを可能にする
		setSelectionType(SelectionStyle.SINGLE);
		setCanDragRecordsOut(true);
		setDragDataAction(DragDataAction.NONE);
		setDragAppearance(DragAppearance.TRACKER);
		setDragType(MetaDataConstants.METADATA_DRAG_DROP_TYPE);

		//Drag開始処理
		addDragStartHandler(new DragStartHandler() {

			@Override
			public void onDragStart(DragStartEvent event) {
				TreeNode node = (TreeNode) getSelectedRecord();
				if (!(node instanceof MetaDataItemMenuTreeNode)) {
					//Folderは対象外
					event.cancel();
				}
			}
		});

		setCanHover(Boolean.TRUE);
		setHoverCustomizer(new HoverCustomizer() {

			@Override
			public String hoverHTML(Object value, ListGridRecord record, int rowNum, final int colNum) {
				final AdminMenuTreeNode node = (AdminMenuTreeNode)Tree.nodeForRecord(record);
				if (node instanceof MetaDataItemMenuTreeNode) {
					final MetaDataItemMenuTreeNode item = (MetaDataItemMenuTreeNode)node;

					if (item.isHoverLoaded()) {
						return SmartGWTUtil.getHoverString(item.getHover());
					} else {

						metaService.getMetaDisplayInfo(TenantInfoHolder.getId(), item.getDefPath(), new AsyncCallback<I18nMetaDisplayInfo>() {

							@Override
							public void onFailure(Throwable caught) {
								//特に何もしない
								item.setHoverLoaded(true);
							}

							@Override
							public void onSuccess(I18nMetaDisplayInfo result) {
								item.setHoverLoaded(true);
								item.setHover(result.getI18nDisplayName());
							}

						});

						//ここでなにか返さないといけない(TODO 1度目では値が返らない)
						return SmartGWTUtil.getHoverString(item.getHover());
					}

				} else {
					return "";
				}
			}

		});
	}

	@Override
	protected List<AdminPlugin> plugins() {
		MetaDataPluginController controller = GWT.create(MetaDataPluginController.class);
		return controller.plugins();
	}

	public void clearMetaDataCache() {

		metaService.clearAllCache(TenantInfoHolder.getId(), new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				//キャッシュのクリアが成功したらTenant情報再読み込み(他で変更されている可能性があるので)
				reloadTenantInfoHolder();
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.warn(AdminClientMessageUtil.getString("ui_MetaDataSettingTreeGrid_failedToClearCache") + caught.getMessage());
			}
		});
	}

	private void reloadTenantInfoHolder() {
		service.getTenantEnv(TenantInfoHolder.getId(), new AsyncCallback<TenantEnv>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.warn(AdminClientMessageUtil.getString("ui_MetaDataSettingTreeGrid_failedToClearCache") + caught.getMessage());
			}

			@Override
			public void onSuccess(TenantEnv result) {
				TenantInfoHolder.reload(result);

				//リフレッシュする
				RefreshMetaDataEvent.fire(AdminConsoleGlobalEventBus.getEventBus());
			}
		});
	}

}
