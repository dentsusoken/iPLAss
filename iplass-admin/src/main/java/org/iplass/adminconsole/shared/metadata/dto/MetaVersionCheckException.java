/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.ApplicationException;

/**
 * MetaData更新時のバージョンチェックエラー
 */
public class MetaVersionCheckException extends ApplicationException {

	private static final long serialVersionUID = 6945611441363248245L;

	/** 更新対象Version */
	private int targetVersion = -1;
	/** 最新Version */
	private int latestVersion = -1;
	/** 最新Shared */
	private boolean latestShared;
	/** 最新SharedOverwrite */
	private boolean latestSharedOverwrite;

	public MetaVersionCheckException() {
	}

	public MetaVersionCheckException(String message) {
		super(message);
	}

	public MetaVersionCheckException(Throwable cause) {
		super(cause);
	}

	public MetaVersionCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public int getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(int targetVersion) {
		this.targetVersion = targetVersion;
	}

	public int getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(int latestVersion) {
		this.latestVersion = latestVersion;
	}

	public boolean isLatestShared() {
		return latestShared;
	}

	public void setLatestShared(boolean latestShared) {
		this.latestShared = latestShared;
	}

	public boolean isLatestSharedOverwrite() {
		return latestSharedOverwrite;
	}

	public void setLatestSharedOverwrite(boolean latestSharedOverwrite) {
		this.latestSharedOverwrite = latestSharedOverwrite;
	}

}
