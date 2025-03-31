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

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * REST XML パラメータ設定パネル
 *
 * <p>
 * REST XML のチェックボックスを設定すると表示されるパネルです。
 * </p>
 */
public class RestXmlParamPane extends HLayout {

	private DynamicForm form;
	private TextItem paramName;
	private TextItem paramType;
	/** 受付可能なContent-Type */
	private TextItem acceptableContentTypes;

	/**
	 * コンストラクタ
	 */
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
		paramType = new TextItem("paramType", "Parameter Type");
		paramType.setWidth(500);
		acceptableContentTypes = new TextItem("acceptableContentTypes", "Acceptable Conetnt Type");
		acceptableContentTypes.setWidth(500);
		acceptableContentTypes.setTooltip(
				SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_webapi_RestXmlParamPane_acceptableContentTypes_tooltip")));

		form.setItems(paramName, paramType, acceptableContentTypes);

		addMember(caption);
		addMember(form);
	}

	/**
	 * 編集されたWebAPIDefinition情報を返します。
	 *
	 * @param definition WebAPI定義情報
	 * @return 編集WebAPIDefinition情報
	 */
	public WebApiDefinition getEditDefinition(WebApiDefinition definition) {

		if (paramName.getValue() != null) {
			definition.setRestXmlParameterName(paramName.getValue().toString());
		}
		if (paramType.getValue() != null) {
			definition.setRestXmlParameterType(paramType.getValue().toString());
		}
		if (acceptableContentTypes.getValue() != null) {
			definition.setRestXmlAcceptableContentTypes(SmartGWTUtil.convertStringToArray(acceptableContentTypes.getValue().toString(), ","));
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
		paramType.setValue(definition.getRestXmlParameterType());
		if (null != definition.getRestXmlAcceptableContentTypes()) {
			acceptableContentTypes.setValue(String.join(",", definition.getRestXmlAcceptableContentTypes()));
		}
	}
}
