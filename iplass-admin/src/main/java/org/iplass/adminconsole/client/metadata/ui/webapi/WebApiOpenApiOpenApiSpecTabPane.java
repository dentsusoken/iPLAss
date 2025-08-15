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
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogSetting;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.openapi.OpenApiVersion;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * OpenAPI OpenAPI Spec. タブ用の入力領域
 * @author SEKIGUCHI Naoya
 */
public class WebApiOpenApiOpenApiSpecTabPane extends VLayout {
	/** 入力フォーム */
	private DynamicForm form;
	/** OpenAPI バージョンフィールド */
	private SelectItem openApiVersionField;
	/** OpenAPI 定義フィールド */
	private TextAreaItem openApiField;
	/** OpenAPI ファイルタイプ */
	private String openApiFileType;

	/**
	 * コンストラクタ
	 */
	public WebApiOpenApiOpenApiSpecTabPane() {
		setMargin(5);
		setAutoHeight();
		setWidth100();
		setOverflow(Overflow.VISIBLE);

		openApiVersionField = new SelectItem("openApiVersionField", "OpenAPI Version");
		openApiVersionField.setValueMap(createVersionValueMap());
		openApiVersionField.setDefaultValue(OpenApiVersion.V31.getSeriesVersion());
		SmartGWTUtil.addHoverToFormItem(openApiVersionField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiOpenApiSpecTabPane_openApiVersionField_hoverMessage"));

		openApiField = new TextAreaItem("openApiField", "OpenAPI Spec");
		openApiField.setWidth("100%");
		openApiField.setHeight(150);
		openApiField.setColSpan(2);
		SmartGWTUtil.setReadOnlyTextArea(openApiField);
		SmartGWTUtil.addHoverToFormItem(openApiField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiOpenApiSpecTabPane_openApiField_hoverMessage"));

		SpacerItem openApiEditButtonSpacer = new SpacerItem();
		ButtonItem openApiEditButton = new ButtonItem("openApiEditButton", "Edit");
		openApiEditButton.setWidth(100);
		openApiEditButton.setStartRow(false);
		openApiEditButton.addClickHandler(this::onClickOpenApiEditButton);

		form = new DynamicForm();
		form.setColWidths(120, 300, "*");
		form.setNumCols(3);
		form.setItems(openApiVersionField, openApiField, openApiEditButtonSpacer, openApiEditButton);
		addMember(form);
	}

	/**
	 * WebAPI定義の設定情報を、本画面領域に反映します。
	 * @param definition WebAPI定義
	 */
	public void setDefinition(WebApiDefinition definition) {
		openApiVersionField.setValue(definition.getOpenApiVersion());
		openApiFileType = definition.getOpenApiFileType();
		openApiField.setValue(definition.getOpenApi());
	}

	/**
	 * 本画面領域の設定情報を、WebAPI定義に反映します。
	 * @param definition WebAPI定義
	 * @return WebAPI定義
	 */
	public WebApiDefinition getDefinition(WebApiDefinition definition) {
		definition.setOpenApiVersion(SmartGWTUtil.getStringValue(openApiVersionField));
		definition.setOpenApiFileType(openApiFileType);
		definition.setOpenApi(SmartGWTUtil.getStringValue(openApiField));

		return definition;
	}

	@Override
	public void destroy() {
		form = null;
		openApiVersionField = null;
		openApiField = null;
		openApiFileType = null;

		super.destroy();
	}
	/**
	 * OpenAPIの編集ボタンがクリックされたときの処理を行います。
	 * @param event クリックイベント
	 */
	private void onClickOpenApiEditButton(ClickEvent event) {
		ScriptEditorDialogMode mode = ScriptEditorDialogMode.getMode(openApiFileType);
		if (mode == null || (ScriptEditorDialogMode.JSON != mode && ScriptEditorDialogMode.YAML != mode)) {
			// 想定外のフォーマットであれば YAML を設定する
			mode = ScriptEditorDialogMode.YAML;
		}

		MetaDataUtil.showScriptEditDialog(mode,
				SmartGWTUtil.getStringValue(openApiField),
				"OpenAPI Specification",
				"ui_metadata_webapi_WebApiOpenApiOpenApiSpecTabPane_openApiField_editorHint",
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
				// ScriptEditorDialogMode を取得する
				openApiFileType = setting.getMode().name();
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
