/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.pushnotification;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorPane;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.gwt.ace.client.EditorMode;
import org.iplass.mtp.pushnotification.template.definition.LocalizedNotificationDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class PushNotificationTemplateSettingByLocaleDialog extends MtpDialog {

	private LocalizedPushNotificationTemplateAttributePane localizedMainPane;

	/** 編集対象 */
	private LocalizedNotificationDefinition curLocalizedDefinition;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public PushNotificationTemplateSettingByLocaleDialog(LocalizedNotificationDefinition definition) {
		curLocalizedDefinition = definition;
		if (curLocalizedDefinition == null) {
			curLocalizedDefinition = new LocalizedNotificationDefinition();
		}

		setTitle("Multilingual Setting Dialog");
		setHeight("80%");
		centerInPage();

		localizedMainPane = new LocalizedPushNotificationTemplateAttributePane();
		localizedMainPane.setHeight100();
		container.addMember(localizedMainPane);

		IButton save = new IButton("Save");
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				boolean validate = localizedMainPane.validate();
				if (!validate) {
					return;
				}

				LocalizedNotificationDefinition definition = curLocalizedDefinition;

				definition = localizedMainPane.getEditDefinition(curLocalizedDefinition);
				fireDataChanged(definition);
				destroy();
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);
	}

	private class LocalizedPushNotificationTemplateAttributePane extends VLayout {
		private DynamicForm form;
		private SelectItem langSelectItem;
		private TextItem titleField;

		private TabSet tabSet;

		private Tab bodyTab;
		private ScriptEditorPane bodyEditor;

		public LocalizedPushNotificationTemplateAttributePane() {

			MetaDataServiceAsync service = MetaDataServiceFactory.get();
			service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {
				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(Map<String, String> result) {
					LinkedHashMap<String, String> enableLanguagesMap = new LinkedHashMap<String, String>();
					for (Map.Entry<String, String> e : result.entrySet()) {
						enableLanguagesMap.put(e.getKey(), e.getValue());
					}

					form = new MtpForm2Column();

					langSelectItem = new SelectItem("language", "Language");
					langSelectItem.setValueMap(enableLanguagesMap);
					SmartGWTUtil.setRequired(langSelectItem);

					titleField = new MtpTextItem("title", AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateSettingByLocaleDialog_title"));
					titleField.setColSpan(3);

					form.setItems(langSelectItem, titleField);

					//メッセージ編集領域
					tabSet = new TabSet();
					tabSet.setWidth100();
					tabSet.setHeight(450);	//高さは固定でないとうまくいかないため指定
					tabSet.setPaneContainerOverflow(Overflow.HIDDEN);	//Editor側のスクロールを利用するため非表示に設定（Editor側のサイズを調整）

					bodyTab = new Tab();
					bodyTab.setTitle(AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateSettingByLocaleDialog_body"));

					bodyEditor = new ScriptEditorPane();
					bodyEditor.setMode(EditorMode.HTML);

					bodyTab.setPane(bodyEditor);
					tabSet.addTab(bodyTab);

					addMember(form);
					addMember(tabSet);

					setDefinition(curLocalizedDefinition);
				}
			});
		}

		/**
		 * LocalizedNotificationDefinitionを展開します。
		 *
		 * @param definition LocalizedNotificationDefinition
		 */
		public void setDefinition(LocalizedNotificationDefinition definition) {
			String body = null;

			if (definition != null) {
				langSelectItem.setValue(definition.getLocaleName());
				titleField.setValue(definition.getTitle());

				body = definition.getBody();
				bodyEditor.setText(body);
			} else {
				titleField.clearValue();
				bodyEditor.setText("");
			}

			tabSet.selectTab(bodyTab);

			setTabTitle(bodyTab, AdminClientMessageUtil.getString("ui_metadata_pushnotification_PushNotificationTemplateSettingByLocaleDialog_body"), body);
		}

		/**
		 * 編集されたLocalizedNotificationDefinition情報を返します。
		 *
		 * @return 編集LocalizedNotificationDefinition情報
		 */
		public LocalizedNotificationDefinition getEditDefinition(LocalizedNotificationDefinition definition) {
			definition.setLocaleName(SmartGWTUtil.getStringValue(langSelectItem));
			definition.setTitle(SmartGWTUtil.getStringValue(titleField));
			definition.setBody(bodyEditor.getText());
			return definition;
		}

		/**
		 * 入力チェックを実行します。
		 *
		 * @return 入力チェック結果
		 */
		public boolean validate() {
			return form.validate();
		}

		/**
		 * メッセージ入力の状態からタブのタイトルを設定します。
		 *
		 * @param target 対象タブ
		 * @param title  タイトル
		 * @param message メッセージ
		 */
		private void setTabTitle(Tab target, String title, String message) {
			if (message != null && !message.isEmpty()) {
				target.setTitle(title + "(*)");
			} else {
				target.setTitle(title);
			}
		}
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}


	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(LocalizedNotificationDefinition paramMap) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(paramMap);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

}
