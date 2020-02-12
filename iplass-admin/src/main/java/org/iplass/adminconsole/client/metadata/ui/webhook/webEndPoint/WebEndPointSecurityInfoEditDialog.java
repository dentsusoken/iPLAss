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
package org.iplass.adminconsole.client.metadata.ui.webhook.webEndPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class WebEndPointSecurityInfoEditDialog extends MtpDialog {
	SecurityAttributePane securityAttributePane;
	String securityType;
	String tokenContent;
	
	private final String BASIC = "Basic Authentication";
	private final String BEARER= "Bearer Authentication";		
	private final String HMAC= "HMAC Token";
	@SuppressWarnings("serial")
	private final HashMap<String,String> codeTrans = new HashMap<String,String>() {{
		put(BASIC,"WHBA");
		put(BEARER,"WHBT");
		put(HMAC,"WHHM");
	}};
	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();
	
	public WebEndPointSecurityInfoEditDialog(String targetSecurityType, String existedTokenContent ) {
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
				
				Map<String, Serializable> valueMap = securityAttributePane.getEditDefinition();
				fireDataChanged(valueMap);
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
	private void fireDataChanged(Map<String, Serializable> valueMap) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueMap(valueMap);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
	public class SecurityAttributePane extends VLayout{
		private TextAreaItem tokenContentField;
		private DynamicForm form;
		private SelectItem typeItemField;
		private TextItem basicUsernameField;
		private TextItem basicPasswordField;
		
		public SecurityAttributePane() {
			form = new DynamicForm();
			form.setNumCols(2);
			typeItemField = new SelectItem("tokenType","Token Type");
			typeItemField.setWidth("100%");
			SmartGWTUtil.setRequired(typeItemField);
			
			typeItemField.addChangedHandler(new ChangedHandler() {
				public void onChanged(ChangedEvent event) {
					adjustContentDisplay();
				}
			});
			
			basicUsernameField = new TextItem("basicUsernameField","UserName");
			basicPasswordField = new TextItem("basicPasswordField","Password");
			
			tokenContentField = new TextAreaItem("tokenContentField","Token Content");
			tokenContentField.setHeight(300);
			tokenContentField.setWidth("100%");
			form.setItems(typeItemField, tokenContentField, basicUsernameField, basicPasswordField);
			addMember(form);
			setDefinition(securityType,tokenContent);
			adjustContentDisplay();
		}
		public void adjustContentDisplay() {
			securityType = SmartGWTUtil.getStringValue(typeItemField);
			if (securityType==null) {
				basicUsernameField.setVisible(false);
				basicPasswordField.setVisible(false);
				tokenContentField.setVisible(false);
			}else if (codeTrans.get(securityType).equals("WHBA")) {
				tokenContentField.setVisible(false);
				basicUsernameField.setVisible(true);
				basicPasswordField.setVisible(true);
			}else if (codeTrans.get(securityType).equals("WHBT")) {
				basicUsernameField.setVisible(false);
				basicPasswordField.setVisible(false);
				tokenContentField.setVisible(true);
			}else if (codeTrans.get(securityType).equals("WHHM")) {
				basicUsernameField.setVisible(false);
				basicPasswordField.setVisible(false);
				tokenContentField.setVisible(true);
			} else {
				basicUsernameField.setVisible(false);
				basicPasswordField.setVisible(false);
				tokenContentField.setVisible(false);
			}
			this.redraw();
		}
		/** definition -> dialog 
		 * @param tokenContent 
		 * @param securityType */
		public void setDefinition(String securityType, String tokenContent) {
			if (securityType == null || securityType.isEmpty()) {
				typeItemField.setValueMap(BASIC,BEARER);
				typeItemField.setCanEdit(true);
				return;
			}
			if (securityType.equals("WHHM")) {
				//hmac
				typeItemField.setValueMap(HMAC);
				typeItemField.setValue(HMAC);
				typeItemField.setCanEdit(false);
				if (tokenContent!=null) {
					tokenContentField.setValue(tokenContent);
				} else {
					tokenContentField.clearValue();
				}
			} else { 
				typeItemField.setValueMap(BASIC,BEARER);
				typeItemField.setCanEdit(true);
				if (securityType.equals("WHBA")) {
					//TODO:調整して、こんなに回りくどくないように
					//basic
					typeItemField.setValue(BASIC);
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
				} else if (securityType.equals("WHBT")) {
					//bearer
					typeItemField.setValue(BEARER);
					if (tokenContent!=null) {
						tokenContentField.setValue(tokenContent);
					} else {
						tokenContentField.clearValue();
					}
				}
			}
			
		}
		
		/** dialog -> definition */
		public Map<String, Serializable> getEditDefinition(){
			Map<String, Serializable> valueMap = new HashMap<String, Serializable>();
			valueMap.put("type", codeTrans.get(SmartGWTUtil.getStringValue(typeItemField)));
			if(valueMap.get("type")==null||((String)valueMap.get("type")).isEmpty()) {
				valueMap.put("content",null);
				return valueMap;
			}
			if (valueMap.get("type").equals("WHBA")) {
				String basicName = SmartGWTUtil.getStringValue(basicUsernameField);
				String basicPass = SmartGWTUtil.getStringValue(basicPasswordField);
				valueMap.put("content", basicName+":"+basicPass);
			} else if (valueMap.get("type").equals("WHBT")){
				valueMap.put("content", SmartGWTUtil.getStringValue(tokenContentField));
			} else {
				valueMap.put("content", SmartGWTUtil.getStringValue(tokenContentField));
			}

			return valueMap;
			
		}
	}
}
