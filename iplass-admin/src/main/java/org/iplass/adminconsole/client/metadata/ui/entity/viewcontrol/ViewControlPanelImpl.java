/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.viewcontrol;

import java.util.Arrays;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.EntityPlugin;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.ViewControlSetting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * ビュー管理設定用パネル
 * @author lis3wg
 *
 */
public class ViewControlPanelImpl extends MetaDataMainEditPane implements ViewControlPanel {
	/** メタデータサービス */
	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;

	private ViewControlSettingPane viewControlSettingPane;

	private EntityView curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	public ViewControlPanelImpl() {
	}

	public void setTarget(final MetaDataItemMenuTreeNode targetNode, EntityPlugin manager) {
		super.setTarget(targetNode, manager);

		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new SaveClickHandler());
		headerPane.setCancelClickHandler(new CancelClickHandler());
		LayoutSpacer space = new LayoutSpacer();
		space.setWidth(95);
		headerPane.addMember(space);

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(curDefinition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//データ部分
		HLayout layout = new HLayout();
		layout.setWidth100();

		// 自動生成設定
		viewControlSettingPane = new ViewControlSettingPane();

		// Section設定
		SectionStackSection section = new SectionStackSection("View Control Setting");
		section.setExpanded(true);
		section.addItem(viewControlSettingPane);

		setMainSections(section);

		addMember(headerPane);
		addMember(mainStack);

		initializeData();
	}

	private void initializeData() {
		service.getDefinitionEntry(TenantInfoHolder.getId(), EntityView.class.getName(), defName, new LoadAsyncCallback());
	}

	/**
	 * 初期化読込処理
	 */
	private final class LoadAsyncCallback implements AsyncCallback<DefinitionEntry> {
		@Override
		public void onFailure(Throwable caught) {
			SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_failedGetScreenInfo") + caught.getMessage());
			GWT.log(caught.toString(), caught);
		}

		@Override
		public void onSuccess(DefinitionEntry entry) {
			if (entry == null || entry.getDefinition() == null) {
				return;
			}

			curDefinition = (EntityView) entry.getDefinition();
			curVersion = entry.getDefinitionInfo().getVersion();
			curDefinitionId = entry.getDefinitionInfo().getObjDefId();

			//作成済みのView名を取得
			List<String> detailViewNames = Arrays.asList(curDefinition.getDetailFormViewNames()) ;
			List<String> searchViewNames = Arrays.asList(curDefinition.getSearchFormViewNames());
			List<String> bulkViewNames = Arrays.asList(curDefinition.getBulkFormViewNames());

			List<ViewControlSetting> settings = curDefinition.getViewControlSettings();

			viewControlSettingPane.setDefinition(settings, detailViewNames, searchViewNames, bulkViewNames);

			//ステータスチェック
			StatusCheckUtil.statuCheck(EntityView.class.getName(), defName.replace(".", "/"), ViewControlPanelImpl.this);
		}
	}

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_saveView"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						//最新のView定義を取得
						service.getDefinition(TenantInfoHolder.getId(), EntityView.class.getName(), defName, new SaveStartAsyncCallback());
					}
				}
			});
		}

		/**
		 * 編集開始
		 */
		private final class SaveStartAsyncCallback implements AsyncCallback<EntityView> {

			@Override
			public void onSuccess(EntityView ev) {
				if (ev == null) {
					//View定義を新規作成
					EntityView tmp = new EntityView();

					tmp.setName(defName);
					tmp.setDefinitionName(defName);

					//自動生成設定を更新
					tmp.setViewControlSettings(viewControlSettingPane.getEditDefinition());

					createEntityView(tmp);
				} else {
					//自動生成設定を更新
					ev.setViewControlSettings(viewControlSettingPane.getEditDefinition());

					updateEntityView(ev, true);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				//定義の取得失敗
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_failedEntityView") + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}
		}

		private void createEntityView(final EntityView  definition) {
			SmartGWTUtil.showSaveProgress();
			service.createDefinition(TenantInfoHolder.getId(), definition, new MetaDataUpdateCallback() {
				@Override
				protected void overwriteUpdate() {
				}

				@Override
				protected void afterUpdate(AdminDefinitionModifyResult result) {
					//追加成功
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_completion"),
							AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_saveEntityViewComp"));

					//ステータスチェック
					StatusCheckUtil.statuCheck(EntityView.class.getName(), defName.replace(".", "/"), ViewControlPanelImpl.this);
				}
			});
		}

		private void updateEntityView(final EntityView  definition, boolean checkVersion) {
			SmartGWTUtil.showSaveProgress();
			service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

				@Override
				protected void overwriteUpdate() {
					updateEntityView(definition, false);
				}

				@Override
				protected void afterUpdate(AdminDefinitionModifyResult result) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_completion"),
							AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_saveEntityViewComp"));

					//ステータスチェック
					StatusCheckUtil.statuCheck(EntityView.class.getName(), defName.replace(".", "/"), ViewControlPanelImpl.this);

					// 画面はリロードされないので、最新バージョンを取得
					service.getDefinitionEntry(TenantInfoHolder.getId(), EntityView.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

						@Override
						public void onFailure(Throwable caught) {
							SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_failedEntityView") + caught.getMessage());
						}

						@Override
						public void onSuccess(DefinitionEntry result) {
							curVersion = result.getDefinitionInfo().getVersion();
						}

					});
				}
			});
		}

	}

	/**
	 * キャンセルボタンイベント
	 */
	private final class CancelClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_entity_viewcontrol_ViewControlPanelImpl_cancelCautionComment")
					, new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if (value) {
						initializeData();
					}
				}
			});
		}
	}

}
