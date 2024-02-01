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

package org.iplass.mtp.definition;

import java.io.Serializable;
import java.util.List;


/**
 * Definitionのメタ情報（共有設定、バージョン番号など）を表すクラスです。
 *
 * @author K.Higuchi
 *
 */
public class DefinitionInfo implements Serializable {
	private static final long serialVersionUID = -5472453590322128804L;

	private String name;
	private String displayName;
	private String description;
	private SharedConfig sharedConfig;
	private boolean shared;
	private boolean sharedOverwrite;
	private int version;
	private String type;//class名(simpleName)
	private List<VersionHistory> versionHistory;
	private String definitionId;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SharedConfig getSharedConfig() {
		return sharedConfig;
	}
	public void setSharedConfig(SharedConfig sharedConfig) {
		this.sharedConfig = sharedConfig;
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<VersionHistory> getVersionHistory() {
		return versionHistory;
	}
	public void setVersionHistory(List<VersionHistory> versionHistory) {
		this.versionHistory = versionHistory;
	}
	public String getObjDefId() {
		return definitionId;
	}
	public void setObjDefId(String definitionId) {
		this.definitionId = definitionId;
	}
}
