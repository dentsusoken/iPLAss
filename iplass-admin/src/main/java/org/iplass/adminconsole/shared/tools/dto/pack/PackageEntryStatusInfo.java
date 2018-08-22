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

package org.iplass.adminconsole.shared.tools.dto.pack;

import java.io.Serializable;
import java.sql.Timestamp;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.SelectValue;

/**
 * PackageEntry EntityのGWT側定義
 */
public class PackageEntryStatusInfo implements Serializable {

	private static final long serialVersionUID = 22473663866337673L;

	/** OID */
	private String oid;

	/** Name */
	private String name;

	/** Description */
	private String description;

	/** 種類(Select) */
	private SelectValue type;

	/** ステータス(Select) */
	private SelectValue status;

	/** 進捗状況 */
	private String progress;

	/** 処理開始日時 */
	private Timestamp execStartDate;

	/** 処理終了日時 */
	private Timestamp execEndDate;

	/** 作成情報(Binary) */
	private BinaryReference createSetting;

//	/** 詳細情報(Binary) */
//	private BinaryReference detail;

	/** アーカイブファイル(Binary) */
	private BinaryReference archive;

	/**
	 * @return oid
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * @param oid セットする oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description セットする description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return type
	 */
	public SelectValue getType() {
		return type;
	}

	/**
	 * @param type セットする type
	 */
	public void setType(SelectValue type) {
		this.type = type;
	}

	/**
	 * @return status
	 */
	public SelectValue getStatus() {
		return status;
	}

	/**
	 * @param status セットする status
	 */
	public void setStatus(SelectValue status) {
		this.status = status;
	}

	/**
	 * @return progress
	 */
	public String getProgress() {
		return progress;
	}

	/**
	 * @param progress セットする progress
	 */
	public void setProgress(String progress) {
		this.progress = progress;
	}

	/**
	 * @return execStartDate
	 */
	public Timestamp getExecStartDate() {
		return execStartDate;
	}

	/**
	 * @param execStartDate セットする execStartDate
	 */
	public void setExecStartDate(Timestamp execStartDate) {
		this.execStartDate = execStartDate;
	}

	/**
	 * @return execEndDate
	 */
	public Timestamp getExecEndDate() {
		return execEndDate;
	}

	/**
	 * @param execEndDate セットする execEndDate
	 */
	public void setExecEndDate(Timestamp execEndDate) {
		this.execEndDate = execEndDate;
	}

	/**
	 * @return createSetting
	 */
	public BinaryReference getCreateSetting() {
		return createSetting;
	}

	/**
	 * @param detail セットする detail
	 */
	public void setCreateSetting(BinaryReference createSetting) {
		this.createSetting = createSetting;
	}

//	/**
//	 * @return detail
//	 */
//	public BinaryReference getDetail() {
//		return detail;
//	}
//
//	/**
//	 * @param detail セットする detail
//	 */
//	public void setDetail(BinaryReference detail) {
//		this.detail = detail;
//	}

	/**
	 * @return archive
	 */
	public BinaryReference getArchive() {
		return archive;
	}

	/**
	 * @param archive セットする archive
	 */
	public void setArchive(BinaryReference archive) {
		this.archive = archive;
	}

}
