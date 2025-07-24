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
package org.iplass.adminconsole.client.tools.ui.openapisupport;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;

/**
 * OpenAPI(Swagger)Support 用のエクスポートダイアログ
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportExportDialog extends AbstractOpenApiSupportDialog {
	/** バージョンフィールド */
	private SelectItem versionField;
	/** ファイルタイプフィールド */
	private SelectItem fileTypeField;

	/** Exportクリック時処理 */
	private ClickExportEventHandler clickExportHandler;

	/**
	 * Exportボタンクリックイベントハンドラ
	 */
	@FunctionalInterface
	public interface ClickExportEventHandler {
		/**
		 * Exportボタンクリック時の処理を実行します。
		 * @param event クリックイベント
		 * @param version OpenAPIのバージョン
		 * @param fileType ファイルタイプ
		 */
		void execute(ClickEvent event, String version, String fileType);
	}

	/**
	 * Exportボタンクリックイベントハンドラを設定します。
	 * @param handler Exportボタンクリックイベントハンドラ
	 */
	public void setExportAction(ClickExportEventHandler handler) {
		this.clickExportHandler = handler;
	}

	@Override
	protected String getDialogTitle() {
		return "OpenApi(Swagger) Export Dialog";
	}

	@Override
	protected String getActionButtonLabel() {
		return "Export";
	}

	@Override
	protected void onCreateForm(DynamicForm form) {
		versionField = createVersionSelectItem();
		fileTypeField = createFileTypeSelectItem();

		form.setItems(versionField, fileTypeField);
	}

	@Override
	protected void onClickAction(ClickEvent event, DynamicForm form) {
		if (null != clickExportHandler) {
			String version = SmartGWTUtil.getStringValue(versionField);
			String fileType = SmartGWTUtil.getStringValue(fileTypeField);
			clickExportHandler.execute(event, version, fileType);
		}

		destroy();
	}

	@Override
	protected boolean onPreDestroy() {
		versionField = null;
		fileTypeField = null;
		clickExportHandler = null;

		return super.onPreDestroy();
	}
}
