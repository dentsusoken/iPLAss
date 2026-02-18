/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.selectvalue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * SelectValueDefinition編集パネル
 */
public class SelectValueEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象 */
	private SelectValueDefinition curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** 設定可能Local */
	private Map<String, String> enableLangMap;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection<SelectValueDefinition> commonSection;

	/** 個別属性部分 */
	private SelectValueAttributePane selectValueAttributePane;

	public SelectValueEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		service = MetaDataServiceFactory.get();

		//レイアウト設定
		setWidth100();

		//ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveDefinition();
			}
		});
		headerPane.setCancelClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancelDefinition();
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
		commonSection = new MetaCommonAttributeSection<>(targetNode, SelectValueDefinition.class);

		//個別属性
		selectValueAttributePane = new SelectValueAttributePane();

		//Section設定
		SectionStackSection selectValueSection = createSection("SelectValue Attribute", selectValueAttributePane);
		setMainSections(commonSection, selectValueSection);

		//全体配置
		addMember(headerPane);
		addMember(mainStack);

		initialize();
	}

	private void initialize() {

		//設定可能Localの取得
		service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_failedGetEnableLang") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				enableLangMap = result;

				//データ初期化
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

		service.getDefinitionEntry(TenantInfoHolder.getId(), SelectValueDefinition.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(SelectValueDefinition.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (SelectValueDefinition) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		commonSection.setDefinition(curDefinition);
		selectValueAttributePane.setDefinition(curDefinition);
	}

	/**
	 * 保存ボタン処理
	 */
	private void saveDefinition() {

		boolean commonValidate = commonSection.validate();
		if (!commonValidate) {
			return;
		}

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_saveConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_saveConfirmComment"), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					final SelectValueDefinition definition = curDefinition;
					commonSection.getEditDefinition(definition);
					selectValueAttributePane.getEditDefinition(definition);

					updateDefinition(definition, true);
				}
			}
		});
	}

	/**
	 * キャンセルボタン処理
	 */
	private void cancelDefinition() {

		SC.ask(AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_cancelConfirm"),
				AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_cancelConfirmComment")
				, new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value) {
					//再表示
					initializeData();
					commonSection.refreshSharedConfig();
				}
			}
		});
	}

	/**
	 * 更新処理
	 *
	 * @param definition 更新対象
	 */
	private void updateDefinition(final SelectValueDefinition definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateDefinition(definition, false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				updateDefinitionComplete(definition);
			}
		});
	}

	/**
	 * 更新完了処理
	 *
	 * @param definition 更新対象
	 */
	private void updateDefinitionComplete(SelectValueDefinition definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_saveSelectValue"));

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

	private class SelectValueAttributePane extends VLayout {

		private ListGrid selectGrid;

		public SelectValueAttributePane() {
			setWidth100();
			setMargin(5);

			selectGrid = new ListGrid();
			selectGrid.setHeight(1);
			selectGrid.setWidth100();
			selectGrid.setShowAllColumns(true);
			selectGrid.setShowAllRecords(true);
			selectGrid.setCanResizeFields(true);

			selectGrid.setOverflow(Overflow.VISIBLE);
			selectGrid.setBodyOverflow(Overflow.VISIBLE);

			selectGrid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される

			selectGrid.setCanResizeFields(true);	//列幅変更可
			selectGrid.setCanSort(false);			//ソート不可
			selectGrid.setCanGroupBy(false);		//Group化不可
			selectGrid.setCanPickFields(false);	//列の選択不可
			selectGrid.setCanAutoFitFields(false);	//列幅の自動調整不可(崩れるので)

			//grid内でのD&Dでの並べ替えを許可
			selectGrid.setCanDragRecordsOut(true);
			selectGrid.setCanAcceptDroppedRecords(true);
			selectGrid.setCanReorderRecords(true);

			ListGridField valueField = new ListGridField("value", "Value");
			valueField.setWidth(150);
			ListGridField dispNameField = new ListGridField("dispName", "DisplayName");
			selectGrid.setFields(valueField, dispNameField);

			selectGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					startSelectValueEdit(false, selectGrid, (SelectValueListGridRecord)event.getRecord());
				}
			});

			IButton addSel = new IButton();
			addSel.setTitle(AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_add"));
			IButton delSel = new IButton();
			delSel.setTitle(AdminClientMessageUtil.getString("ui_metadata_selectvalue_SelectValueEditPane_remove"));
			addSel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					startSelectValueEdit(true, selectGrid, new SelectValueListGridRecord());
				}
			});
			delSel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selectGrid.removeSelectedData();
				}
			});
			HLayout selectBtnLayout = new HLayout(5);
			selectBtnLayout.setWidth100();
			selectBtnLayout.setHeight(30);
			selectBtnLayout.setAlign(Alignment.LEFT);
			selectBtnLayout.setMargin(5);
			selectBtnLayout.addMember(addSel);
			selectBtnLayout.addMember(delSel);


			addMember(SmartGWTUtil.titleLabel("Values"));
			addMember(selectGrid);
			addMember(selectBtnLayout);

		}

		public void setDefinition(SelectValueDefinition definition) {

			selectGrid.setData(new ListGridRecord[]{});

			//Select固有項目の値を画面部品に設定
			if (definition.getSelectValueList() != null && definition.getSelectValueList().size() > 0) {
				List<SelectValue> svList = definition.getSelectValueList();
				List<LocalizedSelectValueDefinition> allLsvdList = definition.getLocalizedSelectValueList();
				SelectValueListGridRecord[] selRecords = new SelectValueListGridRecord[svList.size()];

				for (int i = 0; i < svList.size(); i++) {
					SelectValue sv = svList.get(i);

					List<LocalizedStringDefinition> lsdList = new ArrayList<LocalizedStringDefinition>();
					for (LocalizedSelectValueDefinition lsvd : allLsvdList) {

						if (lsvd.getSelectValueList() != null && lsvd.getSelectValueList().size() > 0) {
							for (SelectValue lsv : lsvd.getSelectValueList()) {
								if (sv.getValue().equals(lsv.getValue())) {
									LocalizedStringDefinition lsd = new LocalizedStringDefinition();
									lsd.setLocaleName(lsvd.getLocaleName());
									lsd.setStringValue(lsv.getDisplayName());
									lsdList.add(lsd);
								}
							}
						}
					}
					SelectValueListGridRecord record = new SelectValueListGridRecord(sv.getValue(), sv.getDisplayName(), lsdList);
					selRecords[i] = record;
				}

				selectGrid.setData(selRecords);
			}
		}

		public void getEditDefinition(SelectValueDefinition definition) {

			List<SelectValue> svList = new ArrayList<SelectValue>();
			List<LocalizedSelectValueDefinition> allLsvdList = new ArrayList<LocalizedSelectValueDefinition>();

			Map<String, List<SelectValue>> temp = new HashMap<String, List<SelectValue>>();
			for (Map.Entry<String, String> e : enableLangMap.entrySet()) {
				List<SelectValue> tempSvList = new ArrayList<SelectValue>();

				temp.put(e.getKey(), tempSvList);
			}

			for (ListGridRecord record : selectGrid.getRecords()) {
				SelectValueListGridRecord sRecord = (SelectValueListGridRecord)record;
				SelectValue sv = new SelectValue(sRecord.getSelectValue(), sRecord.getDispName());
				svList.add(sv);

				if (sRecord.getLocalizedDisplayNameList() != null) {
					for (LocalizedStringDefinition def : sRecord.getLocalizedDisplayNameList()) {
						SelectValue localizedSelect = new SelectValue(sRecord.getSelectValue(), def.getStringValue());

						((List<SelectValue>) temp.get(def.getLocaleName())).add(localizedSelect);
					}
				}
			}
			for (Map.Entry<String, String> e : enableLangMap.entrySet()) {
				LocalizedSelectValueDefinition lsvd = new LocalizedSelectValueDefinition();
				lsvd.setLocaleName(e.getKey());
				lsvd.setSelectValueList(temp.get(e.getKey()));
				allLsvdList.add(lsvd);
			}
			definition.setSelectValueList(svList);
			definition.setLocalizedSelectValueList(allLsvdList);
		}

		private void startSelectValueEdit(boolean isNewRow, ListGrid grid, SelectValueListGridRecord target) {
			SelectValueEditDialog dialog = new SelectValueEditDialog(isNewRow, grid, target, false);
			dialog.show();
		}
	}

}
