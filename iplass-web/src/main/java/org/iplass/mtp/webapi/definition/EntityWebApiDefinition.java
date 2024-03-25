/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.webapi.definition;

import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.definition.Definition;

/**
 * EntityのWebAPIから利用可能なEntityWebApi定義
 * @author Y.Fukuda
 */
@XmlRootElement
public class EntityWebApiDefinition implements Definition {

	private static final long serialVersionUID = 4363768874329012614L;

	/** 定義名 */
	private String name;

	/** Entity表示名（Grid表示用） */
	private String displayName;

	/** エンティティデータの作成可否 */
	private boolean isInsert;

	/** エンティティデータの読込可否（Load） */
	private boolean isLoad;

	/** エンティティデータの読込可否（Query） */
	private boolean isQuery;

	/** エンティティデータの更新可否 */
	private boolean isUpdate;

	/** エンティティデータの削除可否 */
	private boolean isDelete;

	public boolean isInsert() {
		return isInsert;
	}

	public void setInsert(boolean isInsert) {
		this.isInsert = isInsert;
	}

	public boolean isLoad() {
		return isLoad;
	}

	public void setLoad(boolean isLoad) {
		this.isLoad = isLoad;
	}

	public boolean isQuery() {
		return isQuery;
	}

	public void setQuery(boolean isQuery) {
		this.isQuery = isQuery;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void setDescription(String description) {
	}
}
