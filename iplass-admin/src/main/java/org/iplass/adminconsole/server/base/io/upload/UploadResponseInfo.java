/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.io.upload;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;
import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty.Status;

public class UploadResponseInfo extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -8191419173505960013L;

	public UploadResponseInfo() {
		//初期化（順番も指定）
		setStatus(Status.INIT);
		addStatusMessage(null);
		addMessage(null);
	}

	public Status getStatus() {
		return (Status)get(UploadProperty.STATUS_CODE);
	}

	public void setStatus(Status status) {
		put(UploadProperty.STATUS_CODE, status);
	}

	public void setStatusError() {
		setStatus(Status.ERROR);
	}

	public void setStatusWarn() {
		if (Status.ERROR != getStatus()) {
			setStatus(Status.WARN);
		}
	}

	public void setStatusSuccess() {
		if (Status.ERROR != getStatus()
				&& Status.WARN != getStatus()) {
			setStatus(Status.SUCCESS);
		}
	}

	public void addStatusMessage(String statusMessage) {
		@SuppressWarnings("unchecked")
		List<String> infos = (List<String>)get(UploadProperty.STATUS_MESSAGE);
		if (infos == null) {
			infos = new ArrayList<String>();
			put(UploadProperty.STATUS_MESSAGE, infos);
		}
		if (statusMessage != null && !statusMessage.isEmpty()) {
			infos.add(statusMessage);
		}
	}

	public void addMessage(String message) {
		@SuppressWarnings("unchecked")
		List<String> messages = (List<String>)get(UploadProperty.MESSAGE);
		if (messages == null) {
			messages = new ArrayList<String>();
			put(UploadProperty.MESSAGE, messages);
		}
		if (message != null && !message.isEmpty()) {
			messages.add(message);
		}
	}
}
