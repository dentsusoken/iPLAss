/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog.LocalizedStringSettingDialogOption;
import org.iplass.mtp.auth.oauth.definition.ClaimMappingDefinition;
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition;
import org.iplass.mtp.auth.oauth.definition.OIDCClaimScopeDefinition;
import org.iplass.mtp.auth.oauth.definition.ScopeDefinition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
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

	@Override
	public void clearErrors() {
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

		@Override
		public void clearErrors() {
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

	private static class ScopeEditDialog extends MtpDialog implements MtpWidgetConstants {

		private DynamicForm form;

		private TextItem txtName;
		private MetaDataLangTextItem txtDisplayName;
		private TextAreaItem txaDescription;
		private ClaimMappingGridPane pnlClaimMappingGrid;

		private List<LocalizedStringDefinition> localizedDescriptionList;

		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		public ScopeEditDialog() {

			setHeight(450);
			setTitle("Scope");
			centerInPage();

			form = new MtpForm();

			txtName = new MtpTextItem();
			txtName.setTitle("Name");
			SmartGWTUtil.setRequired(txtName);
			txtName.setRequired(true);	//TODO 直接指定しないと効かない

			txtDisplayName = new MetaDataLangTextItem();
			txtDisplayName.setTitle("Display Name");

			txaDescription = new MtpTextAreaItem();
			txaDescription.setTitle("Description");
			txaDescription.setHeight(55);

			SpacerItem spacer = new SpacerItem();
			spacer.setStartRow(true);

			ButtonItem btbDescLang = new ButtonItem();
			btbDescLang.setTitle("Description Lang");
			btbDescLang.setIcon(ICON_LANG);
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

			pnlClaimMappingGrid = new ClaimMappingGridPane();
			CanvasItem canvasClaims = new CanvasItem();
			canvasClaims.setTitle("Claims");
			canvasClaims.setCanvas(pnlClaimMappingGrid);
			canvasClaims.setColSpan(2);
			canvasClaims.setStartRow(true);

			form.setItems(txtName, txtDisplayName, txaDescription, spacer, btbDescLang, canvasClaims);

			container.setMembers(form);

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
		}

		public void setDefinition(ScopeDefinition definition) {

			txtName.setValue(definition.getName());
			txtDisplayName.setValue(definition.getDisplayName());
			txtDisplayName.setLocalizedList(definition.getLocalizedDisplayNameList());
			txaDescription.setValue(definition.getDescription());
			localizedDescriptionList = definition.getLocalizedDescriptionList();

			pnlClaimMappingGrid.setDefinition(definition);
		}

		public void addDataChangeHandler(DataChangedHandler handler) {
			handlers.add(0, handler);
		}

		private void createEditDefinition() {

			ScopeDefinition definition = null;
			if (pnlClaimMappingGrid.isEmpty()) {
				definition = new ScopeDefinition();
			} else {
				definition = new OIDCClaimScopeDefinition();
				pnlClaimMappingGrid.getEditDefinition((OIDCClaimScopeDefinition)definition);
			}
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

	private static class ClaimMappingGridPane extends VLayout implements EditablePane<ScopeDefinition> {

		private ClaimMappingGrid grid;

		public ClaimMappingGridPane() {
			setAutoHeight();
			setWidth100();

			grid = new ClaimMappingGrid();

			IButton btnAdd = new IButton("Add");
			btnAdd.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					grid.addClaimMapping();
				}
			});

			IButton btnDel = new IButton("Remove");
			btnDel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					grid.removeClaimMapping();
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
		public void setDefinition(ScopeDefinition definition) {
			grid.setDefinition(definition);
		}

		@Override
		public ScopeDefinition getEditDefinition(ScopeDefinition definition) {
			return grid.getEditDefinition(definition);
		}

		@Override
		public boolean validate() {
			return grid.validate();
		}

		@Override
		public void clearErrors() {
		}

		public boolean isEmpty() {

			return grid.getRecordList().isEmpty();
		}

	}

	private static class ClaimMappingGrid extends ListGrid implements EditablePane<ScopeDefinition> {

		private enum FIELD_NAME {
			CLAIM_NAME,
			USER_PROPERTY_NAME,
			HAS_SCRIPT,
			VALUE_OBJECT,
		}

		public ClaimMappingGrid() {

			setWidth100();
			setHeight(1);

			setShowAllColumns(true);							//列を全て表示
			setShowAllRecords(true);							//レコードを全て表示
			setCanResizeFields(false);							//列幅変更可能
			setCanSort(false);									//ソート不可
			setCanPickFields(false);							//表示フィールドの選択不可
			setCanGroupBy(false);								//GroupByの選択不可
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			setLeaveScrollbarGap(false);						//縦スクロールバー自動表示制御
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			setCanReorderRecords(true);							//Dragによる並び替えを可能にする

			ListGridField claimNameField = new ListGridField(FIELD_NAME.CLAIM_NAME.name(), "Claim Name");
			ListGridField userPropertyNameField = new ListGridField(FIELD_NAME.USER_PROPERTY_NAME.name(), "User Property Name");
			ListGridField scriptField = new ListGridField(FIELD_NAME.HAS_SCRIPT.name(), "Script");
			scriptField.setWidth(50);

			setFields(claimNameField, userPropertyNameField, scriptField);

			// レコード編集イベント設定
			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					editClaimMapping((ListGridRecord)event.getRecord());

				}
			});

		}

		@Override
		public void setDefinition(ScopeDefinition definition) {

			setData(new ListGridRecord[]{});

			if (definition instanceof OIDCClaimScopeDefinition) {
				OIDCClaimScopeDefinition claimScope = (OIDCClaimScopeDefinition)definition;
				if (claimScope.getClaims() != null) {
					List<ListGridRecord> records = new ArrayList<ListGridRecord>();
					for (ClaimMappingDefinition claim : claimScope.getClaims()) {
						records.add(createRecord(claim, null));
					}
					setData(records.toArray(new ListGridRecord[]{}));
				}
			}
		}

		@Override
		public ScopeDefinition getEditDefinition(ScopeDefinition definition) {

			if (definition instanceof OIDCClaimScopeDefinition) {
				OIDCClaimScopeDefinition claimScope = (OIDCClaimScopeDefinition)definition;
				ListGridRecord[] records = getRecords();
				if (records == null || records.length == 0) {
					claimScope.setClaims(null);
				} else {
					List<ClaimMappingDefinition> claims = new ArrayList<ClaimMappingDefinition>(records.length);
					for (ListGridRecord record : records) {
						ClaimMappingDefinition claim = (ClaimMappingDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
						claims.add(claim);
					}
					claimScope.setClaims(claims);
				}
			}
			return definition;
		}

		@Override
		public boolean validate() {
			return true;
		}

		@Override
		public void clearErrors() {
		}

		public void addClaimMapping() {
			editClaimMapping(null);
		}

		public void removeClaimMapping() {
			removeSelectedData();
		}

		private void editClaimMapping(final ListGridRecord record) {
			final ClaimMappingEditDialog dialog = new ClaimMappingEditDialog();
			dialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					ClaimMappingDefinition claim = event.getValueObject(ClaimMappingDefinition.class);
					ListGridRecord newRecord = createRecord(claim, record);
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
				dialog.setDefinition((ClaimMappingDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
			}
			dialog.show();
		}

		private ListGridRecord createRecord(ClaimMappingDefinition claim, ListGridRecord record) {
			if (record == null) {
				record = new ListGridRecord();
			}
			record.setAttribute(FIELD_NAME.CLAIM_NAME.name(), claim.getClaimName());
			record.setAttribute(FIELD_NAME.USER_PROPERTY_NAME.name(), claim.getUserPropertyName());
			if (claim.getCustomValueScript() != null) {
				record.setAttribute(FIELD_NAME.HAS_SCRIPT.name(), "*");
			} else {
				record.setAttribute(FIELD_NAME.HAS_SCRIPT.name(), "");
			}
			record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), claim);
			return record;
		}

	}

	private static class ClaimMappingEditDialog extends MtpDialog {

		private DynamicForm form;

		private TextItem txtClaimName;
		private TextItem txtUserPropertyName;
		private TextAreaItem txaCustomValueScript;

		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		public ClaimMappingEditDialog() {

			setHeight(250);
			setTitle("Claim Mapping");
			centerInPage();

			form = new MtpForm();

			txtClaimName = new MtpTextItem();
			txtClaimName.setTitle("Claim Name");
			SmartGWTUtil.setRequired(txtClaimName);
			txtClaimName.setRequired(true);	//TODO 直接指定しないと効かない

			txtUserPropertyName = new MtpTextItem();
			txtUserPropertyName.setTitle("User Property Name");

			txaCustomValueScript = new MtpTextAreaItem();
			txaCustomValueScript.setTitle("Custom Value Script");
			txaCustomValueScript.setHeight(55);
			SmartGWTUtil.setReadOnlyTextArea(txaCustomValueScript);

			SpacerItem spacer = new SpacerItem();
			spacer.setStartRow(true);

			ButtonItem btnCustomValueScript = new ButtonItem();
			btnCustomValueScript.setTitle("Script");
			btnCustomValueScript.setShowTitle(false);
			btnCustomValueScript.setStartRow(false);
			btnCustomValueScript.setEndRow(false);
			btnCustomValueScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

					MetaDataUtil.showScriptEditDialog(
							ScriptEditorDialogMode.GROOVY_SCRIPT,
							SmartGWTUtil.getStringValue(txaCustomValueScript, true),
							"Custom Value Script",
							"ui_metadata_oauth_authorize_ScopeGridPane_customValueScriptHint",
							null,
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									txaCustomValueScript.setValue(text);
								}
								@Override
								public void onCancel() {
								}
							});

				}
			});

			form.setItems(txtClaimName, txtUserPropertyName, txaCustomValueScript, spacer, btnCustomValueScript);

			container.setMembers(form);

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
		}

		public void setDefinition(ClaimMappingDefinition definition) {

			txtClaimName.setValue(definition.getClaimName());
			txtUserPropertyName.setValue(definition.getUserPropertyName());
			txaCustomValueScript.setValue(definition.getCustomValueScript());
		}

		public void addDataChangeHandler(DataChangedHandler handler) {
			handlers.add(0, handler);
		}

		private void createEditDefinition() {

			ClaimMappingDefinition definition = new ClaimMappingDefinition();
			definition.setClaimName(SmartGWTUtil.getStringValue(txtClaimName, true));
			definition.setUserPropertyName(SmartGWTUtil.getStringValue(txtUserPropertyName, true));
			definition.setCustomValueScript(SmartGWTUtil.getStringValue(txaCustomValueScript, true));

			fireDataChanged(definition);

			destroy();
		}

		private void fireDataChanged(ClaimMappingDefinition definition) {
			DataChangedEvent event = new DataChangedEvent();
			event.setValueObject(definition);
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}
	}

}
