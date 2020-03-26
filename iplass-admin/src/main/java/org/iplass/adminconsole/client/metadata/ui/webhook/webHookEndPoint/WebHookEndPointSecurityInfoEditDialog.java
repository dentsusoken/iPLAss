/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.webhook.webHookEndPoint;

import java.util.ArrayList;
import java.util.List;
import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class WebHookEndPointSecurityInfoEditDialog extends MtpDialog {
	SecurityAttributePane securityAttributePane;
	String securityType;
	String tokenContent;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();
	
	public WebHookEndPointSecurityInfoEditDialog(String targetSecurityType, String existedTokenContent ) {
		securityType=targetSecurityType;
		tokenContent=existedTokenContent;
		
		setTitle("Security Token Editor");
		setHeight(500);
		centerInPage();
		
		securityAttributePane = new SecurityAttributePane();
		securityAttributePane.setHeight100();
		container.addMember(securityAttributePane);
		
		IButton save = new IButton("Save");
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				
				String result = securityAttributePane.getEditDefinition();
				fireDataChanged(result);
				markForDestroy();
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				markForDestroy();
			}
		});
		footer.setMembers(save, cancel);
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
	private void fireDataChanged(String secret) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValue("result", secret);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
	public class SecurityAttributePane extends VLayout{
		private DynamicForm form;

		private TextAreaItem tokenContentField;

		private TextItem basicUsernameField;
		private TextItem basicPasswordField;

		public SecurityAttributePane() {
			form = new DynamicForm();
			form.setNumCols(2);

			basicUsernameField = new TextItem("basicUsernameField","UserName");
			basicPasswordField = new TextItem("basicPasswordField","Password");
			
			tokenContentField = new TextAreaItem("tokenContentField","Token Content");
			tokenContentField.setHeight(300);
			tokenContentField.setWidth("100%");
			form.setItems(tokenContentField, basicUsernameField, basicPasswordField);
			addMember(form);
			setDefinition(securityType,tokenContent);
			adjustContentDisplay();
		}
		public void adjustContentDisplay() {
			if (securityType==null) {
				basicUsernameField.setVisible(false);
				basicPasswordField.setVisible(false);
				tokenContentField.setVisible(false);
			}else if (securityType.equals("WHBA")) {
				tokenContentField.setVisible(false);
				basicUsernameField.setVisible(true);
				basicPasswordField.setVisible(true);
			} else {
				basicUsernameField.setVisible(false);
				basicPasswordField.setVisible(false);
				tokenContentField.setVisible(true);
			}
			this.redraw();
		}
		/** definition -> dialog 
		 * @param tokenContent 
		 * @param securityType */
		public void setDefinition(String securityType, String tokenContent) {
			if (securityType == null || securityType.isEmpty()) {
				return;
			}
			if (securityType.equals("WHBA")) {
				//basic
				String[] basic;
				if (tokenContent!=null) {
					basic= tokenContent.split(":");

					if (basic.length <2) {
						basicUsernameField.clearValue();
						basicPasswordField.clearValue();
					} else {
						basicUsernameField.setValue(basic[0]);
						basicPasswordField.setValue(basic[1]);
					}
				} else {
					basicUsernameField.clearValue();
					basicPasswordField.clearValue();
				}
			} else {
				if (tokenContent!=null) {
					tokenContentField.setValue(tokenContent);
				} else {
					tokenContentField.clearValue();
				}
			}
		}

		/** dialog -> definition */
		public String getEditDefinition(){
			String result = "";
			if (securityType.equals("WHBA")) {
				String basicName = SmartGWTUtil.getStringValue(basicUsernameField);
				String basicPass = SmartGWTUtil.getStringValue(basicPasswordField);
				if (basicName==null
						||basicPass==null
						||basicName.replaceAll("\\s","").isEmpty()
						||basicPass.replaceAll("\\s","").isEmpty()) {
				} else {
					result = basicName+":"+basicPass;
				}
			} else {
				result = SmartGWTUtil.getStringValue(tokenContentField);
			}
			return result;
			
		}
	}
}
