/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.calendar;

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
import org.iplass.mtp.view.calendar.EntityCalendar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class CalendarEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection<EntityCalendar> commonSection;

	/** 属性部分 */
	private CalendarAttributePane attributePane;

	private CalendarGrid calendarGrid;

	/** 編集対象 */
	private EntityCalendar curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/**
	 * コンストラクタ
	 *
	 * @param defName 選択Menu定義名
	 */
	public CalendarEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		service = MetaDataServiceFactory.get();

		//レイアウト設定
		setWidth100();

		//ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new SaveClickHandler());
		headerPane.setCancelClickHandler(new CancelClickHandler());

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(curDefinition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//共通属性
		commonSection = new MetaCommonAttributeSection<>(targetNode, EntityCalendar.class, true);

		//Calendar属性編集部分
		attributePane = new CalendarAttributePane();

		//メイン編集領域
		HLayout layout = new HLayout();
		layout.setWidth100();

		//Tree構成編集部分
		calendarGrid = new CalendarGrid();

		SectionStack menuLayoutSection = new SectionStack();
		SectionStackSection sec1 = new SectionStackSection(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_targetItems"));
		sec1.setCanCollapse(false);	//CLOSE不可
		sec1.addItem(calendarGrid);
		menuLayoutSection.addSection(sec1);
		layout.addMember(menuLayoutSection);

		Canvas dummy = new Canvas();
		dummy.setWidth(5);
		layout.addMember(dummy);

		//TreeItem選択部分(Entity定義の一覧なので流用可能)
		CalendarDragPane dragPane = new CalendarDragPane();
		layout.addMember(dragPane);

		//Section設定
		SectionStackSection calendarSection = createSection(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_calendarAttribute"), attributePane, layout);
		setMainSections(commonSection, calendarSection);

		//配置
		addMember(headerPane);
		addMember(mainStack);

		//表示データの取得
		initializeData();
	}


	/**
	 * データ初期化処理
	 */
	private void initializeData() {

		//エラーのクリア
		commonSection.clearErrors();

		//Calendarの検索
		service.getDefinitionEntry(TenantInfoHolder.getId(), EntityCalendar.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {
			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry calendar) {

				//画面に反映
				setCalendar(calendar);
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(EntityCalendar.class.getName(), defName, this);

	}


	/**
	 * 対象のCalendarをレイアウト表示パネルに設定します。
	 *
	 * @param entry Calendar
	 */
	private void setCalendar(DefinitionEntry entry) {
		this.curDefinition = (EntityCalendar) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		commonSection.setDefinition(curDefinition);
		commonSection.setLocalizedDisplayNameList(curDefinition.getLocalizedDisplayNameList());
		attributePane.setCalendar(curDefinition);
		calendarGrid.setCalendar(curDefinition);
	}

	/**
	 * カレンダーの更新処理
	 *
	 * @param calendar 更新対象EntityCalendar
	 */
	private void updateCalendar(final EntityCalendar calendar, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), calendar, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateCalendar(calendar, false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				updateComplete(calendar);
			}
		});
	}

	/**
	 * 更新完了処理
	 */
	private void updateComplete(EntityCalendar calendar) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_saveCalendarInfo"));

		//再表示
		initializeData();
		commonSection.refreshSharedConfig();

		//ツリー再表示
		plugin.refreshWithSelect(defName, new AsyncCallback<MetaDataItemMenuTreeNode>() {
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

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			boolean commonValidate = commonSection.validate();
			boolean treeValidate = calendarGrid.validate();
			boolean attributeValidate = attributePane.validate();
			if (!commonValidate || !attributeValidate || !treeValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_saveConfirmComment")
					, new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {

						EntityCalendar calendar = new EntityCalendar();

						calendar = commonSection.getEditDefinition(calendar);
						calendar.setLocalizedDisplayNameList(commonSection.getLocalizedDisplayNameList());
						calendar = attributePane.getEditCalendar(calendar);
						calendar.setItems(calendarGrid.getItems());

						updateCalendar(calendar, true);
					}
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

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarEditPane_cancelConfirmComment")
					, new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if (value) {
						initializeData();
						commonSection.refreshSharedConfig();
					}
				}
			});
		}
	}

}
