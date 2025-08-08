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

import java.util.function.BiConsumer;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.AdminSingleUploader;
import org.iplass.adminconsole.client.base.io.upload.UploadResultInfo;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant;
import org.iplass.mtp.webapi.openapi.OpenApiFileType;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Hidden;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * OpenAPI(Swagger)Support 用のインポートダイアログ
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportImportDialog extends AbstractOpenApiSupportDialog {
	/** ファイルアップローダー */
	private AdminSingleUploader uploader;
	/** バージョン */
	private SelectItem version;
	/** メッセージタブ */
	private MessageTabSet messageTabSet;
	/** ダイアログクローズ時処理 */
	private Runnable dialogCloseHandler;

	@Override
	protected String getDialogTitle() {
		return "OpenApi(Swagger) Import Dialog";
	}

	@Override
	protected String getActionButtonLabel() {
		return "Import";
	}

	@Override
	protected void onBeforeCreateForm(VLayout container) {
		HLayout fileComposit = new HLayout(5);
		fileComposit.setWidth100();
		fileComposit.setHeight(25);

		Label fileLabel = new Label("File :");
		fileLabel.setWrap(false);
		fileLabel.setAlign(Alignment.RIGHT);
		fileLabel.addStyleName("formTitle");
		fileComposit.addMember(fileLabel);

		uploader = new AdminSingleUploader(OpenApiSupportRpcConstant.Import.SERVICE_NAME);
		uploader.setValidExtensions(OpenApiFileType.getAllExtensions());
		uploader.addOnStartUploadHandler((result) -> {
			if (!result.isCanceled()) {
				uploader.debugUploader("onStart");
				startImport();
			} else {
				uploader.removeHidden();
			}
		});
		uploader.addOnFinishUploadHandler((result) -> {
			uploader.debugUploader("onFinish");
			if (uploader.getLastUploadState().isSuccess()) {
				finishImport(uploader.getLastUploadState().getData());
			} else {
				errorImport(uploader.getLastUploadState().getErrorMessage());
			}

			//Hidden項目の削除
			uploader.removeHidden();
		});

		fileComposit.addMember(uploader);
		container.addMember(fileComposit);
	}

	@Override
	protected void onCreateForm(DynamicForm form) {
		version = createVersionSelectItem();
		form.setItems(version);

	}

	@Override
	protected void onAfterCreateContainer(VLayout container) {
		// メッセージタブセットの作成。footer の下に設定する
		messageTabSet = new MessageTabSet();
		messageTabSet.setHeight(250);
		container.addMember(messageTabSet);
	}

	@Override
	protected void onClickAction(ClickEvent event, DynamicForm form) {
		if (uploader.getFileName() == null || uploader.getFileName().isEmpty()) {
			SC.warn(AdminClientMessageUtil.getString("ui_tools_openapisupport_OpenApiSupportImportDialog_selectImportFile"));
			return;
		}

		uploader.add(new Hidden(UploadProperty.TENANT_ID, Integer.toString(TenantInfoHolder.getId())));
		uploader.add(new Hidden(OpenApiSupportRpcConstant.Import.Parameter.VERSION, version.getValueAsString()));
		uploader.submit();

		uploader.getLastUploadState();
	}

	/**
	 * インポート開始時処理
	 */
	private void startImport() {
		disableFooter();
	}

	/**
	 * インポート完了時処理
	 * @param message サーバーメッセージ
	 */
	private void finishImport(JSONValue message) {
		//JSON->Result
		OpenApiImportResultInfo result = new OpenApiImportResultInfo(message);

		// 結果からダイアログにログ出力する。
		messageTabSet.addMessage("[ Import Result. File: " + result.getFileName() + " ]");
		// 詳細メッセージを出力
		result.detailsStream((isSuccess, detailMessage) -> {
			if (isSuccess) {
				messageTabSet.addMessage(detailMessage);
			} else {
				messageTabSet.addWarnMessage(detailMessage);
			}
		});
		// 末尾に空行を追加
		messageTabSet.addMessage("");

		enableFooter();
	}

	/**
	 * インポートエラー時処理
	 * @param message サーバーメッセージ
	 */
	private void errorImport(String message) {
		messageTabSet.addErrorMessage(message);
	}

	@Override
	protected boolean onPreDestroy() {
		if (dialogCloseHandler != null) {
			dialogCloseHandler.run();
		}
		dialogCloseHandler = null;
		version = null;
		uploader = null;
		messageTabSet = null;
		return super.onPreDestroy();
	}

	/**
	 * ダイアログクローズ時のハンドラを設定します。
	 * @param dialogCloseHandler ダイアログクローズ時のハンドラ
	 */
	public void setDialogCloseHandler(Runnable dialogCloseHandler) {
		this.dialogCloseHandler = dialogCloseHandler;
	}

	/**
	 * インポート結果
	 */
	private static class OpenApiImportResultInfo extends UploadResultInfo {

		/**
		 * コンストラクタ
		 * @param json サーバーメッセージ
		 */
		public OpenApiImportResultInfo(JSONValue json) {
			super(json);
		}

		/**
		 * インポートされたファイル名を取得します。
		 * @return インポートされたファイル名
		 */
		public String getFileName() {
			return getValue(OpenApiSupportRpcConstant.Import.Response.FILE_NAME);
		}

		/**
		 * インポート詳細メッセージのストリーム処理を行います。
		 * <p>
		 * コールバック引数は以下の通りです。<br>
		 * 第一引数： 更新が成功しているか。成功の場合 true を設定。<br>
		 * 第二引数： 詳細メッセージ
		 * </p>
		 * @param detailMessageCallback インポート詳細メッセージのコールバック関数
		 */
		public void detailsStream(BiConsumer<Boolean, String> detailMessageCallback) {
			JSONObject jsonObject = getRootValue().isObject();
			JSONValue value = jsonObject.get(OpenApiSupportRpcConstant.Import.Response.DETAILS);
			if (value != null) {
				JSONArray array = value.isArray();
				for (int i = 0; i < array.size(); i++) {
					JSONObject child = array.get(i).isObject();

					// キー名は org.iplass.mtp.impl.webapi.openapi.OpenApiImportResult のフィールド名
					var openApiPath = child.get("openApiPath").isString().stringValue();
					var webApiDefinitionPath = child.get("webApiDefinitionPath").isString().stringValue();
					var updateType = child.get("updateType").isString().stringValue();
					var updateState = child.get("updateState").isString().stringValue();

					// 更新が成功（SUCCESS）であるか判定
					var isSuccess = updateState.equals("SUCCESS");
					var message = "From OpenAPI paths: " + openApiPath + ", To WebAPI path: " + webApiDefinitionPath + ", Result: " + updateType + "("
							+ updateState + ")";
					detailMessageCallback.accept(isSuccess, message);
				}
			}
		}
	}

}

