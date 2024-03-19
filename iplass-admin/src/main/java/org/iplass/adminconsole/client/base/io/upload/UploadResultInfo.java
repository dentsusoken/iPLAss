/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.List;

import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class UploadResultInfo {

	private JSONValue rootValue;

	private String status;
	private List<String> statusMessage;
	private List<String> messages;

	public UploadResultInfo(JSONValue json) {
		rootValue = json;
		if (rootValue == null) {
			return;
		}

		//ステータスの取得
		status = getFileUploadStatus();

		//ステータスメッセージの取得
		statusMessage = getFileUploadStatusMessage();

		//メッセージの取得
		messages = getFileUploadMessage();

	}

	public JSONValue getRootValue() {
		return rootValue;
	}

	public boolean isFileUploadStatusSuccess() {
		return UploadProperty.Status.SUCCESS.name().equals(status);
	}

	public String getStatus() {
		return status;
	}

	public List<String> getStatusMessages() {
		return statusMessage;
	}

	public List<String> getMessages() {
		return messages;
	}

	protected String getValue(String key) {
		if (getRootValue() == null) {
			return null;
		}
		JSONObject jsonObject = getRootValue().isObject();
		JSONValue value = jsonObject.get(key);
		if (value != null) {
			return snipQuote(value.toString());
		} else {
			return "";
		}
	}

	protected List<String> getValues(String key) {
		if (getRootValue() == null) {
			return null;
		}
		List<String> values = new ArrayList<String>();
		JSONObject jsonObject = getRootValue().isObject();
		JSONValue value = jsonObject.get(key);
		if (value != null) {
			JSONArray array = value.isArray();
			for (int i = 0; i < array.size(); i++) {
				JSONValue child = array.get(i);
				values.add(snipQuote(child.toString()));
			}
		}
		return values;
	}


	private String getFileUploadStatus() {
		return getValue(UploadProperty.STATUS_CODE);
	}

	private List<String> getFileUploadStatusMessage() {
		return getValues(UploadProperty.STATUS_MESSAGE);
	}

	private List<String> getFileUploadMessage() {
		return getValues(UploadProperty.MESSAGE);
	}

	private String snipQuote(String value) {
		if (value.startsWith("\"") && value.endsWith("\"")) {
			return value.substring(1, value.length() - 1);
		} else {
			return value;
		}
	}
}
