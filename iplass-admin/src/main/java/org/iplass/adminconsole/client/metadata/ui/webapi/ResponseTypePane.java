/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.webapi;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class ResponseTypePane extends HLayout {

	private DynamicForm form;
	private ComboBoxItem responseTypeField;

	public ResponseTypePane() {

		Label caption = new Label("Response Type :");
		caption.setHeight(21);

		//レイアウト設定
		setWidth100();
		setHeight(20);
		setMargin(5);
		setMembersMargin(10);
		setAlign(Alignment.LEFT);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		form.setNumCols(2);
		form.setMargin(5);

		responseTypeField = new ComboBoxItem("responseType", "Response Type");
		responseTypeField.setWidth(500);

		LinkedHashMap<String, String> responseTypeMap = new LinkedHashMap<String, String>();
		responseTypeMap.put("application/xml", "application/xml");
		responseTypeMap.put("application/json", "application/json");
		responseTypeField.setValueMap(responseTypeMap);

		form.setItems(responseTypeField);

		addMember(caption);
		addMember(form);
	}

	/**
	 * 編集されたWebAPIDefinition情報を返します。
	 *
	 * @return 編集WebAPIDefinition情報
	 */
	public WebApiDefinition getEditDefinition(WebApiDefinition definition) {

		definition.setResponseType(SmartGWTUtil.getStringValue(responseTypeField));
		return definition;
	}

	/**
	 * WebAPIを展開します。
	 *
	 * @param definition WebAPIDefinition
	 */
	public void setDefinition(WebApiDefinition definition) {
		responseTypeField.setValue(definition.getResponseType());
	}

}
