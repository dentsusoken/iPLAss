/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.mail;

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
import org.iplass.mtp.mail.template.definition.HtmlBodyPart;
import org.iplass.mtp.mail.template.definition.LocalizedMailTemplateDefinition;
import org.iplass.mtp.mail.template.definition.PlainTextBodyPart;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class MailTemplateSettingByLocaleDialog extends MtpDialog {

	private LocalizedMailTemplateAttributePane localizedMainPane;

	/** 編集対象 */
	private LocalizedMailTemplateDefinition curLocalizedDefinition;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public MailTemplateSettingByLocaleDialog(LocalizedMailTemplateDefinition definition) {

		curLocalizedDefinition = definition;
		if (curLocalizedDefinition == null) {
			curLocalizedDefinition = new LocalizedMailTemplateDefinition();
		}

		setTitle("Multilingual Setting Dialog");
		setHeight("80%");
		centerInPage();

		localizedMainPane = new LocalizedMailTemplateAttributePane();
		localizedMainPane.setHeight100();
		container.addMember(localizedMainPane);

		IButton save = new IButton("Save");
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				LocalizedMailTemplateDefinition definition = curLocalizedDefinition;

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

	private class LocalizedMailTemplateAttributePane extends VLayout {

		private DynamicForm form;
		private SelectItem langSelectItem;
		private TextItem subjectField;
		private TextItem charsetField;
		private TextItem htmlCharsetField;

		private TabSet massageTabSet;
		private Tab plainMessageTab;
		private Tab htmlMessageTab;
		private ScriptEditorPane plainEditor;
		private ScriptEditorPane htmlEditor;

		public LocalizedMailTemplateAttributePane() {

			MetaDataServiceAsync service = MetaDataServiceFactory.get();
			service.getEnableLanguages(TenantInfoHolder.getId(), new AsyncCallback<Map<String, String>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO 自動生成されたメソッド・スタブ

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

					subjectField = new MtpTextItem("subject", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateSettingByLocaleDialog_subject"));
					subjectField.setColSpan(3);
					SmartGWTUtil.setRequired(subjectField);

					charsetField = new MtpTextItem("charset", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateSettingByLocaleDialog_charaCode"));
					charsetField.setStartRow(true);

					htmlCharsetField = new MtpTextItem("htmlCharset", AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateSettingByLocaleDialog_htmlCharaCode"));

					form.setItems(langSelectItem, subjectField, charsetField, htmlCharsetField, new SpacerItem());

					//メッセージ編集領域
					massageTabSet = new TabSet();
					massageTabSet.setWidth100();
					//massageTabSet.setHeight100();
					massageTabSet.setHeight(450);	//高さは固定でないとうまくいかないため指定
					massageTabSet.setPaneContainerOverflow(Overflow.HIDDEN);	//Editor側のスクロールを利用するため非表示に設定（Editor側のサイズを調整）

			        plainMessageTab = new Tab();
			        plainMessageTab.setTitle(AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateSettingByLocaleDialog_textMessage"));

					plainEditor = new ScriptEditorPane();
					plainEditor.setMode(EditorMode.TEXT);

					plainMessageTab.setPane(plainEditor);
			        massageTabSet.addTab(plainMessageTab);

			        htmlMessageTab = new Tab();
			        htmlMessageTab.setTitle(AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateSettingByLocaleDialog_htmlMessage"));

					htmlEditor = new ScriptEditorPane();
					htmlEditor.setMode(EditorMode.HTML);

					htmlMessageTab.setPane(htmlEditor);
					massageTabSet.addTab(htmlMessageTab);

					addMember(form);
					addMember(massageTabSet);

					setDefinition(curLocalizedDefinition);
				}
			});

		}

		/**
		 * MailTemplateDefinitionを展開します。
		 *
		 * @param definition MailTemplateDefinition
		 */
		public void setDefinition(LocalizedMailTemplateDefinition definition) {
			String plainMessage = null;
			String htmlMessage = null;
			if (definition != null) {
				langSelectItem.setValue(definition.getLocaleName());
				subjectField.setValue(definition.getSubject());
				charsetField.setValue(definition.getCharset());
				boolean existPlainMessage = false;
				boolean existHtmlMessage = false;
				if (definition.getPlainMessage() != null) {
					PlainTextBodyPart part = definition.getPlainMessage();
					plainEditor.setText(part.getContent());
					existPlainMessage
						= (part.getContent() != null && !part.getContent().isEmpty());
					plainMessage = part.getContent();
				}
				if (definition.getHtmlMessage() != null) {
					HtmlBodyPart part = definition.getHtmlMessage();
					htmlEditor.setText(part.getContent());
					htmlCharsetField.setValue(part.getCharset());
					existHtmlMessage
						= (part.getContent() != null && !part.getContent().isEmpty());
					htmlMessage = part.getContent();
				}

				if (existPlainMessage) {
					massageTabSet.selectTab(plainMessageTab);
				} else if (existHtmlMessage) {
					massageTabSet.selectTab(htmlMessageTab);
				} else {
					massageTabSet.selectTab(plainMessageTab);
				}
			} else {
				subjectField.clearValue();
				charsetField.clearValue();
				plainEditor.setText("");
				htmlEditor.setText("");
				htmlCharsetField.clearValue();

				massageTabSet.selectTab(plainMessageTab);
			}

			setTabTitle(plainMessageTab, AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateSettingByLocaleDialog_textMessage"), plainMessage);
			setTabTitle(htmlMessageTab, AdminClientMessageUtil.getString("ui_metadata_mail_MailTemplateSettingByLocaleDialog_htmlMessage"), htmlMessage);
		}

		/**
		 * 編集されたMailTemplateDefinition情報を返します。
		 *
		 * @return 編集MailTemplateDefinition情報
		 */
		public LocalizedMailTemplateDefinition getEditDefinition(LocalizedMailTemplateDefinition definition) {

			definition.setLocaleName(SmartGWTUtil.getStringValue(langSelectItem));
			definition.setSubject(SmartGWTUtil.getStringValue(subjectField));
			definition.setCharset(SmartGWTUtil.getStringValue(charsetField));
			PlainTextBodyPart plainPart = new  PlainTextBodyPart();
			plainPart.setContent(plainEditor.getText());
			definition.setPlainMessage(plainPart);
			HtmlBodyPart htmlPart = new  HtmlBodyPart();
			htmlPart.setContent(htmlEditor.getText());
			htmlPart.setCharset(SmartGWTUtil.getStringValue(htmlCharsetField));
			definition.setHtmlMessage(htmlPart);
			return definition;
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
	private void fireDataChanged(LocalizedMailTemplateDefinition paramMap) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(paramMap);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

}
