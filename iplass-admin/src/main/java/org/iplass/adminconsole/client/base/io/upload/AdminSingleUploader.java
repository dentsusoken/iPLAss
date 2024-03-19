/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.io.upload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;

/**
 * AdminConsole用 単一ファイルアップローダー
 *
 * <p>
 * 単一ファイルをアップロードするクライアント機能。
 * </p>
 *
 * <p>
 * ノード構成イメージ
 *
 * <pre>
 * XsrfProtectedMultipartForm
 *  |
 *  +-- FileUpload
 *  |
 *  +-- FlowPanel
 *       |
 *       +-- add Widgets
 * </pre>
 * </p>
 *
 * <p>
 * NOTE: ライブラリ gwtupload を利用していたが、利用しない形式に変更
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class AdminSingleUploader extends Composite {
	/** XsrfProtectedMultipartForm */
	private XsrfProtectedMultipartForm multipartFormPanel = new XsrfProtectedMultipartForm();
	/** FileUpload ウィジェット */
	private FileUpload fileUpload = new FileUpload();
	/** 追加ウィジェットパネル */
	private FlowPanel addWidgetPanel = new FlowPanel();
	/** 最終アップロード状態 */
	private UploadState lastUploadState = new UploadState();
	/** アップロード可能なファイル拡張子を指定する */
	private List<String> validExtensionList = new ArrayList<>();

	/** 拡張子チェック submit HandlerRegistration */
	private HandlerRegistration validateExtensionSubmitHandlerRegistration;
	/** 状態変更 submit complete HandlerRegistration */
	private HandlerRegistration changeStateSubmitCompleteHandlerRegistration;
	/** ファイルクリア submit complete HandlerRegistration */
	private HandlerRegistration clearFileSubmitCompleteHandlerRegistration;

	/**
	 * デフォルトコンストラクタ
	 * @param service リクエストサービス名を設定する
	 */
	public AdminSingleUploader(String service) {
		multipartFormPanel.setService(service);
		multipartFormPanel.setClassAttribute("adminSingleUploader");
		// submit 開始時イベント
		validateExtensionSubmitHandlerRegistration = multipartFormPanel.addSubmitHandler(event -> {
			// 拡張子チェック
			if (!validExtensionList.isEmpty() && !validExtensionList.contains(getFileUploadFileExtension())) {
				// 拡張子が存在しなければ、イベントをキャンセルしメッセージ表示
				event.cancel();
				// submit 時に登録したファイルクリアハンドラを削除
				removeSubmitCompleteClearFileHandler();

				SC.warn(AdminClientMessageUtil.getString("base_io_upload_AdminSingleUploader_message_invalidFileExtension", validExtensionList));
			}
		});
		// submit 完了時イベント
		changeStateSubmitCompleteHandlerRegistration = multipartFormPanel.addSubmitCompleteHandler(event -> {
			AdminUploadActionResponse response = AdminUploadActionResponseParser.parse(event.getResults());

			// 状態を変更する
			lastUploadState.changeState(response);
		});

		fileUpload.setName("singleFile");

		multipartFormPanel.insertAfter(fileUpload);
		multipartFormPanel.insertAfter(addWidgetPanel);

		initWidget(multipartFormPanel);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		// イベント破棄
		validateExtensionSubmitHandlerRegistration.removeHandler();
		changeStateSubmitCompleteHandlerRegistration.removeHandler();

		removeSubmitCompleteClearFileHandler();
	}

	/**
	 * アップロード可能なファイル拡張子を設定する
	 *
	 * <p>
	 * パラメータの拡張子を input 要素の accept 属性に設定する。
	 * 拡張子の値が存在しない場合は accept 属性を削除する。
	 * </p>
	 *
	 * @param extensions 拡張子
	 */
	public void setValidExtensions(String... extensions) {
		// 許可拡張子をクリア
		validExtensionList.clear();

		// アップロードを許可する拡張子を設定
		StringBuilder acceptValue = new StringBuilder();
		if (null != extensions && 0 < extensions.length) {
			for (String ext : extensions) {
				if (null != ext && 0 < ext.length()) {
					String validExtension = new StringBuilder(".").append(ext).toString();
					acceptValue.append(",").append(validExtension);
				}
			}
		}

		if (0 < acceptValue.length()) {
			acceptValue.deleteCharAt(0);
			String accept = acceptValue.toString();
			fileUpload.getElement().setAttribute("accept", accept);

			// accept に設定する拡張子一覧を設定
			validExtensionList.addAll(Arrays.asList(accept.split(",")));

		} else if (fileUpload.getElement().hasAttribute("accept")) {
			fileUpload.getElement().removeAttribute("accept");

		}
	}

	/**
	 * Hidden ウィジェットを追加する
	 *
	 * @param widget 追加対象ウィジェット
	 */
	public void add(Hidden widget) {
		addWidgetPanel.insert(widget, 0);
	}

	/**
	 * 追加した Hidden ノードを削除する
	 */
	public void removeHidden() {
		removeAll(w -> w instanceof Hidden);
	}

	/**
	 * 追加されたウィジェットを全て削除する
	 *
	 * @param ignoreFn 対象外とするウィジェットを判定するファンクション
	 */
	private void removeAll(Function<Widget, Boolean> ignoreFn) {
		// NOTE: forEach ループしながら remove すると index がずれる為、全て削除できない。削除対象をリストアップ後に全削除する。
		List<Widget> removeTarget = new ArrayList<>();
		addWidgetPanel.forEach(w -> {
			if (!ignoreFn.apply(w)) {
				removeTarget.add(w);
			}
		});

		removeTarget.forEach(w -> addWidgetPanel.remove(w));
	}

	/**
	 * アップロードファイル名を取得する
	 * @return アップロードファイル名
	 */
	public String getFileName() {
		return fileUpload.getFilename();
	}

	/**
	 * アップロードファイルをクリアする
	 */
	private void clearFile() {
		fileUpload.getElement().<InputElement> cast().setValue(null);
	}

	/**
	 * submit する。
	 *
	 * <p>
	 * submit 完了時最後の処理としてファイルクリア処理を登録する。
	 * submit 時の最後のイベントになるように毎回登録・削除を行う。
	 * エラーなど想定外操作でイベントが残ることを考慮し、最初に削除処理を実行する。
	 * </p>
	 */
	public void submit() {
		// 登録済みイベントがあれば削除する
		removeSubmitCompleteClearFileHandler();
		// イベントを登録する
		registerSubmitCompleteClearFileHandler();
		multipartFormPanel.submit();
	}

	/**
	 * submit 開始時処理を追加する
	 * @param handler ハンドラ
	 * @return HandlerRegistration
	 */
	public HandlerRegistration addOnStartUploadHandler(final SubmitHandler handler) {
		return multipartFormPanel.addSubmitHandler(handler);
	}

	/**
	 * submit 完了時処理を追加する
	 * @param handler ハンドラ
	 * @return HandlerRegistration
	 */
	public HandlerRegistration addOnFinishUploadHandler(final SubmitCompleteHandler handler) {
		return multipartFormPanel.addSubmitCompleteHandler(handler);
	}

	/**
	 * submit 完了時 ファイルクリア処理を登録する
	 */
	private void registerSubmitCompleteClearFileHandler() {
		clearFileSubmitCompleteHandlerRegistration = multipartFormPanel.addSubmitCompleteHandler(event -> {
			// アップロードファイルをクリア
			clearFile();
			// 自ハンドラを削除
			removeSubmitCompleteClearFileHandler();
		});
	}

	/**
	 * submit 完了時 ファイルクリア処理を削除する
	 */
	private void removeSubmitCompleteClearFileHandler() {
		if (null != clearFileSubmitCompleteHandlerRegistration) {
			// ハンドラを削除
			clearFileSubmitCompleteHandlerRegistration.removeHandler();
			clearFileSubmitCompleteHandlerRegistration = null;
		}
	}

	/**
	 * アップロードファイルの拡張子を取得する
	 * @return アップロードファイルの拡張子（.csv 等）
	 */
	private String getFileUploadFileExtension() {
		// ファイル名
		String uploadFileName = getInputElementFileName(this.fileUpload.getElement().<InputElement> cast(), 0);
		// "." 最終位置
		int lastDot = uploadFileName.lastIndexOf(".");
		// 拡張子部を返却
		return uploadFileName.substring(lastDot);
	}

	// 状態
	/**
	 * 最終アップロード状態を取得する
	 * @return 最終アップロード状態
	 */
	public UploadState getLastUploadState() {
		return this.lastUploadState;
	}

	/**
	 * アップロード状態管理
	 */
	public static class UploadState {
		/** レスポンス */
		private AdminUploadActionResponse response;

		/**
		 * アップロード状態を変更する
		 *
		 * @param result 実行結果JSON文字列
		 */
		public void changeState(AdminUploadActionResponse response) {
			this.response = response;
		}

		/**
		 * アップロードの成功状態を判定する。
		 *
		 * @return アップロードの成功常態（true： 成功、false： 失敗）
		 */
		public boolean isSuccess() {
			return response != null ? response.isSuccess() : false;
		}

		/**
		 * エラーメッセージを取得する
		 *
		 * <p>
		 * サーバーエラー発生時（response#isSuccess() が失敗の場合）に、エラーメッセージを返却する。
		 * 失敗以外の場合は null を返却する。
		 * </p>
		 *
		 * @return エラーメッセージ
		 */
		public String getErrorMessage() {
			return response != null ? response.getErrorMessage() : null;
		}

		/**
		 * アプリデータを取得する
		 *
		 * @return アプリデータ
		 */
		public JSONValue getData() {
			return response != null ? response.getData() : null;
		}
	}

	// デバッグ機能
	/**
	 * ログを出力します。
	 *
	 * @param eventName イベント名
	 */
	public void debugUploader(String eventName) {
		GWT.log(eventName + ": isSuccess                   ->" + lastUploadState.isSuccess());

		InputElement file = fileUpload.getElement().<InputElement> cast();
		if (0 == getInputElementFileLength(file)) {
			GWT.log(eventName + ": UploadedInfo is Null.");

		} else {
			GWT.log(eventName + ": File name                ->" + getInputElementFileName(file, 0));
			GWT.log(eventName + ": File content-type        ->" + getInputElementFileContentType(file, 0));
			GWT.log(eventName + ": File size                ->" + getInputElementFileSize(file, 0));
		}
		GWT.log(eventName + ": message                  ->" + lastUploadState.getData());
	}

	/**
	 * InputElement の files プロパティの length を取得します
	 * @param element InputElement
	 * @return files プロパティの length
	 */
	private native int getInputElementFileLength(InputElement element) /*-{
		return element.files.length;
	}-*/;

	/**
	 * InputElement の files[0].name プロパティを取得します
	 * @param element InputElement
	 * @param fileIndex ファイル位置
	 * @return files[fileIndex].name プロパティを取得します
	 */
	private native String getInputElementFileName(InputElement element, int fileIndex) /*-{
		return element.files[fileIndex].name;
	}-*/;

	/**
	 * InputElement の files[0].type プロパティを取得します
	 * @param element InputElement
	 * @param fileIndex ファイル位置
	 * @return files[fileIndex].type プロパティを取得します
	 */
	private native String getInputElementFileContentType(InputElement element, int fileIndex) /*-{
		return element.files[fileIndex].type;
	}-*/;

	/**
	 * InputElement の files[0].size プロパティを取得します
	 * @param element InputElement
	 * @param fileIndex ファイル位置
	 * @return files[fileIndex].size プロパティを取得します
	 */
	private native int getInputElementFileSize(InputElement element, int fileIndex) /*-{
		return element.files[fileIndex].size;
	}-*/;
}
