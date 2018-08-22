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

package org.iplass.adminconsole.client.metadata.ui.entity;

import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonAttributeSection;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListPane;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaVersionCheckException;
import org.iplass.adminconsole.shared.metadata.dto.entity.EntitySchemaLockedException;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.DefinitionInfo;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class EntityEditPane extends MetaDataMainEditPane {

	/** ロックのチェック間隔(ミリ秒) */
	private static final int LOCK_STATUS_CHECK_INTERVAL = 3000;
	/** ロック解除が遅い場合のチェック間隔(ミリ秒) */
	private static final int LOCK_STATUS_SLOW_CHECK_INTERVAL = 10000;
	/** ロック解除が遅いと判断するチェック回数 */
	private static final int LOCK_SLOW_THRESHOLD = 300;

	/** 編集対象 */
	private EntityDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;
	/** 編集対象Shared */
	private boolean curShared;
	/** 編集対象SharedOverwrite */
	private boolean curSharedOverwrite;

	private String curDefinitionId;

	/* MetaDataService */
	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	/** ロックエラー */
	private SchemaLockedPane lockedPane;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** Entity属性定義部分 */
	private EntityAttributePane entityAttributePane;
	/** プロパティ定義部分 */
	private PropertyListPane propertyPane;
	/** イベントリスナー定義部分 */
	private EventListenerPane eventListnerPane;
	/** DataLocalization定義部分 */
	private DataLocalizationPane dataLocalizationPane;

	/**
	 * コンストラクタ
	 *
	 * @param defName 選択Entity名
	 */
	public EntityEditPane(final MetaDataItemMenuTreeNode targetNode, EntityPlugin plugin) {
		super(targetNode, plugin);

		setMembersMargin(5);

		lockedPane = new SchemaLockedPane();
		lockedPane.setVisible(false);

		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new SaveClickHandler());
		headerPane.setCancelClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_EntityEditPane_cancelConfirm"),
						AdminClientMessageUtil.getString("ui_metadata_entity_EntityEditPane_cancelConfirmComment")
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
		});

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(curDefinition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//共通属性
		commonSection = new MetaCommonAttributeSection(targetNode, EntityDefinition.class, true);

		// Entity属性用Pane初期化
		entityAttributePane = new EntityAttributePane();
		entityAttributePane.setCrawlStatusChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				crawlStatusChanged(entityAttributePane.isCrawlEntity());
			}
		});

		// Property用Pane初期化
		propertyPane = new PropertyListPane();

		// EventListener用Pane初期化
		eventListnerPane = new EventListenerPane();

		// DataLocalization用Pane初期化
		dataLocalizationPane = new DataLocalizationPane();

		// Section設定
		SectionStackSection propSection = createSection("Properties", entityAttributePane, propertyPane);
		SectionStackSection eListenerSection = createSection("EventListeners", false, eventListnerPane);
		SectionStackSection dataLocalizationSection = createSection("Data Localization", false, dataLocalizationPane);
		setMainSections(commonSection, propSection, eListenerSection, dataLocalizationSection);

		addMember(lockedPane);
		addMember(headerPane);
		addMember(mainStack);

		//表示データの取得
		initializeData();
	}

	private void initializeData() {

		//ロックメッセージを非表示
		lockedPane.setVisible(false);

		//エラーのクリア
		commonSection.clearErrors();
		entityAttributePane.clearErrors();
		propertyPane.clearErrors();
		eventListnerPane.clearErrors();
		dataLocalizationPane.clearErrors();

		service.getEntityDefinitionEntry(TenantInfoHolder.getId(), defName, new AdminAsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry result) {
				//スキーマのロック状態を確認
				getSchemaLockStatus(result);
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(EntityDefinition.class.getName(), defName, this);
	}

	private void getSchemaLockStatus(final DefinitionEntry entry) {

		service.checkLockEntityDefinition(TenantInfoHolder.getId(), defName, new AdminAsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					//ロックされていたらメッセージ表示
					showSchemaLockedMessage();
				} else {
					//ロックされていない場合は、保存ボタンを制御
					if (!targetNode.isShared() || targetNode.isOverwritable()) {
						headerPane.setSaveDisabled(false);
					}
				}

				//言語取得
				getEnableLang(entry);
			}

		});
	}

	private void getEnableLang(final DefinitionEntry entry) {
		service.getEnableLanguages(TenantInfoHolder.getId(), new AdminAsyncCallback<Map<String, String>>() {

			@Override
			public void onSuccess(Map<String, String> enableLangMap) {
				propertyPane.setEnableLangMap(enableLangMap);
				dataLocalizationPane.setEnableLangMap(enableLangMap);
				setDefinition(entry);
			}
		});
	}

	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (EntityDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();
		this.curShared = entry.getDefinitionInfo().isShared();
		this.curSharedOverwrite = entry.getDefinitionInfo().isSharedOverwrite();

		//共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
		commonSection.setDescription(curDefinition.getDescription());
		commonSection.setLocalizedDisplayNameList(curDefinition.getLocalizedDisplayNameList());

		entityAttributePane.setDefinition(curDefinition);
		propertyPane.setDefinition(curDefinition);
		eventListnerPane.setDefinition(curDefinition);
		dataLocalizationPane.setDefinition(curDefinition);
	}

	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			boolean commonValidate = commonSection.validate();
			boolean attributeValidate = entityAttributePane.validate();
			boolean propertyValidate = propertyPane.validate();
			boolean eventListnerValidate = eventListnerPane.validate();
			boolean dataLocalizationValidate = dataLocalizationPane.validate();
			if (!commonValidate || !attributeValidate || !propertyValidate || !eventListnerValidate || !dataLocalizationValidate) {
				return;
			}

			//プロパティ名重複チェック
			String duplicatePropertyName = propertyPane.getDuplicatePropertyName();
			if (!SmartGWTUtil.isEmpty(duplicatePropertyName)) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_EntityEditPane_duplicatePropertyNameErr", duplicatePropertyName));
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_EntityEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_entity_EntityEditPane_saveConfirmComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {

						EntityDefinition definition = curDefinition;
						definition = getEditDefinition(definition);
						Map<String, String> renamePropertyMap = getRenamePropertyMap();
						updateEntity(definition, renamePropertyMap, true);
					}
				}
			});
		}
	}

	private EntityDefinition getEditDefinition(EntityDefinition definition) {
		definition.setName(commonSection.getName());
		definition.setDisplayName(commonSection.getDisplayName());
		definition.setDescription(commonSection.getDescription());
		definition.setLocalizedDisplayNameList(commonSection.getLocalizedDisplayNameList());

		entityAttributePane.getEditDefinition(definition);
//		propertyPane.getEditDefinition(definition, definition.getVersionControlType());
		propertyPane.getEditDefinition(definition);
		eventListnerPane.getEditDefinition(definition);
		dataLocalizationPane.getEditDefinition(definition);

		return definition;
	}

	private Map<String, String> getRenamePropertyMap() {
		return propertyPane.getRenamePropertyMap();
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateEntity(final EntityDefinition definition, final Map<String, String> renamePropertyMap, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateEntityDefinition(TenantInfoHolder.getId(), definition, curVersion, renamePropertyMap, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void beforeOverwriteUpdate(MetaVersionCheckException exp) {
				//更新対象バージョンを入れ替え
				curVersion = exp.getLatestVersion();
				curShared = exp.isLatestShared();
				curSharedOverwrite = exp.isLatestSharedOverwrite();
			}

			@Override
			protected void overwriteUpdate() {
				updateEntity(definition, renamePropertyMap, false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				checkUpdateStatus(definition, 0);
			}

			@Override
			protected void afterFailure(Throwable caught) {
				//更新エラー処理のカスタマイズ
				if (caught instanceof EntitySchemaLockedException) {
					//スキーマロック中
					showSchemaLockedMessage();
				} else {
					super.afterFailure(caught);
				}
			}
		});
	}

	/**
	 * 更新処理のステータス完了チェック
	 * @param definition
	 */
	private void checkUpdateStatus(final EntityDefinition definition, final int count) {

		//一度Progressが終わっているので再度表示
		SmartGWTUtil.showProgress("Saving...");

		Timer timer = new Timer() {

			@Override
			public void run() {
				service.checkLockEntityDefinition(TenantInfoHolder.getId(), definition.getName(), new AdminAsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						cancel();

						if (result) {
							//ロックされていたら再度確認

							//何回実行されるかわからないので閾値以上は増やさない
							int checkCount = count;
							if (checkCount <= LOCK_SLOW_THRESHOLD) {
								checkCount++;
							}
							checkUpdateStatus(definition, checkCount);
						} else {
							updateResultCheck(definition);
						}
					}

					@Override
					protected String failureMessage(Throwable caught){
						return AdminClientMessageUtil.getString("ui_metadata_common_MetaDataUpdateCallback_failedUpdateMetaDataMsg") + caught.getMessage();
					}

				});
			}
		};
		//閾値までは短い間隔で確認
		int delayMillis = LOCK_STATUS_CHECK_INTERVAL;
		if (count > LOCK_SLOW_THRESHOLD) {
			//閾値までチェックしても終わっていない場合は長い間隔に変更
			delayMillis = LOCK_STATUS_SLOW_CHECK_INTERVAL;
		}
		timer.schedule(delayMillis);
	}

	/**
	 * スキーマがロックされていることを通知
	 */
	private void showSchemaLockedMessage() {
		//保存ボタンの利用不可
		headerPane.setSaveDisabled(true);

		//ロックメッセージを表示
		lockedPane.setVisible(true);
	}

	/**
	 * 更新完了後の判定処理
	 *
	 * @param definition 更新対象
	 */
	private void updateResultCheck(final EntityDefinition definition) {

		//更新した結果バージョン番号が変更されているかをチェック
		service.getEntityDefinitionEntry(TenantInfoHolder.getId(), defName, new AdminAsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry result) {
				if (result != null) {
					DefinitionInfo newest = result.getDefinitionInfo();
					//バージョンや共有属性が変わっていれば更新成功と判断
					if (curVersion != newest.getVersion()
							|| curShared != newest.isShared() || curSharedOverwrite != newest.isSharedOverwrite()) {
						updateComplete(definition);
						return;
					}
				}

				//バージョンが変わっていないなどのエラー
				SmartGWTUtil.hideProgress();
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_EntityEditPane_saveFailed"));
			}
		});
	}

	/**
	 * 更新完了処理
	 *
	 * @param definition 更新対象
	 */
	private void updateComplete(EntityDefinition definition) {

		SmartGWTUtil.hideProgress();

		SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityEditPane_completion"), AdminClientMessageUtil.getString("ui_metadata_entity_EntityEditPane_saveCompleted"));

		//再表示
		initializeData();
		commonSection.refreshSharedConfig();

		//ツリー再表示
		plugin.refreshWithSelect(definition.getName(), new AdminAsyncCallback<MetaDataItemMenuTreeNode>() {
			@Override
			public void onSuccess(MetaDataItemMenuTreeNode result) {
				headerPane.setTargetNode(result);
				commonSection.setTargetNode(result);
			}
		});
	}

	/**
	 * タイプ変更処理
	 *
	 * @param type 選択タイプ
	 */
	private void crawlStatusChanged(boolean isCrawlEntity) {
		propertyPane.setCrawlStatusChanged(isCrawlEntity);
	}

	private class SchemaLockedPane extends HLayout {

		/**
		 * コンストラクタ
		 */
		public SchemaLockedPane() {

			//レイアウト設定
			setWidth100();
			setAutoHeight();
			setMargin(6);
			setMembersMargin(5);
			setAlign(Alignment.LEFT);
			setOverflow(Overflow.VISIBLE);

			addMember(new SchemaLockedWindow());
		}

		private class SchemaLockedWindow extends AbstractWindow {

			public SchemaLockedWindow() {
				setWidth100();
				setTitle("Schema Locked Error");
				setHeaderIcon("exclamation.png");
				setHeight(100);
				setCanDragReposition(false);
				setCanDragResize(false);
				setShowCloseButton(false);

				addItem(new SchemaLockedMessagePane());
			}
		}

		private class SchemaLockedMessagePane extends VLayout {

			public SchemaLockedMessagePane() {
				setWidth100();
				setHeight100();
				setPadding(10);

				Label title = new Label(AdminClientMessageUtil.getString("ui_metadata_entity_EntityEditPane_schemaLockedComment"));
				title.setHeight(20);
				title.setWidth100();
				addMember(title);
			}
		}

	}

}
