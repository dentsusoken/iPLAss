/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.fileport;

import java.io.Serializable;

import org.iplass.mtp.async.TaskStatus;

/**
 * <p>CSV Uploadの実行結果</p>
 *
 * <p>非同期実行時は、AsyncTaskの実行結果として永続化される。</p>
 *
 */
public class CsvUploadStatus implements Serializable {

	private static final long serialVersionUID = 2811591873567400850L;

	private String fileName;
	private long uploadDateTime;

	private TaskStatus status;

	private String message;
	private String code;

	private int insertCount;
	private int updateCount;
	private int deleteCount;

	private String defName;
	private String parameter;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getUploadDateTime() {
		return uploadDateTime;
	}

	public void setUploadDateTime(long uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getInsertCount() {
		return insertCount;
	}

	public void setInsertCount(int insertCount) {
		this.insertCount = insertCount;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	public int getDeleteCount() {
		return deleteCount;
	}

	public void setDeleteCount(int deleteCount) {
		this.deleteCount = deleteCount;
	}

	public String getDefName() {
		return defName;
	}

	public void setDefName(String defName) {
		this.defName = defName;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}


}
