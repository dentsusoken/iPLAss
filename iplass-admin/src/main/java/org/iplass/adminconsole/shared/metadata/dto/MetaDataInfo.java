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

package org.iplass.adminconsole.shared.metadata.dto;

import java.io.Serializable;

/**
 * メタデータ情報。
 * MetaDataEntryレベルの情報を保持。
 *
 */
public class MetaDataInfo implements Serializable {

	private static final long serialVersionUID = -1069551179106879217L;

	private String path;
	private String name;
	private String id;

	private boolean shared;
	private boolean sharedOverwrite;

	private boolean overwritable;
	private boolean sharable;
	private boolean dataSharable;
	private boolean permissionSharable;

	private String definitionClassName;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public boolean isSharedOverwrite() {
		return sharedOverwrite;
	}

	public void setSharedOverwrite(boolean sharedOverwrite) {
		this.sharedOverwrite = sharedOverwrite;
	}

	public boolean isOverwritable() {
		return overwritable;
	}

	public void setOverwritable(boolean overwritable) {
		this.overwritable = overwritable;
	}

	public boolean isSharable() {
		return sharable;
	}

	public void setSharable(boolean sharable) {
		this.sharable = sharable;
	}

	public boolean isDataSharable() {
		return dataSharable;
	}

	public void setDataSharable(boolean dataSharable) {
		this.dataSharable = dataSharable;
	}

	public boolean isPermissionSharable() {
		return permissionSharable;
	}

	public void setPermissionSharable(boolean permissionSharable) {
		this.permissionSharable = permissionSharable;
	}

	public String getDefinitionClassName() {
		return definitionClassName;
	}

	public void setDefinitionClassName(String definitionClassName) {
		this.definitionClassName = definitionClassName;
	}

}
