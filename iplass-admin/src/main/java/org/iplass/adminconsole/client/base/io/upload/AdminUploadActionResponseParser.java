/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.adminconsole.shared.base.io.AdminUploadConstant;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

/**
 * AdminUploadAction レスポンス解析機能。
 *
 * <p>
 * AdminUploadAction 実行結果の文字列を解析し、AdminUploadActionResponse の形式に変換する。
 * form イベントに設定する SubmitCompleteHandler 等で本機能を利用し、実行結果を解析する。
 * <p>
 *
 * <p>
 * レスポンスデータのイメージは、{@link org.iplass.adminconsole.server.base.io.upload.AdminUploadAction} のコメントを参照すること。
 * </p>
 *
 * <p>
 * 利用イメージ。
 * <pre><code>
 * class HandlerImpl implements SubmitCompleteHandler {
 *   &#64;Override
 *   public void onSubmitComplete(SubmitCompleteEvent event) {
 *     AdminUploadActionResponse response = AdminUploadActionResponseParser.parse(event.getResults());
 *
 *     // レスポンスを利用した完了時処理を実装
 *   }
 * }
 * </code></pre>
 * </p>
 *
 * @see com.google.gwt.user.client.ui.FormPanel#addSubmitCompleteHandler(com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler)
 * @see org.iplass.adminconsole.shared.base.io.AdminUploadConstant
 * @see org.iplass.adminconsole.server.base.io.upload.AdminUploadAction
 * @see org.iplass.adminconsole.client.base.io.upload.AdminUploadActionResponse
 * @author SEKIGUCHI Naoya
 */

public class AdminUploadActionResponseParser {
	/**
	 * 実行結果
	 *
	 * @param eventResult イベントの実行結果
	 * @return レスポンスインスタンス
	 */
	public static AdminUploadActionResponse parse(String eventResult) {
		// JSON文字列を抽出する
		String jsonString = extractJsonString(eventResult);

		// JSONを解析
		JSONObject serverResponse = JSONParser.parseStrict(jsonString).isObject();
		boolean isSuccess = serverResponse.get(AdminUploadConstant.ResponseKey.IS_SUCCESS).isBoolean().booleanValue();
		JSONValue data = serverResponse.get(AdminUploadConstant.ResponseKey.DATA);
		// 異常終了時のみエラーメッセージを設定する
		String errorMessage = isSuccess ? null : data.isObject().get(AdminUploadConstant.ResponseKey.ERROR_MESSAGE).isString().stringValue();

		// response オブジェクトに変換
		AdminUploadActionResponse parsed = new AdminUploadActionResponse();
		parsed.setSuccess(isSuccess);
		parsed.setData(data);
		parsed.setErrorMessage(errorMessage);

		return parsed;
	}

	/**
	 * イベントの実行結果からJSON文字列を抽出する。
	 *
	 * @param eventResult イベントの実行結果
	 * @return JSON文字列
	 */
	private static String extractJsonString(String eventResult) {
		// content-type: application/json を指定しているが、以下のような形式になってしまう。そのため、テキストの json 部分を抽出
		// Firefox, Edge
		//   e.getResults() = <pre style="word-wrap: break-word; white-space: pre-wrap;">${JSON文字列}</pre>
		// Chrome
		//   e.getResults() = <pre>${JSON文字列}</pre><div></div>

		// ルートノードを response とする。
		String parseTarget = "<response>" + eventResult + "</response>";
		Document parsedDocument = XMLParser.parse(parseTarget);

		// JSON文字列を抽出する
		return parsedDocument
				// <response>
				.getFirstChild()
				// <pre>
				.getFirstChild()
				// #text
				.getFirstChild().getNodeValue();
	}

}
