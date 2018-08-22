/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.sms;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorPane;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.gwt.ace.client.EditorMode;
import org.iplass.mtp.mail.template.definition.PlainTextBodyPart;
import org.iplass.mtp.sms.template.definition.LocalizedSmsMailTemplateDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class SmsMailTemplateSettingByLocaleDialog extends AbstractWindow {

	private LocalizedSmsMailTemplateAttributePane localizedMainPane;
	private LocalizedSmsMailTemplateDefinition curLocalizedDefinition;
	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public SmsMailTemplateSettingByLocaleDialog(LocalizedSmsMailTemplateDefinition definition) {

		curLocalizedDefinition = definition;
		if (curLocalizedDefinition == null) {
			curLocalizedDefinition = new LocalizedSmsMailTemplateDefinition();
		}

		localizedMainPane = new LocalizedSmsMailTemplateAttributePane();

		setTitle("Multilingual Setting Dialog");
		setHeight100();
		setWidth(800);
		setMargin(10);
		setMembersMargin(10);
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		OperationPane opePane = new OperationPane(this);

		localizedMainPane.setHeight100();
		addItem(localizedMainPane);
		addItem(opePane);
	}

	private class LocalizedSmsMailTemplateAttributePane extends VLayout {
		private DynamicForm form;
		private SelectItem langSelectItem;

		private TabSet massageTabSet;
		private Tab plainMessageTab;
		private ScriptEditorPane plainEditor;

		public LocalizedSmsMailTemplateAttributePane() {
			setOverflow(Overflow.AUTO);	//Stack上の表示領域が小さい場合にスクロールができるようにAUTO設定

			MetaDataServiceAsync service = MetaDataServiceFactory.get();
			service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {

				@Override
				public void onFailure(Throwable caught) {
					//"ui_metadata_template_report_SmsMailTemplateSettingByLocaleDialog_failed"
					//"ui_metadata_template_report_SmsMailTemplateSettingByLocaleDialog_failedGetScreenInfo"
					SC.say(AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateSettingByLocaleDialog_failed"),
							AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateSettingByLocaleDialog_failedGetScreenInfo"));
					GWT.log(caught.toString(), caught);
				}

				@Override
				public void onSuccess(Map<String, String> result) {
					LinkedHashMap<String, String> enableLanguagesMap = new LinkedHashMap<String, String>();
					for (Map.Entry<String, String> e : result.entrySet()) {
						enableLanguagesMap.put(e.getKey(), e.getValue());
					}
					langSelectItem = new SelectItem("language", "Language");
					langSelectItem.setValueMap(enableLanguagesMap);

					VLayout mainPane = new VLayout();
					mainPane.setMargin(5);

					form = new DynamicForm();
					form.setWidth100();
					form.setNumCols(5);	//間延びしないように最後に１つ余分に作成
					form.setColWidths(100, "*", 100, "*", "*");

					form.setItems(langSelectItem, new SpacerItem());

					//メッセージ編集領域
					massageTabSet = new TabSet();
					massageTabSet.setWidth100();
					//massageTabSet.setHeight100();
					massageTabSet.setHeight(450);	//高さは固定でないとうまくいかないため指定
					massageTabSet.setPaneContainerOverflow(Overflow.HIDDEN);	//Editor側のスクロールを利用するため非表示に設定（Editor側のサイズを調整）

					plainMessageTab = new Tab();
					plainMessageTab.setTitle(AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateSettingByLocaleDialog_textMessage"));

					plainEditor = new ScriptEditorPane();
					plainEditor.setMode(EditorMode.TEXT);

					plainMessageTab.setPane(plainEditor);
					massageTabSet.addTab(plainMessageTab);

					mainPane.addMember(form);
					mainPane.addMember(massageTabSet);
					addMember(mainPane);
					setDefinition(curLocalizedDefinition);
				}
			});
		}
		public void setDefinition(LocalizedSmsMailTemplateDefinition definition){
			String plainMessage = null;
			if (definition != null) {
				langSelectItem.setValue(definition.getLocaleName());
				if (definition.getMessage() != null) {
					PlainTextBodyPart part = definition.getMessage();
					plainEditor.setText(part.getContent());
					plainMessage = part.getContent();
				}
			} else {
				plainEditor.setText("");
			}
			massageTabSet.selectTab(plainMessageTab);

			setTabTitle(plainMessageTab, AdminClientMessageUtil.getString("ui_metadata_sms_SmsMailTemplateSettingByLocaleDialog_textMessage"), plainMessage);
		}

		public LocalizedSmsMailTemplateDefinition getEditDefinition(LocalizedSmsMailTemplateDefinition definition) {
			definition.setLocaleName(SmartGWTUtil.getStringValue(langSelectItem));
			PlainTextBodyPart plainPart = new  PlainTextBodyPart();
			plainPart.setContent(plainEditor.getText());
			definition.setMessage(plainPart);
			return definition;
		}

		private void setTabTitle(Tab target, String title, String message) {
			if (message != null && !message.isEmpty()) {
				target.setTitle(title + "(*)");
			} else {
				target.setTitle(title);
			}
		}
	}

	private class OperationPane extends HLayout {
		public OperationPane(final SmsMailTemplateSettingByLocaleDialog dialog) {
			HLayout footer = new HLayout(5);
			footer.setMargin(10);
			footer.setWidth100();

			IButton save = new IButton("Save");
			save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					LocalizedSmsMailTemplateDefinition definition = curLocalizedDefinition;

					definition = localizedMainPane.getEditDefinition(curLocalizedDefinition);
					fireDataChanged(definition);
					dialog.destroy();
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					dialog.destroy();
				}
			});

			footer.setMembers(save, cancel);

			addMember(footer);
		}
	}

	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	private void fireDataChanged(LocalizedSmsMailTemplateDefinition definition) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(definition);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
