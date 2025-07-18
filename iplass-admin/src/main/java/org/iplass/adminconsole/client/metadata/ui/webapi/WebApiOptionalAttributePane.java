/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogSetting;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * WebAPIメタデータのオプション属性用の入力領域
 * <p>
 * WebAPIのスタブレスポンス、OpenAPI定義などを入力するための領域です。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class WebApiOptionalAttributePane extends VLayout {
	/** 入力フォーム */
	private DynamicForm form;
	/** 公開WebAPI */
	private CheckboxItem returnStubResponseField;
	/** スタブレスポンス "status" の値フィールド */
	private TextItem stubResponseStatusValueField;
	/** スタブレスポンスフィールド */
	private TextAreaItem stubResposeJsonValueField;
	/** OpenAPI 定義フィールド */
	private TextAreaItem openApiField;

	public WebApiOptionalAttributePane() {
		setMargin(5);
		setAutoHeight();
		setWidth100();
		setOverflow(Overflow.VISIBLE);

		returnStubResponseField = new CheckboxItem("enableStubResponseField", "Enable Stub Response");
		returnStubResponseField.setWidth("100%");
		returnStubResponseField.setColSpan(3);

		stubResponseStatusValueField = new TextItem("stubResponseStatusValueField", "Stub Response Staus Value");
		stubResponseStatusValueField.setWidth("100%");
		stubResponseStatusValueField.setColSpan(3);

		stubResposeJsonValueField = new TextAreaItem("stubResposeJsonValueField", "Stub Response JSON Value");
		stubResposeJsonValueField.setWidth("100%");
		stubResposeJsonValueField.setHeight(150);
		stubResposeJsonValueField.setColSpan(3);
		SmartGWTUtil.setReadOnlyTextArea(stubResposeJsonValueField);

		ButtonItem stubResposeValueEditButton = new ButtonItem("stubResponseStatusCodeEditButton", "Edit");
		stubResposeValueEditButton.setWidth(100);
		stubResposeValueEditButton.setStartRow(false);
		stubResposeValueEditButton.setColSpan(4);
		stubResposeValueEditButton.setAlign(Alignment.RIGHT);
		stubResposeValueEditButton.addClickHandler(this::onClickStubResposeValueEditButton);

		openApiField = new TextAreaItem("openApiField", "OpenAPI Specification");
		openApiField.setWidth("100%");
		openApiField.setHeight(150);
		openApiField.setColSpan(3);
		SmartGWTUtil.setReadOnlyTextArea(openApiField);

		ButtonItem openApiEditButton = new ButtonItem("openApiEditButton", "Edit");
		openApiEditButton.setWidth(100);
		openApiEditButton.setStartRow(false);
		openApiEditButton.setColSpan(4);
		openApiEditButton.setAlign(Alignment.RIGHT);
		openApiEditButton.addClickHandler(this::onClickOpenApiEditButton);

		form = new DynamicForm();
		form.setNumCols(5);
		form.setItems(returnStubResponseField, stubResponseStatusValueField, stubResposeJsonValueField, stubResposeValueEditButton, openApiField,
				openApiEditButton);
		addMember(form);
	}

	/**
	 * WebAPI定義の設定情報を、本画面領域に反映します。
	 * @param definition WebAPI定義
	 */
	public void setDefinition(WebApiDefinition definition) {
		returnStubResponseField.setValue(definition.isReturnStubResponse());
		stubResponseStatusValueField.setValue(definition.getStubResponseStatusValue());
		stubResposeJsonValueField.setValue(definition.getStubResponseJsonValue());
		openApiField.setValue(definition.getOpenApi());
	}

	/**
	 * 本画面領域の設定情報を、WebAPI定義に反映します。
	 * @param definition WebAPI定義
	 * @return WebAPI定義
	 */
	public WebApiDefinition getDefinition(WebApiDefinition definition) {
		definition.setReturnStubResponse(SmartGWTUtil.getBooleanValue(returnStubResponseField));
		definition.setStubResponseStatusValue(SmartGWTUtil.getStringValue(stubResponseStatusValueField));
		definition.setStubResponseJsonValue(SmartGWTUtil.getStringValue(stubResposeJsonValueField));
		definition.setOpenApi(SmartGWTUtil.getStringValue(openApiField));

		return definition;
	}

	private void onClickStubResposeValueEditButton(ClickEvent event) {
		MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.JSON,
				SmartGWTUtil.getStringValue(stubResposeJsonValueField),
				"Stub Respose JSON Value",
				"FIXME 修正する",
				null,
				new ScriptEditorDialogHandler() {

			@Override
			public void onSave(String text) {
				stubResposeJsonValueField.setValue(text);
			}

			@Override
			public void onCancel() {
			}
		});
	}

	private void onClickOpenApiEditButton(ClickEvent event) {
		MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.YAML,
				SmartGWTUtil.getStringValue(openApiField),
				"OpenAPI Specification",
				"FIXME 修正する",
				null,
				new ScriptEditorDialogHandler() {

			@Override
			public void onSave(String text) {
				openApiField.setValue(text);
			}

			@Override
			public void onCancel() {
			}

			@Override
			public void onSaveDialogSetting(ScriptEditorDialogSetting setting) {
				// FIXME これで ScriptEditorDialogMode を取得する
				setting.getMode();
			}
		});

	}
}
