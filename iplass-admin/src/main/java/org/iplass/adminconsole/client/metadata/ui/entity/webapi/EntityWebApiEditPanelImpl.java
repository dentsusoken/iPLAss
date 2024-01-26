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

package org.iplass.adminconsole.client.metadata.ui.entity.webapi;

import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.shared.metadata.dto.MetaVersionCheckException;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * EntityWebApiDefinition Mainパネル
 */
public class EntityWebApiEditPanelImpl extends VLayout implements EntityWebApiEditPanel {

	//TODO MetaDataのステータスチェックエラー表示の方法を検討（全Itemが一覧で表示されているので、一覧に表示するのがいい）

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	private EntityWebApiListPane listPane;

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/**
	 * コンストラクタ
	 */
	public EntityWebApiEditPanelImpl() {

		service = MetaDataServiceFactory.get();

		//レイアウト設定
		setWidth100();

		//ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane();
		headerPane.setSaveClickHandler(new SaveClickHandler());
		headerPane.setCancelClickHandler(new CancelClickHandler());

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				Record record = listPane.getHistoryTargetRecord();

				if (record == null) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_select"));
				} else {
					int version = record.getAttributeAsInt("version");
					String definitionId = record.getAttributeAsString("definitionId");
					MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(EntityWebApiDefinition.class.getName(), definitionId, version);
					metaDataHistoryDialog.show();
				}
			}
		});

		listPane = new EntityWebApiListPane();

		addMember(headerPane);
		addMember(listPane);
	}

	private void initializeData() {
		listPane.refresh();
	}

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			final List<DefinitionEntry> entryList = listPane.getEditGridRecord();

			if (entryList == null || entryList.isEmpty()) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_noChange"));
			} else {
				SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_saveConfirm"),
						AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_saveEntityWebApiDef") , new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							updateEntityWebApiDefinition(entryList, true);
						}
					}
				});
			}
		}
	}

	private void updateEntityWebApiDefinition(final List<DefinitionEntry> entryList, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.registEntityWebApiDefinition(TenantInfoHolder.getId(), entryList, checkVersion, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				SmartGWTUtil.hideProgress();
				if (caught instanceof MetaVersionCheckException) {
					SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_overwriteConfirm"),
							AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_overwriteConfirmComment"), new BooleanCallback() {

						@Override
						public void execute(Boolean value) {

							if (value) {
								updateEntityWebApiDefinition(entryList, false);
							}
						}
					});
				} else {
					// 失敗時
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_failed"),
							AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_failedSaveEntityWebApiDef"));

					GWT.log(caught.toString(), caught);
				}
			}

			@Override
			public void onSuccess(Boolean result) {
				SmartGWTUtil.hideProgress();
				SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_completion"),
						AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_saveEntityWebApiDefComp"));
				initializeData();
			}

		});
	}

	/**
	 * キャンセルボタンイベント
	 */
	private final class CancelClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_entity_entitywebapi_EntityWebApiDefinitionMainPane_cancelConfirmComment")
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

