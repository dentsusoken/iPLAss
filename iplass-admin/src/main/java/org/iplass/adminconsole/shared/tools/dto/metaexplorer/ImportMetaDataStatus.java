/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.dto.metaexplorer;

import java.io.Serializable;

public class ImportMetaDataStatus implements Serializable {

	private static final long serialVersionUID = 472318565900444064L;

	private String id;

	private String path;

	private String importActionName;

	private boolean error;
	private boolean warn;
	private boolean info;

	private String message;
	private String messageDetail;

	/**
	 * コンストラクタ
	 */
	public ImportMetaDataStatus() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getImportActionName() {
		return importActionName;
	}

	public void setImportActionName(String importActionName) {
		this.importActionName = importActionName;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return warn
	 */
	public boolean isWarn() {
		return warn;
	}

	/**
	 * @param warn セットする warn
	 */
	public void setWarn(boolean warn) {
		this.warn = warn;
	}

	public boolean isInfo() {
		return info;
	}

	public void setInfo(boolean info) {
		this.info = info;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageDetail() {
		return messageDetail;
	}

	public void setMessageDetail(String messageDetail) {
		this.messageDetail = messageDetail;
	}

}
