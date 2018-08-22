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

package org.iplass.mtp.impl.metadata;

public class MetaDataEntry {
	
	public enum State {
		VALID, INVALID
	}
	
	public enum RepositoryType {
		SHARED, TENANT_LOCAL, SHARED_OVERWRITE
	}
	
	private String path;
	private RootMetaData metaData;
	private State state;
	private int version;
	private boolean overwritable;
	private boolean sharable;
	private boolean dataSharable;//現状、MetaEntityのみ利用
	private boolean permissionSharable;
	private RepositoryType repositryType;
	
	private MetaDataRuntime runtime;
	
	public MetaDataEntry() {
	}
	
	public MetaDataEntry(String path, RootMetaData metaData, State state, int version, boolean overwritable, boolean sharable, boolean dataSharable, boolean permissionSharable) {
		this.path = path;
		this.metaData = metaData;
		this.state = state;
		this.version = version;
		this.overwritable = overwritable;
		this.sharable = sharable;
		this.dataSharable = dataSharable;
		this.permissionSharable = permissionSharable;
	}
	
	/**
	 * shallow copy
	 * 
	 * @return
	 */
	public MetaDataEntry copy() {
		MetaDataEntry copy = new MetaDataEntry();
		copy.path = path;
		copy.metaData = metaData;
		copy.state = state;
		copy.version = version;
		copy.overwritable = overwritable;
		copy.sharable = sharable;
		copy.dataSharable = dataSharable;
		copy.permissionSharable = permissionSharable;
		copy.repositryType = repositryType;
		copy.runtime = runtime;
		return copy;
	}
	
	public void initRuntime() {
		runtime = metaData.createRuntime(new MetaDataConfig(sharable, overwritable, dataSharable, permissionSharable));
	}
	
	public MetaDataRuntime getRuntime() {
		return runtime;
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

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public RootMetaData getMetaData() {
		return metaData;
	}
	public void setMetaData(RootMetaData metaData) {
		this.metaData = metaData;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
}
