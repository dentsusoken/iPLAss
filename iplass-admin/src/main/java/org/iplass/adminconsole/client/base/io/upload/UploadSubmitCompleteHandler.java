/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * AdminUploadAction のサブクラスのサーブレットに対して submit した際の完了時ハンドラ
 *
 * <p>
 * 本ハンドラを利用する際は、submit 対象フォームは XsrfProtectedMultipartForm を利用する必要がある。
 * </p>
 *
 *
 * @see org.iplass.adminconsole.client.base.io.upload.XsrfProtectedMultipartForm
 * @author SEKIGUCHI Naoya
 */
public abstract class UploadSubmitCompleteHandler implements SubmitCompleteHandler {
	private XsrfProtectedMultipartForm pnlForm;

	public UploadSubmitCompleteHandler(XsrfProtectedMultipartForm pnlForm) {
		this.pnlForm = pnlForm;
	}

	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {
		// 実行結果を解析
		AdminUploadActionResponse response = AdminUploadActionResponseParser.parse(event.getResults());

		GWT.log("onSubmitComplete : service=" + pnlForm.getService() + ", response=" + response.getData());

		for (Widget w : pnlForm.getWidgetList()) {

			if (w instanceof FileUpload) {
				FileUpload f = (FileUpload)w;
				String name = f.getName();

				if (name.startsWith(UploadProperty.LOCALE_PREFIX)) {
					f.removeFromParent();
				}
			}
		}

		if (response.isSuccess()) {
			// JSON->Result
			UploadResultInfo result = new UploadResultInfo(response.getData());
			onSuccess(result);
		} else {
			onFailure(response.getErrorMessage());
		}

	}

	protected abstract void onSuccess(UploadResultInfo result);

	protected abstract void onFailure(String message);

}
