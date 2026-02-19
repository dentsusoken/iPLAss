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

package org.iplass.adminconsole.client.metadata.ui.oauth.resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.auth.oauth.definition.CustomTokenIntrospectorDefinition;
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinition;
import org.iplass.mtp.auth.oauth.definition.introspectors.JavaClassCustomTokenIntrospectorDefinition;
import org.iplass.mtp.auth.oauth.definition.introspectors.ScriptingCustomTokenIntrospectorDefinition;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class CustomTokenIntrospectorGridPane extends VLayout implements EditablePane<OAuthResourceServerDefinition> {

	private CustomTokenIntrospectorGrid grid;

	public CustomTokenIntrospectorGridPane() {
		setAutoHeight();
		setWidth100();

		grid = new CustomTokenIntrospectorGrid();

		IButton btnAdd = new IButton("Add");
		btnAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.addCustomTokenIntrospector();
			}
		});

		IButton btnDel = new IButton("Remove");
		btnDel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.removeCustomTokenIntrospector();
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
	public void setDefinition(OAuthResourceServerDefinition definition) {
		grid.setDefinition(definition);
	}

	@Override
	public OAuthResourceServerDefinition getEditDefinition(OAuthResourceServerDefinition definition) {
		return grid.getEditDefinition(definition);
	}

	@Override
	public boolean validate() {
		return grid.validate();
	}

	@Override
	public void clearErrors() {
	}

	private static class CustomTokenIntrospectorGrid extends ListGrid implements EditablePane<OAuthResourceServerDefinition> {

		private enum FIELD_NAME {
			TYPE,
			VALUE_OBJECT,
		}

		public CustomTokenIntrospectorGrid() {

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

			ListGridField clientTypeField = new ListGridField(FIELD_NAME.TYPE.name(), AdminClientMessageUtil.getString("ui_metadata_ui_oauth_resource_CustomTokenIntrospectorGridPane_type"));

			setFields(clientTypeField);

			// レコード編集イベント設定
			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					editCustomTokenIntrospector((ListGridRecord)event.getRecord());

				}
			});

		}

		@Override
		public void setDefinition(OAuthResourceServerDefinition definition) {

			setData(new ListGridRecord[]{});

			if (definition.getCustomTokenIntrospectors() != null) {
				List<ListGridRecord> records = new ArrayList<ListGridRecord>();
				for (CustomTokenIntrospectorDefinition introspector : definition.getCustomTokenIntrospectors()) {
					records.add(createRecord(introspector, null));
				}
				setData(records.toArray(new ListGridRecord[]{}));
			}
		}

		@Override
		public OAuthResourceServerDefinition getEditDefinition(OAuthResourceServerDefinition definition) {

			ListGridRecord[] records = getRecords();
			if (records == null || records.length == 0) {
				definition.setCustomTokenIntrospectors(null);
			} else {
				List<CustomTokenIntrospectorDefinition> introspectors = new ArrayList<CustomTokenIntrospectorDefinition>(records.length);
				for (ListGridRecord record : records) {
					CustomTokenIntrospectorDefinition introspector = (CustomTokenIntrospectorDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
					introspectors.add(introspector);
				}
				definition.setCustomTokenIntrospectors(introspectors);
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

		public void addCustomTokenIntrospector() {
			editCustomTokenIntrospector(null);
		}

		public void removeCustomTokenIntrospector() {
			removeSelectedData();
		}

		private void editCustomTokenIntrospector(final ListGridRecord record) {
			final CustomTokenIntrospectorEditDialog dialog = new CustomTokenIntrospectorEditDialog();
			dialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {
					CustomTokenIntrospectorDefinition introspector = event.getValueObject(CustomTokenIntrospectorDefinition.class);
					ListGridRecord newRecord = createRecord(introspector, record);
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
				dialog.setDefinition((CustomTokenIntrospectorDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
			}
			dialog.show();
		}

		private ListGridRecord createRecord(CustomTokenIntrospectorDefinition introspector, ListGridRecord record) {
			if (record == null) {
				record = new ListGridRecord();
			}
			if (introspector instanceof JavaClassCustomTokenIntrospectorDefinition) {
				record.setAttribute(FIELD_NAME.TYPE.name(), "Java");
			} else if (introspector instanceof ScriptingCustomTokenIntrospectorDefinition) {
				record.setAttribute(FIELD_NAME.TYPE.name(), "Script");
			}
			record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), introspector);
			return record;
		}

	}

	private static class CustomTokenIntrospectorEditDialog extends MtpDialog {

		/** 種類選択用Map */
		private static LinkedHashMap<String, String> typeMap;
		static {
			typeMap = new LinkedHashMap<String, String>();
			typeMap.put("Java", "Java");
			typeMap.put("Script", "Script");
		}

		private DynamicForm form;

		private SelectItem selType;

		private DynamicForm javaForm;
		private TextItem txtClassName;

		private DynamicForm scriptForm;
		private TextAreaItem txaScript;

		private DynamicForm currentForm;

		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		public CustomTokenIntrospectorEditDialog() {

			setHeight(450);
			setTitle("Custom Token Introspector");
			centerInPage();

			form = new MtpForm();

			selType = new MtpSelectItem();
			selType.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_CustomTokenIntrospectorGridPane_type"));
			selType.setValueMap(typeMap);
			SmartGWTUtil.setRequired(selType);
			selType.setRequired(true);
			selType.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					typeChanged();
				}
			});

			form.setItems(selType);

			javaForm = new MtpForm();

			txtClassName = new MtpTextItem();
			txtClassName.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_CustomTokenIntrospectorGridPane_javaClassName"));
			txtClassName.setStartRow(true);
			SmartGWTUtil.addHoverToFormItem(txtClassName,
					SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_OAuthResourceServerAttributePane_introspectorJavaClassName")));

			javaForm.setItems(txtClassName);

			scriptForm = new MtpForm();

			txaScript = new MtpTextAreaItem();
			txaScript.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_CustomTokenIntrospectorGridPane_script"));
			txaScript.setHeight(255);
			SmartGWTUtil.setReadOnlyTextArea(txaScript);

			SpacerItem spacer = new SpacerItem();
			spacer.setStartRow(true);

			ButtonItem btnScript = new ButtonItem();
			btnScript.setTitle(AdminClientMessageUtil.getString("ui_metadata_oauth_resource_CustomTokenIntrospectorGridPane_editScript"));
			btnScript.setShowTitle(false);
			btnScript.setStartRow(false);
			btnScript.setEndRow(false);
			btnScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

					MetaDataUtil.showScriptEditDialog(
							ScriptEditorDialogMode.GROOVY_SCRIPT,
							SmartGWTUtil.getStringValue(txaScript, true),
							"Custom Token Introspector Script",
							"ui_metadata_oauth_resource_OAuthResourceServerAttributePane_introspectorScriptHint",
							null,
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									txaScript.setValue(text);
								}
								@Override
								public void onCancel() {
								}
							});

				}
			});

			scriptForm.setItems(txaScript, spacer, btnScript);

			container.addMember(form);

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

		public void setDefinition(CustomTokenIntrospectorDefinition definition) {

			if (definition != null) {
				if (definition instanceof JavaClassCustomTokenIntrospectorDefinition) {
					JavaClassCustomTokenIntrospectorDefinition javaIntrospector = (JavaClassCustomTokenIntrospectorDefinition)definition;
					selType.setValue("Java");
					txtClassName.setValue(javaIntrospector.getClassName());
				} else if (definition instanceof ScriptingCustomTokenIntrospectorDefinition) {
					ScriptingCustomTokenIntrospectorDefinition scriptIntrospector = (ScriptingCustomTokenIntrospectorDefinition)definition;
					selType.setValue("Script");
					txaScript.setValue(scriptIntrospector.getScript());
				}
			} else {
				selType.setValue("");
			}
			typeChanged();
		}

		public void addDataChangeHandler(DataChangedHandler handler) {
			handlers.add(0, handler);
		}

		private void typeChanged() {

			if (currentForm != null) {
				if (container.contains(currentForm)) {
					container.removeMember(currentForm);
				}
				currentForm = null;
			}

			String type = SmartGWTUtil.getStringValue(selType, true);
			if (type == null) {
				return;
			}
			if ("Java".equals(type)) {
				container.addMember(javaForm);
				currentForm = javaForm;
			} else if ("Script".equals(type)) {
				container.addMember(scriptForm);
				currentForm = scriptForm;
			}
		}

		private void createEditDefinition() {

			String type = SmartGWTUtil.getStringValue(selType, true);
			if (type == null) {
				return;
			}

			if ("Java".equals(type)) {
				JavaClassCustomTokenIntrospectorDefinition definition = new JavaClassCustomTokenIntrospectorDefinition();
				definition.setClassName(SmartGWTUtil.getStringValue(txtClassName));
				fireDataChanged(definition);
			} else if ("Script".equals(type)) {
				ScriptingCustomTokenIntrospectorDefinition definition = new ScriptingCustomTokenIntrospectorDefinition();
				definition.setScript(SmartGWTUtil.getStringValue(txaScript, true));
				fireDataChanged(definition);
			}

			destroy();
		}

		private void fireDataChanged(CustomTokenIntrospectorDefinition definition) {
			DataChangedEvent event = new DataChangedEvent();
			event.setValueObject(definition);
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}
	}

}
