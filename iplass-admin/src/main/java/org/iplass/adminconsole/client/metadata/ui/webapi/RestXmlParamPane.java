/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class RestXmlParamPane extends HLayout {

	private DynamicForm form;
	private TextItem paramName;

	public RestXmlParamPane() {

		Label caption = new Label("REST XML Parameter :");
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

		paramName = new TextItem("paramName", "Parameter Name");
		paramName.setWidth(500);

		form.setItems(paramName);

		addMember(caption);
		addMember(form);
	}

	/**
	 * 編集されたWebAPIDefinition情報を返します。
	 *
	 * @return 編集WebAPIDefinition情報
	 */
	public WebApiDefinition getEditDefinition(WebApiDefinition definition) {

		if (paramName.getValue() != null) {
			definition.setRestXmlParameterName(paramName.getValue().toString());
		}
		return definition;
	}

	/**
	 * WebAPIを展開します。
	 *
	 * @param definition WebAPIDefinition
	 */
	public void setDefinition(WebApiDefinition definition) {
		paramName.setValue(definition.getRestXmlParameterName());
	}

}
