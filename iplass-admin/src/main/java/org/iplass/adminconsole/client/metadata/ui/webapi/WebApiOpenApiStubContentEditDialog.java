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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.webapi.definition.WebApiStubContent;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * WebAPIスタブコンテンツ編集ダイアログ
 * @author SEKIGUCHI Naoya
 */
public class WebApiOpenApiStubContentEditDialog extends MtpDialog {
	/** 保存時処理 */
	private Consumer<WebApiStubContent> onSaveHandler;

	/** 入力フォーム */
	private DynamicForm form;
	/** コンテンツタイプフィールド */
	private ComboBoxItem contentTypeField;
	/** ラベルフィールド */
	private TextItem labelField;
	/** コンテンツフィールド */
	private TextAreaItem contentField;

	/**
	 * コンストラクタ
	 * @param onSaveHandler 保存時処理
	 * @param responseType レスポンスタイプ（コンテンツタイプの選択肢を生成するために使用）
	 */
	public WebApiOpenApiStubContentEditDialog(Consumer<WebApiStubContent> onSaveHandler, String responseType) {
		this.onSaveHandler = onSaveHandler;

		setHeight(300);
		setTitle("Stub Content Setting");
		centerInPage();

		contentTypeField = new ComboBoxItem("contentTypeField", "Content Type");
		contentTypeField.setWidth("100%");
		contentTypeField.setRequired(Boolean.TRUE);
		contentTypeField.setValueMap(convertContentTypeFieldValueMap(responseType));
		SmartGWTUtil.addHoverToFormItem(contentTypeField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiStubContentEditDialog_contentTypeField_hoverMessage"));

		labelField = new TextItem("labelField", "Label");
		labelField.setWidth("100%");
		labelField.setRequired(Boolean.TRUE);
		SmartGWTUtil.addHoverToFormItem(labelField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiStubContentEditDialog_labelField_hoverMessage"));

		contentField = new TextAreaItem("contentField", "Content");
		contentField.setWidth("100%");
		contentField.setRequired(Boolean.TRUE);
		SmartGWTUtil.addHoverToFormItem(contentField,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiStubContentEditDialog_contentField_hoverMessage"));
		SmartGWTUtil.setReadOnlyTextArea(contentField);

		SpacerItem contentEditButtonSpacer = new SpacerItem();
		ButtonItem contentEditButton = new ButtonItem("contentEditButton", "Edit");
		contentEditButton.addClickHandler(this::onClickContentEditButton);
		contentEditButton.setStartRow(false);
		contentEditButton.setWidth(100);

		form = new DynamicForm();
		form.setColWidths(120, "*");
		form.setNumCols(2);
		form.setItems(contentTypeField, labelField, contentField, contentEditButtonSpacer, contentEditButton);

		container.addMember(form);

		IButton saveButton = new IButton("Save");
		saveButton.addClickHandler(this::onClickSave);

		IButton cancelButton = new IButton("Cancel");
		cancelButton.addClickHandler(this::onClickCancel);

		footer.addMember(saveButton);
		footer.addMember(cancelButton);
	}

	/**
	 * 画面に初期値を設定します。
	 * <p>
	 * 編集の場合に使用してください。
	 * </p>
	 * @param stubContent スタブコンテンツ
	 */
	public void setValue(WebApiStubContent stubContent) {
		contentTypeField.setValue(stubContent.getContentType());
		labelField.setValue(stubContent.getLabel());
		contentField.setValue(stubContent.getContent());
	}

	@Override
	protected boolean onPreDestroy() {
		onSaveHandler = null;

		form = null;
		contentTypeField = null;
		labelField = null;
		contentField = null;

		return super.onPreDestroy();
	}

	/**
	 * コンテンツ編集ボタンがクリックされたときの処理
	 * @param event クリックイベント
	 */
	private void onClickContentEditButton(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
		var contentType = SmartGWTUtil.getStringValue(contentTypeField);
		MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.TEXT,
				SmartGWTUtil.getStringValue(contentField),
				"Edit Stub Content (" + contentType + ")",
				null,
				AdminClientMessageUtil.getString("ui_metadata_webapi_WebApiOpenApiStubContentEditDialog_contentField_editorHint", contentType),
				new ScriptEditorDialogHandler() {

			@Override
			public void onSave(String text) {
				contentField.setValue(text);
			}

			@Override
			public void onCancel() {
			}
		});
	}

	/**
	 * フッターの保存ボタンがクリックされたときの処理
	 * @param event クリックイベント
	 */
	private void onClickSave(ClickEvent event) {
		if (!form.validate()) {
			return;
		}

		try {
			WebApiStubContent stubContent = new WebApiStubContent();
			stubContent.setContentType(SmartGWTUtil.getStringValue(contentTypeField));
			stubContent.setLabel(SmartGWTUtil.getStringValue(labelField));
			stubContent.setContent(SmartGWTUtil.getStringValue(contentField));
			onSaveHandler.accept(stubContent);

		} finally {
			destroy();

		}
	}

	/**
	 * フッターのキャンセルボタンがクリックされたときの処理
	 * @param event クリックイベント
	 */
	private void onClickCancel(ClickEvent event) {
		destroy();
	}

	/**
	 * レスポンスタイプ文字列をコンテンツタイプフィールドの値マップに変換します。
	 * @param responseType レスポンスタイプ文字列
	 * @return コンテンツタイプフィールドの値マップ
	 */
	private Map<String, String> convertContentTypeFieldValueMap(String responseType) {
		var responseTypeList = SmartGWTUtil.convertStringToList(responseType, ",");
		if (null == responseTypeList) {
			return Collections.emptyMap();
		}

		Map<String, String> valueMap = new HashMap<>();
		for (var responseTypeEntry : responseTypeList) {
			var trimed = responseTypeEntry.trim();
			valueMap.put(trimed, trimed);
		}
		return valueMap;
	}
}
