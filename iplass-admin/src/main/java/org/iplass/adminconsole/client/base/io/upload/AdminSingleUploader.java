/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.Utils;

/**
 * gwt-uploadのFirefox不具合対応
 */
public class AdminSingleUploader extends SingleUploader {

	public AdminSingleUploader(String service) {
		//ボタンを利用しない
		super(new AdminUploadStatus(), null);

		IUploader.UploaderConstants constants = GWT.create(CustomUploaderConstants.class);	//Messageのカスタマイズ
		setI18Constants(constants);
		setServletPath(GWT.getModuleBaseURL() + service);					//Servlet
		setAutoSubmit(false);														//自動送信しない
		getFileInput().asWidget().setHeight("20px");
		getFileInput().asWidget().setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH + "px");
	}

	@Override
	public void update() {
		//Firefoxの場合にUNINITIALIZEDになったあとにもSubmitCompleteEventが発生して、
		//serverRawResponseの解析に失敗した後にこのメソッドが呼ばれてget_statusリクエストを実行してしまうため、
		//get_statusリクエストを実行しないように対応
		if (getStatus() == Status.UNINITIALIZED) {
			GWT.log("update() called on UNINITIALIZED status. skip update.");
			return;
		}
		GWT.log("update() called on " + getStatus() + " status.");

		super.update();
	}

	/**
	 * 送信完了後にHiddenパラメータをクリアします。
	 */
	public void removeHidden() {

		//Hidden項目の削除（uploader#reset、clearなどではうまくいかないため下の方法でクリア）
		Widget form = getForm().getWidget();
		if (form instanceof FlowPanel) {
			FlowPanel formPanel = (FlowPanel)form;
			for (int i = formPanel.getWidgetCount() - 1; i >= 0; i--) {
				Widget child = formPanel.getWidget(i);
				if (child instanceof Hidden) {
					formPanel.remove(child);
				}
			}
		}
	}

	/**
	 * サーバからの応答メッセージを返します。
	 *
	 * @return サーバからの応答メッセージ
	 */
	public String getMessage() {
		return getServerMessage().getMessage();
	}

	/**
	 * サーバからのエラーメッセージを返します。
	 *
	 * @return サーバからのエラーメッセージ
	 */
	public String getErrorMessage() {
		Document doc = XMLParser.parse(getServerRawResponse());
		return Utils.getXmlNodeValue(doc, "error");
	}

	/**
	 * ログを出力します。
	 *
	 * @param eventName イベント名
	 */
	public void debugUploader(String eventName) {
		GWT.log(eventName + ": status                   ->" + getStatus());

		UploadedInfo info = getServerInfo();
		if (info == null) {
			GWT.log(eventName + ": UploadedInfo is Null.");
		} else {
			GWT.log(eventName + ": File name                ->" + info.name);
			GWT.log(eventName + ": File content-type        ->" + info.ctype);
			GWT.log(eventName + ": File size                ->" + info.size);
		}
		GWT.log(eventName + ": message                  ->" + getServerMessage().getMessage());
	}

}
