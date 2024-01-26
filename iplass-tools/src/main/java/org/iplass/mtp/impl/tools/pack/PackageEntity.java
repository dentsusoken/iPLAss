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

package org.iplass.mtp.impl.tools.pack;


/**
 * Package Entity定義
 */
//public class PackageEntry extends GenericEntity {
public interface PackageEntity {

//	private static final long serialVersionUID = 4919260971194867549L;

	public static final String ENTITY_DEFINITION_NAME = "mtp.maintenance.Package";

	/** 種類(Select) */
	public static final String TYPE = "type";
	/** ステータス(Select) */
	public static final String STATUS = "status";

	/** タスク数(Integer) */
	public static final String TASK_COUNT = "taskCount";
	/** 完了タスク数(Integer) */
	public static final String COMPLETE_TASK_COUNT = "completeTaskCount";
//	/** 進捗状況 */
//	public static final String PROGRESS = "progress";

	/** 処理開始日時 */
	public static final String EXEC_START_DATE = "execStartDate";
	/** 処理終了日時 */
	public static final String EXEC_END_DATE = "execEndDate";

	/** 作成情報(Binary) */
	public static final String CREATE_SETTING = "createSetting";
//	/** 詳細情報(Binary) */
//	public static final String DETAIL = "detail";

	/** アーカイブファイル(Binary) */
	public static final String ARCHIVE = "archive";

	/** 種類（ローカル） */
	public static final String TYPE_LOCAL = "10";
	/** 種類（手動Upload） */
	public static final String TYPE_UPLOAD = "20";
	/** 種類（オフライン） */
	public static final String TYPE_OFFLINE = "30";

	/** ステータス（Ready） */
	public static final String STATUS_READY = "00";
	/** ステータス（Active） */
	public static final String STATUS_ACTIVE = "10";
	/** ステータス（Completed） */
	public static final String STATUS_COMPLETED = "20";
	/** ステータス（Error） */
	public static final String STATUS_ERROR = "90";

//	public PackageEntry() {
//	}
//
//	public SelectValue getType() {
//		return getValue(TYPE);
//	}
//	public void setType(SelectValue type) {
//		setValue(TYPE, type);
//	}
//
//	public SelectValue getStatus() {
//		return getValue(STATUS);
//	}
//	public void setStatus(SelectValue status) {
//		setValue(STATUS, status);
//	}
//
//	public String getProgress() {
//		return getValue(PROGRESS);
//	}
//	public void setProgress(String progress) {
//		setValue(PROGRESS, progress);
//	}
//
//	public Timestamp getExecStartDate() {
//		return (Timestamp) getValue(EXEC_START_DATE);
//	}
//	public void setExecStartDate(Timestamp execStartDate) {
//		setValue(EXEC_START_DATE, execStartDate);
//	}
//
//	public Timestamp getExecEndDate() {
//		return (Timestamp) getValue(EXEC_END_DATE);
//	}
//	public void setExecEndDate(Timestamp execEndDate) {
//		setValue(EXEC_END_DATE, execEndDate);
//	}
//
//	public BinaryReference getDetail() {
//		return getValue(DETAIL);
//	}
//
//	public void setDetail(BinaryReference detail) {
//		setValue(DETAIL, detail);
//	}
//
//	public BinaryReference getArchive() {
//		return getValue(ARCHIVE);
//	}
//
//	public void setArchive(BinaryReference archive) {
//		setValue(ARCHIVE, archive);
//	}

}
