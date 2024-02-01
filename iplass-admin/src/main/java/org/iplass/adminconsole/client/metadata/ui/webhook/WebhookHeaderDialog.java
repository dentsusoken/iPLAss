/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.webhook;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.webhook.template.definition.WebhookHeaderDefinition;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author lisf06
 *
 */
public class WebhookHeaderDialog extends MtpDialog {
	private HeaderAttributePane headerAttrEditPane;
	
	private WebhookHeaderDefinition curHeaderDefinition;
	
	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();
	
	
	public WebhookHeaderDialog(WebhookHeaderDefinition headerDefinition) {
		curHeaderDefinition = headerDefinition;
		if (curHeaderDefinition==null) {
			curHeaderDefinition = new WebhookHeaderDefinition();
		}
		
		setTitle("Header Editor");
		setHeight("20%");
		centerInPage();
		
		headerAttrEditPane = new HeaderAttributePane();
		headerAttrEditPane.setHeight100();
		
		container.addMember(headerAttrEditPane);
		
		IButton save = new IButton("Save");
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				WebhookHeaderDefinition definition = curHeaderDefinition;
				if (headerAttrEditPane.isHeaderNameFieldEmpty()) {
					return;
				}
				definition = headerAttrEditPane.getEditDefinition(definition);
				
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
	private class HeaderAttributePane extends VLayout {
		private DynamicForm form;
		private TextItem headerNameField;
		private TextItem headerValueField;
		
		public HeaderAttributePane() {
			form = new MtpForm2Column();
			headerNameField = new TextItem("headerName", "Header");
			headerValueField = new TextItem("headerValue","Value");
			
			form.setItems(headerNameField, headerValueField);
			addMember(form);
			setDefinition(curHeaderDefinition);
		}
		
		/** definition -> dialog */
		public void setDefinition(WebhookHeaderDefinition _curHeaderDefinition) {
			if ( _curHeaderDefinition!=null) {
				headerNameField.setValue(_curHeaderDefinition.getKey());
				headerValueField.setValue(_curHeaderDefinition.getValue());
			} else {
				headerNameField.clearValue();
				headerValueField.clearValue();
			}
		}
		
		/** dialog -> definition */
		public WebhookHeaderDefinition getEditDefinition(WebhookHeaderDefinition curHeaderDefinition){
			curHeaderDefinition.setKey((SmartGWTUtil.getStringValue(headerNameField)).replaceAll("\\s+",""));
			curHeaderDefinition.setValue((SmartGWTUtil.getStringValue(headerValueField)).replaceAll("\\s+",""));
			return curHeaderDefinition;
		}
		public boolean isHeaderNameFieldEmpty() {
			if (SmartGWTUtil.getStringValue(headerNameField)==null||SmartGWTUtil.getStringValue(headerNameField).replaceAll("\\s+","").isEmpty()) {
				return true;
			}
			return false;
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
	private void fireDataChanged(WebhookHeaderDefinition paramMap) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(paramMap);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
