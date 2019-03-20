/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.oauth.authorize;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.CommonIconConstants;
import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog.LocalizedStringSettingDialogOption;
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition;
import org.iplass.mtp.auth.oauth.definition.ScopeDefinition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ScopeGridPane extends VLayout implements EditablePane<OAuthAuthorizationDefinition> {

	private ScopeGrid grid;

	public ScopeGridPane() {
		setAutoHeight();
		setWidth100();

		grid = new ScopeGrid();

		IButton btnAdd = new IButton("Add");
		btnAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.addScope();
			}
		});

		IButton btnDel = new IButton("Remove");
		btnDel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.removeScope();
			}
		});

		HLayout buttonPane = new HLayout(5);
		buttonPane.setMargin(5);
		buttonPane.addMember(btnAdd);
		buttonPane.addMember(btnDel);

		addMember(grid);
		addMember(buttonPane);
	}

	@Override
	public void setDefinition(OAuthAuthorizationDefinition definition) {
		grid.setDefinition(definition);
	}

	@Override
	public OAuthAuthorizationDefinition getEditDefinition(OAuthAuthorizationDefinition definition) {
		return grid.getEditDefinition(definition);
	}

	@Override
	public boolean validate() {
		return grid.validate();
	}

	private static class ScopeGrid extends ListGrid implements EditablePane<OAuthAuthorizationDefinition> {

		private enum FIELD_NAME {
			NAME,
			DISPLAY_NAME,
			VALUE_OBJECT,
		}

		public ScopeGrid() {

			setWidth100();
			setHeight(1);

			setShowAllColumns(true);							//列を全て表示
			setShowAllRecords(true);							//レコードを全て表示
			setCanResizeFields(true);							//列幅変更可能
			setCanSort(false);									//ソート不可
			setCanPickFields(false);							//表示フィールドの選択不可
			setCanGroupBy(false);								//GroupByの選択不可
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			setLeaveScrollbarGap(false);						//縦スクロールバー自動表示制御
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			setCanReorderRecords(true);							//Dragによる並び替えを可能にする

			ListGridField nameField = new ListGridField(FIELD_NAME.NAME.name(), "Name");
			ListGridField dispNameField = new ListGridField(FIELD_NAME.DISPLAY_NAME.name(), "Display Name");;

			setFields(nameField, dispNameField);

			// レコード編集イベント設定
			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					editScope((ListGridRecord)event.getRecord());

				}
			});

		}

		@Override
		public void setDefinition(OAuthAuthorizationDefinition definition) {

			setData(new ListGridRecord[]{});

			if (definition.getScopes() != null) {
				List<ListGridRecord> records = new ArrayList<ListGridRecord>();
				for (ScopeDefinition scope : definition.getScopes()) {
					records.add(createRecord(scope, null));
				}
				setData(records.toArray(new ListGridRecord[]{}));
			}
		}

		@Override
		public OAuthAuthorizationDefinition getEditDefinition(OAuthAuthorizationDefinition definition) {

			ListGridRecord[] records = getRecords();
			if (records == null || records.length == 0) {
				definition.setScopes(null);
			} else {
				List<ScopeDefinition> scopes = new ArrayList<ScopeDefinition>(records.length);
				for (ListGridRecord record : records) {
					ScopeDefinition scope = (ScopeDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
					scopes.add(scope);
				}
				definition.setScopes(scopes);
			}
			return definition;
		}

		@Override
		public boolean validate() {
			return true;
		}

		public void addScope() {
			editScope(null);
		}

		public void removeScope() {
			removeSelectedData();
		}

		private void editScope(final ListGridRecord record) {
			final ScopeEditDialog dialog = new ScopeEditDialog();
			dialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					ScopeDefinition scope = event.getValueObject(ScopeDefinition.class);
					ListGridRecord newRecord = createRecord(scope, record);
					if (record != null) {
						updateData(newRecord);
					} else {
						//追加
						addData(newRecord);
					}
					refreshFields();
				}
			});

			if (record != null) {
				dialog.setDefinition((ScopeDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
			}
			dialog.show();
		}

		private ListGridRecord createRecord(ScopeDefinition scope, ListGridRecord record) {
			if (record == null) {
				record = new ListGridRecord();
			}
			record.setAttribute(FIELD_NAME.NAME.name(), scope.getName());
			record.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), scope.getDisplayName());
			record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), scope);
			return record;
		}

	}

	private static class ScopeEditDialog extends AbstractWindow {

		private DynamicForm form;

		private TextItem txtName;
		private MetaDataLangTextItem txtDisplayName;
		private TextAreaItem txaDescription;

		private List<LocalizedStringDefinition> localizedDescriptionList;

		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		public ScopeEditDialog() {

			setHeight(250);
			setWidth(500);
			setTitle("Scope");

			setShowMinimizeButton(false);
			setShowMaximizeButton(false);
			setCanDragResize(true);
			setIsModal(true);
			setShowModalMask(true);

			centerInPage();

			form = new DynamicForm();
			form.setWidth100();
			form.setNumCols(3);	//間延びしないように最後に１つ余分に作成
			form.setColWidths(100, 300, "*");
			form.setMargin(5);

			txtName = new TextItem();
			txtName.setTitle("Name");
			txtName.setWidth("100%");
			txtName.setBrowserSpellCheck(false);
			SmartGWTUtil.setRequired(txtName);
			txtName.setRequired(true);	//TODO 直接指定しないと効かない

			txtDisplayName = new MetaDataLangTextItem();
			txtDisplayName.setTitle("Display Name");
			txtDisplayName.setWidth("100%");
			txtDisplayName.setBrowserSpellCheck(false);

			txaDescription = new TextAreaItem();
			txaDescription.setTitle("Description");
			txaDescription.setWidth("100%");
			txaDescription.setHeight(55);
			txaDescription.setBrowserSpellCheck(false);

			SpacerItem spacer = new SpacerItem();
			spacer.setStartRow(true);

			ButtonItem btbDescLang = new ButtonItem();
			btbDescLang.setTitle("Description Lang");
			btbDescLang.setIcon(CommonIconConstants.COMMON_ICON_LANG);
			btbDescLang.setShowTitle(false);
			btbDescLang.setStartRow(false);
			btbDescLang.setEndRow(false);
			btbDescLang.setPrompt(SmartGWTUtil.getHoverString(
					AdminClientMessageUtil.getString("ui_metadata_oauth_authorize_ScopeGridPane_eachLangDescription")));
			btbDescLang.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

					if (localizedDescriptionList == null) {
						localizedDescriptionList = new ArrayList<LocalizedStringDefinition>();
					}
					LocalizedStringSettingDialogOption option = new LocalizedStringSettingDialogOption();
					option.setModeTextArea(true);
					LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedDescriptionList, option);
					dialog.show();

				}
			});

			form.setItems(txtName, txtDisplayName, txaDescription, spacer, btbDescLang);

			VLayout contents = new VLayout(5);
			contents.setHeight100();
			contents.setOverflow(Overflow.AUTO);
			contents.setMembers(form);

			HLayout footer = new HLayout(5);
			footer.setMargin(10);
			footer.setAutoHeight();
			footer.setWidth100();
			footer.setAlign(VerticalAlignment.CENTER);
			footer.setOverflow(Overflow.VISIBLE);

			IButton btnOK = new IButton("OK");
			btnOK.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (form.validate()){
						createEditDefinition();
					}
				}
			});

			IButton btnCancel = new IButton("Cancel");
			btnCancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(btnOK, btnCancel);

			addItem(contents);
			addItem(SmartGWTUtil.separator());
			addItem(footer);

		}

		public void setDefinition(ScopeDefinition definition) {

			txtName.setValue(definition.getName());
			txtDisplayName.setValue(definition.getDisplayName());
			txtDisplayName.setLocalizedList(definition.getLocalizedDisplayNameList());
			txaDescription.setValue(definition.getDescription());
			localizedDescriptionList = definition.getLocalizedDescriptionList();
		}

		public void addDataChangeHandler(DataChangedHandler handler) {
			handlers.add(0, handler);
		}

		private void createEditDefinition() {

			ScopeDefinition definition = new ScopeDefinition();
			definition.setName(SmartGWTUtil.getStringValue(txtName, true));
			definition.setDisplayName(SmartGWTUtil.getStringValue(txtDisplayName, true));
			definition.setLocalizedDisplayNameList(txtDisplayName.getLocalizedList());
			definition.setDescription(SmartGWTUtil.getStringValue(txaDescription, true));
			if (localizedDescriptionList != null && !localizedDescriptionList.isEmpty()) {
				definition.setLocalizedDescriptionList(localizedDescriptionList);
			} else {
				definition.setLocalizedDescriptionList(null);
			}

			fireDataChanged(definition);

			destroy();
		}

		private void fireDataChanged(ScopeDefinition definition) {
			DataChangedEvent event = new DataChangedEvent();
			event.setValueObject(definition);
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}
	}

}
