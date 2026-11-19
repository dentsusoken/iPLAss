/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.base.io.download;

import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;

/**
 * XSRFトークン付きPOSTで取得したバイナリをプレビュー表示するCanvas。
 */
public class XsrfProtectedImagePreview extends Canvas {

	/** 表示中のblob URL */
	private String objectUrl;

	/** 取得要求の世代（古い応答を破棄するために使用） */
	private int generation;

	public XsrfProtectedImagePreview() {
		setPadding(5);
		setWidth100();
		setHeight100();
		setOverflow(Overflow.AUTO);
		setBorder("1px solid gray");
	}

	/**
	 * プレビュー画像を取得して表示する。
	 *
	 * @param url ダウンロードサーブレットのURL
	 * @param params リクエストパラメータ
	 */
	public void load(String url, Map<String, String> params) {
		clearPreview();

		final int current = ++generation;
		XsrfProtectedImageLoader.load(url, params, new XsrfProtectedImageLoader.Callback() {

			@Override
			public void onLoad(String loadedUrl) {
				if (current != generation) {
					// より新しい要求が発生しているため破棄する
					XsrfProtectedImageLoader.release(loadedUrl);
					return;
				}
				objectUrl = loadedUrl;
				setContents("<img src=\"" + loadedUrl + "\" style=\"max-width:100%;max-height:100%;\"/>");
				setVisible(true);
			}

			@Override
			public void onError(int status) {
				if (current != generation) {
					return;
				}
				setContents("<span style=\"color:#cc0000\">"
						+ AdminClientMessageUtil.getString(
								"base_io_download_XsrfProtectedImagePreview_message_failedToLoad")
						+ (status > 0 ? " (HTTP " + status + ")" : "") + "</span>");
				setVisible(true);
				GWT.log("failed to load preview image. status=" + status);
			}
		});
	}

	/**
	 * 表示中のプレビュー画像を破棄する。
	 */
	public void clearPreview() {
		generation++;
		releaseObjectUrl();
		setContents("");
		setVisible(false);
	}

	@Override
	protected void onDestroy() {
		releaseObjectUrl();
		super.onDestroy();
	}

	private void releaseObjectUrl() {
		if (objectUrl != null) {
			XsrfProtectedImageLoader.release(objectUrl);
			objectUrl = null;
		}
	}
}
