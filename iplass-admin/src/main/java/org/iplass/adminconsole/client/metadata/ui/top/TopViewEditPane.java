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

package org.iplass.adminconsole.client.metadata.ui.top;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonAttributeSection;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.view.top.TopViewDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 *
 * @author lis3wg
 */
public class TopViewEditPane extends MetaDataMainEditPane {

	/** サービス */
	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	/** TOP画面定義 */
	private TopViewDefinition definition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** メニューエリア */
	private TopViewMenuAreaPane menuArea;

	/** メインエリア */
	private TopViewMainAreaPane mainArea;

	/**
	 * コンストラクタ
	 */
	public TopViewEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				save();
			}
		});
		headerPane.setCancelClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancel();
			}
		});

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(definition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//共通属性
		commonSection = new MetaCommonAttributeSection(targetNode, TopViewDefinition.class);

//		//共通パネル
//		attributePane = new TopViewAttributePane();

		//ドロップ領域
		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(5);

		PartsOperationHandlerImpl controler = new PartsOperationHandlerImpl();
		menuArea = new TopViewMenuAreaPane(controler);
		mainArea = new TopViewMainAreaPane(controler);
		TopViewDragPane dragArea = new TopViewDragPane();

		layout.addMember(menuArea);
		layout.addMember(mainArea);
		layout.addMember(dragArea);

		//Section設定
		SectionStackSection topViewSection = createSection("TopView Attribute", layout);
		setMainSections(commonSection, topViewSection);

		//全体配置
		addMember(headerPane);
		addMember(mainStack);

		initialize();
	}

	/**
	 * @param defName
	 */
	private void initialize() {

		//エラーのクリア
		commonSection.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), TopViewDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry entry) {
				TopViewEditPane.this.definition = (TopViewDefinition) entry.getDefinition();
				TopViewEditPane.this.curVersion = entry.getDefinitionInfo().getVersion();
				TopViewEditPane.this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();


				//共通属性
				commonSection.setName(definition.getName());
				commonSection.setDisplayName(definition.getDisplayName());
				commonSection.setDescription(definition.getDescription());

				menuArea.setWidgets(definition.getWidgets());
				mainArea.setParts(definition.getParts());
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_top_TopViewEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_top_TopViewEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(TopViewDefinition.class.getName(), defName, this);
	}

	private void setValue() {
		definition.setName(commonSection.getName());
		definition.setDisplayName(commonSection.getDisplayName());
		definition.setDescription(commonSection.getDescription());

		definition.setWidgets(menuArea.getWidgets());
		definition.setParts(mainArea.getParts());
	}

	private void reset() {
		list.clear();
		menuArea.reset();
		mainArea.reset();
		commonSection.refreshSharedConfig();
	}

	private void save() {

		boolean commonValidate = commonSection.validate();
		if (!commonValidate) {
			return;
		}

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_top_TopViewEditPane_saveConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_top_TopViewEditPane_saveConfirmComment"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					setValue();
					updateDefinition(true);
				}
			}
		});
	}

	private void updateDefinition(boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateDefinition(false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_top_TopViewEditPane_completion"),
						AdminClientMessageUtil.getString("ui_metadata_top_TopViewEditPane_saveTopViewComp"));

				reset();

				//再表示
				initialize();

				//ツリー再表示
				plugin.refreshWithSelect(definition.getName(), new AsyncCallback<MetaDataItemMenuTreeNode>() {
					@Override
					public void onSuccess(MetaDataItemMenuTreeNode result) {
						headerPane.setTargetNode(result);
						commonSection.setTargetNode(result);
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
		});

	}

	private void cancel() {
		SC.ask(AdminClientMessageUtil.getString("ui_metadata_top_TopViewEditPane_cancelConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_top_TopViewEditPane_cancelConfirmComment")
				, new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value) {
					reset();

					initialize();
				}
			}
		});
	}

	private List<String> list = new ArrayList<String>();
	public class PartsOperationHandlerImpl implements PartsOperationHandler {

		@Override
		public boolean check(MTPEvent event) {
			String key = (String) event.getValue("key");
			return !list.contains(key);
		}

		@Override
		public void add(MTPEvent event) {
			String key = (String) event.getValue("key");
			list.add(key);
		}

		@Override
		public void remove(MTPEvent event) {
			String key = (String) event.getValue("key");
			list.remove(key);
		}

	}
}
