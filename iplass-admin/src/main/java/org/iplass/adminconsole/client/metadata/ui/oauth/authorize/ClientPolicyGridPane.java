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
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.auth.oauth.definition.ClientPolicyDefinition;
import org.iplass.mtp.auth.oauth.definition.ClientType;
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.AlwaysConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.OnceConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.ScriptingConsentTypeDefinition;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ClientPolicyGridPane extends VLayout implements EditablePane<OAuthAuthorizationDefinition> {

	private ClientPolicyGrid grid;

	public ClientPolicyGridPane() {
		setAutoHeight();
		setWidth100();

		grid = new ClientPolicyGrid();

		IButton btnAdd = new IButton("Add");
		btnAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.addClientPolicy();
			}
		});

		IButton btnDel = new IButton("Remove");
		btnDel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.removeClientPolicy();
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

	private static class ClientPolicyGrid extends ListGrid implements EditablePane<OAuthAuthorizationDefinition> {

		private enum FIELD_NAME {
			CLIENT_TYPE,
			VALUE_OBJECT,
		}

		public ClientPolicyGrid() {

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

			ListGridField clientTypeField = new ListGridField(FIELD_NAME.CLIENT_TYPE.name(), "Client Type");

			setFields(clientTypeField);

			// レコード編集イベント設定
			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					editClientPolicy((ListGridRecord)event.getRecord());

				}
			});

		}

		@Override
		public void setDefinition(OAuthAuthorizationDefinition definition) {

			setData(new ListGridRecord[]{});

			if (definition.getClientPolicies() != null) {
				List<ListGridRecord> records = new ArrayList<ListGridRecord>();
				for (ClientPolicyDefinition policy : definition.getClientPolicies()) {
					records.add(createRecord(policy, null));
				}
				setData(records.toArray(new ListGridRecord[]{}));
			}
		}

		@Override
		public OAuthAuthorizationDefinition getEditDefinition(OAuthAuthorizationDefinition definition) {

			ListGridRecord[] records = getRecords();
			if (records == null || records.length == 0) {
				definition.setClientPolicies(null);
			} else {
				List<ClientPolicyDefinition> policies = new ArrayList<ClientPolicyDefinition>(records.length);
				for (ListGridRecord record : records) {
					ClientPolicyDefinition policy = (ClientPolicyDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
					policies.add(policy);
				}
				definition.setClientPolicies(policies);
			}
			return definition;
		}

		@Override
		public boolean validate() {
			return true;
		}

		public void addClientPolicy() {
			editClientPolicy(null);
		}

		public void removeClientPolicy() {
			removeSelectedData();
		}

		private void editClientPolicy(final ListGridRecord record) {
			final ClientPolicyEditDialog dialog = new ClientPolicyEditDialog();
			dialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					ClientPolicyDefinition policy = event.getValueObject(ClientPolicyDefinition.class);
					ListGridRecord newRecord = createRecord(policy, record);
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
				dialog.setDefinition((ClientPolicyDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
			}
			dialog.show();
		}

		private ListGridRecord createRecord(ClientPolicyDefinition policy, ListGridRecord record) {
			if (record == null) {
				record = new ListGridRecord();
			}
			record.setAttribute(FIELD_NAME.CLIENT_TYPE.name(), policy.getClientType() != null ? policy.getClientType().name() : "");
			record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), policy);
			return record;
		}

	}

	private static class ClientPolicyEditDialog extends AbstractWindow {

		/** ClientTypeの種類選択用Map */
		private static LinkedHashMap<String, String> clientTypeMap;
		static {
			clientTypeMap = new LinkedHashMap<String, String>();
			clientTypeMap.put("", "");
			for (ClientType type : ClientType.values()) {
				clientTypeMap.put(type.name(), type.name());
			}
		}

		private DynamicForm form;

		private SelectItem selClientType;

		private IntegerItem txtAccessTokenLifetimeSeconds;

		private CheckboxItem chkSupportRefreshToken;

		private IntegerItem txtRefreshTokenLifetimeSeconds;

		private ConsentTypeEditPane pnlConsentType;

		private ScopeGridPane pnlScopeGrid;

		private CheckboxItem chkSupportOpenIDConnect;

		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		public ClientPolicyEditDialog() {

			setHeight(450);
			setWidth(600);
			setTitle("Client Policy");

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

			selClientType = new SelectItem();
			selClientType.setTitle("Client Type");
			selClientType.setWidth("100%");
			selClientType.setValueMap(clientTypeMap);

			txtAccessTokenLifetimeSeconds = new IntegerItem();
			txtAccessTokenLifetimeSeconds.setTitle("Access Token Lifetime Seconds");
			txtAccessTokenLifetimeSeconds.setWidth("100%");
			txtAccessTokenLifetimeSeconds.setBrowserSpellCheck(false);

			chkSupportRefreshToken = new CheckboxItem();
			chkSupportRefreshToken.setTitle("Support Refresh Token");

			txtRefreshTokenLifetimeSeconds = new IntegerItem();
			txtRefreshTokenLifetimeSeconds.setTitle("Refresh Token Lifetime Seconds");
			txtRefreshTokenLifetimeSeconds.setWidth("100%");
			txtRefreshTokenLifetimeSeconds.setBrowserSpellCheck(false);

			pnlConsentType = new ConsentTypeEditPane();
			CanvasItem canvasConsentType = new CanvasItem();
			canvasConsentType.setTitle("Consent Type");
			canvasConsentType.setCanvas(pnlConsentType);
			canvasConsentType.setColSpan(2);
			canvasConsentType.setStartRow(true);

			pnlScopeGrid = new ScopeGridPane();
			CanvasItem canvasScopes = new CanvasItem();
			canvasScopes.setTitle("Scopes");
			canvasScopes.setCanvas(pnlScopeGrid);
			canvasScopes.setColSpan(2);
			canvasScopes.setStartRow(true);

			chkSupportOpenIDConnect = new CheckboxItem();
			chkSupportOpenIDConnect.setTitle("Support OpenID Connect");

			form.setItems(selClientType, txtAccessTokenLifetimeSeconds, chkSupportRefreshToken,
					txtRefreshTokenLifetimeSeconds, canvasConsentType, canvasScopes, chkSupportOpenIDConnect);

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

		public void setDefinition(ClientPolicyDefinition definition) {

			if (definition.getClientType() != null) {
				selClientType.setValue(definition.getClientType().name());
			} else {
				selClientType.setValue("");
			}

			txtAccessTokenLifetimeSeconds.setValue(definition.getAccessTokenLifetimeSeconds());
			chkSupportRefreshToken.setValue(definition.isSupportRefreshToken());
			txtRefreshTokenLifetimeSeconds.setValue(definition.getRefreshTokenLifetimeSeconds());

			pnlConsentType.setDefinition(definition);

			pnlScopeGrid.setDefinition(definition);

			chkSupportOpenIDConnect.setValue(definition.isSupportOpenIDConnect());

		}

		public void addDataChangeHandler(DataChangedHandler handler) {
			handlers.add(0, handler);
		}

		private void createEditDefinition() {

			ClientPolicyDefinition definition = new ClientPolicyDefinition();

			String clientType = SmartGWTUtil.getStringValue(selClientType, true);
			if (clientType != null) {
				definition.setClientType(ClientType.valueOf(clientType));
			} else {
				definition.setClientType(null);
			}

			Long accessTokenLifetimeSeconds = SmartGWTUtil.getLongValue(txtAccessTokenLifetimeSeconds);
			if (accessTokenLifetimeSeconds != null) {
				definition.setAccessTokenLifetimeSeconds(accessTokenLifetimeSeconds);
			}
			definition.setSupportRefreshToken(SmartGWTUtil.getBooleanValue(chkSupportRefreshToken));
			Long refreshTokenLifetimeSeconds = SmartGWTUtil.getLongValue(txtRefreshTokenLifetimeSeconds);
			if (refreshTokenLifetimeSeconds != null) {
				definition.setRefreshTokenLifetimeSeconds(refreshTokenLifetimeSeconds);
			}

			pnlConsentType.getEditDefinition(definition);

			pnlScopeGrid.getEditDefinition(definition);

			definition.setSupportOpenIDConnect(SmartGWTUtil.getBooleanValue(chkSupportOpenIDConnect));

			fireDataChanged(definition);

			destroy();
		}

		private void fireDataChanged(ClientPolicyDefinition definition) {
			DataChangedEvent event = new DataChangedEvent();
			event.setValueObject(definition);
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}
	}

	private static class ScopeGridPane extends VLayout implements EditablePane<ClientPolicyDefinition> {

		private ListGrid grid;

		public ScopeGridPane() {
			setAutoHeight();
			setWidth100();

			grid = new ListGrid();
			grid.setWidth100();
			grid.setHeight(1);

			grid.setShowAllColumns(true);								//列を全て表示
			grid.setShowAllRecords(true);								//レコードを全て表示
			grid.setCanResizeFields(false);								//列幅変更可能
			grid.setCanSort(false);										//ソート不可
			grid.setCanPickFields(false);								//表示フィールドの選択不可
			grid.setCanGroupBy(false);									//GroupByの選択不可
			grid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			grid.setLeaveScrollbarGap(false);							//縦スクロールバー自動表示制御
			grid.setBodyOverflow(Overflow.VISIBLE);
			grid.setOverflow(Overflow.VISIBLE);
			grid.setCanReorderRecords(true);							//Dragによる並び替えを可能にする

			grid.setCanEdit(true);
			grid.setEditEvent(ListGridEditEvent.DOUBLECLICK);

			ListGridField scopeField = new ListGridField("scope", "Scope");
			grid.setFields(scopeField);

			IButton btnAdd = new IButton("Add");
			btnAdd.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					grid.startEditingNew();
				}
			});

			IButton btnDel = new IButton("Remove");
			btnDel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					grid.removeSelectedData();
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
		public void setDefinition(ClientPolicyDefinition definition) {

			if (definition.getScopes() != null) {

				ListGridRecord[] records = new ListGridRecord[definition.getScopes().size()];

				int cnt = 0;
				for (String scope : definition.getScopes()) {
					ListGridRecord record = new ListGridRecord();
					record.setAttribute("scope",scope);
					records[cnt] = record;
					cnt++;
				}
				grid.setData(records);
			}
		}

		@Override
		public ClientPolicyDefinition getEditDefinition(ClientPolicyDefinition definition) {

			ListGridRecord[] records = grid.getRecords();
			if (records == null || records.length == 0) {
				definition.setScopes(null);
			} else {
				List<String> scopes = new ArrayList<String>(records.length);
				for (ListGridRecord record : records) {
					String scope = record.getAttribute("scope");
					if (scope != null && !scope.trim().isEmpty()) {
						scopes.add(scope);
					}
				}
				definition.setScopes(scopes);
			}
			return definition;
		}

		@Override
		public boolean validate() {
			return true;
		}

	}

	private static class ConsentTypeEditPane extends VLayout implements EditablePane<ClientPolicyDefinition> {

		/** ConsentTypeの種類選択用Map */
		private static LinkedHashMap<String, String> consentTypeMap;
		static {
			consentTypeMap = new LinkedHashMap<String, String>();
			consentTypeMap.put("", "");
			consentTypeMap.put(AlwaysConsentTypeDefinition.class.getName(), "Always");
			consentTypeMap.put(OnceConsentTypeDefinition.class.getName(), "Once");
			consentTypeMap.put(ScriptingConsentTypeDefinition.class.getName(), "Scripting");
		}

		private DynamicForm form;

		private SelectItem selConsentType;

		private IButton btnScript;

		private String script;

		public ConsentTypeEditPane() {

			setAutoHeight();
			setWidth100();

			form = new DynamicForm();
			form.setWidth100();
			form.setNumCols(2);
			form.setColWidths(300, "*");

			selConsentType = new SelectItem();
			selConsentType.setShowTitle(false);
			selConsentType.setWidth("100%");
			selConsentType.setValueMap(consentTypeMap);
			selConsentType.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					typeChanged();
				}
			});

			form.setItems(selConsentType);

			btnScript = new IButton();
			btnScript.setTitle("Script");
			btnScript.setVisible(false);
			btnScript.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(
							ScriptEditorDialogMode.GROOVY_SCRIPT,
							script,
							"Consent Type Script",
							"ui_metadata_oauth_authorize_ClientPolicyGridPane_scriptingConsentTypeHint",
							null,
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									script = text;
									scriptChanged();
								}
								@Override
								public void onCancel() {
								}
							});
				}
			});

			addMember(form);
			addMember(btnScript);
		}

		@Override
		public void setDefinition(ClientPolicyDefinition definition) {

			script = null;
			if (definition.getConsentType() != null) {
				String value = consentTypeMap.get(definition.getConsentType().getClass().getName());
				if (value != null) {
					selConsentType.setValue(definition.getConsentType().getClass().getName());

					if (definition.getConsentType() instanceof ScriptingConsentTypeDefinition) {
						script = ((ScriptingConsentTypeDefinition)definition.getConsentType()).getScript();
					}
				} else {
					selConsentType.setValue("");
				}
			} else {
				selConsentType.setValue("");
			}

			typeChanged();
		}

		@Override
		public ClientPolicyDefinition getEditDefinition(ClientPolicyDefinition definition) {

			String type = SmartGWTUtil.getStringValue(selConsentType, true);
			if (type != null) {
				if (type.equals(AlwaysConsentTypeDefinition.class.getName())) {
					AlwaysConsentTypeDefinition edit = new AlwaysConsentTypeDefinition();
					definition.setConsentType(edit);
				} else 	if (type.equals(OnceConsentTypeDefinition.class.getName())) {
					OnceConsentTypeDefinition edit = new OnceConsentTypeDefinition();
					definition.setConsentType(edit);
				} else 	if (type.equals(ScriptingConsentTypeDefinition.class.getName())) {
					ScriptingConsentTypeDefinition edit = new ScriptingConsentTypeDefinition();
					edit.setScript(script);
					definition.setConsentType(edit);
				}
			}
			return definition;
		}

		@Override
		public boolean validate() {
			return form.validate();
		}

		private void typeChanged() {

			String type = SmartGWTUtil.getStringValue(selConsentType, true);
			if (type != null) {
				if (type.equals(ScriptingConsentTypeDefinition.class.getName())) {
					scriptChanged();
					btnScript.setVisible(true);
					return;
				}
			}
			btnScript.setVisible(false);
		}

		private void scriptChanged() {
			if (script != null) {
				btnScript.setTitle("Script(*)");
			} else {
				btnScript.setTitle("Script");
			}
		}

	}

}
