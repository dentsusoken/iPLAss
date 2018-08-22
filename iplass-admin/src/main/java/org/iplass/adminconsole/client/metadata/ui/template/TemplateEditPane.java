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

package org.iplass.adminconsole.client.metadata.ui.template;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
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
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.LocalizedBinaryDefinitionInfo;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.LocalizedReportDefinitionInfo;
import org.iplass.adminconsole.client.metadata.ui.template.report.ReportTemplateEditPane;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class TemplateEditPane extends MetaDataMainEditPane {

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** Definition共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** Template共通属性部分 */
	private TemplateAttributePane attributePane;

	/** 個別属性部分 */
	private VLayout templateTypeMainPane;
	private TemplateTypeEditPane typeEditPane;

	/** 多言語設定部分 */
	private TemplateMultiLanguagePane multilingualPane;

	/** 編集対象 */
	private TemplateDefinition curDefinition;

	/** 編集対象ID */
	private String curDefinitionId;

	/** 編集対象バージョン */
	private int curVersion;

	public TemplateEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(curDefinition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//共通属性
		commonSection = new MetaCommonAttributeSection(targetNode, TemplateDefinition.class, true);

		//Template属性編集部分
		attributePane = new TemplateAttributePane();
		attributePane.setTypeChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				typeChanged(attributePane.selectedType());
			}
		});

		templateTypeMainPane = new VLayout();

		//Section設定
		SectionStackSection templateSection = createSection("Template Attribute", attributePane, templateTypeMainPane);

		//多言語編集部分
		multilingualPane = new TemplateMultiLanguagePane();

		//Section設定
		SectionStackSection multilingualSection = createSection("Multilingual Attribute", false, multilingualPane);

		setMainSections(commonSection, templateSection, multilingualSection);

		//全体配置
		addMember(headerPane);
		addMember(mainStack);

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

		service.getTemplateDefinitionEntry(TenantInfoHolder.getId(), defName, new AdminAsyncCallback<DefinitionEntry>() {
			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition((TemplateDefinition)result.getDefinition());

				//登録済のバージョン情報を保持
				curVersion = result.getDefinitionInfo().getVersion();
				curDefinitionId = result.getDefinitionInfo().getObjDefId();
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(TemplateDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(TemplateDefinition definition) {
		this.curDefinition = definition;

		//共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
		commonSection.setDescription(curDefinition.getDescription());
		commonSection.setLocalizedDisplayNameList(curDefinition.getLocalizedDisplayNameList());

		//Template共通属性
		attributePane.setDefinition(curDefinition);

		//Type別設定のクリア
		if (typeEditPane != null) {
			if (templateTypeMainPane.contains(typeEditPane)) {
				templateTypeMainPane.removeMember(typeEditPane);
			}
			typeEditPane = null;
		}

		//多言語設定のクリア
		multilingualPane.clearRecord();

		//Type別設定
		if (curDefinition != null) {
			TemplateType type = TemplateType.valueOf(curDefinition);

			//LayoutActionの編集可否
			attributePane.disableLayoutAction(type.isDisableLayoutAction());

			//Type別設定
			typeEditPane = TemplateType.typeOfEditPane(type);
			typeEditPane.setDefinition(curDefinition);
			templateTypeMainPane.addMember(typeEditPane);

			//多言語設定
			multilingualPane.setDefinition(type, curDefinition);
		}
	}

	/**
	 * タイプ変更処理
	 *
	 * @param type 選択タイプ
	 */
	private void typeChanged(TemplateType type) {

		//タイプが変更されたら共通属性以外は全部クリア
		TemplateDefinition newDefinition = null;
		if (type != null) {
			//タイプにあったDefinitionを取得
			newDefinition = TemplateType.typeOfDefinition(type);

			//共通属性をコピー
			newDefinition.setName(curDefinition.getName());
			newDefinition.setDisplayName(curDefinition.getDisplayName());
			newDefinition.setDescription(curDefinition.getDescription());
			newDefinition.setContentType(curDefinition.getContentType());
			newDefinition.setLayoutActionName(curDefinition.getLayoutActionName());
		}

		setDefinition(newDefinition);
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateTemplate(final TemplateDefinition definition, boolean checkVersion) {

		TemplateType type = TemplateType.valueOf(definition);

		boolean updateRpc = !typeEditPane.isFileUpload();

		if (updateRpc) {
			//通常のRPCでの更新
			SmartGWTUtil.showSaveProgress();
			service.updateTemplateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

				@Override
				protected void overwriteUpdate() {
					updateTemplate(definition, false);
				}

				@Override
				protected void afterUpdate(AdminDefinitionModifyResult result) {
					updateComplete(definition);
				}
			});
		} else {
			//Binary,Jasperはファイルのアップロードがあるので別
			if (TemplateType.BINARY.equals(type)) {
				SmartGWTUtil.showSaveProgress();

				List<LocalizedBinaryDefinitionInfo> localeList = multilingualPane.getEditBinaryDefinitionList();
				((BinaryTemplateEditPane)typeEditPane).updateBinaryTemplate(definition, localeList,
						curVersion, checkVersion, new MetaDataUpdateCallback() {

					/**
					 * Binaryはメッセージのハンドリングが特殊なため独自実装する
					 */
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
					protected void overwriteUpdate() {
						updateTemplate(definition, false);
					}

					@Override
					protected void afterUpdate(AdminDefinitionModifyResult result) {
						updateComplete(definition);
					}
				});

			} else if(TemplateType.REPORT.equals(type)){
				SmartGWTUtil.showSaveProgress();

				List<LocalizedReportDefinitionInfo> localeList = multilingualPane.getEditReportDefinitionList();
				((ReportTemplateEditPane)typeEditPane).updateReportTemplate(definition, localeList,
						curVersion, checkVersion, new MetaDataUpdateCallback() {

					/**
					 * Reportはメッセージのハンドリングが特殊なため独自実装する
					 */
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
					protected void overwriteUpdate() {
						updateTemplate(definition, false);
					}

					@Override
					protected void afterUpdate(AdminDefinitionModifyResult result) {
						updateComplete(definition);
					}
				});
			}
		}
	}

	/**
	 * 更新完了処理
	 *
	 * @param definition 更新対象
	 */
	private void updateComplete(TemplateDefinition definition) {

		SC.say(getRS("completion"),
				getRS("saveTemplateComp"));

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
		return AdminClientMessageUtil.getString("ui_metadata_template_TemplateEditPane_" + key);
	}

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			boolean commonValidate = commonSection.validate();
			boolean attrValidate = attributePane.validate();
			boolean typeValidate = typeEditPane.validate();
			if (!commonValidate || !attrValidate || !typeValidate) {
				return;
			}

			//多言語だけ別でチェック
			if (!multilingualPane.validate()) {
				return;
			}

			SC.ask(getRS("saveConfirm"),
					getRS("saveTemplateComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						TemplateType type = attributePane.selectedType();
						TemplateDefinition definition = TemplateType.typeOfDefinition(type);

						definition.setName(commonSection.getName());
						definition.setDisplayName(commonSection.getDisplayName());
						definition.setDescription(commonSection.getDescription());
						definition.setLocalizedDisplayNameList(commonSection.getLocalizedDisplayNameList());

						definition = attributePane.getEditDefinition(definition);
						definition = typeEditPane.getEditDefinition(definition);
						definition = multilingualPane.getEditDefinition(definition);

						updateTemplate(definition, true);
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

			SC.ask(getRS("cancelConfirm"),
					getRS("cancelConfirmComment")
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
