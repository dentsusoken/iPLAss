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
import org.iplass.mtp.webapi.openapi.OpenApiVersion;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * オプション属性スタブタブ用の入力領域
 * @author SEKIGUCHI Naoya
 */
public class WebApiOptionalStubTabPane extends VLayout {
	/** 入力フォーム */
	private DynamicForm form;
	/** 公開WebAPI */
	private CheckboxItem returnStubResponseField;
	/** スタブレスポンス "status" の値フィールド */
	private TextItem stubResponseStatusValueField;
	/** スタブレスポンスフィールド */
	private TextAreaItem stubResposeJsonValueField;

	/**
	 * コンストラクタ
	 */
	public WebApiOptionalStubTabPane() {
		setMargin(5);
		setAutoHeight();
		setWidth100();
		setOverflow(Overflow.VISIBLE);

		returnStubResponseField = new CheckboxItem("returnStubResponseField", "Return Stub Response");
		returnStubResponseField.setWidth("100%");
		SmartGWTUtil.addHoverToFormItem(returnStubResponseField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOptionalStubTabPane_returnStubResponseField_hoverMessage"));

		stubResponseStatusValueField = new TextItem("stubResponseStatusValueField", "Stub Response Staus Value");
		stubResponseStatusValueField.setWidth("100%");
		SmartGWTUtil.addHoverToFormItem(stubResponseStatusValueField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOptionalStubTabPane_stubResponseStatusValueField_hoverMessage"));

		stubResposeJsonValueField = new TextAreaItem("stubResposeJsonValueField", "Stub Response JSON Value");
		stubResposeJsonValueField.setWidth("100%");
		stubResposeJsonValueField.setHeight(150);
		stubResposeJsonValueField.setColSpan(2);
		SmartGWTUtil.addHoverToFormItem(stubResposeJsonValueField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOptionalStubTabPane_stubResposeJsonValueField_hoverMessage"));
		SmartGWTUtil.setReadOnlyTextArea(stubResposeJsonValueField);

		SpacerItem stubResposeValueEditButtonSpacer = new SpacerItem();
		ButtonItem stubResposeValueEditButton = new ButtonItem("stubResponseStatusCodeEditButton", "Edit");
		stubResposeValueEditButton.setWidth(100);
		stubResposeValueEditButton.setStartRow(false);
		stubResposeValueEditButton.addClickHandler(this::onClickStubResposeValueEditButton);

		form = new DynamicForm();
		form.setColWidths(150, 300, "*");
		form.setNumCols(3);
		form.setItems(returnStubResponseField, stubResponseStatusValueField, stubResposeJsonValueField, stubResposeValueEditButtonSpacer,
				stubResposeValueEditButton);
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

		return definition;
	}

	/**
	 * スタブレスポンスJSON値編集ボタンがクリックされたときの処理
	 * @param event クリックイベント
	 */
	private void onClickStubResposeValueEditButton(ClickEvent event) {
		MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.JSON,
				SmartGWTUtil.getStringValue(stubResposeJsonValueField),
				"Stub Respose JSON Value",
				"ui_metadata_webapi_WebApiOptionalStubTabPane_stubResposeJsonValueField_editorHint",
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
