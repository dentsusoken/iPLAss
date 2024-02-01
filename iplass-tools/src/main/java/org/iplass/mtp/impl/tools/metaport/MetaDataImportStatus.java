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

package org.iplass.mtp.impl.tools.metaport;

import java.io.Serializable;

import org.iplass.mtp.impl.metadata.RootMetaData;

public class MetaDataImportStatus implements Serializable {

	public enum ImportAction {
		INSERT("Insert"),
		UPDATE("Update"),
		RENAME("Rename"),
		INSERTWITHDELETE("InsertWithDelete"),
		INSERTWITHCOMBI("InsertWithCombi"),
		RENAMEWITHDELETE("RenameWithDelete"),
		RENAMEWITHCOMBI("RenameWithCombi"),
		ERROR("Error");

		private String displayName;

		private ImportAction(String displayName) {
			this.displayName = displayName;
		}

		public String displayName() {
			return displayName;
		}
	}

	private static final long serialVersionUID = -3711275327266081753L;

	/** インポート対象ID */
	private String importId;
	/** インポート対象Path */
	private String importPath;

	/** ERRORがあるか */
	private boolean error;
	/** WARNがあるか */
	private boolean warn;
	/** INFOがあるか */
	private boolean info;

	private ImportAction action;

	/** メッセージ */
	private String message;
	private String messageDetail;

	/** 削除対象となるメタデータID */
	private String removeMetaDataId = null;
	/** 削除対象となるメタデータPath */
	private String removeMetaDataPath = null;
	/** 削除対象となるメタデータ */
	private RootMetaData removeMetaData = null;

	/** 同時に更新が必要となるメタデータID */
	private String combiMetaDataId = null;
	/** 同時に更新が必要となるメタデータPath */
	private String combiMetaDataPath = null;
	/** 同時に更新が必要となるメタデータ */
	private RootMetaData combiMetaData = null;

	/**
	 * コンストラクタ
	 */
	public MetaDataImportStatus() {
	}

	public MetaDataImportStatus(String importId, String importPath) {
		this.importId = importId;
		this.importPath = importPath;
	}

	public String getImportId() {
		return importId;
	}

	public void setImportId(String importId) {
		this.importId = importId;
	}

	public String getImportPath() {
		return importPath;
	}

	public void setImportPath(String importPath) {
		this.importPath = importPath;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isWarn() {
		return warn;
	}

	public void setWarn(boolean warn) {
		this.warn = warn;
	}

	public boolean isInfo() {
		return info;
	}

	public void setInfo(boolean info) {
		this.info = info;
	}

	public ImportAction getAction() {
		return action;
	}

	public void setAction(ImportAction action) {
		this.action = action;
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

	public boolean isInsert() {
		return action == ImportAction.INSERT
				|| action == ImportAction.INSERTWITHCOMBI
				|| action == ImportAction.INSERTWITHDELETE;
	}

	public boolean isUpdate() {
		return action == ImportAction.UPDATE
				|| action == ImportAction.RENAME
				|| action == ImportAction.RENAMEWITHCOMBI
				|| action == ImportAction.RENAMEWITHDELETE;
	}

	public String getRemoveMetaDataId() {
		return removeMetaDataId;
	}

	public void setRemoveMetaDataId(String removeMetaDataId) {
		this.removeMetaDataId = removeMetaDataId;
	}

	public String getRemoveMetaDataPath() {
		return removeMetaDataPath;
	}

	public void setRemoveMetaDataPath(String removeMetaDataPath) {
		this.removeMetaDataPath = removeMetaDataPath;
	}

	public RootMetaData getRemoveMetaData() {
		return removeMetaData;
	}

	public void setRemoveMetaData(RootMetaData removeMetaData) {
		this.removeMetaData = removeMetaData;
	}

	public boolean hasRemoveMetaData() {
		return (getRemoveMetaDataId() != null);
	}

	public String getCombiMetaDataId() {
		return combiMetaDataId;
	}

	public void setCombiMetaDataId(String combiMetaDataId) {
		this.combiMetaDataId = combiMetaDataId;
	}

	public String getCombiMetaDataPath() {
		return combiMetaDataPath;
	}

	public void setCombiMetaDataPath(String combiMetaDataPath) {
		this.combiMetaDataPath = combiMetaDataPath;
	}

	public RootMetaData getCombiMetaData() {
		return combiMetaData;
	}

	public void setCombiMetaData(RootMetaData combiMetaData) {
		this.combiMetaData = combiMetaData;
	}

	public boolean hasCombiMetaData() {
		return (getCombiMetaDataId() != null);
	}

}
