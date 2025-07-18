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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.AdminSingleUploader;
import org.iplass.adminconsole.client.base.io.upload.UploadResultInfo;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ConfigUploadProperty;
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
		messageTabSet.setHeight(150);
		container.addMember(messageTabSet);
	}

	@Override
	protected void onClickAction(ClickEvent event, DynamicForm form) {
		if (uploader.getFileName() == null || uploader.getFileName().isEmpty()) {
			// FIXME メッセージ見直し
			SC.warn(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_selectImportFile"));
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
		// FIXME ここを実装する
		//JSON->Result
		OpenApiImportResultInfo result = new OpenApiImportResultInfo(message);

		// 結果からダイアログにログ出力する。
		messageTabSet.addMessage("[ --- import " + result.getFileName() + " --- ]");
		messageTabSet.addMessage(result.getDetails());

		enableFooter();
	}

	/**
	 * インポートエラー時処理
	 * @param message サーバーメッセージ
	 */
	private void errorImport(String message) {
//		messageTabSet.setErrorMessage(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_uploadingErr"));
//
//		disableComponent(false);
//		messageTabSet.setTabTitleNormal();
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

		public String getFileName() {
			return getValue("fileName");
		}
		/**
		 * インポート詳細メッセージを取得します
		 * @return インポート詳細メッセージのリスト
		 */
		public List<String> getDetails() {
			List<String> details = new ArrayList<>();
			JSONObject jsonObject = getRootValue().isObject();
			// FIXME リテラルを修正すること
			JSONValue value = jsonObject.get("details");
			if (value != null) {
				JSONArray array = value.isArray();
				for (int i = 0; i < array.size(); i++) {
					JSONObject child = array.get(i).isObject();

					var openApiPath = child.get("openApiPath").isString().stringValue();
					var webApiDefinitionPath = child.get("webApiDefinitionPath").isString().stringValue();
					var updateType = child.get("updateType").isString().stringValue();
					var updateState = child.get("updateState").isString().stringValue();

					// FIXME メッセージコードから取得し、置換文字列を設定する
					details.add(openApiPath + " -> " + webApiDefinitionPath + " : " + updateType + "(" + updateState + ")");
				}
			}
			return details;

		}

//		public List<String> getLog() {
//			List<String> logs = new ArrayList<>();
//			logs.add(getFileUploadStatusMessage(getStatus()));
//			if (getStatusMessages() != null && !getStatusMessages().isEmpty()) {
//				logs.add("-----------------------------------");
//				logs.addAll(getStatusMessages());
//			}
//			if (getMessages() != null && !getMessages().isEmpty()) {
//				logs.add("-----------------------------------");
//				logs.addAll(getMessages());
//			}
//			logs.add("-----------------------------------");
//			return logs;
//		}

		private String getFileUploadFileOid() {
			return getValue(ConfigUploadProperty.FILE_OID);
		}

//		private String getFileUploadStatusMessage(String status) {
//			if (ConfigUploadProperty.Status.SUCCESS.name().equals(status)) {
//				return AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_uploadSuccessful");
//			} else if (ConfigUploadProperty.Status.WARN.name().equals(status)) {
//				return AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_uploadWarn");
//			} else if (ConfigUploadProperty.Status.ERROR.name().equals(status)) {
//				return AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_uploadErr");
//			} else {
//				return AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataXMLUploadDialog_notGetUploadResult");
//			}
//		}
	}

}
