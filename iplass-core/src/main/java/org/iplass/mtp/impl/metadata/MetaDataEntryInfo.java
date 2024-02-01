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

package org.iplass.mtp.impl.metadata;

import java.sql.Timestamp;

import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;


/**
 * MetaData本体除く、メタデータ情報。
 *
 * @author K.Higuchi
 *
 */
public class MetaDataEntryInfo {

	private String path;

	private String id;
	private State state;
	private int version;
	private String repository;
	private String displayName;
	private String description;

	private Timestamp createDate;
	private Timestamp updateDate;
	private String createUser;
	private String updateUser;

	private boolean overwritable = true;
	private boolean sharable = false;
	private boolean dataSharable = false;
	private boolean permissionSharable = false;

	private RepositoryType repositryType;

	public MetaDataEntryInfo() {
	}

	public MetaDataEntryInfo copy() {
		MetaDataEntryInfo copy = new MetaDataEntryInfo();

		copy.path = path;
		copy.id = id;
		copy.state = state;
		copy.version = version;
		copy.repository = repository;
		copy.displayName = displayName;
		copy.description = description;

		copy.createDate = createDate;
		copy.updateDate = updateDate;
		copy.createUser = createUser;
		copy.updateUser = updateUser;

		copy.overwritable = overwritable;
		copy.sharable = sharable;
		copy.dataSharable = dataSharable;
		copy.permissionSharable = permissionSharable;

		copy.repositryType = repositryType;

		return copy;
	}

	public RepositoryType getRepositryType() {
		return repositryType;
	}

	public void setRepositryType(RepositoryType repositryType) {
		this.repositryType = repositryType;
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

	/**
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path セットする path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            セットする id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            セットする displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            セットする description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return createDate
	 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            セットする createDate
	 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return updateDate
	 */
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            セットする updateDate
	 */
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            セットする version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            セットする state
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 *            セットする repository
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	public String toString() {
		return "MetaDataEntryInfo [path=" + path + ", id=" + id + "]";
	}

}
