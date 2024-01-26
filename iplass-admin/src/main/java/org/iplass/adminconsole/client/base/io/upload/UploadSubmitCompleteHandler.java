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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

import gwtupload.client.Utils;

public abstract class UploadSubmitCompleteHandler implements SubmitCompleteHandler {

	private static final String TAG_MESSAGE = "message";
	private static final String TAG_MSG_START = "%%%INI%%%";
	private static final String LEGACY_TAG_MSG_START = "%%%START%%%";
	private static final String TAG_MSG_END = "%%%END%%%";
	private static final String TAG_MSG_GT = "^^^@@";
	private static final String TAG_MSG_LT = "@@^^^";
	private static final String TAG_ERROR = "error";

	private FormPanel pnlForm;

	public UploadSubmitCompleteHandler(FormPanel pnlForm) {
		this.pnlForm = pnlForm;
	}

	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {

		String serverResponse = event.getResults();
		if (serverResponse != null) {
			serverResponse = serverResponse.replaceFirst(".*(" + TAG_MSG_START + "|" + LEGACY_TAG_MSG_START + ")([\\s\\S]*?)" + TAG_MSG_END + ".*", "$2");
			serverResponse = serverResponse.replace(TAG_MSG_LT, "<").replace(TAG_MSG_GT, ">").replace("&lt;", "<").replaceAll("&gt;", ">");
		}

		GWT.log("onSubmitComplete : url=" + pnlForm.getAction() + ", response=" + serverResponse);

		Document doc = XMLParser.parse(serverResponse);
		String message = Utils.getXmlNodeValue(doc, TAG_MESSAGE);
		String error = Utils.getXmlNodeValue(doc, TAG_ERROR);

		Widget elm = pnlForm.getWidget();
		FlowPanel fp = (FlowPanel) elm;

		for (int cnt = 0; cnt < fp.getWidgetCount(); cnt++) {
			Widget w = fp.getWidget(cnt);

			if (w instanceof FileUpload) {
				FileUpload f = (FileUpload)w;
				String name = f.getName();

				if (name.startsWith(UploadProperty.LOCALE_PREFIX)) {
					f.removeFromParent();
				}
			}
		}

		if (error == null) {
			// JSON->Result
			UploadResultInfo result = new UploadResultInfo(message);
			onSuccess(result);
		} else {
			onFailure(error);
		}

	}

	protected abstract void onSuccess(UploadResultInfo result);

	protected abstract void onFailure(String message);

}
