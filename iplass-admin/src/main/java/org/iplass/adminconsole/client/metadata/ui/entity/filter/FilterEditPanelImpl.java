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

package org.iplass.adminconsole.client.metadata.ui.entity.filter;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.EQLExecutePane;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.filter.EntityFilterItemDS;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonAttributeSection;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.EntityPlugin;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author lis3wg
 */
public class FilterEditPanelImpl extends MetaDataMainEditPane implements FilterEditPanel {

	/** メタデータサービス */
	private MetaDataServiceAsync service;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	private SectionStack filterListStack;
	private SectionStackSection filterListSection;
	private ListGrid itemGrid;;

	private IButton btnSave;
	private IButton btnExec;
	private IButton btnClear;
	private TextItem nameField;
	private TextItem displayNameField;
	private ButtonItem langBtn;
	private TextAreaItem conditionField;
	private TextAreaItem sortField;

	private EQLExecutePane eqlExecutePane;

//	private String defName;
	private EntityDefinition ed;

	private int selectedIndex;

	/** 編集対象 */
	private EntityFilter ef;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	public List<LocalizedStringDefinition> localizedDisplayNameList;

	public FilterEditPanelImpl() {
		service = MetaDataServiceFactory.get();
	}

	public void setTarget(final MetaDataItemMenuTreeNode targetNode, EntityPlugin manager) {
		super.setTarget(targetNode, manager);


		//レイアウト設定
		setWidth100();

		//ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane();
		headerPane.setSaveClickHandler(new SaveClickHandler());
		headerPane.setCancelClickHandler(new CancelClickHandler());

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(ef.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//共通属性
		commonSection = new MetaCommonAttributeSection(targetNode, EntityFilter.class);

		//Filter編集用部分
		VLayout filterPane = new VLayout();
		filterPane.setWidth100();

		//メイン編集領域
		HLayout filterEditPane = new HLayout();
		filterEditPane.setWidth100();

		//フィルタ設定一覧部
		filterListStack = new SectionStack();
		filterListStack.setWidth("35%");
		filterListSection = new SectionStackSection("Filter Items");
		filterListSection.setCanCollapse(false);	//CLOSE不可
		filterListStack.addSection(filterListSection);
		filterListStack.setShowResizeBar(true);
//		filterListStack.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、次を収縮

		//フィルタ条件表示部
		SectionStack conditionStack = new SectionStack();
		SectionStackSection conditionSection = new SectionStackSection("Filter Item Setting");
		conditionSection.setCanCollapse(false);	//CLOSE不可

		VLayout conditionLayout = new VLayout(5);
		conditionLayout.setMargin(5);

		HLayout conditionHeader = new HLayout();
		//レイアウト設定
		conditionHeader.setWidth100();
		conditionHeader.setHeight(30);
		conditionHeader.setMargin(6);
		conditionHeader.setMembersMargin(5);
		conditionHeader.setAlign(Alignment.LEFT);

		//ボタン
		btnSave = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_save"));
		btnSave.setDisabled(true);
		btnSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!checkCondition()) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_confirm"),
							AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_notSettFilterCond"));
					return;
				}
				ListGridRecord record = itemGrid.getRecord(selectedIndex);
				EntityFilterItem item = (EntityFilterItem) record.getAttributeAsObject("valueObject");
				item.setDisplayName(displayNameField.getValueAsString());
				item.setCondition(conditionField.getValueAsString());
				item.setSort(sortField.getValueAsString());
				item.setLocalizedDisplayNameList(localizedDisplayNameList);
			}
		});
		btnExec = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_exeEql"));
		btnExec.setDisabled(true);
		btnExec.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// "select * from defname where " + conditionField.getValue()で検索
				if (!checkCondition()) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_confirm"),
							AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_notSettFilterCond"));
					return;
				}
				StringBuffer sb = new StringBuffer();
				sb.append("select ");
				if (ed != null) {
					for (PropertyDefinition property : ed.getPropertyList()) {
						if (property instanceof ReferenceProperty) {
							ReferenceProperty refp = (ReferenceProperty)property;
							sb.append(refp.getName() + ".oid, ");
							sb.append(refp.getName() + ".name, ");
						} else {
							sb.append(property.getName() + ", ");
						}
					}
					sb.deleteCharAt(sb.length() - 2);
				} else {
					sb.append("oid, name");
				}
				sb.append(" from ").append(FilterEditPanelImpl.this.defName);
				sb.append(" where ").append(conditionField.getValueAsString());
				if (!SmartGWTUtil.isEmpty(sortField.getValueAsString())) {
					sb.append(" order by ").append(sortField.getValueAsString());
				}
				execute(sb.toString());
			}
		});
		btnClear = new IButton(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_clear"));
		btnClear.setDisabled(true);
		btnClear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				conditionField.setValue("");
				sortField.setValue("");
			}
		});

		//配置
		conditionHeader.addMember(btnSave);
		conditionHeader.addMember(btnExec);
		conditionHeader.addMember(btnClear);

		nameField = new TextItem("name", AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_name"));
		nameField.setHeight(20);
		nameField.setDisabled(true);

		displayNameField = new TextItem("displayName", AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_dispName"));
		displayNameField.setHeight(20);

		langBtn = new ButtonItem("addDisplayName", "Languages");
		langBtn.setShowTitle(false);
		langBtn.setIcon("world.png");
		langBtn.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
		langBtn.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
		langBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_eachLangDspName"));
		langBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

				if (localizedDisplayNameList == null) {
					localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
				}
				LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedDisplayNameList);
				dialog.show();

			}
		});

		DynamicForm form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		form.setNumCols(5);
		form.setAlign(Alignment.LEFT);
		conditionField = new TextAreaItem("condition", "WHERE");
		conditionField.setColSpan(5);
		conditionField.setShowTitle(true);
		conditionField.setWidth("100%");
		conditionField.setHeight("100%");
		conditionField.setValidateOnChange(false);
		conditionField.setValidateOnExit(false);
		conditionField.setShowHint(false);
		conditionField.setDisabled(true);

		sortField = new TextAreaItem("sort", "ORDER BY");
		sortField.setColSpan(5);
		sortField.setShowTitle(true);
		sortField.setWidth("100%");
		sortField.setHeight("100%");
		sortField.setValidateOnChange(false);
		sortField.setValidateOnExit(false);
		sortField.setShowHint(false);
		sortField.setDisabled(true);
		form.setFields(nameField, displayNameField, langBtn, conditionField, sortField);

		conditionLayout.addMember(conditionHeader);
		conditionLayout.addMember(form);

		conditionSection.addItem(conditionLayout);
		conditionStack.addSection(conditionSection);

		filterEditPane.addMember(filterListStack);
		filterEditPane.addMember(conditionStack);

		//結果表示用
		eqlExecutePane = new EQLExecutePane();

		filterPane.addMember(filterEditPane);
		filterPane.addMember(eqlExecutePane);

		// Section設定
		SectionStackSection filterSection = createSection("Filter Attribute", filterPane);
		setMainSections(commonSection, filterSection);

		//全体配置
		addMember(headerPane);
		addMember(mainStack);

		//表示データの取得
		initializeData();
	}

	private boolean checkCondition() {
		String condition = conditionField.getValueAsString();
		return condition != null && !condition.isEmpty();
	}

	private void execute(final String eql) {

		//TODO Filterに対してバージョンをどうするか（現状は最新）
		eqlExecutePane.executeEQL(eql, false);
	}

	/**
	 *
	 */
	private void initializeData() {
		//エラーのクリア
		commonSection.clearErrors();

		loadEntityDefinition();
//		loadEntityFilter();
	}

	private void loadEntityDefinition() {
		service.getEntityDefinition(TenantInfoHolder.getId(), defName, new AsyncCallback<EntityDefinition>() {

			@Override
			public void onSuccess(EntityDefinition result) {
				ed = result;

				loadEntityFilter();
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_failedGetEntityDef") + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}
		});
	}

	private void loadEntityFilter() {
		service.getDefinitionEntry(TenantInfoHolder.getId(), EntityFilter.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onSuccess(DefinitionEntry entry) {

				if (entry != null && entry.getDefinition() != null) {
					ef = (EntityFilter) entry.getDefinition();
					curVersion = entry.getDefinitionInfo().getVersion();
					curDefinitionId = entry.getDefinitionInfo().getObjDefId();

					//共通属性
					commonSection.setName(ef.getName());
					commonSection.setDisplayName(ef.getDisplayName());
					commonSection.setDescription(ef.getDescription());

					//保存されているのでShared設定利用可能
					commonSection.setSharedEditDisabled(false);
				} else {
					//未登録時

					//共通属性（Entityからコピー）
					commonSection.setName(ed.getName());
					commonSection.setDisplayName(ed.getDisplayName());
					//commonSection.setDescription(ed.getDescription());

					//まだ未保存なのでShared設定利用不可
					commonSection.setSharedEditDisabled(true);
				}

				createItemGrid();

				if (ef != null) {
					//ステータスチェック
					StatusCheckUtil.statuCheck(EntityFilter.class.getName(), defName.replace(".", "/"), FilterEditPanelImpl.this);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_failedGetFilterSett") + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}
		});

	}

	private void createItemGrid() {
		if (filterListStack.getSections().length > 0) {
			filterListStack.removeSection(0);
		}
		filterListSection = new SectionStackSection("Filter Items");
		filterListSection.setCanCollapse(false);	//CLOSE不可

		ImgButton addItemButton = new ImgButton();
		addItemButton.setSrc("icon_add_files.png");
		addItemButton.setSize(16);
		addItemButton.setShowFocused(false);
		addItemButton.setShowRollOver(false);
		addItemButton.setShowDown(false);
		addItemButton.setTooltip(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_createFilterSett"));
		addItemButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EntityFilterItemDialog dialog = new EntityFilterItemDialog(itemGrid);
				dialog.show();
			}
		});
		filterListSection.setControls(addItemButton);

		itemGrid = new ListGrid(){
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				String fieldName = this.getFieldName(colNum);
				if (fieldName.equals("editButton")) {

					ImgButton editBtn = new ImgButton();
					editBtn.setShowDown(false);
					editBtn.setShowRollOver(false);
					editBtn.setLayoutAlign(Alignment.CENTER);
					//deleteBtn.setSrc("icon_delete.png");
					editBtn.setSrc("icon_edit.png");
					editBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_editNode"));
					editBtn.setHeight(16);
					editBtn.setWidth(16);
					editBtn.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							EntityFilterItem item = (EntityFilterItem)record.getAttributeAsObject("valueObject");
							setEntityFilterItem(item, itemGrid.getRecordIndex(record));
						}

					});

					return editBtn;
				} else if (fieldName.equals("deleteButton")) {

					ImgButton deleteBtn = new ImgButton();
					deleteBtn.setShowDown(false);
					deleteBtn.setShowRollOver(false);
					deleteBtn.setLayoutAlign(Alignment.CENTER);
					//deleteBtn.setSrc("icon_delete.png");
					deleteBtn.setSrc("remove.png");
					deleteBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_removeFilterSettFromList"));
					deleteBtn.setHeight(16);
					deleteBtn.setWidth(16);
					deleteBtn.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							SC.confirm(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_deleteConfirm"),
									AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_deleteConfComment1") + record.getAttribute("name")
									+ AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_deleteConfComment2")
									, new BooleanCallback() {

								@Override
								public void execute(Boolean value) {
									if (value) {
										itemGrid.removeData(record);
									}

								}
							});
						}
					});

					return deleteBtn;
				}
				return null;
			}

		};
		itemGrid.setEmptyMessage("no message item");
		itemGrid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される
		itemGrid.setShowHeader(false);
		itemGrid.setBorder("none");				//外のSectionと線がかぶるので消す

		ListGridField namedField = new ListGridField(DataSourceConstants.FIELD_NAME, AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_name"));

		ListGridField editField = new ListGridField("editButton");
		editField.setWidth(25);

		ListGridField deleteField = new ListGridField("deleteButton");
		deleteField.setWidth(25);

		itemGrid.setFields(namedField, editField, deleteField);
		itemGrid.setSortField(DataSourceConstants.FIELD_NAME);			//MapなのでIDでソート

		//この２つを指定することでcreateRecordComponentが有効
		itemGrid.setShowRecordComponents(true);
		itemGrid.setShowRecordComponentsByCell(true);

		EntityFilterItemDS dataSource = EntityFilterItemDS.getInstance(defName);
		itemGrid.setDataSource(dataSource);
		itemGrid.setAutoFetchData(true);

		//一覧ダブルクリック処理
		itemGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				//初期化
				EntityFilterItem item = (EntityFilterItem) event.getRecord().getAttributeAsObject("valueObject");
				setEntityFilterItem(item, event.getRecordNum());
			}
		});

		filterListSection.addItem(itemGrid);
		filterListStack.addSection(filterListSection);

		filterListStack.expandSection(0);

		setEntityFilterItem(null, 0);
	}

	private void setEntityFilterItem(EntityFilterItem item, int row) {
		selectedIndex = row;

		if (item == null) {
			nameField.setValue("");
			displayNameField.setValue("");
			conditionField.setValue("");
			sortField.setValue("");
			displayNameField.setDisabled(true);
			langBtn.setDisabled(true);
			conditionField.setDisabled(true);
			sortField.setDisabled(true);

			btnSave.setDisabled(true);
			btnExec.setDisabled(true);
			btnClear.setDisabled(true);
		} else {
			nameField.setValue(item.getName());
			displayNameField.setValue(item.getDisplayName());
			conditionField.setValue(item.getCondition());
			sortField.setValue(item.getSort());
			displayNameField.setDisabled(false);
			langBtn.setDisabled(false);
			localizedDisplayNameList = item.getLocalizedDisplayNameList();
			conditionField.setDisabled(false);
			sortField.setDisabled(false);

			btnSave.setDisabled(false);
			btnExec.setDisabled(false);
			btnClear.setDisabled(false);
		}
	}

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			boolean commonValidate = commonSection.validate();

			if (!commonValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_saveFilter") +
					AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_saveFilterCaution") , new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						if (ef == null) {
							ef = new EntityFilter();
							ef.setName(commonSection.getName());
							ef.setDisplayName(commonSection.getDisplayName());
							ef.setDescription(commonSection.getDescription());
							ef.setDefinitionName(defName);
							getItems();
							createEntityFilter(ef);
						} else {
							ef.setName(commonSection.getName());
							ef.setDisplayName(commonSection.getDisplayName());
							ef.setDescription(commonSection.getDescription());
							getItems();
							updateEntityFilter(ef, true);
						}
					}
				}
			});
		}
	}

	private void createEntityFilter(final EntityFilter definition) {
		SmartGWTUtil.showSaveProgress();
		service.createDefinition(TenantInfoHolder.getId(), definition, new MetaDataUpdateCallback(){

			@Override
			protected void overwriteUpdate() {
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_completion"),
						AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_saveFilterComp"));
				initializeData();
				commonSection.refreshSharedConfig();
			}
		});

	}

	private void updateEntityFilter(final EntityFilter definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), ef, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateEntityFilter(definition, false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_completion"),
						AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_saveFilterComp"));
				initializeData();
				commonSection.refreshSharedConfig();
			}
		});
	}

	private void getItems() {
		ef.getItems().clear();
		ListGridRecord[] records = itemGrid.getRecords();
		for (ListGridRecord record : records) {
			EntityFilterItem item = (EntityFilterItem) record.getAttributeAsObject("valueObject");
			if (item.getCondition() != null && !item.getCondition().isEmpty()) {
				ef.addItem(item);
			}
		}
	}

	/**
	 * キャンセルボタンイベント
	 */
	private final class CancelClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_entity_filter_FilterEditPane_cancelConfirmComment")
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
