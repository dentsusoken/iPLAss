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

import java.util.HashMap;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.openapi.OpenApiVersion;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * OpenAPI スタブタブ用の入力領域
 * @author SEKIGUCHI Naoya
 */
public class WebApiOpenApiStubTabPane extends VLayout {
	/** 入力フォーム */
	private DynamicForm form;
	/** 公開WebAPI */
	private CheckboxItem returnStubResponseField;
	/** スタブレスポンスフィールド */
	private TextAreaItem stubResponseJsonValueField;

	/**
	 * コンストラクタ
	 */
	public WebApiOpenApiStubTabPane() {
		setMargin(5);
		setAutoHeight();
		setWidth100();
		setOverflow(Overflow.VISIBLE);

		returnStubResponseField = new CheckboxItem("returnStubResponseField", "Return Stub Response");
		returnStubResponseField.setWidth("100%");
		SmartGWTUtil.addHoverToFormItem(returnStubResponseField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiStubTabPane_returnStubResponseField_hoverMessage"));

		stubResponseJsonValueField = new TextAreaItem("stubResponseJsonValueField", "Stub Response JSON Value");
		stubResponseJsonValueField.setWidth("100%");
		stubResponseJsonValueField.setHeight(150);
		stubResponseJsonValueField.setColSpan(2);
		SmartGWTUtil.addHoverToFormItem(stubResponseJsonValueField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiStubTabPane_stubResponseJsonValueField_hoverMessage"));
		SmartGWTUtil.setReadOnlyTextArea(stubResponseJsonValueField);

		SpacerItem stubResposeValueEditButtonSpacer = new SpacerItem();
		ButtonItem stubResposeValueEditButton = new ButtonItem("stubResponseStatusCodeEditButton", "Edit");
		stubResposeValueEditButton.setWidth(100);
		stubResposeValueEditButton.setStartRow(false);
		stubResposeValueEditButton.addClickHandler(this::onClickStubResposeValueEditButton);

		form = new DynamicForm();
		form.setColWidths(150, 300, "*");
		form.setNumCols(3);
		form.setItems(returnStubResponseField, stubResponseJsonValueField, stubResposeValueEditButtonSpacer,
				stubResposeValueEditButton);
		addMember(form);
	}

	/**
	 * WebAPI定義の設定情報を、本画面領域に反映します。
	 * @param definition WebAPI定義
	 */
	public void setDefinition(WebApiDefinition definition) {
		returnStubResponseField.setValue(definition.isReturnStubResponse());
		stubResponseJsonValueField.setValue(definition.getStubResponseJsonValue());
	}

	/**
	 * 本画面領域の設定情報を、WebAPI定義に反映します。
	 * @param definition WebAPI定義
	 * @return WebAPI定義
	 */
	public WebApiDefinition getDefinition(WebApiDefinition definition) {
		definition.setReturnStubResponse(SmartGWTUtil.getBooleanValue(returnStubResponseField));
		definition.setStubResponseJsonValue(SmartGWTUtil.getStringValue(stubResponseJsonValueField));

		return definition;
	}

	/**
	 * スタブレスポンスJSON値編集ボタンがクリックされたときの処理
	 * @param event クリックイベント
	 */
	private void onClickStubResposeValueEditButton(ClickEvent event) {
		MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.JSON,
				SmartGWTUtil.getStringValue(stubResponseJsonValueField),
				"Stub Respose JSON Value",
				"ui_metadata_webapi_WebApiOpenApiStubTabPane_stubResponseJsonValueField_editorHint",
				null,
				new ScriptEditorDialogHandler() {

			@Override
			public void onSave(String text) {
				stubResponseJsonValueField.setValue(text);
			}

			@Override
			public void onCancel() {
			}
		});
	}

	/**
	 * OpenAPIのバージョンと値のマップを作成します。
	 * <p>
	 * SelectItem の valueMap に設定するためのマップを作成します。
	 * </p>
	 * @return バージョンと値のマップ
	 */
	protected Map<String, String> createVersionValueMap() {
		Map<String, String> versionValueMap = new HashMap<>();
		for (OpenApiVersion version : OpenApiVersion.values()) {
			versionValueMap.put(version.getSeriesVersion(), version.getSeriesVersion());
		}
		return versionValueMap;
	}

}
