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

package org.iplass.adminconsole.client.metadata.ui.tenant;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataLangTextItem;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.tenant.TenantColType;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedScriptSettingDialog;
import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * テナントプロパティ編集ダイアログ
 *
 */
public class TenantPropertyEditDialog extends MtpDialog {

	/** 対象レコード */
	private Record record;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	/** 入力フィールド */
	private FormItem valueField;
	private FormItem[] valueFields;

	private List<LocalizedStringDefinition> localizedStringList;

	public TenantPropertyEditDialog(Record record) {

		this.record = record;

		setTitle(record.getAttribute("title"));

		final DynamicForm form = createForm(record);
		container.addMember(form);

		if (record.getAttribute("helpText") != null) {
			HLayout help = new HLayout(5);
			help.setHeight(20);
			help.setWidth100();
			help.setAlign(VerticalAlignment.CENTER);

			Label helpText = new Label(record.getAttribute("helpText"));
			helpText.setHeight(30);
			helpText.setPadding(10);
			helpText.setAlign(Alignment.LEFT);
			helpText.setIcon("information.png");
			helpText.setWrap(false);

			help.setMembers(helpText);

			container.addMember(help);
		}

		IButton ok = new IButton("OK");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()) {
					setValue();
				}
			}
		});

		IButton cancel = new IButton(
				AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPropertyEditDialog_cancel"));
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(ok, cancel);

		centerInPage();
	}

	/**
	 * DataChangeHandlerを追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	/**
	 * タイプ別の画面サイズを調整します。
	 */
	@SuppressWarnings("unchecked")
	private DynamicForm createForm(Record record) {

		final DynamicForm form = new MtpForm();
		form.setAutoFocus(true);

		TenantColType type = (TenantColType) record.getAttributeAsObject("colType");
		String name = record.getAttribute("name");
		String title = record.getAttribute("title");

		if (TenantColType.STRING.equals(type)) {
			setHeight(200);

			TextItem textItem = null;
			if ("displayName".equals(name) || "passwordPatternErrorMessage".equals(name)) {
				MetaDataLangTextItem langItem = new MetaDataLangTextItem();
				langItem.setName(name);
				langItem.setTitle(title);
				langItem.setValue(record.getAttributeAsString("value"));

				List<LocalizedStringDefinition> localizedStringList = (List<LocalizedStringDefinition>) JSOHelper
						.convertToJava((JavaScriptObject) record.getAttributeAsObject("localizedStringList"));
				langItem.setLocalizedList(localizedStringList);
				textItem = langItem;
			} else {
				textItem = new MtpTextItem(name, title);
				textItem.setValue(record.getAttributeAsString("value"));
			}
			form.setItems(textItem);
			valueField = textItem;
		} else if (TenantColType.INTEGER.equals(type)) {
			setHeight(200);

			IntegerItem textItem = new IntegerItem(name, title);
			textItem.setValue(record.getAttributeAsInt("value"));
			textItem.setWidth(100);

			// TODO Validatorのセット

			valueField = textItem;
			form.setItems(textItem);

		} else if (TenantColType.DATE.equals(type)) {
			setHeight(200);

			DateItem dateItem = SmartGWTUtil.createDateItem();
			dateItem.setName(name);
			dateItem.setTitle(title);
//			DateItem dateItem = new DateItem(name, title);
//			dateItem.setUseTextField(true);
//			dateItem.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
//			dateItem.setWidth(100);
			dateItem.setValue(record.getAttributeAsDate("value"));
			valueField = dateItem;
			form.setItems(dateItem);
		} else if (TenantColType.PASSWORD.equals(type)) {
			setHeight(200);

			PasswordItem passwdItem = new PasswordItem(name, title);
			passwdItem.setWidth("100%");
			passwdItem.setValue(record.getAttributeAsString("value"));
			valueField = passwdItem;
			form.setItems(passwdItem);
		} else if (TenantColType.BOOLEAN.equals(type)) {
			setHeight(200);

			RadioGroupItem radioGroupItem = new RadioGroupItem();
			radioGroupItem.setTitle(title);
			radioGroupItem.setWrap(false);
			// RecordからはLinkedHashMapは取得できないため、一度Mapで取得後変換（並び順が指定できない）
			Map<String, String> valueMap = record.getAttributeAsMap("selectItem");
			LinkedHashMap<String, String> setMap = new LinkedHashMap<String, String>(valueMap.size());
			setMap.putAll(valueMap);
			radioGroupItem.setValueMap(setMap);
			radioGroupItem.setValue(record.getAttributeAsBoolean("value").toString());
			valueField = radioGroupItem;
			form.setItems(radioGroupItem);
			// 一部タイトルが長いものがあるので設定
			form.setWrapItemTitles(false);
		} else if (TenantColType.SELECTRADIO.equals(type)) {
			setHeight(200);

			RadioGroupItem radioGroupItem = new RadioGroupItem();
			radioGroupItem.setTitle(title);
			radioGroupItem.setWrap(false);
			// RecordからはLinkedHashMapは取得できないため、一度Mapで取得後変換（並び順が指定できない）
			Map<String, String> valueMap = record.getAttributeAsMap("selectItem");
			LinkedHashMap<String, String> setMap = new LinkedHashMap<String, String>(valueMap.size());
			setMap.putAll(valueMap);
			radioGroupItem.setValueMap(setMap);
			radioGroupItem.setValue(record.getAttributeAsString("value"));
			valueField = radioGroupItem;
			form.setItems(radioGroupItem);
			// 一部タイトルが長いものがあるので設定
			form.setWrapItemTitles(false);
		} else if (TenantColType.SELECTCHECKBOX.equals(type)) {

			// RecordからはLinkedHashMapは取得できないため、一度Mapで取得後変換（並び順が指定できない）
			Map<String, String> valueMap = record.getAttributeAsMap("selectItem");
			List<String> valueList = (List<String>) JSOHelper
					.convertToJava((JavaScriptObject) record.getAttributeAsObject("value"));
			LinkedHashMap<String, String> setMap = new LinkedHashMap<String, String>(valueMap.size());
			setMap.putAll(valueMap);

			CheckboxItem[] items = new CheckboxItem[setMap.size()];
			valueFields = new CheckboxItem[setMap.size()];
			int cnt = 0;
			for (Map.Entry<String, String> e : setMap.entrySet()) {
				CheckboxItem checkboxItemItem = new CheckboxItem();
				checkboxItemItem.setTitle(e.getValue());
				checkboxItemItem.setName(e.getKey());

				if (valueList.contains(e.getKey())) {
					checkboxItemItem.setValue(true);
				}
				items[cnt] = checkboxItemItem;
				valueFields[cnt] = checkboxItemItem;
				cnt++;
//				valueField = checkboxItemItem;
			}
			form.setItems(items);

			setHeight(180 + 20 * cnt);
		} else if (TenantColType.SELECTCOMBO.equals(type)) {
			setHeight(200);

			SelectItem selectItem = new MtpSelectItem();
			selectItem.setTitle(title);
			// RecordからはLinkedHashMapは取得できないため、一度Mapで取得後変換（並び順が指定できない）
			Map<String, String> valueMap = record.getAttributeAsMap("selectItem");
			LinkedHashMap<String, String> setMap = new LinkedHashMap<String, String>(valueMap.size());
			setMap.putAll(valueMap);
			selectItem.setValueMap(setMap);
			selectItem.setValue(record.getAttributeAsString("value"));
			valueField = selectItem;
			form.setItems(selectItem);
		} else if (TenantColType.SCRIPT.equals(type)) {
			createScriptDialog(form, type, false, null);
		} else if (TenantColType.GROOVYTEMPLATE.equals(type)) {
			if ("screenTitle".equals(name)) {
				createScriptDialog(form, type, true, "localizedScreenTitle");
			} else {
				createScriptDialog(form, type, false, null);
			}
		}

		// 編集可否設定
		boolean canEdit = record.getAttributeAsBoolean("canEdit");
		if (!canEdit) {
			valueField.setDisabled(true);
		}

		return form;
	}

	@SuppressWarnings("unchecked")
	private void createScriptDialog(DynamicForm form, TenantColType colType, boolean isLocalized,
			String localizedPropertyName) {
		GWT.log(localizedPropertyName);

		setHeight(500);

		final ScriptEditorDialogMode editorMode;

		if (colType == TenantColType.GROOVYTEMPLATE) {
			editorMode = ScriptEditorDialogMode.JSP;
		} else {
			editorMode = ScriptEditorDialogMode.GROOVY_SCRIPT;
		}

		final TextAreaItem sourceField = new MtpTextAreaItem("source", "Script");
		sourceField.setColSpan(2);
		sourceField.setHeight("100%");
		sourceField.setValue(record.getAttributeAsString("value"));
		SmartGWTUtil.setReadOnlyTextArea(sourceField);
		valueField = sourceField;

		if (isLocalized) {
			CanvasItem langBtnField = new CanvasItem();
			langBtnField.setShowTitle(false);
			langBtnField.setStartRow(true);
			langBtnField.setColSpan(3);

			HLayout langBtnLayout = new HLayout();
			langBtnLayout.setAutoHeight();
			langBtnLayout.setAlign(Alignment.RIGHT);
			langBtnField.setCanvas(langBtnLayout);

			IButton editScript = new IButton();
			editScript.setTitle(
					AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPropertyEditDialog_editScript"));
			editScript.setWidth(100);
			editScript.setPrompt(AdminClientMessageUtil
					.getString("ui_metadata_tenant_TenantPropertyEditDialog_dispScriptEditDialog"));
			editScript.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(editorMode, SmartGWTUtil.getStringValue(sourceField), "", null,
							AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPropertyEditDialog_"
									+ record.getAttributeAsString("name") + "Comment"),
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									sourceField.setValue(text);
								}

								@Override
								public void onCancel() {
								}
							});
				}
			});
			langBtnLayout.addMember(editScript);

			IButton langBtn = new IButton();
			langBtn.setTitle("Languages");
			langBtn.setIcon("world.png");
			langBtn.setPrompt(SmartGWTUtil.getHoverString(
					AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPropertyEditDialog_eachLangScript")));
			localizedStringList = (List<LocalizedStringDefinition>) JSOHelper
					.convertToJava((JavaScriptObject) record.getAttributeAsObject(localizedPropertyName)) == null
							? new ArrayList<LocalizedStringDefinition>()
							: (List<LocalizedStringDefinition>) JSOHelper.convertToJava(
									(JavaScriptObject) record.getAttributeAsObject(localizedPropertyName));

			langBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					LocalizedScriptSettingDialog dialog = new LocalizedScriptSettingDialog(localizedStringList,
							AdminClientMessageUtil
									.getString("datasource_tenant_TenantDS_" + record.getAttributeAsString("name")),
							"ui_metadata_tenant_TenantPropertyEditDialog_" + record.getAttributeAsString("name")
									+ "Comment");
					dialog.show();
				}
			});
			langBtnLayout.addMember(langBtn);

			form.setHeight100();
			form.setItems(langBtnField, sourceField);
		} else {
			ButtonItem editScript = new ButtonItem("editScript",
					AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPropertyEditDialog_editScript"));
			editScript.setWidth(100);
			editScript.setColSpan(3);
			editScript.setAlign(Alignment.RIGHT);
			editScript.setPrompt(AdminClientMessageUtil
					.getString("ui_metadata_tenant_TenantPropertyEditDialog_dispScriptEditDialog"));
			editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(editorMode, SmartGWTUtil.getStringValue(sourceField), "", null,
							AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPropertyEditDialog_"
									+ record.getAttributeAsString("name") + "Comment"),
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									sourceField.setValue(text);
								}

								@Override
								public void onCancel() {
								}
							});
				}
			});

			form.setHeight100();
			form.setItems(editScript, sourceField);
		}

	}

	/**
	 * 保存処理
	 */
	private void setValue() {

		// 編集可否設定
		boolean canEdit = record.getAttributeAsBoolean("canEdit");
		if (!canEdit) {
			// ダイアログ消去
			destroy();
			return;
		}

		TenantColType type = (TenantColType) record.getAttributeAsObject("colType");
		if (TenantColType.BOOLEAN.equals(type)) {
			// RadioGroupItemの値はString型のため、Booleanで保存
			record.setAttribute("value", new Boolean(valueField.getValue().toString()));

			@SuppressWarnings("unchecked")
			Map<String, String> valueMap = record.getAttributeAsMap("selectItem");
			record.setAttribute("displayValue", valueMap.get(valueField.getValue()));
		} else if (TenantColType.SELECTRADIO.equals(type)) {
			record.setAttribute("value", valueField.getValue());

			@SuppressWarnings("unchecked")
			Map<String, String> valueMap = record.getAttributeAsMap("selectItem");
			record.setAttribute("displayValue", valueMap.get(valueField.getValue()));
		} else if (TenantColType.SELECTCHECKBOX.equals(type)) {

			List<String> valueList = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			int cnt = 0;
			for (FormItem item : valueFields) {
				if (item.getValue() != null && new Boolean(item.getValue().toString())) {
					valueList.add((String) item.getName());
					if (cnt > 0) {
						sb.append(", ");
					}
					sb.append(item.getTitle());

					cnt++;
				}
			}
			record.setAttribute("value", valueList);

			record.setAttribute("displayValue", sb.toString());
		} else if (TenantColType.SELECTCOMBO.equals(type)) {
			record.setAttribute("value", valueField.getValue());

			@SuppressWarnings("unchecked")
			Map<String, String> valueMap = record.getAttributeAsMap("selectItem");
			record.setAttribute("displayValue", valueMap.get(valueField.getValue()));
		} else if (TenantColType.INTEGER.equals(type)) {
			record.setAttribute("value", ((IntegerItem) valueField).getValueAsInteger());
			record.setAttribute("displayValue", ((IntegerItem) valueField).getValueAsInteger());
		} else if (TenantColType.SCRIPT.equals(type) || TenantColType.GROOVYTEMPLATE.equals(type)) {
			String status = AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPropertyEditDialog_setting");
			if (valueField.getValue() == null || valueField.getValue().equals("")) {
				status = AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPropertyEditDialog_noSetting");
			}
			record.setAttribute("value", valueField.getValue());
			record.setAttribute("displayValue", status);

			if ("screenTitle".equals(record.getAttribute("name"))) {
				record.setAttribute("localizedScreenTitle", localizedStringList);
			}
		} else {

			record.setAttribute("value", valueField.getValue());
			record.setAttribute("displayValue", valueField.getValue());

			if ("displayName".equals(valueField.getName())
					|| "passwordPatternErrorMessage".equals(valueField.getName())) {
				record.setAttribute("localizedStringList", ((MetaDataLangTextItem) valueField).getLocalizedList());
			}
		}

		// ダイアログ消去
		destroy();

		// データ変更を通知
		fireDataChanged(record);
	}

	/**
	 * データの変更を通知します。
	 *
	 * @param record 更新 {@link Record}
	 */
	private void fireDataChanged(Record record) {
		// イベントに更新MenuItemをセットして発行する
		DataChangedEvent event = new DataChangedEvent();
		// event.setValueObject(record);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

}
