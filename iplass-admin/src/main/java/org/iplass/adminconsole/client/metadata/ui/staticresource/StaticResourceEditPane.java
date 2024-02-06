/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.staticresource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.UploadResultInfo;
import org.iplass.adminconsole.client.base.io.upload.UploadSubmitCompleteHandler;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
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
import org.iplass.adminconsole.client.metadata.ui.staticresource.StaticResourceMultiLanguagePane.LocalizedStaticResourceDefinitionInfo;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.EntryPathType;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceInfo;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceUploadProperty;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.staticresource.definition.EntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.MimeTypeMappingDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class StaticResourceEditPane extends MetaDataMainEditPane {

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	/** 編集対象 */
	private StaticResourceInfo curDefinition;
	/** 編集対象ID */
	private String curDefinitionId;
	/** 編集対象バージョン */
	private int curVersion;

	/** 共通ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection<StaticResourceDefinition> commonSection;

	/** StaticResource属性部分 */
	private StaticResourceAttributePane attributePane;

	/** 多言語設定部分 */
	private StaticResourceMultiLanguagePane multilingualPane;

	/** フォーム */
	private FormPanel form;
	private FlowPanel paramPanel;

	private AsyncCallback<AdminDefinitionModifyResult> callback;

	public StaticResourceEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		//レイアウト設定
		setWidth100();

		//ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new SaveClickHandler());
		headerPane.setCancelClickHandler(new CancelClickHandler());

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(StaticResourceDefinition.class.getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//共通属性
		commonSection = new MetaCommonAttributeSection<>(targetNode, StaticResourceDefinition.class, true);

		//Template属性編集部分
		attributePane = new StaticResourceAttributePane();
		attributePane.setEntryPathTypeChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				EntryPathType selectEntryPathType = attributePane.selectedEntryPathType();
				curDefinition.setEntryPathTranslator(EntryPathType.typeOfDefinition(selectEntryPathType));
				setDefinition(curDefinition);
			}});

		//Section設定
		SectionStackSection staticResourceSection = createSection("StaticResource Attribute", attributePane);

		//多言語編集部分
		multilingualPane = new StaticResourceMultiLanguagePane();

		//Section設定
		SectionStackSection multilingualSection = createSection("Multilingual Attribute", false, multilingualPane);

		setMainSections(commonSection, staticResourceSection, multilingualSection);

		// 入力部分
		form = new FormPanel();
		form.setVisible(false); // formは非表示でOK
		form.setHeight("5px");
		form.setAction(GWT.getModuleBaseURL() + StaticResourceUploadProperty.ACTION_URL);
		form.setMethod(FormPanel.METHOD_POST);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.addSubmitCompleteHandler(new StaticResourceDefinitionSubmitCompleteHandler());
		paramPanel = new FlowPanel();
		form.add(paramPanel);

		//全体配置
		addMember(headerPane);
		addMember(mainStack);
		addMember(form);

		//利用可能言語取得
		getEnableLanguage();
	}

	private void getEnableLanguage() {
		service.getEnableLanguages(TenantInfoHolder.getId(), new AdminAsyncCallback<Map<String, String>>() {
			@Override
			public void onSuccess(Map<String, String> result) {
				//SelectItemはLinkedHashMapのみサポートなので置き換え
				LinkedHashMap<String, String> enableLang = new LinkedHashMap<String, String>();
				enableLang.putAll(result);
				multilingualPane.setEnableLanguages(enableLang);

				//表示データの取得
				initializeData();
			}
		});
	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {
		//エラーのクリア
		commonSection.clearErrors();

		service.getStaticResourceDefinitionEntry(TenantInfoHolder.getId(), defName, new AdminAsyncCallback<DefinitionEntry>() {
			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition((StaticResourceInfo) result.getDefinition());

				//登録済のバージョン情報を保持
				curVersion = result.getDefinitionInfo().getVersion();
				curDefinitionId = result.getDefinitionInfo().getObjDefId();
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(StaticResourceDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(StaticResourceInfo difinition) {
		this.curDefinition = difinition;

		//共通属性(StaticResourceInfo->StaticResourceDefinition)
		StaticResourceDefinition dummy = new StaticResourceDefinition();
		dummy.setName(curDefinition.getName());
		dummy.setDisplayName(curDefinition.getDisplayName());
		dummy.setDescription(curDefinition.getDescription());
		commonSection.setDefinition(dummy);
		commonSection.setLocalizedDisplayNameList(curDefinition.getLocalizedDisplayNameList());

		attributePane.setDefinition(curDefinition);
		multilingualPane.setDefinition(curDefinition);
	}

	private void addUploadParameter(String key, String value) {
		paramPanel.add(new Hidden(key, value));
	}

	/**
	 * アップロード処理
	 *
	 * @param definition 更新対象
	 * @param checkVersion バージョンチェック
	 */
	private void uploadStaticResource(final StaticResourceInfo definition, boolean checkVersion) {
		// 上書き再実行の可能性があるので一度クリア
		this.paramPanel.clear();
		this.paramPanel.add(this.attributePane.getEditFileUpload());

		addUploadParameter(StaticResourceUploadProperty.TENANT_ID, Integer.toString(TenantInfoHolder.getId()));

		addUploadParameter(StaticResourceUploadProperty.DEF_NAME, definition.getName());
		addUploadParameter(StaticResourceUploadProperty.DISPLAY_NAME, definition.getDisplayName());
		addUploadParameter(StaticResourceUploadProperty.DESCRIPTION, definition.getDescription());

		// 多言語表示名
		List<LocalizedStringDefinition> localizedDisplayNameList = definition.getLocalizedDisplayNameList();
		if (localizedDisplayNameList != null) {
			for (LocalizedStringDefinition localeDispNameDef : localizedDisplayNameList) {
				addUploadParameter(StaticResourceUploadProperty.DISPLAY_NAME_LOCALE_PREFIX  + localeDispNameDef.getLocaleName(), localeDispNameDef.getStringValue());
			}
		}

		if (definition.getFileType() != null) {
			addUploadParameter(StaticResourceUploadProperty.BINARY_TYPE, definition.getFileType().toString());
		}

		addUploadParameter(StaticResourceUploadProperty.CONTENT_TYPE, definition.getContentType());

		// MIME Type Mapping
		// Field name : mimeTypeMapping_<Extension>, Value : <MimeType>
		List<MimeTypeMappingDefinition> mimeTypeMapList = definition.getMimeTypeMapping();
		if (mimeTypeMapList != null) {
			for (MimeTypeMappingDefinition mimeTypeMapDef : mimeTypeMapList) {
				addUploadParameter(StaticResourceUploadProperty.MIME_TYPE_MAPPING_PREFIX  + mimeTypeMapDef.getExtension(), mimeTypeMapDef.getMimeType());
			}
		}

		addUploadParameter(StaticResourceUploadProperty.ENTRY_TEXT_CHARSET, definition.getEntryTextCharset());

		EntryPathTranslatorDefinition entryPathDef = definition.getEntryPathTranslator();
		if (entryPathDef != null) {
			addUploadParameter(StaticResourceUploadProperty.ENTRY_PATH_TYPE, EntryPathType.valueOf(entryPathDef).toString());
			addUploadParameter(StaticResourceUploadProperty.ENTRY_PATH_CONTENT, EntryPathType.getEntryPath(entryPathDef));
		}

		addUploadParameter(StaticResourceUploadProperty.VERSION, Integer.toString(this.curVersion));
		addUploadParameter(StaticResourceUploadProperty.CHECK_VERSION, String.valueOf(checkVersion));

		List<LocalizedStaticResourceDefinitionInfo> localeList = this.multilingualPane.getEditDefinitionList();

		if (localeList != null && !localeList.isEmpty()) {
			for (LocalizedStaticResourceDefinitionInfo info : localeList) {
				String locale = info.getDefinition().getLocaleName();
				String prefix = StaticResourceUploadProperty.LOCALE_PREFIX + locale + "_";

				if (!SmartGWTUtil.isEmpty(info.getStoredLang())) {
					// 更新の場合は、更新前のLocaleを送ることで削除対象にしない
					addUploadParameter(prefix + StaticResourceUploadProperty.LOCALE_BEFORE, info.getStoredLang());
				}
				// ファイルが選択されているもののみ送る
				// (新規でFileが選択されていないものは除外される
				if (info.getFileItem() != null) {
					FileUpload localeFile = info.getFileItem().getEditFileUpload();
					localeFile.setName(prefix + StaticResourceUploadProperty.UPLOAD_FILE);
					localeFile.setVisible(false);
					paramPanel.add(localeFile);

					addUploadParameter(prefix + StaticResourceUploadProperty.BINARY_TYPE, info.getDefinition().getFileType().toString());
				}
			}
		}

		form.submit();
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateStaticResource(final StaticResourceInfo definition, boolean checkVersion) {
		this.callback = new MetaDataUpdateCallback() {
			@Override
			protected void doSuccess(AdminDefinitionModifyResult result) {
				if (result.isSuccess()) {
					super.doSuccess(result);
				} else {
					if (result.getMessage() != null && result.getMessage().contains("Does not match the latest version.")) {
						SC.ask(getRS("overwriteConfirmMsg"), new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value) {
									overwriteUpdate();
								}
							}
						});
					} else {
						SC.warn(getRS("failedUpdateMetaDataMsg") + result.getMessage());
					}
				}
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				updateComplete(definition);
			}

			@Override
			protected void overwriteUpdate() {
				updateStaticResource(definition, false);
			}
		};

		uploadStaticResource(definition, checkVersion);
	}

	/**
	 * 更新完了処理
	 *
	 * @param definition 更新対象
	 */
	private void updateComplete(final StaticResourceInfo definition) {
		SC.say(getRS("completion"), getRS("saveStaticResourceComp"));

		//再表示
		initializeData();
		commonSection.refreshSharedConfig();

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

	private String getRS(String key) {
		return AdminClientMessageUtil.getString("ui_metadata_staticresource_StaticResourceEditPane_" + key);
	}

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			boolean commonValidate = commonSection.validate();
			boolean attrValidate = attributePane.validate();
			if (!commonValidate || !attrValidate) {
				return;
			}

			//多言語だけ別でチェック
			if (!multilingualPane.validate()) {
				return;
			}

			SC.ask(getRS("saveConfirm"), getRS("saveStaticResourceComment"), new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if (value) {
						StaticResourceInfo definition = new StaticResourceInfo();

						//StaticResourceInfo<-StaticResourceDefinition
						StaticResourceDefinition dummy = new StaticResourceDefinition();
						commonSection.getEditDefinition(dummy);
						definition.setName(dummy.getName());
						definition.setDisplayName(dummy.getDisplayName());
						definition.setDescription(dummy.getDescription());
						definition.setLocalizedDisplayNameList(commonSection.getLocalizedDisplayNameList());

						definition = attributePane.getEditDefinition(definition);
						definition = multilingualPane.getEditDefinition(definition);

						updateStaticResource(definition, true);
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
			SC.ask(getRS("cancelConfirm"), getRS("cancelConfirmComment"), new BooleanCallback() {
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

	private class StaticResourceDefinitionSubmitCompleteHandler extends UploadSubmitCompleteHandler {

		public StaticResourceDefinitionSubmitCompleteHandler() {
			super(form);
		}

		@Override
		protected void onSuccess(UploadResultInfo result) {

			// FlowPanelに追加しているので戻す
			attributePane.getEditUploadFileItem().redrawFileUpload();

			attributePane.resetSrcImg();

			if (callback != null) {
				String message = null;
				if (result.getMessages() != null && result.getMessages().size() > 0) {
					message = result.getMessages().get(0);
				}
				callback.onSuccess(new AdminDefinitionModifyResult(result.isFileUploadStatusSuccess(), message));
			}
			callback = null;
		}

		@Override
		protected void onFailure(String message) {

			// FlowPanelに追加しているので戻す
			attributePane.getEditUploadFileItem().redrawFileUpload();

			if (callback != null) {
				callback.onFailure(new RuntimeException(message));
			}
			callback = null;
		}

	}

}
